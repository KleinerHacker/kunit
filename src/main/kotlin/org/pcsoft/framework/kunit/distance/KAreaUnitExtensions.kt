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

// Area (exponent 2) creator extension properties for the derived special units (are/hectare/acre).
//
// There are intentionally NO squareXxx creators (e.g. squareMeters, squareMiles): a plain area is
// built via the group-agnostic power operation, e.g. `200.meters pow 2` or `1.miles pow 2`
// (see KDistanceUnitInstance.pow / KMixedUnitInstance.pow). The derived units below carry their own
// name/symbol/conversion factor and therefore remain dedicated creators.

private fun areaFrom(value: Number, derived: KDerivedUnit<KDistanceUnit>): KAreaUnitInstance = areaOf(value.toDouble() * derived.baseValue)

/**
 * Creates a pure area value (exponent 2) in ares. Example:
 * `5.ares.valueAs(KDistanceDerivedUnit.ARE) // 5.0`.
 */
val Number.ares: KAreaUnitInstance get() = areaFrom(this, KDistanceDerivedUnit.ARE)

/**
 * Creates a pure area value (exponent 2) in hectares. Example:
 * `5.hectares.value // 50000.0` (normalized to square meters).
 */
val Number.hectares: KAreaUnitInstance get() = areaFrom(this, KDistanceDerivedUnit.HECTARE)

/**
 * Creates a pure area value (exponent 2) in acres. Example:
 * `1.acres.value // 4046.8564224` (normalized to square meters).
 */
val Number.acres: KAreaUnitInstance get() = areaFrom(this, KDistanceDerivedUnit.ACRE)
