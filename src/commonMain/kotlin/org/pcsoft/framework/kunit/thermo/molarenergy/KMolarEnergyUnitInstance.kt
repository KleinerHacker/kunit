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

package org.pcsoft.framework.kunit.thermo.molarenergy

import kotlin.math.pow
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **molar energy** (energy per amount of substance), i.e.
 * exactly four terms in the canonical normal form `mass¹ · distance² · time⁻² · substance⁻¹`
 * (`kg·m²·s⁻²·mol⁻¹` = `J/mol`). The [value] is always normalized internally to joules per mole
 * ([KMolarEnergyUnit.BASE]).
 *
 * Molar energy is a *constructed* unit group. Instances are created via the bare tokens in
 * `KMolarEnergyUnitBareValues.kt` (e.g. `5 of joulesPerMole`), the prefixed templates in
 * `KMolarEnergyUnitExtensions.kt` (e.g. `2 of kilo.joulesPerMole`), the typed
 * `energy / amountOfSubstance` operator in `KMolarEnergyUnitOperators.kt`, or [toMolarEnergy].
 *
 * Example:
 * ```kotlin
 * val dH = (-286 of kilo.joules) / (1 of moles) // enthalpy of formation of liquid water
 * dH into kilo.joulesPerMole                     // -286.0
 * ```
 */
class KMolarEnergyUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KMolarEnergyUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new molar energy with [value] (J/mol) scaled by [factor]. Backs number-times-unit
     * construction (`10 of joulesPerMole`).
     */
    override fun scaledBy(factor: Double): KMolarEnergyUnitInstance = molarEnergyInstanceOf(value * factor)

    /**
     * Adds two molar energies, automatically converting between different [KMolarEnergyUnit]s since both
     * operands are always normalized to [KMolarEnergyUnit.BASE] (J/mol) internally.
     */
    override operator fun plus(other: KMolarEnergyUnitInstance): KMolarEnergyUnitInstance =
        molarEnergyInstanceOf(value + other.value)

    /** Subtracts two molar energies. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KMolarEnergyUnitInstance): KMolarEnergyUnitInstance =
        molarEnergyInstanceOf(value - other.value)

    /** Multiplies two molar energies, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KMolarEnergyUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two molar energies, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KMolarEnergyUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two molar energies by their normalized [value] (J/mol). */
    override operator fun compareTo(other: KMolarEnergyUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two molar energies are equal iff they represent the same
     * quantity (e.g. `(1 of kilo.joulesPerMole) == (1000 of joulesPerMole)`).
     */
    override fun equals(other: Any?): Boolean = other is KMolarEnergyUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"-286000.0 J/mol"`. */
    override fun toString(): String = "${renderDouble(value)} ${KMolarEnergyUnit.BASE.symbol}"
}

// --- Canonical reference --------------------------------------------------------------------------

// The joule's SI definition uses the *kilogram* as its mass dimension, whereas the mass group's base unit
// is the gram. This factor bridges a gram-based canonical product to J/mol; the mass exponent is +1, so
// the bridge divides.
internal val MOLAR_ENERGY_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KMolarEnergyUnitInstance] from a value already expressed in joules per mole
 * ([KMolarEnergyUnit.BASE]).
 *
 * This is the single creation source that every molar energy decomposition must funnel into: it assembles
 * the canonical normal-form [KMixedUnitInstance] with the four terms `mass¹`, `distance²`, `time⁻²`,
 * `substance⁻¹` (each in its group's base unit).
 */
internal fun molarEnergyInstanceOf(joulesPerMole: Double): KMolarEnergyUnitInstance =
    KMolarEnergyUnitInstance(
        KMixedUnitInstance(
            joulesPerMole,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 2),
                KUnitTerm(KTimeUnit.BASE, -2),
                KUnitTerm(KAmountOfSubstanceUnit.BASE, -1),
            ),
        ),
    )

/** Builds a value-1 [KMolarEnergyUnitInstance] for the given [unit] (its [KMolarEnergyUnit.baseValue] J/mol). */
internal fun molarEnergyOfUnit(unit: KMolarEnergyUnit): KMolarEnergyUnitInstance =
    molarEnergyInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" molar energy, as long as it matches the canonical molar energy
 * normal form: exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at `+2`, one
 * [KTimeUnit] term at `-2` and one [KAmountOfSubstanceUnit] term at `-1` (order independent). The terms
 * are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the result is expressed in
 * J/mol regardless of which concrete units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical
 * `mass¹·distance²·time⁻²·substance⁻¹` molar energy.
 */
fun KMixedUnitInstance.toMolarEnergy(): KMolarEnergyUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -2 }
    val substanceTerm = units.singleOrNull { it.unit is KAmountOfSubstanceUnit && it.exponent == -1 }
    check(units.size == 4 && massTerm != null && distanceTerm != null && timeTerm != null && substanceTerm != null) {
        "KMixedUnitInstance $this does not represent a pure molar energy " +
                "(expected KMassUnit^1, KDistanceUnit^2, KTimeUnit^-2 and KAmountOfSubstanceUnit^-1)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue *
            distanceTerm.unit.baseValue.pow(2.0) *
            timeTerm.unit.baseValue.pow(-2.0) *
            substanceTerm.unit.baseValue.pow(-1.0)
    return molarEnergyInstanceOf(gramBaseProduct / MOLAR_ENERGY_MASS_REFERENCE)
}
