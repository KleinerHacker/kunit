# Thermal Resistance (R-value)

Package: `org.pcsoft.framework.kunit.thermo.resistance`
Base unit: **square meter-kelvin per watt**
(`KThermalResistanceUnit.BASE == KThermalResistanceUnit.SQUARE_METER_KELVIN_PER_WATT`)

Type: **constructed unit**

Thermal resistance — the **R-value** — is how strongly a layer resists heat flow: `m²·K/W`. It is the exact reciprocal
of the [heat transfer coefficient](heat-transfer-coefficient.md) (U-value), and the form insulation products are
actually sold in, because R-values of layers in series simply **add up**.

`KThermalResistanceUnitInstance` wraps a `KMixedUnitInstance` of exactly three terms in the canonical normal form
`mass⁻¹ · time³ · temperature¹` (`kg⁻¹·s³·K`), always normalized to m²·K/W.

!!! note "Package name vs. class name"
The package is `thermo.resistance`, not `thermo.thermalresistance` — a unit package must not repeat its field package's
name. The **types** keep the full technical term (`KThermalResistanceUnitInstance`), which distinguishes them from
`electric.resistance`.

## Named units

| Unit                               | Symbol         |                            Token | 1 unit in m²·K/W |
|------------------------------------|----------------|---------------------------------:|-----------------:|
| Square meter-kelvin per watt (RSI) | `m²·K/W`       |       `squareMeterKelvinPerWatt` |              1.0 |
| Imperial R-value                   | `h·ft²·°F/Btu` | `hourSquareFootFahrenheitPerBtu` |       ≈ 0.176110 |
| Clo                                | `clo`          |                            `clo` |            0.155 |
| Tog                                | `tog`          |                            `tog` |              0.1 |

A US "R-30" batt is `30 of hourSquareFootFahrenheitPerBtu` ≈ 5.28 m²·K/W. A business suit is about 1 clo; duvets are
rated in tog (1 clo = 1.55 tog). All units accept the full SI prefix range.

## Real-world example: an insulated wall, layer by layer

A wall consists of 20 cm mineral wool (λ = 0.04 W/ (m·K)) and 12 cm brick (λ = 0.8 W/ (m·K)). What is the total R-value,
the resulting U-value, and the heat loss at ΔT = 25 K?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.heattransfercoefficient.wattsPerSquareMeterKelvin
import org.pcsoft.framework.kunit.thermo.resistance.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val wool  = (20 of centi.meters) / (0.04 of wattsPerMeterKelvin)  // 5.0 m²·K/W
val brick = (12 of centi.meters) / (0.8 of wattsPerMeterKelvin)   // 0.15 m²·K/W

val total = wool + brick                    // layers in series add up
total into squareMeterKelvinPerWatt         // 5.15 m²·K/W
total into hourSquareFootFahrenheitPerBtu   // ≈ 29.2 (an "R-29" wall)

val u = 1 / total                           // KHeatTransferCoefficientUnitInstance
u into wattsPerSquareMeterKelvin            // ≈ 0.194 W/(m²·K)

val drop = KTemperatureDifference.ofKelvin(25)
val flux = drop / total                     // KHeatFluxDensityUnitInstance
flux into wattsPerSquareMeter               // ≈ 4.85 W/m²

val wall = (10 of meters) * (2.5 of meters) // 25 m²
(flux * wall) into watts                    // ≈ 121 W
```

## Computing with the neighbouring units

| Expression                                  | Result type                            | Meaning                     |
|---------------------------------------------|----------------------------------------|-----------------------------|
| `temperatureDifference / heatFluxDensity`   | `KThermalResistanceUnitInstance`       | R from measurement          |
| `length / thermalConductivity`              | `KThermalResistanceUnitInstance`       | R from material + thickness |
| `thermalResistance * heatFluxDensity`       | `KTemperatureDifferenceUnitInstance`   | sustained difference        |
| `heatFluxDensity * thermalResistance`       | `KTemperatureDifferenceUnitInstance`   | same (commutative)          |
| `temperatureDifference / thermalResistance` | `KHeatFluxDensityUnitInstance`         | resulting flux              |
| `thermalResistance * thermalConductivity`   | `KLengthUnitInstance`                  | required thickness          |
| `thermalConductivity * thermalResistance`   | `KLengthUnitInstance`                  | same (commutative)          |
| `length / thermalResistance`                | `KThermalConductivityUnitInstance`     | implied conductivity        |
| `1 / heatTransferCoefficient`               | `KThermalResistanceUnitInstance`       | R from U                    |
| `1 / thermalResistance`                     | `KHeatTransferCoefficientUnitInstance` | U from R                    |

The two reciprocal operators are declared narrowly, so `1 / u` and `1 / r` return a **typed** value rather than the
generic mixed unit the group-agnostic `Number.div` would produce.

## Decompositions

All three decompositions produce the same typed, value-equal instance.

| Decomposition                             | Form                             | Result                           |
|-------------------------------------------|----------------------------------|----------------------------------|
| `temperatureDifference / heatFluxDensity` | typed operator                   | `KThermalResistanceUnitInstance` |
| `length / thermalConductivity`            | typed operator                   | `KThermalResistanceUnitInstance` |
| `mass⁻¹ · time³ · temperature¹`           | native + `toThermalResistance()` | `KThermalResistanceUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter
import org.pcsoft.framework.kunit.thermo.resistance.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaFlux      = KTemperatureDifference.ofKelvin(1) / (1 of wattsPerSquareMeter)
val viaThickness = (1 of meters) / (1 of wattsPerMeterKelvin)
val native = (
    ((1 of seconds).toUnit() pow 3) *
        KTemperatureDifference.ofKelvin(1).toUnit() /
        (1000 of grams).toUnit()
    ).toThermalResistance()

viaFlux == viaThickness // true
viaFlux == native       // true - all are 1.0 m²·K/W
```

## Operators

`+` and `-` are exactly the physically meaningful operation here: layers in series add their R-values.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.resistance.*

val series = (5 of squareMeterKelvinPerWatt) + (0.15 of squareMeterKelvinPerWatt) // 5.15
(1 of squareMeterKelvinPerWatt) > (5 of tog)      // true (5 tog = 0.5 m²·K/W)
(1 of squareMeterKelvinPerWatt) == (10 of tog)    // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.resistance.*

(5 of squareMeterKelvinPerWatt).toString()                                        // "5.0 m²·K/W"
"R-${(5 of squareMeterKelvinPerWatt) into hourSquareFootFahrenheitPerBtu}"        // "R-28.39..."
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics         | Kotlin                                                 | Meaning                                 |
|---------------------|--------------------------------------------------------|-----------------------------------------|
| `m²·K/W`            | `squareMeterKelvinPerWatt`                             | thermal resistance (R-value), base unit |
| `kg⁻¹·s³·K`         | `(seconds pow 3) * ΔK / grams`                         | same quantity in base dimensions        |
| `h·ft²·°F/Btu`      | `hourSquareFootFahrenheitPerBtu`                       | imperial R-value                        |
| `R = d / λ`         | `(20 of centi.meters) / (0.04 of wattsPerMeterKelvin)` | R from thickness ÷ conductivity         |
| `R = ΔT / q̇`        | `drop / (4 of wattsPerSquareMeter)`                    | R from difference ÷ flux                |
| `R_total = R₁ + R₂` | `wool + brick`                                         | layers in series                        |
| `U = 1 / R`         | `1 / total`                                            | U-value from R-value                    |
| `q̇ = ΔT / R`        | `drop / total`                                         | flux from difference ÷ R                |
