# Magnetic Dipole Moment

Package: `org.pcsoft.framework.kunit.electric.magneticmoment`
Base unit: **ampere square metre**
(`KMagneticMomentUnit.BASE == KMagneticMomentUnit.AMPERE_SQUARE_METER`)

Type: **constructed unit**

The magnetic dipole moment `m` of a current loop is the current times the area it encloses: `m = I · A`.
It is what determines the torque a magnetic field exerts on the loop, and the quantity in which atomic and
nuclear magnetism is expressed (Bohr and nuclear magneton).

Its canonical base-dimension normal form is `current · length²`.

## Named units

| Unit                | Symbol  |                Token |     1 unit in A·m² |
|---------------------|---------|---------------------:|-------------------:|
| Ampere square metre | `A*m^2` | `ampereSquareMeters` |                1.0 |
| Joule per tesla     | `J/T`   |      `joulesPerTesla` |                1.0 |
| Bohr magneton       | `μB`    |       `bohrMagnetons` | 9.2740100783e-24   |
| Nuclear magneton    | `μN`    |    `nuclearMagnetons` | 5.0507837461e-27   |

`joulesPerTesla` is the energy-based spelling of the same unit — the energy a dipole gains per unit of
magnetic flux density. All tokens accept every SI prefix.

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance:

| Form             | Expression                                                       |
|------------------|-------------------------------------------------------------------|
| typed operator   | `current * area`                                                 |
| native (`toX()`) | `((2 of amperes).toUnit() * loop.toUnit()).toMagneticMoment()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.magneticmoment.*

val loop = (0.1 of meters) * (0.05 of meters)      // 0.005 m²

val typed = (2 of amperes) * loop
val native = ((2 of amperes).toUnit() * loop.toUnit()).toMagneticMoment()

typed == native                 // true
typed into ampereSquareMeters   // 0.01
```

## Computing with the group

| Expression                  | Result type                      | Meaning          |
|-----------------------------|----------------------------------|------------------|
| `current * area`            | `KMagneticMomentUnitInstance`    | `m = I · A`      |
| `magneticMoment / area`     | `KElectricCurrentUnitInstance`   | the loop current |
| `magneticMoment / current`  | `KAreaUnitInstance`              | the loop area    |

## Real-world example — a coil loop and an atom

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.magneticmoment.*

val loop = (0.1 of meters) * (0.05 of meters)
val m = (2 of amperes) * loop
m into ampereSquareMeters          // 0.01

// How many Bohr magnetons is that?
m into bohrMagnetons                // ≈ 1.078e21

// And the reverse: what current would a 1 cm² loop need for 1 A·m²?
val small = (0.01 of meters) * (0.01 of meters)
((1 of ampereSquareMeters) / small) into amperes   // 10 000 A
```

## Value semantics

`equals`/`hashCode` compare the **normalized A·m² value**, so
`(1 of ampereSquareMeters) == (1 of joulesPerTesla)`. `toString()` renders the value in the base unit:
`"0.01 A*m^2"`.

## See also

* [Magnetic Flux Density](magneticfluxdensity.md) — the field this moment interacts with.
* [Electric Current](ec.md) and [Distance](../kinematics/distance.md) — the two factors.
* [Electrical Engineering overview](overview.md)
