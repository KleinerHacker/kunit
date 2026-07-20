---
name: Tests
description: New tests for new units
---

# Test Structure

The package structure is mirrored.

In accordance with the rule, the following classes exist along with their tests:
* `K<UnitName>UnitSystemTest` (tests for all contents of the `K<UnitName>UnitInstance` class)
* `K<UnitName>OperatorTest` (tests for all operators, some of which are also contained in `K<UnitName>UnitInstance`)
* `K<UnitName>UnitTest` (tests for the use of all `K<UnitName>UnitExtensions` as well as `K<UnitName>UnitBaseValues`)
* `K<UnitName>PrefixTest` (tests for the use of all prefixes together with the unit)

## Special Case: Exponent Specialization

If a class is specialized to an exponent value (e.g. Area), the tests MUST additionally exist separately for each
specialization.

## Miscellaneous

For the overarching mixed unit, the test classes above MUST exist at the package root level.
