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

package org.pcsoft.framework.kunit.mechanic.solidangle

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.KAngleUnitInstance
import org.pcsoft.framework.kunit.mechanic.angle.degrees
import org.pcsoft.framework.kunit.mechanic.angle.radians
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group solid-angle operators: `angle * angle = solidangle` and its inverse. */
class KSolidAngleOperatorTest {

    /** `angle * angle = solidangle`; the typed operator wins over the generic mixed-unit one. */
    @Test
    fun `angle times angle is solid angle`() {
        val omega = (2 of radians) * (3 of radians)
        assertIs<KSolidAngleUnitInstance>(omega)
        assertEquals(6.0, omega into steradians, 1e-12)
    }

    /** The typed decomposition and the native `rad²` form yield the same value. */
    @Test
    fun `both decompositions agree`() {
        val typed = (90 of degrees) * (90 of degrees)
        val native = ((90 of degrees).toUnit() pow 2).toSolidAngle()
        assertEquals(typed into steradians, native into steradians, 1e-12)
        assertEquals(typed, native)
    }

    /** `solidangle / angle = angle`. */
    @Test
    fun `solid angle over angle is angle`() {
        val a: KAngleUnitInstance = (6 of steradians) / (3 of radians)
        assertIs<KAngleUnitInstance>(a)
        assertEquals(2.0, a into radians, 1e-12)
    }
}
