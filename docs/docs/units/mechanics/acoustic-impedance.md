# Specific Acoustic Impedance

Package: `org.pcsoft.framework.kunit.mechanic.acousticimpedance`
Base unit: **pascal second per metre**
(`KAcousticImpedanceUnit.BASE == KAcousticImpedanceUnit.PASCAL_SECOND_PER_METER`)

Type: **constructed unit**

The specific acoustic impedance `Z` is the sound pressure a medium develops per unit of particle velocity:
`Z = p / v = ρ · c`. It decides how much sound is reflected at a boundary — air is ≈ 413 Pa·s/m and water
≈ 1.48 MPa·s/m, a ratio of about 3600, which is why almost no airborne sound enters water.

Its canonical base-dimension normal form is `mass · length⁻² · time⁻¹`.

## Named units

| Unit                    | Symbol       |                   Token | 1 unit in Pa·s/m |
|-------------------------|--------------|------------------------:|-----------------:|
| Pascal second per metre | `Pa*s/m`     | `pascalSecondsPerMeter` |              1.0 |
| SI rayl                 | `rayl`       |                 `rayls` |              1.0 |
| CGS rayl                | `rayl (CGS)` |              `cgsRayls` |               10 |

`rayls` is a second spelling of the base unit, not a unit of its own. All tokens accept every SI prefix
(`mega.rayls` is the usual one for tissue and water). Like the neighbouring force, pressure and density
groups the instance stores its **raw gram-based component value**.

## Decompositions

The group has **two** decompositions. Both funnel into the same normalizing factory:

| Form             | Expression                                                    |
|------------------|----------------------------------------------------------------|
| typed operator A | `pressure / speed`                                            |
| typed operator B | `density * speed` (`Z = ρ · c`, the characteristic impedance) |
| native (`toX()`) | `(1 of kilo.grams / m² / s).toAcousticImpedance()`            |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.acousticimpedance.*

val air = (1.204 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val c = (343 of meters) / (1 of seconds)

val viaDensity = air * c                                        // B
val viaPressure = (412.972 of pascals) / ((1 of meters) / (1 of seconds))  // A

viaDensity into rayls        // ≈ 412.97
viaPressure into rayls       // ≈ 412.97
```

## Computing with the group

| Expression                       | Result type                        | Meaning                |
|----------------------------------|------------------------------------|------------------------|
| `pressure / speed`               | `KAcousticImpedanceUnitInstance`   | `Z = p / v`            |
| `density * speed`                | `KAcousticImpedanceUnitInstance`   | `Z = ρ · c`            |
| `acousticImpedance * speed`      | `KPressureUnitInstance`            | the sound pressure     |
| `pressure / acousticImpedance`   | `KSpeedUnitInstance`               | the particle velocity  |
| `acousticImpedance / speed`      | `KDensityUnitInstance`             | back to `ρ`            |
| `acousticImpedance / density`    | `KSpeedUnitInstance`               | back to `c`            |

## Real-world example — the air/water boundary

Why does shouting at a swimmer's head under water not work? Compare the two characteristic impedances:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.acousticimpedance.*

val air = (1.204 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))
val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

val zAir = air * ((343 of meters) / (1 of seconds))
val zWater = water * ((1480 of meters) / (1 of seconds))

zAir into rayls              // ≈ 413
zWater into mega.rayls       // ≈ 1.48

(zWater into rayls) / (zAir into rayls)   // ≈ 3584 — almost total reflection
```

## Value semantics

`equals`/`hashCode` compare the **normalized component value**, so `(1 of cgsRayls) == (10 of rayls)`.
`toString()` renders the value in the base unit: `"413.0 Pa*s/m"`.

## See also

* [Density](density.md) and [Speed](../kinematics/speed.md) — the two factors of `Z = ρ · c`.
* [Pressure](pressure.md) — the sound pressure side.
* [Mechanics overview](overview.md)
