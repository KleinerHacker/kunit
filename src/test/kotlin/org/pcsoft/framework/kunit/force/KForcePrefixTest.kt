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

package org.pcsoft.framework.kunit.force

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/** Prefixed force templates scale the underlying unit by the prefix factor. */
class KForcePrefixTest {

    @Test
    fun `prefixed newtons`() {
        assertEquals(1000.0, (1 of kilo.newtons) into newtons, 1e-9)      // 1 kN
        assertEquals(1_000_000.0, (1 of mega.newtons) into newtons, 1e-6) // 1 MN
    }

    @Test
    fun `prefixed dynes and ponds`() {
        assertEquals(1e-2, (1 of kilo.dynes) into newtons, 1e-12)   // 1 kdyn = 10⁻² N
        assertEquals(9.80665, (1 of kilo.ponds) into newtons, 1e-9) // 1 kp = 1 kgf
    }
}
