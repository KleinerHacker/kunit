# Electric Flux

Package: `org.pcsoft.framework.kunit.electric.flux`
Base unit: **volt metre** (`KElectricFluxUnit.BASE == KElectricFluxUnit.VOLT_METER`)

Type: **constructed unit**

The electric flux `Φ_E` is the electric field strength integrated over an area: `Φ_E = E · A`. It is the
quantity Gauss's law is written in — the flux through a closed surface equals the enclosed charge divided
by the permittivity.

Its canonical base-dimension normal form is `mass · length³ · time⁻³ · current⁻¹`.

!!! note "Not the electric flux density"
    The [electric flux density](electricfluxdensity.md) `D` (`C/m²`) is a different quantity with a
    different dimension. This page is about the flux itself, in `V·m`.

## Named units

| Unit            | Symbol  |             Token | 1 unit in V·m |
|-----------------|---------|------------------:|--------------:|
| Volt metre      | `V*m`   |      `voltMeters` |           1.0 |
| Volt centimetre | `V*cm`  | `voltCentimeters` |          0.01 |

All tokens accept every SI prefix (`kilo.voltMeters`, …).

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance. The
native form is assembled from **unit templates** because the group carries a mass term: the raw mixed value
is the gram-based product, while a typed instance stores its value in the named unit.

| Form             | Expression                                                     |
|------------------|-----------------------------------------------------------------|
| typed operator   | `electricFieldStrength * area`                                 |
| native (`toX()`) | `(125 of kilo.grams · m³ / s³ / A).toElectricFlux()`           |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.flux.*

val plate = (0.5 of meters) * (0.25 of meters)     // 0.125 m²

val typed = (1000 of voltsPerMeter) * plate
val native = (125 of kilo.grams.toUnit() * (meters pow 3) / (seconds pow 3) / amperes.toUnit())
    .toElectricFlux()

typed == native          // true
typed into voltMeters    // 125.0
```

## Computing with the group

| Expression                         | Result type                            | Meaning        |
|------------------------------------|----------------------------------------|----------------|
| `electricFieldStrength * area`     | `KElectricFluxUnitInstance`            | `Φ_E = E · A`  |
| `electricFlux / area`              | `KElectricFieldStrengthUnitInstance`   | `E = Φ_E / A`  |
| `electricFlux / electricFieldStrength` | `KAreaUnitInstance`                | the area       |

## Real-world example — flux through a capacitor plate

A field of **1000 V/m** passes through a 0.5 m × 0.25 m plate:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electric.fieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.flux.*

val plate = (0.5 of meters) * (0.25 of meters)
val phi = (1000 of voltsPerMeter) * plate
phi into voltMeters                 // 125.0

// The field a given flux implies on that plate
((125 of voltMeters) / plate) into voltsPerMeter   // 1000.0
```

## Value semantics

`equals`/`hashCode` compare the **normalized V·m value**, so `(1 of voltMeters) == (100 of voltCentimeters)`.
`toString()` renders the value in the base unit: `"125.0 V*m"`.

## See also

* [Electric Field Strength](electricfieldstrength.md) — the field being integrated.
* [Electric Flux Density](electricfluxdensity.md) — the differently-dimensioned `D` field.
* [Electrical Engineering overview](overview.md)
