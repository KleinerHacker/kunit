# Volumetric Flow

Package: `org.pcsoft.framework.kunit.kinematic.volumeflow`
Base unit: **cubic meter per second** (`KVolumeFlowUnit.BASE == KVolumeFlowUnit.CUBIC_METER_PER_SECOND`)

Type: **constructed unit**

Volumetric flow (volume flow rate) describes how much volume passes a cross-section per unit of time:
`distance³ · time⁻¹` (`m³/s`). `KVolumeFlowUnitInstance` wraps a `KMixedUnitInstance` of exactly two terms
— one `KDistanceUnit.BASE` (meter) at exponent `+3` and one `KTimeUnit.BASE` (second) at exponent `-1`. The
value is always stored normalized to cubic meters per second, regardless of which unit or volume/time
combination it was created from.

Unlike energy or power, volumetric flow has **no** mass dimension, so its stored value *is* the reading in
`m³/s` — no gram/kilogram bridge is involved.

## Named units

| Unit | Symbol | Token | 1 unit in m³/s |
|---|---|---:|---:|
| Cubic meter per second | `m³/s` | `cubicMetersPerSecond` | 1.0 |
| Cubic meter per hour | `m³/h` | `cubicMetersPerHour` | 1/3600 ≈ 2.778e-4 |
| Liter per second | `l/s` | `litersPerSecond` | 0.001 |
| Liter per minute | `l/min` | `litersPerMinute` | 0.001/60 ≈ 1.667e-5 |
| US gallon per minute | `gpm` | `usGallonsPerMinute` | ≈ 6.309e-5 |

All of them accept the full SI prefix range as well (`milli.litersPerSecond`, `kilo.cubicMetersPerHour`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

val q = 5 of litersPerSecond
q.value                       // 0.005 (normalized to m³/s)
q into litersPerMinute        // 300.0
q into cubicMetersPerHour     // 18.0
q into usGallonsPerMinute     // ≈ 79.25
(250 of milli.litersPerSecond) into litersPerSecond // 0.25
```

## Real-world example: filling a rainwater tank

A garden pump delivers 300 l/min into a 5 m³ tank. How long does the tank take to fill, and what is the
flow rate expressed in the units a pump datasheet uses?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

val pump = 300 of litersPerMinute
val tank = 5000 of liters

val fillTime = tank / pump          // KTimeUnitInstance
fillTime into minutes               // ≈ 16.67 minutes

pump into cubicMetersPerHour        // 18.0 m³/h (datasheet unit)
pump into usGallonsPerMinute        // ≈ 79.25 gpm

// The other direction: how much water in a quarter of an hour?
val volume = pump * (15 of minutes) // KVolumeUnitInstance
volume into liters                  // 4500.0
```

## Computing with the core units (volume & time)

| Expression | Result type | Meaning |
|---|---|---|
| `volume / time` | `KVolumeFlowUnitInstance` | flow rate = volume / duration |
| `volumeFlow * time` | `KVolumeUnitInstance` | volume = flow rate × duration |
| `time * volumeFlow` | `KVolumeUnitInstance` | volume (commutative) |
| `volume / volumeFlow` | `KTimeUnitInstance` | duration = volume / flow rate |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

val q = (600 of liters) / (2 of minutes)  // KVolumeFlowUnitInstance
q into cubicMetersPerSecond               // 0.005

val v = q * (60 of seconds)               // KVolumeUnitInstance
v into liters                             // 300.0

val t = (600 of liters) / q               // KTimeUnitInstance
t into minutes                            // 2.0
```

## Decompositions

Volumetric flow can be reached two ways; both produce the same typed, value-equal instance.

| Decomposition | Form | Result |
|---|---|---|
| `volume / time` | typed operator | `KVolumeFlowUnitInstance` directly |
| `distance³ · time⁻¹` | native expression + `toVolumeFlow()` | `KVolumeFlowUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

// typed operator form
val typed = (8000 of liters) / (4 of seconds)

// native base-dimension form (m³ · s⁻¹), recognised by toVolumeFlow()
val native = (((2 of meters).toUnit() pow 3) / (4 of seconds).toUnit()).toVolumeFlow()

typed == native // true - both are 2.0 m³/s
```

`toVolumeFlow()` recognises **only** the canonical normal form (one `KDistanceUnit` term at exponent `+3`
and one `KTimeUnit` term at exponent `-1`); any equivalent expression reduces onto it automatically. A
wrong shape throws `IllegalStateException` rather than silently returning a wrong value.

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

// + / - : same group, automatic conversion between different flow units
val a = (1 of litersPerSecond) + (60 of litersPerMinute)   // 2 l/s
val b = (1 of litersPerSecond) - (30 of litersPerMinute)   // 0.5 l/s

// comparisons (by normalized m³/s value)
(1 of litersPerSecond) > (30 of litersPerMinute)   // true
(1 of litersPerSecond) == (60 of litersPerMinute)  // true

// * / / between two flows escape to a KMixedUnitInstance
val squared = (1 of litersPerSecond) * (1 of litersPerSecond) // KMixedUnitInstance, [m^6, s^-2]
```

## toString formatting

`toString()` renders the value in the base unit; use `into` for any other unit:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.volumeflow.*

(5 of litersPerSecond).toString()                       // "0.005 m³/s"
"${(5 of litersPerSecond) into litersPerMinute} l/min"  // "300.0 l/min"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `m³/s` | `cubicMetersPerSecond` | volumetric flow, base unit — named token |
| `m³·s⁻¹` | `(meters pow 3) / seconds` | same flow as a base-dimension expression |
| `l/s` | `litersPerSecond` | liter per second |
| `l/min` | `litersPerMinute` | liter per minute |
| `m³/h` | `cubicMetersPerHour` | cubic meter per hour |
| `V / t` | `(600 of liters) / (2 of minutes)` | build from volume ÷ time |
| `V = q̇ · t` | `q * (60 of seconds)` | volume from flow rate × duration |
| `t = V / q̇` | `(600 of liters) / q` | duration from volume ÷ flow rate |
