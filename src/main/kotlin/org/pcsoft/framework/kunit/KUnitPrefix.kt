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

/**
 * A SI-style magnitude prefix (e.g. kilo, milli) usable when reading a [KMixedUnitInstance] or a "pure"
 * unit value (e.g. `KLengthUnitInstance`) in a scaled unit, or when constructing one via the
 * group-specific prefix `infix` functions (e.g. `5 kilo meters`), which each group declares over its
 * own unit type and which return that group's concrete "pure" unit directly.
 *
 * Prefixes are **not** stored as part of a [KUnitTerm]/[KUnit] - they only scale a raw value at the
 * input/output boundary; once a value has been constructed, the prefix used to construct it is no
 * longer recoverable (only the resulting, normalized base-unit value is kept).
 *
 * Example:
 * ```kotlin
 * val d = 5 kilo meters // KLengthUnitInstance, "meters" is the KLengthUnit.METER alias
 * d.value                 // 5000.0 (normalized to meters)
 * d.valueAs(KUnitPrefix.KILO with KLengthUnit.METER) // 5.0 (read back in kilometers)
 * ```
 */
enum class KUnitPrefix(val symbol: String, val factor: Double) {
    /** Quetta: factor 10^30, the largest SI prefix (2022 standard). */
    QUETTA("Q", 1e30),

    /** Ronna: factor 10^27. */
    RONNA("R", 1e27),

    /** Yotta: factor 10^24. */
    YOTTA("Y", 1e24),

    /** Zetta: factor 10^21. */
    ZETTA("Z", 1e21),

    /** Exa: factor 10^18. */
    EXA("E", 1e18),

    /** Peta: factor 10^15. */
    PETA("P", 1e15),

    /** Tera: factor 10^12. */
    TERA("T", 1e12),

    /** Giga: factor 10^9 (10^9), e.g. gigameter = 1 000 000 000 meters. */
    GIGA("G", 1e9),

    /** Mega: factor 1 000 000 (10^6), e.g. megameter = 1 000 000 meters. */
    MEGA("M", 1e6),

    /** Kilo: factor 1 000 (10^3), e.g. kilometer = 1000 meters. */
    KILO("k", 1_000.0),

    /** Hecto: factor 100 (10^2), e.g. hectometer = 100 meters. */
    HECTO("h", 100.0),

    /** Deca: factor 10 (10^1), e.g. decameter = 10 meters. */
    DECA("da", 10.0),

    /** Deci: factor 0.1 (10^-1), e.g. decimeter = 0.1 meters. */
    DECI("d", 0.1),

    /** Centi: factor 0.01 (10^-2), e.g. centimeter = 0.01 meters. */
    CENTI("c", 0.01),

    /** Milli: factor 0.001 (10^-3), e.g. millimeter = 0.001 meters. */
    MILLI("m", 0.001),

    /** Micro: factor 10^-6, e.g. micrometer = 0.000001 meters. */
    MICRO("µ", 1e-6),

    /** Nano: factor 10^-9, e.g. nanometer = 0.000000001 meters. */
    NANO("n", 1e-9),

    /** Pico: factor 10^-12. */
    PICO("p", 1e-12),

    /** Femto: factor 10^-15. */
    FEMTO("f", 1e-15),

    /** Atto: factor 10^-18. */
    ATTO("a", 1e-18),

    /** Zepto: factor 10^-21. */
    ZEPTO("z", 1e-21),

    /** Yocto: factor 10^-24. */
    YOCTO("y", 1e-24),

    /** Ronto: factor 10^-27. */
    RONTO("r", 1e-27),

    /** Quecto: factor 10^-30, the smallest SI prefix (2022 standard). */
    QUECTO("q", 1e-30)
}

/**
 * A [KUnit] combined with a [KUnitPrefix], e.g. "km" = `KILO with METER`.
 *
 * Used as a [KUnitTarget] wherever a plain [KUnit] would otherwise be accepted (e.g.
 * `KLengthUnitInstance.valueAs`, `KMixedUnitInstance.valueAs`/`toString`), so callers can pass either a
 * bare unit or a prefixed one at the same call site.
 *
 * Example:
 * ```kotlin
 * val km = KUnitPrefix.KILO with KLengthUnit.METER
 * km.baseValue // 1000.0 (1 km = 1000 m)
 * km.symbol    // "km"
 * ```
 */
data class KScaledUnit(val prefix: KUnitPrefix, val unit: KUnit) : KUnitTarget {
    /**
     * Combined conversion factor to the group's base unit: [prefix].factor * [unit].baseValue.
     *
     * Example: `(KUnitPrefix.MILLI with KLengthUnit.METER).baseValue == 0.001`.
     */
    val baseValue: Double get() = prefix.factor * unit.baseValue

    /**
     * The combined display symbol, e.g. `"km"` for `KILO with METER`.
     */
    val symbol: String get() = prefix.symbol + unit.symbol
}

/**
 * Combines a prefix and a unit into a [KScaledUnit], e.g. `KUnitPrefix.KILO with KLengthUnit.METER`.
 *
 * Example:
 * ```kotlin
 * val km = KUnitPrefix.KILO with KLengthUnit.METER
 * 5.miles().valueAs(km) // ≈ 8.046722 (miles expressed in kilometers)
 * ```
 */
infix fun KUnitPrefix.with(unit: KUnit): KScaledUnit = KScaledUnit(this, unit)

/**
 * A named unit bound to a specific (unit group, exponent) combination, e.g. hectare for area
 * (length squared, factor 10 000 relative to meter) or liter for volume (length cubed, factor 0.001
 * relative to meter). Unlike [KScaledUnit], the factor here is not a simple SI-prefix multiplier but
 * an arbitrary, named conversion tied to one specific [exponent] of [referenceUnit]'s group.
 *
 * [KDerivedUnit] does **not** replace the normal per-unit exponent mechanism (e.g. `KLengthUnit.METER`
 * at exponent 2 is still a perfectly valid, "raw" area representation) - it only adds an additional,
 * friendlier [KUnitTarget] for `valueAs`/`valueAs`/`toString` conversions.
 *
 * Generic over the referenced unit type `U`, so a group-specific constant (e.g.
 * `KLengthDerivedUnit.HECTARE: KDerivedUnit<KLengthUnit>`) cannot accidentally be constructed with the
 * `referenceUnit` of a different group (e.g. a mass unit) - that mismatch is caught at compile time.
 * The exponent itself remains a runtime value (Kotlin has no types for numeric exponents), so using
 * e.g. `HECTARE` (exponent 2) against a pure length (exponent 1) still fails at runtime with
 * [IllegalStateException], consistent with the rest of the `valueAs`/`valueAs`/`toString` matching.
 *
 * Example:
 * ```kotlin
 * val hectare = KDerivedUnit(symbol = "ha", exponent = 2, baseValue = 10_000.0, referenceUnit = KLengthUnit.BASE)
 * val area = 200.meters() * 50.meters() // KMixedUnitInstance, 10 000 m²
 * area.valueAs(hectare) // 1.0
 * ```
 */
data class KDerivedUnit<U : KUnit>(
    val symbol: String,
    val exponent: Int,
    val baseValue: Double,
    val referenceUnit: U
) : KUnitTarget

/**
 * A [KDerivedUnit] combined with a [KUnitPrefix], e.g. "mL" = `MILLI with LITER`.
 *
 * Lets prefixes apply to derived units the same way they apply to plain [KUnit]s via [KScaledUnit].
 *
 * Example:
 * ```kotlin
 * val milliliter = KUnitPrefix.MILLI with KLengthDerivedUnit.LITER
 * milliliter.baseValue // 1e-6 (1 mL = 1e-6 m³)
 * milliliter.symbol    // "mL"
 * ```
 */
data class KScaledDerivedUnit<U : KUnit>(val prefix: KUnitPrefix, val derivedUnit: KDerivedUnit<U>) : KUnitTarget {
    /**
     * Combined conversion factor to the group's base unit: [prefix].factor * [derivedUnit].baseValue.
     */
    val baseValue: Double get() = prefix.factor * derivedUnit.baseValue

    /**
     * The exponent this target is bound to, inherited from [derivedUnit].
     */
    val exponent: Int get() = derivedUnit.exponent

    /**
     * The reference unit this target is bound to, inherited from [derivedUnit].
     */
    val referenceUnit: U get() = derivedUnit.referenceUnit

    /**
     * The combined display symbol, e.g. `"mL"` for `MILLI with LITER`.
     */
    val symbol: String get() = prefix.symbol + derivedUnit.symbol
}

/**
 * Combines a prefix and a derived unit into a [KScaledDerivedUnit], e.g.
 * `KUnitPrefix.MILLI with KLengthDerivedUnit.LITER` (= "mL", 1e-6 m³).
 *
 * Example:
 * ```kotlin
 * val volume = 2.meters() * 2.meters() * 2.meters() // 8 m³
 * volume.valueAs(KUnitPrefix.MILLI with KLengthDerivedUnit.LITER) // 8 000 000.0 (mL)
 * ```
 */
infix fun <U : KUnit> KUnitPrefix.with(derivedUnit: KDerivedUnit<U>): KScaledDerivedUnit<U> = KScaledDerivedUnit(this, derivedUnit)
