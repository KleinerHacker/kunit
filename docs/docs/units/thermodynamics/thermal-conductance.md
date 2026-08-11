# Thermal Conductance

Package: `org.pcsoft.framework.kunit.thermo.conductance`
Base unit: **watt per kelvin** (`KThermalConductanceUnit.BASE == KThermalConductanceUnit.WATT_PER_KELVIN`)

Type: **constructed unit**

The thermal conductance `G` of a component is how much heat flows through it per unit of temperature
difference: `G = P / ΔT`, measured in `W/K`. It is the exact reciprocal of the
[absolute thermal resistance](thermal-resistance.md), and the more convenient form whenever heat paths sit
in **parallel** — parallel conductances simply add.

Its canonical base-dimension normal form is `mass · length² · time⁻³ · temperature⁻¹`.

## Named units

| Unit                          | Symbol       |                   Token | 1 unit in W/K |
|-------------------------------|--------------|------------------------:|--------------:|
| Watt per kelvin               | `W/K`        |         `wattsPerKelvin` |           1.0 |
| Btu per hour degree-Fahrenheit | `Btu/(h*°F)` | `btusPerHourFahrenheit` |     ≈ 0.52753 |

All tokens accept every SI prefix (`milli.wattsPerKelvin`, …).

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance. The
native form is assembled from **unit templates** because the group carries a mass term.

| Form             | Expression                                                    |
|------------------|----------------------------------------------------------------|
| typed operator   | `power / temperatureDifference`                               |
| native (`toX()`) | `(0.4 of kilo.grams · m² / s³ / K).toThermalConductance()`    |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.conductance.*

val typed = (12 of watts) / KTemperatureDifference.ofKelvin(30)
val kelvinTerm = KTemperatureDifference.ofKelvin(1).toUnit()
val native = (0.4 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 3) / kelvinTerm)
    .toThermalConductance()

typed == native            // true
typed into wattsPerKelvin  // 0.4
```

## Computing with the group

| Expression                             | Result type                          | Meaning                 |
|----------------------------------------|--------------------------------------|-------------------------|
| `power / temperatureDifference`        | `KThermalConductanceUnitInstance`    | `G = P / ΔT`            |
| `thermalConductance * temperatureDifference` | `KPowerUnitInstance`           | `P = G · ΔT`            |
| `power / thermalConductance`           | `KTemperatureDifferenceUnitInstance` | the difference needed   |
| `thermalConductance + …`                | `KThermalConductanceUnitInstance`    | heat paths in parallel  |
| `1 / thermalConductance`               | `KThermalResistanceUnitInstance`     | `R = 1 / G`             |
| `1 / thermalResistance`                | `KThermalConductanceUnitInstance`    | `G = 1 / R`             |

## Real-world example — two parallel heat paths

A module loses heat through its baseplate (0.4 W/K) and through its housing (0.1 W/K). In parallel the
conductances add, and the reciprocal gives back the total resistance:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.thermo.resistance.kelvinsPerWatt
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.conductance.*

val total = (0.4 of wattsPerKelvin) + (0.1 of wattsPerKelvin)
total into wattsPerKelvin                                  // 0.5

val r = 1 / total                                           // KThermalResistanceUnitInstance
r into kelvinsPerWatt                                       // 2.0

val heat = total * KTemperatureDifference.ofKelvin(30)      // KPowerUnitInstance
heat into watts                                             // 15.0 W carried away at ΔT = 30 K
```

## Value semantics

`equals`/`hashCode` compare the **normalized W/K value**, so
`(1 of wattsPerKelvin) == (1000 of milli.wattsPerKelvin)`. `toString()` renders the value in the base unit:
`"0.4 W/K"`.

## See also

* [Absolute Thermal Resistance](thermal-resistance.md) — the reciprocal quantity.
* [Thermal Insulance](thermal-insulance.md) — the per-area form of the resistance.
* [Heat Transfer Coefficient](heat-transfer-coefficient.md) — the per-area form of this quantity.
* [Thermodynamics overview](overview.md)
