# Amount-of-Substance Concentration (Molarity)

Package: `org.pcsoft.framework.kunit.thermo.concentration`
Base unit: **mole per cubic metre** (`KConcentrationUnit.BASE == KConcentrationUnit.MOLES_PER_CUBIC_METER`)

Type: **constructed unit**

The amount-of-substance concentration `c` is how much of a substance is dissolved **per volume of
solution**: `c = n / V`. Chemistry almost always quotes it in moles per litre and calls that the
**molarity**, written `M`; clinical laboratories use millimoles per litre.

Its canonical base-dimension normal form is `substance¹ · length⁻³`.

## Named units

| Unit                    | Symbol    |                 Token | 1 unit in mol/m³ |
|-------------------------|-----------|----------------------:|-----------------:|
| Mole per cubic metre    | `mol/m^3` | `molesPerCubicMeter`  |              1.0 |
| Mole per litre (molar)  | `mol/l`   | `molesPerLiter`       |             1000 |
| Molar (`M`)             | `mol/l`   | `molar`               |             1000 |
| Millimole per litre     | `mmol/l`  | `millimolesPerLiter`  |              1.0 |

`molar` is a second spelling of `molesPerLiter`, not a unit of its own. Note that a millimole per litre is
numerically the same as a mole per cubic metre — the SI base unit is exactly the clinical unit. All tokens
accept every SI prefix (`milli.molesPerLiter`, `micro.molar`, …).

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance:

| Form             | Expression                                                                |
|------------------|---------------------------------------------------------------------------|
| typed operator   | `amountOfSubstance / volume`                                              |
| native (`toX()`) | `((0.5 of moles).toUnit() / (2 of liters).toUnit()).toConcentration()`    |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.*

val typed = (0.5 of moles) / (2 of liters)
val native = ((0.5 of moles).toUnit() / (2 of liters).toUnit()).toConcentration()

typed == native            // true
typed into molesPerLiter   // 0.25
```

## Computing with the group

| Expression                          | Result type                       | Meaning                     |
|-------------------------------------|-----------------------------------|-----------------------------|
| `amountOfSubstance / volume`        | `KConcentrationUnitInstance`      | `c = n / V`                 |
| `concentration * volume`            | `KAmountOfSubstanceUnitInstance`  | `n = c · V`                 |
| `amountOfSubstance / concentration` | `KVolumeUnitInstance`             | the volume needed           |
| `conductivity / concentration`      | `KMolarConductivityUnitInstance`  | `Λ = κ / c`                 |

## Real-world example — blood glucose

A fasting blood glucose of **5.5 mmol/l** in about 5 l of blood corresponds to:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.*

val c = 5.5 of millimolesPerLiter
c into molesPerCubicMeter          // 5.5 — the SI unit is numerically the clinical one

val n = c * (5 of liters)          // KAmountOfSubstanceUnitInstance
n into milli.moles                 // 27.5 mmol of glucose in the bloodstream

// How much solution holds 1 mol at that concentration?
val v = (1 of moles) / c           // KVolumeUnitInstance
v into liters                       // ≈ 181.8 l
```

## Value semantics

`equals`/`hashCode` compare the **normalized mol/m³ value**, so
`(1 of molesPerLiter) == (1000 of molesPerCubicMeter)`. `toString()` renders the value in the base unit:
`"1000.0 mol/m^3"`.

## See also

* [Molality](molality.md) — the same idea per **mass** of solvent, independent of thermal expansion.
* [Amount of Substance](amount-of-substance.md) — the numerator.
* [Molar Volume](molar-volume.md) — the reciprocal quantity for a pure substance.
* [Thermodynamics overview](overview.md)
