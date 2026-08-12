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

package org.pcsoft.framework.kunit.thermo.volumetricheatcapacity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named volumetric heat capacity token carries the correct J/(m³·K) factor. */
class KVolumetricHeatCapacityUnitTest {

    @Test
    fun `named tokens in joules per cubic meter kelvin`() {
        assertEquals(1.0, (1 of joulesPerCubicMeterKelvin) into joulesPerCubicMeterKelvin, 1e-12)
        assertEquals(
            4.184e6,
            (1 of caloriesPerCubicCentimeterKelvin) into joulesPerCubicMeterKelvin,
            1e-3,
        )
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("J/(m^3*K)", KVolumetricHeatCapacityUnit.JOULE_PER_CUBIC_METER_KELVIN.symbol)
        assertEquals(1.0, KVolumetricHeatCapacityUnit.JOULE_PER_CUBIC_METER_KELVIN.baseValue, 1e-12)
        assertEquals(
            "cal/(cm^3*K)",
            KVolumetricHeatCapacityUnit.CALORIE_PER_CUBIC_CENTIMETER_KELVIN.symbol,
        )
        assertEquals(
            4.184e6,
            KVolumetricHeatCapacityUnit.CALORIE_PER_CUBIC_CENTIMETER_KELVIN.baseValue,
            1e-3,
        )
    }

    @Test
    fun `base unit marker`() {
        assertEquals(
            KVolumetricHeatCapacityUnit.JOULE_PER_CUBIC_METER_KELVIN,
            KVolumetricHeatCapacityUnit.BASE,
        )
        assertEquals(1.0, KVolumetricHeatCapacityUnit.BASE.baseValue, 1e-12)
        assertEquals("J/(m^3*K)", KVolumetricHeatCapacityUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(2, KVolumetricHeatCapacityUnit.entries.size)
        assertEquals(
            listOf("J/(m^3*K)", "cal/(cm^3*K)"),
            KVolumetricHeatCapacityUnit.entries.map { it.symbol },
        )
    }
}
