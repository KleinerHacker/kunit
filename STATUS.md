# Implementation Status

## Done

* Root package `org.pcsoft.framework.kunit`:
  * `KUnit`, `KUnitTarget` (base interfaces)
  * `KMixedUnitInstance`, `KUnitTerm` (mixed-unit engine incl. `+`, `-`, `*`, `/`, `hasSameUnits`, `valueAs`, `toString` overload)
  * `KUnitPrefix` (complete SI prefix table, Quetta/Q to Quecto/q, 24 values) as well as `KScaledUnit`,
    `KDerivedUnit`, `KScaledDerivedUnit` and the `with` infix functions (output conversion)
  * `KPrefixBuilder` + 24 generic, group-independent prefix `infix` functions for construction
    (e.g. `5 kilo meters`), followed by `toKMixedUnitInstance()` and the group-specific `toXxxUnit()` conversion
* Sub-package `org.pcsoft.framework.kunit.length` (prototype for the physical quantity length):
  * `KLengthUnit` (meter, mile, nautical mile, yard, foot, inch, fathom, chain, furlong, astronomical unit, light-year, parsec)
  * `KLengthUnitInstance` (incl. `+`, `-`, `*`, `/`, comparison operators, `valueAs`, `toString` overload,
    `toKMixedUnitInstance`) - encapsulates arbitrary exponents of `KLengthUnit.BASE` (length, area, volume, ...)
  * `KLengthUnitExtensions` (creator functions per length, area and volume unit, `toKLengthUnit`)
  * `KLengthDerivedUnit` (area: are, hectare, acre; volume: liter, US/imperial gallon, US fluid ounce, oil barrel)
* All length types consistently carry the `K` prefix naming scheme (`KLengthUnit`, `KLengthUnitInstance`, `KLengthDerivedUnit`)
* Complete test suite for all classes mentioned above (root package + `length` package)
* `CLAUDE.md` extended with the architectural decisions made as part of this prototype
* Sub-package `org.pcsoft.framework.kunit.time` (physical quantity time):
  * `KTimeUnit` (second, minute, hour, day), base unit second
  * `KTimeUnitInstance` - a 100 % wrapper around `java.time.Duration` (the `Duration` is the source of
    truth; the full `Duration` API is forwarded) that additionally offers the standard "pure" unit
    surface (`value`, `valueAs`, `+`/`-`/`*`/`/`, comparison operators, `toString`, `toKMixedUnitInstance`,
    `toDuration`) plus `KMixedUnitInstance.toKTimeUnit()`/`Duration.toKTimeUnit()`. Time is always exponent 1
    (no `KTimeDerivedUnit`); `toKTimeUnit()` therefore only accepts a single `KTimeUnit` term at exponent 1
  * `KTimeUnitExtensions` (creator functions `seconds()`/`minutes()`/… and bare `val` aliases)
  * Note: values outside roughly `[1 ns, Long.MAX seconds]` are not representable by the Duration backing
  * Complete test suite (`time` package) and a dedicated MkDocs page (`docs/docs/units/time.md` + ko/zh/ja);
    the pre-existing throwaway private `TimeUnit` test enums were migrated to the official `KTimeUnit`
  * `CLAUDE.md` extended with the per-unit MkDocs page convention

## Open

* Further unit groups (e.g. mass, temperature, ...) following the pattern established here for `length`/`time`
* Composite "pure" units that are themselves composed of a mixed unit (e.g. Newton), including back- and
  forward conversion (see the "Tests" section in CLAUDE.md)
