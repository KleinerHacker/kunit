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
// The construction fixtures cover the public entry points:
//
//   * `lengthUnitGenerators` — (creator-property lambda, unit) pairs; the lambda `{ n -> n.meters }`
//     exercises the number-extension **creator properties** (`5.meters`, …).
//   * `areaUnitGenerators` / `volumeUnitGenerators` — (lambda, unit) pairs where the lambda builds the
//     area/volume via the **power operation** on a length (`n.<unit> pow 2` / `pow 3`); there are no
//     `squareXxx`/`cubicXxx` creators anymore, so these lists double as the per-unit pow cross-matrix.
//   * `lengthBareValues` — the public length **bare-value alias tokens** (`meters`, `miles`, …), the
//     argument the prefix `infix` functions take, so driving the prefix matrix off this list makes
//     `5 kilo meters` genuinely run through the alias.
//
// The paired `KDistanceUnit` is the reference used to compute the expected value (`unit.baseValue`); enum
// values are used ONLY to compute expected values, never to build the instance under test — see the
// test-construction policy in CLAUDE.md.
//
// The `mkLength`/`mkArea`/`mkVolume` helpers look an instance up by unit and build it; `distanceDelta` is a
// magnitude-relative tolerance (see its own doc).

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

/**
 * All area (exponent 2) generators paired with the [KDistanceUnit] they square. A generator builds `n unit²`
 * (value `n * baseValue²`) - i.e. the same quantity the removed `squareXxx` creators produced - through the
 * public multiplication DSL `n.<unit> * 1.<unit>` (`length * length = area`), so no dedicated area creator
 * is needed. (Note this is deliberately **not** `n.<unit> pow 2`, which would be `(n·baseValue)²`; the
 * per-unit `pow` matrix is covered separately in the pow tests.)
 */
internal val areaUnitGenerators: List<Pair<(Number) -> KAreaUnitInstance, KDistanceUnit>> = listOf(
    ({ n: Number -> n.meters * 1.meters }) to KDistanceUnit.METER,
    ({ n: Number -> n.miles * 1.miles }) to KDistanceUnit.MILE,
    ({ n: Number -> n.nauticalMiles * 1.nauticalMiles }) to KDistanceUnit.NAUTICAL_MILE,
    ({ n: Number -> n.yards * 1.yards }) to KDistanceUnit.YARD,
    ({ n: Number -> n.feet * 1.feet }) to KDistanceUnit.FOOT,
    ({ n: Number -> n.inches * 1.inches }) to KDistanceUnit.INCH,
    ({ n: Number -> n.fathoms * 1.fathoms }) to KDistanceUnit.FATHOM,
    ({ n: Number -> n.chains * 1.chains }) to KDistanceUnit.CHAIN,
    ({ n: Number -> n.furlongs * 1.furlongs }) to KDistanceUnit.FURLONG,
    ({ n: Number -> n.astronomicalUnits * 1.astronomicalUnits }) to KDistanceUnit.ASTRONOMICAL_UNIT,
    ({ n: Number -> n.lightSeconds * 1.lightSeconds }) to KDistanceUnit.LIGHT_SECOND,
    ({ n: Number -> n.lightMinutes * 1.lightMinutes }) to KDistanceUnit.LIGHT_MINUTE,
    ({ n: Number -> n.lightHours * 1.lightHours }) to KDistanceUnit.LIGHT_HOUR,
    ({ n: Number -> n.lightDays * 1.lightDays }) to KDistanceUnit.LIGHT_DAY,
    ({ n: Number -> n.lightWeeks * 1.lightWeeks }) to KDistanceUnit.LIGHT_WEEK,
    ({ n: Number -> n.lightYears * 1.lightYears }) to KDistanceUnit.LIGHT_YEAR,
    ({ n: Number -> n.parsecs * 1.parsecs }) to KDistanceUnit.PARSEC
)

/**
 * All volume (exponent 3) generators paired with the [KDistanceUnit] they cube. A generator builds `n unit³`
 * (value `n * baseValue³`) through `n.<unit> * 1.<unit> * 1.<unit>` (`length * length * length = volume`),
 * matching the quantity the removed `cubicXxx` creators produced.
 */
internal val volumeUnitGenerators: List<Pair<(Number) -> KVolumeUnitInstance, KDistanceUnit>> = listOf(
    ({ n: Number -> n.meters * 1.meters * 1.meters }) to KDistanceUnit.METER,
    ({ n: Number -> n.miles * 1.miles * 1.miles }) to KDistanceUnit.MILE,
    ({ n: Number -> n.nauticalMiles * 1.nauticalMiles * 1.nauticalMiles }) to KDistanceUnit.NAUTICAL_MILE,
    ({ n: Number -> n.yards * 1.yards * 1.yards }) to KDistanceUnit.YARD,
    ({ n: Number -> n.feet * 1.feet * 1.feet }) to KDistanceUnit.FOOT,
    ({ n: Number -> n.inches * 1.inches * 1.inches }) to KDistanceUnit.INCH,
    ({ n: Number -> n.fathoms * 1.fathoms * 1.fathoms }) to KDistanceUnit.FATHOM,
    ({ n: Number -> n.chains * 1.chains * 1.chains }) to KDistanceUnit.CHAIN,
    ({ n: Number -> n.furlongs * 1.furlongs * 1.furlongs }) to KDistanceUnit.FURLONG,
    ({ n: Number -> n.astronomicalUnits * 1.astronomicalUnits * 1.astronomicalUnits }) to KDistanceUnit.ASTRONOMICAL_UNIT,
    ({ n: Number -> n.lightSeconds * 1.lightSeconds * 1.lightSeconds }) to KDistanceUnit.LIGHT_SECOND,
    ({ n: Number -> n.lightMinutes * 1.lightMinutes * 1.lightMinutes }) to KDistanceUnit.LIGHT_MINUTE,
    ({ n: Number -> n.lightHours * 1.lightHours * 1.lightHours }) to KDistanceUnit.LIGHT_HOUR,
    ({ n: Number -> n.lightDays * 1.lightDays * 1.lightDays }) to KDistanceUnit.LIGHT_DAY,
    ({ n: Number -> n.lightWeeks * 1.lightWeeks * 1.lightWeeks }) to KDistanceUnit.LIGHT_WEEK,
    ({ n: Number -> n.lightYears * 1.lightYears * 1.lightYears }) to KDistanceUnit.LIGHT_YEAR,
    ({ n: Number -> n.parsecs * 1.parsecs * 1.parsecs }) to KDistanceUnit.PARSEC
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

/** Builds a length of [n] in [unit] via that unit's creator property. */
internal fun mkLength(unit: KDistanceUnit, n: Number): KLengthUnitInstance = lengthUnitGenerators.first { it.second == unit }.first(n)

/** Builds an area of [n] `unit²` via that unit's `pow 2` generator (`n.<unit> pow 2`). */
internal fun mkArea(unit: KDistanceUnit, n: Number): KAreaUnitInstance = areaUnitGenerators.first { it.second == unit }.first(n)

/** Builds a volume of [n] `unit³` via that unit's `pow 3` generator (`n.<unit> pow 3`). */
internal fun mkVolume(unit: KDistanceUnit, n: Number): KVolumeUnitInstance = volumeUnitGenerators.first { it.second == unit }.first(n)

/** Relative tolerance that stays meaningful across the enormous magnitude span (inch … parsec). */
internal fun distanceDelta(expected: Double): Double = (abs(expected) * 1e-9).coerceAtLeast(1e-12)
