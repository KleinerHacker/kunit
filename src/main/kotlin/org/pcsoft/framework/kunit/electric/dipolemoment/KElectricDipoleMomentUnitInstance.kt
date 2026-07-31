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

package org.pcsoft.framework.kunit.electric.dipolemoment

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnit
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit

/**
 * Wraps a [KMixedUnitInstance] representing an **electric dipole moment**, i.e. exactly three terms in the
 * canonical normal form `current¹ · time¹ · distance¹` (`A·s·m` = `C·m`). The [value] is always normalized
 * internally to coulomb meters ([KElectricDipoleMomentUnit.BASE]), regardless of which
 * [KElectricDipoleMomentUnit], SI [KUnitPrefix], or charge/length combination it was constructed from.
 *
 * Electric dipole moment is a *constructed* unit group: unlike a single-term wrapper it holds a three-term
 * instance. Instances are created via the bare tokens in `KElectricDipoleMomentUnitBareValues.kt` (e.g.
 * `1.85 of debyes`), the prefixed templates in `KElectricDipoleMomentUnitExtensions.kt`, the typed operator
 * (`charge * length`), or [toElectricDipoleMoment] on a canonical `current·time·distance` expression.
 *
 * Example:
 * ```kotlin
 * val p = 1.85 of debyes // the water molecule
 * p.value                // 6.170935... e-30 (normalized to coulomb meters)
 * ```
 */
class KElectricDipoleMomentUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KElectricDipoleMomentUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new electric dipole moment with [value] (coulomb meters) scaled by [factor]. Backs
     * number-times-unit construction (`1.85 of debyes`).
     */
    override fun scaledBy(factor: Double): KElectricDipoleMomentUnitInstance =
        electricDipoleMomentInstanceOf(value * factor)

    /**
     * Adds two electric dipole moments, automatically converting between different
     * [KElectricDipoleMomentUnit]s since both operands are always normalized to
     * [KElectricDipoleMomentUnit.BASE] (coulomb meters) internally.
     *
     * Example:
     * ```kotlin
     * ((1 of coulombMeters) + (1 of debyes)).value // 1.000000000000000000000000000003...
     * ```
     */
    override operator fun plus(other: KElectricDipoleMomentUnitInstance): KElectricDipoleMomentUnitInstance =
        electricDipoleMomentInstanceOf(value + other.value)

    /** Subtracts two electric dipole moments. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KElectricDipoleMomentUnitInstance): KElectricDipoleMomentUnitInstance =
        electricDipoleMomentInstanceOf(value - other.value)

    /**
     * Multiplies two electric dipole moments, producing a new [KMixedUnitInstance] (no longer a "pure"
     * electric dipole moment).
     */
    operator fun times(other: KElectricDipoleMomentUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two electric dipole moments, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KElectricDipoleMomentUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two electric dipole moments by their normalized [value] (coulomb meters). */
    override operator fun compareTo(other: KElectricDipoleMomentUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two electric dipole moments are equal iff they represent the
     * same physical moment, independent of the unit they were constructed with.
     */
    override fun equals(other: Any?): Boolean =
        other is KElectricDipoleMomentUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"6.170935761200001E-30 C·m"`. */
    override fun toString(): String = "$value ${KElectricDipoleMomentUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KElectricDipoleMomentUnitInstance] from a value already expressed in coulomb meters
 * ([KElectricDipoleMomentUnit.BASE]).
 *
 * This is the single creation source that every electric dipole moment decomposition must funnel into: it
 * assembles the canonical normal-form [KMixedUnitInstance] with the three terms `current¹`, `time¹`,
 * `distance¹` (each in its group's base unit). The group has no mass dimension, so no gram/kilogram bridge is
 * required.
 */
internal fun electricDipoleMomentInstanceOf(coulombMeters: Double): KElectricDipoleMomentUnitInstance =
    KElectricDipoleMomentUnitInstance(
        KMixedUnitInstance(
            coulombMeters,
            listOf(
                KUnitTerm(KElectricCurrentUnit.BASE, 1),
                KUnitTerm(KTimeUnit.BASE, 1),
                KUnitTerm(KDistanceUnit.BASE, 1),
            ),
        ),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" electric dipole moment, as long as it matches the canonical normal
 * form: exactly one [KElectricCurrentUnit] term at exponent `+1`, one [KTimeUnit] term at exponent `+1` and
 * one [KDistanceUnit] term at exponent `+1` (order independent). The terms are normalized over their
 * [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the resulting moment is expressed in coulomb meters
 * regardless of which concrete current/time/distance units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `current¹·time¹·distance¹` electric
 * dipole moment.
 *
 * Example:
 * ```kotlin
 * val raw = 2 of (amperes * seconds * meters)
 * raw.toElectricDipoleMoment().value // 2.0
 * ```
 */
fun KMixedUnitInstance.toElectricDipoleMoment(): KElectricDipoleMomentUnitInstance {
    val currentTerm = units.singleOrNull { it.unit is KElectricCurrentUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == 1 }
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 1 }
    check(units.size == 3 && currentTerm != null && timeTerm != null && distanceTerm != null) {
        "KMixedUnitInstance $this does not represent a pure electric dipole moment (expected KElectricCurrentUnit^1, KTimeUnit^1 and KDistanceUnit^1)"
    }
    return electricDipoleMomentInstanceOf(
        value * currentTerm.unit.baseValue * timeTerm.unit.baseValue * distanceTerm.unit.baseValue,
    )
}
