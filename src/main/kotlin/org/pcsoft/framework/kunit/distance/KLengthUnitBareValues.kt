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
