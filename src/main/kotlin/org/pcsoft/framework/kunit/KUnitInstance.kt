package org.pcsoft.framework.kunit

/**
 * One (unit, exponent) pair inside a [KUnitInstance].
 *
 * Per the project convention, a positive exponent conceptually represents the denominator of a
 * physical formula and a negative exponent the numerator; in practice, what matters is that
 * exponents combine consistently through [KUnitInstance.times]/[KUnitInstance.div] (addition /
 * subtraction of exponents), which is what all arithmetic in this library relies on.
 *
 * Example: a pure length value (e.g. `5.meters()`) is represented internally as a single
 * `KUnitTerm(LengthUnit.METER, 1)`. Multiplying two lengths together (`5.meters() * 3.meters()`)
 * yields a single term `KUnitTerm(LengthUnit.METER, 2)` (an area, in square meters).
 */
data class KUnitTerm(val unit: KUnit, val exponent: Int)

/**
 * Represents a "mixed unit" (Mischeinheit): a numeric [value] together with one or more [units]
 * ([KUnitTerm]s) describing its physical dimension, e.g. "10.0 m/s" (a length term and a time term).
 *
 * [KUnitInstance] is the generic engine underlying every "pure" unit wrapper class (e.g.
 * `LengthUnitInstance`); those wrappers always normalize their [value] to their group's base unit,
 * but [KUnitInstance] itself performs **no** normalization - [value] is only meaningful together
 * with its exact [units], in the scale they were constructed with.
 *
 * Example:
 * ```kotlin
 * val speed = 10.meters() / 2.seconds() // KUnitInstance: value = 5.0, units = [METER^1, SECOND^-1]
 * ```
 */
class KUnitInstance(value: Number, val units: List<KUnitTerm>) {

    /**
     * The normalized value, always stored/exposed as [Double] regardless of the [Number] type
     * passed to the constructor (e.g. `KUnitInstance(5, ...)` and `KUnitInstance(5.0, ...)` produce
     * the same [value]).
     */
    val value: Double = value.toDouble()

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
     * val a = 5.meters().toKUnitInstance()       // value=5.0, units=[METER^1]
     * val b = 3.meters().toKUnitInstance()       // value=3.0, units=[METER^1]
     * (a * b).value                              // 15.0
     * (a * b).units                              // [METER^2]
     *
     * val speed = 10.meters() / 2.seconds()      // units=[METER^1, SECOND^-1]
     * val time = 4.seconds().toKUnitInstance()   // units=[SECOND^1]
     * (speed.toKUnitInstance() * time).units     // [METER^1] (SECOND^-1 + SECOND^1 = SECOND^0, removed)
     * ```
     */
    operator fun times(other: KUnitInstance): KUnitInstance =
        KUnitInstance(value * other.value, combineUnits(units, other.units, +1))

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
     * val distance = 10.meters().toKUnitInstance() // units=[METER^1]
     * val time = 2.seconds().toKUnitInstance()     // units=[SECOND^1]
     * val speed = distance / time                  // value=5.0, units=[METER^1, SECOND^-1]
     * ```
     */
    operator fun div(other: KUnitInstance): KUnitInstance =
        KUnitInstance(value / other.value, combineUnits(units, other.units, -1))

    /**
     * Adds two mixed units.
     *
     * Only allowed when both instances have exactly the same [units] (same [KUnit]s with the same
     * exponents, order-independent) - see [hasSameUnits]. This is a stricter rule than the one used
     * by "pure" unit wrapper classes (e.g. `LengthUnitInstance`), which additionally allow automatic
     * conversion between different units of the same group.
     *
     * @throws IllegalStateException if `this` and [other] do not have the same [units].
     *
     * Example:
     * ```kotlin
     * val a = KUnitInstance(5.0, listOf(KUnitTerm(LengthUnit.METER, 1)))
     * val b = KUnitInstance(3.0, listOf(KUnitTerm(LengthUnit.METER, 1)))
     * (a + b).value // 8.0
     *
     * val c = KUnitInstance(3.0, listOf(KUnitTerm(LengthUnit.MILE, 1)))
     * a + c // throws IllegalStateException: different KUnit (METER vs MILE)
     * ```
     */
    operator fun plus(other: KUnitInstance): KUnitInstance {
        check(hasSameUnits(other)) { "Cannot add KUnitInstance with different units: $units vs ${other.units}" }
        return KUnitInstance(value + other.value, units)
    }

    /**
     * Subtracts two mixed units. Same rules as [plus].
     *
     * @throws IllegalStateException if `this` and [other] do not have the same [units].
     */
    operator fun minus(other: KUnitInstance): KUnitInstance {
        check(hasSameUnits(other)) { "Cannot subtract KUnitInstance with different units: $units vs ${other.units}" }
        return KUnitInstance(value - other.value, units)
    }

    /**
     * Checks whether `this` and [other] describe exactly the same physical dimension: the same set
     * of [KUnit]s, each with the same exponent, independent of the order in which they appear in
     * [units].
     *
     * Example:
     * ```kotlin
     * val a = KUnitInstance(5.0, listOf(KUnitTerm(LengthUnit.METER, 1), KUnitTerm(TimeUnit.SECOND, -1)))
     * val b = KUnitInstance(9.0, listOf(KUnitTerm(TimeUnit.SECOND, -1), KUnitTerm(LengthUnit.METER, 1)))
     * a.hasSameUnits(b) // true, even though value and term order differ
     * ```
     */
    fun hasSameUnits(other: KUnitInstance): Boolean = unitSignature() == other.unitSignature()

    private fun unitSignature(): Map<KUnit, Int> = units.associate { it.unit to it.exponent }

    /**
     * Converts [value] as if each of [units] were expressed in the given [targets] instead of their
     * own [KUnit].
     *
     * Each target must match exactly one term in [units] by unit group (i.e. the runtime type of
     * the term's [KUnit], e.g. all `LengthUnit` values belong to the same group) and, for
     * [KDerivedUnit]/[KScaledDerivedUnit] targets, by exponent as well; every term must have exactly
     * one matching target and vice versa (the number of [targets] must equal `units.size`).
     *
     * @throws IllegalStateException if the number of [targets] does not match `units.size`, if a
     * term has no matching target, or if a target does not match any (remaining) term.
     *
     * Example:
     * ```kotlin
     * val speed = 10.meters() / 1.seconds() // KUnitInstance: value=10.0, units=[METER^1, SECOND^-1]
     * speed.valueAs(KUnitPrefix.KILO with LengthUnit.METER, TimeUnit.HOUR) // 36.0 (km/h)
     *
     * val area = (200.meters() * 50.meters()) // units=[METER^2]
     * area.valueAs(LengthDerivedUnit.HECTARE)  // 1.0
     * ```
     */
    fun valueAs(vararg targets: KUnitTarget): Double {
        val matches = matchTargets(targets)
        var divisor = 1.0
        for ((term, resolved) in matches) {
            // For a plain per-dimension unit, baseValue scales linearly per unit of exponent (e.g. km²
            // needs baseValue^2). For a unit already bound to a specific exponent (derived units, e.g.
            // hectare), baseValue already represents the full, exponentiated conversion factor - only
            // its sign (numerator vs. denominator) still needs to be respected.
            val power = if (resolved.isLinearPerDimension) term.exponent.toDouble() else if (term.exponent >= 0) 1.0 else -1.0
            divisor *= Math.pow(resolved.baseValue, power)
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
     * (10.meters() / 2.seconds()).toString() // "5.0 m*s^-1"
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
     * val speed = 10.meters() / 1.seconds()
     * speed.toString(KUnitPrefix.KILO with LengthUnit.METER, TimeUnit.HOUR) // "36.0 km*h^-1"
     * ```
     */
    fun toString(vararg targets: KUnitTarget): String {
        val matches = matchTargets(targets)
        val convertedValue = valueAs(*targets)
        val symbolPart = units.joinToString("*") { term ->
            val resolved = matches.getValue(term)
            resolved.symbol + if (term.exponent != 1) "^${term.exponent}" else ""
        }
        return "$convertedValue $symbolPart"
    }

    override fun equals(other: Any?): Boolean =
        other is KUnitInstance && value == other.value && hasSameUnits(other)

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
