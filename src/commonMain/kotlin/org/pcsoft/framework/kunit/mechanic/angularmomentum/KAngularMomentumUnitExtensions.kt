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

package org.pcsoft.framework.kunit.mechanic.angularmomentum

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 angular-momentum templates: one property per named unit on the prefix builder. The
// group accepts *any* magnitude, so the properties hang on the common base [KPrefixBuilder]. Use with
// `of`/`into`, e.g. `2 of milli.jouleSeconds`.

private fun prefixedAngularMomentum(builder: KPrefixBuilder, unit: KAngularMomentumUnit): KAngularMomentumUnitInstance =
    angularMomentumInstanceOf(builder.prefix.factor * unit.baseValue * KGM2S_IN_BASE)

/** Prefixed kg·m²/s, e.g. `milli.kilogramMetersSquaredPerSecond`. */
val KPrefixBuilder.kilogramMetersSquaredPerSecond: KAngularMomentumUnitInstance
    get() = prefixedAngularMomentum(this, KAngularMomentumUnit.KILOGRAM_METERS_SQUARED_PER_SECOND)

/** Prefixed N·m·s, e.g. `kilo.newtonMeterSeconds`. */
val KPrefixBuilder.newtonMeterSeconds: KAngularMomentumUnitInstance
    get() = prefixedAngularMomentum(this, KAngularMomentumUnit.NEWTON_METER_SECOND)

/** Prefixed J·s, e.g. `milli.jouleSeconds`, `femto.jouleSeconds`. */
val KPrefixBuilder.jouleSeconds: KAngularMomentumUnitInstance
    get() = prefixedAngularMomentum(this, KAngularMomentumUnit.JOULE_SECOND)

/** Prefixed g·cm²/s, e.g. `kilo.gramCentimetersSquaredPerSecond`. */
val KPrefixBuilder.gramCentimetersSquaredPerSecond: KAngularMomentumUnitInstance
    get() = prefixedAngularMomentum(this, KAngularMomentumUnit.GRAM_CENTIMETERS_SQUARED_PER_SECOND)
