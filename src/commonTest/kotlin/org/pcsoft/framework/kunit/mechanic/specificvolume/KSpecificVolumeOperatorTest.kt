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

package org.pcsoft.framework.kunit.mechanic.specificvolume

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.KVolumeUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed specific-volume operators: `volume / mass`, its inverses and the reciprocal density bridge. */
class KSpecificVolumeOperatorTest {

    /** `volume / mass = specificvolume`. */
    @Test
    fun `volume over mass is specific volume`() {
        val v = (2 of liters) / (1 of kilo.grams)
        assertIs<KSpecificVolumeUnitInstance>(v)
        assertEquals(2.0, v into litersPerKilogram, 1e-9)
    }

    /** `specificvolume * mass = volume`, plus its commutative form. */
    @Test
    fun `specific volume times mass is volume`() {
        val sv = 2 of litersPerKilogram
        val v1: KVolumeUnitInstance = sv * (3 of kilo.grams)
        val v2: KVolumeUnitInstance = (3 of kilo.grams) * sv
        assertIs<KVolumeUnitInstance>(v1)
        assertIs<KVolumeUnitInstance>(v2)
        assertEquals(6.0, v1 into liters, 1e-9)
        assertEquals(6.0, v2 into liters, 1e-9)
    }

    /** `volume / specificvolume = mass`. */
    @Test
    fun `volume over specific volume is mass`() {
        val m: KMassUnitInstance = (6 of liters) / (2 of litersPerKilogram)
        assertIs<KMassUnitInstance>(m)
        assertEquals(3.0, m into kilo.grams, 1e-9)
    }

    /** `1 / density = specificvolume` and `1 / specificvolume = density` stay typed and are exact inverses. */
    @Test
    fun `reciprocal bridge to the density group`() {
        val water: KDensityUnitInstance = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
        val v = 1 / water
        assertIs<KSpecificVolumeUnitInstance>(v)
        assertEquals(1.0, v into litersPerKilogram, 1e-9)

        val rho = 1 / v
        assertIs<KDensityUnitInstance>(rho)
        assertEquals(water.value, rho.value, 1e-6)
    }
}
