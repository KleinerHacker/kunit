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

package org.pcsoft.framework.kunit.mechanic.angle

import org.pcsoft.framework.kunit.*

/**
 * A **plane angle** value: the "pure" wrapper of the angle group, a single [KAngleUnit.BASE] (radian)
 * term at exponent 1, always normalized internally to radians.
 *
 * The angle is a *native* unit of this library - a directly measurable quantity, not a composition - so
 * this is the plain, one-dimensional wrapper shape: it encapsulates a [KMixedUnitInstance] via Kotlin
 * `by` delegation and implements [KUnitInstance] directly (adding same-type `+`/`-`/comparison).
 *
 * Deliberately **no** same-type `times` member is declared: the product of two angles is a solid angle
 * (`rad² = sr`), which the more specific extension operator in the `solidangle` package supplies
 * (a member would shadow it and degrade the result to a raw [KMixedUnitInstance]).
 *
 * Instances are created via the bare tokens in `KAngleUnitBareValues.kt` (e.g. `90 of degrees`), the
 * prefixed templates in `KAngleUnitExtensions.kt` (e.g. `5 of milli.radians`), operator results, or
 * [toAngle].
 *
 * Example:
 * ```kotlin
 * val a = 180 of degrees
 * a.value          // ≈ 3.14159 (normalized to radians)
 * a into radians   // ≈ 3.14159
 * ```
 */
class KAngleUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitMeasurable by instance, KUnitInstance<KAngleUnitInstance> {

    /**
     * Returns a new angle value with [value] (radians) scaled by [factor]. Backs number-times-unit
     * construction (`90 of degrees`).
     */
    override fun scaledBy(factor: Double): KAngleUnitInstance = angleOf(value * factor)

    /**
     * Adds two angles, automatically converting between different [KAngleUnit]s since both operands are
     * always normalized to radians internally.
     *
     * Example:
     * ```kotlin
     * ((90 of degrees) + (90 of degrees)) into turns // 0.5
     * ```
     */
    override operator fun plus(other: KAngleUnitInstance): KAngleUnitInstance = angleOf(value + other.value)

    /** Subtracts two angles. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KAngleUnitInstance): KAngleUnitInstance = angleOf(value - other.value)

    /** Divides two angles, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KAngleUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two angles by their normalized [value] (radians). */
    override operator fun compareTo(other: KAngleUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two angles are equal iff they describe the same rotation
     * (e.g. `(180 of degrees) == (0.5 of turns)`).
     */
    override fun equals(other: Any?): Boolean = other is KAngleUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"3.141592653589793 rad"`. */
    override fun toString(): String = instance.toString()

    /** The sine of this angle (a dimensionless [Double]). */
    fun sin(): Double = Math.sin(value)

    /** The cosine of this angle (a dimensionless [Double]). */
    fun cos(): Double = Math.cos(value)

    /** The tangent of this angle (a dimensionless [Double]). */
    fun tan(): Double = Math.tan(value)
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/** Builds a [KAngleUnitInstance] from a value already expressed in radians ([KAngleUnit.BASE]). */
internal fun angleOf(value: Double, display: KUnitDisplay? = null): KAngleUnitInstance =
    KAngleUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KAngleUnit.BASE, 1, display))))

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" [KAngleUnitInstance], as long as it consists of exactly one term
 * of any [KAngleUnit] at exponent 1 - normalizing it to [KAngleUnit.BASE] (radians) if it isn't already.
 *
 * @throws IllegalStateException if this instance does not consist of exactly one [KAngleUnit] term at
 * exponent 1.
 */
fun KMixedUnitInstance.toAngle(): KAngleUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KAngleUnit && term.exponent == 1) {
        "KMixedUnitInstance $this does not represent a pure angle (expected exactly one KAngleUnit^1 term)"
    }
    return angleOf(value * unit.baseValue)
}
