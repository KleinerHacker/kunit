# Mass

Package: `org.pcsoft.framework.kunit.mass`
Base unit: **gram** (`KMassUnit.BASE == KMassUnit.GRAM`)

The mass group models a quantity of mass. It is a **plain, one-dimensional** group (no
exponent-specialized subtypes like the distance group, and no `Duration` backing like the time group):
`KMassUnitInstance` wraps a single `KMassUnit.GRAM` term, always stored normalized to grams.

The base unit is deliberately the **gram**, not the kilogram. The kilogram is not a dedicated unit at
all — it is simply `kilo.grams`, the SI prefix `kilo` applied to the gram. Every decimal magnitude of
the gram (milligram, kilogram, …) is reached the same generic way through the SI prefixes.

## Units

| Group | Unit | Enum value | Symbol | Token | 1 unit in grams |
|---|---|---|---|---:|---:|
| Metric | Gram | `KMassUnit.GRAM` | `g` | `grams` | 1.0 |
| Metric | Tonne (metric ton) | `KMassUnit.TONNE` | `t` | `tonnes` | 1 000 000 |
| Metric | Carat (metric) | `KMassUnit.CARAT` | `ct` | `carats` | 0.2 |
| Avoirdupois | Grain | `KMassUnit.GRAIN` | `gr` | `grains` | 0.06479891 |
| Avoirdupois | Dram | `KMassUnit.DRAM` | `dr` | `drams` | 1.7718451953125 |
| Avoirdupois | Ounce | `KMassUnit.OUNCE` | `oz` | `ounces` | 28.349523125 |
| Avoirdupois | Pound | `KMassUnit.POUND` | `lb` | `pounds` | 453.59237 |
| Avoirdupois | Stone | `KMassUnit.STONE` | `st` | `stones` | 6350.29318 |
| Avoirdupois | Hundredweight US (short) | `KMassUnit.HUNDREDWEIGHT_US` | `cwt(US)` | `hundredweightsUS` | 45 359.237 |
| Avoirdupois | Hundredweight UK (long) | `KMassUnit.HUNDREDWEIGHT_UK` | `cwt(UK)` | `hundredweightsUK` | 50 802.34544 |
| Avoirdupois | Short ton (US) | `KMassUnit.SHORT_TON` | `ton(US)` | `shortTons` | 907 184.74 |
| Avoirdupois | Long ton (UK) | `KMassUnit.LONG_TON` | `ton(UK)` | `longTons` | 1 016 046.9088 |
| Avoirdupois | Slug | `KMassUnit.SLUG` | `slug` | `slugs` | 14 593.90294 |
| Troy | Pennyweight | `KMassUnit.PENNYWEIGHT` | `dwt` | `pennyweights` | 1.55517384 |
| Troy | Troy ounce | `KMassUnit.TROY_OUNCE` | `oz t` | `troyOunces` | 31.1034768 |
| Troy | Troy pound | `KMassUnit.TROY_POUND` | `lb t` | `troyPounds` | 373.2417216 |
| Historical | German pound | `KMassUnit.GERMAN_POUND` | `Pfd` | `germanPounds` | 500 |
| Historical | Zentner | `KMassUnit.ZENTNER` | `Ztr` | `zentners` | 50 000 |
| Historical | Lot | `KMassUnit.LOT` | `Lot` | `lots` | 16.6666667 |
| Regional | Jin / catty | `KMassUnit.JIN` | `斤` | `jin` | 500 |
| Regional | Liang / tael | `KMassUnit.LIANG` | `两` | `liang` | 50 |
| Regional | Momme | `KMassUnit.MOMME` | `匁` | `momme` | 3.75 |
| Regional | Kan / kanme | `KMassUnit.KAN` | `貫` | `kan` | 3750 |
| Scientific | Dalton (u) | `KMassUnit.DALTON` | `Da` | `daltons` | 1.6605390666e-24 |

Each `Token` is a value-1 `KMassUnitInstance` used with `of` (build) and `into` (read).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.*

val m = 2 of kilo.grams      // 2000 g (the kilogram is `kilo.grams`)
m.value                      // 2000.0 (normalized to grams)
m into pounds                // ≈ 4.409 (read back in pounds)
(1 of pounds) into grams     // 453.59237
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.*

// + / - : same group, automatic conversion between units
val a = (1 of kilo.grams) + (500 of grams)   // KMassUnitInstance: 1500.0 g
val b = (1 of kilo.grams) - (500 of grams)   // KMassUnitInstance: 500.0 g

// comparisons
(1 of kilo.grams) == (1000 of grams)         // true (same normalized amount)
(1 of kilo.grams) > (500 of grams)           // true
```

### Comparisons and equality

`==`, `!=`, `<`, `<=`, `>`, `>=` compare the normalized `value` (grams) of two `KMassUnitInstance`
values. `equals` is by normalized quantity, so `(1 of kilo.grams) == (1000 of grams)`.

## Powers with `pow`

Raise a value to an integer power with the infix `pow` operator (Kotlin has no overloadable `^`). For
the mass group `pow` returns a generic `KMixedUnitInstance` (mass has no dimensioned power type):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mass.*

val squared = (2 of grams) pow 2     // KMixedUnitInstance: 4.0 g²
```

## SI prefixes

Mass accepts **any** magnitude, so every SI prefix builder (`quetta` … `quecto`) can be combined with
every mass unit via property access. The kilogram is exactly `kilo.grams`; a milligram is `milli.grams`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mass.*

(1 of kilo.grams).value    // 1000.0     (kilogram)
(1 of milli.grams).value   // 0.001      (milligram)

(2500 of grams) into kilo.grams  // 2.5
```

## toString formatting

Only the base-unit `toString()` exists; format a specific unit via `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.*

(1 of kilo.grams).toString()             // "1000.0 g" (base unit representation)
"${(2000 of grams) into kilo.grams} kg"  // "2.0 kg"
```
