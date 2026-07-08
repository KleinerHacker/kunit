# Storage

Package: `org.pcsoft.framework.kunit.storage`
Base unit: **byte** (`KStorageUnit.BASE == KStorageUnit.BYTE`)

The storage group models a digital data amount. It is a **plain, one-dimensional** group (no
exponent-specialized subtypes like the distance group, and no `Duration` backing like the time group):
`KStorageUnitInstance` wraps a single `KStorageUnit.BASE` (byte) term, always stored normalized to bytes.

Two things make this group special:

* **No diminishing prefixes.** A fraction of a bit is not a meaningful data amount, so the diminishing
  SI prefixes (`deci`, `centi`, `milli`, … — factor `< 1`) are **not** available for `bytes`/`bits`.
  Writing `milli.bytes` is a **compile error**, not a runtime failure: the `bytes`/`bits` properties hang
  only on the augmenting SI builder (`KAugmentingPrefixBuilder`) and the binary builder, never on the
  diminishing builder.
* **Binary (IEC) prefixes.** In addition to the decimal SI builders (`kilo` = 1000) there is a second,
  binary builder system (`kibi` = 1024, `mebi` = 1024², …), so a value can distinguish the decimal step
  1000 from the binary step 1024.

## Units

| Unit | Enum value | Symbol | Token | 1 unit in bytes |
|---|---|---|---:|---:|
| Byte | `KStorageUnit.BYTE` | `B` | `bytes` | 1.0 |
| Bit | `KStorageUnit.BIT` | `bit` | `bits` | 0.125 |

One byte is eight bits. Each `Token` is a value-1 `KStorageUnitInstance` used with `of` (build) and `into`
(read).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*

val size = 5 of bytes
size.value          // 5.0 (normalized to bytes)
size into bits      // 40.0 (read back in bits)
(1 of bytes) into bits   // 8.0
(8 of bits) into bytes   // 1.0
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds

// + / - : same group, automatic conversion between bit and byte
val a = (1 of bytes) + (8 of bits)   // KStorageUnitInstance: 2.0 B
val b = (4 of bytes) - (16 of bits)  // KStorageUnitInstance: 2.0 B

// comparisons
(1 of bytes) == (8 of bits)          // true (same normalized amount)
(2 of bytes) > (1 of bytes)          // true

// storage / time is a typed data rate (see the Data Rate page)
val rate = (1000 of bytes) / (2 of seconds)  // KDataRateUnitInstance: 500 B/s
```

### Comparisons and equality

`==`, `!=`, `<`, `<=`, `>`, `>=` compare the normalized `value` (bytes) of two `KStorageUnitInstance`
values. `equals` is by normalized amount, so `(1 of bytes) == (8 of bits)`.

## Powers with `pow`

Raise a value to an integer power with the infix `pow` operator (Kotlin has no overloadable `^`). For the
storage group `pow` returns a generic `KMixedUnitInstance` (storage has no dimensioned power type):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.storage.*

val squared = (2 of bytes) pow 2     // KMixedUnitInstance: 4.0 B²
```

## Decimal SI prefixes

Any storage unit can be combined with the **augmenting** (supra-unity) SI prefix builders (`deca`,
`hecto`, `kilo`, `mega`, `giga`, `tera`, `peta`, `exa`, `zetta`, `yotta`, `ronna`, `quetta`) via property
access. The diminishing builders (`deci` downward) have **no** `bytes`/`bits` property, so `milli.bytes`
does not compile.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.*

val fiveKb = 5 of kilo.bytes         // KStorageUnitInstance (== 5000 B)
fiveKb.value                         // 5000.0

(3 of bytes) into kilo.bytes         // 0.003 (kB)

// 5 of milli.bytes                  // does NOT compile: no `bytes` on the diminishing builder
```

## Binary (IEC) prefixes

The binary prefix builders are powers of 1024 and let a value distinguish 1000 (`kilo`) from 1024
(`kibi`): `kibi`, `mebi`, `gibi`, `tebi`, `pebi`, `exbi`, `zebi`, `yobi`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.*

(1 of kilo.bytes).value   // 1000.0     (decimal)
(1 of kibi.bytes).value   // 1024.0     (binary)
(1 of mega.bytes).value   // 1_000_000.0
(1 of mebi.bytes).value   // 1_048_576.0

val file = 4 of mebi.bytes
file into kibi.bytes      // 4096.0 (KiB)
```

| Binary builder | Symbol | 1 unit (bytes) |
|---|---|---:|
| `kibi` | `Ki` | 1024 |
| `mebi` | `Mi` | 1024² |
| `gibi` | `Gi` | 1024³ |
| `tebi` | `Ti` | 1024⁴ |
| `pebi` | `Pi` | 1024⁵ |
| `exbi` | `Ei` | 1024⁶ |
| `zebi` | `Zi` | 1024⁷ |
| `yobi` | `Yi` | 1024⁸ |

## Mixing with other units

A storage value combined with a time forms a data rate (`byte·second⁻¹`), and can be decomposed back:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds

val rate = (1000 of bytes) / (1 of seconds)  // 1000 B/s (typed KDataRateUnitInstance)
val amount = rate * (60 of seconds)          // 60000 B (typed KStorageUnitInstance)
amount into kibi.bytes                        // ≈ 58.59 (KiB)
```

## toString formatting

Only the base-unit `toString()` exists; format a specific unit via `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*

(1024 of bytes).toString()               // "1024.0 B" (base unit representation)
"${(5 of bits) into bits} bit"           // "5.0 bit"
"${(2048 of bytes) into kibi.bytes} KiB" // "2.0 KiB"
```
