# Luminous Flux

Package: `org.pcsoft.framework.kunit.optic.luminousflux`
Base unit: **lumen** (`KLuminousFluxUnit.BASE == KLuminousFluxUnit.LUMEN`)

Type: **constructed unit**

Luminous flux `Φ` is the **total amount of visible light** a source emits in all directions it covers — the number
printed on every lamp package. It is the luminous intensity integrated over a solid angle: `Φ = I · Ω`, so
`1 lm = 1 cd·sr`.

Its canonical base-dimension normal form is `luminousIntensity¹ · solidAngle¹`.

## Units

| Unit               | Enum value                            | Symbol  |               Token | 1 unit in lumens |
|--------------------|---------------------------------------|---------|--------------------:|-----------------:|
| Lumen              | `KLuminousFluxUnit.LUMEN`             | `lm`    |            `lumens` |              1.0 |
| Candela steradian  | `KLuminousFluxUnit.CANDELA_STERADIAN` | `cd·sr` | `candelaSteradians` |              1.0 |

`candelaSteradians` is the written-out definition of the lumen — numerically identical, but it lets a formula spell out
where the unit comes from. Both tokens accept every SI prefix (`kilo.lumens`, `milli.lumens`, …).

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance:

| Form                | Expression                                                       |
|---------------------|------------------------------------------------------------------|
| typed operator      | `luminousIntensity * solidAngle`                                  |
| native (`toX()`)    | `((100 of candelas).toUnit() * (2 of steradians).toUnit()).toLuminousFlux()` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminousflux.*

val typed = (100 of candelas) * (2 of steradians)
val native = ((100 of candelas).toUnit() * (2 of steradians).toUnit()).toLuminousFlux()

typed == native          // true
typed into lumens        // 200.0
```

## Computing with the group

| Expression                       | Result type                      | Meaning                       |
|----------------------------------|----------------------------------|-------------------------------|
| `luminousIntensity * solidAngle` | `KLuminousFluxUnitInstance`      | `Φ = I · Ω`                   |
| `luminousFlux / solidAngle`      | `KLuminousIntensityUnitInstance` | `I = Φ / Ω`                   |
| `luminousFlux / luminousIntensity` | `KSolidAngleUnitInstance`      | the cone the flux spreads over |
| `luminousFlux / area`            | `KIlluminanceUnitInstance`       | `E = Φ / A`                   |
| `luminousFlux * time`            | `KLuminousEnergyUnitInstance`    | `Q = Φ · t`                   |
| `luminousFlux / power`           | `KLuminousEfficacyUnitInstance`  | `η = Φ / P`                   |

## Real-world example — an isotropic bulb

A bare bulb radiates equally in all directions. The full sphere is `4π sr`, so a 100 cd source emits:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.steradians
import org.pcsoft.framework.kunit.optic.luminousintensity.candelas
import org.pcsoft.framework.kunit.optic.luminousflux.*

val phi = (100 of candelas) * ((4 * Math.PI) of steradians)
phi into lumens          // ≈ 1256.6 lm — roughly a 100 W incandescent bulb
```

## Value semantics

`equals`/`hashCode` compare the **normalized lumen value**, so `(1 of lumens) == (1000 of milli.lumens)`.
`toString()` renders the value in the base unit: `"800.0 lm"`.

## See also

* [Luminous Intensity](luminous-intensity.md) — flux per solid angle.
* [Illuminance](illuminance.md) — flux per illuminated area.
* [Luminous Efficacy](luminous-efficacy.md) — flux per watt of electrical power.
* [Optics overview](overview.md)
