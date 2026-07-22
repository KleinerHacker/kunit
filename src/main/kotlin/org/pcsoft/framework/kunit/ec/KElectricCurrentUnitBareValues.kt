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

package org.pcsoft.framework.kunit.ec

// Bare, value-1 electric current tokens (each = 1 unit, normalized to amperes). Vocabulary for building
// (`10 of amperes`) and reading (`v into biot`); combine with the prefix builders (`milli.amperes`).
// Prefixed forms live in KElectricCurrentUnitExtensions.kt.

// --- SI ------------------------------------------------------------------------------------------

/** 1 ampere ([KElectricCurrentUnit.AMPERE]). */
val amperes: KElectricCurrentUnitInstance = electricCurrentOf(KElectricCurrentUnit.AMPERE.baseValue)

// --- CGS -----------------------------------------------------------------------------------------

/** 1 biot / abampere ([KElectricCurrentUnit.BIOT]). */
val biot: KElectricCurrentUnitInstance = electricCurrentOf(KElectricCurrentUnit.BIOT.baseValue)

/** 1 abampere, the alias spelling of the biot ([KElectricCurrentUnit.BIOT]). */
val abamperes: KElectricCurrentUnitInstance = electricCurrentOf(KElectricCurrentUnit.BIOT.baseValue)

/** 1 statampere ([KElectricCurrentUnit.STATAMPERE]). */
val statamperes: KElectricCurrentUnitInstance = electricCurrentOf(KElectricCurrentUnit.STATAMPERE.baseValue)
