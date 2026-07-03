# Overview

* Calculation with units
* The basis is always a "mixed unit"
  * This is a unit composed of several units
* Enables calculations in physical contexts with real units in Double

# Architecture

* `KUnitInstance` - Represents a mixed unit.
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
* A `KUnitInstance` is wrapped for each group of units for "pure units"
  * The wrapper classes (e.g. `KLengthUnitInstance`) encapsulate a `KUnitInstance` via delegation
    (no inheritance relationship) and always store their value **normalized to the base unit of the group**
  * A wrapper class is not necessarily limited to exponent 1 - it can also encapsulate derived quantities
    of the same group with a different exponent (e.g. area = exponent 2, volume = exponent 3 for length).
    The rules for `+`/`-`/comparison operators (only allowed for the same group **and** the same exponent,
    otherwise `IllegalStateException`) apply for each exponent, not only for exponent 1
* SI prefixes (the complete SI prefix table, from Quetta/Q to Quecto/q) are not part of
  `KUnit`/the (KUnit, exponent) pair, since they are only relevant when reading/writing values. They are
  represented via a generic `KUnitPrefix` enum (root package)
  * The prefix `infix` functions for construction (e.g. `5 kilo meters`) are **defined generically in the
    root package** (parameterized over `KUnit`, not over a concrete group unit) - not duplicated per group.
    They return an intermediate type `KPrefixBuilder`, **not** a concrete "pure" unit directly, since the
    root package does not know the wrapper classes of the sub-packages. The conversion to the concrete
    "pure" unit happens explicitly via `KPrefixBuilder.toKUnitInstance()` followed by the group-specific
    `KUnitInstance.toXxxUnit()` conversion (e.g. `toKLengthUnit()`)
* For certain combinations of unit group and exponent, **special units** exist with their own
  name/symbol and their own conversion factor (e.g. hectare for area = length², liter for volume = length³).
  These do **not** replace the normal mechanism (e.g. the base unit with exponent 2 remains the
  "raw" representation of an area) - they are purely additional, group- and exponent-bound conversion targets
  for input/output, generic over the referenced unit type (compile-time group safety), combinable analogously
  to the prefixes
* Creation (only constructor and creator extension functions) of `KUnitInstance`/the wrapper classes is possible
  from any `Number` type (`Int`, `Long`, `Float`, `Double`, ...), not only `Double`; internally it is always
  normalized to `Double`. All outputs (value, conversions, text representation) are, without exception, `Double`.
  Operators and comparison operators, on the other hand, **never** work directly with raw `Number` values - only
  between two unit types

## Package Structure

* The root package is called `org.pcsoft.framework.kunit`
* A sub-package is created for each "pure" unit
* The base classes `KUnit` and `KUnitInstance` are located in the root package

## Naming Scheme

* All public types (classes, interfaces, enums, objects) start with `K` project-wide - in the
  root package (`KUnit`, `KUnitInstance`, `KUnitPrefix`, `KDerivedUnit`, `KPrefixBuilder`, ...) just as
  in every sub-package (e.g. `KLengthUnit`, `KLengthUnitInstance`, `KLengthDerivedUnit` in `length`)
* Extension functions and bare `val` aliases (DSL vocabulary such as `meters()`, `kilo`, `meters`) are
  exempt from this rule - they remain named in a language-natural way

# Implementation

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
* Both `KUnitInstance` and the "pure" wrapper classes offer, in addition to the normalized raw value, a
  way to read a converted value for a desired target unit, as well as a `toString` overload that
  takes this target unit(s) into account in the text output. Target units can be a pure unit or
  a unit scaled by a prefix/special unit

### Error Handling

* For comparisons:
  * If there are differences in the `KUnit` or their exponents, an error must be thrown: `IllegalStateException`

## Conversion

* Every "pure" unit offers, via an extension method, a way to convert it into a `KUnitInstance`
* When calculating with the same "pure" unit, that same unit is returned again
* When calculating with different "pure" units, a new `KUnitInstance` is returned
* When calculating a "pure" unit with a mixed unit, or mixed units with each other, new `KUnitInstance`s are returned

### Error Handling

* Every conversion to a "pure" unit must check whether it is also present in a mixed unit
  * If not: `IllegalStateException`
* Calculations with '*' are always allowed
  * For every unit that is already present, both exponents are added
  * For every unit that is not yet present, a new one is created in `KUnitInstance` with exponent 1
* Calculations with '/' are always allowed
  * For every unit that is already present, both exponents are subtracted
  * For every unit that is not yet present, a new one is created in `KUnitInstance` with exponent -1
* Calculations with '+' or '-' are only allowed if
  * Two "pure" units (wrapper classes such as `KLengthUnitInstance`) of the same unit group are calculated
    (e.g. meter + mile is allowed, automatic conversion via normalization) **and** have the same
    exponent (e.g. area must not be calculated with volume)
  * Two mixed units (`KUnitInstance`) are calculated with each other if, for every term on one side, there
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

## Implementation

The implementation status is documented in STATUS.md.
