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

package org.pcsoft.framework.kunit.electric.elastance

import kotlin.math.pow
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnit
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * Wraps a [KMixedUnitInstance] representing an **elastance** (voltage per charge), i.e. exactly four terms
 * in the canonical normal form - [KMassUnit.BASE] (gram) at exponent `+1`, [KDistanceUnit.BASE] (meter) at
 * exponent `+2`, [KTimeUnit.BASE] (second) at exponent `-4` and [KElectricCurrentUnit.BASE] (ampere) at
 * exponent `-2` (`kg·m²·s⁻⁴·A⁻²` = `F⁻¹`).
 *
 * Elastance is a *constructed* unit group with one decomposition, funnelling into [elastanceInstanceOf]:
 * * `voltage / charge` (typed operator, see `KElastanceUnitOperators.kt`)
 *
 * It is the exact reciprocal of the
 * [capacitance][org.pcsoft.framework.kunit.electric.capacitance.KCapacitanceUnitInstance], which is why
 * `1 / capacitance` yields an elastance and `1 / elastance` a capacitance. Capacitors in **series** add
 * their elastances - which is exactly what the same-type `+` does.
 *
 * Instances are additionally created via the bare tokens in `KElastanceUnitBareValues.kt` (e.g.
 * `1000 of reciprocalFarads`), the prefixed templates in `KElastanceUnitExtensions.kt`, or [toElastance].
 *
 * Example:
 * ```kotlin
 * val s = 1 / (1 of milli.farads) // 1000 F⁻¹
 * s into reciprocalFarads
 * ```
 */
class KElastanceUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KElastanceUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new elastance with [value] (F⁻¹) scaled by [factor]. Backs number-times-unit construction
     * (`1000 of reciprocalFarads`).
     */
    override fun scaledBy(factor: Double): KElastanceUnitInstance = elastanceInstanceOf(value * factor)

    /**
     * Adds two elastances - the series connection of two capacitors. Automatically converts between
     * different [KElastanceUnit]s since both operands are always normalized to [KElastanceUnit.BASE]
     * internally.
     */
    override operator fun plus(other: KElastanceUnitInstance): KElastanceUnitInstance =
        elastanceInstanceOf(value + other.value)

    /** Subtracts two elastances. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KElastanceUnitInstance): KElastanceUnitInstance =
        elastanceInstanceOf(value - other.value)

    /** Multiplies two elastances, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KElastanceUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two elastances, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KElastanceUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two elastances by their normalized [value] (F⁻¹). */
    override operator fun compareTo(other: KElastanceUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two elastances are equal iff they represent the same
     * quantity (e.g. `(1 of reciprocalFarads) == (1 of darafs)`).
     */
    override fun equals(other: Any?): Boolean = other is KElastanceUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in F⁻¹, e.g. `"1000.0 1/F"`. */
    override fun toString(): String = "${renderDouble(value)} ${KElastanceUnit.BASE.symbol}"
}

// The farad's SI definition uses the *kilogram* as its mass dimension, whereas the mass group's base unit
// is the gram. The canonical normal form is therefore stored with the mass group's base term (gram), and
// this factor bridges a gram-based canonical product to reciprocal farads. The mass exponent is *positive*
// (+1) here, so the bridge divides.
private val RECIPROCAL_FARAD_MASS_REFERENCE: Double = KUnitPrefix.KILO.factor

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KElastanceUnitInstance] from a value already expressed in reciprocal farads
 * ([KElastanceUnit.BASE]).
 *
 * This is the single creation source that every elastance decomposition must funnel into: it assembles the
 * canonical normal-form [KMixedUnitInstance] with the four terms `mass¹`, `distance²`, `time⁻⁴` and
 * `current⁻²` (each in its group's base unit).
 */
internal fun elastanceInstanceOf(reciprocalFarads: Double): KElastanceUnitInstance =
    KElastanceUnitInstance(
        KMixedUnitInstance(
            reciprocalFarads,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 2),
                KUnitTerm(KTimeUnit.BASE, -4),
                KUnitTerm(KElectricCurrentUnit.BASE, -2),
            ),
        ),
    )

/** Builds a value-1 [KElastanceUnitInstance] for the given [unit] (its [KElastanceUnit.baseValue] F⁻¹). */
internal fun elastanceOfUnit(unit: KElastanceUnit): KElastanceUnitInstance =
    elastanceInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" elastance, as long as it matches the canonical normal form: exactly
 * one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent `+2`, one [KTimeUnit] term
 * at exponent `-4` and one [KElectricCurrentUnit] term at exponent `-2` (order independent). The terms are
 * normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s and bridged from the gram-based
 * product to the kilogram-based farad.
 *
 * @throws IllegalStateException if this instance is not a canonical
 * `mass·distance²·time⁻⁴·current⁻²` elastance.
 */
fun KMixedUnitInstance.toElastance(): KElastanceUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -4 }
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == -2 }
    check(
        units.size == 4 && massTerm != null && distanceTerm != null &&
                timeTerm != null && currentTerm != null
    ) {
        "KMixedUnitInstance $this does not represent a pure elastance (expected one KMassUnit^1, " +
                "one KDistanceUnit^2, one KTimeUnit^-4 and one KElectricCurrentUnit^-2 term)"
    }
    val gramBaseProduct = value *
            massTerm.unit.baseValue *
            distanceTerm.unit.baseValue.pow(2.0) *
            timeTerm.unit.baseValue.pow(-4.0) *
            currentTerm.unit.baseValue.pow(-2.0)
    return elastanceInstanceOf(gramBaseProduct / RECIPROCAL_FARAD_MASS_REFERENCE)
}
