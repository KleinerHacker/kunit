---
description: Package structure when adding new units
---

# Package Structure

## Requirement

All new source files MUST follow this package structure.

If a different package structure appears to be more appropriate, Claude MUST ask the user before creating it and SHOULD provide one or more alternatives.

## Rules

### Root package

The root package is:

`org.pcsoft.framework.kunit`

It contains only framework-wide types that are shared by multiple units.

### Unit packages

Each unit MUST have its own dedicated package.

The package name MUST match the unit name.

All unit-specific classes, interfaces, objects and extensions MUST be placed inside this package.

Framework-wide types MUST NOT be placed inside unit packages.