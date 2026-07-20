---
name: create-unit
description: Create basic classes for new unit
---

# Creating a New Unit

## Main Class

A class following the naming scheme `K<UnitName>UnitInstance` is created for the new unit. This class MUST always
wrap a `KMixedUnitInstance`.

## Unit Enumeration

An enumeration `K<UnitName>Unit` is created that contains all existing units:
* Symbol for the unit (e.g. `m` for meter, ...), `symbol`
* Relative value compared to the base value, `baseValue`
* The base value itself as a retrievable property of the enumeration (static), `BASE`

## Extensions (Units)

A file `K<UnitName>UnitExtensions.kt` is created. It contains all extension properties:
* For the existing concrete units of the system (e.g. meter, mile, ...), extending `Number`
* Optionally, helper functions related to unit calculation

## Bare Values (Units)

The file `K<UnitName>UnitBareValues.kt` is created. It contains all properties for the unit system:
* For every existing concrete unit of the system (e.g. meter, mile, ...), always meaning 1 here

## Concrete Exponent Units (Optional)

Only when explicitly requested, additional units per exponent value (bounded by user input) are to be created:
* For every specific unit, the part above is implemented
* For the unit system (independent of the exponent), only `K<UnitName>Unit` and `K<UnitName>UnitInstance` are implemented
