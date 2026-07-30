# Molar Heat Capacity

Package: `org.pcsoft.framework.kunit.thermo.molarheatcapacity`
Base unit: **joule per mole-kelvin** (`KMolarHeatCapacityUnit.BASE == KMolarHeatCapacityUnit.JOULE_PER_MOLE_KELVIN`)

Type: **constructed unit**

Molar heat capacity is the [heat capacity](heat-capacity.md) of a substance *per mole*: `J/(mol·K)`. It is
the natural form for gases and for chemical thermodynamics, where amounts are counted in moles rather
than kilograms (that is [specific heat capacity](specific-heat-capacity.md)).

`KMolarHeatCapacityUnitInstance` wraps a `KMixedUnitInstance` of exactly five terms in the canonical
normal form `mass¹ · distance² · time⁻² · substance⁻¹ · temperature⁻¹` (`kg·m²·s⁻²·mol⁻¹·K⁻¹`). The
temperature dimension is the **difference** group, never the affine absolute temperature.

## Named units

| Unit | Symbol | Token | 1 unit in J/(mol·K) |
|---|---|---:|---:|
| Joule per mole-kelvin | `J/(mol·K)` | `joulesPerMoleKelvin` | 1.0 |
| Calorie per mole-kelvin | `cal/(mol·K)` | `caloriesPerMoleKelvin` | 4.184 |

Both accept the full SI prefix range (`kilo.joulesPerMoleKelvin`, `milli.joulesPerMoleKelvin`, …).

## The gas constant

The group exposes the exact SI value of the molar gas constant as `GAS_CONSTANT`
(8.31446261815324 J/(mol·K)) — a plain `Double`, so it can serve both as a factor and as a reading.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*

val r = GAS_CONSTANT of joulesPerMoleKelvin
r into joulesPerMoleKelvin   // 8.31446261815324
r into caloriesPerMoleKelvin // ≈ 1.987
```

## Real-world example: heating nitrogen (Dulong-Petit sanity check)

Diatomic nitrogen has `c_p ≈ 29.1 J/(mol·K)`. How much energy does heating 3 moles by 50 K take, and
what is that per mole?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.molarenergy.joulesPerMole
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val nitrogen = 29.1 of joulesPerMoleKelvin
val sample = 3 of moles
val rise = KTemperatureDifference.ofKelvin(50)

// route 1: the sample's heat capacity, then the energy
val sampleCapacity = nitrogen * sample     // KHeatCapacityUnitInstance
sampleCapacity into joulesPerKelvin        // 87.3 J/K
val energy = sampleCapacity * rise         // KEnergyUnitInstance
energy into joules                         // 4365.0 J

// route 2: per mole first
val perMole = nitrogen * rise              // KMolarEnergyUnitInstance
perMole into joulesPerMole                 // 1455.0 J/mol
val sameEnergy = perMole * sample          // KEnergyUnitInstance
sameEnergy into joules                     // 4365.0 J - identical
```

## Computing with the neighbouring units

| Expression | Result type | Meaning |
|---|---|---|
| `heatCapacity / amountOfSubstance` | `KMolarHeatCapacityUnitInstance` | substance property from a sample |
| `molarEnergy / temperatureDifference` | `KMolarHeatCapacityUnitInstance` | same, via molar energy |
| `molarHeatCapacity * amountOfSubstance` | `KHeatCapacityUnitInstance` | the sample's heat capacity |
| `amountOfSubstance * molarHeatCapacity` | `KHeatCapacityUnitInstance` | same (commutative) |
| `heatCapacity / molarHeatCapacity` | `KAmountOfSubstanceUnitInstance` | amount of substance |
| `molarHeatCapacity * temperatureDifference` | `KMolarEnergyUnitInstance` | energy per mole |
| `temperatureDifference * molarHeatCapacity` | `KMolarEnergyUnitInstance` | same (commutative) |
| `molarEnergy / molarHeatCapacity` | `KTemperatureDifferenceUnitInstance` | achievable rise |

## Decompositions

All three decompositions produce the same typed, value-equal instance.

| Decomposition | Form | Result |
|---|---|---|
| `heatCapacity / amountOfSubstance` | typed operator | `KMolarHeatCapacityUnitInstance` |
| `molarEnergy / temperatureDifference` | typed operator | `KMolarHeatCapacityUnitInstance` |
| `mass · distance² · time⁻² · substance⁻¹ · temperature⁻¹` | native + `toMolarHeatCapacity()` | `KMolarHeatCapacityUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.molarenergy.joulesPerMole
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val viaHeatCapacity = (1 of joulesPerKelvin) / (1 of moles)
val viaMolarEnergy  = (1 of joulesPerMole) / KTemperatureDifference.ofKelvin(1)
val native = (
    (1000 of grams).toUnit() *
        ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        (1 of moles).toUnit() /
        KTemperatureDifference.ofKelvin(1).toUnit()
    ).toMolarHeatCapacity()

viaHeatCapacity == viaMolarEnergy // true
viaHeatCapacity == native         // true - all are 1.0 J/(mol·K)
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*

val total = (1 of kilo.joulesPerMoleKelvin) + (500 of joulesPerMoleKelvin)  // 1500 J/(mol·K)
(1 of kilo.joulesPerMoleKelvin) > (500 of joulesPerMoleKelvin)              // true
(1 of kilo.joulesPerMoleKelvin) == (1000 of joulesPerMoleKelvin)            // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.molarheatcapacity.*

(29.1 of joulesPerMoleKelvin).toString()                                     // "29.1 J/(mol·K)"
"${(29.1 of joulesPerMoleKelvin) into caloriesPerMoleKelvin} cal/(mol·K)"    // "6.955... cal/(mol·K)"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `J/(mol·K)` | `joulesPerMoleKelvin` | molar heat capacity, base unit |
| `kg·m²·s⁻²·mol⁻¹·K⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / moles / ΔK` | base dimensions |
| `cal/(mol·K)` | `caloriesPerMoleKelvin` | calorie per mole-kelvin |
| `R` | `GAS_CONSTANT of joulesPerMoleKelvin` | molar gas constant, 8.3145 J/(mol·K) |
| `C_m = C / n` | `(58.2 of joulesPerKelvin) / (2 of moles)` | from heat capacity ÷ amount |
| `C_m = ΔH_m / ΔT` | `(58.2 of joulesPerMole) / rise` | from molar energy ÷ temperature rise |
| `Q = C_m · n · ΔT` | `nitrogen * sample * rise` | total energy |
