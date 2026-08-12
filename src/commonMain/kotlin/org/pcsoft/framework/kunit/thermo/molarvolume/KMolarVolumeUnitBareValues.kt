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

package org.pcsoft.framework.kunit.thermo.molarvolume

// Value-1 molar volume templates for the named units of the group, used with `of`/`into`
// (`22.4 of litersPerMole`, `v into cubicCentimetersPerMole`). Prefixed forms live in
// KMolarVolumeUnitExtensions.kt.

/** 1 m³/mol ([KMolarVolumeUnit.CUBIC_METERS_PER_MOLE]), the group's base unit. */
val cubicMetersPerMole: KMolarVolumeUnitInstance = molarVolumeOfUnit(KMolarVolumeUnit.CUBIC_METERS_PER_MOLE)

/** 1 l/mol ([KMolarVolumeUnit.LITERS_PER_MOLE], 0.001 m³/mol). */
val litersPerMole: KMolarVolumeUnitInstance = molarVolumeOfUnit(KMolarVolumeUnit.LITERS_PER_MOLE)

/** 1 cm³/mol ([KMolarVolumeUnit.CUBIC_CENTIMETERS_PER_MOLE], 1e-6 m³/mol). */
val cubicCentimetersPerMole: KMolarVolumeUnitInstance =
    molarVolumeOfUnit(KMolarVolumeUnit.CUBIC_CENTIMETERS_PER_MOLE)
