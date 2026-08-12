# Formatting Output

This page is the overview of the **Formatter** group. It explains the `format` verb — the entry point for all
formatting. Two dedicated pages go deeper:

- [Default Formatter](default-formatter.md) — how the shipped `KDefaultUnitFormatter` renders the unit part (the
  notation you get out of the box), with example outputs.
- [Custom Formatter](custom-formatters.md) — how to plug in your own rendering (LaTeX, MathML, HTML, …).

Every value knows how to print itself in its **base unit** via `toString()`, and it can be **read** into a specific unit
with [`into`](../mixed-units.md) — but `into` returns only a bare `Double`, without a unit symbol. The `format` verb
closes that gap: it is the display counterpart of `into`, returning value **and**
unit symbol as a `String`.

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds

val v = 3 of meters / seconds

v format kilo.meters / hours       // "10.799999999999999 km/h"
```

Just like `into`, `format` first reads the value into the target unit (running the same dimension check and the same
affine-aware conversion), and then appends the target's unit symbol. Because the target carries the unit it was written
down as, prefixed and alternate units render under their **own** symbol (`km`, `h`,
`mi`) rather than the group base symbol (`m`, `s`).

## Number formatting: pattern and locale

The infix form renders the raw `Double`. To round or localize the **numeric part**, use the `format`
overload with a number pattern and an optional `KLocale`:

```kotlin
import org.pcsoft.framework.kunit.formatter.KLocale

v.format(kilo.meters / hours, "%.1f")                 // "10.8 km/h"
v.format(kilo.meters / hours, "%.1f", KLocale.DE_DE)  // "10,8 km/h"
```

The pattern affects **only** the number; the unit part is unchanged. An invalid pattern throws
`IllegalArgumentException`, and an incompatible target dimension throws `IllegalStateException`
(exactly like `into`).

### `KLocale`

Kotlin has no common locale API — `java.util.Locale` exists on the JVM only — so kunit carries its own minimal
description of how a number is written down: decimal separator, grouping separator and grouping sizes. Because the
conventions travel with the value, **the same pattern renders the same string on every target**.

`KLocale.ROOT` (dot decimal, comma grouping) is the default. Predefined constants cover the common cases:
`EN_US`, `EN_GB`, `DE_DE`, `FR_FR`, `ES_ES`, `IT_IT`, `PT_BR`, `NL_NL`, `RU_RU`, `JA_JP`, `ZH_CN`, `KO_KR`,
`AR_SA` and `HI_IN` (which models the Indian 3-then-2 grouping). Any other convention is expressible by constructing a
`KLocale` directly.

On the JVM a `java.util.Locale` still works: overloads taking it are available in the JVM source set and convert via
`toKLocale()`.

```kotlin
import java.util.Locale

v.format(kilo.meters / hours, "%.1f", Locale.GERMANY) // "10,8 km/h" (JVM only)
```

### Supported patterns

The pattern is a printf subset applied to the single numeric value:

```
%[flags][width][.precision]conversion
```

| Part         | Meaning                                                                        |
|--------------|--------------------------------------------------------------------------------|
| flags        | `-` left-justify · `+` always sign · space for positive · `0` zero-pad · `,` group |
| width        | minimum total number of characters                                             |
| precision    | number of fraction digits (conversions `f`, `e`, `E`)                          |
| conversion   | `f` fixed · `e`/`E` scientific · `d` integer · `s` plain rendering              |

`%%` emits a literal percent sign, and literal text around the conversion is copied verbatim.

```kotlin
(1500 of meters).toString("%,.2f", KLocale.EN_US) // "1,500.00 m"
(1500 of meters).toString("%,.2f", KLocale.DE_DE) // "1.500,00 m"
(1500 of meters).toString("%.2e", KLocale.EN_US)  // "1.50e+03 m"
```

## Fraction vs. product notation

The built-in formatter renders the unit part like this:

| Terms                                   | Rendered              |
|-----------------------------------------|-----------------------|
| single unit, exponent 1                 | `km`                  |
| exponent ≠ 1                            | `m^2`                 |
| one numerator + exactly one denominator | `km/h`, `m/s^2`       |
| anything else                           | `m*s^-3*A^-2`, `s^-1` |
| no unit (dimensionless)                 | just the number       |

## `toString` with a pattern

The no-argument `toString()` is unchanged (base-unit rendering). An additional overload adds the same number
pattern/locale to the base-unit output — the `format` verb without a target:

```kotlin
(3 of meters / seconds).toString("%.2f", KLocale.EN_US) // "3.00 m/s"
(1500 of meters).toString("%.1f", KLocale.EN_US)        // "1500.0 m"
```

## A real-world example

Convert a running pace and print it cleanly:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*
import org.pcsoft.framework.kunit.kinematic.time.*
import org.pcsoft.framework.kunit.formatter.KLocale

val distance = 10 of kilo.meters
val time = 50 of minutes
val speed = distance / time                    // KSpeedUnitInstance

println(speed.format(kilo.meters / hours, "%.1f", KLocale.EN_US)) // "12.0 km/h"
println(speed.format(meters / seconds, "%.2f", KLocale.EN_US))    // "3.33 m/s"
```

## Custom rendering

The unit part is produced by a pluggable [`KUnitFormatter`](custom-formatters.md); the shipped
`KDefaultUnitFormatter` produces the plain text shown above — see [Default Formatter](default-formatter.md)
for its exact rules and example outputs. To emit a completely different notation — LaTeX or MathML for a graphical
formula renderer, HTML, ... — implement your own formatter and pass it explicitly. See
[Custom Formatter](custom-formatters.md).
