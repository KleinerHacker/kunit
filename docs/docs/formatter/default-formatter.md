# Default Formatter

`KDefaultUnitFormatter` is the formatter that kunit uses out of the box. Whenever you call
[`format`](formatting.md) or the parameterised `toString` **without** passing your own formatter, this one produces the
result — plain, human-readable text such as `"10.8 km/h"`. This page describes exactly **what**
it renders and **how**, with example outputs, and shows how to use it explicitly.

It is an immutable, thread-safe `class` and lives in the `org.pcsoft.framework.kunit.formatter` package. Construct it
without arguments for the historical behaviour, or pass a `KDefaultFormatConfig` to change how it renders
(see [Configuration](#configuration)).

## What it produces

A rendered string has two parts: the **number** and the **unit part**, separated by a single space
(`"<number> <unit>"`). If the value is dimensionless (no units), only the number is rendered.

### The number

- Without a pattern, the raw `Double` is printed in kunit's portable plain form (`10.8`, `5.0`, `1.0E7`) — identical on
  every target, unlike the platform's own `Double.toString()`.
- With a number pattern (and optional `KLocale`), the pattern is applied to the number only, never to the unit part.
  See [Formatting Output](formatting.md) for the supported patterns.

| Call                                                 | Number rendered      |
|------------------------------------------------------|----------------------|
| `format(kilo.meters / hours)`                        | `10.799999999999999` |
| `format(kilo.meters / hours, "%.1f")`                | `10.8`               |
| `format(kilo.meters / hours, "%.1f", KLocale.DE_DE)` | `10,8`               |

### The unit part

Each unit term is rendered under its **own** written-down symbol (honouring prefix and alternate-unit display metadata),
so `km`, `h`, `mi`, `KiB` render as themselves rather than the group base symbol. The overall shape depends on the
terms:

| Terms                                   | Rendered              |
|-----------------------------------------|-----------------------|
| single unit, exponent 1                 | `km`                  |
| exponent ≠ 1                            | `m^2`                 |
| one numerator + exactly one denominator | `km/h`, `m/s^2`       |
| anything else                           | `m*s^-3*A^-2`, `s^-1` |
| no unit (dimensionless)                 | just the number       |

The single-fraction form (`a/b`) is only used when there is **exactly one** numerator term and **exactly one**
denominator term. Everything else is rendered as a flat product with explicit (possibly negative)
exponents.

## Example outputs

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.distance.*
import org.pcsoft.framework.kunit.kinematic.time.*

(1500 of meters).toString()                          // "1500.0 m"
(3 of meters / seconds).format(kilo.meters / hours)  // "10.799999999999999 km/h"
(3 of meters / seconds).format(meters / seconds, "%.2f") // "3.33 m/s"
(9.81 of meters / (seconds pow 2)).format(meters / (seconds pow 2), "%.2f") // "9.81 m/s^2"
```

## Configuration

`KDefaultFormatConfig` (a value type) changes the rendering without affecting the layout rules:

| Option            | Values                                                         | Default    |
|-------------------|----------------------------------------------------------------|------------|
| `exponentStyle`   | `CARET` (`m^2`), `SUPERSCRIPT` (`m²`)                          | `CARET`    |
| `multiplication`  | `ASTERISK` (`*`), `MIDDLE_DOT` (`·`), `CROSS` (`×`)            | `ASTERISK` |
| `division`        | `SLASH` (`/`), `OBELUS` (`÷`)                                  | `SLASH`    |
| `functionSymbols` | `KDefaultFunctionSymbols` — `UNICODE`, `ASCII` (`√`/`sqrt`, …) | `UNICODE`  |

The `functionSymbols` table (roots `√`/`∛`/`∜`, `±`, `∞`, `°`) is prepared configuration for where a function
representation applies; with plain integer exponents it is not used. Presets: `DEFAULT` (historical output),
`SUPERSCRIPT` (real superscript exponents).

```kotlin
import org.pcsoft.framework.kunit.formatter.KDefaultFormatConfig
import org.pcsoft.framework.kunit.formatter.KDefaultUnitFormatter

(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", KLocale.EN_US, KDefaultUnitFormatter(KDefaultFormatConfig.SUPERSCRIPT))
// "9.81 m/s²"
```

## Using it explicitly

The default formatter is applied automatically, so you rarely name it. You may still pass it explicitly — for symmetry
with a custom formatter, or to make the choice obvious at the call site:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KDefaultUnitFormatter
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.formatter.KLocale

val v = 3 of meters / seconds

// explicit formatter, same result as the default call
v.format(kilo.meters / hours, "%.1f", KLocale.EN_US, KDefaultUnitFormatter()) // "10.8 km/h"

// base-unit rendering with the default formatter and no target
(5 of meters).toString(pattern = null, formatter = KDefaultUnitFormatter()) // "5.0 m"
```

To emit a completely different notation instead, see [Custom Formatter](custom-formatters.md).
