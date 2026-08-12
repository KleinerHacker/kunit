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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals

/**
 * Every predefined [KLocale] constant, asserted individually (a constant that is merely class-loaded but
 * never read would count as covered without being tested), plus the constructor's own guarantees.
 */
class KLocaleTest {

    /** The locale-independent default: dot decimal, comma grouping, no language tag. */
    @Test
    fun `ROOT`() {
        assertEquals("", KLocale.ROOT.tag)
        assertEquals('.', KLocale.ROOT.decimalSeparator)
        assertEquals(',', KLocale.ROOT.groupingSeparator)
        assertEquals(3, KLocale.ROOT.groupingSize)
        assertEquals(3, KLocale.ROOT.secondaryGroupingSize)
    }

    /** English (United States). */
    @Test
    fun `EN_US`() {
        assertEquals("en-US", KLocale.EN_US.tag)
        assertEquals('.', KLocale.EN_US.decimalSeparator)
        assertEquals(',', KLocale.EN_US.groupingSeparator)
        assertEquals(3, KLocale.EN_US.groupingSize)
    }

    /** English (United Kingdom). */
    @Test
    fun `EN_GB`() {
        assertEquals("en-GB", KLocale.EN_GB.tag)
        assertEquals('.', KLocale.EN_GB.decimalSeparator)
        assertEquals(',', KLocale.EN_GB.groupingSeparator)
    }

    /** German (Germany): comma decimal, dot grouping. */
    @Test
    fun `DE_DE`() {
        assertEquals("de-DE", KLocale.DE_DE.tag)
        assertEquals(',', KLocale.DE_DE.decimalSeparator)
        assertEquals('.', KLocale.DE_DE.groupingSeparator)
    }

    /** French (France): comma decimal, narrow no-break space (U+202F) grouping. */
    @Test
    fun `FR_FR`() {
        assertEquals("fr-FR", KLocale.FR_FR.tag)
        assertEquals(',', KLocale.FR_FR.decimalSeparator)
        assertEquals('\u202F', KLocale.FR_FR.groupingSeparator)
    }

    /** Spanish (Spain). */
    @Test
    fun `ES_ES`() {
        assertEquals("es-ES", KLocale.ES_ES.tag)
        assertEquals(',', KLocale.ES_ES.decimalSeparator)
        assertEquals('.', KLocale.ES_ES.groupingSeparator)
    }

    /** Italian (Italy). */
    @Test
    fun `IT_IT`() {
        assertEquals("it-IT", KLocale.IT_IT.tag)
        assertEquals(',', KLocale.IT_IT.decimalSeparator)
        assertEquals('.', KLocale.IT_IT.groupingSeparator)
    }

    /** Portuguese (Brazil). */
    @Test
    fun `PT_BR`() {
        assertEquals("pt-BR", KLocale.PT_BR.tag)
        assertEquals(',', KLocale.PT_BR.decimalSeparator)
        assertEquals('.', KLocale.PT_BR.groupingSeparator)
    }

    /** Dutch (Netherlands). */
    @Test
    fun `NL_NL`() {
        assertEquals("nl-NL", KLocale.NL_NL.tag)
        assertEquals(',', KLocale.NL_NL.decimalSeparator)
        assertEquals('.', KLocale.NL_NL.groupingSeparator)
    }

    /** Russian (Russia): comma decimal, no-break space (U+00A0) grouping. */
    @Test
    fun `RU_RU`() {
        assertEquals("ru-RU", KLocale.RU_RU.tag)
        assertEquals(',', KLocale.RU_RU.decimalSeparator)
        assertEquals('\u00A0', KLocale.RU_RU.groupingSeparator)
    }

    /** Japanese (Japan). */
    @Test
    fun `JA_JP`() {
        assertEquals("ja-JP", KLocale.JA_JP.tag)
        assertEquals('.', KLocale.JA_JP.decimalSeparator)
        assertEquals(',', KLocale.JA_JP.groupingSeparator)
    }

    /** Chinese (China). */
    @Test
    fun `ZH_CN`() {
        assertEquals("zh-CN", KLocale.ZH_CN.tag)
        assertEquals('.', KLocale.ZH_CN.decimalSeparator)
        assertEquals(',', KLocale.ZH_CN.groupingSeparator)
    }

    /** Korean (Korea). */
    @Test
    fun `KO_KR`() {
        assertEquals("ko-KR", KLocale.KO_KR.tag)
        assertEquals('.', KLocale.KO_KR.decimalSeparator)
        assertEquals(',', KLocale.KO_KR.groupingSeparator)
    }

    /** Arabic (Saudi Arabia) in the Latin numbering system. */
    @Test
    fun `AR_SA`() {
        assertEquals("ar-SA", KLocale.AR_SA.tag)
        assertEquals('.', KLocale.AR_SA.decimalSeparator)
        assertEquals(',', KLocale.AR_SA.groupingSeparator)
    }

    /** Hindi (India): the Indian numbering system groups 3 then 2 digits. */
    @Test
    fun `HI_IN`() {
        assertEquals("hi-IN", KLocale.HI_IN.tag)
        assertEquals('.', KLocale.HI_IN.decimalSeparator)
        assertEquals(',', KLocale.HI_IN.groupingSeparator)
        assertEquals(3, KLocale.HI_IN.groupingSize)
        assertEquals(2, KLocale.HI_IN.secondaryGroupingSize)
    }

    /** The secondary grouping size defaults to the primary one. */
    @Test
    fun `secondary grouping size defaults`() {
        assertEquals(4, KLocale("xx", '.', ',', groupingSize = 4).secondaryGroupingSize)
    }

    /** Non-positive grouping sizes are rejected. */
    @Test
    fun `grouping sizes must be positive`() {
        assertFailsWith<IllegalArgumentException> { KLocale("xx", '.', ',', groupingSize = 0) }
        assertFailsWith<IllegalArgumentException> {
            KLocale("xx", '.', ',', groupingSize = 3, secondaryGroupingSize = 0)
        }
    }

    /** Value semantics: two locales are equal exactly when all their conventions match. */
    @Test
    fun `value semantics`() {
        assertEquals(KLocale.EN_US, KLocale("en-US", '.', ','))
        assertEquals(KLocale.EN_US.hashCode(), KLocale("en-US", '.', ',').hashCode())
        assertNotEquals(KLocale.EN_US, KLocale.DE_DE)
    }
}
