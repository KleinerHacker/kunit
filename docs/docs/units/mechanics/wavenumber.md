# Wavenumber

Package: `org.pcsoft.framework.kunit.common.reciprocallength`
Base unit: **reciprocal metre** (`KReciprocalLengthUnit.BASE == KReciprocalLengthUnit.RECIPROCAL_METER`)

Type: **constructed unit**

The wavenumber `ṽ` of a wave is the reciprocal of its wavelength: `ṽ = 1 / λ` — the number of wave cycles per unit of
length. Spectroscopy uses it instead of the wavelength because it is proportional to photon energy, and it is almost
always quoted in **reciprocal centimetres** (`cm⁻¹`, historically called the *kayser*): visible light spans roughly
14 000–25 000 cm⁻¹, the infrared fingerprint region 400–1500 cm⁻¹.

Its dimension is `distance⁻¹` — **the same** as the refractive power of a lens, the
[dioptre](../optics/dioptre.md). KUnit models one neutral group, `reciprocallength`, for both readings; the wavenumber
is one of them. This page documents that reading.

!!! note "One group, two readings"
    `KReciprocalLengthUnitInstance` is the shared type, so a wavenumber and a refractive power are the same unit as far
    as KUnit is concerned. The group carries the neutral name `reciprocallength` so that neither reading claims the
    other's name. Distinguish them by naming your values.

## Named units

| Unit                  | Symbol |                   Token | 1 unit in m⁻¹ |
|-----------------------|--------|------------------------:|--------------:|
| Reciprocal metre      | `1/m`  |      `reciprocalMeters` |           1.0 |
| Reciprocal centimetre | `1/cm` | `reciprocalCentimeters` |         100.0 |
| Kayser                | `1/cm` |                `kaysers` |         100.0 |
| Dioptre               | `dpt`  |               `dioptres` |           1.0 |

All tokens accept every SI prefix (`kilo.reciprocalCentimeters`, …).

## Computing with the group

| Expression                  | Result type                     | Meaning                         |
|-----------------------------|---------------------------------|---------------------------------|
| `1 / length`                | `KReciprocalLengthUnitInstance` | `ṽ = 1 / λ`                     |
| `1 / reciprocalLength`      | `KLengthUnitInstance`           | back to the wavelength          |
| `reciprocalLength * length` | `Double`                        | dimensionless cycle count       |
| `reciprocalLength + …`      | `KReciprocalLengthUnitInstance` | same-type addition              |

The native form converts with `toReciprocalLength()`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val native = (100 of ((1 of meters).toUnit() pow -1)).toReciprocalLength()
native into reciprocalCentimeters      // 1.0
```

## Real-world example — green laser light

A 500 nm laser line converts to a wavenumber of 20 000 cm⁻¹, and the number of cycles fitting into a 1 mm path
follows directly:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val k = 1 / (500 of nano.meters)       // KReciprocalLengthUnitInstance
k into reciprocalCentimeters            // 20_000.0
k into kaysers                          // 20_000.0 (same unit, classical name)

val cycles = k * (1 of milli.meters)    // Double
cycles                                   // 2000.0 wave cycles per millimetre

val lambda = 1 / k                       // KLengthUnitInstance
lambda into nano.meters                  // 500.0
```

## Value semantics

`equals`/`hashCode` compare the **normalized m⁻¹ value**, so
`(1 of reciprocalCentimeters) == (100 of reciprocalMeters)`. `toString()` renders the value in the base unit:
`"2000000.0 1/m"`.

## See also

* [Dioptre](../optics/dioptre.md) — the same type, read as a refractive power.
* [Frequency](../kinematics/frequency.md) — the reciprocal of time, the temporal analogue of this group.
* [Mechanics overview](overview.md)
