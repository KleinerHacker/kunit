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

package org.pcsoft.framework.kunit.mechanic.specificweight

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named specific weight token carries the correct N/m³ factor. */
class KSpecificWeightUnitTest {

    @Test
    fun `named tokens in newtons per cubic meter`() {
        assertEquals(1.0, (1 of newtonsPerCubicMeter) into newtonsPerCubicMeter, 1e-12)
        assertEquals(1000.0, (1 of kilonewtonsPerCubicMeter) into newtonsPerCubicMeter, 1e-9)
        assertEquals(157.08746384, (1 of poundsForcePerCubicFoot) into newtonsPerCubicMeter, 1e-6)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("N/m^3", KSpecificWeightUnit.NEWTON_PER_CUBIC_METER.symbol)
        assertEquals(1.0, KSpecificWeightUnit.NEWTON_PER_CUBIC_METER.baseValue, 1e-12)
        assertEquals("kN/m^3", KSpecificWeightUnit.KILONEWTON_PER_CUBIC_METER.symbol)
        assertEquals(1000.0, KSpecificWeightUnit.KILONEWTON_PER_CUBIC_METER.baseValue, 1e-9)
        assertEquals("lbf/ft^3", KSpecificWeightUnit.POUND_FORCE_PER_CUBIC_FOOT.symbol)
        assertEquals(157.08746384, KSpecificWeightUnit.POUND_FORCE_PER_CUBIC_FOOT.baseValue, 1e-6)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KSpecificWeightUnit.NEWTON_PER_CUBIC_METER, KSpecificWeightUnit.BASE)
        assertEquals(1.0, KSpecificWeightUnit.BASE.baseValue, 1e-12)
        assertEquals("N/m^3", KSpecificWeightUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(3, KSpecificWeightUnit.entries.size)
        assertEquals(
            listOf("N/m^3", "kN/m^3", "lbf/ft^3"),
            KSpecificWeightUnit.entries.map { it.symbol },
        )
    }
}
