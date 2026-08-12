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

package org.pcsoft.framework.kunit.mechanic.angularmomentum

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every prefixed angular-momentum template scales its unit by the prefix factor. */
class KAngularMomentumPrefixTest {

    @Test
    fun `prefixed kilogram meters squared per second`() {
        assertEquals(
            0.001,
            (1 of milli.kilogramMetersSquaredPerSecond) into kilogramMetersSquaredPerSecond,
            1e-12,
        )
    }

    @Test
    fun `prefixed newton meter seconds`() {
        assertEquals(1000.0, (1 of kilo.newtonMeterSeconds) into newtonMeterSeconds, 1e-6)
    }

    @Test
    fun `prefixed joule seconds`() {
        assertEquals(0.001, (1 of milli.jouleSeconds) into jouleSeconds, 1e-12)
    }

    @Test
    fun `prefixed gram centimeters squared per second`() {
        assertEquals(
            1000.0,
            (1 of kilo.gramCentimetersSquaredPerSecond) into gramCentimetersSquaredPerSecond,
            1e-6,
        )
    }
}
