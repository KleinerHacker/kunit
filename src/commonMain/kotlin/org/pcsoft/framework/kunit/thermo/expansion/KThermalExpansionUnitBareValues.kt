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

package org.pcsoft.framework.kunit.thermo.expansion

// Value-1 thermal expansion templates for the named units of the group, used with `of`/`into`
// (`12 of ppmPerKelvin`, `alpha into perKelvin`).

/** 1 per kelvin ([KThermalExpansionUnit.PER_KELVIN]), the group's base unit. */
val perKelvin: KThermalExpansionUnitInstance = thermalExpansionOfUnit(KThermalExpansionUnit.PER_KELVIN)

/** 1 per degree Fahrenheit ([KThermalExpansionUnit.PER_FAHRENHEIT], 1.8 1/K). */
val perFahrenheit: KThermalExpansionUnitInstance =
    thermalExpansionOfUnit(KThermalExpansionUnit.PER_FAHRENHEIT)

/** 1 part per million per kelvin ([KThermalExpansionUnit.PPM_PER_KELVIN], 1e-6 1/K). */
val ppmPerKelvin: KThermalExpansionUnitInstance =
    thermalExpansionOfUnit(KThermalExpansionUnit.PPM_PER_KELVIN)
