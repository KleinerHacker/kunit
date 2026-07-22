---
description: Architecture for newly created units
---

# Architecture

Fundamentally, all units can always be represented as mixed units.

## Units

### Mixed Unit Systems

A mixed unit is a unit composed of several unit components (examples):
* kilometer / hour
* meter * gram

### Native Unit Systems

These are units that are inherently measurable base units (example):
* length
* weight
* time
* temperature

They can generally be contained within mixed units. In that case they are "pure" units that consist of only a
single unit.

### Standardized Unit Systems

Every mixed unit can represent a standardized unit (example):
* kilometer / hour = speed
* meter / second ^ 2 = acceleration

They can generally be contained within mixed units.

#### Multiple decompositions

A standardized unit MAY be composed from several equivalent decompositions, all describing the same
physical quantity (example - resistance):
* voltage / current
* mass * distance ^ 2 / (time ^ 3 * current ^ 2)

Every standardized group has ONE canonical **base-dimension normal form** (resistance:
`mass ^ 1 * distance ^ 2 * time ^ -3 * current ^ -2`). All decompositions reduce onto this normal form
and MUST produce the same typed, value-equal instance. New decompositions are added additively (see
`operators.md`, "Multiple decompositions of a standardized unit") without changing existing ones.


### Specific Units of a Unit System

These must contain all supported units per unit system. This includes in particular:
* Symbol for the unit
* Relative value compared to the unit's standard value
* The standard unit must be marked (see skill)

### Value Semantics (equals / hashCode / toString)

Every `K<UnitName>UnitInstance` MUST provide `equals`, `hashCode` and `toString` **value-based**,
following the existing units as the reference:
* `equals`/`hashCode` compare the **normalized base value** (two values that represent the same
  physical quantity are equal, independent of the unit they were constructed with, e.g.
  `1.bytes == 8.bits`, `(0 of celsius) == (273.15 of kelvin)`).
* `toString` renders the value in the group's base unit(s) (delegating to the underlying
  `KMixedUnitInstance`).

### Non-linear (affine) Conversions

By default a unit's conversion to its base is a single multiplicative factor (`baseValue`). A unit
group whose conversion is **not** a single factor (offset-and-scale, i.e. *affine* - e.g. temperature:
`°C = K − 273.15`) MUST NOT change the shared engine and MUST NOT add `of`/`into` overloads (those get
shadowed by an explicitly imported generic verb). Instead it stores the **absolute base value** and
injects its transform through the two measurable hooks:
* `scaledBy(factor)` - the construction hook behind `of` (interpret `factor` as a reading in the unit
  and convert it to the base).
* `readBaseValue(baseValue)` - the reading hook behind `into` (convert the normalized base value back
  into the unit's reading).

This keeps `of`/`into` correct for every group and is a deliberate, contained exception.

## Exponents

Every individual unit within a mixed unit carries an exponent. This is normally 1, but it can be increased or
decreased through the corresponding arithmetic operations. For example, Meter ^ -1 = 1/Meter.

If the exponent is 0, the unit is removed.

### Special Cases

In exceptional cases, dedicated units for special exponent handling may exist (example):
* Area (Meter ^ 2)
* Volume (Meter ^ 3)

These are only to be created at the explicit request of the user.
