---
name: Prefix
description: Prefix support for each unit
---

The file `KPrefixBuilder.kt` should contain a builder for creating prefixes, so that the following Kotlin construct can be built:
* `3 kilo meters`
* `10 milli meters`

The enumeration `KUnitPrefix` lists all existing prefixes, together with:
* The symbol (e.g. `k` for kilo), `symbol`
* The relative factor itself (e.g. 1000 for kilo), `factor`
