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

package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.times
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** All mass operators: `+`/`-` (normalized to grams), comparison, and the generic `*`/`/` fallback. */
class KMassOperatorTest {

    /** Add/subtract normalize to grams; comparison uses the gram value. */
    @Test
    fun `arithmetic and comparison`() {
        assertEquals(1500.0, ((1 of kilo.grams) + (500 of grams)).value, 1e-9)
        assertEquals(500.0, ((1 of kilo.grams) - (500 of grams)).value, 1e-9)
        assertTrue((1 of kilo.grams) > (500 of grams))
        assertTrue((500 of grams) < (1 of kilo.grams))
        assertTrue((1 of kilo.grams).compareTo(1000 of grams) == 0)
    }

    /** A non-meaningful cross-group combination falls back to a generic mixed unit. */
    @Test
    fun `generic times and div`() {
        val product = (2 of grams) * (3 of seconds)
        assertEquals(6.0, product.value, 1e-9)
        assertEquals(2, product.units.size)

        val quotient = (6 of grams) / (2 of seconds)
        assertEquals(3.0, quotient.value, 1e-9)
        assertEquals(2, quotient.units.size)
    }
}
