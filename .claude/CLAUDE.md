# Global Rules

* NEVER EVER write project context to your memory!!!
* All code is written in Kotlin
    * Code must be created accordingly, observing all applicable rules
    * All public members must be documented with KDoc
* The skillset (`.claude/skills`) and ruleset (`.claude/ruleset`) must be observed for every change
* Do NOT scan ANY code of ANY project; create code only from the skillset and ruleset
* Test coverage must reach 100% at all times; missing coverage MUST be closed by extending the tests
    * Tool-reported 100% is NOT sufficient: EVERY public API member (builders, enum entries, constants,
      factories) MUST be verified by a real assertion
    * Reasoning and consequences: `.claude/ruleset/tests.md`
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
* A new field MUST be agreed with the user first
* Binding rules (field mapping, naming, `common` criterion): `.claude/ruleset/package.md`

# Unit Overview

* Questions about the inventory ("which units already exist?") MUST ALWAYS be answered from the
  "Unit Groups" table in `README.md`, NEVER by scanning `src/`
    * Skill: `unit-overview`, agent: `overview-units`
* Every new unit group MUST add its row in the same change; renames and removals likewise
* Binding rules: `.claude/ruleset/unit-overview.md`

# Communication Notes

* Every output the AI writes to the console must be in German
* NEVER implement code directly; always create a plan first
    * Switch to plan mode on your own if necessary

# Documentation

* For each unit there MUST exist a MkDocs documentation file under `docs/docs/units/<field>/`
* ALWAYS use the `translator-doc` agent for translations; new pages are written in English ONLY
* README.md must be up to date for existing units and architecture
* All user-relevant changes must be documented in CHANGELOG.md
    * ONLY changes the user notices externally (new units, changed public API or behavior)
    * Purely internal changes (refactoring, private helpers) and documentation/KDoc fixes MUST NOT be listed
* Binding rules (fields, languages, page structure, examples): `.claude/ruleset/documentation.md`
    * Skill: `documentation`

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

Whenever work is resumed - after `/clear`, a context summary, an interruption, an error, a new session, or ANY
message like "weiter" / "continue" - the FIRST action of that turn MUST be, in this order, BEFORE any other
tool, edit or analysis:

1. Read `.claude/plans/<name>.md` and `.claude/plans/<name>_status.md`.
2. DISPLAY plan and current status to the user (in German): tasks done, and the next one.
3. ONLY afterwards continue the work and update the status file again.

Skipping step 2 is a rule violation. If NO plan file exists, say so explicitly and create a plan first.

# GIT

* ALWAYS use GIt commands for:
    * Renaming and Moving: git mv
    * Deleting: git rm
    * Creating: git add
