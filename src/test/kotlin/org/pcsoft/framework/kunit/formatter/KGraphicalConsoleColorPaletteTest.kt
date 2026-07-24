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

/** The predefined graphical console palettes (including the bar role) and their value semantics. */
class KGraphicalConsoleColorPaletteTest {

    private val esc = 27.toChar()

    private fun assertSgr(code: String, body: String) {
        assertEquals("$esc[${body}m", code)
    }

    /** The default reset sequence is `ESC[0m`. */
    @Test
    fun `reset sequence`() {
        assertSgr(KGraphicalConsoleColorPalette.RESET, "0")
        assertEquals(KGraphicalConsoleColorPalette.RESET, KGraphicalConsoleColorPalette.CLASSIC.reset)
    }

    /** CLASSIC: cyan / yellow / grey / magenta / grey bar. */
    @Test
    fun `classic palette`() {
        val p = KGraphicalConsoleColorPalette.CLASSIC
        assertSgr(p.numberColor, "36")
        assertSgr(p.symbolColor, "33")
        assertSgr(p.operatorColor, "90")
        assertSgr(p.exponentColor, "35")
        assertSgr(p.barColor, "90")
    }

    /** VIVID: bright green bold / bright blue / white / bright magenta / white bar. */
    @Test
    fun `vivid palette`() {
        val p = KGraphicalConsoleColorPalette.VIVID
        assertSgr(p.numberColor, "92;1")
        assertSgr(p.symbolColor, "94")
        assertSgr(p.operatorColor, "97")
        assertSgr(p.exponentColor, "95")
        assertSgr(p.barColor, "97")
    }

    /** MONOCHROME: bold / dim / dim / uncoloured exponent / dim bar. */
    @Test
    fun `monochrome palette`() {
        val p = KGraphicalConsoleColorPalette.MONOCHROME
        assertSgr(p.numberColor, "1")
        assertSgr(p.symbolColor, "2")
        assertSgr(p.operatorColor, "2")
        assertTrue(p.exponentColor.isEmpty())
        assertSgr(p.barColor, "2")
    }

    /** Value semantics of the palette data class. */
    @Test
    fun `value semantics`() {
        val a = KGraphicalConsoleColorPalette("n", "s", "o", "e", "b")
        val b = KGraphicalConsoleColorPalette("n", "s", "o", "e", "b")
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
        assertEquals(KGraphicalConsoleColorPalette.RESET, a.reset)
        assertNotEquals(a, a.copy(barColor = "x"))
        assertEquals("x", a.copy(barColor = "x").barColor)
    }
}
