# Molar Conductivity

Package: `org.pcsoft.framework.kunit.electric.molarconductivity`
Base unit: **siemens square metre per mole**
(`KMolarConductivityUnit.BASE == KMolarConductivityUnit.SIEMENS_SQUARE_METER_PER_MOLE`)

Type: **constructed unit**

The molar conductivity `Λ` of an electrolyte is its [conductivity](conductivity.md) normalized by the
[concentration](../thermodynamics/concentration.md): `Λ = κ / c`. Dividing out the concentration makes
solutions of different strength comparable — it answers "how well does *this ion* conduct", not "how well
does this particular beaker conduct".

Its canonical base-dimension normal form is `mass⁻¹ · time³ · current² · substance⁻¹`. The length dimension
cancels completely: the conductivity contributes `length⁻³` and the concentration another `length⁻³` in the
denominator.

## Named units

| Unit                             | Symbol       |                            Token | 1 unit in S·m²/mol |
|----------------------------------|--------------|---------------------------------:|-------------------:|
| Siemens square metre per mole    | `S*m^2/mol`  |    `siemensSquareMetersPerMole` |                1.0 |
| Siemens square centimetre per mole | `S*cm^2/mol` | `siemensSquareCentimetersPerMole` |             1e-4 |

Electrochemistry tables usually quote S·cm²/mol; the SI form is normally written with a milli prefix
(`milli.siemensSquareMetersPerMole`). All tokens accept every SI prefix.

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance. The
native form is assembled from **unit templates** because the group carries a mass term: the raw mixed value
is the gram-based product, while a typed instance stores its value in the named unit.

| Form             | Expression                                                          |
|------------------|---------------------------------------------------------------------|
| typed operator   | `conductivity / concentration`                                      |
| native (`toX()`) | `(0.01 of s³ · A² / kilo.grams / moles).toMolarConductivity()`      |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.conductivity.siemensPerMeter
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.concentration.molesPerLiter
import org.pcsoft.framework.kunit.electric.molarconductivity.*

val typed = (1.0 of siemensPerMeter) / (0.1 of molesPerLiter)
val native = (
    0.01 of (seconds pow 3) * (amperes.toUnit() pow 2) / kilo.grams.toUnit() / moles.toUnit()
).toMolarConductivity()

typed == native                          // true
typed into siemensSquareMetersPerMole    // 0.01
```

## Computing with the group

| Expression                          | Result type                      | Meaning       |
|-------------------------------------|----------------------------------|---------------|
| `conductivity / concentration`      | `KMolarConductivityUnitInstance` | `Λ = κ / c`   |
| `molarConductivity * concentration` | `KConductivityUnitInstance`      | `κ = Λ · c`   |
| `conductivity / molarConductivity`  | `KConcentrationUnitInstance`     | `c = κ / Λ`   |
| `molarConductivity + …`             | `KMolarConductivityUnitInstance` | Kohlrausch's law |

Kohlrausch's law of independent ion migration states that at infinite dilution the molar conductivity is
the **sum** of the ionic contributions — which is exactly the group's same-type `+`.

## Real-world example — Kohlrausch's law for KCl

The limiting ionic conductivities are 7.35 mS·m²/mol for K⁺ and 7.63 mS·m²/mol for Cl⁻. Their sum is the
limiting molar conductivity of potassium chloride, and multiplying by a concentration gives back the
conductivity a meter would read:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.conductivity.siemensPerMeter
import org.pcsoft.framework.kunit.thermo.concentration.molesPerLiter
import org.pcsoft.framework.kunit.electric.molarconductivity.*

val potassium = 7.350 of milli.siemensSquareMetersPerMole
val chloride  = 7.635 of milli.siemensSquareMetersPerMole

val kcl = potassium + chloride                       // Kohlrausch
kcl into milli.siemensSquareMetersPerMole            // 14.985
kcl into siemensSquareCentimetersPerMole             // ≈ 149.85 (the table value)

val kappa = kcl * (0.01 of molesPerLiter)            // KConductivityUnitInstance
kappa into siemensPerMeter                            // ≈ 0.1499 S/m
```

## Value semantics

`equals`/`hashCode` compare the **normalized S·m²/mol value**, so
`(1 of siemensSquareMetersPerMole) == (10000 of siemensSquareCentimetersPerMole)`. `toString()` renders the
value in the base unit: `"0.0126 S*m^2/mol"`.

## See also

* [Conductivity](conductivity.md) — the numerator.
* [Amount-of-Substance Concentration](../thermodynamics/concentration.md) — the denominator.
* [Conductance](conductance.md) — the un-normalized quantity a meter measures.
* [Electrical Engineering overview](overview.md)
