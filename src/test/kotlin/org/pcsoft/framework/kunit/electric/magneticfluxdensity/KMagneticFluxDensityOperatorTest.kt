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

package org.pcsoft.framework.kunit.electric.magneticfluxdensity

import org.junit.jupiter.api.TestInstance
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.electric.magneticflux.KMagneticFluxUnitInstance
import org.pcsoft.framework.kunit.electric.magneticflux.webers
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * The magnetic flux density operators: same-type arithmetic/comparison, the escape of
 * `fluxDensity*fluxDensity`/`fluxDensity/fluxDensity` to a mixed unit, both flux density decompositions
 * (`flux / area` and the canonical native `mass·time⁻²·current⁻¹` form via `toMagneticFluxDensity`), the
 * inverse operators, and the invalid-shape guard.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KMagneticFluxDensityOperatorTest {

    /** Same-type flux density operators: `+`, `-`, comparison, and the escape to a mixed unit. */
    @Test
    fun `magnetic flux density same-type operators`() {
        val b1 = 3 of teslas
        val b2 = 1 of teslas
        assertEquals(4.0, (b1 + b2).value, 1e-9)
        assertEquals(2.0, (b1 - b2).value, 1e-9)
        assertTrue(b1 > b2)
        assertIs<KMixedUnitInstance>(b1 * b2)
        assertIs<KMixedUnitInstance>(b1 / b2)
    }

    /** Both decompositions agree: `Φ/A` and the native `kg·s⁻²·A⁻¹` expression. */
    @Test
    fun `both magnetic flux density decompositions agree`() {
        // Decomposition A: the definition B = Φ / A
        val area = (2 of meters) * (3 of meters) // 6 m²
        val fromFluxArea = (18 of webers) / area
        assertEquals(3 of teslas, fromFluxArea)

        // Decomposition B: native canonical expression
        val native = 3 of (kilo.grams / ((seconds pow 2) * (amperes pow 1)))
        assertEquals(3 of teslas, native.toMagneticFluxDensity())
        assertEquals(3.0, native.toMagneticFluxDensity().value, 1e-9)

        // Both funnel into magneticFluxDensityInstanceOf and are value-equal
        assertEquals(fromFluxArea, native.toMagneticFluxDensity())
        assertEquals(fromFluxArea.value, native.toMagneticFluxDensity().value, 1e-9)
    }

    /** `toMagneticFluxDensity` is order independent (current term first). */
    @Test
    fun `toMagneticFluxDensity is order independent`() {
        val native = 3 of (kilo.grams / ((amperes pow 1) * (seconds pow 2)))
        assertEquals(3 of teslas, native.toMagneticFluxDensity())
    }

    /** Inverse operators: `B·A = Φ`, `A·B = Φ`, `Φ/B = A`. */
    @Test
    fun `inverse magnetic flux density operators`() {
        val b = 3 of teslas
        val area = (2 of meters) * (3 of meters) // 6 m²
        assertIs<KMagneticFluxUnitInstance>(b * area)
        assertEquals(18 of webers, b * area)
        assertIs<KMagneticFluxUnitInstance>(area * b)
        assertEquals(18 of webers, area * b)
        assertIs<KAreaUnitInstance>((18 of webers) / b)
        assertEquals(6.0, ((18 of webers) / b).value, 1e-9)
    }

    /** Real-world example: an MRI scanner at 3 T through a 0.25 m² bore cross-section. */
    @Test
    fun `mri scanner flux`() {
        val bore = (0.5 of meters) * (0.5 of meters) // 0.25 m²
        assertEquals(0.75, ((3 of teslas) * bore).value, 1e-9)
    }

    /** A non-canonical shape cannot be read back as a magnetic flux density. */
    @Test
    fun `invalid magnetic flux density decomposition fails`() {
        // extra distance term
        val withDistance = (kilo.grams * (meters pow 2)) / ((seconds pow 2) * (amperes pow 1))
        assertFailsWith<IllegalStateException> { withDistance.toMagneticFluxDensity() }
        // too few terms
        assertFailsWith<IllegalStateException> { (100 of grams).toUnit().toMagneticFluxDensity() }
    }
}
