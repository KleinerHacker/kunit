---
description: where the overview of all existing units lives
---

# Unit Overview

## Single source of truth

The complete list of all unit groups that already exist in the framework is the **Unit Groups** table in
`README.md`, under the section *What does the framework currently support?*.

Every row of that table names:

* the group (with its base-dimension normal form for constructed units)
* the sub-package (`org.pcsoft.framework.kunit.<field>.<unit>`)
* the base unit (`KXUnit.BASE`)

## Answering "which units already exist?"

Any question about the current inventory - "does unit X exist?", "which units are there?", "what is the
package of unit X?" - MUST be answered from that table.

Do NOT scan `src/` to determine the inventory. Scanning project code is forbidden by the global rules, and
the table is the maintained answer. Use the `unit-overview` skill or the `overview-units` agent for the
lookup.

If a unit is NOT in the table, it does not exist yet - continue with the `create-unit` skill.

## Keeping it current

The table is part of the deliverable of every new unit group. Adding a group without adding its row leaves
the inventory wrong for everyone who relies on this rule, so the row MUST be added in the same change that
adds the group.

The same applies to renames and removals: the row is updated or deleted together with the code.
