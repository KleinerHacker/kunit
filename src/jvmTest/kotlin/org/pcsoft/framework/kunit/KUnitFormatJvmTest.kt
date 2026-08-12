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

import org.pcsoft.framework.kunit.formatter.KConsoleUnitFormatter
import org.pcsoft.framework.kunit.formatter.KLocale
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

/** The `java.util.Locale` overloads of the formatting verbs, which live in the JVM source set. */
class KUnitFormatJvmTest {

    /** `format` with a java.util.Locale matches the common overload with the equivalent KLocale. */
    @Test
    fun `format with java locale`() {
        val v = 3 of meters / seconds
        assertEquals("10,8 km/h", v.format(kilo.meters / hours, "%.1f", Locale.GERMANY))
        assertEquals(
            v.format(kilo.meters / hours, "%.1f", KLocale.EN_US),
            v.format(kilo.meters / hours, "%.1f", Locale.US),
        )
    }

    /** The formatter argument is forwarded unchanged. */
    @Test
    fun `format with java locale and custom formatter`() {
        val v = 3 of meters / seconds
        assertEquals(
            v.format(kilo.meters / hours, "%.1f", KLocale.EN_US, KConsoleUnitFormatter()),
            v.format(kilo.meters / hours, "%.1f", Locale.US, KConsoleUnitFormatter()),
        )
    }

    /** `toString` with a java.util.Locale renders the value in its own base units. */
    @Test
    fun `toString with java locale`() {
        assertEquals("1.500,00 m", (1500 of meters).toString("%,.2f", Locale.GERMANY))
        assertEquals("1,500.00 m", (1500 of meters).toString("%,.2f", Locale.US))
        assertEquals(
            (1500 of meters).toString("%,.2f", KLocale.EN_US, KConsoleUnitFormatter()),
            (1500 of meters).toString("%,.2f", Locale.US, KConsoleUnitFormatter()),
        )
    }
}
