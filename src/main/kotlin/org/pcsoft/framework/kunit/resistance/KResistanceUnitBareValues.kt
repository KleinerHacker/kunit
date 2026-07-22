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

package org.pcsoft.framework.kunit.resistance

// Bare, value-1 resistance tokens (each = 1 unit, normalized to ohms). Vocabulary for building
// (`10 of ohms`) and reading (`r into statohms`); combine with the prefix builders (`kilo.ohms`).
// Prefixed forms live in KResistanceUnitExtensions.kt.

/** 1 ohm ([KResistanceUnit.OHM]). */
val ohms: KResistanceUnitInstance = resistanceInstanceOf(KResistanceUnit.OHM.baseValue)

/** 1 statohm ([KResistanceUnit.STATOHM], CGS-ESU, 8.98755179e11 Ω). */
val statohms: KResistanceUnitInstance = resistanceInstanceOf(KResistanceUnit.STATOHM.baseValue)

/** 1 abohm ([KResistanceUnit.ABOHM], CGS-EMU, 10⁻⁹ Ω). */
val abohms: KResistanceUnitInstance = resistanceInstanceOf(KResistanceUnit.ABOHM.baseValue)

/** 1 international ohm ([KResistanceUnit.INTERNATIONAL_OHM], 1.000049 Ω). */
val internationalOhms: KResistanceUnitInstance = resistanceInstanceOf(KResistanceUnit.INTERNATIONAL_OHM.baseValue)

/** 1 legal ohm ([KResistanceUnit.LEGAL_OHM], 0.9972 Ω). */
val legalOhms: KResistanceUnitInstance = resistanceInstanceOf(KResistanceUnit.LEGAL_OHM.baseValue)

/** 1 Siemens mercury unit ([KResistanceUnit.SIEMENS_UNIT], 0.9534 Ω). */
val siemensUnits: KResistanceUnitInstance = resistanceInstanceOf(KResistanceUnit.SIEMENS_UNIT.baseValue)
