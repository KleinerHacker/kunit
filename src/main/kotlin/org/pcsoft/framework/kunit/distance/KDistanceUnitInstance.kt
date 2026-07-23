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

package org.pcsoft.framework.kunit.distance

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitDisplay
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm

/**
 * The general, "any exponent" wrapper of the distance group: a single-term [KMixedUnitInstance] of
 * [KDistanceUnit.BASE] (meter) at an arbitrary exponent. It is the open **base type / catch-all** of
 * the distance hierarchy and the abstraction under which the exponent-specialized leaf types live:
 *
 * - [KLengthUnitInstance] - exponent 1 (a length)
 * - [KAreaUnitInstance] - exponent 2 (an area)
 * - [KVolumeUnitInstance] - exponent 3 (a volume)
 *
 * Results whose exponent falls **outside** `{1, 2, 3}` (e.g. `m⁴`, `m⁻¹`) are represented by this base
 * type directly; a dimensionless result (exponent 0) drops to a raw [KMixedUnitInstance].
 *
 * By design the base type is **not additive**: it exposes **no** `plus`/`minus`/`compareTo`. Those live
 * only on the leaf types, restricted to their own dimension - which is exactly what makes a
 * cross-dimension operation such as `length + area` a **compile error** rather than a runtime failure.
 * Multiplication and division, on the other hand, are always allowed and may change the dimension
 * (see [times]/[div] and the leaf overloads).
 *
 * The value is always normalized internally to [KDistanceUnit.BASE], regardless of which unit or
 * [org.pcsoft.framework.kunit.KUnitPrefix] it was constructed with.
 *
 * Example:
 * ```kotlin
 * val d = 5.miles            // KLengthUnitInstance (exponent 1)
 * d.value                    // 8046.72 (normalized to meters)
 *
 * val weird = 2.meters * 2.meters * 2.meters * 2.meters // KDistanceUnitInstance (exponent 4)
 * weird.exponent             // 4
 * ```
 */
open class KDistanceUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitMeasurable by instance {

    /** The single term's exponent (1 for a length, 2 for an area, 3 for a volume, 4+/negative for the general type). */
    val exponent: Int get() = instance.units.single().exponent

    /**
     * Returns a new distance value of the same exponent with [value] scaled by [factor] (the concrete
     * leaf type follows the exponent). Backs number-times-unit construction (`10 of kilo.meters`).
     */
    override fun scaledBy(factor: Double): KDistanceUnitInstance = distanceOf(value * factor, exponent)

    /**
     * Multiplies this distance value by another distance value of **any** dimension, producing a raw
     * [KMixedUnitInstance] whose exponent is the sum of both exponents (a resulting exponent of `0`
     * drops the unit entirely). Always allowed.
     *
     * The exponent-specialized leaf types ([KLengthUnitInstance] etc.) additionally provide **typed**
     * overloads for the common cases (e.g. `length * length = area`); this broad member is the fallback
     * whenever the static type of an operand is the general [KDistanceUnitInstance].
     */
    operator fun times(other: KDistanceUnitInstance): KMixedUnitInstance = instance * other.instance

    /**
     * Divides this distance value by another distance value of **any** dimension, producing a raw
     * [KMixedUnitInstance] whose exponent is the difference of both exponents (a resulting exponent of
     * `0` drops the unit entirely). Always allowed. See [times] for the typed leaf overloads.
     */
    operator fun div(other: KDistanceUnitInstance): KMixedUnitInstance = instance / other.instance

    /**
     * Narrows this distance value to a [KLengthUnitInstance], requiring [exponent] to be **1**. For a value
     * that is already a length this returns it unchanged; it is the in-hierarchy counterpart of
     * [KMixedUnitInstance.toLength] (e.g. to narrow an element of a `List<KDistanceUnitInstance>`).
     *
     * @throws IllegalStateException if [exponent] is not 1.
     */
    fun toLength(): KLengthUnitInstance {
        check(exponent == 1) { "KDistanceUnitInstance $this is not a length (expected exponent 1, was $exponent)" }
        // Exponent 1 is only ever produced as a KLengthUnitInstance (see distanceOf), so the cast holds.
        return this as KLengthUnitInstance
    }

    /**
     * Narrows this distance value to a [KAreaUnitInstance], requiring [exponent] to be **2**. For a value
     * that is already an area this returns it unchanged; the in-hierarchy counterpart of
     * [KMixedUnitInstance.toArea].
     *
     * @throws IllegalStateException if [exponent] is not 2.
     */
    fun toArea(): KAreaUnitInstance {
        check(exponent == 2) { "KDistanceUnitInstance $this is not an area (expected exponent 2, was $exponent)" }
        // Exponent 2 is only ever produced as a KAreaUnitInstance (see distanceOf), so the cast holds.
        return this as KAreaUnitInstance
    }

    /**
     * Narrows this distance value to a [KVolumeUnitInstance], requiring [exponent] to be **3**. For a value
     * that is already a volume this returns it unchanged; the in-hierarchy counterpart of
     * [KMixedUnitInstance.toVolume].
     *
     * @throws IllegalStateException if [exponent] is not 3.
     */
    fun toVolume(): KVolumeUnitInstance {
        check(exponent == 3) { "KDistanceUnitInstance $this is not a volume (expected exponent 3, was $exponent)" }
        // Exponent 3 is only ever produced as a KVolumeUnitInstance (see distanceOf), so the cast holds.
        return this as KVolumeUnitInstance
    }

    /**
     * Structural equality by **concrete type**, [exponent] and normalized [value]: two values are equal
     * only if they are the same dimension (same runtime class and exponent) and the same normalized
     * magnitude. Cross-dimension comparison therefore simply yields `false` (a length never equals an
     * area) rather than throwing.
     */
    override fun equals(other: Any?): Boolean =
        other is KDistanceUnitInstance && javaClass == other.javaClass && exponent == other.exponent && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"5.0 m"` for a length, `"10000.0 m^2"` for an area. */
    override fun toString(): String = instance.toString()
}

/**
 * Raises this distance value to the integer power [n], staying **inside** the distance hierarchy: the
 * normalized [KDistanceUnitInstance.value] is raised to [n] and the [KDistanceUnitInstance.exponent] is
 * multiplied by [n], so the concrete result type follows the resulting exponent
 * ([KLengthUnitInstance]/[KAreaUnitInstance]/[KVolumeUnitInstance] for 1/2/3, otherwise the general
 * [KDistanceUnitInstance]).
 *
 * This is the distance-typed counterpart of the group-agnostic [KMixedUnitInstance.pow]; unlike that
 * one it keeps the value dimensioned. `2.meters pow 2` is therefore a [KAreaUnitInstance] of `4.0 m²`
 * (i.e. `(2 m)²`), `2.meters pow 3` a [KVolumeUnitInstance].
 *
 * Precedence note: `pow` is a named infix function and binds **weaker** than `* / + -`; parenthesize in
 * mixed expressions.
 *
 * @param n the integer exponent. `n == 0` yields the dimensionless case within the distance group
 * (`m⁰`, value `1.0`); for a truly unit-less [KMixedUnitInstance] use `x.toUnit() pow 0`. Negative `n`
 * inverts the dimension (e.g. `m` → `m⁻ⁿ`).
 *
 * Example:
 * ```kotlin
 * (2.meters pow 2)          // KAreaUnitInstance: 4.0 m²
 * (2 kilo meters pow 2)     // KAreaUnitInstance: 4_000_000.0 m²  ((2000 m)²)
 * (2.meters pow 2 pow 2)    // KVolumeUnitInstance? no - exponent 4 -> KDistanceUnitInstance: 16.0 m⁴
 * ```
 */
infix fun KDistanceUnitInstance.pow(n: Int): KDistanceUnitInstance =
    distanceOf(Math.pow(value, n.toDouble()), exponent * n)

// --- Factory helpers (single creation source; constructors stay internal) -----------------------

/**
 * Builds the correct distance instance for a value already normalized to [KDistanceUnit.BASE] and the
 * given [exponent]: a [KLengthUnitInstance]/[KAreaUnitInstance]/[KVolumeUnitInstance] for exponent
 * 1/2/3, otherwise the general [KDistanceUnitInstance].
 */
internal fun distanceOf(value: Double, exponent: Int, display: KUnitDisplay? = null): KDistanceUnitInstance {
    val instance = KMixedUnitInstance(value, listOf(KUnitTerm(KDistanceUnit.BASE, exponent, display)))
    return when (exponent) {
        1 -> KLengthUnitInstance(instance)
        2 -> KAreaUnitInstance(instance)
        3 -> KVolumeUnitInstance(instance)
        else -> KDistanceUnitInstance(instance)
    }
}

/** Builds a [KLengthUnitInstance] (exponent 1) from a value already expressed in meters. */
internal fun lengthOf(value: Double, display: KUnitDisplay? = null): KLengthUnitInstance =
    KLengthUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KDistanceUnit.BASE, 1, display))))

/** Builds a [KAreaUnitInstance] (exponent 2) from a value already expressed in square meters. */
internal fun areaOf(value: Double, display: KUnitDisplay? = null): KAreaUnitInstance =
    KAreaUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KDistanceUnit.BASE, 2, display))))

/** Builds a [KVolumeUnitInstance] (exponent 3) from a value already expressed in cubic meters. */
internal fun volumeOf(value: Double, display: KUnitDisplay? = null): KVolumeUnitInstance =
    KVolumeUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KDistanceUnit.BASE, 3, display))))

// --- Conversions from the generic engine --------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" distance value (length, area, volume, or any other exponent),
 * as long as it consists of exactly one term of any [KDistanceUnit], at any exponent - normalizing it
 * to [KDistanceUnit.BASE] if it isn't already. The concrete runtime type is the matching leaf
 * ([KLengthUnitInstance]/[KAreaUnitInstance]/[KVolumeUnitInstance]) for exponent 1/2/3, or the general
 * [KDistanceUnitInstance] otherwise.
 *
 * @throws IllegalStateException if this instance does not consist of exactly one term of a
 * [KDistanceUnit].
 */
fun KMixedUnitInstance.toDistance(): KDistanceUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KDistanceUnit) {
        "KMixedUnitInstance $this does not represent a pure distance value (expected exactly one term of a KDistanceUnit)"
    }
    val normalizedValue = value * Math.pow(unit.baseValue, term.exponent.toDouble())
    return distanceOf(normalizedValue, term.exponent)
}

/**
 * Converts this mixed unit to a [KLengthUnitInstance], requiring it to be a single [KDistanceUnit] term
 * at **exponent 1**.
 *
 * @throws IllegalStateException if it is not a pure distance value, or its exponent is not 1.
 */
fun KMixedUnitInstance.toLength(): KLengthUnitInstance {
    val d = toDistance()
    check(d.exponent == 1) { "KMixedUnitInstance $this is not a length (expected exponent 1, was ${d.exponent})" }
    return d as KLengthUnitInstance
}

/**
 * Converts this mixed unit to a [KAreaUnitInstance], requiring it to be a single [KDistanceUnit] term
 * at **exponent 2**.
 *
 * @throws IllegalStateException if it is not a pure distance value, or its exponent is not 2.
 */
fun KMixedUnitInstance.toArea(): KAreaUnitInstance {
    val d = toDistance()
    check(d.exponent == 2) { "KMixedUnitInstance $this is not an area (expected exponent 2, was ${d.exponent})" }
    return d as KAreaUnitInstance
}

/**
 * Converts this mixed unit to a [KVolumeUnitInstance], requiring it to be a single [KDistanceUnit] term
 * at **exponent 3**.
 *
 * @throws IllegalStateException if it is not a pure distance value, or its exponent is not 3.
 */
fun KMixedUnitInstance.toVolume(): KVolumeUnitInstance {
    val d = toDistance()
    check(d.exponent == 3) { "KMixedUnitInstance $this is not a volume (expected exponent 3, was ${d.exponent})" }
    return d as KVolumeUnitInstance
}
