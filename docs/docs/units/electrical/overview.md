# Electrical Engineering — Overview

Packages: `org.pcsoft.framework.kunit.ec`, `…voltage`, `…resistance`

Electrical engineering ties together the current flowing through a circuit, the voltage driving it, and
the resistance opposing it. These three are bound by **Ohm's law**, and KUnit expresses that law directly
as typed `*` and `/` operators: one **native** base quantity (electric current) and two quantities
**constructed** from the base dimensions (voltage and resistance).

## Units in this topic

| Unit | Type | Base unit | Page |
|---|---|---|---|
| Electric Current | native | ampere (`A`) | [Electric Current](ec.md) |
| Voltage | constructed | volt (`V`) | [Voltage](voltage.md) |
| Resistance | constructed | ohm (`Ω`) | [Resistance](resistance.md) |

## Ohm's law as typed operators

| Expression | Result | Formula |
|---|---|---|
| `resistance * current` | Voltage | `U = R · I` |
| `current * resistance` | Voltage | `U = R · I` (commutative) |
| `voltage / current` | Resistance | `R = U / I` |
| `voltage / resistance` | Electric Current | `I = U / R` |

Each result is the correct typed quantity — no raw mixed unit is assembled by hand. Voltage and resistance
additionally recognise their fully **native** decomposition (`kg·m²·s⁻³·A⁻¹` and `kg·m²·s⁻³·A⁻²`) via
`toVoltage()` / `toResistance()`.

## Worked example — Ohm's law around one circuit

A load drops **230 V** while drawing **2 A**. Its resistance is `R = U / I`; feeding that resistance back
with the current reproduces the voltage `U = R · I`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.resistance.*

val r = (230 of volts) / (2 of amperes)   // KResistanceUnitInstance, 115 Ω
r into ohms                               // 115.0

val u = r * (2 of amperes)                // KVoltageUnitInstance
u into volts                              // 230.0

val i = (230 of volts) / (115 of ohms)    // KElectricCurrentUnitInstance
i into amperes                            // 2.0
```

## Printing a value (`toString`)

`toString()` renders a value in its group's **base unit** (value + symbol); for any other unit, read it
with `into` inside a string template and append the symbol yourself:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.voltage.*

val u = 230 of volts
u.toString()               // "230.0 V" (base unit)
"${u into kilo.volts} kV"  // "0.23 kV"
```

## Notation

The table shows Ohm's law mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts
(`²`, `⁻¹`), `·` denotes multiplication and `/` a fraction.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `R = U / I` | `(230 of volts) / (2 of amperes)` | resistance from voltage ÷ current |
| `U = R · I` | `r * (2 of amperes)` | voltage from resistance × current |
| `I = U / R` | `(230 of volts) / (115 of ohms)` | current from voltage ÷ resistance |
| `Ω = kg·m²·s⁻³·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -2)` | resistance as its native normal form |

## Where to go next

* [Electric Current](ec.md) — the native ampere group (plus the CGS biot and statampere).
* [Voltage](voltage.md) — the volt, and its decompositions `R · I` and the native form.
* [Resistance](resistance.md) — the ohm, `U / I`, and the inverse Ohm's-law operators.
