# AsciiMath Formatter

`KAsciiMathUnitFormatter` renders a value as **AsciiMath**, the concise input syntax of MathJax. With the
default configuration `3 of meters / seconds` read into `km/h` becomes `10.8 "km"/"h"`.

It lives in the `org.pcsoft.framework.kunit.formatter` package and is an immutable, thread-safe `class`.

## What it produces

The layout follows the shared formatter rules: with the `FRACTION` style a clean single-denominator shape
uses the `a/b` fraction form (grouping the numerator or a powered denominator in parentheses where needed);
every other shape — and the whole of the `EXPONENT` style — is a flat product joined by the multiplication
marker with signed exponents. A dimensionless value renders as just the number.

## Configuration

`KAsciiMathFormatConfig` is a value type; pick a preset or build your own:

| Option           | Values                                | Default   |
|------------------|---------------------------------------|-----------|
| `fractionStyle`  | `FRACTION`, `EXPONENT`                | `FRACTION`|
| `quoting`        | `QUOTED` (`"km"`), `BARE` (`km`)      | `QUOTED`  |
| `multiplication` | `ASTERISK` (`*`), `TIMES` (`xx`), `SPACE` (space) | `SPACE` |

Presets: `DEFAULT`, `PLAIN` (bare symbols joined by `*`).

## Real-world example

Speed from distance and time (`v = s / t`):

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KAsciiMathUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

v.format(kilo.meters / hours, "%.1f", Locale.US, KAsciiMathUnitFormatter())
// 90.0 "km"/"h"
```

Acceleration (`a = m/s²`) groups the powered denominator:

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KAsciiMathUnitFormatter())
// 9.81 "m"/("s"^2)
```

To emit a completely different notation, see [Custom Formatter](custom-formatters.md).
