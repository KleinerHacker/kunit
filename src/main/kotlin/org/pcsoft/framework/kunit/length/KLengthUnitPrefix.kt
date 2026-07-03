package org.pcsoft.framework.kunit.length

import org.pcsoft.framework.kunit.KUnitPrefix

// SI-prefix `infix` constructors for the length group. Each takes a bare [KLengthUnit] (e.g. the
// `meters` alias) and returns a [KLengthUnitInstance] directly (exponent 1), scaled by the prefix and
// normalized to the base unit - e.g. `5 kilo meters` == `5000.meters()`. Unlike a group-agnostic root
// helper, these live in the length package, where the concrete wrapper type is known, so no
// intermediate builder or explicit `toKLengthUnit()` step is needed.

private fun prefixed(value: Number, prefix: KUnitPrefix, unit: KLengthUnit): KLengthUnitInstance =
    lengthUnitInstanceOf(value.toDouble() * prefix.factor * unit.baseValue)

/** Scales [unit] by [KUnitPrefix.QUETTA], e.g. `(5 quetta meters).value // 5e30`. */
infix fun Number.quetta(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.QUETTA, unit)

/** Scales [unit] by [KUnitPrefix.RONNA], e.g. `(5 ronna meters).value // 5e27`. */
infix fun Number.ronna(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.RONNA, unit)

/** Scales [unit] by [KUnitPrefix.YOTTA], e.g. `(5 yotta meters).value // 5e24`. */
infix fun Number.yotta(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.YOTTA, unit)

/** Scales [unit] by [KUnitPrefix.ZETTA], e.g. `(5 zetta meters).value // 5e21`. */
infix fun Number.zetta(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.ZETTA, unit)

/** Scales [unit] by [KUnitPrefix.EXA], e.g. `(5 exa meters).value // 5e18`. */
infix fun Number.exa(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.EXA, unit)

/** Scales [unit] by [KUnitPrefix.PETA], e.g. `(5 peta meters).value // 5e15`. */
infix fun Number.peta(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.PETA, unit)

/** Scales [unit] by [KUnitPrefix.TERA], e.g. `(5 tera meters).value // 5e12`. */
infix fun Number.tera(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.TERA, unit)

/** Scales [unit] by [KUnitPrefix.GIGA], e.g. `(5 giga meters).value // 5.0e9`. */
infix fun Number.giga(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.GIGA, unit)

/** Scales [unit] by [KUnitPrefix.MEGA], e.g. `(5 mega meters).value // 5000000.0`. */
infix fun Number.mega(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.MEGA, unit)

/** Scales [unit] by [KUnitPrefix.KILO], e.g. `(5 kilo meters).value // 5000.0`. */
infix fun Number.kilo(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.KILO, unit)

/** Scales [unit] by [KUnitPrefix.HECTO], e.g. `(5 hecto meters).value // 500.0`. */
infix fun Number.hecto(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.HECTO, unit)

/** Scales [unit] by [KUnitPrefix.DECA], e.g. `(5 deca meters).value // 50.0`. */
infix fun Number.deca(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.DECA, unit)

/** Scales [unit] by [KUnitPrefix.DECI], e.g. `(5 deci meters).value // 0.5`. */
infix fun Number.deci(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.DECI, unit)

/** Scales [unit] by [KUnitPrefix.CENTI], e.g. `(5 centi meters).value // 0.05`. */
infix fun Number.centi(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.CENTI, unit)

/** Scales [unit] by [KUnitPrefix.MILLI], e.g. `(5 milli meters).value // 0.005`. */
infix fun Number.milli(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.MILLI, unit)

/** Scales [unit] by [KUnitPrefix.MICRO], e.g. `(5 micro meters).value // 0.000005`. */
infix fun Number.micro(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.MICRO, unit)

/** Scales [unit] by [KUnitPrefix.NANO], e.g. `(5 nano meters).value // 5e-9`. */
infix fun Number.nano(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.NANO, unit)

/** Scales [unit] by [KUnitPrefix.PICO], e.g. `(5 pico meters).value // 5e-12`. */
infix fun Number.pico(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.PICO, unit)

/** Scales [unit] by [KUnitPrefix.FEMTO], e.g. `(5 femto meters).value // 5e-15`. */
infix fun Number.femto(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.FEMTO, unit)

/** Scales [unit] by [KUnitPrefix.ATTO], e.g. `(5 atto meters).value // 5e-18`. */
infix fun Number.atto(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.ATTO, unit)

/** Scales [unit] by [KUnitPrefix.ZEPTO], e.g. `(5 zepto meters).value // 5e-21`. */
infix fun Number.zepto(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.ZEPTO, unit)

/** Scales [unit] by [KUnitPrefix.YOCTO], e.g. `(5 yocto meters).value // 5e-24`. */
infix fun Number.yocto(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.YOCTO, unit)

/** Scales [unit] by [KUnitPrefix.RONTO], e.g. `(5 ronto meters).value // 5e-27`. */
infix fun Number.ronto(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.RONTO, unit)

/** Scales [unit] by [KUnitPrefix.QUECTO], e.g. `(5 quecto meters).value // 5e-30`. */
infix fun Number.quecto(unit: KLengthUnit): KLengthUnitInstance = prefixed(this, KUnitPrefix.QUECTO, unit)
