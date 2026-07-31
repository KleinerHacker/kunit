# Specific Heat Capacity

Package: `org.pcsoft.framework.kunit.thermo.specificheatcapacity`
Base unit: **joule per kilogram-kelvin**
(`KSpecificHeatCapacityUnit.BASE == KSpecificHeatCapacityUnit.JOULE_PER_KILOGRAM_KELVIN`)

Type: **constructed unit**

Specific heat capacity is the [heat capacity](heat-capacity.md) of a material *per unit of mass*:
`J/(kg·K)`. It is the material property behind every "how much energy to heat this up" calculation.

`KSpecificHeatCapacityUnitInstance` wraps a `KMixedUnitInstance` of exactly three terms in the canonical normal form
`distance² · time⁻² · temperature⁻¹` (`m²·s⁻²·K⁻¹`) — the mass dimension cancels out, exactly as
for [specific energy](specific-energy.md). The temperature dimension is the **difference** group
(`KTemperatureDifferenceUnit`), never the affine absolute temperature.

## Named units

| Unit                      | Symbol        |                     Token | 1 unit in J/(kg·K) |
|---------------------------|---------------|--------------------------:|-------------------:|
| Joule per kilogram-kelvin | `J/(kg·K)`    | `joulesPerKilogramKelvin` |                1.0 |
| Calorie per gram-kelvin   | `cal/(g·K)`   |   `caloriesPerGramKelvin` |             4184.0 |
| Btu per pound-°F          | `Btu/(lb·°F)` |  `btusPerPoundFahrenheit` |             4186.8 |

All accept the full SI prefix range (`kilo.joulesPerKilogramKelvin`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*

val water = 4184 of joulesPerKilogramKelvin
water into caloriesPerGramKelvin   // 1.0 (water is 1 cal/(g·K) by definition of the calorie)
```

## Real-world example: heating a bathtub

150 liters of water (150 kg) are heated from 12 °C to 40 °C. Water has a specific heat capacity of 4184 J/ (kg·K).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.*

val water = 4184 of joulesPerKilogramKelvin
val bath = 150 of kilo.grams
val rise = (40 of celsius) - (12 of celsius)  // 28 K

// route 1: build the tub's heat capacity first
val tubCapacity = water * bath                // KHeatCapacityUnitInstance
tubCapacity into joulesPerKelvin              // 627_600.0 J/K
val energy = tubCapacity * rise               // KEnergyUnitInstance
energy into mega.joules                       // ≈ 17.57 MJ

// route 2: go via specific energy (energy per kilogram) instead
val perKilogram = water * rise                // KSpecificEnergyUnitInstance, 117_152 J/kg
val sameEnergy = perKilogram * bath           // KEnergyUnitInstance
sameEnergy into mega.joules                   // ≈ 17.57 MJ - identical
```

## Computing with the neighbouring units

| Expression                                     | Result type                          | Meaning                          |
|------------------------------------------------|--------------------------------------|----------------------------------|
| `heatCapacity / mass`                          | `KSpecificHeatCapacityUnitInstance`  | material property from an object |
| `specificEnergy / temperatureDifference`       | `KSpecificHeatCapacityUnitInstance`  | same, via specific energy        |
| `specificHeatCapacity * mass`                  | `KHeatCapacityUnitInstance`          | the object's heat capacity       |
| `mass * specificHeatCapacity`                  | `KHeatCapacityUnitInstance`          | same (commutative)               |
| `heatCapacity / specificHeatCapacity`          | `KMassUnitInstance`                  | mass of the object               |
| `specificHeatCapacity * temperatureDifference` | `KSpecificEnergyUnitInstance`        | energy per kilogram              |
| `temperatureDifference * specificHeatCapacity` | `KSpecificEnergyUnitInstance`        | same (commutative)               |
| `specificEnergy / specificHeatCapacity`        | `KTemperatureDifferenceUnitInstance` | achievable rise                  |

## Decompositions

All three decompositions produce the same typed, value-equal instance.

| Decomposition                            | Form                                           | Result                              |
|------------------------------------------|------------------------------------------------|-------------------------------------|
| `heatCapacity / mass`                    | typed operator                                 | `KSpecificHeatCapacityUnitInstance` |
| `specificEnergy / temperatureDifference` | typed operator                                 | `KSpecificHeatCapacityUnitInstance` |
| `distance² · time⁻² · temperature⁻¹`     | native expression + `toSpecificHeatCapacity()` | `KSpecificHeatCapacityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaHeatCapacity   = (1 of joulesPerKelvin) / (1 of kilo.grams)
val viaSpecificEnergy = (1 of joulesPerKilogram) / KTemperatureDifference.ofKelvin(1)
val native = (
    ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toSpecificHeatCapacity()

viaHeatCapacity == viaSpecificEnergy // true
viaHeatCapacity == native            // true - all are 1.0 J/(kg·K)
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*

val total = (1 of kilo.joulesPerKilogramKelvin) + (500 of joulesPerKilogramKelvin)  // 1500
(1 of kilo.joulesPerKilogramKelvin) > (500 of joulesPerKilogramKelvin)              // true
(1 of kilo.joulesPerKilogramKelvin) == (1000 of joulesPerKilogramKelvin)            // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.*

(4184 of joulesPerKilogramKelvin).toString()                                // "4184.0 J/(kg·K)"
"${(4184 of joulesPerKilogramKelvin) into caloriesPerGramKelvin} cal/(g·K)" // "1.0 cal/(g·K)"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics      | Kotlin                                          | Meaning                                   |
|------------------|-------------------------------------------------|-------------------------------------------|
| `J/(kg·K)`       | `joulesPerKilogramKelvin`                       | specific heat capacity, base unit         |
| `m²·s⁻²·K⁻¹`     | `(meters pow 2) / (seconds pow 2) / ΔK`         | same quantity in base dimensions          |
| `cal/(g·K)`      | `caloriesPerGramKelvin`                         | calorie per gram-kelvin                   |
| `c = C / m`      | `(4184 of joulesPerKelvin) / (1 of kilo.grams)` | from heat capacity ÷ mass                 |
| `c = q / ΔT`     | `(8368 of joulesPerKilogram) / rise`            | from specific energy ÷ temperature rise   |
| `C = c · m`      | `water * bath`                                  | object heat capacity from material × mass |
| `Q = c · m · ΔT` | `water * bath * rise`                           | total energy                              |
