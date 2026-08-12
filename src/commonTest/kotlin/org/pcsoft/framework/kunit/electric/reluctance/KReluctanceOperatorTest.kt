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

package org.pcsoft.framework.kunit.electric.reluctance

import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.KElectricCurrentUnitInstance
import org.pcsoft.framework.kunit.electric.current.ampereTurns
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.inductance.KInductanceUnitInstance
import org.pcsoft.framework.kunit.electric.inductance.henries
import org.pcsoft.framework.kunit.electric.magneticflux.KMagneticFluxUnitInstance
import org.pcsoft.framework.kunit.electric.magneticflux.webers
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

/**
 * The reluctance operators: all three decompositions (Hopkinson's law `current/magneticFlux`, the reciprocal
 * `1/inductance`, and the canonical native `mass⁻¹·length⁻²·time²·current²` form via `toReluctance`), the
 * inverse operators, and the invalid-shape guard.
 */
class KReluctanceOperatorTest {

    /** All reluctance decompositions agree on the same typed, value-equal result. */
    @Test
    fun `all reluctance decompositions agree`() {
        // Decomposition A: Hopkinson's law
        val fromHopkinson = (6 of amperes) / (3 of webers)
        assertEquals(2 of amperesPerWeber, fromHopkinson)

        // Decomposition B: the reciprocal of a permeance
        val fromPermeance = 1 / (0.5 of henries)
        assertEquals(2 of amperesPerWeber, fromPermeance)

        // Decomposition C: native canonical expression
        val native = 2 of ((seconds pow 2) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
        assertEquals(2 of amperesPerWeber, native.toReluctance())
        assertEquals(2.0, native.toReluctance().value, 1e-9)

        // All three funnel into reluctanceInstanceOf and are value-equal
        assertEquals(fromHopkinson, fromPermeance)
        assertEquals(fromHopkinson, native.toReluctance())
        assertEquals(fromPermeance.value, native.toReluctance().value, 1e-9)
    }

    /** Inverse and commutative operators of Hopkinson's law: `Rm·Φ = Θ`, `Θ/Rm = Φ`. */
    @Test
    fun `inverse hopkinson operators`() {
        val rm = 2 of amperesPerWeber
        assertIs<KElectricCurrentUnitInstance>(rm * (3 of webers))
        assertEquals(6 of amperes, rm * (3 of webers))
        assertEquals(6 of amperes, (3 of webers) * rm)
        assertIs<KMagneticFluxUnitInstance>((6 of amperes) / rm)
        assertEquals(3.0, (6 of amperes) / rm into webers, 1e-12)
    }

    /** The reciprocal pair is symmetric: `1/Rm` yields the permeance in henries. */
    @Test
    fun `reciprocal pair is symmetric`() {
        val rm = 2 of amperesPerWeber
        assertIs<KInductanceUnitInstance>(1 / rm)
        assertEquals(0.5, (1 / rm) into henries, 1e-12)
        assertEquals(rm, 1 / (1 / rm))
    }

    /** Real-world example: 2 kAt of magnetomotive force through 2 MA/Wb yields 1 mWb of flux. */
    @Test
    fun `magnetic circuit real world`() {
        val rm = 2_000_000 of amperesPerWeber
        val flux = (2000 of ampereTurns) / rm
        assertEquals(1.0, flux into milli.webers, 1e-9)
    }

    /** A non-canonical shape cannot be read back as a reluctance. */
    @Test
    fun `invalid reluctance decomposition fails`() {
        // wrong distance exponent (-3 instead of -2)
        val wrongDistance = ((seconds pow 2) * (amperes pow 2)) / (kilo.grams * (meters pow 3))
        assertFailsWith<IllegalStateException> { wrongDistance.toReluctance() }
        // wrong time exponent (+3 instead of +2)
        val wrongTime = ((seconds pow 3) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
        assertFailsWith<IllegalStateException> { wrongTime.toReluctance() }
        // wrong current exponent (+1 instead of +2)
        val wrongCurrent = ((seconds pow 2) * (amperes pow 1)) / (kilo.grams * (meters pow 2))
        assertFailsWith<IllegalStateException> { wrongCurrent.toReluctance() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toReluctance() }
    }
}
