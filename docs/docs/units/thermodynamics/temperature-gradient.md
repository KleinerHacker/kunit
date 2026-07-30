# Temperature Gradient

Package: `org.pcsoft.framework.kunit.thermo.temperaturegradient`
Base unit: **kelvin per meter** (`KTemperatureGradientUnit.BASE == KTemperatureGradientUnit.KELVIN_PER_METER`)

Type: **constructed unit**

A temperature gradient is a temperature change per unit of length: `temperatureDifference / length`
(`K/m`). It is the driving quantity of conduction — multiplied by a
[thermal conductivity](thermal-conductivity.md) it yields a
[heat flux density](heat-flux-density.md).

`KTemperatureGradientUnitInstance` wraps a `KMixedUnitInstance` of exactly two terms in the canonical
normal form `temperature¹ · distance⁻¹` (`K·m⁻¹`), always normalized to K/m.

!!! note "A gradient is a *change* per length"
    The temperature dimension is the **difference** group (`KTemperatureDifferenceUnit`). An absolute
    scale with an offset (°C, °F) has no meaning in a gradient — only intervals do. That is also why
    `°F/ft` converts with the Fahrenheit *interval* factor 5/9, not with the −32 offset.

## Named units

| Unit | Symbol | Token | 1 unit in K/m |
|---|---|---:|---:|
| Kelvin per meter | `K/m` | `kelvinPerMeter` | 1.0 |
| Kelvin per kilometer | `K/km` | `kelvinPerKilometer` | 0.001 |
| Degree Fahrenheit per foot | `°F/ft` | `fahrenheitPerFoot` | ≈ 1.822689 |

All accept the full SI prefix range (`milli.kelvinPerMeter`, …).

## Real-world example: the geothermal gradient

Earth's crust warms by about 25 K per kilometre of depth. A borehole reaches 3.5 km. How much warmer is
the rock at the bottom, and how deep must one drill for a 100 K rise?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val geothermal = 25 of kelvinPerKilometer
val borehole = 3.5 of kilo.meters

val rise = geothermal * borehole            // KTemperatureDifferenceUnitInstance
rise into KTemperatureDifference.ofKelvin(1) // 87.5 K warmer at the bottom

val depthFor100K = KTemperatureDifference.ofKelvin(100) / geothermal // KLengthUnitInstance
depthFor100K into kilo.meters               // 4.0 km
depthFor100K into meters                    // 4000.0 m
```

## Computing with the core units (temperature difference & length)

| Expression | Result type | Meaning |
|---|---|---|
| `temperatureDifference / length` | `KTemperatureGradientUnitInstance` | gradient |
| `temperatureGradient * length` | `KTemperatureDifferenceUnitInstance` | rise across the length |
| `length * temperatureGradient` | `KTemperatureDifferenceUnitInstance` | rise (commutative) |
| `temperatureDifference / temperatureGradient` | `KLengthUnitInstance` | length spanned |

## Decompositions

Both decompositions produce the same typed, value-equal instance.

| Decomposition | Form | Result |
|---|---|---|
| `temperatureDifference / length` | typed operator | `KTemperatureGradientUnitInstance` |
| `temperature · distance⁻¹` | native expression + `toTemperatureGradient()` | `KTemperatureGradientUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val typed  = KTemperatureDifference.ofKelvin(1) / (1 of meters)
val native = (KTemperatureDifference.ofKelvin(1).toUnit() / (1 of meters).toUnit()).toTemperatureGradient()

typed == native // true - both are 1.0 K/m
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*

val total = (1 of kelvinPerMeter) + (500 of kelvinPerKilometer)  // 1.5 K/m
(1 of kelvinPerMeter) > (500 of kelvinPerKilometer)              // true
(1 of kelvinPerMeter) == (1000 of kelvinPerKilometer)            // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.temperaturegradient.*

(25 of kelvinPerKilometer).toString()                        // "0.025 K/m"
"${(25 of kelvinPerKilometer) into kelvinPerKilometer} K/km" // "25.0 K/km"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `K/m` | `kelvinPerMeter` | temperature gradient, base unit |
| `K·m⁻¹` | `ΔK / meters` | same quantity in base dimensions |
| `K/km` | `kelvinPerKilometer` | kelvin per kilometre (geothermal gradient) |
| `°F/ft` | `fahrenheitPerFoot` | degree Fahrenheit per foot |
| `∇T = ΔT / L` | `KTemperatureDifference.ofKelvin(25) / (1 of kilo.meters)` | gradient from rise ÷ length |
| `ΔT = ∇T · L` | `geothermal * borehole` | rise from gradient × length |
| `L = ΔT / ∇T` | `KTemperatureDifference.ofKelvin(100) / geothermal` | length from rise ÷ gradient |
