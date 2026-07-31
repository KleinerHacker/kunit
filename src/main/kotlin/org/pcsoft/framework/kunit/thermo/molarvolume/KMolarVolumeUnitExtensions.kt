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

package org.pcsoft.framework.kunit.thermo.molarvolume

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 molar volume templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `22.4 of milli.cubicMetersPerMole`.

private fun prefixedMolarVolume(builder: KPrefixBuilder, unit: KMolarVolumeUnit): KMolarVolumeUnitInstance =
    molarVolumeInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed cubic meters per mole, e.g. `milli.cubicMetersPerMole` (= l/mol). */
val KPrefixBuilder.cubicMetersPerMole: KMolarVolumeUnitInstance
    get() = prefixedMolarVolume(this, KMolarVolumeUnit.CUBIC_METERS_PER_MOLE)

/** Prefixed liters per mole, e.g. `milli.litersPerMole` (ml/mol). */
val KPrefixBuilder.litersPerMole: KMolarVolumeUnitInstance
    get() = prefixedMolarVolume(this, KMolarVolumeUnit.LITERS_PER_MOLE)

/** Prefixed cubic centimeters per mole, e.g. `kilo.cubicCentimetersPerMole`. */
val KPrefixBuilder.cubicCentimetersPerMole: KMolarVolumeUnitInstance
    get() = prefixedMolarVolume(this, KMolarVolumeUnit.CUBIC_CENTIMETERS_PER_MOLE)
