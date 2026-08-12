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

package org.pcsoft.framework.kunit.thermo.molality

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 molality templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `5 of milli.molesPerKilogram`.

private fun prefixedMolality(builder: KPrefixBuilder, unit: KMolalityUnit): KMolalityUnitInstance =
    molalityInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed moles per kilogram, e.g. `milli.molesPerKilogram`, `kilo.molesPerKilogram`. */
val KPrefixBuilder.molesPerKilogram: KMolalityUnitInstance
    get() = prefixedMolality(this, KMolalityUnit.MOLES_PER_KILOGRAM)

/** Prefixed millimoles per kilogram, e.g. `kilo.millimolesPerKilogram`. */
val KPrefixBuilder.millimolesPerKilogram: KMolalityUnitInstance
    get() = prefixedMolality(this, KMolalityUnit.MILLIMOLES_PER_KILOGRAM)
