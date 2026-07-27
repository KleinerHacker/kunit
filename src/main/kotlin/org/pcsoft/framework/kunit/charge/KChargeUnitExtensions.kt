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

package org.pcsoft.framework.kunit.charge

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 charge templates: one property per charge unit on the prefix builder (e.g.
// `milli.ampereHours` = 3.6 C, `kilo.coulombs` = 1000 C). Charge accepts *any* magnitude, so the
// properties hang on the common base [KPrefixBuilder]. Use with `of`/`into`, e.g.
// `2000 of milli.ampereHours`, `q into kilo.coulombs`.

private fun prefixedCharge(builder: KPrefixBuilder, unit: KChargeUnit): KChargeUnitInstance =
    chargeInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed coulombs, e.g. `kilo.coulombs` = 1000 C, `milli.coulombs` = 0.001 C. */
val KPrefixBuilder.coulombs: KChargeUnitInstance get() = prefixedCharge(this, KChargeUnit.COULOMB)

/** Prefixed ampere seconds, e.g. `milli.ampereSeconds`. */
val KPrefixBuilder.ampereSeconds: KChargeUnitInstance get() = prefixedCharge(this, KChargeUnit.AMPERE_SECOND)

/** Prefixed ampere hours, e.g. `milli.ampereHours` = 3.6 C (battery capacity). */
val KPrefixBuilder.ampereHours: KChargeUnitInstance get() = prefixedCharge(this, KChargeUnit.AMPERE_HOUR)

/** Prefixed abcoulombs, e.g. `kilo.abcoulombs`. */
val KPrefixBuilder.abcoulombs: KChargeUnitInstance get() = prefixedCharge(this, KChargeUnit.ABCOULOMB)

/** Prefixed statcoulombs, e.g. `kilo.statcoulombs`. */
val KPrefixBuilder.statcoulombs: KChargeUnitInstance get() = prefixedCharge(this, KChargeUnit.STATCOULOMB)

/** Prefixed faradays, e.g. `milli.faradays`. */
val KPrefixBuilder.faradays: KChargeUnitInstance get() = prefixedCharge(this, KChargeUnit.FARADAY)

/** Prefixed elementary charges, e.g. `mega.elementaryCharges`. */
val KPrefixBuilder.elementaryCharges: KChargeUnitInstance get() = prefixedCharge(this, KChargeUnit.ELEMENTARY_CHARGE)
