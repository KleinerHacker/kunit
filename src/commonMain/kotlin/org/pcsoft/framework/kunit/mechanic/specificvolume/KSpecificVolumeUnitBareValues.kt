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

package org.pcsoft.framework.kunit.mechanic.specificvolume

// Value-1 specific-volume templates for the named units of the group, used with `of`/`into`
// (`2 of litersPerKilogram`, `v into cubicMetersPerKilogram`). Prefixed forms live in
// KSpecificVolumeUnitExtensions.kt.

/** 1 m³/kg ([KSpecificVolumeUnit.CUBIC_METERS_PER_KILOGRAM]), the group's base unit. */
val cubicMetersPerKilogram: KSpecificVolumeUnitInstance =
    specificVolumeOfUnit(KSpecificVolumeUnit.CUBIC_METERS_PER_KILOGRAM)

/** 1 l/kg ([KSpecificVolumeUnit.LITERS_PER_KILOGRAM], 0.001 m³/kg). */
val litersPerKilogram: KSpecificVolumeUnitInstance =
    specificVolumeOfUnit(KSpecificVolumeUnit.LITERS_PER_KILOGRAM)

/** 1 cm³/g ([KSpecificVolumeUnit.CUBIC_CENTIMETERS_PER_GRAM], 0.001 m³/kg). */
val cubicCentimetersPerGram: KSpecificVolumeUnitInstance =
    specificVolumeOfUnit(KSpecificVolumeUnit.CUBIC_CENTIMETERS_PER_GRAM)

/** 1 ft³/lb ([KSpecificVolumeUnit.CUBIC_FEET_PER_POUND], ≈ 0.0624280 m³/kg). */
val cubicFeetPerPound: KSpecificVolumeUnitInstance =
    specificVolumeOfUnit(KSpecificVolumeUnit.CUBIC_FEET_PER_POUND)
