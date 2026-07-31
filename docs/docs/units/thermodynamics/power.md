# Power (Thermodynamics)

Package: `org.pcsoft.framework.kunit.common.power`
Base unit: **watt** (`KPowerUnit.BASE == KPowerUnit.WATT`)

Type: **constructed unit**

Power is a **constructed** unit: the composition `mass · length² · time⁻³` (`kg·m²·s⁻³`).
`KPowerUnitInstance` wraps a `KMixedUnitInstance` of three terms — `KMassUnit.BASE` (gram) at `+1`,
`KDistanceUnit.BASE` (meter) at `+2` and `KTimeUnit.BASE` (second) at `-3`. Because the mass component of the library is
normalized to **grams** (not kilograms), the canonical product is divided by 1000 to reach watts; the stored value is
always normalized to watts.

Power is technically **one** quantity that appears in several subject areas. This page describes its *thermodynamic*
reading — the **heat flow rate** `Φ = Q / t`, i.e. a thermal energy per time. The same Kotlin group is documented for
the other areas in [Power (Electrical)](../electrical/power.md) and
[Power (Mechanics)](../mechanics/power.md).

Within thermodynamics the same type also carries the **heat flow** reading — see
[Heat flow](heat-flow.md) for that perspective and for the chain down to
[heat flux density](heat-flux-density.md).

## Building a power

Build a power with a named token, or from a decomposition (see below). Named units survive as value-1 tokens (used with
`of`/`into`):

| Power                 | Symbol  |                   Token |       1 unit in W |
|-----------------------|---------|------------------------:|------------------:|
| Watt                  | `W`     |                 `watts` |               1.0 |
| Metric horsepower     | `PS`    |     `metricHorsePowers` |         735.49875 |
| Mechanical horsepower | `hp`    | `mechanicalHorsePowers` | 745.6998715822702 |
| Erg per second (CGS)  | `erg/s` |         `ergsPerSecond` |            1.0e-7 |

Named units support the SI prefixes via `KPrefixBuilder` (`kilo.watts`, `mega.watts`, `milli.watts`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.*

val heatFlow = 9 of kilo.watts   // a room heater
heatFlow into kilo.watts         // 9.0
heatFlow into watts              // 9000.0
```

## Multiple decompositions

Power can be reached through several **equivalent decompositions**, all producing the same value-equal power:

| Expression           | Result type          | Meaning                                                                         |
|----------------------|----------------------|---------------------------------------------------------------------------------|
| `energy / time`      | `KPowerUnitInstance` | heat flow rate `Φ = Q / t` (see [Energy (Thermodynamics)](energy.md))           |
| `voltage * current`  | `KPowerUnitInstance` | electrical power `P = U · I` (see [Power (Electrical)](../electrical/power.md)) |
| `force * speed`      | `KPowerUnitInstance` | mechanical power `P = F · v` (see [Power (Mechanics)](../mechanics/power.md))   |
| `mass·length²/time³` | via `.toPower()`     | native canonical `kg·m²·s⁻³` expression                                         |

The typed operator forms return a power directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toPower()` (which recognises only the canonical normal form and throws
`IllegalStateException` otherwise). All routes are value-equal.

The inverse operators of the heat-flow form tie energy, time and power together:

| Expression       | Result type           | Meaning                                       |
|------------------|-----------------------|-----------------------------------------------|
| `power * time`   | `KEnergyUnitInstance` | the heat delivered, `Q = Φ · t` (commutative) |
| `energy / power` | `KTimeUnitInstance`   | the time needed, `t = Q / Φ`                  |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.common.energy.*
import org.pcsoft.framework.kunit.common.power.*

// Real-world example - a water boiler: 1200 kJ of heat delivered within 10 minutes is a 2 kW heat flow.
val heatFlow = (1200 of kilo.joules) / (10 of minutes)   // KPowerUnitInstance
heatFlow into kilo.watts                                 // 2.0

// The heat flow solved for the heat delivered in one hour:
val heat = (2 of kilo.watts) * (60 of minutes)           // KEnergyUnitInstance, 7.2 MJ

// The same heat flow as the native kg·m²·s⁻³ expression:
val raw = 2000 of (kilo.grams * (meters pow 2)) / (seconds pow 3)
raw.toPower() == (2 of kilo.watts)                       // true
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

(9 of kilo.watts).toString()     // "9000.0 W" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `⁻³`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written
both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin                                            | Meaning                                               |
|-------------|---------------------------------------------------|-------------------------------------------------------|
| `W`         | `watts`                                           | power (heat flow rate), base unit (named token, watt) |
| `Q / t`     | `(1200 of kilo.joules) / (10 of minutes)`         | heat flow rate from heat and time                     |
| `kg·m²/s³`  | `(kilo.grams * (meters pow 2)) / (seconds pow 3)` | power as mass·length² / time³ (fraction form)         |
| `kg·m²·s⁻³` | `kilo.grams * (meters pow 2) * (seconds pow -3)`  | same power as a pure product                          |
| `kW`        | `kilo.watts`                                      | prefixed power (kilowatt)                             |
