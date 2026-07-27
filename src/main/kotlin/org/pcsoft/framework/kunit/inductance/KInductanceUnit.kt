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

package org.pcsoft.framework.kunit.inductance

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **inductance** (a *constructed* quantity: `mass¹ · distance² · time⁻² ·
 * current⁻²`, i.e. `kg·m²·s⁻²·A⁻²`). [baseValue] is the factor to convert into the group's base unit
 * ([BASE], henry): `1 unit = baseValue * H`.
 *
 * Unlike length or mass, inductance is not a "real" single-group unit - it is composed of a mass, a length,
 * a time and a current term. Each [KInductanceUnit] therefore carries a single, pre-computed factor to
 * henries (e.g. `1 abH = 1e-9 H`), so it can still be used as a plain [KUnit]/target.
 *
 * Example:
 * ```kotlin
 * KInductanceUnit.ABHENRY.baseValue // 1.0e-9 (1 abH = 1e-9 H)
 * ```
 */
enum class KInductanceUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Henry ("H"): the SI derived unit and base unit of the inductance group ([baseValue] `1.0`). */
    HENRY("H", 1.0),

    /** Weber per ampere ("Wb/A"), the explicit `Φ/I` spelling of the henry, 1 Wb/A = 1 H. */
    WEBER_PER_AMPERE("Wb/A", 1.0),

    /** Abhenry ("abH"), the inductance unit of the CGS electromagnetic system (EMU), 1 abH = 1e-9 H. */
    ABHENRY("abH", 1e-9),

    /** Stathenry ("statH"), the inductance unit of the CGS electrostatic system (ESU), 1 statH = 8.987551787e11 H. */
    STATHENRY("statH", 8.987551787e11);

    companion object {
        /**
         * The base unit of the inductance group: [HENRY]. All internal values of [KInductanceUnitInstance]
         * are normalized to this unit.
         */
        val BASE: KInductanceUnit = HENRY
    }
}
