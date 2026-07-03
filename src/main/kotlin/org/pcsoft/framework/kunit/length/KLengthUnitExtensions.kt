package org.pcsoft.framework.kunit.length

import org.pcsoft.framework.kunit.KDerivedUnit
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm

// Bare unit references, usable both as a KUnitTarget (e.g. `d.valueAs(meters)`) and as the `unit`
// argument of the generic, root-level prefix `infix` functions (e.g. `5 kilo meters`, see
// `KUnitPrefix.kt`). Only METER is an SI unit and therefore sensibly combinable with a prefix, but
// the others are still accepted since KUnitPrefix is a purely mathematical scale factor.

/** Bare reference to [KLengthUnit.METER], for use with [valueAs][KLengthUnitInstance.valueAs] or the prefix `infix` functions. */
val meters: KLengthUnit = KLengthUnit.METER

/** Bare reference to [KLengthUnit.MILE]. */
val miles: KLengthUnit = KLengthUnit.MILE

/** Bare reference to [KLengthUnit.NAUTICAL_MILE]. */
val nauticalMiles: KLengthUnit = KLengthUnit.NAUTICAL_MILE

/** Bare reference to [KLengthUnit.YARD]. */
val yards: KLengthUnit = KLengthUnit.YARD

/** Bare reference to [KLengthUnit.FOOT]. */
val feet: KLengthUnit = KLengthUnit.FOOT

/** Bare reference to [KLengthUnit.INCH]. */
val inches: KLengthUnit = KLengthUnit.INCH

/** Bare reference to [KLengthUnit.FATHOM]. */
val fathoms: KLengthUnit = KLengthUnit.FATHOM

/** Bare reference to [KLengthUnit.CHAIN]. */
val chains: KLengthUnit = KLengthUnit.CHAIN

/** Bare reference to [KLengthUnit.FURLONG]. */
val furlongs: KLengthUnit = KLengthUnit.FURLONG

/** Bare reference to [KLengthUnit.ASTRONOMICAL_UNIT]. */
val astronomicalUnits: KLengthUnit = KLengthUnit.ASTRONOMICAL_UNIT

/** Bare reference to [KLengthUnit.LIGHT_SECOND]. */
val lightSeconds: KLengthUnit = KLengthUnit.LIGHT_SECOND

/** Bare reference to [KLengthUnit.LIGHT_MINUTE]. */
val lightMinutes: KLengthUnit = KLengthUnit.LIGHT_MINUTE

/** Bare reference to [KLengthUnit.LIGHT_HOUR]. */
val lightHours: KLengthUnit = KLengthUnit.LIGHT_HOUR

/** Bare reference to [KLengthUnit.LIGHT_DAY]. */
val lightDays: KLengthUnit = KLengthUnit.LIGHT_DAY

/** Bare reference to [KLengthUnit.LIGHT_WEEK]. */
val lightWeeks: KLengthUnit = KLengthUnit.LIGHT_WEEK

/** Bare reference to [KLengthUnit.LIGHT_YEAR]. */
val lightYears: KLengthUnit = KLengthUnit.LIGHT_YEAR

/** Bare reference to [KLengthUnit.PARSEC]. */
val parsecs: KLengthUnit = KLengthUnit.PARSEC

private fun of(value: Number, unit: KLengthUnit): KLengthUnitInstance = lengthUnitInstanceOf(value.toDouble() * unit.baseValue)

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
fun Number.meters(): KLengthUnitInstance = of(this, KLengthUnit.METER)

/** Creates a pure length value in miles. Example: `5.miles().value // 8046.72` (normalized to meters). */
fun Number.miles(): KLengthUnitInstance = of(this, KLengthUnit.MILE)

/** Creates a pure length value in nautical miles. Example: `1.nauticalMiles().value // 1852.0`. */
fun Number.nauticalMiles(): KLengthUnitInstance = of(this, KLengthUnit.NAUTICAL_MILE)

/** Creates a pure length value in yards. Example: `1.yards().value // 0.9144`. */
fun Number.yards(): KLengthUnitInstance = of(this, KLengthUnit.YARD)

/** Creates a pure length value in feet. Example: `1.feet().value // 0.3048`. */
fun Number.feet(): KLengthUnitInstance = of(this, KLengthUnit.FOOT)

/** Creates a pure length value in inches. Example: `1.inches().value // 0.0254`. */
fun Number.inches(): KLengthUnitInstance = of(this, KLengthUnit.INCH)

/** Creates a pure length value in fathoms. Example: `1.fathoms().value // 1.8288`. */
fun Number.fathoms(): KLengthUnitInstance = of(this, KLengthUnit.FATHOM)

/** Creates a pure length value in chains. Example: `1.chains().value // 20.1168`. */
fun Number.chains(): KLengthUnitInstance = of(this, KLengthUnit.CHAIN)

/** Creates a pure length value in furlongs. Example: `1.furlongs().value // 201.168`. */
fun Number.furlongs(): KLengthUnitInstance = of(this, KLengthUnit.FURLONG)

/** Creates a pure length value in astronomical units. Example: `1.astronomicalUnits().value // 1.495978707e11`. */
fun Number.astronomicalUnits(): KLengthUnitInstance = of(this, KLengthUnit.ASTRONOMICAL_UNIT)

/** Creates a pure length value in light-seconds. Example: `1.lightSeconds().value // 299792458.0`. */
fun Number.lightSeconds(): KLengthUnitInstance = of(this, KLengthUnit.LIGHT_SECOND)

/** Creates a pure length value in light-minutes. Example: `1.lightMinutes().value // 1.798754748e10`. */
fun Number.lightMinutes(): KLengthUnitInstance = of(this, KLengthUnit.LIGHT_MINUTE)

/** Creates a pure length value in light-hours. Example: `1.lightHours().value // 1.0792528488e12`. */
fun Number.lightHours(): KLengthUnitInstance = of(this, KLengthUnit.LIGHT_HOUR)

/** Creates a pure length value in light-days. Example: `1.lightDays().value // 2.59020683712e13`. */
fun Number.lightDays(): KLengthUnitInstance = of(this, KLengthUnit.LIGHT_DAY)

/** Creates a pure length value in light-weeks. Example: `1.lightWeeks().value // 1.813144785984e14`. */
fun Number.lightWeeks(): KLengthUnitInstance = of(this, KLengthUnit.LIGHT_WEEK)

/** Creates a pure length value in light-years. Example: `1.lightYears().value // 9.4607304725808e15`. */
fun Number.lightYears(): KLengthUnitInstance = of(this, KLengthUnit.LIGHT_YEAR)

/** Creates a pure length value in parsecs. Example: `1.parsecs().value // 3.0856775814913673e16`. */
fun Number.parsecs(): KLengthUnitInstance = of(this, KLengthUnit.PARSEC)

private fun of(value: Number, derived: KDerivedUnit<KLengthUnit>): KLengthUnitInstance =
    KLengthUnitInstance(KMixedUnitInstance(value.toDouble() * derived.baseValue, listOf(KUnitTerm(derived.referenceUnit, derived.exponent))))

/**
 * Creates a pure area value (exponent 2 of [KLengthUnit.BASE]) in ares. Example:
 * `5.ares().valueAs(KLengthDerivedUnit.ARE) // 5.0`.
 */
fun Number.ares(): KLengthUnitInstance = of(this, KLengthDerivedUnit.ARE)

/**
 * Creates a pure area value (exponent 2 of [KLengthUnit.BASE]) in hectares. Example:
 * `5.hectares().value // 50000.0` (normalized to square meters).
 */
fun Number.hectares(): KLengthUnitInstance = of(this, KLengthDerivedUnit.HECTARE)

/**
 * Creates a pure area value (exponent 2 of [KLengthUnit.BASE]) in acres. Example:
 * `1.acres().value // 4046.8564224` (normalized to square meters).
 */
fun Number.acres(): KLengthUnitInstance = of(this, KLengthDerivedUnit.ACRE)

/**
 * Creates a pure volume value (exponent 3 of [KLengthUnit.BASE]) in liters. Example:
 * `1.liters().value // 0.001` (normalized to cubic meters).
 */
fun Number.liters(): KLengthUnitInstance = of(this, KLengthDerivedUnit.LITER)

/**
 * Creates a pure volume value (exponent 3 of [KLengthUnit.BASE]) in US liquid gallons. Example:
 * `1.usGallons().value // 0.003785411784` (normalized to cubic meters).
 */
fun Number.usGallons(): KLengthUnitInstance = of(this, KLengthDerivedUnit.US_GALLON)

/**
 * Creates a pure volume value (exponent 3 of [KLengthUnit.BASE]) in imperial gallons. Example:
 * `1.imperialGallons().value // 0.00454609` (normalized to cubic meters).
 */
fun Number.imperialGallons(): KLengthUnitInstance = of(this, KLengthDerivedUnit.IMPERIAL_GALLON)

/**
 * Creates a pure volume value (exponent 3 of [KLengthUnit.BASE]) in US fluid ounces. Example:
 * `1.usFluidOunces().value // 2.95735295625e-5` (normalized to cubic meters).
 */
fun Number.usFluidOunces(): KLengthUnitInstance = of(this, KLengthDerivedUnit.US_FLUID_OUNCE)

/**
 * Creates a pure volume value (exponent 3 of [KLengthUnit.BASE]) in oil barrels. Example:
 * `1.oilBarrels().value // 0.158987294928` (normalized to cubic meters).
 */
fun Number.oilBarrels(): KLengthUnitInstance = of(this, KLengthDerivedUnit.OIL_BARREL)
