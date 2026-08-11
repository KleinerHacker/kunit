# Illuminance

Package: `org.pcsoft.framework.kunit.optic.illuminance`
Base unit: **lux** (`KIlluminanceUnit.BASE == KIlluminanceUnit.LUX`)

Type: **constructed unit**

Illuminance `E` is the luminous flux **arriving on a surface**, per unit of that surface: `E = Φ / A`, so
`1 lx = 1 lm/m²`. It is the quantity every workplace lighting standard is written in — and, unlike the luminous flux,
it depends on how far the lamp is and how big the lit area is, not only on the lamp.

Its canonical base-dimension normal form is `luminousIntensity¹ · solidAngle¹ · distance⁻²`.

## Units

| Unit         | Enum value                     | Symbol |         Token | 1 unit in lux |
|--------------|--------------------------------|--------|--------------:|--------------:|
| Lux          | `KIlluminanceUnit.LUX`         | `lx`   |         `lux` |           1.0 |
| Phot         | `KIlluminanceUnit.PHOT`        | `ph`   |       `phots` |        10 000 |
| Foot-candle  | `KIlluminanceUnit.FOOT_CANDLE` | `fc`   | `footCandles` |    ≈ 10.76391 |
| Nox          | `KIlluminanceUnit.NOX`         | `nx`   |         `nox` |         0.001 |

The phot is the CGS unit (1 lm/cm²), the foot-candle the imperial one (1 lm/ft²), and the nox is used for very low
light levels such as moonlight. All tokens accept every SI prefix (`kilo.lux`, `milli.lux`, …).

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance:

| Form             | Expression                                                             |
|------------------|------------------------------------------------------------------------|
| typed operator   | `luminousFlux / area`                                                  |
| native (`toX()`) | `(cd.toUnit() * sr.toUnit() / (m.toUnit() pow 2)).toIlluminance()`     |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.illuminance.*

val native = (
    (1 of candelas).toUnit() * (1 of steradians).toUnit() / ((1 of meters).toUnit() pow 2)
).toIlluminance()
native into lux          // 1.0
```

## Computing with the group

| Expression                 | Result type                 | Meaning                     |
|----------------------------|-----------------------------|-----------------------------|
| `luminousFlux / area`      | `KIlluminanceUnitInstance`  | `E = Φ / A`                 |
| `illuminance * area`       | `KLuminousFluxUnitInstance` | `Φ = E · A`                 |
| `luminousFlux / illuminance` | `KAreaUnitInstance`       | the area a flux can light   |
| `illuminance / solidAngle` | `KLuminanceUnitInstance`    | `L = E / Ω`                 |
| `illuminance * time`       | `KLuminousExposureUnitInstance` | `H = E · t`             |

## Real-world example — is my desk bright enough?

Office work wants roughly **500 lx**. An 800 lm bulb over a 2 m² desk delivers:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.illuminance.*

val desk = (2 of meters) * (1 of meters)     // 2 m²
val e = (800 of lumens) / desk               // KIlluminanceUnitInstance

e into lux                                    // 400.0 — short of the 500 lx target
e into footCandles                            // ≈ 37.2

val needed = (500 of lux) * desk              // KLuminousFluxUnitInstance
needed into lumens                            // 1000.0 lm would be required
```

## Value semantics

`equals`/`hashCode` compare the **normalized lux value**, so `(1 of phots) == (10000 of lux)`.
`toString()` renders the value in the base unit: `"500.0 lx"`.

## See also

* [Luminous Flux](luminous-flux.md) — what the lamp emits.
* [Luminance](luminance.md) — illuminance per solid angle, the "brightness" of a surface.
* [Luminous Exposure](luminous-exposure.md) — illuminance accumulated over time.
* [Optics overview](overview.md)
