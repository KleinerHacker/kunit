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

import org.pcsoft.framework.kunit.formatter.KDefaultUnitFormatter
import org.pcsoft.framework.kunit.formatter.KUnitFormatter
import org.pcsoft.framework.kunit.formatter.toKLocale
import java.util.*

/**
 * Reads this value in [target] and renders it with a number [pattern] under a `java.util.Locale` - the
 * JVM convenience overload of the common [format], which takes a
 * [org.pcsoft.framework.kunit.formatter.KLocale].
 *
 * The [locale] is converted with [toKLocale] and the rendering then runs on the common implementation, so
 * the result is identical to the common overload's.
 *
 * Unlike the common overload, [locale] has **no default** - omitting it selects the common overload (and
 * with it [org.pcsoft.framework.kunit.formatter.KLocale.ROOT]).
 *
 * @throws IllegalStateException if [target] does not describe the same physical dimension as this value.
 * @throws IllegalArgumentException if [pattern] is not a valid number pattern.
 *
 * Example:
 * ```kotlin
 * val v = 3 of meters / seconds
 * v.format(kilo.meters / hours, "%.1f", Locale.GERMANY) // "10,8 km/h"
 * ```
 */
fun KUnitMeasurable.format(
    target: KUnitMeasurable,
    pattern: String?,
    locale: Locale,
    formatter: KUnitFormatter = KDefaultUnitFormatter(),
): String = format(target, pattern, locale.toKLocale(), formatter)

/**
 * Renders this value in its own **base unit(s)** with a number [pattern] under a `java.util.Locale` - the
 * JVM convenience overload of the common [toString].
 *
 * The [locale] is converted with [toKLocale]; see the common overload for the rendering rules. Unlike it,
 * [locale] has **no default**.
 *
 * @throws IllegalArgumentException if [pattern] is not a valid number pattern.
 *
 * Example:
 * ```kotlin
 * (1500 of meters).toString("%,.2f", Locale.GERMANY) // "1.500,00 m"
 * ```
 */
fun KUnitMeasurable.toString(
    pattern: String?,
    locale: Locale,
    formatter: KUnitFormatter = KDefaultUnitFormatter(),
): String = toString(pattern, locale.toKLocale(), formatter)
