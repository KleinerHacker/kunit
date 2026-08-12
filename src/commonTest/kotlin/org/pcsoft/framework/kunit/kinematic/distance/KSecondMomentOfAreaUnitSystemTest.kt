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

package org.pcsoft.framework.kunit.kinematic.distance

import org.pcsoft.framework.kunit.*
import kotlin.test.*

/**
 * `KSecondMomentOfAreaUnitInstance` surface: the typed operators of the exponent-4 leaf, its additive
 * restriction to its own dimension, and `toSecondMomentOfArea`.
 */
class KSecondMomentOfAreaUnitSystemTest {

    private val squareMeter: KAreaUnitInstance = (1 of meters) * (1 of meters)
    private val cubicMeter: KVolumeUnitInstance = squareMeter * (1 of meters)

    /** `area * area`, `volume * length` and `length * volume` all produce the typed exponent-4 leaf. */
    @Test
    fun `typed products yield the exponent four leaf`() {
        val viaArea = squareMeter * squareMeter
        val viaVolume = cubicMeter * (1 of meters)
        val viaLength = (1 of meters) * cubicMeter
        assertIs<KSecondMomentOfAreaUnitInstance>(viaArea)
        assertIs<KSecondMomentOfAreaUnitInstance>(viaVolume)
        assertIs<KSecondMomentOfAreaUnitInstance>(viaLength)
        assertEquals(1.0, viaArea into quarticMeters, 1e-12)
        assertEquals(viaArea into quarticMeters, viaVolume into quarticMeters, 1e-12)
        assertEquals(viaArea into quarticMeters, viaLength into quarticMeters, 1e-12)
    }

    /** A 1 cm × 1 cm square gives exactly 1 cm⁴. */
    @Test
    fun `centimeter square`() {
        val side = 1 of centi.meters
        val i = (side * side) * (side * side)
        assertIs<KSecondMomentOfAreaUnitInstance>(i)
        assertEquals(1.0, i into quarticCentimeters, 1e-9)
    }

    /** Same-type addition: the parts of a built-up section combine, a hollow section subtracts. */
    @Test
    fun `same-type operators`() {
        val a = 1940 of quarticCentimeters
        val b = 60 of quarticCentimeters
        assertEquals(2000.0, (a + b) into quarticCentimeters, 1e-9)
        assertEquals(1880.0, (a - b) into quarticCentimeters, 1e-9)
        assertTrue(a > b)
        assertEquals(0, (a).compareTo(1940 of quarticCentimeters))
        assertIs<KMixedUnitInstance>(a / b)
    }

    /** Division narrows back to the lower leaves; multiplication escapes to the general type. */
    @Test
    fun `typed quotients and products`() {
        val i = 1 of quarticMeters
        assertIs<KVolumeUnitInstance>(i / (1 of meters))
        assertIs<KAreaUnitInstance>(i / squareMeter)
        assertIs<KLengthUnitInstance>(i / cubicMeter)
        assertEquals(5, (i * (1 of meters)).exponent)
        assertEquals(6, (i * squareMeter).exponent)
    }

    /** A single distance term at exponent 4 narrows; other exponents fail. */
    @Test
    fun `toSecondMomentOfArea round-trip and failure`() {
        val raw = (1 of meters).toUnit() pow 4
        assertEquals(1.0, raw.toSecondMomentOfArea() into quarticMeters, 1e-9)

        // An equivalent expression in centimeters reduces onto the same normal form.
        val rawCm = (1 of centi.meters).toUnit() pow 4
        assertEquals(1.0, rawCm.toSecondMomentOfArea() into quarticCentimeters, 1e-9)

        assertFailsWith<IllegalStateException> { ((1 of meters).toUnit() pow 3).toSecondMomentOfArea() }
        assertFailsWith<IllegalStateException> { ((1 of meters).toUnit() pow 5).toSecondMomentOfArea() }
    }

    /** `toDistance` reports the exponent-4 leaf as the concrete runtime type. */
    @Test
    fun `toDistance yields the leaf type`() {
        val d = ((1 of meters).toUnit() pow 4).toDistance()
        assertIs<KSecondMomentOfAreaUnitInstance>(d)
        assertEquals(4, d.exponent)
    }
}
