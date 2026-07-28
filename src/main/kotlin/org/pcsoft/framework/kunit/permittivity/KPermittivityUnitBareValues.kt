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

package org.pcsoft.framework.kunit.permittivity

// Bare, value-1 permittivity tokens (each = 1 unit, normalized to farads per meter). Vocabulary for building
// (`10 of faradsPerMeter`) and reading (`eps into faradsPerCentimeter`); combine with the prefix builders
// (`pico.faradsPerMeter`). Prefixed forms live in KPermittivityUnitExtensions.kt.

/** 1 farad per meter ([KPermittivityUnit.FARAD_PER_METER]). */
val faradsPerMeter: KPermittivityUnitInstance =
    permittivityInstanceOf(KPermittivityUnit.FARAD_PER_METER.baseValue)

/** 1 farad per centimeter ([KPermittivityUnit.FARAD_PER_CENTIMETER], 100 F/m). */
val faradsPerCentimeter: KPermittivityUnitInstance =
    permittivityInstanceOf(KPermittivityUnit.FARAD_PER_CENTIMETER.baseValue)

/** The vacuum permittivity `ε₀` ([KPermittivityUnit.VACUUM_PERMITTIVITY], 8.854 187 8188e-12 F/m). */
val vacuumPermittivity: KPermittivityUnitInstance =
    permittivityInstanceOf(KPermittivityUnit.VACUUM_PERMITTIVITY)
