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

package org.pcsoft.framework.kunit.kinematic.jerk

import kotlin.math.pow
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.distance.KDistanceUnit
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit

/**
 * Wraps a [KMixedUnitInstance] representing a **jerk** (the rate at which an acceleration changes), i.e.
 * exactly two terms in the canonical normal form - [KDistanceUnit.BASE] (meter) at exponent `+1` and
 * [KTimeUnit.BASE] (second) at exponent `-3` (`m·s⁻³`). Both components are stored in their group's base
 * unit, so the raw component base *is* the named base unit ([KJerkUnit.METER_PER_SECOND_CUBED]) and no
 * bridging factor is needed.
 *
 * Jerk is what ride-comfort standards limit: a lift or a train may accelerate hard, but the acceleration
 * must not change abruptly. Typical comfort limits are around 0.5 m/s³.
 *
 * Jerk is a *constructed* unit group with one decomposition, funnelling into [jerkInstanceOf]:
 * * `acceleration / time` (typed operator, see `KJerkUnitOperators.kt`)
 *
 * Instances are additionally created via the bare tokens in `KJerkUnitBareValues.kt` (e.g.
 * `0.5 of metersPerSecondCubed`), the prefixed templates in `KJerkUnitExtensions.kt`, or [toJerk].
 *
 * Example:
 * ```kotlin
 * val j = (((1.2 of meters) / (1 of seconds)) / (1 of seconds)) / (2 of seconds) // 0.6 m/s³
 * j into metersPerSecondCubed
 * ```
 */
class KJerkUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KJerkUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new jerk with [value] (m/s³) scaled by [factor]. Backs number-times-unit construction
     * (`0.5 of metersPerSecondCubed`).
     */
    override fun scaledBy(factor: Double): KJerkUnitInstance = jerkInstanceOf(value * factor)

    /**
     * Adds two jerks, automatically converting between different [KJerkUnit]s since both operands are
     * always normalized to [KJerkUnit.BASE] (m/s³) internally.
     */
    override operator fun plus(other: KJerkUnitInstance): KJerkUnitInstance =
        jerkInstanceOf(value + other.value)

    /** Subtracts two jerks. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KJerkUnitInstance): KJerkUnitInstance =
        jerkInstanceOf(value - other.value)

    /** Multiplies two jerks, producing a new [KMixedUnitInstance] (no longer a "pure" one). */
    operator fun times(other: KJerkUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two jerks, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KJerkUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two jerks by their normalized [value] (m/s³). */
    override operator fun compareTo(other: KJerkUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two jerks are equal iff they represent the same quantity
     * (e.g. `(1 of metersPerSecondCubed) == (1000 of milli.metersPerSecondCubed)`).
     */
    override fun equals(other: Any?): Boolean = other is KJerkUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation in m/s³, e.g. `"0.6 m/s^3"`. */
    override fun toString(): String = "${renderDouble(value)} ${KJerkUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KJerkUnitInstance] from a value already expressed in meters per second cubed
 * ([KJerkUnit.BASE]).
 *
 * This is the single creation source that every jerk decomposition must funnel into: it assembles the
 * canonical normal-form [KMixedUnitInstance] with the two terms `distance¹` and `time⁻³` (each in its
 * group's base unit).
 */
internal fun jerkInstanceOf(metersPerSecondCubed: Double): KJerkUnitInstance =
    KJerkUnitInstance(
        KMixedUnitInstance(
            metersPerSecondCubed,
            listOf(
                KUnitTerm(KDistanceUnit.BASE, 1),
                KUnitTerm(KTimeUnit.BASE, -3),
            ),
        ),
    )

/** Builds a value-1 [KJerkUnitInstance] for the given [unit] (its [KJerkUnit.baseValue] m/s³). */
internal fun jerkOfUnit(unit: KJerkUnit): KJerkUnitInstance = jerkInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" jerk, as long as it matches the canonical jerk normal form: exactly
 * one [KDistanceUnit] term at exponent `+1` and one [KTimeUnit] term at exponent `-3` (order independent).
 * The terms are normalized over their [org.pcsoft.framework.kunit.KUnit.baseValue]s, so the result is
 * expressed in m/s³ regardless of which concrete units the terms were tagged with.
 *
 * @throws IllegalStateException if this instance is not a canonical `distance·time⁻³` jerk.
 */
fun KMixedUnitInstance.toJerk(): KJerkUnitInstance {
    val distanceTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -3 }
    check(units.size == 2 && distanceTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure jerk " +
                "(expected one KDistanceUnit^1 and one KTimeUnit^-3 term)"
    }
    return jerkInstanceOf(
        value * distanceTerm.unit.baseValue / timeTerm.unit.baseValue.pow(3.0),
    )
}
