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

package org.pcsoft.framework.kunit.electric.flux

// Value-1 electric flux templates for the named units of the group, used with `of`/`into`
// (`5 of voltMeters`). Prefixed forms live in KElectricFluxUnitExtensions.kt.

/** 1 V·m ([KElectricFluxUnit.VOLT_METER]), the group's base unit. */
val voltMeters: KElectricFluxUnitInstance = electricFluxOfUnit(KElectricFluxUnit.VOLT_METER)

/** 1 V·cm ([KElectricFluxUnit.VOLT_CENTIMETER], 0.01 V·m). */
val voltCentimeters: KElectricFluxUnitInstance = electricFluxOfUnit(KElectricFluxUnit.VOLT_CENTIMETER)
