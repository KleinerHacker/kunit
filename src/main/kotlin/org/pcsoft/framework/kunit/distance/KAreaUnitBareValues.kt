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

// Bare area tokens for the area prefix `infix` functions (e.g. `5 kilo squareMeters`, see
// `KDistanceUnitPrefix.kt`): the SI prefix scales the *linear* base unit before it is squared, so
// `5 kilo squareMeters` == `5 * (1000 m)^2` == 5_000_000 m² (i.e. 5 square kilometers).

/** Bare area token for [KDistanceUnit.METER], for the area prefix infix functions. */
val squareMeters: KDistanceAreaUnit = KDistanceAreaUnit(KDistanceUnit.METER)

/** Bare area token for [KDistanceUnit.MILE], for the area prefix infix functions. */
val squareMiles: KDistanceAreaUnit = KDistanceAreaUnit(KDistanceUnit.MILE)

/** Bare area token for [KDistanceUnit.NAUTICAL_MILE], for the area prefix infix functions. */
val squareNauticalMiles: KDistanceAreaUnit = KDistanceAreaUnit(KDistanceUnit.NAUTICAL_MILE)

/** Bare area token for [KDistanceUnit.YARD], for the area prefix infix functions. */
val squareYards: KDistanceAreaUnit = KDistanceAreaUnit(KDistanceUnit.YARD)

/** Bare area token for [KDistanceUnit.FOOT], for the area prefix infix functions. */
val squareFeet: KDistanceAreaUnit = KDistanceAreaUnit(KDistanceUnit.FOOT)

/** Bare area token for [KDistanceUnit.INCH], for the area prefix infix functions. */
val squareInches: KDistanceAreaUnit = KDistanceAreaUnit(KDistanceUnit.INCH)

/** Bare area token for [KDistanceUnit.FATHOM], for the area prefix infix functions. */
val squareFathoms: KDistanceAreaUnit = KDistanceAreaUnit(KDistanceUnit.FATHOM)

/** Bare area token for [KDistanceUnit.CHAIN], for the area prefix infix functions. */
val squareChains: KDistanceAreaUnit = KDistanceAreaUnit(KDistanceUnit.CHAIN)

/** Bare area token for [KDistanceUnit.FURLONG], for the area prefix infix functions. */
val squareFurlongs: KDistanceAreaUnit = KDistanceAreaUnit(KDistanceUnit.FURLONG)

/** Bare area token for [KDistanceUnit.ASTRONOMICAL_UNIT], for the area prefix infix functions. */
val squareAstronomicalUnits: KDistanceAreaUnit = KDistanceAreaUnit(KDistanceUnit.ASTRONOMICAL_UNIT)

/** Bare area token for [KDistanceUnit.LIGHT_SECOND], for the area prefix infix functions. */
val squareLightSeconds: KDistanceAreaUnit = KDistanceAreaUnit(KDistanceUnit.LIGHT_SECOND)

/** Bare area token for [KDistanceUnit.LIGHT_MINUTE], for the area prefix infix functions. */
val squareLightMinutes: KDistanceAreaUnit = KDistanceAreaUnit(KDistanceUnit.LIGHT_MINUTE)

/** Bare area token for [KDistanceUnit.LIGHT_HOUR], for the area prefix infix functions. */
val squareLightHours: KDistanceAreaUnit = KDistanceAreaUnit(KDistanceUnit.LIGHT_HOUR)

/** Bare area token for [KDistanceUnit.LIGHT_DAY], for the area prefix infix functions. */
val squareLightDays: KDistanceAreaUnit = KDistanceAreaUnit(KDistanceUnit.LIGHT_DAY)

/** Bare area token for [KDistanceUnit.LIGHT_WEEK], for the area prefix infix functions. */
val squareLightWeeks: KDistanceAreaUnit = KDistanceAreaUnit(KDistanceUnit.LIGHT_WEEK)

/** Bare area token for [KDistanceUnit.LIGHT_YEAR], for the area prefix infix functions. */
val squareLightYears: KDistanceAreaUnit = KDistanceAreaUnit(KDistanceUnit.LIGHT_YEAR)

/** Bare area token for [KDistanceUnit.PARSEC], for the area prefix infix functions. */
val squareParsecs: KDistanceAreaUnit = KDistanceAreaUnit(KDistanceUnit.PARSEC)
