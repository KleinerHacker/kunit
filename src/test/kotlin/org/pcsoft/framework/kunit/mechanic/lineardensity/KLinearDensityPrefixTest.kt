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

package org.pcsoft.framework.kunit.mechanic.lineardensity

import org.pcsoft.framework.kunit.*
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every prefixed linear-density template scales its unit by the prefix factor. */
class KLinearDensityPrefixTest {

    @Test
    fun `prefixed kilograms per meter`() {
        assertEquals(0.001, (1 of milli.kilogramsPerMeter) into kilogramsPerMeter, 1e-12)
        assertEquals(1000.0, (1 of kilo.kilogramsPerMeter) into kilogramsPerMeter, 1e-6)
    }

    @Test
    fun `prefixed grams per meter`() {
        assertEquals(0.001, (1 of milli.gramsPerMeter) into gramsPerMeter, 1e-12)
    }

    @Test
    fun `prefixed grams per centimeter`() {
        assertEquals(0.001, (1 of milli.gramsPerCentimeter) into gramsPerCentimeter, 1e-12)
    }

    @Test
    fun `prefixed tex`() {
        assertEquals(0.1, (1 of deci.tex) into tex, 1e-12)
        assertEquals(1000.0, (1 of kilo.tex) into tex, 1e-6)
    }

    @Test
    fun `prefixed denier`() {
        assertEquals(0.1, (1 of deci.denier) into denier, 1e-12)
    }

    @Test
    fun `prefixed pounds per foot`() {
        assertEquals(1000.0, (1 of kilo.poundsPerFoot) into poundsPerFoot, 1e-6)
    }
}
