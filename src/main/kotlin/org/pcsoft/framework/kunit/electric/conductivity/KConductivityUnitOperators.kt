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

package org.pcsoft.framework.kunit.electric.conductivity

import org.pcsoft.framework.kunit.electric.conductance.KConductanceUnitInstance
import org.pcsoft.framework.kunit.electric.conductance.conductanceInstanceOf
import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.lengthOf
import org.pcsoft.framework.kunit.electric.resistivity.KResistivityUnitInstance
import org.pcsoft.framework.kunit.electric.resistivity.resistivityInstanceOf

// Cross-unit operators of the conductivity group - the reciprocal pair with resistivity (`σ = 1 / ρ`) and
// the conductance-over-geometry form (`σ = G · l / A`, where the geometry factor `l / A` is a reciprocal
// length, so the typed operator divides a conductance by a length). They live in the conductivity package
// because conductivity depends on resistivity, conductance and distance (never the other way round), so
// placing them here avoids a dependency cycle. Every typed decomposition funnels into the group factory
// (`conductivityInstanceOf` / `resistivityInstanceOf` / `conductanceInstanceOf` / `lengthOf`), keeping all
// decomposition paths value-equal.

// --- Reciprocal pair with resistivity (σ = 1 / ρ) ------------------------------------------------

/**
 * Decomposition A - the reciprocal of a resistivity: `1 / resistivity = conductivity` (`σ = 1 / ρ`).
 *
 * The numerator is a plain [Number] (typically `1`), mirroring the conductance/resistance reciprocal pair.
 *
 * @return the typed [KConductivityUnitInstance] (`this / resistivity.value` S/m).
 */
operator fun Number.div(resistivity: KResistivityUnitInstance): KConductivityUnitInstance =
    conductivityInstanceOf(this.toDouble() / resistivity.value)

/**
 * The mirrored reciprocal - `1 / conductivity = resistivity` (`ρ = 1 / σ`).
 *
 * @return the typed [KResistivityUnitInstance] (`this / conductivity.value` Ω·m).
 */
operator fun Number.div(conductivity: KConductivityUnitInstance): KResistivityUnitInstance =
    resistivityInstanceOf(this.toDouble() / conductivity.value)

// --- Conductance over the conductor geometry (σ = G · l / A) --------------------------------------

/**
 * Decomposition B - conductivity from a conductance and the conductor geometry: `conductance / length =
 * conductivity` (`σ = G · l / A`; the geometry factor `l / A` is a reciprocal length, hence the division).
 *
 * @return the typed [KConductivityUnitInstance] (`this.value / length.value` S/m).
 */
operator fun KConductanceUnitInstance.div(length: KLengthUnitInstance): KConductivityUnitInstance =
    conductivityInstanceOf(value / length.value)

/**
 * Solved for the conductance - `conductivity · length = conductance` (`G = σ · A / l`).
 *
 * @return the typed [KConductanceUnitInstance] (`this.value * length.value` siemens).
 */
operator fun KConductivityUnitInstance.times(length: KLengthUnitInstance): KConductanceUnitInstance =
    conductanceInstanceOf(value * length.value)

/**
 * Commutative form of [KConductivityUnitInstance.times] - `length · conductivity = conductance`.
 *
 * @return the typed [KConductanceUnitInstance] (`conductivity.value * this.value` siemens).
 */
operator fun KLengthUnitInstance.times(conductivity: KConductivityUnitInstance): KConductanceUnitInstance =
    conductanceInstanceOf(conductivity.value * value)

/**
 * Solved for the geometry factor - `conductance / conductivity = length` (`A / l = G / σ`).
 *
 * @return the typed [KLengthUnitInstance] (`this.value / conductivity.value` meters).
 */
operator fun KConductanceUnitInstance.div(conductivity: KConductivityUnitInstance): KLengthUnitInstance =
    lengthOf(value / conductivity.value)
