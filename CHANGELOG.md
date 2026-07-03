# kunit Changelog

## [Unreleased]

### Added

- New unit group **Time** (`org.pcsoft.framework.kunit.time`):
  - Units: second (base), minute, hour, day. Calendar-based units (week, year) are intentionally
    excluded, since they are defined by calendars rather than by a fixed physical quantity.
  - `KTimeUnitInstance` is a 100 % wrapper around `java.time.Duration` (the `Duration` is the source of
    truth and the full `Duration` API is forwarded), additionally offering the standard "pure" unit surface
    (`value`, `valueAs`, `+`/`-`/`*`/`/`, comparisons, `toString`, `toKMixedUnitInstance`) plus `toDuration()`,
    `Duration.toKTimeUnit()` and `KMixedUnitInstance.toKTimeUnit()`.
  - Sub-second scales are reached via the SI prefixes on `second`; time is always exponent 1 (no derived
    units). Values outside roughly `[1 ns, Long.MAX seconds]` are not representable by the Duration backing.
  - Dedicated MkDocs page (`docs/docs/units/time.md`) with Korean, Chinese and Japanese translations.
- Common interface hierarchy for all unit-value types:
  - `KUnitMeasurable` — the group-agnostic surface shared by every value (`value`,
    `toKMixedUnitInstance()`, `*`/`/` against a `KMixedUnitInstance`). The "pure" wrappers implement it
    via Kotlin `by` delegation to their internal `KMixedUnitInstance`.
  - `KUnitInstance<SELF>` — the self-typed (F-bounded) interface of the "pure" wrappers, adding
    same-type `+`/`-`/comparison, same-group `*`/`/`, and single-target `valueAs`/`toString`.

### Changed

- Migrated the throwaway private `TimeUnit` test enums to the official `KTimeUnit`.
- **Breaking:** renamed the mixed-unit class `KUnitInstance` → `KMixedUnitInstance` (the name
  `KUnitInstance` is now the "pure" wrapper interface) and its conversion accessor
  `toKUnitInstance()` → `toKMixedUnitInstance()`.
- **Breaking:** renamed the "pure" wrapper accessor `valueIn(target)` → `valueAs(target)`, matching
  `KMixedUnitInstance.valueAs`.

## [0.1.0]

### Added

- Core mixed-unit engine (`KUnitInstance`, `KUnitTerm`) supporting arbitrary combinations of units and
  exponents, with `+`, `-`, `*`, `/` operators and a `hasSameUnits` check for unit/exponent equality.
- Full SI prefix support (`KUnitPrefix`, Quetta/Q to Quecto/q) usable with any unit via generic `infix`
  construction, e.g. `5 kilo meters`.
- Support for special/derived units per unit group and exponent (`KDerivedUnit`, `KScaledDerivedUnit`), e.g.
  hectare for area or liter for volume.
- New unit group **Length** (`org.pcsoft.framework.kunit.length`):
  - Units: meter, mile, nautical mile, yard, foot, inch, fathom, chain, furlong, astronomical unit,
    light-year, parsec.
  - Multi-dimensional support for area (exponent 2) and volume (exponent 3), including the special units
    are, hectare, acre (area) and liter, US gallon, imperial gallon, US fluid ounce, oil barrel (volume).
  - Full operator (`+`, `-`, `*`, `/`) and comparison (`==`, `!=`, `<`, `<=`, `>`, `>=`) support, with
    automatic conversion between length units and `toString`/`valueAs` output in any target unit.
- Creation of units and mixed units from any `Number` type (`Int`, `Long`, `Float`, `Double`, ...), always
  normalized to `Double` internally.
