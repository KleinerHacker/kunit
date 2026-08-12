# Luminous Efficacy

Package: `org.pcsoft.framework.kunit.optic.efficacy`
Base unit: **lumen per watt** (`KLuminousEfficacyUnit.BASE == KLuminousEfficacyUnit.LUMEN_PER_WATT`)

Type: **constructed unit**

Luminous efficacy `η` is the luminous flux a lamp produces **per watt of electrical power**: `η = Φ / P`. It is the
single number that says how good a light source is, and it is the bridge between the photometric and the radiometric
family: it converts watts, which a detector measures, into lumens, which the eye perceives.

Its canonical base-dimension normal form is `luminousIntensity¹ · solidAngle¹ · mass⁻¹ · distance⁻² · time³`.

## Units

| Unit           | Enum value                              | Symbol |           Token | 1 unit in lm/W |
|----------------|-----------------------------------------|--------|----------------:|---------------:|
| Lumen per watt | `KLuminousEfficacyUnit.LUMEN_PER_WATT`  | `lm/W` | `lumensPerWatt` |            1.0 |

The token accepts every SI prefix (`milli.lumensPerWatt`, `kilo.lumensPerWatt`, …).

## Constant

| Constant                | Value       | Meaning                                                       |
|-------------------------|-------------|---------------------------------------------------------------|
| `MAX_LUMINOUS_EFFICACY` | `683 lm/W`  | the physical ceiling at 555 nm, from the SI candela definition |

No light source can exceed 683 lm/W, because that is the efficacy of monochromatic green light at the peak of the
photopic luminosity function. Every real lamp is a fraction of it.

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance. Note that the
native form is assembled from **unit templates**: for a group carrying a mass term the raw mixed value is the
gram-based product, while a typed instance stores its value in the named unit.

| Form             | Expression                                                                       |
|------------------|-----------------------------------------------------------------------------------|
| typed operator   | `luminousFlux / power`                                                            |
| native (`toX()`) | `(120 of (cd·sr) / (kilo.grams · m² / s³)).toLuminousEfficacy()`                  |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.efficacy.*

val typed = (1200 of lumens) / (10 of watts)
val native = (
    120 of (candelas.toUnit() * steradians.toUnit()) /
        (kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3))
).toLuminousEfficacy()

typed == native              // true
typed into lumensPerWatt     // 120.0
```

## Computing with the group

| Expression                          | Result type                     | Meaning                |
|-------------------------------------|---------------------------------|------------------------|
| `luminousFlux / power`              | `KLuminousEfficacyUnitInstance` | `η = Φ / P`            |
| `luminousEfficacy * power`          | `KLuminousFluxUnitInstance`     | `Φ = η · P`            |
| `luminousFlux / luminousEfficacy`   | `KPowerUnitInstance`            | the power required     |

## Real-world example — comparing three bulbs

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.efficacy.*

val incandescent = (800 of lumens) / (60 of watts)
val halogen      = (800 of lumens) / (42 of watts)
val led          = (800 of lumens) / (7 of watts)

incandescent into lumensPerWatt      // ≈ 13.3
halogen into lumensPerWatt           // ≈ 19.0
led into lumensPerWatt               // ≈ 114.3

led.value / MAX_LUMINOUS_EFFICACY    // ≈ 0.167 — 17 % of the physical ceiling

// How much power does an LED strip need for 3000 lm?
val p = (3000 of lumens) / led       // KPowerUnitInstance
p into watts                          // 26.25
```

## Value semantics

`equals`/`hashCode` compare the **normalized lm/W value**, so
`(1 of lumensPerWatt) == (1000 of milli.lumensPerWatt)`. `toString()` renders the value in the base unit:
`"120.0 lm/W"`.

## See also

* [Luminous Flux](luminous-flux.md) — the numerator.
* [Radiant Intensity](radiant-intensity.md) and [Radiance](radiance.md) — the radiometric side of the bridge.
* [Power (Electrical)](../electrical/power.md) — the denominator.
* [Optics overview](overview.md)
