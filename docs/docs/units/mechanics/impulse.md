# Impulse

Package: `org.pcsoft.framework.kunit.mechanic.momentum`
Base unit: **kilogram meter per second** (`KMomentumUnit.BASE`), read as the **newton second**
(`KMomentumUnit.NEWTON_SECOND`)

Type: **constructed unit**

The impulse `J = F · t` is the momentum a force imparts over the time it acts. Dimensionally it *is* a
[momentum](momentum.md): `1 N·s = 1 kg·m/s`. KUnit therefore does **not** introduce a second unit group for it — the
impulse is a **reading** of the momentum group, expressed with the `newtonSeconds` token. This page documents that
reading; the group itself is described on the [momentum](momentum.md) page.

!!! note "Same group, two readings"
`(1 of newtonSeconds) == (1 of kilogramMetersPerSecond)` is `true`. Choosing a token only changes how you read a value,
never what it is. Use `newtonSeconds` when you think in "force × time", and
`kilogramMetersPerSecond` when you think in "mass × velocity".

## Named units

| Unit                       | Symbol    |                      Token | 1 unit in kg·m/s |
|----------------------------|-----------|---------------------------:|-----------------:|
| Newton second              | `N*s`     |            `newtonSeconds` |              1.0 |
| Kilogram meter per second  | `kg*m/s`  |  `kilogramMetersPerSecond` |              1.0 |
| Gram centimeter per second | `g*cm/s`  | `gramCentimetersPerSecond` |             1e-5 |
| Pound-foot per second      | `lb*ft/s` |       `poundFeetPerSecond` |       ≈ 0.138255 |

Prefixed forms exist for every token (`kilo.newtonSeconds` = kN·s, `milli.newtonSeconds` = mN·s).

## Computing an impulse

| Expression        | Result type             | Meaning                      |
|-------------------|-------------------------|------------------------------|
| `force * time`    | `KMomentumUnitInstance` | `J = F · t`                  |
| `time * force`    | `KMomentumUnitInstance` | same, commutative            |
| `impulse / time`  | `KForceUnitInstance`    | average force `F = J / t`    |
| `impulse / force` | `KTimeUnitInstance`     | acting time `t = J / F`      |
| `impulse / mass`  | `KSpeedUnitInstance`    | velocity change `Δv = J / m` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.momentum.*

val j = (10 of newtons) * (3 of seconds)
j into newtonSeconds             // 30.0
j into kilogramMetersPerSecond   // 30.0 (identical dimension)
```

## Real-world example: rocket stage burn

A model rocket motor delivers a mean thrust of 12 N for 1.6 s. What total impulse does it produce, and which velocity
change does that give a 0.8 kg rocket?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.momentum.*

val impulse = (12 of newtons) * (1.6 of seconds)
impulse into newtonSeconds              // 19.2

val deltaV = impulse / (0.8 of kilo.grams) // KSpeedUnitInstance
deltaV into (meters / seconds)             // 24.0
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

val total = (19.2 of newtonSeconds) + (5 of newtonSeconds) // 24.2 N·s
(19.2 of newtonSeconds) > (10 of newtonSeconds)            // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

(19.2 of newtonSeconds).toString()                  // "19.2 kg*m/s" (group base unit)
"${(19.2 of newtonSeconds) into newtonSeconds} N*s" // "19.2 N*s"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics  | Kotlin                                   | Meaning                                     |
|--------------|------------------------------------------|---------------------------------------------|
| `N·s`        | `newtonSeconds`                          | impulse (named token of the momentum group) |
| `kg·m·s⁻¹`   | `kilo.grams * meters * (seconds pow -1)` | same quantity in base dimensions            |
| `J = F · t`  | `force * time`                           | typed decomposition                         |
| `F = J / t`  | `impulse / time`                         | solved for the mean force                   |
| `Δv = J / m` | `impulse / mass`                         | velocity change of a mass                   |
| `kN·s`       | `kilo.newtonSeconds`                     | prefixed impulse                            |
