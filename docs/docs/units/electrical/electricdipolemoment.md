# Electric Dipole Moment

Package: `org.pcsoft.framework.kunit.electric.electricdipolemoment`
Base unit: **coulomb meter**
(`KElectricDipoleMomentUnit.BASE == KElectricDipoleMomentUnit.COULOMB_METER`)

Type: **constructed unit**

Electric dipole moment is a **constructed** unit: the composition `current · time · length`
(`A·s·m` = `C·m`). `KElectricDipoleMomentUnitInstance` wraps a `KMixedUnitInstance` of three terms —
`KElectricCurrentUnit.BASE` (ampere) at `+1`, `KTimeUnit.BASE` (second) at `+1` and `KDistanceUnit.BASE`
(meter) at `+1`. The group carries no mass dimension, so no gram/kilogram bridge is needed; the stored value is always
normalized to coulomb meters.

The electric dipole moment `p = Q · d` measures the separation of a positive and a negative
[Charge](charge.md). It is the quantity that couples a molecule to an
[Electric Field Strength](electricfieldstrength.md).

## Building an electric dipole moment

Build a dipole moment with a named token, or from a decomposition (see below). Named units survive as value-1 tokens
(used with `of`/`into`):

| Dipole moment | Symbol |           Token |   1 unit in C·m |
|---------------|--------|----------------:|----------------:|
| Coulomb meter | `C·m`  | `coulombMeters` |             1.0 |
| Debye (CGS)   | `D`    |        `debyes` | 3.335640952e-30 |

The debye dominates molecular physics and chemistry. Named units support the SI prefixes via `KPrefixBuilder`
(`pico.coulombMeters`, `milli.debyes`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.dipolemoment.*

val p = 1.85 of debyes        // the water molecule
p into debyes                 // 1.85
p into coulombMeters          // 6.1709357612e-30
```

## Multiple decompositions

Electric dipole moment can be reached through several **equivalent decompositions**, all producing the same value-equal
moment:

| Expression            | Result type                         | Meaning                                                  |
|-----------------------|-------------------------------------|----------------------------------------------------------|
| `charge * length`     | `KElectricDipoleMomentUnitInstance` | `p = Q · d`, a charge times its separation (commutative) |
| `current·time·length` | via `.toElectricDipoleMoment()`     | native canonical `A·s·m` expression                      |

The typed operator form returns a dipole moment directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toElectricDipoleMoment()` (which recognises only the canonical normal form
and throws `IllegalStateException` otherwise). Both routes are value-equal.

The inverse operators tie charge, separation and moment together:

| Expression                      | Result type           | Meaning     |
|---------------------------------|-----------------------|-------------|
| `electricDipoleMoment / charge` | `KLengthUnitInstance` | `d = p / Q` |
| `electricDipoleMoment / length` | `KChargeUnitInstance` | `Q = p / d` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.dipolemoment.*

// Real-world example - 1 pC separated by 1 nm gives 1e-21 C·m, about 3.0e8 debyes.
val p = (1 of pico.coulombs) * (1 of nano.meters)   // KElectricDipoleMomentUnitInstance
p into debyes                                       // 2.997924579983392e8

// Solved back for the separation:
val d = (6 of coulombMeters) / (2 of coulombs)      // KLengthUnitInstance, 3 m

// The same moment as the native A·s·m expression:
val raw = 6 of ((amperes pow 1) * (seconds pow 1) * (meters pow 1))
raw.toElectricDipoleMoment() == (6 of coulombMeters) // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.dipolemoment.*

val s = (2 of coulombMeters) + (3 of coulombMeters)  // 5 C·m
(1 of coulombMeters) > (1 of debyes)                 // true
(2 of coulombMeters) * (3 of coulombMeters)          // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.dipolemoment.*

(2 of coulombMeters).toString()   // "2.0 C·m" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts, `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a
fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin                                               | Meaning                                                        |
|-------------|------------------------------------------------------|----------------------------------------------------------------|
| `C·m`       | `coulombMeters`                                      | electric dipole moment, base unit (named token, coulomb meter) |
| `D`         | `debyes`                                             | the CGS debye, 3.335 640 952e-30 C·m                           |
| `Q · d`     | `(1 of pico.coulombs) * (1 of nano.meters)`          | moment from a charge and its separation                        |
| `p / Q`     | `(6 of coulombMeters) / (2 of coulombs)`             | the separation behind a moment                                 |
| `A·s·m`     | `(amperes pow 1) * (seconds pow 1) * (meters pow 1)` | moment as current·time·length (pure product)                   |
| `pC·m`      | `pico.coulombMeters`                                 | prefixed moment (picocoulomb meter)                            |
