# Force

Package: `org.pcsoft.framework.kunit.force`
Base unit: **newton** (`KForceUnit.BASE == KForceUnit.NEWTON`)

Force is a **constructed** unit: the composition `mass · length · time⁻²` (`kg·m/s²`).
`KForceUnitInstance` wraps a `KMixedUnitInstance` of three terms — `KMassUnit.BASE` (gram) at `+1`,
`KDistanceUnit.BASE` (meter) at `+1` and `KTimeUnit.BASE` (second) at `-2`. Because the mass component of the
library is normalized to **grams** (not kilograms), the newton is 1000× the raw component base; the stored
value is the raw component value and readings in newtons divide by that fixed factor.

## Building a force

Build a force from `mass * acceleration`, or with a named token. Named units survive as value-1 tokens (used
with `of`/`into`):

| Force | Symbol | Token | 1 unit in N |
|---|---|---:|---:|
| Newton | `N` | `newtons` | 1.0 |
| Dyne | `dyn` | `dynes` | 1.0e-5 |
| Pound-force | `lbf` | `poundsForce` | 4.4482216152605 |
| Pond (gram-force) | `p` | `ponds` | 9.80665e-3 |

The **kilopond / kilogram-force (kgf) is not a dedicated token** — it is `kilo.ponds`, just as the kilonewton
is `kilo.newtons`. Named units support the SI prefixes via `KPrefixBuilder` (`kilo.newtons`, `mega.newtons`,
`kilo.ponds`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.force.*

val f = 10 of newtons
f into newtons               // 10.0
f into poundsForce           // ≈ 2.248
(1 of kilo.ponds) into newtons // 9.80665 (1 kp = 1 kgf)
```

## Computing with the core units (mass & acceleration)

| Expression | Result type | Meaning |
|---|---|---|
| `mass * acceleration` | `KForceUnitInstance` | force = m · a (Newton's second law) |
| `acceleration * mass` | `KForceUnitInstance` | force (commutative) |
| `force / mass` | `KAccelerationUnitInstance` | acceleration = F / m |
| `force / acceleration` | `KMassUnitInstance` | mass = F / a |
| `force / area` | `KPressureUnitInstance` | pressure (see Pressure) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.acceleration.*
import org.pcsoft.framework.kunit.force.*

val f = (2 of kilo.grams) * (3 of standardGravities) // KForceUnitInstance
f into newtons               // ≈ 58.84
val a = (10 of newtons) / (2 of kilo.grams)          // KAccelerationUnitInstance, 5 m/s²
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.force.*

val s = (10 of newtons) + (4 of newtons)  // 14 N
(10 of newtons) > (4 of newtons)          // true
(10 of newtons) * (2 of newtons)          // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.force.*

(10 of newtons).toString()   // "10.0 N" (base unit)
"${(1 of kilo.ponds) into newtons} N" // "9.80665 N"
```
