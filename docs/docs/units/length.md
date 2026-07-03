# Length

Package: `org.pcsoft.framework.kunit.length`
Base unit: **meter** (`KLengthUnit.BASE == KLengthUnit.METER`)

`KLengthUnitInstance` wraps a `KMixedUnitInstance` restricted to a single term of `KLengthUnit.BASE`, at any
exponent - exponent 1 for a plain length, 2 for an area, 3 for a volume. The value is always stored
normalized to meters (or square/cubic meters), regardless of which unit it was created from.

## Exponent 1 - Length

| Unit | Enum value | Symbol | Creator | 1 unit in meters |
|---|---|---|---:|---:|
| Meter | `KLengthUnit.METER` | `m` | `Number.meters()` | 1.0 |
| Mile | `KLengthUnit.MILE` | `mi` | `Number.miles()` | 1609.344 |
| Nautical mile | `KLengthUnit.NAUTICAL_MILE` | `nmi` | `Number.nauticalMiles()` | 1852.0 |
| Yard | `KLengthUnit.YARD` | `yd` | `Number.yards()` | 0.9144 |
| Foot | `KLengthUnit.FOOT` | `ft` | `Number.feet()` | 0.3048 |
| Inch | `KLengthUnit.INCH` | `in` | `Number.inches()` | 0.0254 |
| Fathom | `KLengthUnit.FATHOM` | `ftm` | `Number.fathoms()` | 1.8288 |
| Chain | `KLengthUnit.CHAIN` | `ch` | `Number.chains()` | 20.1168 |
| Furlong | `KLengthUnit.FURLONG` | `fur` | `Number.furlongs()` | 201.168 |
| Astronomical unit | `KLengthUnit.ASTRONOMICAL_UNIT` | `AU` | `Number.astronomicalUnits()` | 1.495978707e11 |
| Light-second | `KLengthUnit.LIGHT_SECOND` | `ls` | `Number.lightSeconds()` | 299792458.0 |
| Light-minute | `KLengthUnit.LIGHT_MINUTE` | `lmin` | `Number.lightMinutes()` | 1.798754748e10 |
| Light-hour | `KLengthUnit.LIGHT_HOUR` | `lh` | `Number.lightHours()` | 1.0792528488e12 |
| Light-day | `KLengthUnit.LIGHT_DAY` | `ld` | `Number.lightDays()` | 2.59020683712e13 |
| Light-week | `KLengthUnit.LIGHT_WEEK` | `lw` | `Number.lightWeeks()` | 1.813144785984e14 |
| Light-year | `KLengthUnit.LIGHT_YEAR` | `ly` | `Number.lightYears()` | 9.4607304725808e15 |
| Parsec | `KLengthUnit.PARSEC` | `pc` | `Number.parsecs()` | 3.0856775814913673e16 |

Every unit above has a matching bare `val` alias for use as a `valueAs`/`toString` target or as the `unit`
argument of a prefix `infix` function, e.g. `meters`, `miles`, `nauticalMiles`, `yards`, `feet`, `inches`,
`fathoms`, `chains`, `furlongs`, `astronomicalUnits`, `lightSeconds`, `lightMinutes`, `lightHours`,
`lightDays`, `lightWeeks`, `lightYears`, `parsecs`.

```kotlin
import org.pcsoft.framework.kunit.length.*

val d = 5.miles()
d.value                        // 8046.72 (normalized to meters)
d.valueAs(KLengthUnit.MILE)    // 5.0 (read back in miles)
d.valueAs(feet)                 // 26400.0
d.valueAs(nauticalMiles)        // ≈ 4.3452 (read back as nautical miles)
```

### Operators

```kotlin
import org.pcsoft.framework.kunit.length.*

// + / - : same group, automatic conversion between different length units
val a = 1.miles() + 500.meters()   // KLengthUnitInstance, normalized to meters
val b = 2.miles() - 800.meters()

// comparisons
2.miles() > 1.miles()               // true
1.miles() == 1609.344.meters()      // true (same normalized value)
5.hectares() > 5.meters()           // throws IllegalStateException (area vs. length, different exponent)

// * / / : always allowed, produces a KMixedUnitInstance with a new exponent
val area = 200.meters() * 50.meters()   // KMixedUnitInstance: value=10000.0, units=[METER^2]
val lengthAgain = area / 50.meters().toKMixedUnitInstance() // KMixedUnitInstance: value=200.0, units=[METER^1]
```

### Comparisons and equality

`==`, `!=`, `<`, `<=`, `>`, `>=` compare the normalized `value` of two `KLengthUnitInstance`s of the **same
exponent**. Comparing across different exponents (e.g. a length to an area) throws `IllegalStateException`,
consistent with the `+`/`-` rules.

## Exponent 2 - Area

Any `KLengthUnitInstance` with exponent 2 represents an area, e.g. the result of multiplying two lengths.
In addition to the raw `KLengthUnit.BASE^2` (square meters) representation, the following named special
units (`KLengthDerivedUnit`) are available as conversion/formatting targets:

| Special unit | Enum value | Symbol | Creator | 1 unit in m² |
|---|---|---:|---:|---:|
| Are | `KLengthDerivedUnit.ARE` | `a` | `Number.ares()` | 100.0 |
| Hectare | `KLengthDerivedUnit.HECTARE` | `ha` | `Number.hectares()` | 10 000.0 |
| Acre | `KLengthDerivedUnit.ACRE` | `ac` | `Number.acres()` | 4046.8564224 |

```kotlin
import org.pcsoft.framework.kunit.length.*

val plot = 3.hectares()
plot.value                                   // 30000.0 (m²)
plot.valueAs(KLengthDerivedUnit.ARE)          // 300.0
plot.valueAs(KLengthDerivedUnit.ACRE)         // ≈ 7.4132

val computed = 200.meters() * 50.meters()     // KMixedUnitInstance, units=[METER^2]
computed.toKLengthUnit().valueAs(KLengthDerivedUnit.HECTARE) // 1.0

plot + computed.toKLengthUnit()                // allowed: both exponent 2 (area)
plot + 5.meters()                              // throws IllegalStateException (area vs. length)
```

## Exponent 3 - Volume

Any `KLengthUnitInstance` with exponent 3 represents a volume. In addition to the raw
`KLengthUnit.BASE^3` (cubic meters) representation, the following named special units are available:

| Special unit | Enum value | Symbol | Creator | 1 unit in m³ |
|---|---|---:|---:|---:|
| Liter | `KLengthDerivedUnit.LITER` | `L` | `Number.liters()` | 0.001 |
| US liquid gallon | `KLengthDerivedUnit.US_GALLON` | `gal (US)` | `Number.usGallons()` | 0.003785411784 |
| Imperial gallon | `KLengthDerivedUnit.IMPERIAL_GALLON` | `gal (UK)` | `Number.imperialGallons()` | 0.00454609 |
| US fluid ounce | `KLengthDerivedUnit.US_FLUID_OUNCE` | `fl oz` | `Number.usFluidOunces()` | 2.95735295625e-5 |
| Oil barrel | `KLengthDerivedUnit.OIL_BARREL` | `bbl` | `Number.oilBarrels()` | 0.158987294928 |

```kotlin
import org.pcsoft.framework.kunit.length.*

val tank = 200.liters()
tank.value                                        // 0.2 (m³)
tank.valueAs(KLengthDerivedUnit.US_GALLON)        // ≈ 52.834

val cube = 2.meters() * 2.meters() * 2.meters()   // KMixedUnitInstance, units=[METER^3]
cube.toKLengthUnit().valueAs(KLengthDerivedUnit.LITER) // 8000.0

tank + cube.toKLengthUnit()                        // allowed: both exponent 3 (volume)
```

## SI prefixes

Any `KLengthUnit` can be combined with any of the 24 SI prefixes (`KUnitPrefix`, root package, Quetta/Q to
Quecto/q) using the length-group `infix` construction functions (which return a `KLengthUnitInstance`
directly) and `with` (for `valueAs`/`toString` targets):

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.length.*

// Construction: "5 kilo meters" -> KLengthUnitInstance (direct, == 5000.meters())
val fiveKm = 5 kilo meters
fiveKm.value // 5000.0

// Reading back a value using a prefixed target
val d = 5.miles()
d.valueAs(KUnitPrefix.KILO with KLengthUnit.METER)  // 8.04672 (km)
d.toString(KUnitPrefix.KILO with KLengthUnit.METER) // "8.04672 km"

// Prefixes also combine with derived units (area/volume)
val tank = 200.liters()
tank.valueAs(KUnitPrefix.MILLI with KLengthDerivedUnit.LITER) // 200000.0 (mL)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.length.*

5.meters().toString()                        // "5.0 m" (base unit representation)
5.miles().toString(KLengthUnit.MILE)          // "5.0 mi"
(200.meters() * 50.meters()).toKLengthUnit().toString(KLengthDerivedUnit.HECTARE) // "1.0 ha"
```
