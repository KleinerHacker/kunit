/*
 * Copyright (c) KleinerHacker alias Pfeiffer C Soft 2026.
 * This work is licensed under the Apache License, Version 2.0.
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, this software is distributed on an “AS IS” BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations.
 */

package org.pcsoft.framework.kunit.formatter

import org.pcsoft.framework.kunit.KUnitTerm

/**
 * A [KUnitFormatter] that renders a value for an ANSI-capable terminal: it produces the **same** notation
 * as [KDefaultUnitFormatter] (`"10.8 km/h"`, `"m^2"`, `"m*s^-3*A^-2"`), but wraps each visual role in the
 * ANSI SGR colours of a [KConsoleColorPalette] - the numeric magnitude, the unit symbols, the operators
 * (`*`, `/`) and the exponent markers (`^n`) each get their own colour.
 *
 * Construct it without arguments to use the default [KConsoleColorPalette.CLASSIC] palette, or pass a
 * predefined ([KConsoleColorPalette.VIVID], [KConsoleColorPalette.MONOCHROME]) or a custom palette:
 * ```kotlin
 * val v = 3 of meters / seconds
 * v.format(kilo.meters / hours, "%.1f", Locale.US, KConsoleUnitFormatter())                       // classic
 * v.format(kilo.meters / hours, "%.1f", Locale.US, KConsoleUnitFormatter(KConsoleColorPalette.VIVID))
 * ```
 *
 * The instance is immutable and therefore thread-safe. The unit layout rules are identical to
 * [KDefaultUnitFormatter]: the single-fraction form `a/b` is used only for exactly one numerator and
 * exactly one denominator term, everything else is a flat product with signed exponents, and a
 * dimensionless value renders as the coloured number alone.
 *
 * @property palette the colours applied to each visual role; defaults to [KConsoleColorPalette.CLASSIC].
 */
class KConsoleUnitFormatter(
    val palette: KConsoleColorPalette = KConsoleColorPalette.CLASSIC,
) : KUnitFormatter {

    override fun format(context: KUnitFormatContext): String {
        val number = wrap(palette.numberColor, context.renderValue())
        val unitPart = renderUnits(context.units)
        return if (unitPart.isEmpty()) number else "$number $unitPart"
    }

    /** Wraps [text] in [code] + [KConsoleColorPalette.reset]; an empty [code] leaves [text] uncoloured. */
    private fun wrap(code: String, text: String): String =
        if (code.isEmpty()) text else code + text + palette.reset

    /** Renders a single term as a coloured `symbol` or `symbol^exponent` (the exponent kept with its sign). */
    private fun signedTerm(term: KUnitTerm): String =
        wrap(palette.symbolColor, term.displaySymbol) +
            if (term.exponent != 1) wrap(palette.exponentColor, "^${term.exponent}") else ""

    private fun renderUnits(units: List<KUnitTerm>): String {
        if (units.isEmpty()) return ""
        val positives = units.filter { it.exponent > 0 }
        val negatives = units.filter { it.exponent < 0 }
        val star = wrap(palette.operatorColor, "*")

        // Fraction notation only for the clean "a / b" shape: some numerator and exactly one denominator.
        if (positives.isNotEmpty() && negatives.size == 1) {
            val numerator = positives.joinToString(star) { signedTerm(it) }
            val denom = negatives.single()
            val magnitude = -denom.exponent
            val denominator = wrap(palette.symbolColor, denom.displaySymbol) +
                if (magnitude != 1) wrap(palette.exponentColor, "^$magnitude") else ""
            return numerator + wrap(palette.operatorColor, "/") + denominator
        }

        return units.joinToString(star) { signedTerm(it) }
    }
}
