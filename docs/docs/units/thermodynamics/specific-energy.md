# Specific Energy

Package: `org.pcsoft.framework.kunit.thermo.specificenergy`
Base unit: **joule per kilogram** (`KSpecificEnergyUnit.BASE == KSpecificEnergyUnit.JOULE_PER_KILOGRAM`)

Type: **constructed unit**

Specific energy is energy per unit of mass: `energy / mass` (`J/kg`). The same quantity is called *specific enthalpy*,
*specific latent heat* or *calorific value* depending on context — they all share this unit group.

`KSpecificEnergyUnitInstance` wraps a `KMixedUnitInstance` of exactly two terms in the canonical normal form
`distance² · time⁻²` (`m²·s⁻²`), always normalized to J/kg.

!!! note "The mass dimension cancels out"
`J/kg = kg·m²·s⁻²/kg = m²·s⁻²`. The canonical normal form therefore carries **no** mass term at all. Only the operators
against a `KMassUnitInstance` bridge the mass group's gram base to this group's per-kilogram definition.

Per unit of temperature this becomes [specific heat capacity](specific-heat-capacity.md); per mole instead of per
kilogram it becomes [molar energy](molar-energy.md).

## Named units

| Unit                   | Symbol   |                  Token | 1 unit in J/kg |
|------------------------|----------|-----------------------:|---------------:|
| Joule per kilogram     | `J/kg`   |    `joulesPerKilogram` |            1.0 |
| Calorie per gram       | `cal/g`  |      `caloriesPerGram` |         4184.0 |
| Watt-hour per kilogram | `Wh/kg`  | `wattHoursPerKilogram` |         3600.0 |
| Btu per pound          | `Btu/lb` |         `btusPerPound` |         2326.0 |

All accept the full SI prefix range (`kilo.joulesPerKilogram`, `mega.joulesPerKilogram`,
`kilo.wattHoursPerKilogram`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val h = 334 of kilo.joulesPerKilogram
h into joulesPerKilogram      // 334_000.0
h into caloriesPerGram        // ≈ 79.83
h into wattHoursPerKilogram   // ≈ 92.78
```

## Real-world example: melting ice

The latent heat of fusion of water is 334 kJ/kg. How much energy does melting a 2.5 kg block of ice take?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.*
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val latentHeat = 334 of kilo.joulesPerKilogram
val block = 2.5 of kilo.grams

val energy = latentHeat * block     // KEnergyUnitInstance
energy into kilo.joules             // 835.0 kJ
energy into joules                  // 835_000.0 J

// Inverse: how much ice can 1 MJ melt?
val melted = (1000 of kilo.joules) / latentHeat  // KMassUnitInstance
melted into kilo.grams              // ≈ 2.994 kg
```

## Computing with the core units (energy & mass)

| Expression                | Result type                   | Meaning                    |
|---------------------------|-------------------------------|----------------------------|
| `energy / mass`           | `KSpecificEnergyUnitInstance` | specific energy            |
| `specificEnergy * mass`   | `KEnergyUnitInstance`         | total energy               |
| `mass * specificEnergy`   | `KEnergyUnitInstance`         | total energy (commutative) |
| `energy / specificEnergy` | `KMassUnitInstance`           | mass involved              |

## Decompositions

Both decompositions produce the same typed, value-equal instance.

| Decomposition        | Form                                     | Result                                 |
|----------------------|------------------------------------------|----------------------------------------|
| `energy / mass`      | typed operator                           | `KSpecificEnergyUnitInstance` directly |
| `distance² · time⁻²` | native expression + `toSpecificEnergy()` | `KSpecificEnergyUnitInstance`          |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificenergy.*

// typed operator form
val typed = (1 of joules) / (1 of kilo.grams)

// native base-dimension form (m²·s⁻²), recognised by toSpecificEnergy()
val native = (((1 of meters).toUnit() pow 2) / ((1 of seconds).toUnit() pow 2)).toSpecificEnergy()

typed == native // true - both are 1.0 J/kg
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val total = (1 of kilo.joulesPerKilogram) + (500 of joulesPerKilogram)  // 1500 J/kg
val rest  = (1 of kilo.joulesPerKilogram) - (250 of joulesPerKilogram)  // 750 J/kg

(1 of kilo.joulesPerKilogram) > (500 of joulesPerKilogram)   // true
(1 of kilo.joulesPerKilogram) == (1000 of joulesPerKilogram) // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificenergy.*

(334 of kilo.joulesPerKilogram).toString()                        // "334000.0 J/kg"
"${(334 of kilo.joulesPerKilogram) into caloriesPerGram} cal/g"   // "79.83..."
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin                                     | Meaning                                         |
|-------------|--------------------------------------------|-------------------------------------------------|
| `J/kg`      | `joulesPerKilogram`                        | specific energy, base unit — named token        |
| `m²·s⁻²`    | `(meters pow 2) / (seconds pow 2)`         | same quantity in base dimensions                |
| `kJ/kg`     | `kilo.joulesPerKilogram`                   | kilojoule per kilogram                          |
| `Wh/kg`     | `wattHoursPerKilogram`                     | watt-hour per kilogram (battery energy density) |
| `q = Q / m` | `(334 of kilo.joules) / (1 of kilo.grams)` | specific energy from energy ÷ mass              |
| `Q = q · m` | `latentHeat * block`                       | energy from specific energy × mass              |
| `m = Q / q` | `(1000 of kilo.joules) / latentHeat`       | mass from energy ÷ specific energy              |
