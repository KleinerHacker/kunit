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

package org.pcsoft.framework.kunit.thermo.specificheatcapacity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** `KSpecificHeatCapacityUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KSpecificHeatCapacityUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(4184.0, (4184 of joulesPerKilogramKelvin) into joulesPerKilogramKelvin, 1e-9)
        assertEquals(1.0, (4184 of joulesPerKilogramKelvin) into caloriesPerGramKelvin, 1e-9)
        assertEquals(4.184, (4184 of joulesPerKilogramKelvin) into kilo.joulesPerKilogramKelvin, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilo.joulesPerKilogramKelvin, 1000 of joulesPerKilogramKelvin)
        assertEquals(
            (1 of kilo.joulesPerKilogramKelvin).hashCode(),
            (1000 of joulesPerKilogramKelvin).hashCode(),
        )
        assertFalse((1 of joulesPerKilogramKelvin) == (2 of joulesPerKilogramKelvin))
        assertFalse((1 of joulesPerKilogramKelvin).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("4184.0 J/(kg·K)", (4184 of joulesPerKilogramKelvin).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of joulesPerKilogramKelvin
        val b = 4 of joulesPerKilogramKelvin
        assertEquals(6.0, (a - b) into joulesPerKilogramKelvin, 1e-9)
        assertEquals(14.0, (a + b) into joulesPerKilogramKelvin, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical distance²·time⁻²·temperature⁻¹ mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toSpecificHeatCapacity round-trip and failure`() {
        val kelvin = KTemperatureDifference.ofKelvin(1).toUnit()
        val raw = ((1 of meters).toUnit() pow 2) / ((1 of seconds).toUnit() pow 2) / kelvin
        assertEquals(1.0, raw.toSpecificHeatCapacity() into joulesPerKilogramKelvin, 1e-9)

        assertFailsWith<IllegalStateException> { (1 of meters).toUnit().toSpecificHeatCapacity() }
        val m2 = (1 of meters).toUnit() pow 2
        val s2 = (1 of seconds).toUnit() pow 2
        assertFailsWith<IllegalStateException> {
            (((1 of meters).toUnit() pow 3) / s2 / kelvin).toSpecificHeatCapacity()
        }
        assertFailsWith<IllegalStateException> {
            (m2 / ((1 of seconds).toUnit() pow 3) / kelvin).toSpecificHeatCapacity()
        }
        assertFailsWith<IllegalStateException> { (m2 / s2 / (kelvin pow 2)).toSpecificHeatCapacity() }
    }
}
