# kunit Changelog

## [Unreleased]

### Added

- Test coverage in `KMixedUnitInstanceTest` verifying that `KMixedUnitInstance` term exponents are
  added on `*` and subtracted on `/` — including crossings of the 0-point (negative→positive and
  positive→negative) and the removal of a term whose exponent cancels to exactly `0` — both for pure
  terms and when combining with other mixed units.

- New light-distance units in the **Length** group: light-second (`ls`), light-minute (`lmin`),
  light-hour (`lh`), light-day (`ld`) and light-week (`lw`), complementing the existing light-year.
  Defined via the speed of light (`c = 299 792 458 m/s`) and the Julian time base, with matching
  `Number.lightSeconds()` … `Number.lightWeeks()` creators and bare `val` aliases.
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
- **Breaking:** the `KMixedUnitInstance` primary constructor is now `internal`, matching the "pure"
  wrapper classes (`KLengthUnitInstance`, `KTimeUnitInstance`). Externally, mixed units may only be
  obtained via `toKMixedUnitInstance()`, operator results, or the number-extension creators — never by
  constructing `KMixedUnitInstance(...)` directly.

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
