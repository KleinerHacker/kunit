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
     * Converts [value] as if each of [units] were expressed in the given [targets] instead of their
     * own [KUnit].
     *
     * Each target must match exactly one term in [units] by unit group (i.e. the runtime type of
     * the term's [KUnit], e.g. all `KDistanceUnit` values belong to the same group) and, for
     * [KDerivedUnit]/[KScaledDerivedUnit] targets, by exponent as well; every term must have exactly
     * one matching target and vice versa (the number of [targets] must equal `units.size`).
     *
     * @throws IllegalStateException if the number of [targets] does not match `units.size`, if a
     * term has no matching target, or if a target does not match any (remaining) term.
     *
     * Example:
     * ```kotlin
     * val speed = 10.meters / 1.seconds // KMixedUnitInstance: value=10.0, units=[METER^1, SECOND^-1]
     * speed.valueAs(KUnitPrefix.KILO with KDistanceUnit.METER, TimeUnit.HOUR) // 36.0 (km/h)
     *
     * val area = (200.meters * 50.meters) // units=[METER^2]
     * area.valueAs(KDistanceDerivedUnit.HECTARE)  // 1.0
     * ```
     */
    fun valueAs(vararg targets: KUnitTarget): Double {
        val matches = matchTargets(targets)
        val divisor = matches.entries.fold(1.0) { acc, (term, resolved) ->
            // For a plain per-dimension unit, baseValue scales linearly per unit of exponent (e.g. km²
            // needs baseValue^2). For a unit already bound to a specific exponent (derived units, e.g.
            // hectare), baseValue already represents the full, exponentiated conversion factor - only
            // its sign (numerator vs. denominator) still needs to be respected.
            val power = if (resolved.isLinearPerDimension) term.exponent.toDouble() else if (term.exponent >= 0) 1.0 else -1.0
            acc * Math.pow(resolved.baseValue, power)
        }
        return value / divisor
    }

    /**
     * Base-unit representation of this instance, e.g. `"10.0 m/s"` for a speed built from meters and
     * seconds - each term is printed using its own [KUnit.symbol] (with `^exponent` appended when the
     * exponent is not `1`), in [units] order, joined with `"*"`. No unit conversion is performed.
     *
     * Example:
     * ```kotlin
     * (10.meters / 2.seconds).toString() // "5.0 m*s^-1"
     * ```
     */
    override fun toString(): String =
        "$value " + units.joinToString("*") { term ->
            term.unit.symbol + if (term.exponent != 1) "^${term.exponent}" else ""
        }

    /**
     * Representation of this instance using the given [targets] instead of each term's own unit,
     * e.g. `"36.0 km*h^-1"` for a speed converted to km/h. See [valueAs] for the matching rules.
     *
     * @throws IllegalStateException under the same conditions as [valueAs].
     *
     * Example:
     * ```kotlin
     * val speed = 10.meters / 1.seconds
     * speed.toString(KUnitPrefix.KILO with KDistanceUnit.METER, TimeUnit.HOUR) // "36.0 km*h^-1"
     * ```
     */
    fun toString(vararg targets: KUnitTarget): String {
        val matches = matchTargets(targets)
        val convertedValue = valueAs(*targets)
        val symbolPart = units.joinToString("*") { term ->
            val resolved = matches.getValue(term)
            // Only append the exponent suffix for "linear" targets (a plain KUnit/KScaledUnit, whose
            // symbol represents one dimension raised to the term's exponent, e.g. "km^2"). A derived
            // unit's symbol (e.g. "ha") already represents the full, exponentiated quantity, so no
            // suffix must be appended there (it would wrongly read "ha^2" instead of "ha").
            val suffix = if (resolved.isLinearPerDimension && term.exponent != 1) "^${term.exponent}" else ""
            resolved.symbol + suffix
        }
        return "$convertedValue $symbolPart"
    }

    override fun equals(other: Any?): Boolean =
        other is KMixedUnitInstance && value == other.value && hasSameUnits(other)

    override fun hashCode(): Int = value.hashCode() * 31 + unitSignature().hashCode()

    private data class ResolvedTarget(
        val groupClass: Class<out KUnit>,
        val exponent: Int?,
        val baseValue: Double,
        val symbol: String,
        /**
         * `true` for a plain per-dimension unit ([KUnit]/[KScaledUnit]), whose [baseValue] must be
         * raised to the term's exponent to convert a whole term (e.g. km² needs `1000^2`); `false`
         * for a unit already bound to a specific exponent ([KDerivedUnit]/[KScaledDerivedUnit], e.g.
         * hectare), whose [baseValue] already represents the full, exponentiated conversion factor.
         */
        val isLinearPerDimension: Boolean
    )

    private fun resolve(target: KUnitTarget): ResolvedTarget = when (target) {
        is KScaledDerivedUnit<*> -> ResolvedTarget(target.referenceUnit.javaClass, target.exponent, target.baseValue, target.symbol, isLinearPerDimension = false)
        is KDerivedUnit<*> -> ResolvedTarget(target.referenceUnit.javaClass, target.exponent, target.baseValue, target.symbol, isLinearPerDimension = false)
        is KScaledUnit -> ResolvedTarget(target.unit.javaClass, null, target.baseValue, target.symbol, isLinearPerDimension = true)
        is KUnit -> ResolvedTarget(target.javaClass, null, target.baseValue, target.symbol, isLinearPerDimension = true)
        else -> error("Unsupported KUnitTarget implementation: $target")
    }

    private fun matchTargets(targets: Array<out KUnitTarget>): Map<KUnitTerm, ResolvedTarget> {
        check(targets.size == units.size) {
            "Expected ${units.size} target(s) for units $units, but got ${targets.size}"
        }
        val remaining = targets.map(::resolve).toMutableList()
        val result = LinkedHashMap<KUnitTerm, ResolvedTarget>()
        for (term in units) {
            val index = remaining.indexOfFirst {
                it.groupClass == term.unit.javaClass && (it.exponent == null || it.exponent == term.exponent)
            }
            check(index >= 0) { "No target matches term $term" }
            result[term] = remaining.removeAt(index)
        }
        return result
    }
}

private fun combineUnits(a: List<KUnitTerm>, b: List<KUnitTerm>, sign: Int): List<KUnitTerm> {
    val exponents = LinkedHashMap<KUnit, Int>()

    for (term in a) exponents[term.unit] = (exponents[term.unit] ?: 0) + term.exponent
    for (term in b) exponents[term.unit] = (exponents[term.unit] ?: 0) + sign * term.exponent

    return exponents.filterValues { it != 0 }.map { (unit, exponent) -> KUnitTerm(unit, exponent) }
}
