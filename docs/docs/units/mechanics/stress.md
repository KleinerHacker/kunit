# Mechanical Stress & Elastic Modulus

Package: `org.pcsoft.framework.kunit.mechanic.pressure`
Base unit: **pascal** (`KPressureUnit.BASE == KPressureUnit.PASCAL`)

Type: **constructed unit**

The mechanical stress `σ = F / A` and the elastic (Young's) modulus `E = σ / ε` have exactly the dimension of
a [pressure](pressure.md): `mass · length⁻¹ · time⁻²`. KUnit therefore does **not** introduce a unit group for them —
both are **readings** of the pressure group, expressed through its prefix aliases. This page documents those readings;
the group itself is described on the [pressure](pressure.md) page.

!!! note "MPa, N/mm² and GPa are prefix aliases"
The statics units are **not** dedicated tokens, because they are reachable exactly:
**MPa = N/mm² = `mega.pascals`** and **GPa = `giga.pascals`**. `(1 of newtons) / ((1 of milli.meters) *
    (1 of milli.meters))` yields the very same value as `1 of mega.pascals`.

## Reading table

| Reading             | Symbol | Kotlin         | 1 unit in Pa |
|---------------------|--------|----------------|-------------:|
| Pascal              | `Pa`   | `pascals`      |          1.0 |
| Kilopascal          | `kPa`  | `kilo.pascals` |          1e3 |
| Megapascal = N/mm²  | `MPa`  | `mega.pascals` |          1e6 |
| Gigapascal (moduli) | `GPa`  | `giga.pascals` |          1e9 |
| Force per area      | `N/m²` | `force / area` |          1.0 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.pressure.*

val fromExpression = (1 of newtons) / ((1 of milli.meters) * (1 of milli.meters))
fromExpression into mega.pascals // 1.0 (N/mm² is the megapascal)
```

## Hooke's law

Together with the [strain](strain.md) group, the pressure group carries both sides of Hooke's law:

| Expression                               | Result type             | Meaning                      |
|------------------------------------------|-------------------------|------------------------------|
| `force / area`                           | `KPressureUnitInstance` | stress `σ = F / A`           |
| `stress / strain`                        | `KPressureUnitInstance` | elastic modulus `E = σ / ε`  |
| `pressure * strain`, `strain * pressure` | `KPressureUnitInstance` | stress `σ = E · ε`           |
| `pressure * area`                        | `KForceUnitInstance`    | the acting force `F = σ · A` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.mechanic.pressure.*
import org.pcsoft.framework.kunit.mechanic.strain.perMille
import org.pcsoft.framework.kunit.mechanic.strain.div
import org.pcsoft.framework.kunit.mechanic.strain.times

val modulus = (210 of mega.pascals) / (1 of perMille) // E = σ / ε
modulus into giga.pascals                              // 210.0 (steel)

val stress = (210 of giga.pascals) * (2 of perMille)   // σ = E · ε
stress into mega.pascals                                // 420.0
```

## Real-world example: tie rod under load

A steel tie rod of 20 mm diameter (A ≈ 314 mm²) carries 60 kN. What is the stress, is it below the 235 MPa yield
strength of S235 steel, and how much does a 3 m rod stretch (E = 210 GPa)?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.pressure.*
import org.pcsoft.framework.kunit.mechanic.strain.ratio
import org.pcsoft.framework.kunit.times

val area = (10 of milli.meters) * (10 of milli.meters) * Math.PI // ≈ 314 mm²
val stress = (60 of kilo.newtons) / area
stress into mega.pascals                     // ≈ 191.0
stress < (235 of mega.pascals)                // true - within the yield strength

val strainRatio = (stress into giga.pascals) / 210.0 // ε = σ / E as a plain ratio
val elongation = (3 of meters) * strainRatio
elongation into milli.meters                          // ≈ 2.73
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.mechanic.pressure.*

val sum = (100 of mega.pascals) + (50 of mega.pascals) // 150 MPa
(1 of giga.pascals) > (999 of mega.pascals)            // true
(1000 of mega.pascals) == (1 of giga.pascals)          // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.mechanic.pressure.*

(210 of mega.pascals).toString()                    // "2.1E8 Pa" (group base unit)
"${(210 of mega.pascals) into mega.pascals} MPa"    // "210.0 MPa"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics  | Kotlin                                            | Meaning                             |
|--------------|---------------------------------------------------|-------------------------------------|
| `MPa`        | `mega.pascals`                                    | stress reading (= N/mm²)            |
| `N/mm²`      | `newtons / (milli.meters pow 2)`                  | same reading as force per area      |
| `GPa`        | `giga.pascals`                                    | elastic-modulus reading             |
| `kg·m⁻¹·s⁻²` | `kilo.grams * (meters pow -1) * (seconds pow -2)` | same quantity in base dimensions    |
| `σ = F / A`  | `force / area`                                    | stress from force and area          |
| `E = σ / ε`  | `stress / strain`                                 | Hooke's law, solved for the modulus |
| `σ = E · ε`  | `pressure * strain`                               | Hooke's law, solved for the stress  |
