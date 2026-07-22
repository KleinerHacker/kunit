# Information Technology — Overview

Packages: `org.pcsoft.framework.kunit.storage`, `…datarate`

Information technology deals with **digital data amounts** and how fast they move. KUnit models a stored
amount as a **native** base quantity (storage, in bytes) and the throughput as a quantity **constructed**
from it (data rate = storage per time), so the everyday "how long does this download take?" question
becomes a typed expression.

## Units in this topic

| Unit | Type | Base unit | Page |
|---|---|---|---|
| Storage | native | byte (`B`) | [Storage](storage.md) |
| Data Rate | constructed | byte per second (`B/s`) | [Data Rate](datarate.md) |

Both groups share the same prefix policy: **no diminishing prefixes** (a fraction of a bit is meaningless)
and, besides the decimal SI prefixes (`kilo` = 1000), a second **binary (IEC)** family (`kibi` = 1024).

## How the quantities relate

| Expression | Result | Formula |
|---|---|---|
| `storage / time` | Data Rate | `r = amount / t` |
| `data rate * time` | Storage | `amount = r · t` |
| `time * data rate` | Storage | `amount = r · t` (commutative) |
| `storage / data rate` | Time | `t = amount / r` |

## Worked example — download time

A **500 MB** file is downloaded over a **10 MB/s** link. The time is `t = amount / rate`; multiplying the
rate by that time reproduces the amount `amount = r · t`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.datarate.*

val amount = 500 of mega.bytes
val rate   = 10 of mega.bytes / seconds        // KDataRateUnitInstance, 10 MB/s

val time = amount / rate                        // KTimeUnitInstance
time into seconds                               // 50.0 (s)

val transferred = rate * (50 of seconds)        // KStorageUnitInstance
transferred into mega.bytes                     // 500.0 (MB)
```

## Worked example — decimal vs. binary

The same numeric amount reads differently against a decimal (`kB`) and a binary (`KiB`) template — 1000
versus 1024:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.storage.*

val size = 4096 of bytes
size into kilo.bytes    // 4.096 (kB, decimal 1000)
size into kibi.bytes    // 4.0   (KiB, binary 1024)
```

## Printing a value (`toString`)

`toString()` renders a value in its group's **base unit** (value + symbol); for any other unit, read it
with `into` inside a string template and append the symbol yourself:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.storage.*
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.datarate.*

val r = (10 of bytes) / (1 of seconds)   // KDataRateUnitInstance
r.toString()                             // "10.0 B/s" (base unit)
"${(4096 of bytes / seconds) into (kibi.bytes / seconds)} KiB/s" // "4.0 KiB/s"
```

## Notation

The table shows the field's core relations mathematically versus in Kotlin with KUnit. Exponents use
Unicode superscripts (`⁻¹`), `·` denotes multiplication and `/` a fraction.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `r = amount / t` | `(500 of mega.bytes) / (50 of seconds)` | data rate from amount ÷ time |
| `amount = r · t` | `rate * (50 of seconds)` | amount from rate × time |
| `t = amount / r` | `amount / rate` | time from amount ÷ rate |
| `1 kB = 1000 B` | `kilo.bytes` | decimal-prefixed byte |
| `1 KiB = 1024 B` | `kibi.bytes` | binary-prefixed byte |

## Where to go next

* [Storage](storage.md) — the native byte group, decimal and binary prefixes.
* [Data Rate](datarate.md) — storage per time, and the storage ↔ time ↔ rate operators.
