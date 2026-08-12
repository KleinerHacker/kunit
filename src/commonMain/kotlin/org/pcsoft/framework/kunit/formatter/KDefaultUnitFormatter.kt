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
 * The library's built-in plain-text formatter and the default of every `formatter` parameter (so
 * `format`/`toString` behave identically whether or not a formatter is passed explicitly).
 *
 * It renders `"<value> <units>"`, where the value uses [renderValue] and the units use each term's
 * [displaySymbol]:
 * - a term's exponent is appended when it is not `1`, either as `^n` or as real superscript digits
 *   (`"m^2"`/`"m²"`), controlled by [KDefaultFormatConfig.exponentStyle];
 * - if there is **exactly one** negative-exponent term **and at least one** positive term, fraction
 *   notation is used - the positive terms form the numerator (joined by the configured multiplication sign)
 *   and the single negative term the denominator, its exponent rendered as a positive magnitude
 *   (`"km/h"`, `"m/s^2"`);
 * - otherwise every term is joined by the multiplication sign with its signed exponent
 *   (`"m*s^-3*A^-2"`, `"s^-1"`);
 * - a dimensionless value (no terms) renders as just the number.
 *
 * The instance is immutable and therefore thread-safe. Construct it without arguments for the historical
 * behaviour ([KDefaultFormatConfig.DEFAULT]) or pass a [KDefaultFormatConfig] (e.g.
 * [KDefaultFormatConfig.SUPERSCRIPT]) to change the exponent style, the arithmetic signs or the function
 * symbols.
 *
 * @property config the rendering options; defaults to [KDefaultFormatConfig.DEFAULT].
 */
class KDefaultUnitFormatter(
    val config: KDefaultFormatConfig = KDefaultFormatConfig.DEFAULT,
) : KUnitFormatter {

    override fun format(context: KUnitFormatContext): String {
        val number = context.renderValue()
        val unitPart = renderUnits(context.units)
        return if (unitPart.isEmpty()) number else "$number $unitPart"
    }

    /** Renders a non-`1` exponent either as `^n` or as real superscript digits, per the config. */
    private fun exponent(value: Int): String = when (config.exponentStyle) {
        KDefaultExponentStyle.CARET -> "^$value"
        KDefaultExponentStyle.SUPERSCRIPT -> renderSuperscriptExponent(value)
    }

    /** Renders a single term as `symbol` or `symbol`+exponent (the exponent kept with its sign). */
    private fun signedTerm(term: KUnitTerm): String =
        term.displaySymbol + if (term.exponent != 1) exponent(term.exponent) else ""

    private fun renderUnits(units: List<KUnitTerm>): String {
        if (units.isEmpty()) return ""
        val positives = units.filter { it.exponent > 0 }
        val negatives = units.filter { it.exponent < 0 }
        val star = config.multiplication.symbol

        // Fraction notation only for the clean "a / b" shape: some numerator and exactly one denominator.
        if (positives.isNotEmpty() && negatives.size == 1) {
            val numerator = positives.joinToString(star) { signedTerm(it) }
            val denom = negatives.single()
            val magnitude = -denom.exponent
            val denominator = denom.displaySymbol + if (magnitude != 1) exponent(magnitude) else ""
            return "$numerator${config.division.symbol}$denominator"
        }

        return units.joinToString(star) { signedTerm(it) }
    }
}
