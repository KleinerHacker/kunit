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
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

/** The ANSI-colouring console formatter: same layout as the default formatter, wrapped in palette colours. */
class KConsoleUnitFormatterTest {

    private val classic = KConsoleColorPalette.CLASSIC

    /** Wraps [text] in [code] + reset, mirroring the formatter (empty code leaves the text untouched). */
    private fun col(code: String, text: String): String =
        if (code.isEmpty()) text else code + text + classic.reset

    /** A single unit term (exponent 1): coloured number, space, coloured symbol. */
    @Test
    fun `single unit`() {
        val expected = col(classic.numberColor, "5.0") + " " + col(classic.symbolColor, "m")
        assertEquals(expected, (5 of meters).format(meters, "%.1f", Locale.US, KConsoleUnitFormatter()))
    }

    /** Fraction shape `km/h`: symbols and the `/` operator get their own colours. */
    @Test
    fun `fraction notation`() {
        val v = 3 of meters / seconds
        val expected = col(classic.numberColor, "10.8") + " " +
            col(classic.symbolColor, "km") + col(classic.operatorColor, "/") + col(classic.symbolColor, "h")
        assertEquals(expected, v.format(kilo.meters / hours, "%.1f", Locale.US, KConsoleUnitFormatter()))
    }

    /** A denominator exponent (`m/s^2`) colours the `^2` marker separately. */
    @Test
    fun `denominator exponent`() {
        val v = 9.81 of meters / (seconds pow 2)
        val expected = col(classic.numberColor, "9.81") + " " +
            col(classic.symbolColor, "m") + col(classic.operatorColor, "/") +
            col(classic.symbolColor, "s") + col(classic.exponentColor, "^2")
        assertEquals(expected, v.format(meters / (seconds pow 2), "%.2f", Locale.US, KConsoleUnitFormatter()))
    }

    /** Flat product form (not a clean `a/b`): terms joined by the coloured `*`, signed exponents coloured. */
    @Test
    fun `product notation`() {
        val context = KUnitFormatContext(
            2.0,
            listOf(
                KUnitTerm(KDistanceUnit.BASE, 1),
                KUnitTerm(KTimeUnit.BASE, -3),
                KUnitTerm(KTimeUnit.BASE, -2),
            ),
            pattern = "%.1f",
            locale = Locale.US,
        )
        val star = col(classic.operatorColor, "*")
        val expected = col(classic.numberColor, "2.0") + " " +
            col(classic.symbolColor, "m") + star +
            col(classic.symbolColor, "s") + col(classic.exponentColor, "^-3") + star +
            col(classic.symbolColor, "s") + col(classic.exponentColor, "^-2")
        assertEquals(expected, KConsoleUnitFormatter().format(context))
    }

    /** Only-negative terms (no numerator) skip the fraction form and render as a flat product. */
    @Test
    fun `no numerator uses product form`() {
        val context = KUnitFormatContext(
            2.0,
            listOf(KUnitTerm(KTimeUnit.BASE, -1)),
            pattern = "%.1f",
            locale = Locale.US,
        )
        val expected = col(classic.numberColor, "2.0") + " " +
            col(classic.symbolColor, "s") + col(classic.exponentColor, "^-1")
        assertEquals(expected, KConsoleUnitFormatter().format(context))
    }

    /** A dimensionless value renders as the coloured number alone (no trailing space). */
    @Test
    fun `dimensionless value`() {
        val context = KUnitFormatContext(2.0, emptyList(), pattern = "%.1f", locale = Locale.US)
        assertEquals(col(classic.numberColor, "2.0"), KConsoleUnitFormatter().format(context))
    }

    /** The VIVID palette produces its own sequences. */
    @Test
    fun `vivid palette`() {
        val p = KConsoleColorPalette.VIVID
        val context = KUnitFormatContext(5.0, listOf(KUnitTerm(KDistanceUnit.BASE, 1)), "%.1f", Locale.US)
        val expected = p.numberColor + "5.0" + p.reset + " " + p.symbolColor + "m" + p.reset
        assertEquals(expected, KConsoleUnitFormatter(p).format(context))
    }

    /** The MONOCHROME palette leaves the exponent uncoloured (empty code => no escape sequence). */
    @Test
    fun `monochrome palette leaves exponent uncoloured`() {
        val p = KConsoleColorPalette.MONOCHROME
        val context = KUnitFormatContext(2.0, listOf(KUnitTerm(KDistanceUnit.BASE, 2)), "%.1f", Locale.US)
        val expected = p.numberColor + "2.0" + p.reset + " " + p.symbolColor + "m" + p.reset + "^2"
        assertEquals(expected, KConsoleUnitFormatter(p).format(context))
    }

    /** A custom palette passed through the constructor is honoured for every role. */
    @Test
    fun `custom palette`() {
        val p = KConsoleColorPalette("N", "S", "O", "E", reset = "R")
        val context = KUnitFormatContext(
            1.0,
            listOf(KUnitTerm(KDistanceUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -1)),
            "%.0f",
            Locale.US,
        )
        // number "N1R", space, numerator "SmR", operator "O/R", denominator "SsR"
        val expected = "N1R" + " " + "SmR" + "O/R" + "SsR"
        assertEquals(expected, KConsoleUnitFormatter(p).format(context))
    }
}
