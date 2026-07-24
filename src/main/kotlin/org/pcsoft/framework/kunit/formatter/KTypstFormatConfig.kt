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
 * How the [KTypstUnitFormatter] arranges numerator and denominator.
 */
enum class KTypstFractionStyle {
    /** `numerator/denominator`. */
    FRACTION,

    /** Flat product with signed exponents. */
    EXPONENT,
}

/**
 * How the [KTypstUnitFormatter] renders a unit symbol so it is set upright.
 */
enum class KTypstUnitStyle {
    /** `upright("km")`. */
    UPRIGHT,

    /** `"km"`. */
    TEXT,
}

/**
 * The multiplication marker the [KTypstUnitFormatter] places between product terms.
 *
 * @property separator the emitted Typst (including surrounding spacing where needed).
 */
enum class KTypstMultiplication(val separator: String) {
    /** A single space (juxtaposition) - the default. */
    SPACE(" "),

    /** The `dot` operator. */
    DOT(" dot "),

    /** The `times` operator. */
    TIMES(" times ");
}

/**
 * The math delimiters the [KTypstUnitFormatter] wraps the whole expression in.
 */
enum class KTypstDelimiter {
    /** `$…$`. */
    MATH,

    /** No delimiters. */
    FRAGMENT,
}

/**
 * The rendering options of the [KTypstUnitFormatter]. It is a plain value type; applications can pick a
 * preset ([DEFAULT], [FRAGMENT]) or build their own.
 *
 * @property fractionStyle `a/b` fraction vs. inline negative exponents.
 * @property unitStyle how a unit symbol is set upright.
 * @property multiplication the marker between product terms.
 * @property delimiter the surrounding math delimiters.
 */
data class KTypstFormatConfig(
    val fractionStyle: KTypstFractionStyle = KTypstFractionStyle.FRACTION,
    val unitStyle: KTypstUnitStyle = KTypstUnitStyle.UPRIGHT,
    val multiplication: KTypstMultiplication = KTypstMultiplication.SPACE,
    val delimiter: KTypstDelimiter = KTypstDelimiter.MATH,
) {
    companion object {
        /** Fraction form, upright units, space-separated products, `$…$` - e.g. `$1.5 upright("km")/upright("h")$`. */
        val DEFAULT: KTypstFormatConfig = KTypstFormatConfig()

        /** Like [DEFAULT] but without the `$…$` delimiters - a bare fragment for embedding. */
        val FRAGMENT: KTypstFormatConfig = KTypstFormatConfig(delimiter = KTypstDelimiter.FRAGMENT)
    }
}
