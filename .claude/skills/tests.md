---
name: Tests
description: New tests for new units
---

# Test Structure

The package structure is mirrored.

`<UnitName>` is the **group name** (e.g. `Distance`, `Time`, `Storage`, `Speed`, `DataRate`), matching
`K<UnitName>Unit` / `K<UnitName>UnitInstance`. In accordance with the rule, the following classes exist
along with their tests:
* `K<UnitName>UnitSystemTest` (tests for all contents of the `K<UnitName>UnitInstance` class:
  `of`/`into`, `scaledBy`/type preservation, `equals`/`hashCode`/`toString`, narrowing, `pow`, interop)
* `K<UnitName>OperatorTest` (tests for all operators - `+`/`-`/`compareTo`/`*`/`/`, typed transitions and
  the generic fallback, incompatible-dimension failures)
* `K<UnitName>UnitTest` (tests for the use of all `K<UnitName>UnitExtensions` as well as
  `K<UnitName>UnitBareValues`, incl. the conversion matrix and special/derived units)
* `K<UnitName>PrefixTest` (tests for the use of all prefixes together with the unit; incl. binary IEC
  prefixes where they exist)

Prefer parameterized tests (`@ParameterizedTest` / `@MethodSource`, with
`@TestInstance(Lifecycle.PER_CLASS)`) for the unit/prefix/operator matrices.

## Only Required Where the Logic Exists

A test class is only created when the corresponding logic actually exists. If a group has no such logic,
that class is omitted:
* A composed unit without its own prefix builders (speed, data rate) has **no** `K<UnitName>PrefixTest`.
* The open root mixed unit (no specific units, no prefixes of its own) has only `KMixedUnitSystemTest`
  and `KMixedUnitOperatorTest`.

## Special Case: Exponent Specialization

If a class is specialized to an exponent value (e.g. Area), the tests MUST additionally exist separately
for each specialization.

## Miscellaneous

For the overarching mixed unit, the applicable test classes above exist at the package root level.

## Formatter

Each formatter implementation MUST have a corresponding test class, named `K<UnitName>FormatterTest`.
