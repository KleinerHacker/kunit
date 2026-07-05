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

package org.pcsoft.framework.kunit.distance

import org.pcsoft.framework.kunit.KDerivedUnit

// Volume (exponent 3) creator extension properties for the derived special units (liter/gallon/…).
//
// There are intentionally NO cubicXxx creators (e.g. cubicMeters, cubicMiles): a plain volume is
// built via the group-agnostic power operation, e.g. `2.meters pow 3` or `1.miles pow 3`
// (see KDistanceUnitInstance.pow / KMixedUnitInstance.pow). The derived units below carry their own
// name/symbol/conversion factor and therefore remain dedicated creators.

private fun volumeFrom(value: Number, derived: KDerivedUnit<KDistanceUnit>): KVolumeUnitInstance = volumeOf(value.toDouble() * derived.baseValue)

/**
 * Creates a pure volume value (exponent 3) in liters. Example:
 * `1.liters.value // 0.001` (normalized to cubic meters).
 */
val Number.liters: KVolumeUnitInstance get() = volumeFrom(this, KDistanceDerivedUnit.LITER)

/**
 * Creates a pure volume value (exponent 3) in US liquid gallons. Example:
 * `1.usGallons.value // 0.003785411784` (normalized to cubic meters).
 */
val Number.usGallons: KVolumeUnitInstance get() = volumeFrom(this, KDistanceDerivedUnit.US_GALLON)

/**
 * Creates a pure volume value (exponent 3) in imperial gallons. Example:
 * `1.imperialGallons.value // 0.00454609` (normalized to cubic meters).
 */
val Number.imperialGallons: KVolumeUnitInstance get() = volumeFrom(this, KDistanceDerivedUnit.IMPERIAL_GALLON)

/**
 * Creates a pure volume value (exponent 3) in US fluid ounces. Example:
 * `1.usFluidOunces.value // 2.95735295625e-5` (normalized to cubic meters).
 */
val Number.usFluidOunces: KVolumeUnitInstance get() = volumeFrom(this, KDistanceDerivedUnit.US_FLUID_OUNCE)

/**
 * Creates a pure volume value (exponent 3) in oil barrels. Example:
 * `1.oilBarrels.value // 0.158987294928` (normalized to cubic meters).
 */
val Number.oilBarrels: KVolumeUnitInstance get() = volumeFrom(this, KDistanceDerivedUnit.OIL_BARREL)
