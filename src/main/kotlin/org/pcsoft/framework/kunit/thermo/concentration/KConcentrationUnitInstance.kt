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

package org.pcsoft.framework.kunit.thermo.concentration

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnit

/**
 * Wraps a [KMixedUnitInstance] representing an **amount-of-substance concentration** (amount of substance
 * per volume), i.e. exactly two terms in the canonical normal form -
 * [KAmountOfSubstanceUnit.BASE] (mole) at exponent `+1` and [KDistanceUnit.BASE] (meter) at exponent `-3`
 * (`mol·m⁻³`). Both components are stored in their group's base unit, so the raw component base *is* the
 * named base unit ([KConcentrationUnit.MOLES_PER_CUBIC_METER]) and no bridging factor is needed.
 *
 * Concentration is a *constructed* unit group with one decomposition, funnelling into
 * [concentrationInstanceOf]:
 * * `amountOfSubstance / volume` (typed operator, see `KConcentrationUnitOperators.kt`)
 *
 * Instances are additionally created via the bare tokens in `KConcentrationUnitBareValues.kt` (e.g.
 * `0.1 of molesPerLiter`), the prefixed templates in `KConcentrationUnitExtensions.kt`, or
 * [toConcentration].
 *
 * Example:
 * ```kotlin
 * val c = (0.5 of moles) / (2 of liters) // 0.25 mol/l
 * c into molesPerLiter
 * ```
 */
class KConcentrationUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KConcentrationUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new concentration with [value] (mol/m³) scaled by [factor]. Backs number-times-unit
     * construction (`0.1 of molesPerLiter`).
     */
    override fun scaledBy(factor: Double): KConcentrationUnitInstance = concentrationInstanceOf(value * factor)

    /**
     * Adds two concentrations, automatically converting between different [KConcentrationUnit]s since both
     * operands are always normalized to [KConcentrationUnit.BASE] (mol/m³) internally.
     */
    override operator fun plus(other: KConcentrationUnitInstance): KConcentrationUnitInstance =
        concentrationInstanceOf(value + other.value)

    /** Subtracts two concentrations. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KConcentrationUnitInstance): KConcentrationUnitInstance =
        concentrationInstanceOf(value - other.value)

    /** Multiplies two concentrations, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KConcentrationUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two concentrations, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KConcentrationUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two concentrations by their normalized [value] (mol/m³). */
    override operator fun compareTo(other: KConcentrationUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two concentrations are equal iff they represent the same
     * quantity (e.g. `(1 of molesPerLiter) == (1000 of molesPerCubicMeter)`).
     */
    override fun equals(other: Any?): Boolean = other is KConcentrationUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in mol/m³, e.g. `"250.0 mol/m^3"`. */
    override fun toString(): String = "$value ${KConcentrationUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KConcentrationUnitInstance] from a value already expressed in moles per cubic meter
 * ([KConcentrationUnit.BASE]).
 *
 * This is the single creation source that every concentration decomposition must funnel into: it assembles
 * the canonical normal-form [KMixedUnitInstance] with the two terms `substance¹` and `distance⁻³` (each in
 * its group's base unit).
 */
internal fun concentrationInstanceOf(molesPerCubicMeter: Double): KConcentrationUnitInstance =
    KConcentrationUnitInstance(
        KMixedUnitInstance(
            molesPerCubicMeter,
            listOf(
                KUnitTerm(KAmountOfSubstanceUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, -3),
            ),
        ),
    )

/**
 * Builds a value-1 [KConcentrationUnitInstance] for the given [unit] (its [KConcentrationUnit.baseValue]
 * mol/m³).
 */
internal fun concentrationOfUnit(unit: KConcentrationUnit): KConcentrationUnitInstance =
    concentrationInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" concentration, as long as it matches the canonical concentration
 * normal form: exactly one [KAmountOfSubstanceUnit] term at exponent `+1` and one [KDistanceUnit] term at
 * exponent `-3` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the result is expressed in mol/m³ regardless of which
 * concrete units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `substance·distance⁻³` concentration.
 */
fun KMixedUnitInstance.toConcentration(): KConcentrationUnitInstance {
    val substanceTerm = units.singleOrNull { it.unit is KAmountOfSubstanceUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -3 }
    check(units.size == 2 && substanceTerm != null && distanceTerm != null) {
        "KMixedUnitInstance $this does not represent a pure concentration " +
                "(expected one KAmountOfSubstanceUnit^1 and one KDistanceUnit^-3 term)"
    }
    val base = distanceTerm.unit.baseValue
    return concentrationInstanceOf(value * substanceTerm.unit.baseValue / (base * base * base))
}
