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

package org.pcsoft.framework.kunit.thermo.heatfluxdensity

import org.pcsoft.framework.kunit.common.power.KPowerUnitInstance
import org.pcsoft.framework.kunit.common.power.powerInstanceOf
import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.areaOf

// Cross-group operators that let power and area combine *directly* into a strongly typed heat flux
// density, and back. Both operand values are already normalized (W, m²), so the arithmetic is exact.

/**
 * Divides a power by an area to obtain a [KHeatFluxDensityUnitInstance] (`power / area = heat flux density`).
 *
 * Example:
 * ```kotlin
 * val q = (1000 of watts) / ((2 of meters) * (1 of meters)) // 500 W/m²
 * ```
 */
operator fun KPowerUnitInstance.div(other: KAreaUnitInstance): KHeatFluxDensityUnitInstance =
    heatFluxDensityInstanceOf(value / other.value)

/**
 * Multiplies a heat flux density by an area to obtain the total heat flow
 * (`heat flux density * area = power`).
 */
operator fun KHeatFluxDensityUnitInstance.times(other: KAreaUnitInstance): KPowerUnitInstance =
    powerInstanceOf(value * other.value)

/**
 * Multiplies an area by a heat flux density to obtain the total heat flow; the commutative counterpart of
 * [KHeatFluxDensityUnitInstance.times].
 */
operator fun KAreaUnitInstance.times(other: KHeatFluxDensityUnitInstance): KPowerUnitInstance =
    powerInstanceOf(value * other.value)

/**
 * Divides a power by a heat flux density to obtain the area it is spread over
 * (`power / heat flux density = area`).
 */
operator fun KPowerUnitInstance.div(other: KHeatFluxDensityUnitInstance): KAreaUnitInstance =
    areaOf(value / other.value)
