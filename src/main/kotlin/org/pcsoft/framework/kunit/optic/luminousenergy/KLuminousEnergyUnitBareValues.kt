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

package org.pcsoft.framework.kunit.optic.luminousenergy

// Value-1 luminous energy templates for the named units of the group, used with `of`/`into`
// (`5 of lumenSeconds`, `q into lumenHours`). Prefixed forms live in KLuminousEnergyUnitExtensions.kt.

/** 1 lm·s ([KLuminousEnergyUnit.LUMEN_SECOND]), the group's base unit. */
val lumenSeconds: KLuminousEnergyUnitInstance = luminousEnergyOfUnit(KLuminousEnergyUnit.LUMEN_SECOND)

/** 1 talbot - the classical name for [KLuminousEnergyUnit.LUMEN_SECOND]. */
val talbots: KLuminousEnergyUnitInstance = luminousEnergyOfUnit(KLuminousEnergyUnit.LUMEN_SECOND)

/** 1 lm·h ([KLuminousEnergyUnit.LUMEN_HOUR], 3600 lm·s). */
val lumenHours: KLuminousEnergyUnitInstance = luminousEnergyOfUnit(KLuminousEnergyUnit.LUMEN_HOUR)
