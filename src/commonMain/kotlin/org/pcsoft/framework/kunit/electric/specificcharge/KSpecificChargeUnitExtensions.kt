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

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 specific charge templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `2.58 of milli.coulombsPerKilogram`.

private fun prefixedSpecificCharge(
    builder: KPrefixBuilder,
    unit: KSpecificChargeUnit
): KSpecificChargeUnitInstance = specificChargeInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed coulombs per kilogram, e.g. `milli.coulombsPerKilogram`, `giga.coulombsPerKilogram`. */
val KPrefixBuilder.coulombsPerKilogram: KSpecificChargeUnitInstance
    get() = prefixedSpecificCharge(this, KSpecificChargeUnit.COULOMB_PER_KILOGRAM)

/** Prefixed roentgens, e.g. `milli.roentgens`, `micro.roentgens`. */
val KPrefixBuilder.roentgens: KSpecificChargeUnitInstance
    get() = prefixedSpecificCharge(this, KSpecificChargeUnit.ROENTGEN)
