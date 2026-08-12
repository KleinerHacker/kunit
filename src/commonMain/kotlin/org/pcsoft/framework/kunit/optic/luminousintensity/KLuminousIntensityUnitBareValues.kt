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

package org.pcsoft.framework.kunit.optic.luminousintensity

// Value-1 luminous intensity templates for the named units of the group, used with `of`/`into`
// (`5 of candelas`, `i into candlepower`).

/** 1 candela ([KLuminousIntensityUnit.CANDELA]), the group's base unit. */
val candelas: KLuminousIntensityUnitInstance = luminousIntensityOf(KLuminousIntensityUnit.CANDELA.baseValue)

/** 1 Hefner candle ([KLuminousIntensityUnit.HEFNER_CANDLE], 0.903 cd). */
val hefnerCandles: KLuminousIntensityUnitInstance =
    luminousIntensityOf(KLuminousIntensityUnit.HEFNER_CANDLE.baseValue)

/** 1 candlepower ([KLuminousIntensityUnit.CANDLEPOWER], 0.981 cd). */
val candlepower: KLuminousIntensityUnitInstance =
    luminousIntensityOf(KLuminousIntensityUnit.CANDLEPOWER.baseValue)

/** 1 carcel ([KLuminousIntensityUnit.CARCEL], 9.74 cd). */
val carcels: KLuminousIntensityUnitInstance = luminousIntensityOf(KLuminousIntensityUnit.CARCEL.baseValue)
