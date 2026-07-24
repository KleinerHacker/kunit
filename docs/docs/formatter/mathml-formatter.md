# MathML Formatter

`KMathMlUnitFormatter` renders a value as **Presentation MathML**, rendered natively by browsers and by
MathJax. With the default configuration `3 of meters / seconds` read into `km/h` becomes an inline `<math>`
with an `<mfrac>` of `<mi>km</mi>` over `<mi>h</mi>`.

It lives in the `org.pcsoft.framework.kunit.formatter` package and is an immutable, thread-safe `class`.

## What it produces

The layout follows the shared formatter rules: with the `MFRAC` style a clean single-denominator shape is
stacked in an `<mfrac>`; every other shape — and the whole of the `EXPONENT` style — is a flat product joined
by the multiplication `<mo>` with signed `<msup>` exponents. A dimensionless value renders as just the
`<mn>`.

## Configuration

`KMathMlFormatConfig` is a value type; pick a preset or build your own:

| Option           | Values                                   | Default        |
|------------------|------------------------------------------|----------------|
| `fractionStyle`  | `MFRAC`, `EXPONENT`                      | `MFRAC`        |
| `unitTag`        | `MI`, `MTEXT`                            | `MI`           |
| `multiplication` | `MIDDLE_DOT` (`·`), `TIMES` (`×`), `INVISIBLE_TIMES` | `INVISIBLE_TIMES` |
| `wrapper`        | `MATH_INLINE`, `MATH_BLOCK`, `FRAGMENT`  | `MATH_INLINE`  |

Presets: `DEFAULT`, `INLINE` (inline `<msup>` exponents), `FRAGMENT` (no `<math>` root).

## Real-world example

Speed from distance and time (`v = s / t`):

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KMathMlUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

v.format(kilo.meters / hours, "%.1f", Locale.US, KMathMlUnitFormatter())
// <math display="inline"><mn>90.0</mn><mo>⁢</mo>
//   <mfrac><mrow><mi>km</mi></mrow><mrow><mi>h</mi></mrow></mfrac></math>
```

Acceleration (`a = m/s²`) puts the exponent into an `<msup>` inside the fraction:

```kotlin
(9.81 of meters / (seconds pow 2))
    .format(meters / (seconds pow 2), "%.2f", Locale.US, KMathMlUnitFormatter())
// <math display="inline"><mn>9.81</mn><mo>⁢</mo>
//   <mfrac><mrow><mi>m</mi></mrow><mrow><msup><mi>s</mi><mn>2</mn></msup></mrow></mfrac></math>
```

To emit a completely different notation, see [Custom Formatter](custom-formatters.md).
