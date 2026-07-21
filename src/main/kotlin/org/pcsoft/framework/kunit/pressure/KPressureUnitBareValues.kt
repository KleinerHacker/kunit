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

package org.pcsoft.framework.kunit.pressure

// Value-1 pressure templates for the genuinely named units of the group, used with `of`/`into`
// (`2 of bars`, `p into atmospheres`). Prefix-derivable spellings (kPa, MPa = N/mm², hPa) are reached
// via the prefix builders in `KPressureUnitExtensions.kt` and therefore have no own tokens here.

/** 1 pascal ([KPressureUnit.PASCAL]). */
val pascals: KPressureUnitInstance = pressureOfUnit(KPressureUnit.PASCAL)

/** 1 bar ([KPressureUnit.BAR], 100 000 Pa). */
val bars: KPressureUnitInstance = pressureOfUnit(KPressureUnit.BAR)

/** 1 standard atmosphere ([KPressureUnit.ATMOSPHERE], 101 325 Pa). */
val atmospheres: KPressureUnitInstance = pressureOfUnit(KPressureUnit.ATMOSPHERE)

/** 1 psi ([KPressureUnit.PSI], 6894.757 Pa). */
val psis: KPressureUnitInstance = pressureOfUnit(KPressureUnit.PSI)

/** 1 Torr ([KPressureUnit.TORR], ≈ 133.322 Pa, = 1 mmHg). */
val torrs: KPressureUnitInstance = pressureOfUnit(KPressureUnit.TORR)
