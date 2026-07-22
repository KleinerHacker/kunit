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

package org.pcsoft.framework.kunit.ec

import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.times
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** All electric current operators: `+`/`-` (normalized to amperes), comparison, and the generic `*`/`/` fallback. */
class KElectricCurrentOperatorTest {

    /** Add/subtract normalize to amperes; comparison uses the ampere value. */
    @Test
    fun `arithmetic and comparison`() {
        assertEquals(11.0, ((1 of amperes) + (1 of biot)).value, 1e-9)
        assertEquals(9.0, ((1 of biot) - (1 of amperes)).value, 1e-9)
        assertTrue((1 of biot) > (1 of amperes))
        assertTrue((1 of amperes) < (1 of biot))
        assertTrue((1 of biot).compareTo(10 of amperes) == 0)
    }

    /** A non-meaningful cross-group combination falls back to a generic mixed unit. */
    @Test
    fun `generic times and div`() {
        val product = (2 of amperes) * (3 of seconds)
        assertEquals(6.0, product.value, 1e-9)
        assertEquals(2, product.units.size)

        val quotient = (6 of amperes) / (2 of seconds)
        assertEquals(3.0, quotient.value, 1e-9)
        assertEquals(2, quotient.units.size)
    }
}
