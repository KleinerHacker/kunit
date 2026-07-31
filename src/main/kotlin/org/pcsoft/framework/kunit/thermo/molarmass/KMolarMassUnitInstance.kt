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

package org.pcsoft.framework.kunit.thermo.molarmass

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **molar mass** (mass per amount of substance), i.e. exactly
 * two terms in the canonical normal form - [KMassUnit.BASE] (gram) at exponent `+1` and
 * [KAmountOfSubstanceUnit.BASE] (mole) at exponent `-1` (`g·mol⁻¹`). Since the mass component of this
 * library is normalized to grams, the raw component base *is* the named base unit
 * ([KMolarMassUnit.GRAM_PER_MOLE]), so no bridging factor is needed.
 *
 * Molar mass is a *constructed* unit group. Instances are created via the bare tokens in
 * `KMolarMassUnitBareValues.kt` (e.g. `55.845 of gramsPerMole`), the prefixed templates in
 * `KMolarMassUnitExtensions.kt` (e.g. `2 of kilo.gramsPerMole`), the typed `mass / amountOfSubstance`
 * operator in `KMolarMassUnitOperators.kt`, or [toMolarMass].
 *
 * Example:
 * ```kotlin
 * val m = (18.015 of grams) / (1 of moles) // molar mass of water
 * m into gramsPerMole                       // 18.015
 * ```
 */
class KMolarMassUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KMolarMassUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new molar mass with [value] (g/mol) scaled by [factor]. Backs number-times-unit
     * construction (`55.845 of gramsPerMole`).
     */
    override fun scaledBy(factor: Double): KMolarMassUnitInstance = molarMassInstanceOf(value * factor)

    /**
     * Adds two molar masses, automatically converting between different [KMolarMassUnit]s since both
     * operands are always normalized to [KMolarMassUnit.BASE] (g/mol) internally.
     */
    override operator fun plus(other: KMolarMassUnitInstance): KMolarMassUnitInstance =
        molarMassInstanceOf(value + other.value)

    /** Subtracts two molar masses. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KMolarMassUnitInstance): KMolarMassUnitInstance =
        molarMassInstanceOf(value - other.value)

    /** Multiplies two molar masses, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KMolarMassUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two molar masses, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KMolarMassUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two molar masses by their normalized [value] (g/mol). */
    override operator fun compareTo(other: KMolarMassUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two molar masses are equal iff they represent the same
     * quantity (e.g. `(1 of kilo.gramsPerMole) == (1000 of gramsPerMole)`).
     */
    override fun equals(other: Any?): Boolean = other is KMolarMassUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in g/mol, e.g. `"18.015 g/mol"`. */
    override fun toString(): String = "$value ${KMolarMassUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KMolarMassUnitInstance] from a value already expressed in grams per mole
 * ([KMolarMassUnit.BASE]).
 *
 * This is the single creation source that every molar mass decomposition must funnel into: it assembles
 * the canonical normal-form [KMixedUnitInstance] with the two terms `mass¹` and `substance⁻¹` (each in its
 * group's base unit).
 */
internal fun molarMassInstanceOf(gramsPerMole: Double): KMolarMassUnitInstance =
    KMolarMassUnitInstance(
        KMixedUnitInstance(
            gramsPerMole,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KAmountOfSubstanceUnit.BASE, -1),
            ),
        ),
    )

/** Builds a value-1 [KMolarMassUnitInstance] for the given [unit] (its [KMolarMassUnit.baseValue] g/mol). */
internal fun molarMassOfUnit(unit: KMolarMassUnit): KMolarMassUnitInstance = molarMassInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" molar mass, as long as it matches the canonical molar mass normal
 * form: exactly one [KMassUnit] term at exponent `+1` and one [KAmountOfSubstanceUnit] term at exponent
 * `-1` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the result is expressed in g/mol regardless of which
 * concrete units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass¹·substance⁻¹` molar mass.
 */
fun KMixedUnitInstance.toMolarMass(): KMolarMassUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val substanceTerm = units.singleOrNull { it.unit is KAmountOfSubstanceUnit && it.exponent == -1 }
    check(units.size == 2 && massTerm != null && substanceTerm != null) {
        "KMixedUnitInstance $this does not represent a pure molar mass " +
                "(expected one KMassUnit^1 and one KAmountOfSubstanceUnit^-1 term)"
    }
    return molarMassInstanceOf(value * massTerm.unit.baseValue / substanceTerm.unit.baseValue)
}
