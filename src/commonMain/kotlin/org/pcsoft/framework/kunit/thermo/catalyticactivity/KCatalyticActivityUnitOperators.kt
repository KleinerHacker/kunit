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

package org.pcsoft.framework.kunit.thermo.catalyticactivity

import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.timeUnitInstanceOf
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnitInstance
import org.pcsoft.framework.kunit.thermo.amountofsubstance.amountOfSubstanceOf

// Cross-group operators for the decomposition of the catalytic activity - `amountOfSubstance / time` -
// plus its inverses. They live in the catalytic activity package because it may depend on
// amount-of-substance/time (the reverse must never happen).

/**
 * Divides an amount of substance by a time to obtain a [KCatalyticActivityUnitInstance]
 * (`amountOfSubstance / time = catalytic activity`).
 *
 * Example:
 * ```kotlin
 * val a = (0.5 of milli.moles) / (10 of seconds) // 5e-5 kat
 * ```
 */
operator fun KAmountOfSubstanceUnitInstance.div(other: KTimeUnitInstance): KCatalyticActivityUnitInstance =
    catalyticActivityInstanceOf(value / other.value)

/**
 * Multiplies a catalytic activity by a time to obtain the amount of substance converted
 * (`catalytic activity * time = amountOfSubstance`).
 */
operator fun KCatalyticActivityUnitInstance.times(
    other: KTimeUnitInstance
): KAmountOfSubstanceUnitInstance = amountOfSubstanceOf(value * other.value)

/**
 * Multiplies a time by a catalytic activity to obtain the amount of substance converted; the commutative
 * counterpart of [KCatalyticActivityUnitInstance.times].
 */
operator fun KTimeUnitInstance.times(
    other: KCatalyticActivityUnitInstance
): KAmountOfSubstanceUnitInstance = amountOfSubstanceOf(value * other.value)

/**
 * Divides an amount of substance by a catalytic activity to obtain the time the conversion takes
 * (`amountOfSubstance / catalytic activity = time`).
 */
operator fun KAmountOfSubstanceUnitInstance.div(
    other: KCatalyticActivityUnitInstance
): KTimeUnitInstance = timeUnitInstanceOf(value / other.value)
