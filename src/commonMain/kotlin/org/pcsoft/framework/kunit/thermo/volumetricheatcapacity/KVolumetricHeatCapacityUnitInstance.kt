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

package org.pcsoft.framework.kunit.thermo.volumetricheatcapacity

import kotlin.math.pow
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **volumetric heat capacity** (heat capacity per volume), i.e.
 * exactly four terms in the canonical normal form - [KMassUnit.BASE] (gram) at exponent `+1`,
 * [KDistanceUnit.BASE] (meter) at exponent `-1`, [KTimeUnit.BASE] (second) at exponent `-2` and
 * [KTemperatureDifferenceUnit.BASE] at exponent `-1` (`kg·m⁻¹·s⁻²·K⁻¹` = `J/(m³·K)`).
 *
 * Volumetric heat capacity is a *constructed* unit group with two decompositions, both funnelling into
 * [volumetricHeatCapacityInstanceOf]:
 * * `heatCapacity / volume` (typed operator, see `KVolumetricHeatCapacityUnitOperators.kt`)
 * * `specificHeatCapacity * density` (typed operator, same file)
 *
 * Instances are additionally created via the bare tokens in
 * `KVolumetricHeatCapacityUnitBareValues.kt`, the prefixed templates in
 * `KVolumetricHeatCapacityUnitExtensions.kt`, or [toVolumetricHeatCapacity].
 *
 * Example:
 * ```kotlin
 * val cv = (4184 of joulesPerKilogramKelvin) * water // water: ≈ 4.18 MJ/(m³·K)
 * cv into mega.joulesPerCubicMeterKelvin
 * ```
 */
class KVolumetricHeatCapacityUnitInstance internal constructor(
    internal val instance: KMixedUnitInstance
) : KUnitInstance<KVolumetricHeatCapacityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new volumetric heat capacity with [value] (J/(m³·K)) scaled by [factor]. Backs
     * number-times-unit construction (`4.18 of mega.joulesPerCubicMeterKelvin`).
     */
    override fun scaledBy(factor: Double): KVolumetricHeatCapacityUnitInstance =
        volumetricHeatCapacityInstanceOf(value * factor)

    /**
     * Adds two volumetric heat capacities, automatically converting between different
     * [KVolumetricHeatCapacityUnit]s since both operands are always normalized to
     * [KVolumetricHeatCapacityUnit.BASE] internally.
     */
    override operator fun plus(
        other: KVolumetricHeatCapacityUnitInstance
    ): KVolumetricHeatCapacityUnitInstance = volumetricHeatCapacityInstanceOf(value + other.value)

    /** Subtracts two volumetric heat capacities. See [plus] for the automatic unit conversion. */
    override operator fun minus(
        other: KVolumetricHeatCapacityUnitInstance
    ): KVolumetricHeatCapacityUnitInstance = volumetricHeatCapacityInstanceOf(value - other.value)

    /** Multiplies two volumetric heat capacities, producing a new [KMixedUnitInstance]. */
    operator fun times(other: KVolumetricHeatCapacityUnitInstance): KMixedUnitInstance =
        instance * other.instance

    /** Divides two volumetric heat capacities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KVolumetricHeatCapacityUnitInstance): KMixedUnitInstance =
        instance / other.instance

    /** Compares two volumetric heat capacities by their normalized [value] (J/(m³·K)). */
    override operator fun compareTo(other: KVolumetricHeatCapacityUnitInstance): Int =
        value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two volumetric heat capacities are equal iff they
     * represent the same quantity.
     */
    override fun equals(other: Any?): Boolean =
        other is KVolumetricHeatCapacityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in J/(m³·K), e.g. `"4184000.0 J/(m^3*K)"`. */
    override fun toString(): String = "${renderDouble(value)} ${KVolumetricHeatCapacityUnit.BASE.symbol}"
}

// The joule's SI definition uses the *kilogram* as its mass dimension, whereas the mass group's base unit
// is the gram. The canonical normal form is therefore stored with the mass group's base term (gram), and
// this factor bridges a gram-based canonical product to the named unit. The mass exponent is *positive*
// (+1) here, so the bridge divides (like the watt).
internal val VOLUMETRIC_HEAT_CAPACITY_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KVolumetricHeatCapacityUnitInstance] from a value already expressed in joules per cubic meter
 * kelvin ([KVolumetricHeatCapacityUnit.BASE]).
 *
 * This is the single creation source that every decomposition must funnel into: it assembles the canonical
 * normal-form [KMixedUnitInstance] with the four terms `mass¹`, `distance⁻¹`, `time⁻²` and `temperature⁻¹`
 * (each in its group's base unit).
 */
internal fun volumetricHeatCapacityInstanceOf(
    joulesPerCubicMeterKelvin: Double
): KVolumetricHeatCapacityUnitInstance =
    KVolumetricHeatCapacityUnitInstance(
        KMixedUnitInstance(
            joulesPerCubicMeterKelvin,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, -1),
                KUnitTerm(KTimeUnit.BASE, -2),
                KUnitTerm(KTemperatureDifferenceUnit.BASE, -1),
            ),
        ),
    )

/** Builds a value-1 [KVolumetricHeatCapacityUnitInstance] for the given [unit]. */
internal fun volumetricHeatCapacityOfUnit(
    unit: KVolumetricHeatCapacityUnit
): KVolumetricHeatCapacityUnitInstance = volumetricHeatCapacityInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" volumetric heat capacity, as long as it matches the canonical normal
 * form: exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent `-1`, one
 * [KTimeUnit] term at exponent `-2` and one [KTemperatureDifferenceUnit] term at exponent `-1` (order
 * independent). The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s and
 * bridged from the gram-based product to the kilogram-based joule.
 *
 * @throws IllegalStateException if this instance is not a canonical
 * `mass·distance⁻¹·time⁻²·temperature⁻¹` volumetric heat capacity.
 */
fun KMixedUnitInstance.toVolumetricHeatCapacity(): KVolumetricHeatCapacityUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -2 }
    val temperatureTerm = units.singleOrNull { it.unit is KTemperatureDifferenceUnit && it.exponent == -1 }
    check(
        units.size == 4 && massTerm != null && distanceTerm != null &&
                timeTerm != null && temperatureTerm != null
    ) {
        "KMixedUnitInstance $this does not represent a pure volumetric heat capacity (expected one " +
                "KMassUnit^1, one KDistanceUnit^-1, one KTimeUnit^-2 and one KTemperatureDifferenceUnit^-1 term)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue *
            distanceTerm.unit.baseValue.pow(-1.0) *
            timeTerm.unit.baseValue.pow(-2.0) *
            temperatureTerm.unit.baseValue.pow(-1.0)
    return volumetricHeatCapacityInstanceOf(gramBaseProduct / VOLUMETRIC_HEAT_CAPACITY_MASS_REFERENCE)
}
