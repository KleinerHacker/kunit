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

package org.pcsoft.framework.kunit.optic.radiantintensity

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 radiant intensity templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `250 of milli.wattsPerSteradian`.

private fun prefixedRadiantIntensity(
    builder: KPrefixBuilder,
    unit: KRadiantIntensityUnit
): KRadiantIntensityUnitInstance =
    radiantIntensityInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed watts per steradian, e.g. `milli.wattsPerSteradian`, `kilo.wattsPerSteradian`. */
val KPrefixBuilder.wattsPerSteradian: KRadiantIntensityUnitInstance
    get() = prefixedRadiantIntensity(this, KRadiantIntensityUnit.WATT_PER_STERADIAN)
