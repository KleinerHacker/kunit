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

package org.pcsoft.framework.kunit.electric.specificcharge

import org.pcsoft.framework.kunit.electric.charge.KChargeUnitInstance
import org.pcsoft.framework.kunit.electric.charge.chargeInstanceOf
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.massOf

// Cross-group operators for the decomposition of the specific charge - `charge / mass` - plus its
// inverses. They live in the specific charge package because it may depend on charge/mass (the reverse
// must never happen).
//
// The charge stores its value in coulombs while the mass group's base unit is the gram, so every operator
// bridges with COULOMB_PER_KILOGRAM_MASS_REFERENCE.

/**
 * Divides a charge by a mass to obtain a [KSpecificChargeUnitInstance] (`charge / mass = specific charge`).
 *
 * Example:
 * ```kotlin
 * val ratio = (1 of coulombs) / (1 of kilo.grams) // 1 C/kg
 * ```
 */
operator fun KChargeUnitInstance.div(other: KMassUnitInstance): KSpecificChargeUnitInstance =
    specificChargeInstanceOf(value / other.value * COULOMB_PER_KILOGRAM_MASS_REFERENCE)

/**
 * Multiplies a specific charge by a mass to obtain the total charge
 * (`specific charge * mass = charge`).
 */
operator fun KSpecificChargeUnitInstance.times(other: KMassUnitInstance): KChargeUnitInstance =
    chargeInstanceOf(value * other.value / COULOMB_PER_KILOGRAM_MASS_REFERENCE)

/**
 * Multiplies a mass by a specific charge to obtain the total charge; the commutative counterpart of
 * [KSpecificChargeUnitInstance.times].
 */
operator fun KMassUnitInstance.times(other: KSpecificChargeUnitInstance): KChargeUnitInstance =
    chargeInstanceOf(value * other.value / COULOMB_PER_KILOGRAM_MASS_REFERENCE)

/**
 * Divides a charge by a specific charge to obtain the mass carrying it
 * (`charge / specific charge = mass`).
 */
operator fun KChargeUnitInstance.div(other: KSpecificChargeUnitInstance): KMassUnitInstance =
    massOf(value / other.value * COULOMB_PER_KILOGRAM_MASS_REFERENCE)
