# Permittivity

Package: `org.pcsoft.framework.kunit.electric.permittivity`
Base unit: **farad per meter** (`KPermittivityUnit.BASE == KPermittivityUnit.FARAD_PER_METER`)

Type: **constructed unit**

Permittivity is a **constructed** unit: the composition `mass⁻¹ · length⁻³ · time⁴ · current²`
(`kg⁻¹·m⁻³·s⁴·A²` = `F/m`). `KPermittivityUnitInstance` wraps a `KMixedUnitInstance` of four terms —
`KMassUnit.BASE` (gram) at `-1`, `KDistanceUnit.BASE` (meter) at `-3`, `KTimeUnit.BASE` (second) at `+4` and
`KElectricCurrentUnit.BASE` (ampere) at `+2`. Because the mass component of the library is normalized to **grams** (not
kilograms) and the mass exponent is negative, the canonical product is multiplied by 1000 to reach farads per meter; the
stored value is always normalized to farads per meter.

The permittivity `ε` is the electric constant of a material: it links the
[Electric Flux Density](electricfluxdensity.md) to the
[Electric Field Strength](electricfieldstrength.md) (`ε = D / E`) and the
[Capacitance](capacitance.md) to the plate geometry. Its magnetic counterpart is the
[Permeability](permeability.md).

## Building a permittivity

Build a permittivity with a named token, or from a decomposition (see below). Named units survive as value-1 tokens
(used with `of`/`into`):

| Permittivity             | Symbol |                 Token |    1 unit in F/m |
|--------------------------|--------|----------------------:|-----------------:|
| Farad per meter          | `F/m`  |      `faradsPerMeter` |              1.0 |
| Farad per centimeter     | `F/cm` | `faradsPerCentimeter` |            100.0 |
| Vacuum permittivity `ε₀` | `F/m`  |  `vacuumPermittivity` | 8.8541878188e-12 |

Named units support the SI prefixes via `KPrefixBuilder` (`pico.faradsPerMeter`, `nano.faradsPerMeter`, …). The constant
is also available as `KPermittivityUnit.VACUUM_PERMITTIVITY`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.electric.permittivity.*

val eps = 1 of vacuumPermittivity     // ε₀
eps into faradsPerMeter               // 8.8541878188e-12
eps into pico.faradsPerMeter          // 8.8541878188
(1 of faradsPerCentimeter) into faradsPerMeter // 100.0
```

## Multiple decompositions

Permittivity can be reached through several **equivalent decompositions**, all producing the same value-equal
permittivity:

| Expression                                    | Result type                 | Meaning                                                  |
|-----------------------------------------------|-----------------------------|----------------------------------------------------------|
| `capacitance / length`                        | `KPermittivityUnitInstance` | `ε = C · d / A`, the geometry factor `d / A` is a length |
| `electricFluxDensity / electricFieldStrength` | `KPermittivityUnitInstance` | `ε = D / E`                                              |
| `(time⁴·current²)/(mass·length³)`             | via `.toPermittivity()`     | native canonical `kg⁻¹·m⁻³·s⁴·A²` expression             |

The typed operator forms return a permittivity directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toPermittivity()` (which recognises only the canonical normal form and throws
`IllegalStateException` otherwise). All routes are value-equal.

The inverse operators tie capacitance, length and the two field quantities together:

| Expression                             | Result type                          | Meaning                             |
|----------------------------------------|--------------------------------------|-------------------------------------|
| `permittivity * length`                | `KCapacitanceUnitInstance`           | `C = ε · A / d` (commutative)       |
| `capacitance / permittivity`           | `KLengthUnitInstance`                | the geometry factor `A / d = C / ε` |
| `permittivity * electricFieldStrength` | `KElectricFluxDensityUnitInstance`   | `D = ε · E` (commutative)           |
| `electricFluxDensity / permittivity`   | `KElectricFieldStrengthUnitInstance` | `E = D / ε`                         |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pico
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.capacitance.farads
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.electric.fluxdensity.coulombsPerSquareMeter
import org.pcsoft.framework.kunit.electric.permittivity.*

// Real-world example - in vacuum a field of 1 MV/m produces a flux density of 8.854 µC/m².
val d = (1 of vacuumPermittivity) * (1_000_000 of voltsPerMeter)  // 8.8541878188e-6 C/m²

// The definition solved for the permittivity:
val eps = (6 of coulombsPerSquareMeter) / (3 of voltsPerMeter)    // 2 F/m
val fromCapacitance = (10 of farads) / (5 of meters)              // 2 F/m

// The same permittivity as the native kg⁻¹·m⁻³·s⁴·A² expression:
val raw = 2 of ((seconds pow 4) * (amperes pow 2)) / (kilo.grams * (meters pow 3))
raw.toPermittivity() == (2 of faradsPerMeter)                     // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.permittivity.*

val s = (1 of faradsPerMeter) + (1 of faradsPerCentimeter)  // 101 F/m
(1 of faradsPerCentimeter) > (1 of faradsPerMeter)          // true
(2 of faradsPerMeter) * (3 of faradsPerMeter)               // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.permittivity.*

(1 of faradsPerCentimeter).toString()   // "100.0 F/m" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`⁴`, `⁻³`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written
both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics       | Kotlin                                                                      | Meaning                                                           |
|-------------------|-----------------------------------------------------------------------------|-------------------------------------------------------------------|
| `F/m`             | `faradsPerMeter`                                                            | permittivity, base unit (named token, farad per meter)            |
| `ε₀`              | `vacuumPermittivity`                                                        | the vacuum permittivity constant, 8.854 pF/m                      |
| `D / E`           | `(6 of coulombsPerSquareMeter) / (3 of voltsPerMeter)`                      | permittivity as flux density over field strength                  |
| `C · (d/A)`       | `(10 of farads) / (5 of meters)`                                            | permittivity from capacitance and geometry factor                 |
| `(s⁴·A²)/(kg·m³)` | `((seconds pow 4) * (amperes pow 2)) / (kilo.grams * (meters pow 3))`       | permittivity as (time⁴·current²) / (mass·length³) (fraction form) |
| `kg⁻¹·m⁻³·s⁴·A²`  | `(kilo.grams pow -1) * (meters pow -3) * (seconds pow 4) * (amperes pow 2)` | same permittivity as a pure product                               |
| `pF/m`            | `pico.faradsPerMeter`                                                       | prefixed permittivity (picofarad per meter)                       |
