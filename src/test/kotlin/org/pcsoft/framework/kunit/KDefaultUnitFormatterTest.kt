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

package org.pcsoft.framework.kunit

import org.pcsoft.framework.kunit.formatter.KDefaultDivision
import org.pcsoft.framework.kunit.formatter.KDefaultFormatConfig
import org.pcsoft.framework.kunit.formatter.KDefaultMultiplication
import org.pcsoft.framework.kunit.formatter.KDefaultUnitFormatter
import org.pcsoft.framework.kunit.formatter.KUnitFormatContext
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.ec.KElectricCurrentUnit
import org.pcsoft.framework.kunit.mass.KMassUnit
import org.pcsoft.framework.kunit.time.KTimeUnit
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Direct, full-branch coverage of [KDefaultUnitFormatter] by feeding it hand-built [KUnitFormatContext]s,
 * independent of any unit group's construction path.
 */
class KDefaultUnitFormatterTest {

    private fun render(
        value: Double,
        units: List<KUnitTerm>,
        pattern: String? = null,
        locale: Locale = Locale.US,
        config: KDefaultFormatConfig = KDefaultFormatConfig.DEFAULT,
    ) = KDefaultUnitFormatter(config).format(KUnitFormatContext(value, units, pattern, locale))

    /** No terms -> just the number, no trailing unit part. */
    @Test
    fun `dimensionless renders only the number`() {
        assertEquals("5.0", render(5.0, emptyList()))
    }

    /** A single exponent-1 term without display renders the base symbol. */
    @Test
    fun `single term exponent one`() {
        assertEquals("5.0 m", render(5.0, listOf(KUnitTerm(KDistanceUnit.BASE, 1))))
    }

    /** A display carries a prefixed symbol. */
    @Test
    fun `single term with prefixed display`() {
        val term = KUnitTerm(KDistanceUnit.BASE, 1, KUnitDisplay(KDistanceUnit.METER, "k"))
        assertEquals("1.5 km", render(1.5, listOf(term)))
    }

    /** A positive exponent other than 1 is appended as `^n`. */
    @Test
    fun `positive exponent renders caret`() {
        assertEquals("20000.0 m^2", render(20000.0, listOf(KUnitTerm(KDistanceUnit.BASE, 2))))
    }

    /** One numerator + one denominator -> fraction, denominator exponent -1 dropped. */
    @Test
    fun `fraction with unit denominator`() {
        val units = listOf(KUnitTerm(KDistanceUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -1))
        assertEquals("10.8 m/s", render(10.8, units))
    }

    /** A denominator exponent other than -1 renders its positive magnitude. */
    @Test
    fun `fraction with powered denominator`() {
        val units = listOf(KUnitTerm(KDistanceUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -2))
        assertEquals("9.81 m/s^2", render(9.81, units))
    }

    /** More than one negative term -> no fraction, signed `*` form. */
    @Test
    fun `multiple negatives use star form`() {
        val units = listOf(
            KUnitTerm(KDistanceUnit.BASE, 1),
            KUnitTerm(KTimeUnit.BASE, -3),
            KUnitTerm(KElectricCurrentUnit.BASE, -2),
        )
        assertEquals("1.0 m*s^-3*A^-2", render(1.0, units))
    }

    /** Only negative terms (no numerator) -> signed `*` form, no fraction. */
    @Test
    fun `only negatives use star form`() {
        assertEquals("2.0 s^-1", render(2.0, listOf(KUnitTerm(KTimeUnit.BASE, -1))))
    }

    /** Several positive terms join with `*`. */
    @Test
    fun `multiple positives join with star`() {
        val units = listOf(KUnitTerm(KDistanceUnit.BASE, 1), KUnitTerm(KMassUnit.BASE, 1))
        assertEquals("1.0 m*g", render(1.0, units))
    }

    /** A null pattern uses Double.toString; a pattern uses String.format. */
    @Test
    fun `pattern controls number rendering`() {
        val units = listOf(KUnitTerm(KDistanceUnit.BASE, 1))
        assertEquals("1.5 m", render(1.5, units, pattern = null))
        assertEquals("1.50 m", render(1.5, units, pattern = "%.2f"))
    }

    /** The locale drives the decimal separator. */
    @Test
    fun `locale controls decimal separator`() {
        val units = listOf(KUnitTerm(KDistanceUnit.BASE, 1))
        assertEquals("1,50 m", render(1.5, units, pattern = "%.2f", locale = Locale.GERMAN))
    }

    /** An invalid pattern surfaces the formatter exception. */
    @Test
    fun `invalid pattern fails`() {
        assertFailsWith<java.util.IllegalFormatException> {
            render(1.0, listOf(KUnitTerm(KDistanceUnit.BASE, 1)), pattern = "%.2d")
        }
    }

    /** SUPERSCRIPT config renders a positive exponent as real superscript digits (`m²`). */
    @Test
    fun `superscript positive exponent`() {
        val sup2 = 0x00B2.toChar().toString()
        assertEquals(
            "20000.0 m$sup2",
            render(20000.0, listOf(KUnitTerm(KDistanceUnit.BASE, 2)), config = KDefaultFormatConfig.SUPERSCRIPT),
        )
    }

    /** SUPERSCRIPT config renders a negative exponent with the superscript minus (`s⁻¹`). */
    @Test
    fun `superscript negative exponent`() {
        val supMinus1 = 0x207B.toChar().toString() + 0x00B9.toChar().toString()
        assertEquals(
            "2.0 s$supMinus1",
            render(2.0, listOf(KUnitTerm(KTimeUnit.BASE, -1)), config = KDefaultFormatConfig.SUPERSCRIPT),
        )
    }

    /** The MIDDLE_DOT multiplication sign is used between product terms. */
    @Test
    fun `middle dot multiplication`() {
        val dot = KDefaultMultiplication.MIDDLE_DOT.symbol
        val units = listOf(KUnitTerm(KDistanceUnit.BASE, 1), KUnitTerm(KMassUnit.BASE, 1))
        val config = KDefaultFormatConfig(multiplication = KDefaultMultiplication.MIDDLE_DOT)
        assertEquals("1.0 m${dot}g", render(1.0, units, config = config))
    }

    /** The CROSS multiplication sign is used between product terms. */
    @Test
    fun `cross multiplication`() {
        val cross = KDefaultMultiplication.CROSS.symbol
        val units = listOf(KUnitTerm(KDistanceUnit.BASE, 1), KUnitTerm(KMassUnit.BASE, 1))
        val config = KDefaultFormatConfig(multiplication = KDefaultMultiplication.CROSS)
        assertEquals("1.0 m${cross}g", render(1.0, units, config = config))
    }

    /** The OBELUS division sign is used between numerator and denominator. */
    @Test
    fun `obelus division`() {
        val obelus = KDefaultDivision.OBELUS.symbol
        val units = listOf(KUnitTerm(KDistanceUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -1))
        val config = KDefaultFormatConfig(division = KDefaultDivision.OBELUS)
        assertEquals("10.8 m${obelus}s", render(10.8, units, config = config))
    }
}
