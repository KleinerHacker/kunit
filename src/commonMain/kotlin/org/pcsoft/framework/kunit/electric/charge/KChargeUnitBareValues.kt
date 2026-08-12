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

package org.pcsoft.framework.kunit.electric.charge

// Bare, value-1 charge tokens (each = 1 unit, normalized to coulombs). Vocabulary for building
// (`10 of coulombs`) and reading (`q into ampereHours`); combine with the prefix builders
// (`milli.ampereHours`). Prefixed forms live in KChargeUnitExtensions.kt.

/** 1 coulomb ([KChargeUnit.COULOMB]). */
val coulombs: KChargeUnitInstance = chargeInstanceOf(KChargeUnit.COULOMB.baseValue)

/** 1 ampere second ([KChargeUnit.AMPERE_SECOND], 1 C). */
val ampereSeconds: KChargeUnitInstance = chargeInstanceOf(KChargeUnit.AMPERE_SECOND.baseValue)

/** 1 ampere hour ([KChargeUnit.AMPERE_HOUR], 3600 C). */
val ampereHours: KChargeUnitInstance = chargeInstanceOf(KChargeUnit.AMPERE_HOUR.baseValue)

/** 1 abcoulomb ([KChargeUnit.ABCOULOMB], CGS-EMU, 10 C). */
val abcoulombs: KChargeUnitInstance = chargeInstanceOf(KChargeUnit.ABCOULOMB.baseValue)

/** 1 statcoulomb ([KChargeUnit.STATCOULOMB], CGS-ESU, 3.335641e-10 C). */
val statcoulombs: KChargeUnitInstance = chargeInstanceOf(KChargeUnit.STATCOULOMB.baseValue)

/** 1 faraday ([KChargeUnit.FARADAY], 96485.332 C). */
val faradays: KChargeUnitInstance = chargeInstanceOf(KChargeUnit.FARADAY.baseValue)

/** 1 elementary charge ([KChargeUnit.ELEMENTARY_CHARGE], 1.602176634e-19 C). */
val elementaryCharges: KChargeUnitInstance = chargeInstanceOf(KChargeUnit.ELEMENTARY_CHARGE.baseValue)
