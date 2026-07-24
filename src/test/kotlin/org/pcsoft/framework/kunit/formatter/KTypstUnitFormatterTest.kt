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
import org.pcsoft.framework.kunit.mass.KMassUnit
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

/** The Typst formatter: a/b fraction, upright units, grouping, delimiters and multiplication markers. */
class KTypstUnitFormatterTest {

    private fun render(
        value: Double,
        units: List<KUnitTerm>,
        config: KTypstFormatConfig = KTypstFormatConfig.DEFAULT,
    ) = KTypstUnitFormatter(config).format(KUnitFormatContext(value, units, "%.1f", Locale.US))

    private val meter = KUnitTerm(KDistanceUnit.BASE, 1)
    private val gram = KUnitTerm(KMassUnit.BASE, 1)
    private val perSecond = KUnitTerm(KTimeUnit.BASE, -1)
    private val perSecond2 = KUnitTerm(KTimeUnit.BASE, -2)

    /** A dimensionless value renders as just the number inside `$…$`. */
    @Test
    fun dimensionless() {
        assertEquals("\$5.0\$", render(5.0, emptyList()))
    }

    /** A single term: number, space, upright symbol. */
    @Test
    fun `single term`() {
        assertEquals("\$5.0 upright(\"m\")\$", render(5.0, listOf(meter)))
    }

    /** Default fraction: end-to-end `km/h`. */
    @Test
    fun `fraction default`() {
        val v = 3 of meters / seconds
        assertEquals(
            "\$10.8 upright(\"km\")/upright(\"h\")\$",
            v.format(kilo.meters / hours, "%.1f", Locale.US, KTypstUnitFormatter()),
        )
    }

    /** A powered denominator is grouped in parentheses. */
    @Test
    fun `fraction with powered denominator`() {
        assertEquals("\$9.8 upright(\"m\")/(upright(\"s\")^2)\$", render(9.8, listOf(meter, perSecond2)))
    }

    /** Several numerator terms are grouped in parentheses. */
    @Test
    fun `grouped numerator`() {
        assertEquals(
            "\$1.0 (upright(\"m\") upright(\"g\"))/upright(\"s\")\$",
            render(1.0, listOf(meter, gram, perSecond)),
        )
    }

    /** More than one negative term falls back to the flat product form. */
    @Test
    fun `product form for multiple negatives`() {
        assertEquals(
            "\$1.0 upright(\"m\") upright(\"s\")^(-1) upright(\"s\")^(-2)\$",
            render(1.0, listOf(meter, perSecond, perSecond2)),
        )
    }

    /** EXPONENT style renders `km/h` as a product with a negative exponent. */
    @Test
    fun `exponent style`() {
        assertEquals(
            "\$10.8 upright(\"m\") upright(\"s\")^(-1)\$",
            render(10.8, listOf(meter, perSecond), KTypstFormatConfig(fractionStyle = KTypstFractionStyle.EXPONENT)),
        )
    }

    /** The FRAGMENT preset drops the `$…$` delimiters. */
    @Test
    fun `fragment preset`() {
        assertEquals("5.0 upright(\"m\")", render(5.0, listOf(meter), KTypstFormatConfig.FRAGMENT))
    }

    /** The TEXT unit style drops `upright(...)`. */
    @Test
    fun `text unit style`() {
        val config = KTypstFormatConfig(unitStyle = KTypstUnitStyle.TEXT)
        assertEquals("\$5.0 \"m\"\$", render(5.0, listOf(meter), config))
    }

    /** The `dot` multiplication marker is used when configured. */
    @Test
    fun `dot multiplication`() {
        val config = KTypstFormatConfig(multiplication = KTypstMultiplication.DOT)
        assertEquals(
            "\$1.0 upright(\"m\") dot upright(\"s\")^(-1) dot upright(\"s\")^(-2)\$",
            render(1.0, listOf(meter, perSecond, perSecond2), config),
        )
    }

    /** The `times` multiplication marker is used when configured. */
    @Test
    fun `times multiplication`() {
        val config = KTypstFormatConfig(multiplication = KTypstMultiplication.TIMES)
        assertEquals(
            "\$1.0 upright(\"m\") times upright(\"s\")^(-1) times upright(\"s\")^(-2)\$",
            render(1.0, listOf(meter, perSecond, perSecond2), config),
        )
    }
}
