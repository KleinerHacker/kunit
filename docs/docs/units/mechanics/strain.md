# Strain

Package: `org.pcsoft.framework.kunit.mechanic.strain`
Base unit: **plain ratio** (`KStrainUnit.BASE == KStrainUnit.RATIO`)

Type: **constructed unit**

The strain `ε = ΔL / L` is the relative deformation of a body. It is **dimensionless** — a length divided by a length —
but its readings (percent, per mille, microstrain) form a genuine unit vocabulary, so KUnit models it as its own group.

`KStrainUnitInstance` wraps a `KMixedUnitInstance` of a single `KStrainUnit.BASE` term at exponent 1, always normalized
to the plain ratio.

!!! note "Why `toStrain()` and not an operator"
The generic engine represents `length / length` as a mixed unit with **no** unit terms. Since
`KLengthUnitInstance.div` is a member operator it cannot be overridden, so the native decomposition is reached through
the form-recognition hook `toStrain()` instead of a typed operator.

## Named units

| Unit              | Symbol |         Token | 1 unit as ratio |
|-------------------|--------|--------------:|----------------:|
| Plain ratio (m/m) | `1`    |       `ratio` |             1.0 |
| Percent           | `%`    |     `percent` |            0.01 |
| Per mille         | `‰`    |    `perMille` |            1e-3 |
| Microstrain       | `µe`   | `microstrain` |            1e-6 |

All units accept the full SI prefix range, so `micro.ratio` is another spelling of the microstrain.

## Building a strain

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.strain.*

// a 1 m bar elongated by 2 mm
val e = ((2 of milli.meters) / (1 of meters)).toStrain()
e into perMille     // 2.0
e into percent      // 0.2
e into microstrain  // 2000.0
e into ratio        // 0.002
```

## Computing with a strain

| Expression                               | Result type             | Meaning                     |
|------------------------------------------|-------------------------|-----------------------------|
| `(length / length).toStrain()`           | `KStrainUnitInstance`   | `ε = ΔL / L` (native form)  |
| `stress / strain`                        | `KPressureUnitInstance` | elastic modulus `E = σ / ε` |
| `pressure * strain`, `strain * pressure` | `KPressureUnitInstance` | stress `σ = E · ε`          |
| `strain + strain`, `strain - strain`     | `KStrainUnitInstance`   | same-type arithmetic        |

See the [stress](stress.md) page for the elastic-modulus side of Hooke's law.

## Real-world example: strain gauge on a steel bar

A strain gauge on a steel bar (E = 210 GPa) reads 950 µe. Which mechanical stress does that correspond to, and how much
does a 2 m bar elongate?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.strain.*
import org.pcsoft.framework.kunit.times

val e = 950 of microstrain
val stress = (210 of giga.pascals) * e
stress into mega.pascals               // ≈ 199.5

val elongation = (2 of meters) * (e into ratio) // scalar scaling of a length
elongation into milli.meters                    // 1.9
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.strain.*

val sum = (3 of perMille) + (1 of perMille) // 4 ‰
(1 of percent) > (5 of perMille)            // true
(1 of percent) == (10 of perMille)          // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.strain.*

(2 of perMille).toString()                 // "0.002 1" (base unit: the plain ratio)
"${(2 of perMille) into percent} %"        // "0.2 %"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics  | Kotlin                         | Meaning                           |
|--------------|--------------------------------|-----------------------------------|
| `1` (m/m)    | `ratio`                        | strain, base unit (dimensionless) |
| `%`          | `percent`                      | percent reading                   |
| `‰`          | `perMille`                     | per-mille reading                 |
| `µe`         | `microstrain`                  | strain-gauge reading (1 µm/m)     |
| `ε = ΔL / L` | `(length / length).toStrain()` | native decomposition              |
| `σ = E · ε`  | `pressure * strain`            | Hooke's law                       |
| `E = σ / ε`  | `stress / strain`              | elastic modulus                   |
