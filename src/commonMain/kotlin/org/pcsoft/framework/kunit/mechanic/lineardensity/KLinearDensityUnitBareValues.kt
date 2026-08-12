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

package org.pcsoft.framework.kunit.mechanic.lineardensity

// Value-1 linear-density templates for the named units of the group, used with `of`/`into`
// (`5 of gramsPerMeter`, `rho into tex`). Prefixed forms live in KLinearDensityUnitExtensions.kt.

/** 1 kg/m ([KLinearDensityUnit.KILOGRAMS_PER_METER]), the group's base unit. */
val kilogramsPerMeter: KLinearDensityUnitInstance = linearDensityOfUnit(KLinearDensityUnit.KILOGRAMS_PER_METER)

/** 1 g/m ([KLinearDensityUnit.GRAMS_PER_METER], 0.001 kg/m). */
val gramsPerMeter: KLinearDensityUnitInstance = linearDensityOfUnit(KLinearDensityUnit.GRAMS_PER_METER)

/** 1 g/cm ([KLinearDensityUnit.GRAMS_PER_CENTIMETER], 0.1 kg/m). */
val gramsPerCentimeter: KLinearDensityUnitInstance = linearDensityOfUnit(KLinearDensityUnit.GRAMS_PER_CENTIMETER)

/** 1 tex ([KLinearDensityUnit.TEX], 1 g/km). */
val tex: KLinearDensityUnitInstance = linearDensityOfUnit(KLinearDensityUnit.TEX)

/** 1 denier ([KLinearDensityUnit.DENIER], 1 g / 9000 m). */
val denier: KLinearDensityUnitInstance = linearDensityOfUnit(KLinearDensityUnit.DENIER)

/** 1 lb/ft ([KLinearDensityUnit.POUNDS_PER_FOOT], ≈ 1.48816 kg/m). */
val poundsPerFoot: KLinearDensityUnitInstance = linearDensityOfUnit(KLinearDensityUnit.POUNDS_PER_FOOT)
