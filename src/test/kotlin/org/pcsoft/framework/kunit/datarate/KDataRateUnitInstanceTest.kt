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

package org.pcsoft.framework.kunit.datarate

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.storage.KStorageBinaryPrefix
import org.pcsoft.framework.kunit.storage.KStorageUnit
import org.pcsoft.framework.kunit.storage.with
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.with
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * Full behavioural matrix for [KDataRateUnitInstance]: construction/conversion, every operator (`+`,
 * `-`, `*`, `/`) and every comparison, each parameterized over every unit (and every unit pair) of the
 * group. Instances are built via [dataRateOf] (the creator properties); expected values come from
 * `unit.baseValue`. `PER_CLASS` lifecycle lets the `@MethodSource` providers below be non-static.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KDataRateUnitInstanceTest {

    /** Provider: every data-rate unit, for the single-unit parameterized tests. */
    private fun units(): List<Arguments> = dataRateUnitGenerators.map { Arguments.of(it.second) }

    /** Provider: the full cross-product of every unit against every other unit, for the pairwise tests. */
    private fun unitPairs(): List<Arguments> =
        dataRateUnitGenerators.flatMap { (_, a) -> dataRateUnitGenerators.map { (_, b) -> Arguments.of(a, b) } }

    // region construction / conversion matrix

    /** Each creator property builds `5 unit`, normalizes to the base value and reads back exactly 5 via `valueAs(unit)`. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `every creator property round trips through valueAs`(unit: KDataRateUnit) {
        val instance = dataRateOf(unit, 5)
        assertEquals(5.0 * unit.baseValue, instance.value, dataRateDelta(5.0 * unit.baseValue), "value mismatch for $unit")
        assertEquals(5.0, instance.valueAs(unit), 5.0 * 1e-9, "valueAs round trip mismatch for $unit")
    }

    /** Converting `5 from` into every other unit yields `5 * from.baseValue / to.baseValue` — the full conversion matrix. */
    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("unitPairs")
    fun `every unit converts into every other unit`(from: KDataRateUnit, to: KDataRateUnit) {
        val expected = 5.0 * from.baseValue / to.baseValue
        assertEquals(expected, dataRateOf(from, 5).valueAs(to), dataRateDelta(expected), "$from -> $to mismatch")
    }

    /** 1 B/s equals exactly 8 bit/s (the defining relation between the two units). */
    @Test
    fun `one byte per second is eight bits per second`() {
        assertEquals(8.0, 1.bytesPerSecond.valueAs(bitsPerSecond), 1e-9)
        assertEquals(1.0, 8.bitsPerSecond.valueAs(bytesPerSecond), 1e-9)
    }

    // endregion

    // region operator matrix (every unit against every other unit)

    /** `a + b` for every unit pair normalizes both operands and returns their sum as a data rate (`[B¹, s⁻¹]`). */
    @ParameterizedTest(name = "{0} + {1}")
    @MethodSource("unitPairs")
    fun `plus combines every pair of units`(a: KDataRateUnit, b: KDataRateUnit) {
        val result = dataRateOf(a, 5) + dataRateOf(b, 3)
        val expected = 5.0 * a.baseValue + 3.0 * b.baseValue
        assertEquals(expected, result.value, dataRateDelta(expected), "$a + $b mismatch")
        assertEquals(setOf(KUnitTerm(KStorageUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -1)), result.toUnit().units.toSet())
    }

    /** `a - b` for every unit pair normalizes both operands and returns their difference as a data rate (`[B¹, s⁻¹]`). */
    @ParameterizedTest(name = "{0} - {1}")
    @MethodSource("unitPairs")
    fun `minus combines every pair of units`(a: KDataRateUnit, b: KDataRateUnit) {
        val result = dataRateOf(a, 5) - dataRateOf(b, 3)
        val expected = 5.0 * a.baseValue - 3.0 * b.baseValue
        assertEquals(expected, result.value, dataRateDelta(expected), "$a - $b mismatch")
        assertEquals(setOf(KUnitTerm(KStorageUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -1)), result.toUnit().units.toSet())
    }

    /** `a * b` for every unit pair multiplies the normalized values and doubles the exponents to `[B², s⁻²]`. */
    @ParameterizedTest(name = "{0} * {1}")
    @MethodSource("unitPairs")
    fun `times combines every pair of units`(a: KDataRateUnit, b: KDataRateUnit) {
        val result = dataRateOf(a, 5) * dataRateOf(b, 3)
        val expected = (5.0 * a.baseValue) * (3.0 * b.baseValue)
        assertEquals(expected, result.value, dataRateDelta(expected), "$a * $b mismatch")
        assertEquals(setOf(KUnitTerm(KStorageUnit.BASE, 2), KUnitTerm(KTimeUnit.BASE, -2)), result.units.toSet())
    }

    /** `a / b` for every unit pair divides the normalized values and cancels the terms to a dimensionless ratio. */
    @ParameterizedTest(name = "{0} / {1}")
    @MethodSource("unitPairs")
    fun `div combines every pair of units into a dimensionless ratio`(a: KDataRateUnit, b: KDataRateUnit) {
        val result = dataRateOf(a, 5) / dataRateOf(b, 3)
        val expected = (5.0 * a.baseValue) / (3.0 * b.baseValue)
        assertEquals(expected, result.value, dataRateDelta(expected), "$a / $b mismatch")
        assertTrue(result.units.isEmpty(), "$a / $b should be dimensionless")
    }

    // endregion

    // region comparison matrix (every unit against every other unit)

    /** `==` is true exactly when the two operands' normalized base values are equal, across every unit pair. */
    @ParameterizedTest(name = "{0} == {1}")
    @MethodSource("unitPairs")
    fun `equals holds exactly when normalized values match`(a: KDataRateUnit, b: KDataRateUnit) {
        assertEquals(dataRateOf(a, 5).value == dataRateOf(b, 3).value, dataRateOf(a, 5) == dataRateOf(b, 3), "$a == $b mismatch")
    }

    /** `!=` is the exact negation of `==` for every unit pair. */
    @ParameterizedTest(name = "{0} != {1}")
    @MethodSource("unitPairs")
    fun `not equals is the negation of equals`(a: KDataRateUnit, b: KDataRateUnit) {
        assertEquals(dataRateOf(a, 5).value != dataRateOf(b, 3).value, dataRateOf(a, 5) != dataRateOf(b, 3), "$a != $b mismatch")
    }

    /** `<` follows the ordering of the normalized base values, across every unit pair. */
    @ParameterizedTest(name = "{0} < {1}")
    @MethodSource("unitPairs")
    fun `less than follows normalized values`(a: KDataRateUnit, b: KDataRateUnit) {
        assertEquals(dataRateOf(a, 5).value < dataRateOf(b, 3).value, dataRateOf(a, 5) < dataRateOf(b, 3), "$a < $b mismatch")
    }

    /** `<=` follows the ordering of the normalized base values, across every unit pair. */
    @ParameterizedTest(name = "{0} <= {1}")
    @MethodSource("unitPairs")
    fun `less than or equal follows normalized values`(a: KDataRateUnit, b: KDataRateUnit) {
        assertEquals(dataRateOf(a, 5).value <= dataRateOf(b, 3).value, dataRateOf(a, 5) <= dataRateOf(b, 3), "$a <= $b mismatch")
    }

    /** `>` follows the ordering of the normalized base values, across every unit pair. */
    @ParameterizedTest(name = "{0} > {1}")
    @MethodSource("unitPairs")
    fun `greater than follows normalized values`(a: KDataRateUnit, b: KDataRateUnit) {
        assertEquals(dataRateOf(a, 5).value > dataRateOf(b, 3).value, dataRateOf(a, 5) > dataRateOf(b, 3), "$a > $b mismatch")
    }

    /** `>=` follows the ordering of the normalized base values, across every unit pair. */
    @ParameterizedTest(name = "{0} >= {1}")
    @MethodSource("unitPairs")
    fun `greater than or equal follows normalized values`(a: KDataRateUnit, b: KDataRateUnit) {
        assertEquals(dataRateOf(a, 5).value >= dataRateOf(b, 3).value, dataRateOf(a, 5) >= dataRateOf(b, 3), "$a >= $b mismatch")
    }

    /** Comparing data rates that normalize equal but were built from different units still compares by value (8 bit/s == 1 B/s). */
    @Test
    fun `comparison is by normalized rate not by constructing unit`() {
        assertTrue(8.bitsPerSecond == 1.bytesPerSecond)
        assertTrue(4.bitsPerSecond < 1.bytesPerSecond)
    }

    // endregion

    // region toString matrix (every unit)

    /** `toString()` with no target renders the normalized value followed by the base-unit symbol (B/s). */
    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString default renders the value in the base unit`(unit: KDataRateUnit) {
        val instance = dataRateOf(unit, 5)
        assertEquals("${instance.value} ${KDataRateUnit.BASE.symbol}", instance.toString())
    }

    /** `toString(unit)` renders the value converted into that unit followed by the unit's own symbol. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString with the own unit renders that symbol`(unit: KDataRateUnit) {
        val instance = dataRateOf(unit, 5)
        assertEquals("${instance.valueAs(unit)} ${unit.symbol}", instance.toString(unit))
    }

    /** `toString(kilo·unit)` renders the value in a decimal-scaled whole-rate target with the prefixed symbol (`kB/s`). */
    @ParameterizedTest(name = "kilo {0}")
    @MethodSource("units")
    fun `toString with a decimal-scaled target renders the prefixed symbol`(unit: KDataRateUnit) {
        val instance = dataRateOf(unit, 5000)
        val scaled = KUnitPrefix.KILO with unit
        assertEquals("${instance.valueAs(scaled)} k${unit.symbol}", instance.toString(scaled))
    }

    /** `toString(kibi·unit)` renders the value in a binary-scaled whole-rate target with the binary-prefixed symbol (`KiB/s`). */
    @ParameterizedTest(name = "kibi {0}")
    @MethodSource("units")
    fun `toString with a binary-scaled target renders the binary-prefixed symbol`(unit: KDataRateUnit) {
        val instance = dataRateOf(unit, 4096)
        val scaled = KStorageBinaryPrefix.KIBI with unit
        assertEquals("${instance.valueAs(scaled)} Ki${unit.symbol}", instance.toString(scaled))
    }

    /** `toString(byte, second)` renders the value as a storage-per-time pair with the `B*s^-1` symbol. */
    @ParameterizedTest(name = "{0}")
    @MethodSource("units")
    fun `toString with a storage-per-time pair target renders the composed symbol`(unit: KDataRateUnit) {
        val instance = dataRateOf(unit, 5)
        val pair = instance.valueAs(KUnitPrefix.KILO with KStorageUnit.BYTE, KTimeUnit.SECOND)
        assertEquals("$pair kB*s^-1", instance.toString(KUnitPrefix.KILO with KStorageUnit.BYTE, KTimeUnit.SECOND))
    }

    // endregion

    // region concrete conversions and group-specific behaviour

    /** The creator property accepts any `Number` (`Int`/`Long`/`Float`/`Double`) and always normalizes to the same Double value. */
    @Test
    fun `construction from non-Double Number types`() {
        assertEquals(10.0, 10.bytesPerSecond.value, 1e-9)
        assertEquals(10.0, 10L.bytesPerSecond.value, 1e-9)
        assertEquals(10.0, 10.0f.bytesPerSecond.value, 1e-9)
        assertEquals(10.0, 10.0.bytesPerSecond.value, 1e-9)
    }

    /** A binary-scaled whole-rate target reads back correctly (`4096 B/s == 4 KiB/s`). */
    @Test
    fun `binary-scaled whole-rate target reads back correctly`() {
        assertEquals(4.0, 4096.bytesPerSecond.valueAs(KStorageBinaryPrefix.KIBI with bytesPerSecond), 1e-9)
        assertEquals("4.0 KiB/s", 4096.bytesPerSecond.toString(KStorageBinaryPrefix.KIBI with bytesPerSecond))
    }

    /** `valueAs(storage, time)` accepts the two composed targets in either order and yields the same result. */
    @Test
    fun `valueAs as a storage per time pair works in any order`() {
        val r = 5000.bytesPerSecond
        assertEquals(5.0, r.valueAs(KUnitPrefix.KILO with KStorageUnit.BYTE, KTimeUnit.SECOND), 1e-9)
        assertEquals(5.0, r.valueAs(KTimeUnit.SECOND, KUnitPrefix.KILO with KStorageUnit.BYTE), 1e-9)
    }

    /** A data rate decomposed to a `KMixedUnitInstance` via `toUnit()` and recomposed via `toDataRate()` equals the original. */
    @Test
    fun `toUnit and toDataRate round trip`() {
        val original = 500.bitsPerSecond

        val roundTripped = original.toUnit().toDataRate()

        assertEquals(original, roundTripped)
    }

    /** `toDataRate()` on a raw `[BIT¹, HOUR⁻¹]` mixed unit normalizes both terms and reads back correctly. */
    @Test
    fun `toDataRate normalizes non-base storage and time terms`() {
        // a raw [BIT^1, HOUR^-1] instance: 3600 bit per hour == 0.125 B/s
        val perHour = KMixedUnitInstance(3600.0, listOf(KUnitTerm(KStorageUnit.BIT, 1), KUnitTerm(KTimeUnit.HOUR, -1)))

        assertEquals(1.0, perHour.toDataRate().valueAs(KDataRateUnit.BITS_PER_SECOND), 1e-9)
    }

    /** `toDataRate()` on a mixed unit that is not a storage-per-time shape (e.g. `B²`) throws `IllegalStateException`. */
    @Test
    fun `toDataRate fails for a non-rate mixed unit`() {
        val squared = KMixedUnitInstance(5.0, listOf(KUnitTerm(KStorageUnit.BASE, 2)))

        assertFailsWith<IllegalStateException> { squared.toDataRate() }
    }

    /** `toDataRate()` on a `[B², s⁻¹]` shape throws — the storage exponent must be exactly 1. */
    @Test
    fun `toDataRate fails when the storage exponent is not one`() {
        val squaredPerTime = KMixedUnitInstance(5.0, listOf(KUnitTerm(KStorageUnit.BASE, 2), KUnitTerm(KTimeUnit.BASE, -1)))

        assertFailsWith<IllegalStateException> { squaredPerTime.toDataRate() }
    }

    /** `toDataRate()` on a `[B¹, s¹]` shape throws — the time exponent must be exactly −1. */
    @Test
    fun `toDataRate fails when the time exponent is not minus one`() {
        val storageTime = KMixedUnitInstance(5.0, listOf(KUnitTerm(KStorageUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, 1)))

        assertFailsWith<IllegalStateException> { storageTime.toDataRate() }
    }

    // endregion
}
