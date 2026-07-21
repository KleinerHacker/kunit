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

import org.pcsoft.framework.kunit.KPrefixBuilder
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.of
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/** The SI prefix builders on the time units: standalone scaling and prefixed read-back. */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KTimePrefixTest {

    private val prefixes: List<Pair<KPrefixBuilder, Double>> =
        listOf(kilo to 1e3, milli to 1e-3, micro to 1e-6, nano to 1e-9)

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-12)
    private fun prefixArgs(): List<Array<Any>> = prefixes.map { arrayOf<Any>(it.first, it.second) }

    /** Every prefix builder scales a seconds template by its factor (`1 of milli.seconds == 0.001 s`). */
    @ParameterizedTest
    @MethodSource("prefixArgs")
    fun `prefix standalone`(builder: KPrefixBuilder, factor: Double) {
        assertEquals(factor, (1 of builder.seconds).value, rel(factor))
    }

    /** Prefixed time reads back correctly: 5000 s read in kilo.seconds is 5. */
    @Test
    fun `prefixed read`() {
        assertEquals(5.0, (5000 of seconds) into kilo.seconds, 1e-9)
        assertEquals(2.0, (0.002 of seconds) into milli.seconds, 1e-9)
    }

    /** The prefixed extensions for the non-second time units resolve via the `kilo` builder. */
    @Test
    fun `prefixed minutes hours days`() {
        assertEquals(1000.0 * KTimeUnit.MINUTE.baseValue, (1 of kilo.minutes).value, 1e-6)
        assertEquals(1000.0 * KTimeUnit.HOUR.baseValue, (1 of kilo.hours).value, 1e-3)
        assertEquals(1000.0 * KTimeUnit.DAY.baseValue, (1 of kilo.days).value, 1e-3)
    }
}
