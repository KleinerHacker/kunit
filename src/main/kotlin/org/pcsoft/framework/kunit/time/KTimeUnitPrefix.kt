package org.pcsoft.framework.kunit.time

import org.pcsoft.framework.kunit.KUnitPrefix

// SI-prefix `infix` constructors for the time group. Each takes a bare [KTimeUnit] (e.g. the `seconds`
// alias) and returns a [KTimeUnitInstance] directly, scaled by the prefix and normalized to seconds -
// e.g. `5 milli seconds` == `0.005.seconds()`. Values outside roughly `[1 ns, Long.MAX seconds]` are
// not representable by the Duration backing (see KTimeUnitInstance).

private fun prefixed(value: Number, prefix: KUnitPrefix, unit: KTimeUnit): KTimeUnitInstance =
    timeUnitInstanceOf(value.toDouble() * prefix.factor * unit.baseValue)

/** Scales [unit] by [KUnitPrefix.QUETTA], e.g. `(5 quetta seconds).value // 5e30`. */
infix fun Number.quetta(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.QUETTA, unit)

/** Scales [unit] by [KUnitPrefix.RONNA], e.g. `(5 ronna seconds).value // 5e27`. */
infix fun Number.ronna(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.RONNA, unit)

/** Scales [unit] by [KUnitPrefix.YOTTA], e.g. `(5 yotta seconds).value // 5e24`. */
infix fun Number.yotta(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.YOTTA, unit)

/** Scales [unit] by [KUnitPrefix.ZETTA], e.g. `(5 zetta seconds).value // 5e21`. */
infix fun Number.zetta(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.ZETTA, unit)

/** Scales [unit] by [KUnitPrefix.EXA], e.g. `(5 exa seconds).value // 5e18`. */
infix fun Number.exa(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.EXA, unit)

/** Scales [unit] by [KUnitPrefix.PETA], e.g. `(5 peta seconds).value // 5e15`. */
infix fun Number.peta(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.PETA, unit)

/** Scales [unit] by [KUnitPrefix.TERA], e.g. `(5 tera seconds).value // 5e12`. */
infix fun Number.tera(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.TERA, unit)

/** Scales [unit] by [KUnitPrefix.GIGA], e.g. `(5 giga seconds).value // 5.0e9`. */
infix fun Number.giga(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.GIGA, unit)

/** Scales [unit] by [KUnitPrefix.MEGA], e.g. `(5 mega seconds).value // 5000000.0`. */
infix fun Number.mega(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.MEGA, unit)

/** Scales [unit] by [KUnitPrefix.KILO], e.g. `(5 kilo seconds).value // 5000.0`. */
infix fun Number.kilo(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.KILO, unit)

/** Scales [unit] by [KUnitPrefix.HECTO], e.g. `(5 hecto seconds).value // 500.0`. */
infix fun Number.hecto(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.HECTO, unit)

/** Scales [unit] by [KUnitPrefix.DECA], e.g. `(5 deca seconds).value // 50.0`. */
infix fun Number.deca(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.DECA, unit)

/** Scales [unit] by [KUnitPrefix.DECI], e.g. `(5 deci seconds).value // 0.5`. */
infix fun Number.deci(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.DECI, unit)

/** Scales [unit] by [KUnitPrefix.CENTI], e.g. `(5 centi seconds).value // 0.05`. */
infix fun Number.centi(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.CENTI, unit)

/** Scales [unit] by [KUnitPrefix.MILLI], e.g. `(5 milli seconds).value // 0.005`. */
infix fun Number.milli(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.MILLI, unit)

/** Scales [unit] by [KUnitPrefix.MICRO], e.g. `(5 micro seconds).value // 0.000005`. */
infix fun Number.micro(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.MICRO, unit)

/** Scales [unit] by [KUnitPrefix.NANO], e.g. `(5 nano seconds).value // 5e-9`. */
infix fun Number.nano(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.NANO, unit)

/** Scales [unit] by [KUnitPrefix.PICO], e.g. `(5 pico seconds).value // 5e-12`. */
infix fun Number.pico(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.PICO, unit)

/** Scales [unit] by [KUnitPrefix.FEMTO], e.g. `(5 femto seconds).value // 5e-15`. */
infix fun Number.femto(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.FEMTO, unit)

/** Scales [unit] by [KUnitPrefix.ATTO], e.g. `(5 atto seconds).value // 5e-18`. */
infix fun Number.atto(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.ATTO, unit)

/** Scales [unit] by [KUnitPrefix.ZEPTO], e.g. `(5 zepto seconds).value // 5e-21`. */
infix fun Number.zepto(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.ZEPTO, unit)

/** Scales [unit] by [KUnitPrefix.YOCTO], e.g. `(5 yocto seconds).value // 5e-24`. */
infix fun Number.yocto(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.YOCTO, unit)

/** Scales [unit] by [KUnitPrefix.RONTO], e.g. `(5 ronto seconds).value // 5e-27`. */
infix fun Number.ronto(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.RONTO, unit)

/** Scales [unit] by [KUnitPrefix.QUECTO], e.g. `(5 quecto seconds).value // 5e-30`. */
infix fun Number.quecto(unit: KTimeUnit): KTimeUnitInstance = prefixed(this, KUnitPrefix.QUECTO, unit)
