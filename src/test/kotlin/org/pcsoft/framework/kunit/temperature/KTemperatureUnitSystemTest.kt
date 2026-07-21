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

import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

/**
 * `KTemperatureUnitInstance` surface: affine `of`/`into` construction and round-trip, the internal
 * absolute-kelvin storage, `scaledBy`, `equals`/`hashCode`/`toString`, `pow`, and generic-engine interop.
 */
class KTemperatureUnitSystemTest {

    /** `n of celsius` stores absolute kelvin and round-trips back through `into`. */
    @Test
    fun `affine construction and round-trip`() {
        assertEquals(298.15, (25 of celsius).value, 1e-9)         // stored as absolute kelvin
        assertEquals(25.0, (25 of celsius) into celsius, 1e-9)    // round-trip
        assertEquals(273.15, (0 of celsius).value, 1e-9)
        assertEquals(68.0 * 5.0 / 9.0 + 273.15, (100 of fahrenheit).value, 1e-9)
    }

    /** `scaledBy` is the affine construction hook that backs the generic `of` verb. */
    @Test
    fun `scaledBy is the affine of hook`() {
        // celsius template: scaledBy(25) reads 25 °C -> 298.15 K (identical to `25 of celsius`)
        assertEquals(298.15, celsius.scaledBy(25.0).value, 1e-9)
        // kelvin base is the identity transform
        assertEquals(25.0, kelvin.scaledBy(25.0).value, 1e-9)
    }

    /** Equality/hash by normalized absolute kelvin, independent of construction unit. */
    @Test
    fun `equals and hashCode`() {
        assertEquals(0 of celsius, 273.15 of kelvin)
        assertEquals((0 of celsius).hashCode(), (273.15 of kelvin).hashCode())
        assertNotEquals(0 of celsius, 1 of celsius)
        assertFalse((0 of celsius).equals(273.15)) // not a KTemperatureUnitInstance
    }

    /** `toString` prints the base-unit (kelvin) representation. */
    @Test
    fun `toString is kelvin representation`() {
        assertEquals("298.15 K", (25 of celsius).toString())
    }

    /** `pow` runs linearly through the generic engine on the absolute kelvin term. */
    @Test
    fun `pow through generic engine`() {
        val squared = (2 of kelvin) pow 2
        assertEquals(4.0, squared.value, 1e-9)
        assertEquals(listOf(KUnitTerm(KTemperatureUnit.KELVIN, 2)), squared.units)
    }

    /** A single non-kelvin temperature term is normalized to absolute kelvin on `toTemperature`. */
    @Test
    fun `toTemperature normalizes to kelvin`() {
        assertEquals(273.15, (0 of celsius).toUnit().toTemperature().value, 1e-9)
    }

    /** A mixed unit that is not a single temperature term cannot be converted to a temperature value. */
    @Test
    fun `toTemperature on non-temperature fails`() {
        assertFailsWith<IllegalStateException> { (1 of seconds).toUnit().toTemperature() }
        // a dimensionless (no-term) mixed unit also fails (the term is null)
        assertFailsWith<IllegalStateException> { ((1 of kelvin).toUnit() / (1 of kelvin).toUnit()).toTemperature() }
    }
}
