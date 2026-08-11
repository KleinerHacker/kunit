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

package org.pcsoft.framework.kunit.thermo.doserate

// Value-1 dose rate templates for the named units of the group, used with `of`/`into`
// (`0.1 of micro.sievertsPerHour`). Prefixed forms live in KDoseRateUnitExtensions.kt.

/** 1 Gy/s ([KDoseRateUnit.GRAY_PER_SECOND]), the group's base unit. */
val graysPerSecond: KDoseRateUnitInstance = doseRateOfUnit(KDoseRateUnit.GRAY_PER_SECOND)

/** 1 Gy/h ([KDoseRateUnit.GRAY_PER_HOUR], 1/3600 Gy/s). */
val graysPerHour: KDoseRateUnitInstance = doseRateOfUnit(KDoseRateUnit.GRAY_PER_HOUR)

/** 1 Sv/s ([KDoseRateUnit.SIEVERT_PER_SECOND]), the equivalent-dose spelling. */
val sievertsPerSecond: KDoseRateUnitInstance = doseRateOfUnit(KDoseRateUnit.SIEVERT_PER_SECOND)

/** 1 Sv/h ([KDoseRateUnit.SIEVERT_PER_HOUR], 1/3600 Gy/s), the radiation-protection unit. */
val sievertsPerHour: KDoseRateUnitInstance = doseRateOfUnit(KDoseRateUnit.SIEVERT_PER_HOUR)
