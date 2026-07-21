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

package org.pcsoft.framework.kunit.temperature

import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.times
import java.lang.reflect.InvocationTargetException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * All absolute-temperature operators: `AbsTemp − AbsTemp` yields a temperature *difference*,
 * `AbsTemp ± difference` yields an absolute temperature again, `compareTo` on absolute kelvin, and the
 * cross-group `*`/`/` fallback to a generic mixed unit (no standardized temperature combination exists).
 */
class KTemperatureOperatorTest {

    /** Subtracting two absolute temperatures yields the kelvin interval between them, not a temperature. */
    @Test
    fun `absolute minus absolute yields difference`() {
        val diff = (30 of celsius) - (10 of celsius)
        assertEquals(KTemperatureDifference.ofKelvin(20), diff)
        assertEquals(20.0, diff.value, 1e-9)
    }

    /** Adding/subtracting a difference to/from an absolute temperature stays absolute (in kelvin). */
    @Test
    fun `absolute plus and minus difference stays absolute`() {
        assertEquals(303.15, ((25 of celsius) + KTemperatureDifference.ofKelvin(5)).value, 1e-9)
        assertEquals(293.15, ((25 of celsius) - KTemperatureDifference.ofKelvin(5)).value, 1e-9)
    }

    /** Comparison uses the normalized absolute kelvin value. */
    @Test
    fun `comparison`() {
        assertTrue((100 of celsius) > (100 of fahrenheit))
        assertEquals(0, (0 of celsius).compareTo(273.15 of kelvin))
    }

    /** Multiplying/dividing across groups yields a generic mixed unit (no typed temperature result). */
    @Test
    fun `cross-group multiply and divide fall back to mixed unit`() {
        val product = (2 of kelvin) * (3 of bytes)
        assertEquals(6.0, product.value, 1e-9)
        assertEquals(2, product.units.size)

        val quotient = (6 of kelvin) / (3 of bytes)
        assertEquals(2.0, quotient.value, 1e-9)
        assertEquals(2, quotient.units.size)
    }

    /** The symmetric direction (temperature on the right) also falls back to a generic mixed unit. */
    @Test
    fun `cross-group with temperature on the right`() {
        val product = (3 of bytes) * (2 of kelvin)
        assertEquals(6.0, product.value, 1e-9)
        assertEquals(2, product.units.size)

        val quotient = (6 of bytes) / (2 of kelvin)
        assertEquals(3.0, quotient.value, 1e-9)
        assertEquals(2, quotient.units.size)
    }

    /**
     * All four scalar operators are blocked (compile error via `@Deprecated(ERROR)`) for an affine
     * absolute temperature. Invoked here through reflection to prove they throw at runtime too.
     */
    @Test
    fun `scalar operators on absolute temperature are blocked`() {
        val t = 20 of celsius
        val cls = KTemperatureUnitInstance::class.java
        val facade = Class.forName("org.pcsoft.framework.kunit.temperature.KTemperatureUnitInstanceKt")

        val cases = listOf(
            { cls.getMethod("times", Number::class.java).invoke(t, 2) },
            { cls.getMethod("div", Number::class.java).invoke(t, 2) },
            { facade.getMethod("times", Number::class.java, KTemperatureUnitInstance::class.java).invoke(null, 2, t) },
            { facade.getMethod("div", Number::class.java, KTemperatureUnitInstance::class.java).invoke(null, 2, t) },
        )
        cases.forEach { call ->
            val ex = assertFailsWith<InvocationTargetException> { call() }
            assertIs<UnsupportedOperationException>(ex.cause)
        }
    }

    /** A temperature *difference* is linear, so scalar scaling is allowed and stays a difference. */
    @Test
    fun `scalar scaling of a temperature difference is allowed`() {
        val doubled = KTemperatureDifference.ofKelvin(5) * 2
        assertIs<KTemperatureDifferenceUnitInstance>(doubled)
        assertEquals(10.0, doubled.value, 1e-9)
    }
}
