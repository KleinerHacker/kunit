# Dynamic Viscosity

Package: `org.pcsoft.framework.kunit.mechanic.viscosity`
Base unit: **pascal second** (`KViscosityUnit.BASE == KViscosityUnit.PASCAL_SECOND`)

Type: **constructed unit**

The dynamic viscosity `η` describes a fluid's resistance to shear. It is a **constructed** unit — the composition
`pressure · time`, i.e. `mass · length⁻¹ · time⁻¹` (`Pa·s`).

`KViscosityUnitInstance` wraps a `KMixedUnitInstance` of exactly three terms in the canonical normal form:
`KMassUnit.BASE` (gram) at `+1`, `KDistanceUnit.BASE` (meter) at `-1` and `KTimeUnit.BASE` (second) at `-1`. Since the
mass component of this library is normalized to grams, the stored value is the raw gram-based component value and
readings in Pa·s divide by a fixed factor.

!!! note "Dynamic vs. kinematic viscosity"
The **kinematic** viscosity `ν = η / ρ` (`m²/s`) is a different quantity and lives in the diffusivity group —
see [kinematic viscosity](kinematic-viscosity.md).

## Named units

| Unit                               | Symbol       |                            Token | 1 unit in Pa·s |
|------------------------------------|--------------|---------------------------------:|---------------:|
| Pascal second                      | `Pa*s`       |                  `pascalSeconds` |            1.0 |
| Poise                              | `P`          |                         `poises` |            0.1 |
| Pound-force second per square foot | `lbf*s/ft^2` | `poundForceSecondsPerSquareFoot` |      ≈ 47.8803 |
| Reyn (lbf·s/in²)                   | `reyn`       |                          `reyns` |     ≈ 6894.757 |

The two everyday spellings for water-like fluids are prefixed forms, not own tokens: the **millipascal second** is
`milli.pascalSeconds` and the **centipoise** is `centi.poises` — and they are equal (`1 mPa·s = 1 cP`, water at 20 °C).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.viscosity.*

val water = 1 of milli.pascalSeconds
water into centi.poises  // 1.0
water into pascalSeconds // 0.001
(1 of poises) into pascalSeconds // 0.1
```

## Computing with the core units (pressure & time)

| Expression                           | Result type                | Meaning                         |
|--------------------------------------|----------------------------|---------------------------------|
| `pressure * time`, `time * pressure` | `KViscosityUnitInstance`   | `η = p · t`                     |
| `viscosity / pressure`               | `KTimeUnitInstance`        | `t = η / p`                     |
| `viscosity / time`                   | `KPressureUnitInstance`    | `p = η / t`                     |
| `viscosity / density`                | `KDiffusivityUnitInstance` | kinematic viscosity `ν = η / ρ` |
| `viscosity / diffusivity`            | `KDensityUnitInstance`     | `ρ = η / ν`                     |

The native form converts with `toViscosity()`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.viscosity.*

val typed = (2 of pascals) * (3 of seconds)
val native = ((2 of pascals).toUnit() * (3 of seconds).toUnit()).toViscosity()

typed == native            // true - both are 6 Pa·s
typed into pascalSeconds   // 6.0
```

## Real-world example: engine oil at operating temperature

An SAE 30 oil measures 9.3 cP at 100 °C with a density of 850 kg/m³. What is that in Pa·s, and which kinematic viscosity
does it correspond to?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.diffusivity.centistokes
import org.pcsoft.framework.kunit.common.diffusivity.div
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.viscosity.*
import org.pcsoft.framework.kunit.pow

val oil = 9.3 of centi.poises
oil into pascalSeconds        // 0.0093

val rho = (850 of kilo.grams) / (1 of (meters pow 3))
val nu = oil / rho            // KDiffusivityUnitInstance
nu into centistokes           // ≈ 10.94
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.viscosity.*

val sum = (10 of pascalSeconds) + (4 of pascalSeconds) // 14 Pa·s
(1 of poises) > (1 of milli.pascalSeconds)             // true
(1 of poises) == (100 of milli.pascalSeconds)          // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.mechanic.viscosity.*

(2 of pascalSeconds).toString()                    // "2.0 Pa*s" (base unit)
"${(2 of pascalSeconds) into centi.poises} cP"     // "2000.0 cP"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics  | Kotlin                                            | Meaning                                    |
|--------------|---------------------------------------------------|--------------------------------------------|
| `Pa·s`       | `pascalSeconds`                                   | dynamic viscosity, base unit (named token) |
| `kg·m⁻¹·s⁻¹` | `kilo.grams * (meters pow -1) * (seconds pow -1)` | same quantity as a pure product            |
| `cP`         | `centi.poises`                                    | centipoise (= 1 mPa·s)                     |
| `η = p · t`  | `pressure * time`                                 | typed decomposition                        |
| `ν = η / ρ`  | `viscosity / density`                             | kinematic viscosity                        |
| `mPa·s`      | `milli.pascalSeconds`                             | prefixed viscosity                         |
