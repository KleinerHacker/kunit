# Resistivity

Package: `org.pcsoft.framework.kunit.resistivity`
Base unit: **ohm meter** (`KResistivityUnit.BASE == KResistivityUnit.OHM_METER`)

Type: **constructed unit**

Electrical resistivity is a **constructed** unit: the composition `mass · length³ · time⁻³ · current⁻²`
(`kg·m³·s⁻³·A⁻²`). `KResistivityUnitInstance` wraps a `KMixedUnitInstance` of four terms — `KMassUnit.BASE`
(gram) at `+1`, `KDistanceUnit.BASE` (meter) at `+3`, `KTimeUnit.BASE` (second) at `-3` and
`KElectricCurrentUnit.BASE` (ampere) at `-2`. Because the mass component of the library is normalized to
**grams** (not kilograms), the canonical product is divided by 1000 to reach ohm meters; the stored value is
always normalized to ohm meters.

Resistivity is the material property behind a resistance and the reciprocal of
[Conductivity](conductivity.md) (`ρ = 1 / σ`).

## Building a resistivity

Build a resistivity with a named token, or from a decomposition (see below). Named units survive as value-1
tokens (used with `of`/`into`):

| Resistivity | Symbol | Token | 1 unit in Ω·m |
|---|---|---:|---:|
| Ohm meter | `Ω·m` | `ohmMeters` | 1.0 |
| Ohm centimeter | `Ω·cm` | `ohmCentimeters` | 0.01 |
| Statohm centimeter (CGS-ESU) | `statΩ·cm` | `statohmCentimeters` | 8.98755179e9 |

Named units support the SI prefixes via `KPrefixBuilder` (`nano.ohmMeters`, `micro.ohmMeters`,
`milli.ohmCentimeters`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.resistivity.*

val rho = 17 of nano.ohmMeters     // copper
rho into nano.ohmMeters            // 17.0
rho into ohmMeters                 // 1.7e-8
(1 of ohmMeters) into ohmCentimeters // 100.0
```

## Multiple decompositions

Resistivity can be reached through several **equivalent decompositions**, all producing the same value-equal
resistivity:

| Expression | Result type | Meaning |
|---|---|---|
| `resistance * length` | `KResistivityUnitInstance` | `ρ = R · A / l`, the geometry factor `A / l` is a length (commutative) |
| `1 / conductivity` | `KResistivityUnitInstance` | the reciprocal `ρ = 1 / σ` |
| `mass·length³/(time³·current²)` | via `.toResistivity()` | native canonical `kg·m³·s⁻³·A⁻²` expression |

The typed operator forms return a resistivity directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toResistivity()` (which recognises only the canonical normal form
and throws `IllegalStateException` otherwise). All routes are value-equal.

The inverse operators tie resistance, length and resistivity together:

| Expression | Result type | Meaning |
|---|---|---|
| `resistivity / length` | `KResistanceUnitInstance` | `R = ρ · l / A` |
| `resistivity / resistance` | `KLengthUnitInstance` | the geometry factor `A / l = ρ / R` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.resistance.ohms
import org.pcsoft.framework.kunit.resistivity.*

// Real-world example - copper wiring: 17 nΩ·m over a 1 mm geometry factor gives 17 µΩ.
val r = (17 of nano.ohmMeters) / (1 of milli.meters)  // KResistanceUnitInstance, 1.7e-5 Ω

// The definition solved for the resistivity:
val rho = (5 of ohms) * (0.4 of meters)               // KResistivityUnitInstance, 2 Ω·m

// The same resistivity as the native kg·m³·s⁻³·A⁻² expression:
val raw = 2 of (kilo.grams * (meters pow 3)) / ((seconds pow 3) * (amperes pow 2))
raw.toResistivity() == (2 of ohmMeters)               // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.resistivity.*

val s = (100 of ohmMeters) + (40 of ohmMeters)  // 140 Ω·m
(100 of ohmMeters) > (40 of ohmMeters)          // true
(100 of ohmMeters) * (40 of ohmMeters)          // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.resistivity.*

(1 of ohmCentimeters).toString()   // "0.01 Ω·m" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`³`, `⁻²`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `Ω·m` | `ohmMeters` | resistivity, base unit (named token, ohm meter) |
| `R · (A/l)` | `(5 of ohms) * (0.4 of meters)` | resistivity from resistance and geometry factor |
| `kg·m³/(s³·A²)` | `(kilo.grams * (meters pow 3)) / ((seconds pow 3) * (amperes pow 2))` | resistivity as mass·length³ / (time³·current²) (fraction form) |
| `kg·m³·s⁻³·A⁻²` | `kilo.grams * (meters pow 3) * (seconds pow -3) * (amperes pow -2)` | same resistivity as a pure product |
| `nΩ·m` | `nano.ohmMeters` | prefixed resistivity (nanoohm meter) |
