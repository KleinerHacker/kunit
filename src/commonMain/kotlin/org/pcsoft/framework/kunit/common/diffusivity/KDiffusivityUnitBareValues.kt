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

package org.pcsoft.framework.kunit.common.diffusivity

// Value-1 diffusivity templates for the named units of the group, used with `of`/`into`
// (`111 of squareMillimetersPerSecond` as a thermal diffusivity, `1 of centistokes` as a kinematic
// viscosity). Prefixed forms live in KDiffusivityUnitExtensions.kt.

/** 1 square meter per second ([KDiffusivityUnit.SQUARE_METER_PER_SECOND]), the group's base unit. */
val squareMetersPerSecond: KDiffusivityUnitInstance =
    diffusivityOfUnit(KDiffusivityUnit.SQUARE_METER_PER_SECOND)

/**
 * 1 square millimeter per second ([KDiffusivityUnit.SQUARE_MILLIMETER_PER_SECOND], 1e-6 m²/s) -
 * the magnitude material tables use.
 */
val squareMillimetersPerSecond: KDiffusivityUnitInstance =
    diffusivityOfUnit(KDiffusivityUnit.SQUARE_MILLIMETER_PER_SECOND)

/** 1 square foot per hour ([KDiffusivityUnit.SQUARE_FOOT_PER_HOUR], ≈ 2.58064e-5 m²/s). */
val squareFeetPerHour: KDiffusivityUnitInstance =
    diffusivityOfUnit(KDiffusivityUnit.SQUARE_FOOT_PER_HOUR)

/** 1 stokes ([KDiffusivityUnit.STOKES], 1 cm²/s = 1e-4 m²/s) - the CGS kinematic-viscosity unit. */
val stokes: KDiffusivityUnitInstance = diffusivityOfUnit(KDiffusivityUnit.STOKES)

/** 1 centistokes ([KDiffusivityUnit.CENTISTOKES], 1e-6 m²/s = 1 mm²/s; water at 20 °C ≈ 1 cSt). */
val centistokes: KDiffusivityUnitInstance = diffusivityOfUnit(KDiffusivityUnit.CENTISTOKES)
