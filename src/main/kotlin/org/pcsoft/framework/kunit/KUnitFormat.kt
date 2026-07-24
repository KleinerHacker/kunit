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
import org.pcsoft.framework.kunit.formatter.KUnitFormatContext
import org.pcsoft.framework.kunit.formatter.KUnitFormatter
import java.util.Locale

/**
 * Reads this value in the unit described by [target] and renders it - value **and** unit symbol - as a
 * string: the display counterpart of [into] (which returns only the plain [Double]). The one and only
 * formatting verb of the library.
 *
 * The value is read exactly as [into] does (same dimension check, same affine-aware reading hook), then
 * the target's unit symbol is appended by [KDefaultUnitFormatter], e.g. `"10.8 km/h"`. Because the target
 * carries its written-down [KUnitTerm.display] metadata, prefixed and alternate units render under their
 * own symbol (`km`, `h`, `mi`) rather than the group base symbol.
 *
 * For number formatting (decimals, locale) or a custom rendering, use the [format] overload with
 * `pattern`/`formatter` arguments.
 *
 * @throws IllegalStateException if [target] does not describe the same physical dimension as this value.
 *
 * Example:
 * ```kotlin
 * val v = 3 of meters / seconds
 * v format kilo.meters / hours       // "10.8 km/h"
 * (1500 of meters) format kilo.meters // "1.5 km"
 * ```
 */
infix fun KUnitMeasurable.format(target: KUnitMeasurable): String =
    format(target, null, Locale.getDefault(), KDefaultUnitFormatter())

/**
 * Reads this value in [target] and renders it with an optional number [pattern]/[locale] and an optional
 * [formatter]. The options-carrying counterpart of the infix [format] verb.
 *
 * [pattern] is a [java.util.Formatter] pattern applied to the **numeric part only** (e.g. `"%.1f"`);
 * `null` keeps the plain [Double.toString]. [formatter] swaps the rendering entirely (e.g. a LaTeX
 * formatter) while still receiving the value already read into [target].
 *
 * @throws IllegalStateException if [target] does not describe the same physical dimension as this value.
 * @throws java.util.IllegalFormatException if [pattern] is not a valid format string.
 *
 * Example:
 * ```kotlin
 * val v = 3 of meters / seconds
 * v.format(kilo.meters / hours, "%.1f")                 // "10.8 km/h"
 * v.format(kilo.meters / hours, "%.1f", Locale.GERMAN)  // "10,8 km/h"
 * v.format(kilo.meters / hours, null, formatter = LatexFormatter)
 * ```
 */
fun KUnitMeasurable.format(
    target: KUnitMeasurable,
    pattern: String?,
    locale: Locale = Locale.getDefault(),
    formatter: KUnitFormatter = KDefaultUnitFormatter(),
): String {
    val reading = this into target
    return formatter.format(KUnitFormatContext(reading, target.toUnit().units, pattern, locale))
}

/**
 * Renders this value in its own **base unit(s)** with an optional number [pattern]/[locale] and an
 * optional [formatter] - the parameterised counterpart of the plain [toString]. It is the [format] verb
 * without a target: the value is not converted, only rendered.
 *
 * The no-argument [toString] stays exactly as before; this overload only adds number formatting and the
 * pluggable [formatter]. For a composed/standardized unit (speed, force, ...) it renders the base-dimension
 * form (e.g. `"m/s"`), which may differ from that type's own symbolic [toString] (`"N"`).
 *
 * @throws java.util.IllegalFormatException if [pattern] is not a valid format string.
 *
 * Example:
 * ```kotlin
 * (3 of meters / seconds).toString("%.2f")               // "3.00 m/s"
 * (1500 of meters).toUnit().toString("%.1f")             // "1500.0 m"
 * ```
 */
fun KUnitMeasurable.toString(
    pattern: String?,
    locale: Locale = Locale.getDefault(),
    formatter: KUnitFormatter = KDefaultUnitFormatter(),
): String {
    val self = toUnit()
    return formatter.format(KUnitFormatContext(self.value, self.units, pattern, locale))
}
