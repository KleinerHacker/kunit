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

package org.pcsoft.framework.kunit.thermo.resistance

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.power.KPowerUnitInstance
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifferenceUnitInstance
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group thermal resistance operators and the typed/native decomposition equivalence. */
class KThermalResistanceOperatorTest {

    /** `temperatureDifference / power = thermal resistance`. */
    @Test
    fun `temperature difference over power is thermal resistance`() {
        val r = KTemperatureDifference.ofKelvin(30) / (12 of watts)
        assertIs<KThermalResistanceUnitInstance>(r)
        assertEquals(2.5, r into kelvinsPerWatt, 1e-9)
    }

    /** `thermal resistance * power = temperatureDifference` and its commutative counterpart. */
    @Test
    fun `thermal resistance times power is temperature difference`() {
        val r = 2.5 of kelvinsPerWatt
        val p = 12 of watts
        val d1 = r * p
        val d2 = p * r
        assertIs<KTemperatureDifferenceUnitInstance>(d1)
        assertIs<KTemperatureDifferenceUnitInstance>(d2)
        assertEquals(30.0, d1 into KTemperatureDifference.ofKelvin(1), 1e-9)
        assertEquals(30.0, d2 into KTemperatureDifference.ofKelvin(1), 1e-9)
    }

    /** `temperatureDifference / thermal resistance = power`. */
    @Test
    fun `temperature difference over thermal resistance is power`() {
        val p = KTemperatureDifference.ofKelvin(30) / (2.5 of kelvinsPerWatt)
        assertIs<KPowerUnitInstance>(p)
        assertEquals(12.0, p into watts, 1e-9)
    }

    /** A heat-sink budget: how warm does the junction get above ambient at 12 W? */
    @Test
    fun `heat sink chain`() {
        val chain = (0.5 of kelvinsPerWatt) + (0.2 of degreesCelsiusPerWatt) + (1.8 of kelvinsPerWatt)
        val rise = chain * (12 of watts)
        assertEquals(30.0, rise into KTemperatureDifference.ofKelvin(1), 1e-9)
    }

    /** The typed operator and the native `kg⁻¹·m⁻²·s³·K` expression agree. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = KTemperatureDifference.ofKelvin(30) / (12 of watts)
        val kelvinTerm = KTemperatureDifference.ofKelvin(1).toUnit()
        val native = (
                2.5 of (seconds pow 3) * kelvinTerm / kilo.grams.toUnit() / (meters pow 2)
                ).toThermalResistance()
        assertIs<KThermalResistanceUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(typed into kelvinsPerWatt, native into kelvinsPerWatt, 1e-9)
    }
}
