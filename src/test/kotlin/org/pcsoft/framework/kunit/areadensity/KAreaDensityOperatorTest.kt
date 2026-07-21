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

package org.pcsoft.framework.kunit.areadensity

import org.pcsoft.framework.kunit.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.density.div
import org.pcsoft.framework.kunit.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group area-density operators: `mass / area = area density`, its inverses, and the density·length bridge. */
class KAreaDensityOperatorTest {

    private val area = (5 of meters) * (1 of meters) // 5 m²

    /** `mass / area = area density`. */
    @Test
    fun `mass over area is area density`() {
        val q = (25 of kilo.grams) / area
        assertIs<KAreaDensityUnitInstance>(q)
        assertEquals(5.0, q into (kilo.grams / (meters pow 2)), 1e-9)
    }

    /** `area density * area = mass` and the commutative `area * area density = mass`. */
    @Test
    fun `area density times area is mass`() {
        val q = (25 of kilo.grams) / area // 5 kg/m²
        val m1 = q * area
        val m2 = area * q
        assertIs<KMassUnitInstance>(m1)
        assertIs<KMassUnitInstance>(m2)
        assertEquals(25.0, m1 into kilo.grams, 1e-9)
        assertEquals(25.0, m2 into kilo.grams, 1e-9)
    }

    /** `mass / area density = area`. */
    @Test
    fun `mass over area density is area`() {
        val q = (25 of kilo.grams) / area // 5 kg/m²
        val a: KAreaUnitInstance = (25 of kilo.grams) / q
        assertIs<KAreaUnitInstance>(a)
        assertEquals(5.0, a into ((1 of meters) * (1 of meters)), 1e-9)
    }

    /** `density * length = area density` and `area density / length = density`. */
    @Test
    fun `density length bridge`() {
        val density = (2 of kilo.grams) / (1 of liters) // 2000 kg/m³
        val q = density * (3 of meters)
        assertIs<KAreaDensityUnitInstance>(q)
        assertEquals(6000.0, q into (kilo.grams / (meters pow 2)), 1e-6) // 2000 kg/m³ · 3 m = 6000 kg/m²

        val back: KDensityUnitInstance = q / (3 of meters)
        assertIs<KDensityUnitInstance>(back)
        assertEquals(2000.0, back into (kilo.grams / (meters pow 3)), 1e-6)
    }
}
