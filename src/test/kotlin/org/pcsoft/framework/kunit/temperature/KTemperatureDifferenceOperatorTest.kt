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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * All temperature difference operators: same-type `+`/`-`/`compareTo` (linear), and the generic
 * cross-group `*`/`/` fallback to a generic mixed unit.
 */
class KTemperatureDifferenceOperatorTest {

    /** Differences add and subtract linearly, staying differences. */
    @Test
    fun `add and subtract`() {
        assertEquals(KTemperatureDifference.ofKelvin(30), KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10))
        assertEquals(KTemperatureDifference.ofKelvin(10), KTemperatureDifference.ofKelvin(20) - KTemperatureDifference.ofKelvin(10))
    }

    /** Comparison uses the normalized kelvin value. */
    @Test
    fun `comparison`() {
        assertTrue(KTemperatureDifference.ofKelvin(20) > KTemperatureDifference.ofKelvin(10))
        assertEquals(0, KTemperatureDifference.ofKelvin(5).compareTo(KTemperatureDifference.ofKelvin(5)))
    }

    /** Multiplying/dividing across groups yields a generic mixed unit. */
    @Test
    fun `cross-group multiply and divide fall back to mixed unit`() {
        val product = KTemperatureDifference.ofKelvin(2) * (3 of bytes)
        assertEquals(6.0, product.value, 1e-9)
        assertEquals(2, product.units.size)

        val quotient = KTemperatureDifference.ofKelvin(6) / (3 of bytes)
        assertEquals(2.0, quotient.value, 1e-9)
        assertEquals(2, quotient.units.size)
    }
}
