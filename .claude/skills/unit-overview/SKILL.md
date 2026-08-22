---
name: unit-overview
description: Look up which units already exist in the framework
---

> **Verbindliche Regel:** vor der Arbeit `.claude/ruleset/unit-overview.md` lesen und einhalten.

# Looking Up Existing Units

Use this skill whenever the question is about the current inventory:

* "Which units already exist?"
* "Does a unit for X exist?"
* "Which package / base unit does unit X use?"
* "Which units are still missing?" (the inventory is the starting point for that answer)

## Where to look

The one authoritative overview is the **Unit Groups** table in `README.md`, section
*What does the framework currently support?* (see `.claude/ruleset/unit-overview.md`).

Read that table. Do NOT scan `src/` - scanning project code is forbidden by the global rules, and the table
is the maintained answer.

## How to answer

Report per matching group:

* the group name (including its base-dimension normal form for constructed units)
* the sub-package `org.pcsoft.framework.kunit.<field>.<unit>`
* the base unit (`KXUnit.BASE`)

For a broad question, group the answer by field (`common`, `kinematic`, `mechanic`, `electric`, `thermo`,
`it`) so the inventory stays readable. For a question about one specific unit, answer only for that unit.

Two further sources refine the answer when needed:

* `CHANGELOG.md` - when the question is *when* a group was added or what changed about it
* `docs/mkdocs.yml` (nav) - when the question is about the documentation pages of a group

## When the unit is not listed

A unit that is absent from the table does not exist yet. Say so explicitly and point to the `create-unit`
skill.

Watch for a unit that exists under a different name: several quantities share one type because they share
one base-dimension normal form (e.g. entropy is `thermo.heatcapacity`, torque is `common.energy`, stiffness
and surface tension are both `mechanic.lineforce`). Check `docs/mkdocs.yml` for a page with the asked-for
name before declaring a unit missing.
