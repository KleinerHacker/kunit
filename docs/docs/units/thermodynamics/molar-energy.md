# Molar Energy

Package: `org.pcsoft.framework.kunit.thermo.molarenergy`
Base unit: **joule per mole** (`KMolarEnergyUnit.BASE == KMolarEnergyUnit.JOULE_PER_MOLE`)

Type: **constructed unit**

Molar energy is energy per amount of substance: `energy / amountOfSubstance` (`J/mol`). Depending on
context the same quantity is called *molar enthalpy*, *reaction enthalpy* or *bond energy*.

`KMolarEnergyUnitInstance` wraps a `KMixedUnitInstance` of exactly four terms in the canonical normal form
`mass¹ · distance² · time⁻² · substance⁻¹` (`kg·m²·s⁻²·mol⁻¹`), always normalized to J/mol.

Per unit of temperature this becomes [molar heat capacity](molar-heat-capacity.md); per kilogram instead
of per mole it becomes [specific energy](specific-energy.md).

## Named units

| Unit | Symbol | Token | 1 unit in J/mol |
|---|---|---:|---:|
| Joule per mole | `J/mol` | `joulesPerMole` | 1.0 |
| Calorie per mole | `cal/mol` | `caloriesPerMole` | 4.184 |
| Electronvolt per entity | `eV/entity` | `electronVoltsPerEntity` | 96485.33212 |

The electronvolt-per-entity token converts a *per-particle* energy into a *per-mole* energy — its factor
is the Faraday constant. All units accept the full SI prefix range (`kilo.joulesPerMole`,
`kilo.caloriesPerMole`, `milli.electronVoltsPerEntity`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarenergy.*

val dH = 286 of kilo.joulesPerMole
dH into joulesPerMole            // 286_000.0
dH into kilo.caloriesPerMole     // ≈ 68.36
dH into electronVoltsPerEntity   // ≈ 2.964 eV per molecule
```

## Real-world example: burning hydrogen

The enthalpy of formation of liquid water is −286 kJ/mol. How much energy is released when 4 moles of
hydrogen burn, and what is that per molecule?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarenergy.*

val formation = -286 of kilo.joulesPerMole
val hydrogen = 4 of moles

val released = formation * hydrogen   // KEnergyUnitInstance
released into kilo.joules             // -1144.0 kJ
released into mega.joules             // -1.144 MJ

// per molecule, in the chemists' unit
formation into electronVoltsPerEntity // ≈ -2.964 eV

// Inverse: how much substance does 1 MJ correspond to?
val n = (1 of mega.joules) / formation // KAmountOfSubstanceUnitInstance
n into moles                           // ≈ -3.497 mol
```

## Computing with the core units (energy & amount of substance)

| Expression | Result type | Meaning |
|---|---|---|
| `energy / amountOfSubstance` | `KMolarEnergyUnitInstance` | molar energy |
| `molarEnergy * amountOfSubstance` | `KEnergyUnitInstance` | total energy |
| `amountOfSubstance * molarEnergy` | `KEnergyUnitInstance` | total energy (commutative) |
| `energy / molarEnergy` | `KAmountOfSubstanceUnitInstance` | amount of substance involved |

## Decompositions

Both decompositions produce the same typed, value-equal instance.

| Decomposition | Form | Result |
|---|---|---|
| `energy / amountOfSubstance` | typed operator | `KMolarEnergyUnitInstance` directly |
| `mass · distance² · time⁻² · substance⁻¹` | native expression + `toMolarEnergy()` | `KMolarEnergyUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarenergy.*

// typed operator form
val typed = (1 of joules) / (1 of moles)

// native base-dimension form (kg·m²·s⁻²·mol⁻¹), recognised by toMolarEnergy()
val native = (
    (1000 of grams).toUnit() *
        ((1 of meters).toUnit() pow 2) /
        ((1 of seconds).toUnit() pow 2) /
        (1 of moles).toUnit()
    ).toMolarEnergy()

typed == native // true - both are 1.0 J/mol
```

`toMolarEnergy()` recognises **only** the canonical normal form; a wrong shape throws
`IllegalStateException`.

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarenergy.*

val total = (1 of kilo.joulesPerMole) + (500 of joulesPerMole)  // 1500 J/mol
val rest  = (1 of kilo.joulesPerMole) - (250 of joulesPerMole)  // 750 J/mol

(1 of kilo.joulesPerMole) > (500 of joulesPerMole)   // true
(1 of kilo.joulesPerMole) == (1000 of joulesPerMole) // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.molarenergy.*

(286 of kilo.joulesPerMole).toString()                        // "286000.0 J/mol"
"${(286 of kilo.joulesPerMole) into caloriesPerMole} cal/mol" // "68355.6... cal/mol"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `J/mol` | `joulesPerMole` | molar energy, base unit — named token |
| `kg·m²·s⁻²·mol⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / moles` | same quantity in base dimensions |
| `kJ/mol` | `kilo.joulesPerMole` | kilojoule per mole |
| `eV` (per particle) | `electronVoltsPerEntity` | electronvolt per elementary entity |
| `ΔH_m = Q / n` | `(572 of kilo.joules) / (2 of moles)` | molar energy from energy ÷ amount |
| `Q = ΔH_m · n` | `formation * hydrogen` | energy from molar energy × amount |
| `n = Q / ΔH_m` | `(1 of mega.joules) / formation` | amount from energy ÷ molar energy |
