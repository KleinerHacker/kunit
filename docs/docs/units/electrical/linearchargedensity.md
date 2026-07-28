# Linear Charge Density

Package: `org.pcsoft.framework.kunit.electric.linearchargedensity`
Base unit: **coulomb per meter**
(`KLinearChargeDensityUnit.BASE == KLinearChargeDensityUnit.COULOMB_PER_METER`)

Type: **constructed unit**

Linear charge density is a **constructed** unit: the composition `current · time · length⁻¹`
(`A·s·m⁻¹` = `C/m`). `KLinearChargeDensityUnitInstance` wraps a `KMixedUnitInstance` of three terms —
`KElectricCurrentUnit.BASE` (ampere) at `+1`, `KTimeUnit.BASE` (second) at `+1` and `KDistanceUnit.BASE`
(meter) at `-1`. The group carries no mass dimension, so no gram/kilogram bridge is needed; the stored value
is always normalized to coulombs per meter.

The linear charge density `λ` is the charge carried per unit of length, e.g. along a wire or a charged
filament. It has **no named unit of its own**: every spelling is a ratio (C/m, µC/cm), so the group has no
bare tokens and no prefix builders — values are built from an expression or through the typed operators. The
two- and three-dimensional counterparts are the [Electric Flux Density](electricfluxdensity.md) (C/m²) and
the [Charge Density](chargedensity.md) (C/m³).

## Building a linear charge density

There are no named tokens. Build a value from a charge over a length:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

val lambda = (5 of micro.coulombs) / (2 of meters)  // 2.5e-6 C/m
lambda.value                                        // 2.5e-6 (normalized to C/m)
```

## Multiple decompositions

Linear charge density can be reached through several **equivalent decompositions**, all producing the same
value-equal density:

| Expression | Result type | Meaning |
|---|---|---|
| `charge / length` | `KLinearChargeDensityUnitInstance` | `λ = Q / l`, the charge spread along a length |
| `current·time/length` | via `.toLinearChargeDensity()` | native canonical `A·s·m⁻¹` expression |

The typed operator form returns a linear charge density directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toLinearChargeDensity()` (which recognises only the canonical
normal form and throws `IllegalStateException` otherwise). Both routes are value-equal.

The inverse operators tie charge, length and density together:

| Expression | Result type | Meaning |
|---|---|---|
| `linearChargeDensity * length` | `KChargeUnitInstance` | `Q = λ · l` (commutative) |
| `charge / linearChargeDensity` | `KLengthUnitInstance` | `l = Q / λ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

// Real-world example - a filament carrying 5 µC over 2 m has a linear charge density of 2.5 µC/m.
val lambda = (5 of micro.coulombs) / (2 of meters)   // 2.5e-6 C/m

// Solved back for the charge:
val q = lambda * (2 of meters)                       // KChargeUnitInstance, 5 µC
q into micro.coulombs                                // 5.0

// The same density as the native A·s·m⁻¹ expression:
val raw = 2.5e-6 of ((amperes pow 1) * (seconds pow 1)) / (meters pow 1)
raw.toLinearChargeDensity() == lambda                // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

val a = (2 of coulombs) / (1 of meters)
val b = (3 of coulombs) / (1 of meters)
(a + b).value    // 5.0 C/m
b > a            // true
(a * b)          // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.linearchargedensity.*

((2 of coulombs) / (1 of meters)).toString()   // "2.0 C/m" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `C/m` | `(1 of coulombs) / (1 of meters)` | linear charge density, base unit (no named token) |
| `Q / l` | `(5 of micro.coulombs) / (2 of meters)` | density from charge along a length |
| `λ · l` | `lambda * (2 of meters)` | the charge carried by a length |
| `A·s/m` | `((amperes pow 1) * (seconds pow 1)) / (meters pow 1)` | density as current·time / length (fraction form) |
| `A·s·m⁻¹` | `(amperes pow 1) * (seconds pow 1) * (meters pow -1)` | same density as a pure product |
