# Moment of Inertia

Package: `org.pcsoft.framework.kunit.mechanic.inertia`
Base unit: **kilogram meter squared** (`KInertiaUnit.BASE == KInertiaUnit.KILOGRAM_METERS_SQUARED`)

Type: **constructed unit**

The moment of inertia `J` is the rotational counterpart of the [mass](mass.md): it says how strongly a body resists a
change of its rotation. It is a **constructed** unit — the composition `mass · length²` (`kg·m²`).

`KInertiaUnitInstance` wraps a `KMixedUnitInstance` of exactly two terms in the canonical normal form:
`KMassUnit.BASE` (gram) at `+1` and `KDistanceUnit.BASE` (meter) at `+2`. Since the mass component of this library is
normalized to grams, the stored value is the raw gram-based component value and readings in kg·m² divide by a fixed
factor.

## Named units

| Unit                    | Symbol    |                    Token | 1 unit in kg·m² |
|-------------------------|-----------|-------------------------:|----------------:|
| Kilogram meter squared  | `kg*m^2`  |  `kilogramMetersSquared` |             1.0 |
| Gram centimeter squared | `g*cm^2`  | `gramCentimetersSquared` |            1e-7 |
| Pound-foot squared      | `lb*ft^2` |       `poundFeetSquared` |     ≈ 0.0421401 |

All units accept the full SI prefix range (`milli.kilogramMetersSquared` for small servo rotors).

## Decompositions

| Form              | Kotlin                                                  | Result type            |
|-------------------|---------------------------------------------------------|------------------------|
| mass × area       | `mass * area`                                           | `KInertiaUnitInstance` |
| native expression | `(mass.toUnit() * (length.toUnit() pow 2)).toInertia()` | `KInertiaUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.inertia.*
import org.pcsoft.framework.kunit.mechanic.mass.grams

val typed = (2 of kilo.grams) * ((3 of meters) * (3 of meters))
val native = ((2000 of grams).toUnit() * ((3 of meters).toUnit() pow 2)).toInertia()

typed == native                     // true
typed into kilogramMetersSquared    // 18.0
```

## Computing with the core units

| Expression                      | Result type                    | Meaning                                             |
|---------------------------------|--------------------------------|-----------------------------------------------------|
| `mass * area`, `area * mass`    | `KInertiaUnitInstance`         | `J = m · r²`                                        |
| `inertia / mass`                | `KAreaUnitInstance`            | squared radius of gyration `r² = J / m`             |
| `inertia / area`                | `KMassUnitInstance`            | `m = J / r²`                                        |
| `inertia * angularvelocity`     | `KAngularMomentumUnitInstance` | [angular momentum](angular-momentum.md) `L = J · ω` |
| `inertia * angularacceleration` | `KEnergyUnitInstance`          | [torque](torque.md) `M = J · α`                     |

## Real-world example: flywheel of a press

A solid flywheel disc (`J = ½ · m · r²`) has a mass of 40 kg and a radius of 0.3 m. What is its moment of inertia, and
which angular momentum does it carry at 1500 rpm?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerMinute
import org.pcsoft.framework.kunit.mechanic.inertia.*

val r = 0.3 of meters
val j = ((40 of kilo.grams) * (r * r)) / 2  // ½ · m · r²
j into kilogramMetersSquared                // 1.8

val l = j * (1500 of revolutionsPerMinute)  // KAngularMomentumUnitInstance
l into kilogramMetersSquaredPerSecond       // ≈ 282.74
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.inertia.*

val total = (10 of kilogramMetersSquared) + (4 of kilogramMetersSquared) // 14 kg·m²
(10 of kilogramMetersSquared) > (4 of kilogramMetersSquared)            // true
(10 of kilogramMetersSquared) * (2 of kilogramMetersSquared)            // KMixedUnitInstance
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.inertia.*

(18 of kilogramMetersSquared).toString()                       // "18.0 kg*m^2" (base unit)
"${(18 of kilogramMetersSquared) into poundFeetSquared} lb*ft^2" // "427.1... lb*ft^2"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics  | Kotlin                          | Meaning                                    |
|--------------|---------------------------------|--------------------------------------------|
| `kg·m²`      | `kilogramMetersSquared`         | moment of inertia, base unit (named token) |
| `kg·m^2`     | `kilo.grams * (meters pow 2)`   | same quantity as a pure product            |
| `J = m · r²` | `mass * area`                   | typed decomposition                        |
| `r² = J / m` | `inertia / mass`                | squared radius of gyration                 |
| `L = J · ω`  | `inertia * angularvelocity`     | angular momentum                           |
| `M = J · α`  | `inertia * angularacceleration` | torque                                     |
