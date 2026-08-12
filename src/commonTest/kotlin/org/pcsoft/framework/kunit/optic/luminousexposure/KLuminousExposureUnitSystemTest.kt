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

package org.pcsoft.framework.kunit.optic.luminousexposure

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import kotlin.test.*

/** `KLuminousExposureUnitInstance` surface: round-trip, equality, `toString`, operators, conversion. */
class KLuminousExposureUnitSystemTest {

    @Test
    fun `construction and round-trip`() {
        assertEquals(3600.0, (1 of luxHours) into luxSeconds, 1e-9)
        assertEquals(1.0, (3600 of luxSeconds) into luxHours, 1e-12)
        assertEquals(400.0, (400 of luxHours) into luxHours, 1e-9)
    }

    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of luxHours, 3600 of luxSeconds)
        assertEquals((1 of luxHours).hashCode(), (3600 of luxSeconds).hashCode())
        assertFalse((1 of luxSeconds) == (2 of luxSeconds))
        assertFalse((1 of luxSeconds).equals(1.0))
    }

    @Test
    fun `toString base unit`() {
        assertEquals("3600.0 lx*s", (1 of luxHours).toString())
    }

    @Test
    fun `same-type operators`() {
        val a = 10 of luxSeconds
        val b = 4 of luxSeconds
        assertEquals(14.0, (a + b) into luxSeconds, 1e-9)
        assertEquals(6.0, (a - b) into luxSeconds, 1e-9)
        assertTrue(a > b)
        assertIs<KMixedUnitInstance>(a * b)
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** A canonical cd·sr·m⁻²·s mixed unit converts back; wrong shapes fail. */
    @Test
    fun `toLuminousExposure round-trip and failure`() {
        val m2 = (1 of meters).toUnit() pow 2
        val raw = (1 of candelas).toUnit() * (1 of steradians).toUnit() / m2 * (1 of seconds).toUnit()
        assertEquals(1.0, raw.toLuminousExposure() into luxSeconds, 1e-9)

        // An equivalent expression written in hours reduces onto the same normal form.
        val rawHours = (1 of candelas).toUnit() * (1 of steradians).toUnit() / m2 * (1 of hours).toUnit()
        assertEquals(1.0, rawHours.toLuminousExposure() into luxHours, 1e-9)

        val sr = (1 of steradians).toUnit()
        val s = (1 of seconds).toUnit()
        assertFailsWith<IllegalStateException> {
            ((1 of candelas).toUnit() * sr * s).toLuminousExposure()
        }
        assertFailsWith<IllegalStateException> {
            ((1 of candelas).toUnit() * sr / m2).toLuminousExposure()
        }
    }
}
