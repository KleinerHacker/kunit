# Specific Volume

Package: `org.pcsoft.framework.kunit.mechanic.specificvolume`
Base unit: **cubic meter per kilogram**
(`KSpecificVolumeUnit.BASE == KSpecificVolumeUnit.CUBIC_METERS_PER_KILOGRAM`)

Type: **constructed unit**

The specific volume `v` is the volume occupied per unit of mass — the **reciprocal of the
[density](density.md)**. It is a **constructed** unit — the composition `length³ · mass⁻¹` (`m³/kg`).

`KSpecificVolumeUnitInstance` wraps a `KMixedUnitInstance` of exactly two terms in the canonical normal form:
`KDistanceUnit.BASE` (meter) at `+3` and `KMassUnit.BASE` (gram) at `-1`. Since the mass component of this library is
normalized to grams, the stored value is the raw gram-based component value and readings in m³/kg are bridged by a fixed
factor.

## Named units

| Unit                      | Symbol    |                     Token | 1 unit in m³/kg |
|---------------------------|-----------|--------------------------:|----------------:|
| Cubic meter per kilogram  | `m^3/kg`  |  `cubicMetersPerKilogram` |             1.0 |
| Liter per kilogram        | `l/kg`    |       `litersPerKilogram` |            1e-3 |
| Cubic centimeter per gram | `cm^3/g`  | `cubicCentimetersPerGram` |            1e-3 |
| Cubic foot per pound      | `ft^3/lb` |       `cubicFeetPerPound` |     ≈ 0.0624280 |

All units accept the full SI prefix range (`milli.cubicMetersPerKilogram`).

## Computing with the core units

| Expression                                       | Result type                   | Meaning     |
|--------------------------------------------------|-------------------------------|-------------|
| `volume / mass`                                  | `KSpecificVolumeUnitInstance` | `v = V / m` |
| `specificvolume * mass`, `mass * specificvolume` | `KVolumeUnitInstance`         | `V = v · m` |
| `volume / specificvolume`                        | `KMassUnitInstance`           | `m = V / v` |
| `1 / density`                                    | `KSpecificVolumeUnitInstance` | `v = 1 / ρ` |
| `1 / specificvolume`                             | `KDensityUnitInstance`        | `ρ = 1 / v` |

The reciprocal operators are typed: `1 / density` keeps a real unit type instead of degrading to a generic mixed unit.
The native form converts with `toSpecificVolume()`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mechanic.specificvolume.*

val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

val viaQuotient = (2 of liters) / (1 of kilo.grams)
val viaReciprocal = 1 / water

viaQuotient into litersPerKilogram   // 2.0
viaReciprocal into litersPerKilogram // 1.0
(1 / viaReciprocal).value == water.value // true - exact round-trip
```

## Real-world example: steam table lookup

Saturated steam at 1 bar has a specific volume of about 1.694 m³/kg. Which volume does 2 kg of that steam occupy, and
what is its density?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.specificvolume.*
import org.pcsoft.framework.kunit.pow

val v = 1.694 of cubicMetersPerKilogram
val volume = v * (2 of kilo.grams)   // KVolumeUnitInstance
volume into liters                   // 3388.0

val rho = 1 / v                      // KDensityUnitInstance
rho into (kilo.grams / (meters pow 3)) // ≈ 0.5903
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.specificvolume.*

val sum = (10 of litersPerKilogram) + (4 of litersPerKilogram) // 14 l/kg
(1 of cubicMetersPerKilogram) > (1 of litersPerKilogram)       // true
(1 of litersPerKilogram) == (1 of cubicCentimetersPerGram)     // true (same value)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.specificvolume.*

(2 of cubicMetersPerKilogram).toString()                      // "2.0 m^3/kg" (base unit)
"${(2 of cubicMetersPerKilogram) into litersPerKilogram} l/kg" // "2000.0 l/kg"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin                                 | Meaning                                  |
|-------------|----------------------------------------|------------------------------------------|
| `m³/kg`     | `cubicMetersPerKilogram`               | specific volume, base unit (named token) |
| `m³·kg⁻¹`   | `(meters pow 3) * (kilo.grams pow -1)` | same quantity as a pure product          |
| `l/kg`      | `litersPerKilogram`                    | liter-per-kilogram reading               |
| `v = V / m` | `volume / mass`                        | typed decomposition                      |
| `v = 1 / ρ` | `1 / density`                          | reciprocal of the density                |
| `ρ = 1 / v` | `1 / specificvolume`                   | back to the density                      |
