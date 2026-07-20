---
name: Operators
description: Operator functions all units MUST HAVE
---

## Operators

All required operator functions are available for every unit (mostly through the shared engine
`KMixedUnitInstance` and the `KUnitMeasurable`/`KUnitInstance` surfaces):
* add (+)
* subtract (-)
* multiply (*)
* divide (/)

### Add (+) and Subtract (-)

`+` and `-` are **same-type** only and live on the "pure" wrapper via `KUnitInstance<SELF>` (they combine
two values of the same group and exponent, converting units automatically). They are therefore only
present on **dimension-sharp (leaf) types** - a type with per-exponent specializations exposes `+`/`-` on
the specialized leaves (e.g. `KLengthUnitInstance`), **not** on the open base type
(`KDistanceUnitInstance`). This makes a cross-dimension combination such as `length + area` a **compile
error** rather than a runtime failure.

### Multiply (*) and Divide (/)

`*` and `/` are **always allowed** and may change the dimension. The result type depends on whether the
combination forms a known unit:

* A **meaningful, standardized** combination returns a **typed** instance, via a more specific operator:
  * `length / time = speed` (`KSpeedUnitInstance`)
  * `length * length = area` (`KAreaUnitInstance`)
  * `storage / time = data rate` (`KDataRateUnitInstance`)
* Any **other / non-meaningful** combination (e.g. `meter * byte`) returns a generic
  **`KMixedUnitInstance`** - the union of both sides' terms with exponents added (`*`) or subtracted
  (`/`); a resulting exponent of `0` drops that unit.

The typed operators are declared narrowly (member or specific extension) so Kotlin's overload resolution
prefers them; the generic `KUnitInstance.times`/`div` extension is the fallback for everything else.

### Pow

The special method `pow` is an infix method so the corresponding exponential functions can be called:
* `(3 of meters) pow 2` = `3 m` raised to `Meter ^ 2`

It is implemented for the mixed unit (all exponents of all terms are multiplied by `n`) and, where a
dimensioned result exists, typed on the group (e.g. `KDistanceUnitInstance.pow` returns a distance whose
exponent follows the result: `(2 of meters) pow 2` is a `KAreaUnitInstance`). `pow` binds **weaker** than
`* / + -`, so parenthesize in mixed expressions.
