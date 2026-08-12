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

package org.pcsoft.framework.kunit.mechanic.inertia

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed moment-of-inertia operators: `mass * area` and its inverses. */
class KInertiaOperatorTest {

    private val area = (3 of meters) * (3 of meters) // 9 m²

    /** `mass * area = inertia`, plus its commutative form. */
    @Test
    fun `mass times area is inertia`() {
        val j1 = (2 of kilo.grams) * area
        val j2 = area * (2 of kilo.grams)
        assertIs<KInertiaUnitInstance>(j1)
        assertIs<KInertiaUnitInstance>(j2)
        assertEquals(18.0, j1 into kilogramMetersSquared, 1e-9)
        assertEquals(18.0, j2 into kilogramMetersSquared, 1e-9)
    }

    /** The typed operator and the native `mass * length²` form agree. */
    @Test
    fun `both decompositions agree`() {
        val typed = (2 of kilo.grams) * area
        val native = ((2000 of grams).toUnit() * ((3 of meters).toUnit() pow 2)).toInertia()
        assertEquals(typed, native)
    }

    /** `inertia / mass = area` (the squared radius of gyration) and `inertia / area = mass`. */
    @Test
    fun `inertia inverses`() {
        val j = 18 of kilogramMetersSquared
        val a: KAreaUnitInstance = j / (2 of kilo.grams)
        val m: KMassUnitInstance = j / area
        assertIs<KAreaUnitInstance>(a)
        assertIs<KMassUnitInstance>(m)
        assertEquals(9.0, a into ((1 of meters) * (1 of meters)), 1e-9)
        assertEquals(2.0, m into kilo.grams, 1e-9)
    }
}
