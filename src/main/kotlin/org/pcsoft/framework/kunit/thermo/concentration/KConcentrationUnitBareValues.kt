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

package org.pcsoft.framework.kunit.thermo.concentration

// Value-1 concentration templates for the named units of the group, used with `of`/`into`
// (`0.1 of molesPerLiter`, `c into millimolesPerLiter`). Prefixed forms live in
// KConcentrationUnitExtensions.kt.

/** 1 mol/m³ ([KConcentrationUnit.MOLES_PER_CUBIC_METER]), the group's base unit. */
val molesPerCubicMeter: KConcentrationUnitInstance =
    concentrationOfUnit(KConcentrationUnit.MOLES_PER_CUBIC_METER)

/** 1 mol/l ([KConcentrationUnit.MOLES_PER_LITER], 1000 mol/m³) - the molarity `M`. */
val molesPerLiter: KConcentrationUnitInstance = concentrationOfUnit(KConcentrationUnit.MOLES_PER_LITER)

/** 1 mol/l written as the classical molarity symbol `M`; identical to [molesPerLiter]. */
val molar: KConcentrationUnitInstance = concentrationOfUnit(KConcentrationUnit.MOLES_PER_LITER)

/** 1 mmol/l ([KConcentrationUnit.MILLIMOLES_PER_LITER], 1 mol/m³) - the clinical blood-value unit. */
val millimolesPerLiter: KConcentrationUnitInstance =
    concentrationOfUnit(KConcentrationUnit.MILLIMOLES_PER_LITER)
