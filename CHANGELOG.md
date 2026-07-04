# kunit Changelog

## [Unreleased]

### Added

- New **constructed** unit group **Speed** (`org.pcsoft.framework.kunit.speed`) — the first composed
  quantity (length·time⁻¹):
  - Units: meter per second (base), kilometer per hour, mile per hour, knot, foot per second, Mach
    (ISA sea-level speed of sound, `Ma = 340.29 m/s`) and the speed of light (`c = 299 792 458 m/s`),
    with matching `Number.metersPerSecond` … `Number.speedOfLight` creators, bare `val` aliases and
    the 24 SI-prefix `infix` constructors (`5 kilo metersPerSecond`).
  - `KSpeedUnitInstance` stores a two-term `[m¹, s⁻¹]` mixed instance normalized to m/s and offers the
    standard "pure" unit surface (`value`, `valueAs`, `+`/`-`/`*`/`/`, comparisons, `toString`,
    `toKMixedUnitInstance`), reading back either as a whole speed unit or as a length-per-time pair,
    plus `KMixedUnitInstance.toKSpeedUnit()`.
  - **Direct cross-group operators** so the core units combine straight into a typed speed and back,
    without touching a raw `KMixedUnitInstance`: `length / time → speed`, `speed * time → length`,
    `time * speed → length`, `length / speed → time`. Non-speed shapes (e.g. `area / time`) fail with
    `IllegalStateException` rather than producing a misleading value.
  - Dedicated MkDocs page (`docs/docs/units/speed.md`) with Korean, Chinese and Japanese translations,
    under a new "Constructed Units" navigation section, including a thorough "computing with the core
    units" section.
- Comprehensive **parameterized cross-matrix tests** for every unit group and the mixed unit, built on
  JUnit Jupiter `@ParameterizedTest`/`@MethodSource` (new `junit-jupiter-params` test dependency): a full
  prefix × unit matrix plus one standalone test per SI prefix, a unit → every-other-unit conversion
  matrix, one method per operator (`+`/`-`/`*`/`/`) and per comparison (`==`/`!=`/`<`/`<=`/`>`/`>=`) over
  every unit pair, per-unit `toString`/`toString(target)`, and a length × time cross-group matrix for the
  mixed unit. The procedure is documented in `CLAUDE.md` as mandatory for future groups.
- Per-group SI-prefix `infix` constructors (`KLengthUnitPrefix.kt`, `KTimeUnitPrefix.kt`): `5 kilo meters`
  now returns a `KLengthUnitInstance` directly (and `5 milli seconds` a `KTimeUnitInstance`), rather than an
  intermediate builder. `5 kilo meters` is exactly equivalent to `5000.meters` and is the preferred
  construction form.
- Test coverage in `KMixedUnitInstanceTest` verifying that `KMixedUnitInstance` term exponents are
  added on `*` and subtracted on `/` — including crossings of the 0-point (negative→positive and
  positive→negative) and the removal of a term whose exponent cancels to exactly `0` — both for pure
  terms and when combining with other mixed units.

- New light-distance units in the **Length** group: light-second (`ls`), light-minute (`lmin`),
  light-hour (`lh`), light-day (`ld`) and light-week (`lw`), complementing the existing light-year.
  Defined via the speed of light (`c = 299 792 458 m/s`) and the Julian time base, with matching
  `Number.lightSeconds` … `Number.lightWeeks` creators and bare `val` aliases.
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

- **Breaking:** the number-extension creators are now extension **properties** instead of functions —
  construct pure units with `5.meters`, `2.hours`, `5.hectares` (previously `5.meters()`, `2.hours()`,
  `5.hectares()`). The `toXxx()` conversions (`toKMixedUnitInstance()`, `toKLengthUnit()`,
  `toKTimeUnit()`, `toDuration()`) remain functions.
- **Breaking:** the prefix `infix` functions are now declared **per group** (over the group's own unit
  type) and return the concrete "pure" unit directly, replacing the single set of generic root-package
  functions that returned a `KPrefixBuilder`. `(5 kilo meters).toKMixedUnitInstance().toKLengthUnit()`
  becomes simply `5 kilo meters`.
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

### Removed

- **Breaking:** the `KPrefixBuilder` class and the generic, root-package prefix `infix` functions that
  returned it. Prefix construction now goes through the per-group functions that return the concrete unit
  directly (see _Changed_).

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
