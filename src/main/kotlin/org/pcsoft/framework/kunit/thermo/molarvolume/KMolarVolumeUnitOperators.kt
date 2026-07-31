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

package org.pcsoft.framework.kunit.thermo.molarvolume

import org.pcsoft.framework.kunit.kinematic.distance.KVolumeUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.volumeOf
import org.pcsoft.framework.kunit.mechanic.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.mechanic.density.densityUnitInstanceOf
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnitInstance
import org.pcsoft.framework.kunit.thermo.amountofsubstance.amountOfSubstanceOf
import org.pcsoft.framework.kunit.thermo.molarmass.KMolarMassUnitInstance
import org.pcsoft.framework.kunit.thermo.molarmass.molarMassInstanceOf

// Cross-group operators for the two decompositions of the molar volume - `volume / amountOfSubstance` and
// `molarMass / density` - plus their inverses. They live in the molar volume package because it may depend
// on distance/amount-of-substance/molar-mass/density (the reverse must never happen).
//
// The second decomposition works on the normalized component values: the molar mass is stored in g/mol and
// the density in `g·m⁻³`, so their quotient is directly m³/mol - the group's base unit.

/**
 * Divides a volume by an amount of substance to obtain a [KMolarVolumeUnitInstance]
 * (`volume / amountOfSubstance = molar volume`).
 *
 * Example:
 * ```kotlin
 * val v = (22.4 of liters) / (1 of moles) // ≈ 0.0224 m³/mol
 * ```
 */
operator fun KVolumeUnitInstance.div(other: KAmountOfSubstanceUnitInstance): KMolarVolumeUnitInstance =
    molarVolumeInstanceOf(value / other.value)

/**
 * Divides a molar mass by a density to obtain the molar volume (`molarMass / density = molar volume`) -
 * the second decomposition of this group, yielding the same typed, value-equal result as
 * `volume / amountOfSubstance`.
 *
 * Example:
 * ```kotlin
 * val v = (18.015 of gramsPerMole) / ((1 of kilo.grams) / (1 of liters)) // water: ≈ 18.015 cm³/mol
 * ```
 */
operator fun KMolarMassUnitInstance.div(other: KDensityUnitInstance): KMolarVolumeUnitInstance =
    molarVolumeInstanceOf(value / other.value)

/**
 * Multiplies a molar volume by an amount of substance to obtain the total volume
 * (`molar volume * amountOfSubstance = volume`).
 */
operator fun KMolarVolumeUnitInstance.times(other: KAmountOfSubstanceUnitInstance): KVolumeUnitInstance =
    volumeOf(value * other.value)

/**
 * Multiplies an amount of substance by a molar volume to obtain the total volume; the commutative
 * counterpart of [KMolarVolumeUnitInstance.times].
 */
operator fun KAmountOfSubstanceUnitInstance.times(other: KMolarVolumeUnitInstance): KVolumeUnitInstance =
    volumeOf(value * other.value)

/**
 * Divides a volume by a molar volume to obtain the amount of substance it holds
 * (`volume / molar volume = amountOfSubstance`).
 */
operator fun KVolumeUnitInstance.div(other: KMolarVolumeUnitInstance): KAmountOfSubstanceUnitInstance =
    amountOfSubstanceOf(value / other.value)

/**
 * Multiplies a molar volume by a density to obtain the molar mass (`molar volume * density = molar mass`) -
 * the inverse of the `molarMass / density` decomposition.
 */
operator fun KMolarVolumeUnitInstance.times(other: KDensityUnitInstance): KMolarMassUnitInstance =
    molarMassInstanceOf(value * other.value)

/**
 * Multiplies a density by a molar volume to obtain the molar mass; the commutative counterpart of
 * [KMolarVolumeUnitInstance.times].
 */
operator fun KDensityUnitInstance.times(other: KMolarVolumeUnitInstance): KMolarMassUnitInstance =
    molarMassInstanceOf(value * other.value)

/**
 * Divides a molar mass by a molar volume to obtain the density (`molar mass / molar volume = density`).
 */
operator fun KMolarMassUnitInstance.div(other: KMolarVolumeUnitInstance): KDensityUnitInstance =
    densityUnitInstanceOf(value / other.value)
