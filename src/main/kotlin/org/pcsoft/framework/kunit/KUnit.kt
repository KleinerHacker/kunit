package org.pcsoft.framework.kunit

/**
 * Marker interface for anything that can be used as a display/conversion target for a unit value.
 *
 * A target is either:
 * - a plain [KUnit] (no scaling, e.g. `LengthUnit.METER`),
 * - a [KScaledUnit] (a [KUnit] combined with a [KUnitPrefix], e.g. `KUnitPrefix.KILO with LengthUnit.METER`),
 * - a [KDerivedUnit] (a named unit bound to a specific unit group and exponent, e.g. `LengthDerivedUnit.HECTARE`), or
 * - a [KScaledDerivedUnit] (a [KDerivedUnit] combined with a [KUnitPrefix], e.g. `KUnitPrefix.MILLI with LengthDerivedUnit.LITER`).
 *
 * This lets conversion/formatting functions such as `KUnitInstance.valueAs`, `KUnitInstance.toString`,
 * or `LengthUnitInstance.valueIn` accept any of these interchangeably at the same call site.
 *
 * Example:
 * ```kotlin
 * val d = 5.miles()
 * d.valueIn(LengthUnit.MILE)                       // plain KUnit target
 * d.valueIn(KUnitPrefix.KILO with LengthUnit.METER) // KScaledUnit target
 * ```
 */
interface KUnitTarget

/**
 * Represents a single physical unit belonging to a unit group (e.g. meter within the length group).
 *
 * Concrete unit groups are modeled as `enum class` implementations of this interface.
 *
 * Two [KUnit] instances are considered to belong to the same group when they share the same runtime
 * type.
 */
interface KUnit : KUnitTarget {
    /**
     * The symbol used to display this unit, e.g. `"m"` for meter or `"mi"` for mile.
     */
    val symbol: String

    /**
     * The conversion factor from this unit to the base unit of its group.
     *
     * The base unit of a group has `baseValue == 1.0` by definition.
     */
    val baseValue: Double
}
