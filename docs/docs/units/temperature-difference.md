# Temperature Difference

Package: `org.pcsoft.framework.kunit.temperature`
Base unit: **kelvin** (`KTemperatureDifferenceUnit.BASE == KTemperatureDifferenceUnit.KELVIN`)

A temperature *difference* is the interval between two temperatures — a **linear** quantity, in contrast
to the affine, absolute [Temperature](temperature.md) group. It carries **no offset** (only the scale of
kelvin), so it behaves like an ordinary unit group and runs through the generic engine unchanged.

Physically this is why subtracting two absolute temperatures yields kelvin rather than a temperature:
`30 °C − 10 °C = 20 ΔK`, not `20 °C`. A difference of `20 ΔK` equals a difference of `20 °C` numerically
anyway (identical step size), so the group deliberately offers **only kelvin** and **no prefixes**.

## Units

| Unit | Enum value | Symbol | To/from kelvin |
|---|---|---|---|
| Kelvin | `KTemperatureDifferenceUnit.KELVIN` | `ΔK` | identity |

!!! note "Symbol `ΔK`, not `K`"
    A temperature difference is printed with the symbol **`ΔK`** (e.g. `"20.0 ΔK"`), deliberately distinct
    from an absolute kelvin (`K`). Both are the same *dimension* (kelvin) but different quantities — an
    affine point vs. a linear interval. In a [mixed unit](../mixed-units.md) `m·K` (absolute) and
    `m·ΔK` (difference) are therefore **not** the same unit and are neither equal nor addable; the distinct
    symbol makes that visible at a glance.

## Construction

A difference is not built with the generic `of` verb (which is reserved for absolute quantities). It is
produced either by **subtracting two absolute temperatures** or **explicitly** via the
`KTemperatureDifference.ofKelvin(…)` factory — making the "this is an interval" intent explicit:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

val d1 = (30 of celsius) - (10 of celsius)   // KTemperatureDifferenceUnitInstance: 20 ΔK
val d2 = KTemperatureDifference.ofKelvin(20) // explicit, equal to d1
d1.value                                      // 20.0 (kelvin)
```

## Operators

`+`/`-`/comparison are the ordinary linear same-type operators (a difference plus a difference is a
difference):

```kotlin
import org.pcsoft.framework.kunit.temperature.*

val sum  = KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10) // 30 ΔK
val diff = KTemperatureDifference.ofKelvin(20) - KTemperatureDifference.ofKelvin(10) // 10 ΔK

KTemperatureDifference.ofKelvin(20) > KTemperatureDifference.ofKelvin(10) // true
```

A difference can be added to / subtracted from an absolute temperature to yield an absolute temperature
again (see [Temperature](temperature.md)):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

(25 of celsius) + KTemperatureDifference.ofKelvin(5) // KTemperatureUnitInstance: 303.15 K
```

## Mixing with other units

Multiplying or dividing a difference by another group yields a generic `KMixedUnitInstance`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.times
import org.pcsoft.framework.kunit.temperature.*

KTemperatureDifference.ofKelvin(2) * (3 of bytes) // KMixedUnitInstance
```

## toString formatting

Only the base-unit `toString()` exists (kelvin):

```kotlin
import org.pcsoft.framework.kunit.temperature.*

KTemperatureDifference.ofKelvin(20).toString() // "20.0 ΔK"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed. A difference carries only the kelvin scale (no offset) and is built explicitly, never with the generic `of`.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `ΔK` | `KTemperatureDifference.ofKelvin(20)` | temperature interval, base unit (kelvin) |
| `30 °C − 10 °C` | `(30 of celsius) - (10 of celsius)` | difference from two absolute temperatures |
| `20 ΔK + 10 ΔK` | `KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10)` | sum of two differences |
