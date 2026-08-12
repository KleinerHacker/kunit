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

package org.pcsoft.framework.kunit.electric.molarconductivity

import kotlin.math.pow
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnit
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **molar conductivity** (electrolytic conductivity per
 * concentration), i.e. exactly four terms in the canonical normal form - [KMassUnit.BASE] (gram) at
 * exponent `-1`, [KTimeUnit.BASE] (second) at exponent `+3`, [KElectricCurrentUnit.BASE] (ampere) at
 * exponent `+2` and [KAmountOfSubstanceUnit.BASE] (mole) at exponent `-1`
 * (`kg⁻¹·s³·A²·mol⁻¹` = `S·m²/mol`).
 *
 * The length dimension cancels: the conductivity contributes `distance⁻³` and the concentration
 * `distance⁻³` in the denominator, so no distance term remains.
 *
 * Molar conductivity is a *constructed* unit group with one decomposition, funnelling into
 * [molarConductivityInstanceOf]:
 * * `conductivity / concentration` (typed operator, see `KMolarConductivityUnitOperators.kt`)
 *
 * Instances are additionally created via the bare tokens in `KMolarConductivityUnitBareValues.kt`, the
 * prefixed templates in `KMolarConductivityUnitExtensions.kt`, or [toMolarConductivity].
 *
 * Example:
 * ```kotlin
 * val lambda = (1.29 of milli.siemensPerMeter) / (0.1 of molesPerLiter)
 * lambda into milli.siemensSquareMetersPerMole
 * ```
 */
class KMolarConductivityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KMolarConductivityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new molar conductivity with [value] (S·m²/mol) scaled by [factor]. Backs number-times-unit
     * construction (`12.6 of milli.siemensSquareMetersPerMole`).
     */
    override fun scaledBy(factor: Double): KMolarConductivityUnitInstance =
        molarConductivityInstanceOf(value * factor)

    /**
     * Adds two molar conductivities, automatically converting between different [KMolarConductivityUnit]s
     * since both operands are always normalized to [KMolarConductivityUnit.BASE] internally. This is how
     * Kohlrausch's law of independent ion migration combines the ionic contributions.
     */
    override operator fun plus(other: KMolarConductivityUnitInstance): KMolarConductivityUnitInstance =
        molarConductivityInstanceOf(value + other.value)

    /** Subtracts two molar conductivities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KMolarConductivityUnitInstance): KMolarConductivityUnitInstance =
        molarConductivityInstanceOf(value - other.value)

    /** Multiplies two molar conductivities, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KMolarConductivityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two molar conductivities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KMolarConductivityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two molar conductivities by their normalized [value] (S·m²/mol). */
    override operator fun compareTo(other: KMolarConductivityUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two molar conductivities are equal iff they represent the
     * same quantity (e.g. `(1 of siemensSquareMetersPerMole) == (10000 of siemensSquareCentimetersPerMole)`).
     */
    override fun equals(other: Any?): Boolean =
        other is KMolarConductivityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in S·m²/mol, e.g. `"0.0126 S*m^2/mol"`. */
    override fun toString(): String = "${renderDouble(value)} ${KMolarConductivityUnit.BASE.symbol}"
}

// The siemens' SI definition uses the *kilogram* as its mass dimension, whereas the mass group's base unit
// is the gram. The canonical normal form is therefore stored with the mass group's base term (gram), and
// this factor bridges a gram-based canonical product to the named unit. The mass exponent is *negative*
// (-1) here, so the bridge is applied as `pow(reference, -1.0)` (like the siemens itself).
private val MOLAR_CONDUCTIVITY_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KMolarConductivityUnitInstance] from a value already expressed in siemens square meters per
 * mole ([KMolarConductivityUnit.BASE]).
 *
 * This is the single creation source that every molar conductivity decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the four terms `mass⁻¹`, `time³`,
 * `current²` and `substance⁻¹` (each in its group's base unit).
 */
internal fun molarConductivityInstanceOf(
    siemensSquareMetersPerMole: Double
): KMolarConductivityUnitInstance =
    KMolarConductivityUnitInstance(
        KMixedUnitInstance(
            siemensSquareMetersPerMole,
            listOf(
                KUnitTerm(KMassUnit.BASE, -1),
                KUnitTerm(KTimeUnit.BASE, 3),
                KUnitTerm(KElectricCurrentUnit.BASE, 2),
                KUnitTerm(KAmountOfSubstanceUnit.BASE, -1),
            ),
        ),
    )

/**
 * Builds a value-1 [KMolarConductivityUnitInstance] for the given [unit] (its
 * [KMolarConductivityUnit.baseValue] S·m²/mol).
 */
internal fun molarConductivityOfUnit(unit: KMolarConductivityUnit): KMolarConductivityUnitInstance =
    molarConductivityInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" molar conductivity, as long as it matches the canonical molar
 * conductivity normal form: exactly one [KMassUnit] term at exponent `-1`, one [KTimeUnit] term at exponent
 * `+3`, one [KElectricCurrentUnit] term at exponent `+2` and one [KAmountOfSubstanceUnit] term at exponent
 * `-1` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s and bridged from the gram-based product to the
 * kilogram-based siemens.
 *
 * @throws IllegalStateException if this instance is not a canonical
 * `mass⁻¹·time³·current²·substance⁻¹` molar conductivity.
 */
fun KMixedUnitInstance.toMolarConductivity(): KMolarConductivityUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == -1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 3 }
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == 2 }
    val substanceTerm = units.singleOrNull { it.unit is KAmountOfSubstanceUnit && it.exponent == -1 }
    check(
        units.size == 4 && massTerm != null && timeTerm != null &&
                currentTerm != null && substanceTerm != null
    ) {
        "KMixedUnitInstance $this does not represent a pure molar conductivity (expected one KMassUnit^-1, " +
                "one KTimeUnit^3, one KElectricCurrentUnit^2 and one KAmountOfSubstanceUnit^-1 term)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue.pow(-1.0) *
            timeTerm.unit.baseValue.pow(3.0) *
            currentTerm.unit.baseValue.pow(2.0) *
            substanceTerm.unit.baseValue.pow(-1.0)
    return molarConductivityInstanceOf(
        gramBaseProduct / MOLAR_CONDUCTIVITY_MASS_REFERENCE.pow(-1.0),
    )
}
