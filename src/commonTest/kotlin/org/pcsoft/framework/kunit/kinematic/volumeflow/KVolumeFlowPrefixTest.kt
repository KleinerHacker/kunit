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

package org.pcsoft.framework.kunit.kinematic.volumeflow

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every prefixed volumetric flow template scales its unit by the prefix factor. */
class KVolumeFlowPrefixTest {

    @Test
    fun `prefixed cubic meters per second`() {
        assertEquals(1.0e-3, (1 of milli.cubicMetersPerSecond) into cubicMetersPerSecond, 1e-12)
        assertEquals(1.0e3, (1 of kilo.cubicMetersPerSecond) into cubicMetersPerSecond, 1e-9)
    }

    @Test
    fun `prefixed cubic meters per hour`() {
        assertEquals(1.0e3 / 3600.0, (1 of kilo.cubicMetersPerHour) into cubicMetersPerSecond, 1e-9)
    }

    @Test
    fun `prefixed liters per second`() {
        assertEquals(1.0e-6, (1 of milli.litersPerSecond) into cubicMetersPerSecond, 1e-15)
    }

    @Test
    fun `prefixed liters per minute`() {
        assertEquals(1.0e-6 / 60.0, (1 of milli.litersPerMinute) into cubicMetersPerSecond, 1e-18)
    }

    @Test
    fun `prefixed us gallons per minute`() {
        assertEquals(1000.0 * 0.003785411784 / 60.0, (1 of kilo.usGallonsPerMinute) into cubicMetersPerSecond, 1e-12)
    }
}
