# Energy (Mechanics)

Package: `org.pcsoft.framework.kunit.common.energy`
Base unit: **joule** (`KEnergyUnit.BASE == KEnergyUnit.JOULE`)

Type: **constructed unit**

Energy is a **constructed** unit: the composition `mass · length² · time⁻²` (`kg·m²·s⁻²`).
`KEnergyUnitInstance` wraps a `KMixedUnitInstance` of three terms — `KMassUnit.BASE` (gram) at `+1`,
`KDistanceUnit.BASE` (meter) at `+2` and `KTimeUnit.BASE` (second) at `-2`. Because the mass component of the library is
normalized to **grams** (not kilograms), the canonical product is divided by 1000 to reach joules; the stored value is
always normalized to joules.

Energy is technically **one** quantity that appears in several subject areas. This page describes its *mechanical*
reading — **work**, `W = F · s`. The same Kotlin group is documented for the other areas in
[Energy (Electrical)](../electrical/energy.md) and [Energy (Thermodynamics)](../thermodynamics/energy.md).

## Building an energy

Build an energy with a named token, or from a decomposition (see below). Named units survive as value-1 tokens (used
with `of`/`into`):

| Energy                   | Symbol |                 Token |     1 unit in J |
|--------------------------|--------|----------------------:|----------------:|
| Joule                    | `J`    |              `joules` |             1.0 |
| Erg (CGS)                | `erg`  |                `ergs` |          1.0e-7 |
| Calorie (thermochemical) | `cal`  |            `calories` |           4.184 |
| Electron volt            | `eV`   |       `electronVolts` | 1.602176634e-19 |
| British thermal unit     | `BTU`  | `britishThermalUnits` |   1055.05585262 |

Named units support the SI prefixes via `KPrefixBuilder` (`kilo.joules`, `mega.joules`, `kilo.calories`, …).

The **kilowatt hour has no token of its own** — it is not a genuinely named unit but the product
`kilo.watts * hours` and is built that way.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*

val w = 500 of joules
w into joules                   // 500.0
w into calories                 // 119.502868...
(1 of kilo.joules) into joules  // 1000.0
```

## Multiple decompositions

Energy can be reached through several **equivalent decompositions**, all producing the same value-equal energy:

| Expression           | Result type           | Meaning                                                                            |
|----------------------|-----------------------|------------------------------------------------------------------------------------|
| `force * length`     | `KEnergyUnitInstance` | mechanical work `W = F · s` (commutative)                                          |
| `power * time`       | `KEnergyUnitInstance` | work from a power over time `W = P · t` (commutative)                              |
| `power / frequency`  | `KEnergyUnitInstance` | the inverse-time form (`W/Hz = W·s`)                                               |
| `charge * voltage`   | `KEnergyUnitInstance` | electrical energy `W = Q · U` (see [Energy (Electrical)](../electrical/energy.md)) |
| `mass·length²/time²` | via `.toEnergy()`     | native canonical `kg·m²·s⁻²` expression                                            |

The typed operator forms return an energy directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toEnergy()` (which recognises only the canonical normal form and throws
`IllegalStateException` otherwise). All routes are value-equal.

The inverse operators tie power, time and energy together:

| Expression       | Result type          | Meaning                                         |
|------------------|----------------------|-------------------------------------------------|
| `energy / time`  | `KPowerUnitInstance` | `P = W / t` (see [Power (Mechanics)](power.md)) |
| `energy / power` | `KTimeUnitInstance`  | `t = W / P`                                     |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.common.energy.*

// Real-world example - lifting work: pulling with 100 N over 5 m of distance is 500 J of work.
val w = (100 of newtons) * (5 of meters)   // KEnergyUnitInstance
w into joules                              // 500.0

// The work solved for the power needed to do it within 5 s:
val p = (500 of joules) / (5 of seconds)   // KPowerUnitInstance, 100 W

// And solved for the time a 100 W drive needs for that work:
val t = (500 of joules) / (100 of watts)   // KTimeUnitInstance, 5 s

// The same work as the native kg·m²·s⁻² expression:
val raw = 500 of (kilo.grams * (meters pow 2)) / (seconds pow 2)
raw.toEnergy() == (500 of joules)          // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.energy.*

val s = (100 of joules) + (40 of joules)  // 140 J
(100 of joules) > (40 of joules)          // true
(100 of joules) * (40 of joules)          // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.energy.*

(1 of calories).toString()     // "4.184 J" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `⁻²`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written
both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin                                            | Meaning                                        |
|-------------|---------------------------------------------------|------------------------------------------------|
| `J`         | `joules`                                          | energy (work), base unit (named token, joule)  |
| `F · s`     | `(100 of newtons) * (5 of meters)`                | mechanical work from force and length          |
| `kg·m²/s²`  | `(kilo.grams * (meters pow 2)) / (seconds pow 2)` | energy as mass·length² / time² (fraction form) |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)`  | same energy as a pure product                  |
| `kJ`        | `kilo.joules`                                     | prefixed energy (kilojoule)                    |
