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

package org.pcsoft.framework.kunit.thermo.doserate

import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.timeUnitInstanceOf
import org.pcsoft.framework.kunit.thermo.specificenergy.KSpecificEnergyUnitInstance
import org.pcsoft.framework.kunit.thermo.specificenergy.specificEnergyInstanceOf

// Cross-group operators for the decomposition of the dose rate - `specificEnergy / time` - plus its
// inverses. They live in the dose rate package because it may depend on specific-energy/time (the reverse
// must never happen). The absorbed dose itself is the specific energy group (`J/kg` = `Gy`).

/**
 * Divides an absorbed dose (a specific energy) by a time to obtain a [KDoseRateUnitInstance]
 * (`specificEnergy / time = dose rate`).
 *
 * Example:
 * ```kotlin
 * val rate = (2 of milli.joulesPerKilogram) / (1 of hours) // 2 mGy/h
 * ```
 */
operator fun KSpecificEnergyUnitInstance.div(other: KTimeUnitInstance): KDoseRateUnitInstance =
    doseRateInstanceOf(value / other.value)

/**
 * Multiplies a dose rate by a time to obtain the accumulated dose
 * (`dose rate * time = specificEnergy`).
 */
operator fun KDoseRateUnitInstance.times(other: KTimeUnitInstance): KSpecificEnergyUnitInstance =
    specificEnergyInstanceOf(value * other.value)

/**
 * Multiplies a time by a dose rate to obtain the accumulated dose; the commutative counterpart of
 * [KDoseRateUnitInstance.times].
 */
operator fun KTimeUnitInstance.times(other: KDoseRateUnitInstance): KSpecificEnergyUnitInstance =
    specificEnergyInstanceOf(value * other.value)

/**
 * Divides an absorbed dose by a dose rate to obtain the exposure time it corresponds to
 * (`specificEnergy / dose rate = time`).
 */
operator fun KSpecificEnergyUnitInstance.div(other: KDoseRateUnitInstance): KTimeUnitInstance =
    timeUnitInstanceOf(value / other.value)
