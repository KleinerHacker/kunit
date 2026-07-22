# Frequency

Package: `org.pcsoft.framework.kunit.frequency`
Base unit: **hertz** (`KFrequencyUnit.BASE == KFrequencyUnit.HERTZ`)

The frequency group models how often something happens per unit of time. It is a **native, one-dimensional**
group and the **inverse of time** (`1 Hz = 1/s`): `KFrequencyUnitInstance` wraps a single
`KFrequencyUnit.HERTZ` term, always stored normalized to hertz.

Because frequency is the reciprocal of time, its cross-group behaviour is defined to be **exactly inverse
to time**: multiplying by a frequency behaves like dividing by a time, and dividing by a frequency like
multiplying by a time.

## Units

| Unit | Enum value | Symbol | Token | 1 unit in hertz |
|---|---|---|---:|---:|
| Hertz | `KFrequencyUnit.HERTZ` | `Hz` | `hertz` | 1.0 |
| Revolutions per second | `KFrequencyUnit.RPS` | `rps` | `rps` | 1.0 |
| Frames per second | `KFrequencyUnit.FPS` | `fps` | `fps` | 1.0 |
| Revolutions per minute | `KFrequencyUnit.RPM` | `rpm` | `rpm` | 1/60 |
| Beats per minute | `KFrequencyUnit.BPM` | `bpm` | `bpm` | 1/60 |

Each `Token` is a value-1 `KFrequencyUnitInstance` used with `of` (build) and `into` (read).

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.frequency.*

val f = 2 of kilo.hertz      // 2000 Hz (kHz via the SI prefix)
f.value                      // 2000.0 (normalized to hertz)
(3000 of rpm) into hertz     // 50.0  (3000 rpm = 50 Hz)
(50 of hertz) into rpm       // 3000.0
```

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.frequency.*

// + / - : same group, automatic conversion between units
val a = (1 of kilo.hertz) + (500 of hertz)   // KFrequencyUnitInstance: 1500.0 Hz
val b = (1 of kilo.hertz) - (500 of hertz)   // KFrequencyUnitInstance: 500.0 Hz

// comparisons and equality (by normalized hertz value)
(1 of kilo.hertz) == (1000 of hertz)         // true
(1 of kilo.hertz) > (500 of hertz)           // true
```

### Inverse-of-time cross operators

A frequency and a time are reciprocals, so they combine into strongly typed results:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.frequency.*

val f = 60 / (1 of seconds)          // KFrequencyUnitInstance, 60 Hz (count / time = frequency)
val period = 1 / (2 of hertz)        // KTimeUnitInstance, 0.5 s   (count / frequency = time)
val count = (50 of hertz) * (2 of seconds)   // 100.0 (frequency * time = dimensionless count)

val v = (2 of meters) * (5 of hertz) // KSpeedUnitInstance, 10 m/s (length * frequency = speed)
(v / (5 of hertz)) into meters       // 2.0 (speed / frequency = distance)
```

## Real-world example: surface speed of a spinning wheel

A wheel with a circumference of **2 m** spins at **5 revolutions per second**. Multiplying its
circumference by the rotation frequency gives the surface (contact) speed — `length * frequency = speed`,
the inverse of the familiar `length / time = speed`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.frequency.*

val circumference = 2 of meters
val revolutions = 5 of rps               // 5 Hz
val surfaceSpeed = circumference * revolutions // KSpeedUnitInstance
surfaceSpeed into meters                 // reads in m/s via the speed group
surfaceSpeed.value                       // 10.0 m/s
```

## Powers with `pow`

Raise a value to an integer power with the infix `pow` operator (Kotlin has no overloadable `^`). For the
frequency group `pow` returns a generic `KMixedUnitInstance` (frequency has no dimensioned power type):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.frequency.*

val squared = (2 of hertz) pow 2     // KMixedUnitInstance: 4.0 Hz²
```

## SI prefixes

Frequency accepts **any** magnitude, so every SI prefix builder (`quetta` … `quecto`) can be combined with
every frequency unit via property access. `kilo.hertz` is kHz, `mega.hertz` is MHz, `giga.hertz` is GHz.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.frequency.*

(1 of mega.hertz).value          // 1000000.0 (MHz)
(2_400_000_000 of hertz) into giga.hertz // 2.4 (GHz)
```

## toString formatting

Only the base-unit `toString()` exists; format a specific unit via `into`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.frequency.*

(1 of kilo.hertz).toString()             // "1000.0 Hz" (base unit representation)
"${(50 of hertz) into rpm} rpm"          // "3000.0 rpm"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `Hz` | `hertz` | frequency, base unit (hertz) |
| `kHz` | `kilo.hertz` | kilohertz (prefix applied to the hertz) |
| `1/s` = `s⁻¹` | `1 / (1 of seconds)` | frequency from a period (typed hertz) |
| `Hz²` | `hertz pow 2` | hertz squared (generic mixed unit) |
