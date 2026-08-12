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

package org.pcsoft.framework.kunit.common.reciprocallength

// Value-1 reciprocal length templates for the named units of the group, used with `of`/`into`
// (`2.5 of dioptres`, `k into reciprocalCentimeters`). Prefixed forms live in
// KReciprocalLengthUnitExtensions.kt.

/** 1 m⁻¹ ([KReciprocalLengthUnit.RECIPROCAL_METER]), the group's base unit. */
val reciprocalMeters: KReciprocalLengthUnitInstance =
    reciprocalLengthOfUnit(KReciprocalLengthUnit.RECIPROCAL_METER)

/** 1 dpt ([KReciprocalLengthUnit.DIOPTRE]), the optometric spelling of the reciprocal meter. */
val dioptres: KReciprocalLengthUnitInstance = reciprocalLengthOfUnit(KReciprocalLengthUnit.DIOPTRE)

/** 1 cm⁻¹ ([KReciprocalLengthUnit.RECIPROCAL_CENTIMETER], 100 m⁻¹), the spectroscopic wavenumber unit. */
val reciprocalCentimeters: KReciprocalLengthUnitInstance =
    reciprocalLengthOfUnit(KReciprocalLengthUnit.RECIPROCAL_CENTIMETER)

/** 1 kayser - the classical name for [KReciprocalLengthUnit.RECIPROCAL_CENTIMETER]. */
val kaysers: KReciprocalLengthUnitInstance =
    reciprocalLengthOfUnit(KReciprocalLengthUnit.RECIPROCAL_CENTIMETER)
