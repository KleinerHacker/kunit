# Amount of Substance

Package: `org.pcsoft.framework.kunit.thermo.amountofsubstance`
Base unit: **mole** (`KAmountOfSubstanceUnit.BASE == KAmountOfSubstanceUnit.MOLE`)

Type: **native unit**

Amount of substance is one of the seven SI base quantities — a directly measurable, non-composed quantity, hence a
**native unit**. `KAmountOfSubstanceUnitInstance` is the plain, one-dimensional wrapper shape: a single
`KAmountOfSubstanceUnit.BASE` (mole) term at exponent 1, always normalized to moles.

It is the foundation of every *molar* quantity in the thermodynamics field
([molar energy](molar-energy.md), [molar heat capacity](molar-heat-capacity.md)).

## Named units

| Unit       | Symbol  |        Token | 1 unit in mol |
|------------|---------|-------------:|--------------:|
| Mole       | `mol`   |      `moles` |           1.0 |
| Pound-mole | `lbmol` | `poundMoles` |     453.59237 |

Both accept the full SI prefix range (`milli.moles`, `micro.moles`, `kilo.moles`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

val n = 2 of moles
n.value                 // 2.0 (normalized to moles)
n into milli.moles      // 2000.0
(1 of kilo.moles) into moles // 1000.0
(1 of poundMoles) into moles // 453.59237
```

## The Avogadro constant

The group exposes the exact SI value of the Avogadro constant as `AVOGADRO_CONSTANT`
(6.02214076e23 mol⁻¹) and the convenience `particleCount()` on an instance. Both return a plain `Double`, because a
particle count is dimensionless.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

AVOGADRO_CONSTANT             // 6.02214076e23
(2 of moles).particleCount()  // ≈ 1.20443e24 particles
```

## Real-world example: dissolving table salt

How many moles of sodium chloride (molar mass 58.44 g/mol) are in 25 g of table salt, and how many formula units is
that?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

val molarMass = 58.44        // g/mol for NaCl
val sample = 25 of grams

val n = (sample.value / molarMass) of moles
n into moles                 // ≈ 0.4278 mol
n into milli.moles           // ≈ 427.8 mmol
n.particleCount()            // ≈ 2.576e23 formula units
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

// + / - : same group, automatic conversion between different units and prefixes
val total = (1 of moles) + (500 of milli.moles)   // 1.5 mol
val rest  = (1 of moles) - (250 of milli.moles)   // 0.75 mol

// comparisons (by normalized mole value)
(1 of moles) > (500 of milli.moles)   // true
(1 of moles) == (1000 of milli.moles) // true
```

Multiplying or dividing an amount of substance by another quantity escapes to the generic mixed-unit engine unless a
typed result exists — e.g. `energy / amountOfSubstance` is a typed
[molar energy](molar-energy.md).

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.thermo.amountofsubstance.*

(2 of moles).toString()                        // "2.0 mol"
"${(2 of moles) into milli.moles} mmol"        // "2000.0 mmol"
```

## Notation

The table below shows how this unit is written mathematically versus in Kotlin with KUnit. Exponents use Unicode
superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction.

| Mathematics   | Kotlin                                | Meaning                                        |
|---------------|---------------------------------------|------------------------------------------------|
| `mol`         | `moles`                               | amount of substance, base unit                 |
| `mmol`        | `milli.moles`                         | millimole                                      |
| `kmol`        | `kilo.moles`                          | kilomole                                       |
| `lbmol`       | `poundMoles`                          | pound-mole (imperial engineering unit)         |
| `n = m / M`   | `(sample.value / molarMass) of moles` | amount from mass ÷ molar mass                  |
| `N = n · N_A` | `n.particleCount()`                   | particle count from amount × Avogadro constant |
