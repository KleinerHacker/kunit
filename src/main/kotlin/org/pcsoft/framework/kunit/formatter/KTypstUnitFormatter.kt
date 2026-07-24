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
 * A [KUnitFormatter] that renders a value as **Typst** math. With the default configuration
 * `3 of meters / seconds` read into `km/h` becomes `$1.5 upright("km")/upright("h")$`.
 *
 * The layout follows the shared rules: with [KTypstFractionStyle.FRACTION] a clean single-denominator shape
 * uses the `a/b` fraction form; every other shape - and the whole of [KTypstFractionStyle.EXPONENT] - is a
 * flat product joined by the configured multiplication marker with signed exponents
 * (`upright("m") upright("s")^(-1)`). A dimensionless value renders as just the number.
 *
 * The instance is immutable and therefore thread-safe. Construct it without arguments for
 * [KTypstFormatConfig.DEFAULT] or pass a [KTypstFormatConfig].
 *
 * @property config the rendering options; defaults to [KTypstFormatConfig.DEFAULT].
 */
class KTypstUnitFormatter(
    val config: KTypstFormatConfig = KTypstFormatConfig.DEFAULT,
) : KUnitFormatter {

    override fun format(context: KUnitFormatContext): String {
        val number = context.renderValue()
        val unitPart = renderUnits(context.units)
        val body = if (unitPart.isEmpty()) number else "$number $unitPart"
        return when (config.delimiter) {
            KTypstDelimiter.MATH -> "\$$body\$"
            KTypstDelimiter.FRAGMENT -> body
        }
    }

    /** Renders an exponent: a bare `^n` for a single positive digit, otherwise the grouped `^(n)`. */
    private fun exponent(value: Int): String = if (value in 2..9) "^$value" else "^($value)"

    /** Renders a unit symbol upright per the configured [KTypstUnitStyle]. */
    private fun unit(symbol: String): String = when (config.unitStyle) {
        KTypstUnitStyle.UPRIGHT -> "upright(\"$symbol\")"
        KTypstUnitStyle.TEXT -> "\"$symbol\""
    }

    /** Renders a single term as its upright symbol plus an exponent when it is not `1`. */
    private fun term(t: KUnitTerm): String =
        unit(t.displaySymbol) + if (t.exponent != 1) exponent(t.exponent) else ""

    private fun renderUnits(units: List<KUnitTerm>): String {
        if (units.isEmpty()) return ""
        val positives = units.filter { it.exponent > 0 }
        val negatives = units.filter { it.exponent < 0 }
        val times = config.multiplication.separator

        if (config.fractionStyle == KTypstFractionStyle.FRACTION &&
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
