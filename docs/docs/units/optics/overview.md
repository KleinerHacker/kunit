# Optics — Overview

Packages: `org.pcsoft.framework.kunit.optic.luminousintensity`, `…luminousflux`, `…illuminance`,
`…luminance`, `…luminousenergy`, `…luminousexposure`, `…efficacy`, `…radiantintensity`, `…radiance`,
and `org.pcsoft.framework.kunit.common.reciprocallength`

Optics is the description of **light** — how much of it a source emits, how much of it arrives on a surface, and how
efficiently electrical power is turned into it. The field is built on the **candela**, the seventh and last SI base
unit, which is the only base unit that is defined in terms of human perception: it weights radiant power by the
sensitivity of the eye.

That is why the field has two parallel families. The **photometric** quantities (candela, lumen, lux, nit) describe
light *as the eye sees it*; the **radiometric** quantities (watt per steradian, watt per steradian square metre)
describe the same radiation *as a detector measures it*, without the eye's weighting. The bridge between them is the
[luminous efficacy](luminous-efficacy.md), capped at 683 lm/W.

## Units in this topic

| Unit               | Type        | Base unit                              | Page                                     |
|--------------------|-------------|----------------------------------------|------------------------------------------|
| Luminous Intensity | native      | candela (`cd`)                         | [Luminous Intensity](luminous-intensity.md) |
| Luminous Flux      | constructed | lumen (`lm`)                           | [Luminous Flux](luminous-flux.md)        |
| Illuminance        | constructed | lux (`lx`)                             | [Illuminance](illuminance.md)            |
| Luminance          | constructed | candela per square metre (`cd/m²`)     | [Luminance](luminance.md)                |
| Luminous Energy    | constructed | lumen second (`lm·s`)                  | [Luminous Energy](luminous-energy.md)    |
| Luminous Exposure  | constructed | lux second (`lx·s`)                    | [Luminous Exposure](luminous-exposure.md) |
| Luminous Efficacy  | constructed | lumen per watt (`lm/W`)                | [Luminous Efficacy](luminous-efficacy.md) |
| Radiant Intensity  | constructed | watt per steradian (`W/sr`)            | [Radiant Intensity](radiant-intensity.md) |
| Radiance           | constructed | watt per steradian m² (`W/(sr·m²)`)    | [Radiance](radiance.md)                  |
| Refractive Power   | constructed | dioptre (`dpt` = `m⁻¹`)                | [Dioptre](dioptre.md)                    |

The solid angle that ties the intensity quantities to the flux quantities is **not** part of this field — it lives with
the [Mechanics](../mechanics/solid-angle.md) topic and is reused here as-is.

## How the quantities relate

Every relation below returns the correct **typed** quantity; you never assemble a raw mixed unit by hand:

| Expression                     | Result             | Formula        |
|--------------------------------|--------------------|----------------|
| `luminousIntensity * solidAngle` | Luminous Flux    | `Φ = I · Ω`    |
| `luminousFlux / area`          | Illuminance        | `E = Φ / A`    |
| `luminousIntensity / area`     | Luminance          | `L = I / A`    |
| `illuminance / solidAngle`     | Luminance          | `L = E / Ω`    |
| `luminousFlux * time`          | Luminous Energy    | `Q = Φ · t`    |
| `illuminance * time`           | Luminous Exposure  | `H = E · t`    |
| `luminousFlux / power`         | Luminous Efficacy  | `η = Φ / P`    |
| `power / solidAngle`           | Radiant Intensity  | `Iₑ = P / Ω`   |
| `radiantIntensity / area`      | Radiance           | `Lₑ = Iₑ / A`  |
| `1 / length`                   | Refractive Power   | `D = 1 / f`    |

## Worked example — is this bulb bright enough for my desk?

An LED bulb is rated **800 lm** at **7 W**. It hangs over a **2 m²** desk. Office work wants about 500 lx. Does it
suffice, and how efficient is the bulb?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.optic.luminousflux.*
import org.pcsoft.framework.kunit.optic.illuminance.*
import org.pcsoft.framework.kunit.optic.efficacy.*

val flux = 800 of lumens
val desk = (2 of meters) * (1 of meters)          // KAreaUnitInstance, 2 m²

val e = flux / desk                                // KIlluminanceUnitInstance
e into lux                                         // 400.0 — a bit short of the 500 lx target

val eta = flux / (7 of watts)                      // KLuminousEfficacyUnitInstance
eta into lumensPerWatt                             // ≈ 114.3
eta.value / MAX_LUMINOUS_EFFICACY                  // ≈ 0.167 — 17 % of the physical ceiling
```

## Worked example — reading glasses

A lens with a focal length of **40 cm** has a refractive power of `D = 1 / f`. Two thin lenses in contact simply add
their powers, which is exactly what `+` does on the typed quantity:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.common.reciprocallength.*

val d = 1 / (40 of centi.meters)   // KReciprocalLengthUnitInstance
d into dioptres                     // 2.5

val combined = d + (1.5 of dioptres) // lenses in contact add
combined into dioptres               // 4.0
1 / combined into meters             // 0.25 — the combined focal length
```

## Notation

The table shows the field's core relations mathematically versus in Kotlin with KUnit. Exponents use Unicode
superscripts (`²`, `⁻¹`), `·` denotes multiplication and `/` a fraction.

| Mathematics   | Kotlin                                    | Meaning                             |
|---------------|-------------------------------------------|-------------------------------------|
| `Φ = I · Ω`   | `(100 of candelas) * (2 of steradians)`   | luminous flux from intensity × cone |
| `E = Φ / A`   | `(800 of lumens) / desk`                  | illuminance from flux ÷ area        |
| `L = I / A`   | `(250 of candelas) / screen`              | luminance from intensity ÷ area     |
| `Q = Φ · t`   | `(800 of lumens) * (2 of hours)`          | luminous energy from flux × time    |
| `H = E · t`   | `(50 of lux) * (8 of hours)`              | light dose from illuminance × time  |
| `η = Φ / P`   | `(800 of lumens) / (7 of watts)`          | luminous efficacy                   |
| `Iₑ = P / Ω`  | `(20 of watts) / (4 of steradians)`       | radiant intensity                   |
| `D = 1 / f`   | `1 / (40 of centi.meters)`                | refractive power from focal length  |

## Where to go next

* [Luminous Intensity](luminous-intensity.md) — the candela, the field's native base quantity.
* [Luminous Flux](luminous-flux.md) and [Illuminance](illuminance.md) — what a lamp emits and what a surface receives.
* [Luminance](luminance.md) — the quantity a display's "nits" rating refers to.
* [Luminous Efficacy](luminous-efficacy.md) — the bridge between the photometric and the radiometric family.
* [Dioptre](dioptre.md) — refractive power, and its spectroscopic twin the [wavenumber](../mechanics/wavenumber.md).
