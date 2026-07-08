# Mixed Units

A **mixed unit** (German: *Mischeinheit*) is a value composed of several `KUnit`s, each raised to its own
exponent, e.g. `m^1 * s^-1` for a speed, or `m^1 * kg^1 * s^-2` for a force. In kunit this is represented by
the generic `KMixedUnitInstance` class.

While the group-specific wrapper classes (like `KLengthUnitInstance`, see [Predefined Units](units/distance.md))
are convenient for working with a single physical dimension, `KMixedUnitInstance` is what you reach for once you
need to combine units from **different** groups, or when you don't want the automatic same-group conversion
that the wrapper classes provide.

## Anatomy

```kotlin
data class KUnitTerm(val unit: KUnit, val exponent: Int)

class KMixedUnitInstance(value: Number, val units: List<KUnitTerm>)
```

- `value` is the normalized `Double` magnitude, always relative to exactly the units and exponents listed in
  `units` - unlike the group wrappers, `KMixedUnitInstance` performs **no** normalization to a group's base unit.
- `units` is the list of `(KUnit, exponent)` pairs describing the physical dimension.

Every "pure" unit exposes a `toUnit()` extension to convert to this generic representation:

```kotlin
import org.pcsoft.framework.kunit.distance.*

val d = 5 of meters
val mixed = d.toUnit() // KMixedUnitInstance: value=5.0, units=[METER^1]
```

## Multiplication and division

`*` and `/` are **always** allowed between two `KMixedUnitInstance`s - there is no dimensional restriction, since
multiplying/dividing units is always physically meaningful.

- `*` adds up exponents of matching units, and simply carries over any unit that only exists on one side.
- `/` subtracts the right-hand side's exponents from matching units (and negates the exponent for units that
  only exist on the right-hand side).
- A resulting exponent of `0` removes that unit from the result entirely.

```kotlin
import org.pcsoft.framework.kunit.distance.*

val distance = (10 of meters).toUnit()   // units=[METER^1]
val width = (4 of meters).toUnit()       // units=[METER^1]

val area = distance * width                     // value=40.0, units=[METER^2]
val backToLength = area / width                 // value=10.0, units=[METER^1]
```

Mixing two different unit groups (e.g. length and, once available, time) works exactly the same way and
produces a genuinely mixed unit:

```kotlin
import org.pcsoft.framework.kunit.time.seconds

val distance = (100 of meters).toUnit()
val time = (10 of seconds).toUnit()

val speed = distance / time // value=10.0, units=[METER^1, SECOND^-1]
```

## Addition and subtraction

Unlike `*`/`/`, `+` and `-` are only allowed between two `KMixedUnitInstance`s that describe the **same physical
dimension**: for every term on one side there must be exactly one term on the other side belonging to the
same unit group (e.g. all `KDistanceUnit` values) with the same exponent (order-independent). The `KUnit`s
themselves do **not** need to be identical - matching terms are automatically converted via normalization,
the same way the group-specific wrapper classes (`KLengthUnitInstance`, etc.) do it for "pure" units. The
result is expressed in the left-hand operand's `units`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.distance.miles

val a = (5 of meters).toUnit()
val b = (3 of meters).toUnit()
(a + b).value // 8.0

val c = (3 of miles).toUnit()
(a + c).value // 4832.032 (3 miles converted to meters, then added), units=[METER^1]
```

Mismatched unit groups or mismatched exponents still fail:

```kotlin
import org.pcsoft.framework.kunit.time.seconds

a + (3 of seconds).toUnit()       // throws IllegalStateException: no matching unit group for a time term
a + ((2 of meters) pow 2).toUnit() // throws IllegalStateException: mismatched exponents (1 vs 2)
```

Use `hasSameUnits` to check for an **exact** match (same `KUnit`s, not just the same group) up front:

```kotlin
val x = (5 of meters).toUnit()
val y = (3 of meters).toUnit()
x.hasSameUnits(y) // compares the (unit -> exponent) signature, order-independent
```

## Reading values

`into` reads the value in a target unit template (a bare token, a prefixed builder template, or a special
value-1 instance), returning a plain `Double`. Both sides must describe the same physical dimension. There is
no `valueAs` and no custom-unit `toString`; format a specific unit as `"${v into kilo.meters} km"`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds

val speed = (10 of meters) / (1 of seconds)

speed into (kilo.meters / hours)   // 36.0 (km/h)

val area = (200 of meters) * (50 of meters)
area into hectares                 // 1.0
```

The default (no-argument) `toString()` always uses each term's own `KUnit.symbol`, joined with `*`, e.g.
`"5.0 m*s^-1"`.

## Mixing pure units and mixed units

Every pure unit wrapper class supports `*`/`/` directly against a `KMixedUnitInstance`, so you rarely need to call
`toUnit()` explicitly for these operators:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*

val distance = 100 of meters        // KLengthUnitInstance
val mixed = distance.toUnit()       // KMixedUnitInstance

val combined = distance * mixed              // KMixedUnitInstance: METER^2
```

## Converting back to a pure unit

Once a `KMixedUnitInstance` again represents exactly one term of a single unit group, it can be converted back
into that group's wrapper class via the group-specific `toXxxUnit()` extension (e.g. `toDistance()`):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.seconds

val speed = (10 of meters) / (2 of seconds)    // KSpeedUnitInstance
val distanceAgain = speed.toUnit() * (2 of seconds).toUnit() // units=[METER^1]
distanceAgain.toDistance().value               // 10.0

val area = (200 of meters) * (50 of meters)    // KAreaUnitInstance
area.toUnit().toDistance().value               // 10000.0 (an area, exponent 2)
```

If the `KMixedUnitInstance` does **not** consist of exactly one term of that group (e.g. it's still a mixed
length/time value), the conversion throws `IllegalStateException`.

The same narrowing is available **directly on a distance value** (not only on `KMixedUnitInstance`): a
general `KDistanceUnitInstance` — or any leaf — can be narrowed to a specific dimension with `toLength()`,
`toArea()` or `toVolume()`, which are exponent-checked and throw `IllegalStateException` on a mismatch:

```kotlin
val area = (200 of meters) * (50 of meters)  // KAreaUnitInstance (exponent 2)
area.toArea().value                          // 10000.0
area.toDistance().toArea().value             // 10000.0 (widened, then narrowed back)
area.toLength()                              // IllegalStateException (exponent 2, not 1)
```
