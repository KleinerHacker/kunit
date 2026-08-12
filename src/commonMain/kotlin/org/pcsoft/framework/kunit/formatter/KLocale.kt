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

/**
 * The multiplatform replacement for `java.util.Locale` in the formatting layer: the handful of numeric
 * conventions a [KUnitFormatter] actually needs, and nothing else.
 *
 * Kotlin has no common locale API - `java.util.Locale` and `String.format` exist on the JVM only - so the
 * library carries its own, deliberately minimal description of how a number is written down. It covers the
 * decimal separator, the grouping (thousands) separator and the grouping sizes; everything else that a full
 * locale implies (collation, calendars, translated unit names) is out of scope, because a unit symbol like
 * `km/h` is not translated.
 *
 * Because the conventions are carried by the value itself, formatting is **identical on every target** -
 * JVM, JS, Wasm and native produce the same string for the same [KLocale]. On the JVM a
 * `java.util.Locale` can be converted with `toKLocale()`.
 *
 * The predefined constants of the [Companion] cover the common cases; any other convention is expressible by
 * constructing a [KLocale] directly.
 *
 * Example:
 * ```kotlin
 * (1500 of meters).toString("%,.2f", KLocale.DE_DE) // "1.500,00 m"
 * (1500 of meters).toString("%,.2f", KLocale.EN_US) // "1,500.00 m"
 * ```
 *
 * @property tag the BCP-47 language tag this locale describes (e.g. `"de-DE"`); purely descriptive.
 * @property decimalSeparator the character separating the integer from the fraction part.
 * @property groupingSeparator the character separating groups of integer digits (used only when the
 * format pattern requests grouping via the `,` flag).
 * @property groupingSize the number of digits in the group closest to the decimal separator.
 * @property secondaryGroupingSize the number of digits in every further group; differs from [groupingSize]
 * only in the Indian numbering system (3 then 2, e.g. `12,34,567`). Defaults to [groupingSize].
 */
data class KLocale(
    val tag: String,
    val decimalSeparator: Char,
    val groupingSeparator: Char,
    val groupingSize: Int = 3,
    val secondaryGroupingSize: Int = groupingSize,
) {
    init {
        require(groupingSize > 0) { "groupingSize must be positive, was $groupingSize" }
        require(secondaryGroupingSize > 0) { "secondaryGroupingSize must be positive, was $secondaryGroupingSize" }
    }

    /** The predefined locales shipped with the library. */
    companion object {
        /**
         * The locale-independent default: `.` as decimal separator, `,` as grouping separator. Used
         * whenever no locale is given, so unqualified formatting is deterministic on every platform
         * (unlike `java.util.Locale.getDefault()`, which depends on the machine).
         */
        val ROOT: KLocale = KLocale("", '.', ',')

        /** English (United States): `1,234.56`. */
        val EN_US: KLocale = KLocale("en-US", '.', ',')

        /** English (United Kingdom): `1,234.56`. */
        val EN_GB: KLocale = KLocale("en-GB", '.', ',')

        /** German (Germany): `1.234,56`. */
        val DE_DE: KLocale = KLocale("de-DE", ',', '.')

        /** French (France): `1 234,56` (narrow no-break space as grouping separator). */
        val FR_FR: KLocale = KLocale("fr-FR", ',', ' ')

        /** Spanish (Spain): `1.234,56`. */
        val ES_ES: KLocale = KLocale("es-ES", ',', '.')

        /** Italian (Italy): `1.234,56`. */
        val IT_IT: KLocale = KLocale("it-IT", ',', '.')

        /** Portuguese (Brazil): `1.234,56`. */
        val PT_BR: KLocale = KLocale("pt-BR", ',', '.')

        /** Dutch (Netherlands): `1.234,56`. */
        val NL_NL: KLocale = KLocale("nl-NL", ',', '.')

        /** Russian (Russia): `1 234,56` (no-break space as grouping separator). */
        val RU_RU: KLocale = KLocale("ru-RU", ',', ' ')

        /** Japanese (Japan): `1,234.56`. */
        val JA_JP: KLocale = KLocale("ja-JP", '.', ',')

        /** Chinese (China): `1,234.56`. */
        val ZH_CN: KLocale = KLocale("zh-CN", '.', ',')

        /** Korean (Korea): `1,234.56`. */
        val KO_KR: KLocale = KLocale("ko-KR", '.', ',')

        /**
         * Arabic (Saudi Arabia) in the Latin numbering system (`ar-SA-u-nu-latn`): `1,234.56`. The library
         * never substitutes digit glyphs, so the Arabic-Indic numbering system is intentionally not modelled.
         */
        val AR_SA: KLocale = KLocale("ar-SA", '.', ',')

        /**
         * Hindi (India): `12,34,567.89` - the Indian numbering system groups the last three digits and
         * every further **two** digits (see [secondaryGroupingSize]).
         */
        val HI_IN: KLocale = KLocale("hi-IN", '.', ',', groupingSize = 3, secondaryGroupingSize = 2)
    }
}
