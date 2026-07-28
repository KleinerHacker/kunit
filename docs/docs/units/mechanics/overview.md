# Mechanics — Overview

Packages: `org.pcsoft.framework.kunit.mechanic.mass`, `…force`, `…pressure`, `…density`, `…areadensity`, `…power`,
`…energy`

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
| Power | constructed | watt (`W`) | [Power (Mechanics)](power.md) |
| Energy | constructed | joule (`J`) | [Energy (Mechanics)](energy.md) |

Power and energy are technically **one** quantity each, shared with other subject areas; they are documented
per field and cross-reference each other ([Power (Electrical)](../electrical/power.md),
[Power (Thermodynamics)](../thermodynamics/power.md), [Energy (Electrical)](../electrical/energy.md),
[Energy (Thermodynamics)](../thermodynamics/energy.md)).

## How the quantities relate

| Expression | Result | Formula |
|---|---|---|
| `mass * acceleration` | Force | `F = m · a` |
| `force / area` | Pressure | `p = F / A` |
| `pressure * area` | Force | `F = p · A` |
| `mass / volume` | Density | `ρ = m / V` |
| `density * length` | Area Density | `ρ_A = ρ · d` |
| `force * speed` | Power | `P = F · v` |
| `power / speed` | Force | `F = P / v` |
| `power / force` | Speed | `v = P / F` |
| `force * length` | Energy (work) | `W = F · s` |
| `power * time` | Energy | `W = P · t` |
| `energy / time` | Power | `P = W / t` |
| `energy / power` | Time | `t = W / P` |

## Worked example — Newton's second law and ground pressure

A **2 kg** block is accelerated at standard gravity, and the resulting weight force is spread over a
**0.5 m²** footprint. The force is `F = m · a`, the pressure `p = F / A`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.acceleration.*
import org.pcsoft.framework.kunit.mechanic.force.*
import org.pcsoft.framework.kunit.mechanic.pressure.*

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
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mechanic.density.*

val steel = (7850 of kilo.grams) / (1 of (meters pow 3)) // KDensityUnitInstance, 7850 kg/m³
val mass = steel * (2 of liters)                          // KMassUnitInstance
mass into kilo.grams                                      // 15.7 (kg per 2 L)
```

## Worked example — work and power of a winch

A winch pulls with **100 N** over **5 m** within **5 s**. The work is `W = F · s`, the power `P = W / t` —
which equals the direct mechanical form `P = F · v`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.common.power.*
import org.pcsoft.framework.kunit.common.energy.*

val w = (100 of newtons) * (5 of meters)                    // KEnergyUnitInstance
w into joules                                                // 500.0

val p = w / (5 of seconds)                                   // KPowerUnitInstance
p into watts                                                 // 100.0

val direct = (100 of newtons) * ((1 of meters) / (1 of seconds)) // P = F · v, 100 W
p == direct                                                  // true
```

## Printing a value (`toString`)

`toString()` renders a value in its group's **base unit** (value + symbol); for any other unit, read it
with `into` inside a string template and append the symbol yourself:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.force.*

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
| `W = F · s` | `(100 of newtons) * (5 of meters)` | work from force × length |
| `P = F · v` | `(100 of newtons) * ((1 of meters) / (1 of seconds))` | power from force × speed |
| `P = W / t` | `w / (5 of seconds)` | power from work ÷ time |

## Where to go next

* [Mass](mass.md) — the native base quantity (gram-normalised).
* [Force](force.md) and [Pressure](pressure.md) — Newton's law and force over area.
* [Density](density.md) and [Area Density](areadensity.md) — mass per volume and per surface.
* [Power (Mechanics)](power.md) — the watt, `F · v`, and the horsepower units.
* [Energy (Mechanics)](energy.md) — the joule as mechanical work `F · s`.
