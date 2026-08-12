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

package org.pcsoft.framework.kunit.kinematic.time

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import java.time.Duration
import java.time.temporal.ChronoUnit
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The `java.time` interop of `KTimeUnitInstance`, which lives in the JVM source set: conversion in both
 * directions plus the `Duration`/`TemporalUnit`-typed copy operations.
 */
class KTimeUnitInstanceJvmTest {

    /** A java.time.Duration round-trips through toTime()/toDuration(). */
    @Test
    fun `duration round trip`() {
        assertEquals(90.0 * 60, Duration.ofMinutes(90).toTime().value, 1e-9)
        assertEquals(1.5, Duration.ofMinutes(90).toTime() into hours, 1e-9)
        assertEquals(Duration.ofMinutes(90), (90 of minutes).toDuration())
    }

    /** Sub-second precision survives the conversion in both directions. */
    @Test
    fun `duration round trip keeps nanoseconds`() {
        val duration = Duration.ofSeconds(3, 500)
        assertEquals(duration, duration.toTime().toDuration())
    }

    /** The Duration- and TemporalUnit-typed copy operations. */
    @Test
    fun `duration typed copies`() {
        val t = 90 of minutes // 5400 s

        assertEquals(5460.0, t.plus(Duration.ofMinutes(1)).value, 1e-9)
        assertEquals(5460.0, t.plus(1, ChronoUnit.MINUTES).value, 1e-9)
        assertEquals(5340.0, t.minus(Duration.ofMinutes(1)).value, 1e-9)
        assertEquals(5340.0, t.minus(1, ChronoUnit.MINUTES).value, 1e-9)
        assertEquals(5400.0, t.truncatedTo(ChronoUnit.SECONDS).value, 1e-9)
        assertEquals(3600.0, t.truncatedTo(ChronoUnit.HOURS).value, 1e-9)
    }
}
