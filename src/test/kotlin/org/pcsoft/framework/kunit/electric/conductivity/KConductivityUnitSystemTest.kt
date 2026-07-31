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

package org.pcsoft.framework.kunit.electric.conductivity

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.of
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

/**
 * `KConductivityUnitInstance` surface: `of`/`into` construction and round-trip, equality, `toString`,
 * decomposition guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KConductivityUnitSystemTest {

    private val tokens: List<Pair<KConductivityUnitInstance, Double>> = listOf(
        siemensPerMeter to KConductivityUnit.SIEMENS_PER_METER.baseValue,
        siemensPerCentimeter to KConductivityUnit.SIEMENS_PER_CENTIMETER.baseValue,
        microsiemensPerCentimeter to KConductivityUnit.MICROSIEMENS_PER_CENTIMETER.baseValue,
        megasiemensPerMeter to KConductivityUnit.MEGASIEMENS_PER_METER.baseValue,
    )

    private fun rel(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-30)
    private fun tokenArgs(): List<Array<Any>> = tokens.map { arrayOf<Any>(it.first, it.second) }

    /** `n of token` normalizes to siemens per meter and round-trips through `into`. */
    @ParameterizedTest
    @MethodSource("tokenArgs")
    fun `construction and round-trip`(token: KConductivityUnitInstance, base: Double) {
        assertEquals(4.0 * base, (4 of token).value, rel(4.0 * base))
        assertEquals(4.0, (4 of token) into token, rel(4.0))
    }

    /** Equality/hash by normalized S/m value (`1 S/cm == 100 S/m`). */
    @Test
    fun `equals and hashCode`() {
        assertEquals(1 of siemensPerCentimeter, 100 of siemensPerMeter)
        assertEquals((1 of siemensPerCentimeter).hashCode(), (100 of siemensPerMeter).hashCode())
        assertFalse((1 of siemensPerMeter) == (2 of siemensPerMeter))
        assertFalse((1 of siemensPerMeter).equals(1.0)) // not a KConductivityUnitInstance
    }

    /** `toString` renders the normalized S/m value. */
    @Test
    fun `toString base unit`() {
        assertEquals("100.0 S/m", (1 of siemensPerCentimeter).toString())
    }

    /** The real-world material reading: copper conducts at 58 MS/m. */
    @Test
    fun `copper conductivity real world`() {
        assertEquals(5.8e7, (58 of mega.siemensPerMeter).value, 1e-3)
        assertEquals(58.0, (58 of mega.siemensPerMeter) into megasiemensPerMeter, 1e-9)
    }

    /** A mixed unit that is not a canonical conductivity normal form cannot be converted to a conductivity. */
    @Test
    fun `toConductivity on non-conductivity fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toConductivity() }
        assertFailsWith<IllegalStateException> {
            ((1 of siemensPerMeter).toUnit() * (1 of siemensPerMeter).toUnit()).toConductivity()
        }
    }
}
