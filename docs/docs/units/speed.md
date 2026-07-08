# Speed

Package: `org.pcsoft.framework.kunit.speed`
Base unit: **meter per second** (`KSpeedUnit.BASE == KSpeedUnit.METERS_PER_SECOND`)

Speed is the first **constructed** unit: unlike length or time it is not a single "real" quantity but a
composition, `length · time⁻¹` (`m/s`). `KSpeedUnitInstance` therefore wraps a `KMixedUnitInstance` of
exactly two terms - one `KDistanceUnit.BASE` (meter) at exponent `+1` and one `KTimeUnit.BASE` (second) at
exponent `-1`. The value is always stored normalized to meters per second, regardless of which unit or
length/time combination it was created from.

## Building a speed

A speed is built as a **length-per-time expression**, e.g. `10 of kilo.meters / hours` or
`100 of meters / (10 of seconds)` — both yield a `KSpeedUnitInstance`. Read it back in any length-per-time
template (`v into (kilo.meters / hours)`). There are deliberately **no** spelled-out composite tokens like
`metersPerSecond` or `kilometersPerHour` (they are exactly `meters / seconds` / `kilo.meters / hours`).

Only speeds with a genuinely single, conventional name survive as value-1 tokens (used with `of`/`into`):

| Speed | Symbol | Token | 1 unit in m/s |
|---|---|---:|---:|
| Knot | `kn` | `knots` | 0.514444 (1852/3600) |
| Mach (ISA sea level) | `Ma` | `mach` | 340.29 |
| Speed of light | `c` | `speedOfLight` | 299792458.0 |

> **Mach** is the speed of sound in the International Standard Atmosphere at sea level (15 °C). It is a
> convenient reference point, not a physical constant - the real speed of sound varies with temperature
> and altitude.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.miles
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.speed.*

val v = 50 of kilo.meters / hours
v.value                        // 13.888... (normalized to m/s)
v into (kilo.meters / hours)   // 50.0 (read back in km/h)
v into (miles / hours)         // ≈ 31.07
v into knots                   // ≈ 26.998
v into mach                    // ≈ 0.0408 (fraction of the speed of sound)
```

## Computing with the core units (length & time)

This is the whole point of a constructed unit. A speed *is* a length divided by a time. KUnit lets you move
between the three quantities - length, time and speed - with plain `*` and `/`, and each result is
**strongly typed**. You never have to build or unwrap a raw `KMixedUnitInstance` yourself.

| Expression | Result type | Meaning |
|---|---|---|
| `length / time` | `KSpeedUnitInstance` | speed = distance / duration |
| `speed * time` | `KLengthUnitInstance` | distance = speed × duration |
| `time * speed` | `KLengthUnitInstance` | distance (commutative) |
| `length / speed` | `KTimeUnitInstance` | duration = distance / speed |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.speed.*

// --- core units -> speed ------------------------------------------------
val v = (100 of meters) / (10 of seconds)  // KSpeedUnitInstance (NO .toSpeed() needed!)
v.value                    // 10.0 (m/s)
v into (kilo.meters / hours) // 36.0
v into (miles / hours)     // ≈ 22.37
v into knots               // ≈ 19.44

// Prefix the length for a compact rate (klammerfrei, `of` binds weaker than `/`):
val fast = 10 of kilo.meters / hours   // KSpeedUnitInstance

// --- speed -> length (multiply by a time) -------------------------------
val distance = v * (60 of seconds)     // KLengthUnitInstance
distance into meters       // 600.0
distance into feet         // ≈ 1968.5
(60 of seconds) * v        // same result (commutative)

// --- speed -> time (divide a length by it) ------------------------------
val time = (600 of meters) / v         // KTimeUnitInstance
time into minutes          // 1.0
```

!!! warning "Only a *pure* length divides into a speed"
    `length / time` and `length / speed` require the length to have exponent 1. An **area** (`m²`) or a
    **volume** (`m³`) is not a length, so `area / time` would be `m²/s`, not a speed - the operator throws
    `IllegalStateException` rather than silently returning a wrong value. To build such an intermediate
    deliberately, drop one operand to the mixed level with `toUnit()`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

val area = (2 of meters) * (2 of meters)         // KAreaUnitInstance
val areaPerTime = area.toUnit() / (2 of seconds).toUnit() // KMixedUnitInstance, [METER^2, SECOND^-1]
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*

// + / - : same group, automatic conversion between different speed expressions
val a = (36 of kilo.meters / hours) + (10 of meters / seconds)  // KSpeedUnitInstance, 20 m/s
val b = (20 of meters / seconds) - (36 of kilo.meters / hours)  // 10 m/s

// comparisons (by normalized m/s value)
(50 of kilo.meters / hours) > (10 of meters / seconds)   // true
(36 of kilo.meters / hours) == (10 of meters / seconds)  // true

// * / / between two speeds escape to a KMixedUnitInstance (no longer a pure speed)
val squared = (10 of meters / seconds) * (2 of meters / seconds) // KMixedUnitInstance, [m^2, s^-2]
```

## toString formatting

Only the base-unit `toString()` exists; format a specific unit via `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*

(10 of meters / seconds).toString()   // "10.0 m/s" (base unit)
"${(10 of meters / seconds) into (kilo.meters / hours)} km/h" // "36.0 km/h"
```
