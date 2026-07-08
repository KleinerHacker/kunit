# Implementation Status

## Done

* **Construction/reading DSL: `of` + `into` + prefix builders** (the current, breaking model):
  * Build with `number of <value-1 unit template>` (`10.5 of kilo.meters / milli.seconds`), read with
    `value into <unit>` (`v into kilo.meters`). `Number.of`/`KUnitMeasurable.into`/`KUnitMeasurable.scaledBy`
    live in `KUnitMeasurable.kt`.
  * Prefix builders in `KPrefixBuilder.kt`: `KPrefixBuilder` (base) → `KDiminishingPrefixBuilder` /
    `KAugmentingPrefixBuilder`, plus the 24 builder values (`kilo`, `milli`, …). Storage's binary IEC
    prefixes are `KStorageBinaryPrefixBuilder` values (`kibi`, …). Units are extension properties on the
    appropriate builder type (`val KPrefixBuilder.meters`, `val KAugmentingPrefixBuilder.bytes`), so
    `milli.bytes` is a **compile error** while `kilo.bytes`/`kibi.bytes` compile.
  * Bare tokens (`meters`, `seconds`, `bytes`, `knots`, `hectares`, `liters`, …) are value-1
    `K…UnitInstance` values used as templates for `of`/`into`. Constructed groups have no composite
    `*PerY` tokens - a speed/rate is an expression (`meters / seconds`), only single-named specials
    (`knots`, `mach`, `speedOfLight`) remain.
  * Removed entirely: `valueAs`, custom-unit `toString(target)`, the `Number.xxx` creators, the prefix
    `infix` functions, and the whole `KUnitTarget`/`KScaledUnit`/`KDerivedUnit`/`with` system.
  * Full parameterized test suite migrated to `of`/`into` (one consolidated `K<Group>Test.kt` per group +
    `KMixedUnitTest`); the `milli.bytes` compile-failure is verified.

* Root package `org.pcsoft.framework.kunit`:
  * `KUnit`, `KUnitTarget` (base interfaces)
  * `KMixedUnitInstance`, `KUnitTerm` (mixed-unit engine incl. `+`, `-`, `*`, `/`, `hasSameUnits`, `valueAs`, `toString` overload)
  * `KUnitPrefix` (complete SI prefix table, Quetta/Q to Quecto/q, 24 values) as well as `KScaledUnit`,
    `KDerivedUnit`, `KScaledDerivedUnit` and the `with` infix functions (output conversion)
  * `KUnitPrefix` prefixes are applied via **per-group** prefix `infix` functions (24 per group, declared
    in each sub-package over that group's unit type, e.g. `KLengthUnitPrefix.kt`, `KTimeUnitPrefix.kt`),
    each returning that group's concrete "pure" unit directly (e.g. `5 kilo meters` -> `KLengthUnitInstance`)
* Sub-package `org.pcsoft.framework.kunit.length` (prototype for the physical quantity length):
  * `KLengthUnit` (meter, mile, nautical mile, yard, foot, inch, fathom, chain, furlong, astronomical unit, light-year, parsec)
  * `KLengthUnitInstance` (incl. `+`, `-`, `*`, `/`, comparison operators, `valueAs`, `toString` overload,
    `toKMixedUnitInstance`) - encapsulates arbitrary exponents of `KLengthUnit.BASE` (length, area, volume, ...)
  * `KLengthUnitExtensions` (creator properties per length, area and volume unit, `toKLengthUnit`)
  * `KLengthDerivedUnit` (area: are, hectare, acre; volume: liter, US/imperial gallon, US fluid ounce, oil barrel)
* All length types consistently carry the `K` prefix naming scheme (`KLengthUnit`, `KLengthUnitInstance`, `KLengthDerivedUnit`)
* Complete test suite for all classes mentioned above (root package + `length` package)
* `CLAUDE.md` extended with the architectural decisions made as part of this prototype
* Sub-package `org.pcsoft.framework.kunit.time` (physical quantity time):
  * `KTimeUnit` (second, minute, hour, day), base unit second
  * `KTimeUnitInstance` - a 100 % wrapper around `java.time.Duration` (the `Duration` is the source of
    truth; the full `Duration` API is forwarded) that additionally offers the standard "pure" unit
    surface (`value`, `valueAs`, `+`/`-`/`*`/`/`, comparison operators, `toString`, `toKMixedUnitInstance`,
    `toDuration`) plus `KMixedUnitInstance.toKTimeUnit()`/`Duration.toKTimeUnit()`. Time is always exponent 1
    (no `KTimeDerivedUnit`); `toKTimeUnit()` therefore only accepts a single `KTimeUnit` term at exponent 1
  * `KTimeUnitExtensions` (creator properties `seconds`/`minutes`/… and bare `val` aliases)
  * Note: values outside roughly `[1 ns, Long.MAX seconds]` are not representable by the Duration backing
  * Complete test suite (`time` package) and a dedicated MkDocs page (`docs/docs/units/time.md` + ko/zh/ja);
    the pre-existing throwaway private `TimeUnit` test enums were migrated to the official `KTimeUnit`
  * `CLAUDE.md` extended with the per-unit MkDocs page convention

* Number-extension creators are extension **properties** (`5.meters`, `2.hours`); the `toXxx()`
  conversions remain functions
* Every group and the mixed unit are covered by parameterized cross-matrix tests (JUnit Jupiter
  `@ParameterizedTest`/`@MethodSource`) per the procedure in CLAUDE.md

* Sub-package `org.pcsoft.framework.kunit.speed` (first **constructed** quantity: speed = length·time⁻¹):
  * `KSpeedUnit` (meter per second (base), kilometer per hour, mile per hour, knot, foot per second, Mach,
    speed of light) — each carrying a single factor to m/s so it doubles as a plain `KUnit`/target
  * `KSpeedUnitInstance` — wraps a two-term `[m¹, s⁻¹]` `KMixedUnitInstance` normalized to m/s; offers the
    standard "pure" unit surface and reads back either as a whole speed unit or as a length-per-time pair,
    plus `KMixedUnitInstance.toKSpeedUnit()` (validates the two-term speed signature)
  * `KSpeedUnitExtensions` (creator properties + bare `val` aliases), `KSpeedUnitPrefix` (24 prefixes)
  * `KSpeedUnitOperators` — direct cross-group operators `length / time → speed`, `speed * time → length`,
    `time * speed → length`, `length / speed → time` (each re-wrapping the mixed-unit result)
  * Complete parameterized test suite incl. the mandatory **bidirectional** decomposition (core → speed
    and speed → every core unit) and a dedicated MkDocs page (`docs/docs/units/speed.md` + ko/zh/ja)

* **Exponentiation via `pow`** (group-agnostic): infix `pow(Int)` on `KMixedUnitInstance` and the
  `KUnitMeasurable.pow` extension raise any unit to an integer power (`2.meters pow 2`, `2 kilo meters pow 2`,
  `2.hours pow 2`); the distance group adds a dimensioned `KDistanceUnitInstance.pow` so `2.meters pow 2`
  is a `KAreaUnitInstance`. This replaces the removed named `squareXxx`/`cubicXxx` constructors and the
  prefixed area/volume DSL. Full cross-matrix tests (per unit, per prefix, per exponent) added.

* Sub-package `org.pcsoft.framework.kunit.storage` (digital data amount; first **plain one-dimensional**,
  `Double`-backed wrapper shape):
  * `KStorageUnit` (byte (base), bit = 0.125 B)
  * `KStorageUnitInstance` — wraps a single-term `[B¹]` `KMixedUnitInstance` normalized to bytes
    (`KUnitMeasurable by instance` + `KUnitInstance<KStorageUnitInstance>` directly), plus
    `KMixedUnitInstance.toStorage()`
  * `KStorageUnitExtensions` (creator properties `bytes`/`bits`), `KStorageUnitBareValues` (`bytes`/`bits`)
  * `KStorageUnitPrefix` — only the **non-diminishing** SI prefixes (`deca`..`quetta`; the diminishing ones
    are intentionally absent → compile error) **plus** the binary IEC prefixes (`kibi`..`yobi`)
  * `KStorageBinaryPrefix` (Ki..Yi, powers of 1024) + `KBinaryScaledUnit` target + `with` infix; wired into
    `KMixedUnitInstance.resolve()`
  * Complete parameterized test suite (incl. decimal vs. binary 1000/1024, cross-group storage × time) and a
    dedicated MkDocs page (`docs/docs/units/storage.md` + ko/zh/ja)
  * `CLAUDE.md` reconciled with the actual code (target system, wrapper shapes, root file layout, naming rule)

* Sub-package `org.pcsoft.framework.kunit.datarate` (second **constructed** quantity: data rate =
  storage·time⁻¹):
  * `KDataRateUnit` (byte per second (base), bit per second = 0.125 B/s) — each carrying a single factor to
    B/s so it doubles as a plain `KUnit`/target
  * `KDataRateUnitInstance` — wraps a two-term `[B¹, s⁻¹]` `KMixedUnitInstance` normalized to B/s; reads back
    either as a whole data-rate unit (bare, SI-scaled or binary-scaled) or as a storage-per-time pair, plus
    `KMixedUnitInstance.toDataRate()` (validates the two-term rate signature)
  * `KDataRateUnitExtensions` (creator properties + bare `val` aliases), `KDataRateUnitPrefix` — the same
    prefix policy as storage: non-diminishing SI prefixes **plus** the binary IEC prefixes (reused from
    `KStorageBinaryPrefix`); no root `resolve()` change needed
  * `KDataRateUnitOperators` — direct cross-group operators `storage / time → data rate`,
    `data rate * time → storage`, `time * data rate → storage`, `storage / data rate → time`
  * Complete parameterized test suite incl. the mandatory **bidirectional** decomposition and a dedicated
    MkDocs page (`docs/docs/units/datarate.md` + ko/zh/ja)

## Open

* Further unit groups (e.g. mass, temperature, ...) following the pattern established here for `length`/`time`
* Further composite "pure" units that are themselves composed of a mixed unit (e.g. Newton, force =
  mass·length·time⁻²), following the pattern established here for `speed` (see the "Tests" section in CLAUDE.md)
