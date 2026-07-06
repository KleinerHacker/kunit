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

package org.pcsoft.framework.kunit

import org.pcsoft.framework.kunit.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.speed.KSpeedUnitInstance
import org.pcsoft.framework.kunit.speed.div
import org.pcsoft.framework.kunit.storage.KStorageUnit
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * Verifies the group-agnostic cross-group `*`/`/` extension operators
 * ([KUnitInstance.times]/[div], defined in `KUnitMeasurable.kt`) with **both** the generic and the
 * statically-typed cross-group operators (speed) in scope at once, so overload resolution is exercised
 * exactly as a real caller sees it. The generic operators are in scope here without import (same root
 * package); the typed speed operators are imported.
 */
class KCrossGroupOperatorTest {

    /**
     * Two pure units of different groups without a dedicated operator combine directly (no `toUnit()`)
     * into a `KMixedUnitInstance` with the correct value and term signature.
     */
    @Test
    fun `storage over time is a mixed unit`() {
        val rate = 1000.bytes / 2.seconds
        assertIs<KMixedUnitInstance>(rate)
        assertEquals(500.0, rate.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KStorageUnit.BASE, 1), KUnitTerm(KTimeUnit.SECOND, -1)), rate.units)
    }

    /**
     * Regression: with the generic operator in scope, the more specific typed speed extension still
     * wins - `length / time` remains a `KSpeedUnitInstance`, not degraded to `KMixedUnitInstance`.
     */
    @Test
    fun `typed speed operator wins over the generic one`() {
        val speed = 100.meters / 5.seconds
        assertIs<KSpeedUnitInstance>(speed)
        assertEquals(20.0, speed.value, 1e-9)
    }

    /**
     * Regression: with the generic operator in scope, the member `length * length` still wins and
     * returns a `KAreaUnitInstance`.
     */
    @Test
    fun `member area operator wins over the generic one`() {
        val area = 3.meters * 4.meters
        assertIs<KAreaUnitInstance>(area)
        assertEquals(12.0, area.value, 1e-9)
    }
}
