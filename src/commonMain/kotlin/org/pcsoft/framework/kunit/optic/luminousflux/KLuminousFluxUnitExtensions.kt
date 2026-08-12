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

package org.pcsoft.framework.kunit.optic.luminousflux

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 luminous flux templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `1.5 of kilo.lumens`.

private fun prefixedLuminousFlux(builder: KPrefixBuilder, unit: KLuminousFluxUnit): KLuminousFluxUnitInstance =
    luminousFluxInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed lumens, e.g. `kilo.lumens` (klm), `milli.lumens` (mlm). */
val KPrefixBuilder.lumens: KLuminousFluxUnitInstance
    get() = prefixedLuminousFlux(this, KLuminousFluxUnit.LUMEN)

/** Prefixed candela steradians, e.g. `kilo.candelaSteradians`. */
val KPrefixBuilder.candelaSteradians: KLuminousFluxUnitInstance
    get() = prefixedLuminousFlux(this, KLuminousFluxUnit.CANDELA_STERADIAN)
