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

package org.pcsoft.framework.kunit.distance

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * The core of the dimensioned-subtype refactor: `*`/`/` stay in the length family with statically known
 * result types. The `val x: T = ...` bindings are the actual guarantee - a resolution regression would
 * be a **compile error** here, not a silent `KMixedUnitInstance`.
 */
class KDistanceTypeTransitionTest {

    private val l = 2.meters          // KLengthUnitInstance
    private val a = 3.squareMeters    // KAreaUnitInstance
    private val v = 5.cubicMeters     // KVolumeUnitInstance

    // region times matrix — statically typed results

    @Test
    fun `times result types`() {
        val ll: KAreaUnitInstance = l * l
        assertEquals(4.0, ll.value, 1e-9); assertEquals(2, ll.exponent)

        val lA: KVolumeUnitInstance = l * a
        assertEquals(6.0, lA.value, 1e-9); assertEquals(3, lA.exponent)

        val lV: KDistanceUnitInstance = l * v
        assertEquals(10.0, lV.value, 1e-9); assertEquals(4, lV.exponent)

        val aL: KVolumeUnitInstance = a * l
        assertEquals(6.0, aL.value, 1e-9); assertEquals(3, aL.exponent)

        val aA: KDistanceUnitInstance = a * a
        assertEquals(9.0, aA.value, 1e-9); assertEquals(4, aA.exponent)

        val aV: KDistanceUnitInstance = a * v
        assertEquals(15.0, aV.value, 1e-9); assertEquals(5, aV.exponent)

        val vL: KDistanceUnitInstance = v * l
        assertEquals(10.0, vL.value, 1e-9); assertEquals(4, vL.exponent)

        val vA: KDistanceUnitInstance = v * a
        assertEquals(15.0, vA.value, 1e-9); assertEquals(5, vA.exponent)

        val vV: KDistanceUnitInstance = v * v
        assertEquals(25.0, vV.value, 1e-9); assertEquals(6, vV.exponent)
    }

    // endregion

    // region div matrix — statically typed results

    @Test
    fun `div result types`() {
        val ll: KMixedUnitInstance = l / l
        assertTrue(ll.units.isEmpty()); assertEquals(1.0, ll.value, 1e-9)

        val lA: KDistanceUnitInstance = l / a
        assertEquals(-1, lA.exponent)

        val lV: KDistanceUnitInstance = l / v
        assertEquals(-2, lV.exponent)

        val aL: KLengthUnitInstance = a / l
        assertEquals(1.5, aL.value, 1e-9); assertEquals(1, aL.exponent)

        val aA: KMixedUnitInstance = a / a
        assertTrue(aA.units.isEmpty())

        val aV: KDistanceUnitInstance = a / v
        assertEquals(-1, aV.exponent)

        val vL: KAreaUnitInstance = v / l
        assertEquals(2.5, vL.value, 1e-9); assertEquals(2, vL.exponent)

        val vA: KLengthUnitInstance = v / a
        assertEquals(5.0 / 3.0, vA.value, 1e-9); assertEquals(1, vA.exponent)

        val vV: KMixedUnitInstance = v / v
        assertTrue(vV.units.isEmpty())
    }

    // endregion

    // region fallbacks and mixed operands

    @Test
    fun `general distance operand falls back to mixed`() {
        val general: KDistanceUnitInstance = a * a // exponent 4, general type
        val result = l * general
        assertEquals(listOf(KUnitTerm(KDistanceUnit.BASE, 5)), result.units)
    }

    @Test
    fun `leaf times raw mixed unit stays mixed`() {
        val mixed = KMixedUnitInstance(2.0, listOf(KUnitTerm(KDistanceUnit.BASE, 1)))
        val result = l * mixed
        assertEquals(listOf(KUnitTerm(KDistanceUnit.BASE, 2)), result.units)
    }

    // endregion

    // region equality across dimensions

    @Test
    fun `equals across dimensions is false, not an error`() {
        assertFalse(5.meters.equals(5.squareMeters))
        assertFalse(5.squareMeters.equals(5.cubicMeters))
        assertTrue(5.meters == 500.centi(meters))
    }

    // endregion
}
