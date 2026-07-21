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

/**
 * The `light` grouping builder for light-travel distances (light-second … light-year). Unlike other
 * length units these deliberately do **not** accept SI prefixes (a `kilo.lightYears` is physically
 * meaningless). Instead they are grouped behind the [light] entry point and read almost like prose:
 * `5 of light.seconds`, `3 of light.years`.
 *
 * Each property is a bare, value-1 length template (= 1 light-<unit>, normalized to meters) and is used
 * with the `of`/`into` verbs and the length operators, exactly like the other length tokens, e.g.
 * `5 of light.seconds`, `v into light.years`, `2 of light.years / seconds`.
 */
object KLengthLightUnitBuilder {

    /** 1 light-second ([KDistanceUnit.LIGHT_SECOND]). Build with `n of light.seconds`. */
    val seconds: KLengthUnitInstance = lengthOf(KDistanceUnit.LIGHT_SECOND.baseValue)

    /** 1 light-minute ([KDistanceUnit.LIGHT_MINUTE]). Build with `n of light.minutes`. */
    val minutes: KLengthUnitInstance = lengthOf(KDistanceUnit.LIGHT_MINUTE.baseValue)

    /** 1 light-hour ([KDistanceUnit.LIGHT_HOUR]). Build with `n of light.hours`. */
    val hours: KLengthUnitInstance = lengthOf(KDistanceUnit.LIGHT_HOUR.baseValue)

    /** 1 light-day ([KDistanceUnit.LIGHT_DAY]). Build with `n of light.days`. */
    val days: KLengthUnitInstance = lengthOf(KDistanceUnit.LIGHT_DAY.baseValue)

    /** 1 light-week ([KDistanceUnit.LIGHT_WEEK]). Build with `n of light.weeks`. */
    val weeks: KLengthUnitInstance = lengthOf(KDistanceUnit.LIGHT_WEEK.baseValue)

    /** 1 light-year ([KDistanceUnit.LIGHT_YEAR]). Build with `n of light.years`. */
    val years: KLengthUnitInstance = lengthOf(KDistanceUnit.LIGHT_YEAR.baseValue)
}

/** DSL entry point for the prefix-free light-travel distances, e.g. `5 of light.seconds`, `3 of light.years`. */
val light: KLengthLightUnitBuilder = KLengthLightUnitBuilder
