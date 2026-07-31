# Molar Mass

Package: `org.pcsoft.framework.kunit.thermo.molarmass`
Base unit: **gram per mole** (`KMolarMassUnit.BASE == KMolarMassUnit.GRAM_PER_MOLE`)

Type: **constructed unit**

Molar mass is mass per amount of substance: `mass / amountOfSubstance` (`g/mol`). It is the bridge between the
macroscopic world (grams on a balance) and the particle world (moles), and numerically equals the relative atomic or
molecular mass of a substance.

`KMolarMassUnitInstance` wraps a `KMixedUnitInstance` of exactly two terms in the canonical normal form
`mass¹ · substance⁻¹` (`g·mol⁻¹`), always normalized to g/mol. Because the library normalizes masses to grams, the raw
component base *is* the named base unit — no bridging factor is involved.

Divided by a density it becomes the [molar volume](molar-volume.md); every element of the
[periodic table](../../periodic-table.md) exposes its molar mass as a value of this group.

## Named units

| Unit                 | Symbol     |                Token | 1 unit in g/mol |
|----------------------|------------|---------------------:|----------------:|
| Gram per mole        | `g/mol`    |       `gramsPerMole` |             1.0 |
| Kilogram per mole    | `kg/mol`   |   `kilogramsPerMole` |          1000.0 |
| Pound per pound-mole | `lb/lbmol` | `poundsPerPoundMole` |             1.0 |
| Dalton per entity    | `Da`       |   `daltonsPerEntity` |   1.00000000105 |

The pound-mole is defined so that its mass in pounds equals the molar mass, which makes `lb/lbmol`
numerically identical to `g/mol`. Since the 2019 SI redefinition the molar mass constant is no longer exactly 1 g/mol,
hence the dalton factor. All units accept the full SI prefix range (`kilo.gramsPerMole`, `milli.kilogramsPerMole`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarmass.*

val water = 18.015 of gramsPerMole
water into gramsPerMole      // 18.015
water into kilogramsPerMole  // 0.018015
water into daltonsPerEntity  // ≈ 18.015 Da per molecule
```

## Real-world example: weighing out a mole

A recipe calls for 0.25 mol of table salt (NaCl, 58.44 g/mol). How much do you weigh out — and how many moles are in a
500 g package?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.*

val saltMolarMass = 58.44 of gramsPerMole

// How much mass are 0.25 mol?
val portion = saltMolarMass * (0.25 of moles) // KMassUnitInstance
portion into grams                            // 14.61 g

// How many moles are in a 500 g package?
val amount = (500 of grams) / saltMolarMass   // KAmountOfSubstanceUnitInstance
amount into moles                             // ≈ 8.556 mol

// And the molar mass itself, measured from a weighed sample:
val measured = (14.61 of grams) / (0.25 of moles)
measured into gramsPerMole                    // 58.44
```

## Computing with the core units (mass & amount of substance)

| Expression                      | Result type                      | Meaning                         |
|---------------------------------|----------------------------------|---------------------------------|
| `mass / amountOfSubstance`      | `KMolarMassUnitInstance`         | molar mass                      |
| `molarMass * amountOfSubstance` | `KMassUnitInstance`              | total mass                      |
| `amountOfSubstance * molarMass` | `KMassUnitInstance`              | total mass (commutative)        |
| `mass / molarMass`              | `KAmountOfSubstanceUnitInstance` | amount of substance contained   |
| `molarMass / density`           | `KMolarVolumeUnitInstance`       | [molar volume](molar-volume.md) |

## Decompositions

Both decompositions produce the same typed, value-equal instance.

| Decomposition              | Form                                | Result                            |
|----------------------------|-------------------------------------|-----------------------------------|
| `mass / amountOfSubstance` | typed operator                      | `KMolarMassUnitInstance` directly |
| `mass · substance⁻¹`       | native expression + `toMolarMass()` | `KMolarMassUnitInstance`          |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.*

// typed operator form
val typed = (18.015 of grams) / (1 of moles)

// native base-dimension form (g·mol⁻¹), recognised by toMolarMass()
val native = ((18.015 of grams).toUnit() / (1 of moles).toUnit()).toMolarMass()

typed == native // true - both are 18.015 g/mol
```

`toMolarMass()` recognises **only** the canonical normal form; a wrong shape throws
`IllegalStateException`.

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarmass.*

val total = (10 of gramsPerMole) + (4 of gramsPerMole) // 14 g/mol
val rest  = (10 of gramsPerMole) - (4 of gramsPerMole) // 6 g/mol

(1 of kilogramsPerMole) > (500 of gramsPerMole)   // true
(1 of kilogramsPerMole) == (1000 of gramsPerMole) // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarmass.*

(1 of kilogramsPerMole).toString()  // "1000.0 g/mol"
(18.015 of gramsPerMole).toString() // "18.015 g/mol"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics   | Kotlin                               | Meaning                                |
|---------------|--------------------------------------|----------------------------------------|
| `g/mol`       | `gramsPerMole`                       | molar mass, base unit — named token    |
| `g·mol⁻¹`     | `grams / moles`                      | same quantity in base dimensions       |
| `kg/mol`      | `kilogramsPerMole`                   | kilogram per mole                      |
| `Da`          | `daltonsPerEntity`                   | dalton per elementary entity           |
| `M = m / n`   | `(14.61 of grams) / (0.25 of moles)` | molar mass from mass ÷ amount          |
| `m = M · n`   | `saltMolarMass * (0.25 of moles)`    | mass from molar mass × amount          |
| `n = m / M`   | `(500 of grams) / saltMolarMass`     | amount from mass ÷ molar mass          |
| `V_m = M / ρ` | `molarMass / density`                | molar volume from molar mass ÷ density |
