# Storage

Package: `org.pcsoft.framework.kunit.storage`
Base unit: **byte** (`KStorageUnit.BASE == KStorageUnit.BYTE`)

The storage group models a digital data amount. It is a **plain, one-dimensional** group (no
exponent-specialized subtypes like the distance group, and no `Duration` backing like the time group):
`KStorageUnitInstance` wraps a single `KStorageUnit.BASE` (byte) term, always stored normalized to bytes.

Two things make this group special:

* **No diminishing prefixes.** A fraction of a bit is not a meaningful data amount, so the diminishing
  SI prefixes (`deci`, `centi`, `milli`, … — factor `< 1`) are **not** offered. Writing `5 milli bytes`
  is a **compile error**, not a runtime failure. Only the non-diminishing SI prefixes (`deca` upward)
  exist.
* **Binary (IEC) prefixes.** In addition to the decimal SI prefixes (`kilo` = 1000) there is a second,
  binary prefix system (`KStorageBinaryPrefix`: `kibi` = 1024, `mebi` = 1024², …), so a value can
  distinguish the decimal step 1000 from the binary step 1024.

## Units

| Unit | Enum value | Symbol | Creator | 1 unit in bytes |
|---|---|---|---:|---:|
| Byte | `KStorageUnit.BYTE` | `B` | `Number.bytes` | 1.0 |
| Bit | `KStorageUnit.BIT` | `bit` | `Number.bits` | 0.125 |

One byte is eight bits. Both units have a matching bare `val` alias (`bytes`, `bits`) for use as a
`valueAs`/`toString` target or as the `unit` argument of a prefix `infix` function.

```kotlin
import org.pcsoft.framework.kunit.storage.*

val size = 5.bytes
size.value                     // 5.0 (normalized to bytes)
size.valueAs(bits)             // 40.0 (read back in bits)
1.bytes.valueAs(bits)          // 8.0
8.bits.valueAs(bytes)          // 1.0
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.storage.*

// + / - : same group, automatic conversion between bit and byte
val a = 1.bytes + 8.bits        // KStorageUnitInstance: 2.0 B
val b = 4.bytes - 16.bits       // KStorageUnitInstance: 2.0 B

// comparisons
1.bytes == 8.bits               // true (same normalized amount)
2.bytes > 1.bytes               // true

// * / / delegate to the mixed engine (against a KMixedUnitInstance)
val rate = 1000.bytes.toUnit() / 2.seconds.toUnit() // KMixedUnitInstance: 500 B·s⁻¹
```

### Comparisons and equality

`==`, `!=`, `<`, `<=`, `>`, `>=` compare the normalized `value` (bytes) of two `KStorageUnitInstance`
values. `equals` is by normalized amount, so `1.bytes == 8.bits`.

## Powers with `pow`

Raise a value to an integer power with the infix `pow` operator (Kotlin has no overloadable `^`). For the
storage group `pow` returns a generic `KMixedUnitInstance` (storage has no dimensioned power type):

```kotlin
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.storage.*

val squared = 2.bytes pow 2     // KMixedUnitInstance: 4.0 B²
```

`pow` is a named infix function, so it binds **weaker** than `* / + -`; parenthesize in mixed
expressions (`(a * b) pow 2`).

## Decimal SI prefixes

Any `KStorageUnit` can be combined with the **non-diminishing** SI prefixes (`deca`, `hecto`, `kilo`,
`mega`, `giga`, `tera`, `peta`, `exa`, `zetta`, `yotta`, `ronna`, `quetta`) via the storage-group `infix`
construction functions (which return a `KStorageUnitInstance` directly) and `with` (for
`valueAs`/`toString` targets). The diminishing prefixes (`deci` downward) do **not** exist for storage.

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.storage.*

val fiveKb = 5 kilo bytes                 // KStorageUnitInstance (== 5000.bytes)
fiveKb.value                              // 5000.0

val big = 3.bytes
big.valueAs(KUnitPrefix.KILO with bytes)  // 0.003 (kB)

// 5 milli bytes                          // does NOT compile: diminishing prefixes are not offered
```

## Binary (IEC) prefixes

The binary prefixes are powers of 1024 and let a value distinguish 1000 (`kilo`) from 1024 (`kibi`).
They come as `infix` construction functions (`kibi`, `mebi`, `gibi`, `tebi`, `pebi`, `exbi`, `zebi`,
`yobi`) and as `valueAs`/`toString` targets via `KStorageBinaryPrefix` + `with`.

```kotlin
import org.pcsoft.framework.kunit.storage.*

(1 kilo bytes).value                                  // 1000.0  (decimal)
(1 kibi bytes).value                                  // 1024.0  (binary)
(1 mega bytes).value                                  // 1_000_000.0
(1 mebi bytes).value                                  // 1_048_576.0

val file = 4 mebi bytes
file.valueAs(KStorageBinaryPrefix.KIBI with bytes)    // 4096.0 (KiB)
file.toString(KStorageBinaryPrefix.MEBI with bytes)   // "4.0 MiB"
```

| Binary prefix | Enum value | Symbol | `infix` | Factor |
|---|---|---|---:|---:|
| Kibi | `KStorageBinaryPrefix.KIBI` | `Ki` | `kibi` | 1024 |
| Mebi | `KStorageBinaryPrefix.MEBI` | `Mi` | `mebi` | 1024² |
| Gibi | `KStorageBinaryPrefix.GIBI` | `Gi` | `gibi` | 1024³ |
| Tebi | `KStorageBinaryPrefix.TEBI` | `Ti` | `tebi` | 1024⁴ |
| Pebi | `KStorageBinaryPrefix.PEBI` | `Pi` | `pebi` | 1024⁵ |
| Exbi | `KStorageBinaryPrefix.EXBI` | `Ei` | `exbi` | 1024⁶ |
| Zebi | `KStorageBinaryPrefix.ZEBI` | `Zi` | `zebi` | 1024⁷ |
| Yobi | `KStorageBinaryPrefix.YOBI` | `Yi` | `yobi` | 1024⁸ |

## Mixing with other units

A storage value combined with a time forms a data rate (`byte·second⁻¹`) through the mixed engine, and
can be decomposed back:

```kotlin
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds

val rate = 1000.bytes.toUnit() / 1.seconds.toUnit()   // 1000 B/s
val amount = (rate * 60.seconds.toUnit()).toStorage() // 60000 B
amount.valueAs(KStorageBinaryPrefix.KIBI with bytes)  // ≈ 58.59 (KiB)
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.storage.*

1024.bytes.toString()                                   // "1024.0 B" (base unit representation)
5.bits.toString(bits)                                   // "5.0 bit"
2048.bytes.toString(KStorageBinaryPrefix.KIBI with bytes) // "2.0 KiB"
```
