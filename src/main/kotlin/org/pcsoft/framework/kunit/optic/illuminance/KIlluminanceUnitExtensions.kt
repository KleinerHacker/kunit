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

package org.pcsoft.framework.kunit.optic.illuminance

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 illuminance templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `50 of kilo.lux`.

private fun prefixedIlluminance(builder: KPrefixBuilder, unit: KIlluminanceUnit): KIlluminanceUnitInstance =
    illuminanceInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed lux, e.g. `kilo.lux` (klx), `milli.lux` (mlx = nox). */
val KPrefixBuilder.lux: KIlluminanceUnitInstance
    get() = prefixedIlluminance(this, KIlluminanceUnit.LUX)

/** Prefixed phots, e.g. `milli.phots`. */
val KPrefixBuilder.phots: KIlluminanceUnitInstance
    get() = prefixedIlluminance(this, KIlluminanceUnit.PHOT)

/** Prefixed foot-candles, e.g. `kilo.footCandles`. */
val KPrefixBuilder.footCandles: KIlluminanceUnitInstance
    get() = prefixedIlluminance(this, KIlluminanceUnit.FOOT_CANDLE)

/** Prefixed nox, e.g. `milli.nox`. */
val KPrefixBuilder.nox: KIlluminanceUnitInstance
    get() = prefixedIlluminance(this, KIlluminanceUnit.NOX)
