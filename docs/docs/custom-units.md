# Adding Custom Units

kunit ships several unit groups today ([Distance](units/distance.md), [Time](units/time.md),
[Storage](units/storage.md), [Speed](units/speed.md), [Data Rate](units/datarate.md)), but the whole engine
(`KUnit`, `KMixedUnitInstance`, the `of`/`into` verbs, prefix builders) is generic and group-independent.
Adding a new physical quantity means following the same pattern. This page walks through adding a
demonstrative **Mass** group (`org.pcsoft.framework.kunit.mass`) — a plain, one-dimensional group modeled on
the storage group.

## 1. Create the sub-package and the `KUnit` enum

Every unit group gets its own sub-package under `org.pcsoft.framework.kunit`, and its units are declared as
an `enum class` implementing `KUnit`. `baseValue` is the conversion factor to the group's base unit -
the base unit itself has `baseValue == 1.0`.

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KUnit

enum class KMassUnit(override val symbol: String, override val baseValue: Double) : KUnit {
    /** Kilogram, the SI base unit of mass; [baseValue] = 1.0 by definition. */
    KILOGRAM("kg", 1.0),

    /** Gram, 1 g = 0.001 kg. */
    GRAM("g", 0.001),

    /** International avoirdupois pound, 1 lb = 0.45359237 kg. */
    POUND("lb", 0.45359237),

    /** International avoirdupois ounce, 1 oz = 0.028349523125 kg. */
    OUNCE("oz", 0.028349523125);

    companion object {
        /** The base unit of the mass group; all internal values of [KMassUnitInstance] are normalized to this unit. */
        val BASE: KMassUnit = KILOGRAM
    }
}
```

## 2. Create the wrapper class

The wrapper (`KMassUnitInstance`) encapsulates a `KMixedUnitInstance` by **delegation** (`KUnitMeasurable by
instance`) and implements `KUnitInstance<KMassUnitInstance>`. It hand-writes only the `KUnitInstance`-only
members (`plus`/`minus`/`compareTo`) plus the `scaledBy` override (which backs `of`) and
`equals`/`hashCode`/`toString`. There is **no** `valueAs`/`toString(target)` - reading is the group-agnostic
`into` verb. Copy the shape of `KStorageUnitInstance`.

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitInstance
import org.pcsoft.framework.kunit.KUnitMeasurable
import org.pcsoft.framework.kunit.KUnitTerm

class KMassUnitInstance internal constructor(internal val instance: KMixedUnitInstance) :
    KUnitMeasurable by instance, KUnitInstance<KMassUnitInstance> {

    /** Backs `of`: scales the value (kilograms), returning the same type. */
    override fun scaledBy(factor: Double): KMassUnitInstance = massUnitInstanceOf(value * factor)

    override operator fun plus(other: KMassUnitInstance): KMassUnitInstance = massUnitInstanceOf(value + other.value)
    override operator fun minus(other: KMassUnitInstance): KMassUnitInstance = massUnitInstanceOf(value - other.value)
    override operator fun compareTo(other: KMassUnitInstance): Int = value.compareTo(other.value)

    override fun equals(other: Any?): Boolean = other is KMassUnitInstance && value == other.value
    override fun hashCode(): Int = value.hashCode()
    override fun toString(): String = instance.toString()
}

/** Builds a [KMassUnitInstance] from a value already expressed in kilograms ([KMassUnit.BASE]). */
internal fun massUnitInstanceOf(value: Double): KMassUnitInstance =
    KMassUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KMassUnit.BASE, 1))))

/** Converts a pure-mass [KMixedUnitInstance] back into a [KMassUnitInstance], normalizing to [KMassUnit.BASE]. */
fun KMixedUnitInstance.toMass(): KMassUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KMassUnit) {
        "KMixedUnitInstance $this does not represent a pure mass value (expected exactly one term of a KMassUnit)"
    }
    return massUnitInstanceOf(value * unit.baseValue)
}
```

## 3. Add value-1 bare tokens and prefix-builder properties

Split the DSL vocabulary into two files, per the project convention: the value-1 bare tokens go into
`K...UnitBareValues.kt`, and the prefix-builder property extensions go into `K...UnitExtensions.kt`. Together
they let callers write `5 of kilograms` or `5 of kilo.grams` and read back with `into`.

`KMassUnitBareValues.kt`:

```kotlin
package org.pcsoft.framework.kunit.mass

/** 1 kilogram ([KMassUnit.KILOGRAM]). */
val kilograms: KMassUnitInstance = massUnitInstanceOf(KMassUnit.KILOGRAM.baseValue)

/** 1 gram ([KMassUnit.GRAM]). */
val grams: KMassUnitInstance = massUnitInstanceOf(KMassUnit.GRAM.baseValue)

/** 1 pound ([KMassUnit.POUND]). */
val pounds: KMassUnitInstance = massUnitInstanceOf(KMassUnit.POUND.baseValue)

/** 1 ounce ([KMassUnit.OUNCE]). */
val ounces: KMassUnitInstance = massUnitInstanceOf(KMassUnit.OUNCE.baseValue)
```

`KMassUnitExtensions.kt` (mass accepts any magnitude, so the properties hang on the common base
`KPrefixBuilder`):

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KPrefixBuilder

private fun prefixedMass(builder: KPrefixBuilder, unit: KMassUnit): KMassUnitInstance =
    massUnitInstanceOf(builder.prefix.factor * unit.baseValue)

/** Prefixed kilograms, e.g. `kilo.kilograms`. */
val KPrefixBuilder.kilograms: KMassUnitInstance get() = prefixedMass(this, KMassUnit.KILOGRAM)

/** Prefixed grams, e.g. `milli.grams` = 1 mg. */
val KPrefixBuilder.grams: KMassUnitInstance get() = prefixedMass(this, KMassUnit.GRAM)

/** Prefixed pounds. */
val KPrefixBuilder.pounds: KMassUnitInstance get() = prefixedMass(this, KMassUnit.POUND)

/** Prefixed ounces. */
val KPrefixBuilder.ounces: KMassUnitInstance get() = prefixedMass(this, KMassUnit.OUNCE)
```

That's it - this already gives you full `+`, `-`, `*`, `/`, comparisons, the SI prefix builders
(`5 of milli.grams`), and `toUnit()`/`toMass()` round-tripping for free.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mass.*

val a = 500 of grams
val b = 2 of pounds
val total = a + b            // KMassUnitInstance, normalized to kilograms
println(total into kilograms)
println(total into grams)

val heavier = b > a          // true
```

## 4. (Optional) Add special/derived units

If your group has commonly used named units bound to a specific scaling (like hectare for area), add them as
named value-1 instances — no separate target type is needed:

```kotlin
package org.pcsoft.framework.kunit.mass

/** 1 metric ton (1000 kg). */
val tonnes: KMassUnitInstance = massUnitInstanceOf(1000.0)
```

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mass.*

println((2500 of grams) into tonnes) // 0.0025
```

## 5. Combine with other groups

Because everything ultimately funnels through the generic `KMixedUnitInstance` engine, your new group
immediately composes with any other group via `*`/`/` - see [Mixed Units](mixed-units.md) for the rules. For
a strongly-typed cross-group result (like `mass / volume = density`), add typed operator extensions in a
`K...UnitOperators.kt`, mirroring `KSpeedUnitOperators.kt`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.mass.*

// density = mass / volume (generic KMixedUnitInstance: [KILOGRAM^1, METER^-3])
val density = (5 of kilograms) / (2 of liters)
```

## 6. Naming and testing checklist

- All public types start with `K` (`KMassUnit`, `KMassUnitInstance`, ...); the value-1 bare tokens and the
  prefix-builder property extensions (`kilograms`, `grams`, ...) are exempt and stay language-natural.
- Cover the group with the parameterized cross-matrix test procedure, built through `of`/`into` (never the
  raw enum): unit → unit conversion, one method per operator and per comparison over every unit pair, the
  prefix-builder matrix, `of` type-preservation, and `into` error cases — see the "Parameterized
  cross-matrix test procedure" section in `CLAUDE.md`.
- Document every public member in English, in Markdown, with examples where useful - especially operators.
- If the group is magnitude-restricted (like storage, which rejects diminishing prefixes), hang its unit
  properties on `KAugmentingPrefixBuilder`/`KDiminishingPrefixBuilder` instead of the base `KPrefixBuilder`,
  so the disallowed prefixes are a **compile error**.
```
