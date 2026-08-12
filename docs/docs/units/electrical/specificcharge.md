# Specific Charge

Package: `org.pcsoft.framework.kunit.electric.specificcharge`
Base unit: **coulomb per kilogram**
(`KSpecificChargeUnit.BASE == KSpecificChargeUnit.COULOMB_PER_KILOGRAM`)

Type: **constructed unit**

The specific charge `q/m` is the charge a body carries per unit of its mass. It is the quantity J. J.
Thomson measured to identify the electron, and it is what mass spectrometry separates particles by.

Its canonical base-dimension normal form is `current · time · mass⁻¹`.

!!! note "One group, two readings"
    The same dimension carries the **ionisation dose** (exposure) of radiation protection, historically
    measured in roentgen — see [Exposure](../thermodynamics/exposure.md). A single normal form maps to a
    single type, so both readings share this group; the roentgen is one of its named units. Distinguish
    them by naming your values.

## Named units

| Unit                 | Symbol |                 Token | 1 unit in C/kg |
|----------------------|--------|----------------------:|---------------:|
| Coulomb per kilogram | `C/kg` | `coulombsPerKilogram` |            1.0 |
| Roentgen             | `R`    |            `roentgens` |        2.58e-4 |

All tokens accept every SI prefix (`milli.roentgens`, …).

## Constant

| Constant                    | Value               | Meaning                                  |
|-----------------------------|---------------------|------------------------------------------|
| `ELECTRON_SPECIFIC_CHARGE`  | `1.75882001076e11 C/kg` | the electron's charge-to-mass ratio  |

The sign is omitted: the electron's charge is negative, but the ratio is quoted as a magnitude.

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance. The
native form is assembled from **unit templates** because the group carries a mass term.

| Form             | Expression                                              |
|------------------|----------------------------------------------------------|
| typed operator   | `charge / mass`                                         |
| native (`toX()`) | `(2 of A · s / kilo.grams).toSpecificCharge()`          |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

val typed = (4 of coulombs) / (2 of kilo.grams)
val native = (2 of amperes.toUnit() * (seconds pow 1) / kilo.grams.toUnit()).toSpecificCharge()

typed == native                   // true
typed into coulombsPerKilogram    // 2.0
```

## Computing with the group

| Expression                  | Result type                     | Meaning              |
|-----------------------------|---------------------------------|----------------------|
| `charge / mass`             | `KSpecificChargeUnitInstance`   | `q/m`                |
| `specificCharge * mass`     | `KChargeUnitInstance`           | the total charge     |
| `charge / specificCharge`   | `KMassUnitInstance`             | the carrying mass    |

## Real-world example — the electron, and an exposure reading

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

// Thomson's ratio
val electron = ELECTRON_SPECIFIC_CHARGE of coulombsPerKilogram
electron into coulombsPerKilogram          // ≈ 1.7588e11

// The exposure reading of a survey meter, and the charge it liberates in 1 kg of air
val exposure = 1 of roentgens
exposure into coulombsPerKilogram          // 2.58e-4
(exposure * (1 of kilo.grams)) into coulombs   // 2.58e-4
```

## Value semantics

`equals`/`hashCode` compare the **normalized C/kg value**, so
`(1 of roentgens) == (2.58e-4 of coulombsPerKilogram)`. `toString()` renders the value in the base unit:
`"1.0 C/kg"`.

## See also

* [Charge](charge.md) and [Mass](../mechanics/mass.md) — the two operands.
* [Exposure](../thermodynamics/exposure.md) — the same type read as an ionisation dose.
* [Electrical Engineering overview](overview.md)
