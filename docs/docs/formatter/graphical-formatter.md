# Graphical Formatter

`KGraphicalConsoleUnitFormatter` renders a value **graphically over several lines** for an ANSI-capable
terminal: a fraction is drawn as a real two-dimensional stack — numerator, a horizontal bar, denominator —
with the value on the bar (middle) line. Exponents are always set as real Unicode superscript digits, and
every visual role is coloured through a `KGraphicalConsoleColorPalette`.

It lives in the `org.pcsoft.framework.kunit.formatter` package and is an immutable, thread-safe `class`.

## What it produces

A clean single-denominator shape is stacked over three lines; every other shape is a single-line product
joined by the multiplication sign with superscript exponents; a dimensionless value is the coloured number
alone. The numerator and denominator are centred over the bar using their **visible** width (ANSI colour
sequences do not count towards the width). Acceleration `9.81 m/s²` renders (uncoloured) as:

```
     m
9.81 ──
     s²
```

## Configuration

`KGraphicalConsoleFormatConfig` is a value type; pick the `DEFAULT` preset or build your own:

| Option            | Values / type                                   | Default    |
|-------------------|-------------------------------------------------|------------|
| `palette`         | `KGraphicalConsoleColorPalette` — `CLASSIC`, `VIVID`, `MONOCHROME` | `CLASSIC` |
| `fractionBar`     | `LINE` (`─`), `HEAVY` (`━`), `ASCII` (`-`)      | `LINE`     |
| `multiplication`  | `ASTERISK` (`*`), `MIDDLE_DOT` (`·`), `CROSS` (`×`) | `MIDDLE_DOT` |
| `functionSymbols` | `KGraphicalFunctionSymbols` — `UNICODE`, `ASCII` | `UNICODE`  |

The palette colours five roles: number, symbol, operator, exponent and the fraction **bar**. A role whose
colour is the empty string is left uncoloured (this is how `MONOCHROME` leaves the exponent untouched).

## Real-world example

Speed and acceleration in the terminal:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KGraphicalConsoleUnitFormatter
import org.pcsoft.framework.kunit.distance.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 150 of kilo.meters / (6000 of seconds)   // 25 m/s

// default CLASSIC palette (colours omitted here); geometry:
//      km
// 90.0 ──
//      h
println(v.format(kilo.meters / hours, "%.1f", Locale.US, KGraphicalConsoleUnitFormatter()))

// a heavy ASCII-free bar and cross products
val config = KGraphicalConsoleFormatConfig(fractionBar = KGraphicalFractionBar.HEAVY)
println(v.format(kilo.meters / hours, "%.1f", Locale.US, KGraphicalConsoleUnitFormatter(config)))
```

To emit a completely different notation, see [Custom Formatter](custom-formatters.md).
