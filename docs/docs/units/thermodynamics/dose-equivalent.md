# Dose Equivalent (Sievert)

Package: `org.pcsoft.framework.kunit.thermo.specificenergy`
Base unit: **joule per kilogram**
(`KSpecificEnergyUnit.BASE == KSpecificEnergyUnit.JOULE_PER_KILOGRAM`)

Type: **constructed unit**

The dose equivalent `H` weights the [absorbed dose](absorbed-dose.md) by a **dimensionless** radiation
weighting factor `w_R`, which accounts for how damaging a given radiation type is: `H = w_R · D`. Its unit
is the **sievert**, and because `w_R` is dimensionless, `1 Sv = 1 J/kg` — the same dimension as the gray.

## Why the sievert has no type of its own

KUnit models the dose equivalent with `KSpecificEnergyUnitInstance`, the same type as the gray and the
specific energy. The reason is the form-recognition contract of this library:

* every standardized group has **one** canonical base-dimension normal form, and
* `toX()` recognises exactly that form.

Sievert, gray and specific energy all share the normal form `length² · time⁻²`. Several types over one
normal form would make the native expression ambiguous, and no answer would be more correct than another.
A single type keeps the round-trip deterministic.

!!! warning "The weighting factor is yours to apply"
    Because `w_R` is dimensionless, KUnit cannot tell a gray from a sievert. Multiplying an absorbed dose by
    the weighting factor is an ordinary scalar multiplication — the library will not do it for you, and it
    will not stop you from mixing the two readings. Name your values accordingly.

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val absorbed = 2 of milli.joulesPerKilogram   // 2 mGy of alpha radiation
val wR = 20.0                                  // weighting factor for alpha

val equivalent = absorbed * wR                 // read as 40 mSv
equivalent into milli.joulesPerKilogram        // 40.0
```

## Real-world example — a flight and a background year

Natural background is about **2.4 mSv per year**; a transatlantic flight adds roughly 0.05 mSv:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val perYear = 2.4 of milli.joulesPerKilogram
val flight = 0.05 of milli.joulesPerKilogram

(perYear into milli.joulesPerKilogram) / (flight into milli.joulesPerKilogram)  // 48 flights

// Ten flights added to the annual background
val total = perYear + (flight * 10)
total into milli.joulesPerKilogram                                              // 2.9
```

## See also

* [Absorbed Dose](absorbed-dose.md) — the unweighted gray.
* [Specific Energy](specific-energy.md) — the underlying type.
* [Dose Rate](dose-rate.md) — dose per time, with the sievert spellings.
* [Exposure](exposure.md) — the charge-based ionisation dose.
