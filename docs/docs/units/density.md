# Density

Package: `org.pcsoft.framework.kunit.density`
Base unit: **kilogram per cubic meter** (`KDensityUnit.BASE == KDensityUnit.KILOGRAM_PER_CUBIC_METER`)

Density (mass density) is a **constructed** unit: the composition `mass · length⁻³` (`kg/m³`).
`KDensityUnitInstance` wraps a `KMixedUnitInstance` of two terms — `KMassUnit.BASE` (gram) at `+1` and
`KDistanceUnit.BASE` (meter) at `-3`. The stored value is the raw gram-based component value; readings in
kg/m³ divide by a fixed factor.

## Building a density

Density has **no bare token** — every spelling (kg/m³, g/cm³, …) is a ratio. Build it as an expression or via
the typed `mass / volume` operator, and read it back with `into` against such an expression:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*

val steel = (7850 of kilo.grams) / (1 of (meters pow 3)) // KDensityUnitInstance, 7850 kg/m³
steel into (kilo.grams / (meters pow 3))   // 7850.0
steel into (kilo.grams / (centi.meters pow 3)) // 0.00785 (= 7.85 g/cm³)

val d = (6 of kilo.grams) / (2 of liters)  // 3 kg/L = 3000 kg/m³
```

## Computing with the core units (mass & volume)

| Expression | Result type | Meaning |
|---|---|---|
| `mass / volume` | `KDensityUnitInstance` | density = m / V |
| `density * volume` | `KMassUnitInstance` | mass = ρ · V |
| `volume * density` | `KMassUnitInstance` | mass (commutative) |
| `mass / density` | `KVolumeUnitInstance` | volume = m / ρ |
| `density * length` | `KAreaDensityUnitInstance` | areal density (see Area Density) |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.density.*

val d = (2 of kilo.grams) / (1 of liters)  // 2 kg/L
val m = d * (3 of liters)                  // KMassUnitInstance
m into kilo.grams                          // 6.0
val v = (6 of kilo.grams) / d              // KVolumeUnitInstance
v into liters                              // 3.0
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*

val a = (3 of kilo.grams) / (1 of liters)
val b = (1 of kilo.grams) / (1 of liters)
(a - b) into (kilo.grams / (meters pow 3)) // 2000.0
a > b                                       // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.density.*

((1 of kilo.grams) / (1 of liters)).toString() // "1000.0 kg/m³" (base unit)
```
