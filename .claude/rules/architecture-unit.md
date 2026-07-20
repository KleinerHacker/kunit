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


### Specific Units of a Unit System

These must contain all supported units per unit system. This includes in particular:
* Symbol for the unit
* Relative value compared to the unit's standard value
* The standard unit must be marked (see skill)

## Exponents

Every individual unit within a mixed unit carries an exponent. This is normally 1, but it can be increased or
decreased through the corresponding arithmetic operations. For example, Meter ^ -1 = 1/Meter.

If the exponent is 0, the unit is removed.

### Special Cases

In exceptional cases, dedicated units for special exponent handling may exist (example):
* Area (Meter ^ 2)
* Volume (Meter ^ 3)

These are only to be created at the explicit request of the user.
