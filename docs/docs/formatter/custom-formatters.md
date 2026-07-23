# Custom Formatters

The [`format`](formatting.md) verb and the parameterised `toString` render text through a pluggable
`KUnitFormatter`. The shipped `KDefaultUnitFormatter` produces plain text such as `"10.8 km/h"`, but you can
plug in a completely custom rendering — for example to emit **LaTeX** or **MathML** for a graphical formula
renderer, HTML, or any domain-specific notation. This makes kunit easy to extend towards third-party
frameworks that turn a string into a typeset formula.

## The contract

A formatter receives everything it needs in a single `KUnitFormatContext` and returns the finished string:

```kotlin
interface KUnitFormatter {
    fun format(context: KUnitFormatContext): String
}

data class KUnitFormatContext(
    val value: Double,            // number, already converted into the target unit(s)
    val units: List<KUnitTerm>,   // target dimension's terms (with prefix/exponent display metadata)
    val pattern: String? = null,  // optional java.util.Formatter pattern for the number
    val locale: Locale = Locale.getDefault(),
)
```

Everything is passed in **one** context object so the interface can grow additively (new fields get
defaults) without breaking your implementation. Two reusable helpers cover the common building blocks:

- `KUnitFormatContext.renderValue()` — renders the number: plain `Double.toString()` when `pattern` is
  `null`, otherwise `String.format(locale, pattern, value)`.
- `KUnitTerm.displaySymbol` — the written-down symbol of a term (`"km"`, `"h"`), honouring its display
  metadata; falls back to the group base symbol when there is none.

A term's `exponent` tells you the power (positive = numerator, negative = denominator); your formatter
decides how to render it.

## Step by step: a LaTeX formatter

The following formatter renders `\frac{...}{...}` from the numerator and denominator terms, using
`\mathrm{...}` for each unit symbol:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.formatter.*

object LatexFormatter : KUnitFormatter {
    override fun format(context: KUnitFormatContext): String {
        // 1. split into numerator (exponent > 0) and denominator (exponent < 0)
        val (numerator, denominator) = context.units.partition { it.exponent > 0 }

        // 2. render one term, e.g. \mathrm{km} or \mathrm{s}^{2}
        fun render(terms: List<KUnitTerm>) = terms.joinToString(" ") { term ->
            val magnitude = kotlin.math.abs(term.exponent)
            val base = "\\mathrm{${term.displaySymbol}}"      // uses the display metadata
            if (magnitude == 1) base else "$base^{$magnitude}"
        }

        // 3. number via the reusable helper (respects pattern + locale)
        val value = context.renderValue()

        // 4. assemble
        if (denominator.isEmpty()) return "$value\\,${render(numerator)}".trim()
        return "$value\\,\\frac{${render(numerator)}}{${render(denominator)}}"
    }
}
```

## Using it

Pass the formatter explicitly — the default behaviour never changes unless you ask for it:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.time.seconds
import java.util.Locale

val v = 3 of meters / seconds

// format into a target unit with a custom formatter
v.format(kilo.meters / hours, "%.1f", Locale.US, LatexFormatter)
// "10.8\,\frac{\mathrm{km}}{\mathrm{h}}"

// or render the base units with a custom formatter (no target)
(5 of meters).toString(pattern = null, formatter = LatexFormatter)
// "5.0\,\mathrm{m}"
```

## Notes

- Keep a formatter **stateless** and therefore thread-safe — the shipped `KDefaultUnitFormatter` is a plain
  `object`, and so is `LatexFormatter` above.
- The `KUnitFormatContext` receives the value **already converted** into the target unit, so a formatter
  never performs unit conversion itself — it only renders.
- The `units` terms carry cosmetic display metadata (`KUnitTerm.display`); always read a symbol via
  `displaySymbol` so prefixed and alternate units (`km`, `mi`, `KiB`) render correctly.
