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

package org.pcsoft.framework.kunit.optic.luminousexposure

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named luminous exposure token carries the correct lx·s factor. */
class KLuminousExposureUnitTest {

    @Test
    fun `named tokens in lux seconds`() {
        assertEquals(1.0, (1 of luxSeconds) into luxSeconds, 1e-12)
        assertEquals(3600.0, (1 of luxHours) into luxSeconds, 1e-9)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("lx*s", KLuminousExposureUnit.LUX_SECOND.symbol)
        assertEquals(1.0, KLuminousExposureUnit.LUX_SECOND.baseValue, 1e-12)
        assertEquals("lx*h", KLuminousExposureUnit.LUX_HOUR.symbol)
        assertEquals(3600.0, KLuminousExposureUnit.LUX_HOUR.baseValue, 1e-9)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KLuminousExposureUnit.LUX_SECOND, KLuminousExposureUnit.BASE)
        assertEquals(1.0, KLuminousExposureUnit.BASE.baseValue, 1e-12)
        assertEquals("lx*s", KLuminousExposureUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(2, KLuminousExposureUnit.entries.size)
        assertEquals(listOf("lx*s", "lx*h"), KLuminousExposureUnit.entries.map { it.symbol })
    }
}
