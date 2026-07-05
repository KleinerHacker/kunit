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

// Length (exponent 1) creator extension properties.

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
