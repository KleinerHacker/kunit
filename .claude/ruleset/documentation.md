# Documentation

All MkDocs documentation lives under `/docs`. This rule is binding for every documentation change.

## Language

* Supported languages: English, Japanese, Chinese, Korean, Arabic, Hindi
* A NEW page is written in English ONLY; all other languages are added EXPLICITLY BY USER REQUEST
* ALWAYS use the `translator-doc` agent to produce a translated page; never translate inline
* Every language variant (`.md`, `.ja.md`, …) of a page lives in the SAME field folder as its source

## Structure

* Unit doc pages are grouped by subject area under `docs/docs/units/<field>/`
  (currently: `kinematics`, `mechanics`, `electrical`, `thermodynamics`, `information`)
* A new unit page MUST be placed in the folder of its subject area
* A new field folder MUST be agreed with the user first
* The folders mirror the code field packages; see `.claude/ruleset/package.md`

## Unit pages

* For each unit there MUST exist a MkDocs documentation file
* Every unit detail page MUST state its unit type in the intro block: **native unit** (predefined,
  measurable base unit) or **constructed unit** (standardized/composed unit)
* Every unit page MUST include at least one real-world example (e.g. area calculation, speed from
  distance/time) in EVERY supported language it exists in
* A technically identical unit that belongs to several subject areas (e.g. energy as heat / kinetic /
  electrical energy) gets its own field-specific page per area; these pages MUST cross-reference each other

## Overview pages

* Each subject area MUST have an `overview.md`, in every supported language
* The overview explains the field and MUST include at least one real-world worked example in
  mathematical vs. Kotlin notation, in the `## Notation` table form: `Mathematics | Kotlin | Meaning`

## Decompositions

* A standardized unit with multiple decompositions MUST show EVERY decomposition on its MkDocs page,
  in the typed operator form AND the native expression form via `toX()`, in every supported language
* The underlying operator rules are defined in `.claude/ruleset/operators.md`
