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
import org.pcsoft.framework.kunit.mass.KMassUnit
import org.pcsoft.framework.kunit.time.KTimeUnit
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

/** The multi-line graphical console formatter: 2D fraction bar, superscripts, centring and ANSI colouring. */
class KGraphicalConsoleUnitFormatterTest {

    /** An all-empty palette so the geometry can be asserted as plain text. */
    private val plain = KGraphicalConsoleColorPalette("", "", "", "", "", reset = "")

    private val bar = KGraphicalFractionBar.LINE.symbol
    private val dot = KGraphicalMultiplication.MIDDLE_DOT.symbol

    private fun render(
        value: Double,
        units: List<KUnitTerm>,
        config: KGraphicalConsoleFormatConfig = KGraphicalConsoleFormatConfig(palette = plain),
        pattern: String = "%.1f",
    ) = KGraphicalConsoleUnitFormatter(config).format(KUnitFormatContext(value, units, pattern, Locale.US))

    private val meter = KUnitTerm(KDistanceUnit.BASE, 1)
    private val gram = KUnitTerm(KMassUnit.BASE, 1)
    private val perSecond = KUnitTerm(KTimeUnit.BASE, -1)
    private val perSecond2 = KUnitTerm(KTimeUnit.BASE, -2)
    private val perSecond3 = KUnitTerm(KTimeUnit.BASE, -3)

    /** A dimensionless value is the number alone (single line). */
    @Test
    fun dimensionless() {
        assertEquals("5.0", render(5.0, emptyList()))
    }

    /** A pure product (no single denominator) stays on one line with the multiplication sign. */
    @Test
    fun `single term single line`() {
        assertEquals("5.0 m", render(5.0, listOf(meter)))
    }

    /** A fraction with a powered denominator is drawn over three lines with a superscript exponent. */
    @Test
    fun `fraction with powered denominator`() {
        val sup2 = renderSuperscriptExponent(2)
        // number "9.81" -> indent 5; numerator "m" width 1, denominator "s²" width 2, bar width 2.
        val expected = "     m\n" +
            "9.81 $bar$bar\n" +
            "     s$sup2"
        assertEquals(expected, render(9.81, listOf(meter, perSecond2), pattern = "%.2f"))
    }

    /** A multi-term numerator is centred over the bar; the shorter denominator is centred too. */
    @Test
    fun `centred numerator and denominator`() {
        // numerator "m·g" width 3, denominator "s" width 1, bar width 3; number "1.0" -> indent 4.
        val expected = "    m${dot}g\n" +
            "1.0 $bar$bar$bar\n" +
            "     s"
        assertEquals(expected, render(1.0, listOf(meter, gram, perSecond)))
    }

    /** More than one negative term stays a single-line product with superscript exponents. */
    @Test
    fun `product with multiple negatives`() {
        val sup3 = renderSuperscriptExponent(-3)
        val sup2 = renderSuperscriptExponent(-2)
        assertEquals("1.0 m${dot}s$sup3${dot}s$sup2", render(1.0, listOf(meter, perSecond3, perSecond2)))
    }

    /** The HEAVY bar and CROSS multiplication signs are honoured. */
    @Test
    fun `heavy bar and cross multiplication`() {
        val heavy = KGraphicalFractionBar.HEAVY.symbol
        val cross = KGraphicalMultiplication.CROSS.symbol
        val config = KGraphicalConsoleFormatConfig(
            palette = plain,
            fractionBar = KGraphicalFractionBar.HEAVY,
            multiplication = KGraphicalMultiplication.CROSS,
        )
        // numerator "m×g" width 3, denominator "s" width 1, bar width 3; number "1.0" -> indent 4.
        val expected = "    m${cross}g\n" +
            "1.0 $heavy$heavy$heavy\n" +
            "     s"
        assertEquals(expected, render(1.0, listOf(meter, gram, perSecond), config))
    }

    /** Every visual role of a product is wrapped in its palette colour. */
    @Test
    fun `product colouring`() {
        val palette = KGraphicalConsoleColorPalette("N", "S", "O", "E", "B", reset = "R")
        val config = KGraphicalConsoleFormatConfig(palette = palette)
        val sup3 = renderSuperscriptExponent(-3)
        val sup2 = renderSuperscriptExponent(-2)
        // Multiple negatives -> single-line product: "N1.0R" space "SmR" "O·R" "SsR" "E⁻³R" "O·R" "SsR" "E⁻²R"
        val expected = "N1.0R" + " " + "SmR" + "O${dot}R" + "SsR" + "E${sup3}R" + "O${dot}R" + "SsR" + "E${sup2}R"
        assertEquals(
            expected,
            KGraphicalConsoleUnitFormatter(config)
                .format(KUnitFormatContext(1.0, listOf(meter, perSecond3, perSecond2), "%.1f", Locale.US)),
        )
    }

    /** The fraction bar is wrapped in the palette bar colour. */
    @Test
    fun `fraction bar colouring`() {
        val palette = KGraphicalConsoleColorPalette("N", "S", "O", "E", "B", reset = "R")
        val config = KGraphicalConsoleFormatConfig(palette = palette)
        // number "1.0" -> indent 4; numerator "SmR" width 1, denominator "SsR" width 1, bar width 1.
        val expected = "    SmR\n" +
            "N1.0R B${bar}R\n" +
            "    SsR"
        assertEquals(
            expected,
            KGraphicalConsoleUnitFormatter(config)
                .format(KUnitFormatContext(1.0, listOf(meter, perSecond), "%.1f", Locale.US)),
        )
    }
}
