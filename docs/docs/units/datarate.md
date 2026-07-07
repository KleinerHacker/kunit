# Data Rate

Package: `org.pcsoft.framework.kunit.datarate`
Base unit: **byte per second** (`KDataRateUnit.BASE == KDataRateUnit.BYTES_PER_SECOND`)

Data rate is a **constructed** unit (the second one, after [Speed](speed.md)): it is not a single "real"
quantity but a composition, `storage · time⁻¹` (`B/s`). `KDataRateUnitInstance` therefore wraps a
`KMixedUnitInstance` of exactly two terms - one `KStorageUnit.BASE` (byte) at exponent `+1` and one
`KTimeUnit.BASE` (second) at exponent `-1`. The value is always stored normalized to bytes per second,
regardless of which unit, prefix, or storage/time combination it was created from.

## Units

| Unit | Enum value | Symbol | Creator | 1 unit in B/s |
|---|---|---|---:|---:|
| Byte per second | `KDataRateUnit.BYTES_PER_SECOND` | `B/s` | `Number.bytesPerSecond` | 1.0 |
| Bit per second | `KDataRateUnit.BITS_PER_SECOND` | `bit/s` | `Number.bitsPerSecond` | 0.125 |

Both units have a matching bare `val` alias for use as a `valueAs`/`toString` target or as the `unit`
argument of a prefix `infix` function: `bytesPerSecond`, `bitsPerSecond`.

> **Byte-based base.** The base unit is a *byte* per second, consistent with the storage group (whose base
> is the byte). The networking-native bit/s (`bps`) is `0.125 B/s`; a "megabit per second" is
> `1 mega bitsPerSecond`. All the usual larger units (kB/s, MB/s, Mbit/s, KiB/s, …) come from the prefix
> DSL below rather than dedicated enum values.

```kotlin
import org.pcsoft.framework.kunit.datarate.*

val r = 100.bytesPerSecond
r.value                                     // 100.0 (normalized to B/s)
r.valueAs(KDataRateUnit.BITS_PER_SECOND)    // 800.0 (read back in bit/s)
r.valueAs(bitsPerSecond)                     // 800.0 (via the bare alias)
```

## Computing with the core units (storage & time)

This is the whole point of a constructed unit. A data rate *is* a storage amount divided by a time. KUnit
lets you move between the three quantities - storage, time and data rate - with plain `*` and `/`, and each
result is **strongly typed**. You never have to build or unwrap a raw `KMixedUnitInstance` yourself.

The four legal combinations and their result type:

| Expression | Result type | Meaning |
|---|---|---|
| `storage / time` | `KDataRateUnitInstance` | rate = amount / duration |
| `data rate * time` | `KStorageUnitInstance` | amount = rate × duration |
| `time * data rate` | `KStorageUnitInstance` | amount (commutative) |
| `storage / data rate` | `KTimeUnitInstance` | duration = amount / rate |

```kotlin
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.datarate.*

// --- core units -> data rate --------------------------------------------
val r = 100.bytes / 10.seconds            // KDataRateUnitInstance (NO .toDataRate() needed!)
r.value                                     // 10.0 (B/s)
r.valueAs(KDataRateUnit.BITS_PER_SECOND)    // 80.0

// The assignment target type does NOT convert anything - the operator already
// returns a KDataRateUnitInstance. Kotlin has no implicit conversions.
val explicit: KDataRateUnitInstance = 100.bytes / 10.seconds

// --- data rate -> storage (multiply by a time) --------------------------
val amount = r * 60.seconds               // KStorageUnitInstance
amount.value                                // 600.0 (B)
amount.valueAs(bytes)                        // 600.0
amount.valueAs(bits)                         // 4800.0 (read back in any storage unit)
60.seconds * r                            // same result (commutative)

// --- data rate -> time (divide a storage amount by it) ------------------
val time = 600.bytes / r                  // KTimeUnitInstance
time.value                                  // 60.0 (s)
time.valueAs(KTimeUnit.MINUTE)              // 1.0
```

!!! warning "Only a *pure* storage / time shape is a data rate"
    `KMixedUnitInstance.toDataRate()` requires exactly one storage term at exponent `+1` and one time term
    at exponent `-1`. A `B²` (storage squared), a `B·s⁻²`, or a `B·s` shape is not a data rate - the
    conversion throws `IllegalStateException` rather than silently returning a wrong value. Likewise,
    `storage + data rate` (different dimensions) is a compile error.

## Operators

```kotlin
import org.pcsoft.framework.kunit.datarate.*

// + / - : same group, automatic conversion between different data-rate units
val a = 1.bytesPerSecond + 8.bitsPerSecond   // KDataRateUnitInstance, 2 B/s
val b = 2.bytesPerSecond - 8.bitsPerSecond   // 1 B/s

// comparisons (by normalized B/s value)
1.bytesPerSecond > 4.bitsPerSecond           // true  (1 B/s > 0.5 B/s)
1.bytesPerSecond == 8.bitsPerSecond          // true  (same normalized value)

// * / / between two data rates escape to a KMixedUnitInstance (no longer a pure rate)
val squared = 10.bytesPerSecond * 2.bytesPerSecond // KMixedUnitInstance, units=[B^2, s^-2]
```

## Comparisons and equality

`==`, `!=`, `<`, `<=`, `>`, `>=` compare the normalized `value` (bytes per second) of two
`KDataRateUnitInstance`s. Since a data rate always has the same dimension, no exponent check is needed.

## SI prefixes and binary (IEC) prefixes

The data-rate group mirrors the [Storage](storage.md) group's prefix policy (its numerator is a storage
amount):

* Only the **non-diminishing** decimal SI prefixes (`deca` upward, factor >= 1) are offered. The
  diminishing ones (`deci`, `centi`, `milli`, …) do **not** exist - `5 milli bytesPerSecond` is a **compile
  error**, not a runtime failure.
* In addition to the decimal SI prefixes, the **binary IEC prefixes** (`kibi`, `mebi`, `gibi`, …, powers of
  1024, reused from `KStorageBinaryPrefix`) are available, so a rate can distinguish 1000 (`kilo`) from 1024
  (`kibi`).

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.storage.KStorageBinaryPrefix
import org.pcsoft.framework.kunit.storage.with
import org.pcsoft.framework.kunit.datarate.*

// Construction: "5 mega bytesPerSecond" -> KDataRateUnitInstance (direct, == 5_000_000.bytesPerSecond)
val download = 5 mega bytesPerSecond
download.value // 5000000.0

// Decimal vs binary: 1000 (kilo) != 1024 (kibi)
(1 kilo bytesPerSecond).value // 1000.0
(1 kibi bytesPerSecond).value // 1024.0

// Reading a value back using a scaled whole-rate target
val r = 4096.bytesPerSecond
r.valueAs(KUnitPrefix.KILO with bytesPerSecond)              // 4.096  (kB/s)
r.valueAs(KStorageBinaryPrefix.KIBI with bytesPerSecond)     // 4.0    (KiB/s)
```

You can also read a data rate back as an explicit **storage-per-time pair** (two targets):

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.storage.KStorageUnit
import org.pcsoft.framework.kunit.time.KTimeUnit
import org.pcsoft.framework.kunit.datarate.*

val r = 5000.bytesPerSecond
r.valueAs(KUnitPrefix.KILO with KStorageUnit.BYTE, KTimeUnit.SECOND)   // 5.0 (kB per s)
r.toString(KUnitPrefix.KILO with KStorageUnit.BYTE, KTimeUnit.SECOND)  // "5.0 kB*s^-1"
```

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.storage.KStorageBinaryPrefix
import org.pcsoft.framework.kunit.storage.with
import org.pcsoft.framework.kunit.datarate.*

10.bytesPerSecond.toString()                                    // "10.0 B/s" (base unit)
(100.bytes / 10.seconds).toString(KDataRateUnit.BITS_PER_SECOND) // "80.0 bit/s"
4096.bytesPerSecond.toString(KStorageBinaryPrefix.KIBI with bytesPerSecond) // "4.0 KiB/s"
```
