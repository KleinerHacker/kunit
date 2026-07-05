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
import kotlin.math.abs

// Shared, cross-dimension test fixtures for the distance group: the construction matrices and the small
// builder/tolerance helpers used by the per-dimension test classes (KLengthUnitInstanceTest,
// KAreaUnitInstanceTest, KVolumeUnitInstanceTest, the prefix/mixed/derived tests). Kept here so no single
// dimension's test file owns the material the others depend on.
//
// There are TWO parallel families of construction fixtures, each covering a different public entry point:
//
//   * `*UnitGenerators` — lists of (creator-property lambda, unit) pairs. The lambda `{ n -> n.meters }`
//     exercises the number-extension **creator properties** (`5.meters`, `3.squareMiles`, …). The paired
//     `KDistanceUnit` is the reference used to compute the expected value (`unit.baseValue`).
//   * `*BareValues` — the public **bare-value alias tokens** (`meters`, `squareMiles`, `cubicYards`, …).
//     These are the argument the prefix `infix` functions take, so driving the prefix matrices off this
//     list makes `5 kilo meters` genuinely run through the alias (and thus covers K*UnitBareValues.kt).
//     Area/volume tokens are wrappers, so they are paired with the underlying linear unit for the expected
//     `baseValue`.
//
// Enum/wrapper values (KDistanceUnit.METER, …) are used ONLY to compute expected values, never to build the
// instance under test — see the test-construction policy in CLAUDE.md.
//
// The `mkLength`/`mkArea`/`mkVolume` helpers look an instance up by unit and build it (via the creator
// property); `distanceDelta` is a magnitude-relative tolerance (see its own doc).

/** All length (exponent 1) creator properties paired with the [KDistanceUnit] they construct. */
internal val lengthUnitGenerators: List<Pair<(Number) -> KLengthUnitInstance, KDistanceUnit>> = listOf(
    ({ n: Number -> n.meters }) to KDistanceUnit.METER,
    ({ n: Number -> n.miles }) to KDistanceUnit.MILE,
    ({ n: Number -> n.nauticalMiles }) to KDistanceUnit.NAUTICAL_MILE,
    ({ n: Number -> n.yards }) to KDistanceUnit.YARD,
    ({ n: Number -> n.feet }) to KDistanceUnit.FOOT,
    ({ n: Number -> n.inches }) to KDistanceUnit.INCH,
    ({ n: Number -> n.fathoms }) to KDistanceUnit.FATHOM,
    ({ n: Number -> n.chains }) to KDistanceUnit.CHAIN,
    ({ n: Number -> n.furlongs }) to KDistanceUnit.FURLONG,
    ({ n: Number -> n.astronomicalUnits }) to KDistanceUnit.ASTRONOMICAL_UNIT,
    ({ n: Number -> n.lightSeconds }) to KDistanceUnit.LIGHT_SECOND,
    ({ n: Number -> n.lightMinutes }) to KDistanceUnit.LIGHT_MINUTE,
    ({ n: Number -> n.lightHours }) to KDistanceUnit.LIGHT_HOUR,
    ({ n: Number -> n.lightDays }) to KDistanceUnit.LIGHT_DAY,
    ({ n: Number -> n.lightWeeks }) to KDistanceUnit.LIGHT_WEEK,
    ({ n: Number -> n.lightYears }) to KDistanceUnit.LIGHT_YEAR,
    ({ n: Number -> n.parsecs }) to KDistanceUnit.PARSEC
)

/** All area (exponent 2) `square…` creator properties paired with the [KDistanceUnit] they square. */
internal val areaUnitGenerators: List<Pair<(Number) -> KAreaUnitInstance, KDistanceUnit>> = listOf(
    ({ n: Number -> n.squareMeters }) to KDistanceUnit.METER,
    ({ n: Number -> n.squareMiles }) to KDistanceUnit.MILE,
    ({ n: Number -> n.squareNauticalMiles }) to KDistanceUnit.NAUTICAL_MILE,
    ({ n: Number -> n.squareYards }) to KDistanceUnit.YARD,
    ({ n: Number -> n.squareFeet }) to KDistanceUnit.FOOT,
    ({ n: Number -> n.squareInches }) to KDistanceUnit.INCH,
    ({ n: Number -> n.squareFathoms }) to KDistanceUnit.FATHOM,
    ({ n: Number -> n.squareChains }) to KDistanceUnit.CHAIN,
    ({ n: Number -> n.squareFurlongs }) to KDistanceUnit.FURLONG,
    ({ n: Number -> n.squareAstronomicalUnits }) to KDistanceUnit.ASTRONOMICAL_UNIT,
    ({ n: Number -> n.squareLightSeconds }) to KDistanceUnit.LIGHT_SECOND,
    ({ n: Number -> n.squareLightMinutes }) to KDistanceUnit.LIGHT_MINUTE,
    ({ n: Number -> n.squareLightHours }) to KDistanceUnit.LIGHT_HOUR,
    ({ n: Number -> n.squareLightDays }) to KDistanceUnit.LIGHT_DAY,
    ({ n: Number -> n.squareLightWeeks }) to KDistanceUnit.LIGHT_WEEK,
    ({ n: Number -> n.squareLightYears }) to KDistanceUnit.LIGHT_YEAR,
    ({ n: Number -> n.squareParsecs }) to KDistanceUnit.PARSEC
)

/** All volume (exponent 3) `cubic…` creator properties paired with the [KDistanceUnit] they cube. */
internal val volumeUnitGenerators: List<Pair<(Number) -> KVolumeUnitInstance, KDistanceUnit>> = listOf(
    ({ n: Number -> n.cubicMeters }) to KDistanceUnit.METER,
    ({ n: Number -> n.cubicMiles }) to KDistanceUnit.MILE,
    ({ n: Number -> n.cubicNauticalMiles }) to KDistanceUnit.NAUTICAL_MILE,
    ({ n: Number -> n.cubicYards }) to KDistanceUnit.YARD,
    ({ n: Number -> n.cubicFeet }) to KDistanceUnit.FOOT,
    ({ n: Number -> n.cubicInches }) to KDistanceUnit.INCH,
    ({ n: Number -> n.cubicFathoms }) to KDistanceUnit.FATHOM,
    ({ n: Number -> n.cubicChains }) to KDistanceUnit.CHAIN,
    ({ n: Number -> n.cubicFurlongs }) to KDistanceUnit.FURLONG,
    ({ n: Number -> n.cubicAstronomicalUnits }) to KDistanceUnit.ASTRONOMICAL_UNIT,
    ({ n: Number -> n.cubicLightSeconds }) to KDistanceUnit.LIGHT_SECOND,
    ({ n: Number -> n.cubicLightMinutes }) to KDistanceUnit.LIGHT_MINUTE,
    ({ n: Number -> n.cubicLightHours }) to KDistanceUnit.LIGHT_HOUR,
    ({ n: Number -> n.cubicLightDays }) to KDistanceUnit.LIGHT_DAY,
    ({ n: Number -> n.cubicLightWeeks }) to KDistanceUnit.LIGHT_WEEK,
    ({ n: Number -> n.cubicLightYears }) to KDistanceUnit.LIGHT_YEAR,
    ({ n: Number -> n.cubicParsecs }) to KDistanceUnit.PARSEC
)

/** Area derived-unit creator properties paired with the [KDerivedUnit] they construct. */
internal val areaDerivedGenerators: List<Pair<(Number) -> KAreaUnitInstance, KDerivedUnit<KDistanceUnit>>> = listOf(
    ({ n: Number -> n.ares }) to KDistanceDerivedUnit.ARE,
    ({ n: Number -> n.hectares }) to KDistanceDerivedUnit.HECTARE,
    ({ n: Number -> n.acres }) to KDistanceDerivedUnit.ACRE
)

/** Volume derived-unit creator properties paired with the [KDerivedUnit] they construct. */
internal val volumeDerivedGenerators: List<Pair<(Number) -> KVolumeUnitInstance, KDerivedUnit<KDistanceUnit>>> = listOf(
    ({ n: Number -> n.liters }) to KDistanceDerivedUnit.LITER,
    ({ n: Number -> n.usGallons }) to KDistanceDerivedUnit.US_GALLON,
    ({ n: Number -> n.imperialGallons }) to KDistanceDerivedUnit.IMPERIAL_GALLON,
    ({ n: Number -> n.usFluidOunces }) to KDistanceDerivedUnit.US_FLUID_OUNCE,
    ({ n: Number -> n.oilBarrels }) to KDistanceDerivedUnit.OIL_BARREL
)

// Bare-value alias lists — the public DSL tokens (`meters`, `squareMiles`, `cubicYards`, …) used to
// construct instances through the prefix `infix` functions (e.g. `5 kilo meters`). Referencing the
// aliases here executes their `val` initializers, so the K*UnitBareValues.kt surface is covered. The
// paired KDistanceUnit is only the reference for expected-value computation (`unit.baseValue`).

/** All length bare-value aliases, in the same order as [lengthUnitGenerators]. */
internal val lengthBareValues: List<KDistanceUnit> = listOf(
    meters, miles, nauticalMiles, yards, feet, inches, fathoms, chains, furlongs, astronomicalUnits,
    lightSeconds, lightMinutes, lightHours, lightDays, lightWeeks, lightYears, parsecs
)

/** All area bare-value tokens paired with the underlying [KDistanceUnit] (for the expected `baseValue`). */
internal val areaBareValues: List<Pair<KDistanceAreaUnit, KDistanceUnit>> = listOf(
    squareMeters to meters, squareMiles to miles, squareNauticalMiles to nauticalMiles, squareYards to yards,
    squareFeet to feet, squareInches to inches, squareFathoms to fathoms, squareChains to chains,
    squareFurlongs to furlongs, squareAstronomicalUnits to astronomicalUnits, squareLightSeconds to lightSeconds,
    squareLightMinutes to lightMinutes, squareLightHours to lightHours, squareLightDays to lightDays,
    squareLightWeeks to lightWeeks, squareLightYears to lightYears, squareParsecs to parsecs
)

/** All volume bare-value tokens paired with the underlying [KDistanceUnit] (for the expected `baseValue`). */
internal val volumeBareValues: List<Pair<KDistanceVolumeUnit, KDistanceUnit>> = listOf(
    cubicMeters to meters, cubicMiles to miles, cubicNauticalMiles to nauticalMiles, cubicYards to yards,
    cubicFeet to feet, cubicInches to inches, cubicFathoms to fathoms, cubicChains to chains,
    cubicFurlongs to furlongs, cubicAstronomicalUnits to astronomicalUnits, cubicLightSeconds to lightSeconds,
    cubicLightMinutes to lightMinutes, cubicLightHours to lightHours, cubicLightDays to lightDays,
    cubicLightWeeks to lightWeeks, cubicLightYears to lightYears, cubicParsecs to parsecs
)

/** Builds a length of [n] in [unit] via that unit's creator property. */
internal fun mkLength(unit: KDistanceUnit, n: Number): KLengthUnitInstance = lengthUnitGenerators.first { it.second == unit }.first(n)

/** Builds an area of [n] `square unit` via that unit's creator property. */
internal fun mkArea(unit: KDistanceUnit, n: Number): KAreaUnitInstance = areaUnitGenerators.first { it.second == unit }.first(n)

/** Builds a volume of [n] `cubic unit` via that unit's creator property. */
internal fun mkVolume(unit: KDistanceUnit, n: Number): KVolumeUnitInstance = volumeUnitGenerators.first { it.second == unit }.first(n)

/** Relative tolerance that stays meaningful across the enormous magnitude span (inch … parsec). */
internal fun distanceDelta(expected: Double): Double = (abs(expected) * 1e-9).coerceAtLeast(1e-12)
