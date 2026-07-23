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

import org.pcsoft.framework.kunit.distance.hectares
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.celsius
import org.pcsoft.framework.kunit.temperature.fahrenheit
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.toString
import java.util.IllegalFormatException
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/** Surface of the `format` verb and the parameterised `toString` overload (the display-rendering path). */
class KUnitFormatTest {

    /** The infix verb renders value (full precision) and the target's prefixed symbol. */
    @Test
    fun `format infix renders prefixed symbol`() {
        assertEquals("1.5 km", (1500 of meters) format kilo.meters)
    }

    /** The headline case: read a speed into km/h and render it with a rounding pattern. */
    @Test
    fun `format speed into kilometers per hour`() {
        val v = 3 of meters / seconds
        assertEquals("10.8 km/h", v.format(kilo.meters / hours, "%.1f", Locale.US))
    }

    /** An exponent other than 1 is appended as `^n`. */
    @Test
    fun `format renders exponent`() {
        assertEquals("20000.0 m^2", (2 of hectares) format (meters * meters))
    }

    /** The affine temperature group renders correctly: the value is converted first, then the °F symbol. */
    @Test
    fun `format affine temperature`() {
        assertEquals("77.0 °F", (25 of celsius) format fahrenheit)
    }

    /** The locale drives the decimal separator of the pattern. */
    @Test
    fun `format with pattern and locale`() {
        val v = 3 of meters / seconds
        assertEquals("10,8 km/h", v.format(kilo.meters / hours, "%.1f", Locale.GERMAN))
    }

    /** `toString(pattern)` renders the value's own base units with a formatted number. */
    @Test
    fun `toString with pattern on mixed unit`() {
        assertEquals("3.00 m/s", (3 of meters / seconds).toString("%.2f", Locale.US))
    }

    /** `toString(pattern)` is available on the typed wrappers too (via the KUnitMeasurable extension). */
    @Test
    fun `toString with pattern on typed unit`() {
        assertEquals("1500.0 m", (1500 of meters).toString("%.1f", Locale.US))
    }

    /** The no-argument `toString` is unchanged by the formatting feature. */
    @Test
    fun `plain toString unchanged`() {
        assertEquals("1500.0 m", (1500 of meters).toString())
    }

    /** An incompatible target dimension fails, exactly like `into`. */
    @Test
    fun `format incompatible dimension fails`() {
        assertFailsWith<IllegalStateException> { (1 of meters) format seconds }
    }

    /** An invalid number pattern surfaces the underlying formatter exception. */
    @Test
    fun `format invalid pattern fails`() {
        assertFailsWith<IllegalFormatException> { (1 of meters).format(meters, "%.1d") }
    }
}
