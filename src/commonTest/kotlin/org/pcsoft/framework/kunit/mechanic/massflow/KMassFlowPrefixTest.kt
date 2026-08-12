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

package org.pcsoft.framework.kunit.mechanic.massflow

import org.pcsoft.framework.kunit.*
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every prefixed mass-flow template scales its unit by the prefix factor. */
class KMassFlowPrefixTest {

    @Test
    fun `prefixed kilograms per second`() {
        assertEquals(0.001, (1 of milli.kilogramsPerSecond) into kilogramsPerSecond, 1e-12)
        assertEquals(1000.0, (1 of kilo.kilogramsPerSecond) into kilogramsPerSecond, 1e-6)
    }

    @Test
    fun `prefixed grams per second`() {
        assertEquals(0.001, (1 of milli.gramsPerSecond) into gramsPerSecond, 1e-12)
        assertEquals(1.0e-6, (1 of micro.gramsPerSecond) into gramsPerSecond, 1e-15)
    }

    @Test
    fun `prefixed kilograms per hour`() {
        assertEquals(1000.0, (1 of kilo.kilogramsPerHour) into kilogramsPerHour, 1e-6)
    }

    @Test
    fun `prefixed tonnes per hour`() {
        assertEquals(1000.0, (1 of kilo.tonnesPerHour) into tonnesPerHour, 1e-6)
    }

    @Test
    fun `prefixed pounds per second`() {
        assertEquals(1000.0, (1 of kilo.poundsPerSecond) into poundsPerSecond, 1e-6)
    }

    @Test
    fun `prefixed pounds per hour`() {
        assertEquals(1000.0, (1 of kilo.poundsPerHour) into poundsPerHour, 1e-6)
    }
}
