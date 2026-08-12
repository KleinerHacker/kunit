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

package org.pcsoft.framework.kunit.mechanic.solidangle

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.angle.KAngleUnit

/**
 * A **solid angle** value: the "pure" wrapper of the solid-angle group, a single [KSolidAngleUnit.BASE]
 * (steradian) term at exponent 1, always normalized internally to steradians.
 *
 * A solid angle is the squared plane angle (`1 sr = 1 rad²`), so it is a *constructed* quantity - but
 * because the steradian is an independently named SI unit with its own vocabulary (square degree, spat),
 * it is modelled as its own group with a one-term wrapper. The bridge to the angle group is the typed
 * operator `angle * angle = solidangle` in `KSolidAngleUnitOperators.kt` and the form-recognition hook
 * [toSolidAngle], which accepts the native `rad²` form.
 *
 * Instances are created via the bare tokens in `KSolidAngleUnitBareValues.kt` (e.g. `2 of steradians`),
 * the prefixed templates in `KSolidAngleUnitExtensions.kt` (e.g. `5 of milli.steradians`), operator
 * results, or [toSolidAngle].
 *
 * Example:
 * ```kotlin
 * val omega = (90 of degrees) * (90 of degrees)
 * omega into steradians // ≈ 2.4674 ((π/2)² sr)
 * ```
 */
class KSolidAngleUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitMeasurable by instance, KUnitInstance<KSolidAngleUnitInstance> {

    /**
     * Returns a new solid angle with [value] (steradians) scaled by [factor]. Backs number-times-unit
     * construction (`2 of steradians`).
     */
    override fun scaledBy(factor: Double): KSolidAngleUnitInstance = solidAngleOf(value * factor)

    /**
     * Adds two solid angles, automatically converting between different [KSolidAngleUnit]s since both
     * operands are always normalized to steradians internally.
     */
    override operator fun plus(other: KSolidAngleUnitInstance): KSolidAngleUnitInstance =
        solidAngleOf(value + other.value)

    /** Subtracts two solid angles. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KSolidAngleUnitInstance): KSolidAngleUnitInstance =
        solidAngleOf(value - other.value)

    /** Multiplies two solid angles, producing a new [KMixedUnitInstance] (no longer a "pure" solid angle). */
    operator fun times(other: KSolidAngleUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two solid angles, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KSolidAngleUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two solid angles by their normalized [value] (steradians). */
    override operator fun compareTo(other: KSolidAngleUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two solid angles are equal iff they describe the same
     * cone (e.g. `(1 of spats) == (4 * PI of steradians)`).
     */
    override fun equals(other: Any?): Boolean = other is KSolidAngleUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"2.0 sr"`. */
    override fun toString(): String = instance.toString()
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KSolidAngleUnitInstance] from a value already expressed in steradians
 * ([KSolidAngleUnit.BASE]). This is the single normalizing creation source every decomposition
 * (`angle * angle`, the native `rad²` form) funnels into.
 */
internal fun solidAngleOf(value: Double, display: KUnitDisplay? = null): KSolidAngleUnitInstance =
    KSolidAngleUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KSolidAngleUnit.BASE, 1, display))))

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" [KSolidAngleUnitInstance]. Two shapes are recognized:
 * * exactly one [KSolidAngleUnit] term at exponent 1 (already a solid angle), and
 * * exactly one [KAngleUnit] term at exponent 2 - the canonical native form `rad²`, e.g. the result of
 *   `(1 of radians).toUnit() pow 2`.
 *
 * @throws IllegalStateException if this instance matches neither shape.
 */
fun KMixedUnitInstance.toSolidAngle(): KSolidAngleUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    if (term != null && unit is KSolidAngleUnit && term.exponent == 1) {
        return solidAngleOf(value * unit.baseValue)
    }
    check(term != null && unit is KAngleUnit && term.exponent == 2) {
        "KMixedUnitInstance $this does not represent a pure solid angle (expected one KSolidAngleUnit^1 or one KAngleUnit^2 term)"
    }
    return solidAngleOf(value * unit.baseValue * unit.baseValue)
}
