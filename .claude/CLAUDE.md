# Global Rules

* All code is written in Kotlin
  * Code must be created accordingly, observing all applicable rules
  * All public members must be documented with KDoc
* The skillset and ruleset must be observed for every change
* Test coverage must reach 100% at all times
  * If it does not, the tests must be extended appropriately to cover what is missing
  * 100% tool-reported coverage is NOT sufficient on its own: every public API member
    (builders, enum entries, constants, factories) MUST be verified by a real assertion
  * Rationale: top-level `val` initializers and enum entries run in the class `<clinit>` at
    class-loading, so a coverage tool marks them "covered" even when NO test ever reads or
    asserts their value — such unused public API stays effectively untested despite 100%
  * Therefore: unused/open public API MUST have its own assertion (e.g. every prefix builder,
    not just a representative subset)
* Do NOT scan ANY code of ANY project
  * Create code only based on the skillset and ruleset
* Do NOT add new dependencies without asking
  * Always ask first and provide suitable suggestions for what could be used to solve the problem
* Do NOT introduce breaking changes without asking; when you ask, present the following options:
  * Keep the old code and mark it as deprecated with a reference to the new code (recommended)
  * Keep the old code and mark it for removal with a reference to the new code
  * Apply the change without regard for the breaking change (remove the old code, add the new one)

# Communication Notes

* Every output the AI writes to the console must be in German
* NEVER implement code directly; always create a plan first
  * Switch to plan mode on your own if necessary

# Documentation

* For each unit there MUST exist a MkDocs documentation file
  * /docs contains all MkDocs documentation files
  * Supported languages: English, Japanese, Chinese, Korean, Arabic, Hindi
  * Every MkDocs unit page MUST include at least one real-world example (e.g. area calculation, speed from distance/time) in every supported language
* Unit doc pages are grouped by subject area (Fachgebiet) under `docs/docs/units/<field>/`
  (currently: `kinematics`, `mechanics`, `electrical`, `thermodynamics`, `information`)
  * A new unit page MUST be placed in the folder of its subject area (ask the user if a new field is needed)
  * Every language variant (`.md`, `.ja.md`, …) of a page lives together in that same field folder
* Each subject area MUST have an `overview.md` (in every supported language)
  * The overview explains the field and MUST include at least one real-world worked example in
    mathematical vs. Kotlin notation (the `## Notation` table form: `Mathematics | Kotlin | Meaning`)
* Every unit detail page MUST state its unit type in the intro block: **native unit** (predefined,
  measurable base unit) or **constructed unit** (standardized/composed unit)
* A technically identical unit that belongs to several subject areas (e.g. energy as heat / kinetic /
  electrical energy) gets its own field-specific page per area; these pages MUST cross-reference each other
* README.md must be up to date for existing units and architecture
  * Supported languages: English, Japanese, Chinese, Korean, Arabic, Hindi
* All user-relevant changes must be documented in CHANGELOG.md
  * CHANGELOG.md contains ONLY changes the user notices externally (new units, changed public API or behavior)
  * Purely internal changes (refactoring, private helpers) and documentation/KDoc fixes MUST NOT be listed
* A standardized unit with multiple decompositions (see `rules/operators.md`) MUST document AND explicitly test every decomposition
  * Each decomposition must be shown in the MkDocs page (typed operator form AND native expression form via `toX()`) in every supported language
  * The tests must prove that all decompositions yield the same typed, value-equal result

# Planning

* A plan describes multiple tasks to do
  * Describe each part in short bullet points (max. 20 words each)
  * Do NOT create a plan summary
    * All bullet points MUST describe each task to implement it
* Do not execute the whole plan at once; work through it task by task