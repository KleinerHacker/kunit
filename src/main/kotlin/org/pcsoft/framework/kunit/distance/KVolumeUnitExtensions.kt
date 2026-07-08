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

// Prefixed, value-1 templates for the named volume special units (e.g. `milli.liters` = 1 mL). The plain
// per-length volume has no prefixed token (build it via `pow`, e.g. `2 of kilo.meters pow 3`).

private fun prefixedVolume(builder: KPrefixBuilder, cubicMeters: Double): KVolumeUnitInstance =
    volumeOf(builder.prefix.factor * cubicMeters)

/** Prefixed liters, e.g. `milli.liters` = 1 mL, `hecto.liters` = 1 hL. */
val KPrefixBuilder.liters: KVolumeUnitInstance get() = prefixedVolume(this, 0.001)

/** Prefixed US liquid gallons. */
val KPrefixBuilder.usGallons: KVolumeUnitInstance get() = prefixedVolume(this, 0.003785411784)

/** Prefixed imperial gallons. */
val KPrefixBuilder.imperialGallons: KVolumeUnitInstance get() = prefixedVolume(this, 0.00454609)

/** Prefixed US fluid ounces. */
val KPrefixBuilder.usFluidOunces: KVolumeUnitInstance get() = prefixedVolume(this, 2.95735295625e-5)

/** Prefixed oil barrels. */
val KPrefixBuilder.oilBarrels: KVolumeUnitInstance get() = prefixedVolume(this, 0.158987294928)
