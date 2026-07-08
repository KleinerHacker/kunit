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

    /**
     * Returns a **new** measure of the very same physical dimension (same runtime type, same unit
     * terms) whose [value] is scaled by [factor]. This is the single primitive behind number-times-unit
     * construction ([of]): a value-1 unit template (e.g. `kilo.meters`, `meters / seconds`) is scaled by
     * the leading number.
     *
     * Every concrete measure returns **its own** type (e.g. `KLengthUnitInstance.scaledBy` yields a
     * `KLengthUnitInstance`), which is what lets [of] preserve the strong result type. The immutability
     * invariant holds: the receiver is never modified.
     */
    fun scaledBy(factor: Double): KUnitMeasurable
}

/**
 * Builds a concrete unit value by scaling a **value-1 unit template** by this number: the one and only
 * construction verb of the library. The template is any [KUnitMeasurable] describing the desired unit -
 * a bare token (`meters`, `seconds`, `bytes`), a prefixed token from a builder (`kilo.meters`,
 * `milli.seconds`), a named special unit (`hectare`, `liter`), or an expression combining them with
 * `*`/`/`/`pow` (`kilo.meters / milli.seconds`, `meters * (milli.seconds pow 2)`).
 *
 * The **strong type is preserved**: `10.5 of kilo.meters` is a `KLengthUnitInstance`,
 * `10.5 of kilo.meters / milli.seconds` a `KSpeedUnitInstance`, and a genuinely composite template a
 * [KMixedUnitInstance].
 *
 * Precedence note: `of` is a named infix function and binds **weaker** than `* /`, so
 * `10.5 of kilo.meters / milli.seconds` reads as `10.5 of (kilo.meters / milli.seconds)`. Since `pow`
 * is weaker than `* /` too, parenthesize a powered denominator: `10 of meters * (milli.seconds pow 2)`.
 *
 * Example:
 * ```kotlin
 * 10.5 of meters                      // KLengthUnitInstance, 10.5 m
 * 10.5 of kilo.meters                 // KLengthUnitInstance, 10500 m
 * 10.5 of kilo.meters / milli.seconds // KSpeedUnitInstance
 * 2 of hectare                        // KAreaUnitInstance
 * ```
 */
@Suppress("UNCHECKED_CAST")
infix fun <T : KUnitMeasurable> Number.of(template: T): T = template.scaledBy(this.toDouble()) as T

/**
 * Reads this value in the unit described by [target] (a value-1 unit template), returning the plain
 * [Double] count - the one and only reading verb of the library (there is no `valueAs`).
 *
 * Both sides are compared by their normalized unit signature (same unit groups, same exponents,
 * order-independent); the result is simply `this.value / target.value`, because both values are
 * normalized to their groups' base units. The scale of the requested unit is carried by the template's
 * own [value] (e.g. `kilo.meters.value == 1000.0`), so `v into kilo.meters` yields kilometers while
 * `v into meters` yields meters.
 *
 * @throws IllegalStateException if [target] does not describe the same physical dimension as this value.
 *
 * Example:
 * ```kotlin
 * val v = 10500.0 of meters
 * v into kilo.meters                 // 10.5
 * v into meters                      // 10500.0
 * (2 of hectare) into (meters * meters) // 20000.0
 * ```
 */
infix fun KUnitMeasurable.into(target: KUnitMeasurable): Double {
    val self = toUnit()
    val unit = target.toUnit()
    check(self.hasSameUnits(unit)) {
        "Cannot read $self in unit $unit: incompatible physical dimensions"
    }
    return self.value / unit.value
}

/**
 * The common abstraction of every "pure" unit wrapper (e.g. `KLengthUnitInstance`,
 * `KTimeUnitInstance`): a value that belongs to exactly one unit group and therefore supports
 * same-group, same-exponent arithmetic and comparison against **its own type**. Construction and
 * reading are the group-agnostic [of]/[into] verbs, so no group-specific conversion members live here.
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

/**
 * Multiplies this "pure" unit value with **any** other "pure" unit value - across group boundaries -
 * producing a new [KMixedUnitInstance] whose unit terms are the union of both sides' terms with their
 * exponents added (a resulting exponent of `0` drops that unit). Always allowed.
 *
 * This is the group-agnostic catch-all that lets any two wrappers be combined directly, without first
 * normalizing them via [toUnit]:
 * ```kotlin
 * 20.bytes * 20.seconds   // KMixedUnitInstance [BYTE^1, SECOND^1]
 * ```
 *
 * It is deliberately an **extension**, not a member of [KUnitInstance]/[KUnitMeasurable]: a member
 * would (by Kotlin's "member always wins" rule) shadow the more specific, statically-typed cross-group
 * operators such as `KLengthUnitInstance.times(...)`, degrading their result to [KMixedUnitInstance].
 * As an extension it only applies when no more specific member or extension matches, so typed results
 * are preserved:
 * ```kotlin
 * 3.meters * 4.meters     // KAreaUnitInstance   (member wins)
 * ```
 */
operator fun KUnitInstance<*>.times(other: KUnitInstance<*>): KMixedUnitInstance =
    toUnit() * other.toUnit()

/**
 * Divides this "pure" unit value by **any** other "pure" unit value - across group boundaries -
 * producing a new [KMixedUnitInstance] whose unit terms are this side's terms combined with `other`'s
 * terms with their exponents negated (a resulting exponent of `0` drops that unit). Always allowed.
 *
 * The group-agnostic counterpart to [times], letting any two wrappers be combined directly:
 * ```kotlin
 * 20.bytes / 20.seconds   // KMixedUnitInstance [BYTE^1, SECOND^-1]
 * ```
 *
 * Like [times] it is deliberately an **extension** rather than a member, so it does not shadow the
 * statically-typed cross-group operators (e.g. `100.meters / 5.seconds` stays a `KSpeedUnitInstance`,
 * because that more specific extension wins over this generic one).
 */
operator fun KUnitInstance<*>.div(other: KUnitInstance<*>): KMixedUnitInstance =
    toUnit() / other.toUnit()
