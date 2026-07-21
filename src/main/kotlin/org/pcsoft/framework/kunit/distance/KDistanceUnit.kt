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

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of length. [baseValue] is the factor to convert into the group's base
 * unit ([BASE], meter): `1 unit = baseValue * meter`.
 *
 * Example:
 * ```kotlin
 * KDistanceUnit.MILE.baseValue // 1609.344 (1 mile = 1609.344 meters)
 * ```
 */
enum class KDistanceUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Meter, the SI base unit of length; [baseValue] = 1.0 by definition. */
    METER("m", 1.0),

    /** International mile (statute mile), 1 mi = 1609.344 m. */
    MILE("mi", 1609.344),

    /** International nautical mile, 1 nmi = 1852 m (used in aviation/maritime navigation). */
    NAUTICAL_MILE("nmi", 1852.0),

    /** International yard, 1 yd = 0.9144 m. */
    YARD("yd", 0.9144),

    /** International foot, 1 ft = 0.3048 m. */
    FOOT("ft", 0.3048),

    /** International inch, 1 in = 0.0254 m. */
    INCH("in", 0.0254),

    /** Fathom, 1 ftm = 1.8288 m (= 2 yd; traditionally used for water depth). */
    FATHOM("ftm", 1.8288),

    /** Chain (surveyor's chain), 1 ch = 20.1168 m. */
    CHAIN("ch", 20.1168),

    /** Furlong, 1 fur = 201.168 m (= 10 ch). */
    FURLONG("fur", 201.168),

    /** Astronomical unit, 1 AU = 149 597 870 700 m (mean Earth-Sun distance). */
    ASTRONOMICAL_UNIT("AU", 1.495978707e11),

    /** Light-second, 1 ls = 299 792 458 m (distance light travels in one second; c = 299 792 458 m/s). */
    LIGHT_SECOND("ls", 299792458.0),

    /** Light-minute, 1 lmin = 17 987 547 480 m (distance light travels in one minute, c · 60 s). */
    LIGHT_MINUTE("lmin", 17987547480.0),

    /** Light-hour, 1 lh = 1 079 252 848 800 m (distance light travels in one hour, c · 3600 s). */
    LIGHT_HOUR("lh", 1079252848800.0),

    /** Light-day, 1 ld = 25 902 068 371 200 m (distance light travels in one day, c · 86 400 s). */
    LIGHT_DAY("ld", 25902068371200.0),

    /** Light-week, 1 lw = 181 314 478 598 400 m (distance light travels in one week, c · 604 800 s). */
    LIGHT_WEEK("lw", 181314478598400.0),

    /** Light-year, 1 ly = 9 460 730 472 580 800 m (distance light travels in one Julian year). */
    LIGHT_YEAR("ly", 9.4607304725808e15),

    /** Parsec, 1 pc ≈ 3.0856775814913673e16 m (commonly used in astronomy). */
    PARSEC("pc", 3.0856775814913673e16),

    /** Cubit (Elle), 1 cubit = 0.4572 m (historical unit based on the forearm length). */
    CUBIT("cubit", 0.4572),

    /** Roman foot (pes), 1 pes = 0.2957 m (historical Roman length unit). */
    ROMAN_FOOT("pes", 0.2957),

    /** Roman pace (passus), 1 passus = 1.4787 m (= 5 Roman feet; historical Roman length unit). */
    ROMAN_PACE("passus", 1.4787),

    /** Stadium, 1 stadium = 185.0 m (historical Greek/Roman length unit). */
    STADIUM("stadium", 185.0),

    /** Roman mile (mille passus), 1 mille passus = 1481.5 m (= 1000 Roman paces). */
    ROMAN_MILE("mp", 1481.5),

    /** Rod (perch), 1 rod = 5.0292 m (historical surveyor's unit; = 16.5 ft). */
    ROD("rod", 5.0292),

    /** League, 1 lea = 4828.032 m (historical unit; = 3 statute miles). */
    LEAGUE("lea", 4828.032),

    /** Cable length, 1 cable = 185.2 m (maritime unit; = 1/10 nautical mile). */
    CABLE_LENGTH("cable", 185.2),

    /** Verst (Werst), 1 verst = 1066.8 m (historical Russian length unit). */
    VERST("verst", 1066.8),

    /** Prussian mile, 1 prussian mile = 7532.5 m (historical German length unit). */
    PRUSSIAN_MILE("prussian mi", 7532.5);

    companion object {
        /**
         * The base unit of the length group; all internal values of [KLengthUnitInstance] are
         * normalized to this unit.
         */
        val BASE: KDistanceUnit = METER
    }
}
