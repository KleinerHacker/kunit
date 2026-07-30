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

package org.pcsoft.framework.kunit.thermo.molarenergy

import org.pcsoft.framework.kunit.common.energy.KEnergyUnitInstance
import org.pcsoft.framework.kunit.common.energy.energyInstanceOf
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnitInstance
import org.pcsoft.framework.kunit.thermo.amountofsubstance.amountOfSubstanceOf

// Cross-group operators that let energy and amount of substance combine *directly* into a strongly typed
// molar energy, and back. They live in the molar energy package because it may depend on
// energy/amount-of-substance (the reverse must never happen). Both operand values are already normalized
// (J, mol), so the arithmetic is exact and needs no dimension bridge.

/**
 * Divides an energy by an amount of substance to obtain a [KMolarEnergyUnitInstance]
 * (`energy / amountOfSubstance = molar energy`).
 *
 * Example:
 * ```kotlin
 * val dH = (286 of kilo.joules) / (1 of moles) // 286 000 J/mol
 * ```
 */
operator fun KEnergyUnitInstance.div(other: KAmountOfSubstanceUnitInstance): KMolarEnergyUnitInstance =
    molarEnergyInstanceOf(value / other.value)

/**
 * Multiplies a molar energy by an amount of substance to obtain the total energy
 * (`molar energy * amountOfSubstance = energy`).
 */
operator fun KMolarEnergyUnitInstance.times(other: KAmountOfSubstanceUnitInstance): KEnergyUnitInstance =
    energyInstanceOf(value * other.value)

/**
 * Multiplies an amount of substance by a molar energy to obtain the total energy; the commutative
 * counterpart of [KMolarEnergyUnitInstance.times].
 */
operator fun KAmountOfSubstanceUnitInstance.times(other: KMolarEnergyUnitInstance): KEnergyUnitInstance =
    energyInstanceOf(value * other.value)

/**
 * Divides an energy by a molar energy to obtain the amount of substance involved
 * (`energy / molar energy = amountOfSubstance`).
 */
operator fun KEnergyUnitInstance.div(other: KMolarEnergyUnitInstance): KAmountOfSubstanceUnitInstance =
    amountOfSubstanceOf(value / other.value)
