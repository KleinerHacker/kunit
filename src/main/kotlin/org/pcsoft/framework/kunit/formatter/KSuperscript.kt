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

/** Unicode superscript glyphs for the digits 0..9 (built from code points to stay ASCII-only in source). */
private val SUPERSCRIPT_DIGITS: List<Char> =
    listOf(0x2070, 0x00B9, 0x00B2, 0x00B3, 0x2074, 0x2075, 0x2076, 0x2077, 0x2078, 0x2079)
        .map { it.toChar() }

/** The Unicode superscript minus sign (U+207B). */
private val SUPERSCRIPT_MINUS: Char = 0x207B.toChar()

/**
 * Renders [exponent] as a sequence of real Unicode superscript characters, e.g. `2` -> `²`, `-3` -> `⁻³`.
 * Shared by the console-based formatters that offer a superscript exponent style.
 */
internal fun renderSuperscriptExponent(exponent: Int): String =
    buildString {
        for (c in exponent.toString()) {
            append(if (c == '-') SUPERSCRIPT_MINUS else SUPERSCRIPT_DIGITS[c - '0'])
        }
    }
