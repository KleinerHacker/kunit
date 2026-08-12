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

package org.pcsoft.framework.kunit.mechanic.angularvelocity

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.formatter.renderDouble
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnit
import org.pcsoft.framework.kunit.mechanic.angle.KAngleUnit

/**
 * Wraps a [KMixedUnitInstance] representing an **angular velocity**, i.e. exactly two terms - one
 * [KAngleUnit.BASE] (radian) at exponent `+1` and one [KTimeUnit.BASE] (second) at exponent `-1`
 * (`rad·s⁻¹`). The [value] is always normalized internally to radians per second
 * ([KAngularVelocityUnit.BASE]), regardless of which [KAngularVelocityUnit] or angle/time combination it
 * was constructed from.
 *
 * Angular velocity is a *constructed* unit group produced by the cross-group operators in
 * `KAngularVelocityUnitOperators.kt` (e.g. `angle / time`), the bare tokens in
 * `KAngularVelocityUnitBareValues.kt` (e.g. `3000 of revolutionsPerMinute`), or [toAngularVelocity].
 *
 * Example:
 * ```kotlin
 * val w = (1 of turns) / (1 of seconds)
 * w into revolutionsPerMinute // 60.0
 * ```
 */
class KAngularVelocityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KAngularVelocityUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new angular velocity with [value] (rad/s) scaled by [factor]. Backs number-times-unit
     * construction (`3000 of revolutionsPerMinute`).
     */
    override fun scaledBy(factor: Double): KAngularVelocityUnitInstance = angularVelocityInstanceOf(value * factor)

    /**
     * Adds two angular velocities, automatically converting between different [KAngularVelocityUnit]s
     * since both operands are always normalized to rad/s internally.
     */
    override operator fun plus(other: KAngularVelocityUnitInstance): KAngularVelocityUnitInstance =
        angularVelocityInstanceOf(value + other.value)

    /** Subtracts two angular velocities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KAngularVelocityUnitInstance): KAngularVelocityUnitInstance =
        angularVelocityInstanceOf(value - other.value)

    /** Multiplies two angular velocities, producing a new [KMixedUnitInstance] (`rad²·s⁻²`). */
    operator fun times(other: KAngularVelocityUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two angular velocities, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KAngularVelocityUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two angular velocities by their normalized [value] (rad/s). */
    override operator fun compareTo(other: KAngularVelocityUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized [value] (e.g. `(60 of revolutionsPerMinute) == (1 of revolutionsPerSecond)`). */
    override fun equals(other: Any?): Boolean = other is KAngularVelocityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"6.283185307179586 rad/s"`. */
    override fun toString(): String = "${renderDouble(value)} ${KAngularVelocityUnit.BASE.symbol}"
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KAngularVelocityUnitInstance] from a value already expressed in radians per second
 * ([KAngularVelocityUnit.BASE]). Single normalizing creation source of every decomposition.
 */
internal fun angularVelocityInstanceOf(radiansPerSecond: Double): KAngularVelocityUnitInstance =
    KAngularVelocityUnitInstance(
        KMixedUnitInstance(
            radiansPerSecond,
            listOf(KUnitTerm(KAngleUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -1)),
        ),
    )

/**
 * Builds a value-1 [KAngularVelocityUnitInstance] for the given [unit] (its
 * [KAngularVelocityUnit.baseValue] radians per second).
 */
internal fun angularVelocityOfUnit(unit: KAngularVelocityUnit): KAngularVelocityUnitInstance =
    angularVelocityInstanceOf(unit.baseValue)

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" angular velocity, as long as it matches the canonical normal form:
 * exactly one [KAngleUnit] term at exponent `+1` and one [KTimeUnit] term at exponent `-1` (order
 * independent). The terms are normalized over their base values, so the result is expressed in rad/s.
 *
 * @throws IllegalStateException if this instance is not an angle-per-time angular velocity.
 */
fun KMixedUnitInstance.toAngularVelocity(): KAngularVelocityUnitInstance {
    val angleTerm = units.singleOrNull { it.unit is KAngleUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -1 }
    check(units.size == 2 && angleTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure angular velocity (expected one KAngleUnit^1 and one KTimeUnit^-1 term)"
    }
    return angularVelocityInstanceOf(value * angleTerm.unit.baseValue / timeTerm.unit.baseValue)
}
