# Linear Density

Package: `org.pcsoft.framework.kunit.mechanic.lineardensity`
Base unit: **kilogram per meter**
(`KLinearDensityUnit.BASE == KLinearDensityUnit.KILOGRAMS_PER_METER`)

Type: **constructed unit**

Linear density is the mass per unit of length — the one-dimensional sibling of
[area density](areadensity.md) (`kg/m²`) and [density](density.md) (`kg/m³`). It is a **constructed** unit — the
composition `mass · length⁻¹` (`kg/m`).

`KLinearDensityUnitInstance` wraps a `KMixedUnitInstance` of exactly two terms in the canonical normal form:
`KMassUnit.BASE` (gram) at `+1` and `KDistanceUnit.BASE` (meter) at `-1`. Since the mass component of this library is
normalized to grams, the stored value is the raw gram-based component value and readings in kg/m divide by a fixed
factor.

## Named units

| Unit                | Symbol  |                Token | 1 unit in kg/m |
|---------------------|---------|---------------------:|---------------:|
| Kilogram per meter  | `kg/m`  |  `kilogramsPerMeter` |            1.0 |
| Gram per meter      | `g/m`   |      `gramsPerMeter` |           1e-3 |
| Gram per centimeter | `g/cm`  | `gramsPerCentimeter` |            0.1 |
| Tex (textile)       | `tex`   |                `tex` |           1e-6 |
| Denier (textile)    | `den`   |             `denier` |    ≈ 1.1111e-7 |
| Pound per foot      | `lb/ft` |      `poundsPerFoot` |      ≈ 1.48816 |

All units accept the full SI prefix range; the textile decitex is `deci.tex`.

## Computing with the core units

| Expression                                         | Result type                  | Meaning       |
|----------------------------------------------------|------------------------------|---------------|
| `mass / length`                                    | `KLinearDensityUnitInstance` | `ρ_l = m / l` |
| `lineardensity * length`, `length * lineardensity` | `KMassUnitInstance`          | `m = ρ_l · l` |
| `mass / lineardensity`                             | `KLengthUnitInstance`        | `l = m / ρ_l` |

The native form is available too: any gram-per-meter expression built through the generic engine converts with
`toLinearDensity()`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.lineardensity.*
import org.pcsoft.framework.kunit.mechanic.mass.grams

val typed = (2 of kilo.grams) / (4 of meters)
val native = ((2000 of grams).toUnit() / (4 of meters).toUnit()).toLinearDensity()

typed == native                 // true - both are 0.5 kg/m
typed into gramsPerMeter        // 500.0
```

## Real-world example: steel cable on a drum

A steel cable weighs 2.6 kg/m. What is the mass of a 45 m length, and how much cable does a 500 kg payload limit allow?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.lineardensity.*

val cable = 2.6 of kilogramsPerMeter
val mass = cable * (45 of meters)     // KMassUnitInstance
mass into kilo.grams                  // 117.0

val maxLength = (500 of kilo.grams) / cable // KLengthUnitInstance
maxLength into meters                        // ≈ 192.31
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineardensity.*

val sum = (10 of kilogramsPerMeter) + (4 of kilogramsPerMeter) // 14 kg/m
(1 of kilogramsPerMeter) > (1 of gramsPerMeter)                // true
(1 of kilogramsPerMeter) == (1000 of gramsPerMeter)            // true
(1 of tex) == (9 of denier)                                     // true (textile relation)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.lineardensity.*

(0.5 of kilogramsPerMeter).toString()                 // "0.5 kg/m" (base unit)
"${(0.5 of kilogramsPerMeter) into gramsPerMeter} g/m" // "500.0 g/m"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics   | Kotlin                         | Meaning                                 |
|---------------|--------------------------------|-----------------------------------------|
| `kg/m`        | `kilogramsPerMeter`            | linear density, base unit (named token) |
| `kg·m⁻¹`      | `kilo.grams * (meters pow -1)` | same quantity as a pure product         |
| `tex`         | `tex`                          | textile linear density (1 g/km)         |
| `ρ_l = m / l` | `mass / length`                | typed decomposition                     |
| `m = ρ_l · l` | `lineardensity * length`       | solved for the mass                     |
| `dtex`        | `deci.tex`                     | prefixed textile reading                |
