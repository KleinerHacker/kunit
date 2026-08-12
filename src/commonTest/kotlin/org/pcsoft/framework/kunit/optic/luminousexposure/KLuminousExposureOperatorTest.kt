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

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.optic.illuminance.KIlluminanceUnitInstance
import org.pcsoft.framework.kunit.optic.illuminance.lux
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group luminous exposure operators and the typed/native decomposition equivalence. */
class KLuminousExposureOperatorTest {

    /** `illuminance * time = luminous exposure` and its commutative counterpart. */
    @Test
    fun `illuminance times time is luminous exposure`() {
        val e = 50 of lux
        val t = 8 of hours
        val h1 = e * t
        val h2 = t * e
        assertIs<KLuminousExposureUnitInstance>(h1)
        assertIs<KLuminousExposureUnitInstance>(h2)
        assertEquals(400.0, h1 into luxHours, 1e-9)
        assertEquals(400.0, h2 into luxHours, 1e-9)
    }

    /** `luminous exposure / time = illuminance`. */
    @Test
    fun `luminous exposure over time is illuminance`() {
        val e = (400 of luxHours) / (8 of hours)
        assertIs<KIlluminanceUnitInstance>(e)
        assertEquals(50.0, e into lux, 1e-9)
    }

    /** `luminous exposure / illuminance = time`. */
    @Test
    fun `luminous exposure over illuminance is time`() {
        val t = (400 of luxHours) / (50 of lux)
        assertIs<KTimeUnitInstance>(t)
        assertEquals(8.0, t into hours, 1e-9)
    }

    /** The typed operator and the native `cd·sr·m⁻²·s` expression agree. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (50 of lux) * (10 of seconds)
        val native = ((50 of lux).toUnit() * (10 of seconds).toUnit()).toLuminousExposure()
        assertIs<KLuminousExposureUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(typed into luxSeconds, native into luxSeconds, 1e-12)
    }
}
