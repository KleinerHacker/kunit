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

// Bare, value-1 length tokens (each = 1 unit, normalized to meters). They are the vocabulary for both
// building (`10 of meters`) and reading (`v into miles`), and combine with the prefix builders
// (`kilo.meters`) and operators (`meters / seconds`). See KLengthUnitExtensions.kt for the prefixed
// forms.

/** 1 meter ([KDistanceUnit.METER]). Build with `n of meters`, read with `v into meters`. */
val meters: KLengthUnitInstance = lengthOf(KDistanceUnit.METER.baseValue)

/** 1 mile ([KDistanceUnit.MILE]). */
val miles: KLengthUnitInstance = lengthOf(KDistanceUnit.MILE.baseValue)

/** 1 nautical mile ([KDistanceUnit.NAUTICAL_MILE]). */
val nauticalMiles: KLengthUnitInstance = lengthOf(KDistanceUnit.NAUTICAL_MILE.baseValue)

/** 1 yard ([KDistanceUnit.YARD]). */
val yards: KLengthUnitInstance = lengthOf(KDistanceUnit.YARD.baseValue)

/** 1 foot ([KDistanceUnit.FOOT]). */
val feet: KLengthUnitInstance = lengthOf(KDistanceUnit.FOOT.baseValue)

/** 1 inch ([KDistanceUnit.INCH]). */
val inches: KLengthUnitInstance = lengthOf(KDistanceUnit.INCH.baseValue)

/** 1 fathom ([KDistanceUnit.FATHOM]). */
val fathoms: KLengthUnitInstance = lengthOf(KDistanceUnit.FATHOM.baseValue)

/** 1 chain ([KDistanceUnit.CHAIN]). */
val chains: KLengthUnitInstance = lengthOf(KDistanceUnit.CHAIN.baseValue)

/** 1 furlong ([KDistanceUnit.FURLONG]). */
val furlongs: KLengthUnitInstance = lengthOf(KDistanceUnit.FURLONG.baseValue)

/** 1 astronomical unit ([KDistanceUnit.ASTRONOMICAL_UNIT]). */
val astronomicalUnits: KLengthUnitInstance = lengthOf(KDistanceUnit.ASTRONOMICAL_UNIT.baseValue)

/** 1 light-second ([KDistanceUnit.LIGHT_SECOND]). */
val lightSeconds: KLengthUnitInstance = lengthOf(KDistanceUnit.LIGHT_SECOND.baseValue)

/** 1 light-minute ([KDistanceUnit.LIGHT_MINUTE]). */
val lightMinutes: KLengthUnitInstance = lengthOf(KDistanceUnit.LIGHT_MINUTE.baseValue)

/** 1 light-hour ([KDistanceUnit.LIGHT_HOUR]). */
val lightHours: KLengthUnitInstance = lengthOf(KDistanceUnit.LIGHT_HOUR.baseValue)

/** 1 light-day ([KDistanceUnit.LIGHT_DAY]). */
val lightDays: KLengthUnitInstance = lengthOf(KDistanceUnit.LIGHT_DAY.baseValue)

/** 1 light-week ([KDistanceUnit.LIGHT_WEEK]). */
val lightWeeks: KLengthUnitInstance = lengthOf(KDistanceUnit.LIGHT_WEEK.baseValue)

/** 1 light-year ([KDistanceUnit.LIGHT_YEAR]). */
val lightYears: KLengthUnitInstance = lengthOf(KDistanceUnit.LIGHT_YEAR.baseValue)

/** 1 parsec ([KDistanceUnit.PARSEC]). */
val parsecs: KLengthUnitInstance = lengthOf(KDistanceUnit.PARSEC.baseValue)

/** 1 cubit / Elle ([KDistanceUnit.CUBIT]). */
val cubits: KLengthUnitInstance = lengthOf(KDistanceUnit.CUBIT.baseValue)

/** 1 Roman foot / pes ([KDistanceUnit.ROMAN_FOOT]). */
val romanFeet: KLengthUnitInstance = lengthOf(KDistanceUnit.ROMAN_FOOT.baseValue)

/** 1 Roman pace / passus ([KDistanceUnit.ROMAN_PACE]). */
val romanPaces: KLengthUnitInstance = lengthOf(KDistanceUnit.ROMAN_PACE.baseValue)

/** 1 stadium ([KDistanceUnit.STADIUM]). */
val stadia: KLengthUnitInstance = lengthOf(KDistanceUnit.STADIUM.baseValue)

/** 1 Roman mile / mille passus ([KDistanceUnit.ROMAN_MILE]). */
val romanMiles: KLengthUnitInstance = lengthOf(KDistanceUnit.ROMAN_MILE.baseValue)

/** 1 rod / perch ([KDistanceUnit.ROD]). */
val rods: KLengthUnitInstance = lengthOf(KDistanceUnit.ROD.baseValue)

/** 1 league ([KDistanceUnit.LEAGUE]). */
val leagues: KLengthUnitInstance = lengthOf(KDistanceUnit.LEAGUE.baseValue)

/** 1 cable length ([KDistanceUnit.CABLE_LENGTH]). */
val cableLengths: KLengthUnitInstance = lengthOf(KDistanceUnit.CABLE_LENGTH.baseValue)

/** 1 verst / Werst ([KDistanceUnit.VERST]). */
val versts: KLengthUnitInstance = lengthOf(KDistanceUnit.VERST.baseValue)

/** 1 Prussian mile ([KDistanceUnit.PRUSSIAN_MILE]). */
val prussianMiles: KLengthUnitInstance = lengthOf(KDistanceUnit.PRUSSIAN_MILE.baseValue)
