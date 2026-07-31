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

package org.pcsoft.framework.kunit.mechanic.lineforce

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every prefixed force-per-length template scales its unit by the prefix factor. */
class KLineForcePrefixTest {

    @Test
    fun `prefixed newtons per meter`() {
        assertEquals(0.001, (1 of milli.newtonsPerMeter) into newtonsPerMeter, 1e-12)
        assertEquals(1000.0, (1 of kilo.newtonsPerMeter) into newtonsPerMeter, 1e-6)
    }

    @Test
    fun `prefixed newtons per millimeter`() {
        assertEquals(1000.0, (1 of kilo.newtonsPerMillimeter) into newtonsPerMillimeter, 1e-6)
    }

    @Test
    fun `prefixed dynes per centimeter`() {
        assertEquals(0.001, (1 of milli.dynesPerCentimeter) into dynesPerCentimeter, 1e-12)
    }

    @Test
    fun `prefixed pounds force per inch`() {
        assertEquals(1000.0, (1 of kilo.poundsForcePerInch) into poundsForcePerInch, 1e-6)
    }

    @Test
    fun `prefixed kiloponds per meter`() {
        assertEquals(1000.0, (1 of kilo.kilopondsPerMeter) into kilopondsPerMeter, 1e-6)
    }
}
