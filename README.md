<p align="center">
  <img src="docs/docs/assets/images/logo.png" alt="KUnit logo" width="320">
</p>

# kunit

> 🌐 **English** · [한국어](README.ko.md) · [中文](README.zh.md) · [日本語](README.ja.md)
>
> The full documentation is also available in all four languages on
> [GitHub Pages](https://kleinerhacker.github.io/kunit/)
> ([EN](https://kleinerhacker.github.io/kunit/) ·
> [KO](https://kleinerhacker.github.io/kunit/ko/) ·
> [ZH](https://kleinerhacker.github.io/kunit/zh/) ·
> [JA](https://kleinerhacker.github.io/kunit/ja/)).

Kotlin Unit Framework to calculate with different units in Kotlin (and Java) - calculate with real physical
units in `Double` precision instead of bare numbers.

## Checkout & Build

```bash
git clone <repository-url>
cd kunit
```

The project uses Gradle (the wrapper is included in the repository, no local Gradle installation needed):

```bash
# Build
./gradlew build          # Windows: gradlew.bat build

# Run tests only
./gradlew test            # Windows: gradlew.bat test
```

A JDK capable of resolving toolchain 25 is required (the `foojay-resolver` plugin downloads it automatically
if needed).

## Documentation Site

📖 **[Read the documentation on GitHub Pages](https://kleinerhacker.github.io/kunit/)**

The full documentation (overview, quick start, mixed units, adding custom units, predefined units) is built
with [MkDocs Material](https://squidfunk.github.io/mkdocs-material/) and available in English, Korean,
Chinese and Japanese via [mkdocs-static-i18n](https://github.com/ultrabug/mkdocs-static-i18n), with a
light/dark mode toggle.

```bash
pip install -r docs/requirements.txt

# Serve locally with live-reload
mkdocs serve

# Build the static site into ./site
mkdocs build
```

## Architecture

* **`KMixedUnitInstance`** - represents a *mixed unit*: a normalized `Double` base value plus a set of `KUnit`s,
  each combined with an exponent (positive = numerator, negative = denominator) that are thought of as
  multiplied together.
* **`KUnit`** - interface for a single "pure" unit (symbol + conversion factor to the base unit of its group).
  Implemented per unit group as `enum class ... : KUnit` (e.g. `KDistanceUnit`).
* **Wrapper classes** (e.g. `KLengthUnitInstance`) - encapsulate a `KMixedUnitInstance` via delegation for a
  concrete group and always keep their value normalized to that group's base unit. They are not limited to
  exponent 1 - they also cover derived quantities of the same group (e.g. area = length², volume = length³).
* **`KUnitPrefix`** - generic root-package enum with the complete SI prefix table (Quetta/Q to Quecto/q).
  Prefixes are not part of `KUnit` itself, they only matter for reading/writing values, and are applied via
  per-group `infix` functions (e.g. `5 kilo meters`) that return the group's concrete unit directly
  (`5 kilo meters` is a `KLengthUnitInstance`, equivalent to `5000.meters`).
* **Special units** (`KDerivedUnit` / `KScaledDerivedUnit`) - additional, group- and exponent-bound conversion
  targets with their own name/symbol (e.g. hectare for area, liter for volume), complementing rather than
  replacing the normal mechanism.

```mermaid
classDiagram
    class KUnit {
        <<interface>>
        +symbol: String
        +baseValue: Double
    }
    class KMixedUnitInstance {
        +value: Double
        +units: List~KUnitTerm~
        +valueAs(...)
        +toString()
        +plus() minus() times() div()
    }
    class KUnitTerm {
        +unit: KUnit
        +exponent: Int
    }
    class KUnitPrefix {
        <<enum>>
        Quetta ... Quecto
    }
    class KDerivedUnit {
        <<interface>>
        +referenceUnit: KUnit
        +exponent: Int
        +baseValue: Double
    }

    KMixedUnitInstance "1" o-- "many" KUnitTerm
    KUnitTerm --> KUnit
    KDerivedUnit --> KUnit : referenceUnit

    class KDistanceUnit {
        <<enum>>
        METER, MILE, YARD, ...
    }
    class KLengthUnitInstance {
        +value: Double
        +valueAs(unit)
        +plus() minus() times() div()
    }
    class KDistanceDerivedUnit {
        <<enum>>
        HECTARE, ARE, ACRE, LITER, ...
    }

    KUnit <|.. KDistanceUnit
    KDerivedUnit <|.. KDistanceDerivedUnit
    KLengthUnitInstance *-- KMixedUnitInstance : delegates to
    KDistanceDerivedUnit --> KDistanceUnit : referenceUnit
```

### Package Structure

* Root package `org.pcsoft.framework.kunit` contains the base types `KUnit`, `KMixedUnitInstance`, `KUnitPrefix`,
  `KDerivedUnit`, ...
  * each unit sub-package additionally declares its own prefix `infix` functions (e.g. `KDistanceUnitPrefix.kt`)
* Every "pure" unit group gets its own sub-package (e.g. `org.pcsoft.framework.kunit.distance`) with its own
  `KXxxUnit`, `KXxxUnitInstance`, `KXxxDerivedUnit` and the associated creator extensions.

### Operators

* `+`, `-`, `*`, `/` are supported for pure units, mixed units and mixing both.
* `==`, `!=`, `<`, `<=`, `>`, `>=` are supported for pure units; mixed units additionally offer a method for
  pure unit/exponent checking (`hasSameUnits`).
* `+`/`-` are only allowed within the same unit group and with the same exponent (pure units), or with exactly
  the same `KUnit`s including exponents (mixed units) - otherwise an `IllegalStateException` is thrown.

## What does the framework currently support?

Current implementation status (see [STATUS.md](STATUS.md) for details):

### Root Engine

* `KMixedUnitInstance`/`KUnitTerm` mixed-unit engine with full operators and `toString` conversion
* Complete SI prefix table (24 values, Quetta/Q to Quecto/q) via `KUnitPrefix`
* Per-group prefix construction returning the concrete unit directly (`5 kilo meters`)
* Generic mechanism for special/derived units (`KScaledUnit`, `KDerivedUnit`, `KScaledDerivedUnit`)

### Unit Groups

| Group | Sub-package | Base unit |
|---|---|---|
| Distance | `org.pcsoft.framework.kunit.distance` | Meter (`KDistanceUnit.BASE`) |
| Time | `org.pcsoft.framework.kunit.time` | Second (`KTimeUnit.BASE`) |
| Storage | `org.pcsoft.framework.kunit.storage` | Byte (`KStorageUnit.BASE`) |
| Speed (constructed: length·time⁻¹) | `org.pcsoft.framework.kunit.speed` | Meter per second (`KSpeedUnit.BASE`) |
| Data Rate (constructed: storage·time⁻¹) | `org.pcsoft.framework.kunit.datarate` | Byte per second (`KDataRateUnit.BASE`) |

#### Distance (`KDistanceUnit`)

Meter, mile, nautical mile, yard, foot, inch, fathom, chain, furlong, astronomical unit, light-second …
light-year, parsec.

#### Dimensioned subtypes (exponent as a type)

The distance group models exponents as their own compile-time-safe types under an open base
`KDistanceUnitInstance` (any exponent):

* **`KLengthUnitInstance`** - exponent 1 (a length): `5.meters`, `3 kilo meters`
* **`KAreaUnitInstance`** - exponent 2 (an area): `2.meters pow 2`, `2 kilo meters pow 2`, plus the
  special units (`KDistanceDerivedUnit`) are, hectare, acre
* **`KVolumeUnitInstance`** - exponent 3 (a volume): `2.meters pow 3`, `2 kilo meters pow 3`, plus liter,
  US gallon, imperial gallon, US fluid ounce, oil barrel

`*`/`/` stay in this family where possible (`length * length = area`, `area / length = length`); a
resulting exponent outside `{1,2,3}` falls back to `KDistanceUnitInstance`. Cross-dimension `+`/`-`/
comparison (`length + area`) are a **compile error**, not a runtime failure.

Raise a unit to a power with the infix `pow` (Kotlin has no overloadable `^`): `2.meters pow 2` is
`(2 m)² = 4 m²`, `2.meters pow 3` a volume, and `pow` works on every group (`2.hours pow 2`). It is the
only power syntax — there are no `squareXxx`/`cubicXxx` constructors.

#### Constructed groups (composed of two core groups)

* **Speed** (`KSpeedUnit`) - `length · time⁻¹`; build it directly with `100.meters / 10.seconds`
  (a `KSpeedUnitInstance`), recover the core units with `speed * time` / `length / speed`.
* **Data Rate** (`KDataRateUnit`) - `storage · time⁻¹`; build it directly with `100.bytes / 10.seconds`
  (a `KDataRateUnitInstance`), recover the core units with `rate * time` / `storage / rate`. Base unit
  byte per second (`bytesPerSecond`), plus `bitsPerSecond`; mirrors storage's prefix policy
  (non-diminishing SI prefixes + binary IEC prefixes).

### Still Open

* Further unit groups following the `length` pattern (e.g. mass, temperature)
* Composite "pure" units that are themselves composed of a mixed unit (e.g. Newton)

## Quick Start

Add the module as a dependency (or include it as a project/source set) and import the vocabulary of the unit
group you need.

### Distance

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.distance.*

// Create pure length values from any Number type
val distance = 5.meters              // KLengthUnitInstance (exponent 1)
val trip = 10.miles

// Operators: automatic conversion within the same group and exponent
val total = distance + trip          // KLengthUnitInstance, normalized to meters
val diff = trip - distance

// distance + (3.meters pow 2)        // does NOT compile: length + area is a compile error

// Comparisons
val isFarther = trip > distance      // true

// Read the value in a specific unit
println(total.valueAs(KUnitPrefix.KILO with meters)) // e.g. 21.0467...
println(total.valueAs(yards))         // e.g. 23018.4...

// Multiplying two lengths yields a strongly typed area; area / length yields a length again
val area = 200.meters * 50.meters    // KAreaUnitInstance (10 000 m²)
val side = area / 100.meters         // KLengthUnitInstance (100 m)

// Powers via `pow`, plus the special named area/volume units
val hall = 3.meters pow 2            // KAreaUnitInstance (9 m²)
val bigPlot = 2 kilo meters pow 2    // KAreaUnitInstance (4 000 000 m²)
val box = 2.meters pow 3             // KVolumeUnitInstance (8 m³)
val plot = 3.hectares                // KAreaUnitInstance
println(plot.valueAs(KDistanceDerivedUnit.ARE))   // 300.0
val tank = 200.liters                // KVolumeUnitInstance
println(tank.valueAs(KDistanceDerivedUnit.US_GALLON))
```

### SI prefixes

```kotlin
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters

// "5 kilo meters" -> KLengthUnitInstance (direct, == 5000.meters)
val fiveKm = 5 kilo meters
println(fiveKm.value) // 5000.0 (normalized to meters)
```

### Mixed units

```kotlin
import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.distance.KDistanceUnit

// Manually composing a mixed unit, e.g. meters per second (length^1 * time^-1 once a time group exists)
val speed = KMixedUnitInstance(10.0, listOf(KUnitTerm(KDistanceUnit.METER, 1)))
val doubled = speed * speed // exponents are added -> length^2
```
