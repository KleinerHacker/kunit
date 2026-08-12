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

package org.pcsoft.framework.kunit.mechanic.specificweight

import kotlin.math.pow
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * The fixed factor between the group's named base unit (newton per cubic meter) and the raw component
 * storage (`g·m⁻²·s⁻²`): 1 N/m³ = 1 kg/(m²·s²) = 1000 g/(m²·s²). It exists because the mass component of
 * this library is normalized to grams, not kilograms. The [KSpecificWeightUnitInstance.value] is always
 * the raw component value; readings in newtons per cubic meter divide by this factor.
 */
internal const val N_PER_M3_IN_BASE: Double = 1000.0

/**
 * Wraps a [KMixedUnitInstance] representing a **specific weight** (weight force per volume), i.e. exactly
 * three terms - [KMassUnit.BASE] (gram) at exponent `+1`, [KDistanceUnit.BASE] (meter) at exponent `-2`
 * and [KTimeUnit.BASE] (second) at exponent `-2` (`g·m⁻²·s⁻²`). The [value] is the raw component value;
 * readings in newtons per cubic meter ([KSpecificWeightUnit.BASE]) divide by [N_PER_M3_IN_BASE] - the same
 * convention the neighbouring force, pressure and density groups use.
 *
 * Specific weight is a *constructed* unit group with two decompositions, both funnelling into
 * [specificWeightInstanceOf]:
 * * `force / volume` (typed operator, see `KSpecificWeightUnitOperators.kt`)
 * * `density * acceleration` (typed operator, same file)
 *
 * Instances are additionally created via the bare tokens in `KSpecificWeightUnitBareValues.kt` (e.g.
 * `9807 of newtonsPerCubicMeter`), the prefixed templates in `KSpecificWeightUnitExtensions.kt`, or
 * [toSpecificWeight].
 *
 * Example:
 * ```kotlin
 * val gamma = (9807 of newtons) / ((1 of meters) * (1 of meters) * (1 of meters)) // water
 * gamma into kilonewtonsPerCubicMeter // ≈ 9.807
 * ```
 */
class KSpecificWeightUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KSpecificWeightUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new specific weight with the component [value] scaled by [factor]. Backs number-times-unit
     * construction (`9807 of newtonsPerCubicMeter`).
     */
    override fun scaledBy(factor: Double): KSpecificWeightUnitInstance =
        specificWeightInstanceOf(value * factor)

    /**
     * Adds two specific weights, automatically converting between different [KSpecificWeightUnit]s since
     * both operands are always normalized to the same component base internally.
     */
    override operator fun plus(other: KSpecificWeightUnitInstance): KSpecificWeightUnitInstance =
        specificWeightInstanceOf(value + other.value)

    /** Subtracts two specific weights. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KSpecificWeightUnitInstance): KSpecificWeightUnitInstance =
        specificWeightInstanceOf(value - other.value)

    /** Multiplies two specific weights, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KSpecificWeightUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two specific weights, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KSpecificWeightUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two specific weights by their normalized component [value]. */
    override operator fun compareTo(other: KSpecificWeightUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized component [value]: two specific weights are equal iff they
     * represent the same quantity (e.g.
     * `(1 of kilonewtonsPerCubicMeter) == (1000 of newtonsPerCubicMeter)`).
     */
    override fun equals(other: Any?): Boolean =
        other is KSpecificWeightUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in N/m³, e.g. `"9807.0 N/m^3"`. */
    override fun toString(): String = "${renderDouble(value / N_PER_M3_IN_BASE)} ${KSpecificWeightUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KSpecificWeightUnitInstance] from a raw component value (`g·m⁻²·s⁻²`). To build from a
 * newton-per-cubic-meter reading, use [specificWeightOfUnit] or the tokens in
 * `KSpecificWeightUnitBareValues.kt`.
 *
 * This is the single creation source that every specific weight decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the three terms `mass¹`, `distance⁻²` and
 * `time⁻²` (each in its group's base unit).
 */
internal fun specificWeightInstanceOf(componentValue: Double): KSpecificWeightUnitInstance =
    KSpecificWeightUnitInstance(
        KMixedUnitInstance(
            componentValue,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, -2),
                KUnitTerm(KTimeUnit.BASE, -2),
            ),
        ),
    )

/**
 * Builds a value-1 [KSpecificWeightUnitInstance] for the given [unit] (its
 * [KSpecificWeightUnit.baseValue] newtons per cubic meter).
 */
internal fun specificWeightOfUnit(unit: KSpecificWeightUnit): KSpecificWeightUnitInstance =
    specificWeightInstanceOf(unit.baseValue * N_PER_M3_IN_BASE)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" specific weight, as long as it matches the canonical specific
 * weight normal form: exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent
 * `-2` and one [KTimeUnit] term at exponent `-2` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass·distance⁻²·time⁻²` specific
 * weight.
 */
fun KMixedUnitInstance.toSpecificWeight(): KSpecificWeightUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -2 }
    check(units.size == 3 && massTerm != null && distanceTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure specific weight " +
                "(expected one KMassUnit^1, one KDistanceUnit^-2 and one KTimeUnit^-2 term)"
    }
    val component = value *
            massTerm.unit.baseValue *
            distanceTerm.unit.baseValue.pow(-2.0) *
            timeTerm.unit.baseValue.pow(-2.0)
    return specificWeightInstanceOf(component)
}
