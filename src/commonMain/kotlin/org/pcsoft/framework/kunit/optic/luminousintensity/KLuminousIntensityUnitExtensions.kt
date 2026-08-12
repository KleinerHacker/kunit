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

package org.pcsoft.framework.kunit.optic.luminousintensity

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 luminous intensity templates: one property per named unit on the prefix builder.
// Luminous intensity accepts *any* magnitude, so both the augmenting and the diminishing builders are
// served by the common KPrefixBuilder base. Use with `of`/`into`, e.g. `250 of milli.candelas`.

private fun prefixedLuminousIntensity(
    builder: KPrefixBuilder,
    unit: KLuminousIntensityUnit
): KLuminousIntensityUnitInstance =
    luminousIntensityOf(builder.prefix.factor * unit.baseValue)

/** Prefixed candelas, e.g. `milli.candelas` (mcd), `kilo.candelas` (kcd). */
val KPrefixBuilder.candelas: KLuminousIntensityUnitInstance
    get() = prefixedLuminousIntensity(this, KLuminousIntensityUnit.CANDELA)

/** Prefixed Hefner candles, e.g. `milli.hefnerCandles`. */
val KPrefixBuilder.hefnerCandles: KLuminousIntensityUnitInstance
    get() = prefixedLuminousIntensity(this, KLuminousIntensityUnit.HEFNER_CANDLE)

/** Prefixed candlepower, e.g. `kilo.candlepower`. */
val KPrefixBuilder.candlepower: KLuminousIntensityUnitInstance
    get() = prefixedLuminousIntensity(this, KLuminousIntensityUnit.CANDLEPOWER)

/** Prefixed carcels, e.g. `milli.carcels`. */
val KPrefixBuilder.carcels: KLuminousIntensityUnitInstance
    get() = prefixedLuminousIntensity(this, KLuminousIntensityUnit.CARCEL)
