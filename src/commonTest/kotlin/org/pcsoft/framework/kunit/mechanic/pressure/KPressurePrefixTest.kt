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

package org.pcsoft.framework.kunit.mechanic.pressure

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import kotlin.test.Test
import kotlin.test.assertEquals

/** Prefixed pressure templates scale the underlying unit by the prefix factor. */
class KPressurePrefixTest {

    @Test
    fun `prefixed pascals`() {
        assertEquals(100.0, (1 of hecto.pascals) into pascals, 1e-9)         // 1 hPa
        assertEquals(1000.0, (1 of kilo.pascals) into pascals, 1e-9)         // 1 kPa
        assertEquals(1_000_000.0, (1 of mega.pascals) into pascals, 1e-3)    // 1 MPa = N/mm²
    }

    @Test
    fun `prefixed bars`() {
        assertEquals(100.0, (1 of milli.bars) into pascals, 1e-9) // 1 mbar = 100 Pa
    }

    /**
     * The mechanical-stress and elastic-modulus spellings are prefix aliases of the pascal, not units of
     * their own: N/mm² = MPa and the GPa of the elastic modulus (see the `stress` documentation page).
     */
    @Test
    fun `stress and modulus aliases`() {
        assertEquals(1.0e6, (1 of mega.pascals) into pascals, 1e-3)
        assertEquals(1.0e9, (1 of giga.pascals) into pascals, 1.0)
        assertEquals(1000.0, (1 of giga.pascals) into mega.pascals, 1e-6)
        // N/mm² is exactly the megapascal
        assertEquals(
            1.0,
            ((1 of newtons) / ((1 of milli.meters) * (1 of milli.meters))) into mega.pascals,
            1e-9,
        )
    }
}
