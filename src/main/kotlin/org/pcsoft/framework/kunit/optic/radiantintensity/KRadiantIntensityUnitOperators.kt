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

package org.pcsoft.framework.kunit.optic.radiantintensity

import org.pcsoft.framework.kunit.common.power.KPowerUnitInstance
import org.pcsoft.framework.kunit.common.power.powerInstanceOf
import org.pcsoft.framework.kunit.mechanic.solidangle.KSolidAngleUnitInstance
import org.pcsoft.framework.kunit.mechanic.solidangle.solidAngleOf

// Cross-group operators for the decomposition of the radiant intensity - `power / solidAngle` - plus its
// inverses. They live in the radiant intensity package because it may depend on power/solid-angle (the
// reverse must never happen).

/**
 * Divides a radiant flux (power) by a solid angle to obtain a [KRadiantIntensityUnitInstance]
 * (`power / solidAngle = radiant intensity`).
 *
 * Example:
 * ```kotlin
 * val i = (20 of watts) / (4 of steradians) // 5 W/sr
 * ```
 */
operator fun KPowerUnitInstance.div(other: KSolidAngleUnitInstance): KRadiantIntensityUnitInstance =
    radiantIntensityInstanceOf(value / other.value)

/**
 * Multiplies a radiant intensity by a solid angle to obtain the radiant flux
 * (`radiant intensity * solidAngle = power`).
 */
operator fun KRadiantIntensityUnitInstance.times(other: KSolidAngleUnitInstance): KPowerUnitInstance =
    powerInstanceOf(value * other.value)

/**
 * Multiplies a solid angle by a radiant intensity to obtain the radiant flux; the commutative counterpart
 * of [KRadiantIntensityUnitInstance.times].
 */
operator fun KSolidAngleUnitInstance.times(other: KRadiantIntensityUnitInstance): KPowerUnitInstance =
    powerInstanceOf(value * other.value)

/**
 * Divides a radiant flux by a radiant intensity to obtain the solid angle it is spread over
 * (`power / radiant intensity = solidAngle`).
 */
operator fun KPowerUnitInstance.div(other: KRadiantIntensityUnitInstance): KSolidAngleUnitInstance =
    solidAngleOf(value / other.value)
