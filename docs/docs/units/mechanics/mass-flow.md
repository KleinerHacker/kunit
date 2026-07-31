# Mass Flow Rate

Package: `org.pcsoft.framework.kunit.mechanic.massflow`
Base unit: **kilogram per second** (`KMassFlowUnit.BASE == KMassFlowUnit.KILOGRAMS_PER_SECOND`)

Type: **constructed unit**

The mass flow rate `ṁ` is the mass transported per unit of time — the mass counterpart of the
[volumetric flow](../kinematics/volume-flow.md). It is a **constructed** unit — the composition
`mass · time⁻¹` (`kg/s`).

`KMassFlowUnitInstance` wraps a `KMixedUnitInstance` of exactly two terms in the canonical normal form:
`KMassUnit.BASE` (gram) at `+1` and `KTimeUnit.BASE` (second) at `-1`. Since the mass component of this library is
normalized to grams, the stored value is the raw gram-based component value and readings in kg/s divide by a fixed
factor.

## Named units

| Unit                | Symbol |                Token |     1 unit in kg/s |
|---------------------|--------|---------------------:|-------------------:|
| Kilogram per second | `kg/s` | `kilogramsPerSecond` |                1.0 |
| Gram per second     | `g/s`  |     `gramsPerSecond` |               1e-3 |
| Kilogram per hour   | `kg/h` |   `kilogramsPerHour` |             1/3600 |
| Tonne per hour      | `t/h`  |      `tonnesPerHour` | 1000/3600 ≈ 0.2778 |
| Pound per second    | `lb/s` |    `poundsPerSecond` |         0.45359237 |
| Pound per hour      | `lb/h` |      `poundsPerHour` |       ≈ 1.25998e-4 |

All units accept the full SI prefix range (`milli.gramsPerSecond` for dosing pumps).

## Decompositions

Mass flow has two equivalent decompositions; both funnel into the same normalizing factory.

| Form                      | Kotlin                                         | Result type             |
|---------------------------|------------------------------------------------|-------------------------|
| mass / time               | `mass / time`                                  | `KMassFlowUnitInstance` |
| density × volumetric flow | `density * volumeflow`                         | `KMassFlowUnitInstance` |
| native expression         | `(mass.toUnit() / time.toUnit()).toMassFlow()` | `KMassFlowUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.volumeflow.cubicMetersPerSecond
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.massflow.*

val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val viaMassTime = (2000 of kilo.grams) / (1 of seconds)
val viaDensityFlow = water * (2 of cubicMetersPerSecond)

viaMassTime == viaDensityFlow          // true - both are 2000 kg/s
viaMassTime into kilogramsPerSecond    // 2000.0
```

## Computing with the core units

| Expression                                     | Result type               | Meaning                      |
|------------------------------------------------|---------------------------|------------------------------|
| `mass / time`                                  | `KMassFlowUnitInstance`   | `ṁ = m / t`                  |
| `massflow * time`, `time * massflow`           | `KMassUnitInstance`       | transported mass `m = ṁ · t` |
| `mass / massflow`                              | `KTimeUnitInstance`       | required time `t = m / ṁ`    |
| `density * volumeflow`, `volumeflow * density` | `KMassFlowUnitInstance`   | `ṁ = ρ · Q`                  |
| `massflow / density`                           | `KVolumeFlowUnitInstance` | `Q = ṁ / ρ`                  |
| `massflow / volumeflow`                        | `KDensityUnitInstance`    | `ρ = ṁ / Q`                  |

## Real-world example: pump throughput

A pump moves 15 m³/h of water (ρ = 998 kg/m³). What mass flow is that in t/h, and how much mass passes in 8 hours?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.volumeflow.cubicMetersPerHour
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.massflow.*

val water = (998 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val flow = water * (15 of cubicMetersPerHour)
flow into tonnesPerHour                 // ≈ 14.97

val perShift = flow * (8 of hours)      // KMassUnitInstance
perShift into kilo.grams                // ≈ 119760.0
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.massflow.*

val sum = (10 of kilogramsPerSecond) + (4 of kilogramsPerSecond) // 14 kg/s
(1 of kilogramsPerSecond) > (1 of tonnesPerHour)                 // true
(3.6 of tonnesPerHour) == (1 of kilogramsPerSecond)              // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.massflow.*

(2 of kilogramsPerSecond).toString()                     // "2.0 kg/s" (base unit)
"${(2 of kilogramsPerSecond) into tonnesPerHour} t/h"    // "7.2 t/h"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin                          | Meaning                            |
|-------------|---------------------------------|------------------------------------|
| `kg/s`      | `kilogramsPerSecond`            | mass flow, base unit (named token) |
| `kg·s⁻¹`    | `kilo.grams * (seconds pow -1)` | same quantity as a pure product    |
| `t/h`       | `tonnesPerHour`                 | industrial throughput reading      |
| `ṁ = m / t` | `mass / time`                   | decomposition A                    |
| `ṁ = ρ · Q` | `density * volumeflow`          | decomposition B                    |
| `Q = ṁ / ρ` | `massflow / density`            | solved for the volumetric flow     |
| `mg/s`      | `milli.gramsPerSecond`          | prefixed mass flow                 |
