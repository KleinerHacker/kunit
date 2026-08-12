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
import kotlin.math.max

/**
 * A [KUnitFormatter] that renders a value **graphically over several lines** for an ANSI-capable terminal:
 * a fraction is drawn as a real two-dimensional stack - numerator, a horizontal bar, denominator - with the
 * value on the bar (middle) line, and exponents are always set as real Unicode superscript digits. Every
 * visual role is coloured through a [KGraphicalConsoleColorPalette].
 *
 * With the default configuration `9.81 of meters / (seconds pow 2)` renders (uncoloured) as:
 * ```
 *      m
 * 9.81 ──
 *      s²
 * ```
 *
 * The layout follows the shared rules: a clean single-denominator shape (some numerator, exactly one
 * denominator) is stacked; every other shape is a single-line product joined by the configured
 * multiplication sign with superscript exponents; a dimensionless value is the coloured number alone. The
 * numerator and denominator are centred over the bar using their **visible** width (ANSI colour sequences
 * do not count towards the width).
 *
 * The instance is immutable and therefore thread-safe. Construct it without arguments for
 * [KGraphicalConsoleFormatConfig.DEFAULT] or pass a [KGraphicalConsoleFormatConfig].
 *
 * @property config the palette, fraction bar, multiplication sign and function symbols.
 */
class KGraphicalConsoleUnitFormatter(
    val config: KGraphicalConsoleFormatConfig = KGraphicalConsoleFormatConfig.DEFAULT,
) : KUnitFormatter {

    /** A rendered fragment together with its visible width (excluding ANSI escape sequences). */
    private class Cell(val colored: String, val width: Int)

    override fun format(context: KUnitFormatContext): String {
        val numberVisible = context.renderValue()
        val numberColored = wrap(config.palette.numberColor, numberVisible)
        val units = context.units
        if (units.isEmpty()) return numberColored

        val positives = units.filter { it.exponent > 0 }
        val negatives = units.filter { it.exponent < 0 }
        if (positives.isNotEmpty() && negatives.size == 1) {
            return renderFraction(numberColored, numberVisible.length, positives, negatives.single())
        }
        return numberColored + " " + joinTerms(units).colored
    }

    /** Wraps [text] in [code] + reset; an empty [code] leaves [text] uncoloured. */
    private fun wrap(code: String, text: String): String =
        if (code.isEmpty()) text else code + text + config.palette.reset

    /** A single term as a coloured cell: symbol plus a coloured superscript exponent when not `1`. */
    private fun termCell(symbol: String, exponent: Int): Cell {
        var colored = wrap(config.palette.symbolColor, symbol)
        var width = symbol.length
        if (exponent != 1) {
            val sup = renderSuperscriptExponent(exponent)
            colored += wrap(config.palette.exponentColor, sup)
            width += sup.length
        }
        return Cell(colored, width)
    }

    /** Joins the terms into one cell, separated by the coloured multiplication sign. */
    private fun joinTerms(terms: List<KUnitTerm>): Cell {
        val op = config.multiplication.symbol
        val opColored = wrap(config.palette.operatorColor, op)
        val builder = StringBuilder()
        var width = 0
        terms.forEachIndexed { index, t ->
            if (index > 0) {
                builder.append(opColored)
                width += op.length
            }
            val cell = termCell(t.displaySymbol, t.exponent)
            builder.append(cell.colored)
            width += cell.width
        }
        return Cell(builder.toString(), width)
    }

    private fun renderFraction(
        numberColored: String,
        numberWidth: Int,
        positives: List<KUnitTerm>,
        denominator: KUnitTerm,
    ): String {
        val numerator = joinTerms(positives)
        val denom = termCell(denominator.displaySymbol, -denominator.exponent)
        val barWidth = max(numerator.width, denom.width)
        val bar = wrap(config.palette.barColor, config.fractionBar.symbol.repeat(barWidth))
        val indent = " ".repeat(numberWidth + 1)
        val top = indent + " ".repeat((barWidth - numerator.width) / 2) + numerator.colored
        val middle = "$numberColored $bar"
        val bottom = indent + " ".repeat((barWidth - denom.width) / 2) + denom.colored
        return "$top\n$middle\n$bottom"
    }
}
