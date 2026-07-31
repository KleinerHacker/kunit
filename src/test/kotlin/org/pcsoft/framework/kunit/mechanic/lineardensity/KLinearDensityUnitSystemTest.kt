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

package org.pcsoft.framework.kunit.mechanic.lineardensity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import kotlin.test.*

/** `KLinearDensityUnitInstance` surface: construction, equality, `toString`, operators, `toLinearDensity`. */
class KLinearDensityUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(0.5, (0.5 of kilogramsPerMeter) into kilogramsPerMeter, 1e-9)
        assertEquals(500.0, (0.5 of kilogramsPerMeter) into gramsPerMeter, 1e-6)
        assertEquals(500000.0, (0.5 of kilogramsPerMeter) into tex, 1e-3)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of kilogramsPerMeter, 1000 of gramsPerMeter)
        assertEquals((1 of kilogramsPerMeter).hashCode(), (1000 of gramsPerMeter).hashCode())
        assertEquals(1000 of kilogramsPerMeter, 1 of kilo.kilogramsPerMeter)
        assertFalse((1 of kilogramsPerMeter) == (2 of kilogramsPerMeter))
        assertFalse((1 of kilogramsPerMeter).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("0.5 kg/m", (0.5 of kilogramsPerMeter).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of kilogramsPerMeter
        val b = 4 of kilogramsPerMeter
        assertEquals(14.0, (a + b) into kilogramsPerMeter, 1e-9)
        assertEquals(6.0, (a - b) into kilogramsPerMeter, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    @Test
    fun `toLinearDensity round-trip and failure`() {
        val raw = (2000 of grams).toUnit() / (4 of meters).toUnit()
        assertEquals(0.5, raw.toLinearDensity() into kilogramsPerMeter, 1e-9)
        assertFailsWith<IllegalStateException> { (1000 of grams).toUnit().toLinearDensity() }
        assertFailsWith<IllegalStateException> {
            ((1000 of grams).toUnit() / (1 of seconds).toUnit()).toLinearDensity()
        }
        assertFailsWith<IllegalStateException> {
            ((1000 of grams).toUnit() / (1 of meters).toUnit() / (1 of seconds).toUnit()).toLinearDensity()
        }
    }
}
