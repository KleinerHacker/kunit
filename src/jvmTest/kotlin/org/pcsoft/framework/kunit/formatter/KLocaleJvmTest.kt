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

package org.pcsoft.framework.kunit.formatter

import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

/** The JVM bridge from `java.util.Locale` to [KLocale]. */
class KLocaleJvmTest {

    /** The US locale maps to dot decimal / comma grouping. */
    @Test
    fun `us locale`() {
        val locale = Locale.US.toKLocale()
        assertEquals("en-US", locale.tag)
        assertEquals('.', locale.decimalSeparator)
        assertEquals(',', locale.groupingSeparator)
        assertEquals(3, locale.groupingSize)
    }

    /** The German locale maps to comma decimal / dot grouping. */
    @Test
    fun `german locale`() {
        val locale = Locale.GERMANY.toKLocale()
        assertEquals("de-DE", locale.tag)
        assertEquals(',', locale.decimalSeparator)
        assertEquals('.', locale.groupingSeparator)
    }

    /** The secondary grouping size is not derivable from java.text and mirrors the primary one. */
    @Test
    fun `secondary grouping size mirrors the primary one`() {
        val locale = Locale.forLanguageTag("hi-IN").toKLocale()
        assertEquals(locale.groupingSize, locale.secondaryGroupingSize)
    }

    /** The converted locale drives the common formatter, producing the same output as its KLocale twin. */
    @Test
    fun `converted locale formats like its KLocale twin`() {
        assertEquals(
            formatNumber(1234.5, "%,.2f", KLocale.DE_DE),
            formatNumber(1234.5, "%,.2f", Locale.GERMANY.toKLocale()),
        )
    }
}
