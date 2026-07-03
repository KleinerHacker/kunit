# Mixed Units

A **mixed unit** (German: *Mischeinheit*) is a value composed of several `KUnit`s, each raised to its own
exponent, e.g. `m^1 * s^-1` for a speed, or `m^1 * kg^1 * s^-2` for a force. In kunit this is represented by
the generic `KUnitInstance` class.

While the group-specific wrapper classes (like `KLengthUnitInstance`, see [Predefined Units](units/length.md))
are convenient for working with a single physical dimension, `KUnitInstance` is what you reach for once you
need to combine units from **different** groups, or when you don't want the automatic same-group conversion
that the wrapper classes provide.

## Anatomy

```kotlin
data class KUnitTerm(val unit: KUnit, val exponent: Int)

class KUnitInstance(value: Number, val units: List<KUnitTerm>)
```

- `value` is the normalized `Double` magnitude, always relative to exactly the units and exponents listed in
  `units` - unlike the group wrappers, `KUnitInstance` performs **no** normalization to a group's base unit.
- `units` is the list of `(KUnit, exponent)` pairs describing the physical dimension.

Every "pure" unit exposes a `toKUnitInstance()` extension to convert to this generic representation:

```kotlin
import org.pcsoft.framework.kunit.length.*

val d = 5.meters()
val mixed = d.toKUnitInstance() // KUnitInstance: value=5.0, units=[METER^1]
```

## Multiplication and division

`*` and `/` are **always** allowed between two `KUnitInstance`s - there is no dimensional restriction, since
multiplying/dividing units is always physically meaningful.

- `*` adds up exponents of matching units, and simply carries over any unit that only exists on one side.
- `/` subtracts the right-hand side's exponents from matching units (and negates the exponent for units that
  only exist on the right-hand side).
- A resulting exponent of `0` removes that unit from the result entirely.

```kotlin
import org.pcsoft.framework.kunit.length.*

val distance = 10.meters().toKUnitInstance()   // units=[METER^1]
val width = 4.meters().toKUnitInstance()       // units=[METER^1]

val area = distance * width                     // value=40.0, units=[METER^2]
val backToLength = area / width                 // value=10.0, units=[METER^1]
```

Mixing two different unit groups (e.g. length and, once available, time) works exactly the same way and
produces a genuinely mixed unit:

```kotlin
// Once a "time" unit group exists, following the pattern in "Adding Custom Units":
val distance = 100.meters().toKUnitInstance()
val time = 10.seconds().toKUnitInstance()

val speed = distance / time // value=10.0, units=[METER^1, SECOND^-1]
```

## Addition and subtraction

Unlike `*`/`/`, `+` and `-` are only allowed between two `KUnitInstance`s that describe the **same physical
dimension**: for every term on one side there must be exactly one term on the other side belonging to the
same unit group (e.g. all `KLengthUnit` values) with the same exponent (order-independent). The `KUnit`s
themselves do **not** need to be identical - matching terms are automatically converted via normalization,
the same way the group-specific wrapper classes (`KLengthUnitInstance`, etc.) do it for "pure" units. The
result is expressed in the left-hand operand's `units`.

```kotlin
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitTerm
import org.pcsoft.framework.kunit.length.KLengthUnit

val a = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
val b = KUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
(a + b).value // 8.0

val c = KUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.MILE, 1)))
(a + c).value // 4832.032 (3 miles converted to meters, then added), units=[METER^1]
```

Mismatched unit groups or mismatched exponents still fail:

```kotlin
val time = KUnitInstance(3.0, listOf(KUnitTerm(TimeUnit.SECOND, 1)))
a + time // throws IllegalStateException: no matching unit group for TimeUnit.SECOND

val area = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 2)))
a + area // throws IllegalStateException: mismatched exponents (1 vs 2)
```

Use `hasSameUnits` to check for an **exact** match (same `KUnit`s, not just the same group) up front:

```kotlin
val a = KUnitInstance(5.0, listOf(KUnitTerm(KLengthUnit.METER, 1)))
val b = KUnitInstance(3.0, listOf(KUnitTerm(KLengthUnit.METER, 1), KUnitTerm(KLengthUnit.METER, 0)))
a.hasSameUnits(b) // compares the (unit -> exponent) signature, order-independent
```

!!! note
    The examples above and below that reference `seconds()`/`TimeUnit` illustrate what a **second** unit
    group would look like combined with length - kunit currently ships only the `length` group (see
    [Predefined Units](units/length.md)). Follow [Adding Custom Units](custom-units.md) to add your own.

## Reading and formatting values

`valueAs` converts the value into an arbitrary set of target units - each target must match exactly one term
by unit group (and, for derived units, by exponent). `toString` overloads do the same but also render the
symbols.

```kotlin
import org.pcsoft.framework.kunit.KUnitPrefix
import org.pcsoft.framework.kunit.with
import org.pcsoft.framework.kunit.length.*

val speed = 10.meters().toKUnitInstance() / 1.seconds().toKUnitInstance()

speed.valueAs(KUnitPrefix.KILO with KLengthUnit.METER, TimeUnit.HOUR) // 36.0 (km/h)
speed.toString(KUnitPrefix.KILO with KLengthUnit.METER, TimeUnit.HOUR) // "36.0 km*h^-1"

val area = 200.meters().toKUnitInstance() * 50.meters().toKUnitInstance()
area.valueAs(KLengthDerivedUnit.HECTARE) // 1.0
```

The default (no-argument) `toString()` always uses each term's own `KUnit.symbol`, joined with `*`, e.g.
`"5.0 m*s^-1"`.

## Mixing pure units and mixed units

Every pure unit wrapper class supports `*`/`/` directly against a `KUnitInstance`, so you rarely need to call
`toKUnitInstance()` explicitly for these operators:

```kotlin
import org.pcsoft.framework.kunit.length.*

val distance = 100.meters()                 // KLengthUnitInstance
val mixed = distance.toKUnitInstance()       // KUnitInstance

val combined = distance * mixed              // KUnitInstance: METER^2
```

## Converting back to a pure unit

Once a `KUnitInstance` again represents exactly one term of a single unit group, it can be converted back
into that group's wrapper class via the group-specific `toXxxUnit()` extension (e.g. `toKLengthUnit()`):

```kotlin
import org.pcsoft.framework.kunit.length.*

val speed = 10.meters() / 2.seconds()          // KUnitInstance (once time exists)
val distanceAgain = speed.toKUnitInstance() * 2.seconds() // units=[METER^1]
distanceAgain.toKLengthUnit().value             // 10.0

val area = 200.meters() * 50.meters()           // units=[METER^2]
area.toKLengthUnit().value                        // 10000.0 (an area, exponent 2)
```

If the `KUnitInstance` does **not** consist of exactly one term of that group (e.g. it's still a mixed
length/time value), the conversion throws `IllegalStateException`.
