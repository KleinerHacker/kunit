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

package org.pcsoft.framework.kunit.temperature

import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The concrete temperature units (`KTemperatureUnitBareValues` + `KTemperatureUnit` affine transforms):
 * the full K↔C↔F conversion matrix including the agreed fixpoints.
 */
class KTemperatureUnitTest {

    /** Verified fixpoints: 0 °C = 273.15 K, 100 °C = 212 °F, 32 °F = 0 °C. */
    @Test
    fun `agreed fixpoints`() {
        assertEquals(273.15, (0 of celsius) into kelvin, 1e-9)
        assertEquals(212.0, (100 of celsius) into fahrenheit, 1e-9)
        assertEquals(0.0, (32 of fahrenheit) into celsius, 1e-9)
    }

    /** Identity reads and the standard 25 °C reference across all three units. */
    @Test
    fun `reference conversions`() {
        assertEquals(25.0, (25 of celsius) into celsius, 1e-9)
        assertEquals(298.15, (25 of celsius) into kelvin, 1e-9)
        assertEquals(77.0, (25 of celsius) into fahrenheit, 1e-9)
        assertEquals(0.0, (0 of kelvin) into kelvin, 1e-9)
    }

    /** Round-trips through every unit return the original reading. */
    @Test
    fun `round trips`() {
        assertEquals(37.0, ((37 of celsius) into fahrenheit).let { (it of fahrenheit) into celsius }, 1e-9)
        assertEquals(500.0, ((500 of kelvin) into celsius).let { (it of celsius) into kelvin }, 1e-9)
        assertEquals(-40.0, (-40 of celsius) into fahrenheit, 1e-9) // −40 is the C/F crossover
    }

    /** Rankine: absolute scale, zero at absolute zero, Fahrenheit-sized degrees. */
    @Test
    fun `rankine conversions`() {
        assertEquals(0.0, (0 of kelvin) into rankine, 1e-9)          // absolute zero
        assertEquals(491.67, (0 of celsius) into rankine, 1e-9)      // 273.15 K · 9/5
        assertEquals(273.15, (491.67 of rankine) into kelvin, 1e-9)  // round-trip to kelvin
        assertEquals(536.67, (25 of celsius) into rankine, 1e-9)     // 298.15 K · 9/5
        assertEquals(671.67, (100 of celsius) into rankine, 1e-9)    // boiling point
    }
}
