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

package org.pcsoft.framework.kunit.thermo.heatfluxdensity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import kotlin.test.*

/** `KHeatFluxDensityUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KHeatFluxDensityUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(1361.0, (1361 of wattsPerSquareMeter) into wattsPerSquareMeter, 1e-9)
        assertEquals(1.361, (1361 of wattsPerSquareMeter) into kilo.wattsPerSquareMeter, 1e-9)
        assertEquals(1.0, (41840 of wattsPerSquareMeter) into caloriesPerSecondSquareCentimeter, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilo.wattsPerSquareMeter, 1000 of wattsPerSquareMeter)
        assertEquals((1 of kilo.wattsPerSquareMeter).hashCode(), (1000 of wattsPerSquareMeter).hashCode())
        assertFalse((1 of wattsPerSquareMeter) == (2 of wattsPerSquareMeter))
        assertFalse((1 of wattsPerSquareMeter).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("1361.0 W/m²", (1361 of wattsPerSquareMeter).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of wattsPerSquareMeter
        val b = 4 of wattsPerSquareMeter
        assertEquals(6.0, (a - b) into wattsPerSquareMeter, 1e-9)
        assertEquals(14.0, (a + b) into wattsPerSquareMeter, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical mass·time⁻³ mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toHeatFluxDensity round-trip and failure`() {
        val g = (1000 of grams).toUnit()
        val s3 = (1 of seconds).toUnit() pow 3
        assertEquals(1.0, (g / s3).toHeatFluxDensity() into wattsPerSquareMeter, 1e-9) // 1 kg/s³ = 1 W/m²

        assertFailsWith<IllegalStateException> { g.toHeatFluxDensity() }
        assertFailsWith<IllegalStateException> { ((g pow 2) / s3).toHeatFluxDensity() }
        assertFailsWith<IllegalStateException> { (g / ((1 of seconds).toUnit() pow 2)).toHeatFluxDensity() }
    }
}
