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

package org.pcsoft.framework.kunit.electric.specificcharge

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.KChargeUnitInstance
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.KMassUnitInstance
import org.pcsoft.framework.kunit.mechanic.mass.grams
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group specific charge operators and the typed/native decomposition equivalence. */
class KSpecificChargeOperatorTest {

    /** `charge / mass = specific charge`. */
    @Test
    fun `charge over mass is specific charge`() {
        val ratio = (4 of coulombs) / (2 of kilo.grams)
        assertIs<KSpecificChargeUnitInstance>(ratio)
        assertEquals(2.0, ratio into coulombsPerKilogram, 1e-9)
    }

    /** `specific charge * mass = charge` and its commutative counterpart. */
    @Test
    fun `specific charge times mass is charge`() {
        val ratio = 2 of coulombsPerKilogram
        val m = 2 of kilo.grams
        val q1 = ratio * m
        val q2 = m * ratio
        assertIs<KChargeUnitInstance>(q1)
        assertIs<KChargeUnitInstance>(q2)
        assertEquals(4.0, q1 into coulombs, 1e-9)
        assertEquals(4.0, q2 into coulombs, 1e-9)
    }

    /** `charge / specific charge = mass`. */
    @Test
    fun `charge over specific charge is mass`() {
        val m = (4 of coulombs) / (2 of coulombsPerKilogram)
        assertIs<KMassUnitInstance>(m)
        assertEquals(2.0, m into kilo.grams, 1e-9)
    }

    /** The ionisation-dose reading: 1 R is 2.58e-4 C/kg. */
    @Test
    fun `roentgen as ionisation dose`() {
        val exposure = 1 of roentgens
        assertEquals(2.58e-4, exposure into coulombsPerKilogram, 1e-16)

        // The charge liberated in 1 kg of air
        val q = exposure * (1 of kilo.grams)
        assertEquals(2.58e-4, q into coulombs, 1e-16)
    }

    /** The typed operator and the native `A·s·kg⁻¹` expression agree. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (4 of coulombs) / (2 of kilo.grams)
        val native = (2 of amperesSecondsPerKilogram()).toSpecificCharge()
        assertIs<KSpecificChargeUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(typed into coulombsPerKilogram, native into coulombsPerKilogram, 1e-9)
    }

    private fun amperesSecondsPerKilogram() =
        org.pcsoft.framework.kunit.electric.current.amperes.toUnit() *
                (org.pcsoft.framework.kunit.kinematic.time.seconds pow 1) /
                kilo.grams.toUnit()
}
