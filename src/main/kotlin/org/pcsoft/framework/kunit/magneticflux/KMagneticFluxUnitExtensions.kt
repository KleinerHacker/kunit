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

package org.pcsoft.framework.kunit.magneticflux

import org.pcsoft.framework.kunit.KPrefixBuilder

// Prefixed, value-1 magnetic flux templates: one property per flux unit on the prefix builder (e.g.
// `milli.webers` = 1e-3 Wb, `micro.webers` = 1e-6 Wb). Magnetic flux accepts *any* magnitude, so the
// properties hang on the common base [KPrefixBuilder]. Use with `of`/`into`, e.g.
// `20 of milli.webers`, `phi into micro.webers`.

private fun prefixedMagneticFlux(builder: KPrefixBuilder, unit: KMagneticFluxUnit): KMagneticFluxUnitInstance =
    magneticFluxInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed webers, e.g. `milli.webers` = 1e-3 Wb, `micro.webers` = 1e-6 Wb. */
val KPrefixBuilder.webers: KMagneticFluxUnitInstance get() = prefixedMagneticFlux(this, KMagneticFluxUnit.WEBER)

/** Prefixed maxwells, e.g. `kilo.maxwells`. */
val KPrefixBuilder.maxwells: KMagneticFluxUnitInstance get() = prefixedMagneticFlux(this, KMagneticFluxUnit.MAXWELL)

/** Prefixed unit poles, e.g. `kilo.unitPoles`. */
val KPrefixBuilder.unitPoles: KMagneticFluxUnitInstance get() = prefixedMagneticFlux(this, KMagneticFluxUnit.UNIT_POLE)
