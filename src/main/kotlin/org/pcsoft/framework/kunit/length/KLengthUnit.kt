package org.pcsoft.framework.kunit.length

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of length. [baseValue] is the factor to convert into the group's base
 * unit ([BASE], meter): `1 unit = baseValue * meter`.
 *
 * Example:
 * ```kotlin
 * KLengthUnit.MILE.baseValue // 1609.344 (1 mile = 1609.344 meters)
 * ```
 */
enum class KLengthUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Meter, the SI base unit of length; [baseValue] = 1.0 by definition. */
    METER("m", 1.0),

    /** International mile (statute mile), 1 mi = 1609.344 m. */
    MILE("mi", 1609.344),

    /** International nautical mile, 1 nmi = 1852 m (used in aviation/maritime navigation). */
    NAUTICAL_MILE("nmi", 1852.0),

    /** International yard, 1 yd = 0.9144 m. */
    YARD("yd", 0.9144),

    /** International foot, 1 ft = 0.3048 m. */
    FOOT("ft", 0.3048),

    /** International inch, 1 in = 0.0254 m. */
    INCH("in", 0.0254),

    /** Fathom, 1 ftm = 1.8288 m (= 2 yd; traditionally used for water depth). */
    FATHOM("ftm", 1.8288),

    /** Chain (surveyor's chain), 1 ch = 20.1168 m. */
    CHAIN("ch", 20.1168),

    /** Furlong, 1 fur = 201.168 m (= 10 ch). */
    FURLONG("fur", 201.168),

    /** Astronomical unit, 1 AU = 149 597 870 700 m (mean Earth-Sun distance). */
    ASTRONOMICAL_UNIT("AU", 1.495978707e11),

    /** Light-year, 1 ly = 9 460 730 472 580 800 m (distance light travels in one Julian year). */
    LIGHT_YEAR("ly", 9.4607304725808e15),

    /** Parsec, 1 pc ≈ 3.0856775814913673e16 m (commonly used in astronomy). */
    PARSEC("pc", 3.0856775814913673e16);

    companion object {
        /**
         * The base unit of the length group; all internal values of [KLengthUnitInstance] are
         * normalized to this unit.
         */
        val BASE: KLengthUnit = METER
    }
}
