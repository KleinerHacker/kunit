# Molar Volume

Package: `org.pcsoft.framework.kunit.thermo.molarvolume`
Base unit: **cubic meter per mole** (`KMolarVolumeUnit.BASE == KMolarVolumeUnit.CUBIC_METERS_PER_MOLE`)

Type: **constructed unit**

Molar volume is volume per amount of substance: `volume / amountOfSubstance` (`m³/mol`). For an ideal gas it is the same
for every substance (22.711 l/mol at 0 °C and 100 kPa); for solids and liquids it follows from
the [molar mass](molar-mass.md) and the density.

`KMolarVolumeUnitInstance` wraps a `KMixedUnitInstance` of exactly two terms in the canonical normal form
`distance³ · substance⁻¹` (`m³·mol⁻¹`), always normalized to m³/mol. Both components are stored in their group's base
unit, so the raw component base *is* the named base unit.

Every element of the [periodic table](../../periodic-table.md) derives its molar volume from its molar mass and density
through the second decomposition below.

## Named units

| Unit                      | Symbol     |                     Token | 1 unit in m³/mol |
|---------------------------|------------|--------------------------:|-----------------:|
| Cubic meter per mole      | `m^3/mol`  |      `cubicMetersPerMole` |              1.0 |
| Liter per mole            | `l/mol`    |           `litersPerMole` |            0.001 |
| Cubic centimeter per mole | `cm^3/mol` | `cubicCentimetersPerMole` |           1.0e-6 |

All units accept the full SI prefix range (`milli.cubicMetersPerMole`, `milli.litersPerMole`, …). The package
additionally exposes the constant `MOLAR_VOLUME_IDEAL_GAS_STP` = 0.02271095464 (m³/mol), the molar volume of an ideal
gas at standard conditions.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val ideal = MOLAR_VOLUME_IDEAL_GAS_STP of cubicMetersPerMole
ideal into litersPerMole          // ≈ 22.711
ideal into cubicCentimetersPerMole // ≈ 22711.0
```

## Real-world example: a balloon full of helium

How much space do 2 moles of an ideal gas occupy at standard conditions — and how many moles fit into a 5 litre balloon?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val ideal = MOLAR_VOLUME_IDEAL_GAS_STP of cubicMetersPerMole

// Volume of 2 moles
val volume = ideal * (2 of moles) // KVolumeUnitInstance
volume into liters                // ≈ 45.42 l

// How many moles fit into a 5 l balloon?
val amount = (5 of liters) / ideal // KAmountOfSubstanceUnitInstance
amount into moles                  // ≈ 0.2202 mol

// And the molar volume measured from a filled balloon:
val measured = (45.42 of liters) / (2 of moles)
measured into litersPerMole        // ≈ 22.71
```

## Real-world example: the volume of a mole of water

Water has a molar mass of 18.015 g/mol and a density of 1 kg/l, so one mole occupies about 18 cm³ — a tablespoon.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val density = (1 of kilo.grams) / (1 of liters)      // KDensityUnitInstance
val molarVolume = (18.015 of gramsPerMole) / density // KMolarVolumeUnitInstance
molarVolume into cubicCentimetersPerMole             // 18.015
```

## Computing with the core units

| Expression                        | Result type                      | Meaning                             |
|-----------------------------------|----------------------------------|-------------------------------------|
| `volume / amountOfSubstance`      | `KMolarVolumeUnitInstance`       | molar volume                        |
| `molarMass / density`             | `KMolarVolumeUnitInstance`       | molar volume (second decomposition) |
| `molarVolume * amountOfSubstance` | `KVolumeUnitInstance`            | total volume                        |
| `amountOfSubstance * molarVolume` | `KVolumeUnitInstance`            | total volume (commutative)          |
| `volume / molarVolume`            | `KAmountOfSubstanceUnitInstance` | amount of substance contained       |
| `molarVolume * density`           | `KMolarMassUnitInstance`         | [molar mass](molar-mass.md)         |
| `density * molarVolume`           | `KMolarMassUnitInstance`         | molar mass (commutative)            |
| `molarMass / molarVolume`         | `KDensityUnitInstance`           | density                             |

## Decompositions

All decompositions produce the same typed, value-equal instance.

| Decomposition                | Form                                  | Result                              |
|------------------------------|---------------------------------------|-------------------------------------|
| `volume / amountOfSubstance` | typed operator                        | `KMolarVolumeUnitInstance` directly |
| `molarMass / density`        | typed operator                        | `KMolarVolumeUnitInstance` directly |
| `distance³ · substance⁻¹`    | native expression + `toMolarVolume()` | `KMolarVolumeUnitInstance`          |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molarvolume.*

// typed operator form: volume / amount
val typedVolume = (0.018015 of liters) / (1 of moles)

// typed operator form: molar mass / density
val typedMolarMass = (18.015 of gramsPerMole) / ((1 of kilo.grams) / (1 of liters))

// native base-dimension form (m³·mol⁻¹), recognised by toMolarVolume()
val native = (((18.015e-6 of (meters pow 3)).toUnit()) / (1 of moles).toUnit()).toMolarVolume()

typedVolume == typedMolarMass // true
typedVolume == native         // true - all are 1.8015e-5 m³/mol
```

`toMolarVolume()` recognises **only** the canonical normal form; a wrong shape throws
`IllegalStateException`.

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val total = (10 of litersPerMole) + (4 of litersPerMole) // 14 l/mol
val rest  = (10 of litersPerMole) - (4 of litersPerMole) // 6 l/mol

(1 of litersPerMole) > (500 of cubicCentimetersPerMole)   // true
(1 of litersPerMole) == (1000 of cubicCentimetersPerMole) // true
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarvolume.*

(1 of litersPerMole).toString()    // "0.001 m^3/mol"
(22.4 of litersPerMole).toString() // "0.0224 m^3/mol"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics   | Kotlin                               | Meaning                                |
|---------------|--------------------------------------|----------------------------------------|
| `m³/mol`      | `cubicMetersPerMole`                 | molar volume, base unit — named token  |
| `m³·mol⁻¹`    | `(meters pow 3) / moles`             | same quantity in base dimensions       |
| `l/mol`       | `litersPerMole`                      | liter per mole                         |
| `cm³/mol`     | `cubicCentimetersPerMole`            | cubic centimeter per mole              |
| `V_m = V / n` | `(45.42 of liters) / (2 of moles)`   | molar volume from volume ÷ amount      |
| `V_m = M / ρ` | `(18.015 of gramsPerMole) / density` | molar volume from molar mass ÷ density |
| `V = V_m · n` | `ideal * (2 of moles)`               | volume from molar volume × amount      |
| `n = V / V_m` | `(5 of liters) / ideal`              | amount from volume ÷ molar volume      |
| `ρ = M / V_m` | `molarMass / molarVolume`            | density from molar mass ÷ molar volume |
