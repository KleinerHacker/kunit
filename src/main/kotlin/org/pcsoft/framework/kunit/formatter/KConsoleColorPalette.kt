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
 * The set of ANSI SGR escape sequences a [KConsoleUnitFormatter] uses to colour the four visual roles of a
 * rendered value: the numeric magnitude, the unit symbols, the operators (`*`, `/`) and the exponent
 * markers (`^n`). Each role holds the **introducer** sequence (e.g. the cyan `ESC[36m`); the shared
 * [reset] sequence is appended after every coloured fragment.
 *
 * A role whose colour is the **empty string** is rendered without any escape sequence at all (the fragment
 * stays uncoloured) - this is how the [MONOCHROME] palette leaves the exponent untouched.
 *
 * The palette is a plain value type, so applications can either pick one of the predefined palettes
 * ([CLASSIC], [VIVID], [MONOCHROME]) or define their own and hand it to
 * `KConsoleUnitFormatter(palette = …)`.
 *
 * @property numberColor the introducer applied to the numeric magnitude.
 * @property symbolColor the introducer applied to each unit symbol (`"km"`, `"h"`).
 * @property operatorColor the introducer applied to the operators `*` and `/`.
 * @property exponentColor the introducer applied to an exponent marker (`"^2"`, `"^-3"`).
 * @property reset the sequence appended after every coloured fragment to restore the default style.
 */
data class KConsoleColorPalette(
    val numberColor: String,
    val symbolColor: String,
    val operatorColor: String,
    val exponentColor: String,
    val reset: String = RESET,
) {
    companion object {
        /** The ANSI SGR reset sequence (`ESC[0m`), the default [reset] of every palette. */
        const val RESET: String = "[0m"

        /**
         * The default palette: calm and readable on a dark terminal. Number = cyan (`ESC[36m`),
         * symbol = yellow (`ESC[33m`), operator = grey/dim (`ESC[90m`), exponent = magenta (`ESC[35m`).
         */
        val CLASSIC: KConsoleColorPalette = KConsoleColorPalette(
            numberColor = "[36m",
            symbolColor = "[33m",
            operatorColor = "[90m",
            exponentColor = "[35m",
        )

        /**
         * A high-contrast palette. Number = bright green bold (`ESC[92;1m`), symbol = bright blue
         * (`ESC[94m`), operator = white (`ESC[97m`), exponent = bright magenta (`ESC[95m`).
         */
        val VIVID: KConsoleColorPalette = KConsoleColorPalette(
            numberColor = "[92;1m",
            symbolColor = "[94m",
            operatorColor = "[97m",
            exponentColor = "[95m",
        )

        /**
         * A restrained palette for colour-poor terminals: brightness only, no colours. Number = bold
         * (`ESC[1m`), symbol = dim (`ESC[2m`), operator = dim (`ESC[2m`), exponent = uncoloured (empty).
         */
        val MONOCHROME: KConsoleColorPalette = KConsoleColorPalette(
            numberColor = "[1m",
            symbolColor = "[2m",
            operatorColor = "[2m",
            exponentColor = "",
        )
    }
}
