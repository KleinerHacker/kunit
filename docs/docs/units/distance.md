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

Build every value with `number of <token>` and read it back with `value into <token>`.

## Exponent 1 - Length

| Unit | Enum value | Symbol | Token | 1 unit in meters |
|---|---|---|---:|---:|
| Meter | `KDistanceUnit.METER` | `m` | `meters` | 1.0 |
| Mile | `KDistanceUnit.MILE` | `mi` | `miles` | 1609.344 |
| Nautical mile | `KDistanceUnit.NAUTICAL_MILE` | `nmi` | `nauticalMiles` | 1852.0 |
| Yard | `KDistanceUnit.YARD` | `yd` | `yards` | 0.9144 |
| Foot | `KDistanceUnit.FOOT` | `ft` | `feet` | 0.3048 |
| Inch | `KDistanceUnit.INCH` | `in` | `inches` | 0.0254 |
| Fathom | `KDistanceUnit.FATHOM` | `ftm` | `fathoms` | 1.8288 |
| Chain | `KDistanceUnit.CHAIN` | `ch` | `chains` | 20.1168 |
| Furlong | `KDistanceUnit.FURLONG` | `fur` | `furlongs` | 201.168 |
| Astronomical unit | `KDistanceUnit.ASTRONOMICAL_UNIT` | `AU` | `astronomicalUnits` | 1.495978707e11 |
| Parsec | `KDistanceUnit.PARSEC` | `pc` | `parsecs` | 3.0856775814913673e16 |
| Cubit | `KDistanceUnit.CUBIT` | `cubit` | `cubits` | 0.4572 |
| Roman foot (pes) | `KDistanceUnit.ROMAN_FOOT` | `pes` | `romanFeet` | 0.2957 |
| Roman pace (passus) | `KDistanceUnit.ROMAN_PACE` | `passus` | `romanPaces` | 1.4787 |
| Stadium | `KDistanceUnit.STADIUM` | `stadium` | `stadia` | 185.0 |
| Roman mile (mille passus) | `KDistanceUnit.ROMAN_MILE` | `mp` | `romanMiles` | 1481.5 |
| Rod (perch) | `KDistanceUnit.ROD` | `rod` | `rods` | 5.0292 |
| League | `KDistanceUnit.LEAGUE` | `lea` | `leagues` | 4828.032 |
| Cable length | `KDistanceUnit.CABLE_LENGTH` | `cable` | `cableLengths` | 185.2 |
| Verst | `KDistanceUnit.VERST` | `verst` | `versts` | 1066.8 |
| Prussian mile | `KDistanceUnit.PRUSSIAN_MILE` | `prussian mi` | `prussianMiles` | 7532.5 |

### Light-travel distances (prefix-free `light` group)

Light-travel distances are grouped behind the prefix-free `light` builder and read almost like prose,
e.g. `5 of light.seconds`, `3 of light.years`. They deliberately accept **no** SI prefixes (a
`kilo.lightYears` is physically meaningless).

| Unit | Enum value | Symbol | Token | 1 unit in meters |
|---|---|---|---:|---:|
| Light-second | `KDistanceUnit.LIGHT_SECOND` | `ls` | `light.seconds` | 299792458.0 |
| Light-minute | `KDistanceUnit.LIGHT_MINUTE` | `lmin` | `light.minutes` | 1.798754748e10 |
| Light-hour | `KDistanceUnit.LIGHT_HOUR` | `lh` | `light.hours` | 1.0792528488e12 |
| Light-day | `KDistanceUnit.LIGHT_DAY` | `ld` | `light.days` | 2.59020683712e13 |
| Light-week | `KDistanceUnit.LIGHT_WEEK` | `lw` | `light.weeks` | 1.813144785984e14 |
| Light-year | `KDistanceUnit.LIGHT_YEAR` | `ly` | `light.years` | 9.4607304725808e15 |

Each `Token` is a value-1 `KLengthUnitInstance` used with both `of` (build) and `into` (read).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

val d = 5 of miles
d.value               // 8046.72 (normalized to meters)
d into miles          // 5.0 (read back in miles)
d into feet           // 26400.0
d into nauticalMiles  // ≈ 4.3452
```

### Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

// + / - : same group, automatic conversion between different length units
val a = (1 of miles) + (500 of meters)   // KLengthUnitInstance, normalized to meters
val b = (2 of miles) - (800 of meters)

// comparisons
(2 of miles) > (1 of miles)              // true
(1 of miles) == (1609.344 of meters)     // true (same normalized value)
// (5 of hectares) > (5 of meters)       // does NOT compile: area vs. length are different types

// * / / stay in the length family when both operands are statically dimensioned
val area = (200 of meters) * (50 of meters)   // KAreaUnitInstance: value=10000.0 (m²)
val lengthAgain = area / (50 of meters)       // KLengthUnitInstance: value=200.0 (m)
val ratio = (10 of meters) / (2 of meters)    // KMixedUnitInstance (dimensionless), value=5.0
```

### Comparisons and equality

`==`, `!=`, `<`, `<=`, `>`, `>=` compare the normalized `value` of two values of the **same type** (same
dimension). Mixing dimensions (e.g. a length and an area) is rejected by the compiler - there is no such
operator - consistent with the `+`/`-` rules. `equals` across dimensions simply returns `false`.

## Exponent 2 - Area

`KAreaUnitInstance` represents an area, e.g. the result of `length * length` or of raising a length to the
second power with the infix `pow` operator (`(2 of meters) pow 2` == `(2 m)²` == 4 m²,
`(2 of kilo.meters) pow 2` == 4 000 000 m²). There are no `squareXxx` tokens — `pow` is the only power
syntax (see [Powers with `pow`](#powers-with-pow)). The following named special-unit tokens are available:

| Special unit | Symbol | Token | 1 unit in m² |
|---|---:|---:|---:|
| Are | `a` | `ares` | 100.0 |
| Hectare | `ha` | `hectares` | 10 000.0 |
| Acre | `ac` | `acres` | 4046.8564224 |
| Rood | `ro` | `roods` | 1011.7141056 |
| Square perch (square rod) | `perch²` | `squarePerches` | 25.29285264 |
| Morgen (Prussian) | `Mg` | `morgens` | 2553.22 |
| Joch (Austrian) | `Joch` | `jochs` | 5754.642 |
| Tagwerk (Bavarian) | `Tw` | `tagwerks` | 3407.27 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

val plot = 3 of hectares
plot.value        // 30000.0 (m²)
plot into ares    // 300.0
plot into acres   // ≈ 7.4132

val computed = (200 of meters) * (50 of meters)  // KAreaUnitInstance (10 000 m²)
computed into hectares                           // 1.0

plot + computed   // allowed: both are areas -> KAreaUnitInstance
// plot + (5 of meters)  // does NOT compile: area vs. length
```

## Exponent 3 - Volume

`KVolumeUnitInstance` represents a volume, e.g. `length * length * length`, `area * length`, or a length
raised to the third power (`(2 of meters) pow 3` == 8 m³). As with area, there are no `cubicXxx` tokens —
use `pow` (see [Powers with `pow`](#powers-with-pow)). The following named special-unit tokens are available:

| Special unit | Symbol | Token | 1 unit in m³ |
|---|---:|---:|---:|
| Liter | `L` | `liters` | 0.001 |
| US liquid gallon | `gal (US)` | `usGallons` | 0.003785411784 |
| Imperial gallon | `gal (UK)` | `imperialGallons` | 0.00454609 |
| US fluid ounce | `fl oz` | `usFluidOunces` | 2.95735295625e-5 |
| Oil barrel | `bbl` | `oilBarrels` | 0.158987294928 |
| Imperial bushel | `bu (UK)` | `imperialBushels` | 0.03636872 |
| Imperial hogshead | `hhd` | `hogsheads` | 0.32731785 |
| Imperial pint | `pt (UK)` | `imperialPints` | 0.00056826125 |
| Imperial quart | `qt (UK)` | `imperialQuarts` | 0.0011365225 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.*

val tank = 200 of liters
tank.value          // 0.2 (m³)
tank into usGallons // ≈ 52.834

val cube = (2 of meters) * (2 of meters) * (2 of meters)  // KVolumeUnitInstance (8 m³)
cube into liters                                          // 8000.0

tank + cube         // allowed: both are volumes -> KVolumeUnitInstance
```

## Powers with `pow`

Raise a value to an integer power with the infix `pow` operator. Kotlin has no overloadable `^` operator
(and no `^=`), so `pow` is the single, group-wide power syntax — there are no `squareXxx`/`cubicXxx` tokens.

`pow` raises the value **and** multiplies every exponent by `n`, so `(2 of meters) pow 2` is `(2 m)² = 4 m²`
(the value is powered, not merely the exponent). For the distance group the result is dimensioned: `pow 2`
yields a `KAreaUnitInstance`, `pow 3` a `KVolumeUnitInstance`, other exponents the general
`KDistanceUnitInstance`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

val area = (2 of meters) pow 2         // KAreaUnitInstance: 4.0 m²
val big = (2 of kilo.meters) pow 2     // KAreaUnitInstance: 4 000 000 m²  ((2000 m)²)
val volume = (2 of meters) pow 3       // KVolumeUnitInstance: 8.0 m³
val m4 = (2 of meters) pow 2 pow 2     // KDistanceUnitInstance: 16.0 m⁴  ((4 m²)²)
val inverse = (2 of meters) pow -1     // KDistanceUnitInstance: 0.5 m⁻¹
```

`pow` binds **weaker** than `* / + -`; parenthesize in mixed expressions (`(a * b) pow 2`). It is available
on every unit group — e.g. `(2 of hours) pow 2` (a generic `KMixedUnitInstance`, since time has no
dimensioned power type).

## SI prefixes

Any length unit can be combined with any of the 24 SI prefix **builders** (`kilo`, `milli`, …; root
package) via property access, producing a value-1 template for `of`/`into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.distance.*

// Construction: "5 of kilo.meters" -> KLengthUnitInstance (== 5000 m)
val fiveKm = 5 of kilo.meters
fiveKm.value // 5000.0

// Reading back a value in a prefixed unit
val d = 5 of miles
d into kilo.meters  // 8.04672 (km)

// Prefixes also combine with the named area/volume tokens
val tank = 200 of liters
tank into milli.liters  // 200000.0 (mL)
```

## toString formatting

Only the base-unit `toString()` exists; format a specific unit via `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*

(5 of meters).toString()               // "5.0 m" (base unit representation)
"${(5 of miles) into miles} mi"        // "5.0 mi"
"${((200 of meters) * (50 of meters)) into hectares} ha" // "1.0 ha"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `m` | `meters` | length, base unit (metre) |
| `km` | `kilo.meters` | prefixed length (kilometre) |
| `m²` | `meters pow 2` | area (metre squared) |
| `m³` | `meters pow 3` | volume (metre cubed) |
| `m⁻¹` | `meters pow -1` | inverse length |
| `2 m · 2 m` | `(2 of meters) * (2 of meters)` | area built from length × length |
