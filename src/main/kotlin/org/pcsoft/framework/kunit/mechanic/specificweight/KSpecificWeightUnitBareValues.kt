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

package org.pcsoft.framework.kunit.mechanic.specificweight

// Value-1 specific weight templates for the named units of the group, used with `of`/`into`
// (`9807 of newtonsPerCubicMeter`). Prefixed forms live in KSpecificWeightUnitExtensions.kt.

/** 1 N/m³ ([KSpecificWeightUnit.NEWTON_PER_CUBIC_METER]), the group's base unit. */
val newtonsPerCubicMeter: KSpecificWeightUnitInstance =
    specificWeightOfUnit(KSpecificWeightUnit.NEWTON_PER_CUBIC_METER)

/** 1 kN/m³ ([KSpecificWeightUnit.KILONEWTON_PER_CUBIC_METER], 1000 N/m³). */
val kilonewtonsPerCubicMeter: KSpecificWeightUnitInstance =
    specificWeightOfUnit(KSpecificWeightUnit.KILONEWTON_PER_CUBIC_METER)

/** 1 lbf/ft³ ([KSpecificWeightUnit.POUND_FORCE_PER_CUBIC_FOOT], ≈ 157.087 N/m³). */
val poundsForcePerCubicFoot: KSpecificWeightUnitInstance =
    specificWeightOfUnit(KSpecificWeightUnit.POUND_FORCE_PER_CUBIC_FOOT)
