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

package org.pcsoft.framework.kunit.thermo.conductance

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.power.KPowerUnitInstance
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.resistance.KThermalResistanceUnitInstance
import org.pcsoft.framework.kunit.thermo.resistance.kelvinsPerWatt
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnitInstance
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group thermal conductance operators and the typed/native decomposition equivalence. */
class KThermalConductanceOperatorTest {

    /** `power / temperatureDifference = thermal conductance`. */
    @Test
    fun `power over temperature difference is thermal conductance`() {
        val g = (12 of watts) / KTemperatureDifference.ofKelvin(30)
        assertIs<KThermalConductanceUnitInstance>(g)
        assertEquals(0.4, g into wattsPerKelvin, 1e-9)
    }

    /** `thermal conductance * temperatureDifference = power` and its commutative counterpart. */
    @Test
    fun `thermal conductance times temperature difference is power`() {
        val g = 0.4 of wattsPerKelvin
        val d = KTemperatureDifference.ofKelvin(30)
        val p1 = g * d
        val p2 = d * g
        assertIs<KPowerUnitInstance>(p1)
        assertIs<KPowerUnitInstance>(p2)
        assertEquals(12.0, p1 into watts, 1e-9)
        assertEquals(12.0, p2 into watts, 1e-9)
    }

    /** `power / thermal conductance = temperatureDifference`. */
    @Test
    fun `power over thermal conductance is temperature difference`() {
        val d = (12 of watts) / (0.4 of wattsPerKelvin)
        assertIs<KTemperatureDifferenceUnitInstance>(d)
        assertEquals(30.0, d into KTemperatureDifference.ofKelvin(1), 1e-9)
    }

    /** Conductance and absolute thermal resistance are reciprocals. */
    @Test
    fun `reciprocal of thermal resistance is conductance`() {
        val g = 1 / (2.5 of kelvinsPerWatt)
        assertIs<KThermalConductanceUnitInstance>(g)
        assertEquals(0.4, g into wattsPerKelvin, 1e-9)

        val r = 1 / (0.4 of wattsPerKelvin)
        assertIs<KThermalResistanceUnitInstance>(r)
        assertEquals(2.5, r into kelvinsPerWatt, 1e-9)
    }

    /** The typed operator and the native `kg·m²·s⁻³·K⁻¹` expression agree. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (12 of watts) / KTemperatureDifference.ofKelvin(30)
        val kelvinTerm = KTemperatureDifference.ofKelvin(1).toUnit()
        val native = (
                0.4 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / kelvinTerm
                ).toThermalConductance()
        assertIs<KThermalConductanceUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(typed into wattsPerKelvin, native into wattsPerKelvin, 1e-9)
    }
}
