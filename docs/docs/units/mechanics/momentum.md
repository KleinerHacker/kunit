# Momentum

Package: `org.pcsoft.framework.kunit.mechanic.momentum`
Base unit: **kilogram meter per second**
(`KMomentumUnit.BASE == KMomentumUnit.KILOGRAM_METERS_PER_SECOND`)

Type: **constructed unit**

Momentum `p = m · v` is the "quantity of motion" of a body. It is a **constructed** unit — the composition
`mass · length · time⁻¹` (`kg·m/s`).

`KMomentumUnitInstance` wraps a `KMixedUnitInstance` of exactly three terms in the canonical normal form:
`KMassUnit.BASE` (gram) at `+1`, `KDistanceUnit.BASE` (meter) at `+1` and `KTimeUnit.BASE` (second) at
`-1`. Since the mass component of this library is normalized to grams, the stored value is the raw gram-based component
value and readings in kg·m/s divide by a fixed factor.

!!! note "Impulse is the same quantity"
The **impulse** `F · t` has exactly this dimension (`1 N·s = 1 kg·m/s`), so it is *this* group rather than a group of
its own — see the [impulse](impulse.md) page.

## Named units

| Unit                       | Symbol    |                      Token | 1 unit in kg·m/s |
|----------------------------|-----------|---------------------------:|-----------------:|
| Kilogram meter per second  | `kg*m/s`  |  `kilogramMetersPerSecond` |              1.0 |
| Newton second              | `N*s`     |            `newtonSeconds` |              1.0 |
| Gram centimeter per second | `g*cm/s`  | `gramCentimetersPerSecond` |             1e-5 |
| Pound-foot per second      | `lb*ft/s` |       `poundFeetPerSecond` |       ≈ 0.138255 |

All units accept the full SI prefix range (`kilo.newtonSeconds`, `milli.kilogramMetersPerSecond`).

## Decompositions

Momentum has two equivalent decompositions; all of them funnel into the same normalizing factory and therefore produce
the same typed, value-equal result.

| Form                   | Kotlin                                                           | Result type             |
|------------------------|------------------------------------------------------------------|-------------------------|
| mass × speed           | `mass * speed`                                                   | `KMomentumUnitInstance` |
| force × time (impulse) | `force * time`                                                   | `KMomentumUnitInstance` |
| native expression      | `(mass.toUnit() * length.toUnit() / time.toUnit()).toMomentum()` | `KMomentumUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.momentum.*

val speed = (3 of meters) / (1 of seconds)
val viaMassSpeed = (2 of kilo.grams) * speed
val viaForceTime = (6 of newtons) * (1 of seconds)
val viaNative =
    ((2000 of grams).toUnit() * (3 of meters).toUnit() / (1 of seconds).toUnit()).toMomentum()

viaMassSpeed == viaForceTime            // true
viaMassSpeed == viaNative               // true
viaMassSpeed into kilogramMetersPerSecond // 6.0
```

## Computing with the core units

| Expression                     | Result type                    | Meaning                                 |
|--------------------------------|--------------------------------|-----------------------------------------|
| `mass * speed`, `speed * mass` | `KMomentumUnitInstance`        | `p = m · v`                             |
| `force * time`, `time * force` | `KMomentumUnitInstance`        | impulse `p = F · t`                     |
| `momentum / mass`              | `KSpeedUnitInstance`           | `v = p / m`                             |
| `momentum / speed`             | `KMassUnitInstance`            | `m = p / v`                             |
| `momentum / time`              | `KForceUnitInstance`           | average force `F = p / t`               |
| `momentum / force`             | `KTimeUnitInstance`            | acting time `t = p / F`                 |
| `momentum * length`            | `KAngularMomentumUnitInstance` | [angular momentum](angular-momentum.md) |

## Real-world example: braking a car

A car of 1200 kg travels at 20 m/s. What is its momentum, and which constant force stops it in 5 s?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.momentum.*

val v = (20 of meters) / (1 of seconds)
val p = (1200 of kilo.grams) * v
p into kilogramMetersPerSecond      // 24000.0

val brakingForce = p / (5 of seconds)
brakingForce into newtons           // 4800.0
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

val sum = (10 of newtonSeconds) + (4 of newtonSeconds) // 14 N·s
(10 of kilogramMetersPerSecond) > (4 of newtonSeconds) // true
(1 of newtonSeconds) == (1 of kilogramMetersPerSecond) // true (same dimension)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

(6 of kilogramMetersPerSecond).toString()          // "6.0 kg*m/s" (base unit)
"${(6 of kilogramMetersPerSecond) into newtonSeconds} N*s" // "6.0 N*s"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin                                   | Meaning                                |
|-------------|------------------------------------------|----------------------------------------|
| `kg·m/s`    | `kilogramMetersPerSecond`                | momentum, base unit (named token)      |
| `kg·m·s⁻¹`  | `kilo.grams * meters * (seconds pow -1)` | same quantity as a pure product        |
| `N·s`       | `newtonSeconds`                          | impulse spelling of the same dimension |
| `p = m · v` | `mass * speed`                           | decomposition A                        |
| `p = F · t` | `force * time`                           | decomposition B (impulse)              |
| `v = p / m` | `momentum / mass`                        | solved for the speed                   |
| `F = p / t` | `momentum / time`                        | solved for the average force           |
