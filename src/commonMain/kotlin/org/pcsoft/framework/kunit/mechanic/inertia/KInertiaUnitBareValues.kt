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

package org.pcsoft.framework.kunit.mechanic.inertia

// Value-1 moment-of-inertia templates for the named units of the group, used with `of`/`into`
// (`2 of kilogramMetersSquared`, `j into poundFeetSquared`). Prefixed forms live in
// KInertiaUnitExtensions.kt.

/** 1 kg·m² ([KInertiaUnit.KILOGRAM_METERS_SQUARED]), the group's base unit. */
val kilogramMetersSquared: KInertiaUnitInstance = inertiaOfUnit(KInertiaUnit.KILOGRAM_METERS_SQUARED)

/** 1 g·cm² ([KInertiaUnit.GRAM_CENTIMETERS_SQUARED], the CGS unit, 1e-7 kg·m²). */
val gramCentimetersSquared: KInertiaUnitInstance = inertiaOfUnit(KInertiaUnit.GRAM_CENTIMETERS_SQUARED)

/** 1 lb·ft² ([KInertiaUnit.POUND_FEET_SQUARED], ≈ 0.0421401 kg·m²). */
val poundFeetSquared: KInertiaUnitInstance = inertiaOfUnit(KInertiaUnit.POUND_FEET_SQUARED)
