# Thermal Expansion Coefficient

Package: `org.pcsoft.framework.kunit.thermo.expansion`
Base unit: **per kelvin** (`KThermalExpansionUnit.BASE == KThermalExpansionUnit.PER_KELVIN`)

Type: **constructed unit**

The thermal expansion coefficient `α` is the *relative* change of a length (or area, or volume) per
kelvin: `1/K`. It is the reciprocal of a temperature difference.

`KThermalExpansionUnitInstance` wraps a `KMixedUnitInstance` of exactly one term in the canonical normal
form `temperature⁻¹` (`K⁻¹`), always normalized to 1/K. The temperature dimension is the **difference**
group — the coefficient describes a change per temperature *interval*.

!!! note "Package name vs. class name"
    The package is `thermo.expansion`, not `thermo.thermalexpansion` — a unit package must not repeat its
    field package's name. The types keep the full technical term (`KThermalExpansionUnitInstance`).

## Named units

| Unit | Symbol | Token | 1 unit in 1/K |
|---|---|---:|---:|
| Per kelvin | `1/K` | `perKelvin` | 1.0 |
| Per degree Fahrenheit | `1/°F` | `perFahrenheit` | 1.8 |
| Parts per million per kelvin | `ppm/K` | `ppmPerKelvin` | 1e-6 |

Material tables list `α` in ppm/K, which is exactly `micro.perKelvin`. All units accept the full SI
prefix range.

## Typical values

| Material | α |
|---|---:|
| Steel | ≈ 12 ppm/K |
| Concrete | ≈ 12 ppm/K |
| Aluminium | ≈ 23 ppm/K |
| Glass (borosilicate) | ≈ 3.3 ppm/K |

## Real-world example: a steel beam in summer

A 10 m steel beam (α = 12 ppm/K) warms from 0 °C to 50 °C. How much longer does it get? This is why
bridges have expansion joints.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.thermo.expansion.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.temperature.celsius

val steel = 12 of ppmPerKelvin
val beam = 10 of meters
val rise = (50 of celsius) - (0 of celsius)   // 50 K

// the dimensionless relative change
val strain = steel * rise                      // 6.0e-4

// the absolute change, typed
val growth = steel.elongationOf(beam, rise)    // KLengthUnitInstance
growth into milli.meters                       // 6.0 mm

// a 100 m bridge deck under the same swing
steel.elongationOf(100 of meters, rise) into milli.meters // 60.0 mm
```

## Operators

| Expression | Result type | Meaning |
|---|---|---|
| `1 / temperatureDifference` | `KThermalExpansionUnitInstance` | coefficient from an interval |
| `1 / thermalExpansion` | `KTemperatureDifferenceUnitInstance` | interval from a coefficient |
| `thermalExpansion * temperatureDifference` | `Double` | **relative** change (dimensionless) |
| `temperatureDifference * thermalExpansion` | `Double` | same (commutative) |
| `thermalExpansion.elongationOf(length, temperatureDifference)` | `KLengthUnitInstance` | **absolute** change |

The two reciprocal operators are declared narrowly, so `1 / d` and `1 / α` return a **typed** value rather
than the generic mixed unit the group-agnostic `Number.div` would produce.

!!! warning "`elongationOf` instead of chained `*`"
    `α · ΔT` is deliberately a plain `Double` — a relative change is dimensionless. Multiplying that
    `Double` onto a length would need the generic scalar `times` from the root package, and importing it
    explicitly **shadows** this group's `times` operator. `elongationOf` is a plain function precisely so
    it cannot be shadowed; prefer it whenever you want the absolute change.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.expansion.*

val sum = (12 of ppmPerKelvin) + (5 of ppmPerKelvin)   // 17 ppm/K
(12 of ppmPerKelvin) > (5 of ppmPerKelvin)             // true
(1 of perKelvin) == (1_000_000 of ppmPerKelvin)        // true
```

## Decompositions

Both decompositions produce the same typed, value-equal instance.

| Decomposition | Form | Result |
|---|---|---|
| `1 / temperatureDifference` | typed operator | `KThermalExpansionUnitInstance` |
| `temperature⁻¹` | native expression + `toThermalExpansion()` | `KThermalExpansionUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.thermo.expansion.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val typed  = 1 / KTemperatureDifference.ofKelvin(1)
val native = (KTemperatureDifference.ofKelvin(1).toUnit() pow -1).toThermalExpansion()

typed == native // true - both are 1.0 1/K
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.expansion.*

(12 of ppmPerKelvin).toString()                    // "1.2E-5 1/K"
"${(12 of ppmPerKelvin) into ppmPerKelvin} ppm/K"  // "12.0 ppm/K"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `1/K` | `perKelvin` | thermal expansion coefficient, base unit |
| `K⁻¹` | `ΔK pow -1` | same quantity as a negative exponent |
| `ppm/K` | `ppmPerKelvin` | parts per million per kelvin (material tables) |
| `α = 1 / ΔT` | `1 / KTemperatureDifference.ofKelvin(2)` | coefficient from an interval |
| `ε = α · ΔT` | `steel * rise` | relative change (dimensionless) |
| `Δl = α · l · ΔT` | `steel.elongationOf(beam, rise)` | absolute length change |
