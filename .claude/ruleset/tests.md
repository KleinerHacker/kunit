---
description: tests for new units
---

# Test Structure

All packages are mirrored into the test folder. For every unit the following tests must exist:

* For the unit (the system) itself
* For all specific units of the system
* For all prefix units
* For any special mixed units
* At least one for an open mixed unit

A given test category is only required when the corresponding logic actually exists. If a unit has no logic for a
category, that test class is omitted - e.g. a composed unit without its own prefix builders (speed, data rate) has no
prefix test, and the open root mixed unit (which has no specific units or prefixes of its own) is tested only for the
system and its operators.

The tests of a given unit are always placed under the corresponding package.

# Coverage

Test coverage must reach 100% at all times. If it does not, the tests must be extended appropriately to cover
what is missing.

100% tool-reported coverage is NOT sufficient on its own: every public API member (builders, enum entries,
constants, factories) MUST be verified by a real assertion.

Rationale: top-level `val` initializers and enum entries run in the class `<clinit>` at class-loading, so a
coverage tool marks them "covered" even when NO test ever reads or asserts their value - such unused public
API stays effectively untested despite 100%.

Therefore: unused/open public API MUST have its own assertion (e.g. every prefix builder, not just a
representative subset).

# Coverage

Test coverage must reach 100% at all times. If it does not, the tests must be extended appropriately to cover
what is missing.

100% tool-reported coverage is NOT sufficient on its own: every public API member (builders, enum entries,
constants, factories) MUST be verified by a real assertion.

Rationale: top-level `val` initializers and enum entries run in the class `<clinit>` at class-loading, so a
coverage tool marks them "covered" even when NO test ever reads or asserts their value - such unused public
API stays effectively untested despite 100%.

Therefore: unused/open public API MUST have its own assertion (e.g. every prefix builder, not just a
representative subset).

# Decompositions

A standardized unit with multiple decompositions MUST have an explicit test per decomposition. The tests MUST
prove that all decompositions yield the same typed, value-equal result. The decomposition rules themselves are
defined in `.claude/ruleset/operators.md`.
