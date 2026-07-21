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

package org.pcsoft.framework.kunit.pressure

import org.pcsoft.framework.kunit.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.force.KForceUnitInstance
import org.pcsoft.framework.kunit.force.newtons
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group pressure operators: `force / area = pressure` and its inverses. */
class KPressureOperatorTest {

    private val area = (2 of meters) * (1 of meters) // 2 m²

    /** `force / area = pressure`. */
    @Test
    fun `force over area is pressure`() {
        val p = (100 of newtons) / area
        assertIs<KPressureUnitInstance>(p)
        assertEquals(50.0, p into pascals, 1e-9) // 100 N / 2 m² = 50 Pa
    }

    /** `pressure * area = force` and the commutative `area * pressure = force`. */
    @Test
    fun `pressure times area is force`() {
        val p = 50 of pascals
        val f1 = p * area
        val f2 = area * p
        assertIs<KForceUnitInstance>(f1)
        assertIs<KForceUnitInstance>(f2)
        assertEquals(100.0, f1 into newtons, 1e-9)
        assertEquals(100.0, f2 into newtons, 1e-9)
    }

    /** `force / pressure = area`. */
    @Test
    fun `force over pressure is area`() {
        val a: KAreaUnitInstance = (100 of newtons) / (50 of pascals)
        assertIs<KAreaUnitInstance>(a)
        assertEquals(2.0, a into ((1 of meters) * (1 of meters)), 1e-9)
    }
}
