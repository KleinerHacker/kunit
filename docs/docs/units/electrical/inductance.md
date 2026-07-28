# Inductance

Package: `org.pcsoft.framework.kunit.electric.inductance`
Base unit: **henry** (`KInductanceUnit.BASE == KInductanceUnit.HENRY`)

Type: **constructed unit**

Inductance is a **constructed** unit: the composition `mass · length² · time⁻² · current⁻²`
(`kg·m²·s⁻²·A⁻²`). `KInductanceUnitInstance` wraps a `KMixedUnitInstance` of four terms — `KMassUnit.BASE`
(gram) at `+1`, `KDistanceUnit.BASE` (meter) at `+2`, `KTimeUnit.BASE` (second) at `-2` and
`KElectricCurrentUnit.BASE` (ampere) at `-2`. Because the mass component of the library is normalized to
**grams** (not kilograms), the henry is 1000× the raw component base; the stored value is normalized to
henries.

## Building an inductance

Build an inductance with a named token, or from a decomposition (see below). Named units survive as
value-1 tokens (used with `of`/`into`):

| Inductance | Symbol | Token | 1 unit in H |
|---|---|---:|---:|
| Henry | `H` | `henries` | 1.0 |
| Weber per ampere | `Wb/A` | `webersPerAmpere` | 1.0 |
| Abhenry (CGS-EMU) | `abH` | `abhenries` | 1.0e-9 |
| Stathenry (CGS-ESU) | `statH` | `stathenries` | 8.987551787e11 |

Named units support the SI prefixes via `KPrefixBuilder` (`milli.henries`, `micro.henries`,
`nano.henries`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.inductance.*

val l = 470 of micro.henries
l into henries               // 0.00047
l into milli.henries         // 0.47
(1 of henries) into milli.henries  // 1000.0
```

## Multiple decompositions

Inductance can be reached through several **equivalent decompositions**, all producing the same
value-equal inductance:

| Expression | Result type | Meaning |
|---|---|---|
| `flux / current` | `KInductanceUnitInstance` | definition `L = Φ / I` |
| `resistance / frequency` | `KInductanceUnitInstance` | reactance form `L = X / ω` (`Ω/Hz = Ω·s = H`) |
| `mass·length²/(time²·current²)` | via `.toInductance()` | native canonical `kg·m²·s⁻²·A⁻²` expression |

The typed operator forms return an inductance directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toInductance()` (which recognises only the canonical normal
form and throws `IllegalStateException` otherwise). All routes are value-equal.

The inverse operators tie magnetic flux, current, frequency and resistance together:

| Expression | Result type | Meaning |
|---|---|---|
| `inductance * current` | `KMagneticFluxUnitInstance` | `Φ = L · I` (commutative) |
| `flux / inductance` | `KElectricCurrentUnitInstance` | `I = Φ / L` |
| `inductance * frequency` | `KResistanceUnitInstance` | `X = ω · L` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.frequency.hertz
import org.pcsoft.framework.kunit.electric.magneticflux.webers
import org.pcsoft.framework.kunit.electric.resistance.ohms
import org.pcsoft.framework.kunit.electric.inductance.*

// Real-world example - a choke in a switching power supply: a 470 µH coil carrying 2 A links 0.00094 Wb,
// and at an angular frequency of 100 kHz it presents a reactance of 47 Ω.
val l = 470 of micro.henries
val flux = l * (2 of amperes)          // KMagneticFluxUnitInstance, 0.00094 Wb
val x = l * (100_000 of hertz)         // KResistanceUnitInstance, 47 Ω

// The same inductance from its definition and from the reactance form:
(flux / (2 of amperes)) == l           // true
((47 of ohms) / (100_000 of hertz)) == l  // true

// The same inductance as the native kg·m²·s⁻²·A⁻² expression:
val raw = 2 of (kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 2))
raw.toInductance() == (2 of henries)   // true
```

## Permeance

The **permeance** `Λ` of a magnetic circuit is the reciprocal of its
[magnetic reluctance](reluctance.md), `Λ = 1 / Rm`. It is **dimensionally identical** to the inductance
and is measured in henries as well, so KUnit models it with this group and the symbol `H`; there is no
separate token and no separate type. The reciprocal operators tie both groups together:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.inductance.*
import org.pcsoft.framework.kunit.electric.reluctance.*

// A magnetic circuit of Rm = 500 A/Wb has a permeance of 2 mH.
val permeance = 1 / (500 of amperesPerWeber)   // KInductanceUnitInstance
permeance into milli.henries                    // 2.0

// …and back again:
1 / (2 of milli.henries) == (500 of amperesPerWeber)  // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.inductance.*

val s = (100 of henries) + (40 of henries)  // 140 H
(100 of henries) > (40 of henries)          // true
(100 of henries) * (40 of henries)          // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.inductance.*

(2 of henries).toString()     // "2.0 H" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `H` | `henries` | inductance, base unit (named token, henry) |
| `Wb/A` | `webersPerAmpere` | inductance as weber per ampere (named token) |
| `kg·m²/(s²·A²)` | `kilo.grams * (meters pow 2) / ((amperes pow 2) * (seconds pow 2))` | inductance as mass·length² / (time²·current²) (fraction form) |
| `kg·m²·s⁻²·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2) * (amperes pow -2)` | same inductance as a pure product |
| `mH` | `milli.henries` | prefixed inductance (millihenry) |
