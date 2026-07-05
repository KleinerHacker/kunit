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

import org.pcsoft.framework.kunit.KUnitPrefix
import kotlin.math.pow

// SI-prefix `infix` constructors for the distance group. For a plain length the right operand is a bare
// [KDistanceUnit] (e.g. the `meters` alias, see `KLengthUnitBareValues.kt`) and the result is a
// [KLengthUnitInstance] (exponent 1), e.g. `5 kilo meters` == `5000.meters`. For area/volume the right
// operand is a bare area/volume token (e.g. `squareMeters`, `cubicMeters`, see `KAreaUnitBareValues.kt`
// / `KVolumeUnitBareValues.kt`), and the SI prefix scales the *linear* base unit before it is
// squared/cubed: `5 kilo squareMeters` == `5 * (1000 m)^2` == 5_000_000 m² (i.e. 5 square kilometers).

private fun prefixedLength(value: Number, prefix: KUnitPrefix, unit: KDistanceUnit): KLengthUnitInstance =
    lengthOf(value.toDouble() * prefix.factor * unit.baseValue)

private fun prefixedArea(value: Number, prefix: KUnitPrefix, unit: KDistanceUnit): KAreaUnitInstance =
    areaOf(value.toDouble() * (prefix.factor * unit.baseValue).pow(2))

private fun prefixedVolume(value: Number, prefix: KUnitPrefix, unit: KDistanceUnit): KVolumeUnitInstance =
    volumeOf(value.toDouble() * (prefix.factor * unit.baseValue).pow(3))

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

/** Scales an area [unit] by [KUnitPrefix.QUETTA] (linear, then squared), e.g. (5 quetta squareMeters). */
infix fun Number.quetta(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.QUETTA, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.RONNA] (linear, then squared), e.g. (5 ronna squareMeters). */
infix fun Number.ronna(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.RONNA, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.YOTTA] (linear, then squared), e.g. (5 yotta squareMeters). */
infix fun Number.yotta(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.YOTTA, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.ZETTA] (linear, then squared), e.g. (5 zetta squareMeters). */
infix fun Number.zetta(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.ZETTA, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.EXA] (linear, then squared), e.g. (5 exa squareMeters). */
infix fun Number.exa(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.EXA, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.PETA] (linear, then squared), e.g. (5 peta squareMeters). */
infix fun Number.peta(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.PETA, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.TERA] (linear, then squared), e.g. (5 tera squareMeters). */
infix fun Number.tera(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.TERA, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.GIGA] (linear, then squared), e.g. (5 giga squareMeters). */
infix fun Number.giga(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.GIGA, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.MEGA] (linear, then squared), e.g. (5 mega squareMeters). */
infix fun Number.mega(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.MEGA, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.KILO] (linear, then squared), e.g. (5 kilo squareMeters). */
infix fun Number.kilo(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.KILO, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.HECTO] (linear, then squared), e.g. (5 hecto squareMeters). */
infix fun Number.hecto(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.HECTO, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.DECA] (linear, then squared), e.g. (5 deca squareMeters). */
infix fun Number.deca(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.DECA, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.DECI] (linear, then squared), e.g. (5 deci squareMeters). */
infix fun Number.deci(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.DECI, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.CENTI] (linear, then squared), e.g. (5 centi squareMeters). */
infix fun Number.centi(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.CENTI, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.MILLI] (linear, then squared), e.g. (5 milli squareMeters). */
infix fun Number.milli(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.MILLI, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.MICRO] (linear, then squared), e.g. (5 micro squareMeters). */
infix fun Number.micro(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.MICRO, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.NANO] (linear, then squared), e.g. (5 nano squareMeters). */
infix fun Number.nano(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.NANO, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.PICO] (linear, then squared), e.g. (5 pico squareMeters). */
infix fun Number.pico(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.PICO, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.FEMTO] (linear, then squared), e.g. (5 femto squareMeters). */
infix fun Number.femto(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.FEMTO, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.ATTO] (linear, then squared), e.g. (5 atto squareMeters). */
infix fun Number.atto(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.ATTO, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.ZEPTO] (linear, then squared), e.g. (5 zepto squareMeters). */
infix fun Number.zepto(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.ZEPTO, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.YOCTO] (linear, then squared), e.g. (5 yocto squareMeters). */
infix fun Number.yocto(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.YOCTO, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.RONTO] (linear, then squared), e.g. (5 ronto squareMeters). */
infix fun Number.ronto(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.RONTO, unit.unit)

/** Scales an area [unit] by [KUnitPrefix.QUECTO] (linear, then squared), e.g. (5 quecto squareMeters). */
infix fun Number.quecto(unit: KDistanceAreaUnit): KAreaUnitInstance = prefixedArea(this, KUnitPrefix.QUECTO, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.QUETTA] (linear, then cubed), e.g. (5 quetta cubicMeters). */
infix fun Number.quetta(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.QUETTA, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.RONNA] (linear, then cubed), e.g. (5 ronna cubicMeters). */
infix fun Number.ronna(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.RONNA, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.YOTTA] (linear, then cubed), e.g. (5 yotta cubicMeters). */
infix fun Number.yotta(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.YOTTA, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.ZETTA] (linear, then cubed), e.g. (5 zetta cubicMeters). */
infix fun Number.zetta(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.ZETTA, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.EXA] (linear, then cubed), e.g. (5 exa cubicMeters). */
infix fun Number.exa(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.EXA, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.PETA] (linear, then cubed), e.g. (5 peta cubicMeters). */
infix fun Number.peta(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.PETA, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.TERA] (linear, then cubed), e.g. (5 tera cubicMeters). */
infix fun Number.tera(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.TERA, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.GIGA] (linear, then cubed), e.g. (5 giga cubicMeters). */
infix fun Number.giga(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.GIGA, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.MEGA] (linear, then cubed), e.g. (5 mega cubicMeters). */
infix fun Number.mega(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.MEGA, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.KILO] (linear, then cubed), e.g. (5 kilo cubicMeters). */
infix fun Number.kilo(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.KILO, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.HECTO] (linear, then cubed), e.g. (5 hecto cubicMeters). */
infix fun Number.hecto(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.HECTO, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.DECA] (linear, then cubed), e.g. (5 deca cubicMeters). */
infix fun Number.deca(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.DECA, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.DECI] (linear, then cubed), e.g. (5 deci cubicMeters). */
infix fun Number.deci(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.DECI, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.CENTI] (linear, then cubed), e.g. (5 centi cubicMeters). */
infix fun Number.centi(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.CENTI, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.MILLI] (linear, then cubed), e.g. (5 milli cubicMeters). */
infix fun Number.milli(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.MILLI, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.MICRO] (linear, then cubed), e.g. (5 micro cubicMeters). */
infix fun Number.micro(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.MICRO, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.NANO] (linear, then cubed), e.g. (5 nano cubicMeters). */
infix fun Number.nano(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.NANO, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.PICO] (linear, then cubed), e.g. (5 pico cubicMeters). */
infix fun Number.pico(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.PICO, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.FEMTO] (linear, then cubed), e.g. (5 femto cubicMeters). */
infix fun Number.femto(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.FEMTO, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.ATTO] (linear, then cubed), e.g. (5 atto cubicMeters). */
infix fun Number.atto(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.ATTO, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.ZEPTO] (linear, then cubed), e.g. (5 zepto cubicMeters). */
infix fun Number.zepto(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.ZEPTO, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.YOCTO] (linear, then cubed), e.g. (5 yocto cubicMeters). */
infix fun Number.yocto(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.YOCTO, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.RONTO] (linear, then cubed), e.g. (5 ronto cubicMeters). */
infix fun Number.ronto(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.RONTO, unit.unit)

/** Scales a volume [unit] by [KUnitPrefix.QUECTO] (linear, then cubed), e.g. (5 quecto cubicMeters). */
infix fun Number.quecto(unit: KDistanceVolumeUnit): KVolumeUnitInstance = prefixedVolume(this, KUnitPrefix.QUECTO, unit.unit)

