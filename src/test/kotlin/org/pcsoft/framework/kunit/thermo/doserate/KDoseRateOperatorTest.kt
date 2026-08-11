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

package org.pcsoft.framework.kunit.thermo.doserate

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.time.KTimeUnitInstance
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.specificenergy.KSpecificEnergyUnitInstance
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** Typed cross-group dose rate operators and the typed/native decomposition equivalence. */
class KDoseRateOperatorTest {

    /** `specificEnergy / time = dose rate` - an absorbed dose is a specific energy (`J/kg` = `Gy`). */
    @Test
    fun `specific energy over time is dose rate`() {
        val rate = (2 of milli.joulesPerKilogram) / (1 of hours)
        assertIs<KDoseRateUnitInstance>(rate)
        assertEquals(2.0, rate into milli.graysPerHour, 1e-9)
    }

    /** `dose rate * time = specificEnergy` and its commutative counterpart. */
    @Test
    fun `dose rate times time is specific energy`() {
        val rate = 2 of milli.graysPerHour
        val t = 3 of hours
        val d1 = rate * t
        val d2 = t * rate
        assertIs<KSpecificEnergyUnitInstance>(d1)
        assertIs<KSpecificEnergyUnitInstance>(d2)
        assertEquals(6.0, d1 into milli.joulesPerKilogram, 1e-9)
        assertEquals(6.0, d2 into milli.joulesPerKilogram, 1e-9)
    }

    /** `specificEnergy / dose rate = time`. */
    @Test
    fun `specific energy over dose rate is time`() {
        val t = (6 of milli.joulesPerKilogram) / (2 of milli.graysPerHour)
        assertIs<KTimeUnitInstance>(t)
        assertEquals(3.0, t into hours, 1e-9)
    }

    /** An annual background dose of about 2.4 mSv at a constant rate. */
    @Test
    fun `background radiation over a year`() {
        val rate = 0.274 of micro.sievertsPerHour
        val year = 8766 of hours
        val dose = rate * year
        assertEquals(2.4, dose into milli.joulesPerKilogram, 1e-2)
    }

    /** The typed operator and the native `m²·s⁻³` expression agree. */
    @Test
    fun `typed and native decomposition agree`() {
        val typed = (6 of joulesPerKilogram) / (2 of seconds)
        val native = ((6 of joulesPerKilogram).toUnit() / (2 of seconds).toUnit()).toDoseRate()
        assertIs<KDoseRateUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(typed into graysPerSecond, native into graysPerSecond, 1e-12)
    }
}
