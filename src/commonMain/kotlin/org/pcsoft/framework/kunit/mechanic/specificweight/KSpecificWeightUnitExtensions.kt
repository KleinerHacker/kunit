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

package org.pcsoft.framework.kunit.mechanic.specificweight

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 specific weight templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `9.807 of kilo.newtonsPerCubicMeter`.

private fun prefixedSpecificWeight(
    builder: KPrefixBuilder,
    unit: KSpecificWeightUnit
): KSpecificWeightUnitInstance =
    specificWeightInstanceOf(builder.prefix.factor * unit.baseValue * N_PER_M3_IN_BASE)

/** Prefixed newtons per cubic meter, e.g. `kilo.newtonsPerCubicMeter`. */
val KPrefixBuilder.newtonsPerCubicMeter: KSpecificWeightUnitInstance
    get() = prefixedSpecificWeight(this, KSpecificWeightUnit.NEWTON_PER_CUBIC_METER)

/** Prefixed kilonewtons per cubic meter, e.g. `milli.kilonewtonsPerCubicMeter`. */
val KPrefixBuilder.kilonewtonsPerCubicMeter: KSpecificWeightUnitInstance
    get() = prefixedSpecificWeight(this, KSpecificWeightUnit.KILONEWTON_PER_CUBIC_METER)

/** Prefixed pounds-force per cubic foot, e.g. `kilo.poundsForcePerCubicFoot`. */
val KPrefixBuilder.poundsForcePerCubicFoot: KSpecificWeightUnitInstance
    get() = prefixedSpecificWeight(this, KSpecificWeightUnit.POUND_FORCE_PER_CUBIC_FOOT)
