# Periodic Table

Package: `org.pcsoft.framework.kunit`
Types: `KChemicalElement`, `KChemicalElementCategory`

`KChemicalElement` is the central place for the chemical elements. It is a plain Kotlin enum, so every element is a
compile-time constant — and every physical constant it carries is a **typed unit instance**
of this library, ready to compose with everything else.

## Scope

The enum covers the classic school periodic table: the main and sub groups of **periods 1-6 without the f-block**. The
lanthanides (57-71) are therefore missing — atomic numbers jump from barium (56) to hafnium (72) — and neither actinides
nor transactinides are included. That makes 71 entries.

## Positional data

| Property        | Type                       | Meaning                                                             |
|-----------------|----------------------------|---------------------------------------------------------------------|
| `ordinalNumber` | `Int`                      | atomic number Z, the index in the periodic table                    |
| `symbol`        | `String`                   | element symbol, e.g. `"Pb"`                                         |
| `fullName`      | `String`                   | English name, e.g. `"Lead"` (the enum entry is `LEAD`)              |
| `period`        | `Int`                      | period (row), 1-6                                                   |
| `mainGroup`     | `Int?`                     | main group 1-8 for s/p-block elements, `null` for transition metals |
| `subGroup`      | `Int?`                     | sub group 1-8 for d-block elements, `null` otherwise                |
| `category`      | `KChemicalElementCategory` | chemical family                                                     |

Exactly one of `mainGroup` and `subGroup` is set. The sub groups use the classic numbering (Cu = 1, Zn = 2, Sc = 3 …
Fe/Co/Ni = 8).

`KChemicalElementCategory` has the entries `HYDROGEN`, `ALKALI_METAL`, `ALKALINE_EARTH_METAL`,
`TRANSITION_METAL`, `POST_TRANSITION_METAL`, `METALLOID`, `NONMETAL`, `HALOGEN` and `NOBLE_GAS`.

## Unit data

| Property                | Type                                 | Availability flag          |
|-------------------------|--------------------------------------|----------------------------|
| `molarMass`             | `KMolarMassUnitInstance`             | always present             |
| `molarVolume`           | `KMolarVolumeUnitInstance?`          | `hasMolarVolume`           |
| `atomicRadius`          | `KLengthUnitInstance?`               | `hasAtomicRadius`          |
| `covalentRadius`        | `KLengthUnitInstance?`               | `hasCovalentRadius`        |
| `density`               | `KDensityUnitInstance?`              | `hasDensity`               |
| `meltingPoint`          | `KTemperatureUnitInstance?`          | `hasMeltingPoint`          |
| `boilingPoint`          | `KTemperatureUnitInstance?`          | `hasBoilingPoint`          |
| `specificHeatCapacity`  | `KSpecificHeatCapacityUnitInstance?` | `hasSpecificHeatCapacity`  |
| `thermalConductivity`   | `KThermalConductivityUnitInstance?`  | `hasThermalConductivity`   |
| `ionizationEnergy`      | `KEnergyUnitInstance?`               | `hasIonizationEnergy`      |
| `electricalResistivity` | `KResistivityUnitInstance?`          | `hasElectricalResistivity` |
| `electronegativity`     | `Double?` (Pauling, dimensionless)   | `hasElectronegativity`     |

Constants that are not meaningfully defined for an element are `null` — helium has no melting point at normal pressure,
arsenic sublimes instead of boiling, astatine is too rare to have a measured density. The matching `has...` property
answers the same question without null handling.

`molarVolume` is derived from `molarMass / density`, i.e. it uses the second decomposition of the
[molar volume](units/thermodynamics/molar-volume.md) group.

## Real-world example: how heavy is a gold bar?

A standard gold bar measures 7 cm × 4 cm × 2 cm. What does it weigh, and how many moles of gold is that?

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.centi
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.mechanic.density.times
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole

val gold = KChemicalElement.GOLD

val volume = (7 of centi.meters) * (4 of centi.meters) * (2 of centi.meters) // 56 cm³
val mass = gold.density!! * volume                                          // KMassUnitInstance
mass into kilo.grams                                                        // ≈ 1.081 kg

val amount = mass / gold.molarMass                                          // KAmountOfSubstanceUnitInstance
amount into moles                                                           // ≈ 5.49 mol

gold.molarMass into gramsPerMole                                            // 196.966569
```

## Real-world example: heating a copper pan

How much energy does it take to heat a 1.2 kg copper pan from 20 °C to 200 °C?

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificheatcapacity.joulesPerKilogramKelvin

val copper = KChemicalElement.COPPER
val c = copper.specificHeatCapacity!! into joulesPerKilogramKelvin // 385.0
val mass = 1.2 of kilo.grams

val energy = (mass into kilo.grams) * c * 180.0 // ΔT = 180 K
energy                                          // ≈ 83 160 J
```

## Lookups

```kotlin
import org.pcsoft.framework.kunit.KChemicalElement
import org.pcsoft.framework.kunit.KChemicalElementCategory

KChemicalElement.ofSymbol("Fe")        // IRON (case-insensitive)
KChemicalElement.ofFullName("iron")    // IRON (case-insensitive)
KChemicalElement.ofOrdinalNumber(26)   // IRON
KChemicalElement.ofOrdinalNumber(57)   // null - lanthanides are not part of this table
KChemicalElement.ofMainGroup(4, 6)     // LEAD (main group 4, period 6)
KChemicalElement.ofSubGroup(8, 4)      // IRON (sub group 8, period 4 - first of Fe/Co/Ni)
KChemicalElement.ofPeriod(1)           // [HYDROGEN, HELIUM]
KChemicalElement.ofCategory(KChemicalElementCategory.NOBLE_GAS)
// [HELIUM, NEON, ARGON, KRYPTON, XENON, RADON]
```

Sub group 8 holds three elements per period; `ofSubGroup` returns the first one (Fe, Ru, Os) — use
`ofPeriod` and filter to get them all.

## Notation

| Mathematics   | Kotlin                                         | Meaning                                    |
|---------------|------------------------------------------------|--------------------------------------------|
| `Z`           | `element.ordinalNumber`                        | atomic number                              |
| `M`           | `element.molarMass`                            | molar mass, `g/mol`                        |
| `V_m = M / ρ` | `element.molarVolume`                          | molar volume, `m³/mol`                     |
| `ρ`           | `element.density`                              | density                                    |
| `T_m`, `T_b`  | `element.meltingPoint`, `element.boilingPoint` | melting / boiling point in K               |
| `m = ρ · V`   | `gold.density!! * volume`                      | mass from density × volume                 |
| `n = m / M`   | `mass / gold.molarMass`                        | amount of substance from mass ÷ molar mass |
