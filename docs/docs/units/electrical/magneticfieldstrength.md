# Magnetic Field Strength

Package: `org.pcsoft.framework.kunit.electric.magneticfieldstrength`
Base unit: **ampere per meter** (`KMagneticFieldStrengthUnit.BASE == KMagneticFieldStrengthUnit.AMPERE_PER_METER`)

Type: **constructed unit**

Magnetic field strength (the magnetizing field `H`) is a **constructed** unit: the composition
`current · length⁻¹` (`A/m`). `KMagneticFieldStrengthUnitInstance` wraps a `KMixedUnitInstance` of two terms —
`KElectricCurrentUnit.BASE` (ampere) at `+1` and `KDistanceUnit.BASE` (meter) at `-1`. The stored value is always
normalized to amperes per meter.

Related pages: [Electric Current](ec.md) and [Length](../kinematics/distance.md) are the two component groups of this
unit.

## Building a magnetic field strength

Build a field strength with a named token, or from a decomposition (see below). Named units survive as value-1 tokens
(used with `of`/`into`):

| Magnetic field strength | Symbol  |                   Token |     1 unit in A/m |
|-------------------------|---------|------------------------:|------------------:|
| Ampere per meter        | `A/m`   |       `amperesPerMeter` |               1.0 |
| Oersted (CGS-EMU)       | `Oe`    |              `oersteds` | 79.57747154594767 |
| Gilbert per centimeter  | `Gb/cm` | `gilbertsPerCentimeter` | 79.57747154594767 |
| Ampere-turn per inch    | `At/in` |    `ampereTurnsPerInch` | 39.37007874015748 |

Named units support the SI prefixes via `KPrefixBuilder` (`kilo.amperesPerMeter`, `milli.oersteds`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.*

val h = 470 of amperesPerMeter
h into amperesPerMeter                  // 470.0
h into kilo.amperesPerMeter             // 0.47
(1 of kilo.amperesPerMeter) into amperesPerMeter // 1000.0
```

## Multiple decompositions

Magnetic field strength can be reached through several **equivalent decompositions**, all producing the same value-equal
field strength:

| Expression         | Result type                          | Meaning                             |
|--------------------|--------------------------------------|-------------------------------------|
| `current / length` | `KMagneticFieldStrengthUnitInstance` | defining relation `H = I / l`       |
| `current·length⁻¹` | via `.toMagneticFieldStrength()`     | native canonical `A·m⁻¹` expression |

The typed operator form returns a field strength directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toMagneticFieldStrength()` (which recognises only the canonical normal form
and throws `IllegalStateException` otherwise). Both routes are value-equal.

The inverse operators tie current, length and field strength together:

| Expression               | Result type                    | Meaning          |
|--------------------------|--------------------------------|------------------|
| `fieldStrength * length` | `KElectricCurrentUnitInstance` | `I = H · l`      |
| `length * fieldStrength` | `KElectricCurrentUnitInstance` | commutative form |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.*

// Real-world example - a coil of 500 turns carrying 2 A over a length of 0.25 m:
// H = N · I / l = 500 · 2 A / 0.25 m = 4000 A/m
val h = (1000 of amperes) / (0.25 of meters)  // KMagneticFieldStrengthUnitInstance, 4000 A/m

// The same field strength as the native A·m⁻¹ expression:
val raw = 4000 of (amperes pow 1) / (meters pow 1)
raw.toMagneticFieldStrength() == (4000 of amperesPerMeter)  // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.*

val s = (100 of amperesPerMeter) + (40 of amperesPerMeter)  // 140 A/m
(100 of amperesPerMeter) > (40 of amperesPerMeter)          // true
(100 of amperesPerMeter) * (40 of amperesPerMeter)          // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.magneticfieldstrength.*

(470 of amperesPerMeter).toString()     // "470.0 A/m" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin                              | Meaning                                            |
|-------------|-------------------------------------|----------------------------------------------------|
| `A/m`       | `amperesPerMeter`                   | magnetic field strength, base unit (named token)   |
| `A/m`       | `(amperes pow 1) / (meters pow 1)`  | field strength as current / length (fraction form) |
| `A·m⁻¹`     | `(amperes pow 1) * (meters pow -1)` | same field strength as a pure product              |
| `kA/m`      | `kilo.amperesPerMeter`              | prefixed field strength (kiloampere per meter)     |
