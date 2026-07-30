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

package org.pcsoft.framework.kunit.thermo.expansion

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **thermal expansion coefficient**, i.e. exactly one term in
 * the canonical normal form `temperature⁻¹` (`K⁻¹`). The [value] is always normalized internally to 1/K
 * ([KThermalExpansionUnit.BASE]).
 *
 * The coefficient is the *relative* change of a length (or area, or volume) per kelvin, so multiplying it
 * by a temperature difference yields a plain, dimensionless [Double] strain factor - see the operators in
 * `KThermalExpansionUnitOperators.kt`.
 *
 * The temperature dimension is the **difference** group ([KTemperatureDifferenceUnit]): the coefficient
 * describes a change per temperature *interval*.
 *
 * Example:
 * ```kotlin
 * val steel = 12 of ppmPerKelvin // 1.2e-5 1/K
 * ```
 */
class KThermalExpansionUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KThermalExpansionUnitInstance>, KUnitMeasurable by instance {

    /** Returns a new thermal expansion coefficient with [value] (1/K) scaled by [factor]. */
    override fun scaledBy(factor: Double): KThermalExpansionUnitInstance =
        thermalExpansionInstanceOf(value * factor)

    /**
     * Adds two thermal expansion coefficients, automatically converting between different
     * [KThermalExpansionUnit]s since both operands are always normalized to 1/K internally.
     */
    override operator fun plus(other: KThermalExpansionUnitInstance): KThermalExpansionUnitInstance =
        thermalExpansionInstanceOf(value + other.value)

    /** Subtracts two thermal expansion coefficients. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KThermalExpansionUnitInstance): KThermalExpansionUnitInstance =
        thermalExpansionInstanceOf(value - other.value)

    /** Multiplies two coefficients, producing a new [KMixedUnitInstance] (`K⁻²`). */
    operator fun times(other: KThermalExpansionUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two coefficients, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KThermalExpansionUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two coefficients by their normalized [value] (1/K). */
    override operator fun compareTo(other: KThermalExpansionUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized [value] (1/K). */
    override fun equals(other: Any?): Boolean = other is KThermalExpansionUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"1.2E-5 1/K"`. */
    override fun toString(): String = "$value ${KThermalExpansionUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KThermalExpansionUnitInstance] from a value already expressed in 1/K
 * ([KThermalExpansionUnit.BASE]).
 *
 * This is the single creation source that every decomposition must funnel into: it assembles the canonical
 * normal-form [KMixedUnitInstance] with the single term `temperature⁻¹`.
 */
internal fun thermalExpansionInstanceOf(perKelvin: Double): KThermalExpansionUnitInstance =
    KThermalExpansionUnitInstance(
        KMixedUnitInstance(
            perKelvin,
            listOf(KUnitTerm(KTemperatureDifferenceUnit.BASE, -1)),
        ),
    )

/** Builds a value-1 [KThermalExpansionUnitInstance] for the given [unit]. */
internal fun thermalExpansionOfUnit(unit: KThermalExpansionUnit): KThermalExpansionUnitInstance =
    thermalExpansionInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" thermal expansion coefficient, as long as it matches the canonical
 * normal form: exactly one [KTemperatureDifferenceUnit] term at exponent `-1`.
 *
 * @throws IllegalStateException if this instance is not a canonical `temperature⁻¹` coefficient.
 *
 * Example:
 * ```kotlin
 * (KTemperatureDifference.ofKelvin(1) pow -1).toThermalExpansion() // 1.0 1/K
 * ```
 */
fun KMixedUnitInstance.toThermalExpansion(): KThermalExpansionUnitInstance {
    val temperatureTerm = units.singleOrNull { it.unit is KTemperatureDifferenceUnit && it.exponent == -1 }
    check(units.size == 1 && temperatureTerm != null) {
        "KMixedUnitInstance $this does not represent a pure thermal expansion coefficient " +
            "(expected exactly one KTemperatureDifferenceUnit^-1 term)"
    }
    return thermalExpansionInstanceOf(value * Math.pow(temperatureTerm.unit.baseValue, -1.0))
}
