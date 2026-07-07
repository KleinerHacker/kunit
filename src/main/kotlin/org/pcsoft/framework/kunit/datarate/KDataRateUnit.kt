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

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of data rate (a *constructed* quantity: storage · time⁻¹). [baseValue] is
 * the factor to convert into the group's base unit ([BASE], byte per second): `1 unit = baseValue * B/s`.
 *
 * Unlike storage or time, data rate is not a "real" single-group unit - it is composed of a storage and
 * a time term (`B·s⁻¹`). Each [KDataRateUnit] therefore carries a single, pre-computed factor to B/s
 * (e.g. `1 bit/s = 0.125 B/s`), so it can still be used as a plain [KUnit]/target. Further scaling
 * (kB/s, Mbit/s, KiB/s, …) is provided exclusively through the prefix DSL in `KDataRateUnitPrefix.kt`.
 *
 * Example:
 * ```kotlin
 * KDataRateUnit.BITS_PER_SECOND.baseValue // 0.125 (1 bit/s = 1/8 B/s)
 * ```
 */
enum class KDataRateUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Byte per second, the base unit of the data-rate group; [baseValue] = 1.0 by definition. */
    BYTES_PER_SECOND("B/s", 1.0),

    /** Bit per second (`bps`), 1 bit/s = 1/8 B/s = 0.125 B/s (the networking-native unit). */
    BITS_PER_SECOND("bit/s", 0.125);

    companion object {
        /**
         * The base unit of the data-rate group; all internal values of [KDataRateUnitInstance] are
         * normalized to this unit (byte per second).
         */
        val BASE: KDataRateUnit = BYTES_PER_SECOND
    }
}
