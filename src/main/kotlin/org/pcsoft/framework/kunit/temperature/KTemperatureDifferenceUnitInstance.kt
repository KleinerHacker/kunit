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

package org.pcsoft.framework.kunit.temperature

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm

/**
 * A **temperature difference** value: the "pure" wrapper of the temperature difference group, a single
 * [KTemperatureDifferenceUnit.BASE] (kelvin) term at exponent 1, normalized to kelvin.
 *
 * In contrast to the affine [KTemperatureUnitInstance] this is an ordinary **linear** wrapper (a
 * *vector*, not a *point*): it fully implements [KUnitInstance] with same-type `+`/`-`/comparison and
 * inherits the plain multiplicative `readBaseValue`/`times`/`div`/`pow` from its delegate. It overrides
 * [scaledBy] only to keep the concrete type on scalar scaling (`diff * 2`); the scaling itself stays the
 * plain linear one. Two differences are equal iff they describe the same kelvin interval.
 *
 * A difference is produced by subtracting two absolute temperatures
 * (`(30 of celsius) - (10 of celsius)`) or explicitly via [KTemperatureDifference.ofKelvin]; it can be
 * added to / subtracted from an absolute [KTemperatureUnitInstance] to yield an absolute temperature.
 *
 * Example:
 * ```kotlin
 * val d = (30 of celsius) - (10 of celsius)   // 20 K difference
 * (25 of celsius) + d                          // 45 °C (absolute)
 * ```
 */
class KTemperatureDifferenceUnitInstance internal constructor(
    internal val instance: KMixedUnitInstance,
) : KUnitMeasurable by instance, KUnitInstance<KTemperatureDifferenceUnitInstance> {

    /**
     * Returns a new temperature difference with [value] scaled by [factor], preserving the concrete type
     * (so `diff * 2` stays a [KTemperatureDifferenceUnitInstance]). The scaling is plain linear - a
     * difference is a *vector*, unlike the affine absolute [KTemperatureUnitInstance].
     */
    override fun scaledBy(factor: Double): KTemperatureDifferenceUnitInstance = temperatureDifferenceOf(value * factor)

    /** Adds two temperature differences on their kelvin [value]. The result is again a difference. */
    override operator fun plus(other: KTemperatureDifferenceUnitInstance): KTemperatureDifferenceUnitInstance =
        temperatureDifferenceOf(value + other.value)

    /** Subtracts two temperature differences on their kelvin [value]. See [plus]. */
    override operator fun minus(other: KTemperatureDifferenceUnitInstance): KTemperatureDifferenceUnitInstance =
        temperatureDifferenceOf(value - other.value)

    /** Compares two temperature differences by their normalized kelvin [value]. */
    override operator fun compareTo(other: KTemperatureDifferenceUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized kelvin [value]: two differences are equal iff they represent the
     * same kelvin interval.
     */
    override fun equals(other: Any?): Boolean = other is KTemperatureDifferenceUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit (kelvin) representation, e.g. `"20.0 K"`. */
    override fun toString(): String = instance.toString()
}

// --- Explicit construction (Companion factory) ---------------------------------------------------

/**
 * Explicit entry point for building a [KTemperatureDifferenceUnitInstance]. A difference is deliberately
 * **not** created through the generic `of` verb (which is reserved for absolute quantities); constructing
 * it here makes the "this is an interval, not an absolute temperature" intent explicit at the call site.
 */
object KTemperatureDifference {
    /** Builds a temperature difference of [value] kelvin, e.g. `KTemperatureDifference.ofKelvin(20)`. */
    fun ofKelvin(value: Number): KTemperatureDifferenceUnitInstance = temperatureDifferenceOf(value.toDouble())
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/** Builds a [KTemperatureDifferenceUnitInstance] from a value already expressed in kelvin. */
internal fun temperatureDifferenceOf(value: Double): KTemperatureDifferenceUnitInstance =
    KTemperatureDifferenceUnitInstance(
        KMixedUnitInstance(value, listOf(KUnitTerm(KTemperatureDifferenceUnit.BASE, 1))),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" [KTemperatureDifferenceUnitInstance], as long as it consists of
 * exactly one term of any [KTemperatureDifferenceUnit], normalizing it to kelvin.
 *
 * @throws IllegalStateException if this instance does not consist of exactly one term of a
 * [KTemperatureDifferenceUnit].
 */
fun KMixedUnitInstance.toTemperatureDifference(): KTemperatureDifferenceUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KTemperatureDifferenceUnit) {
        "KMixedUnitInstance $this does not represent a pure temperature difference value (expected exactly one term of a KTemperatureDifferenceUnit)"
    }
    return temperatureDifferenceOf(value * Math.pow(unit.baseValue, term.exponent.toDouble()))
}
