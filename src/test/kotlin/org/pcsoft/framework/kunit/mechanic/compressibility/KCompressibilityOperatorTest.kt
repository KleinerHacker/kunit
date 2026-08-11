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

package org.pcsoft.framework.kunit.mechanic.compressibility

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.pressure.KPressureUnitInstance
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group compressibility operators: the reciprocal of the bulk modulus. */
class KCompressibilityOperatorTest {

    /** `1 / pressure = compressibility` - water's bulk modulus is ≈ 2.2 GPa. */
    @Test
    fun `reciprocal of a bulk modulus is compressibility`() {
        val kappa = 1 / (2.2 of giga.pascals)
        assertIs<KCompressibilityUnitInstance>(kappa)
        assertEquals(4.5454545e-10, kappa into reciprocalPascals, 1e-17)
    }

    /** `1 / compressibility = pressure` - the inverse direction. */
    @Test
    fun `reciprocal of a compressibility is a bulk modulus`() {
        val k = 1 / (4.5454545e-10 of reciprocalPascals)
        assertIs<KPressureUnitInstance>(k)
        assertEquals(2.2, k into giga.pascals, 1e-6)
    }

    /** `compressibility * pressure = dimensionless relative volume change` and its commutative form. */
    @Test
    fun `compressibility times pressure is dimensionless`() {
        val kappa = 4.5454545e-10 of reciprocalPascals
        val dp = 10 of mega.pascals
        assertEquals(4.5454545e-3, kappa * dp, 1e-12)
        assertEquals(4.5454545e-3, dp * kappa, 1e-12)
    }

    /** A round trip through both reciprocals returns the original value. */
    @Test
    fun `reciprocal round trip`() {
        val original = 2.2 of giga.pascals
        val back = 1 / (1 / original)
        assertIs<KPressureUnitInstance>(back)
        assertEquals(original into pascals, back into pascals, 1e-3)
    }
}
