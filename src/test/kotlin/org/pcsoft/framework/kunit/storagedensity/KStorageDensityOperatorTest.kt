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

package org.pcsoft.framework.kunit.storagedensity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.KStorageUnit
import org.pcsoft.framework.kunit.storage.KStorageUnitInstance
import org.pcsoft.framework.kunit.storage.bits
import org.pcsoft.framework.kunit.storage.bytes
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * The typed cross-group storage-density operators: core→composed (`storage / area = storage density`)
 * across a storage×area matrix, composed→core decomposition (`density * area`, `storage / density`), and
 * the invalid-shape guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KStorageDensityOperatorTest {

    private val storages = listOf(
        bytes to KStorageUnit.BYTE.baseValue,
        bits to KStorageUnit.BIT.baseValue,
    )

    // area value in square meters: (1 m)² = 1.0, (1 mm)² = 1e-6
    private fun squareMeters(): KAreaUnitInstance = (1 of meters) * (1 of meters)
    private fun squareMillimeters(): KAreaUnitInstance = (1 of milli.meters) * (1 of milli.meters)
    private val areas = listOf(
        squareMeters() to 1.0,
        squareMillimeters() to 1e-6,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-12)
    private fun matrix(): List<Array<Any>> =
        storages.flatMap { s -> areas.map { a -> arrayOf(s.first, s.second, a.first, a.second) } }

    /** core→composed: `storage / area` is a typed storage density whose B/m² value is `s.base / a.base`. */
    @ParameterizedTest
    @MethodSource("matrix")
    fun `storage over area is storage density`(s: KStorageUnitInstance, sb: Double, a: KAreaUnitInstance, ab: Double) {
        val d = (100 of s) / a
        assertIs<KStorageDensityUnitInstance>(d)
        val expected = 100.0 * sb / ab
        assertEquals(expected, d.value, rel(expected))
    }

    /** composed→core: `density * area = storage`, recovered in bytes. */
    @ParameterizedTest
    @MethodSource("matrix")
    fun `density times area is storage`(s: KStorageUnitInstance, sb: Double, a: KAreaUnitInstance, ab: Double) {
        val d = (100 of s) / a
        val amount = d * a
        assertIs<KStorageUnitInstance>(amount)
        assertEquals(100.0 * sb, amount into bytes, rel(100.0 * sb))
    }

    /** composed→core: `storage / density = area`, recovered in square meters. */
    @Test
    fun `storage over density is area`() {
        val d = (100 of bytes) / squareMeters() // 100 B/m²
        val area = (600 of bytes) / d
        assertIs<KAreaUnitInstance>(area)
        assertEquals(6.0, area into squareMeters(), 1e-9)
    }

    /** The commutative `area * density = storage`. */
    @Test
    fun `area times density is storage`() {
        val d = (100 of bytes) / squareMeters() // 100 B/m²
        val amount = squareMeters() * d
        assertIs<KStorageUnitInstance>(amount)
        assertEquals(100.0, amount into bytes, 1e-9)
    }

    /** A non-density shape (storage² / area, a single storage term, or area at +2) fails to convert. */
    @Test
    fun `invalid density decomposition fails`() {
        val sq = (2 of bytes).toUnit() * (2 of bytes).toUnit()
        assertFailsWith<IllegalStateException> { (sq / squareMeters().toUnit()).toStorageDensity() }
        // a single storage term (size != 2, no area term) also fails
        assertFailsWith<IllegalStateException> { (100 of bytes).toUnit().toStorageDensity() }
        // two terms, storage present, but the distance term has the wrong exponent (+2, not -2)
        assertFailsWith<IllegalStateException> { ((1 of bytes).toUnit() * squareMeters().toUnit()).toStorageDensity() }
    }

    /** Same-type density operators: `+`/`-`, comparison, and `density*density`/`density/density` escaping to a mixed unit. */
    @Test
    fun `density same-type operators`() {
        val d1 = (100 of bytes) / squareMeters() // 100 B/m²
        val d2 = (20 of bytes) / squareMeters()  // 20 B/m²
        assertEquals(120.0, (d1 + d2).value, 1e-9)
        assertEquals(80.0, (d1 - d2).value, 1e-9)
        assertTrue(d1 > d2)
        assertIs<KMixedUnitInstance>(d1 * d2)
        assertIs<KMixedUnitInstance>(d1 / d2)
    }
}
