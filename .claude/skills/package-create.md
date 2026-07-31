---
name: create-package
description: Create a new package for a new unit
---

Create a new package named after the unit. All further classes are placed inside it.

The package must be created under its subject-area (field) package:

`org.pcsoft.framework.kunit.<field>.<unit>`

Available fields: `common`, `kinematic`, `mechanic`, `electric`, `thermo`, `it` (see
`rules/package.md`). The field MUST match the MkDocs field folder of the unit's documentation page; a unit documented in
more than one field goes into `common`. If no existing field fits, ask the user before creating a new one.
