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
 * A [KUnitFormatter] that renders a value as **Presentation MathML**, rendered natively by browsers and by
 * MathJax. With the default configuration `3 of meters / seconds` read into `km/h` becomes an inline
 * `<math>` with an `<mfrac>` of `<mi>km</mi>` over `<mi>h</mi>`.
 *
 * The layout follows the shared rules: with [KMathMlFractionStyle.MFRAC] a clean single-denominator shape
 * is stacked in an `<mfrac>`; every other shape - and the whole of [KMathMlFractionStyle.EXPONENT] - is a
 * flat product joined by the configured multiplication `<mo>` with signed `<msup>` exponents. A
 * dimensionless value renders as just the `<mn>`.
 *
 * The instance is immutable and therefore thread-safe. Construct it without arguments for
 * [KMathMlFormatConfig.DEFAULT] or pass a [KMathMlFormatConfig].
 *
 * @property config the rendering options; defaults to [KMathMlFormatConfig.DEFAULT].
 */
class KMathMlUnitFormatter(
    val config: KMathMlFormatConfig = KMathMlFormatConfig.DEFAULT,
) : KUnitFormatter {

    override fun format(context: KUnitFormatContext): String {
        val number = "<mn>${context.renderValue()}</mn>"
        val unitPart = renderUnits(context.units)
        val body = if (unitPart.isEmpty()) number else number + operator() + unitPart
        return when (config.wrapper) {
            KMathMlWrapper.MATH_INLINE -> "<math display=\"inline\">$body</math>"
            KMathMlWrapper.MATH_BLOCK -> "<math display=\"block\">$body</math>"
            KMathMlWrapper.FRAGMENT -> body
        }
    }

    /** The multiplication `<mo>` separator. */
    private fun operator(): String = "<mo>${config.multiplication.moContent}</mo>"

    /** Wraps a bare unit symbol in the configured element. */
    private fun tag(symbol: String): String = when (config.unitTag) {
        KMathMlUnitTag.MI -> "<mi>$symbol</mi>"
        KMathMlUnitTag.MTEXT -> "<mtext>$symbol</mtext>"
    }

    /** Renders a symbol with an exponent as `<msup>`, or the bare symbol when the exponent is `1`. */
    private fun term(symbol: String, exponent: Int): String =
        if (exponent == 1) tag(symbol) else "<msup>${tag(symbol)}<mn>$exponent</mn></msup>"

    private fun renderUnits(units: List<KUnitTerm>): String {
        if (units.isEmpty()) return ""
        val positives = units.filter { it.exponent > 0 }
        val negatives = units.filter { it.exponent < 0 }

        if (config.fractionStyle == KMathMlFractionStyle.MFRAC &&
            positives.isNotEmpty() && negatives.size == 1
        ) {
            val numerator = positives.joinToString(operator()) { term(it.displaySymbol, it.exponent) }
            val denom = negatives.single()
            val denominator = term(denom.displaySymbol, -denom.exponent)
            return "<mfrac><mrow>$numerator</mrow><mrow>$denominator</mrow></mfrac>"
        }

        return units.joinToString(operator()) { term(it.displaySymbol, it.exponent) }
    }
}
