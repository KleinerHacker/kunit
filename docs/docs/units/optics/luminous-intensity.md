# Luminous Intensity

Package: `org.pcsoft.framework.kunit.optic.luminousintensity`
Base unit: **candela** (`KLuminousIntensityUnit.BASE == KLuminousIntensityUnit.CANDELA`)

Type: **native unit**

Luminous intensity `I` is the luminous flux a source emits **per solid angle** in a given direction. Its unit, the
candela, is the **seventh SI base unit** — and the only one defined through human perception: 1 cd is the intensity of
a source emitting monochromatic radiation of 540 THz with a radiant intensity of 1/683 W/sr in that direction.

The group is a **plain, one-dimensional** native group (no exponent-specialized subtypes):
`KLuminousIntensityUnitInstance` wraps a single `KLuminousIntensityUnit.CANDELA` term, always stored normalized to
candelas.

## Units

| Unit            | Enum value                                | Symbol   |          Token | 1 unit in candelas |
|-----------------|-------------------------------------------|----------|---------------:|-------------------:|
| Candela         | `KLuminousIntensityUnit.CANDELA`          | `cd`     |     `candelas` |                1.0 |
| Hefner candle   | `KLuminousIntensityUnit.HEFNER_CANDLE`    | `HK`     | `hefnerCandles` |              0.903 |
| Candlepower     | `KLuminousIntensityUnit.CANDLEPOWER`      | `cp`     |  `candlepower` |              0.981 |
| Carcel          | `KLuminousIntensityUnit.CARCEL`           | `carcel` |      `carcels` |               9.74 |

The three non-SI entries are the historical national standards that preceded the candela — the German Hefner lamp, the
British international candle and the French Carcel oil lamp. They are kept so older datasheets can be read directly.

Each token is a value-1 `KLuminousIntensityUnitInstance` used with `of` (build) and `into` (read). All tokens accept
every SI prefix (`milli.candelas`, `kilo.candelas`, …).

## Computing with the group

| Expression                       | Result type                     | Meaning                          |
|----------------------------------|---------------------------------|----------------------------------|
| `luminousIntensity + …`          | `KLuminousIntensityUnitInstance` | same-type addition               |
| `luminousIntensity * solidAngle` | `KLuminousFluxUnitInstance`     | `Φ = I · Ω`, the emitted flux    |
| `luminousIntensity / area`       | `KLuminanceUnitInstance`        | `L = I / A`, the surface's glow  |
| `luminousFlux / solidAngle`      | `KLuminousIntensityUnitInstance` | back from flux                   |

The native form converts with `toLuminousIntensity()`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.optic.luminousintensity.*

val raw = (1200 of candelas).toUnit()   // KMixedUnitInstance
raw.toLuminousIntensity() into candelas // 1200.0
```

## Real-world example — a car headlight

A low-beam headlight is specified at **1200 cd** on its optical axis. Spread over a 0.05 sr cone, that is the luminous
flux actually aimed at the road:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.*
import org.pcsoft.framework.kunit.optic.luminousflux.*

val i = 1200 of candelas
i into kilo.candelas                     // 1.2

val beam = i * (0.05 of steradians)      // KLuminousFluxUnitInstance
beam into lumens                         // 60.0 lm in the beam cone
```

## Value semantics

`equals`/`hashCode` compare the **normalized candela value**, so `(1 of candelas) == (1000 of milli.candelas)`.
`toString()` renders the value in the base unit: `"1200.0 cd"`.

## See also

* [Luminous Flux](luminous-flux.md) — intensity integrated over a solid angle.
* [Luminance](luminance.md) — intensity per emitting area.
* [Radiant Intensity](radiant-intensity.md) — the radiometric counterpart, unweighted by the eye.
* [Optics overview](overview.md)
