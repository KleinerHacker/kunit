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
 * A [KUnitFormatter] that renders a value as **AsciiMath**, the concise input syntax of MathJax. With the
 * default configuration `3 of meters / seconds` read into `km/h` becomes `10.8 "km"/"h"`.
 *
 * The layout follows the shared rules: with [KAsciiMathFractionStyle.FRACTION] a clean single-denominator
 * shape uses the `a/b` fraction form; every other shape - and the whole of
 * [KAsciiMathFractionStyle.EXPONENT] - is a flat product joined by the configured multiplication marker
 * with signed exponents (`"m" "s"^(-1)`). A dimensionless value renders as just the number.
 *
 * The instance is immutable and therefore thread-safe. Construct it without arguments for
 * [KAsciiMathFormatConfig.DEFAULT] or pass a [KAsciiMathFormatConfig].
 *
 * @property config the rendering options; defaults to [KAsciiMathFormatConfig.DEFAULT].
 */
class KAsciiMathUnitFormatter(
    val config: KAsciiMathFormatConfig = KAsciiMathFormatConfig.DEFAULT,
) : KUnitFormatter {

    override fun format(context: KUnitFormatContext): String {
        val number = context.renderValue()
        val unitPart = renderUnits(context.units)
        return if (unitPart.isEmpty()) number else "$number $unitPart"
    }

    /** Renders an exponent: a bare `^n` for a single positive digit, otherwise the grouped `^(n)`. */
    private fun exponent(value: Int): String = if (value in 2..9) "^$value" else "^($value)"

    /** Quotes a unit symbol per the configured [KAsciiMathUnitQuoting]. */
    private fun unit(symbol: String): String = when (config.quoting) {
        KAsciiMathUnitQuoting.QUOTED -> "\"$symbol\""
        KAsciiMathUnitQuoting.BARE -> symbol
    }

    /** Renders a single term as its (quoted) symbol plus an exponent when it is not `1`. */
    private fun term(t: KUnitTerm): String =
        unit(t.displaySymbol) + if (t.exponent != 1) exponent(t.exponent) else ""

    private fun renderUnits(units: List<KUnitTerm>): String {
        if (units.isEmpty()) return ""
        val positives = units.filter { it.exponent > 0 }
        val negatives = units.filter { it.exponent < 0 }
        val times = config.multiplication.symbol

        if (config.fractionStyle == KAsciiMathFractionStyle.FRACTION &&
            positives.isNotEmpty() && negatives.size == 1
        ) {
            val numerator = positives.joinToString(times) { term(it) }
            val numeratorGroup = if (positives.size > 1) "($numerator)" else numerator
            val denom = negatives.single()
            val magnitude = -denom.exponent
            val denominator = unit(denom.displaySymbol) + if (magnitude != 1) exponent(magnitude) else ""
            val denominatorGroup = if (magnitude != 1) "($denominator)" else denominator
            return "$numeratorGroup/$denominatorGroup"
        }

        return units.joinToString(times) { term(it) }
    }
}
