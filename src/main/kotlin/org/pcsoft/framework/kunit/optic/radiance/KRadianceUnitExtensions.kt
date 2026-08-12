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

package org.pcsoft.framework.kunit.optic.radiance

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 radiance templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `250 of milli.wattsPerSteradianSquareMeter`.

private fun prefixedRadiance(builder: KPrefixBuilder, unit: KRadianceUnit): KRadianceUnitInstance =
    radianceInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed watts per steradian square meter, e.g. `milli.wattsPerSteradianSquareMeter`. */
val KPrefixBuilder.wattsPerSteradianSquareMeter: KRadianceUnitInstance
    get() = prefixedRadiance(this, KRadianceUnit.WATT_PER_STERADIAN_SQUARE_METER)
