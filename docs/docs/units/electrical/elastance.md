# Elastance

Package: `org.pcsoft.framework.kunit.electric.elastance`
Base unit: **reciprocal farad** (`KElastanceUnit.BASE == KElastanceUnit.RECIPROCAL_FARAD`)

Type: **constructed unit**

The elastance `S = U / Q = 1 / C` is the exact reciprocal of the [capacitance](capacitance.md). It is the
convenient form whenever capacitors sit in **series**: series elastances simply add, just as series
resistances do. Its unit, the reciprocal farad, is classically called the **daraf** — "farad" spelled
backwards.

Its canonical base-dimension normal form is `mass · length² · time⁻⁴ · current⁻²`.

## Named units

| Unit              | Symbol  |              Token | 1 unit in F⁻¹ |
|-------------------|---------|-------------------:|--------------:|
| Reciprocal farad  | `1/F`   | `reciprocalFarads` |           1.0 |
| Daraf             | `daraf` |            `darafs` |           1.0 |

`darafs` is a second spelling of the base unit, not a unit of its own. All tokens accept every SI prefix
(`mega.reciprocalFarads`, …).

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance. The
native form is assembled from **unit templates** because the group carries a mass term.

| Form             | Expression                                                    |
|------------------|----------------------------------------------------------------|
| typed operator   | `voltage / charge`                                            |
| native (`toX()`) | `(1 of kilo.grams · m² / s⁴ / A²).toElastance()`              |

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.elastance.*

val typed = (10 of volts) / (10 of milli.coulombs)
val native = (1000 of kilo.grams.toUnit() * (meters pow 2) / (seconds pow 4) / (amperes.toUnit() pow 2))
    .toElastance()

typed == native              // true
typed into reciprocalFarads  // 1000.0
```

## Computing with the group

| Expression             | Result type                     | Meaning                    |
|------------------------|---------------------------------|----------------------------|
| `voltage / charge`     | `KElastanceUnitInstance`        | `S = U / Q`                |
| `elastance * charge`   | `KVoltageUnitInstance`          | `U = S · Q`                |
| `voltage / elastance`  | `KChargeUnitInstance`           | the stored charge          |
| `1 / capacitance`      | `KElastanceUnitInstance`        | `S = 1 / C`                |
| `1 / elastance`        | `KCapacitanceUnitInstance`      | `C = 1 / S`                |
| `elastance + …`        | `KElastanceUnitInstance`        | capacitors in series       |

## Real-world example — two capacitors in series

Two 1 mF capacitors in series behave like a single 0.5 mF one. In elastance terms that is plain addition:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.capacitance.farads
import org.pcsoft.framework.kunit.electric.elastance.*

val total = (1 / (1 of milli.farads)) + (1 / (1 of milli.farads))
total into reciprocalFarads       // 2000.0

(1 / total) into milli.farads     // 0.5 — the equivalent capacitance
```

## Value semantics

`equals`/`hashCode` compare the **normalized F⁻¹ value**, so `(1 of reciprocalFarads) == (1 of darafs)`.
`toString()` renders the value in the base unit: `"1000.0 1/F"`.

## See also

* [Capacitance](capacitance.md) — the reciprocal quantity.
* [Voltage](voltage.md) and [Charge](charge.md) — the two operands of the decomposition.
* [Electrical Engineering overview](overview.md)
