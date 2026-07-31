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

package org.pcsoft.framework.kunit.mechanic.strain

import org.pcsoft.framework.kunit.*

/**
 * A **strain** value (relative deformation `ΔL/L`): the "pure" wrapper of the strain group, a single
 * [KStrainUnit.BASE] term at exponent 1, always normalized internally to the plain ratio.
 *
 * Strain is *dimensionless* - a length divided by a length. The generic engine represents such a quotient as
 * a mixed unit with **no** unit terms, which is why the native decomposition `length / length` is converted
 * through [toStrain] rather than by a typed operator (`KLengthUnitInstance.div` is a member and cannot be
 * overridden). It is still modelled as its own group, because its readings (percent, per mille,
 * microstrain) form a genuine unit vocabulary.
 *
 * Instances are created via the bare tokens in `KStrainUnitBareValues.kt` (e.g. `2 of percent`), the
 * prefixed templates in `KStrainUnitExtensions.kt` (e.g. `5 of milli.ratio`), or [toStrain].
 *
 * Example:
 * ```kotlin
 * // a 1 m bar elongated by 2 mm
 * val e = ((2 of milli.meters) / (1 of meters)).toStrain() // 0.002
 * e into perMille                                          // 2.0
 * e into percent                                           // 0.2
 * ```
 */
class KStrainUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitMeasurable by instance, KUnitInstance<KStrainUnitInstance> {

    /**
     * Returns a new strain value with [value] (the plain ratio) scaled by [factor]. Backs number-times-unit
     * construction (`2 of percent`).
     */
    override fun scaledBy(factor: Double): KStrainUnitInstance = strainOf(value * factor)

    /**
     * Adds two strains, automatically converting between different [KStrainUnit]s since both operands are
     * always normalized to the plain ratio internally.
     */
    override operator fun plus(other: KStrainUnitInstance): KStrainUnitInstance = strainOf(value + other.value)

    /** Subtracts two strains. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KStrainUnitInstance): KStrainUnitInstance = strainOf(value - other.value)

    /** Multiplies two strains, producing a new [KMixedUnitInstance]. */
    operator fun times(other: KStrainUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two strains, producing a new (dimensionless) [KMixedUnitInstance]. */
    operator fun div(other: KStrainUnitInstance): KMixedUnitInstance = instance / other.instance

    /** Compares two strains by their normalized [value] (the plain ratio). */
    override operator fun compareTo(other: KStrainUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized [value] (e.g. `(1 of percent) == (10 of perMille)`). */
    override fun equals(other: Any?): Boolean = other is KStrainUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"0.002 1"` (the plain ratio). */
    override fun toString(): String = instance.toString()
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/** Builds a [KStrainUnitInstance] from a value already expressed as the plain ratio ([KStrainUnit.BASE]). */
internal fun strainOf(value: Double, display: KUnitDisplay? = null): KStrainUnitInstance =
    KStrainUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KStrainUnit.BASE, 1, display))))

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" [KStrainUnitInstance]. Two shapes are recognized:
 * * a **dimensionless** instance with no unit terms at all - the native form `length / length`, and
 * * exactly one [KStrainUnit] term at exponent 1 (already a strain).
 *
 * @throws IllegalStateException if this instance carries any other unit terms (i.e. is not dimensionless).
 */
fun KMixedUnitInstance.toStrain(): KStrainUnitInstance {
    if (units.isEmpty()) return strainOf(value)
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KStrainUnit && term.exponent == 1) {
        "KMixedUnitInstance $this does not represent a pure strain (expected a dimensionless instance or exactly one KStrainUnit^1 term)"
    }
    return strainOf(value * unit.baseValue)
}
