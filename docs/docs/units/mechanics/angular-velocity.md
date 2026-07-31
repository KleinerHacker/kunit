# Angular Velocity

Package: `org.pcsoft.framework.kunit.mechanic.angularvelocity`
Base unit: **radian per second** (`KAngularVelocityUnit.BASE == KAngularVelocityUnit.RADIANS_PER_SECOND`)

Type: **constructed unit**

Angular velocity `ω` is the rotational counterpart of [speed](../kinematics/speed.md): the angle swept per unit of time.
It is a **constructed** unit — the composition `angle · time⁻¹` (`rad/s`).

`KAngularVelocityUnitInstance` wraps a `KMixedUnitInstance` of exactly two terms in the canonical normal form:
`KAngleUnit.BASE` (radian) at `+1` and `KTimeUnit.BASE` (second) at `-1`. The value is always normalized to rad/s.

## Building an angular velocity

Build it from `angle / time`, or with one of the conventional revolution-rate tokens. The plainly composed spellings
have deliberately **no** own tokens: `rad/s` is `radians / seconds` and `°/s` is
`degrees / seconds`. Prefixes are applied to the components (`kilo.radians / seconds`), so this group has no prefix
builders of its own.

| Unit                  | Symbol  |                  Token | 1 unit in rad/s |
|-----------------------|---------|-----------------------:|----------------:|
| Radian per second     | `rad/s` |    `radians / seconds` |             1.0 |
| Degree per second     | `°/s`   |    `degrees / seconds` |           π/180 |
| Revolution per minute | `rpm`   | `revolutionsPerMinute` | 2π/60 ≈ 0.10472 |
| Revolution per second | `rps`   | `revolutionsPerSecond` |     2π ≈ 6.2832 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val w = (1 of turns) / (1 of seconds)
w into revolutionsPerMinute  // 60.0
w into (radians / seconds)   // ≈ 6.2832
```

## Computing with the core units (angle & time)

| Expression                  | Result type                        | Meaning                                         |
|-----------------------------|------------------------------------|-------------------------------------------------|
| `angle / time`              | `KAngularVelocityUnitInstance`     | `ω = φ / t`                                     |
| `angularvelocity * time`    | `KAngleUnitInstance`               | swept angle `φ = ω · t`                         |
| `time * angularvelocity`    | `KAngleUnitInstance`               | same, commutative                               |
| `angle / angularvelocity`   | `KTimeUnitInstance`                | required time `t = φ / ω`                       |
| `angularvelocity / time`    | `KAngularAccelerationUnitInstance` | [angular acceleration](angular-acceleration.md) |
| `inertia * angularvelocity` | `KAngularMomentumUnitInstance`     | [angular momentum](angular-momentum.md)         |
| `torque * angularvelocity`  | `KPowerUnitInstance`               | rotational power, see [torque](torque.md)       |

The native form is available too: any `angle / time` expression built through the generic engine converts with
`toAngularVelocity()`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val typed = (2 of radians) / (4 of seconds)
val native = ((2 of radians).toUnit() / (4 of seconds).toUnit()).toAngularVelocity()

typed == native // true - both are 0.5 rad/s
```

## Real-world example: spindle speed

A milling spindle runs at 12 000 rpm. How far does a point on the tool circumference travel per second in terms of
angle, and how long does one revolution take?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val spindle = 12_000 of revolutionsPerMinute
val perSecond = spindle * (1 of seconds)   // KAngleUnitInstance
perSecond into turns                        // 200.0

val perTurn = (1 of turns) / spindle        // KTimeUnitInstance
perTurn into seconds                        // 0.005
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val sum = (1000 of revolutionsPerMinute) + (500 of revolutionsPerMinute) // 1500 rpm
(1 of revolutionsPerSecond) > (59 of revolutionsPerMinute)               // true
(60 of revolutionsPerMinute) == (1 of revolutionsPerSecond)              // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

(1 of revolutionsPerSecond).toString()                        // "6.283185307179586 rad/s"
"${(1 of revolutionsPerSecond) into revolutionsPerMinute} rpm" // "60.0 rpm"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin                       | Meaning                                     |
|-------------|------------------------------|---------------------------------------------|
| `rad/s`     | `radians / seconds`          | angular velocity, base unit (fraction form) |
| `rad·s⁻¹`   | `radians * (seconds pow -1)` | same quantity as a pure product             |
| `rpm`       | `revolutionsPerMinute`       | revolution per minute (named token)         |
| `ω = φ / t` | `angle / time`               | typed decomposition                         |
| `φ = ω · t` | `angularvelocity * time`     | solved for the angle                        |
| `t = φ / ω` | `angle / angularvelocity`    | solved for the time                         |
