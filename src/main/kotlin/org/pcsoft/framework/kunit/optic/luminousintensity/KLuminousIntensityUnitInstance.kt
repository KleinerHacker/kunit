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

package org.pcsoft.framework.kunit.optic.luminousintensity

import org.pcsoft.framework.kunit.*

/**
 * A **luminous intensity** value: the "pure" wrapper of the luminous-intensity group, a single
 * [KLuminousIntensityUnit.BASE] (candela) term at exponent 1, always normalized internally to candelas.
 *
 * Luminous intensity is a *native* unit - the seventh SI base quantity, not a composition - so this is the
 * plain, one-dimensional wrapper shape: it encapsulates a [KMixedUnitInstance] via Kotlin `by` delegation
 * and implements [KUnitInstance] directly (adding same-type `+`/`-`/comparison).
 *
 * Instances are created via the bare tokens in `KLuminousIntensityUnitBareValues.kt` (e.g. `5 of
 * candelas`), the prefixed templates in `KLuminousIntensityUnitExtensions.kt` (e.g. `2 of milli.candelas`),
 * operator results, or [toLuminousIntensity].
 *
 * Example:
 * ```kotlin
 * val i = 1200 of candelas
 * i.value            // 1200.0 (normalized to candelas)
 * i into kilo.candelas // 1.2
 * ```
 */
class KLuminousIntensityUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitMeasurable by instance, KUnitInstance<KLuminousIntensityUnitInstance> {

    /**
     * Returns a new luminous intensity with [value] (candelas) scaled by [factor]. Backs number-times-unit
     * construction (`10 of milli.candelas`).
     */
    override fun scaledBy(factor: Double): KLuminousIntensityUnitInstance = luminousIntensityOf(value * factor)

    /**
     * Adds two luminous intensities, automatically converting between different [KLuminousIntensityUnit]s
     * since both operands are always normalized to candelas internally.
     *
     * Example:
     * ```kotlin
     * ((1 of candelas) + (500 of milli.candelas)).value // 1.5
     * ```
     */
    override operator fun plus(other: KLuminousIntensityUnitInstance): KLuminousIntensityUnitInstance =
        luminousIntensityOf(value + other.value)

    /** Subtracts two luminous intensities. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KLuminousIntensityUnitInstance): KLuminousIntensityUnitInstance =
        luminousIntensityOf(value - other.value)

    /** Compares two luminous intensities by their normalized [value] (candelas). */
    override operator fun compareTo(other: KLuminousIntensityUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two luminous intensities are equal iff they represent the
     * same intensity (e.g. `(1 of candelas) == (1000 of milli.candelas)`).
     */
    override fun equals(other: Any?): Boolean = other is KLuminousIntensityUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"1200.0 cd"`. */
    override fun toString(): String = instance.toString()
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/**
 * Builds a [KLuminousIntensityUnitInstance] from a value already expressed in candelas
 * ([KLuminousIntensityUnit.BASE]).
 */
internal fun luminousIntensityOf(value: Double, display: KUnitDisplay? = null): KLuminousIntensityUnitInstance =
    KLuminousIntensityUnitInstance(
        KMixedUnitInstance(value, listOf(KUnitTerm(KLuminousIntensityUnit.BASE, 1, display))),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" [KLuminousIntensityUnitInstance], as long as it consists of exactly
 * one term of any [KLuminousIntensityUnit] - normalizing it to [KLuminousIntensityUnit.BASE] (candelas) if
 * it isn't already.
 *
 * @throws IllegalStateException if this instance does not consist of exactly one term of a
 * [KLuminousIntensityUnit].
 */
fun KMixedUnitInstance.toLuminousIntensity(): KLuminousIntensityUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KLuminousIntensityUnit) {
        "KMixedUnitInstance $this does not represent a pure luminous intensity " +
                "(expected exactly one term of a KLuminousIntensityUnit)"
    }
    return luminousIntensityOf(value * Math.pow(unit.baseValue, term.exponent.toDouble()))
}
