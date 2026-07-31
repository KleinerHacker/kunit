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

package org.pcsoft.framework.kunit.common.diffusivity

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 diffusivity templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `111 of micro.squareMetersPerSecond`.

private fun prefixedDiffusivity(
    builder: KPrefixBuilder,
    unit: KDiffusivityUnit,
): KDiffusivityUnitInstance = diffusivityInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed square meters per second, e.g. `micro.squareMetersPerSecond`, `milli.squareMetersPerSecond`. */
val KPrefixBuilder.squareMetersPerSecond: KDiffusivityUnitInstance
    get() = prefixedDiffusivity(this, KDiffusivityUnit.SQUARE_METER_PER_SECOND)

/** Prefixed square millimeters per second, e.g. `milli.squareMillimetersPerSecond`. */
val KPrefixBuilder.squareMillimetersPerSecond: KDiffusivityUnitInstance
    get() = prefixedDiffusivity(this, KDiffusivityUnit.SQUARE_MILLIMETER_PER_SECOND)

/** Prefixed square feet per hour, e.g. `milli.squareFeetPerHour`. */
val KPrefixBuilder.squareFeetPerHour: KDiffusivityUnitInstance
    get() = prefixedDiffusivity(this, KDiffusivityUnit.SQUARE_FOOT_PER_HOUR)

/** Prefixed stokes, e.g. `centi.stokes` (cSt), `milli.stokes`. */
val KPrefixBuilder.stokes: KDiffusivityUnitInstance
    get() = prefixedDiffusivity(this, KDiffusivityUnit.STOKES)

/** Prefixed centistokes, e.g. `milli.centistokes`. */
val KPrefixBuilder.centistokes: KDiffusivityUnitInstance
    get() = prefixedDiffusivity(this, KDiffusivityUnit.CENTISTOKES)
