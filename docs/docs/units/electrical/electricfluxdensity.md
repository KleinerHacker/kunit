# Electric Flux Density

Package: `org.pcsoft.framework.kunit.electricfluxdensity`
Base unit: **coulomb per square meter**
(`KElectricFluxDensityUnit.BASE == KElectricFluxDensityUnit.COULOMB_PER_SQUARE_METER`)

Type: **constructed unit**

Electric flux density is a **constructed** unit: the composition `current · time · length⁻²`
(`A·s·m⁻²` = `C/m²`). `KElectricFluxDensityUnitInstance` wraps a `KMixedUnitInstance` of three terms —
`KElectricCurrentUnit.BASE` (ampere) at `+1`, `KTimeUnit.BASE` (second) at `+1` and `KDistanceUnit.BASE`
(meter) at `-2`. The group carries no mass dimension, so no gram/kilogram bridge is needed; the stored value
is always normalized to coulombs per square meter.

The flux density `D` (also called the electric displacement) is the charge per unit of area. The **surface
charge density** `σ` is dimensionally the same quantity and is therefore represented by this very group
rather than by a separate one. `D` relates to the [Electric Field Strength](electricfieldstrength.md)
through the [Permittivity](permittivity.md) (`D = ε · E`). The one-dimensional counterpart is the
[Linear Charge Density](linearchargedensity.md), the three-dimensional one the
[Charge Density](chargedensity.md).

## Building an electric flux density

Build a flux density with a named token, or from a decomposition (see below). Named units survive as value-1
tokens (used with `of`/`into`):

| Flux density | Symbol | Token | 1 unit in C/m² |
|---|---|---:|---:|
| Coulomb per square meter | `C/m²` | `coulombsPerSquareMeter` | 1.0 |
| Coulomb per square centimeter | `C/cm²` | `coulombsPerSquareCentimeter` | 1.0e4 |

Named units support the SI prefixes via `KPrefixBuilder` (`micro.coulombsPerSquareMeter`,
`milli.coulombsPerSquareMeter`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electricfluxdensity.*

val d = 5 of micro.coulombsPerSquareMeter   // a charged capacitor plate
d into micro.coulombsPerSquareMeter         // 5.0
d into coulombsPerSquareMeter               // 5.0e-6
(1 of coulombsPerSquareCentimeter) into coulombsPerSquareMeter // 10000.0
```

## Multiple decompositions

Electric flux density can be reached through several **equivalent decompositions**, all producing the same
value-equal density:

| Expression | Result type | Meaning |
|---|---|---|
| `charge / area` | `KElectricFluxDensityUnitInstance` | `D = Q / A`, the charge spread over an area |
| `permittivity * electricFieldStrength` | `KElectricFluxDensityUnitInstance` | `D = ε · E` (commutative, see [Permittivity](permittivity.md)) |
| `current·time/length²` | via `.toElectricFluxDensity()` | native canonical `A·s·m⁻²` expression |

The typed operator forms return a flux density directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toElectricFluxDensity()` (which recognises only the canonical
normal form and throws `IllegalStateException` otherwise). All routes are value-equal.

The inverse operators tie charge, area and flux density together:

| Expression | Result type | Meaning |
|---|---|---|
| `electricFluxDensity * area` | `KChargeUnitInstance` | `Q = D · A` (commutative) |
| `charge / electricFluxDensity` | `KAreaUnitInstance` | `A = Q / D` |
| `electricFluxDensity / electricFieldStrength` | `KPermittivityUnitInstance` | `ε = D / E` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.distance.ares
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.distance.KAreaUnitInstance
import org.pcsoft.framework.kunit.electricfluxdensity.*

// Real-world example - 20 µC spread over a 4 m² capacitor plate gives 5 µC/m².
val plate: KAreaUnitInstance = 0.04 of ares            // 4 m²
val d = (20 of micro.coulombs) / plate                 // 5e-6 C/m²

// The same flux density as the native A·s·m⁻² expression:
val raw = 5e-6 of ((amperes pow 1) * (seconds pow 1)) / (meters pow 2)
raw.toElectricFluxDensity() == d                       // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricfluxdensity.*

val s = (1 of coulombsPerSquareMeter) + (1 of coulombsPerSquareCentimeter)  // 10001 C/m²
(1 of coulombsPerSquareCentimeter) > (1 of coulombsPerSquareMeter)          // true
(2 of coulombsPerSquareMeter) * (3 of coulombsPerSquareMeter)               // KMixedUnitInstance
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricfluxdensity.*

(1 of coulombsPerSquareCentimeter).toString()   // "10000.0 C/m²" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `⁻²`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `C/m²` | `coulombsPerSquareMeter` | electric flux density, base unit (named token) |
| `Q / A` | `(20 of micro.coulombs) / plate` | flux density from charge over an area |
| `ε · E` | `(1 of vacuumPermittivity) * (1 of voltsPerMeter)` | flux density from permittivity and field strength |
| `A·s/m²` | `((amperes pow 1) * (seconds pow 1)) / (meters pow 2)` | flux density as current·time / length² (fraction form) |
| `A·s·m⁻²` | `(amperes pow 1) * (seconds pow 1) * (meters pow -2)` | same flux density as a pure product |
| `µC/m²` | `micro.coulombsPerSquareMeter` | prefixed flux density (microcoulomb per square meter) |
