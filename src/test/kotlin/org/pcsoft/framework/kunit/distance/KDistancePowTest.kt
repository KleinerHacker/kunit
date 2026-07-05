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

package org.pcsoft.framework.kunit.distance

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Full `pow` cross-matrix for the distance group: every length unit raised to `pow 2` / `pow 3` yields the
 * correctly typed and valued area/volume `((n·baseValue)ⁿ`, exponent `n`), plus the edge exponents (0, 1,
 * negative) and `pow` chaining. Complements [KDistancePrefixTest], which covers the prefix × unit × pow
 * space.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KDistancePowTest {

    private fun units(): List<Arguments> = lengthUnitGenerators.map { Arguments.of(it.second) }

    /** `n.<unit> pow 2` builds `(3·baseValue)²` m² as an [KAreaUnitInstance] (exponent 2) - for every length unit. */
    @ParameterizedTest(name = "{0} pow 2")
    @MethodSource("units")
    fun `every length pow 2 is a typed area`(unit: KDistanceUnit) {
        val result = mkLength(unit, 3) pow 2
        assertTrue(result is KAreaUnitInstance, "$unit pow 2 should be a KAreaUnitInstance")
        assertEquals(2, result.exponent)
        val expected = (3.0 * unit.baseValue).pow(2)
        assertEquals(expected, result.value, distanceDelta(expected))
    }

    /** `n.<unit> pow 3` builds `(3·baseValue)³` m³ as an [KVolumeUnitInstance] (exponent 3) - for every length unit. */
    @ParameterizedTest(name = "{0} pow 3")
    @MethodSource("units")
    fun `every length pow 3 is a typed volume`(unit: KDistanceUnit) {
        val result = mkLength(unit, 3) pow 3
        assertTrue(result is KVolumeUnitInstance, "$unit pow 3 should be a KVolumeUnitInstance")
        assertEquals(3, result.exponent)
        val expected = (3.0 * unit.baseValue).pow(3)
        assertEquals(expected, result.value, distanceDelta(expected))
    }

    /** `pow 1` returns the same length (exponent 1, value unchanged). */
    @Test
    fun `pow 1 keeps the length`() {
        val result = 2.meters pow 1
        assertTrue(result is KLengthUnitInstance)
        assertEquals(2.0, result.value, 1e-12)
        assertEquals(1, result.exponent)
    }

    /** `2.meters pow 2` is exactly `(2 m)² = 4 m²`, not `2 m²` - the value is powered, not merely the exponent. */
    @Test
    fun `pow squares the whole length not just the exponent`() {
        val area = 2.meters pow 2
        assertEquals(4.0, area.value, 1e-12)
        assertEquals("4.0 m^2", area.toString())
    }

    /** A prefixed length powers correctly: `(2 kilo meters) pow 2 = (2000 m)² = 4e6 m²`. */
    @Test
    fun `prefixed length pow 2`() {
        val area = 2 kilo meters pow 2
        assertEquals(4_000_000.0, area.value, 1e-3)
        assertEquals(2, area.exponent)
    }

    /** Chaining `pow` multiplies the exponent: `(2.meters pow 2) pow 2 = (4 m²)² = 16 m⁴` (general base type). */
    @Test
    fun `pow chains into exponent four`() {
        val m4 = 2.meters pow 2 pow 2
        assertEquals(16.0, m4.value, 1e-9)
        assertEquals(4, m4.exponent)
        assertTrue(m4 !is KLengthUnitInstance && m4 !is KAreaUnitInstance && m4 !is KVolumeUnitInstance)
    }

    /** A negative power inverts value and exponent, landing in the general base type (`(2 m)⁻¹ = 0.5 m⁻¹`). */
    @Test
    fun `pow negative inverts into general type`() {
        val inv = 2.meters pow -1
        assertEquals(0.5, inv.value, 1e-12)
        assertEquals(-1, inv.exponent)
    }
}
