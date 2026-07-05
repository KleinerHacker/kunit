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

// Volume (exponent 3) creator extension properties, including the derived-unit (liter/gallon/…) creators.

private fun volumeFrom(value: Number, unit: KDistanceUnit): KVolumeUnitInstance = volumeOf(value.toDouble() * unit.baseValue.pow(3))

private fun volumeFrom(value: Number, derived: KDerivedUnit<KDistanceUnit>): KVolumeUnitInstance = volumeOf(value.toDouble() * derived.baseValue)

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
