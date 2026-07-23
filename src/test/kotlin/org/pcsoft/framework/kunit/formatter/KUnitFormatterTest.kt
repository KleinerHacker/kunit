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

import org.pcsoft.framework.kunit.KUnitDisplay
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.toString
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

/** A minimal LaTeX-style formatter proving the [KUnitFormatter] extension point and its context. */
private object LatexFormatter : KUnitFormatter {
    override fun format(context: KUnitFormatContext): String {
        val (numerator, denominator) = context.units.partition { it.exponent > 0 }
        fun render(terms: List<KUnitTerm>) = terms.joinToString(" ") { "\\mathrm{${it.displaySymbol}}" }
        val value = context.renderValue()
        if (denominator.isEmpty()) return "$value\\,${render(numerator)}".trim()
        return "$value\\,\\frac{${render(numerator)}}{${render(denominator.map { it.copy(exponent = -it.exponent) })}}"
    }
}

/** The extension point: a custom formatter receives the full context (value, prefixed/exponent terms). */
class KUnitFormatterTest {

    /** A custom formatter is honoured by the `format` verb and sees the target's display metadata. */
    @Test
    fun `custom formatter via format verb`() {
        val v = 3 of meters / seconds
        assertEquals(
            "10.8\\,\\frac{\\mathrm{km}}{\\mathrm{h}}",
            v.format(kilo.meters / hours, "%.1f", Locale.US, LatexFormatter),
        )
    }

    /** A custom formatter is honoured by the `toString` overload too. */
    @Test
    fun `custom formatter via toString`() {
        assertEquals("5.0\\,\\mathrm{m}", (5 of meters).toString(pattern = null, formatter = LatexFormatter))
    }

    /** The reusable helpers behave as documented. */
    @Test
    fun `context helpers`() {
        val term = KUnitTerm(KDistanceUnit.BASE, 1, KUnitDisplay(KDistanceUnit.METER, "k"))
        assertEquals("km", term.displaySymbol)
        assertEquals("m", KUnitTerm(KDistanceUnit.BASE, 1).displaySymbol)
        assertEquals("2.0", KUnitFormatContext(2.0, emptyList()).renderValue())
        assertEquals("2.00", KUnitFormatContext(2.0, emptyList(), pattern = "%.2f", locale = Locale.US).renderValue())
    }

    /** [KUnitDisplay.symbol] with and without a prefix. */
    @Test
    fun `display symbol`() {
        assertEquals("km", KUnitDisplay(KDistanceUnit.METER, "k").symbol)
        assertEquals("m", KUnitDisplay(KDistanceUnit.METER).symbol)
    }
}
