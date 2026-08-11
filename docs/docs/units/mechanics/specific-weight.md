# Specific Weight

Package: `org.pcsoft.framework.kunit.mechanic.specificweight`
Base unit: **newton per cubic metre**
(`KSpecificWeightUnit.BASE == KSpecificWeightUnit.NEWTON_PER_CUBIC_METER`)

Type: **constructed unit**

The specific weight `γ` is the **weight force** of a material per unit of volume: `γ = F / V = ρ · g`. It
is what hydrostatics is written in — the pressure at a depth is simply `p = γ · h` — and what civil
engineering quotes for soils and construction materials. Water is ≈ 9.81 kN/m³.

Its canonical base-dimension normal form is `mass · length⁻² · time⁻²`.

!!! note "Weight, not mass"
    Specific weight depends on the local gravitational acceleration; the [density](density.md) does not.
    On the Moon a material keeps its density but has about a sixth of its specific weight.

## Named units

| Unit                       | Symbol     |                     Token | 1 unit in N/m³ |
|----------------------------|------------|--------------------------:|---------------:|
| Newton per cubic metre     | `N/m^3`    |    `newtonsPerCubicMeter` |            1.0 |
| Kilonewton per cubic metre | `kN/m^3`   | `kilonewtonsPerCubicMeter` |           1000 |
| Pound-force per cubic foot | `lbf/ft^3` | `poundsForcePerCubicFoot` |     ≈ 157.0875 |

All tokens accept every SI prefix. Like the neighbouring force, pressure and density groups, the instance
stores its **raw gram-based component value**; readings in N/m³ divide by 1000.

## Decompositions

The group has **two** decompositions. Both funnel into the same normalizing factory:

| Form             | Expression                                                        |
|------------------|--------------------------------------------------------------------|
| typed operator A | `force / volume`                                                  |
| typed operator B | `density * acceleration` (`γ = ρ · g`)                            |
| native (`toX()`) | `(1 of kilo.grams / m² / s²).toSpecificWeight()`                  |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.acceleration.standardGravities
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.specificweight.*

val cubicMeter = (1 of meters) * (1 of meters) * (1 of meters)
val water = (1000 of kilo.grams) / cubicMeter

val viaForce = (9806.65 of newtons) / cubicMeter        // A
val viaDensity = water * (1 of standardGravities)       // B

viaForce == viaDensity                                   // true
viaForce into newtonsPerCubicMeter                       // 9806.65
```

## Computing with the group

| Expression                     | Result type                    | Meaning              |
|--------------------------------|--------------------------------|----------------------|
| `force / volume`               | `KSpecificWeightUnitInstance`  | `γ = F / V`          |
| `density * acceleration`       | `KSpecificWeightUnitInstance`  | `γ = ρ · g`          |
| `specificWeight * volume`      | `KForceUnitInstance`           | the weight force     |
| `force / specificWeight`       | `KVolumeUnitInstance`          | the volume it fills  |
| `specificWeight / acceleration` | `KDensityUnitInstance`        | back to `ρ`          |
| `specificWeight / density`     | `KAccelerationUnitInstance`    | back to `g`          |

## Real-world example — the weight of a water tank

A **300 l** tank of water, and the force its content exerts on the floor:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.specificweight.*

val water = 9.80665 of kilonewtonsPerCubicMeter
val weight = water * (300 of liters)      // KForceUnitInstance
weight into newtons                        // ≈ 2942.0 N

// And back: what volume weighs 1 kN?
val v = (1000 of newtons) / water          // KVolumeUnitInstance
v into liters                               // ≈ 102.0 l
```

## Value semantics

`equals`/`hashCode` compare the **normalized component value**, so
`(1 of kilonewtonsPerCubicMeter) == (1000 of newtonsPerCubicMeter)`. `toString()` renders the value in the
base unit: `"9807.0 N/m^3"`.

## See also

* [Density](density.md) — the mass-based counterpart, independent of gravity.
* [Force](force.md) and [Pressure](pressure.md) — the neighbouring groups.
* [Mechanics overview](overview.md)
