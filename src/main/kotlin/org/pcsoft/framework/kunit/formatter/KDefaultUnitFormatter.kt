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
 * - a term's exponent is appended as `^n` when it is not `1` (`"m^2"`, `"s^-1"`);
 * - if there is **exactly one** negative-exponent term **and at least one** positive term, fraction
 *   notation is used - the positive terms form the numerator (joined by `*`) and the single negative term
 *   the denominator, its exponent rendered as a positive magnitude (`"km/h"`, `"m/s^2"`);
 * - otherwise every term is joined by `*` with its signed exponent (`"m*s^-3*A^-2"`, `"s^-1"`);
 * - a dimensionless value (no terms) renders as just the number.
 */
object KDefaultUnitFormatter : KUnitFormatter {

    override fun format(context: KUnitFormatContext): String {
        val number = context.renderValue()
        val unitPart = renderUnits(context.units)
        return if (unitPart.isEmpty()) number else "$number $unitPart"
    }

    /** Renders a single term as `symbol` or `symbol^exponent` (the exponent kept with its sign). */
    private fun signedTerm(term: KUnitTerm): String =
        term.displaySymbol + if (term.exponent != 1) "^${term.exponent}" else ""

    private fun renderUnits(units: List<KUnitTerm>): String {
        if (units.isEmpty()) return ""
        val positives = units.filter { it.exponent > 0 }
        val negatives = units.filter { it.exponent < 0 }

        // Fraction notation only for the clean "a / b" shape: some numerator and exactly one denominator.
        if (positives.isNotEmpty() && negatives.size == 1) {
            val numerator = positives.joinToString("*") { signedTerm(it) }
            val denom = negatives.single()
            val magnitude = -denom.exponent
            val denominator = denom.displaySymbol + if (magnitude != 1) "^$magnitude" else ""
            return "$numerator/$denominator"
        }

        return units.joinToString("*") { signedTerm(it) }
    }
}
