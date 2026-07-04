# Overview

* Calculation with units
* The basis is always a "mixed unit"
  * This is a unit composed of several units
* Enables calculations in physical contexts with real units in Double

# Architecture

* `KMixedUnitInstance` - Represents a mixed unit.
  * Consists of a Double (base value)
  * Consists of one or more `KUnit`s, each as a pair with its exponent
    * The exponent is an Integer, which is positive (for the numerator) or negative (for the denominator)
    * The `KUnit`s are multiplied with each other
* `KUnit` - Represents a unit.
  * Is an **interface** (not a class), since concrete units per group are represented as `enum class ... : KUnit`
    (enums cannot extend classes in Kotlin, but can implement interfaces)
  * Consists of a String (symbol)
  * Consists of a Double (base value), the conversion factor to the base unit of the group
  * Belongs to a group of units, e.g. Length (which then includes e.g. Metric, Miles, Yards, ...)
    * For each "pure" unit, a dedicated `enum class` is used (e.g. `KLengthUnit`)
    * A group explicitly declares its base unit (e.g. `KLengthUnit.BASE`)
* A `KMixedUnitInstance` is wrapped for each group of units for "pure units"
  * The wrapper classes (e.g. `KLengthUnitInstance`) encapsulate a `KMixedUnitInstance` via delegation
    (no inheritance relationship) and always store their value **normalized to the base unit of the group**
  * A wrapper class is not necessarily limited to exponent 1 - it can also encapsulate derived quantities
    of the same group with a different exponent (e.g. area = exponent 2, volume = exponent 3 for length).
    The rules for `+`/`-`/comparison operators (only allowed for the same group **and** the same exponent,
    otherwise `IllegalStateException`) apply for each exponent, not only for exponent 1
* **Common interface hierarchy** (root package) for every unit-value type:
  * `KUnitMeasurable` - the group-agnostic surface shared by **all** values (`value`,
    `toKMixedUnitInstance()`, `*`/`/` against a `KMixedUnitInstance`). `KMixedUnitInstance` implements it
    directly; the "pure" wrappers implement it via Kotlin `by` delegation to their internal
    `KMixedUnitInstance` (so `value`/`times`/`div`/`toKMixedUnitInstance` are never hand-written there)
  * `KUnitInstance<SELF : KUnitInstance<SELF>>` - the self-typed (F-bounded) interface of the "pure"
    wrappers, extending `KUnitMeasurable` with same-type `+`/`-`/comparison, same-group `*`/`/`, and
    single-target `valueAs`/`toString`. Each wrapper implements `KUnitInstance<ItsOwnType>`
* **Immutability invariant**: every instance type (`KMixedUnitInstance`, the "pure" wrappers, and any
  future wrapper) is strictly immutable - only `val` state, no mutable fields; every operator/conversion
  returns a **new** instance
* **Creation invariant**: the "pure" wrapper constructors are `internal`. Concrete units may **only** be
  created via number-extension creator **properties** (e.g. `5.meters`, `2.hours` - `val Number.meters`,
  not a function), the group prefix `infix`
  constructors (e.g. `5 kilo meters`), operator results, or the conversion extensions
  (`KMixedUnitInstance.toXxxUnit()`, `Duration.toKTimeUnit()`). A wrapper must
  **never** be constructed directly from a `KMixedUnitInstance` by callers - the only `KMixedUnitInstance`→
  wrapper path is the `toXxxUnit()` extension
* SI prefixes (the complete SI prefix table, from Quetta/Q to Quecto/q) are not part of
  `KUnit`/the (KUnit, exponent) pair, since they are only relevant when reading/writing values. They are
  represented via a generic `KUnitPrefix` enum (root package)
  * The prefix `infix` functions for construction (e.g. `5 kilo meters`) are **defined per group**, in
    each unit sub-package, over that group's own unit type (e.g. `Number.kilo(KLengthUnit): KLengthUnitInstance`
    in `KLengthUnitPrefix.kt`, `Number.kilo(KTimeUnit): KTimeUnitInstance` in `KTimeUnitPrefix.kt`). Each
    returns that group's concrete "pure" unit **directly** (`5 kilo meters` is a `KLengthUnitInstance`, not
    an intermediate) - this is the **preferred** construction form. `5 kilo meters` is exactly equivalent to
    `5000.meters`.
    * Rationale: the wrapper classes live in the sub-packages, so only the group package can build its
      concrete wrapper. Returning the concrete type (rather than a group-agnostic intermediate that the
      caller must convert with `toXxxUnit()`) keeps the call site free of boilerplate. The cost is that the
      24 prefix functions are declared once per group instead of once globally - accepted deliberately, and
      consistent with each group already owning its own creator/`val`-alias DSL.
    * There is intentionally **only one** prefix construction syntax: the bare-unit form `5 kilo meters`
      (`meters` is the `KLengthUnit` alias). No `KPrefixBuilder` intermediate and no `5 kilo meters()`
      wrapper-literal variant exist - a single, unambiguous way to write it.
    * For Duration-backed groups (e.g. `time`), the result is subject to that wrapper's representable range
      (see `KTimeUnitInstance`): extreme prefixes on a multi-unit base (e.g. `5 quetta seconds`) exceed
      `java.time.Duration`'s `Long`-seconds range and are not representable
* For certain combinations of unit group and exponent, **special units** exist with their own
  name/symbol and their own conversion factor (e.g. hectare for area = length², liter for volume = length³).
  These do **not** replace the normal mechanism (e.g. the base unit with exponent 2 remains the
  "raw" representation of an area) - they are purely additional, group- and exponent-bound conversion targets
  for input/output, generic over the referenced unit type (compile-time group safety), combinable analogously
  to the prefixes
* Creation (only constructor and creator extension properties) of `KMixedUnitInstance`/the wrapper classes is possible
  from any `Number` type (`Int`, `Long`, `Float`, `Double`, ...), not only `Double`; internally it is always
  normalized to `Double`. All outputs (value, conversions, text representation) are, without exception, `Double`.
  Operators and comparison operators, on the other hand, **never** work directly with raw `Number` values - only
  between two unit types

## Package Structure

* The root package is called `org.pcsoft.framework.kunit`
* A sub-package is created for each "pure" unit
* The base classes `KUnit` and `KMixedUnitInstance` are located in the root package

## Naming Scheme

* All public types (classes, interfaces, enums, objects) start with `K` project-wide - in the
  root package (`KUnit`, `KMixedUnitInstance`, `KUnitMeasurable`, `KUnitInstance`, `KUnitPrefix`,
  `KDerivedUnit`, ...) just as
  in every sub-package (e.g. `KLengthUnit`, `KLengthUnitInstance`, `KLengthDerivedUnit` in `length`)
* Extension properties/functions and bare `val` aliases (DSL vocabulary such as the `meters` creator property, `kilo`, and the `meters` unit alias) are
  exempt from this rule - they remain named in a language-natural way

# Implementation

## Coding Style

* Use `val` throughout - for properties **and** local variables. Express accumulations functionally
  (`fold`/`map`/`reduce`) rather than mutating a `var`. Only fall back to `var` where it is genuinely
  unavoidable. This complements the immutability invariant (see Architecture)

## Documentation

* Every public member must be documented in English
* The documentation should be formatted in Markdown
* The documentation should be comprehensive and, where useful, contain examples
  * especially for operators

### MkDocs site

* Every new unit group must ship a dedicated MkDocs page at `docs/docs/units/<group>.md`, following the
  existing `docs/docs/units/length.md` as the template (units table, operators, comparisons, SI prefixes,
  `toString` formatting, mixing with other units, plus any group-specific sections)
* The page must be provided in **every** language the site supports (the `mkdocs-static-i18n` suffix
  structure: the default `<group>.md` plus `<group>.ko.md`, `<group>.zh.md`, `<group>.ja.md`), mirroring
  the translation coverage of the `length` page
* `docs/mkdocs.yml` must be updated accordingly: add a `nav` entry under `Predefined Units` and a matching
  label in every locale's `nav_translations`

### Changelog

* `CHANGELOG.md` must always be kept up to date: every change (new unit groups, features, fixes, breaking
  changes, ...) is recorded under the `[Unreleased]` section, grouped as `Added`/`Changed`/`Fixed`/`Removed`,
  as part of the same change - never leave it for later

## Operators

* All standard operators '+', '-', '*', '/' must be supported for:
  * "pure" units
  * mixed units
  * mixing "pure" units and mixed units
* All standard comparison operators '==', '!=', '<', '<=', '>', '>=' must be supported for:
  * "pure" units
  * In addition to the classic equals, there must be a method to check the unit (`KUnit` + exponent)
    for mixed units
* Both `KMixedUnitInstance` and the "pure" wrapper classes offer, in addition to the normalized raw value, a
  way to read a converted value for a desired target unit, as well as a `toString` overload that
  takes this target unit(s) into account in the text output. Target units can be a pure unit or
  a unit scaled by a prefix/special unit

### Error Handling

* For comparisons:
  * If there are differences in the `KUnit` or their exponents, an error must be thrown: `IllegalStateException`

## Conversion

* Every "pure" unit offers, via an extension method, a way to convert it into a `KMixedUnitInstance`
* When calculating with the same "pure" unit, that same unit is returned again
* When calculating with different "pure" units, a new `KMixedUnitInstance` is returned
* When calculating a "pure" unit with a mixed unit, or mixed units with each other, new `KMixedUnitInstance`s are returned

### Error Handling

* Every conversion to a "pure" unit must check whether it is also present in a mixed unit
  * If not: `IllegalStateException`
  * The term's **exponent is irrelevant** to this group check: a term is time-typed / length-typed
    purely by its `KUnit` group, regardless of exponent (even a "square second" is still a time unit).
    `KMixedUnitInstance.toKTimeUnit()` therefore accepts any single `KTimeUnit` term (any exponent) and
    just wraps the numeric value as a `Duration` - it must **not** reject non-1 exponents
* Calculations with '*' are always allowed
  * For every unit that is already present, both exponents are added
  * For every unit that is not yet present, a new one is created in `KMixedUnitInstance` with exponent 1
* Calculations with '/' are always allowed
  * For every unit that is already present, both exponents are subtracted
  * For every unit that is not yet present, a new one is created in `KMixedUnitInstance` with exponent -1
* Calculations with '+' or '-' are only allowed if
  * Two "pure" units (wrapper classes such as `KLengthUnitInstance`) of the same unit group are calculated
    (e.g. meter + mile is allowed, automatic conversion via normalization) **and** have the same
    exponent (e.g. area must not be calculated with volume)
  * Two mixed units (`KMixedUnitInstance`) are calculated with each other if, for every term on one side, there
    is exactly one term on the other side belonging to the same unit group with the same exponent
    (order-independent) - the `KUnit`s themselves do not need to match, since matching terms are
    automatically converted via normalization (analogous to the "pure" wrapper classes)
    * Even with matching unit groups, if any pair of matching terms has different exponents, the operation fails
    * If a term has no matching unit group on the other side (e.g. mixing length with time), the operation fails
    * Result: `IllegalStateException`

## Tests

* Every "pure" unit is tested separately
  * Full tests for the most complete possible test coverage
  * Complete tests for all operations
  * Every operator function ('+', '-', '*', '/') and every comparison operation ('==', '!=', '<', '<=', '>', '>=')
    is tested at least once per type with a success case and, where an error is intended, at least once with
    the corresponding error case (`IllegalStateException`) - it is not enough to test only one operator
    representatively for all of them
  * For every unit defined in a group and every special unit, a dedicated test exists per prefix,
    verifying the combination of prefix and unit (construction + back-conversion) - a complete
    prefix-x-unit matrix, not spot checks. In addition, at least one standalone test independent of
    the respective unit is added for each individual prefix
* The mixed units are tested
  * Composed with at least one other unit each
  * Every "pure" unit is tested together with a mixed unit
  * For a "pure" unit that itself consists of a mixed unit (e.g. Newton), a test exists that
    calculates to this unit, or from the unit to another "pure" unit

Fundamentally, all tests verify the correctness of the values and calculations.

### Parameterized cross-matrix test procedure (mandatory for every unit group)

Every unit group (and the mixed unit) is covered by **real parameterized tests** — JUnit Jupiter
`@ParameterizedTest` with `@MethodSource` (the `junit-jupiter-params` dependency), the test class
annotated `@TestInstance(TestInstance.Lifecycle.PER_CLASS)` so the `@MethodSource` provider methods
can be non-static instance methods. Assertions stay `kotlin.test.*` with explicit deltas (a relative
tolerance for the wide magnitude span); instances are built through the group's creator **properties**
via a shared `internal val xxxUnitGenerators: List<Pair<(Number) -> KXxxUnitInstance, KXxxUnit>>` list
and a `xxxOf(unit, n)` helper. For each new group provide, each as its **own** parameterized method:

* **Prefix × unit** — every SI prefix combined with every unit of the group (construction +
  back-conversion round trip), **plus** one standalone test per individual prefix (all 24). For
  `Duration`-backed groups (e.g. time) restrict the prefix × unit matrix to the representable band and
  apply the standalone tests to a `1 / factor` count so every prefix stays representable.
* **Conversion matrix** — every unit converted into every other unit of the same group (`valueAs`),
  checked against `n * from.baseValue / to.baseValue`.
* **Operator matrix** — one parameterized method **per operator** (`+`, `-`, `*`, `/`), each covering
  every unit against every other unit of the group (value + resulting exponent/`units` verified).
* **Comparison matrix** — one parameterized method **per comparison operator** (`==`, `!=`, `<`, `<=`,
  `>`, `>=`), each covering every unit against every other unit, expectations derived from the
  normalized base values. Plus representative `IllegalStateException` cases (e.g. mixing exponents).
* **`toString`** — `toString()` (default/base unit) and `toString(target)` for every unit, including a
  scaled (prefixed) target and, where applicable, a derived-unit target.
* The same matrices apply to the group's **derived units**, and the mixed unit is exercised with a
  **cross-group** matrix (every unit of one group against every unit of another, e.g. length × time).

## Implementation

The implementation status is documented in STATUS.md.
