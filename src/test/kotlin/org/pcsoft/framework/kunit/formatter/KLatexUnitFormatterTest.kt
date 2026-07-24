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

import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

/** The LaTeX formatter: `\frac`/inline layout, upright unit wrappers, delimiters and multiplication markers. */
class KLatexUnitFormatterTest {

    private fun render(
        value: Double,
        units: List<KUnitTerm>,
        config: KLatexFormatConfig = KLatexFormatConfig.DEFAULT,
    ) = KLatexUnitFormatter(config).format(KUnitFormatContext(value, units, "%.1f", Locale.US))

    private val meter = KUnitTerm(KDistanceUnit.BASE, 1)
    private val perSecond = KUnitTerm(KTimeUnit.BASE, -1)
    private val perSecond2 = KUnitTerm(KTimeUnit.BASE, -2)

    /** A dimensionless value renders as just the number. */
    @Test
    fun dimensionless() {
        assertEquals("5.0", render(5.0, emptyList()))
    }

    /** A single positive term is a product of one, thin-spaced after the number. */
    @Test
    fun `single term`() {
        assertEquals("5.0\\,\\mathrm{m}", render(5.0, listOf(meter)))
    }

    /** Default fraction: end-to-end `km/h`. */
    @Test
    fun `fraction default`() {
        val v = 3 of meters / seconds
        assertEquals(
            "10.8\\,\\frac{\\mathrm{km}}{\\mathrm{h}}",
            v.format(kilo.meters / hours, "%.1f", Locale.US, KLatexUnitFormatter()),
        )
    }

    /** A denominator exponent renders inside the fraction as `^{n}`. */
    @Test
    fun `fraction with powered denominator`() {
        assertEquals("9.8\\,\\frac{\\mathrm{m}}{\\mathrm{s}^{2}}", render(9.8, listOf(meter, perSecond2)))
    }

    /** More than one negative term falls back to the inline product form. */
    @Test
    fun `product form for multiple negatives`() {
        assertEquals(
            "1.0\\,\\mathrm{m}\\cdot\\mathrm{s}^{-1}\\cdot\\mathrm{s}^{-2}",
            render(1.0, listOf(meter, perSecond, perSecond2)),
        )
    }

    /** INLINE style renders `km/h` as a product with a negative exponent. */
    @Test
    fun `inline style`() {
        assertEquals(
            "10.8\\,\\mathrm{m}\\cdot\\mathrm{s}^{-1}",
            render(10.8, listOf(meter, perSecond), KLatexFormatConfig.INLINE),
        )
    }

    /** The `\text{}` wrapper is used when configured. */
    @Test
    fun `text wrapper`() {
        val config = KLatexFormatConfig(unitWrapper = KLatexUnitWrapper.TEXT)
        assertEquals("5.0\\,\\text{m}", render(5.0, listOf(meter), config))
    }

    /** The PLAIN preset drops the wrapper and uses a normal space. */
    @Test
    fun `plain preset`() {
        assertEquals("5.0 m", render(5.0, listOf(meter), KLatexFormatConfig.PLAIN))
    }

    /** The `\times` multiplication marker is used when configured. */
    @Test
    fun `times multiplication`() {
        val config = KLatexFormatConfig(multiplication = KLatexMultiplication.TIMES)
        assertEquals(
            "1.0\\,\\mathrm{m}\\times\\mathrm{s}^{-1}\\times\\mathrm{s}^{-2}",
            render(1.0, listOf(meter, perSecond, perSecond2), config),
        )
    }

    /** The thin-space multiplication marker is used when configured. */
    @Test
    fun `thin space multiplication`() {
        val config = KLatexFormatConfig(multiplication = KLatexMultiplication.THIN_SPACE)
        assertEquals(
            "1.0\\,\\mathrm{m}\\,\\mathrm{s}^{-1}\\,\\mathrm{s}^{-2}",
            render(1.0, listOf(meter, perSecond, perSecond2), config),
        )
    }

    /** The dollar delimiter wraps the whole expression. */
    @Test
    fun `dollar delimiter`() {
        val config = KLatexFormatConfig(delimiter = KLatexDelimiter.DOLLAR)
        assertEquals("\$5.0\\,\\mathrm{m}\$", render(5.0, listOf(meter), config))
    }

    /** The parentheses delimiter wraps the whole expression. */
    @Test
    fun `parentheses delimiter`() {
        val config = KLatexFormatConfig(delimiter = KLatexDelimiter.PARENTHESES)
        assertEquals("\\(5.0\\,\\mathrm{m}\\)", render(5.0, listOf(meter), config))
    }
}
