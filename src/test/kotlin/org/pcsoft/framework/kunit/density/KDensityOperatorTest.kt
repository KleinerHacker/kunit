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

package org.pcsoft.framework.kunit.density

import org.pcsoft.framework.kunit.distance.KVolumeUnitInstance
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group density operators: `mass / volume = density` and its inverses. */
class KDensityOperatorTest {

    /** `mass / volume = density`. */
    @Test
    fun `mass over volume is density`() {
        val d = (6 of kilo.grams) / (2 of liters)
        assertIs<KDensityUnitInstance>(d)
        assertEquals(3.0, (d * (1 of liters)) into kilo.grams, 1e-9) // 3 kg per liter
    }

    /** `density * volume = mass` and the commutative `volume * density = mass`. */
    @Test
    fun `density times volume is mass`() {
        val d = (2 of kilo.grams) / (1 of liters) // 2 kg/L
        val m1 = d * (3 of liters)
        val m2 = (3 of liters) * d
        assertIs<KMassUnitInstance>(m1)
        assertIs<KMassUnitInstance>(m2)
        assertEquals(6.0, m1 into kilo.grams, 1e-9)
        assertEquals(6.0, m2 into kilo.grams, 1e-9)
    }

    /** `mass / density = volume`. */
    @Test
    fun `mass over density is volume`() {
        val d = (2 of kilo.grams) / (1 of liters) // 2 kg/L
        val v: KVolumeUnitInstance = (6 of kilo.grams) / d
        assertIs<KVolumeUnitInstance>(v)
        assertEquals(3.0, v into liters, 1e-9)
    }
}
