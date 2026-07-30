# Thermal Conductivity

Package: `org.pcsoft.framework.kunit.thermo.conductivity`
Base unit: **watt per meter-kelvin** (`KThermalConductivityUnit.BASE == KThermalConductivityUnit.WATT_PER_METER_KELVIN`)

Type: **constructed unit**

Thermal conductivity `λ` (also `k`) is the material property in Fourier's law: the
[heat flux density](heat-flux-density.md) through a material equals its conductivity times the
[temperature gradient](temperature-gradient.md). Unit: `W/(m·K)`.

`KThermalConductivityUnitInstance` wraps a `KMixedUnitInstance` of exactly four terms in the canonical
normal form `mass¹ · distance¹ · time⁻³ · temperature⁻¹` (`kg·m·s⁻³·K⁻¹`), always normalized to W/(m·K).

!!! note "Package name vs. class name"
    The package is `thermo.conductivity`, not `thermo.thermalconductivity` — a unit package must not
    repeat its field package's name. The **types** keep the full technical term
    (`KThermalConductivityUnitInstance`), which is what distinguishes them from
    `electric.conductivity`.

Divided by a thickness this becomes a [heat transfer coefficient](heat-transfer-coefficient.md); the
thickness divided by it is the [thermal resistance](thermal-resistance.md) (R-value).

## Named units

| Unit | Symbol | Token | 1 unit in W/(m·K) |
|---|---|---:|---:|
| Watt per meter-kelvin | `W/(m·K)` | `wattsPerMeterKelvin` | 1.0 |
| Btu per hour-foot-°F | `Btu/(h·ft·°F)` | `btusPerHourFootFahrenheit` | ≈ 1.730735 |
| Calorie per second-cm-kelvin | `cal/(s·cm·K)` | `caloriesPerSecondCentimeterKelvin` | 418.4 |

All accept the full SI prefix range — insulation materials are naturally written as
`40 of milli.wattsPerMeterKelvin`.

## Typical values

| Material | λ |
|---|---:|
| Copper | 401 W/(m·K) |
| Steel | ≈ 50 W/(m·K) |
| Glass | ≈ 1 W/(m·K) |
| Mineral wool | ≈ 0.04 W/(m·K) = 40 mW/(m·K) |

## Real-world example: heat loss through an insulated wall

A 30 cm mineral-wool layer (λ = 0.04 W/(m·K)) separates a 21 °C room from −5 °C outside air. The wall is
12 m². How much heat is lost?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.conductivity.*
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.temperature.celsius
import org.pcsoft.framework.kunit.thermo.temperaturegradient.kelvinPerMeter

val wool = 40 of milli.wattsPerMeterKelvin      // 0.04 W/(m·K)
val thickness = 30 of centi.meters
val drop = (21 of celsius) - (-5 of celsius)    // 26 K

val gradient = drop / thickness                 // KTemperatureGradientUnitInstance, ≈ 86.7 K/m
gradient into kelvinPerMeter                    // 86.666...

val flux = wool * gradient                      // KHeatFluxDensityUnitInstance (Fourier's law)
flux into wattsPerSquareMeter                   // ≈ 3.47 W/m²

val wall = (4 of meters) * (3 of meters)        // 12 m²
val loss = flux * wall                          // KPowerUnitInstance
loss into watts                                 // ≈ 41.6 W
```

## Computing with the neighbouring units

| Expression | Result type | Meaning |
|---|---|---|
| `heatFluxDensity / temperatureGradient` | `KThermalConductivityUnitInstance` | Fourier's law solved for λ |
| `thermalConductivity * temperatureGradient` | `KHeatFluxDensityUnitInstance` | Fourier's law |
| `temperatureGradient * thermalConductivity` | `KHeatFluxDensityUnitInstance` | same (commutative) |
| `heatFluxDensity / thermalConductivity` | `KTemperatureGradientUnitInstance` | implied gradient |

## Decompositions

Both decompositions produce the same typed, value-equal instance.

| Decomposition | Form | Result |
|---|---|---|
| `heatFluxDensity / temperatureGradient` | typed operator | `KThermalConductivityUnitInstance` |
| `mass · distance · time⁻³ · temperature⁻¹` | native + `toThermalConductivity()` | `KThermalConductivityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.*
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperaturegradient.kelvinPerMeter

val typed = (1 of wattsPerSquareMeter) / (1 of kelvinPerMeter)
val native = (
    (1000 of grams).toUnit() *
        (1 of meters).toUnit() /
        ((1 of seconds).toUnit() pow 3) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toThermalConductivity()

typed == native // true - both are 1.0 W/(m·K)
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.conductivity.*

val total = (1 of kilo.wattsPerMeterKelvin) + (500 of wattsPerMeterKelvin)  // 1500 W/(m·K)
(1 of kilo.wattsPerMeterKelvin) > (500 of wattsPerMeterKelvin)              // true
(1 of kilo.wattsPerMeterKelvin) == (1000 of wattsPerMeterKelvin)            // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.conductivity.*

(401 of wattsPerMeterKelvin).toString()                                          // "401.0 W/(m·K)"
"${(401 of wattsPerMeterKelvin) into btusPerHourFootFahrenheit} Btu/(h·ft·°F)"   // "231.7..."
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `W/(m·K)` | `wattsPerMeterKelvin` | thermal conductivity, base unit |
| `kg·m·s⁻³·K⁻¹` | `grams * meters / (seconds pow 3) / ΔK` | same quantity in base dimensions |
| `mW/(m·K)` | `milli.wattsPerMeterKelvin` | milliwatt per meter-kelvin (insulation) |
| `q̇ = λ · ∇T` | `wool * gradient` | Fourier's law |
| `λ = q̇ / ∇T` | `(80 of wattsPerSquareMeter) / gradient` | conductivity from flux ÷ gradient |
| `∇T = q̇ / λ` | `flux / wool` | gradient from flux ÷ conductivity |
