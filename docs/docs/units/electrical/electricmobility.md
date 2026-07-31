# Electric Mobility

Package: `org.pcsoft.framework.kunit.electric.electricmobility`
Base unit: **square meter per volt second**
(`KElectricMobilityUnit.BASE == KElectricMobilityUnit.SQUARE_METER_PER_VOLT_SECOND`)

Type: **constructed unit**

Electric mobility is a **constructed** unit: the composition `mass⁻¹ · time² · current`
(`kg⁻¹·s²·A` = `m²/(V·s)`). `KElectricMobilityUnitInstance` wraps a `KMixedUnitInstance` of three terms —
`KMassUnit.BASE` (gram) at `-1`, `KTimeUnit.BASE` (second) at `+2` and `KElectricCurrentUnit.BASE` (ampere)
at `+1`. The distance dimension cancels out because the volt already carries `m²`, so the canonical form has only three
terms. Because the mass component of the library is normalized to **grams** (not kilograms) and the mass exponent is
negative, the canonical product is multiplied by 1000 to reach the base unit; the stored value is always normalized to
square meters per volt second.

The electric mobility `μ` describes how fast a charge carrier drifts in an electric field: `v = μ · E`, where
`E` is the [Electric Field Strength](electricfieldstrength.md).

## Building an electric mobility

Build a mobility with a named token, or from a decomposition (see below). Named units survive as value-1 tokens (used
with `of`/`into`):

| Mobility                          | Symbol      |                            Token | 1 unit in m²/(V·s) |
|-----------------------------------|-------------|---------------------------------:|-------------------:|
| Square meter per volt second      | `m²/(V·s)`  |      `squareMetersPerVoltSecond` |                1.0 |
| Square centimeter per volt second | `cm²/(V·s)` | `squareCentimetersPerVoltSecond` |             1.0e-4 |

The centimeter form is the notation used throughout semiconductor physics. Named units support the SI prefixes via
`KPrefixBuilder` (`milli.squareMetersPerVoltSecond`, `kilo.squareCentimetersPerVoltSecond`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.mobility.*

val mu = 1400 of squareCentimetersPerVoltSecond   // electron mobility in silicon
mu into squareCentimetersPerVoltSecond            // 1400.0
mu into squareMetersPerVoltSecond                 // 0.14
```

## Multiple decompositions

Electric mobility can be reached through several **equivalent decompositions**, all producing the same value-equal
mobility:

| Expression                      | Result type                     | Meaning                                     |
|---------------------------------|---------------------------------|---------------------------------------------|
| `speed / electricFieldStrength` | `KElectricMobilityUnitInstance` | `μ = v / E`, the drift speed per unit field |
| `(time²·current)/mass`          | via `.toElectricMobility()`     | native canonical `kg⁻¹·s²·A` expression     |

The typed operator form returns a mobility directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toElectricMobility()` (which recognises only the canonical normal form and
throws `IllegalStateException` otherwise). Both routes are value-equal.

The inverse operators tie drift speed, field strength and mobility together:

| Expression                                 | Result type                          | Meaning                   |
|--------------------------------------------|--------------------------------------|---------------------------|
| `electricMobility * electricFieldStrength` | `KSpeedUnitInstance`                 | `v = μ · E` (commutative) |
| `speed / electricMobility`                 | `KElectricFieldStrengthUnitInstance` | `E = v / μ`               |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.electric.mobility.*

// Real-world example - silicon electrons at 1400 cm²/(V·s) drift 140 m/s in a 1 kV/m field.
val v = (1400 of squareCentimetersPerVoltSecond) * (1000 of voltsPerMeter)  // KSpeedUnitInstance, 140 m/s

// The definition solved for the mobility:
val mu = ((6 of meters) / (1 of seconds)) / (3 of voltsPerMeter)   // 2 m²/(V·s)

// The same mobility as the native kg⁻¹·s²·A expression:
val raw = 2 of ((seconds pow 2) * (amperes pow 1)) / (kilo.grams pow 1)
raw.toElectricMobility() == (2 of squareMetersPerVoltSecond)       // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.mobility.*

val s = (1 of squareMetersPerVoltSecond) + (1 of squareCentimetersPerVoltSecond)  // 1.0001 m²/(V·s)
(1 of squareMetersPerVoltSecond) > (1 of squareCentimetersPerVoltSecond)          // true
(2 of squareMetersPerVoltSecond) * (3 of squareMetersPerVoltSecond)               // KMixedUnitInstance
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.mobility.*

(1400 of squareCentimetersPerVoltSecond).toString()   // "0.14 m²/(V·s)" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written
both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin                                                               | Meaning                                            |
|-------------|----------------------------------------------------------------------|----------------------------------------------------|
| `m²/(V·s)`  | `squareMetersPerVoltSecond`                                          | electric mobility, base unit (named token)         |
| `cm²/(V·s)` | `squareCentimetersPerVoltSecond`                                     | the semiconductor-physics notation, 1e-4 m²/(V·s)  |
| `v / E`     | `((6 of meters) / (1 of seconds)) / (3 of voltsPerMeter)`            | mobility from drift speed over field strength      |
| `μ · E`     | `(1400 of squareCentimetersPerVoltSecond) * (1000 of voltsPerMeter)` | the drift speed in a given field                   |
| `(s²·A)/kg` | `((seconds pow 2) * (amperes pow 1)) / (kilo.grams pow 1)`           | mobility as (time²·current) / mass (fraction form) |
| `kg⁻¹·s²·A` | `(kilo.grams pow -1) * (seconds pow 2) * (amperes pow 1)`            | same mobility as a pure product                    |
