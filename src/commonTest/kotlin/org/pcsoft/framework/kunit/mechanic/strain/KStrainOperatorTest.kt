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

package org.pcsoft.framework.kunit.mechanic.strain

import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.pressure.KPressureUnitInstance
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Hooke's law operators between the strain group and the pressure group (stress / elastic modulus). */
class KStrainOperatorTest {

    /** `stress / strain = E` - steel: 210 MPa at 1 ‰ gives 210 GPa. */
    @Test
    fun `stress over strain is the elastic modulus`() {
        val e = (210 of mega.pascals) / (1 of perMille)
        assertIs<KPressureUnitInstance>(e)
        assertEquals(210.0, e into giga.pascals, 1e-6)
    }

    /** `E * strain = stress`, plus its commutative form. */
    @Test
    fun `elastic modulus times strain is stress`() {
        val e = 210 of giga.pascals
        val s1 = e * (1 of perMille)
        val s2 = (1 of perMille) * e
        assertIs<KPressureUnitInstance>(s1)
        assertIs<KPressureUnitInstance>(s2)
        assertEquals(210.0, s1 into mega.pascals, 1e-6)
        assertEquals(210.0, s2 into mega.pascals, 1e-6)
    }

    /** Both directions of Hooke's law are exact inverses. */
    @Test
    fun `hooke round-trip`() {
        val e = 210 of giga.pascals
        val strain = 2 of perMille
        val stress = e * strain
        assertEquals(210.0e9, (stress / strain) into pascals, 1.0)
    }
}
