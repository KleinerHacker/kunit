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

package org.pcsoft.framework.kunit.mechanic.lineardensity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed linear-density operators: `mass / length` and its inverses. */
class KLinearDensityOperatorTest {

    /** `mass / length = lineardensity`. */
    @Test
    fun `mass over length is linear density`() {
        val rho = (2 of kilo.grams) / (4 of meters)
        assertIs<KLinearDensityUnitInstance>(rho)
        assertEquals(0.5, rho into kilogramsPerMeter, 1e-9)
    }

    /** `lineardensity * length = mass`, plus its commutative form. */
    @Test
    fun `linear density times length is mass`() {
        val rho = 0.5 of kilogramsPerMeter
        val m1: KMassUnitInstance = rho * (4 of meters)
        val m2: KMassUnitInstance = (4 of meters) * rho
        assertIs<KMassUnitInstance>(m1)
        assertIs<KMassUnitInstance>(m2)
        assertEquals(2.0, m1 into kilo.grams, 1e-9)
        assertEquals(2.0, m2 into kilo.grams, 1e-9)
    }

    /** `mass / lineardensity = length`. */
    @Test
    fun `mass over linear density is length`() {
        val l: KLengthUnitInstance = (2 of kilo.grams) / (0.5 of kilogramsPerMeter)
        assertIs<KLengthUnitInstance>(l)
        assertEquals(4.0, l into meters, 1e-9)
    }
}
