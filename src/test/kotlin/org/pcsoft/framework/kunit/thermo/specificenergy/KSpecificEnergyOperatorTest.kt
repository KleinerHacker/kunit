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

package org.pcsoft.framework.kunit.thermo.specificenergy

import org.pcsoft.framework.kunit.common.energy.KEnergyUnitInstance
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group specific energy operators and the equivalence of all decompositions. */
class KSpecificEnergyOperatorTest {

    /** `energy / mass = specific energy`. */
    @Test
    fun `energy over mass is specific energy`() {
        val h = (334 of kilo.joules) / (1 of kilo.grams)
        assertIs<KSpecificEnergyUnitInstance>(h)
        assertEquals(334000.0, h into joulesPerKilogram, 1e-6)
    }

    /** `specific energy * mass = energy` and its commutative counterpart. */
    @Test
    fun `specific energy times mass is energy`() {
        val h = 334 of kilo.joulesPerKilogram
        val m = 2 of kilo.grams
        val e1 = h * m
        val e2 = m * h
        assertIs<KEnergyUnitInstance>(e1)
        assertIs<KEnergyUnitInstance>(e2)
        assertEquals(668000.0, e1 into joules, 1e-6)
        assertEquals(668000.0, e2 into joules, 1e-6)
    }

    /** `energy / specific energy = mass`. */
    @Test
    fun `energy over specific energy is mass`() {
        val m = (668 of kilo.joules) / (334 of kilo.joulesPerKilogram)
        assertIs<KMassUnitInstance>(m)
        assertEquals(2000.0, m into grams, 1e-6)
    }

    /**
     * Both decompositions - the typed `energy / mass` operator and the native `distance²·time⁻²`
     * expression through `toSpecificEnergy()` - yield the same typed, value-equal result.
     */
    @Test
    fun `all decompositions agree`() {
        val typed = (1 of joules) / (1 of kilo.grams)
        val native = (((1 of meters).toUnit() pow 2) / ((1 of seconds).toUnit() pow 2)).toSpecificEnergy()
        assertIs<KSpecificEnergyUnitInstance>(typed)
        assertIs<KSpecificEnergyUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(1.0, typed into joulesPerKilogram, 1e-9)
    }
}
