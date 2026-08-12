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

package org.pcsoft.framework.kunit.optic.illuminance

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named illuminance token carries the correct lux factor. */
class KIlluminanceUnitTest {

    @Test
    fun `named tokens in lux`() {
        assertEquals(1.0, (1 of lux) into lux, 1e-12)
        assertEquals(1.0e4, (1 of phots) into lux, 1e-6)
        assertEquals(10.763910416709722, (1 of footCandles) into lux, 1e-9)
        assertEquals(1.0e-3, (1 of nox) into lux, 1e-15)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("lx", KIlluminanceUnit.LUX.symbol)
        assertEquals(1.0, KIlluminanceUnit.LUX.baseValue, 1e-12)
        assertEquals("ph", KIlluminanceUnit.PHOT.symbol)
        assertEquals(1.0e4, KIlluminanceUnit.PHOT.baseValue, 1e-6)
        assertEquals("fc", KIlluminanceUnit.FOOT_CANDLE.symbol)
        assertEquals(10.763910416709722, KIlluminanceUnit.FOOT_CANDLE.baseValue, 1e-9)
        assertEquals("nx", KIlluminanceUnit.NOX.symbol)
        assertEquals(1.0e-3, KIlluminanceUnit.NOX.baseValue, 1e-15)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KIlluminanceUnit.LUX, KIlluminanceUnit.BASE)
        assertEquals(1.0, KIlluminanceUnit.BASE.baseValue, 1e-12)
        assertEquals("lx", KIlluminanceUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(4, KIlluminanceUnit.entries.size)
        assertEquals(listOf("lx", "ph", "fc", "nx"), KIlluminanceUnit.entries.map { it.symbol })
    }
}
