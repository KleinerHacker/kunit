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

package org.pcsoft.framework.kunit.it.storagedensity

import org.pcsoft.framework.kunit.kinematic.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.it.storage.bits
import org.pcsoft.framework.kunit.it.storage.bytes
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

/**
 * `KStorageDensityUnitInstance` surface: a storage density is built as an expression
 * (`bytes / area`, incl. a prefixed numerator `mega.bytes / area`), typed as a storage density, and
 * read back via `into`.
 */
class KStorageDensityUnitSystemTest {

    // one square meter, statically typed as an area
    private val m2: KAreaUnitInstance = (1 of meters) * (1 of meters)

    /** Every public [KStorageDensityUnit] constant carries its documented symbol and factor to B/m². */
    @Test
    fun `unit constants`() {
        assertEquals("B/m²", KStorageDensityUnit.BYTES_PER_SQUARE_METER.symbol)
        assertEquals(1.0, KStorageDensityUnit.BYTES_PER_SQUARE_METER.baseValue, 1e-12)
        assertEquals("bit/m²", KStorageDensityUnit.BITS_PER_SQUARE_METER.symbol)
        assertEquals(0.125, KStorageDensityUnit.BITS_PER_SQUARE_METER.baseValue, 1e-12)
        assertEquals(KStorageDensityUnit.BYTES_PER_SQUARE_METER, KStorageDensityUnit.BASE)
    }

    /** Prefixed numerator, klammerfrei: `5 of mega.bytes / m2`. */
    @Test
    fun `prefixed numerator density`() {
        val a = 5 of mega.bytes / m2
        assertIs<KStorageDensityUnitInstance>(a)
        assertEquals(5e6, a.value, 1e-3)
    }

    /** Densities are built as expressions; byte/bit density reads back via `bits / area`. */
    @Test
    fun `expression densities and conversion`() {
        assertEquals(100.0, ((100 of bytes) / m2).value, 1e-9)
        assertEquals(800.0, ((100 of bytes) / m2) into (bits / m2), 1e-9)
    }

    /** Equality/hash by normalized B/m² value, and the base-unit string form. */
    @Test
    fun `equals hashCode and toString`() {
        val d = (100 of bytes) / m2
        assertEquals((100 of bytes) / m2, d)
        assertEquals(((100 of bytes) / m2).hashCode(), d.hashCode())
        assertEquals("100.0 B/m²", d.toString())
        assertNotEquals((100 of bytes) / m2, (200 of bytes) / m2)
        assertFalse(d.equals(1.0)) // not a KStorageDensityUnitInstance
    }

    /** `format` a storage density into kB/m² (generic target keeps the prefixed display symbol). */
    @Test
    fun `format compositions`() {
        assertEquals(
            "1.0 kB/m^2",
            ((1000 of bytes) / m2) format (kilo.bytes.toUnit() / m2.toUnit()),
        )
    }
}
