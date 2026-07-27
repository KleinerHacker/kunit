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

package org.pcsoft.framework.kunit.magneticfieldstrength

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 magnetic field strength templates: one property per unit on the prefix builder (e.g.
// `kilo.amperesPerMeter` = 1000 A/m, `milli.amperesPerMeter` = 0.001 A/m). Magnetic field strength
// accepts *any* magnitude, so the properties hang on the common base [KPrefixBuilder]. Use with
// `of`/`into`, e.g. `2 of kilo.amperesPerMeter`, `h into milli.amperesPerMeter`.

private fun prefixedMagneticFieldStrength(
    builder: KPrefixBuilder,
    unit: KMagneticFieldStrengthUnit,
): KMagneticFieldStrengthUnitInstance = magneticFieldStrengthInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed amperes per meter, e.g. `kilo.amperesPerMeter` = 1000 A/m, `milli.amperesPerMeter` = 0.001 A/m. */
val KPrefixBuilder.amperesPerMeter: KMagneticFieldStrengthUnitInstance
    get() = prefixedMagneticFieldStrength(this, KMagneticFieldStrengthUnit.AMPERE_PER_METER)

/** Prefixed oersteds, e.g. `kilo.oersteds`. */
val KPrefixBuilder.oersteds: KMagneticFieldStrengthUnitInstance
    get() = prefixedMagneticFieldStrength(this, KMagneticFieldStrengthUnit.OERSTED)

/** Prefixed gilberts per centimeter, e.g. `milli.gilbertsPerCentimeter`. */
val KPrefixBuilder.gilbertsPerCentimeter: KMagneticFieldStrengthUnitInstance
    get() = prefixedMagneticFieldStrength(this, KMagneticFieldStrengthUnit.GILBERT_PER_CENTIMETER)

/** Prefixed ampere-turns per inch, e.g. `milli.ampereTurnsPerInch`. */
val KPrefixBuilder.ampereTurnsPerInch: KMagneticFieldStrengthUnitInstance
    get() = prefixedMagneticFieldStrength(this, KMagneticFieldStrengthUnit.AMPERE_TURN_PER_INCH)
