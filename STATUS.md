# Implementation Status

## Done

* Root package `org.pcsoft.framework.kunit`:
  * `KUnit`, `KUnitTarget` (base interfaces)
  * `KUnitInstance`, `KUnitTerm` (mixed-unit engine incl. `+`, `-`, `*`, `/`, `hasSameUnits`, `valueAs`, `toString` overload)
  * `KUnitPrefix` (complete SI prefix table, Quetta/Q to Quecto/q, 24 values) as well as `KScaledUnit`,
    `KDerivedUnit`, `KScaledDerivedUnit` and the `with` infix functions (output conversion)
  * `KPrefixBuilder` + 24 generic, group-independent prefix `infix` functions for construction
    (e.g. `5 kilo meters`), followed by `toKUnitInstance()` and the group-specific `toXxxUnit()` conversion
* Sub-package `org.pcsoft.framework.kunit.length` (prototype for the physical quantity length):
  * `KLengthUnit` (meter, mile, nautical mile, yard, foot, inch, fathom, chain, furlong, astronomical unit, light-year, parsec)
  * `KLengthUnitInstance` (incl. `+`, `-`, `*`, `/`, comparison operators, `valueIn`, `toString` overload,
    `toKUnitInstance`) - encapsulates arbitrary exponents of `KLengthUnit.BASE` (length, area, volume, ...)
  * `KLengthUnitExtensions` (creator functions per length, area and volume unit, `toKLengthUnit`)
  * `KLengthDerivedUnit` (area: are, hectare, acre; volume: liter, US/imperial gallon, US fluid ounce, oil barrel)
* All length types consistently carry the `K` prefix naming scheme (`KLengthUnit`, `KLengthUnitInstance`, `KLengthDerivedUnit`)
* Complete test suite for all classes mentioned above (root package + `length` package)
* `CLAUDE.md` extended with the architectural decisions made as part of this prototype

## Open

* Further unit groups (e.g. mass, time, temperature, ...) following the pattern established here for `length`
* Composite "pure" units that are themselves composed of a mixed unit (e.g. Newton), including back- and
  forward conversion (see the "Tests" section in CLAUDE.md)
