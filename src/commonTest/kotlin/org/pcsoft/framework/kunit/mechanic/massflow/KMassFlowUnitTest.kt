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

package org.pcsoft.framework.kunit.mechanic.massflow

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** The named mass-flow tokens carry the correct kg/s factor. */
class KMassFlowUnitTest {

    @Test
    fun `named tokens in kilograms per second`() {
        assertEquals(1.0, (1 of kilogramsPerSecond) into kilogramsPerSecond, 1e-9)
        assertEquals(0.001, (1 of gramsPerSecond) into kilogramsPerSecond, 1e-12)
        assertEquals(1.0 / 3600.0, (1 of kilogramsPerHour) into kilogramsPerSecond, 1e-12)
        assertEquals(1000.0 / 3600.0, (1 of tonnesPerHour) into kilogramsPerSecond, 1e-9)
        assertEquals(0.45359237, (1 of poundsPerSecond) into kilogramsPerSecond, 1e-9)
        assertEquals(0.45359237 / 3600.0, (1 of poundsPerHour) into kilogramsPerSecond, 1e-12)
    }

    @Test
    fun `enum entries expose symbol and factor`() {
        assertEquals("kg/s", KMassFlowUnit.KILOGRAMS_PER_SECOND.symbol)
        assertEquals("g/s", KMassFlowUnit.GRAMS_PER_SECOND.symbol)
        assertEquals("kg/h", KMassFlowUnit.KILOGRAMS_PER_HOUR.symbol)
        assertEquals("t/h", KMassFlowUnit.TONNES_PER_HOUR.symbol)
        assertEquals("lb/s", KMassFlowUnit.POUNDS_PER_SECOND.symbol)
        assertEquals("lb/h", KMassFlowUnit.POUNDS_PER_HOUR.symbol)
        assertEquals(1.0, KMassFlowUnit.KILOGRAMS_PER_SECOND.baseValue, 1e-12)
        assertEquals(1.0e-3, KMassFlowUnit.GRAMS_PER_SECOND.baseValue, 1e-15)
        assertEquals(1.0 / 3600.0, KMassFlowUnit.KILOGRAMS_PER_HOUR.baseValue, 1e-15)
        assertEquals(1000.0 / 3600.0, KMassFlowUnit.TONNES_PER_HOUR.baseValue, 1e-12)
        assertEquals(0.45359237, KMassFlowUnit.POUNDS_PER_SECOND.baseValue, 1e-12)
        assertEquals(0.45359237 / 3600.0, KMassFlowUnit.POUNDS_PER_HOUR.baseValue, 1e-15)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KMassFlowUnit.KILOGRAMS_PER_SECOND, KMassFlowUnit.BASE)
        assertEquals(1.0, KMassFlowUnit.BASE.baseValue, 1e-12)
        assertEquals("kg/s", KMassFlowUnit.BASE.symbol)
    }
}
