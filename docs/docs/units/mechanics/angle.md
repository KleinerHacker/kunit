# Angle

Package: `org.pcsoft.framework.kunit.mechanic.angle`
Base unit: **radian** (`KAngleUnit.BASE == KAngleUnit.RADIAN`)

Type: **native unit**

The plane angle is a **native** unit of KUnit: a directly measurable base quantity with its own unit vocabulary, not a
composition. `KAngleUnitInstance` wraps a `KMixedUnitInstance` of a single
`KAngleUnit.BASE` term at exponent 1, always normalized to radians.

The angle is the foundation of the whole rotational part of mechanics:
[angular velocity](angular-velocity.md), [angular acceleration](angular-acceleration.md),
[angular momentum](angular-momentum.md) and the [solid angle](solid-angle.md) are all built on it.

## Named units

| Unit              | Symbol |        Token |     1 unit in rad |
|-------------------|--------|-------------:|------------------:|
| Radian            | `rad`  |    `radians` |               1.0 |
| Degree            | `°`    |    `degrees` | π/180 ≈ 0.0174533 |
| Arcminute         | `'`    | `arcminutes` |           π/10800 |
| Arcsecond         | `"`    | `arcseconds` |          π/648000 |
| Gradian (gon)     | `gon`  |   `gradians` |             π/200 |
| Turn (revolution) | `tr`   |      `turns` |       2π ≈ 6.2832 |

All units accept the full SI prefix range (`milli.radians`, `micro.arcseconds` for astrometry, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.angle.*

val a = 90 of degrees
a into radians      // ≈ 1.5708
a into turns        // 0.25
a into gradians     // 100.0
1 of milli.radians  // 0.001 rad
```

## Computing with angles

| Expression                       | Result type                    | Meaning                      |
|----------------------------------|--------------------------------|------------------------------|
| `angle + angle`, `angle - angle` | `KAngleUnitInstance`           | same-type arithmetic         |
| `angle * angle`                  | `KSolidAngleUnitInstance`      | solid angle (`rad² = sr`)    |
| `angle / time`                   | `KAngularVelocityUnitInstance` | angular velocity `ω = φ / t` |
| `angle / angularvelocity`        | `KTimeUnitInstance`            | the time a rotation takes    |
| `angle / angle`                  | `KMixedUnitInstance`           | dimensionless ratio          |

The trigonometric functions are available directly on the value, because they consume the radian reading: `angle.sin()`,
`angle.cos()`, `angle.tan()`.

## Real-world example: gearbox output angle

A motor shaft turns by 3 full revolutions. A gear pair with ratio 5:1 reduces this. What is the output angle in degrees,
and how long does the motion take at 600 rpm?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val input = 3 of turns
val output = input / 5                 // KAngleUnitInstance, 0.6 turns
output into degrees                    // 216.0

val t = input / (600 of revolutionsPerMinute) // KTimeUnitInstance
t into seconds                                // 0.3
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.*

val sum = (90 of degrees) + (30 of degrees) // 120°
(1 of turns) > (359 of degrees)             // true
(180 of degrees) == (0.5 of turns)          // true (value-based equality)
(90 of degrees).sin()                       // 1.0
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.*

(2 of radians).toString()                    // "2.0 rad" (base unit)
"${(1 of turns) into degrees} °"             // "360.0 °"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics     | Kotlin                      | Meaning                           |
|-----------------|-----------------------------|-----------------------------------|
| `rad`           | `radians`                   | plane angle, base unit            |
| `°`             | `degrees`                   | degree                            |
| `mrad`          | `milli.radians`             | prefixed angle (milliradian)      |
| `1 tr = 2π rad` | `(1 of turns) into radians` | full revolution in radians        |
| `ω = φ / t`     | `angle / time`              | angular velocity from an angle    |
| `Ω = φ²`        | `angle * angle`             | solid angle from two plane angles |
