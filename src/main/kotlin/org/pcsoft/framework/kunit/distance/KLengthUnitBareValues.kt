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

import org.pcsoft.framework.kunit.KUnitDisplay

// Bare, value-1 length tokens (each = 1 unit, normalized to meters). They are the vocabulary for both
// building (`10 of meters`) and reading (`v into miles`), and combine with the prefix builders
// (`kilo.meters`) and operators (`meters / seconds`). See KLengthUnitExtensions.kt for the prefixed
// forms.

// Each token carries its own [KUnitDisplay] so that formatting renders the written-down symbol
// (`"mi"`, `"h"`) rather than the group base symbol; the value stays normalized to meters.
private fun bareLength(unit: KDistanceUnit): KLengthUnitInstance = lengthOf(unit.baseValue, KUnitDisplay(unit))

/** 1 meter ([KDistanceUnit.METER]). Build with `n of meters`, read with `v into meters`. */
val meters: KLengthUnitInstance = bareLength(KDistanceUnit.METER)

/** 1 mile ([KDistanceUnit.MILE]). */
val miles: KLengthUnitInstance = bareLength(KDistanceUnit.MILE)

/** 1 nautical mile ([KDistanceUnit.NAUTICAL_MILE]). */
val nauticalMiles: KLengthUnitInstance = bareLength(KDistanceUnit.NAUTICAL_MILE)

/** 1 yard ([KDistanceUnit.YARD]). */
val yards: KLengthUnitInstance = bareLength(KDistanceUnit.YARD)

/** 1 foot ([KDistanceUnit.FOOT]). */
val feet: KLengthUnitInstance = bareLength(KDistanceUnit.FOOT)

/** 1 inch ([KDistanceUnit.INCH]). */
val inches: KLengthUnitInstance = bareLength(KDistanceUnit.INCH)

/** 1 fathom ([KDistanceUnit.FATHOM]). */
val fathoms: KLengthUnitInstance = bareLength(KDistanceUnit.FATHOM)

/** 1 chain ([KDistanceUnit.CHAIN]). */
val chains: KLengthUnitInstance = bareLength(KDistanceUnit.CHAIN)

/** 1 furlong ([KDistanceUnit.FURLONG]). */
val furlongs: KLengthUnitInstance = bareLength(KDistanceUnit.FURLONG)

/** 1 astronomical unit ([KDistanceUnit.ASTRONOMICAL_UNIT]). */
val astronomicalUnits: KLengthUnitInstance = bareLength(KDistanceUnit.ASTRONOMICAL_UNIT)

// Light-travel distances (light-second … light-year) are grouped behind the prefix-free `light`
// builder instead (`5 of light.seconds`, `3 of light.years`); see KLengthLightUnitBuilder.kt.

/** 1 parsec ([KDistanceUnit.PARSEC]). */
val parsecs: KLengthUnitInstance = bareLength(KDistanceUnit.PARSEC)

/** 1 cubit / Elle ([KDistanceUnit.CUBIT]). */
val cubits: KLengthUnitInstance = bareLength(KDistanceUnit.CUBIT)

/** 1 Roman foot / pes ([KDistanceUnit.ROMAN_FOOT]). */
val romanFeet: KLengthUnitInstance = bareLength(KDistanceUnit.ROMAN_FOOT)

/** 1 Roman pace / passus ([KDistanceUnit.ROMAN_PACE]). */
val romanPaces: KLengthUnitInstance = bareLength(KDistanceUnit.ROMAN_PACE)

/** 1 stadium ([KDistanceUnit.STADIUM]). */
val stadia: KLengthUnitInstance = bareLength(KDistanceUnit.STADIUM)

/** 1 Roman mile / mille passus ([KDistanceUnit.ROMAN_MILE]). */
val romanMiles: KLengthUnitInstance = bareLength(KDistanceUnit.ROMAN_MILE)

/** 1 rod / perch ([KDistanceUnit.ROD]). */
val rods: KLengthUnitInstance = bareLength(KDistanceUnit.ROD)

/** 1 league ([KDistanceUnit.LEAGUE]). */
val leagues: KLengthUnitInstance = bareLength(KDistanceUnit.LEAGUE)

/** 1 cable length ([KDistanceUnit.CABLE_LENGTH]). */
val cableLengths: KLengthUnitInstance = bareLength(KDistanceUnit.CABLE_LENGTH)

/** 1 verst / Werst ([KDistanceUnit.VERST]). */
val versts: KLengthUnitInstance = bareLength(KDistanceUnit.VERST)

/** 1 Prussian mile ([KDistanceUnit.PRUSSIAN_MILE]). */
val prussianMiles: KLengthUnitInstance = bareLength(KDistanceUnit.PRUSSIAN_MILE)
