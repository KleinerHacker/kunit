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

package org.pcsoft.framework.kunit.resistivity

import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.distance.lengthOf
import org.pcsoft.framework.kunit.resistance.KResistanceUnitInstance
import org.pcsoft.framework.kunit.resistance.resistanceInstanceOf

// Cross-unit operators between resistance, length and resistivity (`ρ = R · A / l`, where the geometry
// factor `A / l` - a cross-section over a conductor length - is itself a length). They live in the
// resistivity package because resistivity depends on resistance and distance (never the other way round),
// so placing them here avoids a dependency cycle. Every typed decomposition funnels into the group factory
// (`resistivityInstanceOf` / `resistanceInstanceOf` / `lengthOf`), keeping all decomposition paths
// value-equal.
//
// This set is additively extensible: further resistivity-adjacent decompositions (e.g. the reciprocal pair
// with conductivity, see the conductivity package) can be added later without touching the existing
// operators.

/**
 * Decomposition A - resistivity from a resistance and the conductor geometry: `resistance · length =
 * resistivity` (`ρ = R · A / l`; the geometry factor `A / l` is a length).
 *
 * @return the typed [KResistivityUnitInstance] (`this.value * length.value` Ω·m).
 */
operator fun KResistanceUnitInstance.times(length: KLengthUnitInstance): KResistivityUnitInstance =
    resistivityInstanceOf(value * length.value)

/**
 * Commutative form of [KResistanceUnitInstance.times] - `length · resistance = resistivity`.
 *
 * @return the typed [KResistivityUnitInstance] (`resistance.value * this.value` Ω·m).
 */
operator fun KLengthUnitInstance.times(resistance: KResistanceUnitInstance): KResistivityUnitInstance =
    resistivityInstanceOf(resistance.value * value)

/**
 * Solved for the resistance - `resistivity / length = resistance` (`R = ρ · l / A`, dividing out the
 * geometry factor).
 *
 * @return the typed [KResistanceUnitInstance] (`this.value / length.value` ohms).
 */
operator fun KResistivityUnitInstance.div(length: KLengthUnitInstance): KResistanceUnitInstance =
    resistanceInstanceOf(value / length.value)

/**
 * Solved for the geometry factor - `resistivity / resistance = length` (`A / l = ρ / R`).
 *
 * @return the typed [KLengthUnitInstance] (`this.value / resistance.value` meters).
 */
operator fun KResistivityUnitInstance.div(resistance: KResistanceUnitInstance): KLengthUnitInstance =
    lengthOf(value / resistance.value)
