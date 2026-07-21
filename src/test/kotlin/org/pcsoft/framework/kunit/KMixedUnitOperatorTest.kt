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

package org.pcsoft.framework.kunit

import org.pcsoft.framework.kunit.acceleration.standardGravities
import org.pcsoft.framework.kunit.areadensity.KAreaDensityUnitInstance
import org.pcsoft.framework.kunit.areadensity.div
import org.pcsoft.framework.kunit.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.distance.KLengthUnitInstance
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.force.KForceUnitInstance
import org.pcsoft.framework.kunit.force.newtons
import org.pcsoft.framework.kunit.force.times
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.speed.KSpeedUnitInstance
import org.pcsoft.framework.kunit.speed.div
import org.pcsoft.framework.kunit.storage.KStorageUnit
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.time.seconds
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

/** The cross-group `*`/`/` operators (generic vs typed) and mixed `+` dimension checks. */
class KMixedUnitOperatorTest {

    /** Two unrelated groups combine via the generic cross-group `/` into a KMixedUnitInstance. */
    @Test
    fun `generic cross-group div gives mixed`() {
        val mixed = (20 of bytes) / (20 of meters)
        assertIs<KMixedUnitInstance>(mixed)
        assertEquals(1.0, mixed.value, 1e-9)
        assertTrue(mixed.hasSameUnits(KMixedUnitInstance(1.0, listOf(KUnitTerm(KStorageUnit.BASE, 1), KUnitTerm(KDistanceUnit.BASE, -1)))))
    }

    /** The typed speed extension still wins over the generic operator (`length / time = speed`). */
    @Test
    fun `typed speed operator wins`() {
        val v = (100 of meters) / (5 of seconds)
        assertIs<KSpeedUnitInstance>(v)
        assertEquals(20.0, v.value, 1e-9)
    }

    /** The typed force extension wins over the generic operator (`mass * acceleration = force`). */
    @Test
    fun `typed force operator wins`() {
        val f = (2 of kilo.grams) * (1 of standardGravities)
        assertIs<KForceUnitInstance>(f)
        assertEquals(2.0 * 9.80665, f into newtons, 1e-9)
    }

    /** The typed area-density extension wins over the generic operator (`mass / area = area density`). */
    @Test
    fun `typed area density operator wins`() {
        val q = (10 of kilo.grams) / ((5 of meters) * (1 of meters))
        assertIs<KAreaDensityUnitInstance>(q)
    }

    /** The member `length * length` still wins and returns a typed area. */
    @Test
    fun `member area operator wins`() {
        val area = (3 of meters) * (4 of meters)
        assertIs<KAreaUnitInstance>(area)
        assertEquals(12.0, area.value, 1e-9)
    }

    /** Mixed `+` requires matching dimensions; unrelated shapes fail. */
    @Test
    fun `mixed plus dimension check`() {
        val a = (10 of meters) / (2 of seconds) // m/s
        val b = (4 of meters) / (1 of seconds)  // m/s
        assertEquals(9.0, (a.toUnit() + b.toUnit()).value, 1e-9)
        assertFailsWith<IllegalStateException> { (10 of meters).toUnit() + (2 of seconds).toUnit() }
    }

    /** Two unrelated groups combine via the generic cross-group `*` into a KMixedUnitInstance. */
    @Test
    fun `generic cross-group times gives mixed`() {
        val mixed = (20 of bytes) * (20 of meters)
        assertIs<KMixedUnitInstance>(mixed)
        assertEquals(400.0, mixed.value, 1e-9)
        assertTrue(mixed.hasSameUnits(KMixedUnitInstance(1.0, listOf(KUnitTerm(KStorageUnit.BASE, 1), KUnitTerm(KDistanceUnit.BASE, 1)))))
    }

    /** Mixed `-` and `pow 0` (dimensionless). */
    @Test
    fun `mixed minus and pow zero`() {
        val a = ((10 of meters) / (2 of seconds)).toUnit() // 5 m/s
        val b = ((4 of meters) / (1 of seconds)).toUnit()  // 4 m/s
        assertEquals(1.0, (a - b).value, 1e-9)
        val dimensionless = (2 of meters).toUnit() pow 0
        assertEquals(1.0, dimensionless.value, 1e-9)
        assertTrue(dimensionless.units.isEmpty())
    }

    /** Mixed `+` with a different number of terms fails the dimension check. */
    @Test
    fun `mixed plus size mismatch fails`() {
        val speed = ((10 of meters) / (2 of seconds)).toUnit() // 2 terms
        val length = (3 of meters).toUnit()                    // 1 term
        assertFailsWith<IllegalStateException> { speed + length }
    }

    /** Scalar `unit * n` / `n * unit` keeps the dimension and the typed leaf (length stays length). */
    @Test
    fun `scalar times keeps typed dimension`() {
        val tripled = (12 of meters) * 3
        assertIs<KLengthUnitInstance>(tripled)
        assertEquals(36.0, tripled.value, 1e-9)

        val commuted = 3 * (12 of meters)
        assertIs<KLengthUnitInstance>(commuted)
        assertEquals(36.0, commuted.value, 1e-9)
    }

    /** Scalar `unit / n` scales the magnitude down while keeping the typed leaf. */
    @Test
    fun `scalar div keeps typed dimension`() {
        val leg = (10 of kilo.meters) / 4
        assertIs<KLengthUnitInstance>(leg)
        assertEquals(2500.0, leg.value, 1e-9) // 2.5 km in meters
    }

    /** Real-world case: circle area π·r² built purely through the unit system stays a typed area. */
    @Test
    fun `circle area via scalar operators`() {
        val r = 12 of centi.meters                // 0.12 m
        val area = Math.PI * (r * r)              // KAreaUnitInstance
        assertIs<KAreaUnitInstance>(area)
        assertEquals(Math.PI * 0.0144, area.value, 1e-12)
        assertEquals(Math.PI * 0.0144, area into (meters * meters), 1e-12)
    }

    /** `n / unit` inverts the dimension and degrades to a generic mixed unit (e.g. a frequency). */
    @Test
    fun `scalar over unit inverts dimension`() {
        val frequency = 1 / (2 of seconds)
        assertIs<KMixedUnitInstance>(frequency)
        assertEquals(0.5, frequency.value, 1e-9)
        assertTrue(frequency.hasSameUnits(KMixedUnitInstance(1.0, listOf(KUnitTerm(KTimeUnit.BASE, -1)))))
    }

    /** Scalar scaling of an open mixed unit leaves its terms untouched, scaling only the value. */
    @Test
    fun `scalar scaling of open mixed unit`() {
        val mixed = (20 of bytes) * (20 of meters) // open KMixedUnitInstance, value=400
        val scaled = mixed * 3
        assertIs<KMixedUnitInstance>(scaled)
        assertEquals(1200.0, scaled.value, 1e-9)
        assertTrue(scaled.hasSameUnits(mixed))

        val halved = mixed / 2
        assertEquals(200.0, halved.value, 1e-9)
        assertTrue(halved.hasSameUnits(mixed))
    }

    /** Structural equality and hashCode of the generic mixed unit. */
    @Test
    fun `mixed equals and hashCode`() {
        assertEquals((10 of meters).toUnit(), (10 of meters).toUnit())
        assertEquals((10 of meters).toUnit().hashCode(), (10 of meters).toUnit().hashCode())
        assertFalse((10 of meters).toUnit().equals(1.0))                            // not a KMixedUnitInstance
        assertNotEquals((10 of meters).toUnit(), (20 of meters).toUnit())           // different value
        assertNotEquals((10 of meters).toUnit(), (10 of seconds).toUnit())          // same value, different units
    }
}
