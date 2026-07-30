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

package org.pcsoft.framework.kunit.thermo.molarenergy

// Value-1 molar energy templates for the named units of the group, used with `of`/`into`
// (`286 of kilo.joulesPerMole`, `dH into caloriesPerMole`).

/** 1 joule per mole ([KMolarEnergyUnit.JOULE_PER_MOLE]), the group's base unit. */
val joulesPerMole: KMolarEnergyUnitInstance = molarEnergyOfUnit(KMolarEnergyUnit.JOULE_PER_MOLE)

/** 1 thermochemical calorie per mole ([KMolarEnergyUnit.CALORIE_PER_MOLE], 4.184 J/mol). */
val caloriesPerMole: KMolarEnergyUnitInstance = molarEnergyOfUnit(KMolarEnergyUnit.CALORIE_PER_MOLE)

/** 1 electronvolt per elementary entity ([KMolarEnergyUnit.ELECTRONVOLT_PER_ENTITY], ≈ 96485.33 J/mol). */
val electronVoltsPerEntity: KMolarEnergyUnitInstance = molarEnergyOfUnit(KMolarEnergyUnit.ELECTRONVOLT_PER_ENTITY)
