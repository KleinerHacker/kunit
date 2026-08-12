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
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named volumetric flow token carries the correct m³/s factor. */
class KVolumeFlowUnitTest {

    @Test
    fun `named tokens in cubic meters per second`() {
        assertEquals(1.0, (1 of cubicMetersPerSecond) into cubicMetersPerSecond, 1e-12)
        assertEquals(1.0 / 3600.0, (1 of cubicMetersPerHour) into cubicMetersPerSecond, 1e-12)
        assertEquals(1.0e-3, (1 of litersPerSecond) into cubicMetersPerSecond, 1e-12)
        assertEquals(1.0e-3 / 60.0, (1 of litersPerMinute) into cubicMetersPerSecond, 1e-12)
        assertEquals(0.003785411784 / 60.0, (1 of usGallonsPerMinute) into cubicMetersPerSecond, 1e-15)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("m³/s", KVolumeFlowUnit.CUBIC_METER_PER_SECOND.symbol)
        assertEquals(1.0, KVolumeFlowUnit.CUBIC_METER_PER_SECOND.baseValue, 1e-12)
        assertEquals("m³/h", KVolumeFlowUnit.CUBIC_METER_PER_HOUR.symbol)
        assertEquals(1.0 / 3600.0, KVolumeFlowUnit.CUBIC_METER_PER_HOUR.baseValue, 1e-12)
        assertEquals("l/s", KVolumeFlowUnit.LITER_PER_SECOND.symbol)
        assertEquals(1.0e-3, KVolumeFlowUnit.LITER_PER_SECOND.baseValue, 1e-12)
        assertEquals("l/min", KVolumeFlowUnit.LITER_PER_MINUTE.symbol)
        assertEquals(1.0e-3 / 60.0, KVolumeFlowUnit.LITER_PER_MINUTE.baseValue, 1e-12)
        assertEquals("gpm", KVolumeFlowUnit.US_GALLON_PER_MINUTE.symbol)
        assertEquals(0.003785411784 / 60.0, KVolumeFlowUnit.US_GALLON_PER_MINUTE.baseValue, 1e-15)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KVolumeFlowUnit.CUBIC_METER_PER_SECOND, KVolumeFlowUnit.BASE)
        assertEquals(1.0, KVolumeFlowUnit.BASE.baseValue, 1e-12)
        assertEquals("m³/s", KVolumeFlowUnit.BASE.symbol)
    }
}
