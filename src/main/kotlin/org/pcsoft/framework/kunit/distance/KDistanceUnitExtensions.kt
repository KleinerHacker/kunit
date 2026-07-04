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

package org.pcsoft.framework.kunit.distance

import org.pcsoft.framework.kunit.KDerivedUnit
import kotlin.math.pow

// Bare unit references, usable both as a KUnitTarget (e.g. `d.valueAs(meters)`) and as the `unit`
// argument of the distance-group prefix `infix` functions (e.g. `5 kilo meters`, see
// `KDistanceUnitPrefix.kt`). Only METER is an SI unit and therefore sensibly combinable with a prefix,
// but the others are still accepted since KUnitPrefix is a purely mathematical scale factor.

/** Bare reference to [KDistanceUnit.METER], for use with `valueAs` or the prefix `infix` functions. */
val meters: KDistanceUnit = KDistanceUnit.METER

/** Bare reference to [KDistanceUnit.MILE]. */
val miles: KDistanceUnit = KDistanceUnit.MILE

/** Bare reference to [KDistanceUnit.NAUTICAL_MILE]. */
val nauticalMiles: KDistanceUnit = KDistanceUnit.NAUTICAL_MILE

/** Bare reference to [KDistanceUnit.YARD]. */
val yards: KDistanceUnit = KDistanceUnit.YARD

/** Bare reference to [KDistanceUnit.FOOT]. */
val feet: KDistanceUnit = KDistanceUnit.FOOT

/** Bare reference to [KDistanceUnit.INCH]. */
val inches: KDistanceUnit = KDistanceUnit.INCH

/** Bare reference to [KDistanceUnit.FATHOM]. */
val fathoms: KDistanceUnit = KDistanceUnit.FATHOM

/** Bare reference to [KDistanceUnit.CHAIN]. */
val chains: KDistanceUnit = KDistanceUnit.CHAIN

/** Bare reference to [KDistanceUnit.FURLONG]. */
val furlongs: KDistanceUnit = KDistanceUnit.FURLONG

/** Bare reference to [KDistanceUnit.ASTRONOMICAL_UNIT]. */
val astronomicalUnits: KDistanceUnit = KDistanceUnit.ASTRONOMICAL_UNIT

/** Bare reference to [KDistanceUnit.LIGHT_SECOND]. */
val lightSeconds: KDistanceUnit = KDistanceUnit.LIGHT_SECOND

/** Bare reference to [KDistanceUnit.LIGHT_MINUTE]. */
val lightMinutes: KDistanceUnit = KDistanceUnit.LIGHT_MINUTE

/** Bare reference to [KDistanceUnit.LIGHT_HOUR]. */
val lightHours: KDistanceUnit = KDistanceUnit.LIGHT_HOUR

/** Bare reference to [KDistanceUnit.LIGHT_DAY]. */
val lightDays: KDistanceUnit = KDistanceUnit.LIGHT_DAY

/** Bare reference to [KDistanceUnit.LIGHT_WEEK]. */
val lightWeeks: KDistanceUnit = KDistanceUnit.LIGHT_WEEK

/** Bare reference to [KDistanceUnit.LIGHT_YEAR]. */
val lightYears: KDistanceUnit = KDistanceUnit.LIGHT_YEAR

/** Bare reference to [KDistanceUnit.PARSEC]. */
val parsecs: KDistanceUnit = KDistanceUnit.PARSEC

// --- Length (exponent 1) ------------------------------------------------------------------------

private fun lengthFrom(value: Number, unit: KDistanceUnit): KLengthUnitInstance = lengthOf(value.toDouble() * unit.baseValue)

/**
 * Creates a pure length value (exponent 1) in meters from any [Number] type.
 *
 * Example:
 * ```kotlin
 * 5.meters.value   // 5.0
 * 5L.meters.value  // 5.0
 * 5.0f.meters.value // 5.0
 * ```
 */
val Number.meters: KLengthUnitInstance get() = lengthFrom(this, KDistanceUnit.METER)

/** Creates a pure length value in miles. Example: `5.miles.value // 8046.72` (normalized to meters). */
val Number.miles: KLengthUnitInstance get() = lengthFrom(this, KDistanceUnit.MILE)

/** Creates a pure length value in nautical miles. Example: `1.nauticalMiles.value // 1852.0`. */
val Number.nauticalMiles: KLengthUnitInstance get() = lengthFrom(this, KDistanceUnit.NAUTICAL_MILE)

/** Creates a pure length value in yards. Example: `1.yards.value // 0.9144`. */
val Number.yards: KLengthUnitInstance get() = lengthFrom(this, KDistanceUnit.YARD)

/** Creates a pure length value in feet. Example: `1.feet.value // 0.3048`. */
val Number.feet: KLengthUnitInstance get() = lengthFrom(this, KDistanceUnit.FOOT)

/** Creates a pure length value in inches. Example: `1.inches.value // 0.0254`. */
val Number.inches: KLengthUnitInstance get() = lengthFrom(this, KDistanceUnit.INCH)

/** Creates a pure length value in fathoms. Example: `1.fathoms.value // 1.8288`. */
val Number.fathoms: KLengthUnitInstance get() = lengthFrom(this, KDistanceUnit.FATHOM)

/** Creates a pure length value in chains. Example: `1.chains.value // 20.1168`. */
val Number.chains: KLengthUnitInstance get() = lengthFrom(this, KDistanceUnit.CHAIN)

/** Creates a pure length value in furlongs. Example: `1.furlongs.value // 201.168`. */
val Number.furlongs: KLengthUnitInstance get() = lengthFrom(this, KDistanceUnit.FURLONG)

/** Creates a pure length value in astronomical units. Example: `1.astronomicalUnits.value // 1.495978707e11`. */
val Number.astronomicalUnits: KLengthUnitInstance get() = lengthFrom(this, KDistanceUnit.ASTRONOMICAL_UNIT)

/** Creates a pure length value in light-seconds. Example: `1.lightSeconds.value // 299792458.0`. */
val Number.lightSeconds: KLengthUnitInstance get() = lengthFrom(this, KDistanceUnit.LIGHT_SECOND)

/** Creates a pure length value in light-minutes. Example: `1.lightMinutes.value // 1.798754748e10`. */
val Number.lightMinutes: KLengthUnitInstance get() = lengthFrom(this, KDistanceUnit.LIGHT_MINUTE)

/** Creates a pure length value in light-hours. Example: `1.lightHours.value // 1.0792528488e12`. */
val Number.lightHours: KLengthUnitInstance get() = lengthFrom(this, KDistanceUnit.LIGHT_HOUR)

/** Creates a pure length value in light-days. Example: `1.lightDays.value // 2.59020683712e13`. */
val Number.lightDays: KLengthUnitInstance get() = lengthFrom(this, KDistanceUnit.LIGHT_DAY)

/** Creates a pure length value in light-weeks. Example: `1.lightWeeks.value // 1.813144785984e14`. */
val Number.lightWeeks: KLengthUnitInstance get() = lengthFrom(this, KDistanceUnit.LIGHT_WEEK)

/** Creates a pure length value in light-years. Example: `1.lightYears.value // 9.4607304725808e15`. */
val Number.lightYears: KLengthUnitInstance get() = lengthFrom(this, KDistanceUnit.LIGHT_YEAR)

/** Creates a pure length value in parsecs. Example: `1.parsecs.value // 3.0856775814913673e16`. */
val Number.parsecs: KLengthUnitInstance get() = lengthFrom(this, KDistanceUnit.PARSEC)

// --- Area (exponent 2) --------------------------------------------------------------------------

private fun areaFrom(value: Number, unit: KDistanceUnit): KAreaUnitInstance = areaOf(value.toDouble() * unit.baseValue.pow(2))

/**
 * Creates a pure area value (exponent 2) in square meters. Example: `200.squareMeters.value // 200.0`
 * (already in m²). Equivalent to `x.meters * y.meters` when `x*y = 200`.
 */
val Number.squareMeters: KAreaUnitInstance get() = areaFrom(this, KDistanceUnit.METER)

/** Creates a pure area value in square miles. Example: `1.squareMiles.value // 2589988.110336` (m²). */
val Number.squareMiles: KAreaUnitInstance get() = areaFrom(this, KDistanceUnit.MILE)

/** Creates a pure area value in square nautical miles. */
val Number.squareNauticalMiles: KAreaUnitInstance get() = areaFrom(this, KDistanceUnit.NAUTICAL_MILE)

/** Creates a pure area value in square yards. Example: `1.squareYards.value // 0.83612736` (m²). */
val Number.squareYards: KAreaUnitInstance get() = areaFrom(this, KDistanceUnit.YARD)

/** Creates a pure area value in square feet. Example: `1.squareFeet.value // 0.09290304` (m²). */
val Number.squareFeet: KAreaUnitInstance get() = areaFrom(this, KDistanceUnit.FOOT)

/** Creates a pure area value in square inches. Example: `1.squareInches.value // 0.00064516` (m²). */
val Number.squareInches: KAreaUnitInstance get() = areaFrom(this, KDistanceUnit.INCH)

/** Creates a pure area value in square fathoms. */
val Number.squareFathoms: KAreaUnitInstance get() = areaFrom(this, KDistanceUnit.FATHOM)

/** Creates a pure area value in square chains. */
val Number.squareChains: KAreaUnitInstance get() = areaFrom(this, KDistanceUnit.CHAIN)

/** Creates a pure area value in square furlongs. */
val Number.squareFurlongs: KAreaUnitInstance get() = areaFrom(this, KDistanceUnit.FURLONG)

/** Creates a pure area value in square astronomical units. */
val Number.squareAstronomicalUnits: KAreaUnitInstance get() = areaFrom(this, KDistanceUnit.ASTRONOMICAL_UNIT)

/** Creates a pure area value in square light-seconds. */
val Number.squareLightSeconds: KAreaUnitInstance get() = areaFrom(this, KDistanceUnit.LIGHT_SECOND)

/** Creates a pure area value in square light-minutes. */
val Number.squareLightMinutes: KAreaUnitInstance get() = areaFrom(this, KDistanceUnit.LIGHT_MINUTE)

/** Creates a pure area value in square light-hours. */
val Number.squareLightHours: KAreaUnitInstance get() = areaFrom(this, KDistanceUnit.LIGHT_HOUR)

/** Creates a pure area value in square light-days. */
val Number.squareLightDays: KAreaUnitInstance get() = areaFrom(this, KDistanceUnit.LIGHT_DAY)

/** Creates a pure area value in square light-weeks. */
val Number.squareLightWeeks: KAreaUnitInstance get() = areaFrom(this, KDistanceUnit.LIGHT_WEEK)

/** Creates a pure area value in square light-years. */
val Number.squareLightYears: KAreaUnitInstance get() = areaFrom(this, KDistanceUnit.LIGHT_YEAR)

/** Creates a pure area value in square parsecs. */
val Number.squareParsecs: KAreaUnitInstance get() = areaFrom(this, KDistanceUnit.PARSEC)

// --- Volume (exponent 3) ------------------------------------------------------------------------

private fun volumeFrom(value: Number, unit: KDistanceUnit): KVolumeUnitInstance = volumeOf(value.toDouble() * unit.baseValue.pow(3))

/**
 * Creates a pure volume value (exponent 3) in cubic meters. Example: `2.cubicMeters.value // 2.0`
 * (already in m³). Equivalent to `x.meters * y.meters * z.meters` when `x*y*z = 2`.
 */
val Number.cubicMeters: KVolumeUnitInstance get() = volumeFrom(this, KDistanceUnit.METER)

/** Creates a pure volume value in cubic miles. */
val Number.cubicMiles: KVolumeUnitInstance get() = volumeFrom(this, KDistanceUnit.MILE)

/** Creates a pure volume value in cubic nautical miles. */
val Number.cubicNauticalMiles: KVolumeUnitInstance get() = volumeFrom(this, KDistanceUnit.NAUTICAL_MILE)

/** Creates a pure volume value in cubic yards. Example: `1.cubicYards.value // 0.764554857984` (m³). */
val Number.cubicYards: KVolumeUnitInstance get() = volumeFrom(this, KDistanceUnit.YARD)

/** Creates a pure volume value in cubic feet. Example: `1.cubicFeet.value // 0.028316846592` (m³). */
val Number.cubicFeet: KVolumeUnitInstance get() = volumeFrom(this, KDistanceUnit.FOOT)

/** Creates a pure volume value in cubic inches. Example: `1.cubicInches.value // 1.6387064e-5` (m³). */
val Number.cubicInches: KVolumeUnitInstance get() = volumeFrom(this, KDistanceUnit.INCH)

/** Creates a pure volume value in cubic fathoms. */
val Number.cubicFathoms: KVolumeUnitInstance get() = volumeFrom(this, KDistanceUnit.FATHOM)

/** Creates a pure volume value in cubic chains. */
val Number.cubicChains: KVolumeUnitInstance get() = volumeFrom(this, KDistanceUnit.CHAIN)

/** Creates a pure volume value in cubic furlongs. */
val Number.cubicFurlongs: KVolumeUnitInstance get() = volumeFrom(this, KDistanceUnit.FURLONG)

/** Creates a pure volume value in cubic astronomical units. */
val Number.cubicAstronomicalUnits: KVolumeUnitInstance get() = volumeFrom(this, KDistanceUnit.ASTRONOMICAL_UNIT)

/** Creates a pure volume value in cubic light-seconds. */
val Number.cubicLightSeconds: KVolumeUnitInstance get() = volumeFrom(this, KDistanceUnit.LIGHT_SECOND)

/** Creates a pure volume value in cubic light-minutes. */
val Number.cubicLightMinutes: KVolumeUnitInstance get() = volumeFrom(this, KDistanceUnit.LIGHT_MINUTE)

/** Creates a pure volume value in cubic light-hours. */
val Number.cubicLightHours: KVolumeUnitInstance get() = volumeFrom(this, KDistanceUnit.LIGHT_HOUR)

/** Creates a pure volume value in cubic light-days. */
val Number.cubicLightDays: KVolumeUnitInstance get() = volumeFrom(this, KDistanceUnit.LIGHT_DAY)

/** Creates a pure volume value in cubic light-weeks. */
val Number.cubicLightWeeks: KVolumeUnitInstance get() = volumeFrom(this, KDistanceUnit.LIGHT_WEEK)

/** Creates a pure volume value in cubic light-years. */
val Number.cubicLightYears: KVolumeUnitInstance get() = volumeFrom(this, KDistanceUnit.LIGHT_YEAR)

/** Creates a pure volume value in cubic parsecs. */
val Number.cubicParsecs: KVolumeUnitInstance get() = volumeFrom(this, KDistanceUnit.PARSEC)

// --- Derived area/volume units (special names) --------------------------------------------------

private fun areaFrom(value: Number, derived: KDerivedUnit<KDistanceUnit>): KAreaUnitInstance = areaOf(value.toDouble() * derived.baseValue)

private fun volumeFrom(value: Number, derived: KDerivedUnit<KDistanceUnit>): KVolumeUnitInstance = volumeOf(value.toDouble() * derived.baseValue)

/**
 * Creates a pure area value (exponent 2) in ares. Example:
 * `5.ares.valueAs(KDistanceDerivedUnit.ARE) // 5.0`.
 */
val Number.ares: KAreaUnitInstance get() = areaFrom(this, KDistanceDerivedUnit.ARE)

/**
 * Creates a pure area value (exponent 2) in hectares. Example:
 * `5.hectares.value // 50000.0` (normalized to square meters).
 */
val Number.hectares: KAreaUnitInstance get() = areaFrom(this, KDistanceDerivedUnit.HECTARE)

/**
 * Creates a pure area value (exponent 2) in acres. Example:
 * `1.acres.value // 4046.8564224` (normalized to square meters).
 */
val Number.acres: KAreaUnitInstance get() = areaFrom(this, KDistanceDerivedUnit.ACRE)

/**
 * Creates a pure volume value (exponent 3) in liters. Example:
 * `1.liters.value // 0.001` (normalized to cubic meters).
 */
val Number.liters: KVolumeUnitInstance get() = volumeFrom(this, KDistanceDerivedUnit.LITER)

/**
 * Creates a pure volume value (exponent 3) in US liquid gallons. Example:
 * `1.usGallons.value // 0.003785411784` (normalized to cubic meters).
 */
val Number.usGallons: KVolumeUnitInstance get() = volumeFrom(this, KDistanceDerivedUnit.US_GALLON)

/**
 * Creates a pure volume value (exponent 3) in imperial gallons. Example:
 * `1.imperialGallons.value // 0.00454609` (normalized to cubic meters).
 */
val Number.imperialGallons: KVolumeUnitInstance get() = volumeFrom(this, KDistanceDerivedUnit.IMPERIAL_GALLON)

/**
 * Creates a pure volume value (exponent 3) in US fluid ounces. Example:
 * `1.usFluidOunces.value // 2.95735295625e-5` (normalized to cubic meters).
 */
val Number.usFluidOunces: KVolumeUnitInstance get() = volumeFrom(this, KDistanceDerivedUnit.US_FLUID_OUNCE)

/**
 * Creates a pure volume value (exponent 3) in oil barrels. Example:
 * `1.oilBarrels.value // 0.158987294928` (normalized to cubic meters).
 */
val Number.oilBarrels: KVolumeUnitInstance get() = volumeFrom(this, KDistanceDerivedUnit.OIL_BARREL)
