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

package org.pcsoft.framework.kunit.distance

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 templates for the named area special units (e.g. `milli.hectares`). The plain
// per-length area has no prefixed token (build it via `pow`, e.g. `10 of kilo.meters pow 2`).

private fun prefixedArea(builder: KPrefixBuilder, squareMeters: Double): KAreaUnitInstance =
    areaOf(builder.prefix.factor * squareMeters)

/** Prefixed ares, e.g. `deca.ares`. */
val KPrefixBuilder.ares: KAreaUnitInstance get() = prefixedArea(this, 100.0)

/** Prefixed hectares, e.g. `kilo.hectares`. */
val KPrefixBuilder.hectares: KAreaUnitInstance get() = prefixedArea(this, 10_000.0)

/** Prefixed acres. */
val KPrefixBuilder.acres: KAreaUnitInstance get() = prefixedArea(this, 4046.8564224)
