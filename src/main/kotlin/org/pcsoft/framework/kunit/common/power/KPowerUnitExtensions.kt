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

package org.pcsoft.framework.kunit.common.power

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 power templates: one property per power unit on the prefix builder (e.g. `kilo.watts`
// = 1000 W, `mega.watts` = 1e6 W). Power accepts *any* magnitude, so the properties hang on the common base
// [KPrefixBuilder]. Use with `of`/`into`, e.g. `2 of kilo.watts`, `p into mega.watts`.

private fun prefixedPower(builder: KPrefixBuilder, unit: KPowerUnit): KPowerUnitInstance =
    powerInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed watts, e.g. `kilo.watts` = 1000 W, `mega.watts` = 1e6 W. */
val KPrefixBuilder.watts: KPowerUnitInstance get() = prefixedPower(this, KPowerUnit.WATT)

/** Prefixed metric horsepowers, e.g. `kilo.metricHorsePowers`. */
val KPrefixBuilder.metricHorsePowers: KPowerUnitInstance get() = prefixedPower(this, KPowerUnit.METRIC_HORSE_POWER)

/** Prefixed mechanical horsepowers, e.g. `kilo.mechanicalHorsePowers`. */
val KPrefixBuilder.mechanicalHorsePowers: KPowerUnitInstance
    get() = prefixedPower(this, KPowerUnit.MECHANICAL_HORSE_POWER)

/** Prefixed ergs per second, e.g. `mega.ergsPerSecond`. */
val KPrefixBuilder.ergsPerSecond: KPowerUnitInstance get() = prefixedPower(this, KPowerUnit.ERG_PER_SECOND)

/** Prefixed volt amperes (apparent power), e.g. `kilo.voltAmperes` = 1 kVA. */
val KPrefixBuilder.voltAmperes: KPowerUnitInstance get() = prefixedPower(this, KPowerUnit.VOLT_AMPERE)

/** Prefixed volt amperes reactive, e.g. `kilo.vars` = 1 kvar. */
val KPrefixBuilder.vars: KPowerUnitInstance get() = prefixedPower(this, KPowerUnit.VAR)
