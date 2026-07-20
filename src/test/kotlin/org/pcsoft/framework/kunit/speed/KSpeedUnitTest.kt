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

package org.pcsoft.framework.kunit.speed

import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The genuinely single-named speed tokens (`KSpeedUnitBareValues`): `knots`, `mach`, ... which survive as
 * whole-unit templates (unlike composite rates, which are written as expressions).
 */
class KSpeedUnitTest {

    /** Genuinely single-named speeds survive as tokens and build via `of`. */
    @Test
    fun `named special tokens`() {
        assertEquals(KSpeedUnit.KNOT.baseValue, (1 of knots).value, 1e-9)
        assertEquals(340.29, (1 of mach).value, 1e-9)
        assertEquals(KSpeedUnit.LIGHT_SPEED.baseValue, (1 of speedOfLight).value, 1e-9)
    }
}
