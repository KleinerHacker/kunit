# Angular Acceleration

Package: `org.pcsoft.framework.kunit.mechanic.angularacceleration`
Base unit: **radian per second squared**
(`KAngularAccelerationUnit.BASE == KAngularAccelerationUnit.RADIANS_PER_SECOND_SQUARED`)

Type: **constructed unit**

Angular acceleration `α` is the rotational counterpart of [acceleration](../kinematics/acceleration.md):
the change of [angular velocity](angular-velocity.md) per unit of time. It is a **constructed** unit — the composition
`angle · time⁻²` (`rad/s²`).

`KAngularAccelerationUnitInstance` wraps a `KMixedUnitInstance` of exactly two terms in the canonical normal form:
`KAngleUnit.BASE` (radian) at `+1` and `KTimeUnit.BASE` (second) at `-2`. The value is always normalized to rad/s².

## Named units

| Unit                             | Symbol    |                           Token | 1 unit in rad/s² |
|----------------------------------|-----------|--------------------------------:|-----------------:|
| Radian per second squared        | `rad/s^2` |       `radiansPerSecondSquared` |              1.0 |
| Degree per second squared        | `°/s^2`   |       `degreesPerSecondSquared` |            π/180 |
| Revolution per second squared    | `rps^2`   |   `revolutionsPerSecondSquared` |               2π |
| Revolution per minute per second | `rpm/s`   | `revolutionsPerMinutePerSecond` |            2π/60 |

Prefixes are applied to the components (`kilo.radians / (seconds pow 2)`), so this group has no prefix builders of its
own.

## Decompositions

Angular acceleration has two equivalent decompositions; both reduce onto the same canonical value.

| Form              | Kotlin                                                             | Result type                        |
|-------------------|--------------------------------------------------------------------|------------------------------------|
| typed operator    | `angularvelocity / time`                                           | `KAngularAccelerationUnitInstance` |
| native expression | `(angle.toUnit() / (time.toUnit() pow 2)).toAngularAcceleration()` | `KAngularAccelerationUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val typed = (6 of radians / seconds) / (3 of seconds)
val native = ((2 of radians).toUnit() / ((1 of seconds).toUnit() pow 2)).toAngularAcceleration()

typed == native                        // true - both are 2 rad/s²
typed into radiansPerSecondSquared     // 2.0
```

## Computing with the core units

| Expression                              | Result type                        | Meaning                                     |
|-----------------------------------------|------------------------------------|---------------------------------------------|
| `angularvelocity / time`                | `KAngularAccelerationUnitInstance` | `α = ω / t`                                 |
| `angularacceleration * time`            | `KAngularVelocityUnitInstance`     | gained speed `ω = α · t`                    |
| `time * angularacceleration`            | `KAngularVelocityUnitInstance`     | same, commutative                           |
| `angularvelocity / angularacceleration` | `KTimeUnitInstance`                | run-up time `t = ω / α`                     |
| `inertia * angularacceleration`         | `KEnergyUnitInstance`              | torque `M = J · α`, see [torque](torque.md) |

## Real-world example: motor run-up

A servo motor reaches 3000 rpm in 0.4 s. What is its angular acceleration, and how far has it turned when it accelerates
for 0.2 s from standstill?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val alpha = (3000 of revolutionsPerMinute) / (0.4 of seconds)
alpha into radiansPerSecondSquared      // ≈ 785.4
alpha into revolutionsPerMinutePerSecond // 7500.0

val afterHalf = alpha * (0.2 of seconds) // KAngularVelocityUnitInstance
afterHalf into revolutionsPerMinute      // 1500.0
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*

val sum = (10 of radiansPerSecondSquared) + (4 of radiansPerSecondSquared) // 14 rad/s²
(1 of revolutionsPerSecondSquared) > (300 of degreesPerSecondSquared)      // true
(60 of revolutionsPerMinutePerSecond) == (1 of revolutionsPerSecondSquared) // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*

(2 of radiansPerSecondSquared).toString()                          // "2.0 rad/s^2"
"${(1 of revolutionsPerSecondSquared) into radiansPerSecondSquared} rad/s^2" // "6.283... rad/s^2"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin                                                                  | Meaning                                       |
|-------------|-------------------------------------------------------------------------|-----------------------------------------------|
| `rad/s²`    | `radiansPerSecondSquared`                                               | angular acceleration, base unit (named token) |
| `rad·s⁻²`   | `radians * (seconds pow -2)`                                            | same quantity as a pure product               |
| `rad/s²`    | `(radians.toUnit() / (seconds.toUnit() pow 2)).toAngularAcceleration()` | native decomposition                          |
| `α = ω / t` | `angularvelocity / time`                                                | typed decomposition                           |
| `ω = α · t` | `angularacceleration * time`                                            | solved for the angular velocity               |
| `rpm/s`     | `revolutionsPerMinutePerSecond`                                         | machine run-up rate                           |
