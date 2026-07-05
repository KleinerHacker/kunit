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

// Area (exponent 2) creator extension properties, including the derived-unit (are/hectare/acre) creators.

private fun areaFrom(value: Number, unit: KDistanceUnit): KAreaUnitInstance = areaOf(value.toDouble() * unit.baseValue.pow(2))

private fun areaFrom(value: Number, derived: KDerivedUnit<KDistanceUnit>): KAreaUnitInstance = areaOf(value.toDouble() * derived.baseValue)

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
