# Magnetic Flux

Package: `org.pcsoft.framework.kunit.electric.magneticflux`
Base unit: **weber** (`KMagneticFluxUnit.BASE == KMagneticFluxUnit.WEBER`)

Type: **constructed unit**

Magnetic flux is a **constructed** unit: the composition `mass · length² · time⁻² · current⁻¹`
(`kg·m²·s⁻²·A⁻¹`). `KMagneticFluxUnitInstance` wraps a `KMixedUnitInstance` of four terms — `KMassUnit.BASE`
(gram) at `+1`, `KDistanceUnit.BASE` (meter) at `+2`, `KTimeUnit.BASE` (second) at `-2` and
`KElectricCurrentUnit.BASE` (ampere) at `-1`. Because the mass component of the library is normalized to
**grams** (not kilograms), the canonical product is divided by 1000 to reach webers; the stored value is
always normalized to webers.

## Building a magnetic flux

Build a flux with a named token, or from a decomposition (see below). Named units survive as value-1 tokens
(used with `of`/`into`):

| Magnetic flux | Symbol | Token | 1 unit in Wb |
|---|---|---:|---:|
| Weber | `Wb` | `webers` | 1.0 |
| Maxwell (CGS-EMU) | `Mx` | `maxwells` | 1.0e-8 |
| Unit pole | `pole` | `unitPoles` | 1.2566370614359173e-7 |

Named units support the SI prefixes via `KPrefixBuilder` (`milli.webers`, `micro.webers`, `kilo.maxwells`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.magneticflux.*

val phi = 20 of milli.webers
phi into milli.webers          // 20.0
phi into webers                // 0.02
(1 of webers) into maxwells    // 1.0e8
```

## Multiple decompositions

Magnetic flux can be reached through several **equivalent decompositions**, all producing the same
value-equal flux:

| Expression | Result type | Meaning |
|---|---|---|
| `voltage * time` | `KMagneticFluxUnitInstance` | Faraday's induction law `Φ = U · t` (commutative) |
| `voltage / frequency` | `KMagneticFluxUnitInstance` | the inverse-time form (`V/Hz = V·s`) |
| `inductance * current` | `KMagneticFluxUnitInstance` | `Φ = L · I` (see [Inductance](inductance.md)) |
| `fluxDensity * area` | `KMagneticFluxUnitInstance` | `Φ = B · A` (see [Magnetic Flux Density](magneticfluxdensity.md)) |
| `mass·length²/(time²·current)` | via `.toMagneticFlux()` | native canonical `kg·m²·s⁻²·A⁻¹` expression |

The typed operator forms return a flux directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toMagneticFlux()` (which recognises only the canonical normal form
and throws `IllegalStateException` otherwise). All routes are value-equal.

The inverse operators tie voltage, time and flux together:

| Expression | Result type | Meaning |
|---|---|---|
| `flux / time` | `KVoltageUnitInstance` | induced voltage `U = Φ / t` |
| `flux * frequency` | `KVoltageUnitInstance` | the inverse-time counterpart |
| `flux / voltage` | `KTimeUnitInstance` | `t = Φ / U` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.frequency.hertz
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.electric.magneticflux.*

// Real-world example - an ignition coil: a 20 mWb core flux collapsing within 4 ms induces 5 V.
val u = (20 of milli.webers) / (4 of milli.seconds)   // KVoltageUnitInstance, 5 V

// The induction law solved for the flux:
val phi = (10 of volts) * (0.2 of seconds)            // KMagneticFluxUnitInstance, 2 Wb

// The same flux from a frequency, and as the native kg·m²·s⁻²·A⁻¹ expression:
val fromFrequency = (10 of volts) / (5 of hertz)      // 2 Wb
val raw = 2 of (kilo.grams * (meters pow 2)) / ((seconds pow 2) * (amperes pow 1))
raw.toMagneticFlux() == (2 of webers)                 // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticflux.*

val s = (100 of webers) + (40 of webers)  // 140 Wb
(100 of webers) > (40 of webers)          // true
(100 of webers) * (40 of webers)          // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticflux.*

(20 of webers).toString()     // "20.0 Wb" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `Wb` | `webers` | magnetic flux, base unit (named token, weber) |
| `V·s` | `(10 of volts) * (0.2 of seconds)` | flux as voltage·time (induction law) |
| `kg·m²/(s²·A)` | `(kilo.grams * (meters pow 2)) / ((seconds pow 2) * (amperes pow 1))` | flux as mass·length² / (time²·current) (fraction form) |
| `kg·m²·s⁻²·A⁻¹` | `kilo.grams * (meters pow 2) * (seconds pow -2) * (amperes pow -1)` | same flux as a pure product |
| `mWb` | `milli.webers` | prefixed flux (milliweber) |
