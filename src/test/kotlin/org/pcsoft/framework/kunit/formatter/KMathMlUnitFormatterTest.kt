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
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

/** The Presentation MathML formatter: mfrac/msup layout, mi/mtext symbols, operators and root wrappers. */
class KMathMlUnitFormatterTest {

    /** The invisible-times operator content (U+2062), the default multiplication. */
    private val it = 0x2062.toChar().toString()

    private fun render(
        value: Double,
        units: List<KUnitTerm>,
        config: KMathMlFormatConfig = KMathMlFormatConfig.DEFAULT,
    ) = KMathMlUnitFormatter(config).format(KUnitFormatContext(value, units, "%.1f", Locale.US))

    private val meter = KUnitTerm(KDistanceUnit.BASE, 1)
    private val perSecond = KUnitTerm(KTimeUnit.BASE, -1)
    private val perSecond2 = KUnitTerm(KTimeUnit.BASE, -2)

    /** A dimensionless value renders as just the number inside the math root. */
    @Test
    fun dimensionless() {
        assertEquals("<math display=\"inline\"><mn>5.0</mn></math>", render(5.0, emptyList()))
    }

    /** A single term: number, invisible times, `<mi>`. */
    @Test
    fun `single term`() {
        assertEquals(
            "<math display=\"inline\"><mn>5.0</mn><mo>$it</mo><mi>m</mi></math>",
            render(5.0, listOf(meter)),
        )
    }

    /** Default `<mfrac>`: end-to-end `km/h`. */
    @Test
    fun `fraction default`() {
        val v = 3 of meters / seconds
        assertEquals(
            "<math display=\"inline\"><mn>10.8</mn><mo>$it</mo>" +
                "<mfrac><mrow><mi>km</mi></mrow><mrow><mi>h</mi></mrow></mfrac></math>",
            v.format(kilo.meters / hours, "%.1f", Locale.US, KMathMlUnitFormatter()),
        )
    }

    /** A denominator exponent renders as `<msup>` inside the fraction. */
    @Test
    fun `fraction with powered denominator`() {
        assertEquals(
            "<math display=\"inline\"><mn>9.8</mn><mo>$it</mo>" +
                "<mfrac><mrow><mi>m</mi></mrow><mrow><msup><mi>s</mi><mn>2</mn></msup></mrow></mfrac></math>",
            render(9.8, listOf(meter, perSecond2)),
        )
    }

    /** EXPONENT style renders a flat product with a signed `<msup>`. */
    @Test
    fun `exponent style`() {
        assertEquals(
            "<math display=\"inline\"><mn>10.8</mn><mo>$it</mo>" +
                "<mi>m</mi><mo>$it</mo><msup><mi>s</mi><mn>-1</mn></msup></math>",
            render(10.8, listOf(meter, perSecond), KMathMlFormatConfig.INLINE),
        )
    }

    /** More than one negative term falls back to the flat product form. */
    @Test
    fun `product form for multiple negatives`() {
        assertEquals(
            "<math display=\"inline\"><mn>1.0</mn><mo>$it</mo>" +
                "<mi>m</mi><mo>$it</mo><msup><mi>s</mi><mn>-1</mn></msup>" +
                "<mo>$it</mo><msup><mi>s</mi><mn>-2</mn></msup></math>",
            render(1.0, listOf(meter, perSecond, perSecond2)),
        )
    }

    /** The FRAGMENT wrapper drops the `<math>` root. */
    @Test
    fun `fragment wrapper`() {
        assertEquals("<mn>5.0</mn><mo>$it</mo><mi>m</mi>", render(5.0, listOf(meter), KMathMlFormatConfig.FRAGMENT))
    }

    /** The block wrapper emits `display="block"`. */
    @Test
    fun `block wrapper`() {
        val config = KMathMlFormatConfig(wrapper = KMathMlWrapper.MATH_BLOCK)
        assertEquals(
            "<math display=\"block\"><mn>5.0</mn><mo>$it</mo><mi>m</mi></math>",
            render(5.0, listOf(meter), config),
        )
    }

    /** The `<mtext>` tag is used when configured. */
    @Test
    fun `mtext tag`() {
        val config = KMathMlFormatConfig(unitTag = KMathMlUnitTag.MTEXT)
        assertEquals(
            "<math display=\"inline\"><mn>5.0</mn><mo>$it</mo><mtext>m</mtext></math>",
            render(5.0, listOf(meter), config),
        )
    }

    /** The middle-dot operator is used when configured. */
    @Test
    fun `middle dot operator`() {
        val dot = 0x00B7.toChar().toString()
        val config = KMathMlFormatConfig(multiplication = KMathMlMultiplication.MIDDLE_DOT)
        assertEquals(
            "<math display=\"inline\"><mn>5.0</mn><mo>$dot</mo><mi>m</mi></math>",
            render(5.0, listOf(meter), config),
        )
    }

    /** The times operator is used when configured. */
    @Test
    fun `times operator`() {
        val cross = 0x00D7.toChar().toString()
        val config = KMathMlFormatConfig(multiplication = KMathMlMultiplication.TIMES)
        assertEquals(
            "<math display=\"inline\"><mn>5.0</mn><mo>$cross</mo><mi>m</mi></math>",
            render(5.0, listOf(meter), config),
        )
    }
}
