# Conductivity

Package: `org.pcsoft.framework.kunit.electric.conductivity`
Base unit: **siemens per meter** (`KConductivityUnit.BASE == KConductivityUnit.SIEMENS_PER_METER`)

Type: **constructed unit**

Electrical conductivity is a **constructed** unit: the composition `mass⁻¹ · length⁻³ · time³ · current²`
(`kg⁻¹·m⁻³·s³·A²`). `KConductivityUnitInstance` wraps a `KMixedUnitInstance` of four terms — `KMassUnit.BASE`
(gram) at `-1`, `KDistanceUnit.BASE` (meter) at `-3`, `KTimeUnit.BASE` (second) at `+3` and
`KElectricCurrentUnit.BASE` (ampere) at `+2`. Because the mass component of the library is normalized to
**grams** (not kilograms) and the mass exponent is *negative*, the canonical product is multiplied by 1000 to
reach siemens per meter; the stored value is always normalized to S/m.

Conductivity is the material property behind a conductance and the reciprocal of
[Resistivity](resistivity.md) (`σ = 1 / ρ`).

## Building a conductivity

Build a conductivity with a named token, or from a decomposition (see below). Named units survive as value-1
tokens (used with `of`/`into`):

| Conductivity | Symbol | Token | 1 unit in S/m |
|---|---|---:|---:|
| Siemens per meter | `S/m` | `siemensPerMeter` | 1.0 |
| Siemens per centimeter | `S/cm` | `siemensPerCentimeter` | 100.0 |
| Microsiemens per centimeter | `µS/cm` | `microsiemensPerCentimeter` | 1.0e-4 |
| Megasiemens per meter | `MS/m` | `megasiemensPerMeter` | 1.0e6 |

Named units support the SI prefixes via `KPrefixBuilder` (`mega.siemensPerMeter`, `milli.siemensPerMeter`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.electric.conductivity.*

val sigma = 58 of mega.siemensPerMeter        // copper
sigma into mega.siemensPerMeter               // 58.0
sigma into siemensPerMeter                    // 5.8e7
(1 of siemensPerCentimeter) into siemensPerMeter // 100.0
```

## Multiple decompositions

Conductivity can be reached through several **equivalent decompositions**, all producing the same value-equal
conductivity:

| Expression | Result type | Meaning |
|---|---|---|
| `1 / resistivity` | `KConductivityUnitInstance` | the reciprocal `σ = 1 / ρ` |
| `conductance / length` | `KConductivityUnitInstance` | `σ = G · l / A`; the geometry factor `l / A` is a reciprocal length, hence the division |
| `current²·time³/(mass·length³)` | via `.toConductivity()` | native canonical `kg⁻¹·m⁻³·s³·A²` expression |

The typed operator forms return a conductivity directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toConductivity()` (which recognises only the canonical normal form
and throws `IllegalStateException` otherwise). All routes are value-equal.

The inverse operators tie conductance, length and conductivity together:

| Expression | Result type | Meaning |
|---|---|---|
| `conductivity * length` | `KConductanceUnitInstance` | `G = σ · A / l` (commutative) |
| `conductance / conductivity` | `KLengthUnitInstance` | the geometry factor `A / l = G / σ` |
| `1 / conductivity` | `KResistivityUnitInstance` | back to the resistivity |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.conductance.siemens
import org.pcsoft.framework.kunit.electric.resistivity.ohmMeters
import org.pcsoft.framework.kunit.electric.conductivity.*

// Real-world example - copper: a resistivity of 17 nΩ·m is a conductivity of about 58.8 MS/m.
val sigma = 1 / (17 of nano.ohmMeters)
sigma into mega.siemensPerMeter               // 58.82352941176471

// Conductance over the conductor geometry:
val fromConductance = (10 of siemens) / (5 of meters)  // KConductivityUnitInstance, 2 S/m

// The same conductivity as the native kg⁻¹·m⁻³·s³·A² expression:
val raw = 2 of ((amperes pow 2) * (seconds pow 3)) / (kilo.grams * (meters pow 3))
raw.toConductivity() == (2 of siemensPerMeter) // true

// The reciprocal pair is symmetric:
1 / (2 of siemensPerMeter) into ohmMeters      // 0.5
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.conductivity.*

val s = (100 of siemensPerMeter) + (40 of siemensPerMeter)  // 140 S/m
(100 of siemensPerMeter) > (40 of siemensPerMeter)          // true
(100 of siemensPerMeter) * (40 of siemensPerMeter)          // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.conductivity.*

(1 of siemensPerCentimeter).toString()   // "100.0 S/m" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `S/m` | `siemensPerMeter` | conductivity, base unit (named token, siemens per meter) |
| `1 / ρ` | `1 / (17 of nano.ohmMeters)` | conductivity as the reciprocal resistivity |
| `A²·s³/(kg·m³)` | `((amperes pow 2) * (seconds pow 3)) / (kilo.grams * (meters pow 3))` | conductivity as current²·time³ / (mass·length³) (fraction form) |
| `kg⁻¹·m⁻³·s³·A²` | `(kilo.grams pow -1) * (meters pow -3) * (seconds pow 3) * (amperes pow 2)` | same conductivity as a pure product |
| `MS/m` | `mega.siemensPerMeter` | prefixed conductivity (megasiemens per meter) |
