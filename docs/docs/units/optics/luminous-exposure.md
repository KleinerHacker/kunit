# Luminous Exposure

Package: `org.pcsoft.framework.kunit.optic.luminousexposure`
Base unit: **lux second** (`KLuminousExposureUnit.BASE == KLuminousExposureUnit.LUX_SECOND`)

Type: **constructed unit**

Luminous exposure `H` is the illuminance **accumulated over time**: `H = E · t`. It is the *light dose* a surface has
received — the quantity museum conservators budget in lux hours per year to limit the fading of pigments, and the one
behind a camera's exposure value.

Its canonical base-dimension normal form is `luminousIntensity¹ · solidAngle¹ · distance⁻² · time¹`.

## Units

| Unit       | Enum value                          | Symbol |        Token | 1 unit in lx·s |
|------------|-------------------------------------|--------|-------------:|---------------:|
| Lux second | `KLuminousExposureUnit.LUX_SECOND`  | `lx*s` | `luxSeconds` |            1.0 |
| Lux hour   | `KLuminousExposureUnit.LUX_HOUR`    | `lx*h` |   `luxHours` |           3600 |

All tokens accept every SI prefix (`kilo.luxHours` is the usual unit for an annual light-dose budget).

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance:

| Form             | Expression                                                                   |
|------------------|------------------------------------------------------------------------------|
| typed operator   | `illuminance * time`                                                         |
| native (`toX()`) | `((50 of lux).toUnit() * (10 of seconds).toUnit()).toLuminousExposure()`     |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousexposure.*

val typed = (50 of lux) * (10 of seconds)
val native = ((50 of lux).toUnit() * (10 of seconds).toUnit()).toLuminousExposure()

typed == native          // true
typed into luxSeconds    // 500.0
```

## Computing with the group

| Expression                        | Result type                     | Meaning                    |
|-----------------------------------|---------------------------------|----------------------------|
| `illuminance * time`              | `KLuminousExposureUnitInstance` | `H = E · t`                |
| `luminousExposure / time`         | `KIlluminanceUnitInstance`      | the average illuminance    |
| `luminousExposure / illuminance`  | `KTimeUnitInstance`             | the exposure time          |

## Real-world example — a museum light budget

Sensitive watercolours are limited to about **50 000 lx·h per year**. At a display illuminance of 50 lx and 8 opening
hours a day, how many days may the piece be shown?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.optic.illuminance.lux
import org.pcsoft.framework.kunit.optic.luminousexposure.*

val perDay = (50 of lux) * (8 of hours)     // KLuminousExposureUnitInstance
perDay into luxHours                         // 400.0

val budget = 50_000 of luxHours
(budget into luxHours) / (perDay into luxHours)   // 125 opening days per year

// The other way round: how long may it stay at 200 lx?
val t = budget / (200 of lux)                // KTimeUnitInstance
t into hours                                  // 250.0 h
```

## Value semantics

`equals`/`hashCode` compare the **normalized lx·s value**, so `(1 of luxHours) == (3600 of luxSeconds)`.
`toString()` renders the value in the base unit: `"3600.0 lx*s"`.

## See also

* [Illuminance](illuminance.md) — the rate this quantity accumulates.
* [Luminous Energy](luminous-energy.md) — the same idea for flux instead of illuminance.
* [Optics overview](overview.md)
