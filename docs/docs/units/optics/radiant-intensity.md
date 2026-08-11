# Radiant Intensity

Package: `org.pcsoft.framework.kunit.optic.radiantintensity`
Base unit: **watt per steradian** (`KRadiantIntensityUnit.BASE == KRadiantIntensityUnit.WATT_PER_STERADIAN`)

Type: **constructed unit**

Radiant intensity `Iₑ` is the radiant flux (power) a source emits **per solid angle**: `Iₑ = P / Ω`. It is the
**radiometric** counterpart of the [luminous intensity](luminous-intensity.md) — the same geometry, but measured in
watts rather than lumens, so it counts all radiation including the infrared and ultraviolet the eye cannot see.

Its canonical base-dimension normal form is `mass¹ · distance² · time⁻³ · solidAngle⁻¹`.

## Units

| Unit               | Enum value                                   | Symbol |               Token | 1 unit in W/sr |
|--------------------|----------------------------------------------|--------|--------------------:|---------------:|
| Watt per steradian | `KRadiantIntensityUnit.WATT_PER_STERADIAN`   | `W/sr` | `wattsPerSteradian` |            1.0 |

The token accepts every SI prefix (`milli.wattsPerSteradian`, `kilo.wattsPerSteradian`, …).

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance. The native form is
assembled from **unit templates** because the group carries a mass term (see
[Luminous Efficacy](luminous-efficacy.md) for the same note).

| Form             | Expression                                                        |
|------------------|-------------------------------------------------------------------|
| typed operator   | `power / solidAngle`                                              |
| native (`toX()`) | `(5 of kilo.grams · m² / s³ / sr).toRadiantIntensity()`           |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.radiantintensity.*

val typed = (20 of watts) / (4 of steradians)
val native = (
    5 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / steradians.toUnit()
).toRadiantIntensity()

typed == native                 // true
typed into wattsPerSteradian    // 5.0
```

## Computing with the group

| Expression                        | Result type                       | Meaning                    |
|-----------------------------------|-----------------------------------|----------------------------|
| `power / solidAngle`              | `KRadiantIntensityUnitInstance`   | `Iₑ = P / Ω`               |
| `radiantIntensity * solidAngle`   | `KPowerUnitInstance`              | `P = Iₑ · Ω`               |
| `power / radiantIntensity`        | `KSolidAngleUnitInstance`         | the cone it is spread over |
| `radiantIntensity / area`         | `KRadianceUnitInstance`           | `Lₑ = Iₑ / A`              |

## Real-world example — an infrared LED

An IR emitter radiates **20 mW** into a 0.2 sr cone. Its radiant intensity, and the power a 0.05 sr detector aperture
catches:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.radiantintensity.*

val i = (20 of milli.watts) / (0.2 of steradians)
i into milli.wattsPerSteradian       // 100.0

val caught = i * (0.05 of steradians)  // KPowerUnitInstance
caught into milli.watts                // 5.0 mW reach the detector
```

## Value semantics

`equals`/`hashCode` compare the **normalized W/sr value**, so
`(1 of wattsPerSteradian) == (1000 of milli.wattsPerSteradian)`. `toString()` renders the value in the base unit:
`"5.0 W/sr"`.

## See also

* [Luminous Intensity](luminous-intensity.md) — the photometric counterpart.
* [Radiance](radiance.md) — radiant intensity per emitting area.
* [Luminous Efficacy](luminous-efficacy.md) — the bridge between watts and lumens.
* [Optics overview](overview.md)
