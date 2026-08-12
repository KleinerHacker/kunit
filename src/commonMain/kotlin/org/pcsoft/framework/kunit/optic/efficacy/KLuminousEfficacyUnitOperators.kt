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

package org.pcsoft.framework.kunit.optic.efficacy

import org.pcsoft.framework.kunit.common.power.KPowerUnitInstance
import org.pcsoft.framework.kunit.common.power.powerInstanceOf
import org.pcsoft.framework.kunit.optic.luminousflux.KLuminousFluxUnitInstance
import org.pcsoft.framework.kunit.optic.luminousflux.luminousFluxInstanceOf

// Cross-group operators for the decomposition of the luminous efficacy - `luminousFlux / power` - plus its
// inverses. They live in the efficacy package because it may depend on luminous-flux/power (the reverse
// must never happen).

/**
 * Divides a luminous flux by an electrical power to obtain a [KLuminousEfficacyUnitInstance]
 * (`luminousFlux / power = luminous efficacy`).
 *
 * Example:
 * ```kotlin
 * val eta = (800 of lumens) / (7 of watts) // ≈ 114 lm/W
 * ```
 */
operator fun KLuminousFluxUnitInstance.div(other: KPowerUnitInstance): KLuminousEfficacyUnitInstance =
    luminousEfficacyInstanceOf(value / other.value)

/**
 * Multiplies a luminous efficacy by a power to obtain the luminous flux it produces
 * (`luminous efficacy * power = luminousFlux`).
 */
operator fun KLuminousEfficacyUnitInstance.times(other: KPowerUnitInstance): KLuminousFluxUnitInstance =
    luminousFluxInstanceOf(value * other.value)

/**
 * Multiplies a power by a luminous efficacy to obtain the luminous flux; the commutative counterpart of
 * [KLuminousEfficacyUnitInstance.times].
 */
operator fun KPowerUnitInstance.times(other: KLuminousEfficacyUnitInstance): KLuminousFluxUnitInstance =
    luminousFluxInstanceOf(value * other.value)

/**
 * Divides a luminous flux by a luminous efficacy to obtain the power required
 * (`luminousFlux / luminous efficacy = power`).
 */
operator fun KLuminousFluxUnitInstance.div(other: KLuminousEfficacyUnitInstance): KPowerUnitInstance =
    powerInstanceOf(value / other.value)
