# Angular Momentum

Package: `org.pcsoft.framework.kunit.mechanic.angularmomentum`
Base unit: **kilogram meter squared per second**
(`KAngularMomentumUnit.BASE == KAngularMomentumUnit.KILOGRAM_METERS_SQUARED_PER_SECOND`)

Type: **constructed unit**

Angular momentum `L` is the rotational counterpart of the [momentum](momentum.md) and the conserved quantity of rotating
systems. It is a **constructed** unit — the composition `mass · length² · time⁻¹`
(`kg·m²/s`).

`KAngularMomentumUnitInstance` wraps a `KMixedUnitInstance` of exactly three terms in the canonical normal form:
`KMassUnit.BASE` (gram) at `+1`, `KDistanceUnit.BASE` (meter) at `+2` and `KTimeUnit.BASE` (second) at
`-1`. The radian does **not** appear in the normal form — it is a dimensionless ratio.

!!! note "Action is the same quantity"
The **action** (energy × time) shares this dimension exactly, which is why the joule second (`jouleSeconds`, the unit of
Planck's constant) is a token of *this* group: `1 J·s = 1 kg·m²/s`.

## Named units

| Unit                               | Symbol     |                             Token | 1 unit in kg·m²/s |
|------------------------------------|------------|----------------------------------:|------------------:|
| Kilogram meter squared per second  | `kg*m^2/s` |  `kilogramMetersSquaredPerSecond` |               1.0 |
| Newton meter second                | `N*m*s`    |              `newtonMeterSeconds` |               1.0 |
| Joule second                       | `J*s`      |                    `jouleSeconds` |               1.0 |
| Gram centimeter squared per second | `g*cm^2/s` | `gramCentimetersSquaredPerSecond` |              1e-7 |

All units accept the full SI prefix range (`femto.jouleSeconds`, `milli.jouleSeconds`).

## Decompositions

Angular momentum has two equivalent decompositions; both funnel into the same normalizing factory.

| Form                       | Kotlin                                                                          | Result type                    |
|----------------------------|---------------------------------------------------------------------------------|--------------------------------|
| inertia × angular velocity | `inertia * angularvelocity`                                                     | `KAngularMomentumUnitInstance` |
| momentum × lever arm       | `momentum * length`                                                             | `KAngularMomentumUnitInstance` |
| native expression          | `(mass.toUnit() * (length.toUnit() pow 2) / time.toUnit()).toAngularMomentum()` | `KAngularMomentumUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.radians
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.div
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.momentum.kilogramMetersPerSecond

val omega = (3 of radians) / (1 of seconds)
val viaInertia = (2 of kilogramMetersSquared) * omega
val viaMomentum = (3 of kilogramMetersPerSecond) * (2 of meters)
val viaNative =
    ((2000 of grams).toUnit() * ((3 of meters).toUnit() pow 2) / (1 of seconds).toUnit()).toAngularMomentum()

viaInertia == viaMomentum                       // true - both are 6 kg·m²/s
viaInertia into kilogramMetersSquaredPerSecond  // 6.0
viaNative into kilogramMetersSquaredPerSecond   // 18.0
```

## Computing with the core units

| Expression                               | Result type                    | Meaning           |
|------------------------------------------|--------------------------------|-------------------|
| `inertia * angularvelocity`              | `KAngularMomentumUnitInstance` | `L = J · ω`       |
| `angularvelocity * inertia`              | `KAngularMomentumUnitInstance` | same, commutative |
| `momentum * length`, `length * momentum` | `KAngularMomentumUnitInstance` | `L = p · r`       |
| `angularmomentum / inertia`              | `KAngularVelocityUnitInstance` | `ω = L / J`       |
| `angularmomentum / angularvelocity`      | `KInertiaUnitInstance`         | `J = L / ω`       |
| `angularmomentum / length`               | `KMomentumUnitInstance`        | `p = L / r`       |
| `angularmomentum / momentum`             | `KLengthUnitInstance`          | `r = L / p`       |

## Real-world example: figure skater pulling in the arms

A skater rotates at 2 rev/s with a moment of inertia of 4 kg·m². Pulling the arms in reduces it to 1.6 kg·m². Because
the angular momentum is conserved, the new rate follows from `ω = L / J`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerSecond
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared

val l = (4 of kilogramMetersSquared) * (2 of revolutionsPerSecond)
l into kilogramMetersSquaredPerSecond // ≈ 50.27

val faster = l / (1.6 of kilogramMetersSquared) // KAngularVelocityUnitInstance
faster into revolutionsPerSecond                 // 5.0
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*

val sum = (10 of jouleSeconds) + (4 of jouleSeconds) // 14 J·s
(10 of jouleSeconds) > (4 of newtonMeterSeconds)     // true
(1 of jouleSeconds) == (1 of newtonMeterSeconds)     // true (same dimension)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*

(6 of kilogramMetersSquaredPerSecond).toString()             // "6.0 kg*m^2/s" (base unit)
"${(6 of kilogramMetersSquaredPerSecond) into jouleSeconds} J*s" // "6.0 J*s"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin                                           | Meaning                                   |
|-------------|--------------------------------------------------|-------------------------------------------|
| `kg·m²/s`   | `kilogramMetersSquaredPerSecond`                 | angular momentum, base unit (named token) |
| `kg·m²·s⁻¹` | `kilo.grams * (meters pow 2) * (seconds pow -1)` | same quantity as a pure product           |
| `J·s`       | `jouleSeconds`                                   | action spelling of the same dimension     |
| `L = J · ω` | `inertia * angularvelocity`                      | decomposition A                           |
| `L = p · r` | `momentum * length`                              | decomposition B                           |
| `ω = L / J` | `angularmomentum / inertia`                      | solved for the angular velocity           |
| `r = L / p` | `angularmomentum / momentum`                     | solved for the lever arm                  |
