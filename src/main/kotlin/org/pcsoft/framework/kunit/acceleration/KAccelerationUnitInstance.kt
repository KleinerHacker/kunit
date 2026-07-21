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

package org.pcsoft.framework.kunit.acceleration

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.time.KTimeUnit

/**
 * Wraps a [KMixedUnitInstance] representing an acceleration, i.e. exactly two terms - one
 * [KDistanceUnit.BASE] (meter) at exponent `+1` and one [KTimeUnit.BASE] (second) at exponent `-2`
 * (`m·s⁻²`). The [value] is always normalized internally to meters per second squared
 * ([KAccelerationUnit.BASE]), regardless of which [KAccelerationUnit], SI
 * [org.pcsoft.framework.kunit.KUnitPrefix], or length/time combination it was constructed from.
 *
 * Acceleration is a *constructed* unit group (like speed and data rate): it holds a two-term instance
 * and is produced by the cross-group operators in `KAccelerationUnitOperators.kt` (e.g.
 * `speed / time`), the bare tokens in `KAccelerationUnitBareValues.kt` (e.g. `9.81 of gals`), the
 * prefixed templates in `KAccelerationUnitExtensions.kt` (e.g. `milli.gals`), or [toAcceleration].
 *
 * Example:
 * ```kotlin
 * val a = (10 of meters / seconds) / (2 of seconds) // KAccelerationUnitInstance, 5 m/s²
 * a.value                                            // 5.0 (normalized to m/s²)
 * a into standardGravities                           // ≈ 0.5098
 * ```
 */
class KAccelerationUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KAccelerationUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new acceleration value with [value] (m/s²) scaled by [factor]. Backs number-times-unit
     * construction (`9.81 of gals`).
     */
    override fun scaledBy(factor: Double): KAccelerationUnitInstance = accelerationUnitInstanceOf(value * factor)

    /**
     * Adds two accelerations, automatically converting between different [KAccelerationUnit]s since both
     * operands are always normalized to [KAccelerationUnit.BASE] (m/s²) internally.
     */
    override operator fun plus(other: KAccelerationUnitInstance): KAccelerationUnitInstance =
        KAccelerationUnitInstance(instance + other.instance)

    /** Subtracts two accelerations. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KAccelerationUnitInstance): KAccelerationUnitInstance =
        KAccelerationUnitInstance(instance - other.instance)

    /**
     * Multiplies two accelerations, producing a new [KMixedUnitInstance] (`m²·s⁻⁴`, no longer a "pure"
     * acceleration).
     */
    operator fun times(other: KAccelerationUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two accelerations, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KAccelerationUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two accelerations by their normalized [value] (meters per second squared). */
    override operator fun compareTo(other: KAccelerationUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized [value] (both operands are always normalized to m/s²). */
    override fun equals(other: Any?): Boolean = other is KAccelerationUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"9.80665 m/s²"`. */
    override fun toString(): String = "$value ${KAccelerationUnit.BASE.symbol}"
}

/**
 * Converts this mixed unit to a "pure" acceleration value, as long as it consists of exactly two terms:
 * one [KDistanceUnit] term at exponent `+1` and one [KTimeUnit] term at exponent `-2` (order
 * independent). The terms are normalized to [KDistanceUnit.BASE]/[KTimeUnit.BASE], so the resulting
 * acceleration is expressed in meters per second squared.
 *
 * @throws IllegalStateException if this instance is not a length-per-time² acceleration.
 */
fun KMixedUnitInstance.toAcceleration(): KAccelerationUnitInstance {
    val lengthTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -2 }
    check(units.size == 2 && lengthTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure acceleration (expected one KDistanceUnit^1 and one KTimeUnit^-2 term)"
    }
    val metersPerSecondSquared = value * lengthTerm.unit.baseValue * Math.pow(timeTerm.unit.baseValue, -2.0)
    return accelerationUnitInstanceOf(metersPerSecondSquared)
}

/** Builds a [KAccelerationUnitInstance] from a value already expressed in meters per second squared ([KAccelerationUnit.BASE]). */
internal fun accelerationUnitInstanceOf(metersPerSecondSquared: Double): KAccelerationUnitInstance =
    KAccelerationUnitInstance(KMixedUnitInstance(metersPerSecondSquared, listOf(KUnitTerm(KDistanceUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -2))))
