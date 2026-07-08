# KUnit

**KUnit** is a Kotlin framework (usable from Java as well) for calculating with physical units instead of
bare numbers. Instead of tracking meters, miles, or square meters as plain `Double` values and hoping every
call site agrees on the unit, `kunit` carries the unit alongside the value and does the conversion,
multiplication and dimensional bookkeeping for you.

## Why KUnit?

Working with raw numbers for physical quantities is error-prone: it is easy to accidentally add meters to
miles without converting, or to add an area to a length. kunit solves this by making the unit part of the
type:

- **Two verbs, `of` and `into`.** Build with `number of <unit>` (`5 of meters`), read with
  `value into <unit>` (`v into kilo.meters`). Number and unit are strictly separated.
- **Type-safe arithmetic.** `+` and `-` between incompatible unit groups or exponents throw
  `IllegalStateException` instead of silently producing a wrong number.
- **Automatic conversion.** `(5 of meters) + (3 of miles)` just works - both operands are normalized
  internally, so you never have to manually convert units before combining them.
- **Free-form multiplication and division.** Multiplying or dividing units is *always* allowed and
  automatically tracks the resulting physical dimension (exponent), e.g. `length * length` becomes an area.
- **Full `Number` support.** Construct values from `Int`, `Long`, `Float`, `Double`, and any other `Number`
  type; everything is normalized to `Double` internally.
- **The complete SI prefix table**, from Quetta (Q) to Quecto (q), as prefix builders (`kilo.meters`,
  `milli.seconds`) with compile-time-enforced per-unit prefix policy.
- **Named special units** (like hectare, liter, acre) as ordinary value-1 tokens used with `of`/`into`.

## Core concepts

kunit is built around two central types:

- **`KMixedUnitInstance`** - a *mixed unit* ("Mischeinheit"): a `Double` base value plus one or more `KUnit`s,
  each paired with an integer exponent (e.g. `m^1 * s^-1` for a speed). This is the generic engine that
  powers everything else.
- **`KUnit`** - a single "pure" unit belonging to a unit group (e.g. meter belongs to the length group).
  Concrete unit groups are modeled as `enum class ... : KUnit` (e.g. `KDistanceUnit`).

Every unit group additionally provides a **wrapper class** (e.g. `KLengthUnitInstance`) that encapsulates a
`KMixedUnitInstance` restricted to a single unit group, always normalized to that group's base unit. This is the
type you will use most of the time - see [Predefined Units](units/distance.md) for the units shipped today,
and [Mixed Units](mixed-units.md) for when and how to drop down to the generic `KMixedUnitInstance` engine
directly.

If you want to add support for a new physical quantity (e.g. mass or time), see
[Adding Custom Units](custom-units.md) for a full, step-by-step walkthrough.

!!! note "Unit objects are immutable"
    Every unit value - the `KMixedUnitInstance` engine as well as every "pure" wrapper such as
    `KLengthUnitInstance` or `KTimeUnitInstance` - is **immutable**. No operation ever mutates an
    existing instance; operators (`+`, `-`, `*`, `/`) and conversions always return a **new** object,
    leaving the operands untouched. This makes unit values safe to share freely and to use as keys or
    constants.

## Quick Start

Add the module as a dependency (or include it as a project/source set) and import the vocabulary of the unit
group you need.

### Length

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

// Build pure length values with `of` from any Number type
val distance = 5 of meters
val trip = 10 of miles

// Operators: automatic conversion within the same group and exponent
val total = distance + trip          // KLengthUnitInstance, normalized to meters
val diff = trip - distance

// Comparisons
val isFarther = trip > distance      // true

// Read the value in a specific unit with `into`
println(total into kilo.meters)      // e.g. 21.0467...
println(total into yards)            // e.g. 23018.4...

// Multiplying pure lengths builds a strongly typed area
val area = distance * trip           // KAreaUnitInstance

// Named special units for area (exponent 2) and volume (exponent 3)
val plot = 3 of hectares
println(plot into ares)              // 300.0

val tank = 200 of liters
println(tank into usGallons)
```

### SI prefixes

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters

// `5 of kilo.meters` -> KLengthUnitInstance (== 5000 m)
val fiveKm = 5 of kilo.meters
println(fiveKm.value) // 5000.0 (normalized to meters)
```

### Mixed / composite units

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds

// Compose a unit expression from value-1 templates and scale it with `of`
val accel = 10 of meters / (seconds pow 2)   // KMixedUnitInstance, m·s⁻²
```

## Checkout & Build

```bash
git clone <repository-url>
cd kunit
```

kunit uses Gradle (the wrapper is included in the repository, no local Gradle installation needed):

```bash
# Build
./gradlew build          # Windows: gradlew.bat build

# Run tests only
./gradlew test            # Windows: gradlew.bat test
```

A JDK capable of resolving toolchain 25 is required (the `foojay-resolver` plugin downloads it automatically
if needed).
