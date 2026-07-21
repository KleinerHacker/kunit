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

A given test category is only required when the corresponding logic actually exists. If a unit has no
logic for a category, that test class is omitted - e.g. a composed unit without its own prefix builders
(speed, data rate) has no prefix test, and the open root mixed unit (which has no specific units or
prefixes of its own) is tested only for the system and its operators.

The tests of a given unit are always placed under the corresponding package.
