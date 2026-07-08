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

package org.pcsoft.framework.kunit.speed

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.time.KTimeUnit

/**
 * Wraps a [KMixedUnitInstance] representing a speed, i.e. exactly two terms - one [KDistanceUnit.BASE]
 * (meter) at exponent `+1` and one [KTimeUnit.BASE] (second) at exponent `-1` (`m·s⁻¹`). The [value]
 * is always normalized internally to meters per second ([KSpeedUnit.BASE]), regardless of which
 * [KSpeedUnit], SI [org.pcsoft.framework.kunit.KUnitPrefix], or length/time combination it was
 * constructed from.
 *
 * Speed is the first *constructed* unit group: unlike `KLengthUnitInstance`/`KTimeUnitInstance` (a
 * single-term wrapper), it holds a two-term instance and can therefore be read back either as a whole
 * speed unit (a single [KSpeedUnit], e.g. km/h - see [valueAs]) or as a length-per-time pair (two
 * targets, e.g. `km` + `h` - see the `vararg` [valueAs]).
 *
 * Instances are created via the creator extension properties in `KSpeedUnitExtensions.kt` (e.g.
 * `100.metersPerSecond`, `50.kilometersPerHour`), the SI-prefix `infix` constructors in
 * `KSpeedUnitPrefix.kt` (e.g. `5 kilo metersPerSecond`), the cross-group operators in
 * `KSpeedUnitOperators.kt` (e.g. `100.meters / 10.seconds`), or [toSpeed].
 *
 * Example:
 * ```kotlin
 * val v = 100.meters / 10.seconds
 * v.value                                       // 10.0 (normalized to m/s)
 * v.valueAs(KSpeedUnit.KILOMETERS_PER_HOUR)     // 36.0 (read back in km/h)
 * v.valueAs(KUnitPrefix.KILO with KDistanceUnit.METER, KTimeUnit.HOUR) // 36.0 (as km per h)
 * ```
 */
class KSpeedUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KSpeedUnitInstance>, KUnitMeasurable by instance {

    /**
     * Returns a new speed value with [value] (m/s) scaled by [factor]. Backs number-times-unit
     * construction (`36 of kilometersPerHour`, `10 of kilo.meters / hours`).
     */
    override fun scaledBy(factor: Double): KSpeedUnitInstance = speedUnitInstanceOf(value * factor)

    /**
     * Adds two speeds, automatically converting between different [KSpeedUnit]s since both operands are
     * always normalized to [KSpeedUnit.BASE] (m/s) internally.
     *
     * Example:
     * ```kotlin
     * (36.kilometersPerHour + 10.metersPerSecond).valueAs(KSpeedUnit.METERS_PER_SECOND) // 20.0
     * ```
     */
    override operator fun plus(other: KSpeedUnitInstance): KSpeedUnitInstance = KSpeedUnitInstance(instance + other.instance)

    /** Subtracts two speeds. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KSpeedUnitInstance): KSpeedUnitInstance = KSpeedUnitInstance(instance - other.instance)

    /**
     * Multiplies two speeds, producing a new [KMixedUnitInstance] (`m²·s⁻²`, no longer a "pure" speed).
     * To turn a speed back into a length, multiply it by a *time* instead (see `KSpeedUnitOperators.kt`).
     */
    operator fun times(other: KSpeedUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two speeds, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KSpeedUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two speeds by their normalized [value] (meters per second). */
    override operator fun compareTo(other: KSpeedUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized [value] (both operands are always normalized to m/s). */
    override fun equals(other: Any?): Boolean = other is KSpeedUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"10.0 m/s"`. */
    override fun toString(): String = "$value ${KSpeedUnit.BASE.symbol}"
}

/**
 * Converts this mixed unit to a "pure" speed value, as long as it consists of exactly two terms: one
 * [KDistanceUnit] term at exponent `+1` and one [KTimeUnit] term at exponent `-1` (order independent).
 * The terms are normalized to [KDistanceUnit.BASE]/[KTimeUnit.BASE], so the resulting speed is expressed
 * in meters per second regardless of which length/time units the terms were tagged with (e.g. a
 * `[MILE^1, HOUR^-1]` instance is converted to the equivalent m/s).
 *
 * @throws IllegalStateException if this instance is not a length-per-time speed (not exactly a
 * length term at `+1` and a time term at `-1`).
 *
 * Example:
 * ```kotlin
 * val raw = 100.meters.toUnit() / 10.seconds.toUnit() // [METER^1, SECOND^-1]
 * raw.toSpeed().valueAs(KSpeedUnit.KILOMETERS_PER_HOUR) // 36.0
 *
 * (200.meters * 50.meters).toSpeed() // throws IllegalStateException (an area, not a speed)
 * ```
 */
fun KMixedUnitInstance.toSpeed(): KSpeedUnitInstance {
    val lengthTerm = units.singleOrNull { it.unit is KDistanceUnit && it.exponent == 1 }
    val timeTerm = units.singleOrNull { it.unit is KTimeUnit && it.exponent == -1 }
    check(units.size == 2 && lengthTerm != null && timeTerm != null) {
        "KMixedUnitInstance $this does not represent a pure speed (expected one KDistanceUnit^1 and one KTimeUnit^-1 term)"
    }
    val metersPerSecond = value * lengthTerm.unit.baseValue / timeTerm.unit.baseValue
    return speedUnitInstanceOf(metersPerSecond)
}

/** Builds a [KSpeedUnitInstance] from a value already expressed in meters per second ([KSpeedUnit.BASE]). */
internal fun speedUnitInstanceOf(metersPerSecond: Double): KSpeedUnitInstance =
    KSpeedUnitInstance(KMixedUnitInstance(metersPerSecond, listOf(KUnitTerm(KDistanceUnit.BASE, 1), KUnitTerm(KTimeUnit.BASE, -1))))
