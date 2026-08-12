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

package org.pcsoft.framework.kunit.thermo.molality

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **molality** (amount of substance per mass of solvent), i.e.
 * exactly two terms in the canonical normal form - [KAmountOfSubstanceUnit.BASE] (mole) at exponent `+1`
 * and [KMassUnit.BASE] (gram) at exponent `-1` (`mol·kg⁻¹`).
 *
 * Molality is a *constructed* unit group with one decomposition, funnelling into [molalityInstanceOf]:
 * * `amountOfSubstance / mass` (typed operator, see `KMolalityUnitOperators.kt`)
 *
 * It is the **reciprocal of the molar mass** up to the gram/kilogram bridge, which is why
 * `1 / molarMass` yields a molality and `1 / molality` a molar mass.
 *
 * Instances are additionally created via the bare tokens in `KMolalityUnitBareValues.kt` (e.g.
 * `0.5 of molesPerKilogram`), the prefixed templates in `KMolalityUnitExtensions.kt`, or [toMolality].
 *
 * Example:
 * ```kotlin
 * val b = (0.5 of moles) / (2 of kilo.grams) // 0.25 mol/kg
 * b into molesPerKilogram
 * ```
 */
class KMolalityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KMolalityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new molality with [value] (mol/kg) scaled by [factor]. Backs number-times-unit construction
     * (`0.5 of molesPerKilogram`).
     */
    override fun scaledBy(factor: Double): KMolalityUnitInstance = molalityInstanceOf(value * factor)

    /**
     * Adds two molalities, automatically converting between different [KMolalityUnit]s since both operands
     * are always normalized to [KMolalityUnit.BASE] (mol/kg) internally.
     */
    override operator fun plus(other: KMolalityUnitInstance): KMolalityUnitInstance =
        molalityInstanceOf(value + other.value)

    /** Subtracts two molalities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KMolalityUnitInstance): KMolalityUnitInstance =
        molalityInstanceOf(value - other.value)

    /** Multiplies two molalities, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KMolalityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two molalities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KMolalityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two molalities by their normalized [value] (mol/kg). */
    override operator fun compareTo(other: KMolalityUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two molalities are equal iff they represent the same
     * quantity (e.g. `(1 of molesPerKilogram) == (1000 of millimolesPerKilogram)`).
     */
    override fun equals(other: Any?): Boolean = other is KMolalityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in mol/kg, e.g. `"0.25 mol/kg"`. */
    override fun toString(): String = "$value ${KMolalityUnit.BASE.symbol}"
}

// The molality's named base unit refers to the *kilogram*, whereas the mass group's base unit is the gram.
// The canonical normal form is therefore stored with the mass group's base term (gram), and this factor
// bridges a gram-based canonical product to moles per kilogram. The mass exponent is *negative* (-1) here,
// so the bridge is applied as `pow(reference, -1.0)` (like the siemens).
internal val MOLALITY_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KMolalityUnitInstance] from a value already expressed in moles per kilogram
 * ([KMolalityUnit.BASE]).
 *
 * This is the single creation source that every molality decomposition must funnel into: it assembles the
 * canonical normal-form [KMixedUnitInstance] with the two terms `substance¹` and `mass⁻¹` (each in its
 * group's base unit).
 */
internal fun molalityInstanceOf(molesPerKilogram: Double): KMolalityUnitInstance =
    KMolalityUnitInstance(
        KMixedUnitInstance(
            molesPerKilogram,
            listOf(
                KUnitTerm(KAmountOfSubstanceUnit.BASE, 1),
                KUnitTerm(KMassUnit.BASE, -1),
            ),
        ),
    )

/** Builds a value-1 [KMolalityUnitInstance] for the given [unit] (its [KMolalityUnit.baseValue] mol/kg). */
internal fun molalityOfUnit(unit: KMolalityUnit): KMolalityUnitInstance = molalityInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" molality, as long as it matches the canonical molality normal form:
 * exactly one [KAmountOfSubstanceUnit] term at exponent `+1` and one [KMassUnit] term at exponent `-1`
 * (order independent). The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s
 * and bridged from the gram-based product to the kilogram-based named unit.
 *
 * @throws IllegalStateException if this instance is not a canonical `substance·mass⁻¹` molality.
 */
fun KMixedUnitInstance.toMolality(): KMolalityUnitInstance {
    val substanceTerm = units.singleOrNull { it.unit is KAmountOfSubstanceUnit && it.exponent == 1 }
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == -1 }
    check(units.size == 2 && substanceTerm != null && massTerm != null) {
        "KMixedUnitInstance $this does not represent a pure molality " +
                "(expected one KAmountOfSubstanceUnit^1 and one KMassUnit^-1 term)"
    }
    val gramBaseProduct = value *
            substanceTerm.unit.baseValue *
            Math.pow(massTerm.unit.baseValue, -1.0)
    return molalityInstanceOf(gramBaseProduct / Math.pow(MOLALITY_MASS_REFERENCE, -1.0))
}
