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

package org.pcsoft.framework.kunit.mechanic.specificvolume

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every prefixed specific-volume template scales its unit by the prefix factor. */
class KSpecificVolumePrefixTest {

    @Test
    fun `prefixed cubic meters per kilogram`() {
        assertEquals(0.001, (1 of milli.cubicMetersPerKilogram) into cubicMetersPerKilogram, 1e-12)
        assertEquals(1000.0, (1 of kilo.cubicMetersPerKilogram) into cubicMetersPerKilogram, 1e-6)
    }

    @Test
    fun `prefixed liters per kilogram`() {
        assertEquals(0.001, (1 of milli.litersPerKilogram) into litersPerKilogram, 1e-12)
    }

    @Test
    fun `prefixed cubic centimeters per gram`() {
        assertEquals(0.001, (1 of milli.cubicCentimetersPerGram) into cubicCentimetersPerGram, 1e-12)
    }

    @Test
    fun `prefixed cubic feet per pound`() {
        assertEquals(0.001, (1 of milli.cubicFeetPerPound) into cubicFeetPerPound, 1e-12)
    }
}
