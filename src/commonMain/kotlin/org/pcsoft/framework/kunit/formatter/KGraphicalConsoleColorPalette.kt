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

/** The ANSI escape character (code 27), kept out of the source as a literal per project convention. */
private val ESC: Char = 27.toChar()

/**
 * The ANSI SGR escape sequences a [KGraphicalConsoleUnitFormatter] uses to colour the five visual roles of
 * a multi-line rendered value: the numeric magnitude, the unit symbols, the multiplication operators, the
 * (superscript) exponents and the fraction bar. Each role holds the **introducer** sequence (e.g. the cyan
 * `ESC[36m`); the shared [reset] is appended after every coloured fragment.
 *
 * A role whose colour is the **empty string** is rendered without any escape sequence (uncoloured) - this
 * is how [MONOCHROME] leaves the exponent untouched.
 *
 * @property numberColor the introducer applied to the numeric magnitude.
 * @property symbolColor the introducer applied to each unit symbol.
 * @property operatorColor the introducer applied to the multiplication operators.
 * @property exponentColor the introducer applied to a superscript exponent.
 * @property barColor the introducer applied to the fraction bar.
 * @property reset the sequence appended after every coloured fragment.
 */
data class KGraphicalConsoleColorPalette(
    val numberColor: String,
    val symbolColor: String,
    val operatorColor: String,
    val exponentColor: String,
    val barColor: String,
    val reset: String = RESET,
) {
    companion object {
        /** The ANSI SGR reset sequence (`ESC[0m`), the default [reset] of every palette. */
        val RESET: String = "$ESC[0m"

        /** The default palette: cyan number, yellow symbol, grey operator, magenta exponent, grey bar. */
        val CLASSIC: KGraphicalConsoleColorPalette = KGraphicalConsoleColorPalette(
            numberColor = "$ESC[36m",
            symbolColor = "$ESC[33m",
            operatorColor = "$ESC[90m",
            exponentColor = "$ESC[35m",
            barColor = "$ESC[90m",
        )

        /** A high-contrast palette: bright green bold number, bright blue symbol, white operator/bar. */
        val VIVID: KGraphicalConsoleColorPalette = KGraphicalConsoleColorPalette(
            numberColor = "$ESC[92;1m",
            symbolColor = "$ESC[94m",
            operatorColor = "$ESC[97m",
            exponentColor = "$ESC[95m",
            barColor = "$ESC[97m",
        )

        /** A restrained palette for colour-poor terminals: brightness only, uncoloured exponent. */
        val MONOCHROME: KGraphicalConsoleColorPalette = KGraphicalConsoleColorPalette(
            numberColor = "$ESC[1m",
            symbolColor = "$ESC[2m",
            operatorColor = "$ESC[2m",
            exponentColor = "",
            barColor = "$ESC[2m",
        )
    }
}
