# Activity (Becquerel)

Package: `org.pcsoft.framework.kunit.kinematic.frequency`
Base unit: **hertz** (`KFrequencyUnit.BASE == KFrequencyUnit.HERTZ`)

Type: **native unit**

The activity `A` of a radioactive sample is the number of nuclear decays per second. Its unit is the
**becquerel**, and `1 Bq = 1 s⁻¹` — **dimensionally identical** to the [frequency](frequency.md).

## Why the becquerel has no type of its own

KUnit deliberately models the activity with `KFrequencyUnitInstance` rather than a separate
`KActivityUnitInstance`. The reason is the form-recognition contract of this library:

* every standardized group has **one** canonical base-dimension normal form, and
* `toX()` recognises exactly that form.

Activity and frequency share the normal form `time⁻¹`. Two types over one normal form would make the
native expression ambiguous — `toFrequency()` and a hypothetical `toActivity()` would both match the same
mixed unit, and neither answer would be more correct than the other. A single type keeps the round-trip
deterministic.

The distinction is a matter of *what you name your variable*: a frequency counts periodic cycles, an
activity counts random decays, but both are "events per second".

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.frequency.*
import org.pcsoft.framework.kunit.kinematic.time.seconds

val activity = 37 of giga.hertz     // read as 37 GBq — one gram of radium
activity into mega.hertz             // 37 000.0

// Decays in a minute
val decays = activity * (60 of seconds)   // dimensionless count
decays                                     // 2.22e12
```

!!! note "The curie"
    The historical unit is the curie, 1 Ci = 3.7 × 10¹⁰ Bq. It has no token of its own; write it as
    `37 of giga.hertz` or introduce your own constant.

## Real-world example — a smoke detector source

A domestic smoke detector holds about **30 kBq** of americium-241:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.kinematic.frequency.*
import org.pcsoft.framework.kunit.kinematic.time.hours

val source = 30 of kilo.hertz             // 30 kBq
source into hertz                          // 30 000.0

// Decays over a day
val perDay = source * (24 of hours)
perDay                                      // ≈ 2.59e9
```

## See also

* [Frequency](frequency.md) — the same type, read as a periodic rate.
* [Dose Rate](../thermodynamics/dose-rate.md) — the dose a source delivers per time.
* [Absorbed Dose](../thermodynamics/absorbed-dose.md) — the energy-based dose.
