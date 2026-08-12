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

package org.pcsoft.framework.kunit.electric.molarconductivity

// Value-1 molar conductivity templates for the named units of the group, used with `of`/`into`
// (`12.6 of milli.siemensSquareMetersPerMole`). Prefixed forms live in
// KMolarConductivityUnitExtensions.kt.

/** 1 S·m²/mol ([KMolarConductivityUnit.SIEMENS_SQUARE_METER_PER_MOLE]), the group's base unit. */
val siemensSquareMetersPerMole: KMolarConductivityUnitInstance =
    molarConductivityOfUnit(KMolarConductivityUnit.SIEMENS_SQUARE_METER_PER_MOLE)

/** 1 S·cm²/mol ([KMolarConductivityUnit.SIEMENS_SQUARE_CENTIMETER_PER_MOLE], 1e-4 S·m²/mol). */
val siemensSquareCentimetersPerMole: KMolarConductivityUnitInstance =
    molarConductivityOfUnit(KMolarConductivityUnit.SIEMENS_SQUARE_CENTIMETER_PER_MOLE)
