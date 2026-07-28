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

package org.pcsoft.framework.kunit.electricfluxdensity

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 electric flux density templates: one property per unit on the prefix builder (e.g.
// `micro.coulombsPerSquareMeter` = 1e-6 C/m²). Electric flux density accepts *any* magnitude, so the
// properties hang on the common base [KPrefixBuilder]. Use with `of`/`into`, e.g.
// `2 of micro.coulombsPerSquareMeter`, `d into milli.coulombsPerSquareMeter`.

private fun prefixedElectricFluxDensity(
    builder: KPrefixBuilder,
    unit: KElectricFluxDensityUnit,
): KElectricFluxDensityUnitInstance =
    electricFluxDensityInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed coulombs per square meter, e.g. `micro.coulombsPerSquareMeter` = 1e-6 C/m². */
val KPrefixBuilder.coulombsPerSquareMeter: KElectricFluxDensityUnitInstance
    get() = prefixedElectricFluxDensity(this, KElectricFluxDensityUnit.COULOMB_PER_SQUARE_METER)

/** Prefixed coulombs per square centimeter, e.g. `micro.coulombsPerSquareCentimeter` = 0.01 C/m². */
val KPrefixBuilder.coulombsPerSquareCentimeter: KElectricFluxDensityUnitInstance
    get() = prefixedElectricFluxDensity(this, KElectricFluxDensityUnit.COULOMB_PER_SQUARE_CENTIMETER)
