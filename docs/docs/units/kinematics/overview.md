# Kinematics — Overview

Packages: `org.pcsoft.framework.kunit.kinematic.distance`, `…time`, `…speed`, `…acceleration`,
`…frequency`, `…volumeflow`

Kinematics is the description of **motion** — how far, how long, how fast, and how the rate of motion
itself changes — without yet asking about the forces behind it (that is the [Mechanics](../mechanics/overview.md)
topic). KUnit models this field with two **native** base quantities and three quantities **constructed**
from them, so the classic motion formulas turn into ordinary `*` and `/` expressions that stay strongly
typed.

## Units in this topic

| Unit | Type | Base unit | Page |
|---|---|---|---|
| Distance | native | metre (`m`) | [Distance](distance.md) |
| Time | native | second (`s`) | [Time](time.md) |
| Frequency | native | hertz (`Hz`) | [Frequency](frequency.md) |
| Speed | constructed | metre per second (`m/s`) | [Speed](speed.md) |
| Acceleration | constructed | metre per second² (`m/s²`) | [Acceleration](acceleration.md) |
| Volumetric Flow | constructed | cubic metre per second (`m³/s`) | [Volumetric Flow](volume-flow.md) |

## How the quantities relate

Speed is a distance per time, acceleration a speed per time, and frequency the reciprocal of time. KUnit
returns the correct **typed** quantity for each combination — you never assemble a raw mixed unit by hand:

| Expression | Result | Formula |
|---|---|---|
| `distance / time` | Speed | `v = s / t` |
| `speed * time` | Distance | `s = v · t` |
| `speed / time` | Acceleration | `a = Δv / t` |
| `acceleration * time` | Speed | `v = a · t` |
| `distance * frequency` | Speed | `v = s · f` |

## Worked example — average speed of a trip

A car covers **120 km** in **1.5 h**. Its average speed is `v = s / t`, and multiplying that speed by a
duration gives the distance travelled again:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.*
import org.pcsoft.framework.kunit.kinematic.time.*
import org.pcsoft.framework.kunit.kinematic.speed.*

val v = (120 of kilo.meters) / (1.5 of hours)   // KSpeedUnitInstance
v into (kilo.meters / hours)                     // 80.0  (km/h)
v.value                                          // ≈ 22.22 (m/s)

val distance = v * (3 of hours)                  // KLengthUnitInstance
distance into kilo.meters                        // 240.0 (km in 3 h)
```

## Worked example — acceleration of a sprinter

A sprinter reaches **10 m/s** from standstill in **2 s**. The acceleration is `a = Δv / t`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.speed.*
import org.pcsoft.framework.kunit.kinematic.acceleration.*

val a = ((10 of meters) / (1 of seconds)) / (2 of seconds) // KAccelerationUnitInstance, 5 m/s²
val reached = a * (2 of seconds)                            // KSpeedUnitInstance, 10 m/s
reached.value                                               // 10.0
a into standardGravities                                    // ≈ 0.51 (fraction of g)
```

## Printing a value (`toString`)

`toString()` renders a value in its group's **base unit** (value + symbol); for any other unit, read it
with `into` inside a string template and append the symbol yourself:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.speed.*

val v = (10 of meters) / (2 of seconds)   // KSpeedUnitInstance
v.toString()                              // "5.0 m/s" (base unit)
"${v into (kilo.meters / hours)} km/h"    // "18.0 km/h"
```

## Notation

The table shows the field's core relations mathematically versus in Kotlin with KUnit. Exponents use
Unicode superscripts (`²`, `⁻¹`), `·` denotes multiplication and `/` a fraction.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `v = s / t` | `(120 of kilo.meters) / (1.5 of hours)` | speed from distance ÷ time |
| `s = v · t` | `v * (3 of hours)` | distance from speed × time |
| `a = Δv / t` | `((10 of meters) / (1 of seconds)) / (2 of seconds)` | acceleration from speed ÷ time |
| `v = a · t` | `a * (2 of seconds)` | speed from acceleration × time |
| `f = 1 / T` | `1 / (2 of hertz)` | period ↔ frequency (reciprocal of time) |
| `q̇ = V / t` | `(600 of liters) / (2 of minutes)` | volumetric flow from volume ÷ time |
| `V = q̇ · t` | `q * (15 of minutes)` | volume from flow rate × time |

## Where to go next

* [Distance](distance.md) — length, area and volume under one group.
* [Time](time.md) — durations backed by `Duration`.
* [Speed](speed.md) and [Acceleration](acceleration.md) — the constructed motion rates.
* [Frequency](frequency.md) — the inverse of time, and its cross-operators.
* [Volumetric Flow](volume-flow.md) — volume per time, `m³/s`, `l/min` and `gpm`.
