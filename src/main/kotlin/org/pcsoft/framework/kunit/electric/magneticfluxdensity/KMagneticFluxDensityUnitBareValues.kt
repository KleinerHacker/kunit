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

// Bare, value-1 magnetic flux density tokens (each = 1 unit, normalized to teslas). Vocabulary for
// building (`3 of teslas`) and reading (`b into gauss`); combine with the prefix builders
// (`micro.teslas`). Prefixed forms live in KMagneticFluxDensityUnitExtensions.kt.

/** 1 tesla ([KMagneticFluxDensityUnit.TESLA]). */
val teslas: KMagneticFluxDensityUnitInstance =
    magneticFluxDensityInstanceOf(KMagneticFluxDensityUnit.TESLA.baseValue)

/** 1 weber per square meter ([KMagneticFluxDensityUnit.WEBER_PER_SQUARE_METER], 1 T). */
val webersPerSquareMeter: KMagneticFluxDensityUnitInstance =
    magneticFluxDensityInstanceOf(KMagneticFluxDensityUnit.WEBER_PER_SQUARE_METER.baseValue)

/** 1 gauss ([KMagneticFluxDensityUnit.GAUSS], CGS-EMU, 1e-4 T). */
val gauss: KMagneticFluxDensityUnitInstance =
    magneticFluxDensityInstanceOf(KMagneticFluxDensityUnit.GAUSS.baseValue)

/** 1 gamma ([KMagneticFluxDensityUnit.GAMMA], 1e-9 T). */
val gammas: KMagneticFluxDensityUnitInstance =
    magneticFluxDensityInstanceOf(KMagneticFluxDensityUnit.GAMMA.baseValue)
