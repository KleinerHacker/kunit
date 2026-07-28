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

package org.pcsoft.framework.kunit.electricfieldstrength

// Bare, value-1 electric field strength tokens (each = 1 unit, normalized to volts per meter). Vocabulary
// for building (`10 of voltsPerMeter`) and reading (`e into voltsPerCentimeter`); combine with the prefix
// builders (`kilo.voltsPerMeter`). Prefixed forms live in KElectricFieldStrengthUnitExtensions.kt.

/** 1 volt per meter ([KElectricFieldStrengthUnit.VOLT_PER_METER]). */
val voltsPerMeter: KElectricFieldStrengthUnitInstance =
    electricFieldStrengthInstanceOf(KElectricFieldStrengthUnit.VOLT_PER_METER.baseValue)

/** 1 volt per centimeter ([KElectricFieldStrengthUnit.VOLT_PER_CENTIMETER], 100 V/m). */
val voltsPerCentimeter: KElectricFieldStrengthUnitInstance =
    electricFieldStrengthInstanceOf(KElectricFieldStrengthUnit.VOLT_PER_CENTIMETER.baseValue)

/** 1 statvolt per centimeter ([KElectricFieldStrengthUnit.STATVOLT_PER_CENTIMETER], CGS-ESU, 29 979.2458 V/m). */
val statvoltsPerCentimeter: KElectricFieldStrengthUnitInstance =
    electricFieldStrengthInstanceOf(KElectricFieldStrengthUnit.STATVOLT_PER_CENTIMETER.baseValue)
