# Dioptre (Refractive Power)

Package: `org.pcsoft.framework.kunit.common.reciprocallength`
Base unit: **reciprocal metre** (`KReciprocalLengthUnit.BASE == KReciprocalLengthUnit.RECIPROCAL_METER`)

Type: **constructed unit**

The refractive power `D` of a lens is the reciprocal of its focal length: `D = 1 / f`. Its unit is the **dioptre**,
which is exactly the reciprocal metre — a lens focusing at 1 m has 1 dpt, one focusing at 0.5 m has 2 dpt.

Its dimension is `distance⁻¹` — **the same** as the [wavenumber](../mechanics/wavenumber.md) of spectroscopy. KUnit
models one neutral group, `reciprocallength`, for both readings; the refractive power is one of them. This page
documents that reading.

!!! note "One group, two readings"
    `KReciprocalLengthUnitInstance` is the shared type, so a refractive power and a wavenumber are the same unit as far
    as KUnit is concerned. The group carries the neutral name `reciprocallength` so that neither reading claims the
    other's name. Distinguish them by naming your values.

## Named units

| Unit                  | Symbol |                  Token | 1 unit in m⁻¹ |
|-----------------------|--------|-----------------------:|--------------:|
| Reciprocal metre      | `1/m`  |     `reciprocalMeters` |           1.0 |
| Dioptre               | `dpt`  |             `dioptres` |           1.0 |
| Reciprocal centimetre | `1/cm` | `reciprocalCentimeters` |         100.0 |
| Kayser                | `1/cm` |               `kaysers` |         100.0 |

`dioptres` and `kaysers` are alternative spellings of the reciprocal metre and reciprocal centimetre respectively, not
units of their own. All tokens accept every SI prefix (`milli.dioptres`, …).

## Computing with the group

| Expression                       | Result type                      | Meaning                          |
|----------------------------------|----------------------------------|----------------------------------|
| `1 / length`                     | `KReciprocalLengthUnitInstance`  | `D = 1 / f`                      |
| `1 / reciprocalLength`           | `KLengthUnitInstance`            | back to the focal length         |
| `reciprocalLength + …`           | `KReciprocalLengthUnitInstance`  | thin lenses in contact add power |
| `reciprocalLength * length`      | `Double`                         | dimensionless count (`m⁻¹ · m`)  |

The native form converts with `toReciprocalLength()`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val native = (2.5 of ((1 of meters).toUnit() pow -1)).toReciprocalLength()
native into dioptres      // 2.5
```

## Real-world example — reading glasses

A lens with a focal length of **40 cm** gives `D = 1 / 0.4 m = 2.5 dpt`. Placing a second, weaker lens in contact with
it simply adds the powers — which is exactly what same-type `+` does:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val d = 1 / (40 of centi.meters)     // KReciprocalLengthUnitInstance
d into dioptres                       // 2.5

val combined = d + (1.5 of dioptres)  // lenses in contact
combined into dioptres                // 4.0

val f = 1 / combined                  // KLengthUnitInstance
f into centi.meters                   // 25.0 — the combined focal length
```

## Value semantics

`equals`/`hashCode` compare the **normalized m⁻¹ value**, so
`(1 of reciprocalCentimeters) == (100 of reciprocalMeters)`. `toString()` renders the value in the base unit:
`"2.5 1/m"`.

## See also

* [Wavenumber](../mechanics/wavenumber.md) — the same type, read as a spectroscopic quantity.
* [Distance](../kinematics/distance.md) — the group this one is the reciprocal of.
* [Optics overview](overview.md)
