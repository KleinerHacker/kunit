# Surface Tension

Package: `org.pcsoft.framework.kunit.mechanic.lineforce`
Base unit: **newton per meter** (`KLineForceUnit.BASE == KLineForceUnit.NEWTONS_PER_METER`)

Type: **constructed unit**

The surface tension `σ` is the energy needed to create a unit of new surface, equivalently the force acting per unit of
length along a contact line: `1 J/m² = 1 N/m`. Its dimension is `mass · time⁻²`.

This is exactly the dimension of the **force per length**, which the [stiffness](stiffness.md) shares. KUnit therefore
models one neutral group, `lineforce`, for both readings; the surface tension is one of them. This page documents that
reading.

!!! note "One group, two readings"
`KLineForceUnitInstance` is the shared type. Nothing distinguishes a surface tension from a spring rate but the name you
give it — the group is named neutrally so that neither reading claims the other's name.

## Named units

| Unit                  | Symbol   |                  Token | 1 unit in N/m |
|-----------------------|----------|-----------------------:|--------------:|
| Newton per meter      | `N/m`    |      `newtonsPerMeter` |           1.0 |
| Dyne per centimeter   | `dyn/cm` |   `dynesPerCentimeter` |          1e-3 |
| Newton per millimeter | `N/mm`   | `newtonsPerMillimeter` |        1000.0 |
| Pound-force per inch  | `lbf/in` |   `poundsForcePerInch` |     ≈ 175.127 |
| Kilopond per meter    | `kp/m`   |    `kilopondsPerMeter` |       9.80665 |

Surface tensions are usually quoted in mN/m or the numerically identical dyn/cm: water at 25 °C is ≈ 72 mN/m = 72
dyn/cm. The millinewton per meter is the prefixed form `milli.newtonsPerMeter`.

## Decompositions

| Form              | Kotlin                                                  | Result type              |
|-------------------|---------------------------------------------------------|--------------------------|
| energy / area     | `energy / area`                                         | `KLineForceUnitInstance` |
| force / length    | `force / length`                                        | `KLineForceUnitInstance` |
| native expression | `(mass.toUnit() / (time.toUnit() pow 2)).toLineForce()` | `KLineForceUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val viaEnergy = (2 of joules) / ((1 of meters) * (1 of meters))
val viaForce = (2 of newtons) / (1 of meters)

viaEnergy == viaForce                  // true - both are 2 N/m
(72 of milli.joules) / ((1 of meters) * (1 of meters)) into dynesPerCentimeter // 72.0
```

## Computing with the core units

| Expression                                 | Result type              | Meaning                    |
|--------------------------------------------|--------------------------|----------------------------|
| `energy / area`                            | `KLineForceUnitInstance` | `σ = W / A`                |
| `lineforce * area`, `area * lineforce`     | `KEnergyUnitInstance`    | surface energy `W = σ · A` |
| `energy / lineforce`                       | `KAreaUnitInstance`      | `A = W / σ`                |
| `force / length`                           | `KLineForceUnitInstance` | `σ = F / l`                |
| `lineforce * length`, `length * lineforce` | `KForceUnitInstance`     | `F = σ · l`                |

## Real-world example: energy to create a soap film

Blowing a soap film of 0.05 m² (two surfaces, σ ≈ 25 mN/m per surface). How much energy does that cost, and which force
does the film exert on a 10 cm wire?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val sigma = 25 of milli.newtonsPerMeter
val area = (0.5 of meters) * (0.1 of meters)   // 0.05 m²

val energy = sigma * area                       // KEnergyUnitInstance
energy into milli.joules                        // 1.25

val force = sigma * (10 of centi.meters)        // KForceUnitInstance
force into milli.newtons                        // 2.5
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.lineforce.*

val sum = (72 of dynesPerCentimeter) + (8 of dynesPerCentimeter) // 80 dyn/cm
(72 of dynesPerCentimeter) > (50 of milli.newtonsPerMeter)       // true
(1 of dynesPerCentimeter) == (1 of milli.newtonsPerMeter)        // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineforce.*

(72 of dynesPerCentimeter).toString()                     // "0.072 N/m" (base unit)
"${(72 of dynesPerCentimeter) into dynesPerCentimeter} dyn/cm" // "72.0 dyn/cm"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin                          | Meaning                              |
|-------------|---------------------------------|--------------------------------------|
| `N/m`       | `newtonsPerMeter`               | surface tension, base unit           |
| `kg·s⁻²`    | `kilo.grams * (seconds pow -2)` | same quantity in base dimensions     |
| `mN/m`      | `milli.newtonsPerMeter`         | the everyday surface-tension reading |
| `dyn/cm`    | `dynesPerCentimeter`            | CGS reading (= 1 mN/m)               |
| `σ = W / A` | `energy / area`                 | decomposition A                      |
| `σ = F / l` | `force / length`                | decomposition B                      |
| `W = σ · A` | `lineforce * area`              | surface energy                       |
