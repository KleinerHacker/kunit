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

/** Value semantics and every public member of the Typst formatter's configuration types. */
class KTypstFormatConfigTest {

    /** Fraction styles and unit styles are exposed. */
    @Test
    fun `enum entries`() {
        assertEquals(2, KTypstFractionStyle.entries.size)
        assertEquals(KTypstFractionStyle.FRACTION, KTypstFractionStyle.valueOf("FRACTION"))
        assertEquals(KTypstFractionStyle.EXPONENT, KTypstFractionStyle.valueOf("EXPONENT"))
        assertEquals(2, KTypstUnitStyle.entries.size)
        assertEquals(KTypstUnitStyle.UPRIGHT, KTypstUnitStyle.valueOf("UPRIGHT"))
        assertEquals(KTypstUnitStyle.TEXT, KTypstUnitStyle.valueOf("TEXT"))
    }

    /** Every multiplication marker carries its expected separator. */
    @Test
    fun `multiplication separators`() {
        assertEquals(" ", KTypstMultiplication.SPACE.separator)
        assertEquals(" dot ", KTypstMultiplication.DOT.separator)
        assertEquals(" times ", KTypstMultiplication.TIMES.separator)
    }

    /** All delimiters are exposed. */
    @Test
    fun `delimiter entries`() {
        assertEquals(2, KTypstDelimiter.entries.size)
        assertEquals(KTypstDelimiter.MATH, KTypstDelimiter.valueOf("MATH"))
        assertEquals(KTypstDelimiter.FRAGMENT, KTypstDelimiter.valueOf("FRAGMENT"))
    }

    /** The DEFAULT preset: fraction, upright, space, math delimiters. */
    @Test
    fun `default preset`() {
        val c = KTypstFormatConfig.DEFAULT
        assertEquals(KTypstFractionStyle.FRACTION, c.fractionStyle)
        assertEquals(KTypstUnitStyle.UPRIGHT, c.unitStyle)
        assertEquals(KTypstMultiplication.SPACE, c.multiplication)
        assertEquals(KTypstDelimiter.MATH, c.delimiter)
    }

    /** The FRAGMENT preset drops the delimiters. */
    @Test
    fun `fragment preset`() {
        assertEquals(KTypstDelimiter.FRAGMENT, KTypstFormatConfig.FRAGMENT.delimiter)
        assertEquals(KTypstUnitStyle.UPRIGHT, KTypstFormatConfig.FRAGMENT.unitStyle)
    }

    /** Value semantics of the config data class. */
    @Test
    fun `value semantics`() {
        assertEquals(KTypstFormatConfig(), KTypstFormatConfig.DEFAULT)
        assertEquals(KTypstFormatConfig().hashCode(), KTypstFormatConfig.DEFAULT.hashCode())
        assertNotEquals(KTypstFormatConfig.DEFAULT, KTypstFormatConfig.FRAGMENT)
        assertEquals(
            KTypstUnitStyle.TEXT,
            KTypstFormatConfig.DEFAULT.copy(unitStyle = KTypstUnitStyle.TEXT).unitStyle,
        )
    }
}
