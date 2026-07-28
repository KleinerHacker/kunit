# Energy (Electrical)

Package: `org.pcsoft.framework.kunit.common.energy`
Base unit: **joule** (`KEnergyUnit.BASE == KEnergyUnit.JOULE`)

Type: **constructed unit**

Energy is a **constructed** unit: the composition `mass · length² · time⁻²` (`kg·m²·s⁻²`).
`KEnergyUnitInstance` wraps a `KMixedUnitInstance` of three terms — `KMassUnit.BASE` (gram) at `+1`,
`KDistanceUnit.BASE` (meter) at `+2` and `KTimeUnit.BASE` (second) at `-2`. Because the mass component of the
library is normalized to **grams** (not kilograms), the canonical product is divided by 1000 to reach joules;
the stored value is always normalized to joules.

Energy is technically **one** quantity that appears in several subject areas. This page describes its
*electrical* reading (`W = Q · U`, and `W = P · t` for consumed electrical energy). The same Kotlin group is
documented for the other areas in [Energy (Mechanics)](../mechanics/energy.md) and
[Energy (Thermodynamics)](../thermodynamics/energy.md).

## Building an energy

Build an energy with a named token, or from a decomposition (see below). Named units survive as value-1 tokens
(used with `of`/`into`):

| Energy | Symbol | Token | 1 unit in J |
|---|---|---:|---:|
| Joule | `J` | `joules` | 1.0 |
| Erg (CGS) | `erg` | `ergs` | 1.0e-7 |
| Calorie (thermochemical) | `cal` | `calories` | 4.184 |
| Electron volt | `eV` | `electronVolts` | 1.602176634e-19 |
| British thermal unit | `BTU` | `britishThermalUnits` | 1055.05585262 |

Named units support the SI prefixes via `KPrefixBuilder` (`kilo.joules`, `mega.joules`, `mega.electronVolts`, …).

The **kilowatt hour has no token of its own** — it is not a genuinely named unit but the product
`kilo.watts * hours` and is built that way.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.common.energy.*

val w = 500 of kilo.joules
w into kilo.joules                          // 500.0
w into joules                               // 500000.0

val kwh = (1 of kilo.watts) * (1 of hours)  // 1 kWh = 3.6 MJ
kwh into kilo.joules                        // 3600.0
```

## Multiple decompositions

Energy can be reached through several **equivalent decompositions**, all producing the same value-equal
energy:

| Expression | Result type | Meaning |
|---|---|---|
| `charge * voltage` | `KEnergyUnitInstance` | electrical energy `W = Q · U` (commutative) |
| `power * time` | `KEnergyUnitInstance` | consumed energy `W = P · t` (commutative) |
| `power / frequency` | `KEnergyUnitInstance` | the inverse-time form (`W/Hz = W·s`) |
| `force * length` | `KEnergyUnitInstance` | mechanical work `W = F · s` (see [Energy (Mechanics)](../mechanics/energy.md)) |
| `mass·length²/time²` | via `.toEnergy()` | native canonical `kg·m²·s⁻²` expression |

The typed operator forms return an energy directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toEnergy()` (which recognises only the canonical normal form and
throws `IllegalStateException` otherwise). All routes are value-equal.

The inverse operators tie charge, voltage, power, time and energy together:

| Expression | Result type | Meaning |
|---|---|---|
| `energy / charge` | `KVoltageUnitInstance` | `U = W / Q` |
| `energy / time` | `KPowerUnitInstance` | `P = W / t` |
| `energy / power` | `KTimeUnitInstance` | `t = W / P` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.common.energy.*

// Real-world example - a 2 kW heater running 3 hours consumes 6 kWh = 21600 kJ.
val w = (2 of kilo.watts) * (3 of hours)   // KEnergyUnitInstance
w into kilo.joules                         // 21600.0

// Electrical energy from charge and voltage: 10 C moved across 50 V is 500 J.
val fromCharge = (10 of coulombs) * (50 of volts)  // KEnergyUnitInstance, 500 J

// The definition solved for the voltage:
val u = (500 of joules) / (10 of coulombs)         // KVoltageUnitInstance, 50 V

// The same energy as the native kg·m²·s⁻² expression:
val raw = 500 of (kilo.grams * (meters pow 2)) / (seconds pow 2)
raw.toEnergy() == (500 of joules)                  // true
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
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*

(1 of kilo.joules).toString()     // "1000.0 J" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `⁻²`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `J` | `joules` | energy, base unit (named token, joule) |
| `Q · U` | `(10 of coulombs) * (50 of volts)` | electrical energy from charge and voltage |
| `P · t` | `(2 of kilo.watts) * (3 of hours)` | consumed energy (the kWh has no token) |
| `kg·m²/s²` | `(kilo.grams * (meters pow 2)) / (seconds pow 2)` | energy as mass·length² / time² (fraction form) |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)` | same energy as a pure product |
| `kJ` | `kilo.joules` | prefixed energy (kilojoule) |
