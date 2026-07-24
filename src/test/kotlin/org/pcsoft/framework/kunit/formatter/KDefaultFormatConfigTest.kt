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

/** Value semantics and every public member of the default formatter's configuration types. */
class KDefaultFormatConfigTest {

    /** Both exponent styles are exposed. */
    @Test
    fun `exponent style entries`() {
        assertEquals(2, KDefaultExponentStyle.entries.size)
        assertEquals(KDefaultExponentStyle.CARET, KDefaultExponentStyle.valueOf("CARET"))
        assertEquals(KDefaultExponentStyle.SUPERSCRIPT, KDefaultExponentStyle.valueOf("SUPERSCRIPT"))
    }

    /** Every multiplication sign carries its expected symbol. */
    @Test
    fun `multiplication symbols`() {
        assertEquals("*", KDefaultMultiplication.ASTERISK.symbol)
        assertEquals(0x00B7.toChar().toString(), KDefaultMultiplication.MIDDLE_DOT.symbol)
        assertEquals(0x00D7.toChar().toString(), KDefaultMultiplication.CROSS.symbol)
    }

    /** Every division sign carries its expected symbol. */
    @Test
    fun `division symbols`() {
        assertEquals("/", KDefaultDivision.SLASH.symbol)
        assertEquals(0x00F7.toChar().toString(), KDefaultDivision.OBELUS.symbol)
    }

    /** The UNICODE function-symbol preset holds the real glyphs. */
    @Test
    fun `unicode function symbols`() {
        val s = KDefaultFunctionSymbols.UNICODE
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
        val s = KDefaultFunctionSymbols.ASCII
        assertEquals("sqrt", s.squareRoot)
        assertEquals("cbrt", s.cubeRoot)
        assertEquals("root4", s.fourthRoot)
        assertEquals("root", s.generalRoot)
        assertEquals("+-", s.plusMinus)
        assertEquals("inf", s.infinity)
        assertEquals("deg", s.degree)
    }

    /** The DEFAULT preset is the historical plain-text behaviour. */
    @Test
    fun `default preset`() {
        val c = KDefaultFormatConfig.DEFAULT
        assertEquals(KDefaultExponentStyle.CARET, c.exponentStyle)
        assertEquals(KDefaultMultiplication.ASTERISK, c.multiplication)
        assertEquals(KDefaultDivision.SLASH, c.division)
        assertEquals(KDefaultFunctionSymbols.UNICODE, c.functionSymbols)
    }

    /** The SUPERSCRIPT preset only swaps the exponent style. */
    @Test
    fun `superscript preset`() {
        val c = KDefaultFormatConfig.SUPERSCRIPT
        assertEquals(KDefaultExponentStyle.SUPERSCRIPT, c.exponentStyle)
        assertEquals(KDefaultMultiplication.ASTERISK, c.multiplication)
        assertEquals(KDefaultDivision.SLASH, c.division)
    }

    /** Value semantics of the config data class. */
    @Test
    fun `value semantics`() {
        assertEquals(KDefaultFormatConfig(), KDefaultFormatConfig.DEFAULT)
        assertEquals(KDefaultFormatConfig().hashCode(), KDefaultFormatConfig.DEFAULT.hashCode())
        assertNotEquals(KDefaultFormatConfig.DEFAULT, KDefaultFormatConfig.SUPERSCRIPT)
        assertEquals(
            KDefaultDivision.OBELUS,
            KDefaultFormatConfig.DEFAULT.copy(division = KDefaultDivision.OBELUS).division,
        )
    }
}
