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

package org.pcsoft.framework.kunit.permittivity

import org.pcsoft.framework.kunit.capacitance.KCapacitanceUnitInstance
import org.pcsoft.framework.kunit.capacitance.capacitanceInstanceOf
import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.distance.lengthOf
import org.pcsoft.framework.kunit.electricfieldstrength.KElectricFieldStrengthUnitInstance
import org.pcsoft.framework.kunit.electricfieldstrength.electricFieldStrengthInstanceOf
import org.pcsoft.framework.kunit.electricfluxdensity.KElectricFluxDensityUnitInstance
import org.pcsoft.framework.kunit.electricfluxdensity.electricFluxDensityInstanceOf

// Cross-unit operators around the permittivity: the capacitance decomposition (`ε = C · d / A`, where the
// geometry factor `d / A` is a length) and the field decomposition (`ε = D / E`). They live in the
// permittivity package because the group depends on capacitance, distance, electric flux density and
// electric field strength (never the other way round), so placing them here avoids a dependency cycle. Every
// typed decomposition funnels into a group factory (`permittivityInstanceOf` / `capacitanceInstanceOf` /
// `lengthOf` / `electricFluxDensityInstanceOf` / `electricFieldStrengthInstanceOf`), keeping all
// decomposition paths value-equal.

// --- Decomposition A: capacitance over the plate geometry ----------------------------------------

/**
 * Decomposition A - permittivity from a capacitance and the plate geometry: `capacitance / length =
 * permittivity` (`ε = C · d / A`; the geometry factor `d / A` - a plate distance over a plate area - is
 * itself a length).
 *
 * @return the typed [KPermittivityUnitInstance] (`this.value / length.value` F/m).
 */
operator fun KCapacitanceUnitInstance.div(length: KLengthUnitInstance): KPermittivityUnitInstance =
    permittivityInstanceOf(value / length.value)

/**
 * Solved for the capacitance - `permittivity * length = capacitance` (`C = ε · A / d`, multiplying the
 * geometry factor back in).
 *
 * @return the typed [KCapacitanceUnitInstance] (`this.value * length.value` farads).
 */
operator fun KPermittivityUnitInstance.times(length: KLengthUnitInstance): KCapacitanceUnitInstance =
    capacitanceInstanceOf(value * length.value)

/**
 * Commutative form of [KPermittivityUnitInstance.times] - `length · permittivity = capacitance`.
 *
 * @return the typed [KCapacitanceUnitInstance] (`permittivity.value * this.value` farads).
 */
operator fun KLengthUnitInstance.times(permittivity: KPermittivityUnitInstance): KCapacitanceUnitInstance =
    capacitanceInstanceOf(permittivity.value * value)

/**
 * Solved for the geometry factor - `capacitance / permittivity = length` (`A / d = C / ε`).
 *
 * @return the typed [KLengthUnitInstance] (`this.value / permittivity.value` meters).
 */
operator fun KCapacitanceUnitInstance.div(permittivity: KPermittivityUnitInstance): KLengthUnitInstance =
    lengthOf(value / permittivity.value)

// --- Decomposition B: flux density over field strength -------------------------------------------

/**
 * Decomposition B - permittivity as the ratio of electric flux density to field strength:
 * `electricFluxDensity / electricFieldStrength = permittivity` (`ε = D / E`).
 *
 * @return the typed [KPermittivityUnitInstance] (`this.value / fieldStrength.value` F/m).
 */
operator fun KElectricFluxDensityUnitInstance.div(
    fieldStrength: KElectricFieldStrengthUnitInstance,
): KPermittivityUnitInstance = permittivityInstanceOf(value / fieldStrength.value)

/**
 * Solved for the flux density - `permittivity * electricFieldStrength = electricFluxDensity` (`D = ε · E`).
 *
 * @return the typed [KElectricFluxDensityUnitInstance] (`this.value * fieldStrength.value` C/m²).
 */
operator fun KPermittivityUnitInstance.times(
    fieldStrength: KElectricFieldStrengthUnitInstance,
): KElectricFluxDensityUnitInstance = electricFluxDensityInstanceOf(value * fieldStrength.value)

/**
 * Commutative form of [KPermittivityUnitInstance.times] - `electricFieldStrength · permittivity =
 * electricFluxDensity`.
 *
 * @return the typed [KElectricFluxDensityUnitInstance] (`permittivity.value * this.value` C/m²).
 */
operator fun KElectricFieldStrengthUnitInstance.times(
    permittivity: KPermittivityUnitInstance,
): KElectricFluxDensityUnitInstance = electricFluxDensityInstanceOf(permittivity.value * value)

/**
 * Solved for the field strength - `electricFluxDensity / permittivity = electricFieldStrength` (`E = D / ε`).
 *
 * @return the typed [KElectricFieldStrengthUnitInstance] (`this.value / permittivity.value` V/m).
 */
operator fun KElectricFluxDensityUnitInstance.div(
    permittivity: KPermittivityUnitInstance,
): KElectricFieldStrengthUnitInstance = electricFieldStrengthInstanceOf(value / permittivity.value)
