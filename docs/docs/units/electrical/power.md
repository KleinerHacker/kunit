# Power (Electrical)

Package: `org.pcsoft.framework.kunit.common.power`
Base unit: **watt** (`KPowerUnit.BASE == KPowerUnit.WATT`)

Type: **constructed unit**

Power is a **constructed** unit: the composition `mass · length² · time⁻³` (`kg·m²·s⁻³`).
`KPowerUnitInstance` wraps a `KMixedUnitInstance` of three terms — `KMassUnit.BASE` (gram) at `+1`,
`KDistanceUnit.BASE` (meter) at `+2` and `KTimeUnit.BASE` (second) at `-3`. Because the mass component of the library is
normalized to **grams** (not kilograms), the canonical product is divided by 1000 to reach watts; the stored value is
always normalized to watts.

Power is technically **one** quantity that appears in several subject areas. This page describes its *electrical*
reading (`P = U · I`). The same Kotlin group is documented for the other areas in
[Power (Mechanics)](../mechanics/power.md) and [Power (Thermodynamics)](../thermodynamics/power.md).

## Building a power

Build a power with a named token, or from a decomposition (see below). Named units survive as value-1 tokens (used with
`of`/`into`):

| Power                        | Symbol  |                   Token |       1 unit in W |
|------------------------------|---------|------------------------:|------------------:|
| Watt                         | `W`     |                 `watts` |               1.0 |
| Metric horsepower            | `PS`    |     `metricHorsePowers` |         735.49875 |
| Mechanical horsepower        | `hp`    | `mechanicalHorsePowers` | 745.6998715822702 |
| Erg per second (CGS)         | `erg/s` |         `ergsPerSecond` |            1.0e-7 |
| Volt ampere (apparent power) | `VA`    |           `voltAmperes` |               1.0 |
| Volt ampere reactive         | `var`   |                  `vars` |               1.0 |

Named units support the SI prefixes via `KPrefixBuilder` (`kilo.watts`, `mega.watts`, `milli.watts`, …).

### Apparent and reactive power (VA, var)

In alternating current systems three powers are distinguished, all of them dimensionally identical to the watt:

* **active power** `P = U · I · cos φ` in watts (`W`) — the part that does work,
* **apparent power** `S = U · I` in volt amperes (`VA`) — the product of RMS voltage and RMS current,
* **reactive power** `Q = U · I · sin φ` in volt amperes reactive (`var`) — the part that oscillates between source and
  load without doing work.

Because the three differ only by convention, KUnit keeps them in this one group and separates them by symbol:
`1 VA = 1 var = 1 W`. Prefixes work as usual, so `kilo.voltAmperes` is 1 kVA and `kilo.vars` is 1 kvar.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.*

// A transformer rated 25 kVA feeding a load with power factor cos φ = 0.8:
val s = 25 of kilo.voltAmperes
val p = (25 * 0.8) of kilo.watts     // 20 kW active power
val q = (25 * 0.6) of kilo.vars      // 15 kvar reactive power
s into kilo.voltAmperes               // 25.0
q into kilo.vars                      // 15.0
```

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.*

val p = 2 of kilo.watts
p into kilo.watts               // 2.0
p into watts                    // 2000.0
(100 of metricHorsePowers) into kilo.watts // 73.549875
```

## Multiple decompositions

Power can be reached through several **equivalent decompositions**, all producing the same value-equal power:

| Expression           | Result type          | Meaning                                    |
|----------------------|----------------------|--------------------------------------------|
| `voltage * current`  | `KPowerUnitInstance` | electrical power `P = U · I` (commutative) |
| `force * speed`      | `KPowerUnitInstance` | mechanical power `P = F · v` (commutative) |
| `energy / time`      | `KPowerUnitInstance` | `P = W / t` (see [Energy](energy.md))      |
| `mass·length²/time³` | via `.toPower()`     | native canonical `kg·m²·s⁻³` expression    |

The typed operator forms return a power directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toPower()` (which recognises only the canonical normal form and throws
`IllegalStateException` otherwise). All routes are value-equal.

The inverse operators of the electrical form tie voltage, current and power together:

| Expression        | Result type                    | Meaning     |
|-------------------|--------------------------------|-------------|
| `power / current` | `KVoltageUnitInstance`         | `U = P / I` |
| `power / voltage` | `KElectricCurrentUnitInstance` | `I = P / U` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.common.power.*

// Real-world example - a mains socket: 230 V at 10 A delivers 2.3 kW.
val p = (230 of volts) * (10 of amperes)   // KPowerUnitInstance
p into kilo.watts                          // 2.3

// The definition solved for the current a 2.3 kW load draws at 230 V:
val i = (2.3 of kilo.watts) / (230 of volts) // KElectricCurrentUnitInstance, 10 A

// The same power as the native kg·m²·s⁻³ expression:
val raw = 2300 of (kilo.grams * (meters pow 2)) / (seconds pow 3)
raw.toPower() == (2.3 of kilo.watts)       // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.power.*

val s = (100 of watts) + (40 of watts)  // 140 W
(100 of watts) > (40 of watts)          // true
(100 of watts) * (40 of watts)          // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.*

(1 of kilo.watts).toString()     // "1000.0 W" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `⁻³`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written
both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics         | Kotlin                                            | Meaning                                       |
|---------------------|---------------------------------------------------|-----------------------------------------------|
| `W`                 | `watts`                                           | power, base unit (named token, watt)          |
| `U · I`             | `(230 of volts) * (10 of amperes)`                | electrical power from voltage and current     |
| `kg·m²/s³`          | `(kilo.grams * (meters pow 2)) / (seconds pow 3)` | power as mass·length² / time³ (fraction form) |
| `kg·m²·s⁻³`         | `kilo.grams * (meters pow 2) * (seconds pow -3)`  | same power as a pure product                  |
| `kW`                | `kilo.watts`                                      | prefixed power (kilowatt)                     |
| `S = U · I` in `VA` | `voltAmperes`                                     | apparent power (alternating current)          |
| `Q` in `var`        | `vars`                                            | reactive power (alternating current)          |
| `kVA`               | `kilo.voltAmperes`                                | prefixed apparent power (kilovolt ampere)     |
| `kvar`              | `kilo.vars`                                       | prefixed reactive power                       |
