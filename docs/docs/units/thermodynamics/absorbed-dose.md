# Absorbed Dose (Gray)

Package: `org.pcsoft.framework.kunit.thermo.specificenergy`
Base unit: **joule per kilogram**
(`KSpecificEnergyUnit.BASE == KSpecificEnergyUnit.JOULE_PER_KILOGRAM`)

Type: **constructed unit**

The absorbed dose `D` is the ionising-radiation energy deposited per unit of mass: `D = E / m`. Its unit is
the **gray**, and `1 Gy = 1 J/kg` — **dimensionally identical** to the
[specific energy](specific-energy.md).

## Why the gray has no type of its own

KUnit deliberately models the absorbed dose with `KSpecificEnergyUnitInstance` rather than a separate
`KAbsorbedDoseUnitInstance`. The reason is the form-recognition contract of this library:

* every standardized group has **one** canonical base-dimension normal form, and
* `toX()` recognises exactly that form.

Absorbed dose and specific energy share the normal form `length² · time⁻²`. Two types over one normal form
would make the native expression ambiguous — `toSpecificEnergy()` and a hypothetical `toAbsorbedDose()`
would both match the same mixed unit, and neither answer would be more correct than the other. A single
type keeps the round-trip deterministic.

The distinction is therefore a matter of *what you name your variable*, not of what type the library hands
you — exactly as in physics, where the gray **is** the joule per kilogram.

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val dose = 2 of milli.joulesPerKilogram      // read as 2 mGy
dose into joulesPerKilogram                   // 0.002

// The energy deposited in a 70 kg body
val energy = dose * (70 of kilo.grams)
energy into joules                            // 0.14 J
```

## Real-world example — a chest X-ray

A chest radiograph deposits roughly **0.1 mGy**. What total energy is that in a 70 kg person, and how does
it compare to a year of natural background (≈ 2.4 mGy)?

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val xray = 0.1 of milli.joulesPerKilogram
val background = 2.4 of milli.joulesPerKilogram

(xray * (70 of kilo.grams)) into milli.joules      // 7.0 mJ
(background into joulesPerKilogram) / (xray into joulesPerKilogram)   // 24 X-rays per year of background
```

## See also

* [Specific Energy](specific-energy.md) — the same type, read as an energy density.
* [Dose Equivalent](dose-equivalent.md) — the sievert, weighted for biological effect.
* [Dose Rate](dose-rate.md) — dose per time, which **does** have its own type.
* [Exposure](exposure.md) — the charge-based ionisation dose.
