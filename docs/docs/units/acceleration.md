# Acceleration

Package: `org.pcsoft.framework.kunit.acceleration`
Base unit: **meter per second squared** (`KAccelerationUnit.BASE == KAccelerationUnit.METERS_PER_SECOND_SQUARED`)

Acceleration is a **constructed** unit: the composition `length · time⁻²` (`m/s²`). `KAccelerationUnitInstance`
wraps a `KMixedUnitInstance` of exactly two terms — one `KDistanceUnit.BASE` (meter) at exponent `+1` and one
`KTimeUnit.BASE` (second) at exponent `-2`. The value is always stored normalized to m/s². Because the base
unit coincides with the component base units (meter, second), there is no extra scale factor.

## Building an acceleration

An acceleration is normally built from `speed / time`, or with a named token. There is deliberately **no**
`metersPerSecondSquared` token (that is exactly `meters / (seconds pow 2)`). Only genuinely named units survive
as value-1 tokens (used with `of`/`into`):

| Acceleration | Symbol | Token | 1 unit in m/s² |
|---|---|---:|---:|
| Gal (Galileo) | `Gal` | `gals` | 0.01 (1 cm/s²) |
| Standard gravity | `g₀` | `standardGravities` | 9.80665 |

Both tokens support the full SI prefix table (e.g. `milli.gals` = 1 mGal, the everyday gravimetry unit).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.acceleration.*

val a = 5 of gals               // KAccelerationUnitInstance
a.value                         // 0.05 (normalized to m/s²)
a into standardGravities        // ≈ 0.0051
(1 of milli.gals).value         // 0.00001 (1 mGal)
```

## Computing with the core units (speed & time)

| Expression | Result type | Meaning |
|---|---|---|
| `speed / time` | `KAccelerationUnitInstance` | acceleration = Δspeed / duration |
| `acceleration * time` | `KSpeedUnitInstance` | speed = acceleration × duration |
| `time * acceleration` | `KSpeedUnitInstance` | speed (commutative) |
| `speed / acceleration` | `KTimeUnitInstance` | duration = speed / acceleration |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*
import org.pcsoft.framework.kunit.acceleration.*

val a = ((100 of meters) / (10 of seconds)) / (5 of seconds) // KAccelerationUnitInstance, 2 m/s²
val v = a * (3 of seconds)      // KSpeedUnitInstance, 6 m/s
val t = ((100 of meters) / (10 of seconds)) / a             // KTimeUnitInstance
t into seconds                  // 5.0
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.acceleration.*

// + / - : same group, automatic conversion between different acceleration expressions
val s = (10 of gals) + (4 of gals)   // 0.14 m/s²
(10 of gals) > (4 of gals)           // true
// * / / between two accelerations escape to a KMixedUnitInstance
(10 of gals) * (2 of gals)           // KMixedUnitInstance
```

## toString formatting

Only the base-unit `toString()` exists; format a specific unit via `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.acceleration.*

(1 of gals).toString()               // "0.01 m/s²" (base unit)
"${(1 of standardGravities) into gals} Gal" // "980.665 Gal"
```
