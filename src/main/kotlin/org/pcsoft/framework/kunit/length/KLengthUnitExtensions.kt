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

package org.pcsoft.framework.kunit.length

import org.pcsoft.framework.kunit.KDerivedUnit
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm

// Bare unit references, usable both as a KUnitTarget (e.g. `d.valueAs(meters)`) and as the `unit`
// argument of the length-group prefix `infix` functions (e.g. `5 kilo meters`, see
// `KLengthUnitPrefix.kt`). Only METER is an SI unit and therefore sensibly combinable with a prefix, but
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
 * 5.meters.value   // 5.0
 * 5L.meters.value  // 5.0
 * 5.0f.meters.value // 5.0
 * ```
 */
val Number.meters: KLengthUnitInstance get() = of(this, KLengthUnit.METER)

/** Creates a pure length value in miles. Example: `5.miles.value // 8046.72` (normalized to meters). */
val Number.miles: KLengthUnitInstance get() = of(this, KLengthUnit.MILE)

/** Creates a pure length value in nautical miles. Example: `1.nauticalMiles.value // 1852.0`. */
val Number.nauticalMiles: KLengthUnitInstance get() = of(this, KLengthUnit.NAUTICAL_MILE)

/** Creates a pure length value in yards. Example: `1.yards.value // 0.9144`. */
val Number.yards: KLengthUnitInstance get() = of(this, KLengthUnit.YARD)

/** Creates a pure length value in feet. Example: `1.feet.value // 0.3048`. */
val Number.feet: KLengthUnitInstance get() = of(this, KLengthUnit.FOOT)

/** Creates a pure length value in inches. Example: `1.inches.value // 0.0254`. */
val Number.inches: KLengthUnitInstance get() = of(this, KLengthUnit.INCH)

/** Creates a pure length value in fathoms. Example: `1.fathoms.value // 1.8288`. */
val Number.fathoms: KLengthUnitInstance get() = of(this, KLengthUnit.FATHOM)

/** Creates a pure length value in chains. Example: `1.chains.value // 20.1168`. */
val Number.chains: KLengthUnitInstance get() = of(this, KLengthUnit.CHAIN)

/** Creates a pure length value in furlongs. Example: `1.furlongs.value // 201.168`. */
val Number.furlongs: KLengthUnitInstance get() = of(this, KLengthUnit.FURLONG)

/** Creates a pure length value in astronomical units. Example: `1.astronomicalUnits.value // 1.495978707e11`. */
val Number.astronomicalUnits: KLengthUnitInstance get() = of(this, KLengthUnit.ASTRONOMICAL_UNIT)

/** Creates a pure length value in light-seconds. Example: `1.lightSeconds.value // 299792458.0`. */
val Number.lightSeconds: KLengthUnitInstance get() = of(this, KLengthUnit.LIGHT_SECOND)

/** Creates a pure length value in light-minutes. Example: `1.lightMinutes.value // 1.798754748e10`. */
val Number.lightMinutes: KLengthUnitInstance get() = of(this, KLengthUnit.LIGHT_MINUTE)

/** Creates a pure length value in light-hours. Example: `1.lightHours.value // 1.0792528488e12`. */
val Number.lightHours: KLengthUnitInstance get() = of(this, KLengthUnit.LIGHT_HOUR)

/** Creates a pure length value in light-days. Example: `1.lightDays.value // 2.59020683712e13`. */
val Number.lightDays: KLengthUnitInstance get() = of(this, KLengthUnit.LIGHT_DAY)

/** Creates a pure length value in light-weeks. Example: `1.lightWeeks.value // 1.813144785984e14`. */
val Number.lightWeeks: KLengthUnitInstance get() = of(this, KLengthUnit.LIGHT_WEEK)

/** Creates a pure length value in light-years. Example: `1.lightYears.value // 9.4607304725808e15`. */
val Number.lightYears: KLengthUnitInstance get() = of(this, KLengthUnit.LIGHT_YEAR)

/** Creates a pure length value in parsecs. Example: `1.parsecs.value // 3.0856775814913673e16`. */
val Number.parsecs: KLengthUnitInstance get() = of(this, KLengthUnit.PARSEC)

private fun of(value: Number, derived: KDerivedUnit<KLengthUnit>): KLengthUnitInstance =
    KLengthUnitInstance(KMixedUnitInstance(value.toDouble() * derived.baseValue, listOf(KUnitTerm(derived.referenceUnit, derived.exponent))))

/**
 * Creates a pure area value (exponent 2 of [KLengthUnit.BASE]) in ares. Example:
 * `5.ares.valueAs(KLengthDerivedUnit.ARE) // 5.0`.
 */
val Number.ares: KLengthUnitInstance get() = of(this, KLengthDerivedUnit.ARE)

/**
 * Creates a pure area value (exponent 2 of [KLengthUnit.BASE]) in hectares. Example:
 * `5.hectares.value // 50000.0` (normalized to square meters).
 */
val Number.hectares: KLengthUnitInstance get() = of(this, KLengthDerivedUnit.HECTARE)

/**
 * Creates a pure area value (exponent 2 of [KLengthUnit.BASE]) in acres. Example:
 * `1.acres.value // 4046.8564224` (normalized to square meters).
 */
val Number.acres: KLengthUnitInstance get() = of(this, KLengthDerivedUnit.ACRE)

/**
 * Creates a pure volume value (exponent 3 of [KLengthUnit.BASE]) in liters. Example:
 * `1.liters.value // 0.001` (normalized to cubic meters).
 */
val Number.liters: KLengthUnitInstance get() = of(this, KLengthDerivedUnit.LITER)

/**
 * Creates a pure volume value (exponent 3 of [KLengthUnit.BASE]) in US liquid gallons. Example:
 * `1.usGallons.value // 0.003785411784` (normalized to cubic meters).
 */
val Number.usGallons: KLengthUnitInstance get() = of(this, KLengthDerivedUnit.US_GALLON)

/**
 * Creates a pure volume value (exponent 3 of [KLengthUnit.BASE]) in imperial gallons. Example:
 * `1.imperialGallons.value // 0.00454609` (normalized to cubic meters).
 */
val Number.imperialGallons: KLengthUnitInstance get() = of(this, KLengthDerivedUnit.IMPERIAL_GALLON)

/**
 * Creates a pure volume value (exponent 3 of [KLengthUnit.BASE]) in US fluid ounces. Example:
 * `1.usFluidOunces.value // 2.95735295625e-5` (normalized to cubic meters).
 */
val Number.usFluidOunces: KLengthUnitInstance get() = of(this, KLengthDerivedUnit.US_FLUID_OUNCE)

/**
 * Creates a pure volume value (exponent 3 of [KLengthUnit.BASE]) in oil barrels. Example:
 * `1.oilBarrels.value // 0.158987294928` (normalized to cubic meters).
 */
val Number.oilBarrels: KLengthUnitInstance get() = of(this, KLengthDerivedUnit.OIL_BARREL)
