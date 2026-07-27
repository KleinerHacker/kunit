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

package org.pcsoft.framework.kunit.conductivity

// Bare, value-1 conductivity tokens (each = 1 unit, normalized to siemens per meter). Vocabulary for
// building (`10 of siemensPerMeter`) and reading (`sigma into siemensPerCentimeter`); combine with the
// prefix builders (`mega.siemensPerMeter`). Prefixed forms live in KConductivityUnitExtensions.kt.

/** 1 siemens per meter ([KConductivityUnit.SIEMENS_PER_METER]). */
val siemensPerMeter: KConductivityUnitInstance =
    conductivityInstanceOf(KConductivityUnit.SIEMENS_PER_METER.baseValue)

/** 1 siemens per centimeter ([KConductivityUnit.SIEMENS_PER_CENTIMETER], 100 S/m). */
val siemensPerCentimeter: KConductivityUnitInstance =
    conductivityInstanceOf(KConductivityUnit.SIEMENS_PER_CENTIMETER.baseValue)

/** 1 microsiemens per centimeter ([KConductivityUnit.MICROSIEMENS_PER_CENTIMETER], water quality, 1e-4 S/m). */
val microsiemensPerCentimeter: KConductivityUnitInstance =
    conductivityInstanceOf(KConductivityUnit.MICROSIEMENS_PER_CENTIMETER.baseValue)

/** 1 megasiemens per meter ([KConductivityUnit.MEGASIEMENS_PER_METER], metals, 1e6 S/m). */
val megasiemensPerMeter: KConductivityUnitInstance =
    conductivityInstanceOf(KConductivityUnit.MEGASIEMENS_PER_METER.baseValue)
