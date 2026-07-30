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

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit

/**
 * The number of grams in a kilogram. The mass group of this library is normalized to **grams**, while the
 * specific-energy group is defined *per kilogram*, so every operator that combines the two bridges across
 * this factor.
 */
internal val GRAMS_PER_KILOGRAM: Double = KUnitPrefix.KILO.factor

/**
 * Wraps a [KMixedUnitInstance] representing a **specific energy** (energy per unit of mass), i.e. exactly
 * two terms in the canonical normal form `distance² · time⁻²` (`m²·s⁻²` = `J/kg`). The [value] is always
 * normalized internally to joules per kilogram ([KSpecificEnergyUnit.BASE]).
 *
 * Note that the mass dimension **cancels out**: `J/kg = kg·m²·s⁻²/kg = m²·s⁻²`. The canonical normal form
 * therefore carries no mass term at all, and no gram/kilogram bridge is needed inside [toSpecificEnergy]
 * (only the operators against a [org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance] bridge, via
 * [GRAMS_PER_KILOGRAM]).
 *
 * Specific energy is a *constructed* unit group. Instances are created via the bare tokens in
 * `KSpecificEnergyUnitBareValues.kt` (e.g. `5 of joulesPerKilogram`), the prefixed templates in
 * `KSpecificEnergyUnitExtensions.kt` (e.g. `2 of kilo.joulesPerKilogram`), the typed `energy / mass`
 * operator in `KSpecificEnergyUnitOperators.kt`, or [toSpecificEnergy].
 *
 * Example:
 * ```kotlin
 * val h = (334 of kilo.joules) / (1 of kilo.grams) // latent heat of fusion of ice
 * h into kilo.joulesPerKilogram                     // 334.0
 * ```
 */
class KSpecificEnergyUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KSpecificEnergyUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new specific energy with [value] (J/kg) scaled by [factor]. Backs number-times-unit
     * construction (`10 of joulesPerKilogram`).
     */
    override fun scaledBy(factor: Double): KSpecificEnergyUnitInstance = specificEnergyInstanceOf(value * factor)

    /**
     * Adds two specific energies, automatically converting between different [KSpecificEnergyUnit]s since
     * both operands are always normalized to [KSpecificEnergyUnit.BASE] (J/kg) internally.
     */
    override operator fun plus(other: KSpecificEnergyUnitInstance): KSpecificEnergyUnitInstance =
        specificEnergyInstanceOf(value + other.value)

    /** Subtracts two specific energies. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KSpecificEnergyUnitInstance): KSpecificEnergyUnitInstance =
        specificEnergyInstanceOf(value - other.value)

    /** Multiplies two specific energies, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KSpecificEnergyUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two specific energies, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KSpecificEnergyUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two specific energies by their normalized [value] (J/kg). */
    override operator fun compareTo(other: KSpecificEnergyUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two specific energies are equal iff they represent the
     * same quantity (e.g. `(1 of kilo.joulesPerKilogram) == (1000 of joulesPerKilogram)`).
     */
    override fun equals(other: Any?): Boolean = other is KSpecificEnergyUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"334000.0 J/kg"`. */
    override fun toString(): String = "$value ${KSpecificEnergyUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KSpecificEnergyUnitInstance] from a value already expressed in joules per kilogram
 * ([KSpecificEnergyUnit.BASE]).
 *
 * This is the single creation source that every specific energy decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the two terms `distance²` and `time⁻²`.
 */
internal fun specificEnergyInstanceOf(joulesPerKilogram: Double): KSpecificEnergyUnitInstance =
    KSpecificEnergyUnitInstance(
        KMixedUnitInstance(
            joulesPerKilogram,
            listOf(
                KUnitTerm(KDistanceUnit.BASE, 2),
                KUnitTerm(KTimeUnit.BASE, -2),
            ),
        ),
    )

/** Builds a value-1 [KSpecificEnergyUnitInstance] for the given [unit] (its [KSpecificEnergyUnit.baseValue] J/kg). */
internal fun specificEnergyOfUnit(unit: KSpecificEnergyUnit): KSpecificEnergyUnitInstance =
    specificEnergyInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" specific energy, as long as it matches the canonical specific
 * energy normal form: exactly one [KDistanceUnit] term at exponent `+2` and one [KTimeUnit] term at `-2`
 * (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the result is expressed in J/kg regardless of which
 * concrete units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `distance²·time⁻²` specific energy.
 */
fun KMixedUnitInstance.toSpecificEnergy(): KSpecificEnergyUnitInstance {
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -2 }
    check(units.size == 2 && distanceTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure specific energy (expected KDistanceUnit^2 and KTimeUnit^-2)"
    }
    val normalized = value *
        Math.pow(distanceTerm.unit.baseValue, 2.0) *
        Math.pow(timeTerm.unit.baseValue, -2.0)
    return specificEnergyInstanceOf(normalized)
}
