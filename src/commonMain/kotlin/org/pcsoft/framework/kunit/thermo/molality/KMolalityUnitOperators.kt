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

package org.pcsoft.framework.kunit.thermo.molality

import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.massOf
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnitInstance
import org.pcsoft.framework.kunit.thermo.amountofsubstance.amountOfSubstanceOf
import org.pcsoft.framework.kunit.thermo.molarmass.KMolarMassUnitInstance
import org.pcsoft.framework.kunit.thermo.molarmass.molarMassInstanceOf

// Cross-group operators for the decomposition of the molality - `amountOfSubstance / mass` - plus its
// inverses, and the reciprocal relation to the molar mass. They live in the molality package because it
// may depend on amount-of-substance/mass/molar-mass (the reverse must never happen).

/**
 * Divides an amount of substance by a mass to obtain a [KMolalityUnitInstance]
 * (`amountOfSubstance / mass = molality`).
 *
 * Example:
 * ```kotlin
 * val b = (0.5 of moles) / (2 of kilo.grams) // 0.25 mol/kg
 * ```
 */
operator fun KAmountOfSubstanceUnitInstance.div(other: KMassUnitInstance): KMolalityUnitInstance =
    molalityInstanceOf(value / other.value * MOLALITY_MASS_REFERENCE)

/**
 * Multiplies a molality by a mass to obtain the amount of substance dissolved in it
 * (`molality * mass = amountOfSubstance`).
 */
operator fun KMolalityUnitInstance.times(other: KMassUnitInstance): KAmountOfSubstanceUnitInstance =
    amountOfSubstanceOf(value * other.value / MOLALITY_MASS_REFERENCE)

/**
 * Multiplies a mass by a molality to obtain the amount of substance; the commutative counterpart of
 * [KMolalityUnitInstance.times].
 */
operator fun KMassUnitInstance.times(other: KMolalityUnitInstance): KAmountOfSubstanceUnitInstance =
    amountOfSubstanceOf(value * other.value / MOLALITY_MASS_REFERENCE)

/**
 * Divides an amount of substance by a molality to obtain the solvent mass needed
 * (`amountOfSubstance / molality = mass`).
 */
operator fun KAmountOfSubstanceUnitInstance.div(other: KMolalityUnitInstance): KMassUnitInstance =
    massOf(value / other.value * MOLALITY_MASS_REFERENCE)

/**
 * Divides a scalar count by a molar mass to obtain the molality of the pure substance
 * (`count / molarMass = molality`) - one kilogram of water is `1 / (18.015 g/mol)` ≈ 55.5 mol.
 *
 * Example:
 * ```kotlin
 * val b = 1 / (18.015 of gramsPerMole) // ≈ 55.51 mol/kg
 * ```
 */
operator fun Number.div(molarMass: KMolarMassUnitInstance): KMolalityUnitInstance =
    molalityInstanceOf(this.toDouble() * MOLALITY_MASS_REFERENCE / molarMass.value)

/**
 * Divides a scalar count by a molality to obtain the corresponding molar mass
 * (`count / molality = molarMass`) - the inverse of [Number.div] against a molar mass.
 */
operator fun Number.div(molality: KMolalityUnitInstance): KMolarMassUnitInstance =
    molarMassInstanceOf(this.toDouble() * MOLALITY_MASS_REFERENCE / molality.value)
