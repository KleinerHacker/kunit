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

package org.pcsoft.framework.kunit.mechanic.massflow

// Value-1 mass-flow templates for the named units of the group, used with `of`/`into`
// (`5 of tonnesPerHour`, `m into kilogramsPerSecond`). Prefixed forms live in
// KMassFlowUnitExtensions.kt.

/** 1 kg/s ([KMassFlowUnit.KILOGRAMS_PER_SECOND]), the group's base unit. */
val kilogramsPerSecond: KMassFlowUnitInstance = massFlowOfUnit(KMassFlowUnit.KILOGRAMS_PER_SECOND)

/** 1 g/s ([KMassFlowUnit.GRAMS_PER_SECOND], 0.001 kg/s). */
val gramsPerSecond: KMassFlowUnitInstance = massFlowOfUnit(KMassFlowUnit.GRAMS_PER_SECOND)

/** 1 kg/h ([KMassFlowUnit.KILOGRAMS_PER_HOUR], 1/3600 kg/s). */
val kilogramsPerHour: KMassFlowUnitInstance = massFlowOfUnit(KMassFlowUnit.KILOGRAMS_PER_HOUR)

/** 1 t/h ([KMassFlowUnit.TONNES_PER_HOUR], 1000/3600 kg/s). */
val tonnesPerHour: KMassFlowUnitInstance = massFlowOfUnit(KMassFlowUnit.TONNES_PER_HOUR)

/** 1 lb/s ([KMassFlowUnit.POUNDS_PER_SECOND], 0.45359237 kg/s). */
val poundsPerSecond: KMassFlowUnitInstance = massFlowOfUnit(KMassFlowUnit.POUNDS_PER_SECOND)

/** 1 lb/h ([KMassFlowUnit.POUNDS_PER_HOUR], 0.45359237/3600 kg/s). */
val poundsPerHour: KMassFlowUnitInstance = massFlowOfUnit(KMassFlowUnit.POUNDS_PER_HOUR)
