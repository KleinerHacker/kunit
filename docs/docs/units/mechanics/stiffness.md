# Stiffness (Spring Rate)

Package: `org.pcsoft.framework.kunit.mechanic.lineforce`
Base unit: **newton per meter** (`KLineForceUnit.BASE == KLineForceUnit.NEWTONS_PER_METER`)

Type: **constructed unit**

The stiffness (spring rate) `k = F / s` is the force needed per unit of deflection. Its dimension is
`mass · time⁻²` (`N/m`) — exactly the dimension of the [surface tension](surface-tension.md). KUnit models one neutral
group, `lineforce`, for both readings; the stiffness is one of them. This page documents that reading.

!!! note "One group, two readings"
`KLineForceUnitInstance` is the shared type, so a stiffness and a surface tension are the same unit as far as KUnit is
concerned. The group carries the neutral name `lineforce` so that neither reading claims the other's name. Distinguish
them by naming your values.

## Named units

| Unit                  | Symbol   |                  Token | 1 unit in N/m |
|-----------------------|----------|-----------------------:|--------------:|
| Newton per meter      | `N/m`    |      `newtonsPerMeter` |           1.0 |
| Newton per millimeter | `N/mm`   | `newtonsPerMillimeter` |        1000.0 |
| Kilopond per meter    | `kp/m`   |    `kilopondsPerMeter` |       9.80665 |
| Pound-force per inch  | `lbf/in` |   `poundsForcePerInch` |     ≈ 175.127 |
| Dyne per centimeter   | `dyn/cm` |   `dynesPerCentimeter` |          1e-3 |

Spring data sheets quote N/mm; the kilonewton per meter is the prefixed form `kilo.newtonsPerMeter` and is numerically
the same as N/mm.

## Computing with the core units

| Expression                                 | Result type              | Meaning                                           |
|--------------------------------------------|--------------------------|---------------------------------------------------|
| `force / length`                           | `KLineForceUnitInstance` | `k = F / s`                                       |
| `lineforce * length`, `length * lineforce` | `KForceUnitInstance`     | spring force `F = k · s`                          |
| `force / lineforce`                        | `KLengthUnitInstance`    | deflection `s = F / k`                            |
| `energy / area`                            | `KLineForceUnitInstance` | the [surface tension](surface-tension.md) reading |

The native form converts with `toLineForce()`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*
import org.pcsoft.framework.kunit.mechanic.mass.grams

val typed = (1 of newtons) / (1 of meters)
val native = ((1000 of grams).toUnit() / ((1 of seconds).toUnit() pow 2)).toLineForce()

typed == native            // true - both are 1 N/m
typed into newtonsPerMeter // 1.0
```

## Real-world example: coil spring in a suspension

A coil spring is rated 40 N/mm. How far does it compress under a 2000 N wheel load, and which force does a 15 mm
deflection produce?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val k = 40 of newtonsPerMillimeter
k into newtonsPerMeter                 // 40000.0

val travel = (2000 of newtons) / k     // KLengthUnitInstance
travel into milli.meters               // 50.0

val force = k * (15 of milli.meters)   // KForceUnitInstance
force into newtons                     // 600.0
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.lineforce.*

// springs in parallel simply add up
val parallel = (40 of newtonsPerMillimeter) + (20 of newtonsPerMillimeter) // 60 N/mm
(40 of newtonsPerMillimeter) > (30 of kilo.newtonsPerMeter)                // true
(1 of newtonsPerMillimeter) == (1 of kilo.newtonsPerMeter)                 // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineforce.*

(40 of newtonsPerMillimeter).toString()                          // "40000.0 N/m" (base unit)
"${(40 of newtonsPerMillimeter) into newtonsPerMillimeter} N/mm" // "40.0 N/mm"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin                          | Meaning                          |
|-------------|---------------------------------|----------------------------------|
| `N/m`       | `newtonsPerMeter`               | stiffness, base unit             |
| `kg·s⁻²`    | `kilo.grams * (seconds pow -2)` | same quantity in base dimensions |
| `N/mm`      | `newtonsPerMillimeter`          | spring-data-sheet reading        |
| `k = F / s` | `force / length`                | typed decomposition              |
| `F = k · s` | `lineforce * length`            | spring force                     |
| `s = F / k` | `force / lineforce`             | deflection                       |
