# Pressure

Package: `org.pcsoft.framework.kunit.pressure`
Base unit: **pascal** (`KPressureUnit.BASE == KPressureUnit.PASCAL`)

Type: **constructed unit**

Pressure is a **constructed** unit: the composition `mass · length⁻¹ · time⁻²` (`kg/(m·s²)` = `N/m²`).
`KPressureUnitInstance` wraps a `KMixedUnitInstance` of three terms — `KMassUnit.BASE` (gram) at `+1`,
`KDistanceUnit.BASE` (meter) at `-1` and `KTimeUnit.BASE` (second) at `-2`. As with force, the stored value is
the raw gram-based component value and readings in pascals divide by a fixed factor.

## Building a pressure

Build a pressure from `force / area`, or with a named token. Named units survive as value-1 tokens (used with
`of`/`into`):

| Pressure | Symbol | Token | 1 unit in Pa |
|---|---|---:|---:|
| Pascal | `Pa` | `pascals` | 1.0 |
| Bar | `bar` | `bars` | 100000.0 |
| Atmosphere | `atm` | `atmospheres` | 101325.0 |
| Pound per square inch | `psi` | `psis` | 6894.757 |
| Torr (mmHg) | `Torr` | `torrs` | 133.322 |

Prefix-derivable spellings are **not** dedicated tokens: **hPa** = `hecto.pascals`, **kPa** = `kilo.pascals`,
and the statics unit **N/mm² = MPa** = `mega.pascals` (or the expression `newtons / (milli.meters pow 2)`).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.pressure.*

val p = 2 of bars
p into pascals               // 200000.0
p into atmospheres           // ≈ 1.974
(1 of mega.pascals) into pascals // 1000000.0 (= 1 N/mm²)
```

## Computing with the core units (force & area)

| Expression | Result type | Meaning |
|---|---|---|
| `force / area` | `KPressureUnitInstance` | pressure = F / A |
| `pressure * area` | `KForceUnitInstance` | force = p · A |
| `area * pressure` | `KForceUnitInstance` | force (commutative) |
| `force / pressure` | `KAreaUnitInstance` | area = F / p |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.force.newtons
import org.pcsoft.framework.kunit.pressure.*

val area = (2 of meters) * (1 of meters)   // KAreaUnitInstance, 2 m²
val p = (100 of newtons) / area            // KPressureUnitInstance, 50 Pa
val f = p * area                           // KForceUnitInstance, 100 N
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pressure.*

val s = (10 of pascals) + (4 of pascals)  // 14 Pa
(2 of bars) > (1 of atmospheres)          // true
(10 of pascals) * (2 of pascals)          // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pressure.*

(50 of pascals).toString()   // "50.0 Pa" (base unit)
"${(1 of bars) into pascals} Pa" // "100000.0 Pa"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `Pa` | `pascals` | pressure, base unit (named token, pascal) |
| `N/m²` | `newtons / (meters pow 2)` | pressure as force / area (fraction form) |
| `kg·m⁻¹·s⁻²` | `kilo.grams * (meters pow -1) * (seconds pow -2)` | same pressure as a pure product |
| `kPa` | `kilo.pascals` | prefixed pressure (kilopascal) |
