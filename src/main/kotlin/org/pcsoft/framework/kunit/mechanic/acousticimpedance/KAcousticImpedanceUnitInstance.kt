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

package org.pcsoft.framework.kunit.mechanic.acousticimpedance

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnit

/**
 * The fixed factor between the group's named base unit (pascal second per meter) and the raw component
 * storage (`g·m⁻²·s⁻¹`): 1 Pa·s/m = 1 kg/(m²·s) = 1000 g/(m²·s). It exists because the mass component of
 * this library is normalized to grams, not kilograms. The [KAcousticImpedanceUnitInstance.value] is always
 * the raw component value; readings in pascal seconds per meter divide by this factor.
 */
internal const val RAYL_IN_BASE: Double = 1000.0

/**
 * Wraps a [KMixedUnitInstance] representing a **specific acoustic impedance** (sound pressure per particle
 * velocity), i.e. exactly three terms - [KMassUnit.BASE] (gram) at exponent `+1`, [KDistanceUnit.BASE]
 * (meter) at exponent `-2` and [KTimeUnit.BASE] (second) at exponent `-1` (`g·m⁻²·s⁻¹`). The [value] is
 * the raw component value; readings in pascal seconds per meter ([KAcousticImpedanceUnit.BASE]) divide by
 * [RAYL_IN_BASE] - the same convention the neighbouring force, pressure and density groups use.
 *
 * Acoustic impedance is a *constructed* unit group with two decompositions, both funnelling into
 * [acousticImpedanceInstanceOf]:
 * * `pressure / speed` (typed operator, see `KAcousticImpedanceUnitOperators.kt`)
 * * `density * speed` (typed operator, same file) - the characteristic impedance `Z = ρ · c`
 *
 * Instances are additionally created via the bare tokens in `KAcousticImpedanceUnitBareValues.kt` (e.g.
 * `413 of rayls`), the prefixed templates in `KAcousticImpedanceUnitExtensions.kt`, or
 * [toAcousticImpedance].
 *
 * Example:
 * ```kotlin
 * val z = air * ((343 of meters) / (1 of seconds)) // ≈ 413 Pa·s/m
 * z into rayls
 * ```
 */
class KAcousticImpedanceUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KAcousticImpedanceUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new acoustic impedance with the component [value] scaled by [factor]. Backs
     * number-times-unit construction (`413 of rayls`).
     */
    override fun scaledBy(factor: Double): KAcousticImpedanceUnitInstance =
        acousticImpedanceInstanceOf(value * factor)

    /**
     * Adds two acoustic impedances, automatically converting between different [KAcousticImpedanceUnit]s
     * since both operands are always normalized to the same component base internally.
     */
    override operator fun plus(other: KAcousticImpedanceUnitInstance): KAcousticImpedanceUnitInstance =
        acousticImpedanceInstanceOf(value + other.value)

    /** Subtracts two acoustic impedances. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KAcousticImpedanceUnitInstance): KAcousticImpedanceUnitInstance =
        acousticImpedanceInstanceOf(value - other.value)

    /** Multiplies two acoustic impedances, producing a new [KMixedUnitInstance]. */
    operator fun times(other: KAcousticImpedanceUnitInstance): KMixedUnitInstance =
        instance * other.instance

    /** Divides two acoustic impedances, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KAcousticImpedanceUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two acoustic impedances by their normalized component [value]. */
    override operator fun compareTo(other: KAcousticImpedanceUnitInstance): Int =
        value.compareTo(other.value)

    /**
     * Structural equality by normalized component [value]: two acoustic impedances are equal iff they
     * represent the same quantity (e.g. `(1 of cgsRayls) == (10 of rayls)`).
     */
    override fun equals(other: Any?): Boolean =
        other is KAcousticImpedanceUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in Pa·s/m, e.g. `"413.0 Pa*s/m"`. */
    override fun toString(): String = "${value / RAYL_IN_BASE} ${KAcousticImpedanceUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KAcousticImpedanceUnitInstance] from a raw component value (`g·m⁻²·s⁻¹`). To build from a
 * pascal-second-per-meter reading, use [acousticImpedanceOfUnit] or the tokens in
 * `KAcousticImpedanceUnitBareValues.kt`.
 *
 * This is the single creation source that every decomposition must funnel into: it assembles the canonical
 * normal-form [KMixedUnitInstance] with the three terms `mass¹`, `distance⁻²` and `time⁻¹` (each in its
 * group's base unit).
 */
internal fun acousticImpedanceInstanceOf(componentValue: Double): KAcousticImpedanceUnitInstance =
    KAcousticImpedanceUnitInstance(
        KMixedUnitInstance(
            componentValue,
            listOf(
                KUnitTerm(KMassUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, -2),
                KUnitTerm(KTimeUnit.BASE, -1),
            ),
        ),
    )

/**
 * Builds a value-1 [KAcousticImpedanceUnitInstance] for the given [unit] (its
 * [KAcousticImpedanceUnit.baseValue] pascal seconds per meter).
 */
internal fun acousticImpedanceOfUnit(unit: KAcousticImpedanceUnit): KAcousticImpedanceUnitInstance =
    acousticImpedanceInstanceOf(unit.baseValue * RAYL_IN_BASE)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" acoustic impedance, as long as it matches the canonical normal
 * form: exactly one [KMassUnit] term at exponent `+1`, one [KDistanceUnit] term at exponent `-2` and one
 * [KTimeUnit] term at exponent `-1` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s.
 *
 * @throws IllegalStateException if this instance is not a canonical `mass·distance⁻²·time⁻¹` acoustic
 * impedance.
 */
fun KMixedUnitInstance.toAcousticImpedance(): KAcousticImpedanceUnitInstance {
    val massTerm = units.singleOrNull { it.unit is KMassUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == -2 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -1 }
    check(units.size == 3 && massTerm != null && distanceTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure acoustic impedance " +
                "(expected one KMassUnit^1, one KDistanceUnit^-2 and one KTimeUnit^-1 term)"
    }
    val component = value *
            massTerm.unit.baseValue *
            Math.pow(distanceTerm.unit.baseValue, -2.0) *
            Math.pow(timeTerm.unit.baseValue, -1.0)
    return acousticImpedanceInstanceOf(component)
}
