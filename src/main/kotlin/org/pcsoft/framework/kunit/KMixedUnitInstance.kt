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
 * One (unit, exponent) pair inside a [KMixedUnitInstance].
 *
 * Per the project convention, a positive exponent conceptually represents the denominator of a
 * physical formula and a negative exponent the numerator; in practice, what matters is that
 * exponents combine consistently through [KMixedUnitInstance.times]/[KMixedUnitInstance.div] (addition /
 * subtraction of exponents), which is what all arithmetic in this library relies on.
 *
 * Example: a pure length value (e.g. `5.meters`) is represented internally as a single
 * `KUnitTerm(KDistanceUnit.METER, 1)`. Multiplying two lengths together (`5.meters * 3.meters`)
 * yields a single term `KUnitTerm(KDistanceUnit.METER, 2)` (an area, in square meters).
 */
data class KUnitTerm(val unit: KUnit, val exponent: Int)

/**
 * Represents a "mixed unit" (Mischeinheit): a numeric [value] together with one or more [units]
 * ([KUnitTerm]s) describing its physical dimension, e.g. "10.0 m/s" (a length term and a time term).
 *
 * [KMixedUnitInstance] is the generic engine underlying every "pure" unit wrapper class (e.g.
 * `KLengthUnitInstance`); those wrappers always normalize their [value] to their group's base unit,
 * but [KMixedUnitInstance] itself performs **no** normalization - [value] is only meaningful together
 * with its exact [units], in the scale they were constructed with.
 *
 * Example:
 * ```kotlin
 * val speed = 10.meters / 2.seconds // KMixedUnitInstance: value = 5.0, units = [METER^1, SECOND^-1]
 * ```
 */
class KMixedUnitInstance internal constructor(value: Number, val units: List<KUnitTerm>) : KUnitMeasurable {

    /**
     * The normalized value, always stored/exposed as [Double] regardless of the [Number] type
     * passed to the constructor (e.g. `KMixedUnitInstance(5, ...)` and `KMixedUnitInstance(5.0, ...)` produce
     * the same [value]).
     */
    override val value: Double = value.toDouble()

    /**
     * Returns `this`. A [KMixedUnitInstance] is already the generic mixed-unit representation, so the
     * [KUnitMeasurable] conversion is the identity here (it is the "pure" unit wrappers that produce a
     * genuinely new [KMixedUnitInstance] from their internal state).
     */
    override fun toUnit(): KMixedUnitInstance = this

    /**
     * Returns a new [KMixedUnitInstance] with the same [units] and [value] scaled by [factor] - the
     * scaling primitive behind number-times-unit construction (see [of]).
     */
    override fun scaledBy(factor: Double): KMixedUnitInstance = KMixedUnitInstance(value * factor, units)

    /**
     * Linear reading hook behind [into]: reads a base-normalized [baseValue] into this template's scale
     * as `baseValue / value`. Every "pure" wrapper inherits this via `by` delegation; only groups with a
     * non-linear (e.g. affine temperature) conversion override it.
     */
    override fun readBaseValue(baseValue: Double): Double = baseValue / value

    /**
     * Multiplies two mixed units. Always allowed.
     *
     * The resulting [value] is `this.value * other.value`. The resulting [units] are computed by
     * adding exponents of matching [KUnit]s (exact match, i.e. the very same [KUnit] instance/enum
     * value) and simply including non-matching units from either side unchanged; a resulting
     * exponent of `0` removes that unit from the result entirely.
     *
     * Example:
     * ```kotlin
     * val a = 5.meters.toUnit()       // value=5.0, units=[METER^1]
     * val b = 3.meters.toUnit()       // value=3.0, units=[METER^1]
     * (a * b).value                              // 15.0
     * (a * b).units                              // [METER^2]
     *
     * val speed = 10.meters / 2.seconds      // units=[METER^1, SECOND^-1]
     * val time = 4.seconds.toUnit()   // units=[SECOND^1]
     * (speed.toUnit() * time).units     // [METER^1] (SECOND^-1 + SECOND^1 = SECOND^0, removed)
     * ```
     */
    override operator fun times(other: KMixedUnitInstance): KMixedUnitInstance =
        KMixedUnitInstance(value * other.value, combineUnits(units, other.units, +1))

    /**
     * Divides two mixed units. Always allowed.
     *
     * The resulting [value] is `this.value / other.value`. The resulting [units] are computed by
     * subtracting `other`'s exponents from matching [KUnit]s (exact match) and including
     * non-matching units from `other` with their exponent negated; a resulting exponent of `0`
     * removes that unit from the result entirely.
     *
     * Example:
     * ```kotlin
     * val distance = 10.meters.toUnit() // units=[METER^1]
     * val time = 2.seconds.toUnit()     // units=[SECOND^1]
     * val speed = distance / time                  // value=5.0, units=[METER^1, SECOND^-1]
     * ```
     */
    override operator fun div(other: KMixedUnitInstance): KMixedUnitInstance =
        KMixedUnitInstance(value / other.value, combineUnits(units, other.units, -1))

    /**
     * Raises this mixed unit to the integer power [n]. Always allowed.
     *
     * The resulting [value] is `value.pow(n)` and **every** [KUnitTerm]'s exponent is multiplied by
     * [n]; any term whose resulting exponent becomes `0` is dropped. This is the single, group-agnostic
     * exponentiation form of the library - there are no named `squareXxx`/`cubicXxx` constructors.
     * (`^`/`^=` cannot be overloaded in Kotlin, hence the infix `pow`.)
     *
     * Precedence note: `pow` is a named infix function and therefore binds **weaker** than the
     * arithmetic operators `* / + -`. In mixed expressions, parenthesize accordingly
     * (e.g. `(a * b) pow 2`).
     *
     * @param n the integer exponent. `n == 0` yields a dimensionless result (value `1.0`, no units);
     * negative `n` inverts the dimension (e.g. `m` becomes `m^-n`).
     *
     * Example:
     * ```kotlin
     * val length = 2.meters.toUnit()  // value=2.0, units=[METER^1]
     * (length pow 2).value                       // 4.0
     * (length pow 2).units                       // [METER^2]  ((2 m)² = 4 m²)
     * (length pow 2 pow 2).units                 // [METER^4]  ((2 m²)² = 4 m⁴)
     * (length pow 0).units                       // [] (dimensionless, value 1.0)
     * ```
     */
    infix fun pow(n: Int): KMixedUnitInstance {
        if (n == 0) return KMixedUnitInstance(1.0, emptyList())
        val poweredUnits = units
            .map { it.copy(exponent = it.exponent * n) }
            .filter { it.exponent != 0 }
        return KMixedUnitInstance(Math.pow(value, n.toDouble()), poweredUnits)
    }

    /**
     * Adds two mixed units.
     *
     * Allowed as long as `this` and [other] describe the same physical dimension: for every term in
     * [units] there must be exactly one term in `other.units` belonging to the same unit group (i.e.
     * the same runtime [KUnit] type, e.g. all `KDistanceUnit` values) with the same exponent, and vice
     * versa. Matching terms do **not** need to be the exact same [KUnit] - [other]'s value is
     * automatically converted into `this`'s units first, using each matched pair's [KUnit.baseValue]
     * ratio (analogous to the automatic conversion performed by "pure" unit wrapper classes like
     * `KLengthUnitInstance`). The result is expressed in `this`'s [units].
     *
     * @throws IllegalStateException if `this` and [other] do not describe the same physical dimension
     * (different unit groups, or different exponents for a matching group).
     *
     * Example:
     * ```kotlin
     * val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))
     * val b = KMixedUnitInstance(3.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))
     * (a + b).value // 8.0
     *
     * val c = KMixedUnitInstance(3.0, listOf(KUnitTerm(KDistanceUnit.MILE, 1)))
     * (a + c).value // 4832.032 (3 miles converted to meters, then added), units=[METER^1]
     *
     * val area = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.METER, 2)))
     * val length = KMixedUnitInstance(3.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))
     * area + length // throws IllegalStateException: different exponents (2 vs 1)
     * ```
     */
    operator fun plus(other: KMixedUnitInstance): KMixedUnitInstance {
        val convertedOtherValue = other.value * conversionFactorTo(other)
        return KMixedUnitInstance(value + convertedOtherValue, units)
    }

    /**
     * Subtracts two mixed units. Same rules as [plus].
     *
     * @throws IllegalStateException if `this` and [other] do not describe the same physical dimension.
     */
    operator fun minus(other: KMixedUnitInstance): KMixedUnitInstance {
        val convertedOtherValue = other.value * conversionFactorTo(other)
        return KMixedUnitInstance(value - convertedOtherValue, units)
    }

    /**
     * Matches each term in [units] to exactly one term in `other.units` of the same unit group (i.e.
     * the same runtime [KUnit] type) and exponent, then computes the factor that converts a value
     * expressed in `other`'s units into a value expressed in `this`'s units.
     *
     * @throws IllegalStateException if `this` and [other] do not have the same number of terms, or a
     * term has no group-and-exponent match in the other's [units].
     */
    private fun conversionFactorTo(other: KMixedUnitInstance): Double {
        check(units.size == other.units.size) {
            "Cannot combine KMixedUnitInstance with different dimensions: $units vs ${other.units}"
        }
        return units.fold(other.units to 1.0) { (remaining, factor), term ->
            val index = remaining.indexOfFirst { it.unit.javaClass == term.unit.javaClass && it.exponent == term.exponent }
            check(index >= 0) {
                "Cannot combine KMixedUnitInstance with different dimensions: $units vs ${other.units}"
            }
            val matched = remaining[index]
            val nextFactor = factor * Math.pow(matched.unit.baseValue / term.unit.baseValue, term.exponent.toDouble())
            remaining.filterIndexed { i, _ -> i != index } to nextFactor
        }.second
    }

    /**
     * Checks whether `this` and [other] describe exactly the same physical dimension: the same set
     * of [KUnit]s, each with the same exponent, independent of the order in which they appear in
     * [units].
     *
     * Example:
     * ```kotlin
     * val a = KMixedUnitInstance(5.0, listOf(KUnitTerm(KDistanceUnit.METER, 1), KUnitTerm(TimeUnit.SECOND, -1)))
     * val b = KMixedUnitInstance(9.0, listOf(KUnitTerm(TimeUnit.SECOND, -1), KUnitTerm(KDistanceUnit.METER, 1)))
     * a.hasSameUnits(b) // true, even though value and term order differ
     * ```
     */
    fun hasSameUnits(other: KMixedUnitInstance): Boolean = unitSignature() == other.unitSignature()

    private fun unitSignature(): Map<KUnit, Int> = units.associate { it.unit to it.exponent }

    /**
     * Base-unit representation of this instance, e.g. `"10.0 m/s"` for a speed built from meters and
     * seconds - each term is printed using its own [KUnit.symbol] (with `^exponent` appended when the
     * exponent is not `1`), in [units] order, joined with `"*"`. No unit conversion is performed;
     * to read a value in a specific unit use [into].
     *
     * Example:
     * ```kotlin
     * (10 of meters / 2.let { 2 of seconds }).toString() // "5.0 m*s^-1"
     * ```
     */
    override fun toString(): String =
        "$value " + units.joinToString("*") { term ->
            term.unit.symbol + if (term.exponent != 1) "^${term.exponent}" else ""
        }

    override fun equals(other: Any?): Boolean =
        other is KMixedUnitInstance && value == other.value && hasSameUnits(other)

    override fun hashCode(): Int = value.hashCode() * 31 + unitSignature().hashCode()
}

/**
 * Raises **any** measurable value to the integer power [n], producing a generic [KMixedUnitInstance].
 *
 * This is the group-agnostic entry point that makes `pow` available on every "pure" wrapper of every unit
 * group (time, speed, and any future group): it simply normalizes the receiver via [KUnitMeasurable.toUnit]
 * and applies [KMixedUnitInstance.pow]. Groups that offer a dimensioned result declare a **more specific**
 * `pow` extension on their own type (e.g. `KDistanceUnitInstance.pow`, which returns a
 * `KDistanceUnitInstance`); Kotlin's overload resolution then prefers that narrower extension, so this one
 * only applies where no typed variant exists.
 *
 * Example:
 * ```kotlin
 * val t2 = 2.hours pow 2   // KMixedUnitInstance: value 7200² (base seconds squared), units [s²]
 * ```
 */
infix fun KUnitMeasurable.pow(n: Int): KMixedUnitInstance = toUnit() pow n

private fun combineUnits(a: List<KUnitTerm>, b: List<KUnitTerm>, sign: Int): List<KUnitTerm> {
    val exponents = LinkedHashMap<KUnit, Int>()

    // A single mixed unit never carries the same KUnit twice, so `a`'s terms are inserted directly.
    for (term in a) exponents[term.unit] = term.exponent
    for (term in b) exponents[term.unit] = (exponents[term.unit] ?: 0) + sign * term.exponent

    return exponents.filterValues { it != 0 }.map { (unit, exponent) -> KUnitTerm(unit, exponent) }
}
