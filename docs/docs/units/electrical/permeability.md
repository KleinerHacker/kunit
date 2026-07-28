# Permeability

Package: `org.pcsoft.framework.kunit.permeability`
Base unit: **henry per meter** (`KPermeabilityUnit.BASE == KPermeabilityUnit.HENRY_PER_METER`)

Type: **constructed unit**

Permeability is a **constructed** unit: the composition `mass · length · time⁻² · current⁻²`
(`kg·m·s⁻²·A⁻²` = `H/m`). `KPermeabilityUnitInstance` wraps a `KMixedUnitInstance` of four terms —
`KMassUnit.BASE` (gram) at `+1`, `KDistanceUnit.BASE` (meter) at `+1`, `KTimeUnit.BASE` (second) at `-2` and
`KElectricCurrentUnit.BASE` (ampere) at `-2`. Because the mass component of the library is normalized to
**grams** (not kilograms), the canonical product is divided by 1000 to reach henries per meter; the stored
value is always normalized to henries per meter.

The permeability `μ` is the magnetic constant of a material: it links the
[Magnetic Flux Density](magneticfluxdensity.md) to the
[Magnetic Field Strength](magneticfieldstrength.md) (`μ = B / H`) and the
[Inductance](inductance.md) to the coil geometry. Its electric counterpart is the
[Permittivity](permittivity.md).

## Building a permeability

Build a permeability with a named token, or from a decomposition (see below). Named units survive as value-1
tokens (used with `of`/`into`):

| Permeability | Symbol | Token | 1 unit in H/m |
|---|---|---:|---:|
| Henry per meter | `H/m` | `henriesPerMeter` | 1.0 |
| Henry per centimeter | `H/cm` | `henriesPerCentimeter` | 100.0 |
| Vacuum permeability `μ₀` | `H/m` | `vacuumPermeability` | 1.25663706127e-6 |

Named units support the SI prefixes via `KPrefixBuilder` (`micro.henriesPerMeter`, `milli.henriesPerMeter`,
…). The constant is also available as `KPermeabilityUnit.VACUUM_PERMEABILITY`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.permeability.*

val mu = 1 of vacuumPermeability      // μ₀
mu into henriesPerMeter               // 1.25663706127e-6
mu into micro.henriesPerMeter         // 1.25663706127
(1 of henriesPerCentimeter) into henriesPerMeter // 100.0
```

## Multiple decompositions

Permeability can be reached through several **equivalent decompositions**, all producing the same value-equal
permeability:

| Expression | Result type | Meaning |
|---|---|---|
| `inductance / length` | `KPermeabilityUnitInstance` | `μ = L · l / (N² · A)`, the geometry factor is a length |
| `magneticFluxDensity / magneticFieldStrength` | `KPermeabilityUnitInstance` | `μ = B / H` |
| `mass·length/(time²·current²)` | via `.toPermeability()` | native canonical `kg·m·s⁻²·A⁻²` expression |

The typed operator forms return a permeability directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toPermeability()` (which recognises only the canonical normal form
and throws `IllegalStateException` otherwise). All routes are value-equal.

The inverse operators tie inductance, length and the two magnetic field quantities together:

| Expression | Result type | Meaning |
|---|---|---|
| `permeability * length` | `KInductanceUnitInstance` | `L = μ · N² · A / l` (commutative) |
| `inductance / permeability` | `KLengthUnitInstance` | the geometry factor `N² · A / l = L / μ` |
| `permeability * magneticFieldStrength` | `KMagneticFluxDensityUnitInstance` | `B = μ · H` (commutative) |
| `magneticFluxDensity / permeability` | `KMagneticFieldStrengthUnitInstance` | `H = B / μ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.inductance.henries
import org.pcsoft.framework.kunit.magneticfieldstrength.amperesPerMeter
import org.pcsoft.framework.kunit.magneticfluxdensity.teslas
import org.pcsoft.framework.kunit.permeability.*

// Real-world example - in vacuum a field of 1000 A/m produces a flux density of 1.257 mT.
val b = (1 of vacuumPermeability) * (1000 of amperesPerMeter)  // 1.25663706127e-3 T

// The definition solved for the permeability:
val mu = (6 of teslas) / (3 of amperesPerMeter)                // 2 H/m
val fromInductance = (10 of henries) / (5 of meters)           // 2 H/m

// The same permeability as the native kg·m·s⁻²·A⁻² expression:
val raw = 2 of (kilo.grams * (meters pow 1)) / ((seconds pow 2) * (amperes pow 2))
raw.toPermeability() == (2 of henriesPerMeter)                 // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.permeability.*

val s = (1 of henriesPerMeter) + (1 of henriesPerCentimeter)  // 101 H/m
(1 of henriesPerCentimeter) > (1 of henriesPerMeter)          // true
(2 of henriesPerMeter) * (3 of henriesPerMeter)               // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.permeability.*

(1 of henriesPerCentimeter).toString()   // "100.0 H/m" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `⁻²`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `H/m` | `henriesPerMeter` | permeability, base unit (named token, henry per meter) |
| `μ₀` | `vacuumPermeability` | the vacuum permeability constant, 1.257 µH/m |
| `B / H` | `(6 of teslas) / (3 of amperesPerMeter)` | permeability as flux density over field strength |
| `L · l / (N²·A)` | `(10 of henries) / (5 of meters)` | permeability from inductance and coil geometry |
| `kg·m/(s²·A²)` | `(kilo.grams * (meters pow 1)) / ((seconds pow 2) * (amperes pow 2))` | permeability as mass·length / (time²·current²) (fraction form) |
| `kg·m·s⁻²·A⁻²` | `kilo.grams * (meters pow 1) * (seconds pow -2) * (amperes pow -2)` | same permeability as a pure product |
| `µH/m` | `micro.henriesPerMeter` | prefixed permeability (microhenry per meter) |
