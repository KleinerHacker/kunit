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

package org.pcsoft.framework.kunit.electric.magneticfieldstrength

// Bare, value-1 magnetic field strength tokens (each = 1 unit, normalized to A/m). Vocabulary for
// building (`10 of amperesPerMeter`) and reading (`h into oersteds`); combine with the prefix builders
// (`kilo.amperesPerMeter`). Prefixed forms live in KMagneticFieldStrengthUnitExtensions.kt.

/** 1 ampere per meter ([KMagneticFieldStrengthUnit.AMPERE_PER_METER]). */
val amperesPerMeter: KMagneticFieldStrengthUnitInstance =
    magneticFieldStrengthInstanceOf(KMagneticFieldStrengthUnit.AMPERE_PER_METER.baseValue)

/** 1 oersted ([KMagneticFieldStrengthUnit.OERSTED], CGS-EMU, 1000/4π A/m). */
val oersteds: KMagneticFieldStrengthUnitInstance =
    magneticFieldStrengthInstanceOf(KMagneticFieldStrengthUnit.OERSTED.baseValue)

/** 1 gilbert per centimeter ([KMagneticFieldStrengthUnit.GILBERT_PER_CENTIMETER], numerically the oersted). */
val gilbertsPerCentimeter: KMagneticFieldStrengthUnitInstance =
    magneticFieldStrengthInstanceOf(KMagneticFieldStrengthUnit.GILBERT_PER_CENTIMETER.baseValue)

/** 1 ampere-turn per inch ([KMagneticFieldStrengthUnit.AMPERE_TURN_PER_INCH], 39.37007874015748 A/m). */
val ampereTurnsPerInch: KMagneticFieldStrengthUnitInstance =
    magneticFieldStrengthInstanceOf(KMagneticFieldStrengthUnit.AMPERE_TURN_PER_INCH.baseValue)
