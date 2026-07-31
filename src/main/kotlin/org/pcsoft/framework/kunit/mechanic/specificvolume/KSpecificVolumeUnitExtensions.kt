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

package org.pcsoft.framework.kunit.mechanic.specificvolume

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 specific-volume templates: one property per named unit on the prefix builder. The
// group accepts *any* magnitude, so the properties hang on the common base [KPrefixBuilder]. Use with
// `of`/`into`, e.g. `5 of milli.cubicMetersPerKilogram`.

private fun prefixedSpecificVolume(builder: KPrefixBuilder, unit: KSpecificVolumeUnit): KSpecificVolumeUnitInstance =
    specificVolumeInstanceOf(builder.prefix.factor * unit.baseValue * M3KG_IN_BASE)

/** Prefixed m³/kg, e.g. `milli.cubicMetersPerKilogram`. */
val KPrefixBuilder.cubicMetersPerKilogram: KSpecificVolumeUnitInstance
    get() = prefixedSpecificVolume(this, KSpecificVolumeUnit.CUBIC_METERS_PER_KILOGRAM)

/** Prefixed l/kg, e.g. `milli.litersPerKilogram`. */
val KPrefixBuilder.litersPerKilogram: KSpecificVolumeUnitInstance
    get() = prefixedSpecificVolume(this, KSpecificVolumeUnit.LITERS_PER_KILOGRAM)

/** Prefixed cm³/g, e.g. `milli.cubicCentimetersPerGram`. */
val KPrefixBuilder.cubicCentimetersPerGram: KSpecificVolumeUnitInstance
    get() = prefixedSpecificVolume(this, KSpecificVolumeUnit.CUBIC_CENTIMETERS_PER_GRAM)

/** Prefixed ft³/lb, e.g. `milli.cubicFeetPerPound`. */
val KPrefixBuilder.cubicFeetPerPound: KSpecificVolumeUnitInstance
    get() = prefixedSpecificVolume(this, KSpecificVolumeUnit.CUBIC_FEET_PER_POUND)
