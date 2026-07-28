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
| Electric Field Strength | constructed | volt per meter (`V/m`) | [Electric Field Strength](electricfieldstrength.md) |
| Electric Flux Density | constructed | coulomb per square meter (`C/m²`) | [Electric Flux Density](electricfluxdensity.md) |
| Permittivity | constructed | farad per meter (`F/m`) | [Permittivity](permittivity.md) |
| Permeability | constructed | henry per meter (`H/m`) | [Permeability](permeability.md) |
| Linear Charge Density | constructed | coulomb per meter (`C/m`) | [Linear Charge Density](linearchargedensity.md) |
| Magnetic Reluctance | constructed | ampere per weber (`A/Wb`) | [Magnetic Reluctance](reluctance.md) |
| Electric Mobility | constructed | square meter per volt second (`m²/(V·s)`) | [Electric Mobility](electricmobility.md) |
| Electric Dipole Moment | constructed | coulomb meter (`C·m`) | [Electric Dipole Moment](electricdipolemoment.md) |
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
| `voltage / length` | Electric Field Strength | `E = U / l` |
| `force / charge` | Electric Field Strength | `E = F / Q` |
| `field strength * length` | Voltage | `U = E · l` |
| `field strength * charge` | Force | `F = E · Q` |
| `charge / area` | Electric Flux Density | `D = Q / A` |
| `flux density * area` | Charge | `Q = D · A` |
| `flux density / field strength` | Permittivity | `ε = D / E` |
| `permittivity * field strength` | Electric Flux Density | `D = ε · E` |
| `capacitance / length` | Permittivity | `ε = C · d / A` |
| `permittivity * length` | Capacitance | `C = ε · A / d` |
| `magnetic flux density / magnetic field strength` | Permeability | `μ = B / H` |
| `permeability * magnetic field strength` | Magnetic Flux Density | `B = μ · H` |
| `inductance / length` | Permeability | `μ = L · l / (N² · A)` |
| `permeability * length` | Inductance | `L = μ · N² · A / l` |
| `charge / length` | Linear Charge Density | `λ = Q / l` |
| `linear charge density * length` | Charge | `Q = λ · l` |
| `current / magnetic flux` | Magnetic Reluctance | `Rm = Θ / Φ` |
| `reluctance * magnetic flux` | Electric Current | `Θ = Rm · Φ` |
| `1 / inductance` | Magnetic Reluctance | `Rm = 1 / Λ` |
| `1 / reluctance` | Inductance | `Λ = 1 / Rm` |
| `speed / field strength` | Electric Mobility | `μ = v / E` |
| `mobility * field strength` | Speed | `v = μ · E` |
| `charge * length` | Electric Dipole Moment | `p = Q · d` |
| `dipole moment / charge` | Length | `d = p / Q` |

Each result is the correct typed quantity — no raw mixed unit is assembled by hand. Voltage, resistance,
charge, conductance and magnetic field strength additionally recognise their fully **native**
decomposition (`kg·m²·s⁻³·A⁻¹`, `kg·m²·s⁻³·A⁻²`, `A·s`, `kg⁻¹·m⁻²·s³·A²`, `A·m⁻¹`) via `toVoltage()` /
`toResistance()` / `toCharge()` / `toConductance()` / `toMagneticFieldStrength()`. The same holds for the
newer groups: `toCapacitance()` (`kg⁻¹·m⁻²·s⁴·A²`), `toInductance()` (`kg·m²·s⁻²·A⁻²`), `toMagneticFlux()`
(`kg·m²·s⁻²·A⁻¹`), `toMagneticFluxDensity()` (`kg·s⁻²·A⁻¹`), `toCurrentDensity()` (`A·m⁻²`),
`toChargeDensity()` (`A·s·m⁻³`), `toResistivity()` (`kg·m³·s⁻³·A⁻²`), `toConductivity()`
(`kg⁻¹·m⁻³·s³·A²`), `toPower()` (`kg·m²·s⁻³`) and `toEnergy()` (`kg·m²·s⁻²`). The field, material and
magnetic-circuit groups follow the same pattern: `toElectricFieldStrength()` (`kg·m·s⁻³·A⁻¹`),
`toElectricFluxDensity()` (`A·s·m⁻²`), `toPermittivity()` (`kg⁻¹·m⁻³·s⁴·A²`), `toPermeability()`
(`kg·m·s⁻²·A⁻²`), `toLinearChargeDensity()` (`A·s·m⁻¹`), `toReluctance()` (`kg⁻¹·m⁻²·s²·A²`),
`toElectricMobility()` (`kg⁻¹·s²·A`) and `toElectricDipoleMoment()` (`A·s·m`).

Some quantities are **dimensionally identical** to an existing group and are therefore carried by that
group rather than by one of their own — only the symbol differs to document the reading:

| Quantity | Group | Symbol |
|---|---|---|
| Impedance `Z`, reactance `X` | [Resistance](resistance.md) | `Ω` |
| Admittance `Y`, susceptance `B` | [Conductance](conductance.md) | `S` (`℧`) |
| Apparent power `S`, reactive power `Q` | [Power (Electrical)](power.md) | `VA`, `var` |
| Magnetomotive force `Θ` | [Electric Current](ec.md) | `At` |
| Electric flux `Ψ` | [Charge](charge.md) | `C` |
| Permeance `Λ` | [Inductance](inductance.md) | `H` |
| Surface charge density `σ` | [Electric Flux Density](electricfluxdensity.md) | `C/m²` |

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
* [Electric Field Strength](electricfieldstrength.md) — volt per meter, `U / l`, and `F / Q`.
* [Electric Flux Density](electricfluxdensity.md) — coulomb per square meter, `Q / A`, also the surface charge density `σ`.
* [Permittivity](permittivity.md) — farad per meter, `D / E`, and the vacuum constant `ε₀`.
* [Permeability](permeability.md) — henry per meter, `B / H`, and the vacuum constant `μ₀`.
* [Linear Charge Density](linearchargedensity.md) — coulomb per meter, `Q / l`, for wires and filaments.
* [Magnetic Reluctance](reluctance.md) — ampere per weber, Hopkinson's law `Θ / Φ`, and the permeance `1 / Λ`.
* [Electric Mobility](electricmobility.md) — square meter per volt second, `v / E`, for semiconductors.
* [Electric Dipole Moment](electricdipolemoment.md) — coulomb meter, `Q · d`, and the debye.
