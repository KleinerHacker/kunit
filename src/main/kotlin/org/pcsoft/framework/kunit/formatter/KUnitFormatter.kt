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

import org.pcsoft.framework.kunit.KUnitTerm
import java.util.Locale

/**
 * Everything a [KUnitFormatter] needs to render one measured value as text: the numeric [value] **already
 * expressed in the target unit(s)** together with the [units] describing that target dimension, plus the
 * optional number [pattern] and [locale].
 *
 * A context is created by the `format` verb (`v format kilo.meters / hours` builds it from the value read
 * into `km/h` and the target's terms) and by the value-formatting `toString` overload (it uses the value's
 * own base units). It is passed to a [KUnitFormatter] as its **single** argument so the formatter surface
 * can grow additively (new fields get defaults) without breaking third-party implementations.
 *
 * The [units] terms carry their cosmetic [KUnitTerm.display] metadata, so a formatter can render the
 * written-down symbol (`"km"`, `"h"`) via [displaySymbol] rather than the group base symbol.
 *
 * @property value the numeric magnitude, already converted into the target unit(s).
 * @property units the target dimension's terms (with display metadata); empty for a dimensionless value.
 * @property pattern an optional [java.util.Formatter] pattern applied to [value] only (e.g. `"%.1f"`);
 * `null` renders the plain [Double.toString].
 * @property locale the locale used when [pattern] is applied (decimal separator etc.).
 */
data class KUnitFormatContext(
    val value: Double,
    val units: List<KUnitTerm>,
    val pattern: String? = null,
    val locale: Locale = Locale.getDefault(),
)

/**
 * Renders [KUnitFormatContext.value] as a string: the plain [Double.toString] when
 * [KUnitFormatContext.pattern] is `null`, otherwise [String.format] with the context's
 * [KUnitFormatContext.locale] and pattern.
 *
 * This is a ready-made building block for custom [KUnitFormatter]s that only want to change how the units
 * are rendered while keeping the standard number formatting.
 *
 * @throws java.util.IllegalFormatException if [KUnitFormatContext.pattern] is not a valid format string.
 */
fun KUnitFormatContext.renderValue(): String {
    if (pattern == null) return value.toString()
    return String.format(locale, pattern, value)
}

/**
 * The rendered unit symbol of this term, honouring its cosmetic [KUnitTerm.display] metadata: the
 * written-down symbol (`"km"`, `"h"`) when a display is present, otherwise the group base unit's symbol
 * (`"m"`, `"s"`). The exponent is **not** included - a formatter decides how to render it.
 */
val KUnitTerm.displaySymbol: String get() = if (display == null) unit.symbol else display.symbol

/**
 * The extension point for turning a measured value into text. Implement it to plug a completely custom
 * rendering into both the `format` verb and the value-formatting `toString` overload - for example to emit
 * LaTeX/MathML for a graphical formula renderer, HTML, or a domain-specific notation.
 *
 * An implementation receives everything it needs in a single [KUnitFormatContext] (value, target units
 * with prefix/exponent display metadata, number pattern, locale) and returns the finished string. The
 * shipped [KDefaultUnitFormatter] produces plain text such as `"10.8 km/h"`; a custom formatter is passed
 * explicitly (`v.format(target, null, formatter = LatexFormatter)`), so the default behaviour never changes
 * unless asked for.
 *
 * Implementations should be **stateless** (or immutable) and therefore thread-safe (like
 * [KDefaultUnitFormatter], an immutable `class`); the reusable helpers [renderValue] and [displaySymbol]
 * cover the common building blocks.
 *
 * Example:
 * ```kotlin
 * object LatexFormatter : KUnitFormatter {
 *     override fun format(context: KUnitFormatContext): String {
 *         val (num, den) = context.units.partition { it.exponent > 0 }
 *         // ... assemble \frac{...}{...} from context.displaySymbol values ...
 *     }
 * }
 * val s = (3 of meters / seconds).format(kilo.meters / hours, null, formatter = LatexFormatter)
 * ```
 */
interface KUnitFormatter {
    /** Renders [context] into its final string form. */
    fun format(context: KUnitFormatContext): String
}
