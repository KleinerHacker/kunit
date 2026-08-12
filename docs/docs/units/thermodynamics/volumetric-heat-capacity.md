# Volumetric Heat Capacity

Package: `org.pcsoft.framework.kunit.thermo.volumetricheatcapacity`
Base unit: **joule per cubic metre kelvin**
(`KVolumetricHeatCapacityUnit.BASE == KVolumetricHeatCapacityUnit.JOULE_PER_CUBIC_METER_KELVIN`)

Type: **constructed unit**

The volumetric heat capacity `c_v` is how much heat a **volume** of a material stores per kelvin:
`c_v = C / V = c · ρ`. It is the quantity that decides how much thermal mass a building, a storage tank or
a heat sink actually has — two materials with the same specific heat capacity store very different amounts
of heat if their densities differ.

Its canonical base-dimension normal form is `mass · length⁻¹ · time⁻² · temperature⁻¹`.

## Named units

| Unit                               | Symbol         |                              Token | 1 unit in J/(m³·K) |
|------------------------------------|----------------|-----------------------------------:|-------------------:|
| Joule per cubic metre kelvin       | `J/(m^3*K)`    |       `joulesPerCubicMeterKelvin` |                1.0 |
| Calorie per cubic centimetre kelvin | `cal/(cm^3*K)` | `caloriesPerCubicCentimeterKelvin` |            4.184e6 |

Values are large, so the megajoule form is the practical one: water is ≈ 4.18 MJ/(m³·K). All tokens accept
every SI prefix (`mega.joulesPerCubicMeterKelvin`, …).

## Decompositions

The group has **two** decompositions. Both funnel into the same normalizing factory, so they produce the
same typed, value-equal instance:

| Form             | Expression                                                       |
|------------------|-------------------------------------------------------------------|
| typed operator A | `heatCapacity / volume`                                          |
| typed operator B | `specificHeatCapacity * density`                                 |
| native (`toX()`) | `(1 of kilo.grams / m / s² / K).toVolumetricHeatCapacity()`      |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.density.div
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin
import org.pcsoft.framework.kunit.thermo.volumetricheatcapacity.*

val water = (1000 of kilo.grams) / ((1 of meters) * (1 of meters) * (1 of meters))

val viaHeatCapacity = (4184 of joulesPerKelvin) / (1 of liters)   // A
val viaDensity = (4184 of joulesPerKilogramKelvin) * water        // B

viaHeatCapacity == viaDensity                                      // true
viaHeatCapacity into mega.joulesPerCubicMeterKelvin                // 4.184
```

## Computing with the group

| Expression                                    | Result type                             | Meaning              |
|-----------------------------------------------|-----------------------------------------|----------------------|
| `heatCapacity / volume`                       | `KVolumetricHeatCapacityUnitInstance`   | `c_v = C / V`        |
| `specificHeatCapacity * density`              | `KVolumetricHeatCapacityUnitInstance`   | `c_v = c · ρ`        |
| `volumetricHeatCapacity * volume`             | `KHeatCapacityUnitInstance`             | `C = c_v · V`        |
| `heatCapacity / volumetricHeatCapacity`       | `KVolumeUnitInstance`                   | the volume it belongs to |
| `volumetricHeatCapacity / density`            | `KSpecificHeatCapacityUnitInstance`     | back to `c`          |
| `volumetricHeatCapacity / specificHeatCapacity` | `KDensityUnitInstance`                | back to `ρ`          |

## Real-world example — thermal mass of a water buffer tank

A **300 l** buffer tank of water: how much energy does raising it by 1 K take, and how does that compare to
the same volume of concrete (≈ 2.0 MJ/(m³·K))?

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.heatcapacity.joulesPerKelvin
import org.pcsoft.framework.kunit.thermo.volumetricheatcapacity.*

val water = 4.184 of mega.joulesPerCubicMeterKelvin
val tank = water * (300 of liters)          // KHeatCapacityUnitInstance
tank into kilo.joulesPerKelvin              // ≈ 1255.2 kJ/K

val concrete = 2.0 of mega.joulesPerCubicMeterKelvin
(water into mega.joulesPerCubicMeterKelvin) /
    (concrete into mega.joulesPerCubicMeterKelvin)   // ≈ 2.09× the thermal mass
```

## Value semantics

`equals`/`hashCode` compare the **normalized J/(m³·K) value**, so
`(1 of caloriesPerCubicCentimeterKelvin) == (4.184e6 of joulesPerCubicMeterKelvin)`. `toString()` renders
the value in the base unit: `"4184000.0 J/(m^3*K)"`.

## See also

* [Heat Capacity](heat-capacity.md) — the un-normalized quantity.
* [Specific Heat Capacity](specific-heat-capacity.md) — the same idea per **mass** instead of per volume.
* [Thermodynamics overview](overview.md)
