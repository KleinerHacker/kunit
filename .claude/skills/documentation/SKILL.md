---
name: documentation
description: Write or change MkDocs documentation pages for units and subject areas
---

> **Verbindliche Regel:** vor der Arbeit `.claude/ruleset/documentation.md` lesen und einhalten.

# Documenting a unit

Use this skill whenever a MkDocs page under `/docs` is created or changed.

1. Determine the subject area (field) of the unit; the page belongs into `docs/docs/units/<field>/`.
2. Write the English page first (`<name>.md`) - unit type in the intro block, at least one real-world example.
3. Register the page in `docs/mkdocs.yml` in the navigation of its field.
4. Update the field's `overview.md` if the new unit belongs into its listing.
5. Translations into the other supported languages ONLY on explicit user request, via the `translator-doc` agent.

Documentation is part of the deliverable of a new unit, together with the `README.md` row and the
`CHANGELOG.md` entry.
