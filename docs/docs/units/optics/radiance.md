# Radiance

Package: `org.pcsoft.framework.kunit.optic.radiance`
Base unit: **watt per steradian square metre**
(`KRadianceUnit.BASE == KRadianceUnit.WATT_PER_STERADIAN_SQUARE_METER`)

Type: **constructed unit**

Radiance `Lₑ` is the radiant intensity **per emitting area**: `Lₑ = Iₑ / A`. It is the **radiometric** counterpart of
the [luminance](luminance.md), and the quantity remote sensing and thermal imaging work in — what a camera pixel
actually integrates, independent of how far away the surface is.

Its canonical base-dimension normal form is `mass¹ · time⁻³ · solidAngle⁻¹`. The two length exponents cancel: the
watt contributes `distance²` and the area `distance⁻²`.

## Units

| Unit                            | Enum value                                    | Symbol       |                            Token | 1 unit in W/(sr·m²) |
|---------------------------------|-----------------------------------------------|--------------|---------------------------------:|--------------------:|
| Watt per steradian square metre | `KRadianceUnit.WATT_PER_STERADIAN_SQUARE_METER` | `W/(sr*m^2)` | `wattsPerSteradianSquareMeter`   |                 1.0 |

The token accepts every SI prefix (`milli.wattsPerSteradianSquareMeter`, …).

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance. The native form is
assembled from **unit templates** because the group carries a mass term.

| Form             | Expression                                                    |
|------------------|---------------------------------------------------------------|
| typed operator   | `radiantIntensity / area`                                     |
| native (`toX()`) | `(5 of kilo.grams / s³ / sr).toRadiance()`                    |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.radiantintensity.wattsPerSteradian
import org.pcsoft.framework.kunit.optic.radiance.*

val typed = (10 of wattsPerSteradian) / ((2 of meters) * (1 of meters))
val native = (5 of kilo.grams.toUnit() / (seconds pow 3) / steradians.toUnit()).toRadiance()

typed == native                              // true
typed into wattsPerSteradianSquareMeter      // 5.0
```

## Computing with the group

| Expression                        | Result type                     | Meaning         |
|-----------------------------------|---------------------------------|-----------------|
| `radiantIntensity / area`         | `KRadianceUnitInstance`         | `Lₑ = Iₑ / A`   |
| `radiance * area`                 | `KRadiantIntensityUnitInstance` | `Iₑ = Lₑ · A`   |
| `radiantIntensity / radiance`     | `KAreaUnitInstance`             | the emitting area |

## Real-world example — a thermal camera pixel

A furnace wall of **2 m²** radiates **10 W/sr** towards the camera. Its radiance — the value the camera reports
regardless of distance — is:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.optic.radiantintensity.wattsPerSteradian
import org.pcsoft.framework.kunit.optic.radiance.*

val wall = (2 of meters) * (1 of meters)
val l = (10 of wattsPerSteradian) / wall
l into wattsPerSteradianSquareMeter      // 5.0

// A 0.5 m² patch of the same wall emits proportionally less intensity …
val patch = (0.5 of meters) * (1 of meters)
(l * patch) into wattsPerSteradian       // 2.5 — but the radiance is unchanged
```

## Value semantics

`equals`/`hashCode` compare the **normalized W/(sr·m²) value**, so
`(1 of wattsPerSteradianSquareMeter) == (1000 of milli.wattsPerSteradianSquareMeter)`. `toString()` renders the value
in the base unit: `"5.0 W/(sr*m^2)"`.

## See also

* [Radiant Intensity](radiant-intensity.md) — the numerator.
* [Luminance](luminance.md) — the photometric counterpart.
* [Heat Flux Density](../thermodynamics/heat-flux-density.md) — radiance integrated over the hemisphere.
* [Optics overview](overview.md)
