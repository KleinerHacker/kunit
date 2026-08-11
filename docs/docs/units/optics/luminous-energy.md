# Luminous Energy

Package: `org.pcsoft.framework.kunit.optic.luminousenergy`
Base unit: **lumen second** (`KLuminousEnergyUnit.BASE == KLuminousEnergyUnit.LUMEN_SECOND`)

Type: **constructed unit**

Luminous energy `Q` is the luminous flux **accumulated over time**: `Q = Φ · t`. Where the flux says how bright a lamp
is *right now*, the luminous energy says how much light it has delivered in total — the quantity behind lamp lifetime
ratings and photographic flash energies. The lumen second is also called the **talbot**.

Its canonical base-dimension normal form is `luminousIntensity¹ · solidAngle¹ · time¹`.

## Units

| Unit         | Enum value                          | Symbol |          Token | 1 unit in lm·s |
|--------------|-------------------------------------|--------|---------------:|---------------:|
| Lumen second | `KLuminousEnergyUnit.LUMEN_SECOND`  | `lm*s` | `lumenSeconds` |            1.0 |
| Talbot       | `KLuminousEnergyUnit.LUMEN_SECOND`  | `lm*s` |      `talbots` |            1.0 |
| Lumen hour   | `KLuminousEnergyUnit.LUMEN_HOUR`    | `lm*h` |    `lumenHours` |           3600 |

`talbots` is a second spelling of the base unit, not a unit of its own. All tokens accept every SI prefix
(`kilo.lumenHours`, `milli.lumenSeconds`, …).

## Decomposition

The group has one decomposition, and both of its forms produce the same typed, value-equal instance:

| Form             | Expression                                                                  |
|------------------|-----------------------------------------------------------------------------|
| typed operator   | `luminousFlux * time`                                                       |
| native (`toX()`) | `((800 of lumens).toUnit() * (5 of seconds).toUnit()).toLuminousEnergy()`   |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.luminousenergy.*

val typed = (800 of lumens) * (5 of seconds)
val native = ((800 of lumens).toUnit() * (5 of seconds).toUnit()).toLuminousEnergy()

typed == native            // true
typed into lumenSeconds    // 4000.0
```

## Computing with the group

| Expression                       | Result type                   | Meaning                       |
|----------------------------------|-------------------------------|-------------------------------|
| `luminousFlux * time`            | `KLuminousEnergyUnitInstance` | `Q = Φ · t`                   |
| `luminousEnergy / time`          | `KLuminousFluxUnitInstance`   | the average flux              |
| `luminousEnergy / luminousFlux`  | `KTimeUnitInstance`           | how long the flux was emitted |

## Real-world example — light output over a lamp's life

An 800 lm LED bulb is rated for **25 000 h**. The total light it will ever deliver is:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.optic.luminousflux.lumens
import org.pcsoft.framework.kunit.optic.luminousenergy.*

val q = (800 of lumens) * (25_000 of hours)
q into lumenHours          // 20_000_000.0
q into mega.lumenHours     // 20.0

// Running it 3 h a day, how many days does that cover?
val perDay = (800 of lumens) * (3 of hours)
q into lumenHours / (perDay into lumenHours)   // ≈ 8333 days
```

## Value semantics

`equals`/`hashCode` compare the **normalized lm·s value**, so `(1 of lumenHours) == (3600 of lumenSeconds)`.
`toString()` renders the value in the base unit: `"3600.0 lm*s"`.

## See also

* [Luminous Flux](luminous-flux.md) — the rate this quantity accumulates.
* [Luminous Exposure](luminous-exposure.md) — the same idea for illuminance instead of flux.
* [Optics overview](overview.md)
