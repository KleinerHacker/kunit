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

package org.pcsoft.framework.kunit.kinematic.distance

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named second moment of area token carries the correct m⁴ factor. */
class KSecondMomentOfAreaUnitTest {

    @Test
    fun `named tokens in quartic meters`() {
        assertEquals(1.0, (1 of quarticMeters) into quarticMeters, 1e-12)
        assertEquals(1.0e-8, (1 of quarticCentimeters) into quarticMeters, 1e-20)
        assertEquals(1.0e-12, (1 of quarticMillimeters) into quarticMeters, 1e-24)
        assertEquals(4.162314256e-7, (1 of quarticInches) into quarticMeters, 1e-18)
    }

    /** The conversion chain cm⁴ → mm⁴ is 10⁴. */
    @Test
    fun `centimeter to millimeter chain`() {
        assertEquals(10_000.0, (1 of quarticCentimeters) into quarticMillimeters, 1e-6)
    }

    /** The exponent-4 leaf reports exponent 4. */
    @Test
    fun `exponent is four`() {
        assertEquals(4, (1 of quarticMeters).exponent)
        assertEquals(4, (1 of quarticCentimeters).exponent)
    }
}
