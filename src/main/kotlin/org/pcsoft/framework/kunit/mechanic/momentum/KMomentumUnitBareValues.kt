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

package org.pcsoft.framework.kunit.mechanic.momentum

// Value-1 momentum templates for the named units of the group, used with `of`/`into`
// (`5 of newtonSeconds`, `p into kilogramMetersPerSecond`). Prefixed forms live in
// KMomentumUnitExtensions.kt.

/** 1 kg·m/s ([KMomentumUnit.KILOGRAM_METERS_PER_SECOND]), the group's base unit. */
val kilogramMetersPerSecond: KMomentumUnitInstance = momentumOfUnit(KMomentumUnit.KILOGRAM_METERS_PER_SECOND)

/** 1 N·s ([KMomentumUnit.NEWTON_SECOND]), the impulse reading of the same dimension. */
val newtonSeconds: KMomentumUnitInstance = momentumOfUnit(KMomentumUnit.NEWTON_SECOND)

/** 1 g·cm/s ([KMomentumUnit.GRAM_CENTIMETERS_PER_SECOND], the CGS unit, 1e-5 kg·m/s). */
val gramCentimetersPerSecond: KMomentumUnitInstance = momentumOfUnit(KMomentumUnit.GRAM_CENTIMETERS_PER_SECOND)

/** 1 lb·ft/s ([KMomentumUnit.POUND_FEET_PER_SECOND], ≈ 0.138255 kg·m/s). */
val poundFeetPerSecond: KMomentumUnitInstance = momentumOfUnit(KMomentumUnit.POUND_FEET_PER_SECOND)
