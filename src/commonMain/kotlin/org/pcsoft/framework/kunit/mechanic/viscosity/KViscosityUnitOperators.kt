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

import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.toTime
import org.pcsoft.framework.kunit.mechanic.pressure.KPressureUnitInstance
import org.pcsoft.framework.kunit.mechanic.pressure.toPressure

// Cross-group operators that let pressure and time combine *directly* into a strongly typed dynamic
// viscosity, and back. They live in the viscosity package because it may depend on pressure/time (the
// reverse must never happen), and each delegates to the generic KMixedUnitInstance `*`/`/` engine (both
// pressure and viscosity store gram-based raw components, so the scales match exactly).

/**
 * Multiplies a pressure by a time to obtain the dynamic viscosity (`pressure * time = viscosity`).
 *
 * Example:
 * ```kotlin
 * val eta = (1 of milli.pascals) * (1 of seconds) // 1 mPa·s = 1 cP
 * ```
 */
operator fun KPressureUnitInstance.times(other: KTimeUnitInstance): KViscosityUnitInstance =
    (this.toUnit() * other.toUnit()).toViscosity()

/**
 * Multiplies a time by a pressure to obtain the dynamic viscosity (`time * pressure = viscosity`); the
 * commutative counterpart of [KPressureUnitInstance.times].
 */
operator fun KTimeUnitInstance.times(other: KPressureUnitInstance): KViscosityUnitInstance =
    (this.toUnit() * other.toUnit()).toViscosity()

/** Divides a viscosity by a pressure to obtain the time (`viscosity / pressure = time`). */
operator fun KViscosityUnitInstance.div(other: KPressureUnitInstance): KTimeUnitInstance =
    (this.toUnit() / other.toUnit()).toTime()

/** Divides a viscosity by a time to obtain the pressure (`viscosity / time = pressure`). */
operator fun KViscosityUnitInstance.div(other: KTimeUnitInstance): KPressureUnitInstance =
    (this.toUnit() / other.toUnit()).toPressure()
