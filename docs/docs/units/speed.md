# Speed

Package: `org.pcsoft.framework.kunit.speed`
Base unit: **meter per second** (`KSpeedUnit.BASE == KSpeedUnit.METERS_PER_SECOND`)

Speed is the first **constructed** unit: unlike length or time it is not a single "real" quantity but a
composition, `length · time⁻¹` (`m/s`). `KSpeedUnitInstance` therefore wraps a `KMixedUnitInstance` of
exactly two terms - one `KDistanceUnit.BASE` (meter) at exponent `+1` and one `KTimeUnit.BASE` (second) at
exponent `-1`. The value is always stored normalized to meters per second, regardless of which unit,
prefix, or length/time combination it was created from.

## Units

| Unit | Enum value | Symbol | Creator | 1 unit in m/s |
|---|---|---|---:|---:|
| Meter per second | `KSpeedUnit.METERS_PER_SECOND` | `m/s` | `Number.metersPerSecond` | 1.0 |
| Kilometer per hour | `KSpeedUnit.KILOMETERS_PER_HOUR` | `km/h` | `Number.kilometersPerHour` | 0.277778 (1000/3600) |
| Mile per hour | `KSpeedUnit.MILES_PER_HOUR` | `mph` | `Number.milesPerHour` | 0.44704 (1609.344/3600) |
| Knot | `KSpeedUnit.KNOT` | `kn` | `Number.knots` | 0.514444 (1852/3600) |
| Foot per second | `KSpeedUnit.FEET_PER_SECOND` | `ft/s` | `Number.feetPerSecond` | 0.3048 |
| Mach (ISA sea level) | `KSpeedUnit.MACH` | `Ma` | `Number.mach` | 340.29 |
| Speed of light | `KSpeedUnit.LIGHT_SPEED` | `c` | `Number.speedOfLight` | 299792458.0 |

Every unit above has a matching bare `val` alias for use as a `valueAs`/`toString` target or as the `unit`
argument of a prefix `infix` function: `metersPerSecond`, `kilometersPerHour`, `milesPerHour`, `knots`,
`feetPerSecond`, `mach`, `speedOfLight`.

> **Mach** is the speed of sound in the International Standard Atmosphere at sea level (15 °C). It is a
> convenient reference point, not a physical constant - the real speed of sound varies with temperature
> and altitude.

```kotlin
import org.pcsoft.framework.kunit.speed.*

val v = 50.kilometersPerHour
v.value                                    // 13.888... (normalized to m/s)
v.valueAs(KSpeedUnit.KILOMETERS_PER_HOUR)  // 50.0 (read back in km/h)
v.valueAs(milesPerHour)                     // ≈ 31.07
v.valueAs(knots)                            // ≈ 26.998
v.valueAs(mach)                             // ≈ 0.0408 (fraction of the speed of sound)
```

## Computing with the core units (length & time)

This is the whole point of a constructed unit, and the part that is **not** obvious - read this section
carefully.

**Mental model:** a speed *is* a length divided by a time. KUnit lets you move between the three
quantities - length, time and speed - with plain `*` and `/`, and each result is **strongly typed**. You
never have to build or unwrap a raw `KMixedUnitInstance` yourself.

The four legal combinations and their result type:

| Expression | Result type | Meaning |
|---|---|---|
| `length / time` | `KSpeedUnitInstance` | speed = distance / duration |
| `speed * time` | `KLengthUnitInstance` | distance = speed × duration |
| `time * speed` | `KLengthUnitInstance` | distance (commutative) |
| `length / speed` | `KTimeUnitInstance` | duration = distance / speed |

```kotlin
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.speed.*

// --- core units -> speed ------------------------------------------------
val v = 100.meters / 10.seconds          // KSpeedUnitInstance (NO .toSpeed() needed!)
v.value                                     // 10.0 (m/s)
v.valueAs(KSpeedUnit.KILOMETERS_PER_HOUR)   // 36.0
v.valueAs(KSpeedUnit.MILES_PER_HOUR)        // ≈ 22.37
v.valueAs(KSpeedUnit.KNOT)                  // ≈ 19.44
v.valueAs(KSpeedUnit.MACH)                  // ≈ 0.0294
v.valueAs(KSpeedUnit.LIGHT_SPEED)           // ≈ 3.336e-8

// The assignment target type does NOT convert anything - the operator already
// returns a KSpeedUnitInstance. Kotlin has no implicit conversions.
val explicit: KSpeedUnitInstance = 100.meters / 10.seconds

// --- speed -> length (multiply by a time) -------------------------------
val distance = v * 60.seconds             // KLengthUnitInstance
distance.value                              // 600.0 (m)
distance.valueAs(KDistanceUnit.METER)         // 600.0
distance.valueAs(feet)                      // ≈ 1968.5 (read back in any length unit)
distance.valueAs(miles)                     // ≈ 0.373
60.seconds * v                            // same result (commutative)

// --- speed -> time (divide a length by it) ------------------------------
val time = 600.meters / v                 // KTimeUnitInstance
time.value                                  // 60.0 (s)
time.valueAs(KTimeUnit.MINUTE)              // 1.0
time.valueAs(KTimeUnit.HOUR)                // ≈ 0.0167
```

!!! warning "Only a *pure* length divides into a speed"
    `length / time` and `length / speed` require the length to have exponent 1. An **area** (`m²`, e.g.
    `2.hectares`) or a **volume** (`m³`) is not a length, so `area / time` would be `m²/s`, not a speed -
    the operator throws `IllegalStateException` rather than silently returning a wrong value. Likewise,
    `length * time` (an `m·s`, not a speed) and `length + speed` (different dimensions) are not valid
    speed constructions.

### Deliberately computing a non-speed intermediate (e.g. m²/s)

Because a Kotlin operator has a single, compile-time return type, `KLengthUnitInstance / KTimeUnitInstance`
is *reserved* for building a typed speed and cannot instead yield an `m²/s`. That intermediate is **not
lost**, though - drop one operand to the mixed level with `toUnit()`, which selects the
generic `KMixedUnitInstance` `/` operator (arbitrary exponents, no speed check). This explicit
`toUnit()` is the intended signal that you are leaving the strongly-typed paths. (This applies only
because `length`/`time` already carry dedicated, more specific cross-group operators that take
precedence. For two pure units of groups **without** such an operator - e.g. `10.metersPerSecond * 5.bytes` -
the general `KUnitInstance` `*`/`/` operators apply directly and return a `KMixedUnitInstance`, no
`toUnit()` required.)

```kotlin
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

val area = 2.hectares                 // KLengthUnitInstance, exponent 2 (20 000 m²)

// area / 2.seconds                   // ❌ throws IllegalStateException (would be m²/s, not a speed)

// ✅ deliberate m²/s intermediate: one operand at the mixed level
val areaPerTime = area.toUnit() / 2.seconds.toUnit()
areaPerTime.value                       // 10000.0
areaPerTime.units                       // [METER^2, SECOND^-1]

// ...and it chains onward like any KMixedUnitInstance
val backToArea = areaPerTime * 4.seconds.toUnit() // units=[METER^2], value=40000.0
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.speed.*

// + / - : same group, automatic conversion between different speed units
val a = 36.kilometersPerHour + 10.metersPerSecond  // KSpeedUnitInstance, 20 m/s
val b = 20.metersPerSecond - 36.kilometersPerHour  // 10 m/s

// comparisons (by normalized m/s value)
50.kilometersPerHour > 10.metersPerSecond   // true  (13.89 m/s > 10 m/s)
36.kilometersPerHour == 10.metersPerSecond  // true  (same normalized value)

// * / / between two speeds escape to a KMixedUnitInstance (no longer a pure speed)
val squared = 10.metersPerSecond * 2.metersPerSecond // KMixedUnitInstance, units=[m^2, s^-2]
```

## Comparisons and equality

`==`, `!=`, `<`, `<=`, `>`, `>=` compare the normalized `value` (meters per second) of two
`KSpeedUnitInstance`s. Since a speed always has the same dimension, no exponent check is needed (unlike
length, where an area and a length cannot be compared).

## SI prefixes

Any `KSpeedUnit` can be combined with any of the 24 SI prefixes (`KUnitPrefix`, Quetta/Q to Quecto/q)
using the speed-group `infix` construction functions (which return a `KSpeedUnitInstance` directly) and
`with` (for `valueAs`/`toString` targets):

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.speed.*

// Construction: "5 kilo metersPerSecond" -> KSpeedUnitInstance (direct, == 5000.metersPerSecond)
val fast = 5 kilo metersPerSecond
fast.value // 5000.0

// Reading back a value using a prefixed target
val v = 5.metersPerSecond
v.valueAs(KUnitPrefix.KILO with KSpeedUnit.METERS_PER_SECOND)  // 0.005
```

You can also read a speed back as an explicit **length-per-time pair** (two targets), which is how a
"km/h" is expressed from its length and time parts:

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.distance.KDistanceUnit
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.speed.*

val v = 10.metersPerSecond
v.valueAs(KUnitPrefix.KILO with KDistanceUnit.METER, KTimeUnit.HOUR)   // 36.0 (km per h)
v.toString(KUnitPrefix.KILO with KDistanceUnit.METER, KTimeUnit.HOUR)  // "36.0 km*h^-1"
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.speed.*

10.metersPerSecond.toString()                            // "10.0 m/s" (base unit)
(100.meters / 10.seconds).toString(KSpeedUnit.KILOMETERS_PER_HOUR) // "36.0 km/h"
1.mach.toString(KSpeedUnit.MACH)                          // "1.0 Ma"
```
