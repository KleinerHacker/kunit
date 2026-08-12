# Absolute Thermal Resistance

Package: `org.pcsoft.framework.kunit.thermo.resistance`
Base unit: **kelvin per watt** (`KThermalResistanceUnit.BASE == KThermalResistanceUnit.KELVIN_PER_WATT`)

Type: **constructed unit**

The absolute thermal resistance `R` of a component is the temperature difference it sustains per unit of
heat flowing through it: `R = ΔT / P`, measured in `K/W`. It describes a **whole object** — this heat sink,
this transistor package, this wall of this size.

Its canonical base-dimension normal form is `mass⁻¹ · length⁻² · time³ · temperature`.

!!! warning "Not the same as thermal insulance"
    Do not confuse this group with the [thermal insulance](thermal-insulance.md) `m²·K/W` (the R-value),
    which is the same idea normalized **per unit of area**. The two differ by a factor of area, have
    different normal forms and therefore different types. Up to and including version 0.8.0 the name
    `thermo.resistance` / `KThermalResistanceUnit` referred to the insulance; it now refers to this group.

## Named units

| Unit                       | Symbol     |                   Token | 1 unit in K/W |
|----------------------------|------------|------------------------:|--------------:|
| Kelvin per watt            | `K/W`      |         `kelvinsPerWatt` |           1.0 |
| Degree Celsius per watt    | `°C/W`     |  `degreesCelsiusPerWatt` |           1.0 |
| Hour °F per Btu            | `h*°F/Btu` |    `hourFahrenheitPerBtu` |     ≈ 1.89563 |

A temperature *difference* of 1 °C is 1 K, so `degreesCelsiusPerWatt` — the spelling on semiconductor and
heat-sink datasheets — is numerically identical to `kelvinsPerWatt`. All tokens accept every SI prefix.

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance. The
native form is assembled from **unit templates** because the group carries a mass term: the raw mixed value
is the gram-based product, while a typed instance stores its value in the named unit.

| Form             | Expression                                                            |
|------------------|------------------------------------------------------------------------|
| typed operator   | `temperatureDifference / power`                                        |
| native (`toX()`) | `(2.5 of s³ · K / kilo.grams / m²).toThermalResistance()`              |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.resistance.*

val typed = KTemperatureDifference.ofKelvin(30) / (12 of watts)
val kelvinTerm = KTemperatureDifference.ofKelvin(1).toUnit()
val native = (2.5 of (seconds pow 3) * kelvinTerm / kilo.grams.toUnit() / (meters pow 2))
    .toThermalResistance()

typed == native            // true
typed into kelvinsPerWatt  // 2.5
```

## Computing with the group

| Expression                                | Result type                            | Meaning              |
|-------------------------------------------|----------------------------------------|----------------------|
| `temperatureDifference / power`           | `KThermalResistanceUnitInstance`       | `R = ΔT / P`         |
| `thermalResistance * power`               | `KTemperatureDifferenceUnitInstance`   | `ΔT = R · P`         |
| `temperatureDifference / thermalResistance` | `KPowerUnitInstance`                 | the heat flow driven |
| `thermalResistance + …`                   | `KThermalResistanceUnitInstance`       | resistances in series |
| `1 / thermalResistance`                   | `KThermalConductanceUnitInstance`      | `G = 1 / R`          |

Thermal resistances in **series add up** — which is exactly what the group's same-type `+` does.

## Real-world example — a heat-sink budget

A power transistor dissipates **12 W**. The thermal chain is 0.5 K/W junction-to-case, 0.2 °C/W
case-to-heatsink and 1.8 K/W heatsink-to-air. How far above ambient does the junction sit?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference
import org.pcsoft.framework.kunit.thermo.resistance.*

val chain = (0.5 of kelvinsPerWatt) + (0.2 of degreesCelsiusPerWatt) + (1.8 of kelvinsPerWatt)
chain into kelvinsPerWatt                                   // 2.5

val rise = chain * (12 of watts)                            // KTemperatureDifferenceUnitInstance
rise into KTemperatureDifference.ofKelvin(1)                // 30.0 K above ambient

// How much power may it dissipate for a 25 K limit?
val budget = KTemperatureDifference.ofKelvin(25) / chain    // KPowerUnitInstance
budget into watts                                            // 10.0 W
```

## Value semantics

`equals`/`hashCode` compare the **normalized K/W value**, so
`(1 of kelvinsPerWatt) == (1 of degreesCelsiusPerWatt)`. `toString()` renders the value in the base unit:
`"2.5 K/W"`.

## See also

* [Thermal Insulance](thermal-insulance.md) — the same idea per unit of area (the R-value).
* [Thermal Conductance](thermal-conductance.md) — the reciprocal quantity.
* [Thermodynamics overview](overview.md)
