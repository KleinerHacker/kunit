---
name: Operators
description: Operator functions all units MUST HAVE
---

## Operators

All required operator functions are created for every unit:
* add (+)
* subtract (-)
* multiply (*)
* divide (/)

These MUST return the unit itself as the result when:
* The involved operands represent the same unit

These MUST return a `KMixedUnitInstance` as the result when:
* The involved operands represent different units (only in combination with this unit)

### Pow

The special method `pow` is created as an infix method so that the corresponding exponential functions can be called:
* `3.meter pow 2` = 3 Meter ^ 2

This method must be implemented for every specific unit and for the mixed unit. For the mixed unit, all exponents
of all units are calculated according to the rule.
