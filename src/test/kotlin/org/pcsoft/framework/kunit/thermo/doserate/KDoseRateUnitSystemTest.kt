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

package org.pcsoft.framework.kunit.thermo.doserate

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds
import kotlin.test.*

/** `KDoseRateUnitInstance` surface: round-trip, equality, `toString`, operators, `toDoseRate`. */
class KDoseRateUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(1.0, (1 of graysPerSecond) into graysPerSecond, 1e-12)
        assertEquals(3600.0, (1 of graysPerSecond) into graysPerHour, 1e-9)
        assertEquals(1.0, (3600 of graysPerHour) into graysPerSecond, 1e-12)
        assertEquals(0.1, (0.1 of micro.sievertsPerHour) into micro.sievertsPerHour, 1e-12)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of graysPerHour, 1 of sievertsPerHour)
        assertEquals((1 of graysPerHour).hashCode(), (1 of sievertsPerHour).hashCode())
        assertFalse((1 of graysPerHour) == (2 of graysPerHour))
        assertFalse((1 of graysPerHour).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("1.0 Gy/s", (1 of graysPerSecond).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of micro.sievertsPerHour
        val b = 4 of micro.sievertsPerHour
        assertEquals(14.0, (a + b) into micro.sievertsPerHour, 1e-9)
        assertEquals(6.0, (a - b) into micro.sievertsPerHour, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical distance²·time⁻³ mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toDoseRate round-trip and failure`() {
        val raw = ((1 of meters).toUnit() pow 2) / ((1 of seconds).toUnit() pow 3)
        assertEquals(1.0, raw.toDoseRate() into graysPerSecond, 1e-9)

        // An equivalent expression written per hour reduces onto the same normal form.
        val rawHour = ((1 of meters).toUnit() pow 2) / ((1 of hours).toUnit() pow 3)
        assertEquals(1.0 / (3600.0 * 3600.0), rawHour.toDoseRate() into graysPerHour, 1e-12)

        val s = (1 of seconds).toUnit()
        assertFailsWith<IllegalStateException> { (1 of meters).toUnit().toDoseRate() }
        assertFailsWith<IllegalStateException> {
            (((1 of meters).toUnit() pow 2) / (s pow 2)).toDoseRate()
        }
    }
}
