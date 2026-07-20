# Global Rules

* All code is written in Kotlin
  * Code must be created accordingly, observing all applicable rules
  * All public members must be documented with KDoc
* The skillset and ruleset must be observed for every change
* Test coverage must reach 100% at all times
  * If it does not, the tests must be extended appropriately to cover what is missing
* Do not add new dependencies without asking
  * Always ask first and provide suitable suggestions for what could be used to solve the problem
* Do not introduce breaking changes without asking; when you ask, present the following options:
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
  * Supported languages: English, Japanese, Chinese, Korean
* README.md must be up to date for existing units and architecture
  * Supported languages: English, Japanese, Chinese, Korean
* All user-relevant changes must be documented in CHANGELOG.md

# Planning

* A plan describes multiple tasks to do
  * Describe each part in short bullet points (max. 20 words each)
* Do not execute the whole plan at once; work through it task by task