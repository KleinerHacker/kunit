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

package org.pcsoft.framework.kunit.mechanic.lineforce

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** The named force-per-length tokens carry the correct N/m factor. */
class KLineForceUnitTest {

    @Test
    fun `named tokens in newtons per meter`() {
        assertEquals(1.0, (1 of newtonsPerMeter) into newtonsPerMeter, 1e-9)
        assertEquals(1000.0, (1 of newtonsPerMillimeter) into newtonsPerMeter, 1e-6)
        assertEquals(0.001, (1 of dynesPerCentimeter) into newtonsPerMeter, 1e-12)
        assertEquals(4.4482216152605 / 0.0254, (1 of poundsForcePerInch) into newtonsPerMeter, 1e-9)
        assertEquals(9.80665, (1 of kilopondsPerMeter) into newtonsPerMeter, 1e-9)
    }

    @Test
    fun `enum entries expose symbol and factor`() {
        assertEquals("N/m", KLineForceUnit.NEWTONS_PER_METER.symbol)
        assertEquals("N/mm", KLineForceUnit.NEWTONS_PER_MILLIMETER.symbol)
        assertEquals("dyn/cm", KLineForceUnit.DYNES_PER_CENTIMETER.symbol)
        assertEquals("lbf/in", KLineForceUnit.POUNDS_FORCE_PER_INCH.symbol)
        assertEquals("kp/m", KLineForceUnit.KILOPONDS_PER_METER.symbol)
        assertEquals(1.0, KLineForceUnit.NEWTONS_PER_METER.baseValue, 1e-12)
        assertEquals(1000.0, KLineForceUnit.NEWTONS_PER_MILLIMETER.baseValue, 1e-9)
        assertEquals(1.0e-3, KLineForceUnit.DYNES_PER_CENTIMETER.baseValue, 1e-15)
        assertEquals(175.12683524, KLineForceUnit.POUNDS_FORCE_PER_INCH.baseValue, 1e-6)
        assertEquals(9.80665, KLineForceUnit.KILOPONDS_PER_METER.baseValue, 1e-12)
    }

    /** The surface tension of water at 25 °C reads the same as dyn/cm and mN/m. */
    @Test
    fun `surface tension readings coincide`() {
        assertEquals(72.0, (72 of dynesPerCentimeter) into dynesPerCentimeter, 1e-9)
        assertEquals(0.072, (72 of dynesPerCentimeter) into newtonsPerMeter, 1e-12)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KLineForceUnit.NEWTONS_PER_METER, KLineForceUnit.BASE)
        assertEquals(1.0, KLineForceUnit.BASE.baseValue, 1e-12)
        assertEquals("N/m", KLineForceUnit.BASE.symbol)
    }
}
