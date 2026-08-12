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

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*

/**
 * Derives the [KLocale] matching this [java.util.Locale]: decimal separator, grouping separator and
 * grouping size are read from the platform's locale data, so JVM code can keep using `java.util.Locale`
 * while the formatting itself runs on the common implementation.
 *
 * The Indian secondary grouping (3 then 2 digits) is not exposed by [DecimalFormat] and therefore not
 * derived here; use [KLocale.HI_IN] (or construct a [KLocale] directly) when it is needed.
 *
 * Example:
 * ```kotlin
 * (1500 of meters).toString("%,.2f", Locale.GERMANY.toKLocale()) // "1.500,00 m"
 * ```
 */
fun Locale.toKLocale(): KLocale {
    val symbols = DecimalFormatSymbols.getInstance(this)
    // Every locale the JDK ships resolves to a DecimalFormat here.
    val groupingSize = (NumberFormat.getIntegerInstance(this) as DecimalFormat).groupingSize
    return KLocale(
        tag = toLanguageTag(),
        decimalSeparator = symbols.decimalSeparator,
        groupingSeparator = symbols.groupingSeparator,
        groupingSize = groupingSize,
    )
}
