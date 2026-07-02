# KUnit

**KUnit** is a Kotlin framework (usable from Java as well) for calculating with physical units instead of
bare numbers. Instead of tracking meters, miles, or square meters as plain `Double` values and hoping every
call site agrees on the unit, `kunit` carries the unit alongside the value and does the conversion,
multiplication and dimensional bookkeeping for you.

## Why KUnit?

Working with raw numbers for physical quantities is error-prone: it is easy to accidentally add meters to
miles without converting, or to add an area to a length. kunit solves this by making the unit part of the
type:

- **Type-safe arithmetic.** `+` and `-` between incompatible unit groups or exponents throw
  `IllegalStateException` instead of silently producing a wrong number.
- **Automatic conversion.** Adding `5.meters() + 3.miles()` just works - both operands are normalized
  internally, so you never have to manually convert units before combining them.
- **Free-form multiplication and division.** Multiplying or dividing units is *always* allowed and
  automatically tracks the resulting physical dimension (exponent), e.g. `length * length` becomes an area.
- **Full `Number` support.** Construct values from `Int`, `Long`, `Float`, `Double`, and any other `Number`
  type; everything is normalized to `Double` internally.
- **The complete SI prefix table**, from Quetta (Q) to Quecto (q), usable generically with any unit.
- **Named special units** (like hectare, liter, acre) as convenient, group- and exponent-bound
  input/output targets, without replacing the underlying raw-exponent representation.

## Core concepts

kunit is built around two central types:

- **`KUnitInstance`** - a *mixed unit* ("Mischeinheit"): a `Double` base value plus one or more `KUnit`s,
  each paired with an integer exponent (e.g. `m^1 * s^-1` for a speed). This is the generic engine that
  powers everything else.
- **`KUnit`** - a single "pure" unit belonging to a unit group (e.g. meter belongs to the length group).
  Concrete unit groups are modeled as `enum class ... : KUnit` (e.g. `KLengthUnit`).

Every unit group additionally provides a **wrapper class** (e.g. `KLengthUnitInstance`) that encapsulates a
`KUnitInstance` restricted to a single unit group, always normalized to that group's base unit. This is the
type you will use most of the time - see [Predefined Units](units/length.md) for the units shipped today,
and [Mixed Units](mixed-units.md) for when and how to drop down to the generic `KUnitInstance` engine
directly.

If you want to add support for a new physical quantity (e.g. mass or time), see
[Adding Custom Units](custom-units.md) for a full, step-by-step walkthrough.

## Quick Start

Add the module as a dependency (or include it as a project/source set) and import the vocabulary of the unit
group you need.

### Length

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.length.*

// Create pure length values from any Number type
val distance = 5.meters()
val trip = 10.miles()

// Operators: automatic conversion within the same group and exponent
val total = distance + trip          // KLengthUnitInstance, normalized to meters
val diff = trip - distance

// Comparisons
val isFarther = trip > distance      // true

// Read the value in a specific unit
println(total.valueIn(KUnitPrefix.KILO with meters)) // e.g. 21.0467...
println(total.valueIn(yards))                         // e.g. 23018.4...

// Multiplying/dividing pure units builds a mixed unit (KUnitInstance)
val area = distance.toKUnitInstance() * trip.toKUnitInstance()

// Special units for area (exponent 2) and volume (exponent 3)
val plot = 3.hectares()
println(plot.valueIn(KLengthDerivedUnit.ARE))   // 300.0

val tank = 200.liters()
println(tank.valueIn(KLengthDerivedUnit.US_GALLON))
```

### SI prefixes

```kotlin
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.length.meters
import org.pcsoft.framework.kunit.length.toKLengthUnit

// "5 kilo meters" -> KPrefixBuilder -> KUnitInstance -> KLengthUnitInstance
val fiveKm = (5 kilo meters).toKUnitInstance().toKLengthUnit()
println(fiveKm.value) // 5000.0 (normalized to meters)
```

### Mixed units

```kotlin
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.length.KLengthUnit

// Manually composing a mixed unit, e.g. meters squared (length^1 * length^1)
val speed = KUnitInstance(10.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
val doubled = speed * speed // exponents are added -> length^2
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
