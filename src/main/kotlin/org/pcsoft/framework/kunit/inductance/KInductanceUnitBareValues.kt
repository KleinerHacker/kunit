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

package org.pcsoft.framework.kunit.inductance

// Bare, value-1 inductance tokens (each = 1 unit, normalized to henries). Vocabulary for building
// (`10 of henries`) and reading (`l into milli.henries`); combine with the prefix builders
// (`milli.henries`). Prefixed forms live in KInductanceUnitExtensions.kt.

/** 1 henry ([KInductanceUnit.HENRY]). */
val henries: KInductanceUnitInstance = inductanceInstanceOf(KInductanceUnit.HENRY.baseValue)

/** 1 weber per ampere ([KInductanceUnit.WEBER_PER_AMPERE], 1 H). */
val webersPerAmpere: KInductanceUnitInstance = inductanceInstanceOf(KInductanceUnit.WEBER_PER_AMPERE.baseValue)

/** 1 abhenry ([KInductanceUnit.ABHENRY], CGS-EMU, 1e-9 H). */
val abhenries: KInductanceUnitInstance = inductanceInstanceOf(KInductanceUnit.ABHENRY.baseValue)

/** 1 stathenry ([KInductanceUnit.STATHENRY], CGS-ESU, 8.987551787e11 H). */
val stathenries: KInductanceUnitInstance = inductanceInstanceOf(KInductanceUnit.STATHENRY.baseValue)
