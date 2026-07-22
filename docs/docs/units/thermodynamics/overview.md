# Thermodynamics — Overview

Package: `org.pcsoft.framework.kunit.temperature`

Thermodynamics is the physics of **heat and temperature**. In KUnit the field currently centres on
temperature, which is modelled by **two related native groups** — because a temperature *reading* and a
temperature *change* are physically different kinds of quantity, and keeping them apart is what makes the
arithmetic correct.

## Units in this topic

| Unit | Type | Nature | Base unit | Page |
|---|---|---|---|---|
| Absolute Temperature | native | affine **point** | kelvin (`K`) | [Absolute Temperature](temperature.md) |
| Temperature Difference | native | linear **interval** | kelvin (`ΔK`) | [Temperature Difference](temperature-difference.md) |

A dedicated [Temperature Overview](temperature-overview.md) explains the point-vs-interval distinction in
depth; this page is the entry point for the whole thermodynamics field.

## Point vs. interval — the operator rules

| Operation | Result |
|---|---|
| `AbsTemp − AbsTemp` | **Temperature Difference** |
| `AbsTemp + Difference` | Absolute Temperature |
| `AbsTemp − Difference` | Absolute Temperature |
| `Difference ± Difference` | Temperature Difference |
| `AbsTemp + AbsTemp` | **compile error** (physically meaningless) |

## Worked example — a heating step

Water is heated from **10 °C** to **30 °C**. The *change* is a temperature **difference** (`ΔT`), which is
the quantity that enters heat formulas such as `Q = m · c · ΔT`; the zero-point cancels, so `°C` and `K`
agree on the step size:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

val start = 10 of celsius
val end   = 30 of celsius

val deltaT = end - start                     // KTemperatureDifferenceUnitInstance: 20 ΔK
deltaT.value                                 // 20.0 (kelvin interval)

val back = start + KTemperatureDifference.ofKelvin(20) // KTemperatureUnitInstance: 303.15 K
```

## Printing a value (`toString`)

`toString()` renders a value in its group's **base unit** (kelvin): an absolute temperature prints as
`K`, a difference as the distinct `ΔK` symbol:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

(25 of celsius).toString()                       // "298.15 K" (absolute, base unit)
KTemperatureDifference.ofKelvin(20).toString()   // "20.0 ΔK" (interval)
```

## Notation

The table shows the temperature relations mathematically versus in Kotlin with KUnit. `Δ` marks an
interval quantity, deliberately distinct from an absolute point.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `ΔT = T₂ − T₁` | `(30 of celsius) - (10 of celsius)` | difference from two absolute temperatures |
| `T + ΔT` | `(10 of celsius) + KTemperatureDifference.ofKelvin(20)` | absolute temperature shifted by an interval |
| `ΔK` | `KTemperatureDifference.ofKelvin(20)` | an explicit temperature interval |
| `20 ΔK + 10 ΔK` | `KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10)` | sum of two intervals |

## Where to go next

* [Temperature Overview](temperature-overview.md) — the full point-vs-interval discussion and why it
  matters physically (heat energy, radiation, the ideal-gas law).
* [Absolute Temperature](temperature.md) — Kelvin, Celsius, Fahrenheit, Rankine and the affine operators.
* [Temperature Difference](temperature-difference.md) — the linear kelvin interval group.
