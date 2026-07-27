# Capacitance

Package: `org.pcsoft.framework.kunit.capacitance`
Base unit: **farad** (`KCapacitanceUnit.BASE == KCapacitanceUnit.FARAD`)

Type: **constructed unit**

Electrical capacitance is a **constructed** unit: the composition `mass⁻¹ · length⁻² · time⁴ · current²`
(`kg⁻¹·m⁻²·s⁴·A²`). `KCapacitanceUnitInstance` wraps a `KMixedUnitInstance` of four terms — `KMassUnit.BASE`
(gram) at `-1`, `KDistanceUnit.BASE` (meter) at `-2`, `KTimeUnit.BASE` (second) at `+4` and
`KElectricCurrentUnit.BASE` (ampere) at `+2`. Because the mass component of the library is normalized to
**grams** (not kilograms) and the mass exponent is *negative*, the farad is 1000× the raw component base in
the opposite direction; the stored value is normalized to farads.

## Building a capacitance

Build a capacitance with a named token, or from a decomposition (see below). Named units survive as
value-1 tokens (used with `of`/`into`):

| Capacitance | Symbol | Token | 1 unit in F |
|---|---|---:|---:|
| Farad | `F` | `farads` | 1.0 |
| Abfarad (CGS-EMU) | `abF` | `abfarads` | 1.0e9 |
| Statfarad (CGS-ESU) | `statF` | `statfarads` | 1.112650056e-12 |
| Jar (Leyden jar) | `jar` | `jars` | 1.11265e-9 |

Named units support the SI prefixes via `KPrefixBuilder` (`micro.farads`, `nano.farads`, `pico.farads`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.capacitance.*

val c = 470 of micro.farads
c into micro.farads            // 470.0
c into farads                  // 4.7e-4
(1 of milli.farads) into farads // 0.001
```

## Multiple decompositions

Capacitance can be reached through several **equivalent decompositions**, all producing the same
value-equal capacitance:

| Expression | Result type | Meaning |
|---|---|---|
| `charge / voltage` | `KCapacitanceUnitInstance` | definition `C = Q / U` |
| `current²·time⁴/(mass·length²)` | via `.toCapacitance()` | native canonical `kg⁻¹·m⁻²·s⁴·A²` expression |

The typed operator form returns a capacitance directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toCapacitance()` (which recognises only the canonical normal
form and throws `IllegalStateException` otherwise). Both routes are value-equal.

The inverse operators tie charge, voltage and capacitance together:

| Expression | Result type | Meaning |
|---|---|---|
| `capacitance * voltage` | `KChargeUnitInstance` | `Q = C · U` (commutative) |
| `charge / capacitance` | `KVoltageUnitInstance` | `U = Q / C` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.charge.coulombs
import org.pcsoft.framework.kunit.capacitance.*

// Real-world example - a charged capacitor: 470 µF at 12 V stores 5.64 mC.
val q = (470 of micro.farads) * (12 of volts)  // KChargeUnitInstance, 0.00564 C

// The definition solved for the capacitance:
val c = (10 of coulombs) / (5 of volts)        // KCapacitanceUnitInstance, 2 F

// The same capacitance as the native kg⁻¹·m⁻²·s⁴·A² expression:
val raw = 2 of ((amperes pow 2) * (seconds pow 4)) / (kilo.grams * (meters pow 2))
raw.toCapacitance() == (2 of farads)           // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.capacitance.*

val s = (100 of farads) + (40 of farads)  // 140 F
(100 of farads) > (40 of farads)          // true
(100 of farads) * (40 of farads)          // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.capacitance.*

(470 of farads).toString()     // "470.0 F" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `⁴`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `F` | `farads` | capacitance, base unit (named token, farad) |
| `A²·s⁴/(kg·m²)` | `(amperes pow 2) * (seconds pow 4) / (kilo.grams * (meters pow 2))` | capacitance as current²·time⁴ / (mass·length²) (fraction form) |
| `kg⁻¹·m⁻²·s⁴·A²` | `(kilo.grams pow -1) * (meters pow -2) * (seconds pow 4) * (amperes pow 2)` | same capacitance as a pure product |
| `µF` | `micro.farads` | prefixed capacitance (microfarad) |
