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

package org.pcsoft.framework.kunit.it.storagedensity

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of storage density (a *constructed* quantity: storage · distance⁻²).
 * [baseValue] is the factor to convert into the group's base unit ([BASE], byte per square meter):
 * `1 unit = baseValue * B/m²`.
 *
 * Like data rate, storage density is not a "real" single-group unit - it is composed of a storage and a
 * (squared) distance term (`B·m⁻²`). Each [KStorageDensityUnit] therefore carries a single, pre-computed
 * factor to B/m² (e.g. `1 bit/m² = 0.125 B/m²`), so it can still be used as a plain [KUnit]/target.
 * Further scaling (kB/mm², Gbit/m², …) is produced through prefixed storage numerators and squared,
 * prefixed distance denominators in the source expression.
 *
 * Example:
 * ```kotlin
 * KStorageDensityUnit.BITS_PER_SQUARE_METER.baseValue // 0.125 (1 bit/m² = 1/8 B/m²)
 * ```
 */
enum class KStorageDensityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Byte per square meter, the base unit of the storage-density group; [baseValue] = 1.0 by definition. */
    BYTES_PER_SQUARE_METER("B/m²", 1.0),

    /** Bit per square meter, 1 bit/m² = 1/8 B/m² = 0.125 B/m². */
    BITS_PER_SQUARE_METER("bit/m²", 0.125);

    companion object {
        /**
         * The base unit of the storage-density group; all internal values of [KStorageDensityUnitInstance]
         * are normalized to this unit (byte per square meter).
         */
        val BASE: KStorageDensityUnit = BYTES_PER_SQUARE_METER
    }
}
