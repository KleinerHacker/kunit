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

package org.pcsoft.framework.kunit.speed

import kotlin.test.Test
import kotlin.test.assertEquals

class KSpeedUnitTest {

    /** Metre-per-second is the group's base unit (factor 1.0, symbol "m/s") and equals [KSpeedUnit.BASE]. */
    @Test
    fun `meters per second is the base unit`() {
        assertEquals(KSpeedUnit.METERS_PER_SECOND, KSpeedUnit.BASE)
        assertEquals(1.0, KSpeedUnit.METERS_PER_SECOND.baseValue)
        assertEquals("m/s", KSpeedUnit.METERS_PER_SECOND.symbol)
    }

    /** km/h carries the symbol "km/h" and the conversion factor 1000/3600 m/s. */
    @Test
    fun `kilometers per hour symbol and baseValue`() {
        assertEquals("km/h", KSpeedUnit.KILOMETERS_PER_HOUR.symbol)
        assertEquals(1000.0 / 3600.0, KSpeedUnit.KILOMETERS_PER_HOUR.baseValue)
    }

    /** mph carries the symbol "mph" and the conversion factor 1609.344/3600 m/s. */
    @Test
    fun `miles per hour symbol and baseValue`() {
        assertEquals("mph", KSpeedUnit.MILES_PER_HOUR.symbol)
        assertEquals(1609.344 / 3600.0, KSpeedUnit.MILES_PER_HOUR.baseValue)
    }

    /** The knot carries the symbol "kn" and the conversion factor 1852/3600 m/s. */
    @Test
    fun `knot symbol and baseValue`() {
        assertEquals("kn", KSpeedUnit.KNOT.symbol)
        assertEquals(1852.0 / 3600.0, KSpeedUnit.KNOT.baseValue)
    }

    /** ft/s carries the symbol "ft/s" and the conversion factor 0.3048 m/s. */
    @Test
    fun `feet per second symbol and baseValue`() {
        assertEquals("ft/s", KSpeedUnit.FEET_PER_SECOND.symbol)
        assertEquals(0.3048, KSpeedUnit.FEET_PER_SECOND.baseValue)
    }

    /** Mach carries the symbol "Ma" and the ISA sea-level speed of sound 340.29 m/s. */
    @Test
    fun `mach symbol and baseValue`() {
        assertEquals("Ma", KSpeedUnit.MACH.symbol)
        assertEquals(340.29, KSpeedUnit.MACH.baseValue)
    }

    /** The speed of light carries the symbol "c" and the exact value 299 792 458 m/s. */
    @Test
    fun `speed of light symbol and baseValue`() {
        assertEquals("c", KSpeedUnit.LIGHT_SPEED.symbol)
        assertEquals(299792458.0, KSpeedUnit.LIGHT_SPEED.baseValue)
    }

    /** The group defines exactly seven units — guards against an accidental addition/removal. */
    @Test
    fun `all enum values are covered`() {
        assertEquals(7, KSpeedUnit.entries.size)
    }
}
