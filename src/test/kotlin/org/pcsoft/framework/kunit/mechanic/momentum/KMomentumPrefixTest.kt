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

package org.pcsoft.framework.kunit.mechanic.momentum

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every prefixed momentum template scales its unit by the prefix factor. */
class KMomentumPrefixTest {

    @Test
    fun `prefixed kilogram meters per second`() {
        assertEquals(1000.0, (1 of kilo.kilogramMetersPerSecond) into kilogramMetersPerSecond, 1e-6)
        assertEquals(0.001, (1 of milli.kilogramMetersPerSecond) into kilogramMetersPerSecond, 1e-12)
    }

    @Test
    fun `prefixed newton seconds`() {
        assertEquals(1000.0, (1 of kilo.newtonSeconds) into newtonSeconds, 1e-6)
        assertEquals(0.001, (1 of milli.newtonSeconds) into newtonSeconds, 1e-12)
    }

    @Test
    fun `prefixed gram centimeters per second`() {
        assertEquals(1000.0, (1 of kilo.gramCentimetersPerSecond) into gramCentimetersPerSecond, 1e-6)
    }

    @Test
    fun `prefixed pound feet per second`() {
        assertEquals(1000.0, (1 of kilo.poundFeetPerSecond) into poundFeetPerSecond, 1e-6)
    }
}
