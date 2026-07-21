# Data Rate

Package: `org.pcsoft.framework.kunit.datarate`
Base unit: **byte per second** (`KDataRateUnit.BASE == KDataRateUnit.BYTES_PER_SECOND`)

Data rate is a **constructed** unit (the second one, after [Speed](speed.md)): it is not a single "real"
quantity but a composition, `storage · time⁻¹` (`B/s`). `KDataRateUnitInstance` therefore wraps a
`KMixedUnitInstance` of exactly two terms - one `KStorageUnit.BASE` (byte) at exponent `+1` and one
`KTimeUnit.BASE` (second) at exponent `-1`. The value is always stored normalized to bytes per second,
regardless of which unit or storage/time combination it was created from.

## Building a data rate

A data rate is built as a **storage-per-time expression**, e.g. `100 of bytes / seconds`,
`5 of mega.bytes / seconds` or `10 of kibi.bytes / seconds` — each yields a `KDataRateUnitInstance`. Read it
back in any storage-per-time template (`r into (bits / seconds)`). There are deliberately **no** spelled-out
composite tokens like `bytesPerSecond` (they are exactly `bytes / seconds`).

Base unit: a *byte* per second, consistent with the storage group. The networking-native bit/s (`bps`) is
`0.125 B/s`; a "megabit per second" is `1 of mega.bits / seconds`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

val r = 100 of bytes / seconds
r.value                  // 100.0 (normalized to B/s)
r into (bits / seconds)  // 800.0 (read back in bit/s)
```

## Computing with the core units (storage & time)

A data rate *is* a storage amount divided by a time. Move between the three quantities - storage, time and
data rate - with plain `*` and `/`; each result is **strongly typed**.

| Expression | Result type | Meaning |
|---|---|---|
| `storage / time` | `KDataRateUnitInstance` | rate = amount / duration |
| `data rate * time` | `KStorageUnitInstance` | amount = rate × duration |
| `time * data rate` | `KStorageUnitInstance` | amount (commutative) |
| `storage / data rate` | `KTimeUnitInstance` | duration = amount / rate |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.datarate.*

// --- core units -> data rate --------------------------------------------
val r = (100 of bytes) / (10 of seconds)   // KDataRateUnitInstance (NO .toDataRate() needed!)
r.value                  // 10.0 (B/s)
r into (bits / seconds)  // 80.0

// prefixed numerator, klammerfrei:
val download = 5 of mega.bytes / seconds   // KDataRateUnitInstance (5 MB/s)

// --- data rate -> storage (multiply by a time) --------------------------
val amount = r * (60 of seconds)   // KStorageUnitInstance
amount into bytes     // 600.0
amount into bits      // 4800.0
(60 of seconds) * r   // same result (commutative)

// --- data rate -> time (divide a storage amount by it) ------------------
val time = (600 of bytes) / r      // KTimeUnitInstance
time into minutes     // 1.0
```

!!! warning "Only a *pure* storage / time shape is a data rate"
    `KMixedUnitInstance.toDataRate()` requires exactly one storage term at exponent `+1` and one time term
    at exponent `-1`. A `B²` (storage squared), a `B·s⁻²`, or a `B·s` shape is not a data rate - the
    conversion throws `IllegalStateException`. Likewise, `storage + data rate` (different dimensions) is a
    compile error.

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

// + / - : same group, automatic conversion between byte- and bit-based rates
val a = (1 of bytes / seconds) + (8 of bits / seconds)   // KDataRateUnitInstance, 2 B/s
val b = (2 of bytes / seconds) - (8 of bits / seconds)   // 1 B/s

// comparisons (by normalized B/s value)
(1 of bytes / seconds) > (4 of bits / seconds)           // true
(1 of bytes / seconds) == (8 of bits / seconds)          // true

// * / / between two data rates escape to a KMixedUnitInstance (no longer a pure rate)
val squared = (10 of bytes / seconds) * (2 of bytes / seconds) // KMixedUnitInstance, [B^2, s^-2]
```

## SI and binary (IEC) prefixes

The data-rate group mirrors the [Storage](storage.md) group's prefix policy (its numerator is a storage
amount): the numerator uses the **augmenting** SI builders (`kilo`, `mega`, …) or the **binary** builders
(`kibi`, `mebi`, …); the diminishing builders have no `bytes`/`bits` property, so `milli.bytes / seconds`
does not compile.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

// Decimal vs binary: 1000 (kilo) != 1024 (kibi)
(1 of kilo.bytes / seconds).value // 1000.0
(1 of kibi.bytes / seconds).value // 1024.0

// Reading a value back in a storage-per-time template
val r = 4096 of bytes / seconds
r into (kilo.bytes / seconds)  // 4.096 (kB/s)
r into (kibi.bytes / seconds)  // 4.0   (KiB/s)
```

## toString formatting

Only the base-unit `toString()` exists; format a specific unit via `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.storage.kibi
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

(10 of bytes / seconds).toString()  // "10.0 B/s" (base unit)
"${(4096 of bytes / seconds) into (kibi.bytes / seconds)} KiB/s" // "4.0 KiB/s"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `B/s` | `bytes / seconds` | data rate, base unit (byte per second) — fraction form |
| `B·s⁻¹` | `bytes * (seconds pow -1)` | same rate as a product with a negative exponent |
| `bit/s` | `bits / seconds` | bit per second |
| `MB/s` | `mega.bytes / seconds` | megabyte per second |
| `100 B / 10 s` | `(100 of bytes) / (10 of seconds)` | build from storage ÷ time |
