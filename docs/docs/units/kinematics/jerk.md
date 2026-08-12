# Jerk

Package: `org.pcsoft.framework.kunit.kinematic.jerk`
Base unit: **metre per second cubed** (`KJerkUnit.BASE == KJerkUnit.METER_PER_SECOND_CUBED`)

Type: **constructed unit**

Jerk `j` is the rate at which an **acceleration** changes: `j = Δa / t`. It is the quantity ride-comfort
standards actually limit — a lift or a train may accelerate hard, but the acceleration must not change
abruptly, or passengers lurch. Comfort limits sit around 0.5 m/s³.

Its canonical base-dimension normal form is `length · time⁻³`.

## Named units

| Unit                       | Symbol   |                        Token | 1 unit in m/s³ |
|----------------------------|----------|-----------------------------:|---------------:|
| Metre per second cubed     | `m/s^3`  |       `metersPerSecondCubed` |            1.0 |
| Standard gravity per second | `g/s`   | `standardGravitiesPerSecond` |        9.80665 |
| Foot per second cubed      | `ft/s^3` |          `feetPerSecondCubed` |         0.3048 |

All tokens accept every SI prefix (`milli.metersPerSecondCubed`, …).

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance:

| Form             | Expression                                                        |
|------------------|--------------------------------------------------------------------|
| typed operator   | `acceleration / time`                                             |
| native (`toX()`) | `(acceleration.toUnit() / (2 of seconds).toUnit()).toJerk()`      |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.acceleration.gals
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.jerk.*

val a = 120 of gals                    // 1.2 m/s² (1 Gal = 0.01 m/s²)

val typed = a / (2 of seconds)
val native = (a.toUnit() / (2 of seconds).toUnit()).toJerk()

typed == native                        // true
typed into metersPerSecondCubed        // 0.6
```

## Computing with the group

| Expression             | Result type                    | Meaning                     |
|------------------------|--------------------------------|-----------------------------|
| `acceleration / time`  | `KJerkUnitInstance`            | `j = Δa / t`                |
| `jerk * time`          | `KAccelerationUnitInstance`    | the acceleration built up   |
| `acceleration / jerk`  | `KTimeUnitInstance`            | how long the ramp takes     |

## Real-world example — a lift ramp within the comfort limit

A lift is to reach **1 m/s²** without exceeding a jerk of **0.5 m/s³**. How long must the ramp be?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.acceleration.gals
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.jerk.*

val target = 100 of gals                        // 1 m/s²
val comfort = 0.5 of metersPerSecondCubed

val ramp = target / comfort                     // KTimeUnitInstance
ramp into seconds                                // 2.0 s

// And the other way round: what jerk does a 1 s ramp impose?
val harsh = target / (1 of seconds)
harsh into metersPerSecondCubed                  // 1.0 — twice the comfort limit
```

## Value semantics

`equals`/`hashCode` compare the **normalized m/s³ value**, so
`(1 of metersPerSecondCubed) == (1000 of milli.metersPerSecondCubed)`. `toString()` renders the value in
the base unit: `"0.6 m/s^3"`.

## See also

* [Acceleration](acceleration.md) — the quantity this one is the rate of change of.
* [Speed](speed.md) and [Distance](distance.md) — the rest of the motion chain.
* [Kinematics overview](overview.md)
