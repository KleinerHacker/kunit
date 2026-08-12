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

package org.pcsoft.framework.kunit.electric.flux

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.fieldstrength.KElectricFieldStrengthUnitInstance
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group electric flux operators and the typed/native decomposition equivalence. */
class KElectricFluxOperatorTest {

    /** 0.125 m² - binary-exact, so the typed and native results compare bit-for-bit. */
    private val plate: KAreaUnitInstance = (0.5 of meters) * (0.25 of meters)

    /** `electricFieldStrength * area = electric flux` and its commutative counterpart. */
    @Test
    fun `field strength times area is electric flux`() {
        val e = 1000 of voltsPerMeter
        val phi1 = e * plate
        val phi2 = plate * e
        assertIs<KElectricFluxUnitInstance>(phi1)
        assertIs<KElectricFluxUnitInstance>(phi2)
        assertEquals(125.0, phi1 into voltMeters, 1e-12)
        assertEquals(125.0, phi2 into voltMeters, 1e-12)
    }

    /** `electric flux / area = electricFieldStrength`. */
    @Test
    fun `electric flux over area is field strength`() {
        val e = (125 of voltMeters) / plate
        assertIs<KElectricFieldStrengthUnitInstance>(e)
        assertEquals(1000.0, e into voltsPerMeter, 1e-9)
    }

    /** `electric flux / electricFieldStrength = area`. */
    @Test
    fun `electric flux over field strength is area`() {
        val a = (125 of voltMeters) / (1000 of voltsPerMeter)
        assertIs<KAreaUnitInstance>(a)
        assertEquals(0.125, a into ((1 of meters) * (1 of meters)), 1e-12)
    }

    /**
     * The typed operator and the native `kg·m³·s⁻³·A⁻¹` expression yield the same typed, value-equal
     * result.
     *
     * The native form is assembled from *unit templates*, not from typed instances: for a group carrying a
     * mass term the raw mixed value is the gram-based product, while a typed instance stores its value in
     * the named unit.
     */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (1000 of voltsPerMeter) * plate
        val native = (
                125 of kilo.grams.toUnit() * (meters pow 3) / (seconds pow 3) / amperes.toUnit()
                ).toElectricFlux()
        assertIs<KElectricFluxUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(typed into voltMeters, native into voltMeters, 1e-9)
    }
}
