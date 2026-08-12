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

package org.pcsoft.framework.kunit.electric.elastance

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Every named elastance token carries the correct F⁻¹ factor. */
class KElastanceUnitTest {

    @Test
    fun `named tokens in reciprocal farads`() {
        assertEquals(1.0, (1 of reciprocalFarads) into reciprocalFarads, 1e-12)
        assertEquals(1.0, (1 of darafs) into reciprocalFarads, 1e-12)
    }

    /** The daraf is the classical name for the reciprocal farad. */
    @Test
    fun `daraf is the base unit spelling`() {
        assertEquals(1 of reciprocalFarads, 1 of darafs)
    }

    @Test
    fun `enum entries expose symbol and base value`() {
        assertEquals("1/F", KElastanceUnit.RECIPROCAL_FARAD.symbol)
        assertEquals(1.0, KElastanceUnit.RECIPROCAL_FARAD.baseValue, 1e-12)
        assertEquals("daraf", KElastanceUnit.DARAF.symbol)
        assertEquals(1.0, KElastanceUnit.DARAF.baseValue, 1e-12)
    }

    @Test
    fun `base unit marker`() {
        assertEquals(KElastanceUnit.RECIPROCAL_FARAD, KElastanceUnit.BASE)
        assertEquals(1.0, KElastanceUnit.BASE.baseValue, 1e-12)
        assertEquals("1/F", KElastanceUnit.BASE.symbol)
    }

    @Test
    fun `enum entries are complete`() {
        assertEquals(2, KElastanceUnit.entries.size)
        assertEquals(listOf("1/F", "daraf"), KElastanceUnit.entries.map { it.symbol })
    }
}
