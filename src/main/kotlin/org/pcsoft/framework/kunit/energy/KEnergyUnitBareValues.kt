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

package org.pcsoft.framework.kunit.energy

// Bare, value-1 energy tokens (each = 1 unit, normalized to joules). Vocabulary for building
// (`10 of joules`) and reading (`w into calories`); combine with the prefix builders (`kilo.joules`).
// Prefixed forms live in KEnergyUnitExtensions.kt. The kilowatt hour has no token - build it as
// `kilo.watts * hours`.

/** 1 joule ([KEnergyUnit.JOULE]). */
val joules: KEnergyUnitInstance = energyInstanceOf(KEnergyUnit.JOULE.baseValue)

/** 1 erg ([KEnergyUnit.ERG], CGS, 1e-7 J). */
val ergs: KEnergyUnitInstance = energyInstanceOf(KEnergyUnit.ERG.baseValue)

/** 1 thermochemical calorie ([KEnergyUnit.CALORIE], 4.184 J). */
val calories: KEnergyUnitInstance = energyInstanceOf(KEnergyUnit.CALORIE.baseValue)

/** 1 electron volt ([KEnergyUnit.ELECTRON_VOLT], 1.602176634e-19 J). */
val electronVolts: KEnergyUnitInstance = energyInstanceOf(KEnergyUnit.ELECTRON_VOLT.baseValue)

/** 1 British thermal unit ([KEnergyUnit.BRITISH_THERMAL_UNIT], 1055.05585262 J). */
val britishThermalUnits: KEnergyUnitInstance = energyInstanceOf(KEnergyUnit.BRITISH_THERMAL_UNIT.baseValue)
