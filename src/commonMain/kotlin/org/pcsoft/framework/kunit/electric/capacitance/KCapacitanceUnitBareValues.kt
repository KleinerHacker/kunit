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

package org.pcsoft.framework.kunit.electric.capacitance

// Bare, value-1 capacitance tokens (each = 1 unit, normalized to farads). Vocabulary for building
// (`10 of farads`) and reading (`c into micro.farads`); combine with the prefix builders
// (`micro.farads`). Prefixed forms live in KCapacitanceUnitExtensions.kt.

/** 1 farad ([KCapacitanceUnit.FARAD]). */
val farads: KCapacitanceUnitInstance = capacitanceInstanceOf(KCapacitanceUnit.FARAD.baseValue)

/** 1 abfarad ([KCapacitanceUnit.ABFARAD], CGS-EMU, 1e9 F). */
val abfarads: KCapacitanceUnitInstance = capacitanceInstanceOf(KCapacitanceUnit.ABFARAD.baseValue)

/** 1 statfarad ([KCapacitanceUnit.STATFARAD], CGS-ESU, 1.112650056e-12 F). */
val statfarads: KCapacitanceUnitInstance = capacitanceInstanceOf(KCapacitanceUnit.STATFARAD.baseValue)

/** 1 jar ([KCapacitanceUnit.JAR], historic Leyden-jar unit, 1.11265e-9 F). */
val jars: KCapacitanceUnitInstance = capacitanceInstanceOf(KCapacitanceUnit.JAR.baseValue)
