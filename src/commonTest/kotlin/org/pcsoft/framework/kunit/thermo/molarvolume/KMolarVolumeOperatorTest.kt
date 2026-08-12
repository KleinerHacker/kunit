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

package org.pcsoft.framework.kunit.thermo.molarvolume

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.KVolumeUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.thermo.amountofsubstance.KAmountOfSubstanceUnitInstance
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.KMolarMassUnitInstance
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group molar volume operators and the equivalence of all decompositions. */
class KMolarVolumeOperatorTest {

    /** `volume / amountOfSubstance = molar volume`. */
    @Test
    fun `volume over amount of substance is molar volume`() {
        val v = (44.8 of liters) / (2 of moles)
        assertIs<KMolarVolumeUnitInstance>(v)
        assertEquals(22.4, v into litersPerMole, 1e-9)
    }

    /** `molarMass / density = molar volume` - the second decomposition. */
    @Test
    fun `molar mass over density is molar volume`() {
        val v = (18.015 of gramsPerMole) / ((1 of kilo.grams) / (1 of liters))
        assertIs<KMolarVolumeUnitInstance>(v)
        assertEquals(18.015, v into cubicCentimetersPerMole, 1e-9)
    }

    /** `molar volume * amountOfSubstance = volume` and its commutative counterpart. */
    @Test
    fun `molar volume times amount of substance is volume`() {
        val v = 22.4 of litersPerMole
        val n = 2 of moles
        val v1 = v * n
        val v2 = n * v
        assertIs<KVolumeUnitInstance>(v1)
        assertIs<KVolumeUnitInstance>(v2)
        assertEquals(44.8, v1 into liters, 1e-9)
        assertEquals(44.8, v2 into liters, 1e-9)
    }

    /** `volume / molar volume = amountOfSubstance`. */
    @Test
    fun `volume over molar volume is amount of substance`() {
        val n = (44.8 of liters) / (22.4 of litersPerMole)
        assertIs<KAmountOfSubstanceUnitInstance>(n)
        assertEquals(2.0, n into moles, 1e-9)
    }

    /** `molar volume * density = molar mass` and its commutative counterpart. */
    @Test
    fun `molar volume times density is molar mass`() {
        val v = 18.015 of cubicCentimetersPerMole
        val d = (1 of kilo.grams) / (1 of liters)
        val m1 = v * d
        val m2 = d * v
        assertIs<KMolarMassUnitInstance>(m1)
        assertIs<KMolarMassUnitInstance>(m2)
        assertEquals(18.015, m1 into gramsPerMole, 1e-9)
        assertEquals(18.015, m2 into gramsPerMole, 1e-9)
    }

    /** `molar mass / molar volume = density`. */
    @Test
    fun `molar mass over molar volume is density`() {
        val d = (18.015 of gramsPerMole) / (18.015 of cubicCentimetersPerMole)
        assertIs<KDensityUnitInstance>(d)
        assertEquals(1000.0, d into ((1 of kilo.grams) / (1 of (meters pow 3))), 1e-6)
    }

    /**
     * All three decompositions - the typed `volume / amountOfSubstance` operator, the typed
     * `molarMass / density` operator and the native `distance³·substance⁻¹` expression through
     * `toMolarVolume()` - yield the same typed, value-equal result.
     */
    @Test
    fun `all decompositions agree`() {
        val typedVolume = (0.018015 of liters) / (1 of moles)
        val typedMolarMass = (18.015 of gramsPerMole) / ((1 of kilo.grams) / (1 of liters))
        val native = (((18.015e-6 of (meters pow 3)).toUnit()) / (1 of moles).toUnit()).toMolarVolume()
        assertIs<KMolarVolumeUnitInstance>(typedVolume)
        assertIs<KMolarVolumeUnitInstance>(typedMolarMass)
        assertIs<KMolarVolumeUnitInstance>(native)
        assertEquals(typedVolume, typedMolarMass)
        assertEquals(typedVolume, native)
        assertEquals(18.015, typedVolume into cubicCentimetersPerMole, 1e-9)
    }

    /** `mass / volume` sanity check tying the group to the density group. */
    @Test
    fun `density round trip`() {
        val d = (1 of kilo.grams) / (1 of liters)
        assertEquals(1.0e6, d.value, 1e-3)
    }
}
