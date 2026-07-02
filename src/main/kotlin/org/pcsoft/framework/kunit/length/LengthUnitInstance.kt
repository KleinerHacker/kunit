package org.pcsoft.framework.kunit.length

import org.pcsoft.framework.kunit.KScaledUnit
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitTarget
import org.pcsoft.framework.kunit.KUnitTerm

/**
 * Wraps a [KUnitInstance] representing a pure length value, always normalized internally to
 * [LengthUnit.BASE] (meter), regardless of which unit it was constructed with.
 *
 * Instances are created via the extension functions in `LengthUnitExtensions.kt`
 * (e.g. `5.meters()`, `3 kilo meters`), not directly.
 *
 * Example:
 * ```kotlin
 * val d = 5.miles()
 * d.value             // 8046.72 (normalized to meters)
 * d.valueIn(LengthUnit.MILE) // 5.0 (read back in miles)
 * ```
 */
class LengthUnitInstance internal constructor(internal val instance: KUnitInstance) {

    /** Value expressed in the base unit ([LengthUnit.BASE], meter). */
    val value: Double get() = instance.value

    /**
     * Converts [value] into the given unit or prefixed unit.
     *
     * @throws IllegalStateException if [target] is not a [LengthUnit] or a [KScaledUnit] of a
     * [LengthUnit].
     *
     * Example:
     * ```kotlin
     * val d = 5.miles()
     * d.valueIn(LengthUnit.MILE)                        // 5.0
     * d.valueIn(KUnitPrefix.KILO with LengthUnit.METER) // 8.04672 (km)
     * ```
     */
    fun valueIn(target: KUnitTarget): Double = value / resolve(target).first

    /**
     * Adds two length values, automatically converting between different [LengthUnit]s since both
     * operands are always normalized to [LengthUnit.BASE] internally. Always allowed (both operands
     * are guaranteed to share the same, single-term dimension).
     *
     * Example:
     * ```kotlin
     * (1.kilometers() + 500.meters()).value // 1500.0
     * ```
     */
    operator fun plus(other: LengthUnitInstance): LengthUnitInstance = LengthUnitInstance(instance + other.instance)

    /** Subtracts two length values. See [plus] for the automatic unit conversion. */
    operator fun minus(other: LengthUnitInstance): LengthUnitInstance = LengthUnitInstance(instance - other.instance)

    /**
     * Multiplies two length values, producing an area as a generic [KUnitInstance] (no longer a
     * "pure" length).
     *
     * Example:
     * ```kotlin
     * val area = 200.meters() * 50.meters() // KUnitInstance: value=10000.0, units=[METER^2]
     * ```
     */
    operator fun times(other: LengthUnitInstance): KUnitInstance = instance * other.instance

    /** Divides two length values, producing a [KUnitInstance] with a `LengthUnit^0` (dimensionless) term (removed automatically, see [KUnitInstance.div]). */
    operator fun div(other: LengthUnitInstance): KUnitInstance = instance / other.instance

    /** Multiplies this length value with an arbitrary mixed unit, producing a new [KUnitInstance]. */
    operator fun times(other: KUnitInstance): KUnitInstance = instance * other

    /** Divides this length value by an arbitrary mixed unit, producing a new [KUnitInstance]. */
    operator fun div(other: KUnitInstance): KUnitInstance = instance / other

    /** Compares two length values by their normalized [value]. */
    operator fun compareTo(other: LengthUnitInstance): Int = value.compareTo(other.value)

    /** Structural equality by normalized [value] (both operands are always normalized to the same base unit). */
    override fun equals(other: Any?): Boolean = other is LengthUnitInstance && value == other.value

    override fun hashCode(): Int = value.hashCode()

    /**
     * Base-unit representation, e.g. `"5.0 m"`.
     */
    override fun toString(): String = "$value ${LengthUnit.BASE.symbol}"

    /**
     * Representation in the given unit or prefixed unit.
     *
     * @throws IllegalStateException if [target] is not a [LengthUnit] or a [KScaledUnit] of a
     * [LengthUnit].
     *
     * Example:
     * ```kotlin
     * 5.miles().toString(LengthUnit.MILE)                        // "5.0 mi"
     * 5.miles().toString(KUnitPrefix.KILO with LengthUnit.METER) // "8.04672 km"
     * ```
     */
    fun toString(target: KUnitTarget): String {
        val (baseValue, symbol) = resolve(target)
        return "${value / baseValue} $symbol"
    }

    /** The underlying generic [KUnitInstance] representation (single term: [LengthUnit.BASE]^1). */
    fun toKUnitInstance(): KUnitInstance = instance

    private fun resolve(target: KUnitTarget): Pair<Double, String> = when (target) {
        is LengthUnit -> target.baseValue to target.symbol
        is KScaledUnit -> {
            check(target.unit is LengthUnit) { "Target unit ${target.unit} does not belong to the length group" }
            target.baseValue to target.symbol
        }

        else -> error("Unsupported target for LengthUnitInstance: $target (expected a LengthUnit or a KScaledUnit<LengthUnit>)")
    }
}

/**
 * Converts this mixed unit to a pure length value.
 *
 * @throws IllegalStateException if this instance does not consist of exactly the length base unit
 * with exponent 1 (i.e. it is not a pure length value).
 *
 * Example:
 * ```kotlin
 * val speed = 10.meters() / 2.seconds()
 * val distance = speed.toKUnitInstance() * 2.seconds() // units=[METER^1]
 * distance.toLengthUnit().value // 10.0
 *
 * speed.toKUnitInstance().toLengthUnit() // throws IllegalStateException (not a pure length)
 * ```
 */
fun KUnitInstance.toLengthUnit(): LengthUnitInstance {
    val term = units.singleOrNull()
    check(term != null && term.unit == LengthUnit.BASE && term.exponent == 1) {
        "KUnitInstance $this does not represent a pure length value (expected exactly one term: ${LengthUnit.BASE}^1)"
    }
    return LengthUnitInstance(this)
}

internal fun lengthUnitInstanceOf(value: Double): LengthUnitInstance =
    LengthUnitInstance(KUnitInstance(value, listOf(KUnitTerm(LengthUnit.BASE, 1))))
