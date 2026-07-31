# Thermodynamics — Overview

Packages: `org.pcsoft.framework.kunit.thermo.*`, plus `org.pcsoft.framework.kunit.common.energy`
and `…common.power`

Thermodynamics is the physics of **heat and temperature**. In KUnit the field centres on temperature, which is modelled
by **two related native groups** — because a temperature *reading* and a temperature *change* are physically different
kinds of quantity, and keeping them apart is what makes the arithmetic correct. Around them sit the two **constructed**
quantities of every heat balance: the heat itself (energy) and the rate at which it flows (power).

## Units in this topic

| Unit                      | Type        | Nature                        | Base unit     | Page                                                      |
|---------------------------|-------------|-------------------------------|---------------|-----------------------------------------------------------|
| Absolute Temperature      | native      | affine **point**              | kelvin (`K`)  | [Absolute Temperature](temperature.md)                    |
| Temperature Difference    | native      | linear **interval**           | kelvin (`ΔK`) | [Temperature Difference](temperature-difference.md)       |
| Amount of Substance       | native      | linear quantity               | mole (`mol`)  | [Amount of Substance](amount-of-substance.md)             |
| Energy                    | constructed | linear quantity               | joule (`J`)   | [Energy (Thermodynamics)](energy.md)                      |
| Power                     | constructed | linear quantity               | watt (`W`)    | [Power (Thermodynamics)](power.md)                        |
| Heat Flow                 | constructed | shares the power type         | watt (`W`)    | [Heat Flow](heat-flow.md)                                 |
| Heat Capacity             | constructed | linear quantity               | `J/K`         | [Heat Capacity](heat-capacity.md)                         |
| Entropy                   | constructed | shares the heat capacity type | `J/K`         | [Entropy](entropy.md)                                     |
| Specific Heat Capacity    | constructed | linear quantity               | `J/(kg·K)`    | [Specific Heat Capacity](specific-heat-capacity.md)       |
| Molar Heat Capacity       | constructed | linear quantity               | `J/(mol·K)`   | [Molar Heat Capacity](molar-heat-capacity.md)             |
| Specific Energy           | constructed | linear quantity               | `J/kg`        | [Specific Energy](specific-energy.md)                     |
| Molar Energy              | constructed | linear quantity               | `J/mol`       | [Molar Energy](molar-energy.md)                           |
| Heat Flux Density         | constructed | linear quantity               | `W/m²`        | [Heat Flux Density](heat-flux-density.md)                 |
| Thermal Conductivity      | constructed | linear quantity               | `W/(m·K)`     | [Thermal Conductivity](thermal-conductivity.md)           |
| Heat Transfer Coefficient | constructed | linear quantity               | `W/(m²·K)`    | [Heat Transfer Coefficient](heat-transfer-coefficient.md) |
| Thermal Resistance        | constructed | linear quantity               | `m²·K/W`      | [Thermal Resistance](thermal-resistance.md)               |
| Thermal Expansion         | constructed | linear quantity               | `1/K`         | [Thermal Expansion](thermal-expansion.md)                 |
| Temperature Gradient      | constructed | linear quantity               | `K/m`         | [Temperature Gradient](temperature-gradient.md)           |
| Thermal Diffusivity       | constructed | linear quantity               | `m²/s`        | [Thermal Diffusivity](thermal-diffusivity.md)             |

Two entries deliberately **share** an existing type instead of getting one of their own: entropy is dimensionally
identical to heat capacity, and heat flow is identical to power. A canonical base-dimension normal form must map to
exactly one type, otherwise the `toX()` form recognition would be ambiguous — see the [Entropy](entropy.md)
and [Heat Flow](heat-flow.md) pages for the reasoning.

Energy (heat) and power (heat flow rate) are technically **one** quantity each, shared with other subject areas; they
are documented per field and cross-reference each other
([Energy (Electrical)](../electrical/energy.md), [Energy (Mechanics)](../mechanics/energy.md),
[Power (Electrical)](../electrical/power.md), [Power (Mechanics)](../mechanics/power.md)).

A dedicated [Temperature Overview](temperature-overview.md) explains the point-vs-interval distinction in depth; this
page is the entry point for the whole thermodynamics field.

## Point vs. interval — the operator rules

| Operation                 | Result                                     |
|---------------------------|--------------------------------------------|
| `AbsTemp − AbsTemp`       | **Temperature Difference**                 |
| `AbsTemp + Difference`    | Absolute Temperature                       |
| `AbsTemp − Difference`    | Absolute Temperature                       |
| `Difference ± Difference` | Temperature Difference                     |
| `AbsTemp + AbsTemp`       | **compile error** (physically meaningless) |

## Heat and heat flow as typed operators

| Expression          | Result            | Formula     |
|---------------------|-------------------|-------------|
| `power * time`      | Energy (heat)     | `Q = Φ · t` |
| `energy / time`     | Power (heat flow) | `Φ = Q / t` |
| `energy / power`    | Time              | `t = Q / Φ` |
| `power / frequency` | Energy            | `Q = Φ / f` |

## The heat-transfer chain

The derived groups form one connected chain from material property to total heat loss. Each step is a typed operator, so
no raw `KMixedUnitInstance` ever appears:

| Expression                                  | Result                    | Formula                |
|---------------------------------------------|---------------------------|------------------------|
| `temperatureDifference / length`            | Temperature Gradient      | `∇T = ΔT / d`          |
| `thermalConductivity * temperatureGradient` | Heat Flux Density         | `q̇ = λ · ∇T` (Fourier) |
| `thermalConductivity / length`              | Heat Transfer Coefficient | `U = λ / d`            |
| `length / thermalConductivity`              | Thermal Resistance        | `R = d / λ`            |
| `1 / heatTransferCoefficient`               | Thermal Resistance        | `R = 1 / U`            |
| `heatFluxDensity * area`                    | Power (heat flow)         | `Φ = q̇ · A`            |
| `energy / temperatureDifference`            | Heat Capacity             | `C = Q / ΔT`           |
| `heatCapacity / mass`                       | Specific Heat Capacity    | `c = C / m`            |
| `heatCapacity / amountOfSubstance`          | Molar Heat Capacity       | `C_m = C / n`          |
| `energy / mass`                             | Specific Energy           | `q = Q / m`            |
| `energy / amountOfSubstance`                | Molar Energy              | `ΔH_m = Q / n`         |

Thermal diffusivity is the one **ternary** relation (`α = λ / (ρ · c_p)`); it is exposed as the named functions
`diffusivityWith` / `conductivityWith` rather than as a binary operator, because the intermediate volumetric heat
capacity `ρ · c_p` is not a modelled unit.

## Worked example — a heating step

Water is heated from **10 °C** to **30 °C**. The *change* is a temperature **difference** (`ΔT`), which is the quantity
that enters heat formulas such as `Q = m · c · ΔT`; the zero-point cancels, so `°C` and `K`
agree on the step size:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperature.*

val start = 10 of celsius
val end   = 30 of celsius

val deltaT = end - start                     // KTemperatureDifferenceUnitInstance: 20 ΔK
deltaT.value                                 // 20.0 (kelvin interval)

val back = start + KTemperatureDifference.ofKelvin(20) // KTemperatureUnitInstance: 303.15 K
```

## Worked example — heat and heating time of a boiler

A **2 kW** boiler runs for **10 minutes**. The heat delivered is `Q = Φ · t`; dividing it back by the heat flow returns
the heating time:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.common.power.*
import org.pcsoft.framework.kunit.common.energy.*

val q = (2 of kilo.watts) * (10 of minutes)   // KEnergyUnitInstance
q into kilo.joules                            // 1200.0
q into kilo.calories                          // ≈ 286.8 (kcal)

val t = q / (2 of kilo.watts)                 // KTimeUnitInstance
t into seconds                                // 600.0
```

## Printing a value (`toString`)

`toString()` renders a value in its group's **base unit** (kelvin): an absolute temperature prints as
`K`, a difference as the distinct `ΔK` symbol:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperature.*

(25 of celsius).toString()                       // "298.15 K" (absolute, base unit)
KTemperatureDifference.ofKelvin(20).toString()   // "20.0 ΔK" (interval)
```

## Notation

The table shows the temperature relations mathematically versus in Kotlin with KUnit. `Δ` marks an interval quantity,
deliberately distinct from an absolute point.

| Mathematics     | Kotlin                                                                      | Meaning                                     |
|-----------------|-----------------------------------------------------------------------------|---------------------------------------------|
| `ΔT = T₂ − T₁`  | `(30 of celsius) - (10 of celsius)`                                         | difference from two absolute temperatures   |
| `T + ΔT`        | `(10 of celsius) + KTemperatureDifference.ofKelvin(20)`                     | absolute temperature shifted by an interval |
| `ΔK`            | `KTemperatureDifference.ofKelvin(20)`                                       | an explicit temperature interval            |
| `20 ΔK + 10 ΔK` | `KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10)` | sum of two intervals                        |
| `Q = Φ · t`     | `(2 of kilo.watts) * (10 of minutes)`                                       | heat from heat flow × time                  |
| `Φ = Q / t`     | `(1200 of kilo.joules) / (10 of minutes)`                                   | heat flow from heat ÷ time                  |

## Where to go next

* [Temperature Overview](temperature-overview.md) — the full point-vs-interval discussion and why it matters physically
  (heat energy, radiation, the ideal-gas law).
* [Absolute Temperature](temperature.md) — Kelvin, Celsius, Fahrenheit, Rankine and the affine operators.
* [Temperature Difference](temperature-difference.md) — the linear kelvin interval group.
* [Energy (Thermodynamics)](energy.md) — the joule as heat, plus the calorie and the BTU.
* [Power (Thermodynamics)](power.md) — the watt as heat flow rate, `Q / t`.
* [Amount of Substance](amount-of-substance.md) — the mole, the basis of every molar quantity.

### Storing heat

* [Heat Capacity](heat-capacity.md) — `J/K`, the energy an object absorbs per kelvin.
* [Entropy](entropy.md) — the same `J/K` type, read as `ΔS = Q / T`.
* [Specific Heat Capacity](specific-heat-capacity.md) — `J/(kg·K)`, the material property.
* [Molar Heat Capacity](molar-heat-capacity.md) — `J/(mol·K)`, plus the gas constant `R`.
* [Specific Energy](specific-energy.md) — `J/kg`, also latent heat and calorific value.
* [Molar Energy](molar-energy.md) — `J/mol`, reaction and formation enthalpies.

### Moving heat

* [Heat Flow](heat-flow.md) — the watt read as thermal power.
* [Heat Flux Density](heat-flux-density.md) — `W/m²`, also irradiance; includes the solar constant.
* [Temperature Gradient](temperature-gradient.md) — `K/m`, the driver of conduction.
* [Thermal Conductivity](thermal-conductivity.md) — `W/(m·K)`, Fourier's law.
* [Heat Transfer Coefficient](heat-transfer-coefficient.md) — `W/(m²·K)`, the building-physics U-value.
* [Thermal Resistance](thermal-resistance.md) — `m²·K/W`, the R-value; layers in series add up.
* [Thermal Diffusivity](thermal-diffusivity.md) — `m²/s`, how fast a temperature change propagates.

### Reacting to heat

* [Thermal Expansion](thermal-expansion.md) — `1/K`, why bridges have expansion joints.
