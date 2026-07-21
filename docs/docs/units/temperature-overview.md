# Temperature — Overview

Package: `org.pcsoft.framework.kunit.temperature`

Temperature is modelled by **two related groups**, because a temperature reading and a temperature
*change* are physically different kinds of quantity. Getting this distinction right is what makes the
arithmetic correct.

| Concept | Type | Nature | Base unit |
|---|---|---|---|
| [Absolute Temperature](temperature.md) | `KTemperatureUnitInstance` | affine **point** | kelvin (`K`) |
| [Temperature Difference](temperature-difference.md) | `KTemperatureDifferenceUnitInstance` | linear **interval** | kelvin (`ΔK`) |

## Point vs. interval

An **absolute temperature** (`25 °C`, `300 K`) is an affine *point* on the temperature scale: it is
measured from a fixed zero, and the choice of zero (0 K vs. 0 °C) shifts the number. A **temperature
difference** (`20 ΔK`) is a *vector* — the gap between two points. It has no zero-point baggage: a
difference of `20 ΔK` equals a difference of `20 °C`, regardless of where on the scale it sits.

This is exactly the point/vector distinction of an affine space, and it dictates the arithmetic:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

val a = 30 of celsius
val b = 10 of celsius

a - b                                    // KTemperatureDifferenceUnitInstance: 20 ΔK  (an interval)
a + KTemperatureDifference.ofKelvin(5)   // KTemperatureUnitInstance: 308.15 K         (still a point)
// a + b                                 // does NOT compile — adding two points is meaningless
```

| Operation | Result |
|---|---|
| `AbsTemp − AbsTemp` | **Temperature Difference** (kelvin interval) |
| `AbsTemp + Difference` | Absolute Temperature |
| `AbsTemp − Difference` | Absolute Temperature |
| `Difference ± Difference` | Temperature Difference |
| `AbsTemp + AbsTemp` | **compile error** (physically meaningless) |

## Why it matters physically

Whether a formula wants an absolute temperature or a difference depends on **how** the temperature
appears:

* **As a difference / change** → use a **Temperature Difference**. The zero-point cancels, so `°C` and
  `K` are interchangeable here. Classic case — heat energy:

  $$Q = m \cdot c \cdot \Delta T$$

  `30 °C − 10 °C = 20 ΔK`, and `Q = m·c·20 ΔK`. Also thermal expansion (`ΔL = α·L·ΔT`), conduction, and
  specific-heat / thermal-conductivity units (`J/(kg·ΔK)`, `W/(m·ΔK)`).

* **Multiplicatively** (T alone, as a power `T⁴`, or a ratio `T₁/T₂`) → use an **absolute temperature in
  kelvin**, because the absolute zero is part of the physics: ideal-gas law `pV = nRT`, Stefan–Boltzmann
  radiation `P = εσA·T⁴`, Carnot efficiency `η = 1 − T_c/T_h`.

!!! warning "Same dimension, different quantity"
    Both groups have the dimension *kelvin*, so at the pure-unit level `m·K` and `m·ΔK` look alike. They
    are **not** the same unit here: the two use distinct unit groups, so a mixed unit containing an
    absolute kelvin is neither equal to nor addable with one containing a difference kelvin. The distinct
    **`ΔK`** symbol (vs. `K`) is printed precisely to make that visible in `toString` and mixed-unit
    output — keep it in mind when composing your own mixed units.

## Where to go next

* **[Absolute Temperature](temperature.md)** — units (Kelvin, Celsius, Fahrenheit, Rankine), affine
  `of`/`into` construction, and the asymmetric operators.
* **[Temperature Difference](temperature-difference.md)** — the linear group, explicit
  `KTemperatureDifference.ofKelvin(…)` construction, and its linear operators.
