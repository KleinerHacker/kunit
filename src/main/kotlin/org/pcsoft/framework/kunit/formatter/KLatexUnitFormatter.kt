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
 * A [KUnitFormatter] that renders a value as **LaTeX** math, ready for MathJax, KaTeX or a LaTeX document.
 * With the default configuration `3 of meters / seconds` read into `km/h` becomes
 * `1.5\,\frac{\mathrm{km}}{\mathrm{h}}`.
 *
 * The layout follows the shared rules: with [KLatexFractionStyle.FRACTION] a clean single-denominator shape
 * (some numerator, exactly one denominator) is stacked as `\frac{…}{…}`; every other shape - and the whole
 * of [KLatexFractionStyle.INLINE] - is a flat product joined by the configured multiplication marker with
 * signed exponents (`\mathrm{m}\cdot\mathrm{s}^{-1}`). A dimensionless value renders as just the number.
 *
 * The instance is immutable and therefore thread-safe. Construct it without arguments for
 * [KLatexFormatConfig.DEFAULT] or pass a [KLatexFormatConfig].
 *
 * @property config the rendering options; defaults to [KLatexFormatConfig.DEFAULT].
 */
class KLatexUnitFormatter(
    val config: KLatexFormatConfig = KLatexFormatConfig.DEFAULT,
) : KUnitFormatter {

    override fun format(context: KUnitFormatContext): String {
        val number = context.renderValue()
        val unitPart = renderUnits(context.units)
        val body = if (unitPart.isEmpty()) number else number + config.spacing.latex + unitPart
        return when (config.delimiter) {
            KLatexDelimiter.DOLLAR -> "\$$body\$"
            KLatexDelimiter.PARENTHESES -> "\\($body\\)"
            KLatexDelimiter.NONE -> body
        }
    }

    /** Wraps a bare unit symbol upright per the configured [KLatexUnitWrapper]. */
    private fun unit(symbol: String): String = when (config.unitWrapper) {
        KLatexUnitWrapper.MATHRM -> "\\mathrm{$symbol}"
        KLatexUnitWrapper.TEXT -> "\\text{$symbol}"
        KLatexUnitWrapper.NONE -> symbol
    }

    /** Renders a single term as its wrapped symbol plus a `^{n}` exponent when it is not `1`. */
    private fun term(t: KUnitTerm): String =
        unit(t.displaySymbol) + if (t.exponent != 1) "^{${t.exponent}}" else ""

    private fun renderUnits(units: List<KUnitTerm>): String {
        if (units.isEmpty()) return ""
        val positives = units.filter { it.exponent > 0 }
        val negatives = units.filter { it.exponent < 0 }
        val times = config.multiplication.latex

        if (config.fractionStyle == KLatexFractionStyle.FRACTION &&
            positives.isNotEmpty() && negatives.size == 1
        ) {
            val numerator = positives.joinToString(times) { term(it) }
            val denom = negatives.single()
            val magnitude = -denom.exponent
            val denominator = unit(denom.displaySymbol) + if (magnitude != 1) "^{$magnitude}" else ""
            return "\\frac{$numerator}{$denominator}"
        }

        return units.joinToString(times) { term(it) }
    }
}
