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

package org.pcsoft.framework.kunit.power

// Bare, value-1 power tokens (each = 1 unit, normalized to watts). Vocabulary for building
// (`10 of watts`) and reading (`p into metricHorsePowers`); combine with the prefix builders
// (`kilo.watts`). Prefixed forms live in KPowerUnitExtensions.kt.

/** 1 watt ([KPowerUnit.WATT]). */
val watts: KPowerUnitInstance = powerInstanceOf(KPowerUnit.WATT.baseValue)

/** 1 metric horsepower ([KPowerUnit.METRIC_HORSE_POWER], 735.49875 W). */
val metricHorsePowers: KPowerUnitInstance = powerInstanceOf(KPowerUnit.METRIC_HORSE_POWER.baseValue)

/** 1 mechanical horsepower ([KPowerUnit.MECHANICAL_HORSE_POWER], 745.6998715822702 W). */
val mechanicalHorsePowers: KPowerUnitInstance = powerInstanceOf(KPowerUnit.MECHANICAL_HORSE_POWER.baseValue)

/** 1 erg per second ([KPowerUnit.ERG_PER_SECOND], CGS, 1e-7 W). */
val ergsPerSecond: KPowerUnitInstance = powerInstanceOf(KPowerUnit.ERG_PER_SECOND.baseValue)

/** 1 volt ampere ([KPowerUnit.VOLT_AMPERE], apparent power, 1 VA = 1 W). */
val voltAmperes: KPowerUnitInstance = powerInstanceOf(KPowerUnit.VOLT_AMPERE.baseValue)

/** 1 volt ampere reactive ([KPowerUnit.VAR], reactive power, 1 var = 1 W). */
val vars: KPowerUnitInstance = powerInstanceOf(KPowerUnit.VAR.baseValue)
