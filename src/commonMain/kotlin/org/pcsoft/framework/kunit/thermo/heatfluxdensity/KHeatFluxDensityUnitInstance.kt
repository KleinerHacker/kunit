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

package org.pcsoft.framework.kunit.thermo.heatfluxdensity

import kotlin.math.pow
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **heat flux density** (power per unit of area), i.e. exactly
 * two terms in the canonical normal form `mass¹ · time⁻³` (`kg·s⁻³` = `W/m²`). The [value] is always
 * normalized internally to watts per square meter ([KHeatFluxDensityUnit.BASE]).
 *
 * Note that the area cancels the watt's two length dimensions: `W/m² = kg·m²·s⁻³/m² = kg·s⁻³`, so the
 * canonical normal form carries **no** distance term at all.
 *
 * Instances are created via the bare tokens in `KHeatFluxDensityUnitBareValues.kt`, the prefixed templates
 * in `KHeatFluxDensityUnitExtensions.kt`, the typed `power / area` operator in
 * `KHeatFluxDensityUnitOperators.kt`, or [toHeatFluxDensity].
 *
 * Example:
 * ```kotlin
 * val q = (1000 of watts) / ((2 of meters) * (1 of meters)) // 500 W/m²
 * ```
 */
class KHeatFluxDensityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KHeatFluxDensityUnitInstance>, KUnitMeasurable by instance {

    /** Returns a new heat flux density with [value] (W/m²) scaled by [factor]. */
    override fun scaledBy(factor: Double): KHeatFluxDensityUnitInstance = heatFluxDensityInstanceOf(value * factor)

    /**
     * Adds two heat flux densities, automatically converting between different [KHeatFluxDensityUnit]s
     * since both operands are always normalized to W/m² internally.
     */
    override operator fun plus(other: KHeatFluxDensityUnitInstance): KHeatFluxDensityUnitInstance =
        heatFluxDensityInstanceOf(value + other.value)

    /** Subtracts two heat flux densities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KHeatFluxDensityUnitInstance): KHeatFluxDensityUnitInstance =
        heatFluxDensityInstanceOf(value - other.value)

    /** Multiplies two heat flux densities, producing a new [KMixedUnitInstance]. */
    operator fun times(other: KHeatFluxDensityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two heat flux densities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KHeatFluxDensityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two heat flux densities by their normalized [value] (W/m²). */
    override operator fun compareTo(other: KHeatFluxDensityUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized [value] (W/m²). */
    override fun equals(other: Any?): Boolean = other is KHeatFluxDensityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"1361.0 W/m²"`. */
    override fun toString(): String = "${renderDouble(value)} ${KHeatFluxDensityUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// Bridges the gram-based canonical product to W/m²; the mass exponent is +1, so the bridge divides.
internal val HEAT_FLUX_DENSITY_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KHeatFluxDensityUnitInstance] from a value already expressed in W/m²
 * ([KHeatFluxDensityUnit.BASE]).
 *
 * This is the single creation source that every decomposition must funnel into: it assembles the canonical
 * normal-form [KMixedUnitInstance] with the two terms `mass¹` and `time⁻³`.
 */
internal fun heatFluxDensityInstanceOf(wattsPerSquareMeter: Double): KHeatFluxDensityUnitInstance =
    KHeatFluxDensityUnitInstance(
        KMixedUnitInstance(
            wattsPerSquareMeter,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KTimeUnit.BASE, -3),
            ),
        ),
    )

/** Builds a value-1 [KHeatFluxDensityUnitInstance] for the given [unit]. */
internal fun heatFluxDensityOfUnit(unit: KHeatFluxDensityUnit): KHeatFluxDensityUnitInstance =
    heatFluxDensityInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" heat flux density, as long as it matches the canonical normal form:
 * exactly one [KMassUnit] term at exponent `+1` and one [KTimeUnit] term at `-3` (order independent).
 *
 * @throws IllegalStateException if this instance is not a canonical `mass¹·time⁻³` heat flux density.
 */
fun KMixedUnitInstance.toHeatFluxDensity(): KHeatFluxDensityUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -3 }
    check(units.size == 2 && massTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure heat flux density (expected KMassUnit^1 and KTimeUnit^-3)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue *
            timeTerm.unit.baseValue.pow(-3.0)
    return heatFluxDensityInstanceOf(gramBaseProduct / HEAT_FLUX_DENSITY_MASS_REFERENCE)
}
