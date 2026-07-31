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

package org.pcsoft.framework.kunit.mechanic.momentum

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 momentum templates: one property per named unit on the prefix builder. Momentum
// accepts *any* magnitude, so the properties hang on the common base [KPrefixBuilder]. Use with
// `of`/`into`, e.g. `2 of kilo.newtonSeconds`, `p into milli.kilogramMetersPerSecond`.

private fun prefixedMomentum(builder: KPrefixBuilder, unit: KMomentumUnit): KMomentumUnitInstance =
    momentumInstanceOf(builder.prefix.factor * unit.baseValue * KGMS_IN_BASE)

/** Prefixed kg·m/s, e.g. `kilo.kilogramMetersPerSecond`, `milli.kilogramMetersPerSecond`. */
val KPrefixBuilder.kilogramMetersPerSecond: KMomentumUnitInstance
    get() = prefixedMomentum(this, KMomentumUnit.KILOGRAM_METERS_PER_SECOND)

/** Prefixed newton seconds, e.g. `kilo.newtonSeconds` (kN·s), `milli.newtonSeconds` (mN·s). */
val KPrefixBuilder.newtonSeconds: KMomentumUnitInstance
    get() = prefixedMomentum(this, KMomentumUnit.NEWTON_SECOND)

/** Prefixed g·cm/s, e.g. `kilo.gramCentimetersPerSecond`. */
val KPrefixBuilder.gramCentimetersPerSecond: KMomentumUnitInstance
    get() = prefixedMomentum(this, KMomentumUnit.GRAM_CENTIMETERS_PER_SECOND)

/** Prefixed lb·ft/s, e.g. `kilo.poundFeetPerSecond`. */
val KPrefixBuilder.poundFeetPerSecond: KMomentumUnitInstance
    get() = prefixedMomentum(this, KMomentumUnit.POUND_FEET_PER_SECOND)
