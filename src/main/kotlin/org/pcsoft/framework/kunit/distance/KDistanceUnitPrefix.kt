/*
 * Copyright (c) KleinerHacker alias Pfeiffer C Soft 2026.
 * This work is licensed under the Apache License, Version 2.0.
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, this software is distributed on an â€œAS ISâ€ BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations.
 */

package org.pcsoft.framework.kunit.distance

import org.pcsoft.framework.kunit.KUnitPrefix

// SI-prefix `infix` constructors for the distance group. The right operand is a bare [KDistanceUnit]
// (e.g. the `meters` alias, see `KLengthUnitBareValues.kt`) and the result is a [KLengthUnitInstance]
// (exponent 1), e.g. `5 kilo meters` == `5000.meters`.
//
// There are intentionally no prefixed area/volume constructors (`5 kilo squareMeters` etc.): a scaled
// area/volume is written as a power of the scaled length, e.g. `(5 kilo meters) pow 2` == 5 (km)Â² or
// `(2 kilo meters) pow 3` == 2 (km)Â³ (see KDistanceUnitInstance.pow).

private fun prefixedLength(value: Number, prefix: KUnitPrefix, unit: KDistanceUnit): KLengthUnitInstance =
    lengthOf(value.toDouble() * prefix.factor * unit.baseValue)

/** Scales a length [unit] by [KUnitPrefix.QUETTA], e.g. (5 quetta meters). */
infix fun Number.quetta(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.QUETTA, unit)

/** Scales a length [unit] by [KUnitPrefix.RONNA], e.g. (5 ronna meters). */
infix fun Number.ronna(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.RONNA, unit)

/** Scales a length [unit] by [KUnitPrefix.YOTTA], e.g. (5 yotta meters). */
infix fun Number.yotta(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.YOTTA, unit)

/** Scales a length [unit] by [KUnitPrefix.ZETTA], e.g. (5 zetta meters). */
infix fun Number.zetta(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.ZETTA, unit)

/** Scales a length [unit] by [KUnitPrefix.EXA], e.g. (5 exa meters). */
infix fun Number.exa(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.EXA, unit)

/** Scales a length [unit] by [KUnitPrefix.PETA], e.g. (5 peta meters). */
infix fun Number.peta(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.PETA, unit)

/** Scales a length [unit] by [KUnitPrefix.TERA], e.g. (5 tera meters). */
infix fun Number.tera(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.TERA, unit)

/** Scales a length [unit] by [KUnitPrefix.GIGA], e.g. (5 giga meters). */
infix fun Number.giga(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.GIGA, unit)

/** Scales a length [unit] by [KUnitPrefix.MEGA], e.g. (5 mega meters). */
infix fun Number.mega(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.MEGA, unit)

/** Scales a length [unit] by [KUnitPrefix.KILO], e.g. (5 kilo meters). */
infix fun Number.kilo(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.KILO, unit)

/** Scales a length [unit] by [KUnitPrefix.HECTO], e.g. (5 hecto meters). */
infix fun Number.hecto(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.HECTO, unit)

/** Scales a length [unit] by [KUnitPrefix.DECA], e.g. (5 deca meters). */
infix fun Number.deca(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.DECA, unit)

/** Scales a length [unit] by [KUnitPrefix.DECI], e.g. (5 deci meters). */
infix fun Number.deci(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.DECI, unit)

/** Scales a length [unit] by [KUnitPrefix.CENTI], e.g. (5 centi meters). */
infix fun Number.centi(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.CENTI, unit)

/** Scales a length [unit] by [KUnitPrefix.MILLI], e.g. (5 milli meters). */
infix fun Number.milli(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.MILLI, unit)

/** Scales a length [unit] by [KUnitPrefix.MICRO], e.g. (5 micro meters). */
infix fun Number.micro(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.MICRO, unit)

/** Scales a length [unit] by [KUnitPrefix.NANO], e.g. (5 nano meters). */
infix fun Number.nano(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.NANO, unit)

/** Scales a length [unit] by [KUnitPrefix.PICO], e.g. (5 pico meters). */
infix fun Number.pico(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.PICO, unit)

/** Scales a length [unit] by [KUnitPrefix.FEMTO], e.g. (5 femto meters). */
infix fun Number.femto(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.FEMTO, unit)

/** Scales a length [unit] by [KUnitPrefix.ATTO], e.g. (5 atto meters). */
infix fun Number.atto(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.ATTO, unit)

/** Scales a length [unit] by [KUnitPrefix.ZEPTO], e.g. (5 zepto meters). */
infix fun Number.zepto(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.ZEPTO, unit)

/** Scales a length [unit] by [KUnitPrefix.YOCTO], e.g. (5 yocto meters). */
infix fun Number.yocto(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.YOCTO, unit)

/** Scales a length [unit] by [KUnitPrefix.RONTO], e.g. (5 ronto meters). */
infix fun Number.ronto(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.RONTO, unit)

/** Scales a length [unit] by [KUnitPrefix.QUECTO], e.g. (5 quecto meters). */
infix fun Number.quecto(unit: KDistanceUnit): KLengthUnitInstance = prefixedLength(this, KUnitPrefix.QUECTO, unit)
