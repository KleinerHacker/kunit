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

/** Value semantics and every public member of the AsciiMath formatter's configuration types. */
class KAsciiMathFormatConfigTest {

    /** Fraction styles and quotings are exposed. */
    @Test
    fun `enum entries`() {
        assertEquals(2, KAsciiMathFractionStyle.entries.size)
        assertEquals(KAsciiMathFractionStyle.FRACTION, KAsciiMathFractionStyle.valueOf("FRACTION"))
        assertEquals(KAsciiMathFractionStyle.EXPONENT, KAsciiMathFractionStyle.valueOf("EXPONENT"))
        assertEquals(2, KAsciiMathUnitQuoting.entries.size)
        assertEquals(KAsciiMathUnitQuoting.QUOTED, KAsciiMathUnitQuoting.valueOf("QUOTED"))
        assertEquals(KAsciiMathUnitQuoting.BARE, KAsciiMathUnitQuoting.valueOf("BARE"))
    }

    /** Every multiplication marker carries its expected symbol. */
    @Test
    fun `multiplication symbols`() {
        assertEquals("*", KAsciiMathMultiplication.ASTERISK.symbol)
        assertEquals("xx", KAsciiMathMultiplication.TIMES.symbol)
        assertEquals(" ", KAsciiMathMultiplication.SPACE.symbol)
    }

    /** The DEFAULT preset: fraction, quoted, space. */
    @Test
    fun `default preset`() {
        val c = KAsciiMathFormatConfig.DEFAULT
        assertEquals(KAsciiMathFractionStyle.FRACTION, c.fractionStyle)
        assertEquals(KAsciiMathUnitQuoting.QUOTED, c.quoting)
        assertEquals(KAsciiMathMultiplication.SPACE, c.multiplication)
    }

    /** The PLAIN preset: bare symbols joined by `*`. */
    @Test
    fun `plain preset`() {
        val c = KAsciiMathFormatConfig.PLAIN
        assertEquals(KAsciiMathUnitQuoting.BARE, c.quoting)
        assertEquals(KAsciiMathMultiplication.ASTERISK, c.multiplication)
        assertEquals(KAsciiMathFractionStyle.FRACTION, c.fractionStyle)
    }

    /** Value semantics of the config data class. */
    @Test
    fun `value semantics`() {
        assertEquals(KAsciiMathFormatConfig(), KAsciiMathFormatConfig.DEFAULT)
        assertEquals(KAsciiMathFormatConfig().hashCode(), KAsciiMathFormatConfig.DEFAULT.hashCode())
        assertNotEquals(KAsciiMathFormatConfig.DEFAULT, KAsciiMathFormatConfig.PLAIN)
        assertEquals(
            KAsciiMathUnitQuoting.BARE,
            KAsciiMathFormatConfig.DEFAULT.copy(quoting = KAsciiMathUnitQuoting.BARE).quoting,
        )
    }
}
