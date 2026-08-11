# Dose Rate

Package: `org.pcsoft.framework.kunit.thermo.doserate`
Base unit: **gray per second** (`KDoseRateUnit.BASE == KDoseRateUnit.GRAY_PER_SECOND`)

Type: **constructed unit**

The dose rate is the radiation dose absorbed **per time**: `Ḋ = D / t`. It is what a survey meter reads —
almost always in microsieverts per hour — while the accumulated dose is the integral over the exposure
time.

Its canonical base-dimension normal form is `length² · time⁻³`. The kilogram of the gray's `J/kg` cancels
against the joule's, which is why no mass term remains.

## Named units

| Unit               | Symbol | Token               | 1 unit in Gy/s |
|--------------------|--------|---------------------|---------------:|
| Gray per second    | `Gy/s` | `graysPerSecond`    |            1.0 |
| Gray per hour      | `Gy/h` | `graysPerHour`      |         1/3600 |
| Sievert per second | `Sv/s` | `sievertsPerSecond` |            1.0 |
| Sievert per hour   | `Sv/h` | `sievertsPerHour`   |         1/3600 |

The gray (absorbed dose) and the sievert (equivalent dose) share one dimension, so KUnit models one group
for both — the sievert spellings exist so radiation-protection readings can be written directly. All tokens
accept every SI prefix; `micro.sievertsPerHour` is the everyday one.

!!! note "One group, two readings"
    Gray and sievert differ by the dimensionless radiation weighting factor, not by dimension. A single
    normal form must map to a single type (see [entropy](entropy.md) for the same argument), so the
    distinction is a matter of what you name your value.

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance:

| Form             | Expression                                                                    |
|------------------|--------------------------------------------------------------------------------|
| typed operator   | `specificEnergy / time`                                                       |
| native (`toX()`) | `((6 of joulesPerKilogram).toUnit() / (2 of seconds).toUnit()).toDoseRate()`  |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import org.pcsoft.framework.kunit.thermo.doserate.*

val typed = (6 of joulesPerKilogram) / (2 of seconds)
val native = ((6 of joulesPerKilogram).toUnit() / (2 of seconds).toUnit()).toDoseRate()

typed == native            // true
typed into graysPerSecond  // 3.0
```

## Computing with the group

| Expression                   | Result type                     | Meaning                  |
|------------------------------|---------------------------------|--------------------------|
| `specificEnergy / time`      | `KDoseRateUnitInstance`         | `Ḋ = D / t`              |
| `doseRate * time`            | `KSpecificEnergyUnitInstance`   | the accumulated dose     |
| `specificEnergy / doseRate`  | `KTimeUnitInstance`             | the exposure time        |

The absorbed dose itself is the [specific energy](specific-energy.md) group — 1 Gy = 1 J/kg.

## Real-world example — annual background radiation

Natural background is roughly **0.274 µSv/h**. Over a year (8766 h) that accumulates to the familiar
2.4 mSv:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.thermo.specificenergy.joulesPerKilogram
import org.pcsoft.framework.kunit.thermo.doserate.*

val background = 0.274 of micro.sievertsPerHour
val year = 8766 of hours

val dose = background * year                       // KSpecificEnergyUnitInstance
dose into milli.joulesPerKilogram                  // ≈ 2.4 (mSv)

// How long until a 1 mSv limit is reached?
val t = (1 of milli.joulesPerKilogram) / background
t into hours                                        // ≈ 3650 h
```

## Value semantics

`equals`/`hashCode` compare the **normalized Gy/s value**, so
`(1 of graysPerHour) == (1 of sievertsPerHour)`. `toString()` renders the value in the base unit:
`"1.0 Gy/s"`.

## See also

* [Specific Energy](specific-energy.md) — the absorbed dose itself (`Gy` = `J/kg`).
* [Thermodynamics overview](overview.md)
