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

/** Value semantics and every public member of the LaTeX formatter's configuration types. */
class KLatexFormatConfigTest {

    /** Both fraction styles and all unit wrappers are exposed. */
    @Test
    fun `enum entries`() {
        assertEquals(2, KLatexFractionStyle.entries.size)
        assertEquals(KLatexFractionStyle.FRACTION, KLatexFractionStyle.valueOf("FRACTION"))
        assertEquals(KLatexFractionStyle.INLINE, KLatexFractionStyle.valueOf("INLINE"))
        assertEquals(3, KLatexUnitWrapper.entries.size)
        assertEquals(KLatexUnitWrapper.MATHRM, KLatexUnitWrapper.valueOf("MATHRM"))
        assertEquals(KLatexUnitWrapper.TEXT, KLatexUnitWrapper.valueOf("TEXT"))
        assertEquals(KLatexUnitWrapper.NONE, KLatexUnitWrapper.valueOf("NONE"))
    }

    /** Every multiplication marker carries its expected LaTeX. */
    @Test
    fun `multiplication latex`() {
        assertEquals("\\cdot", KLatexMultiplication.CDOT.latex)
        assertEquals("\\times", KLatexMultiplication.TIMES.latex)
        assertEquals("\\,", KLatexMultiplication.THIN_SPACE.latex)
    }

    /** Every spacing carries its expected LaTeX. */
    @Test
    fun `spacing latex`() {
        assertEquals("\\,", KLatexSpacing.THIN.latex)
        assertEquals(" ", KLatexSpacing.NORMAL.latex)
    }

    /** All delimiters are exposed. */
    @Test
    fun `delimiter entries`() {
        assertEquals(3, KLatexDelimiter.entries.size)
        assertEquals(KLatexDelimiter.DOLLAR, KLatexDelimiter.valueOf("DOLLAR"))
        assertEquals(KLatexDelimiter.PARENTHESES, KLatexDelimiter.valueOf("PARENTHESES"))
        assertEquals(KLatexDelimiter.NONE, KLatexDelimiter.valueOf("NONE"))
    }

    /** The DEFAULT preset: stacked fraction, mathrm, cdot, no delimiter, thin space. */
    @Test
    fun `default preset`() {
        val c = KLatexFormatConfig.DEFAULT
        assertEquals(KLatexFractionStyle.FRACTION, c.fractionStyle)
        assertEquals(KLatexUnitWrapper.MATHRM, c.unitWrapper)
        assertEquals(KLatexMultiplication.CDOT, c.multiplication)
        assertEquals(KLatexDelimiter.NONE, c.delimiter)
        assertEquals(KLatexSpacing.THIN, c.spacing)
    }

    /** The INLINE preset only swaps the fraction style. */
    @Test
    fun `inline preset`() {
        val c = KLatexFormatConfig.INLINE
        assertEquals(KLatexFractionStyle.INLINE, c.fractionStyle)
        assertEquals(KLatexUnitWrapper.MATHRM, c.unitWrapper)
    }

    /** The PLAIN preset drops the wrapper and uses a normal space. */
    @Test
    fun `plain preset`() {
        val c = KLatexFormatConfig.PLAIN
        assertEquals(KLatexUnitWrapper.NONE, c.unitWrapper)
        assertEquals(KLatexSpacing.NORMAL, c.spacing)
        assertEquals(KLatexFractionStyle.FRACTION, c.fractionStyle)
    }

    /** Value semantics of the config data class. */
    @Test
    fun `value semantics`() {
        assertEquals(KLatexFormatConfig(), KLatexFormatConfig.DEFAULT)
        assertEquals(KLatexFormatConfig().hashCode(), KLatexFormatConfig.DEFAULT.hashCode())
        assertNotEquals(KLatexFormatConfig.DEFAULT, KLatexFormatConfig.INLINE)
        assertEquals(
            KLatexDelimiter.DOLLAR,
            KLatexFormatConfig.DEFAULT.copy(delimiter = KLatexDelimiter.DOLLAR).delimiter,
        )
    }
}
