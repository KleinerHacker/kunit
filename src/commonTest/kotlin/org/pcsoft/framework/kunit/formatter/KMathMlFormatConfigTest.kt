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

/** Value semantics and every public member of the MathML formatter's configuration types. */
class KMathMlFormatConfigTest {

    /** Fraction styles and unit tags are exposed. */
    @Test
    fun `enum entries`() {
        assertEquals(2, KMathMlFractionStyle.entries.size)
        assertEquals(KMathMlFractionStyle.MFRAC, KMathMlFractionStyle.valueOf("MFRAC"))
        assertEquals(KMathMlFractionStyle.EXPONENT, KMathMlFractionStyle.valueOf("EXPONENT"))
        assertEquals(2, KMathMlUnitTag.entries.size)
        assertEquals(KMathMlUnitTag.MI, KMathMlUnitTag.valueOf("MI"))
        assertEquals(KMathMlUnitTag.MTEXT, KMathMlUnitTag.valueOf("MTEXT"))
    }

    /** Every multiplication operator carries its expected `<mo>` content. */
    @Test
    fun `multiplication content`() {
        assertEquals(0x00B7.toChar().toString(), KMathMlMultiplication.MIDDLE_DOT.moContent)
        assertEquals(0x00D7.toChar().toString(), KMathMlMultiplication.TIMES.moContent)
        assertEquals(0x2062.toChar().toString(), KMathMlMultiplication.INVISIBLE_TIMES.moContent)
    }

    /** All wrappers are exposed. */
    @Test
    fun `wrapper entries`() {
        assertEquals(3, KMathMlWrapper.entries.size)
        assertEquals(KMathMlWrapper.MATH_INLINE, KMathMlWrapper.valueOf("MATH_INLINE"))
        assertEquals(KMathMlWrapper.MATH_BLOCK, KMathMlWrapper.valueOf("MATH_BLOCK"))
        assertEquals(KMathMlWrapper.FRAGMENT, KMathMlWrapper.valueOf("FRAGMENT"))
    }

    /** The DEFAULT preset: mfrac, mi, invisible times, inline math root. */
    @Test
    fun `default preset`() {
        val c = KMathMlFormatConfig.DEFAULT
        assertEquals(KMathMlFractionStyle.MFRAC, c.fractionStyle)
        assertEquals(KMathMlUnitTag.MI, c.unitTag)
        assertEquals(KMathMlMultiplication.INVISIBLE_TIMES, c.multiplication)
        assertEquals(KMathMlWrapper.MATH_INLINE, c.wrapper)
    }

    /** The INLINE preset swaps to exponent fractions. */
    @Test
    fun `inline preset`() {
        assertEquals(KMathMlFractionStyle.EXPONENT, KMathMlFormatConfig.INLINE.fractionStyle)
        assertEquals(KMathMlWrapper.MATH_INLINE, KMathMlFormatConfig.INLINE.wrapper)
    }

    /** The FRAGMENT preset drops the math root. */
    @Test
    fun `fragment preset`() {
        assertEquals(KMathMlWrapper.FRAGMENT, KMathMlFormatConfig.FRAGMENT.wrapper)
        assertEquals(KMathMlFractionStyle.MFRAC, KMathMlFormatConfig.FRAGMENT.fractionStyle)
    }

    /** Value semantics of the config data class. */
    @Test
    fun `value semantics`() {
        assertEquals(KMathMlFormatConfig(), KMathMlFormatConfig.DEFAULT)
        assertEquals(KMathMlFormatConfig().hashCode(), KMathMlFormatConfig.DEFAULT.hashCode())
        assertNotEquals(KMathMlFormatConfig.DEFAULT, KMathMlFormatConfig.INLINE)
        assertEquals(
            KMathMlUnitTag.MTEXT,
            KMathMlFormatConfig.DEFAULT.copy(unitTag = KMathMlUnitTag.MTEXT).unitTag,
        )
    }
}
