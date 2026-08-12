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
import kotlin.test.assertFailsWith

/**
 * The multiplatform number formatter: every supported conversion, flag and error case, plus the portable
 * unformatted rendering that replaces the platform-dependent `Double.toString()`.
 */
class KNumberFormatTest {

    private fun format(value: Double, pattern: String, locale: KLocale = KLocale.ROOT) =
        formatNumber(value, pattern, locale)

    // --- conversions -------------------------------------------------------------------------

    /** `%f` renders a fixed number of fraction digits, rounded half-up. */
    @Test
    fun `fixed conversion`() {
        assertEquals("1.50", format(1.5, "%.2f"))
        assertEquals("10.8", format(10.8, "%.1f"))
        assertEquals("2", format(1.5, "%.0f"))
        assertEquals("1.235", format(1.23456, "%.3f"))
        assertEquals("3.000000", format(3.0, "%f"))
    }

    /** `%e`/`%E` render the scientific form with an at-least-two-digit signed exponent. */
    @Test
    fun `scientific conversion`() {
        assertEquals("1.500e+00", format(1.5, "%.3e"))
        assertEquals("1.5E+03", format(1500.0, "%.1E"))
        assertEquals("5.4e-05", format(5.4e-5, "%.1e"))
        assertEquals("0.000000e+00", format(0.0, "%e"))
        assertEquals("2e+00", format(1.5, "%.0e"))
    }

    /** Rounding the mantissa up to 10 shifts the exponent. */
    @Test
    fun `scientific conversion carries the exponent`() {
        assertEquals("1.0e+01", format(9.99, "%.1e"))
        assertEquals("9.99e-01", format(0.999, "%.2e"))
        assertEquals("1.00e+00", format(0.9999, "%.2e"))
        assertEquals("1.000e+100", format(9.9999e99, "%.3e"))
    }

    /** More precision than the value has digits pads with zeros; less rounds half-up. */
    @Test
    fun `scientific conversion padding and rounding`() {
        assertEquals("1.5000000e+00", format(1.5, "%.7e"))
        assertEquals("1.2e+00", format(1.24, "%.1e"))
        assertEquals("1.3e+00", format(1.25, "%.1e"))
    }

    /** `%d` rounds to a whole number; `%s` is the portable plain rendering. */
    @Test
    fun `integer and string conversion`() {
        assertEquals("5", format(4.6, "%d"))
        assertEquals("-5", format(-4.6, "%d"))
        assertEquals("1.5", format(1.5, "%s"))
        assertEquals("2.0", format(2.0, "%s"))
    }

    // --- flags -------------------------------------------------------------------------------

    /** The `,` flag groups the integer digits under the locale's conventions. */
    @Test
    fun `grouping flag`() {
        assertEquals("1,234,567.00", format(1234567.0, "%,.2f", KLocale.EN_US))
        assertEquals("1.234.567,00", format(1234567.0, "%,.2f", KLocale.DE_DE))
        assertEquals("12,34,567.00", format(1234567.0, "%,.2f", KLocale.HI_IN))
        assertEquals("1,234", format(1234.0, "%,d"))
        assertEquals("123.00", format(123.0, "%,.2f"))
    }

    /** The sign flags `+` and space apply to non-negative values only. */
    @Test
    fun `sign flags`() {
        assertEquals("+1.5", format(1.5, "%+.1f"))
        assertEquals("-1.5", format(-1.5, "%+.1f"))
        assertEquals(" 1.5", format(1.5, "% .1f"))
        assertEquals("-1.5", format(-1.5, "% .1f"))
        assertEquals("0.0", format(0.0, "%.1f"))
        assertEquals("-0.0", format(-0.0, "%.1f"))
    }

    /** Width pads left by default, right with `-`, and with zeros after the sign with `0`. */
    @Test
    fun `width and justification flags`() {
        assertEquals("   1.5", format(1.5, "%6.1f"))
        assertEquals("1.5   ", format(1.5, "%-6.1f"))
        assertEquals("0001.5", format(1.5, "%06.1f"))
        assertEquals("-001.5", format(-1.5, "%06.1f"))
        assertEquals("1.5", format(1.5, "%2.1f"))
    }

    // --- literals and special values ---------------------------------------------------------

    /** Literal text is copied verbatim and `%%` emits a percent sign. */
    @Test
    fun `literals`() {
        assertEquals("~1.5~", format(1.5, "~%.1f~"))
        assertEquals("50.0%", format(50.0, "%.1f%%"))
    }

    /** Non-finite values render by name and are never zero-padded. */
    @Test
    fun `non finite values`() {
        assertEquals("NaN", format(Double.NaN, "%.2f"))
        assertEquals("       NaN", format(Double.NaN, "%010.2f"))
        assertEquals("Infinity", format(Double.POSITIVE_INFINITY, "%.2f"))
        assertEquals("-Infinity", format(Double.NEGATIVE_INFINITY, "%.2f"))
        assertEquals("+Infinity", format(Double.POSITIVE_INFINITY, "%+.2f"))
    }

    /** Magnitudes beyond Double's exact integer range fall back to the round-trip digits. */
    @Test
    fun `very large magnitudes`() {
        assertEquals("602200000000000000000000.00", format(6.022e23, "%.2f"))
        assertEquals("1000000000000000000000000", format(1e24, "%d"))
        assertEquals("1,000,000,000,000,000,000,000,000", format(1e24, "%,d"))
    }

    /**
     * A precision beyond Double's own resolution takes the same fallback: the integer digits are exact,
     * the surplus fraction digits are zeros.
     */
    @Test
    fun `precision beyond double resolution`() {
        assertEquals("1.0000000000000000000", format(1.5, "%.19f"))
        assertEquals("0.0000000000000000000", format(0.5, "%.19f"))
        assertEquals("0.0000000000000000000", format(0.0, "%.19f"))
    }

    // --- error cases -------------------------------------------------------------------------

    /** Every malformed pattern raises IllegalArgumentException. */
    @Test
    fun `invalid patterns`() {
        assertFailsWith<IllegalArgumentException> { format(1.0, "%.1d") }
        assertFailsWith<IllegalArgumentException> { format(1.0, "%.1s") }
        assertFailsWith<IllegalArgumentException> { format(1.0, "%.1x") }
        assertFailsWith<IllegalArgumentException> { format(1.0, "%,.1e") }
        assertFailsWith<IllegalArgumentException> { format(1.0, "%,.1E") }
        assertFailsWith<IllegalArgumentException> { format(1.0, "%-") }
        assertFailsWith<IllegalArgumentException> { format(1.0, "%5") }
        assertFailsWith<IllegalArgumentException> { format(1.0, "%.f") }
        assertFailsWith<IllegalArgumentException> { format(1.0, "%.1f %.1f") }
        assertFailsWith<IllegalArgumentException> { format(1.0, "no conversion") }
        assertFailsWith<IllegalArgumentException> { format(1.0, "trailing %") }
        assertFailsWith<IllegalArgumentException> { format(1.0, "%.1") }
    }

    // --- portable unformatted rendering ------------------------------------------------------

    /** The plain form keeps at least one fraction digit and stays in decimal notation up to 1e7. */
    @Test
    fun `render double plain`() {
        assertEquals("0.0", renderDouble(0.0))
        assertEquals("-0.0", renderDouble(-0.0))
        assertEquals("5.0", renderDouble(5.0))
        assertEquals("1500.0", renderDouble(1500.0))
        assertEquals("10.8", renderDouble(10.8))
        assertEquals("-2.5", renderDouble(-2.5))
        assertEquals("0.001", renderDouble(0.001))
        assertEquals("9999999.0", renderDouble(9999999.0))
    }

    /** Outside `1e-3 .. 1e7` the scientific form is used, exactly as on the JVM. */
    @Test
    fun `render double scientific`() {
        assertEquals("1.0E7", renderDouble(1e7))
        assertEquals("1.0E-4", renderDouble(1e-4))
        assertEquals("5.4E-5", renderDouble(5.4e-5))
        assertEquals("1.0E21", renderDouble(1e21))
        assertEquals("-1.0E21", renderDouble(-1e21))
        assertEquals("1.234E8", renderDouble(1.234e8))
    }

    /** Non-finite values render by name. */
    @Test
    fun `render double non finite`() {
        assertEquals("NaN", renderDouble(Double.NaN))
        assertEquals("Infinity", renderDouble(Double.POSITIVE_INFINITY))
        assertEquals("-Infinity", renderDouble(Double.NEGATIVE_INFINITY))
    }
}
