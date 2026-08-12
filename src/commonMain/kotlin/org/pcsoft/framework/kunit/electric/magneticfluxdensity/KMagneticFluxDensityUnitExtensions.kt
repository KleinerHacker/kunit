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

package org.pcsoft.framework.kunit.electric.magneticfluxdensity

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 magnetic flux density templates: one property per unit on the prefix builder (e.g.
// `micro.teslas` = 1e-6 T, `milli.teslas` = 0.001 T). Magnetic flux density accepts *any* magnitude, so
// the properties hang on the common base [KPrefixBuilder]. Use with `of`/`into`, e.g.
// `50 of micro.teslas`, `b into milli.teslas`.

private fun prefixedMagneticFluxDensity(
    builder: KPrefixBuilder,
    unit: KMagneticFluxDensityUnit,
): KMagneticFluxDensityUnitInstance = magneticFluxDensityInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed teslas, e.g. `milli.teslas` = 0.001 T, `micro.teslas` = 1e-6 T. */
val KPrefixBuilder.teslas: KMagneticFluxDensityUnitInstance
    get() = prefixedMagneticFluxDensity(this, KMagneticFluxDensityUnit.TESLA)

/** Prefixed webers per square meter, e.g. `milli.webersPerSquareMeter`. */
val KPrefixBuilder.webersPerSquareMeter: KMagneticFluxDensityUnitInstance
    get() = prefixedMagneticFluxDensity(this, KMagneticFluxDensityUnit.WEBER_PER_SQUARE_METER)

/** Prefixed gauss, e.g. `kilo.gauss` = 0.1 T. */
val KPrefixBuilder.gauss: KMagneticFluxDensityUnitInstance
    get() = prefixedMagneticFluxDensity(this, KMagneticFluxDensityUnit.GAUSS)

/** Prefixed gammas, e.g. `kilo.gammas` = 1e-6 T. */
val KPrefixBuilder.gammas: KMagneticFluxDensityUnitInstance
    get() = prefixedMagneticFluxDensity(this, KMagneticFluxDensityUnit.GAMMA)
