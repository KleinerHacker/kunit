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

package org.pcsoft.framework.kunit.thermo.molarheatcapacity

import kotlin.math.pow
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnit
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **molar heat capacity** (heat capacity per amount of
 * substance), i.e. exactly five terms in the canonical normal form
 * `mass¹ · distance² · time⁻² · substance⁻¹ · temperature⁻¹` (`kg·m²·s⁻²·mol⁻¹·K⁻¹` = `J/(mol·K)`). The
 * [value] is always normalized internally to J/(mol·K) ([KMolarHeatCapacityUnit.BASE]).
 *
 * The temperature dimension is the **difference** group ([KTemperatureDifferenceUnit]), never the affine
 * absolute temperature.
 *
 * Instances are created via the bare tokens in `KMolarHeatCapacityUnitBareValues.kt`, the prefixed
 * templates in `KMolarHeatCapacityUnitExtensions.kt`, the typed operators in
 * `KMolarHeatCapacityUnitOperators.kt` (`heatCapacity / amountOfSubstance`,
 * `molarEnergy / temperatureDifference`), or [toMolarHeatCapacity].
 *
 * Example:
 * ```kotlin
 * val cp = 29.1 of joulesPerMoleKelvin // diatomic nitrogen at constant pressure
 * ```
 */
class KMolarHeatCapacityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KMolarHeatCapacityUnitInstance>, KUnitMeasurable by instance {

    /** Returns a new molar heat capacity with [value] (J/(mol·K)) scaled by [factor]. */
    override fun scaledBy(factor: Double): KMolarHeatCapacityUnitInstance =
        molarHeatCapacityInstanceOf(value * factor)

    /**
     * Adds two molar heat capacities, automatically converting between different
     * [KMolarHeatCapacityUnit]s since both operands are always normalized to the base internally.
     */
    override operator fun plus(other: KMolarHeatCapacityUnitInstance): KMolarHeatCapacityUnitInstance =
        molarHeatCapacityInstanceOf(value + other.value)

    /** Subtracts two molar heat capacities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KMolarHeatCapacityUnitInstance): KMolarHeatCapacityUnitInstance =
        molarHeatCapacityInstanceOf(value - other.value)

    /** Multiplies two molar heat capacities, producing a new [KMixedUnitInstance]. */
    operator fun times(other: KMolarHeatCapacityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two molar heat capacities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KMolarHeatCapacityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two molar heat capacities by their normalized [value]. */
    override operator fun compareTo(other: KMolarHeatCapacityUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized [value] (J/(mol·K)). */
    override fun equals(other: Any?): Boolean = other is KMolarHeatCapacityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"29.1 J/(mol·K)"`. */
    override fun toString(): String = "${renderDouble(value)} ${KMolarHeatCapacityUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// Bridges the gram-based canonical product to J/(mol·K); the mass exponent is +1, so the bridge divides.
internal val MOLAR_HEAT_CAPACITY_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KMolarHeatCapacityUnitInstance] from a value already expressed in J/(mol·K)
 * ([KMolarHeatCapacityUnit.BASE]).
 *
 * This is the single creation source that every decomposition must funnel into: it assembles the canonical
 * normal-form [KMixedUnitInstance] with the five terms `mass¹`, `distance²`, `time⁻²`, `substance⁻¹`,
 * `temperature⁻¹`.
 */
internal fun molarHeatCapacityInstanceOf(joulesPerMoleKelvin: Double): KMolarHeatCapacityUnitInstance =
    KMolarHeatCapacityUnitInstance(
        KMixedUnitInstance(
            joulesPerMoleKelvin,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 2),
                KUnitTerm(KTimeUnit.BASE, -2),
                KUnitTerm(KAmountOfSubstanceUnit.BASE, -1),
                KUnitTerm(KTemperatureDifferenceUnit.BASE, -1),
            ),
        ),
    )

/** Builds a value-1 [KMolarHeatCapacityUnitInstance] for the given [unit]. */
internal fun molarHeatCapacityOfUnit(unit: KMolarHeatCapacityUnit): KMolarHeatCapacityUnitInstance =
    molarHeatCapacityInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" molar heat capacity, as long as it matches the canonical normal
 * form: exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at `+2`, one [KTimeUnit]
 * term at `-2`, one [KAmountOfSubstanceUnit] term at `-1` and one [KTemperatureDifferenceUnit] term at
 * `-1` (order independent).
 *
 * @throws IllegalStateException if this instance is not a canonical
 * `mass¹·distance²·time⁻²·substance⁻¹·temperature⁻¹` molar heat capacity.
 */
fun KMixedUnitInstance.toMolarHeatCapacity(): KMolarHeatCapacityUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -2 }
    val substanceTerm = units.singleOrNull { it.unit is KAmountOfSubstanceUnit && it.exponent == -1 }
    val temperatureTerm = units.singleOrNull { it.unit is KTemperatureDifferenceUnit && it.exponent == -1 }
    check(
        units.size == 5 && massTerm != null && distanceTerm != null && timeTerm != null &&
                substanceTerm != null && temperatureTerm != null,
    ) {
        "KMixedUnitInstance $this does not represent a pure molar heat capacity " +
                "(expected KMassUnit^1, KDistanceUnit^2, KTimeUnit^-2, KAmountOfSubstanceUnit^-1 and " +
                "KTemperatureDifferenceUnit^-1)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue *
            distanceTerm.unit.baseValue.pow(2.0) *
            timeTerm.unit.baseValue.pow(-2.0) *
            substanceTerm.unit.baseValue.pow(-1.0) *
            temperatureTerm.unit.baseValue.pow(-1.0)
    return molarHeatCapacityInstanceOf(gramBaseProduct / MOLAR_HEAT_CAPACITY_MASS_REFERENCE)
}
