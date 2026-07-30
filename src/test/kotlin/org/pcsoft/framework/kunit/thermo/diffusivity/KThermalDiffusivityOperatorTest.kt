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

package org.pcsoft.framework.kunit.thermo.diffusivity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.mechanic.density.toDensity
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.thermo.conductivity.KThermalConductivityUnitInstance
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.KSpecificHeatCapacityUnitInstance
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** The defining relation `α = λ / (ρ · c_p)`, its inverses, and the equivalence of all decompositions. */
class KThermalDiffusivityOperatorTest {

    /** 8960 kg/m³, the density of copper, built from the base dimensions. */
    private val copperDensity: KDensityUnitInstance =
        ((8_960_000 of grams).toUnit() / ((1 of meters).toUnit() pow 3)).toDensity()

    private val copperConductivity = 401 of wattsPerMeterKelvin
    private val copperHeat = 385 of joulesPerKilogramKelvin

    /** `α = λ / (ρ · c_p)`. */
    @Test
    fun `diffusivity from conductivity density and specific heat`() {
        val alpha = copperConductivity.diffusivityWith(copperDensity, copperHeat)
        assertIs<KThermalDiffusivityUnitInstance>(alpha)
        // 401 / (8960 * 385) = 1.16249...e-4 m²/s
        assertEquals(401.0 / (8960.0 * 385.0), alpha into squareMetersPerSecond, 1e-15)
        assertEquals(116.245, alpha into squareMillimetersPerSecond, 1e-3)
    }

    /** `λ = α · ρ · c_p` recovers the conductivity exactly. */
    @Test
    fun `conductivity from diffusivity density and specific heat`() {
        val alpha = copperConductivity.diffusivityWith(copperDensity, copperHeat)
        val back = alpha.conductivityWith(copperDensity, copperHeat)
        assertIs<KThermalConductivityUnitInstance>(back)
        assertEquals(401.0, back into wattsPerMeterKelvin, 1e-9)
    }

    /** `ρ = λ / (α · c_p)` recovers the density exactly. */
    @Test
    fun `density from diffusivity conductivity and specific heat`() {
        val alpha = copperConductivity.diffusivityWith(copperDensity, copperHeat)
        val back = alpha.densityWith(copperConductivity, copperHeat)
        assertIs<KDensityUnitInstance>(back)
        assertEquals(copperDensity.value, back.value, 1e-3)
    }

    /** `c_p = λ / (α · ρ)` recovers the specific heat capacity exactly. */
    @Test
    fun `specific heat from diffusivity conductivity and density`() {
        val alpha = copperConductivity.diffusivityWith(copperDensity, copperHeat)
        val back = alpha.specificHeatCapacityWith(copperConductivity, copperDensity)
        assertIs<KSpecificHeatCapacityUnitInstance>(back)
        assertEquals(385.0, back into joulesPerKilogramKelvin, 1e-9)
    }

    /**
     * Both decompositions - the typed `diffusivityWith` relation and the native `distance²·time⁻¹`
     * expression through `toThermalDiffusivity()` - yield the same typed, value-equal result.
     */
    @Test
    fun `all decompositions agree`() {
        // λ = 1 W/(m·K), ρ = 1 kg/m³, c_p = 1 J/(kg·K)  =>  α = 1 m²/s
        val unitDensity = ((1000 of grams).toUnit() / ((1 of meters).toUnit() pow 3)).toDensity()
        val typed = (1 of wattsPerMeterKelvin)
            .diffusivityWith(unitDensity, 1 of joulesPerKilogramKelvin)
        val native = (((1 of meters).toUnit() pow 2) / (1 of seconds).toUnit()).toThermalDiffusivity()

        assertIs<KThermalDiffusivityUnitInstance>(typed)
        assertIs<KThermalDiffusivityUnitInstance>(native)
        assertEquals(typed, native)
        assertEquals(1.0, typed into squareMetersPerSecond, 1e-9)
    }

    /** The kilogram-based spelling of the density gives the same result. */
    @Test
    fun `density spelling does not matter`() {
        val fromKilograms = ((8960 of kilo.grams).toUnit() / ((1 of meters).toUnit() pow 3)).toDensity()
        assertEquals(
            copperConductivity.diffusivityWith(copperDensity, copperHeat),
            copperConductivity.diffusivityWith(fromKilograms, copperHeat),
        )
    }
}
