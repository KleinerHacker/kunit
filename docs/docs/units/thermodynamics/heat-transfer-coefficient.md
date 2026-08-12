# Heat Transfer Coefficient

Package: `org.pcsoft.framework.kunit.thermo.heattransfercoefficient`
Base unit: **watt per square meter-kelvin**
(`KHeatTransferCoefficientUnit.BASE == KHeatTransferCoefficientUnit.WATT_PER_SQUARE_METER_KELVIN`)

Type: **constructed unit**

The heat transfer coefficient — in building physics the **U-value** — is the heat flux density a component passes per
kelvin of temperature difference: `W/(m²·K)`. The lower the U-value, the better the insulation.

`KHeatTransferCoefficientUnitInstance` wraps a `KMixedUnitInstance` of exactly three terms in the canonical normal form
`mass¹ · time⁻³ · temperature⁻¹` (`kg·s⁻³·K⁻¹`), always normalized to W/ (m²·K). As
for [heat flux density](heat-flux-density.md) the area cancels the watt's length dimensions, so the normal form carries
no distance term.

Its reciprocal is the [thermal resistance](thermal-insulance.md) (R-value); multiplied by a thickness it becomes
a [thermal conductivity](thermal-conductivity.md).

## Named units

| Unit                          | Symbol           |                                     Token | 1 unit in W/(m²·K) |
|-------------------------------|------------------|------------------------------------------:|-------------------:|
| Watt per square meter-kelvin  | `W/(m²·K)`       |               `wattsPerSquareMeterKelvin` |                1.0 |
| Btu per hour-square foot-°F   | `Btu/(h·ft²·°F)` |         `btusPerHourSquareFootFahrenheit` |         ≈ 5.678263 |
| Calorie per second-cm²-kelvin | `cal/(s·cm²·K)`  | `caloriesPerSecondSquareCentimeterKelvin` |            41840.0 |

All accept the full SI prefix range (`milli.wattsPerSquareMeterKelvin`, …).

## Typical U-values

| Component          |                    U |
|--------------------|---------------------:|
| Single glazing     |       ≈ 5.8 W/(m²·K) |
| Double glazing     |       ≈ 2.8 W/(m²·K) |
| Triple glazing     | ≈ 0.7 … 1.3 W/(m²·K) |
| Passive-house wall |      ≈ 0.15 W/(m²·K) |

## Real-world example: heat loss through a window

A 2.4 m² triple-glazed window has U = 1.3 W/ (m²·K). It is 21 °C inside and 1 °C outside.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*
import org.pcsoft.framework.kunit.thermo.temperature.celsius

val window = 1.3 of wattsPerSquareMeterKelvin
val drop = (21 of celsius) - (1 of celsius)      // 20 K
val glass = (2 of meters) * (1.2 of meters)      // 2.4 m²

val flux = window * drop                          // KHeatFluxDensityUnitInstance
flux into wattsPerSquareMeter                     // 26.0 W/m²

val loss = flux * glass                           // KPowerUnitInstance
loss into watts                                   // 62.4 W

// What would single glazing cost us?
val single = 5.8 of wattsPerSquareMeterKelvin
((single * drop) * glass) into watts              // 278.4 W - four and a half times as much
```

## Computing with the neighbouring units

| Expression                                        | Result type                            | Meaning                           |
|---------------------------------------------------|----------------------------------------|-----------------------------------|
| `heatFluxDensity / temperatureDifference`         | `KHeatTransferCoefficientUnitInstance` | U-value from measurement          |
| `thermalConductivity / length`                    | `KHeatTransferCoefficientUnitInstance` | U-value from material + thickness |
| `heatTransferCoefficient * temperatureDifference` | `KHeatFluxDensityUnitInstance`         | flux through the component        |
| `temperatureDifference * heatTransferCoefficient` | `KHeatFluxDensityUnitInstance`         | same (commutative)                |
| `heatFluxDensity / heatTransferCoefficient`       | `KTemperatureDifferenceUnitInstance`   | driving difference                |
| `heatTransferCoefficient * length`                | `KThermalConductivityUnitInstance`     | material conductivity             |
| `length * heatTransferCoefficient`                | `KThermalConductivityUnitInstance`     | same (commutative)                |
| `thermalConductivity / heatTransferCoefficient`   | `KLengthUnitInstance`                  | required thickness                |

## Decompositions

All three decompositions produce the same typed, value-equal instance.

| Decomposition                             | Form                                   | Result                                 |
|-------------------------------------------|----------------------------------------|----------------------------------------|
| `heatFluxDensity / temperatureDifference` | typed operator                         | `KHeatTransferCoefficientUnitInstance` |
| `thermalConductivity / length`            | typed operator                         | `KHeatTransferCoefficientUnitInstance` |
| `mass · time⁻³ · temperature⁻¹`           | native + `toHeatTransferCoefficient()` | `KHeatTransferCoefficientUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaFlux         = (1 of wattsPerSquareMeter) / KTemperatureDifference.ofKelvin(1)
val viaConductivity = (1 of wattsPerMeterKelvin) / (1 of meters)
val native = (
    (1000 of grams).toUnit() /
        ((1 of seconds).toUnit() pow 3) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toHeatTransferCoefficient()

viaFlux == viaConductivity // true
viaFlux == native          // true - all are 1.0 W/(m²·K)
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*

val total = (1 of kilo.wattsPerSquareMeterKelvin) + (500 of wattsPerSquareMeterKelvin)  // 1500
(1 of kilo.wattsPerSquareMeterKelvin) > (500 of wattsPerSquareMeterKelvin)              // true
(1 of kilo.wattsPerSquareMeterKelvin) == (1000 of wattsPerSquareMeterKelvin)            // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.*

(1.3 of wattsPerSquareMeterKelvin).toString()                                             // "1.3 W/(m²·K)"
"${(1.3 of wattsPerSquareMeterKelvin) into btusPerHourSquareFootFahrenheit} Btu/(h·ft²·°F)" // "0.229..."
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics      | Kotlin                                            | Meaning                                        |
|------------------|---------------------------------------------------|------------------------------------------------|
| `W/(m²·K)`       | `wattsPerSquareMeterKelvin`                       | heat transfer coefficient (U-value), base unit |
| `kg·s⁻³·K⁻¹`     | `grams / (seconds pow 3) / ΔK`                    | same quantity in base dimensions               |
| `U = q̇ / ΔT`     | `(26 of wattsPerSquareMeter) / drop`              | U-value from flux ÷ temperature difference     |
| `U = λ / d`      | `(0.04 of wattsPerMeterKelvin) / (0.2 of meters)` | U-value from conductivity ÷ thickness          |
| `q̇ = U · ΔT`     | `window * drop`                                   | flux from U-value × temperature difference     |
| `P = U · A · ΔT` | `(window * drop) * glass`                         | total heat loss                                |
