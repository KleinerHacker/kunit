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

package org.pcsoft.framework.kunit.mechanic.strain

import org.pcsoft.framework.kunit.mechanic.pressure.KPressureUnitInstance
import org.pcsoft.framework.kunit.mechanic.pressure.pressureUnitInstanceOf

// Hooke's law operators bridging the (dimensionless) strain group and the pressure group, which carries the
// mechanical stress and the elastic modulus (see the `stress` documentation page). They live in the strain
// package because it may depend on pressure (the reverse must never happen).
//
// Since a strain is a pure ratio, both operations only scale the pressure's normalized component value.

/**
 * Divides a mechanical stress by the strain it causes to obtain the elastic modulus
 * (`stress / strain = E`, Hooke's law). Both operands and the result live in the pressure group.
 *
 * Example:
 * ```kotlin
 * val e = (210 of mega.pascals) / (1 of perMille) // 210 GPa (steel)
 * ```
 */
operator fun KPressureUnitInstance.div(other: KStrainUnitInstance): KPressureUnitInstance =
    pressureUnitInstanceOf(value / other.value)

/**
 * Multiplies an elastic modulus by a strain to obtain the resulting mechanical stress
 * (`E * strain = stress`) - the inverse reading of Hooke's law.
 */
operator fun KPressureUnitInstance.times(other: KStrainUnitInstance): KPressureUnitInstance =
    pressureUnitInstanceOf(value * other.value)

/**
 * Multiplies a strain by an elastic modulus to obtain the mechanical stress (`strain * E = stress`); the
 * commutative counterpart of [KPressureUnitInstance.times].
 */
operator fun KStrainUnitInstance.times(other: KPressureUnitInstance): KPressureUnitInstance =
    pressureUnitInstanceOf(value * other.value)
