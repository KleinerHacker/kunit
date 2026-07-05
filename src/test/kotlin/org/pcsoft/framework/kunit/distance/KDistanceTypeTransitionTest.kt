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
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * The core of the dimensioned-subtype refactor: `*`/`/` stay in the length family with statically known
 * result types. The `val x: T = ...` bindings are the actual guarantee - a resolution regression would
 * be a **compile error** here, not a silent `KMixedUnitInstance`.
 */
class KDistanceTypeTransitionTest {

    private val l = 2.meters                     // KLengthUnitInstance
    private val a = 3.meters * 1.meters          // KAreaUnitInstance (3 m²)
    private val v = 5.meters * 1.meters * 1.meters // KVolumeUnitInstance (5 m³)

    // region times matrix — statically typed results

    /** Every `length/area/volume * length/area/volume` product resolves to the statically correct type (area, volume, or the general base) with the right value and summed exponent. */
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

    /** Every `length/area/volume / length/area/volume` quotient resolves to the statically correct type (length, area, dimensionless mixed, or the general base) with the right value and subtracted exponent. */
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

    /** Multiplying a leaf by a general [KDistanceUnitInstance] operand (exponent outside {1,2,3}) falls back to a raw mixed unit with the summed exponent. */
    @Test
    fun `general distance operand falls back to mixed`() {
        val general: KDistanceUnitInstance = a * a // exponent 4, general type
        val result = l * general
        assertEquals(listOf(KUnitTerm(KDistanceUnit.BASE, 5)), result.units)
    }

    /** Multiplying a leaf by a raw [KMixedUnitInstance] operand stays a mixed unit (the typed overloads only apply between two distance wrappers). */
    @Test
    fun `leaf times raw mixed unit stays mixed`() {
        val mixed = KMixedUnitInstance(2.0, listOf(KUnitTerm(KDistanceUnit.BASE, 1)))
        val result = l * mixed
        assertEquals(listOf(KUnitTerm(KDistanceUnit.BASE, 2)), result.units)
    }

    // endregion

    // region equality across dimensions

    /** Comparing values of different dimensions with `equals` returns `false` (never throws), while same-dimension values compare by normalized magnitude. */
    @Test
    fun `equals across dimensions is false, not an error`() {
        assertFalse(5.meters.equals(5.meters * 1.meters))
        assertFalse((5.meters * 1.meters).equals(5.meters * 1.meters * 1.meters))
        assertTrue(5.meters == 500.centi(meters))
    }

    // endregion

    // region in-hierarchy narrowing (KDistanceUnitInstance.toLength/toArea/toVolume)

    /** Narrowing each leaf to its own dimension via `toLength`/`toArea`/`toVolume` returns the same value at the same exponent. */
    @Test
    fun `narrowing to the matching dimension returns the value`() {
        assertEquals(2.0, l.toLength().value, 1e-9)
        assertEquals(3.0, a.toArea().value, 1e-9)
        assertEquals(5.0, v.toVolume().value, 1e-9)
    }

    /** Narrowing to a dimension whose exponent does not match (e.g. `length.toArea()`) throws `IllegalStateException`. */
    @Test
    fun `narrowing to the wrong dimension throws`() {
        assertFailsWith<IllegalStateException> { l.toArea() }
        assertFailsWith<IllegalStateException> { a.toVolume() }
        assertFailsWith<IllegalStateException> { v.toLength() }
    }

    // endregion
}
