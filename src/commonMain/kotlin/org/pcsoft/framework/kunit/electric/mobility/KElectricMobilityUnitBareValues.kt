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

package org.pcsoft.framework.kunit.electric.mobility

// Bare, value-1 electric mobility tokens (each = 1 unit, normalized to square meters per volt second).
// Vocabulary for building (`1400 of squareCentimetersPerVoltSecond`) and reading
// (`mu into squareMetersPerVoltSecond`); combine with the prefix builders. Prefixed forms live in
// KElectricMobilityUnitExtensions.kt.

/** 1 square meter per volt second ([KElectricMobilityUnit.SQUARE_METER_PER_VOLT_SECOND]). */
val squareMetersPerVoltSecond: KElectricMobilityUnitInstance =
    electricMobilityInstanceOf(KElectricMobilityUnit.SQUARE_METER_PER_VOLT_SECOND.baseValue)

/**
 * 1 square centimeter per volt second ([KElectricMobilityUnit.SQUARE_CENTIMETER_PER_VOLT_SECOND],
 * 1e-4 m²/(V·s)) - the notation used throughout semiconductor physics.
 */
val squareCentimetersPerVoltSecond: KElectricMobilityUnitInstance =
    electricMobilityInstanceOf(KElectricMobilityUnit.SQUARE_CENTIMETER_PER_VOLT_SECOND.baseValue)
