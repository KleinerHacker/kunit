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

package org.pcsoft.framework.kunit.permeability

// Bare, value-1 permeability tokens (each = 1 unit, normalized to henries per meter). Vocabulary for building
// (`10 of henriesPerMeter`) and reading (`mu into henriesPerCentimeter`); combine with the prefix builders
// (`micro.henriesPerMeter`). Prefixed forms live in KPermeabilityUnitExtensions.kt.

/** 1 henry per meter ([KPermeabilityUnit.HENRY_PER_METER]). */
val henriesPerMeter: KPermeabilityUnitInstance =
    permeabilityInstanceOf(KPermeabilityUnit.HENRY_PER_METER.baseValue)

/** 1 henry per centimeter ([KPermeabilityUnit.HENRY_PER_CENTIMETER], 100 H/m). */
val henriesPerCentimeter: KPermeabilityUnitInstance =
    permeabilityInstanceOf(KPermeabilityUnit.HENRY_PER_CENTIMETER.baseValue)

/** The vacuum permeability `μ₀` ([KPermeabilityUnit.VACUUM_PERMEABILITY], 1.256 637 061 27e-6 H/m). */
val vacuumPermeability: KPermeabilityUnitInstance =
    permeabilityInstanceOf(KPermeabilityUnit.VACUUM_PERMEABILITY)
