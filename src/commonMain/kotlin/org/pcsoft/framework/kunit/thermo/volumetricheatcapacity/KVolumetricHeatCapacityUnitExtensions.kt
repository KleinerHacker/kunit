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

package org.pcsoft.framework.kunit.thermo.volumetricheatcapacity

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 volumetric heat capacity templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `4.18 of mega.joulesPerCubicMeterKelvin`.

private fun prefixedVolumetricHeatCapacity(
    builder: KPrefixBuilder,
    unit: KVolumetricHeatCapacityUnit
): KVolumetricHeatCapacityUnitInstance =
    volumetricHeatCapacityInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed joules per cubic meter kelvin, e.g. `mega.joulesPerCubicMeterKelvin`. */
val KPrefixBuilder.joulesPerCubicMeterKelvin: KVolumetricHeatCapacityUnitInstance
    get() = prefixedVolumetricHeatCapacity(this, KVolumetricHeatCapacityUnit.JOULE_PER_CUBIC_METER_KELVIN)

/** Prefixed calories per cubic centimeter kelvin, e.g. `milli.caloriesPerCubicCentimeterKelvin`. */
val KPrefixBuilder.caloriesPerCubicCentimeterKelvin: KVolumetricHeatCapacityUnitInstance
    get() = prefixedVolumetricHeatCapacity(
        this,
        KVolumetricHeatCapacityUnit.CALORIE_PER_CUBIC_CENTIMETER_KELVIN,
    )
