# Charge

Package: `org.pcsoft.framework.kunit.electric.charge`
Base unit: **coulomb** (`KChargeUnit.BASE == KChargeUnit.COULOMB`)

Type: **constructed unit**

Electric charge is a **constructed** unit: the composition `current · time` (`A·s`).
`KChargeUnitInstance` wraps a `KMixedUnitInstance` of two terms — `KElectricCurrentUnit.BASE` (ampere) at
`+1` and `KTimeUnit.BASE` (second) at `+1`. The stored value is always normalized to coulombs, no matter
which named unit, SI prefix or current/time combination it was built from.

## Building a charge

Build a charge with a named token, or from a decomposition (see below). Named units survive as value-1
tokens (used with `of`/`into`):

| Charge | Symbol | Token | 1 unit in C |
|---|---|---:|---:|
| Coulomb | `C` | `coulombs` | 1.0 |
| Ampere second | `As` | `ampereSeconds` | 1.0 |
| Ampere hour | `Ah` | `ampereHours` | 3600.0 |
| Abcoulomb (CGS-EMU) | `abC` | `abcoulombs` | 10.0 |
| Statcoulomb (CGS-ESU) | `statC` | `statcoulombs` | 3.335641e-10 |
| Faraday | `F_c` | `faradays` | 96485.332 |
| Elementary charge | `e` | `elementaryCharges` | 1.602176634e-19 |

Named units support the SI prefixes via `KPrefixBuilder` (`kilo.coulombs`, `milli.coulombs`,
`milli.ampereHours`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.charge.*

val q = 470 of coulombs
q into coulombs                        // 470.0
q into kilo.coulombs                   // 0.47
(1 of ampereHours) into coulombs       // 3600.0
(2000 of milli.ampereHours) into coulombs // 7200.0
```

## Multiple decompositions

Charge can be reached through several **equivalent decompositions**, all producing the same value-equal
charge:

| Expression | Result type | Meaning |
|---|---|---|
| `current * time` | `KChargeUnitInstance` | definition `Q = I · t` |
| `time * current` | `KChargeUnitInstance` | commutative form of `Q = I · t` |
| `current / frequency` | `KChargeUnitInstance` | inverse-time form `Q = I / f` (`1/Hz = s`) |
| `current·time` | via `.toCharge()` | native canonical `A·s` expression |

The typed operator forms return a charge directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toCharge()` (which recognises only the canonical normal form —
one `KElectricCurrentUnit` term at `+1` and one `KTimeUnit` term at `+1` — and throws
`IllegalStateException` otherwise). All routes are value-equal.

The inverse operators tie charge, current and time together:

| Expression | Result type | Meaning |
|---|---|---|
| `charge / time` | `KElectricCurrentUnitInstance` | `I = Q / t` |
| `charge / current` | `KTimeUnitInstance` | `t = Q / I` |
| `charge * frequency` | `KElectricCurrentUnitInstance` | `I = Q · f` (inverse-time form) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.charge.*

// Real-world example - battery capacity: a 2000 mAh cell stores 7200 C.
val battery = 2000 of milli.ampereHours   // KChargeUnitInstance, 7200 C

// How long does it last at a constant 250 mA draw?
battery / (0.25 of amperes)               // KTimeUnitInstance, 28800 s (8 h)

// The same charge from the typed decomposition and from the native A·s expression:
val typed = (2 of amperes) * (1 of hours)                  // KChargeUnitInstance, 7200 C
val raw = (2 of amperes).toUnit() * (1 of hours).toUnit()  // KMixedUnitInstance
raw.toCharge() == typed                                    // true
```

## Electric flux

The **electric flux** `Ψ` through a closed surface equals the charge enclosed by it (Gauss's law,
`Ψ = Q`). It is therefore **dimensionally identical** to the charge and is measured in coulombs as well.
KUnit models it with this group and the symbol `C`; there is no separate token and no separate type:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.charge.*

// A sphere enclosing 2 µC carries an electric flux of 2 µC.
val psi = 2 of micro.coulombs
psi into micro.coulombs        // 2.0
```

Divided by an area the flux yields the [electric flux density](electricfluxdensity.md) `D = Ψ / A`.

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.*

val s = (100 of coulombs) + (40 of coulombs)  // 140 C
(100 of coulombs) > (40 of coulombs)          // true
(100 of coulombs) * (40 of coulombs)          // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.*

(470 of coulombs).toString()   // "470.0 C" (base unit)
(1 of ampereHours).toString()  // "3600.0 C" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `C` | `coulombs` | charge, base unit (named token, coulomb) |
| `A·s` | `amperes * seconds` | charge as current·time (product form) |
| `A/Hz` | `amperes / hertz` | same charge written as current divided by a frequency (`1/Hz = s`) |
| `mAh` | `milli.ampereHours` | prefixed charge (milliampere hour, battery capacity) |

## See also

- [Electric Current](ec.md) — the current factor of the charge composition
- [Voltage](voltage.md) — electric potential difference
- [Resistance](resistance.md) — Ohm's law completes the electrical group
