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

package org.pcsoft.framework.kunit.mechanic.acousticimpedance

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named acoustic impedance token carries the correct Pa·s/m factor. */
class KAcousticImpedanceUnitTest {

    @Test
    fun `named tokens in pascal seconds per meter`() {
        assertEquals(1.0, (1 of pascalSecondsPerMeter) into pascalSecondsPerMeter, 1e-12)
        assertEquals(1.0, (1 of rayls) into pascalSecondsPerMeter, 1e-12)
        assertEquals(10.0, (1 of cgsRayls) into pascalSecondsPerMeter, 1e-9)
    }

    /** The SI rayl is a second spelling of the pascal second per meter. */
    @Test
    fun `rayl is the base unit spelling`() {
        assertEquals(1 of pascalSecondsPerMeter, 1 of rayls)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("Pa*s/m", KAcousticImpedanceUnit.PASCAL_SECOND_PER_METER.symbol)
        assertEquals(1.0, KAcousticImpedanceUnit.PASCAL_SECOND_PER_METER.baseValue, 1e-12)
        assertEquals("rayl", KAcousticImpedanceUnit.RAYL.symbol)
        assertEquals(1.0, KAcousticImpedanceUnit.RAYL.baseValue, 1e-12)
        assertEquals("rayl (CGS)", KAcousticImpedanceUnit.CGS_RAYL.symbol)
        assertEquals(10.0, KAcousticImpedanceUnit.CGS_RAYL.baseValue, 1e-9)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KAcousticImpedanceUnit.PASCAL_SECOND_PER_METER, KAcousticImpedanceUnit.BASE)
        assertEquals(1.0, KAcousticImpedanceUnit.BASE.baseValue, 1e-12)
        assertEquals("Pa*s/m", KAcousticImpedanceUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(3, KAcousticImpedanceUnit.entries.size)
        assertEquals(
            listOf("Pa*s/m", "rayl", "rayl (CGS)"),
            KAcousticImpedanceUnit.entries.map { it.symbol },
        )
    }
}
