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
 * All temperature operators: same-type `+`/`-`/`compareTo` on absolute kelvin, and the generic
 * cross-group `*`/`/` fallback to a generic mixed unit (no standardized temperature combination exists).
 */
class KTemperatureOperatorTest {

    /** Add/subtract operate on absolute kelvin; comparison uses the kelvin value. */
    @Test
    fun `arithmetic and comparison`() {
        assertEquals(303.15, ((25 of celsius) + (5 of kelvin)).value, 1e-9)
        assertEquals(293.15, ((25 of celsius) - (5 of kelvin)).value, 1e-9)
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
}
