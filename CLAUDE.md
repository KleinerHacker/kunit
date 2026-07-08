# Working with this document

* **`CLAUDE.md` is the single source of truth for planning and changes.** For any plan or code change,
  use **only** this file and the files it **explicitly references** as your source of information.
* **Do not scan the code without explicit approval.** Searching or reading source code outside the
  explicitly referenced files requires the user's express permission first.
* **When something important is missing, ask - don't reach for the code.** If a piece of information
  required for the task is not in `CLAUDE.md`, ask the user rather than inferring it from the source.
* **Fold clarifications back in.** From such a question, decide whether `CLAUDE.md` needs to be
  extended, and extend it as part of the change.

# Overview

* Calculation with units
* The basis is always a "mixed unit"
  * This is a unit composed of several units
* Enables calculations in physical contexts with real units in Double

# Construction and Reading DSL (authoritative)

Number and unit are **strictly separated**. There are exactly **two** free-standing verbs for units:

* **Build:** `number of <unit-expression>` — `Number.of(template)` scales a **value-1 unit template**
  by the leading number and returns the template's own (strong) type. Examples:
  `10.5 of meters`, `10.5 of kilo.meters`, `10.5 of kilo.meters / milli.seconds` (a `KSpeedUnitInstance`),
  `2 of hectares`, `10 of meters * (milli.seconds pow 2)`.
* **Read:** `value into <unit-expression>` — `KUnitMeasurable.into(target): Double` returns the numeric
  value in that unit (checked by unit signature). Examples: `v into kilo.meters`, `area into hectares`.
  There is **no** `valueAs` and **no** custom-unit `toString(target)`; format via
  `"${v into kilo.meters} km"`. Only the base-unit `toString()` remains.

The **unit-expression** is built from value-1 instances and the existing operators `*` `/` `pow`:

* **Bare tokens** (`meters`, `seconds`, `bytes`, `knots`, `hectares`, `liters`, …) are value-1
  `K…UnitInstance` values in `K<Dimension>UnitBareValues.kt`. Constructed groups (speed, data rate) have
  **no** spelled-out composite tokens (`metersPerSecond`, `bytesPerSecond`, …) - a speed/rate is an
  expression (`meters / seconds`, `bytes / seconds`); only genuinely single-named specials (`knots`,
  `mach`, `speedOfLight`) remain as tokens.
* **Prefix builders** turn a token into a prefixed value-1 template by property access
  (`kilo.meters`, `milli.seconds`, `kibi.bytes`). The builder hierarchy (root `KPrefixBuilder.kt`) is
  `sealed KPrefixBuilder` → `KDiminishingPrefixBuilder` (factor < 1: `deci`…`quecto`) /
  `KAugmentingPrefixBuilder` (factor > 1: `deca`…`quetta`); the 24 builder values live there. A group hangs
  its unit as an extension property on the **appropriate** builder type: units valid with any magnitude on
  the base `KPrefixBuilder`, magnitude-restricted units on a subtype. Storage's `bytes`/`bits` hang only on
  `KAugmentingPrefixBuilder` (and the binary `KStorageBinaryPrefixBuilder`), so **`milli.bytes` is a
  compile error** while `kilo.bytes`/`kibi.bytes` compile — the storage prefix policy is enforced by types,
  not at runtime.
* **`scaledBy`** (`KUnitMeasurable.scaledBy(Double)`, overridden per wrapper to return its own type) is the
  scaling primitive behind `of`; it is the only reason `of` can preserve the strong type.
* **Special/derived units** (`hectares`, `ares`, `acres`, `liters`, gallons, …) are named value-1
  instances, **not** a separate target type. There is **no** `KUnitTarget`/`KScaledUnit`/`KDerivedUnit`
  system any more.

**Kotlin precedence facts that shaped this:** `as`/`in` are hard keywords (hence `of`/`into`); `* /` bind
tighter than the named infixes `of`/`into`/`pow`, so `10.5 of kilo.meters / milli.seconds` reads as
`10.5 of (kilo.meters / milli.seconds)`; `pow` is weaker than `* /`, so parenthesize a powered term
(`meters * (milli.seconds pow 2)`).

# Architecture

* `KMixedUnitInstance` - Represents a mixed unit.
  * Consists of a Double (base value)
  * Consists of one or more `KUnit`s, each as a pair with its exponent
    * The exponent is an Integer, which is positive (for the numerator) or negative (for the denominator)
    * The `KUnit`s are multiplied with each other
* `KUnit` - Represents a unit.
  * Is an **interface** (not a class), since concrete units per group are represented as `enum class ... : KUnit`
    (enums cannot extend classes in Kotlin, but can implement interfaces)
  * Consists of a String (symbol)
  * Consists of a Double (base value), the conversion factor to the base unit of the group
  * Belongs to a group of units, e.g. Length (which then includes e.g. Metric, Miles, Yards, ...)
    * For each "pure" unit, a dedicated `enum class` is used (e.g. `KLengthUnit`)
    * A group explicitly declares its base unit (e.g. `KLengthUnit.BASE`)
* A `KMixedUnitInstance` is wrapped for each group of units for "pure units"
  * The wrapper classes (e.g. `KLengthUnitInstance`) encapsulate a `KMixedUnitInstance` via delegation
    (no inheritance relationship) and always store their value **normalized to the base unit of the group**
  * **Exponent-dimensioned subtypes (the distance model, the generic pattern for future groups).** A
    group may model its exponents as their own compile-time-safe types under an **open base wrapper**
    (any exponent), e.g. for `distance`:
    * `KDistanceUnitInstance` - the open base / catch-all (any exponent, e.g. `m⁴`, `m⁻¹`)
    * `KLengthUnitInstance` (exponent 1), `KAreaUnitInstance` (2), `KVolumeUnitInstance` (3) - each
      `: KDistanceUnitInstance(instance), KUnitInstance<ItsOwnType>`
    * Results whose exponent leaves `{1,2,3}` fall back to the base type; a dimensionless result
      (exponent 0) is a `KMixedUnitInstance`.
    * **Mechanism:** the base type has **no** `plus`/`minus`/`compareTo`; those live only on the leaf
      types, typed to their own dimension. This makes cross-dimension `+`/`-`/comparison
      (`length + area`, `length < volume`) a **compile error**, not a runtime failure. The tradeoff:
      the base type is not additive (add two `m⁴` values through the mixed engine). `*`/`/` are always
      allowed and, when both operands are statically dimensioned, produce the correctly typed result
      (`length * length = area`, `area / length = length`); a general/mixed operand falls back to
      `KMixedUnitInstance`.
    * Time/Speed deliberately stay **outside** the distance hierarchy (otherwise the base `*`/`/`
      members would shadow the speed cross-group extension operators).
* **Common interface hierarchy** (root package) for every unit-value type:
  * `KUnitMeasurable` - the group-agnostic surface shared by **all** values (`value`,
    `toUnit()`, `*`/`/` against a `KMixedUnitInstance`). `KMixedUnitInstance` implements it
    directly; the "pure" wrappers implement it via Kotlin `by` delegation to their internal
    `KMixedUnitInstance` (so `value`/`times`/`div`/`toUnit` are never hand-written there)
  * `KUnitInstance<SELF : KUnitInstance<SELF>>` - the self-typed (F-bounded) interface of the "pure"
    wrappers, extending `KUnitMeasurable` with same-type `+`/`-`/comparison and single-target
    `valueAs`/`toString`. Each wrapper implements `KUnitInstance<ItsOwnType>`. It does **not** declare
    `times`/`div`: same-group multiplication/division comes from `KUnitMeasurable` (against a
    `KMixedUnitInstance`) plus group-specific typed overloads, so dimensioned subtypes can narrow their
    `*`/`/` return types without a signature clash
* **Wrapper shapes.** There are three concrete shapes a "pure" wrapper can take; pick by the group's
  nature:
  * **dimensioned hierarchy** (distance) - an open base type plus exponent-specialized leaves, see the
    distance bullet above
  * **`Duration`-backed** (time) - the wrapper stores a `java.time.Duration` and delegates
    `KUnitMeasurable` to a `KMixedUnitInstance` built from it; subject to `Duration`'s representable range
  * **plain one-dimensional** (storage) - a single-term, `Double`-backed wrapper with **no** exponent
    subtypes and **no** `Duration` backing. It is the simplest shape:
    `class KStorageUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitMeasurable by instance, KUnitInstance<KStorageUnitInstance>` - it delegates
    `value`/`toUnit`/`times`/`div`/`pow` to `instance` and hand-writes only the `KUnitInstance`-only
    members (`valueAs`, `toString(target)`, `plus`/`minus`/`compareTo`) plus `equals`/`hashCode`/`toString()`.
    Its factory (`storageOf(value: Double)`) builds `KMixedUnitInstance(value, listOf(KUnitTerm(BASE, 1)))`
* **Reading targets are value-1 unit templates** (there is **no** `KUnitTarget` system). `into(target)`
  accepts any `KUnitMeasurable` describing the desired unit - a bare token (`meters`), a prefixed builder
  template (`kilo.meters`), a named special unit (`hectares`), or an expression (`kilo.meters / hours`) -
  and returns `this.value / target.value` after checking that both have the same unit signature. The
  removed types were `KUnitTarget`, `KScaledUnit`, `KDerivedUnit`, `KScaledDerivedUnit`,
  `KBinaryScaledUnit` and every `with` infix; there is no central `resolve()`. A new group adds **no**
  root-code edit for reading - it only contributes its value-1 tokens and builder-property extensions.
* **Immutability invariant**: every instance type (`KMixedUnitInstance`, the "pure" wrappers, and any
  future wrapper) is strictly immutable - only `val` state, no mutable fields; every operator/conversion
  returns a **new** instance
* **Creation invariant**: the "pure" wrapper constructors are `internal`. Concrete units may **only** be
  created via the `of` verb on a value-1 template (e.g. `5 of meters`, `2 of hours`, `5 of kilo.meters`;
  see the *Construction and Reading DSL* section), operator results, or the conversion extensions
  (`KMixedUnitInstance.toDistance()`/`toLength()`/`toTime()`/`toSpeed()`, `Duration.toTime()`). A wrapper
  must **never** be constructed directly from a `KMixedUnitInstance` by callers - the only
  `KMixedUnitInstance`→ wrapper path is the `to<Group>()` extension. The value-1 bare tokens and prefix
  builder templates are themselves built internally via the group factories (`lengthOf`, `timeUnitInstanceOf`,
  `storageOf`, `speedUnitInstanceOf`, `dataRateUnitInstanceOf`)
* **Conversion-method naming**: conversion methods/properties never carry the hard class name - they read
  as `toUnit()` (the generic `KMixedUnitInstance`) and `to<Group/Size>()` (e.g. `toDistance()`,
  `toLength()`, `toArea()`, `toVolume()`, `toTime()`, `toSpeed()`). Apply this to every group
* The `(KUnit, exponent)` pair is the type `KUnitTerm(val unit: KUnit, val exponent: Int)` (declared in
  `KMixedUnitInstance.kt`); a `KMixedUnitInstance` stores `val units: List<KUnitTerm>`
* SI prefixes (the complete SI prefix table, from Quetta/Q to Quecto/q) are not part of
  `KUnit`/the (KUnit, exponent) pair, since they are only relevant when reading/writing values. They are
  represented via a generic `KUnitPrefix` enum (root package; fields `symbol`, `factor` - no `baseValue`)
  * A group is **not** obliged to expose the full prefix table: it may declare only a **subset** of the
    prefix `infix` functions (e.g. storage omits every diminishing prefix - factor < 1, i.e. `deci`
    downward - so `5 milli bytes` is a **compile error**, not a runtime check), and it may add an
    **alternative prefix system** of its own (e.g. storage's binary IEC prefixes `KStorageBinaryPrefix`
    Ki/Mi/Gi…, powers of 1024) as a separate enum plus a matching `KUnitTarget` (`KBinaryScaledUnit`) and
    `with` infix - the pattern mirrors `KUnitPrefix`/`KScaledUnit` exactly
  * Prefixes are applied via the **prefix builders** (`kilo`, `milli`, …; root `KPrefixBuilder.kt`), which
    expose a group's units as value-1 templates through extension **properties** per group
    (`val KPrefixBuilder.meters` in `KLengthUnitExtensions.kt`, `val KPrefixBuilder.seconds` in
    `KTimeUnitExtensions.kt`, `val KAugmentingPrefixBuilder.bytes` in `KStorageUnitExtensions.kt`). Combined
    with `of` this is the construction form: `5 of kilo.meters` is a `KLengthUnitInstance`, exactly
    equivalent to `5 of meters` scaled, i.e. `5000` m. The old per-group prefix `infix` functions
    (`5 kilo meters`) and their `K*UnitPrefix.kt` files were removed.
    * Rationale: the wrapper classes live in the sub-packages, so only the group package can build its
      concrete wrapper. Returning the concrete type (rather than a group-agnostic intermediate that the
      caller must convert with `toXxxUnit()`) keeps the call site free of boilerplate. The cost is that the
      24 prefix functions are declared once per group instead of once globally - accepted deliberately, and
      consistent with each group already owning its own creator/`val`-alias DSL.
    * There is intentionally **only one** prefix construction syntax: the bare-unit form `5 kilo meters`
      (`meters` is the `KLengthUnit` alias). No `KPrefixBuilder` intermediate and no `5 kilo meters()`
      wrapper-literal variant exist - a single, unambiguous way to write it.
    * For Duration-backed groups (e.g. `time`), the result is subject to that wrapper's representable range
      (see `KTimeUnitInstance`): extreme prefixes on a multi-unit base (e.g. `5 quetta seconds`) exceed
      `java.time.Duration`'s `Long`-seconds range and are not representable
* For certain combinations of unit group and exponent, **special units** exist with their own
  name/symbol and their own conversion factor (e.g. hectare for area = length², liter for volume = length³).
  These do **not** replace the normal mechanism (e.g. the base unit with exponent 2 remains the
  "raw" representation of an area) - they are purely additional, group- and exponent-bound conversion targets
  for input/output, generic over the referenced unit type (compile-time group safety), combinable analogously
  to the prefixes
* Creation (only constructor and creator extension properties) of `KMixedUnitInstance`/the wrapper classes is possible
  from any `Number` type (`Int`, `Long`, `Float`, `Double`, ...), not only `Double`; internally it is always
  normalized to `Double`. All outputs (value, conversions, text representation) are, without exception, `Double`.
  Operators and comparison operators, on the other hand, **never** work directly with raw `Number` values - only
  between two unit types

## Package Structure

* The root package is called `org.pcsoft.framework.kunit`
* A sub-package is created for each "pure" unit
* The base classes `KUnit` and `KMixedUnitInstance` are located in the root package. The root package
  holds only four `.kt` files and several root types share a file (there is **no** file-per-type rule
  in root):
  * `KUnit.kt` - `KUnitTarget` (marker root of all targets) and `KUnit`
  * `KUnitMeasurable.kt` - `KUnitMeasurable` **and** `KUnitInstance` (no separate `KUnitInstance.kt`)
  * `KMixedUnitInstance.kt` - `KUnitTerm` and `KMixedUnitInstance` (+ the `KUnitMeasurable.pow` extension)
  * `KUnitPrefix.kt` - `KUnitPrefix`, `KScaledUnit`, `KDerivedUnit`, `KScaledDerivedUnit` and their
    `with` infixes (no separate `KDerivedUnit.kt`)

### DSL file organization

The construction/DSL vocabulary of a group is split into dedicated files, organized **per
dimension**, never mixed into the wrapper class files:

* **Per-dimension, not per-group.** A group with several exponent-dimensioned subtypes
  (distance → length/area/volume) gets one set of DSL files **per dimension** (e.g.
  `KLengthUnit*`, `KAreaUnit*`, `KVolumeUnit*`) - there is **no** shared `KDistanceUnit*`
  catch-all DSL file spanning all three
* **Creators** - the `Number.xxx` creator extension properties, together with their private
  creator helpers (`lengthFrom`/`areaFrom`/`of`, ...), live in `K<Dimension>UnitExtensions.kt`
  (e.g. `KLengthUnitExtensions.kt`, `KAreaUnitExtensions.kt`, `KSpeedUnitExtensions.kt`)
* **Bare values** - the bare unit reference / token `val` aliases (`meters`, `miles`,
  `seconds`, ...) live in `K<Dimension>UnitBareValues.kt`, kept separate from the creators
* **Prefix DSL** - the SI-prefix `infix` constructors keep their own `K<Group>UnitPrefix.kt`
* The `*Instance.kt` files hold **only** the wrapper class, its factory helpers
  (`lengthOf`/`areaOf`/...) and the `to<Group>()` conversions - no creator/bare-value DSL

## Naming Scheme

* All public types (classes, interfaces, enums, objects) start with `K` project-wide - in the
  root package (`KUnit`, `KMixedUnitInstance`, `KUnitMeasurable`, `KUnitInstance`, `KUnitPrefix`,
  `KDerivedUnit`, ...) just as
  in every sub-package (e.g. `KDistanceUnit`, `KDistanceUnitInstance`, `KLengthUnitInstance`,
  `KAreaUnitInstance`, `KVolumeUnitInstance`, `KDistanceDerivedUnit` in `distance`)
* **Unit-enum = group name; wrapper/DSL = dimension name.** The `KUnit` enum is named after the
  **group** (`KDistanceUnit`), while the "pure" wrapper and its DSL files are named after the
  **dimension** (`KLengthUnitInstance`, `KLength*` DSL, over `KDistanceUnit`) - there is deliberately no
  `KLengthUnit` type. For a **one-dimensional** group, group and dimension coincide, so both share the
  one name (`KStorageUnit` + `KStorageUnitInstance`, DSL `KStorage*`)
* Extension properties/functions and bare `val` aliases (DSL vocabulary such as the `meters` creator property, `kilo`, and the `meters` unit alias) are
  exempt from this rule - they remain named in a language-natural way

# Implementation

## Coding Style

* Use `val` throughout - for properties **and** local variables. Express accumulations functionally
  (`fold`/`map`/`reduce`) rather than mutating a `var`. Only fall back to `var` where it is genuinely
  unavoidable. This complements the immutability invariant (see Architecture)

## Documentation

* Every public member must be documented in English
* The documentation should be formatted in Markdown
* The documentation should be comprehensive and, where useful, contain examples
  * especially for operators
* Documentation for units is split into real units category (like length, time, ...) and 
  constructed units category (like speed, force, ...)

### MkDocs site

* Every new unit group must ship a dedicated MkDocs page at `docs/docs/units/<group>.md`, following the
  existing `docs/docs/units/distance.md` as the template (units table, operators, comparisons, SI prefixes,
  `toString` formatting, mixing with other units, plus any group-specific sections)
* The page must be provided in **every** language the site supports (the `mkdocs-static-i18n` suffix
  structure: the default `<group>.md` plus `<group>.ko.md`, `<group>.zh.md`, `<group>.ja.md`), mirroring
  the translation coverage of the `distance` page
* `docs/mkdocs.yml` must be updated accordingly: add a `nav` entry under `Predefined Units` and a matching
  label in every locale's `nav_translations`

### Changelog

* `CHANGELOG.md` must always be kept up to date: every change (new unit groups, features, fixes, breaking
  changes, ...) is recorded under the `[Unreleased]` section, grouped as `Added`/`Changed`/`Fixed`/`Removed`,
  as part of the same change - never leave it for later

## Operators

* All standard operators '+', '-', '*', '/' must be supported for:
  * "pure" units
  * mixed units
  * mixing "pure" units and mixed units
* All standard comparison operators '==', '!=', '<', '<=', '>', '>=' must be supported for:
  * "pure" units
  * In addition to the classic equals, there must be a method to check the unit (`KUnit` + exponent)
    for mixed units
* **Exponentiation (`pow`).** Raising a unit to an integer power is expressed **only** via the infix
  `pow` function (`2.meters pow 2`, `2 kilo meters pow 2`, `x = x pow 2`), never via named
  `squareXxx`/`cubicXxx` constructors (those do not exist). Rationale: Kotlin has no overloadable `^`
  operator (and no `^=`), and a 4-word chain like `2 square kilo meters` is not parseable as infix, so a
  single, group-agnostic `pow` is the one and only power syntax.
  * **Semantics:** the value is raised (`value.pow(n)`) and **every** `(KUnit, exponent)` term's exponent
    is multiplied by `n`; a term reaching exponent 0 is dropped. `pow 0` is dimensionless (value 1),
    `pow 1` the identity, negative `n` inverts the dimension. `2.meters pow 2` is `(2 m)² = 4 m²`
    (the value is powered, **not** merely the exponent), and `pow` chains multiplicatively
    (`2.meters pow 2 pow 2 = 16 m⁴`).
  * **Return type / availability:** `pow` must be available on **every** group. It is declared as the
    group-agnostic extension `KUnitMeasurable.pow(Int): KMixedUnitInstance` (so it reaches every "pure"
    wrapper) and, for groups that model exponents as dimensioned subtypes (e.g. distance), a **more
    specific** typed extension (`KDistanceUnitInstance.pow(Int): KDistanceUnitInstance`, so
    `2.meters pow 2` is a `KAreaUnitInstance`). It is deliberately **not** a member of
    `KUnitMeasurable`/`KUnitInstance` (that would shadow the typed variants, as with `times`/`div`).
  * **Precedence:** `pow` is a named infix function and binds **weaker** than `* / + -`; parenthesize
    in mixed expressions (`(a * b) pow 2`).
* Both `KMixedUnitInstance` and the "pure" wrapper classes offer, in addition to the normalized raw value,
  the `into` verb to read a converted value for a desired unit template (a bare token, a prefixed builder
  template, or a special/derived value-1 instance). Text output is the base-unit `toString()` only; format
  a specific unit as `"${v into kilo.meters} km"`

### Error Handling

* For comparisons:
  * If there are differences in the `KUnit` or their exponents, an error must be thrown: `IllegalStateException`

## Conversion

* Every "pure" unit offers, via an extension method, a way to convert it into a `KMixedUnitInstance`
* For `+`/`-` between two "pure" units of the same type, that same type is returned again
* For `*`/`/` between two "pure" units of the same group **that model exponents as dimensioned subtypes**
  (e.g. distance), the result stays in that type family when both operands are statically dimensioned and
  the resulting exponent has a dedicated type (`length * length = area`); otherwise (resulting exponent
  outside the modelled set, or a general/mixed operand, or a dimensionless exponent-0 result) a
  `KMixedUnitInstance` / the group's base type is returned. For a group without dimensioned subtypes,
  `*`/`/` between different "pure" units yield a `KMixedUnitInstance`
* When calculating a "pure" unit with a mixed unit, or mixed units with each other, new `KMixedUnitInstance`s are returned
* **General cross-group `*`/`/`.** Any two "pure" units can be multiplied/divided **directly**, across
  group boundaries, without first calling `toUnit()` (e.g. `20.bytes / 20.seconds`); the result is a
  `KMixedUnitInstance`. This is provided by **one** group-agnostic pair of extension operators
  `operator fun KUnitInstance<*>.times/div(other: KUnitInstance<*>): KMixedUnitInstance` in the root
  (`KUnitMeasurable.kt`, below the `KUnitInstance` interface). It covers every group (incl. the distance
  base type) and every future group automatically.
  * It is deliberately an **extension, not a member** of `KUnitInstance`/`KUnitMeasurable`. A member
    would, by Kotlin's *"member always wins"* rule (kotlinlang.org/docs/extensions.html), shadow the
    statically-typed cross-group **extension** operators (e.g. speed's `KLengthUnitInstance.div(KTimeUnitInstance)`),
    degrading `length / time` from `KSpeedUnitInstance` to `KMixedUnitInstance`. As an extension it only
    applies when no more specific member or extension matches, so all typed results are preserved
    (`3.meters * 4.meters = KAreaUnitInstance`, `100.meters / 5.seconds = KSpeedUnitInstance`).
  * Only `*`/`/` exist across groups; `+`/`-` across groups remain a deliberate **compile error** (adding
    unequal dimensions is invalid).

### Error Handling

* Every conversion to a "pure" unit must check whether it is also present in a mixed unit
  * If not: `IllegalStateException`
  * The term's **exponent is irrelevant** to this group check: a term is time-typed / length-typed
    purely by its `KUnit` group, regardless of exponent (even a "square second" is still a time unit).
    `KMixedUnitInstance.toKTimeUnit()` therefore accepts any single `KTimeUnit` term (any exponent) and
    just wraps the numeric value as a `Duration` - it must **not** reject non-1 exponents
* Calculations with '*' are always allowed
  * For every unit that is already present, both exponents are added
  * For every unit that is not yet present, a new one is created in `KMixedUnitInstance` with exponent 1
* Calculations with '/' are always allowed
  * For every unit that is already present, both exponents are subtracted
  * For every unit that is not yet present, a new one is created in `KMixedUnitInstance` with exponent -1
* Calculations with '+' or '-' are only allowed if
  * Two "pure" units (wrapper classes such as `KLengthUnitInstance`) of the same unit group are calculated
    (e.g. meter + mile is allowed, automatic conversion via normalization) **and** have the same
    exponent (e.g. area must not be calculated with volume)
  * Two mixed units (`KMixedUnitInstance`) are calculated with each other if, for every term on one side, there
    is exactly one term on the other side belonging to the same unit group with the same exponent
    (order-independent) - the `KUnit`s themselves do not need to match, since matching terms are
    automatically converted via normalization (analogous to the "pure" wrapper classes)
    * Even with matching unit groups, if any pair of matching terms has different exponents, the operation fails
    * If a term has no matching unit group on the other side (e.g. mixing length with time), the operation fails
    * Result: `IllegalStateException`

## Tests

* **Construction runs through the public DSL, not the raw enums.** Test instances are built **primarily
  via the public DSL surface** - the `of` verb on value-1 tokens and builder templates (`5 of meters`,
  `5 of kilo.meters`, `2 of hectares`), the power operation (`(2 of meters) pow 2`), and read back with
  `into`. The raw enum values (`KDistanceUnit.METER`, `KSpeedUnit.METERS_PER_SECOND`) are used **only for
  expected-value computation** (e.g. `unit.baseValue`), never to construct the instance under test. This ensures the suite covers exactly the surface users actually call: every
  bare-value × prefix combination runs through the DSL rather than bypassing the alias. Applies to
  **every** group and is the yardstick for the prefix and construction matrices
* **Every test method carries a KDoc.** Each test function (annotated `@Test` or `@ParameterizedTest`)
  **must** have a KDoc comment stating the **use case it verifies** - what is exercised and what the
  expected outcome is (e.g. "Adding two lengths of different units normalizes both and returns their sum as
  a length."). This is a base rule and applies to **every** test in the suite, success and error cases
  alike. Shared fixtures (generator/bare-value lists, `@MethodSource` providers, builder/tolerance helpers)
  are likewise documented so their purpose is self-evident
* Every "pure" unit is tested separately
  * Full tests for the most complete possible test coverage
  * Complete tests for all operations
  * Every operator function ('+', '-', '*', '/') and every comparison operation ('==', '!=', '<', '<=', '>', '>=')
    is tested at least once per type with a success case and, where an error is intended, at least once with
    the corresponding error case (`IllegalStateException`) - it is not enough to test only one operator
    representatively for all of them
  * For every unit defined in a group and every special unit, a dedicated test exists per prefix,
    verifying the combination of prefix and unit (construction + back-conversion) - a complete
    prefix-x-unit matrix, not spot checks. In addition, at least one standalone test independent of
    the respective unit is added for each individual prefix
* The mixed units are tested
  * Composed with at least one other unit each
  * Every "pure" unit is tested together with a mixed unit
  * For a "pure" unit that itself consists of a mixed unit (e.g. Newton), a test exists that
    calculates to this unit, or from the unit to another "pure" unit
* **Constructed/composed units (e.g. speed = length·time⁻¹, force, ...) must be decomposed in *both*
  directions.** It is not enough to build the composed unit from its core units; the tests must also
  break the composed unit back down into **every** core unit of **each** involved group. Concretely, for
  a composed unit built from groups A and B:
  * **core → composed**: for every unit of A against every unit of B (a full cross-matrix), verify that
    combining them produces the composed unit with the correct value and term/exponent signature
    (e.g. `length / time == speed`).
  * **composed → core**: from the composed unit, recover each core quantity via the inverse operators
    and read it back through `valueAs` in **every** unit of the respective group
    (e.g. `speed * time == length` read back in every length unit; `length / speed == time` read back in
    every time unit). Also verify the direct, strongly-typed cross-group operators return the right
    wrapper type, and that non-matching shapes (e.g. `area / time`) fail with `IllegalStateException`.

Fundamentally, all tests verify the correctness of the values and calculations.

### Parameterized cross-matrix test procedure (mandatory for every unit group)

Every unit group (and the mixed unit) is covered by **real parameterized tests** — JUnit Jupiter
`@ParameterizedTest` with `@MethodSource` (the `junit-jupiter-params` dependency), the test class
annotated `@TestInstance(TestInstance.Lifecycle.PER_CLASS)` so the `@MethodSource` provider methods
can be non-static instance methods. Assertions stay `kotlin.test.*` with explicit deltas (a relative
tolerance for the wide magnitude span); instances are built through the group's creator **properties**
via a shared `internal val xxxUnitGenerators: List<Pair<(Number) -> KXxxUnitInstance, KXxxUnit>>` list
and a `xxxOf(unit, n)` helper. For each new group provide, each as its **own** parameterized method:

* **Prefix × unit** — every SI prefix combined with every unit of the group (construction +
  back-conversion round trip), **plus** one standalone test per individual prefix (all 24). For
  `Duration`-backed groups (e.g. time) restrict the prefix × unit matrix to the representable band and
  apply the standalone tests to a `1 / factor` count so every prefix stays representable.
* **Conversion matrix** — every unit converted into every other unit of the same group (`valueAs`),
  checked against `n * from.baseValue / to.baseValue`.
* **Operator matrix** — one parameterized method **per operator** (`+`, `-`, `*`, `/`), each covering
  every unit against every other unit of the group (value + resulting exponent/`units` verified).
* **Comparison matrix** — one parameterized method **per comparison operator** (`==`, `!=`, `<`, `<=`,
  `>`, `>=`), each covering every unit against every other unit, expectations derived from the
  normalized base values. Plus representative `IllegalStateException` cases (e.g. mixing exponents).
* **Reading (`into`)** — read every unit back in every other unit of the group and in a prefixed template
  (`v into kilo.meters`) and, where applicable, a special-unit template (`area into hectares`); plus
  `toString()` (base unit only). There is no custom-unit `toString(target)`.
* The same matrices apply to the group's **derived units**, and the mixed unit is exercised with a
  **cross-group** matrix (every unit of one group against every unit of another, e.g. length × time).

## Implementation

The implementation status is documented in STATUS.md.
