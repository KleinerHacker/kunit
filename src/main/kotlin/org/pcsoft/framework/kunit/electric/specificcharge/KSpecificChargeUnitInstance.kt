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

package org.pcsoft.framework.kunit.electric.specificcharge

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **specific charge** (charge per mass), i.e. exactly three
 * terms in the canonical normal form - [KElectricCurrentUnit.BASE] (ampere) at exponent `+1`,
 * [KTimeUnit.BASE] (second) at exponent `+1` and [KMassUnit.BASE] (gram) at exponent `-1`
 * (`A·s·kg⁻¹` = `C/kg`).
 *
 * Specific charge is a *constructed* unit group with one decomposition, funnelling into
 * [specificChargeInstanceOf]:
 * * `charge / mass` (typed operator, see `KSpecificChargeUnitOperators.kt`)
 *
 * The same dimension carries the **ionisation dose** (exposure) of radiation protection, which is why the
 * roentgen is one of the group's named units.
 *
 * Instances are additionally created via the bare tokens in `KSpecificChargeUnitBareValues.kt` (e.g.
 * `2.58 of milli.coulombsPerKilogram`), the prefixed templates in `KSpecificChargeUnitExtensions.kt`, or
 * [toSpecificCharge].
 *
 * Example:
 * ```kotlin
 * val ratio = (1 of coulombs) / (1 of kilo.grams) // 1 C/kg
 * ratio into coulombsPerKilogram
 * ```
 */
class KSpecificChargeUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KSpecificChargeUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new specific charge with [value] (C/kg) scaled by [factor]. Backs number-times-unit
     * construction (`2.58 of milli.coulombsPerKilogram`).
     */
    override fun scaledBy(factor: Double): KSpecificChargeUnitInstance =
        specificChargeInstanceOf(value * factor)

    /**
     * Adds two specific charges, automatically converting between different [KSpecificChargeUnit]s since
     * both operands are always normalized to [KSpecificChargeUnit.BASE] (C/kg) internally.
     */
    override operator fun plus(other: KSpecificChargeUnitInstance): KSpecificChargeUnitInstance =
        specificChargeInstanceOf(value + other.value)

    /** Subtracts two specific charges. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KSpecificChargeUnitInstance): KSpecificChargeUnitInstance =
        specificChargeInstanceOf(value - other.value)

    /** Multiplies two specific charges, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KSpecificChargeUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two specific charges, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KSpecificChargeUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two specific charges by their normalized [value] (C/kg). */
    override operator fun compareTo(other: KSpecificChargeUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two specific charges are equal iff they represent the same
     * quantity (e.g. `(1 of roentgens) == (2.58e-4 of coulombsPerKilogram)`).
     */
    override fun equals(other: Any?): Boolean =
        other is KSpecificChargeUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in C/kg, e.g. `"1.0 C/kg"`. */
    override fun toString(): String = "$value ${KSpecificChargeUnit.BASE.symbol}"
}

// The coulomb-per-kilogram refers to the *kilogram*, whereas the mass group's base unit is the gram. The
// canonical normal form is therefore stored with the mass group's base term (gram), and this factor bridges
// a gram-based canonical product to the named unit. The mass exponent is *negative* (-1) here, so the
// bridge is applied as `pow(reference, -1.0)` (like the siemens).
internal val COULOMB_PER_KILOGRAM_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KSpecificChargeUnitInstance] from a value already expressed in coulombs per kilogram
 * ([KSpecificChargeUnit.BASE]).
 *
 * This is the single creation source that every specific charge decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the three terms `current¹`, `time¹` and
 * `mass⁻¹` (each in its group's base unit).
 */
internal fun specificChargeInstanceOf(coulombsPerKilogram: Double): KSpecificChargeUnitInstance =
    KSpecificChargeUnitInstance(
        KMixedUnitInstance(
            coulombsPerKilogram,
            listOf(
                KUnitTerm(KElectricCurrentUnit.BASE, 1),
                KUnitTerm(KTimeUnit.BASE, 1),
                KUnitTerm(KMassUnit.BASE, -1),
            ),
        ),
    )

/**
 * Builds a value-1 [KSpecificChargeUnitInstance] for the given [unit] (its
 * [KSpecificChargeUnit.baseValue] C/kg).
 */
internal fun specificChargeOfUnit(unit: KSpecificChargeUnit): KSpecificChargeUnitInstance =
    specificChargeInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" specific charge, as long as it matches the canonical normal form:
 * exactly one [KElectricCurrentUnit] term at exponent `+1`, one [KTimeUnit] term at exponent `+1` and one
 * [KMassUnit] term at exponent `-1` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s and bridged from the gram-based product to the
 * kilogram-based named unit.
 *
 * @throws IllegalStateException if this instance is not a canonical `current·time·mass⁻¹` specific charge.
 */
fun KMixedUnitInstance.toSpecificCharge(): KSpecificChargeUnitInstance {
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 1 }
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == -1 }
    check(units.size == 3 && currentTerm != null && timeTerm != null && massTerm != null) {
        "KMixedUnitInstance $this does not represent a pure specific charge (expected one " +
                "KElectricCurrentUnit^1, one KTimeUnit^1 and one KMassUnit^-1 term)"
    }
    val gramBaseProduct = value *
            currentTerm.unit.baseValue *
            timeTerm.unit.baseValue *
            Math.pow(massTerm.unit.baseValue, -1.0)
    return specificChargeInstanceOf(
        gramBaseProduct / Math.pow(COULOMB_PER_KILOGRAM_MASS_REFERENCE, -1.0),
    )
}
