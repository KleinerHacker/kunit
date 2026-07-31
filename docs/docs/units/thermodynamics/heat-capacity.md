# Heat Capacity

Package: `org.pcsoft.framework.kunit.thermo.heatcapacity`
Base unit: **joule per kelvin** (`KHeatCapacityUnit.BASE == KHeatCapacityUnit.JOULE_PER_KELVIN`)

Type: **constructed unit**

Heat capacity is the energy an object absorbs per unit of temperature rise: `energy / temperature`
(`J/K`). `KHeatCapacityUnitInstance` wraps a `KMixedUnitInstance` of exactly four terms in the canonical normal form
`mass¹ · distance² · time⁻² · temperature⁻¹` (`kg·m²·s⁻²·K⁻¹`), always normalized to J/K.

!!! note "Temperature *difference*, never absolute temperature"
The temperature dimension is the **difference** group (`KTemperatureDifferenceUnit`, symbol `ΔK`), never the affine
absolute `KTemperatureUnit`. A heat capacity relates energy to a temperature *interval*; an offset-carrying absolute
scale (°C, °F) would be physically wrong in a quotient.

The same dimension `J/K` also describes **entropy** — see [entropy](entropy.md) for why that quantity shares this type
rather than getting one of its own. Per unit of mass it becomes
[specific heat capacity](specific-heat-capacity.md), per mole [molar heat capacity](molar-heat-capacity.md).

## Named units

| Unit                      | Symbol   |               Token | 1 unit in J/K |
|---------------------------|----------|--------------------:|--------------:|
| Joule per kelvin          | `J/K`    |   `joulesPerKelvin` |           1.0 |
| Calorie per kelvin        | `cal/K`  | `caloriesPerKelvin` |         4.184 |
| Btu per degree Fahrenheit | `Btu/°F` | `btusPerFahrenheit` |   ≈ 1899.1005 |

All accept the full SI prefix range (`kilo.joulesPerKelvin`, `kilo.caloriesPerKelvin`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

val c = 4184 of joulesPerKelvin
c into kilo.joulesPerKelvin  // 4.184
c into caloriesPerKelvin     // 1000.0
```

## Real-world example: heating a kettle of water

One liter of water (4184 J/K) is heated from 20 °C to 100 °C. How much energy does that take?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.*

val kettle = 4184 of joulesPerKelvin          // 1 liter of water
val rise = (100 of celsius) - (20 of celsius) // KTemperatureDifferenceUnitInstance, 80 K

val energy = kettle * rise                    // KEnergyUnitInstance
energy into joules                            // 334_720.0 J
energy into kilo.joules                       // 334.72 kJ

// ... and the other way round: how far does 100 kJ get us?
val reachable = (100 of kilo.joules) / kettle // KTemperatureDifferenceUnitInstance
reachable into KTemperatureDifference.ofKelvin(1) // ≈ 23.9 K
```

## Computing with the core units (energy & temperature difference)

| Expression                             | Result type                          | Meaning                     |
|----------------------------------------|--------------------------------------|-----------------------------|
| `energy / temperatureDifference`       | `KHeatCapacityUnitInstance`          | heat capacity               |
| `heatCapacity * temperatureDifference` | `KEnergyUnitInstance`                | energy required             |
| `temperatureDifference * heatCapacity` | `KEnergyUnitInstance`                | energy (commutative)        |
| `energy / heatCapacity`                | `KTemperatureDifferenceUnitInstance` | achievable temperature rise |

## Decompositions

Both decompositions produce the same typed, value-equal instance.

| Decomposition                               | Form                                   | Result                               |
|---------------------------------------------|----------------------------------------|--------------------------------------|
| `energy / temperatureDifference`            | typed operator                         | `KHeatCapacityUnitInstance` directly |
| `mass · distance² · time⁻² · temperature⁻¹` | native expression + `toHeatCapacity()` | `KHeatCapacityUnitInstance`          |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

// typed operator form
val typed = (1 of joules) / KTemperatureDifference.ofKelvin(1)

// native base-dimension form (kg·m²·s⁻²·K⁻¹), recognised by toHeatCapacity()
val native = (
    (1000 of grams).toUnit() *
        ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toHeatCapacity()

typed == native // true - both are 1.0 J/K
```

`toHeatCapacity()` recognises **only** the canonical normal form; any equivalent expression reduces onto it
automatically, and a wrong shape throws `IllegalStateException`.

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

// + / - : same group, automatic conversion between units and prefixes
val total = (1 of kilo.joulesPerKelvin) + (500 of joulesPerKelvin)  // 1500 J/K
val rest  = (1 of kilo.joulesPerKelvin) - (250 of joulesPerKelvin)  // 750 J/K

// comparisons (by normalized J/K value)
(1 of kilo.joulesPerKelvin) > (500 of joulesPerKelvin)   // true
(1 of kilo.joulesPerKelvin) == (1000 of joulesPerKelvin) // true

// * / / between two heat capacities escape to a KMixedUnitInstance
val squared = (2 of joulesPerKelvin) * (2 of joulesPerKelvin)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

(4184 of joulesPerKelvin).toString()                          // "4184.0 J/K"
"${(4184 of joulesPerKelvin) into caloriesPerKelvin} cal/K"   // "1000.0 cal/K"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics     | Kotlin                                          | Meaning                                      |
|-----------------|-------------------------------------------------|----------------------------------------------|
| `J/K`           | `joulesPerKelvin`                               | heat capacity, base unit — named token       |
| `kg·m²·s⁻²·K⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / ΔK` | same quantity in base dimensions             |
| `kJ/K`          | `kilo.joulesPerKelvin`                          | kilojoule per kelvin                         |
| `cal/K`         | `caloriesPerKelvin`                             | calorie per kelvin                           |
| `C = Q / ΔT`    | `(4184 of joules) / rise`                       | heat capacity from energy ÷ temperature rise |
| `Q = C · ΔT`    | `kettle * rise`                                 | energy from heat capacity × temperature rise |
| `ΔT = Q / C`    | `(100 of kilo.joules) / kettle`                 | temperature rise from energy ÷ heat capacity |
