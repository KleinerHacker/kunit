# Magnetic Reluctance

Package: `org.pcsoft.framework.kunit.reluctance`
Base unit: **ampere per weber** (`KReluctanceUnit.BASE == KReluctanceUnit.AMPERE_PER_WEBER`)

Type: **constructed unit**

Magnetic reluctance is a **constructed** unit: the composition `mass⁻¹ · length⁻² · time² · current²`
(`kg⁻¹·m⁻²·s²·A²` = `A/Wb` = `H⁻¹`). `KReluctanceUnitInstance` wraps a `KMixedUnitInstance` of four terms —
`KMassUnit.BASE` (gram) at `-1`, `KDistanceUnit.BASE` (meter) at `-2`, `KTimeUnit.BASE` (second) at `+2` and
`KElectricCurrentUnit.BASE` (ampere) at `+2`. Because the mass component of the library is normalized to
**grams** (not kilograms) and the mass exponent is negative, the canonical product is multiplied by 1000 to
reach amperes per weber; the stored value is always normalized to amperes per weber.

The reluctance `Rm` is the magnetic circuit's counterpart to the electrical [Resistance](resistance.md): it
relates the magnetomotive force `Θ` (measured in ampere turns, see [Electric Current](ec.md)) to the
resulting [Magnetic Flux](magneticflux.md) through Hopkinson's law `Θ = Rm · Φ`. Its reciprocal is the
**permeance** `Λ`, which is measured in henries and therefore carried by the
[Inductance](inductance.md) group.

## Building a reluctance

Build a reluctance with a named token, or from a decomposition (see below). Named units survive as value-1
tokens (used with `of`/`into`):

| Reluctance | Symbol | Token | 1 unit in A/Wb |
|---|---|---:|---:|
| Ampere per weber | `A/Wb` | `amperesPerWeber` | 1.0 |
| Inverse henry | `H⁻¹` | `inverseHenries` | 1.0 |
| Ampere turn per weber | `At/Wb` | `ampereTurnsPerWeber` | 1.0 |

All three spellings describe the same quantity — the number of coil turns is a pure count — so they are
value-equal; the distinct symbols document the point of view. Named units support the SI prefixes via
`KPrefixBuilder` (`mega.amperesPerWeber`, `kilo.inverseHenries`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.reluctance.*

val rm = 2 of mega.amperesPerWeber    // an air-gapped iron core
rm into mega.amperesPerWeber          // 2.0
rm into amperesPerWeber               // 2.0e6
(1 of amperesPerWeber) == (1 of inverseHenries) // true
```

## Multiple decompositions

Reluctance can be reached through several **equivalent decompositions**, all producing the same value-equal
reluctance:

| Expression | Result type | Meaning |
|---|---|---|
| `current / magneticFlux` | `KReluctanceUnitInstance` | Hopkinson's law `Rm = Θ / Φ` |
| `1 / inductance` | `KReluctanceUnitInstance` | the reciprocal of the permeance, `Rm = 1 / Λ` |
| `(time²·current²)/(mass·length²)` | via `.toReluctance()` | native canonical `kg⁻¹·m⁻²·s²·A²` expression |

The typed operator forms return a reluctance directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toReluctance()` (which recognises only the canonical normal form
and throws `IllegalStateException` otherwise). All routes are value-equal.

The inverse operators tie magnetomotive force, flux, permeance and reluctance together:

| Expression | Result type | Meaning |
|---|---|---|
| `reluctance * magneticFlux` | `KElectricCurrentUnitInstance` | `Θ = Rm · Φ` (commutative) |
| `current / reluctance` | `KMagneticFluxUnitInstance` | `Φ = Θ / Rm` |
| `1 / reluctance` | `KInductanceUnitInstance` | the permeance `Λ = 1 / Rm` (in henries) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.ec.ampereTurns
import org.pcsoft.framework.kunit.magneticflux.webers
import org.pcsoft.framework.kunit.inductance.henries
import org.pcsoft.framework.kunit.reluctance.*

// Real-world example - 2 kAt of magnetomotive force through a 2 MA/Wb core yields 1 mWb of flux.
val rm = 2_000_000 of amperesPerWeber
val flux = (2000 of ampereTurns) / rm       // KMagneticFluxUnitInstance
flux into milli.webers                      // 1.0

// The definition solved for the reluctance:
val fromHopkinson = (6 of amperes) / (3 of webers)   // 2 A/Wb
val fromPermeance = 1 / (0.5 of henries)             // 2 A/Wb

// The same reluctance as the native kg⁻¹·m⁻²·s²·A² expression:
val raw = 2 of ((seconds pow 2) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
raw.toReluctance() == (2 of amperesPerWeber)         // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.reluctance.*

val series = (1 of amperesPerWeber) + (1 of inverseHenries)  // 2 A/Wb (series magnetic circuit)
(3 of amperesPerWeber) > (2 of amperesPerWeber)              // true
(2 of amperesPerWeber) * (3 of amperesPerWeber)              // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.reluctance.*

(2 of inverseHenries).toString()   // "2.0 A/Wb" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `⁻²`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `A/Wb` | `amperesPerWeber` | reluctance, base unit (named token, ampere per weber) |
| `H⁻¹` | `inverseHenries` | the reciprocal-inductance spelling of the same quantity |
| `Θ / Φ` | `(6 of amperes) / (3 of webers)` | reluctance from Hopkinson's law |
| `1 / Λ` | `1 / (0.5 of henries)` | reluctance as the reciprocal of the permeance |
| `(s²·A²)/(kg·m²)` | `((seconds pow 2) * (amperes pow 2)) / (kilo.grams * (meters pow 2))` | reluctance as (time²·current²) / (mass·length²) (fraction form) |
| `kg⁻¹·m⁻²·s²·A²` | `(kilo.grams pow -1) * (meters pow -2) * (seconds pow 2) * (amperes pow 2)` | same reluctance as a pure product |
| `MA/Wb` | `mega.amperesPerWeber` | prefixed reluctance (megaampere per weber) |
