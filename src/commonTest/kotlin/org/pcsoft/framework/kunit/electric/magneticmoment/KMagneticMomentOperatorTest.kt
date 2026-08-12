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

package org.pcsoft.framework.kunit.electric.magneticmoment

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnitInstance
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group magnetic moment operators and the typed/native decomposition equivalence. */
class KMagneticMomentOperatorTest {

    private val loop: KAreaUnitInstance = (0.1 of meters) * (0.05 of meters) // 0.005 m²

    /** `current * area = magnetic moment` and its commutative counterpart. */
    @Test
    fun `current times area is magnetic moment`() {
        val i = 2 of amperes
        val m1 = i * loop
        val m2 = loop * i
        assertIs<KMagneticMomentUnitInstance>(m1)
        assertIs<KMagneticMomentUnitInstance>(m2)
        assertEquals(0.01, m1 into ampereSquareMeters, 1e-12)
        assertEquals(0.01, m2 into ampereSquareMeters, 1e-12)
    }

    /** `magnetic moment / area = current`. */
    @Test
    fun `magnetic moment over area is current`() {
        val i = (0.01 of ampereSquareMeters) / loop
        assertIs<KElectricCurrentUnitInstance>(i)
        assertEquals(2.0, i into amperes, 1e-9)
    }

    /** `magnetic moment / current = area`. */
    @Test
    fun `magnetic moment over current is area`() {
        val a = (0.01 of ampereSquareMeters) / (2 of amperes)
        assertIs<KAreaUnitInstance>(a)
        assertEquals(0.005, a into ((1 of meters) * (1 of meters)), 1e-12)
    }

    /** The typed operator and the native `A·m²` expression yield the same typed, value-equal result. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (2 of amperes) * loop
        val native = ((2 of amperes).toUnit() * loop.toUnit()).toMagneticMoment()
        assertIs<KMagneticMomentUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(typed into ampereSquareMeters, native into ampereSquareMeters, 1e-15)
    }
}
