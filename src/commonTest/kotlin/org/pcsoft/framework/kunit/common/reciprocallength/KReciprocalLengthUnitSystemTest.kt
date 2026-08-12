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

package org.pcsoft.framework.kunit.common.reciprocallength

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import kotlin.test.*

/** `KReciprocalLengthUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KReciprocalLengthUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(2.5, (2.5 of dioptres) into reciprocalMeters, 1e-12)
        assertEquals(100.0, (1 of reciprocalCentimeters) into reciprocalMeters, 1e-9)
        assertEquals(1.0, (100 of reciprocalMeters) into reciprocalCentimeters, 1e-12)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of reciprocalCentimeters, 100 of reciprocalMeters)
        assertEquals((1 of reciprocalCentimeters).hashCode(), (100 of reciprocalMeters).hashCode())
        assertFalse((1 of dioptres) == (2 of dioptres))
        assertFalse((1 of dioptres).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("2.5 1/m", (2.5 of dioptres).toString())
    }

    /** Thin lenses in contact add their powers - that is exactly the same-type `+`. */
    @Test
    fun `same-type operators`() {
        val a = 2.5 of dioptres
        val b = 1.5 of dioptres
        assertEquals(4.0, (a + b) into dioptres, 1e-9)
        assertEquals(1.0, (a - b) into dioptres, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical distance⁻¹ mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toReciprocalLength round-trip and failure`() {
        val raw = (1 of meters).toUnit() pow -1
        assertEquals(1.0, raw.toReciprocalLength() into reciprocalMeters, 1e-9)

        // An equivalent expression in centimeters reduces onto the same normal form.
        val rawCm = (1 of centi.meters).toUnit() pow -1
        assertEquals(1.0, rawCm.toReciprocalLength() into reciprocalCentimeters, 1e-9)

        assertFailsWith<IllegalStateException> { (1 of meters).toUnit().toReciprocalLength() }
        assertFailsWith<IllegalStateException> { ((1 of meters).toUnit() pow -2).toReciprocalLength() }
        assertFailsWith<IllegalStateException> {
            (((1 of meters).toUnit() pow -1) / (1 of seconds).toUnit()).toReciprocalLength()
        }
    }
}
