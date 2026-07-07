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

import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.storage.KStorageBinaryPrefix
import org.pcsoft.framework.kunit.storage.with
import org.pcsoft.framework.kunit.with
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Prefix × unit cross-matrix for the data-rate group. Mirroring the storage group (its numerator), two
 * prefix systems are exercised through the bare-value DSL (`5 kilo bytesPerSecond`, `5 kibi bytesPerSecond`):
 *
 *  * the **non-diminishing** decimal SI prefixes (`deca` … `quetta`, factor >= 1); the diminishing ones
 *    (`deci` downward) intentionally do not exist for the data-rate group (a compile-time guarantee), and
 *  * the **binary** IEC prefixes (`kibi` … `yobi`, powers of 1024), reused from the storage group.
 *
 * Each `ops` list captures the prefix `infix` functions as `(expectedFactor, (count, unit) -> instance)`
 * pairs: the lambda is the code under test (`{ n, u -> n kilo u }`), the factor is computed independently.
 */
class KDataRatePrefixTest {

    // The 12 non-diminishing decimal prefix infix functions (factor >= 1), keyed by their SI factor.
    private val decimalOps: List<Pair<Double, (Number, KDataRateUnit) -> KDataRateUnitInstance>> = listOf(
        KUnitPrefix.QUETTA.factor to { n, u -> n quetta u },
        KUnitPrefix.RONNA.factor to { n, u -> n ronna u },
        KUnitPrefix.YOTTA.factor to { n, u -> n yotta u },
        KUnitPrefix.ZETTA.factor to { n, u -> n zetta u },
        KUnitPrefix.EXA.factor to { n, u -> n exa u },
        KUnitPrefix.PETA.factor to { n, u -> n peta u },
        KUnitPrefix.TERA.factor to { n, u -> n tera u },
        KUnitPrefix.GIGA.factor to { n, u -> n giga u },
        KUnitPrefix.MEGA.factor to { n, u -> n mega u },
        KUnitPrefix.KILO.factor to { n, u -> n kilo u },
        KUnitPrefix.HECTO.factor to { n, u -> n hecto u },
        KUnitPrefix.DECA.factor to { n, u -> n deca u },
    )

    // The 8 binary IEC prefix infix functions (powers of 1024), keyed by their factor.
    private val binaryOps: List<Pair<Double, (Number, KDataRateUnit) -> KDataRateUnitInstance>> = listOf(
        KStorageBinaryPrefix.KIBI.factor to { n, u -> n kibi u },
        KStorageBinaryPrefix.MEBI.factor to { n, u -> n mebi u },
        KStorageBinaryPrefix.GIBI.factor to { n, u -> n gibi u },
        KStorageBinaryPrefix.TEBI.factor to { n, u -> n tebi u },
        KStorageBinaryPrefix.PEBI.factor to { n, u -> n pebi u },
        KStorageBinaryPrefix.EXBI.factor to { n, u -> n exbi u },
        KStorageBinaryPrefix.ZEBI.factor to { n, u -> n zebi u },
        KStorageBinaryPrefix.YOBI.factor to { n, u -> n yobi u },
    )

    // Relative tolerance across the huge magnitude span (deca … quetta, kibi … yobi).
    private fun delta(e: Double) = (abs(e) * 1e-9).coerceAtLeast(1e-300)

    /** Every non-diminishing decimal prefix applied to every data-rate unit via `n <prefix> unit` yields `count * factor * baseValue` B/s. */
    @Test
    fun `decimal prefix x unit matrix`() {
        for ((factor, op) in decimalOps) for (unit in dataRateBareValues) {
            val expected = 3.0 * factor * unit.baseValue
            assertEquals(expected, op(3, unit).value, delta(expected), "decimal $factor x $unit")
        }
    }

    /** Every binary prefix applied to every data-rate unit via `n <prefix> unit` yields `count * factor * baseValue` B/s. */
    @Test
    fun `binary prefix x unit matrix`() {
        for ((factor, op) in binaryOps) for (unit in dataRateBareValues) {
            val expected = 3.0 * factor * unit.baseValue
            assertEquals(expected, op(3, unit).value, delta(expected), "binary $factor x $unit")
        }
    }

    /** Each decimal prefix applied to 1 B/s equals exactly its numeric factor — isolates the prefix from the unit. */
    @Test
    fun `every decimal prefix standalone against bytes per second`() {
        for ((factor, op) in decimalOps) {
            assertEquals(factor, op(1, bytesPerSecond).value, delta(factor), "decimal $factor")
        }
    }

    /** Each binary prefix applied to 1 B/s equals exactly its numeric factor — isolates the prefix from the unit. */
    @Test
    fun `every binary prefix standalone against bytes per second`() {
        for ((factor, op) in binaryOps) {
            assertEquals(factor, op(1, bytesPerSecond).value, delta(factor), "binary $factor")
        }
    }

    /** A prefix constructed value round-trips: reading `1 kibi bytesPerSecond` back as KiB/s via `valueAs` yields 1.0. */
    @Test
    fun `binary prefix round trips through valueAs`() {
        assertEquals(1.0, (1 kibi bytesPerSecond).valueAs(KStorageBinaryPrefix.KIBI with bytesPerSecond), 1e-9)
        assertEquals(1.0, (1 mebi bytesPerSecond).valueAs(KStorageBinaryPrefix.MEBI with bytesPerSecond), 1e-9)
    }

    /**
     * The whole point of the binary prefixes: the decimal step (1000) and the binary step (1024) differ,
     * so `1 kilo bytesPerSecond` (1000 B/s) is not `1 kibi bytesPerSecond` (1024 B/s), and likewise mega vs mebi.
     */
    @Test
    fun `decimal and binary prefixes differ`() {
        assertEquals(1000.0, (1 kilo bytesPerSecond).value, 1e-9)
        assertEquals(1024.0, (1 kibi bytesPerSecond).value, 1e-9)
        assertEquals(1_000_000.0, (1 mega bytesPerSecond).value, 1e-3)
        assertEquals(1_048_576.0, (1 mebi bytesPerSecond).value, 1e-3)
    }

    /** The prefix `infix` form `5 kilo bytesPerSecond` is exactly equivalent to the scaled creator `5000.bytesPerSecond`. */
    @Test
    fun `decimal prefix infix equals scaled creator`() {
        assertEquals(5000.bytesPerSecond.value, (5 kilo bytesPerSecond).value, 1e-9)
    }

    /** The prefix `infix` form returns a typed [KDataRateUnitInstance] with the canonical `[B¹, s⁻¹]` term signature. */
    @Test
    fun `prefix infix returns a KDataRateUnitInstance directly`() {
        val r: KDataRateUnitInstance = 5 mega bytesPerSecond
        assertEquals(5_000_000.0, r.value, 1e-3)
        assertEquals(5.0, r.valueAs(KUnitPrefix.MEGA with KDataRateUnit.BYTES_PER_SECOND), 1e-9)
    }
}
