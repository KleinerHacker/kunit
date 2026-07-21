---
name: create-unit
description: Create basic classes for new unit
---

# Creating a New Unit

The whole library is built on the `of` / `into` DSL: a **value-1 unit template** (an
instance of the unit worth exactly `1`) is scaled by a number to construct a value (`10 of meters`)
and used as the target to read a value back (`v into kilo.meters`). A new unit therefore contributes
the instance type, the unit enumeration, and the value-1 templates (bare + prefixed).

## Core Abstractions (framework-wide, reuse - do not recreate)

These already exist in the root package `org.pcsoft.framework.kunit` and every new unit builds on them:

* `KUnit` - a single physical unit of a group (carries `symbol` and `baseValue`). Concrete groups
  implement it as an `enum class`.
* `KMixedUnitInstance` + `KUnitTerm` - the generic engine: a numeric value together with a list of
  `(KUnit, exponent)` terms. It performs all arithmetic (`times`/`div`/`plus`/`minus`/`pow`).
* `KUnitMeasurable` - the common surface of every value: `value` (normalized `Double`), `toUnit()`
  (conversion into the generic engine), `scaledBy(factor)` (the primitive behind `of`),
  `readBaseValue(baseValue)` (the primitive behind `into`), and the mixed `times`/`div`.
* `KUnitInstance<SELF>` - the self-typed surface of a "pure" wrapper: same-type `plus`/`minus`/
  `compareTo`.
* `Number.of(template)` / `KUnitMeasurable.into(target)` - the single construction / reading verbs.

## Main Class

A class following the naming scheme `K<UnitName>UnitInstance` is created for the new unit. It wraps a
`KMixedUnitInstance` (via `KUnitMeasurable by instance` delegation) and adds the self-typed surface
(`KUnitInstance<K<UnitName>UnitInstance>`: `plus`/`minus`/`compareTo`) as well as `scaledBy` returning
its own type (so `of` preserves the strong result type).

It MUST also implement **value-based** `equals`/`hashCode`/`toString` following the existing units:
`equals`/`hashCode` on the normalized base value (equal quantity ⇒ equal, regardless of construction
unit), `toString` delegating to the underlying `KMixedUnitInstance` (base-unit rendering).

### Non-linear (affine) groups

If the group's conversion is not a single multiplicative factor (offset-and-scale, e.g. temperature),
do **not** touch the shared engine and do **not** add `of`/`into` overloads (a generic verb imported
explicitly would shadow them and silently misconvert). Store the **absolute base value** and inject the
transform through the two hooks instead: override `scaledBy` (construction, behind `of`) and
`readBaseValue` (reading, behind `into`). Carry the construction unit on the instance so the value-1
templates know which transform to apply. This is a deliberate, contained exception.

## Unit Enumeration

An enumeration `K<UnitName>Unit` is created that implements `KUnit` and contains all existing units:
* Symbol for the unit (e.g. `m` for meter, ...), `symbol`
* Relative value compared to the base value, `baseValue`
* The base value itself as a retrievable property of the companion (static), `BASE`

## Extensions (Prefixed Templates)

A file `K<UnitName>UnitExtensions.kt` is created. It contains the **prefixed, value-1 templates** as
**extension properties on the prefix builder** (`KPrefixBuilder`, or the augmenting/diminishing subtype -
see the prefix skill), never on `Number`:
* One property per concrete unit, e.g. `val KPrefixBuilder.meters: K<UnitName>UnitInstance`, so that
  `kilo.meters` / `milli.meters` yield value-1 templates for use with `of`/`into`.
* Optionally, helper functions related to unit calculation.

## Bare Values (Templates)

The file `K<UnitName>UnitBareValues.kt` is created. It contains the **bare, value-1 templates** as
top-level `val`s (each = 1 unit, normalized to the base unit):
* For every concrete unit, e.g. `val meters: K<UnitName>UnitInstance` (= 1 meter).
* These are the vocabulary for both building (`10 of meters`) and reading (`v into miles`).

## Conversions from the Engine

Provide `KMixedUnitInstance.to<UnitName>()` to convert a matching single-term mixed unit back into the
"pure" wrapper (and, for exponent specializations, narrowing helpers like `toLength`/`toArea`).

## Operators (Optional file)

For **standardized / composed** units whose value arises from combining other groups (e.g. speed =
length / time, data rate = storage / time), create a `K<UnitName>UnitOperators.kt` file holding the
typed cross-group operators (e.g. `KLengthUnitInstance.div(KTimeUnitInstance): KSpeedUnitInstance` and
the inverse decompositions). It lives in the composed unit's package (which may depend on its component
packages, never the reverse). See the operator skill for the return-type rules.

## Concrete Exponent Units (Optional)

Only when explicitly requested, additional units per exponent value (bounded by user input) are to be
created:
* For every specific unit, the part above is implemented
* For the unit system (independent of the exponent), only `K<UnitName>Unit` and `K<UnitName>UnitInstance`
  are implemented
