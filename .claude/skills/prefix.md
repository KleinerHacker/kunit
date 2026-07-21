---
name: Prefix
description: Prefix support for each unit
---

# Prefixes

Prefixes are exposed as **builder values** that turn a bare unit token into a **prefixed, value-1
template** through a plain **property access** - `kilo.meters` (1000 m), `milli.seconds` (0.001 s) - for
use with the `of`/`into` verbs (`10 of kilo.meters`, `v into milli.seconds`). There is **no** infix form
like `3 kilo meters`; property access composes cleanly with `* / pow` and the weaker `of`/`into`.

## Prefix Enumeration

The enumeration `KUnitPrefix` (file `KUnitPrefix.kt`) lists all existing SI prefixes, together with:
* The symbol (e.g. `k` for kilo), `symbol`
* The relative factor itself (e.g. 1000 for kilo), `factor`

## Builder Hierarchy (`KPrefixBuilder.kt`)

`KPrefixBuilder.kt` contains a **sealed** builder hierarchy plus one top-level builder value per prefix
(`val kilo`, `val milli`, ...). The hierarchy encodes, at compile time, which units accept which prefixes:

* `KPrefixBuilder` (sealed base) - carries the `KUnitPrefix`. Units that accept **any** magnitude hang
  their template properties here (length, time, ...).
* `KDiminishingPrefixBuilder` (factor < 1: `deci` … `quecto`) - units that are only sensible when scaled
  **down** hang here (in addition to the base).
* `KAugmentingPrefixBuilder` (factor > 1: `deca` … `quetta`) - units that are only sensible when scaled
  **up** hang here.

This is how the exclusion from `architecture-prefix.md` is enforced **at compile time**: e.g. storage's
`bytes`/`bits` are declared only on the augmenting builder, so `kilo.bytes` compiles while `milli.bytes`
is a **compile error** (there is no `bytes` property on the diminishing builder). The unit properties
themselves are contributed per group in `K<UnitName>UnitExtensions.kt`, not in this file.

## Binary Prefixes (IEC, optional per unit)

Units that need power-of-1024 magnitudes (storage) additionally get a binary prefix system in their own
package (e.g. `KStorageBinaryPrefix.kt`):
* An enumeration `KStorageBinaryPrefix` (`KIBI` … `YOBI`) with `symbol` / `factor` (powers of 1024).
* A builder `KStorageBinaryPrefixBuilder` with top-level values (`val kibi`, `val mebi`, ...) exposing the
  same `bytes`/`bits` templates, so `kibi.bytes` (1024 B) is distinct from `kilo.bytes` (1000 B).
