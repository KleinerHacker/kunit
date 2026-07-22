---
description: Required operators for new and existing units
---

# Operators

Every unit MUST always support the following operators:
* add (+)
* subtract (-)
* multiply (*)
* divide (/)

## Add (+) and Subtract (-)

When combining units, the following MUST be observed:
* All units on both sides are identical
* The exponent of every unit is identical on both sides

If this is not the case, the operation results in an error.

`+` and `-` are same-type operations. For a unit that has **per-exponent specializations** (see
`architecture-unit.md`), `+`/`-` MUST live on the specialized (leaf) types only; the open base type
(which may hold any exponent) MUST NOT provide them, so that combining different dimensions of the same
group (e.g. length + area) fails at compile time rather than at runtime.

## Multiply (*) and Divide (/)

When combining units, the following MUST be observed:
* For multiplication, the exponents of all units must be added
* For division, the exponents of all units must be subtracted
* The following generally applies:
  * If a unit is not present, it MUST be assumed to have an exponent of 0 (i.e. 0 + x or 0 - x)
  * If, after the calculation, a unit's exponent is 0, that unit MUST be removed

Result type: if the combination yields a known, standardized unit (e.g. length / time = speed,
length * length = area, storage / time = data rate), the operation MUST return that **typed** unit. Any
other, non-meaningful combination (e.g. meter * byte) MUST return a generic mixed unit.

### Multiple decompositions of a standardized unit

A standardized unit MAY be reachable through several equivalent decompositions (example: resistance =
`voltage / current` = `mass * distance ^ 2 / (time ^ 3 * current ^ 2)`). All of them describe the same
physical quantity and MUST yield the same typed value. The following rules apply:

* All decompositions MUST funnel into **one** normalizing factory (e.g. `resistanceInstanceOf`) that
  stores the value in the group's canonical **base-dimension normal form**.
* A decomposition whose operands are already typed (e.g. `voltage / current`) is realised as an
  overloaded operator returning the typed unit directly.
* A decomposition expressed from native base units (the fully generic form, e.g.
  `mass * distance ^ 2 / (time ^ 3 * current ^ 2)`) stays a generic mixed unit and is converted to the
  typed unit through the group's `toX()` form-recognition hook (mirroring `toSpeed()`); the shared
  engine MUST NOT be changed and MUST NOT learn about the standardized groups.
* `toX()` recognises ONLY the canonical base-dimension normal form; any equivalent expression reduces
  onto it automatically.
* Decompositions are **additive**: a new decomposition adds only one operator (typed operands) or is
  already covered by `toX()` (native form). It MUST NOT require changing existing decompositions.

## Special Operators

### Pow

It must be possible to modify a unit arbitrarily using an exponential function (example):
* (Meter ^ 2) ^ 2 = Meter ^ 4 (result of 2*2)
* (Meter ^ -2) ^ 2 = Meter ^ -4 (result of -2*2)

This also applies to mixed units. Here the calculation affects all existing exponents of all units
according to the mathematical rule.
