# Electric Field Strength

Package: `org.pcsoft.framework.kunit.electric.electricfieldstrength`
Base unit: **volt per meter** (`KElectricFieldStrengthUnit.BASE == KElectricFieldStrengthUnit.VOLT_PER_METER`)

Type: **constructed unit**

Electric field strength is a **constructed** unit: the composition `mass · length · time⁻³ · current⁻¹`
(`kg·m·s⁻³·A⁻¹`). `KElectricFieldStrengthUnitInstance` wraps a `KMixedUnitInstance` of four terms —
`KMassUnit.BASE` (gram) at `+1`, `KDistanceUnit.BASE` (meter) at `+1`, `KTimeUnit.BASE` (second) at `-3` and
`KElectricCurrentUnit.BASE` (ampere) at `-1`. Because the mass component of the library is normalized to
**grams** (not kilograms), the canonical product is divided by 1000 to reach volts per meter; the stored value
is always normalized to volts per meter.

The field strength `E` is the voltage drop per unit of length and, equally, the force acting on a unit charge.
It relates to [Electric Flux Density](electricfluxdensity.md) through the
[Permittivity](permittivity.md) (`D = ε · E`) and drives charge carriers at a speed given by their
[electric mobility](electricmobility.md) (`v = μ · E`).

## Building an electric field strength

Build a field strength with a named token, or from a decomposition (see below). Named units survive as
value-1 tokens (used with `of`/`into`):

| Field strength | Symbol | Token | 1 unit in V/m |
|---|---|---:|---:|
| Volt per meter | `V/m` | `voltsPerMeter` | 1.0 |
| Volt per centimeter | `V/cm` | `voltsPerCentimeter` | 100.0 |
| Statvolt per centimeter (CGS-ESU) | `statV/cm` | `statvoltsPerCentimeter` | 29979.2458 |

Named units support the SI prefixes via `KPrefixBuilder` (`kilo.voltsPerMeter`, `mega.voltsPerMeter`,
`kilo.voltsPerCentimeter`, …).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.electric.fieldstrength.*

val e = 3 of mega.voltsPerMeter        // dielectric strength of air
e into mega.voltsPerMeter              // 3.0
e into voltsPerMeter                   // 3.0e6
(1 of voltsPerCentimeter) into voltsPerMeter // 100.0
```

## Multiple decompositions

Electric field strength can be reached through several **equivalent decompositions**, all producing the same
value-equal field strength:

| Expression | Result type | Meaning |
|---|---|---|
| `voltage / length` | `KElectricFieldStrengthUnitInstance` | `E = U / l`, the voltage drop per unit of length |
| `force / charge` | `KElectricFieldStrengthUnitInstance` | `E = F / Q`, the force acting on a unit charge |
| `mass·length/(time³·current)` | via `.toElectricFieldStrength()` | native canonical `kg·m·s⁻³·A⁻¹` expression |

The typed operator forms return a field strength directly. The fully native expression stays a generic
`KMixedUnitInstance` and is narrowed with `toElectricFieldStrength()` (which recognises only the canonical
normal form and throws `IllegalStateException` otherwise). All routes are value-equal.

The inverse operators tie voltage, length, force, charge and field strength together:

| Expression | Result type | Meaning |
|---|---|---|
| `electricFieldStrength * length` | `KVoltageUnitInstance` | `U = E · l` (commutative) |
| `voltage / electricFieldStrength` | `KLengthUnitInstance` | `l = U / E` |
| `electricFieldStrength * charge` | `KForceUnitInstance` | `F = E · Q` (commutative) |
| `force / electricFieldStrength` | `KChargeUnitInstance` | `Q = F / E` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.electric.fieldstrength.*

// Real-world example - mains voltage across a 2 mm air gap gives 115 kV/m.
val e = (230 of volts) / (2 of milli.meters)   // KElectricFieldStrengthUnitInstance, 115000 V/m

// The same field strength from the force decomposition:
val fromForce = (6 of newtons) / (3 of coulombs)  // 2 V/m

// The same field strength as the native kg·m·s⁻³·A⁻¹ expression:
val raw = 2 of (kilo.grams * (meters pow 1)) / ((seconds pow 3) * (amperes pow 1))
raw.toElectricFieldStrength() == (2 of voltsPerMeter)  // true
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.fieldstrength.*

val s = (1 of voltsPerMeter) + (1 of voltsPerCentimeter)  // 101 V/m
(1 of voltsPerCentimeter) > (1 of voltsPerMeter)          // true
(2 of voltsPerMeter) * (3 of voltsPerMeter)               // KMixedUnitInstance (escapes the group)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.fieldstrength.*

(1 of voltsPerCentimeter).toString()   // "100.0 V/m" (base unit)
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `V/m` | `voltsPerMeter` | electric field strength, base unit (named token, volt per meter) |
| `U / l` | `(230 of volts) / (2 of milli.meters)` | field strength from voltage over a distance |
| `F / Q` | `(6 of newtons) / (3 of coulombs)` | field strength as force per unit charge |
| `kg·m/(s³·A)` | `(kilo.grams * (meters pow 1)) / ((seconds pow 3) * (amperes pow 1))` | field strength as mass·length / (time³·current) (fraction form) |
| `kg·m·s⁻³·A⁻¹` | `kilo.grams * (meters pow 1) * (seconds pow -3) * (amperes pow -1)` | same field strength as a pure product |
| `kV/m` | `kilo.voltsPerMeter` | prefixed field strength (kilovolt per meter) |
