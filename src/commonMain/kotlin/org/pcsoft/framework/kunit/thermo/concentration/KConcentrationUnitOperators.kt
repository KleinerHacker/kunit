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

package org.pcsoft.framework.kunit.thermo.concentration

import org.pcsoft.framework.kunit.kinematic.distance.KVolumeUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.volumeOf
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnitInstance
import org.pcsoft.framework.kunit.thermo.amountofsubstance.amountOfSubstanceOf

// Cross-group operators for the decomposition of the concentration - `amountOfSubstance / volume` - plus
// its inverses. They live in the concentration package because it may depend on
// amount-of-substance/distance (the reverse must never happen).

/**
 * Divides an amount of substance by a volume to obtain a [KConcentrationUnitInstance]
 * (`amountOfSubstance / volume = concentration`).
 *
 * Example:
 * ```kotlin
 * val c = (0.5 of moles) / (2 of liters) // 0.25 mol/l
 * ```
 */
operator fun KAmountOfSubstanceUnitInstance.div(other: KVolumeUnitInstance): KConcentrationUnitInstance =
    concentrationInstanceOf(value / other.value)

/**
 * Multiplies a concentration by a volume to obtain the amount of substance it contains
 * (`concentration * volume = amountOfSubstance`).
 */
operator fun KConcentrationUnitInstance.times(other: KVolumeUnitInstance): KAmountOfSubstanceUnitInstance =
    amountOfSubstanceOf(value * other.value)

/**
 * Multiplies a volume by a concentration to obtain the amount of substance; the commutative counterpart of
 * [KConcentrationUnitInstance.times].
 */
operator fun KVolumeUnitInstance.times(other: KConcentrationUnitInstance): KAmountOfSubstanceUnitInstance =
    amountOfSubstanceOf(value * other.value)

/**
 * Divides an amount of substance by a concentration to obtain the volume needed to hold it
 * (`amountOfSubstance / concentration = volume`).
 */
operator fun KAmountOfSubstanceUnitInstance.div(other: KConcentrationUnitInstance): KVolumeUnitInstance =
    volumeOf(value / other.value)
