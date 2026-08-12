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

package org.pcsoft.framework.kunit.mechanic.inertia

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 moment-of-inertia templates: one property per named unit on the prefix builder. The
// moment of inertia accepts *any* magnitude, so the properties hang on the common base [KPrefixBuilder].
// Use with `of`/`into`, e.g. `2 of milli.kilogramMetersSquared`.

private fun prefixedInertia(builder: KPrefixBuilder, unit: KInertiaUnit): KInertiaUnitInstance =
    inertiaInstanceOf(builder.prefix.factor * unit.baseValue * KGM2_IN_BASE)

/** Prefixed kg·m², e.g. `milli.kilogramMetersSquared`, `kilo.kilogramMetersSquared`. */
val KPrefixBuilder.kilogramMetersSquared: KInertiaUnitInstance
    get() = prefixedInertia(this, KInertiaUnit.KILOGRAM_METERS_SQUARED)

/** Prefixed g·cm², e.g. `kilo.gramCentimetersSquared`. */
val KPrefixBuilder.gramCentimetersSquared: KInertiaUnitInstance
    get() = prefixedInertia(this, KInertiaUnit.GRAM_CENTIMETERS_SQUARED)

/** Prefixed lb·ft², e.g. `kilo.poundFeetSquared`. */
val KPrefixBuilder.poundFeetSquared: KInertiaUnitInstance
    get() = prefixedInertia(this, KInertiaUnit.POUND_FEET_SQUARED)
