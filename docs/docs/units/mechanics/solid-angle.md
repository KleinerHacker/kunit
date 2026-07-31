# Solid Angle

Package: `org.pcsoft.framework.kunit.mechanic.solidangle`
Base unit: **steradian** (`KSolidAngleUnit.BASE == KSolidAngleUnit.STERADIAN`)

Type: **constructed unit**

The solid angle is the two-dimensional angle: the fraction of a sphere's surface a cone cuts out. It is a
**constructed** unit — `1 sr = 1 rad²` — but because the steradian is an independently named SI unit with its own
vocabulary (square degree, spat), it is modelled as its own group with a single-term wrapper.

`KSolidAngleUnitInstance` wraps a `KMixedUnitInstance` of one `KSolidAngleUnit.BASE` term at exponent 1, always
normalized to steradians. The bridge to the [angle](angle.md) group is the typed operator
`angle * angle` and the form-recognition hook `toSolidAngle()`, which also accepts the native `rad²` form.

## Named units

| Unit               | Symbol |           Token |          1 unit in sr |
|--------------------|--------|----------------:|----------------------:|
| Steradian          | `sr`   |    `steradians` |                   1.0 |
| Square degree      | `deg²` | `squareDegrees` | (π/180)² ≈ 3.04617e-4 |
| Spat (full sphere) | `sp`   |         `spats` |          4π ≈ 12.5664 |

All units accept the full SI prefix range (`milli.steradians`, `micro.steradians`).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val full = 1 of spats
full into steradians    // ≈ 12.566
full into squareDegrees // ≈ 41252.96 (the whole sky)
```

## Decompositions

A solid angle can be reached in two equivalent ways; both reduce onto the same canonical value.

| Form              | Kotlin                                  | Result type               |
|-------------------|-----------------------------------------|---------------------------|
| typed operator    | `angle * angle`                         | `KSolidAngleUnitInstance` |
| native expression | `(angle.toUnit() pow 2).toSolidAngle()` | `KSolidAngleUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val typed = (90 of degrees) * (90 of degrees)
val native = ((90 of degrees).toUnit() pow 2).toSolidAngle()

typed == native            // true - both are (π/2)² sr ≈ 2.4674 sr
typed into steradians      // ≈ 2.4674
```

## Computing with plane angles

| Expression                | Result type               | Meaning                   |
|---------------------------|---------------------------|---------------------------|
| `angle * angle`           | `KSolidAngleUnitInstance` | solid angle `Ω = φ²`      |
| `solidangle / angle`      | `KAngleUnitInstance`      | the remaining plane angle |
| `solidangle + solidangle` | `KSolidAngleUnitInstance` | same-type arithmetic      |

## Real-world example: LED beam angle

An LED emits into a square beam of 30° × 30°. What solid angle does it illuminate, and which fraction of the full sphere
is that?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val beam = (30 of degrees) * (30 of degrees)
beam into steradians    // ≈ 0.2742
beam into squareDegrees // 900.0
beam into spats         // ≈ 0.0218 (about 2.2 % of the sphere)
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val sum = (3 of steradians) + (1 of steradians) // 4 sr
(1 of spats) > (10 of steradians)               // true
(3 of steradians) * (2 of steradians)           // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.*

(2 of steradians).toString()               // "2.0 sr" (base unit)
"${(1 of spats) into squareDegrees} deg²"  // "41252.96... deg²"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics   | Kotlin                                    | Meaning                                          |
|---------------|-------------------------------------------|--------------------------------------------------|
| `sr`          | `steradians`                              | solid angle, base unit                           |
| `deg²`        | `squareDegrees`                           | square degree                                    |
| `rad²`        | `(radians.toUnit() pow 2).toSolidAngle()` | solid angle as squared plane angle (native form) |
| `Ω = φ₁ · φ₂` | `angle * angle`                           | typed decomposition                              |
| `φ = Ω / φ₁`  | `solidangle / angle`                      | solved for the plane angle                       |
| `msr`         | `milli.steradians`                        | prefixed solid angle                             |
