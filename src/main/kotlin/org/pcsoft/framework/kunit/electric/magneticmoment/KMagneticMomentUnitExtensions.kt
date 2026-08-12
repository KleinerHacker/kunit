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

package org.pcsoft.framework.kunit.electric.magneticmoment

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 magnetic moment templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `5 of milli.ampereSquareMeters`.

private fun prefixedMagneticMoment(
    builder: KPrefixBuilder,
    unit: KMagneticMomentUnit
): KMagneticMomentUnitInstance =
    magneticMomentInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed ampere square meters, e.g. `milli.ampereSquareMeters`. */
val KPrefixBuilder.ampereSquareMeters: KMagneticMomentUnitInstance
    get() = prefixedMagneticMoment(this, KMagneticMomentUnit.AMPERE_SQUARE_METER)

/** Prefixed joules per tesla, e.g. `milli.joulesPerTesla`. */
val KPrefixBuilder.joulesPerTesla: KMagneticMomentUnitInstance
    get() = prefixedMagneticMoment(this, KMagneticMomentUnit.JOULE_PER_TESLA)

/** Prefixed Bohr magnetons, e.g. `kilo.bohrMagnetons`. */
val KPrefixBuilder.bohrMagnetons: KMagneticMomentUnitInstance
    get() = prefixedMagneticMoment(this, KMagneticMomentUnit.BOHR_MAGNETON)

/** Prefixed nuclear magnetons, e.g. `kilo.nuclearMagnetons`. */
val KPrefixBuilder.nuclearMagnetons: KMagneticMomentUnitInstance
    get() = prefixedMagneticMoment(this, KMagneticMomentUnit.NUCLEAR_MAGNETON)
