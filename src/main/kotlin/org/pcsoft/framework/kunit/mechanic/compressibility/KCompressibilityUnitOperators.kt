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

package org.pcsoft.framework.kunit.mechanic.compressibility

import org.pcsoft.framework.kunit.mechanic.pressure.KPressureUnitInstance
import org.pcsoft.framework.kunit.mechanic.pressure.pressureUnitInstanceOf

// Cross-group operators linking the compressibility to the bulk modulus, which is a pressure. Both groups
// store their raw gram-based component value, and the two component bases are exact reciprocals
// (`g·m⁻¹·s⁻²` vs. `g⁻¹·m·s²`), so the scalar reciprocal needs no bridging factor.

/**
 * Inverts a pressure (an elastic or bulk modulus) into the corresponding compressibility
 * (`κ = 1 / K`).
 *
 * Example:
 * ```kotlin
 * val kappa = 1 / (2.2 of giga.pascals) // water: ≈ 4.5e-10 Pa⁻¹
 * ```
 */
operator fun Number.div(modulus: KPressureUnitInstance): KCompressibilityUnitInstance =
    compressibilityInstanceOf(this.toDouble() / modulus.value)

/**
 * Inverts a compressibility into the corresponding bulk modulus (`K = 1 / κ`), a pressure - the inverse of
 * [Number.div] against a pressure.
 *
 * Example:
 * ```kotlin
 * val k = 1 / (4.5e-10 of reciprocalPascals) // ≈ 2.22 GPa
 * ```
 */
operator fun Number.div(compressibility: KCompressibilityUnitInstance): KPressureUnitInstance =
    pressureUnitInstanceOf(this.toDouble() / compressibility.value)

/**
 * Multiplies a compressibility by a pressure to obtain the **dimensionless** relative volume change
 * (`κ · Δp = ΔV/V`).
 *
 * Example:
 * ```kotlin
 * val shrink = (4.5e-10 of reciprocalPascals) * (10 of mega.pascals) // ≈ 0.0045 (0.45 %)
 * ```
 */
operator fun KCompressibilityUnitInstance.times(other: KPressureUnitInstance): Double =
    value * other.value

/**
 * Multiplies a pressure by a compressibility to obtain the relative volume change; the commutative
 * counterpart of [KCompressibilityUnitInstance.times].
 */
operator fun KPressureUnitInstance.times(other: KCompressibilityUnitInstance): Double =
    value * other.value
