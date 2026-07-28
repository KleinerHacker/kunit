# Conductance

Package: `org.pcsoft.framework.kunit.electric.conductance`
Base unit: **siemens** (`KConductanceUnit.BASE == KConductanceUnit.SIEMENS`)

Type: **constructed unit**

Electrical conductance is a **constructed** unit: the composition `mass⁻¹ · length⁻² · time³ · current²`
(`kg⁻¹·m⁻²·s³·A²`). `KConductanceUnitInstance` wraps a `KMixedUnitInstance` of four terms —
`KMassUnit.BASE` (gram) at `-1`, `KDistanceUnit.BASE` (meter) at `-2`, `KTimeUnit.BASE` (second) at `+3`
and `KElectricCurrentUnit.BASE` (ampere) at `+2`. Because the mass component of the library is normalized
to **grams** (not kilograms) and the mass exponent is negative, the siemens is 1/1000× the raw component
base; the stored value is normalized to siemens.

Conductance is the reciprocal of [resistance](resistance.md) (`G = 1 / R`) and ties together
[voltage](voltage.md) and [electric current](ec.md) through Ohm's law.

## Building a conductance

Build a conductance with a named token, or from a decomposition (see below). Named units survive as
value-1 tokens (used with `of`/`into`):

| Conductance | Symbol | Token | 1 unit in S |
|---|---|---:|---:|
| Siemens | `S` | `siemens` | 1.0 |
| Mho (traditional name) | `℧` | `mhos` | 1.0 |
| Abmho (CGS-EMU) | `ab℧` | `abmhos` | 1.0e9 |
| Statmho (CGS-ESU) | `stat℧` | `statmhos` | 1.112650e-12 |

!!! note "`siemens` vs. `siemensUnits`"
    `siemens` (this package) is the SI unit of **conductance**. The similarly named
    `siemensUnits` in `org.pcsoft.framework.kunit.electric.resistance` is the historical **Siemens mercury unit**,
    a *resistance* of 0.9534 Ω. They are unrelated quantities in different packages.

Named units support the SI prefixes via `KPrefixBuilder` (`milli.siemens`, `micro.siemens`,
`kilo.siemens`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.conductance.*

val g = 4 of siemens
g into siemens                    // 4.0
g into milli.siemens              // 4000.0
(1 of milli.siemens) into siemens // 0.001
```

## Multiple decompositions

Conductance can be reached through several **equivalent decompositions**, all producing the same
value-equal conductance:

| Expression | Result type | Meaning |
|---|---|---|
| `current / voltage` | `KConductanceUnitInstance` | Ohm's law `G = I / U` |
| `1 / resistance` | `KConductanceUnitInstance` | reciprocal of a resistance `G = 1 / R` |
| `time³·current²/(mass·length²)` | via `.toConductance()` | native canonical `kg⁻¹·m⁻²·s³·A²` expression |

The typed operator forms return a conductance directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toConductance()` (which recognises only the canonical normal
form and throws `IllegalStateException` otherwise). All routes are value-equal.

The inverse operators tie conductance, voltage and current together:

| Expression | Result type | Meaning |
|---|---|---|
| `conductance * voltage` | `KElectricCurrentUnitInstance` | `I = G · U` (commutative) |
| `current / conductance` | `KVoltageUnitInstance` | `U = I / G` |
| `1 / conductance` | `KResistanceUnitInstance` | `R = 1 / G` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.electric.resistance.ohms
import org.pcsoft.framework.kunit.electric.conductance.*

// Real-world example - conductance of a supply cable: a cable carrying 2 A with a
// measured voltage drop of 1 V has a conductance of 2 S (i.e. a resistance of 0.5 Ω).
val g = (2 of amperes) / (1 of volts)    // KConductanceUnitInstance, 2 S
val r = 1 / g                            // KResistanceUnitInstance, 0.5 Ω

// The reciprocal relation to resistance:
1 / (1 of ohms) == (1 of siemens)        // true

// The same conductance as the native kg⁻¹·m⁻²·s³·A² expression:
val raw = 2 of ((seconds pow 3) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
raw.toConductance() == (2 of siemens)    // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.conductance.*

val s = (100 of siemens) + (40 of siemens)  // 140 S
(100 of siemens) > (40 of siemens)          // true
(100 of siemens) * (40 of siemens)          // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.conductance.*

(4 of siemens).toString()     // "4.0 S" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `S` | `siemens` | conductance, base unit (named token, siemens) |
| `s³·A²/(kg·m²)` | `((seconds pow 3) * (amperes pow 2)) / (kilo.grams * (meters pow 2))` | conductance as time³·current² / (mass·length²) (fraction form) |
| `kg⁻¹·m⁻²·s³·A²` | `(kilo.grams pow -1) * (meters pow -2) * (seconds pow 3) * (amperes pow 2)` | same conductance as a pure product |
| `mS` | `milli.siemens` | prefixed conductance (millisiemens) |
