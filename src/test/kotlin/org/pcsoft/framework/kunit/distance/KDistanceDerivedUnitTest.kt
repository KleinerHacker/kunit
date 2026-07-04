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
import org.pcsoft.framework.kunit.KDerivedUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KDistanceDerivedUnitTest {

    private fun areaDeriveds(): List<Arguments> = areaDerivedGenerators.map { Arguments.of(it.second) }
    private fun volumeDeriveds(): List<Arguments> = volumeDerivedGenerators.map { Arguments.of(it.second) }

    private fun mkAreaDerived(d: KDerivedUnit<KDistanceUnit>, n: Number) = areaDerivedGenerators.first { it.second == d }.first(n)
    private fun mkVolumeDerived(d: KDerivedUnit<KDistanceUnit>, n: Number) = volumeDerivedGenerators.first { it.second == d }.first(n)

    @ParameterizedTest(name = "{0}")
    @MethodSource("areaDeriveds")
    fun `area derived units round trip and are exponent 2`(d: KDerivedUnit<KDistanceUnit>) {
        val instance = mkAreaDerived(d, 5)
        assertTrue(instance is KAreaUnitInstance)
        assertEquals(2, instance.exponent)
        assertEquals(5.0 * d.baseValue, instance.value, distanceDelta(5.0 * d.baseValue))
        assertEquals(5.0, instance.valueAs(d), 1e-9)
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("volumeDeriveds")
    fun `volume derived units round trip and are exponent 3`(d: KDerivedUnit<KDistanceUnit>) {
        val instance = mkVolumeDerived(d, 5)
        assertTrue(instance is KVolumeUnitInstance)
        assertEquals(3, instance.exponent)
        assertEquals(5.0 * d.baseValue, instance.value, distanceDelta(5.0 * d.baseValue))
        assertEquals(5.0, instance.valueAs(d), 1e-9)
    }

    @Test
    fun `an area built from two lengths reads back in hectares`() {
        val area = 200.meters * 50.meters // 10 000 m²
        assertEquals(1.0, area.valueAs(KDistanceDerivedUnit.HECTARE), 1e-9)
    }

    @Test
    fun `a volume built from three lengths reads back in liters`() {
        val volume = 2.meters * 2.meters * 2.meters // 8 m³
        assertEquals(8000.0, volume.valueAs(KDistanceDerivedUnit.LITER), 1e-6)
    }

    @Test
    fun `a derived-unit target of the wrong exponent fails`() {
        assertFailsWith<IllegalStateException> { 5.meters.valueAs(KDistanceDerivedUnit.HECTARE) }
        assertFailsWith<IllegalStateException> { 5.squareMeters.valueAs(KDistanceDerivedUnit.LITER) }
    }
}
