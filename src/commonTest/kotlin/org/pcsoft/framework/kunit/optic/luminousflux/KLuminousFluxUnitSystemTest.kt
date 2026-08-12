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

package org.pcsoft.framework.kunit.optic.luminousflux

import kotlin.math.PI
import kotlin.test.*
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.solidangle.spats
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas

/** `KLuminousFluxUnitInstance` surface: round-trip, equality, `toString`, operators, `toLuminousFlux`. */
class KLuminousFluxUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(800.0, (800 of lumens) into lumens, 1e-9)
        assertEquals(0.8, (800 of lumens) into kilo.lumens, 1e-9)
        assertEquals(800.0, (800 of candelaSteradians) into lumens, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of lumens, 1000 of milli.lumens)
        assertEquals((1 of lumens).hashCode(), (1000 of milli.lumens).hashCode())
        assertFalse((1 of lumens) == (2 of lumens))
        assertFalse((1 of lumens).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("800.0 lm", (800 of lumens).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of lumens
        val b = 4 of lumens
        assertEquals(14.0, (a + b) into lumens, 1e-9)
        assertEquals(6.0, (a - b) into lumens, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical luminousIntensity·solidAngle mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toLuminousFlux round-trip and failure`() {
        val raw = (1 of candelas).toUnit() * (1 of steradians).toUnit()
        assertEquals(1.0, raw.toLuminousFlux() into lumens, 1e-9)

        // An equivalent expression tagged with a non-base solid angle reduces onto the same normal form.
        val rawSpat = (1 of candelas).toUnit() * (1 of spats).toUnit()
        assertEquals(4.0 * PI, rawSpat.toLuminousFlux() into lumens, 1e-9)

        val sr = (1 of steradians).toUnit()
        assertFailsWith<IllegalStateException> { (1 of candelas).toUnit().toLuminousFlux() }
        assertFailsWith<IllegalStateException> { ((1 of candelas).toUnit() / sr).toLuminousFlux() }
        assertFailsWith<IllegalStateException> {
            ((1 of candelas).toUnit() * sr * (1 of meters).toUnit()).toLuminousFlux()
        }
    }
}
