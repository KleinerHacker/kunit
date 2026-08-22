---
description: Package structure when adding new units
---

# Package Structure

## Requirement

All new source files MUST follow this package structure.

If a different package structure appears to be more appropriate, Claude MUST ask the user before creating it and SHOULD
provide one or more alternatives.

## Rules

### Root package

The root package is:

`org.pcsoft.framework.kunit`

It contains only framework-wide types that are shared by multiple units.

### Field packages (Fachgebiete)

Below the root package there is exactly one level of subject-area packages, mirroring the MkDocs field folders
(`docs/docs/units/<field>/`):

| Code package                           | MkDocs field                        |
|----------------------------------------|-------------------------------------|
| `org.pcsoft.framework.kunit.common`    | units that belong to several fields |
| `org.pcsoft.framework.kunit.kinematic` | `kinematics`                        |
| `org.pcsoft.framework.kunit.mechanic`  | `mechanics`                         |
| `org.pcsoft.framework.kunit.electric`  | `electrical`                        |
| `org.pcsoft.framework.kunit.thermo`    | `thermodynamics`                    |
| `org.pcsoft.framework.kunit.it`        | `information`                       |

A unit belongs to `common` if and only if it is documented as its own page in MORE THAN ONE MkDocs field (currently
`energy` and `power`). Otherwise it belongs to the field package of its single MkDocs field.

If a new field is needed, Claude MUST ask the user first (same as for a new MkDocs field folder).

The `formatter` package and the framework-wide root types are NOT field packages and stay untouched.

### Unit packages

Each unit MUST have its own dedicated package inside its field package:

`org.pcsoft.framework.kunit.<field>.<unit>`

The package name MUST match the unit name.

All unit-specific classes, interfaces, objects and extensions MUST be placed inside this package.

Framework-wide types MUST NOT be placed inside unit packages.

#### Naming

A multi-word unit name is written in the package as a single all-lowercase token, without separators
(`heattransfercoefficient`, `amountofsubstance`).

The package name MUST NOT repeat the name of its enclosing field package. The field is already expressed by the parent
package, so the unit part carries only the remaining qualifier:

| Wrong                        | Correct               |
|------------------------------|-----------------------|
| `electric.electricdensity`   | `electric.density`    |
| `thermo.thermalconductivity` | `thermo.conductivity` |
| `thermo.thermalresistance`   | `thermo.resistance`   |

This applies to the PACKAGE NAME ONLY. Class, interface and file names keep the full technical term
(`KThermalConductivityUnitInstance`), because the same short name may legitimately occur in several fields
(`electric.resistance` vs. `thermo.resistance`).