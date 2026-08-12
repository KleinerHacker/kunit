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

package org.pcsoft.framework.kunit.electric.magneticmoment

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import kotlin.test.*

/** `KMagneticMomentUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KMagneticMomentUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(0.01, (0.01 of ampereSquareMeters) into ampereSquareMeters, 1e-12)
        assertEquals(10.0, (0.01 of ampereSquareMeters) into milli.ampereSquareMeters, 1e-9)
        assertEquals(1.0, (9.2740100783e-24 of ampereSquareMeters) into bohrMagnetons, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of ampereSquareMeters, 1000 of milli.ampereSquareMeters)
        assertEquals(
            (1 of ampereSquareMeters).hashCode(),
            (1000 of milli.ampereSquareMeters).hashCode(),
        )
        assertFalse((1 of ampereSquareMeters) == (2 of ampereSquareMeters))
        assertFalse((1 of ampereSquareMeters).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("0.01 A*m^2", (0.01 of ampereSquareMeters).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of ampereSquareMeters
        val b = 4 of ampereSquareMeters
        assertEquals(14.0, (a + b) into ampereSquareMeters, 1e-9)
        assertEquals(6.0, (a - b) into ampereSquareMeters, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical current·distance² mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toMagneticMoment round-trip and failure`() {
        val raw = (1 of amperes).toUnit() * ((1 of meters).toUnit() pow 2)
        assertEquals(1.0, raw.toMagneticMoment() into ampereSquareMeters, 1e-9)

        // An equivalent expression in centimeters reduces onto the same normal form.
        val rawCm = (1 of amperes).toUnit() * ((1 of centi.meters).toUnit() pow 2)
        assertEquals(1.0e-4, rawCm.toMagneticMoment() into ampereSquareMeters, 1e-12)

        assertFailsWith<IllegalStateException> { (1 of amperes).toUnit().toMagneticMoment() }
        assertFailsWith<IllegalStateException> {
            ((1 of amperes).toUnit() * ((1 of meters).toUnit() pow 3)).toMagneticMoment()
        }
        assertFailsWith<IllegalStateException> {
            ((1 of amperes).toUnit() * ((1 of meters).toUnit() pow 2) / (1 of seconds).toUnit())
                .toMagneticMoment()
        }
    }
}
