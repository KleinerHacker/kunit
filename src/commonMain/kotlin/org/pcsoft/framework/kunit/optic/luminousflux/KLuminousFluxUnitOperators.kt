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

package org.pcsoft.framework.kunit.optic.luminousflux

import org.pcsoft.framework.kunit.mechanic.solidangle.KSolidAngleUnitInstance
import org.pcsoft.framework.kunit.mechanic.solidangle.solidAngleOf
import org.pcsoft.framework.kunit.optic.luminousintensity.KLuminousIntensityUnitInstance
import org.pcsoft.framework.kunit.optic.luminousintensity.luminousIntensityOf

// Cross-group operators for the decomposition of the luminous flux - `luminousIntensity * solidAngle` -
// plus its inverses. They live in the luminous flux package because it may depend on
// luminous-intensity/solid-angle (the reverse must never happen).

/**
 * Multiplies a luminous intensity by a solid angle to obtain a [KLuminousFluxUnitInstance]
 * (`luminousIntensity * solidAngle = luminous flux`).
 *
 * Example:
 * ```kotlin
 * val phi = (100 of candelas) * (4 * PI of steradians) // ≈ 1256.6 lm
 * ```
 */
operator fun KLuminousIntensityUnitInstance.times(other: KSolidAngleUnitInstance): KLuminousFluxUnitInstance =
    luminousFluxInstanceOf(value * other.value)

/**
 * Multiplies a solid angle by a luminous intensity to obtain the luminous flux; the commutative
 * counterpart of [KLuminousIntensityUnitInstance.times].
 */
operator fun KSolidAngleUnitInstance.times(other: KLuminousIntensityUnitInstance): KLuminousFluxUnitInstance =
    luminousFluxInstanceOf(value * other.value)

/**
 * Divides a luminous flux by a solid angle to obtain the luminous intensity
 * (`luminous flux / solidAngle = luminousIntensity`).
 */
operator fun KLuminousFluxUnitInstance.div(other: KSolidAngleUnitInstance): KLuminousIntensityUnitInstance =
    luminousIntensityOf(value / other.value)

/**
 * Divides a luminous flux by a luminous intensity to obtain the solid angle it is spread over
 * (`luminous flux / luminousIntensity = solidAngle`).
 */
operator fun KLuminousFluxUnitInstance.div(other: KLuminousIntensityUnitInstance): KSolidAngleUnitInstance =
    solidAngleOf(value / other.value)
