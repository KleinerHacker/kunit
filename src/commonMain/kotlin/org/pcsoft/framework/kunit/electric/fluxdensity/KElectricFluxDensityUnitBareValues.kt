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

package org.pcsoft.framework.kunit.electric.fluxdensity

// Bare, value-1 electric flux density tokens (each = 1 unit, normalized to coulombs per square meter).
// Vocabulary for building (`10 of coulombsPerSquareMeter`) and reading (`d into coulombsPerSquareCentimeter`);
// combine with the prefix builders (`micro.coulombsPerSquareMeter`). Prefixed forms live in
// KElectricFluxDensityUnitExtensions.kt.

/** 1 coulomb per square meter ([KElectricFluxDensityUnit.COULOMB_PER_SQUARE_METER]). */
val coulombsPerSquareMeter: KElectricFluxDensityUnitInstance =
    electricFluxDensityInstanceOf(KElectricFluxDensityUnit.COULOMB_PER_SQUARE_METER.baseValue)

/** 1 coulomb per square centimeter ([KElectricFluxDensityUnit.COULOMB_PER_SQUARE_CENTIMETER], 1e4 C/m²). */
val coulombsPerSquareCentimeter: KElectricFluxDensityUnitInstance =
    electricFluxDensityInstanceOf(KElectricFluxDensityUnit.COULOMB_PER_SQUARE_CENTIMETER.baseValue)
