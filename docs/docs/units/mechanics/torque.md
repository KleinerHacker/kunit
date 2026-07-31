# Torque

Package: `org.pcsoft.framework.kunit.common.energy`
Base unit: **joule** (`KEnergyUnit.BASE == KEnergyUnit.JOULE`), read as **newton meter** (`N·m`)

Type: **constructed unit**

Torque `M = F · r` is the rotational effect of a force acting at a lever arm. Dimensionally it *is* an
[energy](energy.md): `1 N·m = 1 J`. KUnit therefore does **not** introduce a second unit group for it — the torque is a
**reading** of the energy group. This page documents that reading; the group itself is described on
the [energy (mechanics)](energy.md) page.

!!! note "Same dimension, different physics"
Torque and work are physically different (torque is an axial vector, work a scalar), but they share the dimension
`kg·m²·s⁻²` exactly. Because KUnit models *units*, not vector character, both live in one group. Keep them apart by
naming: `val torque = (100 of newtons) * (2 of meters)` reads as N·m,
`val work = force * distance` along the path reads as J.

## Building a torque

| Expression                         | Result type                        | Meaning                          |
|------------------------------------|------------------------------------|----------------------------------|
| `force * length`, `length * force` | `KEnergyUnitInstance`              | `M = F · r` (lever arm)          |
| `inertia * angularacceleration`    | `KEnergyUnitInstance`              | `M = J · α` (rotational Newton)  |
| `power / angularvelocity`          | `KEnergyUnitInstance`              | `M = P / ω` (drivetrain formula) |
| `torque * angularvelocity`         | `KPowerUnitInstance`               | `P = M · ω`                      |
| `torque / inertia`                 | `KAngularAccelerationUnitInstance` | `α = M / J`                      |
| `torque / angularacceleration`     | `KInertiaUnitInstance`             | `J = M / α`                      |
| `power / torque`                   | `KAngularVelocityUnitInstance`     | `ω = P / M`                      |

All three building forms funnel into the energy group's single factory, so they are value-equal:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.energy.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.angularacceleration.radiansPerSecondSquared
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerSecond
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared

val viaLever = (100 of newtons) * (2 of meters)                          // 200 N·m
val viaPower = (200.0 * 2.0 * Math.PI of watts) / (1 of revolutionsPerSecond)
val viaInertia = (2 of kilogramMetersSquared) * (100 of radiansPerSecondSquared) // 200 N·m

viaLever into joules   // 200.0
viaPower into joules   // 200.0
viaInertia into joules // 200.0
```

## Named units

The torque uses the energy group's tokens; `newtons * meters` is the idiomatic N·m spelling, and prefixed readings come
from the energy tokens (`kilo.joules` = kN·m).

| Reading                | Symbol | Kotlin                           |
|------------------------|--------|----------------------------------|
| Newton meter           | `N*m`  | `(1 of newtons) * (1 of meters)` |
| Joule (same dimension) | `J`    | `joules`                         |
| Kilonewton meter       | `kN*m` | `kilo.joules`                    |

## Real-world example: engine torque and power

An engine delivers 62.83 kW at 3000 rpm. What torque is that? And what power results if the same torque is held at 6000
rpm?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerMinute

val torque = (62.83 of kilo.watts) / (3000 of revolutionsPerMinute)
torque into joules                     // ≈ 200.0 (N·m)

val doubled = torque * (6000 of revolutionsPerMinute)
doubled into kilo.watts                // ≈ 125.7
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.energy.*

val sum = (200 of joules) + (50 of joules) // 250 N·m
(200 of joules) > (150 of joules)          // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*

(200 of joules).toString()                 // "200.0 J" (group base unit)
"${(200 of joules) into kilo.joules} kN*m" // "0.2 kN*m"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin                                           | Meaning                          |
|-------------|--------------------------------------------------|----------------------------------|
| `N·m`       | `(1 of newtons) * (1 of meters)`                 | torque, lever-arm form           |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)` | same quantity in base dimensions |
| `M = F · r` | `force * length`                                 | decomposition A                  |
| `M = J · α` | `inertia * angularacceleration`                  | decomposition B                  |
| `M = P / ω` | `power / angularvelocity`                        | decomposition C (drivetrain)     |
| `P = M · ω` | `torque * angularvelocity`                       | rotational power                 |
| `kN·m`      | `kilo.joules`                                    | prefixed torque reading          |
