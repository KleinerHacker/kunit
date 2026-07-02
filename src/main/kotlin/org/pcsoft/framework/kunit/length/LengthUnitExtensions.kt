package org.pcsoft.framework.kunit.length

import org.pcsoft.framework.kunit.KUnitPrefix

// Bare unit references, usable both as a KUnitTarget (e.g. `d.valueIn(meters)`) and as the argument
// of the infix prefix functions below (e.g. `5 kilo meters`). Only METER is an SI unit and therefore
// sensibly combinable with a prefix, but the others are still accepted by the infix functions since
// KUnitPrefix is a purely mathematical scale factor - see the infix functions' KDoc for details.

/** Bare reference to [LengthUnit.METER], for use with [valueIn][LengthUnitInstance.valueIn] or the prefix `infix` functions. */
val meters: LengthUnit = LengthUnit.METER

/** Bare reference to [LengthUnit.MILE]. */
val miles: LengthUnit = LengthUnit.MILE

/** Bare reference to [LengthUnit.NAUTICAL_MILE]. */
val nauticalMiles: LengthUnit = LengthUnit.NAUTICAL_MILE

/** Bare reference to [LengthUnit.YARD]. */
val yards: LengthUnit = LengthUnit.YARD

/** Bare reference to [LengthUnit.FOOT]. */
val feet: LengthUnit = LengthUnit.FOOT

/** Bare reference to [LengthUnit.INCH]. */
val inches: LengthUnit = LengthUnit.INCH

/** Bare reference to [LengthUnit.FATHOM]. */
val fathoms: LengthUnit = LengthUnit.FATHOM

/** Bare reference to [LengthUnit.CHAIN]. */
val chains: LengthUnit = LengthUnit.CHAIN

/** Bare reference to [LengthUnit.FURLONG]. */
val furlongs: LengthUnit = LengthUnit.FURLONG

/** Bare reference to [LengthUnit.ASTRONOMICAL_UNIT]. */
val astronomicalUnits: LengthUnit = LengthUnit.ASTRONOMICAL_UNIT

/** Bare reference to [LengthUnit.LIGHT_YEAR]. */
val lightYears: LengthUnit = LengthUnit.LIGHT_YEAR

/** Bare reference to [LengthUnit.PARSEC]. */
val parsecs: LengthUnit = LengthUnit.PARSEC

private fun of(value: Number, unit: LengthUnit): LengthUnitInstance = lengthUnitInstanceOf(value.toDouble() * unit.baseValue)

/**
 * Creates a pure length value in meters from any [Number] type.
 *
 * Example:
 * ```kotlin
 * 5.meters().value   // 5.0
 * 5L.meters().value  // 5.0
 * 5.0f.meters().value // 5.0
 * ```
 */
fun Number.meters(): LengthUnitInstance = of(this, LengthUnit.METER)

/** Creates a pure length value in miles. Example: `5.miles().value // 8046.72` (normalized to meters). */
fun Number.miles(): LengthUnitInstance = of(this, LengthUnit.MILE)

/** Creates a pure length value in nautical miles. Example: `1.nauticalMiles().value // 1852.0`. */
fun Number.nauticalMiles(): LengthUnitInstance = of(this, LengthUnit.NAUTICAL_MILE)

/** Creates a pure length value in yards. Example: `1.yards().value // 0.9144`. */
fun Number.yards(): LengthUnitInstance = of(this, LengthUnit.YARD)

/** Creates a pure length value in feet. Example: `1.feet().value // 0.3048`. */
fun Number.feet(): LengthUnitInstance = of(this, LengthUnit.FOOT)

/** Creates a pure length value in inches. Example: `1.inches().value // 0.0254`. */
fun Number.inches(): LengthUnitInstance = of(this, LengthUnit.INCH)

/** Creates a pure length value in fathoms. Example: `1.fathoms().value // 1.8288`. */
fun Number.fathoms(): LengthUnitInstance = of(this, LengthUnit.FATHOM)

/** Creates a pure length value in chains. Example: `1.chains().value // 20.1168`. */
fun Number.chains(): LengthUnitInstance = of(this, LengthUnit.CHAIN)

/** Creates a pure length value in furlongs. Example: `1.furlongs().value // 201.168`. */
fun Number.furlongs(): LengthUnitInstance = of(this, LengthUnit.FURLONG)

/** Creates a pure length value in astronomical units. Example: `1.astronomicalUnits().value // 1.495978707e11`. */
fun Number.astronomicalUnits(): LengthUnitInstance = of(this, LengthUnit.ASTRONOMICAL_UNIT)

/** Creates a pure length value in light-years. Example: `1.lightYears().value // 9.4607304725808e15`. */
fun Number.lightYears(): LengthUnitInstance = of(this, LengthUnit.LIGHT_YEAR)

/** Creates a pure length value in parsecs. Example: `1.parsecs().value // 3.0856775814913673e16`. */
fun Number.parsecs(): LengthUnitInstance = of(this, LengthUnit.PARSEC)

/**
 * Creates a pure length value by first scaling this number with [KUnitPrefix.KILO] and then
 * interpreting the result in [unit].
 *
 * Example:
 * ```kotlin
 * (5 kilo meters).value // 5000.0
 * ```
 */
infix fun Number.kilo(unit: LengthUnit): LengthUnitInstance = of(toDouble() * KUnitPrefix.KILO.factor, unit)

/**
 * Creates a pure length value by first scaling this number with [KUnitPrefix.HECTO] and then
 * interpreting the result in [unit]. Example: `(5 hecto meters).value // 500.0`.
 */
infix fun Number.hecto(unit: LengthUnit): LengthUnitInstance = of(toDouble() * KUnitPrefix.HECTO.factor, unit)

/**
 * Creates a pure length value by first scaling this number with [KUnitPrefix.DECA] and then
 * interpreting the result in [unit]. Example: `(5 deca meters).value // 50.0`.
 */
infix fun Number.deca(unit: LengthUnit): LengthUnitInstance = of(toDouble() * KUnitPrefix.DECA.factor, unit)

/**
 * Creates a pure length value by first scaling this number with [KUnitPrefix.DECI] and then
 * interpreting the result in [unit]. Example: `(5 deci meters).value // 0.5`.
 */
infix fun Number.deci(unit: LengthUnit): LengthUnitInstance = of(toDouble() * KUnitPrefix.DECI.factor, unit)

/**
 * Creates a pure length value by first scaling this number with [KUnitPrefix.CENTI] and then
 * interpreting the result in [unit]. Example: `(5 centi meters).value // 0.05`.
 */
infix fun Number.centi(unit: LengthUnit): LengthUnitInstance = of(toDouble() * KUnitPrefix.CENTI.factor, unit)

/**
 * Creates a pure length value by first scaling this number with [KUnitPrefix.MILLI] and then
 * interpreting the result in [unit]. Example: `(5 milli meters).value // 0.005`.
 */
infix fun Number.milli(unit: LengthUnit): LengthUnitInstance = of(toDouble() * KUnitPrefix.MILLI.factor, unit)
