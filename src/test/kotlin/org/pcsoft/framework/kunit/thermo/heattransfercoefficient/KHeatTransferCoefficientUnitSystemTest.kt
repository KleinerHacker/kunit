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

package org.pcsoft.framework.kunit.thermo.heattransfercoefficient

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** `KHeatTransferCoefficientUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KHeatTransferCoefficientUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(1.3, (1.3 of wattsPerSquareMeterKelvin) into wattsPerSquareMeterKelvin, 1e-12)
        assertEquals(1300.0, (1.3 of wattsPerSquareMeterKelvin) into milli.wattsPerSquareMeterKelvin, 1e-9)
        assertEquals(0.0013, (1.3 of wattsPerSquareMeterKelvin) into kilo.wattsPerSquareMeterKelvin, 1e-12)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilo.wattsPerSquareMeterKelvin, 1000 of wattsPerSquareMeterKelvin)
        assertEquals(
            (1 of kilo.wattsPerSquareMeterKelvin).hashCode(),
            (1000 of wattsPerSquareMeterKelvin).hashCode(),
        )
        assertFalse((1 of wattsPerSquareMeterKelvin) == (2 of wattsPerSquareMeterKelvin))
        assertFalse((1 of wattsPerSquareMeterKelvin).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("1.3 W/(m²·K)", (1.3 of wattsPerSquareMeterKelvin).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of wattsPerSquareMeterKelvin
        val b = 4 of wattsPerSquareMeterKelvin
        assertEquals(6.0, (a - b) into wattsPerSquareMeterKelvin, 1e-9)
        assertEquals(14.0, (a + b) into wattsPerSquareMeterKelvin, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical mass·time⁻³·temperature⁻¹ mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toHeatTransferCoefficient round-trip and failure`() {
        val kelvin = KTemperatureDifference.ofKelvin(1).toUnit()
        val g = (1000 of grams).toUnit()
        val s3 = (1 of seconds).toUnit() pow 3
        assertEquals(
            1.0,
            (g / s3 / kelvin).toHeatTransferCoefficient() into wattsPerSquareMeterKelvin,
            1e-9,
        )

        assertFailsWith<IllegalStateException> { g.toHeatTransferCoefficient() }
        assertFailsWith<IllegalStateException> { ((g pow 2) / s3 / kelvin).toHeatTransferCoefficient() }
        assertFailsWith<IllegalStateException> {
            (g / ((1 of seconds).toUnit() pow 2) / kelvin).toHeatTransferCoefficient()
        }
        assertFailsWith<IllegalStateException> { (g / s3 / (kelvin pow 2)).toHeatTransferCoefficient() }
    }
}
