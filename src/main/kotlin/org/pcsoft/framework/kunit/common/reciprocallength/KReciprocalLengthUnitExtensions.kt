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

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 reciprocal length templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `250 of milli.dioptres`.

private fun prefixedReciprocalLength(
    builder: KPrefixBuilder,
    unit: KReciprocalLengthUnit
): KReciprocalLengthUnitInstance =
    reciprocalLengthInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed reciprocal meters, e.g. `kilo.reciprocalMeters`. */
val KPrefixBuilder.reciprocalMeters: KReciprocalLengthUnitInstance
    get() = prefixedReciprocalLength(this, KReciprocalLengthUnit.RECIPROCAL_METER)

/** Prefixed dioptres, e.g. `milli.dioptres`. */
val KPrefixBuilder.dioptres: KReciprocalLengthUnitInstance
    get() = prefixedReciprocalLength(this, KReciprocalLengthUnit.DIOPTRE)

/** Prefixed reciprocal centimeters, e.g. `kilo.reciprocalCentimeters`. */
val KPrefixBuilder.reciprocalCentimeters: KReciprocalLengthUnitInstance
    get() = prefixedReciprocalLength(this, KReciprocalLengthUnit.RECIPROCAL_CENTIMETER)

/** Prefixed kaysers, e.g. `kilo.kaysers` - the classical spelling of `kilo.reciprocalCentimeters`. */
val KPrefixBuilder.kaysers: KReciprocalLengthUnitInstance
    get() = prefixedReciprocalLength(this, KReciprocalLengthUnit.RECIPROCAL_CENTIMETER)
