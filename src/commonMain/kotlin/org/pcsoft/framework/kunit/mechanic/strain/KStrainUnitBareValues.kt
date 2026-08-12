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

package org.pcsoft.framework.kunit.mechanic.strain

import org.pcsoft.framework.kunit.KUnitDisplay

// Bare, value-1 strain tokens (each = 1 unit, normalized to the plain ratio). Vocabulary for building
// (`2 of percent`) and reading (`e into microstrain`). Prefixed forms live in KStrainUnitExtensions.kt.

// Each token carries its own [KUnitDisplay] so that formatting renders the written-down symbol (`"%"`,
// `"‰"`) rather than the base symbol `"1"`; the value stays normalized to the plain ratio.
private fun bareStrain(unit: KStrainUnit): KStrainUnitInstance = strainOf(unit.baseValue, KUnitDisplay(unit))

/** The plain ratio 1 ([KStrainUnit.RATIO]), i.e. m/m; the group's base unit. */
val ratio: KStrainUnitInstance = bareStrain(KStrainUnit.RATIO)

/** 1 percent ([KStrainUnit.PERCENT], 0.01). */
val percent: KStrainUnitInstance = bareStrain(KStrainUnit.PERCENT)

/** 1 per mille ([KStrainUnit.PER_MILLE], 0.001). */
val perMille: KStrainUnitInstance = bareStrain(KStrainUnit.PER_MILLE)

/** 1 microstrain ([KStrainUnit.MICROSTRAIN], 1 µm/m = 1e-6). */
val microstrain: KStrainUnitInstance = bareStrain(KStrainUnit.MICROSTRAIN)
