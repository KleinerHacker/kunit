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

package org.pcsoft.framework.kunit.force

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 force templates: one property per named force unit on the prefix builder. This is
// how the kilonewton (`kilo.newtons`) and the kilopond / kilogram-force (`kilo.ponds`) are expressed -
// they have no own tokens. Use with `of`/`into`, e.g. `5 of kilo.newtons`, `v into kilo.ponds`.

private fun prefixedForce(builder: KPrefixBuilder, unit: KForceUnit): KForceUnitInstance =
    forceUnitInstanceOf(builder.prefix.factor * unit.baseValue * N_IN_BASE)

/** Prefixed newtons, e.g. `kilo.newtons` (kN), `mega.newtons` (MN). */
val KPrefixBuilder.newtons: KForceUnitInstance get() = prefixedForce(this, KForceUnit.NEWTON)

/** Prefixed dynes, e.g. `kilo.dynes`. */
val KPrefixBuilder.dynes: KForceUnitInstance get() = prefixedForce(this, KForceUnit.DYNE)

/** Prefixed ponds, e.g. `kilo.ponds` = 1 kp = 1 kgf (kilogram-force). */
val KPrefixBuilder.ponds: KForceUnitInstance get() = prefixedForce(this, KForceUnit.POND)
