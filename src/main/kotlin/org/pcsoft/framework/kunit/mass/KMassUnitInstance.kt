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

package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitDisplay
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm

/**
 * A **mass** value: the "pure" wrapper of the mass group, a single [KMassUnit.BASE] (gram) term at
 * exponent 1, always normalized internally to grams.
 *
 * This is the *plain, one-dimensional* wrapper shape: it encapsulates a [KMixedUnitInstance] via
 * Kotlin `by` delegation (so `value`/`toUnit`/`times`/`div` and the group-agnostic `pow` come from the
 * delegate) and implements [KUnitInstance] directly (adding same-type `+`/`-`/comparison). There is no
 * exponent-specialized subtype hierarchy (unlike the distance group) and no `Duration` backing (unlike
 * the time group).
 *
 * Instances are created via the bare tokens in `KMassUnitBareValues.kt` (e.g. `5 of grams`), the
 * prefixed templates in `KMassUnitExtensions.kt` (e.g. `2 of kilo.grams`), operator results, or
 * [KMixedUnitInstance.toMass]. The constructor is `internal`; callers must never build one directly.
 *
 * Example:
 * ```kotlin
 * val m = 2 of kilo.grams   // 2000 g
 * m.value                   // 2000.0 (normalized to grams)
 * m into pounds             // ≈ 4.409 (read back in pounds)
 * ```
 */
class KMassUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitMeasurable by instance, KUnitInstance<KMassUnitInstance> {

    /**
     * Returns a new mass value with [value] (grams) scaled by [factor]. Backs number-times-unit
     * construction (`10 of kilo.grams`).
     */
    override fun scaledBy(factor: Double): KMassUnitInstance = massOf(value * factor)

    /**
     * Adds two mass values, automatically converting between different [KMassUnit]s since both operands
     * are always normalized to grams internally. Only another [KMassUnitInstance] is accepted.
     *
     * Example:
     * ```kotlin
     * ((1 of kilo.grams) + (500 of grams)).value // 1500.0
     * ```
     */
    override operator fun plus(other: KMassUnitInstance): KMassUnitInstance = massOf(value + other.value)

    /** Subtracts two mass values. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KMassUnitInstance): KMassUnitInstance = massOf(value - other.value)

    /** Compares two mass values by their normalized [value] (grams). */
    override operator fun compareTo(other: KMassUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two mass values are equal iff they represent the same
     * quantity of mass (e.g. `(1 of kilo.grams) == (1000 of grams)`).
     */
    override fun equals(other: Any?): Boolean = other is KMassUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"2000.0 g"`. */
    override fun toString(): String = instance.toString()
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/** Builds a [KMassUnitInstance] from a value already expressed in grams ([KMassUnit.BASE]). */
internal fun massOf(value: Double, display: KUnitDisplay? = null): KMassUnitInstance =
    KMassUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KMassUnit.BASE, 1, display))))

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" [KMassUnitInstance], as long as it consists of exactly one term
 * of any [KMassUnit] - normalizing it to [KMassUnit.BASE] (grams) if it isn't already.
 *
 * The term's **exponent is irrelevant** to the group check: a term is mass-typed purely by its
 * [KMassUnit] group, so this just wraps the numeric value normalized to grams.
 *
 * @throws IllegalStateException if this instance does not consist of exactly one term of a [KMassUnit].
 */
fun KMixedUnitInstance.toMass(): KMassUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KMassUnit) {
        "KMixedUnitInstance $this does not represent a pure mass value (expected exactly one term of a KMassUnit)"
    }
    val normalizedValue = value * Math.pow(unit.baseValue, term.exponent.toDouble())
    return massOf(normalizedValue)
}
