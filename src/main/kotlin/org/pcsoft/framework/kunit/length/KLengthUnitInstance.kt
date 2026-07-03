package org.pcsoft.framework.kunit.length

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTarget
import org.pcsoft.framework.kunit.KUnitTerm

/**
 * Wraps a [KMixedUnitInstance] with exactly one term of [KLengthUnit.BASE] (meter), at any exponent -
 * exponent 1 for a "pure" length, 2 for an area, 3 for a volume, etc. The value is always normalized
 * internally to [KLengthUnit.BASE], regardless of which unit or [org.pcsoft.framework.kunit.KUnitPrefix]
 * it was constructed with.
 *
 * Instances are created via the extension functions in `KLengthUnitExtensions.kt`
 * (e.g. `5.meters()`, `5.hectares()`) or the SI-prefix `infix` constructors in `KLengthUnitPrefix.kt`
 * (e.g. `3 kilo meters`).
 *
 * Example:
 * ```kotlin
 * val d = 5.miles()
 * d.value             // 8046.72 (normalized to meters)
 * d.valueAs(KLengthUnit.MILE) // 5.0 (read back in miles)
 *
 * val area = 5.hectares()
 * area.value          // 50000.0 (normalized to square meters)
 * ```
 */
class KLengthUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitInstance<KLengthUnitInstance>, KUnitMeasurable by instance {

    /** The single term's exponent (1 for a pure length, 2 for an area, 3 for a volume, ...). */
    private val exponent: Int get() = instance.units.single().exponent

    /**
     * Converts [value] into the given unit, prefixed unit, or (for the matching exponent) derived
     * unit - see [KMixedUnitInstance.valueAs] for the exact matching rules.
     *
     * @throws IllegalStateException if [target] does not belong to the length group, or (for a
     * [org.pcsoft.framework.kunit.KDerivedUnit]/[org.pcsoft.framework.kunit.KScaledDerivedUnit]
     * target) if its exponent does not match [exponent].
     *
     * Example:
     * ```kotlin
     * val d = 5.miles()
     * d.valueAs(KLengthUnit.MILE)                        // 5.0
     * d.valueAs(KUnitPrefix.KILO with KLengthUnit.METER) // 8.04672 (km)
     *
     * val area = 5.hectares()
     * area.valueAs(KLengthDerivedUnit.HECTARE) // 5.0
     * ```
     */
    override fun valueAs(target: KUnitTarget): Double = instance.valueAs(target)

    /**
     * Adds two values of the same physical dimension, automatically converting between different
     * [KLengthUnit]s since both operands are always normalized to [KLengthUnit.BASE] internally.
     *
     * @throws IllegalStateException if the two operands have different [exponent]s (e.g. adding an
     * area to a plain length), since they are then not the same physical dimension - delegates to
     * [KMixedUnitInstance.plus], which enforces this.
     *
     * Example:
     * ```kotlin
     * (1.kilometers() + 500.meters()).value // 1500.0
     *
     * 5.hectares() + 5.meters() // throws IllegalStateException (area vs. length)
     * ```
     */
    override operator fun plus(other: KLengthUnitInstance): KLengthUnitInstance = KLengthUnitInstance(instance + other.instance)

    /** Subtracts two values of the same physical dimension. See [plus] for the automatic unit conversion and exponent check. */
    override operator fun minus(other: KLengthUnitInstance): KLengthUnitInstance = KLengthUnitInstance(instance - other.instance)

    /**
     * Multiplies two length-based values, producing a new [KMixedUnitInstance] whose exponent is the sum
     * of both operands' exponents (no longer necessarily a "pure" length).
     *
     * Example:
     * ```kotlin
     * val area = 200.meters() * 50.meters() // KMixedUnitInstance: value=10000.0, units=[METER^2]
     * ```
     */
    override operator fun times(other: KLengthUnitInstance): KMixedUnitInstance = instance * other.instance

    /** Divides two length-based values, producing a new [KMixedUnitInstance] whose exponent is the difference of both operands' exponents. */
    override operator fun div(other: KLengthUnitInstance): KMixedUnitInstance = instance / other.instance

    /**
     * Compares two values of the same physical dimension by their normalized [value].
     *
     * @throws IllegalStateException if the two operands have different [exponent]s (e.g. comparing
     * an area to a plain length) - per CLAUDE.md, comparing values of a different unit/exponent must
     * fail rather than silently comparing unrelated magnitudes.
     */
    override operator fun compareTo(other: KLengthUnitInstance): Int {
        check(exponent == other.exponent) { "Cannot compare KLengthUnitInstance with different exponents: $exponent vs ${other.exponent}" }
        return value.compareTo(other.value)
    }

    /**
     * Structural equality by normalized [value] (both operands are always normalized to the same base unit).
     *
     * @throws IllegalStateException if [other] is a [KLengthUnitInstance] with a different [exponent]
     * (e.g. comparing an area to a plain length) - see [compareTo].
     */
    override fun equals(other: Any?): Boolean {
        if (other !is KLengthUnitInstance) return false
        check(exponent == other.exponent) { "Cannot compare KLengthUnitInstance with different exponents: $exponent vs ${other.exponent}" }
        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()

    /**
     * Base-unit representation, e.g. `"5.0 m"` for a length, `"10000.0 m^2"` for an area.
     */
    override fun toString(): String = instance.toString()

    /**
     * Representation in the given unit, prefixed unit, or (for the matching exponent) derived unit -
     * see [valueAs] for the exact matching rules.
     *
     * @throws IllegalStateException under the same conditions as [valueAs].
     *
     * Example:
     * ```kotlin
     * 5.miles().toString(KLengthUnit.MILE)                        // "5.0 mi"
     * 5.miles().toString(KUnitPrefix.KILO with KLengthUnit.METER) // "8.04672 km"
     * 5.hectares().toString(KLengthDerivedUnit.HECTARE)           // "5.0 ha"
     * ```
     */
    override fun toString(target: KUnitTarget): String = instance.toString(target)
}

/**
 * Converts this mixed unit to a "pure" length-based value (length, area, volume, ...), as long as it
 * consists of exactly one term of any [KLengthUnit], at any exponent - normalizing it to
 * [KLengthUnit.BASE] if it isn't already (e.g. a term tagged with `KLengthUnit.MILE` is converted to
 * the equivalent value in meters). This is what lets an arbitrary single-[KLengthUnit] mixed instance
 * (whose term may be tagged with any [KLengthUnit], not necessarily the group's base unit) be
 * converted into a [KLengthUnitInstance].
 *
 * @throws IllegalStateException if this instance does not consist of exactly one term of a
 * [KLengthUnit] (i.e. it is not a pure length-based value).
 *
 * Example:
 * ```kotlin
 * val speed = 10.meters() / 2.seconds()
 * val distance = speed.toKMixedUnitInstance() * 2.seconds() // units=[METER^1]
 * distance.toKLengthUnit().value // 10.0
 *
 * val area = 200.meters() * 50.meters() // units=[METER^2]
 * area.toKLengthUnit().value // 10000.0
 *
 * (5 kilo miles).value // 5000 * 1609.344 (normalized to meters)
 *
 * speed.toKMixedUnitInstance().toKLengthUnit() // throws IllegalStateException (mixed length/time, not pure length)
 * ```
 */
fun KMixedUnitInstance.toKLengthUnit(): KLengthUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KLengthUnit) {
        "KMixedUnitInstance $this does not represent a pure length-based value (expected exactly one term of a KLengthUnit)"
    }
    val normalizedValue = value * Math.pow(unit.baseValue, term.exponent.toDouble())
    return KLengthUnitInstance(KMixedUnitInstance(normalizedValue, listOf(KUnitTerm(KLengthUnit.BASE, term.exponent))))
}

internal fun lengthUnitInstanceOf(value: Double): KLengthUnitInstance =
    KLengthUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KLengthUnit.BASE, 1))))
