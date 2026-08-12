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

package org.pcsoft.framework.kunit.electric.magneticflux

// Bare, value-1 magnetic flux tokens (each = 1 unit, normalized to webers). Vocabulary for building
// (`10 of webers`) and reading (`phi into maxwells`); combine with the prefix builders
// (`milli.webers`). Prefixed forms live in KMagneticFluxUnitExtensions.kt.

/** 1 weber ([KMagneticFluxUnit.WEBER]). */
val webers: KMagneticFluxUnitInstance = magneticFluxInstanceOf(KMagneticFluxUnit.WEBER.baseValue)

/** 1 maxwell ([KMagneticFluxUnit.MAXWELL], CGS-EMU, 1e-8 Wb). */
val maxwells: KMagneticFluxUnitInstance = magneticFluxInstanceOf(KMagneticFluxUnit.MAXWELL.baseValue)

/** 1 unit pole ([KMagneticFluxUnit.UNIT_POLE], historic CGS pole flux, 4π·1e-8 Wb). */
val unitPoles: KMagneticFluxUnitInstance = magneticFluxInstanceOf(KMagneticFluxUnit.UNIT_POLE.baseValue)
