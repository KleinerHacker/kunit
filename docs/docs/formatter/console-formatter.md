# Console Formatter

`KConsoleUnitFormatter` renders a value for an **ANSI-capable terminal**. It produces exactly the same
notation as the [Default Formatter](default-formatter.md) (`"10.8 km/h"`, `"m^2"`, `"m*s^-3*A^-2"`), but
wraps each visual role — the number, the unit symbols, the operators and the exponents — in ANSI colour
sequences so the parts stand out on the console.

It lives in the `org.pcsoft.framework.kunit.formatter` package. Unlike the default formatter it is a
regular (immutable, thread-safe) `class`, because it carries a colour palette.

## What it produces

The layout is **identical** to the [Default Formatter](default-formatter.md): a `"<number> <unit>"`
string, the single-fraction form `a/b` only for exactly one numerator and exactly one denominator term,
otherwise a flat product with signed exponents, and just the number for a dimensionless value. The only
difference is that each part is wrapped in an ANSI SGR colour and closed with the reset sequence `ESC[0m`.

### Coloured roles

Four visual roles are coloured independently through a [`KConsoleColorPalette`](#palettes):

| Role       | Palette field   | Example fragment |
|------------|-----------------|------------------|
| number     | `numberColor`   | `10.8`           |
| unit symbol| `symbolColor`   | `km`, `h`, `m`   |
| operator   | `operatorColor` | `*`, `/`         |
| exponent   | `exponentColor` | `^2`, `^-3`      |

A role whose colour is the **empty string** is emitted without any escape sequence (the fragment stays
uncoloured) — this is how `MONOCHROME` leaves the exponent untouched.

## Palettes

The colours are a value type, `KConsoleColorPalette`. Three palettes are predefined:

| Palette      | number             | symbol       | operator   | exponent           |
|--------------|--------------------|--------------|------------|--------------------|
| `CLASSIC`    | cyan `ESC[36m`     | yellow `ESC[33m` | grey `ESC[90m` | magenta `ESC[35m` |
| `VIVID`      | bright green bold `ESC[92;1m` | bright blue `ESC[94m` | white `ESC[97m` | bright magenta `ESC[95m` |
| `MONOCHROME` | bold `ESC[1m`      | dim `ESC[2m` | dim `ESC[2m` | uncoloured (empty) |

- `CLASSIC` is calm and readable on a dark terminal and is the **default**.
- `VIVID` is high-contrast and plakative.
- `MONOCHROME` uses brightness only (no colours) for colour-poor terminals.

## Using it

Construct it without arguments to use the default `CLASSIC` palette, or pass a predefined or custom
palette. It is then handed to the [`format`](formatting.md) verb (or the parameterised `toString`) like
any other formatter:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.KConsoleColorPalette
import org.pcsoft.framework.kunit.formatter.KConsoleUnitFormatter
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 3 of meters / seconds

// default CLASSIC palette
v.format(kilo.meters / hours, "%.1f", Locale.US, KConsoleUnitFormatter())

// a predefined palette
v.format(kilo.meters / hours, "%.1f", Locale.US, KConsoleUnitFormatter(KConsoleColorPalette.VIVID))

// base-unit rendering with no target
(5 of meters).toString(pattern = "%.1f", formatter = KConsoleUnitFormatter(KConsoleColorPalette.MONOCHROME))
```

## Configuration (exponents, signs, function symbols)

Independently of the colours, a second argument `KConsoleFormatConfig` controls the notation, exactly like
the [Default Formatter](default-formatter.md):

| Option            | Values                                     | Default   |
|-------------------|--------------------------------------------|-----------|
| `exponentStyle`   | `CARET` (`m^2`), `SUPERSCRIPT` (`m²`)      | `CARET`   |
| `multiplication`  | `ASTERISK` (`*`), `MIDDLE_DOT` (`·`), `CROSS` (`×`) | `ASTERISK` |
| `division`        | `SLASH` (`/`), `OBELUS` (`÷`)              | `SLASH`   |
| `functionSymbols` | `KConsoleFunctionSymbols` — `UNICODE`, `ASCII` | `UNICODE` |

Both constructor arguments default, so `KConsoleUnitFormatter()` is the classic palette with the historical
notation. Pass the config as the second argument (`KConsoleUnitFormatter(palette, config)`):

```kotlin
import org.pcsoft.framework.kunit.formatter.KConsoleColorPalette
import org.pcsoft.framework.kunit.formatter.KConsoleFormatConfig
import org.pcsoft.framework.kunit.formatter.KConsoleUnitFormatter

val formatter = KConsoleUnitFormatter(KConsoleColorPalette.CLASSIC, KConsoleFormatConfig.SUPERSCRIPT)
// renders "m²", "s⁻¹" with real superscript exponents, coloured as usual
```

For a multi-line, two-dimensional fraction (a real fraction bar), see the
[Graphical Formatter](graphical-formatter.md).

## Defining your own palette

`KConsoleColorPalette` is a plain data class, so you can supply your own colour sequences. Each field
holds the ANSI **introducer** (e.g. `ESC[31m` for red, where `ESC` is the escape character, code 27); the
shared `reset` (default `ESC[0m`) is appended after every coloured fragment:

```kotlin
import org.pcsoft.framework.kunit.formatter.KConsoleColorPalette
import org.pcsoft.framework.kunit.formatter.KConsoleUnitFormatter

val esc = 27.toChar()   // the ANSI escape character (ESC)
val myPalette = KConsoleColorPalette(
    numberColor = "$esc[31m",   // red
    symbolColor = "$esc[32m",   // green
    operatorColor = "$esc[34m", // blue
    exponentColor = "$esc[35m", // magenta
)
val formatter = KConsoleUnitFormatter(myPalette)
```

For a completely different notation (not just colours), implement your own
[Custom Formatter](custom-formatters.md) instead.
