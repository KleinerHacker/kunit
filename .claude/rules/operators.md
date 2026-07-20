---
description: Required operators for new and existing units
---

# Operators

Every unit MUST always support the following operators for itself:
* add (+)
* subtract (-)
* multiply (*)
* divide (/)

## Add (+) and Subtract (-)

When combining units, the following MUST be observed:
* All units on both sides are identical
* The exponent of every unit is identical on both sides

If this is not the case, the operation results in an error.

## Multiply (*) and Divide (/)

When combining units, the following MUST be observed:
* For multiplication, the exponents of all units must be added
* For division, the exponents of all units must be subtracted
* The following generally applies:
  * If a unit is not present, it MUST be assumed to have an exponent of 0 (i.e. 0 + x or 0 - x)
  * If, after the calculation, a unit's exponent is 0, that unit MUST be removed

## Special Operators

### Pow

It must be possible to modify a unit arbitrarily using an exponential function (example):
* (Meter ^ 2) ^ 2 = Meter ^ 4 (result of 2*2)
* (Meter ^ -2) ^ 2 = Meter ^ -4 (result of -2*2)

This also applies to mixed units. Here the calculation affects all existing exponents of all units
according to the mathematical rule.
