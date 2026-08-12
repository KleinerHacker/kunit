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

package org.pcsoft.framework.kunit.electric.specificcharge

// Value-1 specific charge templates for the named units of the group, used with `of`/`into`
// (`2.58 of milli.coulombsPerKilogram`). Prefixed forms live in KSpecificChargeUnitExtensions.kt.

/** 1 C/kg ([KSpecificChargeUnit.COULOMB_PER_KILOGRAM]), the group's base unit. */
val coulombsPerKilogram: KSpecificChargeUnitInstance =
    specificChargeOfUnit(KSpecificChargeUnit.COULOMB_PER_KILOGRAM)

/** 1 R ([KSpecificChargeUnit.ROENTGEN], 2.58e-4 C/kg) - the historical ionisation dose unit. */
val roentgens: KSpecificChargeUnitInstance = specificChargeOfUnit(KSpecificChargeUnit.ROENTGEN)
