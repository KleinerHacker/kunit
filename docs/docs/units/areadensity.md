# Area Density

Package: `org.pcsoft.framework.kunit.areadensity`
Base unit: **kilogram per square meter** (`KAreaDensityUnit.BASE == KAreaDensityUnit.KILOGRAM_PER_SQUARE_METER`)

Area density (surface mass / areal load, common in construction statics) is a **constructed** unit: the
composition `mass · length⁻²` (`kg/m²`). `KAreaDensityUnitInstance` wraps a `KMixedUnitInstance` of two
terms — `KMassUnit.BASE` (gram) at `+1` and `KDistanceUnit.BASE` (meter) at `-2`. The stored value is the raw
gram-based component value; readings in kg/m² divide by a fixed factor.

## Building an area density

Like density, area density has **no bare token** — every spelling (kg/m², g/mm², …) is a ratio. Build it as an
expression or via the typed `mass / area` operator, and read it back with `into` against such an expression:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.areadensity.*

val q = (25 of kilo.grams) / ((5 of meters) * (1 of meters)) // KAreaDensityUnitInstance, 5 kg/m²
q into (kilo.grams / (meters pow 2))       // 5.0
q into (grams / (milli.meters pow 2))      // 0.005 (= 5 g/mm² ÷ 1000... expressed per mm²)
```

## Computing with the core units (mass, area & density)

| Expression | Result type | Meaning |
|---|---|---|
| `mass / area` | `KAreaDensityUnitInstance` | areal density = m / A |
| `area density * area` | `KMassUnitInstance` | mass = q · A |
| `area * area density` | `KMassUnitInstance` | mass (commutative) |
| `mass / area density` | `KAreaUnitInstance` | area = m / q |
| `density * length` | `KAreaDensityUnitInstance` | plate of given material & thickness |
| `area density / length` | `KDensityUnitInstance` | back to volumetric density |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*
import org.pcsoft.framework.kunit.areadensity.*

// a 3 mm steel plate: density × thickness = surface mass
val density = (2 of kilo.grams) / (1 of liters)      // 2000 kg/m³
val q = density * (3 of meters)                      // KAreaDensityUnitInstance
q into (kilo.grams / (meters pow 2))                 // 6000.0
val back = q / (3 of meters)                         // KDensityUnitInstance, 2000 kg/m³
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.areadensity.*

val area = (5 of meters) * (1 of meters)
val a = (15 of kilo.grams) / area   // 3 kg/m²
val b = (5 of kilo.grams) / area    // 1 kg/m²
(a - b) into (kilo.grams / (meters pow 2)) // 2.0
a > b                                       // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.areadensity.*

((5 of kilo.grams) / ((5 of meters) * (1 of meters))).toString() // "1.0 kg/m²" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `kg/m²` | `kilo.grams / (meters pow 2)` | area density, base unit (kilogram per square metre) — fraction form |
| `kg·m⁻²` | `kilo.grams * (meters pow -2)` | same area density as a product with a negative exponent |
| `g/mm²` | `grams / (milli.meters pow 2)` | gram per square millimetre |
| `25 kg / (5 m · 1 m)` | `(25 of kilo.grams) / ((5 of meters) * (1 of meters))` | build from mass ÷ area |
