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

package org.pcsoft.framework.kunit.ec

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm

/**
 * An **electric current** value: the "pure" wrapper of the electric current group, a single
 * [KElectricCurrentUnit.BASE] (ampere) term at exponent 1, always normalized internally to amperes.
 *
 * This is the *plain, one-dimensional* wrapper shape: it encapsulates a [KMixedUnitInstance] via
 * Kotlin `by` delegation (so `value`/`toUnit`/`times`/`div` and the group-agnostic `pow` come from the
 * delegate) and implements [KUnitInstance] directly (adding same-type `+`/`-`/comparison). There is no
 * exponent-specialized subtype hierarchy, and the group defines **no** cross-unit typed combination
 * results (any `*`/`/` with a foreign group yields a generic mixed unit).
 *
 * Instances are created via the bare tokens in `KElectricCurrentUnitBareValues.kt` (e.g. `5 of amperes`),
 * the prefixed templates in `KElectricCurrentUnitExtensions.kt` (e.g. `2 of milli.amperes`), operator
 * results, or [KMixedUnitInstance.toElectricCurrent]. The constructor is `internal`; callers must never
 * build one directly.
 *
 * Example:
 * ```kotlin
 * val i = 2 of milli.amperes   // 0.002 A
 * i.value                      // 0.002 (normalized to amperes)
 * i into amperes               // 0.002 (read back in amperes)
 * ```
 */
class KElectricCurrentUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitMeasurable by instance, KUnitInstance<KElectricCurrentUnitInstance> {

    /**
     * Returns a new electric current value with [value] (amperes) scaled by [factor]. Backs
     * number-times-unit construction (`10 of milli.amperes`).
     */
    override fun scaledBy(factor: Double): KElectricCurrentUnitInstance = electricCurrentOf(value * factor)

    /**
     * Adds two electric current values, automatically converting between different
     * [KElectricCurrentUnit]s since both operands are always normalized to amperes internally. Only
     * another [KElectricCurrentUnitInstance] is accepted.
     *
     * Example:
     * ```kotlin
     * ((1 of amperes) + (1 of biot)).value // 11.0
     * ```
     */
    override operator fun plus(other: KElectricCurrentUnitInstance): KElectricCurrentUnitInstance =
        electricCurrentOf(value + other.value)

    /** Subtracts two electric current values. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KElectricCurrentUnitInstance): KElectricCurrentUnitInstance =
        electricCurrentOf(value - other.value)

    /** Compares two electric current values by their normalized [value] (amperes). */
    override operator fun compareTo(other: KElectricCurrentUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two electric current values are equal iff they
     * represent the same current (e.g. `(10 of amperes) == (1 of biot)`).
     */
    override fun equals(other: Any?): Boolean = other is KElectricCurrentUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"10.0 A"`. */
    override fun toString(): String = instance.toString()
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/** Builds a [KElectricCurrentUnitInstance] from a value already expressed in amperes ([KElectricCurrentUnit.BASE]). */
internal fun electricCurrentOf(value: Double): KElectricCurrentUnitInstance =
    KElectricCurrentUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KElectricCurrentUnit.BASE, 1))))

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" [KElectricCurrentUnitInstance], as long as it consists of exactly
 * one term of any [KElectricCurrentUnit] - normalizing it to [KElectricCurrentUnit.BASE] (amperes) if it
 * isn't already.
 *
 * The term's **exponent is irrelevant** to the group check: a term is current-typed purely by its
 * [KElectricCurrentUnit] group, so this just wraps the numeric value normalized to amperes.
 *
 * @throws IllegalStateException if this instance does not consist of exactly one term of a [KElectricCurrentUnit].
 */
fun KMixedUnitInstance.toElectricCurrent(): KElectricCurrentUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KElectricCurrentUnit) {
        "KMixedUnitInstance $this does not represent a pure electric current value (expected exactly one term of a KElectricCurrentUnit)"
    }
    val normalizedValue = value * Math.pow(unit.baseValue, term.exponent.toDouble())
    return electricCurrentOf(normalizedValue)
}
