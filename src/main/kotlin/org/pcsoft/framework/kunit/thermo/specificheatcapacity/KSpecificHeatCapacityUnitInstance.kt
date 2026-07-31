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

package org.pcsoft.framework.kunit.thermo.specificheatcapacity

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnit

/**
 * The number of grams in a kilogram. The mass group of this library is normalized to **grams**, while this
 * group is defined *per kilogram*, so every operator that combines the two bridges across this factor.
 */
internal val SPECIFIC_HEAT_GRAMS_PER_KILOGRAM: Double = KUnitPrefix.KILO.factor

/**
 * Wraps a [KMixedUnitInstance] representing a **specific heat capacity** (heat capacity per unit of mass),
 * i.e. exactly three terms in the canonical normal form `distance² · time⁻² · temperature⁻¹`
 * (`m²·s⁻²·K⁻¹` = `J/(kg·K)`). The [value] is always normalized internally to J/(kg·K)
 * ([KSpecificHeatCapacityUnit.BASE]).
 *
 * As with [org.pcsoft.framework.kunit.thermo.specificenergy.KSpecificEnergyUnitInstance] the mass
 * dimension **cancels out**, so the canonical normal form carries no mass term; only the operators against
 * a mass bridge via [SPECIFIC_HEAT_GRAMS_PER_KILOGRAM]. The temperature dimension is the **difference**
 * group ([KTemperatureDifferenceUnit]), never the affine absolute temperature.
 *
 * Instances are created via the bare tokens in `KSpecificHeatCapacityUnitBareValues.kt`, the prefixed
 * templates in `KSpecificHeatCapacityUnitExtensions.kt`, the typed operators in
 * `KSpecificHeatCapacityUnitOperators.kt` (`heatCapacity / mass`,
 * `specificEnergy / temperatureDifference`), or [toSpecificHeatCapacity].
 *
 * Example:
 * ```kotlin
 * val c = 4184 of joulesPerKilogramKelvin // water
 * c into caloriesPerGramKelvin             // 1.0
 * ```
 */
class KSpecificHeatCapacityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KSpecificHeatCapacityUnitInstance>, KUnitMeasurable by instance {

    /** Returns a new specific heat capacity with [value] (J/(kg·K)) scaled by [factor]. */
    override fun scaledBy(factor: Double): KSpecificHeatCapacityUnitInstance =
        specificHeatCapacityInstanceOf(value * factor)

    /**
     * Adds two specific heat capacities, automatically converting between different
     * [KSpecificHeatCapacityUnit]s since both operands are always normalized to the base internally.
     */
    override operator fun plus(other: KSpecificHeatCapacityUnitInstance): KSpecificHeatCapacityUnitInstance =
        specificHeatCapacityInstanceOf(value + other.value)

    /** Subtracts two specific heat capacities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KSpecificHeatCapacityUnitInstance): KSpecificHeatCapacityUnitInstance =
        specificHeatCapacityInstanceOf(value - other.value)

    /** Multiplies two specific heat capacities, producing a new [KMixedUnitInstance]. */
    operator fun times(other: KSpecificHeatCapacityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two specific heat capacities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KSpecificHeatCapacityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two specific heat capacities by their normalized [value]. */
    override operator fun compareTo(other: KSpecificHeatCapacityUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized [value] (J/(kg·K)). */
    override fun equals(other: Any?): Boolean = other is KSpecificHeatCapacityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"4184.0 J/(kg·K)"`. */
    override fun toString(): String = "$value ${KSpecificHeatCapacityUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KSpecificHeatCapacityUnitInstance] from a value already expressed in J/(kg·K)
 * ([KSpecificHeatCapacityUnit.BASE]).
 *
 * This is the single creation source that every decomposition must funnel into: it assembles the canonical
 * normal-form [KMixedUnitInstance] with the three terms `distance²`, `time⁻²`, `temperature⁻¹`.
 */
internal fun specificHeatCapacityInstanceOf(joulesPerKilogramKelvin: Double): KSpecificHeatCapacityUnitInstance =
    KSpecificHeatCapacityUnitInstance(
        KMixedUnitInstance(
            joulesPerKilogramKelvin,
            listOf(
                KUnitTerm(KDistanceUnit.BASE, 2),
                KUnitTerm(KTimeUnit.BASE, -2),
                KUnitTerm(KTemperatureDifferenceUnit.BASE, -1),
            ),
        ),
    )

/** Builds a value-1 [KSpecificHeatCapacityUnitInstance] for the given [unit]. */
internal fun specificHeatCapacityOfUnit(unit: KSpecificHeatCapacityUnit): KSpecificHeatCapacityUnitInstance =
    specificHeatCapacityInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" specific heat capacity, as long as it matches the canonical normal
 * form: exactly one [KDistanceUnit] term at exponent `+2`, one [KTimeUnit] term at `-2` and one
 * [KTemperatureDifferenceUnit] term at `-1` (order independent).
 *
 * @throws IllegalStateException if this instance is not a canonical `distance²·time⁻²·temperature⁻¹`
 * specific heat capacity.
 */
fun KMixedUnitInstance.toSpecificHeatCapacity(): KSpecificHeatCapacityUnitInstance {
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -2 }
    val temperatureTerm = units.singleOrNull { it.unit is KTemperatureDifferenceUnit && it.exponent == -1 }
    check(units.size == 3 && distanceTerm != null && timeTerm != null && temperatureTerm != null) {
        "KMixedUnitInstance $this does not represent a pure specific heat capacity " +
                "(expected KDistanceUnit^2, KTimeUnit^-2 and KTemperatureDifferenceUnit^-1)"
    }
    val normalized = value *
            Math.pow(distanceTerm.unit.baseValue, 2.0) *
            Math.pow(timeTerm.unit.baseValue, -2.0) *
            Math.pow(temperatureTerm.unit.baseValue, -1.0)
    return specificHeatCapacityInstanceOf(normalized)
}
