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

package org.pcsoft.framework.kunit.common.diffusivity

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.KDensityUnitInstance
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.viscosity.KViscosityUnitInstance
import org.pcsoft.framework.kunit.mechanic.viscosity.pascalSeconds
import org.pcsoft.framework.kunit.mechanic.viscosity.toViscosity
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * The **kinematic viscosity** reading of the diffusivity group: `ν = η / ρ`, its inverses, and the
 * equality with the native `distance² · time⁻¹` form.
 */
class KKinematicViscosityOperatorTest {

    private val water: KDensityUnitInstance =
        (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

    /** `viscosity / density = diffusivity` - water at 20 °C is 1 cSt. */
    @Test
    fun `dynamic viscosity over density is kinematic viscosity`() {
        val nu = (1 of milli.pascalSeconds) / water
        assertIs<KDiffusivityUnitInstance>(nu)
        assertEquals(1.0, nu into centistokes, 1e-9)
        assertEquals(1.0e-6, nu into squareMetersPerSecond, 1e-15)
    }

    /** `diffusivity * density = viscosity`, plus its commutative form. */
    @Test
    fun `kinematic viscosity times density is dynamic viscosity`() {
        val nu = 1 of centistokes
        val eta1 = nu * water
        val eta2 = water * nu
        assertIs<KViscosityUnitInstance>(eta1)
        assertIs<KViscosityUnitInstance>(eta2)
        assertEquals(0.001, eta1 into pascalSeconds, 1e-12)
        assertEquals(0.001, eta2 into pascalSeconds, 1e-12)
    }

    /** `viscosity / diffusivity = density`. */
    @Test
    fun `dynamic over kinematic viscosity is density`() {
        val rho: KDensityUnitInstance = (1 of milli.pascalSeconds) / (1 of centistokes)
        assertIs<KDensityUnitInstance>(rho)
        assertEquals(water.value, rho.value, 1e-6)
    }

    /** The typed decomposition and the native `m²/s` form yield the same value-equal result. */
    @Test
    fun `typed and native form agree`() {
        val typed = (1 of milli.pascalSeconds) / water
        val native = (((1 of milli.meters).toUnit() pow 2) / (1 of seconds).toUnit()).toDiffusivity()
        assertEquals(typed, native)
    }

    /** A pure gram-based viscosity expression divided by a density also lands on the normal form. */
    @Test
    fun `gram based expression reduces onto the normal form`() {
        val eta = ((1 of grams).toUnit() / (1 of meters).toUnit() / (1 of seconds).toUnit()).toViscosity()
        val nu = eta / water
        assertEquals(1.0e-6, nu into squareMetersPerSecond, 1e-15)
    }
}
