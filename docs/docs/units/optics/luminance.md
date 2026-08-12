# Luminance

Package: `org.pcsoft.framework.kunit.optic.luminance`
Base unit: **candela per square metre** (`KLuminanceUnit.BASE == KLuminanceUnit.CANDELA_PER_SQUARE_METER`)

Type: **constructed unit**

Luminance `L` is the luminous intensity **per emitting area**: `L = I / A`, so `1 cd/m² = 1 nit`. It is what the eye
actually perceives as "brightness" of a surface, and it is the number every display specification quotes — a typical
office monitor is 250–350 nits, an HDR television 1000 nits or more.

Its canonical base-dimension normal form is `luminousIntensity¹ · distance⁻²`.

## Units

| Unit                     | Enum value                                | Symbol   |                    Token | 1 unit in cd/m² |
|--------------------------|-------------------------------------------|----------|-------------------------:|----------------:|
| Candela per square metre | `KLuminanceUnit.CANDELA_PER_SQUARE_METER` | `cd/m^2` | `candelasPerSquareMeter` |             1.0 |
| Nit                      | `KLuminanceUnit.CANDELA_PER_SQUARE_METER` | `cd/m^2` |                   `nits` |             1.0 |
| Stilb                    | `KLuminanceUnit.STILB`                    | `sb`     |                 `stilbs` |          10 000 |
| Apostilb                 | `KLuminanceUnit.APOSTILB`                 | `asb`    |              `apostilbs` |           1 / π |
| Lambert                  | `KLuminanceUnit.LAMBERT`                  | `L`      |               `lamberts` |        10⁴ / π  |
| Foot-lambert             | `KLuminanceUnit.FOOT_LAMBERT`             | `fL`     |           `footLamberts` |      ≈ 3.426259 |

`nits` is a second spelling of the base unit, not a unit of its own — it is the display industry's name for the candela
per square metre. The apostilb, lambert and foot-lambert belong to the *lambertian* family and carry the factor `1/π`
that converts an ideal diffuse emitter's illuminance into its luminance. All tokens accept every SI prefix.

## Decompositions

The group has **two** decompositions. Both funnel into the same normalizing factory, so they produce the same typed,
value-equal instance:

| Form                   | Expression                                                     |
|------------------------|----------------------------------------------------------------|
| typed operator A       | `luminousIntensity / area`                                     |
| typed operator B       | `illuminance / solidAngle`                                     |
| native (`toX()`)       | `((250 of candelas).toUnit() / area.toUnit()).toLuminance()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminance.*

val squareMeter = (1 of meters) * (1 of meters)

val viaIntensity  = (250 of candelas) / squareMeter      // A
val viaIlluminance = (500 of lux) / (2 of steradians)    // B
val native = ((250 of candelas).toUnit() / squareMeter.toUnit()).toLuminance()

viaIntensity == viaIlluminance   // true
viaIntensity == native           // true
viaIntensity into nits           // 250.0
```

## Computing with the group

| Expression                     | Result type                      | Meaning                    |
|--------------------------------|----------------------------------|----------------------------|
| `luminousIntensity / area`     | `KLuminanceUnitInstance`         | `L = I / A`                |
| `illuminance / solidAngle`     | `KLuminanceUnitInstance`         | `L = E / Ω`                |
| `luminance * area`             | `KLuminousIntensityUnitInstance` | `I = L · A`                |
| `luminance * solidAngle`       | `KIlluminanceUnitInstance`       | `E = L · Ω`                |
| `luminousIntensity / luminance` | `KAreaUnitInstance`             | the emitting area          |
| `illuminance / luminance`      | `KSolidAngleUnitInstance`        | the cone it is spread over |

## Real-world example — a monitor's nit rating

A 27" monitor with a **0.21 m²** panel is rated **300 nits**. That corresponds to a total on-axis intensity of:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminance.*

val panel = (0.6 of meters) * (0.35 of meters)   // ≈ 0.21 m²
val l = 300 of nits

val i = l * panel                                 // KLuminousIntensityUnitInstance
i into candelas                                   // 63.0 cd

l into footLamberts                               // ≈ 87.6 (the imperial reading)
```

## Value semantics

`equals`/`hashCode` compare the **normalized cd/m² value**, so `(1 of stilbs) == (10000 of candelasPerSquareMeter)`.
`toString()` renders the value in the base unit: `"250.0 cd/m^2"`.

## See also

* [Luminous Intensity](luminous-intensity.md) — the numerator of the luminance.
* [Illuminance](illuminance.md) — light arriving on a surface rather than leaving it.
* [Radiance](radiance.md) — the radiometric counterpart.
* [Optics overview](overview.md)
