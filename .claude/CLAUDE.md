# Global Rules

* NEVER EVER write project context to your memory!!!
* All code is written in Kotlin
    * Code must be created accordingly, observing all applicable rules
    * All public members must be documented with KDoc
* The skillset and ruleset must be observed for every change
* Test coverage must reach 100% at all times
    * If it does not, the tests must be extended appropriately to cover what is missing
    * 100% tool-reported coverage is NOT sufficient on its own: every public API member (builders, enum entries,
      constants, factories) MUST be verified by a real assertion
    * Rationale: top-level `val` initializers and enum entries run in the class `<clinit>` at class-loading, so a
      coverage tool marks them "covered" even when NO test ever reads or asserts their value — such unused public API
      stays effectively untested despite 100%
    * Therefore: unused/open public API MUST have its own assertion (e.g. every prefix builder, not just a
      representative subset)
* Do NOT scan ANY code of ANY project
    * Create code only based on the skillset and ruleset
* Do NOT add new dependencies without asking
    * Always ask first and provide suitable suggestions for what could be used to solve the problem
* Do NOT introduce breaking changes without asking; when you ask, present the following options:
    * Keep the old code and mark it as deprecated with a reference to the new code (recommended)
    * Keep the old code and mark it for removal with a reference to the new code
    * Apply the change without regard for the breaking change (remove the old code, add the new one)
* ALWAYS edit ANY file at one time (not in parts)

# Package Structure

* Unit packages are grouped by subject area: `org.pcsoft.framework.kunit.<field>.<unit>`
  (fields: `common`, `kinematic`, `mechanic`, `electric`, `thermo`, `it`)
* The code fields mirror the MkDocs field folders (`kinematics`, `mechanics`, `electrical`,
  `thermodynamics`, `information`); `it` corresponds to `information`
* A unit documented in MORE THAN ONE MkDocs field goes into `common` (currently energy, power)
* A multi-word unit package name is written as a single all-lowercase token and does NOT repeat the field name
  (`thermo.conductivity`, not `thermo.thermalconductivity`); class names keep the full technical term
* Details and the rule for new fields: `rules/package.md`

# Unit Overview

* The overview of ALL existing unit groups is the "Unit Groups" table in `README.md`
* Questions about the inventory ("which units already exist?") MUST ALWAYS be answered from that table,
  NEVER by scanning `src/`
    * Skill: `unit-overview`, agent: `overview-units`
* Every new unit group MUST add its row in the same change; renames and removals likewise
* Details: `rules/unit-overview.md`

# Communication Notes

* Every output the AI writes to the console must be in German
* NEVER implement code directly; always create a plan first
    * Switch to plan mode on your own if necessary

# Documentation

* Use ALWAYS the translator agent for MKDocs documentation.
* Each new MKDocs page MUST write in English ONLY; all other languages should be added EXPLICITLY BY USER
    * Supported languages: English, Japanese, Chinese, Korean, Arabic, Hindi
* For each unit there MUST exist a MkDocs documentation file
    * /docs contains all MkDocs documentation files
    * Supported languages: English, Japanese, Chinese, Korean, Arabic, Hindi
    * Every MkDocs unit page MUST include at least one real-world example (e.g. area calculation, speed from
      distance/time) in every supported language
* Unit doc pages are grouped by subject area (Fachgebiet) under `docs/docs/units/<field>/`
  (currently: `kinematics`, `mechanics`, `electrical`, `thermodynamics`, `information`)
    * A new unit page MUST be placed in the folder of its subject area (ask the user if a new field is needed)
    * Every language variant (`.md`, `.ja.md`, …) of a page lives together in that same field folder
* Each subject area MUST have an `overview.md` (in every supported language)
    * The overview explains the field and MUST include at least one real-world worked example in mathematical vs. Kotlin
      notation (the `## Notation` table form: `Mathematics | Kotlin | Meaning`)
* Every unit detail page MUST state its unit type in the intro block: **native unit** (predefined, measurable base unit)
  or **constructed unit** (standardized/composed unit)
* A technically identical unit that belongs to several subject areas (e.g. energy as heat / kinetic / electrical energy)
  gets its own field-specific page per area; these pages MUST cross-reference each other
* README.md must be up to date for existing units and architecture
* All user-relevant changes must be documented in CHANGELOG.md
    * CHANGELOG.md contains ONLY changes the user notices externally (new units, changed public API or behavior)
    * Purely internal changes (refactoring, private helpers) and documentation/KDoc fixes MUST NOT be listed
* A standardized unit with multiple decompositions (see `rules/operators.md`) MUST document AND explicitly test every
  decomposition
    * Each decomposition must be shown in the MkDocs page (typed operator form AND native expression form via `toX()`)
      in every supported language
    * The tests must prove that all decompositions yield the same typed, value-equal result

# Planning

* A plan describes multiple tasks to do
    * Describe each part in short bullet points (max. 20 words each)
    * All bullet points MUST describe each task to implement it
* Do NOT create a plan summary at ANY TIME
* ALWAYS write a plan to `.claude/plans/<name>.md`
    * After finishing the plan / all plans, you MUST clean up this path completely
* ALWAYS write the development status into `.claude/plans/<name>_status.md`
    * You MUST update this file after every completed task, not only at the end

## Continuation protocol (MANDATORY, no exceptions)

Whenever work is resumed - after `/clear`, after a context summary, after an interruption, an error, a new session, or
ANY user message like "weiter" / "mach weiter" / "continue" - the FIRST action of that turn MUST be, in exactly this
order and BEFORE any code, edit, tool or analysis:

1. Read `.claude/plans/<name>.md` and `.claude/plans/<name>_status.md`.
2. DISPLAY the plan and the current development status to the user in the console (in German), including which tasks are
   done and which is the next one.
3. Only AFTER this output: continue the work, switch to auto mode, and write the development status again.

Skipping step 2 is a rule violation. If NO plan file exists, say so explicitly and create a plan first. NEVER continue
work silently from a plan the user has not seen in the current context.

# GIT

* ALWAYS use GIt commands for:
    * Renaming and Moving: git mv
    * Deleting: git rm
    * Creating: git add