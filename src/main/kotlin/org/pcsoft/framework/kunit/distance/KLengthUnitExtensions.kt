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

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 length templates: one property per length unit on the prefix builder (e.g.
// `kilo.meters` = 1000 m, `milli.meters` = 0.001 m). Length accepts *any* magnitude, so the properties
// hang on the common base [KPrefixBuilder] (reachable from both the augmenting and diminishing
// builders). Use with `of`/`into`, e.g. `10 of kilo.meters`, `v into milli.meters`.

private fun prefixedLength(builder: KPrefixBuilder, unit: KDistanceUnit): KLengthUnitInstance =
    lengthOf(builder.prefix.factor * unit.baseValue)

/** Prefixed meters, e.g. `kilo.meters` = 1000 m, `milli.meters` = 0.001 m. */
val KPrefixBuilder.meters: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.METER)

/** Prefixed miles, e.g. `kilo.miles` = 1000 mi. */
val KPrefixBuilder.miles: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.MILE)

/** Prefixed nautical miles. */
val KPrefixBuilder.nauticalMiles: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.NAUTICAL_MILE)

/** Prefixed yards. */
val KPrefixBuilder.yards: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.YARD)

/** Prefixed feet. */
val KPrefixBuilder.feet: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.FOOT)

/** Prefixed inches. */
val KPrefixBuilder.inches: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.INCH)

/** Prefixed fathoms. */
val KPrefixBuilder.fathoms: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.FATHOM)

/** Prefixed chains. */
val KPrefixBuilder.chains: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.CHAIN)

/** Prefixed furlongs. */
val KPrefixBuilder.furlongs: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.FURLONG)

/** Prefixed astronomical units. */
val KPrefixBuilder.astronomicalUnits: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.ASTRONOMICAL_UNIT)

/** Prefixed light-seconds. */
val KPrefixBuilder.lightSeconds: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.LIGHT_SECOND)

/** Prefixed light-minutes. */
val KPrefixBuilder.lightMinutes: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.LIGHT_MINUTE)

/** Prefixed light-hours. */
val KPrefixBuilder.lightHours: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.LIGHT_HOUR)

/** Prefixed light-days. */
val KPrefixBuilder.lightDays: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.LIGHT_DAY)

/** Prefixed light-weeks. */
val KPrefixBuilder.lightWeeks: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.LIGHT_WEEK)

/** Prefixed light-years. */
val KPrefixBuilder.lightYears: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.LIGHT_YEAR)

/** Prefixed parsecs. */
val KPrefixBuilder.parsecs: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.PARSEC)

/** Prefixed cubits (Elle). */
val KPrefixBuilder.cubits: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.CUBIT)

/** Prefixed Roman feet (pes). */
val KPrefixBuilder.romanFeet: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.ROMAN_FOOT)

/** Prefixed Roman paces (passus). */
val KPrefixBuilder.romanPaces: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.ROMAN_PACE)

/** Prefixed stadia. */
val KPrefixBuilder.stadia: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.STADIUM)

/** Prefixed Roman miles (mille passus). */
val KPrefixBuilder.romanMiles: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.ROMAN_MILE)

/** Prefixed rods (perch). */
val KPrefixBuilder.rods: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.ROD)

/** Prefixed leagues. */
val KPrefixBuilder.leagues: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.LEAGUE)

/** Prefixed cable lengths. */
val KPrefixBuilder.cableLengths: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.CABLE_LENGTH)

/** Prefixed versts (Werst). */
val KPrefixBuilder.versts: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.VERST)

/** Prefixed Prussian miles. */
val KPrefixBuilder.prussianMiles: KLengthUnitInstance get() = prefixedLength(this, KDistanceUnit.PRUSSIAN_MILE)
