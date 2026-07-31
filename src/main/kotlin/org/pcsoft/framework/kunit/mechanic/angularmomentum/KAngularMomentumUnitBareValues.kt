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

package org.pcsoft.framework.kunit.mechanic.angularmomentum

// Value-1 angular-momentum templates for the named units of the group, used with `of`/`into`
// (`5 of jouleSeconds`, `l into kilogramMetersSquaredPerSecond`). Prefixed forms live in
// KAngularMomentumUnitExtensions.kt.

/** 1 kg·m²/s ([KAngularMomentumUnit.KILOGRAM_METERS_SQUARED_PER_SECOND]), the group's base unit. */
val kilogramMetersSquaredPerSecond: KAngularMomentumUnitInstance =
    angularMomentumOfUnit(KAngularMomentumUnit.KILOGRAM_METERS_SQUARED_PER_SECOND)

/** 1 N·m·s ([KAngularMomentumUnit.NEWTON_METER_SECOND]), the torque-impulse reading. */
val newtonMeterSeconds: KAngularMomentumUnitInstance =
    angularMomentumOfUnit(KAngularMomentumUnit.NEWTON_METER_SECOND)

/** 1 J·s ([KAngularMomentumUnit.JOULE_SECOND]), the action reading (unit of Planck's constant). */
val jouleSeconds: KAngularMomentumUnitInstance =
    angularMomentumOfUnit(KAngularMomentumUnit.JOULE_SECOND)

/** 1 g·cm²/s ([KAngularMomentumUnit.GRAM_CENTIMETERS_SQUARED_PER_SECOND], the CGS unit, 1e-7 kg·m²/s). */
val gramCentimetersSquaredPerSecond: KAngularMomentumUnitInstance =
    angularMomentumOfUnit(KAngularMomentumUnit.GRAM_CENTIMETERS_SQUARED_PER_SECOND)
