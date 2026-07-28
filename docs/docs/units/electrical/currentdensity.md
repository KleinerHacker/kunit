# Current Density

Package: `org.pcsoft.framework.kunit.electric.currentdensity`
Base unit: **ampere per square meter** (`KCurrentDensityUnit.BASE == KCurrentDensityUnit.AMPERE_PER_SQUARE_METER`)

Type: **constructed unit**

Current density is a **constructed** unit: the composition `current · length⁻²` (`A/m²`) — the electric
current per conductor cross-section. `KCurrentDensityUnitInstance` wraps a `KMixedUnitInstance` of two terms —
`KElectricCurrentUnit.BASE` (ampere) at `+1` and `KDistanceUnit.BASE` (meter) at `-2`. Both components are
stored in their group base units, so the value is directly the reading in A/m².

## Building a current density

Current density has **no named tokens** and no prefix builders of its own: every spelling is a ratio
(`A/m²`, `A/mm²`, …). Build it as an expression or with the typed `current / area` operator, and read it back
with `into` against such an expression:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

val crossSection = (2.5 of milli.meters) * (1 of milli.meters)  // 2.5 mm²
val j = (16 of amperes) / crossSection                          // KCurrentDensityUnitInstance

j into (amperes / (meters pow 2))       // 6.4e6
j into (amperes / (milli.meters pow 2)) // 6.4
```

## Multiple decompositions

| Expression | Result type | Meaning |
|---|---|---|
| `current / area` | `KCurrentDensityUnitInstance` | definition `J = I / A` |
| `current/length²` | via `.toCurrentDensity()` | native canonical `A·m⁻²` expression |

The typed operator form returns a current density directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toCurrentDensity()` (which recognises only the canonical normal
form and throws `IllegalStateException` otherwise). Both routes are value-equal.

The inverse operators tie current, area and current density together:

| Expression | Result type | Meaning |
|---|---|---|
| `currentDensity * area` | `KElectricCurrentUnitInstance` | `I = J · A` (commutative) |
| `current / currentDensity` | `KAreaUnitInstance` | `A = I / J` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

// Real-world example - wire sizing: 16 A through a 2.5 mm² copper wire is 6.4 A/mm².
val j = (16 of amperes) / ((2.5 of milli.meters) * (1 of milli.meters))
j into (amperes / (milli.meters pow 2))     // 6.4

// Solved for the current a given cross-section may carry at that density:
val i = j * ((4 of milli.meters) * (1 of milli.meters))  // KElectricCurrentUnitInstance, 25.6 A

// The same density as the native A·m⁻² expression:
val raw = (16 of amperes).toUnit() / (2.5e-6 of (meters pow 2))
raw.toCurrentDensity() == j                 // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

val a = (3 of amperes) / ((1 of meters) * (1 of meters))
val b = (1 of amperes) / ((1 of meters) * (1 of meters))
(a + b) into (amperes / (meters pow 2))  // 4.0
a > b                                     // true
a * b                                     // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

((5 of amperes) / ((1 of meters) * (1 of meters))).toString()  // "5.0 A/m²" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `⁻²`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `A/m²` | `amperes / (meters pow 2)` | current density, base unit (fraction form) |
| `A·m⁻²` | `amperes * (meters pow -2)` | same current density as a pure product |
| `I / A` | `(16 of amperes) / crossSection` | current density from current and area |
| `A/mm²` | `amperes / (milli.meters pow 2)` | current density in the common wiring unit |
