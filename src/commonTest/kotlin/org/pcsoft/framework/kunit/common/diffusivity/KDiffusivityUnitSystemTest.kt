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

package org.pcsoft.framework.kunit.common.diffusivity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.of
import kotlin.test.*

/** `KDiffusivityUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KDiffusivityUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(1.11e-4, (111 of squareMillimetersPerSecond) into squareMetersPerSecond, 1e-18)
        assertEquals(111.0, (1.11e-4 of squareMetersPerSecond) into squareMillimetersPerSecond, 1e-9)
        assertEquals(111.0, (1.11e-4 of squareMetersPerSecond) into micro.squareMetersPerSecond, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of squareMetersPerSecond, 1_000_000 of squareMillimetersPerSecond)
        assertEquals(
            (1 of squareMetersPerSecond).hashCode(),
            (1_000_000 of squareMillimetersPerSecond).hashCode(),
        )
        assertFalse((1 of squareMetersPerSecond) == (2 of squareMetersPerSecond))
        assertFalse((1 of squareMetersPerSecond).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("1.11E-4 m²/s", (111 of squareMillimetersPerSecond).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of squareMillimetersPerSecond
        val b = 4 of squareMillimetersPerSecond
        assertEquals(6.0, (a - b) into squareMillimetersPerSecond, 1e-9)
        assertEquals(14.0, (a + b) into squareMillimetersPerSecond, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical distance²·time⁻¹ mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toDiffusivity round-trip and failure`() {
        val m2 = (1 of meters).toUnit() pow 2
        val s = (1 of seconds).toUnit()
        assertEquals(1.0, (m2 / s).toDiffusivity() into squareMetersPerSecond, 1e-9)

        assertFailsWith<IllegalStateException> { m2.toDiffusivity() }
        assertFailsWith<IllegalStateException> {
            (((1 of meters).toUnit() pow 3) / s).toDiffusivity()
        }
        assertFailsWith<IllegalStateException> { (m2 / (s pow 2)).toDiffusivity() }
    }
}
