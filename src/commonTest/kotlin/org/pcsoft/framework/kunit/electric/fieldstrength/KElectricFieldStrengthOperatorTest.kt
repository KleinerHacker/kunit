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

package org.pcsoft.framework.kunit.electric.fieldstrength

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.KChargeUnitInstance
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.KVoltageUnitInstance
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.kinematic.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.KForceUnitInstance
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.mass.grams
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

/**
 * The electric field strength operators: all three decompositions (`voltage/length`, `force/charge` and the
 * canonical native `mass·length·time⁻³·current⁻¹` form via `toElectricFieldStrength`), the inverse
 * operators, and the invalid-shape guard.
 */
class KElectricFieldStrengthOperatorTest {

    /** All electric field strength decompositions agree on the same typed, value-equal result. */
    @Test
    fun `all electric field strength decompositions agree`() {
        // Decomposition A: voltage over a distance
        val fromVoltage = (10 of volts) / (5 of meters)
        assertEquals(2 of voltsPerMeter, fromVoltage)

        // Decomposition B: force per charge
        val fromForce = (6 of newtons) / (3 of coulombs)
        assertEquals(2 of voltsPerMeter, fromForce)

        // Decomposition C: native canonical expression
        val native = 2 of (kilo.grams * (meters pow 1)) / ((seconds pow 3) * (amperes pow 1))
        assertEquals(2 of voltsPerMeter, native.toElectricFieldStrength())
        assertEquals(2.0, native.toElectricFieldStrength().value, 1e-9)

        // All three funnel into electricFieldStrengthInstanceOf and are value-equal
        assertEquals(fromVoltage, fromForce)
        assertEquals(fromVoltage, native.toElectricFieldStrength())
        assertEquals(fromForce.value, native.toElectricFieldStrength().value, 1e-9)
    }

    /** Inverse and commutative operators of the voltage decomposition: `E·l = U`, `U/E = l`. */
    @Test
    fun `inverse voltage operators`() {
        val e = 2 of voltsPerMeter
        assertIs<KVoltageUnitInstance>(e * (5 of meters))
        assertEquals(10 of volts, e * (5 of meters))
        assertEquals(10 of volts, (5 of meters) * e)
        assertIs<KLengthUnitInstance>((10 of volts) / e)
        assertEquals(5.0, (10 of volts) / e into meters, 1e-12)
    }

    /** Inverse and commutative operators of the force decomposition: `E·Q = F`, `F/E = Q`. */
    @Test
    fun `inverse force operators`() {
        val e = 2 of voltsPerMeter
        assertIs<KForceUnitInstance>(e * (3 of coulombs))
        assertEquals(6.0, (e * (3 of coulombs)) into newtons, 1e-9)
        assertEquals(6.0, ((3 of coulombs) * e) into newtons, 1e-9)
        assertIs<KChargeUnitInstance>((6 of newtons) / e)
        assertEquals(3.0, (6 of newtons) / e into coulombs, 1e-9)
    }

    /** Real-world example: 230 V across a 2 mm air gap gives a field strength of 115 kV/m. */
    @Test
    fun `air gap field strength real world`() {
        val e = (230 of volts) / (2 of milli.meters)
        assertEquals(115_000.0, e.value, 1e-6)
        assertEquals(1150.0, e into voltsPerCentimeter, 1e-9)
    }

    /** A non-canonical shape cannot be read back as an electric field strength. */
    @Test
    fun `invalid electric field strength decomposition fails`() {
        // wrong distance exponent (+2 instead of +1) - that is a voltage
        val wrongDistance = (kilo.grams * (meters pow 2)) / ((seconds pow 3) * (amperes pow 1))
        assertFailsWith<IllegalStateException> { wrongDistance.toElectricFieldStrength() }
        // wrong time exponent (-2 instead of -3)
        val wrongTime = (kilo.grams * (meters pow 1)) / ((seconds pow 2) * (amperes pow 1))
        assertFailsWith<IllegalStateException> { wrongTime.toElectricFieldStrength() }
        // wrong current exponent (-2 instead of -1)
        val wrongCurrent = (kilo.grams * (meters pow 1)) / ((seconds pow 3) * (amperes pow 2))
        assertFailsWith<IllegalStateException> { wrongCurrent.toElectricFieldStrength() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toElectricFieldStrength() }
    }
}
