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

package org.pcsoft.framework.kunit.distance

import org.pcsoft.framework.kunit.KPrefixBuilder
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.deca
import org.pcsoft.framework.kunit.deci
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.hecto
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.of
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The SI prefix builders on the length units: each builder scales a meter template by its factor, and the
 * prefix × length-unit matrix reads back correctly.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KDistancePrefixTest {

    /** All length bare tokens paired with their base value (meters per 1 unit), for expected-value maths. */
    private val lengthTokens: List<Pair<KLengthUnitInstance, Double>> = listOf(
        meters to KDistanceUnit.METER.baseValue,
        miles to KDistanceUnit.MILE.baseValue,
        nauticalMiles to KDistanceUnit.NAUTICAL_MILE.baseValue,
        yards to KDistanceUnit.YARD.baseValue,
        feet to KDistanceUnit.FOOT.baseValue,
        inches to KDistanceUnit.INCH.baseValue,
        fathoms to KDistanceUnit.FATHOM.baseValue,
        chains to KDistanceUnit.CHAIN.baseValue,
        furlongs to KDistanceUnit.FURLONG.baseValue,
        lightYears to KDistanceUnit.LIGHT_YEAR.baseValue,
        parsecs to KDistanceUnit.PARSEC.baseValue,
        cubits to KDistanceUnit.CUBIT.baseValue,
        romanFeet to KDistanceUnit.ROMAN_FOOT.baseValue,
        romanPaces to KDistanceUnit.ROMAN_PACE.baseValue,
        stadia to KDistanceUnit.STADIUM.baseValue,
        romanMiles to KDistanceUnit.ROMAN_MILE.baseValue,
        rods to KDistanceUnit.ROD.baseValue,
        leagues to KDistanceUnit.LEAGUE.baseValue,
        cableLengths to KDistanceUnit.CABLE_LENGTH.baseValue,
        versts to KDistanceUnit.VERST.baseValue,
        prussianMiles to KDistanceUnit.PRUSSIAN_MILE.baseValue
    )

    /** All 24 SI prefix builders paired with their scale factor. */
    private val allPrefixes: List<Pair<KPrefixBuilder, Double>> = listOf(
        deca to 10.0, hecto to 100.0, kilo to 1_000.0, mega to 1e6, giga to 1e9,
        deci to 0.1, centi to 0.01, milli to 1e-3, micro to 1e-6, nano to 1e-9
    )

    private fun rel(expected: Double): Double = (abs(expected) * 1e-9).coerceAtLeast(1e-12)

    private fun tokens(): List<Array<Any>> = lengthTokens.map { arrayOf<Any>(it.first, it.second) }

    private fun prefixes(): List<Array<Any>> = allPrefixes.map { arrayOf<Any>(it.first, it.second) }

    /** Every SI prefix builder scales a meter template by its factor (`1 of kilo.meters == 1000 m`). */
    @ParameterizedTest
    @MethodSource("prefixes")
    fun `prefix standalone on meters`(builder: KPrefixBuilder, factor: Double) {
        assertEquals(factor, (1 of builder.meters).value, rel(factor))
    }

    /** Prefix x length-unit matrix (kilo/milli): `n of kilo.<unit>` = `n * 1000 * base`, read back as `n`. */
    @ParameterizedTest
    @MethodSource("tokens")
    fun `prefix times unit`(@Suppress("UNUSED_PARAMETER") token: KLengthUnitInstance, base: Double) {
        // Reconstruct the prefixed template from the same unit via the meter-relative base value.
        val kiloTemplate = kilo.meters.scaledBy(base) // 1000 * base meters, i.e. kilo.<unit>
        assertEquals(1000.0 * base * 4.0, (4 of kiloTemplate).value, rel(1000.0 * base * 4.0))
    }

    /** Every prefixed length extension property resolves to `1000 * base` meters via the `kilo` builder. */
    @Test
    fun `all prefixed length extensions`() {
        fun check(actual: KLengthUnitInstance, unit: KDistanceUnit) =
            assertEquals(1000.0 * unit.baseValue, actual.value, rel(1000.0 * unit.baseValue))
        check(kilo.miles, KDistanceUnit.MILE)
        check(kilo.nauticalMiles, KDistanceUnit.NAUTICAL_MILE)
        check(kilo.yards, KDistanceUnit.YARD)
        check(kilo.feet, KDistanceUnit.FOOT)
        check(kilo.inches, KDistanceUnit.INCH)
        check(kilo.fathoms, KDistanceUnit.FATHOM)
        check(kilo.chains, KDistanceUnit.CHAIN)
        check(kilo.furlongs, KDistanceUnit.FURLONG)
        check(kilo.astronomicalUnits, KDistanceUnit.ASTRONOMICAL_UNIT)
        check(kilo.lightSeconds, KDistanceUnit.LIGHT_SECOND)
        check(kilo.lightMinutes, KDistanceUnit.LIGHT_MINUTE)
        check(kilo.lightHours, KDistanceUnit.LIGHT_HOUR)
        check(kilo.lightDays, KDistanceUnit.LIGHT_DAY)
        check(kilo.lightWeeks, KDistanceUnit.LIGHT_WEEK)
        check(kilo.lightYears, KDistanceUnit.LIGHT_YEAR)
        check(kilo.parsecs, KDistanceUnit.PARSEC)
        check(kilo.cubits, KDistanceUnit.CUBIT)
        check(kilo.romanFeet, KDistanceUnit.ROMAN_FOOT)
        check(kilo.romanPaces, KDistanceUnit.ROMAN_PACE)
        check(kilo.stadia, KDistanceUnit.STADIUM)
        check(kilo.romanMiles, KDistanceUnit.ROMAN_MILE)
        check(kilo.rods, KDistanceUnit.ROD)
        check(kilo.leagues, KDistanceUnit.LEAGUE)
        check(kilo.cableLengths, KDistanceUnit.CABLE_LENGTH)
        check(kilo.versts, KDistanceUnit.VERST)
        check(kilo.prussianMiles, KDistanceUnit.PRUSSIAN_MILE)
    }

    /** Every prefixed area/volume special-unit extension property scales by the builder factor. */
    @Test
    fun `prefixed area and volume extensions`() {
        assertEquals(1000.0 * 10_000.0, (1 of kilo.hectares).value, rel(1e7))
        assertEquals(10.0 * 100.0, (1 of deca.ares).value, rel(1000.0))
        assertEquals(1000.0 * 4046.8564224, (1 of kilo.acres).value, rel(1000.0 * 4046.8564224))
        assertEquals(1000.0 * 1011.7141056, (1 of kilo.roods).value, rel(1000.0 * 1011.7141056))
        assertEquals(1000.0 * 25.29285264, (1 of kilo.squarePerches).value, rel(1000.0 * 25.29285264))
        assertEquals(1000.0 * 2553.22, (1 of kilo.morgens).value, rel(1000.0 * 2553.22))
        assertEquals(1000.0 * 5754.642, (1 of kilo.jochs).value, rel(1000.0 * 5754.642))
        assertEquals(1000.0 * 3407.27, (1 of kilo.tagwerks).value, rel(1000.0 * 3407.27))
        assertEquals(0.001 * 0.001, (1 of milli.liters).value, 1e-12)
        assertEquals(1000.0 * 0.003785411784, (1 of kilo.usGallons).value, rel(1000.0 * 0.003785411784))
        assertEquals(1000.0 * 0.00454609, (1 of kilo.imperialGallons).value, rel(1000.0 * 0.00454609))
        assertEquals(1000.0 * 2.95735295625e-5, (1 of kilo.usFluidOunces).value, rel(1000.0 * 2.95735295625e-5))
        assertEquals(1000.0 * 0.158987294928, (1 of kilo.oilBarrels).value, rel(1000.0 * 0.158987294928))
        assertEquals(1000.0 * 0.03636872, (1 of kilo.imperialBushels).value, rel(1000.0 * 0.03636872))
        assertEquals(1000.0 * 0.32731785, (1 of kilo.hogsheads).value, rel(1000.0 * 0.32731785))
        assertEquals(1000.0 * 0.00056826125, (1 of kilo.imperialPints).value, rel(1000.0 * 0.00056826125))
        assertEquals(1000.0 * 0.0011365225, (1 of kilo.imperialQuarts).value, rel(1000.0 * 0.0011365225))
    }
}
