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

package org.pcsoft.framework.kunit.thermo.heatcapacity

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **heat capacity**, i.e. exactly four terms in the canonical
 * normal form `mass¹ · distance² · time⁻² · temperature⁻¹` (`kg·m²·s⁻²·K⁻¹` = `J/K`). The [value] is
 * always normalized internally to joules per kelvin ([KHeatCapacityUnit.BASE]), regardless of which
 * [KHeatCapacityUnit], SI prefix, or energy/temperature combination it was constructed from.
 *
 * The temperature dimension is deliberately the **difference** group
 * ([KTemperatureDifferenceUnit]), never the affine absolute temperature: a heat capacity relates energy
 * to a temperature *interval*, so an offset-carrying absolute scale would be physically wrong here.
 *
 * Heat capacity is a *constructed* unit group. Instances are created via the bare tokens in
 * `KHeatCapacityUnitBareValues.kt` (e.g. `5 of joulesPerKelvin`), the prefixed templates in
 * `KHeatCapacityUnitExtensions.kt` (e.g. `2 of kilo.joulesPerKelvin`), the typed
 * `energy / temperatureDifference` operator in `KHeatCapacityUnitOperators.kt`, or [toHeatCapacity].
 *
 * Example:
 * ```kotlin
 * val c = (4184 of joules) / KTemperatureDifference.ofKelvin(1) // 4184 J/K
 * c into kilo.joulesPerKelvin                                    // 4.184
 * ```
 */
class KHeatCapacityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KHeatCapacityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new heat capacity with [value] (J/K) scaled by [factor]. Backs number-times-unit
     * construction (`10 of joulesPerKelvin`).
     */
    override fun scaledBy(factor: Double): KHeatCapacityUnitInstance = heatCapacityInstanceOf(value * factor)

    /**
     * Adds two heat capacities, automatically converting between different [KHeatCapacityUnit]s since both
     * operands are always normalized to [KHeatCapacityUnit.BASE] (J/K) internally.
     */
    override operator fun plus(other: KHeatCapacityUnitInstance): KHeatCapacityUnitInstance =
        heatCapacityInstanceOf(value + other.value)

    /** Subtracts two heat capacities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KHeatCapacityUnitInstance): KHeatCapacityUnitInstance =
        heatCapacityInstanceOf(value - other.value)

    /** Multiplies two heat capacities, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KHeatCapacityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two heat capacities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KHeatCapacityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two heat capacities by their normalized [value] (J/K). */
    override operator fun compareTo(other: KHeatCapacityUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two heat capacities are equal iff they represent the same
     * quantity (e.g. `(1 of kilo.joulesPerKelvin) == (1000 of joulesPerKelvin)`).
     */
    override fun equals(other: Any?): Boolean = other is KHeatCapacityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"4184.0 J/K"`. */
    override fun toString(): String = "$value ${KHeatCapacityUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// The joule's SI definition uses the *kilogram* as its mass dimension, whereas the mass group's base unit
// is the gram. The canonical normal form is therefore stored with the mass group's base term (gram), and
// this factor bridges a gram-based canonical product to J/K. The mass exponent is +1, so the bridge
// divides.
internal val HEAT_CAPACITY_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KHeatCapacityUnitInstance] from a value already expressed in joules per kelvin
 * ([KHeatCapacityUnit.BASE]).
 *
 * This is the single creation source that every heat capacity decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the four terms `mass¹`, `distance²`,
 * `time⁻²`, `temperature⁻¹` (each in its group's base unit).
 */
internal fun heatCapacityInstanceOf(joulesPerKelvin: Double): KHeatCapacityUnitInstance =
    KHeatCapacityUnitInstance(
        KMixedUnitInstance(
            joulesPerKelvin,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 2),
                KUnitTerm(KTimeUnit.BASE, -2),
                KUnitTerm(KTemperatureDifferenceUnit.BASE, -1),
            ),
        ),
    )

/** Builds a value-1 [KHeatCapacityUnitInstance] for the given [unit] (its [KHeatCapacityUnit.baseValue] J/K). */
internal fun heatCapacityOfUnit(unit: KHeatCapacityUnit): KHeatCapacityUnitInstance =
    heatCapacityInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" heat capacity, as long as it matches the canonical heat capacity
 * normal form: exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at `+2`, one
 * [KTimeUnit] term at `-2` and one [KTemperatureDifferenceUnit] term at `-1` (order independent). The
 * terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the result is
 * expressed in J/K regardless of which concrete units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass¹·distance²·time⁻²·temperature⁻¹`
 * heat capacity.
 *
 * Example:
 * ```kotlin
 * val raw = (1 of kilo.grams).toUnit() * ((1 of meters).toUnit() pow 2) /
 *     ((1 of seconds).toUnit() pow 2) / KTemperatureDifference.ofKelvin(1).toUnit()
 * raw.toHeatCapacity() into joulesPerKelvin // 1.0
 * ```
 */
fun KMixedUnitInstance.toHeatCapacity(): KHeatCapacityUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -2 }
    val temperatureTerm = units.singleOrNull { it.unit is KTemperatureDifferenceUnit && it.exponent == -1 }
    check(units.size == 4 && massTerm != null && distanceTerm != null && timeTerm != null && temperatureTerm != null) {
        "KMixedUnitInstance $this does not represent a pure heat capacity " +
                "(expected KMassUnit^1, KDistanceUnit^2, KTimeUnit^-2 and KTemperatureDifferenceUnit^-1)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue *
            Math.pow(distanceTerm.unit.baseValue, 2.0) *
            Math.pow(timeTerm.unit.baseValue, -2.0) *
            Math.pow(temperatureTerm.unit.baseValue, -1.0)
    return heatCapacityInstanceOf(gramBaseProduct / HEAT_CAPACITY_MASS_REFERENCE)
}
