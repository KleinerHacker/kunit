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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/** The predefined console colour palettes and the value semantics of the palette data class. */
class KConsoleColorPaletteTest {

    private val esc = 27.toChar()

    /** Every non-empty colour is a real ANSI SGR sequence: `ESC[` … `m`. */
    private fun assertSgr(code: String, body: String) {
        assertEquals("$esc[${body}m", code)
    }

    /** The default reset sequence is `ESC[0m`. */
    @Test
    fun `reset sequence`() {
        assertSgr(KConsoleColorPalette.RESET, "0")
        assertEquals(KConsoleColorPalette.RESET, KConsoleColorPalette.CLASSIC.reset)
    }

    /** CLASSIC: cyan / yellow / grey / magenta. */
    @Test
    fun `classic palette`() {
        val p = KConsoleColorPalette.CLASSIC
        assertSgr(p.numberColor, "36")
        assertSgr(p.symbolColor, "33")
        assertSgr(p.operatorColor, "90")
        assertSgr(p.exponentColor, "35")
    }

    /** VIVID: bright green bold / bright blue / white / bright magenta. */
    @Test
    fun `vivid palette`() {
        val p = KConsoleColorPalette.VIVID
        assertSgr(p.numberColor, "92;1")
        assertSgr(p.symbolColor, "94")
        assertSgr(p.operatorColor, "97")
        assertSgr(p.exponentColor, "95")
    }

    /** MONOCHROME: bold / dim / dim / uncoloured exponent. */
    @Test
    fun `monochrome palette`() {
        val p = KConsoleColorPalette.MONOCHROME
        assertSgr(p.numberColor, "1")
        assertSgr(p.symbolColor, "2")
        assertSgr(p.operatorColor, "2")
        assertTrue(p.exponentColor.isEmpty())
    }

    /** Value semantics of the data class: equality, copy and a defaulted reset. */
    @Test
    fun `value semantics`() {
        val a = KConsoleColorPalette("n", "s", "o", "e")
        val b = KConsoleColorPalette("n", "s", "o", "e")
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
        assertEquals(KConsoleColorPalette.RESET, a.reset)
        assertNotEquals(a, a.copy(numberColor = "x"))
        assertEquals("x", a.copy(numberColor = "x").numberColor)
    }
}
