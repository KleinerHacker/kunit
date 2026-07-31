# Magnetic flux density

Package: `org.pcsoft.framework.kunit.electric.magneticfluxdensity`
Base unit: **tesla** (`KMagneticFluxDensityUnit.BASE == KMagneticFluxDensityUnit.TESLA`)

Type: **constructed unit**

Magnetic flux density (magnetic induction `B`) is a **constructed** unit: the composition
`mass · time⁻² · current⁻¹` (`kg·s⁻²·A⁻¹`). `KMagneticFluxDensityUnitInstance` wraps a
`KMixedUnitInstance` of three terms — `KMassUnit.BASE` (gram) at `+1`, `KTimeUnit.BASE` (second) at `-2`
and `KElectricCurrentUnit.BASE` (ampere) at `-1`. Because the mass component of the library is normalized to **grams**
(not kilograms), the tesla is 1000× the raw component base; the stored value is normalized to teslas.

## Building a magnetic flux density

Build a flux density with a named token, or from a decomposition (see below). Named units survive as value-1 tokens
(used with `of`/`into`):

| Flux density           | Symbol  |                  Token | 1 unit in T |
|------------------------|---------|-----------------------:|------------:|
| Tesla                  | `T`     |               `teslas` |         1.0 |
| Weber per square meter | `Wb/m²` | `webersPerSquareMeter` |         1.0 |
| Gauss (CGS-EMU)        | `G`     |                `gauss` |      1.0e-4 |
| Gamma                  | `γ`     |               `gammas` |      1.0e-9 |

Named units support the SI prefixes via `KPrefixBuilder` (`milli.teslas`, `micro.teslas`, `nano.teslas`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

val b = 50 of micro.teslas
b into teslas                 // 5.0e-5
b into gauss                  // 0.5
(1 of teslas) into gammas     // 1.0e9
```

## Multiple decompositions

Magnetic flux density can be reached through several **equivalent decompositions**, all producing the same value-equal
flux density:

| Expression             | Result type                        | Meaning                                  |
|------------------------|------------------------------------|------------------------------------------|
| `flux / area`          | `KMagneticFluxDensityUnitInstance` | definition `B = Φ / A`                   |
| `mass/(time²·current)` | via `.toMagneticFluxDensity()`     | native canonical `kg·s⁻²·A⁻¹` expression |

The typed operator form returns a flux density directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toMagneticFluxDensity()` (which recognises only the canonical normal form and
throws `IllegalStateException` otherwise). Both routes are value-equal.

The inverse operators tie flux, flux density and area together:

| Expression           | Result type                 | Meaning                   |
|----------------------|-----------------------------|---------------------------|
| `fluxDensity * area` | `KMagneticFluxUnitInstance` | `Φ = B · A`               |
| `area * fluxDensity` | `KMagneticFluxUnitInstance` | `Φ = A · B` (commutative) |
| `flux / fluxDensity` | `KAreaUnitInstance`         | `A = Φ / B`               |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.magneticflux.webers
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

// Real-world example - an MRI scanner: 18 Wb of flux through a 6 m² coil is a 3 T field.
val b = (18 of webers) / ((2 of meters) * (3 of meters))  // KMagneticFluxDensityUnitInstance, 3 T

// The same flux density as the native kg·s⁻²·A⁻¹ expression:
val raw = 3 of (kilo.grams / ((seconds pow 2) * (amperes pow 1)))
raw.toMagneticFluxDensity() == (3 of teslas)              // true

// Earth's magnetic field of 50 µT through a 2 m² loop gives 1e-4 Wb of flux.
val flux = (50 of micro.teslas) * ((2 of meters) * (1 of meters))  // KMagneticFluxUnitInstance, 1e-4 Wb
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

val s = (3 of teslas) + (1 of teslas)  // 4 T
(3 of teslas) > (1 of teslas)          // true
(3 of teslas) * (1 of teslas)          // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticfluxdensity.*

(3 of teslas).toString()     // "3.0 T" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics  | Kotlin                                             | Meaning                                                |
|--------------|----------------------------------------------------|--------------------------------------------------------|
| `T`          | `teslas`                                           | flux density, base unit (named token, tesla)           |
| `Wb/m²`      | `webersPerSquareMeter`                             | flux density as flux per area (named token)            |
| `kg/(s²·A)`  | `kilo.grams / ((seconds pow 2) * (amperes pow 1))` | flux density as mass / (time²·current) (fraction form) |
| `kg·s⁻²·A⁻¹` | `kilo.grams * (seconds pow -2) * (amperes pow -1)` | same flux density as a pure product                    |
| `µT`         | `micro.teslas`                                     | prefixed flux density (microtesla)                     |
