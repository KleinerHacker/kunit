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

package org.pcsoft.framework.kunit

/**
 * The common abstraction shared by **every** unit value in this library: the generic mixed unit
 * ([KMixedUnitInstance]) as well as every "pure" unit wrapper (e.g. `KLengthUnitInstance`,
 * `KTimeUnitInstance`).
 *
 * It captures exactly the surface that is identical across all of them:
 * - a normalized [value] (always a [Double]),
 * - a conversion into the generic [KMixedUnitInstance] engine ([toUnit]),
 * - multiplication and division against an arbitrary [KMixedUnitInstance] ([times]/[div]).
 *
 * The "pure" wrappers do **not** implement these members by hand - they delegate this interface to
 * their underlying [KMixedUnitInstance] via Kotlin's `by` delegation (e.g.
 * `class KLengthUnitInstance(...) : KUnitInstance<KLengthUnitInstance>, KUnitMeasurable by instance`).
 * Only [KMixedUnitInstance] implements it directly.
 *
 * The richer, self-typed surface of the "pure" wrappers (same-type `+`/`-`/comparison, unit-target
 * conversion) lives in the sub-interface [KUnitInstance].
 *
 * Example:
 * ```kotlin
 * val values: List<KUnitMeasurable> = listOf(5.meters, 2.hours, 10.meters / 2.seconds)
 * values.map { it.value }                 // normalized base values
 * values.map { it.toUnit() } // uniform generic representation
 * ```
 */
interface KUnitMeasurable {

    /**
     * The value normalized to the base unit(s) of this measure, always exposed as a [Double].
     *
     * For a "pure" wrapper this is the value in its group's base unit (e.g. meters, seconds), raised
     * to its exponent; for a [KMixedUnitInstance] it is the raw value in the exact scale of its
     * [KMixedUnitInstance.units].
     */
    val value: Double

    /**
     * The generic [KMixedUnitInstance] representation of this measure, the common currency of the
     * mixed-unit engine (`*`, `/`, `+`, `-`, conversion, formatting).
     *
     * For [KMixedUnitInstance] this returns `this`; for a "pure" wrapper it returns the underlying
     * single-term instance (e.g. `METER^1`, `SECOND^1`).
     */
    fun toUnit(): KMixedUnitInstance

    /**
     * Multiplies this measure with an arbitrary mixed unit, producing a new [KMixedUnitInstance] whose
     * unit terms are the union of both sides' terms with their exponents added (a resulting exponent
     * of `0` drops that unit). Always allowed.
     *
     * Example:
     * ```kotlin
     * val speed = 10.meters / 2.seconds          // [METER^1, SECOND^-1]
     * (speed * 4.seconds.toUnit())   // [METER^1] (SECOND^-1 + SECOND^1 cancels)
     * ```
     */
    operator fun times(other: KMixedUnitInstance): KMixedUnitInstance

    /**
     * Divides this measure by an arbitrary mixed unit, producing a new [KMixedUnitInstance] whose unit
     * terms are this side's terms combined with `other`'s terms with their exponents negated (a
     * resulting exponent of `0` drops that unit). Always allowed.
     *
     * Example:
     * ```kotlin
     * val distance = 10.meters.toUnit() // [METER^1]
     * val time = 2.seconds.toUnit()     // [SECOND^1]
     * (distance / time)                                 // value=5.0, [METER^1, SECOND^-1]
     * ```
     */
    operator fun div(other: KMixedUnitInstance): KMixedUnitInstance
}

/**
 * The common abstraction of every "pure" unit wrapper (e.g. `KLengthUnitInstance`,
 * `KTimeUnitInstance`): a value that belongs to exactly one unit group and therefore supports
 * same-group, same-exponent arithmetic and comparison against **its own type**, plus conversion and
 * formatting against a single [KUnitTarget].
 *
 * It is a self-referential ("F-bounded") generic: [SELF] is the concrete wrapper itself, so that
 * `+`, `-` and comparison are statically restricted to the same unit type (e.g. a
 * `KLengthUnitInstance` can only be added to another `KLengthUnitInstance`, never to a
 * `KTimeUnitInstance`). Mixing different groups goes through the inherited [times]/[div] against a
 * [KMixedUnitInstance] instead.
 *
 * Implementations gain the group-agnostic [KUnitMeasurable] surface (value, generic conversion,
 * mixed `*`/`/`) via `by` delegation to their internal [KMixedUnitInstance]; only the members
 * declared here are implemented on the wrapper itself.
 *
 * Example:
 * ```kotlin
 * class KLengthUnitInstance internal constructor(internal val instance: KMixedUnitInstance)
 *     : KUnitInstance<KLengthUnitInstance>, KUnitMeasurable by instance { ... }
 * ```
 */
interface KUnitInstance<SELF : KUnitInstance<SELF>> : KUnitMeasurable {

    /**
     * Converts [value] into the given single [target] unit, prefixed unit, or (for the matching
     * exponent) derived unit.
     *
     * Unlike [KMixedUnitInstance.valueAs], which takes one target **per term** (vararg), a "pure"
     * wrapper always has exactly one term and therefore accepts exactly one target.
     *
     * @throws IllegalStateException if [target] does not belong to this value's unit group, or (for a
     * [KDerivedUnit]/[KScaledDerivedUnit] target) if its exponent does not match this value's exponent.
     *
     * Example:
     * ```kotlin
     * 5.miles.valueAs(KDistanceUnit.MILE)                        // 5.0
     * 2.hours.valueAs(KUnitPrefix.MILLI with KTimeUnit.SECOND) // 7_200_000.0
     * ```
     */
    fun valueAs(target: KUnitTarget): Double

    /**
     * Formats [value] using the given single [target] unit instead of the base unit, e.g. `"5.0 mi"`
     * or `"2.0 h"`. See [valueAs] for the matching rules and errors.
     *
     * @throws IllegalStateException under the same conditions as [valueAs].
     */
    fun toString(target: KUnitTarget): String

    /**
     * Adds another value of the **same** unit type, automatically converting between different units
     * of the group (both operands are normalized to the group's base unit internally). The result is
     * again of type [SELF].
     *
     * @throws IllegalStateException if the two operands do not describe the same physical dimension
     * (e.g. adding an area to a plain length - same group but different exponent).
     *
     * Example:
     * ```kotlin
     * ((1 kilo meters) + 500.meters).value // 1500.0
     * ```
     */
    operator fun plus(other: SELF): SELF

    /**
     * Subtracts another value of the same unit type. See [plus] for the automatic conversion and the
     * same-dimension rule.
     *
     * @throws IllegalStateException under the same conditions as [plus].
     */
    operator fun minus(other: SELF): SELF

    /**
     * Compares two values of the same unit type by their normalized [value].
     *
     * @throws IllegalStateException if the two operands do not describe the same physical dimension
     * (same group but different exponent) - comparing unrelated magnitudes must fail rather than
     * silently produce a result.
     *
     * Example:
     * ```kotlin
     * (1 kilo meters) > 500.meters // true
     * ```
     */
    operator fun compareTo(other: SELF): Int
}
