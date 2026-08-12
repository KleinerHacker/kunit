# Second Moment of Area

Package: `org.pcsoft.framework.kunit.kinematic.distance`
Base unit: **quartic metre** (`m⁴`, the exponent-4 leaf of the distance group)

Type: **constructed unit**

The second moment of area `I` (area moment of inertia) is the geometric property that decides how stiff a
beam cross-section is in bending — the `I` in the flexural rigidity `EI`. Steel-profile tables quote it in
`cm⁴`, small sections in `mm⁴`.

Unlike the other groups on this site it is not a group of its own: it is the **exponent-4 leaf** of the
distance group, `KSecondMomentOfAreaUnitInstance`, sitting beside
[length](../kinematics/distance.md) (exponent 1), area (2) and volume (3).

!!! warning "Not the moment of inertia"
    Do not confuse this with the *mass* [moment of inertia](moment-of-inertia.md) (`kg·m²`), which
    describes resistance to angular acceleration. The names are similar, the dimensions are not.

## Named tokens

| Unit                  | Symbol |                Token | 1 unit in m⁴ |
|-----------------------|--------|---------------------:|-------------:|
| Quartic metre         | `m⁴`   |       `quarticMeters` |          1.0 |
| Quartic centimetre    | `cm⁴`  |  `quarticCentimeters` |         1e-8 |
| Quartic millimetre    | `mm⁴`  |  `quarticMillimeters` |        1e-12 |
| Quartic inch          | `in⁴`  |       `quarticInches` | ≈ 4.16231e-7 |

All tokens accept every SI prefix.

## Computing with the leaf

Every product that lands on exponent 4 now returns the typed leaf instead of the general
`KDistanceUnitInstance`:

| Expression                    | Result type                          | Meaning                    |
|-------------------------------|--------------------------------------|----------------------------|
| `area * area`                 | `KSecondMomentOfAreaUnitInstance`    | m² · m² = m⁴               |
| `volume * length`             | `KSecondMomentOfAreaUnitInstance`    | m³ · m = m⁴                |
| `length * volume`             | `KSecondMomentOfAreaUnitInstance`    | m · m³ = m⁴                |
| `secondMomentOfArea / length` | `KVolumeUnitInstance`                | the section modulus        |
| `secondMomentOfArea / area`   | `KAreaUnitInstance`                  | m⁴/m² = m²                 |
| `secondMomentOfArea / volume` | `KLengthUnitInstance`                | m⁴/m³ = m                  |
| `secondMomentOfArea + …`      | `KSecondMomentOfAreaUnitInstance`    | parts of a built-up section |

Adding is restricted to the same dimension — `secondMomentOfArea + area` is a **compile error**, exactly
like `length + area`.

The native form converts with `toSecondMomentOfArea()`:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*

val native = ((1 of centi.meters).toUnit() pow 4).toSecondMomentOfArea()
native into quarticCentimeters      // 1.0
```

## Real-world example — a rectangular beam

For a rectangle of width `b` and height `h`, `I = b·h³/12`. For 100 mm × 200 mm:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*

val b = 100 of milli.meters
val h = 200 of milli.meters

val i = (b * (h * h * h)) / 12       // KSecondMomentOfAreaUnitInstance
i into quarticCentimeters             // ≈ 6666.7 cm⁴

// The section modulus W = I / (h/2)
val w = i / (h / 2)                   // KVolumeUnitInstance
w.value                                // ≈ 6.667e-4 m³

// A built-up section: two such beams side by side
val doubled = i + i
doubled into quarticCentimeters        // ≈ 13333.3
```

## Value semantics

`equals`/`hashCode` and comparison work on the normalized `m⁴` value, restricted to the same dimension.
`exponent` reports `4`.

## See also

* [Distance](../kinematics/distance.md) — the group this leaf belongs to.
* [Moment of Inertia](moment-of-inertia.md) — the *mass*-based quantity with the similar name.
* [Mechanics overview](overview.md)
