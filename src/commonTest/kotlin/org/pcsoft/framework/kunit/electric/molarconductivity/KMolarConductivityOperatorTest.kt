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

package org.pcsoft.framework.kunit.electric.molarconductivity

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.conductivity.KConductivityUnitInstance
import org.pcsoft.framework.kunit.electric.conductivity.siemensPerMeter
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.KConcentrationUnitInstance
import org.pcsoft.framework.kunit.thermo.concentration.molesPerLiter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group molar conductivity operators and the typed/native decomposition equivalence. */
class KMolarConductivityOperatorTest {

    /** `conductivity / concentration = molar conductivity`. */
    @Test
    fun `conductivity over concentration is molar conductivity`() {
        val lambda = (1.0 of siemensPerMeter) / (0.1 of molesPerLiter)
        assertIs<KMolarConductivityUnitInstance>(lambda)
        assertEquals(0.01, lambda into siemensSquareMetersPerMole, 1e-12)
    }

    /** `molar conductivity * concentration = conductivity` and its commutative counterpart. */
    @Test
    fun `molar conductivity times concentration is conductivity`() {
        val lambda = 0.01 of siemensSquareMetersPerMole
        val c = 0.1 of molesPerLiter
        val k1 = lambda * c
        val k2 = c * lambda
        assertIs<KConductivityUnitInstance>(k1)
        assertIs<KConductivityUnitInstance>(k2)
        assertEquals(1.0, k1 into siemensPerMeter, 1e-9)
        assertEquals(1.0, k2 into siemensPerMeter, 1e-9)
    }

    /** `conductivity / molar conductivity = concentration`. */
    @Test
    fun `conductivity over molar conductivity is concentration`() {
        val c = (1.0 of siemensPerMeter) / (0.01 of siemensSquareMetersPerMole)
        assertIs<KConcentrationUnitInstance>(c)
        assertEquals(0.1, c into molesPerLiter, 1e-9)
    }

    /**
     * The typed operator and the native `kg⁻¹·s³·A²·mol⁻¹` expression agree.
     *
     * The native form is assembled from *unit templates*, not from typed instances: for a group carrying a
     * mass term the raw mixed value is the gram-based product, while a typed instance stores its value in
     * the named unit. Building from templates keeps both readings consistent.
     */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (1.0 of siemensPerMeter) / (0.1 of molesPerLiter)
        val native = (
                0.01 of (seconds pow 3) * (amperes.toUnit() pow 2) /
                        kilo.grams.toUnit() / moles.toUnit()
                ).toMolarConductivity()
        assertIs<KMolarConductivityUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(
            typed into siemensSquareMetersPerMole,
            native into siemensSquareMetersPerMole,
            1e-12,
        )
    }
}
