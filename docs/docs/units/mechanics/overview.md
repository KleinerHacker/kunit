# Mechanics — Overview

Packages: `org.pcsoft.framework.kunit.mass`, `…force`, `…pressure`, `…density`, `…areadensity`

Mechanics (dynamics) asks **why** bodies move and how matter is distributed: the interplay of mass, the
forces acting on it, the pressure a force exerts over an area, and how much mass is packed into a volume
or a surface. Building on the [Kinematics](../kinematics/overview.md) rates, this topic adds one **native**
base quantity (mass) and four quantities **constructed** from mass, length and time.

## Units in this topic

| Unit | Type | Base unit | Page |
|---|---|---|---|
| Mass | native | gram (`g`) | [Mass](mass.md) |
| Force | constructed | newton (`N`) | [Force](force.md) |
| Pressure | constructed | pascal (`Pa`) | [Pressure](pressure.md) |
| Density | constructed | kilogram per cubic metre (`kg/m³`) | [Density](density.md) |
| Area Density | constructed | kilogram per square metre (`kg/m²`) | [Area Density](areadensity.md) |

## How the quantities relate

| Expression | Result | Formula |
|---|---|---|
| `mass * acceleration` | Force | `F = m · a` |
| `force / area` | Pressure | `p = F / A` |
| `pressure * area` | Force | `F = p · A` |
| `mass / volume` | Density | `ρ = m / V` |
| `density * length` | Area Density | `ρ_A = ρ · d` |

## Worked example — Newton's second law and ground pressure

A **2 kg** block is accelerated at standard gravity, and the resulting weight force is spread over a
**0.5 m²** footprint. The force is `F = m · a`, the pressure `p = F / A`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.acceleration.*
import org.pcsoft.framework.kunit.force.*
import org.pcsoft.framework.kunit.pressure.*

val f = (2 of kilo.grams) * (1 of standardGravities)  // KForceUnitInstance
f into newtons                                         // ≈ 19.61 (N)

val area = (1 of meters) * (0.5 of meters)             // KAreaUnitInstance, 0.5 m²
val p = f / area                                       // KPressureUnitInstance
p into pascals                                         // ≈ 39.23 (Pa)
```

## Worked example — mass of a steel part from its density

Steel has a density of **7850 kg/m³**. The mass of a **2 L** part is `m = ρ · V`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.liters
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.density.*

val steel = (7850 of kilo.grams) / (1 of (meters pow 3)) // KDensityUnitInstance, 7850 kg/m³
val mass = steel * (2 of liters)                          // KMassUnitInstance
mass into kilo.grams                                      // 15.7 (kg per 2 L)
```

## Printing a value (`toString`)

`toString()` renders a value in its group's **base unit** (value + symbol); for any other unit, read it
with `into` inside a string template and append the symbol yourself:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.force.*

val f = 10 of newtons
f.toString()                 // "10.0 N" (base unit)
"${f into kilo.newtons} kN"  // "0.01 kN"
```

## Notation

The table shows the field's core relations mathematically versus in Kotlin with KUnit. Exponents use
Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `F = m · a` | `(2 of kilo.grams) * (1 of standardGravities)` | force from mass × acceleration |
| `p = F / A` | `f / area` | pressure from force ÷ area |
| `F = p · A` | `p * area` | force from pressure × area |
| `ρ = m / V` | `(6 of kilo.grams) / (2 of liters)` | density from mass ÷ volume |
| `m = ρ · V` | `steel * (2 of liters)` | mass from density × volume |

## Where to go next

* [Mass](mass.md) — the native base quantity (gram-normalised).
* [Force](force.md) and [Pressure](pressure.md) — Newton's law and force over area.
* [Density](density.md) and [Area Density](areadensity.md) — mass per volume and per surface.
