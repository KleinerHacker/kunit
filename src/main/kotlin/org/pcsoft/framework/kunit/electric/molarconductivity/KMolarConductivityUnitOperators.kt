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

package org.pcsoft.framework.kunit.electric.molarconductivity

import org.pcsoft.framework.kunit.electric.conductivity.KConductivityUnitInstance
import org.pcsoft.framework.kunit.electric.conductivity.conductivityInstanceOf
import org.pcsoft.framework.kunit.thermo.concentration.KConcentrationUnitInstance
import org.pcsoft.framework.kunit.thermo.concentration.concentrationInstanceOf

// Cross-group operators for the decomposition of the molar conductivity -
// `conductivity / concentration` - plus its inverses. They live in the molar conductivity package because
// it may depend on conductivity/concentration (the reverse must never happen).

/**
 * Divides an electrolytic conductivity by a concentration to obtain a [KMolarConductivityUnitInstance]
 * (`conductivity / concentration = molar conductivity`).
 *
 * Example:
 * ```kotlin
 * val lambda = (1.29 of milli.siemensPerMeter) / (0.1 of molesPerLiter)
 * ```
 */
operator fun KConductivityUnitInstance.div(
    other: KConcentrationUnitInstance
): KMolarConductivityUnitInstance = molarConductivityInstanceOf(value / other.value)

/**
 * Multiplies a molar conductivity by a concentration to obtain the electrolytic conductivity
 * (`molar conductivity * concentration = conductivity`).
 */
operator fun KMolarConductivityUnitInstance.times(
    other: KConcentrationUnitInstance
): KConductivityUnitInstance = conductivityInstanceOf(value * other.value)

/**
 * Multiplies a concentration by a molar conductivity to obtain the conductivity; the commutative
 * counterpart of [KMolarConductivityUnitInstance.times].
 */
operator fun KConcentrationUnitInstance.times(
    other: KMolarConductivityUnitInstance
): KConductivityUnitInstance = conductivityInstanceOf(value * other.value)

/**
 * Divides a conductivity by a molar conductivity to obtain the concentration
 * (`conductivity / molar conductivity = concentration`).
 */
operator fun KConductivityUnitInstance.div(
    other: KMolarConductivityUnitInstance
): KConcentrationUnitInstance = concentrationInstanceOf(value / other.value)
