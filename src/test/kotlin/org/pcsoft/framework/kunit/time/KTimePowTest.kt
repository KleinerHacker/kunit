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

package org.pcsoft.framework.kunit.time

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.pow
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * `pow` cross-matrix for the time group. Time has no dimensioned power wrapper (no "time²" type), so `pow`
 * resolves to the group-agnostic `KUnitMeasurable.pow` and yields a generic [KMixedUnitInstance] carrying
 * the powered value and the [KTimeUnit.BASE] term at the multiplied exponent. This proves `pow` is
 * available on a non-distance group.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KTimePowTest {

    private fun units(): List<Arguments> = timeUnitGenerators.map { Arguments.of(it.second) }

    /** `n.<time> pow 2` squares `(3·baseValue)` and produces a `[s²]` mixed unit - for every time unit. */
    @ParameterizedTest(name = "{0} pow 2")
    @MethodSource("units")
    fun `every time pow 2 yields a squared-seconds mixed unit`(unit: KTimeUnit) {
        val result: KMixedUnitInstance = timeOf(unit, 3) pow 2
        val expected = (3.0 * unit.baseValue).pow(2)
        assertEquals(expected, result.value, timeDelta(expected))
        assertEquals(listOf(KUnitTerm(KTimeUnit.BASE, 2)), result.units)
    }

    /** `n.<time> pow 3` cubes `(2·baseValue)` and produces a `[s³]` mixed unit - for every time unit. */
    @ParameterizedTest(name = "{0} pow 3")
    @MethodSource("units")
    fun `every time pow 3 yields a cubed-seconds mixed unit`(unit: KTimeUnit) {
        val result: KMixedUnitInstance = timeOf(unit, 2) pow 3
        val expected = (2.0 * unit.baseValue).pow(3)
        assertEquals(expected, result.value, timeDelta(expected))
        assertEquals(listOf(KUnitTerm(KTimeUnit.BASE, 3)), result.units)
    }

    /** `pow 0` on a time collapses to a dimensionless unit of value 1. */
    @Test
    fun `time pow 0 is dimensionless one`() {
        val result = 2.hours pow 0
        assertEquals(1.0, result.value, 1e-12)
        assertTrue(result.units.isEmpty())
    }

    /** A prefixed time powers correctly: `(3 milli seconds) pow 2 = (0.003 s)²`. */
    @Test
    fun `prefixed time pow 2`() {
        val result = 3 milli seconds pow 2
        val expected = 0.003.pow(2)
        assertEquals(expected, result.value, 1e-15)
        assertEquals(listOf(KUnitTerm(KTimeUnit.BASE, 2)), result.units)
    }
}
