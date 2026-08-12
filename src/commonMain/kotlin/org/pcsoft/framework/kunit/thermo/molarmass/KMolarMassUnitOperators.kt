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

package org.pcsoft.framework.kunit.thermo.molarmass

import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.massOf
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnitInstance
import org.pcsoft.framework.kunit.thermo.amountofsubstance.amountOfSubstanceOf

// Cross-group operators that let mass and amount of substance combine *directly* into a strongly typed
// molar mass, and back. They live in the molar mass package because it may depend on
// mass/amount-of-substance (the reverse must never happen). Both operand values are already normalized
// (g, mol) and the group's base unit is g/mol, so the arithmetic needs no dimension bridge.

/**
 * Divides a mass by an amount of substance to obtain a [KMolarMassUnitInstance]
 * (`mass / amountOfSubstance = molar mass`).
 *
 * Example:
 * ```kotlin
 * val m = (18.015 of grams) / (1 of moles) // 18.015 g/mol (water)
 * ```
 */
operator fun KMassUnitInstance.div(other: KAmountOfSubstanceUnitInstance): KMolarMassUnitInstance =
    molarMassInstanceOf(value / other.value)

/**
 * Multiplies a molar mass by an amount of substance to obtain the total mass
 * (`molar mass * amountOfSubstance = mass`).
 *
 * Example:
 * ```kotlin
 * val m = (18.015 of gramsPerMole) * (2 of moles) // 36.03 g
 * ```
 */
operator fun KMolarMassUnitInstance.times(other: KAmountOfSubstanceUnitInstance): KMassUnitInstance =
    massOf(value * other.value)

/**
 * Multiplies an amount of substance by a molar mass to obtain the total mass; the commutative counterpart
 * of [KMolarMassUnitInstance.times].
 */
operator fun KAmountOfSubstanceUnitInstance.times(other: KMolarMassUnitInstance): KMassUnitInstance =
    massOf(value * other.value)

/**
 * Divides a mass by a molar mass to obtain the amount of substance it contains
 * (`mass / molar mass = amountOfSubstance`).
 *
 * Example:
 * ```kotlin
 * val n = (36.03 of grams) / (18.015 of gramsPerMole) // 2 mol
 * ```
 */
operator fun KMassUnitInstance.div(other: KMolarMassUnitInstance): KAmountOfSubstanceUnitInstance =
    amountOfSubstanceOf(value / other.value)
