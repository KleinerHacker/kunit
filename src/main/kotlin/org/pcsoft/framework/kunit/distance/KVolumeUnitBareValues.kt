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

// Bare volume tokens for the volume prefix `infix` functions (e.g. `5 kilo cubicMeters`, see
// `KDistanceUnitPrefix.kt`): the SI prefix scales the *linear* base unit before it is cubed, so
// `5 kilo cubicMeters` == `5 * (1000 m)^3` == 5e9 m³ (i.e. 5 cubic kilometers).

/** Bare volume token for [KDistanceUnit.METER], for the volume prefix infix functions. */
val cubicMeters: KDistanceVolumeUnit = KDistanceVolumeUnit(KDistanceUnit.METER)

/** Bare volume token for [KDistanceUnit.MILE], for the volume prefix infix functions. */
val cubicMiles: KDistanceVolumeUnit = KDistanceVolumeUnit(KDistanceUnit.MILE)

/** Bare volume token for [KDistanceUnit.NAUTICAL_MILE], for the volume prefix infix functions. */
val cubicNauticalMiles: KDistanceVolumeUnit = KDistanceVolumeUnit(KDistanceUnit.NAUTICAL_MILE)

/** Bare volume token for [KDistanceUnit.YARD], for the volume prefix infix functions. */
val cubicYards: KDistanceVolumeUnit = KDistanceVolumeUnit(KDistanceUnit.YARD)

/** Bare volume token for [KDistanceUnit.FOOT], for the volume prefix infix functions. */
val cubicFeet: KDistanceVolumeUnit = KDistanceVolumeUnit(KDistanceUnit.FOOT)

/** Bare volume token for [KDistanceUnit.INCH], for the volume prefix infix functions. */
val cubicInches: KDistanceVolumeUnit = KDistanceVolumeUnit(KDistanceUnit.INCH)

/** Bare volume token for [KDistanceUnit.FATHOM], for the volume prefix infix functions. */
val cubicFathoms: KDistanceVolumeUnit = KDistanceVolumeUnit(KDistanceUnit.FATHOM)

/** Bare volume token for [KDistanceUnit.CHAIN], for the volume prefix infix functions. */
val cubicChains: KDistanceVolumeUnit = KDistanceVolumeUnit(KDistanceUnit.CHAIN)

/** Bare volume token for [KDistanceUnit.FURLONG], for the volume prefix infix functions. */
val cubicFurlongs: KDistanceVolumeUnit = KDistanceVolumeUnit(KDistanceUnit.FURLONG)

/** Bare volume token for [KDistanceUnit.ASTRONOMICAL_UNIT], for the volume prefix infix functions. */
val cubicAstronomicalUnits: KDistanceVolumeUnit = KDistanceVolumeUnit(KDistanceUnit.ASTRONOMICAL_UNIT)

/** Bare volume token for [KDistanceUnit.LIGHT_SECOND], for the volume prefix infix functions. */
val cubicLightSeconds: KDistanceVolumeUnit = KDistanceVolumeUnit(KDistanceUnit.LIGHT_SECOND)

/** Bare volume token for [KDistanceUnit.LIGHT_MINUTE], for the volume prefix infix functions. */
val cubicLightMinutes: KDistanceVolumeUnit = KDistanceVolumeUnit(KDistanceUnit.LIGHT_MINUTE)

/** Bare volume token for [KDistanceUnit.LIGHT_HOUR], for the volume prefix infix functions. */
val cubicLightHours: KDistanceVolumeUnit = KDistanceVolumeUnit(KDistanceUnit.LIGHT_HOUR)

/** Bare volume token for [KDistanceUnit.LIGHT_DAY], for the volume prefix infix functions. */
val cubicLightDays: KDistanceVolumeUnit = KDistanceVolumeUnit(KDistanceUnit.LIGHT_DAY)

/** Bare volume token for [KDistanceUnit.LIGHT_WEEK], for the volume prefix infix functions. */
val cubicLightWeeks: KDistanceVolumeUnit = KDistanceVolumeUnit(KDistanceUnit.LIGHT_WEEK)

/** Bare volume token for [KDistanceUnit.LIGHT_YEAR], for the volume prefix infix functions. */
val cubicLightYears: KDistanceVolumeUnit = KDistanceVolumeUnit(KDistanceUnit.LIGHT_YEAR)

/** Bare volume token for [KDistanceUnit.PARSEC], for the volume prefix infix functions. */
val cubicParsecs: KDistanceVolumeUnit = KDistanceVolumeUnit(KDistanceUnit.PARSEC)
