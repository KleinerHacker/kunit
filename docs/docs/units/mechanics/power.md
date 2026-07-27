# Power (Mechanics)

Package: `org.pcsoft.framework.kunit.power`
Base unit: **watt** (`KPowerUnit.BASE == KPowerUnit.WATT`)

Type: **constructed unit**

Power is a **constructed** unit: the composition `mass · length² · time⁻³` (`kg·m²·s⁻³`).
`KPowerUnitInstance` wraps a `KMixedUnitInstance` of three terms — `KMassUnit.BASE` (gram) at `+1`,
`KDistanceUnit.BASE` (meter) at `+2` and `KTimeUnit.BASE` (second) at `-3`. Because the mass component of the
library is normalized to **grams** (not kilograms), the canonical product is divided by 1000 to reach watts;
the stored value is always normalized to watts.

Power is technically **one** quantity that appears in several subject areas. This page describes its
*mechanical* reading (`P = F · v`). The same Kotlin group is documented for the other areas in
[Power (Electrical)](../electrical/power.md) and [Power (Thermodynamics)](../thermodynamics/power.md).

## Building a power

Build a power with a named token, or from a decomposition (see below). Named units survive as value-1 tokens
(used with `of`/`into`):

| Power | Symbol | Token | 1 unit in W |
|---|---|---:|---:|
| Watt | `W` | `watts` | 1.0 |
| Metric horsepower | `PS` | `metricHorsePowers` | 735.49875 |
| Mechanical horsepower | `hp` | `mechanicalHorsePowers` | 745.6998715822702 |
| Erg per second (CGS) | `erg/s` | `ergsPerSecond` | 1.0e-7 |

Named units support the SI prefixes via `KPrefixBuilder` (`kilo.watts`, `mega.watts`, `milli.watts`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.power.*

val p = 100 of metricHorsePowers
p into kilo.watts               // 73.549875
p into mechanicalHorsePowers    // 98.63200706...
```

## Multiple decompositions

Power can be reached through several **equivalent decompositions**, all producing the same value-equal power:

| Expression | Result type | Meaning |
|---|---|---|
| `force * speed` | `KPowerUnitInstance` | mechanical power `P = F · v` (commutative) |
| `voltage * current` | `KPowerUnitInstance` | electrical power `P = U · I` (see [Power (Electrical)](../electrical/power.md)) |
| `energy / time` | `KPowerUnitInstance` | `P = W / t` (see [Energy (Mechanics)](energy.md)) |
| `mass·length²/time³` | via `.toPower()` | native canonical `kg·m²·s⁻³` expression |

The typed operator forms return a power directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toPower()` (which recognises only the canonical normal form and
throws `IllegalStateException` otherwise). All routes are value-equal.

The inverse operators of the mechanical form tie force, speed and power together:

| Expression | Result type | Meaning |
|---|---|---|
| `power / force` | `KSpeedUnitInstance` | `v = P / F` |
| `power / speed` | `KForceUnitInstance` | `F = P / v` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.div
import org.pcsoft.framework.kunit.force.newtons
import org.pcsoft.framework.kunit.power.*

// Real-world example - a cargo winch: 100 N of pull at 5 m/s needs 500 W.
val p = (100 of newtons) * ((5 of meters) / (1 of seconds))  // KPowerUnitInstance
p into watts                                                 // 500.0

// The definition solved for the pulling force at a given speed:
val f = (500 of watts) / ((5 of meters) / (1 of seconds))     // KForceUnitInstance, 100 N

// And solved for the achievable speed at a given force:
val v = (500 of watts) / (100 of newtons)                     // KSpeedUnitInstance, 5 m/s

// The same power as the native kg·m²·s⁻³ expression:
val raw = 500 of (kilo.grams * (meters pow 2)) / (seconds pow 3)
raw.toPower() == (500 of watts)                               // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.power.*

val s = (100 of watts) + (40 of watts)  // 140 W
(100 of watts) > (40 of watts)          // true
(100 of watts) * (40 of watts)          // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.power.*

(1 of metricHorsePowers).toString()     // "735.49875 W" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `⁻³`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `W` | `watts` | power, base unit (named token, watt) |
| `F · v` | `(100 of newtons) * ((5 of meters) / (1 of seconds))` | mechanical power from force and speed |
| `kg·m²/s³` | `(kilo.grams * (meters pow 2)) / (seconds pow 3)` | power as mass·length² / time³ (fraction form) |
| `kg·m²·s⁻³` | `kilo.grams * (meters pow 2) * (seconds pow -3)` | same power as a pure product |
| `PS` | `metricHorsePowers` | metric horsepower (named token) |
