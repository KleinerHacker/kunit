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

package org.pcsoft.framework.kunit.thermo.molarheatcapacity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named molar heat capacity token carries the correct J/(mol·K) factor. */
class KMolarHeatCapacityUnitTest {

    @Test
    fun `named tokens in joules per mole kelvin`() {
        assertEquals(1.0, (1 of joulesPerMoleKelvin) into joulesPerMoleKelvin, 1e-12)
        assertEquals(4.184, (1 of caloriesPerMoleKelvin) into joulesPerMoleKelvin, 1e-9)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("J/(mol·K)", KMolarHeatCapacityUnit.JOULE_PER_MOLE_KELVIN.symbol)
        assertEquals(1.0, KMolarHeatCapacityUnit.JOULE_PER_MOLE_KELVIN.baseValue, 1e-12)
        assertEquals("cal/(mol·K)", KMolarHeatCapacityUnit.CALORIE_PER_MOLE_KELVIN.symbol)
        assertEquals(4.184, KMolarHeatCapacityUnit.CALORIE_PER_MOLE_KELVIN.baseValue, 1e-12)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KMolarHeatCapacityUnit.JOULE_PER_MOLE_KELVIN, KMolarHeatCapacityUnit.BASE)
        assertEquals(1.0, KMolarHeatCapacityUnit.BASE.baseValue, 1e-12)
        assertEquals("J/(mol·K)", KMolarHeatCapacityUnit.BASE.symbol)
    }

    @Test
    fun `gas constant`() {
        assertEquals(8.31446261815324, GAS_CONSTANT, 1e-12)
        assertEquals(8.31446261815324, (GAS_CONSTANT of joulesPerMoleKelvin) into joulesPerMoleKelvin, 1e-12)
    }
}
