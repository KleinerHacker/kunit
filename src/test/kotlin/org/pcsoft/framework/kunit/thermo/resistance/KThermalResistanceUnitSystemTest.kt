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

package org.pcsoft.framework.kunit.thermo.resistance

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

/** `KThermalResistanceUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KThermalResistanceUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(5.0, (5 of squareMeterKelvinPerWatt) into squareMeterKelvinPerWatt, 1e-12)
        assertEquals(0.005, (5 of squareMeterKelvinPerWatt) into kilo.squareMeterKelvinPerWatt, 1e-15)
        assertEquals(
            30.0,
            (30.0 / 5.678263341113489 of squareMeterKelvinPerWatt) into hourSquareFootFahrenheitPerBtu,
            1e-9,
        )
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of squareMeterKelvinPerWatt, 10 of tog)
        assertEquals((1 of squareMeterKelvinPerWatt).hashCode(), (10 of tog).hashCode())
        assertFalse((1 of squareMeterKelvinPerWatt) == (2 of squareMeterKelvinPerWatt))
        assertFalse((1 of squareMeterKelvinPerWatt).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("5.0 m²·K/W", (5 of squareMeterKelvinPerWatt).toString())
    }

    /** Layers in series simply add their R-values - the physically meaningful same-type operator. */
    @Test
    fun `same-type operators`() {
        val a = 10 of squareMeterKelvinPerWatt
        val b = 4 of squareMeterKelvinPerWatt
        assertEquals(6.0, (a - b) into squareMeterKelvinPerWatt, 1e-9)
        assertEquals(14.0, (a + b) into squareMeterKelvinPerWatt, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical mass⁻¹·time³·temperature mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toThermalResistance round-trip and failure`() {
        val kelvin = KTemperatureDifference.ofKelvin(1).toUnit()
        val g = (1000 of grams).toUnit()
        val s3 = (1 of seconds).toUnit() pow 3
        assertEquals(
            1.0,
            (s3 * kelvin / g).toThermalResistance() into squareMeterKelvinPerWatt,
            1e-9,
        )

        assertFailsWith<IllegalStateException> { g.toThermalResistance() }
        assertFailsWith<IllegalStateException> { (s3 * kelvin / (g pow 2)).toThermalResistance() }
        assertFailsWith<IllegalStateException> {
            (((1 of seconds).toUnit() pow 2) * kelvin / g).toThermalResistance()
        }
        assertFailsWith<IllegalStateException> { (s3 * (kelvin pow 2) / g).toThermalResistance() }
    }
}
