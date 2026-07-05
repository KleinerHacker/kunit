# Adding Custom Units

kunit ships one unit group today ([Distance](units/distance.md)), but the whole engine (`KUnit`, `KMixedUnitInstance`,
prefixes, derived units) is generic and group-independent. Adding a new physical quantity means following
the same pattern the `length` package already establishes. This page walks through adding a demonstrative
**Mass** group (`org.pcsoft.framework.kunit.mass`) from scratch.

## 1. Create the sub-package and the `KUnit` enum

Every unit group gets its own sub-package under `org.pcsoft.framework.kunit`, and its units are declared as
an `enum class` implementing `KUnit`. `baseValue` is the conversion factor to the group's base unit -
the base unit itself has `baseValue == 1.0`.

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KUnit

/**
 * Enumerates concrete units of mass. [baseValue] is the factor to convert into the group's base
 * unit ([BASE], kilogram): `1 unit = baseValue * kilogram`.
 */
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

The wrapper class (`KMassUnitInstance`) encapsulates a `KMixedUnitInstance` by **delegation** (not inheritance)
and always normalizes its value to the group's base unit. Copy the shape of `KLengthUnitInstance` - it is
generic in the exponent, so the same wrapper also serves derived quantities of mass if you ever need them.

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KMixedUnitInstance
import org.pcsoft.framework.kunit.KUnitTarget
import org.pcsoft.framework.kunit.KUnitTerm

class KMassUnitInstance internal constructor(internal val instance: KMixedUnitInstance) {

    private val exponent: Int get() = instance.units.single().exponent

    val value: Double get() = instance.value

    fun valueAs(target: KUnitTarget): Double = instance.valueAs(target)

    operator fun plus(other: KMassUnitInstance): KMassUnitInstance = KMassUnitInstance(instance + other.instance)
    operator fun minus(other: KMassUnitInstance): KMassUnitInstance = KMassUnitInstance(instance - other.instance)

    operator fun times(other: KMassUnitInstance): KMixedUnitInstance = instance * other.instance
    operator fun div(other: KMassUnitInstance): KMixedUnitInstance = instance / other.instance
    operator fun times(other: KMixedUnitInstance): KMixedUnitInstance = instance * other
    operator fun div(other: KMixedUnitInstance): KMixedUnitInstance = instance / other

    operator fun compareTo(other: KMassUnitInstance): Int {
        check(exponent == other.exponent) { "Cannot compare KMassUnitInstance with different exponents: $exponent vs ${other.exponent}" }
        return value.compareTo(other.value)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is KMassUnitInstance) return false
        check(exponent == other.exponent) { "Cannot compare KMassUnitInstance with different exponents: $exponent vs ${other.exponent}" }
        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = instance.toString()
    fun toString(target: KUnitTarget): String = instance.toString(target)

    fun toUnit(): KMixedUnitInstance = instance
}

/** Converts a pure-mass [KMixedUnitInstance] back into a [KMassUnitInstance], normalizing to [KMassUnit.BASE]. */
fun KMixedUnitInstance.toKMassUnit(): KMassUnitInstance {
    val term = units.singleOrNull()
    val unit = term?.unit
    check(term != null && unit is KMassUnit) {
        "KMixedUnitInstance $this does not represent a pure mass-based value (expected exactly one term of a KMassUnit)"
    }
    val normalizedValue = value * Math.pow(unit.baseValue, term.exponent.toDouble())
    return KMassUnitInstance(KMixedUnitInstance(normalizedValue, listOf(KUnitTerm(KMassUnit.BASE, term.exponent))))
}

internal fun massUnitInstanceOf(value: Double): KMassUnitInstance =
    KMassUnitInstance(KMixedUnitInstance(value, listOf(KUnitTerm(KMassUnit.BASE, 1))))
```

## 3. Add creator extension properties

Following the `K...UnitExtensions.kt` pattern, add a bare `val` alias plus a `Number` extension **property** per
unit, so callers can write `5.kilograms` or `1 kilo grams` and also pass `kilograms` as a plain `valueAs`
target:

```kotlin
package org.pcsoft.framework.kunit.mass

/** Bare reference to [KMassUnit.KILOGRAM], for use with [valueAs][KMassUnitInstance.valueAs] or the prefix `infix` functions. */
val kilograms: KMassUnit = KMassUnit.KILOGRAM

/** Bare reference to [KMassUnit.GRAM]. */
val grams: KMassUnit = KMassUnit.GRAM

/** Bare reference to [KMassUnit.POUND]. */
val pounds: KMassUnit = KMassUnit.POUND

/** Bare reference to [KMassUnit.OUNCE]. */
val ounces: KMassUnit = KMassUnit.OUNCE

private fun of(value: Number, unit: KMassUnit): KMassUnitInstance = massUnitInstanceOf(value.toDouble() * unit.baseValue)

/** Creates a pure mass value in kilograms from any [Number] type. */
val Number.kilograms: KMassUnitInstance get() = of(this, KMassUnit.KILOGRAM)

/** Creates a pure mass value in grams. */
val Number.grams: KMassUnitInstance get() = of(this, KMassUnit.GRAM)

/** Creates a pure mass value in pounds. */
val Number.pounds: KMassUnitInstance get() = of(this, KMassUnit.POUND)

/** Creates a pure mass value in ounces. */
val Number.ounces: KMassUnitInstance get() = of(this, KMassUnit.OUNCE)
```

That's it - this already gives you full `+`, `-`, `*`, `/`, comparisons, SI prefixes (`5 kilo grams`), and
`toUnit()`/`toKMassUnit()` round-tripping for free, since all of that lives in the generic root
package and only needs `KMassUnit : KUnit` to work.

```kotlin
import org.pcsoft.framework.kunit.mass.*

val a = 500.grams
val b = 2.pounds
val total = a + b            // KMassUnitInstance, normalized to kilograms
println(total.valueAs(kilograms))
println(total.valueAs(grams))

val heavier = b > a          // true
```

## 4. (Optional) Add special/derived units

If your group has commonly used named units bound to a specific exponent (like hectare for area), add a
`KDerivedUnit` object analogous to `KDistanceDerivedUnit`:

```kotlin
package org.pcsoft.framework.kunit.mass

import org.pcsoft.framework.kunit.KDerivedUnit

object KMassDerivedUnit {
    /** Metric ton, 1 t = 1000 kg (exponent 1, an alternative "named" scaling of the base unit). */
    val TONNE: KDerivedUnit<KMassUnit> = KDerivedUnit(symbol = "t", exponent = 1, baseValue = 1000.0, referenceUnit = KMassUnit.BASE)
}
```

```kotlin
val truckLoad = 3.pounds.toUnit().toKMassUnit() // just for illustration
println(2500.grams.valueAs(KMassDerivedUnit.TONNE)) // 0.0025
```

## 5. Combine with other groups

Because everything ultimately funnels through the generic `KMixedUnitInstance` engine, your new group immediately
composes with any other group (e.g. length) via `*`/`/` - see [Mixed Units](mixed-units.md) for the full
rules:

```kotlin
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.mass.*

// density = mass / volume
val density = 5.kilograms.toUnit() / 2.liters.toUnit()
```

## 6. Naming and testing checklist

- All public types start with `K` (`KMassUnit`, `KMassUnitInstance`, `KMassDerivedUnit`, ...); creator
  extension properties and bare `val` aliases (`kilograms`, `grams`, ...) are exempt and stay
  language-natural.
- Cover the group with the parameterized cross-matrix test procedure (prefix × unit, unit → unit
  conversion, one method per operator and per comparison over every unit pair, `toString`) — see the
  "Parameterized cross-matrix test procedure" section in `CLAUDE.md`.
- Document every public member in English, in Markdown, with examples where useful - especially operators.
- Write a full test suite per group, mirroring the structure under `length`:
    - a dedicated test class for the `KUnit` enum values themselves,
    - a dedicated test class for the wrapper class covering every operator (`+`, `-`, `*`, `/`) and every
      comparison operator (`==`, `!=`, `<`, `<=`, `>`, `>=`) with both a success and (where applicable) an
      `IllegalStateException` failure case,
    - a full prefix × unit test matrix (every unit/derived unit combined with every SI prefix), plus one
      standalone test per prefix,
    - mixed-unit tests combining the new group with at least one other group.
