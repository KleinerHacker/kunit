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

package org.pcsoft.framework.kunit.storage

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.time.KTimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * Full behavioural matrix for [KStorageUnitInstance]: construction/conversion, every operator and every
 * comparison, parameterized over every storage unit (and every unit pair). Instances come from
 * [storageUnitGenerators] (creator properties); expected values from `unit.baseValue`.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KStorageUnitInstanceTest {

    /** Provider: every storage unit, for the single-unit parameterized tests. */
    private fun units(): List<Arguments> = storageUnitGenerators.map { Arguments.of(it.second) }

    /** Provider: the full cross-product of every unit against every other unit, for the pairwise tests. */
    private fun unitPairs(): List<Arguments> =
        storageUnitGenerators.flatMap { (_, a) -> storageUnitGenerators.map { (_, b) -> Arguments.of(a, b) } }

    // region construction / conversion

    /** Each storage creator builds `5 unit`, normalizes to bytes, and reads back exactly 5 via `valueAs`. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `every storage creator round trips through valueAs`(unit: KStorageUnit) {
        val instance = mkStorage(unit, 5)
        assertEquals(5.0 * unit.baseValue, instance.value, storageDelta(5.0 * unit.baseValue))
        assertEquals(5.0, instance.valueAs(unit), 5.0 * 1e-9)
    }

    /** Converting `5 from` into every other storage unit yields `5 * from.baseValue / to.baseValue` — the full conversion matrix. */
    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("unitPairs")
    fun `every storage converts into every other storage`(from: KStorageUnit, to: KStorageUnit) {
        val expected = 5.0 * from.baseValue / to.baseValue
        assertEquals(expected, mkStorage(from, 5).valueAs(to), storageDelta(expected))
    }

    /** 1 byte equals exactly 8 bits (the defining relation of the group). */
    @Test
    fun `one byte is eight bits`() {
        assertEquals(8.0, 1.bytes.valueAs(bits), 1e-9)
        assertEquals(1.0, 8.bits.valueAs(bytes), 1e-9)
    }

    // endregion

    // region operators

    /** `storage + storage` for every unit pair normalizes both and returns their sum as a storage value. */
    @ParameterizedTest(name = "{0} + {1}")
    @MethodSource("unitPairs")
    fun `plus combines every pair of storage values`(a: KStorageUnit, b: KStorageUnit) {
        val result: KStorageUnitInstance = mkStorage(a, 5) + mkStorage(b, 3)
        val expected = 5.0 * a.baseValue + 3.0 * b.baseValue
        assertEquals(expected, result.value, storageDelta(expected))
        assertEquals(listOf(KUnitTerm(KStorageUnit.BASE, 1)), result.toUnit().units)
    }

    /** `storage - storage` for every unit pair normalizes both and returns their difference as a storage value. */
    @ParameterizedTest(name = "{0} - {1}")
    @MethodSource("unitPairs")
    fun `minus combines every pair of storage values`(a: KStorageUnit, b: KStorageUnit) {
        val result: KStorageUnitInstance = mkStorage(a, 5) - mkStorage(b, 3)
        val expected = 5.0 * a.baseValue - 3.0 * b.baseValue
        assertEquals(expected, result.value, storageDelta(expected))
        assertEquals(listOf(KUnitTerm(KStorageUnit.BASE, 1)), result.toUnit().units)
    }

    /** `storage * storage` for every unit pair delegates to the mixed engine, forming a `byte²` term with the product value. */
    @ParameterizedTest(name = "{0} * {1}")
    @MethodSource("unitPairs")
    fun `times combines every pair of storage values`(a: KStorageUnit, b: KStorageUnit) {
        val result = mkStorage(a, 5) * mkStorage(b, 3).toUnit()
        val expected = (5.0 * a.baseValue) * (3.0 * b.baseValue)
        assertEquals(expected, result.value, storageDelta(expected))
        assertEquals(listOf(KUnitTerm(KStorageUnit.BASE, 2)), result.units)
    }

    /** `storage / storage` for every unit pair cancels the terms to a dimensionless [KMixedUnitInstance] holding the ratio. */
    @ParameterizedTest(name = "{0} / {1}")
    @MethodSource("unitPairs")
    fun `div combines every pair of storage values`(a: KStorageUnit, b: KStorageUnit) {
        val result = mkStorage(a, 5) / mkStorage(b, 3).toUnit()
        val expected = (5.0 * a.baseValue) / (3.0 * b.baseValue)
        assertEquals(expected, result.value, storageDelta(expected))
        assertTrue(result.units.isEmpty())
    }

    /** `storage pow 2` raises the whole value and doubles the exponent (group-agnostic `pow`). */
    @Test
    fun `pow squares the value and the exponent`() {
        val squared = 2.bytes pow 2
        assertEquals(4.0, squared.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KStorageUnit.BASE, 2)), squared.units)
    }

    // endregion

    // region comparisons

    /** All six comparison operators (`==`, `!=`, `<`, `<=`, `>`, `>=`) follow the normalized base values, across every unit pair. */
    @ParameterizedTest(name = "{0} cmp {1}")
    @MethodSource("unitPairs")
    fun `comparison operators follow normalized values`(a: KStorageUnit, b: KStorageUnit) {
        val x = mkStorage(a, 5)
        val y = mkStorage(b, 3)
        val xv = x.value
        val yv = y.value
        assertEquals(xv == yv, x == y)
        assertEquals(xv != yv, x != y)
        assertEquals(xv < yv, x < y)
        assertEquals(xv <= yv, x <= y)
        assertEquals(xv > yv, x > y)
        assertEquals(xv >= yv, x >= y)
    }

    /** `1.bytes == 8.bits`: equality is by normalized data amount, not by the constructing unit. */
    @Test
    fun `equality is by normalized amount`() {
        assertTrue(1.bytes == 8.bits)
        assertTrue(1.bytes != 4.bits)
    }

    // endregion

    // region toString

    /** `toString()` with no target renders the normalized value followed by the byte symbol. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString default renders the value in bytes`(unit: KStorageUnit) {
        val instance = mkStorage(unit, 5)
        assertEquals("${instance.value} ${KStorageUnit.BASE.symbol}", instance.toString())
    }

    /** `toString(unit)` renders the value converted into that unit followed by the unit's own symbol. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString with the own unit renders that symbol`(unit: KStorageUnit) {
        val instance = mkStorage(unit, 5)
        assertEquals("${instance.valueAs(unit)} ${unit.symbol}", instance.toString(unit))
    }

    /** `toString(kilo·unit)` renders the value in the decimal-scaled target with the prefixed symbol (`k…`). */
    @ParameterizedTest(name = "kilo {0}")
    @MethodSource("units")
    fun `toString with a decimal-scaled target renders the prefixed symbol`(unit: KStorageUnit) {
        val instance = mkStorage(unit, 5000)
        val scaled = KUnitPrefix.KILO with unit
        assertEquals("${instance.valueAs(scaled)} k${unit.symbol}", instance.toString(scaled))
    }

    /** `toString(kibi·unit)` renders the value in a binary-scaled target with the binary-prefixed symbol (`Ki…`). */
    @ParameterizedTest(name = "kibi {0}")
    @MethodSource("units")
    fun `toString with a binary-scaled target renders the binary-prefixed symbol`(unit: KStorageUnit) {
        val instance = mkStorage(unit, 4096)
        val scaled = KStorageBinaryPrefix.KIBI with unit
        assertEquals("${instance.valueAs(scaled)} Ki${unit.symbol}", instance.toString(scaled))
    }

    // endregion

    // region group-specific behaviour

    /** The creator property accepts any `Number` (`Int`/`Long`/`Float`/`Double`) and normalizes to the same Double value. */
    @Test
    fun `construction from non-Double Number types`() {
        assertEquals(5.0, 5.bytes.value, 1e-9)
        assertEquals(5.0, 5L.bytes.value, 1e-9)
        assertEquals(5.0, 5.0f.bytes.value, 1e-9)
        assertEquals(5.0, 5.0.bytes.value, 1e-9)
    }

    /** A storage value decomposed via `toUnit()` and recomposed via `toStorage()` equals the original. */
    @Test
    fun `toUnit and toStorage round trip`() {
        val original = 5.bits
        assertEquals(original, original.toUnit().toStorage())
    }

    /** `toStorage()` normalizes a non-base unit term (bits) to bytes before wrapping it. */
    @Test
    fun `toStorage normalizes a non-base unit`() {
        val sixteenBits = KMixedUnitInstance(16.0, listOf(KUnitTerm(KStorageUnit.BIT, 1)))
        assertEquals(2.0, sixteenBits.toStorage().value, 1e-9)
    }

    /** `toStorage()` on a non-storage term (e.g. a second) throws `IllegalStateException`. */
    @Test
    fun `toStorage fails for a non-storage unit`() {
        val notStorage = KMixedUnitInstance(5.0, listOf(KUnitTerm(KTimeUnit.SECOND, 1)))
        assertFailsWith<IllegalStateException> { notStorage.toStorage() }
    }

    /** Adding storage values built from a mixed unit of a different group must fail via the engine (`IllegalStateException`). */
    @Test
    fun `mixed-engine plus across groups fails`() {
        val storage = 5.bytes.toUnit()
        val time = KMixedUnitInstance(5.0, listOf(KUnitTerm(KTimeUnit.SECOND, 1)))
        assertFailsWith<IllegalStateException> { storage + time }
    }

    /** Multiplying a storage value by a raw [KMixedUnitInstance] delegates to the mixed engine. */
    @Test
    fun `times with KMixedUnitInstance delegates to the engine`() {
        val scalar = KMixedUnitInstance(2.0, listOf())
        val result = 10.bytes * scalar
        assertEquals(20.0, result.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KStorageUnit.BASE, 1)), result.units)
    }

    // endregion
}
