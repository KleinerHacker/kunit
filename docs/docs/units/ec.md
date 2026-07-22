# Electric Current

Package: `org.pcsoft.framework.kunit.ec`
Base unit: **ampere** (`KElectricCurrentUnit.BASE == KElectricCurrentUnit.AMPERE`)

The electric current group models an electric current. It is a **plain, one-dimensional** native group
(no exponent-specialized subtypes, no cross-unit typed results): `KElectricCurrentUnitInstance` wraps a
single `KElectricCurrentUnit.AMPERE` term, always stored normalized to amperes.

Besides the SI ampere the group offers the two classic CGS current units: the **biot** (abampere) of the
electromagnetic system (`1 Bi = 10 A`) and the **statampere** of the electrostatic system
(`1 statA ≈ 3.335 641 × 10⁻¹⁰ A`).

## Units

| Group | Unit | Enum value | Symbol | Token | 1 unit in amperes |
|---|---|---|---|---:|---:|
| SI | Ampere | `KElectricCurrentUnit.AMPERE` | `A` | `amperes` | 1.0 |
| CGS | Biot / abampere | `KElectricCurrentUnit.BIOT` | `Bi` (`abA`) | `biot` / `abamperes` | 10 |
| CGS | Statampere | `KElectricCurrentUnit.STATAMPERE` | `statA` | `statamperes` | 3.335641e-10 |

Each `Token` is a value-1 `KElectricCurrentUnitInstance` used with `of` (build) and `into` (read).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

val i = 2 of milli.amperes    // 0.002 A
i.value                       // 0.002 (normalized to amperes)
i into amperes                // 0.002 (read back in amperes)
(1 of biot) into amperes      // 10.0
```

## Real-world example

Ohm's law: a resistor of `R = 220 Ω` across `U = 5 V` carries a current `I = U / R`. Expressed through
the current unit:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

val voltage = 5.0    // V
val resistance = 220.0 // Ω
val current = (voltage / resistance) of amperes   // ≈ 0.0227 A
current into milli.amperes                         // ≈ 22.7 mA
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.ec.*

// + / - : same group, automatic conversion between units
val a = (1 of amperes) + (1 of biot)   // KElectricCurrentUnitInstance: 11.0 A
val b = (1 of biot) - (1 of amperes)   // KElectricCurrentUnitInstance: 9.0 A

// comparisons
(1 of biot) == (10 of amperes)         // true (same normalized amount)
(1 of biot) > (1 of amperes)           // true
```

### Comparisons and equality

`==`, `!=`, `<`, `<=`, `>`, `>=` compare the normalized `value` (amperes) of two
`KElectricCurrentUnitInstance` values. `equals` is by normalized quantity, so
`(1 of biot) == (10 of amperes)`.

## Powers with `pow`

Raise a value to an integer power with the infix `pow` operator (Kotlin has no overloadable `^`). For
the electric current group `pow` returns a generic `KMixedUnitInstance` (current has no dimensioned
power type):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.ec.*

val squared = (2 of amperes) pow 2     // KMixedUnitInstance: 4.0 A²
```

## SI prefixes

Electric current accepts **any** magnitude, so every SI prefix builder (`quetta` … `quecto`) can be
combined with every current unit via property access. A milliampere is `milli.amperes`, a kiloampere is
`kilo.amperes`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

(1 of milli.amperes).value   // 0.001      (milliampere)
(1 of kilo.amperes).value    // 1000.0     (kiloampere)

(2500 of amperes) into kilo.amperes  // 2.5
```

## toString formatting

Only the base-unit `toString()` exists; format a specific unit via `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.ec.*

(1 of biot).toString()                       // "10.0 A" (base unit representation)
"${(0.002 of amperes) into milli.amperes} mA" // "2.0 mA"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `A` | `amperes` | electric current, base unit (ampere) |
| `mA` | `milli.amperes` | milliampere (prefix applied to the ampere) |
| `kA` | `kilo.amperes` | kiloampere |
| `Bi` | `biot` | biot / abampere (10 A) |
| `A²` | `amperes pow 2` | ampere squared (generic mixed unit) |
