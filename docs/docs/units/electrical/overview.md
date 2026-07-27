# Electrical Engineering — Overview

Packages: `org.pcsoft.framework.kunit.ec`, `…voltage`, `…resistance`, `…charge`, `…conductance`,
`…magneticfieldstrength`, `…capacitance`, `…inductance`, `…magneticflux`, `…magneticfluxdensity`,
`…currentdensity`, `…chargedensity`, `…resistivity`, `…conductivity`, `…power`, `…energy`

Electrical engineering ties together the current flowing through a circuit, the voltage driving it, and
the resistance opposing it. These three are bound by **Ohm's law**, and KUnit expresses that law directly
as typed `*` and `/` operators: one **native** base quantity (electric current) and the quantities
**constructed** from the base dimensions (voltage, resistance, charge, conductance and magnetic field
strength).

## Units in this topic

| Unit | Type | Base unit | Page |
|---|---|---|---|
| Electric Current | native | ampere (`A`) | [Electric Current](ec.md) |
| Voltage | constructed | volt (`V`) | [Voltage](voltage.md) |
| Resistance | constructed | ohm (`Ω`) | [Resistance](resistance.md) |
| Charge | constructed | coulomb (`C`) | [Charge](charge.md) |
| Conductance | constructed | siemens (`S`) | [Conductance](conductance.md) |
| Magnetic Field Strength | constructed | ampere per meter (`A/m`) | [Magnetic Field Strength](magneticfieldstrength.md) |
| Capacitance | constructed | farad (`F`) | [Capacitance](capacitance.md) |
| Inductance | constructed | henry (`H`) | [Inductance](inductance.md) |
| Magnetic Flux | constructed | weber (`Wb`) | [Magnetic Flux](magneticflux.md) |
| Magnetic Flux Density | constructed | tesla (`T`) | [Magnetic Flux Density](magneticfluxdensity.md) |
| Current Density | constructed | ampere per square meter (`A/m²`) | [Current Density](currentdensity.md) |
| Charge Density | constructed | coulomb per cubic meter (`C/m³`) | [Charge Density](chargedensity.md) |
| Resistivity | constructed | ohm meter (`Ω·m`) | [Resistivity](resistivity.md) |
| Conductivity | constructed | siemens per meter (`S/m`) | [Conductivity](conductivity.md) |
| Power | constructed | watt (`W`) | [Power (Electrical)](power.md) |
| Energy | constructed | joule (`J`) | [Energy (Electrical)](energy.md) |

Power and energy are technically **one** quantity each, shared with other subject areas; they are documented
per field and cross-reference each other ([Power (Mechanics)](../mechanics/power.md),
[Power (Thermodynamics)](../thermodynamics/power.md), [Energy (Mechanics)](../mechanics/energy.md),
[Energy (Thermodynamics)](../thermodynamics/energy.md)).

## Ohm's law as typed operators

| Expression | Result | Formula |
|---|---|---|
| `resistance * current` | Voltage | `U = R · I` |
| `current * resistance` | Voltage | `U = R · I` (commutative) |
| `voltage / current` | Resistance | `R = U / I` |
| `voltage / resistance` | Electric Current | `I = U / R` |
| `current / voltage` | Conductance | `G = I / U` |
| `1 / resistance` | Conductance | `G = 1 / R` |
| `1 / conductance` | Resistance | `R = 1 / G` |
| `conductance * voltage` | Electric Current | `I = G · U` |
| `current / conductance` | Voltage | `U = I / G` |

## Further typed operators

| Expression | Result | Formula |
|---|---|---|
| `current * time` | Charge | `Q = I · t` |
| `current / frequency` | Charge | `Q = I / f` |
| `charge / time` | Electric Current | `I = Q / t` |
| `charge / current` | Time | `t = Q / I` |
| `current / length` | Magnetic Field Strength | `H = I / l` |
| `field strength * length` | Electric Current | `I = H · l` |
| `charge / voltage` | Capacitance | `C = Q / U` |
| `capacitance * voltage` | Charge | `Q = C · U` |
| `voltage * time` | Magnetic Flux | `Φ = U · t` |
| `flux / time` | Voltage | `U = Φ / t` |
| `flux / current` | Inductance | `L = Φ / I` |
| `inductance * current` | Magnetic Flux | `Φ = L · I` |
| `resistance / frequency` | Inductance | `L = X / ω` |
| `flux / area` | Magnetic Flux Density | `B = Φ / A` |
| `flux density * area` | Magnetic Flux | `Φ = B · A` |
| `current / area` | Current Density | `J = I / A` |
| `current density * area` | Electric Current | `I = J · A` |
| `charge / volume` | Charge Density | `ρ = Q / V` |
| `charge density * volume` | Charge | `Q = ρ · V` |
| `resistance * length` | Resistivity | `ρ = R · A / l` |
| `1 / resistivity` | Conductivity | `σ = 1 / ρ` |
| `1 / conductivity` | Resistivity | `ρ = 1 / σ` |
| `conductance / length` | Conductivity | `σ = G · l / A` |
| `conductivity * length` | Conductance | `G = σ · A / l` |
| `voltage * current` | Power | `P = U · I` |
| `power / voltage` | Electric Current | `I = P / U` |
| `power / current` | Voltage | `U = P / I` |
| `power * time` | Energy | `W = P · t` |
| `energy / time` | Power | `P = W / t` |
| `charge * voltage` | Energy | `W = Q · U` |
| `energy / charge` | Voltage | `U = W / Q` |

Each result is the correct typed quantity — no raw mixed unit is assembled by hand. Voltage, resistance,
charge, conductance and magnetic field strength additionally recognise their fully **native**
decomposition (`kg·m²·s⁻³·A⁻¹`, `kg·m²·s⁻³·A⁻²`, `A·s`, `kg⁻¹·m⁻²·s³·A²`, `A·m⁻¹`) via `toVoltage()` /
`toResistance()` / `toCharge()` / `toConductance()` / `toMagneticFieldStrength()`. The same holds for the
newer groups: `toCapacitance()` (`kg⁻¹·m⁻²·s⁴·A²`), `toInductance()` (`kg·m²·s⁻²·A⁻²`), `toMagneticFlux()`
(`kg·m²·s⁻²·A⁻¹`), `toMagneticFluxDensity()` (`kg·s⁻²·A⁻¹`), `toCurrentDensity()` (`A·m⁻²`),
`toChargeDensity()` (`A·s·m⁻³`), `toResistivity()` (`kg·m³·s⁻³·A⁻²`), `toConductivity()`
(`kg⁻¹·m⁻³·s³·A²`), `toPower()` (`kg·m²·s⁻³`) and `toEnergy()` (`kg·m²·s⁻²`).

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

## Worked example — from mains power to consumed energy

A **230 V** socket feeding a **10 A** load delivers `P = U · I`; running it for three hours consumes
`W = P · t`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.power.*
import org.pcsoft.framework.kunit.energy.*

val p = (230 of volts) * (10 of amperes)  // KPowerUnitInstance
p into kilo.watts                         // 2.3

val w = p * (3 of hours)                  // KEnergyUnitInstance
w into kilo.joules                        // 24840.0
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
* [Charge](charge.md) — the coulomb, `I · t`, and the battery-capacity ampere hour.
* [Conductance](conductance.md) — the siemens, `1 / R`, and `I / U`.
* [Magnetic Field Strength](magneticfieldstrength.md) — ampere per meter, `I / l`, and the oersted.
* [Capacitance](capacitance.md) — the farad, `Q / U`, and the CGS abfarad/statfarad.
* [Inductance](inductance.md) — the henry, `Φ / I`, and the reactance form `X / ω`.
* [Magnetic Flux](magneticflux.md) — the weber, `U · t`, and the maxwell.
* [Magnetic Flux Density](magneticfluxdensity.md) — the tesla, `Φ / A`, and the gauss.
* [Current Density](currentdensity.md) — ampere per square meter, `I / A`, for wire sizing.
* [Charge Density](chargedensity.md) — coulomb per cubic meter, `Q / V`.
* [Resistivity](resistivity.md) — the ohm meter, `R · A / l`, the material property behind a resistance.
* [Conductivity](conductivity.md) — siemens per meter, `1 / ρ`, and `G · l / A`.
* [Power (Electrical)](power.md) — the watt, `U · I`, and the horsepower units.
* [Energy (Electrical)](energy.md) — the joule, `Q · U`, `P · t`, and the kilowatt hour as `kilo.watts * hours`.
