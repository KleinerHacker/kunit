# Molality

Package: `org.pcsoft.framework.kunit.thermo.molality`
Base unit: **mole per kilogram** (`KMolalityUnit.BASE == KMolalityUnit.MOLES_PER_KILOGRAM`)

Type: **constructed unit**

The molality `b` is how much of a substance is dissolved **per mass of solvent**: `b = n / m`. Unlike the
[concentration](concentration.md), which refers to a volume, the molality does not change when the solution
is heated — the solvent's mass is unaffected by thermal expansion. That makes it the quantity of choice for
colligative properties such as freezing-point depression and boiling-point elevation.

Its canonical base-dimension normal form is `substance¹ · mass⁻¹`.

## Named units

| Unit                    | Symbol    |                    Token | 1 unit in mol/kg |
|-------------------------|-----------|-------------------------:|-----------------:|
| Mole per kilogram       | `mol/kg`  |       `molesPerKilogram` |              1.0 |
| Millimole per kilogram  | `mmol/kg` | `millimolesPerKilogram`  |            0.001 |

All tokens accept every SI prefix (`milli.molesPerKilogram`, …).

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance. Note
that the native form is assembled from **unit templates**: for a group carrying a mass term the raw mixed
value is the gram-based product, while a typed instance stores its value in the named unit.

| Form             | Expression                                              |
|------------------|---------------------------------------------------------|
| typed operator   | `amountOfSubstance / mass`                              |
| native (`toX()`) | `(0.25 of moles / kilo.grams).toMolality()`             |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molality.*

val typed = (0.5 of moles) / (2 of kilo.grams)
val native = (0.25 of moles.toUnit() / kilo.grams.toUnit()).toMolality()

typed == native               // true
typed into molesPerKilogram   // 0.25
```

## Computing with the group

| Expression                     | Result type                      | Meaning                     |
|--------------------------------|----------------------------------|-----------------------------|
| `amountOfSubstance / mass`     | `KMolalityUnitInstance`          | `b = n / m`                 |
| `molality * mass`              | `KAmountOfSubstanceUnitInstance` | `n = b · m`                 |
| `amountOfSubstance / molality` | `KMassUnitInstance`              | the solvent mass needed     |
| `1 / molarMass`                | `KMolalityUnitInstance`          | molality of a pure substance |
| `1 / molality`                 | `KMolarMassUnitInstance`         | back to the molar mass      |

The last two relations reflect that molality and [molar mass](molar-mass.md) are reciprocals of each other.

## Real-world example — how many moles are in a kilogram of water?

Water has a molar mass of 18.015 g/mol, so one kilogram of it holds about 55.5 mol — the reciprocal
relation in action:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molality.*

val b = 1 / (18.015 of gramsPerMole)   // KMolalityUnitInstance
b into molesPerKilogram                 // ≈ 55.51

// A 0.5 molal salt solution in 2 kg of water
val n = (0.5 of molesPerKilogram) * (2 of kilo.grams)
n into moles                            // 1.0

// And back to the molar mass
(1 / b) into gramsPerMole               // ≈ 18.015
```

## Value semantics

`equals`/`hashCode` compare the **normalized mol/kg value**, so
`(1 of molesPerKilogram) == (1000 of millimolesPerKilogram)`. `toString()` renders the value in the base
unit: `"0.25 mol/kg"`.

## See also

* [Amount-of-Substance Concentration](concentration.md) — the same idea per volume.
* [Molar Mass](molar-mass.md) — the reciprocal quantity.
* [Thermodynamics overview](overview.md)
