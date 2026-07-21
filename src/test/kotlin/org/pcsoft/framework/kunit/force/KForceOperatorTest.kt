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

package org.pcsoft.framework.kunit.force

import org.pcsoft.framework.kunit.acceleration.KAccelerationUnitInstance
import org.pcsoft.framework.kunit.acceleration.standardGravities
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group force operators: `mass * acceleration = force` and its inverses. */
class KForceOperatorTest {

    /** `mass * acceleration = force` and the commutative `acceleration * mass = force`. */
    @Test
    fun `mass times acceleration is force`() {
        val f1 = (2 of kilo.grams) * (3 of standardGravities)
        val f2 = (3 of standardGravities) * (2 of kilo.grams)
        assertIs<KForceUnitInstance>(f1)
        assertIs<KForceUnitInstance>(f2)
        assertEquals(2.0 * 3.0 * 9.80665, f1 into newtons, 1e-9)
        assertEquals(2.0 * 3.0 * 9.80665, f2 into newtons, 1e-9)
    }

    /** `force / mass = acceleration`. */
    @Test
    fun `force over mass is acceleration`() {
        val a: KAccelerationUnitInstance = (10 of newtons) / (2 of kilo.grams)
        assertIs<KAccelerationUnitInstance>(a)
        assertEquals(5.0, a.value, 1e-9) // 10 N / 2 kg = 5 m/s²
    }

    /** `force / acceleration = mass`. */
    @Test
    fun `force over acceleration is mass`() {
        val m: KMassUnitInstance = (10 of newtons) / (1 of standardGravities)
        assertIs<KMassUnitInstance>(m)
        assertEquals(10.0 / 9.80665, (m into grams) / 1000.0, 1e-6) // in kg
    }
}
