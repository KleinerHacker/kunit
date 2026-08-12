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

import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToLong

/**
 * The multiplatform number formatter behind [renderValue] - the replacement for the JVM-only
 * `String.format`, implemented from the Kotlin standard library alone so every target produces the very
 * same string.
 *
 * Supported pattern grammar (a printf subset applied to the **single** numeric value):
 *
 * ```
 * %[flags][width][.precision]conversion
 * flags       -  '-' left-justify | '+' always sign | ' ' space for positive | '0' zero-pad | ',' group
 * width       -  minimum total number of characters
 * precision   -  number of fraction digits (conversions f, e, E)
 * conversion  -  'f' fixed | 'e'/'E' scientific | 'd' integer | 's' plain Double.toString
 * ```
 *
 * `%%` emits a literal percent sign. Literal text around the conversion is copied verbatim. The pattern
 * must contain exactly one conversion, because it is applied to exactly one value.
 *
 * Two deliberate deviations from `String.format`:
 * * an invalid pattern raises [IllegalArgumentException] (there is no common `IllegalFormatException`);
 * * for magnitudes beyond `Double`'s exact integer range the digits come from [Double.toString] (the
 *   shortest round-trip representation) padded with zeros, rather than from the exact binary expansion -
 *   `6.022e23` renders as `602200000000000000000000` instead of `601999999999999975882752`.
 */
internal fun formatNumber(value: Double, pattern: String, locale: KLocale): String {
    val result = StringBuilder()
    var index = 0
    var converted = false
    while (index < pattern.length) {
        val char = pattern[index]
        if (char != '%') {
            result.append(char)
            index++
            continue
        }
        require(index + 1 < pattern.length) { "format pattern '$pattern' ends with a dangling '%'" }
        if (pattern[index + 1] == '%') {
            result.append('%')
            index += 2
            continue
        }
        require(!converted) { "format pattern '$pattern' contains more than one conversion, but only one value is formatted" }
        val spec = parseSpec(pattern, index)
        result.append(render(value, spec, locale))
        index = spec.end
        converted = true
    }
    require(converted) { "format pattern '$pattern' contains no conversion" }
    return result.toString()
}

/** One parsed `%...` conversion specification; [end] is the index just behind the conversion character. */
private class Spec(
    val leftJustify: Boolean,
    val plusSign: Boolean,
    val spaceSign: Boolean,
    val zeroPad: Boolean,
    val grouping: Boolean,
    val width: Int,
    val precision: Int?,
    val conversion: Char,
    val end: Int,
)

/** Parses the conversion specification starting at the `%` on [start]. */
private fun parseSpec(pattern: String, start: Int): Spec {
    var index = start + 1
    var leftJustify = false
    var plusSign = false
    var spaceSign = false
    var zeroPad = false
    var grouping = false
    while (index < pattern.length && pattern[index] in "-+ 0,") {
        when (pattern[index]) {
            '-' -> leftJustify = true
            '+' -> plusSign = true
            ' ' -> spaceSign = true
            '0' -> zeroPad = true
            ',' -> grouping = true
        }
        index++
    }

    val widthStart = index
    while (index < pattern.length && pattern[index].isDigit()) index++
    val width = if (index > widthStart) pattern.substring(widthStart, index).toInt() else 0

    var precision: Int? = null
    if (index < pattern.length && pattern[index] == '.') {
        index++
        val precisionStart = index
        while (index < pattern.length && pattern[index].isDigit()) index++
        require(index > precisionStart) { "format pattern '$pattern' has a '.' without precision digits" }
        precision = pattern.substring(precisionStart, index).toInt()
    }

    require(index < pattern.length) { "format pattern '$pattern' ends before its conversion character" }
    val conversion = pattern[index]
    require(conversion in "feEds") {
        "format pattern '$pattern' uses the unsupported conversion '$conversion' (supported: f, e, E, d, s)"
    }
    require(!(grouping && (conversion == 'e' || conversion == 'E'))) {
        "format pattern '$pattern' combines grouping with the scientific conversion '$conversion'"
    }
    require(!(precision != null && (conversion == 'd' || conversion == 's'))) {
        "format pattern '$pattern' sets a precision for the conversion '$conversion', which has no fraction part"
    }
    return Spec(leftJustify, plusSign, spaceSign, zeroPad, grouping, width, precision, conversion, index + 1)
}

/** Renders [value] according to [spec] and the conventions of [locale]. */
private fun render(value: Double, spec: Spec, locale: KLocale): String {
    if (spec.conversion == 's') return pad(renderDouble(value), "", spec)
    if (value.isNaN()) return pad("NaN", "", spec.withoutZeroPad())
    val negative = value < 0.0 || (value == 0.0 && 1.0 / value < 0.0)
    val sign = when {
        negative -> "-"
        spec.plusSign -> "+"
        spec.spaceSign -> " "
        else -> ""
    }
    if (value.isInfinite()) return pad("Infinity", sign, spec.withoutZeroPad())

    val magnitude = abs(value)
    val body = when (spec.conversion) {
        'f' -> fixedBody(magnitude, spec.precision ?: 6, spec.grouping, locale)
        'e', 'E' -> scientificBody(magnitude, spec.precision ?: 6, spec.conversion == 'E', locale)
        else -> integerBody(magnitude, spec.grouping, locale)
    }
    return pad(body, sign, spec)
}

/** A copy of this spec with zero-padding disabled (used for `NaN`/`Infinity`, which are never zero-padded). */
private fun Spec.withoutZeroPad(): Spec =
    Spec(leftJustify, plusSign, spaceSign, false, grouping, width, precision, conversion, end)

/** Renders [magnitude] with exactly [precision] fraction digits. */
private fun fixedBody(magnitude: Double, precision: Int, grouping: Boolean, locale: KLocale): String {
    val (integerDigits, fractionDigits) = fixedDigits(magnitude, precision)
    val integerPart = if (grouping) group(integerDigits, locale) else integerDigits
    return if (precision == 0) integerPart else integerPart + locale.decimalSeparator + fractionDigits
}

/**
 * Renders [magnitude] as `m.mmm` plus an exponent of at least two digits, e.g. `1.234e+05`.
 *
 * The mantissa is derived from the exact decimal digits rather than from `log10`/`pow`, so no rounding
 * drift at the decade boundaries can occur.
 */
private fun scientificBody(magnitude: Double, precision: Int, upperCase: Boolean, locale: KLocale): String {
    val exponent: Int
    val digits: String
    if (magnitude == 0.0) {
        exponent = 0
        digits = "0".repeat(precision + 1)
    } else {
        val (allDigits, position) = decimalParts(magnitude)
        val (rounded, carry) = roundDigits(allDigits, precision + 1)
        exponent = position - 1 + carry
        digits = rounded
    }
    val mantissaPart =
        if (precision == 0) digits else digits.substring(0, 1) + locale.decimalSeparator + digits.substring(1)
    val exponentSign = if (exponent < 0) "-" else "+"
    val exponentDigits = abs(exponent).toString().padStart(2, '0')
    return mantissaPart + (if (upperCase) "E" else "e") + exponentSign + exponentDigits
}

/**
 * Rounds the significant [digits] half-up to exactly [count] digits, padding with zeros when there are
 * fewer. The second component is `1` when the rounding carried into an additional leading digit (`999` to
 * `100` with one more decade), otherwise `0`.
 */
private fun roundDigits(digits: String, count: Int): Pair<String, Int> {
    if (digits.length <= count) return digits.padEnd(count, '0') to 0
    val kept = digits.substring(0, count)
    if (digits[count] < '5') return kept to 0
    val incremented = (kept.toLong() + 1).toString()
    return if (incremented.length > count) incremented.substring(0, count) to 1 else incremented to 0
}

/** Renders [magnitude] rounded to a whole number, without any fraction part. */
private fun integerBody(magnitude: Double, grouping: Boolean, locale: KLocale): String {
    val digits = fixedDigits(magnitude, 0).first
    return if (grouping) group(digits, locale) else digits
}

/**
 * Renders [value] the way an unformatted measured value is written down: `5.0`, `1500.0`, `10.8`,
 * `1.0E7`, `5.4E-5`.
 *
 * This is the library's canonical replacement for a bare `Double.toString()`, which is **not** portable:
 * Kotlin/JS renders `1.0` as `"1"` and switches to the exponential form at different magnitudes than the
 * JVM does. The shortest round-trip digits are taken from the platform (every target produces the same
 * ones) and re-assembled under one fixed rule, so a value renders identically on JVM, JS, Wasm and native:
 *
 * * plain notation for `1e-3 <= |value| < 1e7`, always with at least one fraction digit;
 * * scientific notation `d.dddEn` outside that range.
 */
internal fun renderDouble(value: Double): String {
    if (value.isNaN()) return "NaN"
    if (value == Double.POSITIVE_INFINITY) return "Infinity"
    if (value == Double.NEGATIVE_INFINITY) return "-Infinity"
    val sign = if (value < 0.0 || (value == 0.0 && 1.0 / value < 0.0)) "-" else ""
    if (value == 0.0) return sign + "0.0"

    val (digits, pointPosition) = decimalParts(abs(value))
    if (pointPosition in -2..7) {
        val plain = when {
            pointPosition <= 0 -> "0." + "0".repeat(-pointPosition) + digits
            pointPosition >= digits.length -> digits + "0".repeat(pointPosition - digits.length) + ".0"
            else -> digits.substring(0, pointPosition) + "." + digits.substring(pointPosition)
        }
        return sign + plain
    }
    val fraction = if (digits.length > 1) digits.substring(1) else "0"
    return "$sign${digits[0]}.${fraction}E${pointPosition - 1}"
}

/**
 * The shortest round-trip decimal digits of [magnitude] (non-negative, finite, non-zero) together with the
 * position of the decimal point relative to the first digit, so that
 * `magnitude == 0.<digits> * 10^<position>`.
 *
 * The digits come from [Double.toString], whose layout differs per platform (`"1e+21"` on JS,
 * `"1.0E21"` on the JVM, `"100000000"` on both for 1e8) - only the layout, never the digits.
 *
 * [magnitude] must be non-zero; every caller guards that case separately, because zero has no significant
 * digit to anchor the point position to.
 */
private fun decimalParts(magnitude: Double): Pair<String, Int> {
    val text = magnitude.toString().lowercase()
    val exponentIndex = text.indexOf('e')
    val mantissa = if (exponentIndex < 0) text else text.substring(0, exponentIndex)
    val exponent = if (exponentIndex < 0) 0 else text.substring(exponentIndex + 1).removePrefix("+").toInt()

    // `substringBefore`/`replace` cope with both layouts (`"1.5"` and JS's dot-less `"15"`).
    val rawDigits = mantissa.replace(".", "")
    val firstSignificant = rawDigits.indexOfFirst { it != '0' }
    val position = mantissa.substringBefore('.').length + exponent - firstSignificant
    return rawDigits.substring(firstSignificant).trimEnd('0') to position
}

/** The largest power of ten that is still exactly representable as the scaling factor used below. */
private const val MAX_SCALED = 9.0e18

/**
 * Splits [magnitude] (non-negative) into its integer and fraction digits, rounded half-up to [precision]
 * fraction digits. The fraction string always has exactly [precision] characters.
 */
private fun fixedDigits(magnitude: Double, precision: Int): Pair<String, String> {
    val scale = 10.0.pow(precision)
    val scaled = magnitude * scale
    if (precision <= 18 && scaled < MAX_SCALED) {
        val rounded = scaled.roundToLong().toString().padStart(precision + 1, '0')
        val cut = rounded.length - precision
        return rounded.substring(0, cut) to rounded.substring(cut)
    }
    // Beyond Double's exact integer range the fraction digits carry no information anyway.
    return plainDigits(magnitude) to "0".repeat(precision)
}

/** Expands [magnitude] into its plain (non-scientific) integer digits, truncated towards zero. */
private fun plainDigits(magnitude: Double): String {
    if (magnitude == 0.0) return "0"
    val (digits, position) = decimalParts(magnitude)
    return when {
        position <= 0 -> "0"
        position >= digits.length -> digits + "0".repeat(position - digits.length)
        else -> digits.substring(0, position)
    }
}

/**
 * Inserts [KLocale.groupingSeparator] into [digits]: the group closest to the decimal point has
 * [KLocale.groupingSize] digits, every further group [KLocale.secondaryGroupingSize].
 */
private fun group(digits: String, locale: KLocale): String {
    val groups = mutableListOf<String>()
    var rest = digits
    var size = locale.groupingSize
    while (rest.length > size) {
        groups.add(0, rest.substring(rest.length - size))
        rest = rest.substring(0, rest.length - size)
        size = locale.secondaryGroupingSize
    }
    groups.add(0, rest)
    return groups.joinToString(locale.groupingSeparator.toString())
}

/** Applies [sign] and the width/justification flags of [spec] to the rendered [body]. */
private fun pad(body: String, sign: String, spec: Spec): String {
    val text = sign + body
    if (text.length >= spec.width) return text
    val fill = spec.width - text.length
    return when {
        spec.leftJustify -> text + " ".repeat(fill)
        spec.zeroPad -> sign + "0".repeat(fill) + body
        else -> " ".repeat(fill) + text
    }
}
