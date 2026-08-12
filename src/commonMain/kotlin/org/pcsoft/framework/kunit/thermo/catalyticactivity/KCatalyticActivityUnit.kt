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

package org.pcsoft.framework.kunit.thermo.catalyticactivity

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **catalytic activity** (amount of substance converted per time; a
 * *constructed* quantity: substance · time⁻¹). [baseValue] is the factor to convert into the group's base
 * unit ([BASE], katal): `1 unit = baseValue * kat`.
 *
 * Example:
 * ```kotlin
 * KCatalyticActivityUnit.ENZYME_UNIT.baseValue // ≈ 1.6667e-8 (1 U = 1 µmol/min)
 * ```
 */
enum class KCatalyticActivityUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Katal ("kat"), the coherent SI unit of catalytic activity (1 kat = 1 mol/s); [baseValue] = 1.0. */
    KATAL("kat", 1.0),

    /**
     * Enzyme unit ("U"), the traditional biochemistry unit: the activity converting one micromole of
     * substrate per minute. 1 U = 1 µmol/min = 1/60 µkat ≈ 1.6667e-8 kat.
     */
    ENZYME_UNIT("U", 1.0e-6 / 60.0);

    companion object {
        /**
         * The base unit of the catalytic-activity group: [KATAL]. All internal values of
         * [KCatalyticActivityUnitInstance] are normalized to katals.
         */
        val BASE: KCatalyticActivityUnit = KATAL
    }
}
