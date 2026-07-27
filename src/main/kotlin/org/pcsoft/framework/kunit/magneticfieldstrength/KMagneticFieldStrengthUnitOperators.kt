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

package org.pcsoft.framework.kunit.magneticfieldstrength

import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.ec.KElectricCurrentUnitInstance
import org.pcsoft.framework.kunit.ec.electricCurrentOf

// Cross-unit operators between electric current, length and magnetic field strength (`H = I / l`). They
// live in the magnetic field strength package because it may depend on electric current and distance (the
// reverse must never happen). Every typed decomposition funnels into the group factory
// (`magneticFieldStrengthInstanceOf` / `electricCurrentOf`), keeping all decomposition paths value-equal.
//
// This set is additively extensible: further field-strength-adjacent decompositions can be added here
// later without touching the existing operators.

/**
 * Decomposition A - the defining relation solved for field strength: `current / length = field strength`.
 * Since the divisor is a [KLengthUnitInstance] (exponent 1 by construction), this is compile-time safe.
 *
 * @return the typed [KMagneticFieldStrengthUnitInstance] (`this.value / length.value` A/m).
 */
operator fun KElectricCurrentUnitInstance.div(length: KLengthUnitInstance): KMagneticFieldStrengthUnitInstance =
    magneticFieldStrengthInstanceOf(value / length.value)

/**
 * Inverse of the defining relation - `field strength · length = current` (the magnetomotive force along
 * the path, i.e. turns × current).
 *
 * @return the typed [KElectricCurrentUnitInstance] (`this.value * length.value` amperes).
 */
operator fun KMagneticFieldStrengthUnitInstance.times(length: KLengthUnitInstance): KElectricCurrentUnitInstance =
    electricCurrentOf(value * length.value)

/**
 * Commutative form of [times] - `length · field strength = current`.
 *
 * @return the typed [KElectricCurrentUnitInstance] (`this.value * fieldStrength.value` amperes).
 */
operator fun KLengthUnitInstance.times(fieldStrength: KMagneticFieldStrengthUnitInstance): KElectricCurrentUnitInstance =
    electricCurrentOf(value * fieldStrength.value)
