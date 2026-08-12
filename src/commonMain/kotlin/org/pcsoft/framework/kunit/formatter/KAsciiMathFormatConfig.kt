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
 * How the [KAsciiMathUnitFormatter] arranges numerator and denominator.
 */
enum class KAsciiMathFractionStyle {
    /** `numerator/denominator`. */
    FRACTION,

    /** Flat product with signed exponents. */
    EXPONENT,
}

/**
 * How the [KAsciiMathUnitFormatter] renders a unit symbol so it is set as upright text.
 */
enum class KAsciiMathUnitQuoting {
    /** `"km"`. */
    QUOTED,

    /** `km`. */
    BARE,
}

/**
 * The multiplication marker the [KAsciiMathUnitFormatter] places between product terms.
 *
 * @property symbol the emitted AsciiMath.
 */
enum class KAsciiMathMultiplication(val symbol: String) {
    /** Asterisk `*`. */
    ASTERISK("*"),

    /** The AsciiMath cross `xx`. */
    TIMES("xx"),

    /** A single space (juxtaposition) - the default. */
    SPACE(" ");
}

/**
 * The rendering options of the [KAsciiMathUnitFormatter]. It is a plain value type; applications can pick a
 * preset ([DEFAULT], [PLAIN]) or build their own.
 *
 * @property fractionStyle `a/b` fraction vs. inline negative exponents.
 * @property quoting whether unit symbols are quoted.
 * @property multiplication the marker between product terms.
 */
data class KAsciiMathFormatConfig(
    val fractionStyle: KAsciiMathFractionStyle = KAsciiMathFractionStyle.FRACTION,
    val quoting: KAsciiMathUnitQuoting = KAsciiMathUnitQuoting.QUOTED,
    val multiplication: KAsciiMathMultiplication = KAsciiMathMultiplication.SPACE,
) {
    companion object {
        /** Fraction form, quoted upright units, space-separated products - e.g. `1.5 "km"/"h"`. */
        val DEFAULT: KAsciiMathFormatConfig = KAsciiMathFormatConfig()

        /** Bare (unquoted) symbols joined by `*` - the plainest AsciiMath output. */
        val PLAIN: KAsciiMathFormatConfig = KAsciiMathFormatConfig(
            quoting = KAsciiMathUnitQuoting.BARE,
            multiplication = KAsciiMathMultiplication.ASTERISK,
        )
    }
}
