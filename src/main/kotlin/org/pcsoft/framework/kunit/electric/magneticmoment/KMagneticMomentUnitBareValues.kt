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

package org.pcsoft.framework.kunit.electric.magneticmoment

// Value-1 magnetic moment templates for the named units of the group, used with `of`/`into`
// (`2 of ampereSquareMeters`). Prefixed forms live in KMagneticMomentUnitExtensions.kt.

/** 1 A·m² ([KMagneticMomentUnit.AMPERE_SQUARE_METER]), the group's base unit. */
val ampereSquareMeters: KMagneticMomentUnitInstance =
    magneticMomentOfUnit(KMagneticMomentUnit.AMPERE_SQUARE_METER)

/** 1 J/T ([KMagneticMomentUnit.JOULE_PER_TESLA]), the energy-based spelling; = 1 A·m². */
val joulesPerTesla: KMagneticMomentUnitInstance =
    magneticMomentOfUnit(KMagneticMomentUnit.JOULE_PER_TESLA)

/** 1 μB ([KMagneticMomentUnit.BOHR_MAGNETON], 9.2740100783e-24 A·m²). */
val bohrMagnetons: KMagneticMomentUnitInstance =
    magneticMomentOfUnit(KMagneticMomentUnit.BOHR_MAGNETON)

/** 1 μN ([KMagneticMomentUnit.NUCLEAR_MAGNETON], 5.0507837461e-27 A·m²). */
val nuclearMagnetons: KMagneticMomentUnitInstance =
    magneticMomentOfUnit(KMagneticMomentUnit.NUCLEAR_MAGNETON)
