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

package org.pcsoft.framework.kunit.force

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of force (a *constructed* quantity: mass · length · time⁻²). [baseValue] is
 * the factor to convert into the group's base unit ([BASE], newton): `1 unit = baseValue * N`.
 *
 * Force is a *constructed*, three-term unit (`kg·m·s⁻²`). Because the mass component of this library is
 * normalized to **grams** (not kilograms), the newton is 1000× the raw component base
 * (`g·m·s⁻²`); that fixed factor is [org.pcsoft.framework.kunit.force.N_IN_BASE] and is applied by the
 * [KForceUnitInstance] factory, not by these [baseValue]s (which stay relative to the newton).
 *
 * The **kilopond (kp) / kilogram-force (kgf) is intentionally not a dedicated entry**: it is reached
 * generically through the SI prefix table applied to [POND] (`kilo.ponds`), just like the kilonewton is
 * `kilo.newtons`.
 *
 * Example:
 * ```kotlin
 * KForceUnit.POND.baseValue // 0.009 806 65 (1 p = 9.806 65 mN)
 * ```
 */
enum class KForceUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Newton ("N"), the SI base unit of force; [baseValue] = 1.0 by definition (1 N = 1 kg·m/s²). */
    NEWTON("N", 1.0),

    /** Dyne ("dyn"), the CGS unit of force, 1 dyn = 1 g·cm/s² = 10⁻⁵ N. */
    DYNE("dyn", 1.0e-5),

    /** Pound-force ("lbf"), 1 lbf = 4.448 221 615 260 5 N. */
    POUND_FORCE("lbf", 4.4482216152605),

    /** Pond / gram-force ("p"), 1 p = 9.806 65 × 10⁻³ N; the kilopond (kp = kgf) is `kilo.ponds`. */
    POND("p", 9.80665e-3);

    companion object {
        /**
         * The base unit of the force group: [NEWTON]. All internal values of [KForceUnitInstance] are
         * normalized to newtons (via the [org.pcsoft.framework.kunit.force.N_IN_BASE] component factor).
         */
        val BASE: KForceUnit = NEWTON
    }
}
