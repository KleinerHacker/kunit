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

package org.pcsoft.framework.kunit.thermo.amountofsubstance

import org.pcsoft.framework.kunit.*

/**
 * An **amount of substance** value: the "pure" wrapper of the amount-of-substance group, a single
 * [KAmountOfSubstanceUnit.BASE] (mole) term at exponent 1, always normalized internally to moles.
 *
 * Amount of substance is a *native* unit - a measurable SI base quantity, not a composition - so this is
 * the plain, one-dimensional wrapper shape: it encapsulates a [KMixedUnitInstance] via Kotlin `by`
 * delegation and implements [KUnitInstance] directly (adding same-type `+`/`-`/comparison).
 *
 * Instances are created via the bare tokens in `KAmountOfSubstanceUnitBareValues.kt` (e.g. `5 of moles`),
 * the prefixed templates in `KAmountOfSubstanceUnitExtensions.kt` (e.g. `2 of milli.moles`), operator
 * results, or [toAmountOfSubstance].
 *
 * Example:
 * ```kotlin
 * val n = 2 of moles
 * n.value          // 2.0 (normalized to moles)
 * n into milli.moles // 2000.0
 * ```
 */
class KAmountOfSubstanceUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitMeasurable by instance, KUnitInstance<KAmountOfSubstanceUnitInstance> {

    /**
     * Returns a new amount value with [value] (moles) scaled by [factor]. Backs number-times-unit
     * construction (`10 of milli.moles`).
     */
    override fun scaledBy(factor: Double): KAmountOfSubstanceUnitInstance = amountOfSubstanceOf(value * factor)

    /**
     * Adds two amounts, automatically converting between different [KAmountOfSubstanceUnit]s since both
     * operands are always normalized to moles internally.
     *
     * Example:
     * ```kotlin
     * ((1 of moles) + (500 of milli.moles)).value // 1.5
     * ```
     */
    override operator fun plus(other: KAmountOfSubstanceUnitInstance): KAmountOfSubstanceUnitInstance =
        amountOfSubstanceOf(value + other.value)

    /** Subtracts two amounts. See [plus] for the automatic unit conversion. */
    override operator fun minus(other: KAmountOfSubstanceUnitInstance): KAmountOfSubstanceUnitInstance =
        amountOfSubstanceOf(value - other.value)

    /** Compares two amounts by their normalized [value] (moles). */
    override operator fun compareTo(other: KAmountOfSubstanceUnitInstance): Int = value.compareTo(other.value)

    /**
     * Structural equality by normalized [value]: two amounts are equal iff they represent the same amount
     * of substance (e.g. `(1 of moles) == (1000 of milli.moles)`).
     */
    override fun equals(other: Any?): Boolean = other is KAmountOfSubstanceUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /** Base-unit representation, e.g. `"2.0 mol"`. */
    override fun toString(): String = instance.toString()

    /**
     * The number of elementary entities this amount contains, i.e. `value * `[AVOGADRO_CONSTANT]. The
     * result is a plain [Double] because a particle count is dimensionless.
     *
     * Example:
     * ```kotlin
     * (2 of moles).particleCount() // ≈ 1.2044e24
     * ```
     */
    fun particleCount(): Double = value * AVOGADRO_CONSTANT
}

// --- Factory helper (single creation source; constructor stays internal) -------------------------

/** Builds a [KAmountOfSubstanceUnitInstance] from a value already expressed in moles ([KAmountOfSubstanceUnit.BASE]). */
internal fun amountOfSubstanceOf(value: Double, display: KUnitDisplay? = null): KAmountOfSubstanceUnitInstance =
    KAmountOfSubstanceUnitInstance(
        KMixedUnitInstance(value, listOf(KUnitTerm(KAmountOfSubstanceUnit.BASE, 1, display))),
    )

// --- Conversion from the generic engine ----------------------------------------------------------

/**
 * Converts this mixed unit to a "pure" [KAmountOfSubstanceUnitInstance], as long as it consists of exactly
 * one term of any [KAmountOfSubstanceUnit] - normalizing it to [KAmountOfSubstanceUnit.BASE] (moles) if it
 * isn't already.
 *
 * @throws IllegalStateException if this instance does not consist of exactly one term of a
 * [KAmountOfSubstanceUnit].
 */
fun KMixedUnitInstance.toAmountOfSubstance(): KAmountOfSubstanceUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KAmountOfSubstanceUnit) {
        "KMixedUnitInstance $this does not represent a pure amount of substance (expected exactly one term of a KAmountOfSubstanceUnit)"
    }
    return amountOfSubstanceOf(value * Math.pow(unit.baseValue, term.exponent.toDouble()))
}
