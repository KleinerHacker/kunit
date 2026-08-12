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

package org.pcsoft.framework.kunit.mechanic.viscosity

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 dynamic-viscosity templates: one property per named unit on the prefix builder. This
// is how the millipascal second (`milli.pascalSeconds`) and the centipoise (`centi.poises`) - the two
// everyday spellings for water-like viscosities - are expressed. Use with `of`/`into`.

private fun prefixedViscosity(builder: KPrefixBuilder, unit: KViscosityUnit): KViscosityUnitInstance =
    viscosityInstanceOf(builder.prefix.factor * unit.baseValue * PAS_IN_BASE)

/** Prefixed pascal seconds, e.g. `milli.pascalSeconds` (mPa·s = cP), `kilo.pascalSeconds`. */
val KPrefixBuilder.pascalSeconds: KViscosityUnitInstance get() = prefixedViscosity(this, KViscosityUnit.PASCAL_SECOND)

/** Prefixed poises, e.g. `centi.poises` (cP), `milli.poises`. */
val KPrefixBuilder.poises: KViscosityUnitInstance get() = prefixedViscosity(this, KViscosityUnit.POISE)

/** Prefixed lbf·s/ft², e.g. `milli.poundForceSecondsPerSquareFoot`. */
val KPrefixBuilder.poundForceSecondsPerSquareFoot: KViscosityUnitInstance
    get() = prefixedViscosity(this, KViscosityUnit.POUND_FORCE_SECOND_PER_SQUARE_FOOT)

/** Prefixed reyns, e.g. `micro.reyns`. */
val KPrefixBuilder.reyns: KViscosityUnitInstance get() = prefixedViscosity(this, KViscosityUnit.REYN)
