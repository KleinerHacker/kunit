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
 * How the [KLatexUnitFormatter] arranges numerator and denominator.
 */
enum class KLatexFractionStyle {
    /** `\frac{numerator}{denominator}`. */
    FRACTION,

    /** Flat product with signed exponents. */
    INLINE,
}

/**
 * How the [KLatexUnitFormatter] wraps a unit symbol so it is set upright rather than as math variables.
 */
enum class KLatexUnitWrapper {
    /** `\mathrm{…}`. */
    MATHRM,

    /** `\text{…}`. */
    TEXT,

    /** No wrapper. */
    NONE,
}

/**
 * The multiplication marker the [KLatexUnitFormatter] places between product terms.
 *
 * @property latex the emitted LaTeX.
 */
enum class KLatexMultiplication(val latex: String) {
    /** Centred dot `\cdot` - the default. */
    CDOT("\\cdot"),

    /** Cross `\times`. */
    TIMES("\\times"),

    /** A thin space `\,` (juxtaposition). */
    THIN_SPACE("\\,");
}

/**
 * The math delimiters the [KLatexUnitFormatter] wraps the whole expression in.
 */
enum class KLatexDelimiter {
    /** `$…$`. */
    DOLLAR,

    /** `\(…\)`. */
    PARENTHESES,

    /** No delimiters. */
    NONE,
}

/**
 * The spacing the [KLatexUnitFormatter] inserts between the numeric value and the unit part.
 *
 * @property latex the emitted LaTeX.
 */
enum class KLatexSpacing(val latex: String) {
    /** A thin space `\,` - the default. */
    THIN("\\,"),

    /** A normal space. */
    NORMAL(" ");
}

/**
 * The rendering options of the [KLatexUnitFormatter]. It is a plain value type; applications can pick a
 * preset ([DEFAULT], [INLINE], [PLAIN]) or build their own.
 *
 * @property fractionStyle stacked `\frac` vs. inline negative exponents.
 * @property unitWrapper how a unit symbol is set upright.
 * @property multiplication the marker between product terms.
 * @property delimiter the surrounding math delimiters.
 * @property spacing the spacing between value and units.
 */
data class KLatexFormatConfig(
    val fractionStyle: KLatexFractionStyle = KLatexFractionStyle.FRACTION,
    val unitWrapper: KLatexUnitWrapper = KLatexUnitWrapper.MATHRM,
    val multiplication: KLatexMultiplication = KLatexMultiplication.CDOT,
    val delimiter: KLatexDelimiter = KLatexDelimiter.NONE,
    val spacing: KLatexSpacing = KLatexSpacing.THIN,
) {
    companion object {
        /** Stacked fraction, `\mathrm{}` units, `\cdot`, no delimiters, thin space - a ready fragment. */
        val DEFAULT: KLatexFormatConfig = KLatexFormatConfig()

        /** Inline product with negative exponents (no stacked fraction), otherwise like [DEFAULT]. */
        val INLINE: KLatexFormatConfig = KLatexFormatConfig(fractionStyle = KLatexFractionStyle.INLINE)

        /** Unwrapped symbols, no delimiters, normal spacing - the plainest LaTeX output. */
        val PLAIN: KLatexFormatConfig = KLatexFormatConfig(
            unitWrapper = KLatexUnitWrapper.NONE,
            spacing = KLatexSpacing.NORMAL,
        )
    }
}
