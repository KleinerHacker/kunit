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

package org.pcsoft.framework.kunit.electric.flux

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnit
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * Wraps a [KMixedUnitInstance] representing an **electric flux** (an electric field strength through an
 * area), i.e. exactly four terms in the canonical normal form - [KMassUnit.BASE] (gram) at exponent `+1`,
 * [KDistanceUnit.BASE] (meter) at exponent `+3`, [KTimeUnit.BASE] (second) at exponent `-3` and
 * [KElectricCurrentUnit.BASE] (ampere) at exponent `-1` (`kg·m³·s⁻³·A⁻¹` = `V·m`).
 *
 * Electric flux is a *constructed* unit group with one decomposition, funnelling into
 * [electricFluxInstanceOf]:
 * * `electricFieldStrength * area` (typed operator, see `KElectricFluxUnitOperators.kt`)
 *
 * Instances are additionally created via the bare tokens in `KElectricFluxUnitBareValues.kt` (e.g.
 * `5 of voltMeters`), the prefixed templates in `KElectricFluxUnitExtensions.kt`, or [toElectricFlux].
 *
 * Example:
 * ```kotlin
 * val phi = (1000 of voltsPerMeter) * ((0.1 of meters) * (0.05 of meters)) // 5 V·m
 * phi into voltMeters
 * ```
 */
class KElectricFluxUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KElectricFluxUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new electric flux with [value] (V·m) scaled by [factor]. Backs number-times-unit
     * construction (`5 of voltMeters`).
     */
    override fun scaledBy(factor: Double): KElectricFluxUnitInstance =
        electricFluxInstanceOf(value * factor)

    /**
     * Adds two electric fluxes, automatically converting between different [KElectricFluxUnit]s since both
     * operands are always normalized to [KElectricFluxUnit.BASE] (V·m) internally.
     */
    override operator fun plus(other: KElectricFluxUnitInstance): KElectricFluxUnitInstance =
        electricFluxInstanceOf(value + other.value)

    /** Subtracts two electric fluxes. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KElectricFluxUnitInstance): KElectricFluxUnitInstance =
        electricFluxInstanceOf(value - other.value)

    /** Multiplies two electric fluxes, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KElectricFluxUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two electric fluxes, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KElectricFluxUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two electric fluxes by their normalized [value] (V·m). */
    override operator fun compareTo(other: KElectricFluxUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two electric fluxes are equal iff they represent the same
     * quantity (e.g. `(1 of voltMeters) == (100 of voltCentimeters)`).
     */
    override fun equals(other: Any?): Boolean = other is KElectricFluxUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in V·m, e.g. `"5.0 V*m"`. */
    override fun toString(): String = "$value ${KElectricFluxUnit.BASE.symbol}"
}

// The volt's SI definition uses the *kilogram* as its mass dimension, whereas the mass group's base unit is
// the gram. The canonical normal form is therefore stored with the mass group's base term (gram), and this
// factor bridges a gram-based canonical product to volt meters. The mass exponent is *positive* (+1) here,
// so the bridge divides (like the volt itself).
private val VOLT_METER_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KElectricFluxUnitInstance] from a value already expressed in volt meters
 * ([KElectricFluxUnit.BASE]).
 *
 * This is the single creation source that every electric flux decomposition must funnel into: it assembles
 * the canonical normal-form [KMixedUnitInstance] with the four terms `mass¹`, `distance³`, `time⁻³` and
 * `current⁻¹` (each in its group's base unit).
 */
internal fun electricFluxInstanceOf(voltMeters: Double): KElectricFluxUnitInstance =
    KElectricFluxUnitInstance(
        KMixedUnitInstance(
            voltMeters,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 3),
                KUnitTerm(KTimeUnit.BASE, -3),
                KUnitTerm(KElectricCurrentUnit.BASE, -1),
            ),
        ),
    )

/**
 * Builds a value-1 [KElectricFluxUnitInstance] for the given [unit] (its [KElectricFluxUnit.baseValue]
 * V·m).
 */
internal fun electricFluxOfUnit(unit: KElectricFluxUnit): KElectricFluxUnitInstance =
    electricFluxInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" electric flux, as long as it matches the canonical normal form:
 * exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent `+3`, one
 * [KTimeUnit] term at exponent `-3` and one [KElectricCurrentUnit] term at exponent `-1` (order
 * independent). The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s and
 * bridged from the gram-based product to the kilogram-based volt.
 *
 * @throws IllegalStateException if this instance is not a canonical
 * `mass·distance³·time⁻³·current⁻¹` electric flux.
 */
fun KMixedUnitInstance.toElectricFlux(): KElectricFluxUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 3 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -3 }
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == -1 }
    check(
        units.size == 4 && massTerm != null && distanceTerm != null &&
                timeTerm != null && currentTerm != null
    ) {
        "KMixedUnitInstance $this does not represent a pure electric flux (expected one KMassUnit^1, " +
                "one KDistanceUnit^3, one KTimeUnit^-3 and one KElectricCurrentUnit^-1 term)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue *
            Math.pow(distanceTerm.unit.baseValue, 3.0) *
            Math.pow(timeTerm.unit.baseValue, -3.0) *
            Math.pow(currentTerm.unit.baseValue, -1.0)
    return electricFluxInstanceOf(gramBaseProduct / VOLT_METER_MASS_REFERENCE)
}
