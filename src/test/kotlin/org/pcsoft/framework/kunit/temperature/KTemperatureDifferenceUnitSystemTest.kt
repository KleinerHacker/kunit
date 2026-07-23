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

import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

/**
 * `KTemperatureDifferenceUnitInstance` surface: explicit construction, the internal kelvin storage,
 * `equals`/`hashCode`/`toString`, `pow`, and generic-engine interop (`toTemperatureDifference`).
 */
class KTemperatureDifferenceUnitSystemTest {

    /** `ofKelvin` stores the plain (linear) kelvin interval. */
    @Test
    fun `explicit construction stores kelvin`() {
        assertEquals(20.0, KTemperatureDifference.ofKelvin(20).value, 1e-9)
        assertEquals(-5.0, KTemperatureDifference.ofKelvin(-5).value, 1e-9)
    }

    /** Equality/hash by normalized kelvin value. */
    @Test
    fun `equals and hashCode`() {
        assertEquals(KTemperatureDifference.ofKelvin(20), KTemperatureDifference.ofKelvin(20))
        assertEquals(KTemperatureDifference.ofKelvin(20).hashCode(), KTemperatureDifference.ofKelvin(20).hashCode())
        assertNotEquals(KTemperatureDifference.ofKelvin(20), KTemperatureDifference.ofKelvin(21))
        assertFalse(KTemperatureDifference.ofKelvin(20).equals(20.0)) // not a difference instance
    }

    /** `toString` prints the base-unit representation with the distinct "ΔK" symbol. */
    @Test
    fun `toString is kelvin representation`() {
        assertEquals("20.0 ΔK", KTemperatureDifference.ofKelvin(20).toString())
    }

    /** `pow` runs linearly through the generic engine on the kelvin term. */
    @Test
    fun `pow through generic engine`() {
        val squared = KTemperatureDifference.ofKelvin(2) pow 2
        assertEquals(4.0, squared.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KTemperatureDifferenceUnit.KELVIN, 2)), squared.units)
    }

    /** A single difference term is normalized back to a pure difference on `toTemperatureDifference`. */
    @Test
    fun `toTemperatureDifference normalizes`() {
        assertEquals(5.0, KTemperatureDifference.ofKelvin(5).toUnit().toTemperatureDifference().value, 1e-9)
    }

    /** A mixed unit that is not a single difference term cannot be converted to a difference value. */
    @Test
    fun `toTemperatureDifference on non-difference fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toTemperatureDifference() }
        assertFailsWith<IllegalStateException> {
            (KTemperatureDifference.ofKelvin(1).toUnit() / KTemperatureDifference.ofKelvin(1).toUnit())
                .toTemperatureDifference()
        }
    }

    /** `format` into a kelvin-difference template. */
    @Test
    fun `format compositions`() {
        assertEquals("20.0 ΔK", KTemperatureDifference.ofKelvin(20) format KTemperatureDifference.ofKelvin(1))
    }
}
