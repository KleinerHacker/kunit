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

package org.pcsoft.framework.kunit.thermo.specificenergy

import org.pcsoft.framework.kunit.common.energy.KEnergyUnitInstance
import org.pcsoft.framework.kunit.common.energy.energyInstanceOf
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.massOf

// Cross-group operators that let energy and mass combine *directly* into a strongly typed specific
// energy, and back. They live in the specific energy package because it may depend on energy/mass (the
// reverse must never happen). Each bridges the mass group's gram base to the group's per-kilogram
// definition via GRAMS_PER_KILOGRAM.

/**
 * Divides an energy by a mass to obtain a [KSpecificEnergyUnitInstance] (`energy / mass = specific energy`).
 *
 * Example:
 * ```kotlin
 * val h = (334 of kilo.joules) / (1 of kilo.grams) // 334 000 J/kg
 * ```
 */
operator fun KEnergyUnitInstance.div(other: KMassUnitInstance): KSpecificEnergyUnitInstance =
    specificEnergyInstanceOf(value / (other.value / GRAMS_PER_KILOGRAM))

/**
 * Multiplies a specific energy by a mass to obtain the total energy (`specific energy * mass = energy`).
 */
operator fun KSpecificEnergyUnitInstance.times(other: KMassUnitInstance): KEnergyUnitInstance =
    energyInstanceOf(value * (other.value / GRAMS_PER_KILOGRAM))

/**
 * Multiplies a mass by a specific energy to obtain the total energy; the commutative counterpart of
 * [KSpecificEnergyUnitInstance.times].
 */
operator fun KMassUnitInstance.times(other: KSpecificEnergyUnitInstance): KEnergyUnitInstance =
    energyInstanceOf((value / GRAMS_PER_KILOGRAM) * other.value)

/**
 * Divides an energy by a specific energy to obtain the mass involved
 * (`energy / specific energy = mass`).
 */
operator fun KEnergyUnitInstance.div(other: KSpecificEnergyUnitInstance): KMassUnitInstance =
    massOf(value / other.value * GRAMS_PER_KILOGRAM)
