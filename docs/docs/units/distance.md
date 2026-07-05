# Distance

Package: `org.pcsoft.framework.kunit.distance`
Base unit: **meter** (`KDistanceUnit.BASE == KDistanceUnit.METER`)

The distance group models exponents as their own compile-time-safe types under an open base wrapper
`KDistanceUnitInstance` (a single term of `KDistanceUnit.BASE` at **any** exponent):

* **`KLengthUnitInstance`** - exponent 1 (a length)
* **`KAreaUnitInstance`** - exponent 2 (an area)
* **`KVolumeUnitInstance`** - exponent 3 (a volume)

The value is always stored normalized to meters (or square/cubic meters). Because length, area and volume
are distinct types, mixing them in `+`/`-`/comparison is a **compile error** (there is no such operator),
while `*`/`/` stay in the family where possible (`length * length = area`, `area / length = length`) and
fall back to `KDistanceUnitInstance`/`KMixedUnitInstance` for exponents outside `{1,2,3}` (or a
dimensionless exponent-0 result).

## Exponent 1 - Length

| Unit | Enum value | Symbol | Creator | 1 unit in meters |
|---|---|---|---:|---:|
| Meter | `KDistanceUnit.METER` | `m` | `Number.meters` | 1.0 |
| Mile | `KDistanceUnit.MILE` | `mi` | `Number.miles` | 1609.344 |
| Nautical mile | `KDistanceUnit.NAUTICAL_MILE` | `nmi` | `Number.nauticalMiles` | 1852.0 |
| Yard | `KDistanceUnit.YARD` | `yd` | `Number.yards` | 0.9144 |
| Foot | `KDistanceUnit.FOOT` | `ft` | `Number.feet` | 0.3048 |
| Inch | `KDistanceUnit.INCH` | `in` | `Number.inches` | 0.0254 |
| Fathom | `KDistanceUnit.FATHOM` | `ftm` | `Number.fathoms` | 1.8288 |
| Chain | `KDistanceUnit.CHAIN` | `ch` | `Number.chains` | 20.1168 |
| Furlong | `KDistanceUnit.FURLONG` | `fur` | `Number.furlongs` | 201.168 |
| Astronomical unit | `KDistanceUnit.ASTRONOMICAL_UNIT` | `AU` | `Number.astronomicalUnits` | 1.495978707e11 |
| Light-second | `KDistanceUnit.LIGHT_SECOND` | `ls` | `Number.lightSeconds` | 299792458.0 |
| Light-minute | `KDistanceUnit.LIGHT_MINUTE` | `lmin` | `Number.lightMinutes` | 1.798754748e10 |
| Light-hour | `KDistanceUnit.LIGHT_HOUR` | `lh` | `Number.lightHours` | 1.0792528488e12 |
| Light-day | `KDistanceUnit.LIGHT_DAY` | `ld` | `Number.lightDays` | 2.59020683712e13 |
| Light-week | `KDistanceUnit.LIGHT_WEEK` | `lw` | `Number.lightWeeks` | 1.813144785984e14 |
| Light-year | `KDistanceUnit.LIGHT_YEAR` | `ly` | `Number.lightYears` | 9.4607304725808e15 |
| Parsec | `KDistanceUnit.PARSEC` | `pc` | `Number.parsecs` | 3.0856775814913673e16 |

Every unit above has a matching bare `val` alias for use as a `valueAs`/`toString` target or as the `unit`
argument of a prefix `infix` function, e.g. `meters`, `miles`, `nauticalMiles`, `yards`, `feet`, `inches`,
`fathoms`, `chains`, `furlongs`, `astronomicalUnits`, `lightSeconds`, `lightMinutes`, `lightHours`,
`lightDays`, `lightWeeks`, `lightYears`, `parsecs`.

```kotlin
import org.pcsoft.framework.kunit.distance.*

val d = 5.miles
d.value                        // 8046.72 (normalized to meters)
d.valueAs(KDistanceUnit.MILE)    // 5.0 (read back in miles)
d.valueAs(feet)                 // 26400.0
d.valueAs(nauticalMiles)        // ≈ 4.3452 (read back as nautical miles)
```

### Operators

```kotlin
import org.pcsoft.framework.kunit.distance.*

// + / - : same group, automatic conversion between different length units
val a = 1.miles + 500.meters   // KLengthUnitInstance, normalized to meters
val b = 2.miles - 800.meters

// comparisons
2.miles > 1.miles               // true
1.miles == 1609.344.meters      // true (same normalized value)
// 5.hectares > 5.meters        // does NOT compile: area vs. length are different types

// * / / stay in the length family when both operands are statically dimensioned
val area = 200.meters * 50.meters   // KAreaUnitInstance: value=10000.0 (m²)
val lengthAgain = area / 50.meters  // KLengthUnitInstance: value=200.0 (m)
val ratio = 10.meters / 2.meters    // KMixedUnitInstance (dimensionless), value=5.0
```

### Comparisons and equality

`==`, `!=`, `<`, `<=`, `>`, `>=` compare the normalized `value` of two values of the **same type** (same
dimension). Mixing dimensions (e.g. a length and an area) is rejected by the compiler - there is no such
operator - consistent with the `+`/`-` rules. `equals` across dimensions simply returns `false`.

## Exponent 2 - Area

`KAreaUnitInstance` represents an area, e.g. the result of `length * length` or of raising a length to the
second power with the infix `pow` operator (`2.meters pow 2` == `(2 m)²` == 4 m², `2 kilo meters pow 2`
== 4 000 000 m²). There are no `squareXxx` creators — `pow` is the only power syntax (see
[Powers with `pow`](#powers-with-pow)). The following named special units (`KDistanceDerivedUnit`) are
available as conversion/formatting targets:

| Special unit | Enum value | Symbol | Creator | 1 unit in m² |
|---|---|---:|---:|---:|
| Are | `KDistanceDerivedUnit.ARE` | `a` | `Number.ares` | 100.0 |
| Hectare | `KDistanceDerivedUnit.HECTARE` | `ha` | `Number.hectares` | 10 000.0 |
| Acre | `KDistanceDerivedUnit.ACRE` | `ac` | `Number.acres` | 4046.8564224 |

```kotlin
import org.pcsoft.framework.kunit.distance.*

val plot = 3.hectares
plot.value                                   // 30000.0 (m²)
plot.valueAs(KDistanceDerivedUnit.ARE)          // 300.0
plot.valueAs(KDistanceDerivedUnit.ACRE)         // ≈ 7.4132

val computed = 200.meters * 50.meters     // KAreaUnitInstance (10 000 m²)
computed.valueAs(KDistanceDerivedUnit.HECTARE) // 1.0

plot + computed                              // allowed: both are areas -> KAreaUnitInstance
// plot + 5.meters                           // does NOT compile: area vs. length
```

## Exponent 3 - Volume

`KVolumeUnitInstance` represents a volume, e.g. `length * length * length`, `area * length`, or a length
raised to the third power (`2.meters pow 3` == 8 m³, `2 kilo meters pow 3`). As with area, there are no
`cubicXxx` creators — use `pow` (see [Powers with `pow`](#powers-with-pow)). The following named special
units are available:

| Special unit | Enum value | Symbol | Creator | 1 unit in m³ |
|---|---|---:|---:|---:|
| Liter | `KDistanceDerivedUnit.LITER` | `L` | `Number.liters` | 0.001 |
| US liquid gallon | `KDistanceDerivedUnit.US_GALLON` | `gal (US)` | `Number.usGallons` | 0.003785411784 |
| Imperial gallon | `KDistanceDerivedUnit.IMPERIAL_GALLON` | `gal (UK)` | `Number.imperialGallons` | 0.00454609 |
| US fluid ounce | `KDistanceDerivedUnit.US_FLUID_OUNCE` | `fl oz` | `Number.usFluidOunces` | 2.95735295625e-5 |
| Oil barrel | `KDistanceDerivedUnit.OIL_BARREL` | `bbl` | `Number.oilBarrels` | 0.158987294928 |

```kotlin
import org.pcsoft.framework.kunit.distance.*

val tank = 200.liters
tank.value                                        // 0.2 (m³)
tank.valueAs(KDistanceDerivedUnit.US_GALLON)        // ≈ 52.834

val cube = 2.meters * 2.meters * 2.meters   // KVolumeUnitInstance (8 m³)
cube.valueAs(KDistanceDerivedUnit.LITER)    // 8000.0

tank + cube                                  // allowed: both are volumes -> KVolumeUnitInstance
```

## Powers with `pow`

Raise a value to an integer power with the infix `pow` operator. Kotlin has no overloadable `^` operator
(and no `^=`), so `pow` is the single, group-wide power syntax — there are no `squareXxx`/`cubicXxx`
constructors.

`pow` raises the value **and** multiplies every exponent by `n`, so `2.meters pow 2` is `(2 m)² = 4 m²`
(the value is powered, not merely the exponent). For the distance group the result is dimensioned: `pow 2`
yields a `KAreaUnitInstance`, `pow 3` a `KVolumeUnitInstance`, other exponents the general
`KDistanceUnitInstance`.

```kotlin
import org.pcsoft.framework.kunit.distance.*

val area = 2.meters pow 2         // KAreaUnitInstance: 4.0 m²
val big = 2 kilo meters pow 2     // KAreaUnitInstance: 4 000 000 m²  ((2000 m)²)
val volume = 2.meters pow 3       // KVolumeUnitInstance: 8.0 m³
val m4 = 2.meters pow 2 pow 2     // KDistanceUnitInstance: 16.0 m⁴  ((4 m²)²)
val inverse = 2.meters pow -1     // KDistanceUnitInstance: 0.5 m⁻¹
```

`pow` is a named infix function, so it binds **weaker** than `* / + -`; parenthesize in mixed expressions
(`(a * b) pow 2`). It is available on every unit group — e.g. `2.hours pow 2` (a generic
`KMixedUnitInstance`, since time has no dimensioned power type).

## SI prefixes

Any `KDistanceUnit` can be combined with any of the 24 SI prefixes (`KUnitPrefix`, root package, Quetta/Q to
Quecto/q) using the length-group `infix` construction functions (which return a `KLengthUnitInstance`
directly) and `with` (for `valueAs`/`toString` targets):

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.distance.*

// Construction: "5 kilo meters" -> KLengthUnitInstance (direct, == 5000.meters)
val fiveKm = 5 kilo meters
fiveKm.value // 5000.0

// Reading back a value using a prefixed target
val d = 5.miles
d.valueAs(KUnitPrefix.KILO with KDistanceUnit.METER)  // 8.04672 (km)
d.toString(KUnitPrefix.KILO with KDistanceUnit.METER) // "8.04672 km"

// Prefixes also combine with derived units (area/volume)
val tank = 200.liters
tank.valueAs(KUnitPrefix.MILLI with KDistanceDerivedUnit.LITER) // 200000.0 (mL)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.distance.*

5.meters.toString()                        // "5.0 m" (base unit representation)
5.miles.toString(KDistanceUnit.MILE)          // "5.0 mi"
(200.meters * 50.meters).toString(KDistanceDerivedUnit.HECTARE) // "1.0 ha"
```
