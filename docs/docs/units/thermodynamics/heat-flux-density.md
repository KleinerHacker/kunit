# Heat Flux Density

Package: `org.pcsoft.framework.kunit.thermo.heatfluxdensity`
Base unit: **watt per square meter** (`KHeatFluxDensityUnit.BASE == KHeatFluxDensityUnit.WATT_PER_SQUARE_METER`)

Type: **constructed unit**

Heat flux density is heat flow per unit of area: `power / area` (`W/m²`). The same unit measures *irradiance* and
*radiant exitance* — the intensity of radiation hitting or leaving a surface.

`KHeatFluxDensityUnitInstance` wraps a `KMixedUnitInstance` of exactly two terms in the canonical normal form
`mass¹ · time⁻³` (`kg·s⁻³`), always normalized to W/m².

!!! note "The distance dimension cancels out"
`W/m² = kg·m²·s⁻³/m² = kg·s⁻³`. The canonical normal form therefore carries **no** distance term.

The total heat flow itself is simply a [power](power.md); see
[heat flow](heat-flow.md). Divided by a temperature difference this becomes a
[heat transfer coefficient](heat-transfer-coefficient.md).

## Named units

| Unit                                 | Symbol        |                               Token | 1 unit in W/m² |
|--------------------------------------|---------------|------------------------------------:|---------------:|
| Watt per square meter                | `W/m²`        |               `wattsPerSquareMeter` |            1.0 |
| Btu per hour-square foot             | `Btu/(h·ft²)` |             `btusPerHourSquareFoot` |      ≈ 3.15459 |
| Calorie per second-square centimeter | `cal/(s·cm²)` | `caloriesPerSecondSquareCentimeter` |        41840.0 |

All accept the full SI prefix range (`kilo.wattsPerSquareMeter`, `milli.wattsPerSquareMeter`, …).

## The solar constant

The group exposes the mean extraterrestrial solar irradiance as `SOLAR_CONSTANT` (1361 W/m²), a plain
`Double`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val sun = SOLAR_CONSTANT of wattsPerSquareMeter
sun into wattsPerSquareMeter // 1361.0
```

## Real-world example: sizing a solar array

A rooftop receives 800 W/m² on a clear day. The array covers 25 m² and converts 20 % of the incident radiation. What
electrical power does it deliver?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val irradiance = 800 of wattsPerSquareMeter
val roof = (5 of meters) * (5 of meters)   // 25 m²

val incident = irradiance * roof           // KPowerUnitInstance
incident into kilo.watts                   // 20.0 kW

val electrical = incident * 0.2            // scalar scaling keeps the type
electrical into kilo.watts                 // 4.0 kW

// Inverse: how much roof area for 10 kW electrical at 20 % efficiency?
val needed = (50 of kilo.watts) / irradiance // KAreaUnitInstance
needed into ((1 of meters) * (1 of meters))  // 62.5 m²
```

## Computing with the core units (power & area)

| Expression                | Result type                    | Meaning                       |
|---------------------------|--------------------------------|-------------------------------|
| `power / area`            | `KHeatFluxDensityUnitInstance` | heat flux density             |
| `heatFluxDensity * area`  | `KPowerUnitInstance`           | total heat flow               |
| `area * heatFluxDensity`  | `KPowerUnitInstance`           | total heat flow (commutative) |
| `power / heatFluxDensity` | `KAreaUnitInstance`            | area it is spread over        |

## Decompositions

Both decompositions produce the same typed, value-equal instance.

| Decomposition   | Form                                      | Result                                  |
|-----------------|-------------------------------------------|-----------------------------------------|
| `power / area`  | typed operator                            | `KHeatFluxDensityUnitInstance` directly |
| `mass · time⁻³` | native expression + `toHeatFluxDensity()` | `KHeatFluxDensityUnitInstance`          |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val typed  = (1 of watts) / ((1 of meters) * (1 of meters))
val native = ((1000 of grams).toUnit() / ((1 of seconds).toUnit() pow 3)).toHeatFluxDensity()

typed == native // true - both are 1.0 W/m²
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

val total = (1 of kilo.wattsPerSquareMeter) + (500 of wattsPerSquareMeter)  // 1500 W/m²
(1 of kilo.wattsPerSquareMeter) > (500 of wattsPerSquareMeter)              // true
(1 of kilo.wattsPerSquareMeter) == (1000 of wattsPerSquareMeter)            // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.*

(1361 of wattsPerSquareMeter).toString()                                 // "1361.0 W/m²"
"${(1361 of wattsPerSquareMeter) into btusPerHourSquareFoot} Btu/(h·ft²)" // "431.4..."
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin                                  | Meaning                                    |
|-------------|-----------------------------------------|--------------------------------------------|
| `W/m²`      | `wattsPerSquareMeter`                   | heat flux density, base unit — named token |
| `kg·s⁻³`    | `grams / (seconds pow 3)`               | same quantity in base dimensions           |
| `kW/m²`     | `kilo.wattsPerSquareMeter`              | kilowatt per square meter                  |
| `E_0`       | `SOLAR_CONSTANT of wattsPerSquareMeter` | solar constant, 1361 W/m²                  |
| `q̇ = P / A` | `(1000 of watts) / roof`                | flux density from power ÷ area             |
| `P = q̇ · A` | `irradiance * roof`                     | power from flux density × area             |
| `A = P / q̇` | `(50 of kilo.watts) / irradiance`       | area from power ÷ flux density             |
