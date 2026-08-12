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

package org.pcsoft.framework.kunit.kinematic.frequency

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import org.pcsoft.framework.kunit.forEachCase
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of

/**
 * The concrete frequency units (`KFrequencyUnitBareValues` + `KFrequencyUnitExtensions`): every unit
 * builds to its hertz value, round-trips through `into`, and reads correctly against hertz. Covers all
 * bare tokens.
 */
class KFrequencyUnitTest {

    private val tokens: List<Triple<String, KFrequencyUnitInstance, Double>> = listOf(
        Triple("hertz", hertz, KFrequencyUnit.HERTZ.baseValue),
        Triple("rps", rps, KFrequencyUnit.RPS.baseValue),
        Triple("fps", fps, KFrequencyUnit.FPS.baseValue),
        Triple("rpm", rpm, KFrequencyUnit.RPM.baseValue),
        Triple("bpm", bpm, KFrequencyUnit.BPM.baseValue),
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-15)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second, it.third) }

    /** Each token builds to its documented hertz value and round-trips through `into`. */
    @Test
    fun `unit conversion`() = forEachCase(tokenArgs()) { case ->
        `unit conversion`(case[0] as String, case[1] as KFrequencyUnitInstance, case[2] as Double)
    }

    private fun `unit conversion`(name: String, token: KFrequencyUnitInstance, base: Double) {
        assertEquals(base, (1 of token).value, rel(base))
        assertEquals(base, (1 of token) into hertz, rel(base))
        assertEquals(1.0, (1 of token) into token, rel(1.0))
    }
}
