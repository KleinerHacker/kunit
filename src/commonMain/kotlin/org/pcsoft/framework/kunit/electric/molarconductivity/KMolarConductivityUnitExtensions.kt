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

package org.pcsoft.framework.kunit.electric.molarconductivity

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 molar conductivity templates: one property per named unit on the prefix builder.
// Use with `of`/`into`, e.g. `12.6 of milli.siemensSquareMetersPerMole`.

private fun prefixedMolarConductivity(
    builder: KPrefixBuilder,
    unit: KMolarConductivityUnit
): KMolarConductivityUnitInstance =
    molarConductivityInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed siemens square meters per mole, e.g. `milli.siemensSquareMetersPerMole`. */
val KPrefixBuilder.siemensSquareMetersPerMole: KMolarConductivityUnitInstance
    get() = prefixedMolarConductivity(this, KMolarConductivityUnit.SIEMENS_SQUARE_METER_PER_MOLE)

/** Prefixed siemens square centimeters per mole, e.g. `kilo.siemensSquareCentimetersPerMole`. */
val KPrefixBuilder.siemensSquareCentimetersPerMole: KMolarConductivityUnitInstance
    get() = prefixedMolarConductivity(this, KMolarConductivityUnit.SIEMENS_SQUARE_CENTIMETER_PER_MOLE)
