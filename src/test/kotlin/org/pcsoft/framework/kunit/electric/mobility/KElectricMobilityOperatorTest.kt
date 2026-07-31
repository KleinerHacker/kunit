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

package org.pcsoft.framework.kunit.electric.mobility

import org.junit.jupiter.api.TestInstance
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.fieldstrength.KElectricFieldStrengthUnitInstance
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.KSpeedUnitInstance
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

/**
 * The electric mobility operators: both decompositions (`speed/electricFieldStrength` and the canonical
 * native `mass⁻¹·time²·current` form via `toElectricMobility`), the inverse operators, and the
 * invalid-shape guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KElectricMobilityOperatorTest {

    /** A drift speed in meters per second, built from the native `length / time` expression. */
    private fun mps(value: Double): KSpeedUnitInstance = (value of meters) / (1 of seconds)

    /** Both electric mobility decompositions agree on the same typed, value-equal result. */
    @Test
    fun `all electric mobility decompositions agree`() {
        // Decomposition A: drift speed in a given field
        val fromSpeed = mps(6.0) / (3 of voltsPerMeter)
        assertEquals(2 of squareMetersPerVoltSecond, fromSpeed)

        // Decomposition B: native canonical expression
        val native = 2 of ((seconds pow 2) * (amperes pow 1)) / (kilo.grams pow 1)
        assertEquals(2 of squareMetersPerVoltSecond, native.toElectricMobility())
        assertEquals(2.0, native.toElectricMobility().value, 1e-9)

        // Both funnel into electricMobilityInstanceOf and are value-equal
        assertEquals(fromSpeed, native.toElectricMobility())
        assertEquals(fromSpeed.value, native.toElectricMobility().value, 1e-9)
    }

    /** Inverse and commutative operators: `μ·E = v`, `v/μ = E`. */
    @Test
    fun `inverse electric mobility operators`() {
        val mu = 2 of squareMetersPerVoltSecond
        assertIs<KSpeedUnitInstance>(mu * (3 of voltsPerMeter))
        assertEquals(6.0, (mu * (3 of voltsPerMeter)).value, 1e-9)
        assertEquals(6.0, ((3 of voltsPerMeter) * mu).value, 1e-9)
        assertIs<KElectricFieldStrengthUnitInstance>(mps(6.0) / mu)
        assertEquals(3.0, mps(6.0) / mu into voltsPerMeter, 1e-12)
    }

    /** Real-world example: silicon electrons at 0.14 m²/(V·s) drift 140 m/s in a 1 kV/m field. */
    @Test
    fun `silicon drift speed real world`() {
        val v = (1400 of squareCentimetersPerVoltSecond) * (1000 of voltsPerMeter)
        assertEquals(140.0, v.value, 1e-9)
    }

    /** A non-canonical shape cannot be read back as an electric mobility. */
    @Test
    fun `invalid electric mobility decomposition fails`() {
        // an extra distance term is not part of the canonical form
        val withDistance = ((seconds pow 2) * (amperes pow 1) * (meters pow 1)) / (kilo.grams pow 1)
        assertFailsWith<IllegalStateException> { withDistance.toElectricMobility() }
        // wrong time exponent (+3 instead of +2)
        val wrongTime = ((seconds pow 3) * (amperes pow 1)) / (kilo.grams pow 1)
        assertFailsWith<IllegalStateException> { wrongTime.toElectricMobility() }
        // wrong current exponent (+2 instead of +1)
        val wrongCurrent = ((seconds pow 2) * (amperes pow 2)) / (kilo.grams pow 1)
        assertFailsWith<IllegalStateException> { wrongCurrent.toElectricMobility() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toElectricMobility() }
    }
}
