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

package org.pcsoft.framework.kunit.thermo.catalyticactivity

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import kotlin.test.*

/** `KCatalyticActivityUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KCatalyticActivityUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(1.0, (1 of katals) into katals, 1e-12)
        assertEquals(60_000_000.0, (1 of katals) into enzymeUnits, 1e-3)
        assertEquals(1.0, (60_000_000 of enzymeUnits) into katals, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of katals, 1000 of milli.katals)
        assertEquals((1 of katals).hashCode(), (1000 of milli.katals).hashCode())
        assertFalse((1 of katals) == (2 of katals))
        assertFalse((1 of katals).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("1.0 kat", (1 of katals).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of katals
        val b = 4 of katals
        assertEquals(14.0, (a + b) into katals, 1e-9)
        assertEquals(6.0, (a - b) into katals, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical substance·time⁻¹ mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toCatalyticActivity round-trip and failure`() {
        val raw = (1 of moles).toUnit() / (1 of seconds).toUnit()
        assertEquals(1.0, raw.toCatalyticActivity() into katals, 1e-9)

        // An equivalent expression per minute reduces onto the same normal form: 1 µmol/min = 1 U.
        val rawUnit = (1 of micro.moles).toUnit() / (1 of minutes).toUnit()
        assertEquals(1.0, rawUnit.toCatalyticActivity() into enzymeUnits, 1e-9)

        assertFailsWith<IllegalStateException> { (1 of moles).toUnit().toCatalyticActivity() }
        assertFailsWith<IllegalStateException> {
            ((1 of moles).toUnit() / (1 of seconds).toUnit() / (1 of meters).toUnit()).toCatalyticActivity()
        }
    }
}
