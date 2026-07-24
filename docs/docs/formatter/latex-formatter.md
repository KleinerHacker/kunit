# LaTeX Formatter

`KLatexUnitFormatter` renders a value as **LaTeX** math, ready for MathJax, KaTeX or a LaTeX document. With
the default configuration `3 of meters / seconds` read into `km/h` becomes
`1.5\,\frac{\mathrm{km}}{\mathrm{h}}`.

It lives in the `org.pcsoft.framework.kunit.formatter` package and is an immutable, thread-safe `class`.

## What it produces

The layout follows the shared formatter rules: with the `FRACTION` style a clean single-denominator shape
(some numerator, exactly one denominator) is stacked as `\frac{…}{…}`; every other shape — and the whole of
the `INLINE` style — is a flat product joined by the multiplication marker with signed exponents. A
dimensionless value renders as just the number.

## Configuration

`KLatexFormatConfig` is a value type; pick a preset or build your own:

| Option           | Values                                   | Default   |
|------------------|------------------------------------------|-----------|
| `fractionStyle`  | `FRACTION`, `INLINE`                     | `FRACTION`|
| `unitWrapper`    | `MATHRM`, `TEXT`, `NONE`                 | `MATHRM`  |
| `multiplication` | `CDOT` (`\cdot`), `TIMES` (`\times`), `THIN_SPACE` (`\,`) | `CDOT` |
| `delimiter`      | `DOLLAR` (`$…$`), `PARENTHESES` (`\(…\)`), `NONE` | `NONE` |
| `spacing`        | `THIN` (`\,`), `NORMAL` (space)          | `THIN`    |

Presets: `DEFAULT`, `INLINE` (inline product), `PLAIN` (unwrapped symbols, normal spacing).

## Real-world example

Speed from distance and time (`v = s / t`), 150 km in 100 min:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KLatexUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

v.format(kilo.meters / hours, "%.1f", Locale.US, KLatexUnitFormatter())
// 90.0\,\frac{\mathrm{km}}{\mathrm{h}}

v.format(kilo.meters / hours, "%.1f", Locale.US, KLatexUnitFormatter(KLatexFormatConfig.INLINE))
// 90.0\,\mathrm{km}\cdot\mathrm{h}^{-1}
```

Acceleration (`a = m/s²`) shows the powered denominator inside the fraction:

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KLatexUnitFormatter())
// 9.81\,\frac{\mathrm{m}}{\mathrm{s}^{2}}
```

To emit a completely different notation, see [Custom Formatter](custom-formatters.md).
