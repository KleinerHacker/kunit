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

package org.pcsoft.framework.kunit.thermo.heattransfercoefficient

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **heat transfer coefficient** (the building-physics
 * *U-value*), i.e. exactly three terms in the canonical normal form `mass¹ · time⁻³ · temperature⁻¹`
 * (`kg·s⁻³·K⁻¹` = `W/(m²·K)`). The [value] is always normalized internally to W/(m²·K)
 * ([KHeatTransferCoefficientUnit.BASE]).
 *
 * As for the heat flux density the area cancels the watt's length dimensions, so the canonical normal
 * form carries **no** distance term. The temperature dimension is the **difference** group
 * ([KTemperatureDifferenceUnit]).
 *
 * Instances are created via the bare tokens, the prefixed templates, the typed operators in
 * `KHeatTransferCoefficientUnitOperators.kt` (`heatFluxDensity / temperatureDifference`,
 * `thermalConductivity / length`), or [toHeatTransferCoefficient].
 *
 * Example:
 * ```kotlin
 * val window = 1.3 of wattsPerSquareMeterKelvin // a decent triple-glazed window
 * ```
 */
class KHeatTransferCoefficientUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KHeatTransferCoefficientUnitInstance>, KUnitMeasurable by instance {

    /** Returns a new heat transfer coefficient with [value] (W/(m²·K)) scaled by [factor]. */
    override fun scaledBy(factor: Double): KHeatTransferCoefficientUnitInstance =
        heatTransferCoefficientInstanceOf(value * factor)

    /**
     * Adds two heat transfer coefficients, automatically converting between different
     * [KHeatTransferCoefficientUnit]s since both operands are always normalized internally.
     */
    override operator fun plus(other: KHeatTransferCoefficientUnitInstance): KHeatTransferCoefficientUnitInstance =
        heatTransferCoefficientInstanceOf(value + other.value)

    /** Subtracts two heat transfer coefficients. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KHeatTransferCoefficientUnitInstance): KHeatTransferCoefficientUnitInstance =
        heatTransferCoefficientInstanceOf(value - other.value)

    /** Multiplies two heat transfer coefficients, producing a new [KMixedUnitInstance]. */
    operator fun times(other: KHeatTransferCoefficientUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two heat transfer coefficients, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KHeatTransferCoefficientUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two heat transfer coefficients by their normalized [value] (W/(m²·K)). */
    override operator fun compareTo(other: KHeatTransferCoefficientUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized [value] (W/(m²·K)). */
    override fun equals(other: Any?): Boolean =
        other is KHeatTransferCoefficientUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"1.3 W/(m²·K)"`. */
    override fun toString(): String = "$value ${KHeatTransferCoefficientUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// Bridges the gram-based canonical product to W/(m²·K); the mass exponent is +1, so the bridge divides.
internal val HEAT_TRANSFER_COEFFICIENT_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KHeatTransferCoefficientUnitInstance] from a value already expressed in W/(m²·K)
 * ([KHeatTransferCoefficientUnit.BASE]).
 *
 * This is the single creation source that every decomposition must funnel into: it assembles the canonical
 * normal-form [KMixedUnitInstance] with the three terms `mass¹`, `time⁻³`, `temperature⁻¹`.
 */
internal fun heatTransferCoefficientInstanceOf(
    wattsPerSquareMeterKelvin: Double,
): KHeatTransferCoefficientUnitInstance =
    KHeatTransferCoefficientUnitInstance(
        KMixedUnitInstance(
            wattsPerSquareMeterKelvin,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KTimeUnit.BASE, -3),
                KUnitTerm(KTemperatureDifferenceUnit.BASE, -1),
            ),
        ),
    )

/** Builds a value-1 [KHeatTransferCoefficientUnitInstance] for the given [unit]. */
internal fun heatTransferCoefficientOfUnit(
    unit: KHeatTransferCoefficientUnit,
): KHeatTransferCoefficientUnitInstance = heatTransferCoefficientInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" heat transfer coefficient, as long as it matches the canonical
 * normal form: exactly one [KMassUnit] term at exponent `+1`, one [KTimeUnit] term at `-3` and one
 * [KTemperatureDifferenceUnit] term at `-1` (order independent).
 *
 * @throws IllegalStateException if this instance is not a canonical `mass¹·time⁻³·temperature⁻¹` heat
 * transfer coefficient.
 */
fun KMixedUnitInstance.toHeatTransferCoefficient(): KHeatTransferCoefficientUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -3 }
    val temperatureTerm = units.singleOrNull { it.unit is KTemperatureDifferenceUnit && it.exponent == -1 }
    check(units.size == 3 && massTerm != null && timeTerm != null && temperatureTerm != null) {
        "KMixedUnitInstance $this does not represent a pure heat transfer coefficient " +
                "(expected KMassUnit^1, KTimeUnit^-3 and KTemperatureDifferenceUnit^-1)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue *
            Math.pow(timeTerm.unit.baseValue, -3.0) *
            Math.pow(temperatureTerm.unit.baseValue, -1.0)
    return heatTransferCoefficientInstanceOf(gramBaseProduct / HEAT_TRANSFER_COEFFICIENT_MASS_REFERENCE)
}
