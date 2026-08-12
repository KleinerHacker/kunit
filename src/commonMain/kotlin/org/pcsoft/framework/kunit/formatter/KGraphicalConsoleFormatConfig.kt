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
 * The character the [KGraphicalConsoleUnitFormatter] repeats to draw the horizontal fraction bar.
 *
 * @property symbol the rendered character.
 */
enum class KGraphicalFractionBar(val symbol: String) {
    /** A light box-drawing line `─` (U+2500) - the default. */
    LINE(0x2500.toChar().toString()),

    /** A heavy box-drawing line `━` (U+2501). */
    HEAVY(0x2501.toChar().toString()),

    /** A plain ASCII hyphen `-`. */
    ASCII("-");
}

/**
 * The multiplication sign the [KGraphicalConsoleUnitFormatter] places between numerator (or denominator)
 * terms. Division itself is drawn as the visual fraction bar, not a sign.
 *
 * @property symbol the rendered character(s).
 */
enum class KGraphicalMultiplication(val symbol: String) {
    /** ASCII asterisk `*`. */
    ASTERISK("*"),

    /** Middle dot `·` (U+00B7) - the default. */
    MIDDLE_DOT(0x00B7.toChar().toString()),

    /** Multiplication cross `×` (U+00D7). */
    CROSS(0x00D7.toChar().toString());
}

/**
 * The mathematical function symbols a [KGraphicalConsoleUnitFormatter] uses where a function representation
 * applies (e.g. a root). With purely integer exponents no such representation occurs, so this table is a
 * prepared, user-overridable configuration rather than something applied to standard unit rendering.
 *
 * @property squareRoot the square-root sign (`√`).
 * @property cubeRoot the cube-root sign (`∛`).
 * @property fourthRoot the fourth-root sign (`∜`).
 * @property generalRoot the sign for an n-th root (`√` with an explicit index).
 * @property plusMinus the plus-minus sign (`±`).
 * @property infinity the infinity sign (`∞`).
 * @property degree the degree sign (`°`).
 */
data class KGraphicalFunctionSymbols(
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
        val UNICODE: KGraphicalFunctionSymbols = KGraphicalFunctionSymbols(
            squareRoot = 0x221A.toChar().toString(),
            cubeRoot = 0x221B.toChar().toString(),
            fourthRoot = 0x221C.toChar().toString(),
            generalRoot = 0x221A.toChar().toString(),
            plusMinus = 0x00B1.toChar().toString(),
            infinity = 0x221E.toChar().toString(),
            degree = 0x00B0.toChar().toString(),
        )

        /** Plain ASCII fallbacks (`sqrt`, `cbrt`, `root4`, `+-`, `inf`, `deg`) for ASCII-only terminals. */
        val ASCII: KGraphicalFunctionSymbols = KGraphicalFunctionSymbols(
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
 * The rendering options of the [KGraphicalConsoleUnitFormatter]: the colour [palette], the fraction-bar
 * character, the multiplication sign and the function-symbol table. Applications can pick the [DEFAULT]
 * preset or build their own.
 *
 * @property palette the colours applied to each visual role.
 * @property fractionBar the character used to draw the horizontal fraction bar.
 * @property multiplication the sign between numerator/denominator terms.
 * @property functionSymbols the function-symbol table (roots etc.).
 */
data class KGraphicalConsoleFormatConfig(
    val palette: KGraphicalConsoleColorPalette = KGraphicalConsoleColorPalette.CLASSIC,
    val fractionBar: KGraphicalFractionBar = KGraphicalFractionBar.LINE,
    val multiplication: KGraphicalMultiplication = KGraphicalMultiplication.MIDDLE_DOT,
    val functionSymbols: KGraphicalFunctionSymbols = KGraphicalFunctionSymbols.UNICODE,
) {
    companion object {
        /** Classic palette, light line bar, middle-dot products, Unicode function symbols. */
        val DEFAULT: KGraphicalConsoleFormatConfig = KGraphicalConsoleFormatConfig()
    }
}
