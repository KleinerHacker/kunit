# Voltage

Package: `org.pcsoft.framework.kunit.voltage`
Base unit: **volt** (`KVoltageUnit.BASE == KVoltageUnit.VOLT`)

Type: **constructed unit**

Voltage (electric potential difference) is a **constructed** unit: the composition
`mass · length² · time⁻³ · current⁻¹` (`kg·m²·s⁻³·A⁻¹`). `KVoltageUnitInstance` wraps a
`KMixedUnitInstance` of four terms — `KMassUnit.BASE` (gram) at `+1`, `KDistanceUnit.BASE` (meter) at
`+2`, `KTimeUnit.BASE` (second) at `-3` and `KElectricCurrentUnit.BASE` (ampere) at `-1`. Because the
mass component of the library is normalized to **grams** (not kilograms), the volt is 1000× the raw
component base; the stored value is normalized to volts.

## Building a voltage

Build a voltage with a named token, or from a decomposition (see below). Named units survive as value-1
tokens (used with `of`/`into`):

| Voltage | Symbol | Token | 1 unit in V |
|---|---|---:|---:|
| Volt | `V` | `volts` | 1.0 |
| Statvolt (CGS-ESU) | `statV` | `statvolts` | 299.792458 |
| Abvolt (CGS-EMU) | `abV` | `abvolts` | 1.0e-8 |
| Weston cell | `V_W` | `westonCells` | 1.0183 |
| Daniell cell | `V_Da` | `daniells` | 1.1 |

Named units support the SI prefixes via `KPrefixBuilder` (`kilo.volts`, `mega.volts`, `milli.volts`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.voltage.*

val u = 230 of volts
u into volts                 // 230.0
u into kilo.volts            // 0.23
(1 of kilo.volts) into volts // 1000.0
```

## Multiple decompositions

Voltage can be reached through several **equivalent decompositions**, all producing the same value-equal
voltage:

| Expression | Result type | Meaning |
|---|---|---|
| `resistance * current` | `KVoltageUnitInstance` | Ohm's law `U = R · I` (see Resistance) |
| `current * resistance` | `KVoltageUnitInstance` | Ohm's law (commutative) |
| `mass·length²/(time³·current)` | via `.toVoltage()` | native canonical `kg·m²·s⁻³·A⁻¹` expression |

The typed operator forms return a voltage directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toVoltage()` (which recognises only the canonical normal form
and throws `IllegalStateException` otherwise). Both routes are value-equal.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.resistance.ohms
import org.pcsoft.framework.kunit.voltage.*

// Real-world example - Ohm's law: a 115 Ω resistor carrying 2 A drops 230 V.
val u = (115 of ohms) * (2 of amperes)   // KVoltageUnitInstance, 230 V

// The same voltage as the native kg·m²·s⁻³·A⁻¹ expression:
val raw = 230 of (kilo.grams * (meters pow 2)) / (amperes * (seconds pow 3))
raw.toVoltage() == (230 of volts)        // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.voltage.*

val s = (100 of volts) + (40 of volts)  // 140 V
(100 of volts) > (40 of volts)          // true
(100 of volts) * (40 of volts)          // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.voltage.*

(230 of volts).toString()    // "230.0 V" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `V` | `volts` | voltage, base unit (named token, volt) |
| `kg·m²/(s³·A)` | `kilo.grams * (meters pow 2) / (amperes * (seconds pow 3))` | voltage as mass·length² / (time³·current) (fraction form) |
| `kg·m²·s⁻³·A⁻¹` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -1)` | same voltage as a pure product |
| `kV` | `kilo.volts` | prefixed voltage (kilovolt) |
