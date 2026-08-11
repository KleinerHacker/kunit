# Catalytic Activity

Package: `org.pcsoft.framework.kunit.thermo.catalyticactivity`
Base unit: **katal** (`KCatalyticActivityUnit.BASE == KCatalyticActivityUnit.KATAL`)

Type: **constructed unit**

The catalytic activity `z` of an enzyme preparation is how much substrate it converts **per time**:
`z = n / t`. Its SI unit is the **katal** (1 kat = 1 mol/s) — a very large unit, so practice works in
microkatals or in the traditional **enzyme unit** `U` (one micromole per minute).

Its canonical base-dimension normal form is `substance¹ · time⁻¹`.

## Named units

| Unit        | Symbol |         Token |          1 unit in kat |
|-------------|--------|--------------:|-----------------------:|
| Katal       | `kat`  |      `katals` |                    1.0 |
| Enzyme unit | `U`    | `enzymeUnits` | 1/60 × 10⁻⁶ ≈ 1.667e-8 |

1 U = 1 µmol/min, so 1 kat = 60 000 000 U and 1 U ≈ 16.67 nkat. All tokens accept every SI prefix
(`micro.katals`, `nano.katals`, …).

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance:

| Form             | Expression                                                                   |
|------------------|------------------------------------------------------------------------------|
| typed operator   | `amountOfSubstance / time`                                                   |
| native (`toX()`) | `((2 of moles).toUnit() / (4 of seconds).toUnit()).toCatalyticActivity()`    |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.catalyticactivity.*

val typed = (2 of moles) / (4 of seconds)
val native = ((2 of moles).toUnit() / (4 of seconds).toUnit()).toCatalyticActivity()

typed == native      // true
typed into katals    // 0.5
```

## Computing with the group

| Expression                               | Result type                      | Meaning              |
|------------------------------------------|----------------------------------|----------------------|
| `amountOfSubstance / time`               | `KCatalyticActivityUnitInstance` | `z = n / t`          |
| `catalyticActivity * time`               | `KAmountOfSubstanceUnitInstance` | `n = z · t`          |
| `amountOfSubstance / catalyticActivity`  | `KTimeUnitInstance`              | how long it takes    |

## Real-world example — an enzyme assay

An assay converts **0.5 mmol** of substrate in **10 s**. Expressed both ways, and the time a smaller batch
would take:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.catalyticactivity.*

val z = (0.5 of milli.moles) / (10 of seconds)
z into micro.katals        // 50.0
z into enzymeUnits         // ≈ 3000.0 U

// The enzyme unit by definition: one micromole per minute
val one = (1 of micro.moles) / (1 of minutes)
one into enzymeUnits       // 1.0

// How long for 2 mmol at that activity?
val t = (2 of milli.moles) / z
t into seconds             // 40.0
```

## Value semantics

`equals`/`hashCode` compare the **normalized katal value**, so `(1 of katals) == (1000 of milli.katals)`.
`toString()` renders the value in the base unit: `"5.0E-5 kat"`.

## See also

* [Amount of Substance](amount-of-substance.md) — the numerator.
* [Amount-of-Substance Concentration](concentration.md) — what an assay usually measures.
* [Thermodynamics overview](overview.md)
