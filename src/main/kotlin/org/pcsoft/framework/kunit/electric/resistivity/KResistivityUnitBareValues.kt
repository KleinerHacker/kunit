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

package org.pcsoft.framework.kunit.electric.resistivity

// Bare, value-1 resistivity tokens (each = 1 unit, normalized to ohm meters). Vocabulary for building
// (`10 of ohmMeters`) and reading (`rho into ohmCentimeters`); combine with the prefix builders
// (`nano.ohmMeters`). Prefixed forms live in KResistivityUnitExtensions.kt.

/** 1 ohm meter ([KResistivityUnit.OHM_METER]). */
val ohmMeters: KResistivityUnitInstance = resistivityInstanceOf(KResistivityUnit.OHM_METER.baseValue)

/** 1 ohm centimeter ([KResistivityUnit.OHM_CENTIMETER], 0.01 Ω·m). */
val ohmCentimeters: KResistivityUnitInstance = resistivityInstanceOf(KResistivityUnit.OHM_CENTIMETER.baseValue)

/** 1 statohm centimeter ([KResistivityUnit.STATOHM_CENTIMETER], CGS-ESU, 8.98755179e9 Ω·m). */
val statohmCentimeters: KResistivityUnitInstance =
    resistivityInstanceOf(KResistivityUnit.STATOHM_CENTIMETER.baseValue)
