# Thermal Diffusivity

Package: `org.pcsoft.framework.kunit.thermo.diffusivity`
Base unit: **square meter per second** (`KThermalDiffusivityUnit.BASE == KThermalDiffusivityUnit.SQUARE_METER_PER_SECOND`)

Type: **constructed unit**

Thermal diffusivity `α` says how *fast* a temperature change propagates through a material — as opposed
to [thermal conductivity](thermal-conductivity.md), which says how *much* heat flows in the steady
state. Unit: `m²/s`. It is defined as

```
α = λ / (ρ · c_p)
```

`KThermalDiffusivityUnitInstance` wraps a `KMixedUnitInstance` of exactly two terms in the canonical
normal form `distance² · time⁻¹` (`m²·s⁻¹`), always normalized to m²/s.

!!! note "Package name vs. class name"
    The package is `thermo.diffusivity`, not `thermo.thermaldiffusivity` — a unit package must not repeat
    its field package's name. The types keep the full technical term
    (`KThermalDiffusivityUnitInstance`). The dimension `m²/s` is shared with kinematic viscosity and mass
    diffusivity; this group models the thermal quantity.

## Named units

| Unit | Symbol | Token | 1 unit in m²/s |
|---|---|---:|---:|
| Square meter per second | `m²/s` | `squareMetersPerSecond` | 1.0 |
| Square millimeter per second | `mm²/s` | `squareMillimetersPerSecond` | 1e-6 |
| Square foot per hour | `ft²/h` | `squareFeetPerHour` | ≈ 2.58064e-5 |

Material tables list `α` in mm²/s, which is exactly `micro.squareMetersPerSecond`. All units accept the
full SI prefix range.

## Typical values

| Material | α |
|---|---:|
| Copper | ≈ 116 mm²/s |
| Steel | ≈ 14 mm²/s |
| Glass | ≈ 0.34 mm²/s |
| Water | ≈ 0.14 mm²/s |
| Mineral wool | ≈ 1.2 mm²/s |

## Real-world example: how fast copper equalises

Copper has λ = 401 W/(m·K), ρ = 8960 kg/m³ and c_p = 385 J/(kg·K).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.toDensity
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.diffusivity.*
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin

val density = ((8960 of kilo.grams).toUnit() / ((1 of meters).toUnit() pow 3)).toDensity()
val alpha = (401 of wattsPerMeterKelvin)
    .diffusivityWith(density, 385 of joulesPerKilogramKelvin)

alpha into squareMillimetersPerSecond // ≈ 116.25 mm²/s
alpha into squareMetersPerSecond      // ≈ 1.1625e-4 m²/s

// Inverse: recover the conductivity from the diffusivity
alpha.conductivityWith(density, 385 of joulesPerKilogramKelvin) into wattsPerMeterKelvin // 401.0
```

## Computing with the neighbouring units

The defining relation is **ternary** (`α = λ / (ρ · c_p)`), so unlike every other group here it cannot be
a single binary operator without inventing an intermediate type for the volumetric heat capacity
`ρ · c_p` (J/(m³·K)), which this library does not model. The relation is therefore exposed as named,
strongly typed functions:

| Function | Result type | Meaning |
|---|---|---|
| `thermalConductivity.diffusivityWith(density, specificHeatCapacity)` | `KThermalDiffusivityUnitInstance` | `α = λ / (ρ · c_p)` |
| `thermalDiffusivity.conductivityWith(density, specificHeatCapacity)` | `KThermalConductivityUnitInstance` | `λ = α · ρ · c_p` |
| `thermalDiffusivity.densityWith(conductivity, specificHeatCapacity)` | `KDensityUnitInstance` | `ρ = λ / (α · c_p)` |
| `thermalDiffusivity.specificHeatCapacityWith(conductivity, density)` | `KSpecificHeatCapacityUnitInstance` | `c_p = λ / (α · ρ)` |

All four funnel into the same normalizing factory as every other decomposition.

## Decompositions

Both decompositions produce the same typed, value-equal instance.

| Decomposition | Form | Result |
|---|---|---|
| `λ / (ρ · c_p)` | typed function `diffusivityWith` | `KThermalDiffusivityUnitInstance` |
| `distance² · time⁻¹` | native expression + `toThermalDiffusivity()` | `KThermalDiffusivityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.toDensity
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.conductivity.wattsPerMeterKelvin
import org.pcsoft.framework.kunit.thermo.diffusivity.*
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin

// λ = 1 W/(m·K), ρ = 1 kg/m³, c_p = 1 J/(kg·K)  =>  α = 1 m²/s
val unitDensity = ((1000 of grams).toUnit() / ((1 of meters).toUnit() pow 3)).toDensity()
val typed = (1 of wattsPerMeterKelvin).diffusivityWith(unitDensity, 1 of joulesPerKilogramKelvin)
val native = (((1 of meters).toUnit() pow 2) / (1 of seconds).toUnit()).toThermalDiffusivity()

typed == native // true - both are 1.0 m²/s
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.diffusivity.*

val sum = (10 of squareMillimetersPerSecond) + (4 of squareMillimetersPerSecond) // 14 mm²/s
(10 of squareMillimetersPerSecond) > (4 of squareMillimetersPerSecond)           // true
(1 of squareMetersPerSecond) == (1_000_000 of squareMillimetersPerSecond)        // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.diffusivity.*

(111 of squareMillimetersPerSecond).toString()                                   // "1.11E-4 m²/s"
"${(111 of squareMillimetersPerSecond) into squareMillimetersPerSecond} mm²/s"   // "111.0 mm²/s"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `m²/s` | `squareMetersPerSecond` | thermal diffusivity, base unit |
| `m²·s⁻¹` | `(meters pow 2) / seconds` | same quantity in base dimensions |
| `mm²/s` | `squareMillimetersPerSecond` | square millimetre per second (material tables) |
| `α = λ / (ρ · c_p)` | `conductivity.diffusivityWith(density, heat)` | the defining relation |
| `λ = α · ρ · c_p` | `alpha.conductivityWith(density, heat)` | conductivity from diffusivity |
| `ρ = λ / (α · c_p)` | `alpha.densityWith(conductivity, heat)` | density from diffusivity |
| `c_p = λ / (α · ρ)` | `alpha.specificHeatCapacityWith(conductivity, density)` | specific heat from diffusivity |
