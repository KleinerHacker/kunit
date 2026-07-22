# Resistance

Package: `org.pcsoft.framework.kunit.resistance`
Base unit: **ohm** (`KResistanceUnit.BASE == KResistanceUnit.OHM`)

Electrical resistance is a **constructed** unit: the composition `mass · length² · time⁻³ · current⁻²`
(`kg·m²·s⁻³·A⁻²`). `KResistanceUnitInstance` wraps a `KMixedUnitInstance` of four terms — `KMassUnit.BASE`
(gram) at `+1`, `KDistanceUnit.BASE` (meter) at `+2`, `KTimeUnit.BASE` (second) at `-3` and
`KElectricCurrentUnit.BASE` (ampere) at `-2`. Because the mass component of the library is normalized to
**grams** (not kilograms), the ohm is 1000× the raw component base; the stored value is normalized to ohms.

## Building a resistance

Build a resistance with a named token, or from a decomposition (see below). Named units survive as
value-1 tokens (used with `of`/`into`):

| Resistance | Symbol | Token | 1 unit in Ω |
|---|---|---:|---:|
| Ohm | `Ω` | `ohms` | 1.0 |
| Statohm (CGS-ESU) | `statΩ` | `statohms` | 8.98755179e11 |
| Abohm (CGS-EMU) | `abΩ` | `abohms` | 1.0e-9 |
| International ohm | `Ω_int` | `internationalOhms` | 1.000049 |
| Legal ohm (1884) | `Ω_leg` | `legalOhms` | 0.9972 |
| Siemens mercury unit | `Ω_S` | `siemensUnits` | 0.9534 |

Named units support the SI prefixes via `KPrefixBuilder` (`kilo.ohms`, `mega.ohms`, `milli.ohms`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.resistance.*

val r = 470 of ohms
r into ohms                  // 470.0
r into kilo.ohms             // 0.47
(1 of kilo.ohms) into ohms   // 1000.0
```

## Multiple decompositions

Resistance can be reached through several **equivalent decompositions**, all producing the same
value-equal resistance:

| Expression | Result type | Meaning |
|---|---|---|
| `voltage / current` | `KResistanceUnitInstance` | Ohm's law `R = U / I` |
| `mass·length²/(time³·current²)` | via `.toResistance()` | native canonical `kg·m²·s⁻³·A⁻²` expression |

The typed operator form returns a resistance directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toResistance()` (which recognises only the canonical normal
form and throws `IllegalStateException` otherwise). Both routes are value-equal.

The inverse Ohm's-law operators tie voltage, resistance and current together:

| Expression | Result type | Meaning |
|---|---|---|
| `resistance * current` | `KVoltageUnitInstance` | `U = R · I` (commutative) |
| `voltage / resistance` | `KElectricCurrentUnitInstance` | `I = U / R` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.resistance.*

// Real-world example - Ohm's law: 230 V across a load drawing 2 A means a 115 Ω resistance.
val r = (230 of volts) / (2 of amperes)  // KResistanceUnitInstance, 115 Ω

// The same resistance as the native kg·m²·s⁻³·A⁻² expression:
val raw = 115 of (kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 3))
raw.toResistance() == (115 of ohms)      // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.resistance.*

val s = (100 of ohms) + (40 of ohms)  // 140 Ω
(100 of ohms) > (40 of ohms)          // true
(100 of ohms) * (40 of ohms)          // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.resistance.*

(470 of ohms).toString()     // "470.0 Ω" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `Ω` | `ohms` | resistance, base unit (named token, ohm) |
| `kg·m²/(s³·A²)` | `kilo.grams * (meters pow 2) / ((amperes pow 2) * (seconds pow 3))` | resistance as mass·length² / (time³·current²) (fraction form) |
| `kg·m²·s⁻³·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -2)` | same resistance as a pure product |
| `kΩ` | `kilo.ohms` | prefixed resistance (kiloohm) |
