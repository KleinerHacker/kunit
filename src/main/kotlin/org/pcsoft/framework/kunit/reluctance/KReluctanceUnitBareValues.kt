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

package org.pcsoft.framework.kunit.reluctance

// Bare, value-1 reluctance tokens (each = 1 unit, normalized to amperes per weber). Vocabulary for building
// (`10 of amperesPerWeber`) and reading (`rm into inverseHenries`); combine with the prefix builders
// (`mega.amperesPerWeber`). Prefixed forms live in KReluctanceUnitExtensions.kt.

/** 1 ampere per weber ([KReluctanceUnit.AMPERE_PER_WEBER]). */
val amperesPerWeber: KReluctanceUnitInstance =
    reluctanceInstanceOf(KReluctanceUnit.AMPERE_PER_WEBER.baseValue)

/** 1 inverse henry ([KReluctanceUnit.INVERSE_HENRY], the reciprocal-inductance spelling, 1 H⁻¹ = 1 A/Wb). */
val inverseHenries: KReluctanceUnitInstance = reluctanceInstanceOf(KReluctanceUnit.INVERSE_HENRY.baseValue)

/** 1 ampere turn per weber ([KReluctanceUnit.AMPERE_TURN_PER_WEBER], 1 At/Wb = 1 A/Wb). */
val ampereTurnsPerWeber: KReluctanceUnitInstance =
    reluctanceInstanceOf(KReluctanceUnit.AMPERE_TURN_PER_WEBER.baseValue)
