# Kinematic Viscosity

Package: `org.pcsoft.framework.kunit.common.diffusivity`
Base unit: **square meter per second**
(`KDiffusivityUnit.BASE == KDiffusivityUnit.SQUARE_METER_PER_SECOND`)

Type: **constructed unit**

The kinematic viscosity `ν = η / ρ` is the [dynamic viscosity](viscosity.md) divided by the
[density](density.md) — the quantity that governs how momentum diffuses through a fluid. Its dimension is
`length² · time⁻¹` (`m²/s`).

This is exactly the dimension and quantity of the **diffusivity** group, which is shared with the
[thermal diffusivity](../thermodynamics/thermal-diffusivity.md) of thermodynamics. KUnit therefore does **not**
introduce a second group for it: the kinematic viscosity is a **reading** of
`KDiffusivityUnitInstance`, which is why the group lives in `common`. This page documents the mechanical reading.

!!! note "One group, two subject areas"
`KDiffusivityUnit` carries both vocabularies: the metric readings (m²/s, mm²/s) shared by both fields, and the
traditional kinematic-viscosity spellings stokes and centistokes.

## Named units

| Unit                         | Symbol  |                        Token | 1 unit in m²/s |
|------------------------------|---------|-----------------------------:|---------------:|
| Square meter per second      | `m²/s`  |      `squareMetersPerSecond` |            1.0 |
| Square millimeter per second | `mm²/s` | `squareMillimetersPerSecond` |           1e-6 |
| Stokes                       | `St`    |                     `stokes` |           1e-4 |
| Centistokes                  | `cSt`   |                `centistokes` |           1e-6 |
| Square foot per hour         | `ft²/h` |          `squareFeetPerHour` |   ≈ 2.58064e-5 |

`1 cSt = 1 mm²/s` exactly — water at 20 °C is ≈ 1 cSt. All units accept the full SI prefix range, so
`centi.stokes` is another spelling of the centistokes.

## Decompositions

| Form                        | Kotlin                                                      | Result type                |
|-----------------------------|-------------------------------------------------------------|----------------------------|
| dynamic viscosity / density | `viscosity / density`                                       | `KDiffusivityUnitInstance` |
| native expression           | `((length.toUnit() pow 2) / time.toUnit()).toDiffusivity()` | `KDiffusivityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.viscosity.pascalSeconds

val water = (1000 of kilo.grams) / (1 of (meters pow 3))
val typed = (1 of milli.pascalSeconds) / water
val native = (((1 of milli.meters).toUnit() pow 2) / (1 of seconds).toUnit()).toDiffusivity()

typed == native          // true - both are 1e-6 m²/s
typed into centistokes   // 1.0
```

## Computing with the core units

| Expression                                       | Result type                | Meaning     |
|--------------------------------------------------|----------------------------|-------------|
| `viscosity / density`                            | `KDiffusivityUnitInstance` | `ν = η / ρ` |
| `diffusivity * density`, `density * diffusivity` | `KViscosityUnitInstance`   | `η = ν · ρ` |
| `viscosity / diffusivity`                        | `KDensityUnitInstance`     | `ρ = η / ν` |

## Real-world example: hydraulic oil selection

A hydraulic oil is specified as ISO VG 46, i.e. 46 cSt at 40 °C, with a density of 870 kg/m³. What dynamic viscosity
does that correspond to?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.diffusivity.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.viscosity.pascalSeconds

val nu = 46 of centistokes
nu into squareMillimetersPerSecond // 46.0

val rho = (870 of kilo.grams) / (1 of (meters pow 3))
val eta = nu * rho                 // KViscosityUnitInstance
eta into pascalSeconds             // ≈ 0.04002
eta into centi.poises              // ≈ 40.02
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.diffusivity.*

val sum = (10 of centistokes) + (4 of centistokes) // 14 cSt
(1 of stokes) > (10 of centistokes)                // true
(1 of centistokes) == (1 of squareMillimetersPerSecond) // true (same value)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.diffusivity.*

(46 of centistokes).toString()                  // "4.6E-5 m²/s" (base unit)
"${(46 of centistokes) into centistokes} cSt"   // "46.0 cSt"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin                     | Meaning                          |
|-------------|----------------------------|----------------------------------|
| `m²/s`      | `squareMetersPerSecond`    | kinematic viscosity, base unit   |
| `m²·s⁻¹`    | `(meters pow 2) / seconds` | same quantity in base dimensions |
| `cSt`       | `centistokes`              | centistokes (= 1 mm²/s)          |
| `ν = η / ρ` | `viscosity / density`      | typed decomposition              |
| `η = ν · ρ` | `diffusivity * density`    | solved for the dynamic viscosity |
| `ρ = η / ν` | `viscosity / diffusivity`  | solved for the density           |
