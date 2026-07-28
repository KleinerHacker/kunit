# Energy (Thermodynamics)

Package: `org.pcsoft.framework.kunit.common.energy`
Base unit: **joule** (`KEnergyUnit.BASE == KEnergyUnit.JOULE`)

Type: **constructed unit**

Energy is a **constructed** unit: the composition `mass · length² · time⁻²` (`kg·m²·s⁻²`).
`KEnergyUnitInstance` wraps a `KMixedUnitInstance` of three terms — `KMassUnit.BASE` (gram) at `+1`,
`KDistanceUnit.BASE` (meter) at `+2` and `KTimeUnit.BASE` (second) at `-2`. Because the mass component of the
library is normalized to **grams** (not kilograms), the canonical product is divided by 1000 to reach joules;
the stored value is always normalized to joules.

Energy is technically **one** quantity that appears in several subject areas. This page describes its
*thermodynamic* reading — **heat**, `Q = Φ · t`. The same Kotlin group is documented for the other areas in
[Energy (Electrical)](../electrical/energy.md) and [Energy (Mechanics)](../mechanics/energy.md).

## Building an energy

Build an energy with a named token, or from a decomposition (see below). Named units survive as value-1 tokens
(used with `of`/`into`). The thermal units of the group are the calorie and the British thermal unit:

| Energy | Symbol | Token | 1 unit in J |
|---|---|---:|---:|
| Joule | `J` | `joules` | 1.0 |
| Erg (CGS) | `erg` | `ergs` | 1.0e-7 |
| Calorie (thermochemical) | `cal` | `calories` | 4.184 |
| Electron volt | `eV` | `electronVolts` | 1.602176634e-19 |
| British thermal unit | `BTU` | `britishThermalUnits` | 1055.05585262 |

Named units support the SI prefixes via `KPrefixBuilder` (`kilo.calories` — the "food calorie" —
`kilo.joules`, `mega.joules`, …).

The **kilowatt hour has no token of its own** — it is not a genuinely named unit but the product
`kilo.watts * hours` and is built that way.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*

val q = 2000 of kilo.calories   // a daily diet
q into kilo.joules              // 8368.0
q into britishThermalUnits      // 7931.79...
```

## Multiple decompositions

Energy can be reached through several **equivalent decompositions**, all producing the same value-equal
energy:

| Expression | Result type | Meaning |
|---|---|---|
| `power * time` | `KEnergyUnitInstance` | heat from a heat flow over time `Q = Φ · t` (commutative) |
| `power / frequency` | `KEnergyUnitInstance` | the inverse-time form (`W/Hz = W·s`) |
| `force * length` | `KEnergyUnitInstance` | mechanical work `W = F · s` (see [Energy (Mechanics)](../mechanics/energy.md)) |
| `charge * voltage` | `KEnergyUnitInstance` | electrical energy `W = Q · U` (see [Energy (Electrical)](../electrical/energy.md)) |
| `mass·length²/time²` | via `.toEnergy()` | native canonical `kg·m²·s⁻²` expression |

The typed operator forms return an energy directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toEnergy()` (which recognises only the canonical normal form and
throws `IllegalStateException` otherwise). All routes are value-equal.

The inverse operators tie heat flow, time and heat together:

| Expression | Result type | Meaning |
|---|---|---|
| `energy / time` | `KPowerUnitInstance` | the heat flow rate `Φ = Q / t` (see [Power (Thermodynamics)](power.md)) |
| `energy / power` | `KTimeUnitInstance` | the heating time `t = Q / Φ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.common.energy.*

// Real-world example - a water boiler: a 2 kW heat flow over 10 minutes delivers 1200 kJ of heat.
val q = (2 of kilo.watts) * (10 of minutes)   // KEnergyUnitInstance
q into kilo.joules                            // 1200.0

// The heat solved for the heating time of a 2 kW boiler:
val t = (1200 of kilo.joules) / (2 of kilo.watts)  // KTimeUnitInstance, 600 s

// And solved for the heat flow rate:
val flow = (1200 of kilo.joules) / (10 of minutes) // KPowerUnitInstance, 2 kW

// The same heat as the native kg·m²·s⁻² expression:
val raw = 1_200_000 of (kilo.grams * (meters pow 2)) / (seconds pow 2)
raw.toEnergy() == (1200 of kilo.joules)            // true
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

(1 of britishThermalUnits).toString()     // "1055.05585262 J" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `⁻²`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `J` | `joules` | energy (heat), base unit (named token, joule) |
| `Φ · t` | `(2 of kilo.watts) * (10 of minutes)` | heat from heat flow rate and time |
| `kcal` | `kilo.calories` | prefixed thermal energy (food calorie) |
| `kg·m²/s²` | `(kilo.grams * (meters pow 2)) / (seconds pow 2)` | energy as mass·length² / time² (fraction form) |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)` | same energy as a pure product |
