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

package org.pcsoft.framework.kunit.mechanic.solidangle

import org.pcsoft.framework.kunit.KPrefixBuilder
import org.pcsoft.framework.kunit.KUnitDisplay

// Prefixed, value-1 solid-angle templates: one property per named unit on the prefix builder (e.g.
// `milli.steradians` = 0.001 sr). A solid angle accepts *any* magnitude, so the properties hang on the
// common base [KPrefixBuilder]. Use with `of`/`into`, e.g. `5 of milli.steradians`.

private fun prefixedSolidAngle(builder: KPrefixBuilder, unit: KSolidAngleUnit): KSolidAngleUnitInstance =
    solidAngleOf(builder.prefix.factor * unit.baseValue, KUnitDisplay(unit, builder.prefix.symbol))

/** Prefixed steradians, e.g. `milli.steradians` (msr), `micro.steradians` (µsr). */
val KPrefixBuilder.steradians: KSolidAngleUnitInstance get() = prefixedSolidAngle(this, KSolidAngleUnit.STERADIAN)

/** Prefixed square degrees, e.g. `milli.squareDegrees`. */
val KPrefixBuilder.squareDegrees: KSolidAngleUnitInstance
    get() = prefixedSolidAngle(
        this,
        KSolidAngleUnit.SQUARE_DEGREE
    )

/** Prefixed spats, e.g. `milli.spats`. */
val KPrefixBuilder.spats: KSolidAngleUnitInstance get() = prefixedSolidAngle(this, KSolidAngleUnit.SPAT)
