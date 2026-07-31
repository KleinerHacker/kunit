# Charge Density

Package: `org.pcsoft.framework.kunit.electric.chargedensity`
Base unit: **coulomb per cubic meter** (`KChargeDensityUnit.BASE == KChargeDensityUnit.COULOMB_PER_CUBIC_METER`)

Type: **constructed unit**

(Volume) charge density is a **constructed** unit: the composition `current¹ · time¹ · length⁻³`
(`A·s·m⁻³` = `C/m³`). `KChargeDensityUnitInstance` wraps a `KMixedUnitInstance` of three terms —
`KElectricCurrentUnit.BASE` (ampere) at `+1`, `KTimeUnit.BASE` (second) at `+1` and `KDistanceUnit.BASE`
(meter) at `-3`. Since all components are stored in their group base units, the stored value is directly the reading in
C/m³.

## Building a charge density

Charge density has **no bare token and no prefix builders** — every spelling (C/m³, mC/cm³, …) is a ratio. Build it as
an expression or via the typed `charge / volume` operator, and read it back with `into` against such an expression.
Prefixes come from the component tokens (`milli.coulombs`, `centi.meters`):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

val rho = (6 of coulombs) / (2 of liters)  // KChargeDensityUnitInstance, 3 C/L = 3000 C/m³
rho into (coulombs / (meters pow 3))       // 3000.0
rho into (coulombs / (centi.meters pow 3)) // 0.003 (= 3 mC/cm³)
rho into (milli.coulombs / (meters pow 3)) // 3000000.0
```

## Multiple decompositions

Charge density can be reached through **equivalent decompositions**, all producing the same value-equal charge density:

| Expression             | Result type                  | Meaning                               |
|------------------------|------------------------------|---------------------------------------|
| `charge / volume`      | `KChargeDensityUnitInstance` | definition `ρ = Q / V`                |
| `current·time/length³` | via `.toChargeDensity()`     | native canonical `A·s·m⁻³` expression |

The typed operator form returns a charge density directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toChargeDensity()` (which recognises only the canonical normal form and
throws `IllegalStateException` otherwise). Both routes are value-equal.

The inverse operators tie charge, volume and charge density together:

| Expression               | Result type           | Meaning                   |
|--------------------------|-----------------------|---------------------------|
| `chargeDensity * volume` | `KChargeUnitInstance` | `Q = ρ · V`               |
| `volume * chargeDensity` | `KChargeUnitInstance` | `Q = V · ρ` (commutative) |
| `charge / chargeDensity` | `KVolumeUnitInstance` | `V = Q / ρ`               |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

// Real-world example - space charge in an electrolyte: 12 mC of net charge dissolved in 4 litres of
// electrolyte gives a charge density of 3 C/m³.
val rho = (0.012 of coulombs) / (4 of liters)   // KChargeDensityUnitInstance, 3 C/m³

// The same charge density as the native A·s·m⁻³ expression:
val raw = (0.012 of coulombs).toUnit() / (0.004 of (meters pow 3))
raw.toChargeDensity() == rho                    // true

// Back to the charge contained in 4 litres, and to the volume holding 12 mC:
val q = rho * (4 of liters)                     // KChargeUnitInstance
q into coulombs                                 // 0.012
val v = (0.012 of coulombs) / rho               // KVolumeUnitInstance
v into liters                                   // 4.0
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

val a = (3 of coulombs) / (1 of liters)     // 3000 C/m³
val b = (1 of coulombs) / (1 of liters)     // 1000 C/m³
(a + b) into (coulombs / (meters pow 3))    // 4000.0
(a - b) into (coulombs / (meters pow 3))    // 2000.0
a > b                                       // true
a * b                                       // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.electric.chargedensity.*

((1 of coulombs) / (1 of liters)).toString() // "1000.0 C/m³" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics   | Kotlin                                   | Meaning                                                             |
|---------------|------------------------------------------|---------------------------------------------------------------------|
| `C/m³`        | `coulombs / (meters pow 3)`              | charge density, base unit (coulomb per cubic metre) — fraction form |
| `C·m⁻³`       | `coulombs * (meters pow -3)`             | same charge density as a product with a negative exponent           |
| `A·s/m³`      | `amperes * seconds / (meters pow 3)`     | native canonical form (current·time / length³)                      |
| `mC/cm³`      | `milli.coulombs / (centi.meters pow 3)`  | millicoulomb per cubic centimetre                                   |
| `12 mC / 4 L` | `(12 of milli.coulombs) / (4 of liters)` | build from charge ÷ volume                                          |
