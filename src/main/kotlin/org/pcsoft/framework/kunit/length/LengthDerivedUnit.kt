package org.pcsoft.framework.kunit.length

import org.pcsoft.framework.kunit.KDerivedUnit

/**
 * Named units for length-derived physical quantities (area, volume), each bound to a fixed exponent
 * of [LengthUnit.BASE]. Usable as a `KUnitTarget` wherever a plain [LengthUnit] or `KScaledUnit`
 * would be accepted, e.g. with `KUnitInstance.valueAs`/`toString`.
 *
 * These do **not** replace the normal per-unit exponent mechanism - e.g. `LengthUnit.METER` at
 * exponent 2 is still a perfectly valid, "raw" area representation - they only add friendlier,
 * commonly used named targets for conversion/formatting.
 *
 * Example:
 * ```kotlin
 * val area = 200.meters() * 50.meters()          // KUnitInstance, 10 000 m² (METER^2)
 * area.valueAs(LengthDerivedUnit.HECTARE)         // 1.0
 *
 * val volume = 2.meters() * 2.meters() * 2.meters() // KUnitInstance, 8 m³ (METER^3)
 * volume.valueAs(LengthDerivedUnit.LITER)         // 8000.0
 * ```
 */
object LengthDerivedUnit {
    // --- Area (length squared, exponent 2) ---

    /** Are: unit of area, 1 a = 100 m² (base unit of the are/hectare family). */
    val ARE: KDerivedUnit<LengthUnit> = KDerivedUnit(symbol = "a", exponent = 2, baseValue = 100.0, referenceUnit = LengthUnit.BASE)

    /** Hectare: unit of area, 1 ha = 10 000 m² (= 100 a; common for land area). */
    val HECTARE: KDerivedUnit<LengthUnit> = KDerivedUnit(symbol = "ha", exponent = 2, baseValue = 10_000.0, referenceUnit = LengthUnit.BASE)

    /** International acre, 1 ac = 4046.8564224 m² (common in the US/UK for land area). */
    val ACRE: KDerivedUnit<LengthUnit> = KDerivedUnit(symbol = "ac", exponent = 2, baseValue = 4046.8564224, referenceUnit = LengthUnit.BASE)

    // --- Volume (length cubed, exponent 3) ---

    /** Liter, 1 L = 0.001 m³ (= 1 dm³; the common metric unit of volume/capacity). */
    val LITER: KDerivedUnit<LengthUnit> = KDerivedUnit(symbol = "L", exponent = 3, baseValue = 0.001, referenceUnit = LengthUnit.BASE)

    /** US liquid gallon, 1 gal = 0.003785411784 m³. */
    val US_GALLON: KDerivedUnit<LengthUnit> = KDerivedUnit(symbol = "gal (US)", exponent = 3, baseValue = 0.003785411784, referenceUnit = LengthUnit.BASE)

    /** Imperial gallon, 1 gal = 0.00454609 m³ (UK/Commonwealth). */
    val IMPERIAL_GALLON: KDerivedUnit<LengthUnit> = KDerivedUnit(symbol = "gal (UK)", exponent = 3, baseValue = 0.00454609, referenceUnit = LengthUnit.BASE)

    /** US fluid ounce, 1 fl oz = 2.95735295625e-5 m³. */
    val US_FLUID_OUNCE: KDerivedUnit<LengthUnit> = KDerivedUnit(symbol = "fl oz", exponent = 3, baseValue = 2.95735295625e-5, referenceUnit = LengthUnit.BASE)

    /** Oil barrel, 1 bbl = 0.158987294928 m³ (standard for crude oil in the petroleum industry). */
    val OIL_BARREL: KDerivedUnit<LengthUnit> = KDerivedUnit(symbol = "bbl", exponent = 3, baseValue = 0.158987294928, referenceUnit = LengthUnit.BASE)
}
