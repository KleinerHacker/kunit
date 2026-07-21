# Time

Package: `org.pcsoft.framework.kunit.time`
Base unit: **second** (`KTimeUnit.BASE == KTimeUnit.SECOND`)

`KTimeUnitInstance` is a 100 % wrapper around [`java.time.Duration`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/Duration.html):
the `Duration` is the single source of truth (nanosecond-exact), and the full `Duration` API is
forwarded. On top of that it offers the same surface as every other "pure" unit wrapper
(`value`/`+`/`-`/`*`/`/`/`toString`/`toUnit` plus the `of`/`into` verbs), so a time value plugs into the
generic mixed-unit engine (e.g. `length / time` = speed). The value is always stored normalized to seconds.

Because a `Duration` only ever represents a plain duration, a time value is always exponent 1 - there is
no time² or 1/time wrapper (multiplying/dividing "escapes" to a raw `KMixedUnitInstance`, exactly like
length). Consequently `KMixedUnitInstance.toTime()` accepts only a single `KTimeUnit` term **at exponent 1**.

## Units

| Unit | Enum value | Symbol | Token | 1 unit in seconds |
|---|---|---|---:|---:|
| Second | `KTimeUnit.SECOND` | `s` | `seconds` | 1.0 |
| Minute | `KTimeUnit.MINUTE` | `min` | `minutes` | 60.0 |
| Hour | `KTimeUnit.HOUR` | `h` | `hours` | 3600.0 |
| Day | `KTimeUnit.DAY` | `d` | `days` | 86 400.0 |

Only physical time scales are modeled; calendar-based units (week, year) are intentionally omitted, since
they are defined by calendars rather than by a fixed physical quantity. Each `Token` is a value-1
`KTimeUnitInstance` used with `of` (build) and `into` (read).

Sub-second scales (millisecond, microsecond, nanosecond, ...) are **not** dedicated units - they are
reached generically via the SI prefix builders on `seconds` (see [SI prefixes](#si-prefixes) below).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.time.*

val t = 2 of hours
t.value          // 7200.0 (normalized to seconds)
t into hours     // 2.0 (read back in hours)
t into minutes   // 120.0
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.time.*

// + / - : same group, automatic conversion between different time units (exact Duration arithmetic)
val a = (1 of hours) + (30 of minutes)   // KTimeUnitInstance, normalized to seconds (5400.0)
val b = (2 of hours) - (30 of minutes)

// comparisons
(2 of hours) > (90 of minutes)           // true
(1 of hours) == (60 of minutes)          // true (same normalized value)

// * / / : always allowed, produces a KMixedUnitInstance with a new exponent
val secondsSquared = (3 of seconds) * (4 of seconds)   // KMixedUnitInstance: value=12.0, units=[SECOND^2]
val ratio = (10 of seconds) / (2 of seconds)           // KMixedUnitInstance: value=5.0, dimensionless
```

## Comparisons and equality

`==`, `!=`, `<`, `<=`, `>`, `>=` compare two `KTimeUnitInstance`s by their underlying `Duration`
(nanosecond-exact). Since a time value is always exponent 1, there is no exponent-mismatch error as there
is for length areas/volumes.

## The `java.time.Duration` wrapper

`KTimeUnitInstance` is a drop-in facade over `Duration`: obtain the wrapped `Duration`, wrap an existing
one, and use the forwarded `Duration` methods directly (those returning a `Duration` return a
`KTimeUnitInstance`; query methods pass through).

```kotlin
import java.time.Duration
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.time.*

val t = 90 of minutes
t.toDuration()                  // PT1H30M
Duration.ofMinutes(90).toTime() into hours // 1.5

// forwarded mutators return KTimeUnitInstance
t.plusHours(1) into hours       // 2.5
t.negated().isNegative()        // true

// forwarded query methods pass through
t.toHours()             // 1
t.toMinutesPart()       // 30
t.dividedBy(30 of minutes) // 3
```

## SI prefixes

Any time unit can be combined with any of the 24 SI prefix **builders** (`kilo`, `milli`, `micro`, …; root
package) via property access, producing a value-1 template for `of`/`into`. This is how sub-second scales
are expressed. Note that the `Duration` backing limits the representable range (see the note below), so
extreme prefixes on a multi-second base are not representable:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.time.*

// Construction: "5 of milli.seconds" -> KTimeUnitInstance
val fiveMillis = 5 of milli.seconds
fiveMillis.value // 0.005 (seconds)

// Reading back a value in a prefixed unit
val t = 2 of hours
t into milli.seconds  // 7 200 000.0 (ms)
```

!!! note "Duration range"
    Because the value is backed by `java.time.Duration` (whole seconds stored as a `Long`, nanosecond
    resolution), a `KTimeUnitInstance` can only faithfully represent magnitudes within roughly
    `[1 ns, Long.MAX seconds]` (≈ 292 billion years). Extreme prefixes such as `quetta` applied to days
    exceed this range, and sub-nanosecond values round to zero. The generic `KMixedUnitInstance`/prefix layer
    itself is `Double`-based and unaffected - only the conversion into the Duration-backed wrapper is
    range-limited.

## toString formatting

Only the base-unit `toString()` exists; format a specific unit via `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.time.*

(2 of hours).toString()          // "7200.0 s" (base unit representation)
"${(2 of hours) into hours} h"   // "2.0 h"
"${(2 of hours) into minutes} min" // "120.0 min"
```

## Mixing with other units

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*

val speed = (10 of meters) / (1 of seconds)  // KSpeedUnitInstance
speed into (kilo.meters / hours)             // 36.0 (km/h)

// multiplying speed back by a time recovers a pure length
val distance = speed * (2 of seconds)
distance into meters // 20.0
```

Two pure units of groups **without** a dedicated cross-group operator (e.g. `(2 of hours) * (5 of bytes)`)
combine directly into a `KMixedUnitInstance`, with no `.toUnit()` needed.

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `s` | `seconds` | time, base unit (second) |
| `min` | `minutes` | minute |
| `h` | `hours` | hour |
| `ms` | `milli.seconds` | prefixed time (millisecond) |
| `s⁻¹` | `seconds pow -1` | inverse time (escapes to a mixed unit) |
