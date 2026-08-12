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

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.capacitance.KCapacitanceUnitInstance
import org.pcsoft.framework.kunit.electric.capacitance.farads
import org.pcsoft.framework.kunit.electric.charge.KChargeUnitInstance
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.voltage.KVoltageUnitInstance
import org.pcsoft.framework.kunit.electric.voltage.volts
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group elastance operators: the reciprocal of the capacitance. */
class KElastanceOperatorTest {

    /** `voltage / charge = elastance`. */
    @Test
    fun `voltage over charge is elastance`() {
        val s = (10 of volts) / (10 of milli.coulombs)
        assertIs<KElastanceUnitInstance>(s)
        assertEquals(1000.0, s into reciprocalFarads, 1e-9)
    }

    /** `elastance * charge = voltage` and its commutative counterpart. */
    @Test
    fun `elastance times charge is voltage`() {
        val s = 1000 of reciprocalFarads
        val q = 10 of milli.coulombs
        val u1 = s * q
        val u2 = q * s
        assertIs<KVoltageUnitInstance>(u1)
        assertIs<KVoltageUnitInstance>(u2)
        assertEquals(10.0, u1 into volts, 1e-9)
        assertEquals(10.0, u2 into volts, 1e-9)
    }

    /** `voltage / elastance = charge`. */
    @Test
    fun `voltage over elastance is charge`() {
        val q = (10 of volts) / (1000 of reciprocalFarads)
        assertIs<KChargeUnitInstance>(q)
        assertEquals(10.0, q into milli.coulombs, 1e-9)
    }

    /** Elastance and capacitance are reciprocals in both directions. */
    @Test
    fun `reciprocal of capacitance is elastance`() {
        val s = 1 / (1 of milli.farads)
        assertIs<KElastanceUnitInstance>(s)
        assertEquals(1000.0, s into reciprocalFarads, 1e-9)

        val c = 1 / (1000 of reciprocalFarads)
        assertIs<KCapacitanceUnitInstance>(c)
        assertEquals(1.0, c into milli.farads, 1e-12)
    }

    /** Capacitors in series: the elastances add, the capacitance follows from the reciprocal. */
    @Test
    fun `capacitors in series`() {
        val total = (1 / (1 of milli.farads)) + (1 / (1 of milli.farads))
        assertEquals(2000.0, total into reciprocalFarads, 1e-9)
        assertEquals(0.5, (1 / total) into milli.farads, 1e-12)
    }
}
