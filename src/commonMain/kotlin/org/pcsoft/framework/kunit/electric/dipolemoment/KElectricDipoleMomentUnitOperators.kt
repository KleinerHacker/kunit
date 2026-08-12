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

package org.pcsoft.framework.kunit.electric.dipolemoment

import org.pcsoft.framework.kunit.electric.charge.KChargeUnitInstance
import org.pcsoft.framework.kunit.electric.charge.chargeInstanceOf
import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.lengthOf

// Cross-unit operators between charge, length and electric dipole moment (`p = Q · d`). They live in the
// electricDipoleMoment package because the group depends on charge and distance (never the other way round),
// so placing them here avoids a dependency cycle. Every typed decomposition funnels into a group factory
// (`electricDipoleMomentInstanceOf` / `chargeInstanceOf` / `lengthOf`), keeping all decomposition paths
// value-equal.

/**
 * Decomposition A - dipole moment from a charge separated by a distance: `charge * length =
 * electricDipoleMoment` (`p = Q · d`).
 *
 * @return the typed [KElectricDipoleMomentUnitInstance] (`this.value * length.value` C·m).
 */
operator fun KChargeUnitInstance.times(length: KLengthUnitInstance): KElectricDipoleMomentUnitInstance =
    electricDipoleMomentInstanceOf(value * length.value)

/**
 * Commutative form of [KChargeUnitInstance.times] - `length · charge = electricDipoleMoment`.
 *
 * @return the typed [KElectricDipoleMomentUnitInstance] (`charge.value * this.value` C·m).
 */
operator fun KLengthUnitInstance.times(charge: KChargeUnitInstance): KElectricDipoleMomentUnitInstance =
    electricDipoleMomentInstanceOf(charge.value * value)

/**
 * Solved for the separation - `electricDipoleMoment / charge = length` (`d = p / Q`).
 *
 * @return the typed [KLengthUnitInstance] (`this.value / charge.value` meters).
 */
operator fun KElectricDipoleMomentUnitInstance.div(charge: KChargeUnitInstance): KLengthUnitInstance =
    lengthOf(value / charge.value)

/**
 * Solved for the charge - `electricDipoleMoment / length = charge` (`Q = p / d`).
 *
 * @return the typed [KChargeUnitInstance] (`this.value / length.value` coulombs).
 */
operator fun KElectricDipoleMomentUnitInstance.div(length: KLengthUnitInstance): KChargeUnitInstance =
    chargeInstanceOf(value / length.value)
