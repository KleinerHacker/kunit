# Time

Package: `org.pcsoft.framework.kunit.time`
Base unit: **second** (`KTimeUnit.BASE == KTimeUnit.SECOND`)

`KTimeUnitInstance` is a 100 % wrapper around [`java.time.Duration`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/Duration.html):
the `Duration` is the single source of truth (nanosecond-exact), and the full `Duration` API is
forwarded. On top of that it offers the same surface as every other "pure" unit wrapper
(`value`/`valueAs`/`+`/`-`/`*`/`/`/`toString`/`toKMixedUnitInstance`), so a time value plugs into the generic
mixed-unit engine (e.g. `length / time` = speed). The value is always stored normalized to seconds.

Because a `Duration` only ever represents a plain duration, a time value is always exponent 1 - there is
no time² or 1/time wrapper (multiplying/dividing "escapes" to a raw `KMixedUnitInstance`, exactly like
length). Consequently `KMixedUnitInstance.toKTimeUnit()` accepts only a single `KTimeUnit` term **at exponent 1**.

## Units

| Unit | Enum value | Symbol | Creator | 1 unit in seconds |
|---|---|---|---:|---:|
| Second | `KTimeUnit.SECOND` | `s` | `Number.seconds` | 1.0 |
| Minute | `KTimeUnit.MINUTE` | `min` | `Number.minutes` | 60.0 |
| Hour | `KTimeUnit.HOUR` | `h` | `Number.hours` | 3600.0 |
| Day | `KTimeUnit.DAY` | `d` | `Number.days` | 86 400.0 |

Only physical time scales are modeled; calendar-based units (week, year) are intentionally omitted, since
they are defined by calendars rather than by a fixed physical quantity.

Every unit above has a matching bare `val` alias for use as a `valueAs`/`toString` target or as the `unit`
argument of a prefix `infix` function: `seconds`, `minutes`, `hours`, `days`.

Sub-second scales (millisecond, microsecond, nanosecond, ...) are **not** dedicated units - they are
reached generically via the SI prefixes on `second` (see [SI prefixes](#si-prefixes) below).

```kotlin
import org.pcsoft.framework.kunit.time.*

val t = 2.hours
t.value                      // 7200.0 (normalized to seconds)
t.valueAs(KTimeUnit.HOUR)    // 2.0 (read back in hours)
t.valueAs(minutes)           // 120.0
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.time.*

// + / - : same group, automatic conversion between different time units (exact Duration arithmetic)
val a = 1.hours + 30.minutes   // KTimeUnitInstance, normalized to seconds (5400.0)
val b = 2.hours - 30.minutes

// comparisons
2.hours > 90.minutes            // true
1.hours == 60.minutes           // true (same normalized value)

// * / / : always allowed, produces a KMixedUnitInstance with a new exponent
val secondsSquared = 3.seconds * 4.seconds   // KMixedUnitInstance: value=12.0, units=[SECOND^2]
val ratio = 10.seconds / 2.seconds           // KMixedUnitInstance: value=5.0, dimensionless
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
import org.pcsoft.framework.kunit.time.*

val t = 90.minutes
t.toDuration()                       // PT1H30M
Duration.ofMinutes(90).toKTimeUnit() // KTimeUnitInstance, valueAs(HOUR) == 1.5

// forwarded mutators return KTimeUnitInstance
t.plusHours(1).valueAs(KTimeUnit.HOUR) // 2.5
t.negated().isNegative()               // true

// forwarded query methods pass through
t.toHours()      // 1
t.toMinutesPart() // 30
t.dividedBy(30.minutes) // 3
```

## SI prefixes

Any `KTimeUnit` can be combined with any of the 24 SI prefixes (`KUnitPrefix`, root package, Quetta/Q to
Quecto/q) using the time-group `infix` construction functions (which return a `KTimeUnitInstance` directly)
and `with` (for `valueAs`/`toString` targets). This is how sub-second scales are expressed. Note that the
`Duration` backing limits the representable range (see the note below), so extreme prefixes on a multi-second
base are not representable:

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.time.*

// Construction: "5 milli seconds" -> KTimeUnitInstance (direct)
val fiveMillis = 5 milli seconds
fiveMillis.value // 0.005 (seconds)

// Reading back a value using a prefixed target
val t = 2.hours
t.valueAs(KUnitPrefix.MILLI with KTimeUnit.SECOND)  // 7 200 000.0 (ms)
t.toString(KUnitPrefix.MILLI with KTimeUnit.SECOND) // "7200000.0 ms"
```

!!! note "Duration range"
    Because the value is backed by `java.time.Duration` (whole seconds stored as a `Long`, nanosecond
    resolution), a `KTimeUnitInstance` can only faithfully represent magnitudes within roughly
    `[1 ns, Long.MAX seconds]` (≈ 292 billion years). Extreme prefixes such as `quetta` applied to days
    exceed this range, and sub-nanosecond values round to zero. The generic `KMixedUnitInstance`/prefix layer
    itself is `Double`-based and unaffected - only the conversion into the Duration-backed wrapper is
    range-limited.

## toString formatting

```kotlin
import org.pcsoft.framework.kunit.time.*

2.hours.toString()               // "7200.0 s" (base unit representation)
2.hours.toString(KTimeUnit.HOUR) // "2.0 h"
2.hours.toString(minutes)        // "120.0 min"
```

## Mixing with other units

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.length.*
import org.pcsoft.framework.kunit.time.*

val speed = 10.meters / 1.seconds.toKMixedUnitInstance()          // KMixedUnitInstance, units=[METER^1, SECOND^-1]
speed.toString(KUnitPrefix.KILO with KLengthUnit.METER, KTimeUnit.HOUR) // "36.0 km*h^-1"

// multiplying speed back by a time recovers a pure length
val distance = speed * 2.seconds.toKMixedUnitInstance()
distance.toKLengthUnit().value // 20.0
```
