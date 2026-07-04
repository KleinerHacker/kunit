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

package org.pcsoft.framework.kunit.speed

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.length.KLengthUnit
import org.pcsoft.framework.kunit.length.KLengthUnitInstance
import org.pcsoft.framework.kunit.length.lengthOf
import org.pcsoft.framework.kunit.length.lengthUnitGenerators
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.time.timeOf
import org.pcsoft.framework.kunit.time.timeUnitGenerators
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Cross-group tests for the *constructed* speed unit: every core length unit combined with every core
 * time unit, verifying the **bidirectional** decomposition required for composed units - from the core
 * units *into* the speed, and from the speed back *into* every core unit of both involved groups.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KSpeedMixedUnitTest {

    private fun lengthTimePairs(): List<Arguments> =
        lengthUnitGenerators.flatMap { (_, l) -> timeUnitGenerators.map { (_, t) -> Arguments.of(l, t) } }

    private fun lengths(): List<Arguments> = lengthUnitGenerators.map { Arguments.of(it.second) }

    private fun times(): List<Arguments> = timeUnitGenerators.map { Arguments.of(it.second) }

    private fun delta(expected: Double): Double = (abs(expected) * 1e-9).coerceAtLeast(1e-12)

    // region core -> composed (length / time == speed)

    @ParameterizedTest(name = "{0} / {1}")
    @MethodSource("lengthTimePairs")
    fun `dividing a length by a time yields a typed speed`(length: KLengthUnit, time: KTimeUnit) {
        val speed: KSpeedUnitInstance = lengthOf(length, 10) / timeOf(time, 2)

        val expected = 10.0 * length.baseValue / (2.0 * time.baseValue)
        assertEquals(expected, speed.value, delta(expected), "$length / $time value mismatch")
        assertEquals(expected, speedOf(KSpeedUnit.METERS_PER_SECOND, expected).value, delta(expected))
        assertEquals(
            setOf(KUnitTerm(KLengthUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -1)),
            speed.toKMixedUnitInstance().units.toSet(),
            "$length / $time term signature mismatch"
        )
    }

    @ParameterizedTest(name = "{0} / {1}")
    @MethodSource("lengthTimePairs")
    fun `the raw mixed division agrees with toKSpeedUnit`(length: KLengthUnit, time: KTimeUnit) {
        val viaOperator = lengthOf(length, 10) / timeOf(time, 2)
        val viaConversion = (lengthOf(length, 10).toKMixedUnitInstance() / timeOf(time, 2).toKMixedUnitInstance()).toKSpeedUnit()

        assertEquals(viaConversion.value, viaOperator.value, delta(viaConversion.value), "$length / $time mismatch")
    }

    // endregion

    // region composed -> core (speed * time == length, read back into every length unit)

    @ParameterizedTest(name = "speed * time read back in {0}")
    @MethodSource("lengths")
    fun `speed times time decomposes back into every length unit`(readBack: KLengthUnit) {
        val speed = speedOf(KSpeedUnit.KILOMETERS_PER_HOUR, 36) // 10 m/s

        timeUnitGenerators.forEach { (_, timeUnit) ->
            val distance: KLengthUnitInstance = speed * timeOf(timeUnit, 5)
            val expectedMeters = 10.0 * (5.0 * timeUnit.baseValue)
            assertEquals(expectedMeters, distance.value, delta(expectedMeters), "value mismatch for $timeUnit")
            // read the resulting length back into the parametrized unit
            val expectedReadBack = expectedMeters / readBack.baseValue
            assertEquals(expectedReadBack, distance.valueAs(readBack), delta(expectedReadBack),
                "read-back mismatch into $readBack for $timeUnit")
            assertEquals(listOf(KUnitTerm(KLengthUnit.BASE, 1)), distance.toKMixedUnitInstance().units)
        }
    }

    // endregion

    // region composed -> core (length / speed == time, read back into every time unit)

    @ParameterizedTest(name = "length / speed read back in {0}")
    @MethodSource("times")
    fun `length divided by speed decomposes back into every time unit`(readBack: KTimeUnit) {
        val speed = speedOf(KSpeedUnit.METERS_PER_SECOND, 10)

        lengthUnitGenerators.forEach { (_, lengthUnit) ->
            val time: KTimeUnitInstance = lengthOf(lengthUnit, 100) / speed
            val expectedSeconds = 100.0 * lengthUnit.baseValue / 10.0
            assertEquals(expectedSeconds, time.value, delta(expectedSeconds), "value mismatch for $lengthUnit")
            val expectedReadBack = expectedSeconds / readBack.baseValue
            assertEquals(expectedReadBack, time.valueAs(readBack), delta(expectedReadBack),
                "read-back mismatch into $readBack for $lengthUnit")
            assertEquals(listOf(KUnitTerm(KTimeUnit.BASE, 1)), time.toKMixedUnitInstance().units)
        }
    }

    // endregion

    // region commutativity and error cases

    @Test
    fun `time times speed equals speed times time`() {
        val speed = speedOf(KSpeedUnit.METERS_PER_SECOND, 10)

        assertEquals((speed * timeOf(KTimeUnit.SECOND, 60)).value, (timeOf(KTimeUnit.SECOND, 60) * speed).value, 1e-9)
    }

    @Test
    fun `dividing an area by a time is not a speed`() {
        // an area (m^2) divided by a time is m^2/s, not a speed - must fail rather than mislead
        val areaPerTime = KMixedUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.BASE, 2), KUnitTerm(KTimeUnit.BASE, -1)))

        assertFailsWith<IllegalStateException> { areaPerTime.toKSpeedUnit() }
    }

    @Test
    fun `a plain length is not a speed`() {
        assertFailsWith<IllegalStateException> { lengthOf(KLengthUnit.METER, 5).toKMixedUnitInstance().toKSpeedUnit() }
    }

    // endregion
}
