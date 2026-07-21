# Absolute Temperature

> Part of the **Temperature** topic — see the [Overview](temperature-overview.md) and the linear
> counterpart [Temperature Difference](temperature-difference.md).

Package: `org.pcsoft.framework.kunit.temperature`
Base unit: **kelvin** (`KTemperatureUnit.BASE == KTemperatureUnit.KELVIN`)

The temperature group models a thermodynamic temperature. It is the framework's **first (and, by
design, permanent) affine exception**: unlike every other group, converting between temperature units is
not a single multiplicative factor but an **offset-and-scale** (affine) transform — `25 °C` is *not*
`25 × 1 °C`. Values are stored normalized to **absolute kelvin**, so `*`/`/`/`pow` keep running through
the generic engine unchanged.

Two things make this group special:

* **Affine conversions via hooks, not overloads.** The shared engine stays purely multiplicative. The
  affine transform is injected through the two measurable hooks `scaledBy` (construction, behind `of`)
  and `readBaseValue` (reading, behind `into`), so `25 of celsius` and `t into fahrenheit` work through
  the normal verbs — no group-specific `of`/`into` overload (which an explicitly imported generic verb
  would shadow).
* **No prefixes.** The temperature group deliberately offers **no** prefix builders (a `milli.celsius`
  is not modeled). There is no `KTemperatureUnitExtensions.kt`.

## Units

| Unit | Enum value | Symbol | Token | To/from kelvin |
|---|---|---|---:|---|
| Kelvin | `KTemperatureUnit.KELVIN` | `K` | `kelvin` | identity |
| Degree Celsius | `KTemperatureUnit.CELSIUS` | `°C` | `celsius` | `K = °C + 273.15` |
| Degree Fahrenheit | `KTemperatureUnit.FAHRENHEIT` | `°F` | `fahrenheit` | `K = (°F − 32)·5/9 + 273.15` |
| Degree Rankine | `KTemperatureUnit.RANKINE` | `°R` | `rankine` | `K = °R·5/9` |

Each `Token` is a value-1 `KTemperatureUnitInstance` used with `of` (build) and `into` (read).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.temperature.*

val t = 25 of celsius
t.value             // 298.15 (normalized to absolute kelvin)
t into fahrenheit   // 77.0
t into kelvin       // 298.15

(0 of celsius) into kelvin       // 273.15
(100 of celsius) into fahrenheit // 212.0
(32 of fahrenheit) into celsius  // 0.0
(-40 of celsius) into fahrenheit // -40.0 (the C/F crossover)
```

## Operators

An absolute temperature is an affine **point**, not a vector. Its arithmetic is therefore deliberately
asymmetric — the physically correct behaviour (see also [Temperature Difference](temperature-difference.md)):

* `AbsTemp − AbsTemp` → a **`KTemperatureDifferenceUnitInstance`** (the kelvin *interval* between them,
  e.g. `30 °C − 10 °C = 20 ΔK`, **not** `20 °C`).
* `AbsTemp ± difference` → an absolute temperature again.
* `AbsTemp + AbsTemp` → **compile error** (adding two absolute temperatures is physically meaningless).
* `AbsTemp * number` / `AbsTemp / number` → **compile error**: scaling an affine point by a plain number is
  meaningless (its kelvin value carries the −273.15 offset). Scale a
  [temperature difference](temperature-difference.md) instead, which is linear.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

// absolute − absolute = temperature difference (in kelvin)
val d = (30 of celsius) - (10 of celsius)          // KTemperatureDifferenceUnitInstance: 20 ΔK
d.value                                             // 20.0

// absolute ± difference = absolute temperature
val a = (25 of celsius) + KTemperatureDifference.ofKelvin(5)   // KTemperatureUnitInstance: 303.15 K
val b = (25 of celsius) - KTemperatureDifference.ofKelvin(5)   // KTemperatureUnitInstance: 293.15 K

// (30 of celsius) + (10 of celsius)               // does NOT compile

// comparisons (by absolute kelvin)
(0 of celsius) == (273.15 of kelvin)      // true (same absolute temperature)
(100 of celsius) > (100 of fahrenheit)    // true
```

### Comparisons and equality

`==`, `!=`, `<`, `<=`, `>`, `>=` compare the normalized absolute kelvin `value`. `equals` is by absolute
temperature, independent of the construction unit, so `(0 of celsius) == (273.15 of kelvin)`.

## Powers with `pow`

Raise a value to an integer power with the infix `pow` operator. For the temperature group `pow` returns
a generic `KMixedUnitInstance` (temperature has no dimensioned power type), operating linearly on the
absolute kelvin term:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.temperature.*

val squared = (2 of kelvin) pow 2   // KMixedUnitInstance: 4.0 K²
```

## Mixing with other units

Multiplying or dividing a temperature by another group yields a generic `KMixedUnitInstance` (there is no
standardized temperature combination), computed on the absolute kelvin value:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*
import org.pcsoft.framework.kunit.time.seconds

val rate = (2 of kelvin) / (1 of seconds)   // KMixedUnitInstance: 2.0 K·s⁻¹
```

## toString formatting

Only the base-unit `toString()` exists; format a specific unit via `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.temperature.*

(25 of celsius).toString()               // "298.15 K" (base unit representation)
"${(25 of celsius) into fahrenheit} °F"  // "77.0 °F"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed. Temperature is affine, so there is no `·`/exponent product form — only named units and the offset transform.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `K` | `kelvin` | absolute temperature, base unit (kelvin) |
| `°C` | `celsius` | degree Celsius (`K = °C + 273.15`) |
| `°F` | `fahrenheit` | degree Fahrenheit |
| `25 °C` | `25 of celsius` | build an absolute temperature |
