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

package org.pcsoft.framework.kunit.optic.luminousenergy

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import kotlin.test.*

/** `KLuminousEnergyUnitInstance` surface: round-trip, equality, `toString`, operators, `toLuminousEnergy`. */
class KLuminousEnergyUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(3600.0, (1 of lumenHours) into lumenSeconds, 1e-9)
        assertEquals(1.0, (3600 of lumenSeconds) into lumenHours, 1e-12)
        assertEquals(1.0, (1000 of milli.lumenSeconds) into lumenSeconds, 1e-12)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of lumenHours, 3600 of lumenSeconds)
        assertEquals((1 of lumenHours).hashCode(), (3600 of lumenSeconds).hashCode())
        assertFalse((1 of lumenSeconds) == (2 of lumenSeconds))
        assertFalse((1 of lumenSeconds).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("3600.0 lm*s", (1 of lumenHours).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of lumenSeconds
        val b = 4 of lumenSeconds
        assertEquals(14.0, (a + b) into lumenSeconds, 1e-9)
        assertEquals(6.0, (a - b) into lumenSeconds, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical luminousIntensity·solidAngle·time mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toLuminousEnergy round-trip and failure`() {
        val raw = (1 of candelas).toUnit() * (1 of steradians).toUnit() * (1 of seconds).toUnit()
        assertEquals(1.0, raw.toLuminousEnergy() into lumenSeconds, 1e-9)

        // An equivalent expression written in hours reduces onto the same normal form.
        val rawHours = (1 of candelas).toUnit() * (1 of steradians).toUnit() * (1 of hours).toUnit()
        assertEquals(1.0, rawHours.toLuminousEnergy() into lumenHours, 1e-9)

        val sr = (1 of steradians).toUnit()
        val s = (1 of seconds).toUnit()
        assertFailsWith<IllegalStateException> { ((1 of candelas).toUnit() * sr).toLuminousEnergy() }
        assertFailsWith<IllegalStateException> { ((1 of candelas).toUnit() * sr / s).toLuminousEnergy() }
        assertFailsWith<IllegalStateException> {
            ((1 of candelas).toUnit() * sr * s * (1 of meters).toUnit()).toLuminousEnergy()
        }
    }
}
