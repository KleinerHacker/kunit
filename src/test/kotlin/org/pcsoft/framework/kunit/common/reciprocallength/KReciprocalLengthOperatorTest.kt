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

package org.pcsoft.framework.kunit.common.reciprocallength

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group reciprocal length operators and the typed/native decomposition equivalence. */
class KReciprocalLengthOperatorTest {

    /** `count / length = reciprocal length` - a lens with a 0.4 m focal length has 2.5 dpt. */
    @Test
    fun `count over length is reciprocal length`() {
        val d = 1 / (0.4 of meters)
        assertIs<KReciprocalLengthUnitInstance>(d)
        assertEquals(2.5, d into dioptres, 1e-9)
    }

    /** `count / reciprocal length = length`. */
    @Test
    fun `count over reciprocal length is length`() {
        val f = 1 / (2.5 of dioptres)
        assertIs<KLengthUnitInstance>(f)
        assertEquals(0.4, f into meters, 1e-9)
    }

    /** `reciprocal length * length = count` and its commutative counterpart. */
    @Test
    fun `reciprocal length times length is dimensionless`() {
        assertEquals(6.0, (2 of reciprocalMeters) * (3 of meters), 1e-9)
        assertEquals(6.0, (3 of meters) * (2 of reciprocalMeters), 1e-9)
    }

    /** A wavenumber built from a wavelength: 500 nm light is 20 000 cm⁻¹. */
    @Test
    fun `wavenumber from wavelength`() {
        val k = 1 / (500 of nano.meters)
        assertIs<KReciprocalLengthUnitInstance>(k)
        assertEquals(20000.0, k into reciprocalCentimeters, 1e-6)
    }

    /** The typed operator and the native `m⁻¹` expression yield the same typed, value-equal result. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = 1 / (0.4 of meters)
        val native = (2.5 of ((1 of meters).toUnit() pow -1)).toReciprocalLength()
        assertIs<KReciprocalLengthUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(typed into dioptres, native into dioptres, 1e-12)
    }
}
