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

/** Value semantics and every public member of the graphical console formatter's configuration types. */
class KGraphicalConsoleFormatConfigTest {

    /** Every fraction-bar character is exposed with its expected symbol. */
    @Test
    fun `fraction bar symbols`() {
        assertEquals(3, KGraphicalFractionBar.entries.size)
        assertEquals(0x2500.toChar().toString(), KGraphicalFractionBar.LINE.symbol)
        assertEquals(0x2501.toChar().toString(), KGraphicalFractionBar.HEAVY.symbol)
        assertEquals("-", KGraphicalFractionBar.ASCII.symbol)
    }

    /** Every multiplication sign carries its expected symbol. */
    @Test
    fun `multiplication symbols`() {
        assertEquals("*", KGraphicalMultiplication.ASTERISK.symbol)
        assertEquals(0x00B7.toChar().toString(), KGraphicalMultiplication.MIDDLE_DOT.symbol)
        assertEquals(0x00D7.toChar().toString(), KGraphicalMultiplication.CROSS.symbol)
    }

    /** The UNICODE function-symbol preset holds the real glyphs. */
    @Test
    fun `unicode function symbols`() {
        val s = KGraphicalFunctionSymbols.UNICODE
        assertEquals(0x221A.toChar().toString(), s.squareRoot)
        assertEquals(0x221B.toChar().toString(), s.cubeRoot)
        assertEquals(0x221C.toChar().toString(), s.fourthRoot)
        assertEquals(0x221A.toChar().toString(), s.generalRoot)
        assertEquals(0x00B1.toChar().toString(), s.plusMinus)
        assertEquals(0x221E.toChar().toString(), s.infinity)
        assertEquals(0x00B0.toChar().toString(), s.degree)
    }

    /** The ASCII function-symbol preset holds the plain fallbacks. */
    @Test
    fun `ascii function symbols`() {
        val s = KGraphicalFunctionSymbols.ASCII
        assertEquals("sqrt", s.squareRoot)
        assertEquals("cbrt", s.cubeRoot)
        assertEquals("root4", s.fourthRoot)
        assertEquals("root", s.generalRoot)
        assertEquals("+-", s.plusMinus)
        assertEquals("inf", s.infinity)
        assertEquals("deg", s.degree)
    }

    /** The DEFAULT preset: classic palette, line bar, middle dot, unicode symbols. */
    @Test
    fun `default preset`() {
        val c = KGraphicalConsoleFormatConfig.DEFAULT
        assertEquals(KGraphicalConsoleColorPalette.CLASSIC, c.palette)
        assertEquals(KGraphicalFractionBar.LINE, c.fractionBar)
        assertEquals(KGraphicalMultiplication.MIDDLE_DOT, c.multiplication)
        assertEquals(KGraphicalFunctionSymbols.UNICODE, c.functionSymbols)
    }

    /** Value semantics of the config data class. */
    @Test
    fun `value semantics`() {
        assertEquals(KGraphicalConsoleFormatConfig(), KGraphicalConsoleFormatConfig.DEFAULT)
        assertEquals(KGraphicalConsoleFormatConfig().hashCode(), KGraphicalConsoleFormatConfig.DEFAULT.hashCode())
        assertNotEquals(
            KGraphicalConsoleFormatConfig.DEFAULT,
            KGraphicalConsoleFormatConfig.DEFAULT.copy(fractionBar = KGraphicalFractionBar.ASCII),
        )
        assertEquals(
            KGraphicalFractionBar.ASCII,
            KGraphicalConsoleFormatConfig.DEFAULT.copy(fractionBar = KGraphicalFractionBar.ASCII).fractionBar,
        )
    }
}
