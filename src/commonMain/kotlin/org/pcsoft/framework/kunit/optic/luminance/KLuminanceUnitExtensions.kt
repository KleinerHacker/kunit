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

package org.pcsoft.framework.kunit.optic.luminance

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 luminance templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `1.5 of kilo.candelasPerSquareMeter`.

private fun prefixedLuminance(builder: KPrefixBuilder, unit: KLuminanceUnit): KLuminanceUnitInstance =
    luminanceInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed candelas per square meter, e.g. `kilo.candelasPerSquareMeter`. */
val KPrefixBuilder.candelasPerSquareMeter: KLuminanceUnitInstance
    get() = prefixedLuminance(this, KLuminanceUnit.CANDELA_PER_SQUARE_METER)

/** Prefixed nits, e.g. `kilo.nits` - the display-industry spelling of `kilo.candelasPerSquareMeter`. */
val KPrefixBuilder.nits: KLuminanceUnitInstance
    get() = prefixedLuminance(this, KLuminanceUnit.CANDELA_PER_SQUARE_METER)

/** Prefixed stilbs, e.g. `milli.stilbs`. */
val KPrefixBuilder.stilbs: KLuminanceUnitInstance
    get() = prefixedLuminance(this, KLuminanceUnit.STILB)

/** Prefixed apostilbs, e.g. `kilo.apostilbs`. */
val KPrefixBuilder.apostilbs: KLuminanceUnitInstance
    get() = prefixedLuminance(this, KLuminanceUnit.APOSTILB)

/** Prefixed lamberts, e.g. `milli.lamberts`. */
val KPrefixBuilder.lamberts: KLuminanceUnitInstance
    get() = prefixedLuminance(this, KLuminanceUnit.LAMBERT)

/** Prefixed foot-lamberts, e.g. `kilo.footLamberts`. */
val KPrefixBuilder.footLamberts: KLuminanceUnitInstance
    get() = prefixedLuminance(this, KLuminanceUnit.FOOT_LAMBERT)
