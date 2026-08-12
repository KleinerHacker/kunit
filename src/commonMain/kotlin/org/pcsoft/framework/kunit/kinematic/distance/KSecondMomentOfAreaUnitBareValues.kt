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

package org.pcsoft.framework.kunit.kinematic.distance

// Value-1 second moment of area templates, used with `of`/`into` (`1940 of quarticCentimeters`).
// Prefixed forms live in KSecondMomentOfAreaUnitExtensions.kt.

/** 1 m⁴, the base unit of the exponent-4 leaf of the distance group. */
val quarticMeters: KSecondMomentOfAreaUnitInstance = secondMomentOfAreaOf(1.0)

/** 1 cm⁴ = 1e-8 m⁴ - the unit every steel-profile table is written in. */
val quarticCentimeters: KSecondMomentOfAreaUnitInstance = secondMomentOfAreaOf(1.0e-8)

/** 1 mm⁴ = 1e-12 m⁴, used for small sections. */
val quarticMillimeters: KSecondMomentOfAreaUnitInstance = secondMomentOfAreaOf(1.0e-12)

/** 1 in⁴ ≈ 4.162314e-7 m⁴, the imperial section property. */
val quarticInches: KSecondMomentOfAreaUnitInstance = secondMomentOfAreaOf(4.162314256e-7)
