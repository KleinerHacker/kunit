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

package org.pcsoft.framework.kunit.mechanic.angularacceleration

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.angle.KAngleUnit

/**
 * Wraps a [KMixedUnitInstance] representing an **angular acceleration**, i.e. exactly two terms - one
 * [KAngleUnit.BASE] (radian) at exponent `+1` and one [KTimeUnit.BASE] (second) at exponent `-2`
 * (`rad·s⁻²`). The [value] is always normalized internally to radians per second squared
 * ([KAngularAccelerationUnit.BASE]).
 *
 * Angular acceleration is a *constructed* unit group produced by the cross-group operators in
 * `KAngularAccelerationUnitOperators.kt` (e.g. `angularvelocity / time`), the bare tokens in
 * `KAngularAccelerationUnitBareValues.kt`, or [toAngularAcceleration] (which also recognizes the fully
 * native form `angle / time²`).
 *
 * Example:
 * ```kotlin
 * val alpha = ((1 of revolutionsPerSecond) / (2 of seconds))
 * alpha into radiansPerSecondSquared // ≈ 3.1416
 * ```
 */
class KAngularAccelerationUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KAngularAccelerationUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new angular acceleration with [value] (rad/s²) scaled by [factor]. Backs
     * number-times-unit construction (`5 of radiansPerSecondSquared`).
     */
    override fun scaledBy(factor: Double): KAngularAccelerationUnitInstance =
        angularAccelerationInstanceOf(value * factor)

    /**
     * Adds two angular accelerations, automatically converting between different
     * [KAngularAccelerationUnit]s since both operands are always normalized to rad/s² internally.
     */
    override operator fun plus(other: KAngularAccelerationUnitInstance): KAngularAccelerationUnitInstance =
        angularAccelerationInstanceOf(value + other.value)

    /** Subtracts two angular accelerations. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KAngularAccelerationUnitInstance): KAngularAccelerationUnitInstance =
        angularAccelerationInstanceOf(value - other.value)

    /** Multiplies two angular accelerations, producing a new [KMixedUnitInstance] (`rad²·s⁻⁴`). */
    operator fun times(other: KAngularAccelerationUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two angular accelerations, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KAngularAccelerationUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two angular accelerations by their normalized [value] (rad/s²). */
    override operator fun compareTo(other: KAngularAccelerationUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized [value] (rad/s²). */
    override fun equals(other: Any?): Boolean = other is KAngularAccelerationUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"3.141592653589793 rad/s^2"`. */
    override fun toString(): String = "$value ${KAngularAccelerationUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KAngularAccelerationUnitInstance] from a value already expressed in radians per second
 * squared ([KAngularAccelerationUnit.BASE]). Single normalizing creation source of every decomposition.
 */
internal fun angularAccelerationInstanceOf(radiansPerSecondSquared: Double): KAngularAccelerationUnitInstance =
    KAngularAccelerationUnitInstance(
        KMixedUnitInstance(
            radiansPerSecondSquared,
            listOf(KUnitTerm(KAngleUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -2)),
        ),
    )

/**
 * Builds a value-1 [KAngularAccelerationUnitInstance] for the given [unit] (its
 * [KAngularAccelerationUnit.baseValue] radians per second squared).
 */
internal fun angularAccelerationOfUnit(unit: KAngularAccelerationUnit): KAngularAccelerationUnitInstance =
    angularAccelerationInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" angular acceleration, as long as it matches the canonical normal
 * form: exactly one [KAngleUnit] term at exponent `+1` and one [KTimeUnit] term at exponent `-2` (order
 * independent). This is also the native decomposition `angle / time²`.
 *
 * @throws IllegalStateException if this instance is not an angle-per-time-squared angular acceleration.
 */
fun KMixedUnitInstance.toAngularAcceleration(): KAngularAccelerationUnitInstance {
    val angleTerm = units.singleOrNull { it.unit is KAngleUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -2 }
    check(units.size == 2 && angleTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure angular acceleration (expected one KAngleUnit^1 and one KTimeUnit^-2 term)"
    }
    return angularAccelerationInstanceOf(
        value * angleTerm.unit.baseValue / (timeTerm.unit.baseValue * timeTerm.unit.baseValue),
    )
}
