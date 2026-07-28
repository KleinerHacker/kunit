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

package org.pcsoft.framework.kunit.permeability

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 permeability templates: one property per unit on the prefix builder (e.g.
// `micro.henriesPerMeter` = 1e-6 H/m). Permeability accepts *any* magnitude, so the properties hang on the
// common base [KPrefixBuilder]. Use with `of`/`into`, e.g. `1.257 of micro.henriesPerMeter`,
// `mu into milli.henriesPerMeter`.

private fun prefixedPermeability(builder: KPrefixBuilder, unit: KPermeabilityUnit): KPermeabilityUnitInstance =
    permeabilityInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed henries per meter, e.g. `micro.henriesPerMeter` = 1e-6 H/m. */
val KPrefixBuilder.henriesPerMeter: KPermeabilityUnitInstance
    get() = prefixedPermeability(this, KPermeabilityUnit.HENRY_PER_METER)

/** Prefixed henries per centimeter, e.g. `micro.henriesPerCentimeter` = 1e-4 H/m. */
val KPrefixBuilder.henriesPerCentimeter: KPermeabilityUnitInstance
    get() = prefixedPermeability(this, KPermeabilityUnit.HENRY_PER_CENTIMETER)
