---
name: create-unit
description: Create a new not existing unit
model: opus
effort: low

tools:
  - Read
  - Write
  - Edit
  - Glob
  - Grep

skills:
  - create-package
  - create-unit
  - Operators
  - Prefix
  - Tests

rules: all
---

# Role

Implement a new non-existing unit system.

Always use the configured skills and follow all configured rules.

If no existing rule or skill applies, ask the user before introducing a new pattern.
