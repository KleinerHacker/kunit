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

package org.pcsoft.framework.kunit.thermo.molality

// Value-1 molality templates for the named units of the group, used with `of`/`into`
// (`0.5 of molesPerKilogram`). Prefixed forms live in KMolalityUnitExtensions.kt.

/** 1 mol/kg ([KMolalityUnit.MOLES_PER_KILOGRAM]), the group's base unit. */
val molesPerKilogram: KMolalityUnitInstance = molalityOfUnit(KMolalityUnit.MOLES_PER_KILOGRAM)

/** 1 mmol/kg ([KMolalityUnit.MILLIMOLES_PER_KILOGRAM], 0.001 mol/kg). */
val millimolesPerKilogram: KMolalityUnitInstance =
    molalityOfUnit(KMolalityUnit.MILLIMOLES_PER_KILOGRAM)
