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

package org.pcsoft.framework.kunit.mechanic.massflow

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 mass-flow templates: one property per named unit on the prefix builder. Mass flow
// accepts *any* magnitude, so the properties hang on the common base [KPrefixBuilder]. Use with
// `of`/`into`, e.g. `5 of milli.gramsPerSecond`.

private fun prefixedMassFlow(builder: KPrefixBuilder, unit: KMassFlowUnit): KMassFlowUnitInstance =
    massFlowInstanceOf(builder.prefix.factor * unit.baseValue * KGS_IN_BASE)

/** Prefixed kg/s, e.g. `milli.kilogramsPerSecond`, `kilo.kilogramsPerSecond`. */
val KPrefixBuilder.kilogramsPerSecond: KMassFlowUnitInstance
    get() = prefixedMassFlow(
        this,
        KMassFlowUnit.KILOGRAMS_PER_SECOND
    )

/** Prefixed g/s, e.g. `milli.gramsPerSecond` (mg/s), `micro.gramsPerSecond` (µg/s). */
val KPrefixBuilder.gramsPerSecond: KMassFlowUnitInstance get() = prefixedMassFlow(this, KMassFlowUnit.GRAMS_PER_SECOND)

/** Prefixed kg/h, e.g. `kilo.kilogramsPerHour`. */
val KPrefixBuilder.kilogramsPerHour: KMassFlowUnitInstance
    get() = prefixedMassFlow(
        this,
        KMassFlowUnit.KILOGRAMS_PER_HOUR
    )

/** Prefixed t/h, e.g. `kilo.tonnesPerHour`. */
val KPrefixBuilder.tonnesPerHour: KMassFlowUnitInstance get() = prefixedMassFlow(this, KMassFlowUnit.TONNES_PER_HOUR)

/** Prefixed lb/s, e.g. `kilo.poundsPerSecond`. */
val KPrefixBuilder.poundsPerSecond: KMassFlowUnitInstance
    get() = prefixedMassFlow(
        this,
        KMassFlowUnit.POUNDS_PER_SECOND
    )

/** Prefixed lb/h, e.g. `kilo.poundsPerHour`. */
val KPrefixBuilder.poundsPerHour: KMassFlowUnitInstance get() = prefixedMassFlow(this, KMassFlowUnit.POUNDS_PER_HOUR)
