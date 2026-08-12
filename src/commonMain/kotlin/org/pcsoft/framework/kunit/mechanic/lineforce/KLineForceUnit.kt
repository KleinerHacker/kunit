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

package org.pcsoft.framework.kunit.mechanic.lineforce

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of **force per length** (a *constructed* quantity: mass · time⁻²). [baseValue]
 * is the factor to convert into the group's base unit ([BASE], newton per meter): `1 unit = baseValue * N/m`.
 *
 * Two mechanical quantities share this dimension exactly and are therefore *the same* unit group here:
 * * the **surface tension** (energy per area, `J/m² = N/m`), and
 * * the **stiffness / spring rate** (force per deflection, `N/m`).
 *
 * The group is named neutrally (`lineforce`) because neither reading may claim the other's name; both have
 * their own documentation page.
 *
 * Because the mass component of this library is normalized to **grams**, the N/m is 1000× the raw component
 * base (`g·s⁻²`); that fixed factor is [org.pcsoft.framework.kunit.mechanic.lineforce.NM_IN_BASE].
 *
 * Example:
 * ```kotlin
 * KLineForceUnit.DYNES_PER_CENTIMETER.baseValue // 1.0e-3 (water ≈ 72 dyn/cm = 72 mN/m)
 * ```
 */
enum class KLineForceUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Newton per meter ("N/m"), the SI base unit of force per length; [baseValue] = 1.0 by definition. */
    NEWTONS_PER_METER("N/m", 1.0),

    /** Newton per millimeter ("N/mm"), 1 N/mm = 1000 N/m (typical spring-rate spelling). */
    NEWTONS_PER_MILLIMETER("N/mm", 1000.0),

    /** Dyne per centimeter ("dyn/cm"), the CGS surface-tension unit: 1 dyn/cm = 1e-3 N/m = 1 mN/m. */
    DYNES_PER_CENTIMETER("dyn/cm", 1.0e-3),

    /** Pound-force per inch ("lbf/in"), 1 lbf/in ≈ 175.127 N/m. */
    POUNDS_FORCE_PER_INCH("lbf/in", 4.4482216152605 / 0.0254),

    /** Kilopond per meter ("kp/m"), 1 kp/m = 9.80665 N/m. */
    KILOPONDS_PER_METER("kp/m", 9.80665);

    companion object {
        /**
         * The base unit of the force-per-length group: [NEWTONS_PER_METER]. All readings of
         * [KLineForceUnitInstance] are normalized to this unit (via the
         * [org.pcsoft.framework.kunit.mechanic.lineforce.NM_IN_BASE] factor).
         */
        val BASE: KLineForceUnit = NEWTONS_PER_METER
    }
}
