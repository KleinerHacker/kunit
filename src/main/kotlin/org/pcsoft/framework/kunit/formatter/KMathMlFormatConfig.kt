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

/**
 * How the [KMathMlUnitFormatter] arranges numerator and denominator.
 */
enum class KMathMlFractionStyle {
    /** `<mfrac>…</mfrac>`. */
    MFRAC,

    /** Flat product with signed `<msup>` exponents. */
    EXPONENT,
}

/**
 * The MathML element the [KMathMlUnitFormatter] uses for a unit symbol.
 */
enum class KMathMlUnitTag {
    /** `<mi>…</mi>`. */
    MI,

    /** `<mtext>…</mtext>`. */
    MTEXT,
}

/**
 * The multiplication operator the [KMathMlUnitFormatter] emits inside an `<mo>` between product terms and
 * between the value and its units.
 *
 * @property moContent the character placed inside the `<mo>` element.
 */
enum class KMathMlMultiplication(val moContent: String) {
    /** Middle dot `·` (U+00B7). */
    MIDDLE_DOT(0x00B7.toChar().toString()),

    /** Multiplication cross `×` (U+00D7). */
    TIMES(0x00D7.toChar().toString()),

    /** The invisible-times operator (U+2062) - the default. */
    INVISIBLE_TIMES(0x2062.toChar().toString());
}

/**
 * How the [KMathMlUnitFormatter] wraps the produced markup.
 */
enum class KMathMlWrapper {
    /** `<math display="inline">…</math>`. */
    MATH_INLINE,

    /** `<math display="block">…</math>`. */
    MATH_BLOCK,

    /** No root element. */
    FRAGMENT,
}

/**
 * The rendering options of the [KMathMlUnitFormatter]. It is a plain value type; applications can pick a
 * preset ([DEFAULT], [INLINE], [FRAGMENT]) or build their own.
 *
 * @property fractionStyle stacked `<mfrac>` vs. inline `<msup>` exponents.
 * @property unitTag the element used for a unit symbol.
 * @property multiplication the operator between product terms.
 * @property wrapper the surrounding root element (or none).
 */
data class KMathMlFormatConfig(
    val fractionStyle: KMathMlFractionStyle = KMathMlFractionStyle.MFRAC,
    val unitTag: KMathMlUnitTag = KMathMlUnitTag.MI,
    val multiplication: KMathMlMultiplication = KMathMlMultiplication.INVISIBLE_TIMES,
    val wrapper: KMathMlWrapper = KMathMlWrapper.MATH_INLINE,
) {
    companion object {
        /** Stacked `<mfrac>`, `<mi>` symbols, invisible times, inline `<math>` root - directly renderable. */
        val DEFAULT: KMathMlFormatConfig = KMathMlFormatConfig()

        /** Inline `<msup>` exponents instead of a stacked fraction, otherwise like [DEFAULT]. */
        val INLINE: KMathMlFormatConfig = KMathMlFormatConfig(fractionStyle = KMathMlFractionStyle.EXPONENT)

        /** Like [DEFAULT] but without the `<math>` root - a bare fragment for embedding. */
        val FRAGMENT: KMathMlFormatConfig = KMathMlFormatConfig(wrapper = KMathMlWrapper.FRAGMENT)
    }
}
