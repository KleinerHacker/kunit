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

package org.pcsoft.framework.kunit.mechanic.viscosity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.pressure.KPressureUnitInstance
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed dynamic-viscosity operators: `pressure * time` and its inverses. */
class KViscosityOperatorTest {

    /** `pressure * time = viscosity`, plus its commutative form. */
    @Test
    fun `pressure times time is viscosity`() {
        val eta1 = (1 of milli.pascals) * (1 of seconds)
        val eta2 = (1 of seconds) * (1 of milli.pascals)
        assertIs<KViscosityUnitInstance>(eta1)
        assertIs<KViscosityUnitInstance>(eta2)
        assertEquals(0.001, eta1 into pascalSeconds, 1e-12)
        assertEquals(0.001, eta2 into pascalSeconds, 1e-12)
    }

    /** The typed operator and the native form agree. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (2 of pascals) * (3 of seconds)
        val native = ((2 of pascals).toUnit() * (3 of seconds).toUnit()).toViscosity()
        assertEquals(typed, native)
        assertEquals(6.0, typed into pascalSeconds, 1e-9)
    }

    /** `viscosity / pressure = time` and `viscosity / time = pressure`. */
    @Test
    fun `viscosity inverses`() {
        val eta = 6 of pascalSeconds
        val t: KTimeUnitInstance = eta / (2 of pascals)
        val p: KPressureUnitInstance = eta / (3 of seconds)
        assertIs<KTimeUnitInstance>(t)
        assertIs<KPressureUnitInstance>(p)
        assertEquals(3.0, t into seconds, 1e-9)
        assertEquals(2.0, p into pascals, 1e-9)
    }
}
