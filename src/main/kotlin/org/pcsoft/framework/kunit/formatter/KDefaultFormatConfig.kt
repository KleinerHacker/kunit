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
 * How the [KDefaultUnitFormatter] renders a unit exponent.
 */
enum class KDefaultExponentStyle {
    /** Caret notation `^n`. */
    CARET,

    /** Real Unicode superscript digits. */
    SUPERSCRIPT,
}

/**
 * The multiplication sign the [KDefaultUnitFormatter] places between product terms.
 *
 * @property symbol the rendered character(s).
 */
enum class KDefaultMultiplication(val symbol: String) {
    /** ASCII asterisk `*` - the historical default. */
    ASTERISK("*"),

    /** Middle dot `·` (U+00B7). */
    MIDDLE_DOT(0x00B7.toChar().toString()),

    /** Multiplication cross `×` (U+00D7). */
    CROSS(0x00D7.toChar().toString());
}

/**
 * The division sign the [KDefaultUnitFormatter] places between numerator and denominator.
 *
 * @property symbol the rendered character(s).
 */
enum class KDefaultDivision(val symbol: String) {
    /** ASCII slash `/` - the historical default. */
    SLASH("/"),

    /** Division sign `÷` (U+00F7). */
    OBELUS(0x00F7.toChar().toString());
}

/**
 * The mathematical function symbols a [KDefaultUnitFormatter] uses where a function representation applies
 * (e.g. a root). With purely integer exponents no such representation occurs, so this table is a prepared,
 * user-overridable configuration rather than something applied to standard unit rendering.
 *
 * It is a plain value type, so applications may pick a preset ([UNICODE], [ASCII]) or supply their own
 * symbols.
 *
 * @property squareRoot the square-root sign (`√`).
 * @property cubeRoot the cube-root sign (`∛`).
 * @property fourthRoot the fourth-root sign (`∜`).
 * @property generalRoot the sign for an n-th root (`√` with an explicit index).
 * @property plusMinus the plus-minus sign (`±`).
 * @property infinity the infinity sign (`∞`).
 * @property degree the degree sign (`°`).
 */
data class KDefaultFunctionSymbols(
    val squareRoot: String,
    val cubeRoot: String,
    val fourthRoot: String,
    val generalRoot: String,
    val plusMinus: String,
    val infinity: String,
    val degree: String,
) {
    companion object {
        /** Real Unicode function symbols (`√`, `∛`, `∜`, `±`, `∞`, `°`) - the default. */
        val UNICODE: KDefaultFunctionSymbols = KDefaultFunctionSymbols(
            squareRoot = 0x221A.toChar().toString(),
            cubeRoot = 0x221B.toChar().toString(),
            fourthRoot = 0x221C.toChar().toString(),
            generalRoot = 0x221A.toChar().toString(),
            plusMinus = 0x00B1.toChar().toString(),
            infinity = 0x221E.toChar().toString(),
            degree = 0x00B0.toChar().toString(),
        )

        /** Plain ASCII fallbacks (`sqrt`, `cbrt`, `root4`, `+-`, `inf`, `deg`) for ASCII-only terminals. */
        val ASCII: KDefaultFunctionSymbols = KDefaultFunctionSymbols(
            squareRoot = "sqrt",
            cubeRoot = "cbrt",
            fourthRoot = "root4",
            generalRoot = "root",
            plusMinus = "+-",
            infinity = "inf",
            degree = "deg",
        )
    }
}

/**
 * The rendering options of the [KDefaultUnitFormatter]: the exponent style, the multiplication and division
 * signs and the function-symbol table. It is a plain value type; applications can pick a preset ([DEFAULT],
 * [SUPERSCRIPT]) or build their own.
 *
 * @property exponentStyle how exponents are rendered ([KDefaultExponentStyle.CARET] vs. superscript).
 * @property multiplication the sign between product terms.
 * @property division the sign between numerator and denominator.
 * @property functionSymbols the function-symbol table (roots etc.).
 */
data class KDefaultFormatConfig(
    val exponentStyle: KDefaultExponentStyle = KDefaultExponentStyle.CARET,
    val multiplication: KDefaultMultiplication = KDefaultMultiplication.ASTERISK,
    val division: KDefaultDivision = KDefaultDivision.SLASH,
    val functionSymbols: KDefaultFunctionSymbols = KDefaultFunctionSymbols.UNICODE,
) {
    companion object {
        /** The historical plain-text default: caret exponents, `*` product, `/` fraction. */
        val DEFAULT: KDefaultFormatConfig = KDefaultFormatConfig()

        /** Like [DEFAULT] but with real Unicode superscript exponents (`m²`, `s⁻¹`). */
        val SUPERSCRIPT: KDefaultFormatConfig =
            KDefaultFormatConfig(exponentStyle = KDefaultExponentStyle.SUPERSCRIPT)
    }
}
