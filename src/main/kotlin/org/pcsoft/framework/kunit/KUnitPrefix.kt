package org.pcsoft.framework.kunit

/**
 * A SI-style magnitude prefix (e.g. kilo, milli) usable when reading a [KMixedUnitInstance] or a "pure"
 * unit value (e.g. `KLengthUnitInstance`) in a scaled unit, or when constructing one via the generic
 * prefix `infix` functions declared below (e.g. `kilo`, `milli`), which work for any [KUnit].
 *
 * Prefixes are **not** stored as part of a [KUnitTerm]/[KUnit] - they only scale a raw value at the
 * input/output boundary; once a value has been constructed, the prefix used to construct it is no
 * longer recoverable (only the resulting, normalized base-unit value is kept).
 *
 * Example:
 * ```kotlin
 * val d = (5 kilo meters).toKMixedUnitInstance().toKLengthUnit() // 5 km, "meters" is the KLengthUnit.METER alias
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
 * Intermediate result of applying a [KUnitPrefix] to a [Number] together with a concrete [KUnit],
 * not yet a "pure" unit value. The root package does not know about the "pure" unit wrapper classes
 * defined by each unit sub-package (e.g. `KLengthUnitInstance`), so the prefix `infix` functions
 * below can only build this generic, group-agnostic intermediate; converting it into a concrete
 * "pure" unit is done explicitly via the group's own conversion (e.g. `KMixedUnitInstance.toKLengthUnit()`
 * in the `length` package).
 *
 * Obtained via the prefix `infix` functions, e.g. `5 kilo KLengthUnit.METER`.
 *
 * Example:
 * ```kotlin
 * val builder = 5 kilo KLengthUnit.METER // KPrefixBuilder, not yet a KLengthUnitInstance
 * val mixed = builder.toKMixedUnitInstance()  // KMixedUnitInstance: value=5000.0, units=[METER^1]
 * val length = mixed.toKLengthUnit()     // KLengthUnitInstance: value=5000.0
 * ```
 */
class KPrefixBuilder internal constructor(private val instance: KMixedUnitInstance) {
    /**
     * The generic mixed-unit representation of this builder (single term, exponent 1, prefix-scaled
     * value). Use this together with a group-specific `toXxxUnit()` extension (e.g.
     * `KMixedUnitInstance.toKLengthUnit()`) to obtain the concrete "pure" unit.
     */
    fun toKMixedUnitInstance(): KMixedUnitInstance = instance
}

private fun buildPrefixed(value: Number, prefix: KUnitPrefix, unit: KUnit): KPrefixBuilder =
    KPrefixBuilder(KMixedUnitInstance(value.toDouble() * prefix.factor, listOf(KUnitTerm(unit, 1))))

/**
 * Scales this number by [KUnitPrefix.QUETTA] and pairs it with [unit], e.g.
 * `(5 quetta KLengthUnit.METER).toKMixedUnitInstance().value // 5e30`.
 */
infix fun Number.quetta(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.QUETTA, unit)

/**
 * Scales this number by [KUnitPrefix.RONNA] and pairs it with [unit], e.g.
 * `(5 ronna KLengthUnit.METER).toKMixedUnitInstance().value // 5e27`.
 */
infix fun Number.ronna(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.RONNA, unit)

/**
 * Scales this number by [KUnitPrefix.YOTTA] and pairs it with [unit], e.g.
 * `(5 yotta KLengthUnit.METER).toKMixedUnitInstance().value // 5e24`.
 */
infix fun Number.yotta(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.YOTTA, unit)

/**
 * Scales this number by [KUnitPrefix.ZETTA] and pairs it with [unit], e.g.
 * `(5 zetta KLengthUnit.METER).toKMixedUnitInstance().value // 5e21`.
 */
infix fun Number.zetta(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.ZETTA, unit)

/**
 * Scales this number by [KUnitPrefix.EXA] and pairs it with [unit], e.g.
 * `(5 exa KLengthUnit.METER).toKMixedUnitInstance().value // 5e18`.
 */
infix fun Number.exa(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.EXA, unit)

/**
 * Scales this number by [KUnitPrefix.PETA] and pairs it with [unit], e.g.
 * `(5 peta KLengthUnit.METER).toKMixedUnitInstance().value // 5e15`.
 */
infix fun Number.peta(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.PETA, unit)

/**
 * Scales this number by [KUnitPrefix.TERA] and pairs it with [unit], e.g.
 * `(5 tera KLengthUnit.METER).toKMixedUnitInstance().value // 5e12`.
 */
infix fun Number.tera(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.TERA, unit)

/**
 * Scales this number by [KUnitPrefix.GIGA] and pairs it with [unit], e.g.
 * `(5 giga KLengthUnit.METER).toKMixedUnitInstance().value // 5.0e9`.
 */
infix fun Number.giga(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.GIGA, unit)

/**
 * Scales this number by [KUnitPrefix.MEGA] and pairs it with [unit], e.g.
 * `(5 mega KLengthUnit.METER).toKMixedUnitInstance().value // 5000000.0`.
 */
infix fun Number.mega(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.MEGA, unit)

/**
 * Scales this number by [KUnitPrefix.KILO] and pairs it with [unit], e.g.
 * `(5 kilo KLengthUnit.METER).toKMixedUnitInstance().value // 5000.0`.
 */
infix fun Number.kilo(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.KILO, unit)

/**
 * Scales this number by [KUnitPrefix.HECTO] and pairs it with [unit], e.g.
 * `(5 hecto KLengthUnit.METER).toKMixedUnitInstance().value // 500.0`.
 */
infix fun Number.hecto(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.HECTO, unit)

/**
 * Scales this number by [KUnitPrefix.DECA] and pairs it with [unit], e.g.
 * `(5 deca KLengthUnit.METER).toKMixedUnitInstance().value // 50.0`.
 */
infix fun Number.deca(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.DECA, unit)

/**
 * Scales this number by [KUnitPrefix.DECI] and pairs it with [unit], e.g.
 * `(5 deci KLengthUnit.METER).toKMixedUnitInstance().value // 0.5`.
 */
infix fun Number.deci(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.DECI, unit)

/**
 * Scales this number by [KUnitPrefix.CENTI] and pairs it with [unit], e.g.
 * `(5 centi KLengthUnit.METER).toKMixedUnitInstance().value // 0.05`.
 */
infix fun Number.centi(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.CENTI, unit)

/**
 * Scales this number by [KUnitPrefix.MILLI] and pairs it with [unit], e.g.
 * `(5 milli KLengthUnit.METER).toKMixedUnitInstance().value // 0.005`.
 */
infix fun Number.milli(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.MILLI, unit)

/**
 * Scales this number by [KUnitPrefix.MICRO] and pairs it with [unit], e.g.
 * `(5 micro KLengthUnit.METER).toKMixedUnitInstance().value // 0.000005`.
 */
infix fun Number.micro(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.MICRO, unit)

/**
 * Scales this number by [KUnitPrefix.NANO] and pairs it with [unit], e.g.
 * `(5 nano KLengthUnit.METER).toKMixedUnitInstance().value // 5e-9`.
 */
infix fun Number.nano(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.NANO, unit)

/**
 * Scales this number by [KUnitPrefix.PICO] and pairs it with [unit], e.g.
 * `(5 pico KLengthUnit.METER).toKMixedUnitInstance().value // 5e-12`.
 */
infix fun Number.pico(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.PICO, unit)

/**
 * Scales this number by [KUnitPrefix.FEMTO] and pairs it with [unit], e.g.
 * `(5 femto KLengthUnit.METER).toKMixedUnitInstance().value // 5e-15`.
 */
infix fun Number.femto(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.FEMTO, unit)

/**
 * Scales this number by [KUnitPrefix.ATTO] and pairs it with [unit], e.g.
 * `(5 atto KLengthUnit.METER).toKMixedUnitInstance().value // 5e-18`.
 */
infix fun Number.atto(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.ATTO, unit)

/**
 * Scales this number by [KUnitPrefix.ZEPTO] and pairs it with [unit], e.g.
 * `(5 zepto KLengthUnit.METER).toKMixedUnitInstance().value // 5e-21`.
 */
infix fun Number.zepto(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.ZEPTO, unit)

/**
 * Scales this number by [KUnitPrefix.YOCTO] and pairs it with [unit], e.g.
 * `(5 yocto KLengthUnit.METER).toKMixedUnitInstance().value // 5e-24`.
 */
infix fun Number.yocto(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.YOCTO, unit)

/**
 * Scales this number by [KUnitPrefix.RONTO] and pairs it with [unit], e.g.
 * `(5 ronto KLengthUnit.METER).toKMixedUnitInstance().value // 5e-27`.
 */
infix fun Number.ronto(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.RONTO, unit)

/**
 * Scales this number by [KUnitPrefix.QUECTO] and pairs it with [unit], e.g.
 * `(5 quecto KLengthUnit.METER).toKMixedUnitInstance().value // 5e-30`.
 */
infix fun Number.quecto(unit: KUnit): KPrefixBuilder = buildPrefixed(this, KUnitPrefix.QUECTO, unit)

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
