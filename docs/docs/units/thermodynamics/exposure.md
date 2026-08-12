# Exposure (Ionisation Dose)

Package: `org.pcsoft.framework.kunit.electric.specificcharge`
Base unit: **coulomb per kilogram**
(`KSpecificChargeUnit.BASE == KSpecificChargeUnit.COULOMB_PER_KILOGRAM`)

Type: **constructed unit**

The exposure `X` — the classical **ionisation dose** — measures ionising radiation by the electric charge
it liberates per unit of air mass: `X = Q / m`, in `C/kg`. Its historical unit is the **roentgen**
(1 R = 2.58 × 10⁻⁴ C/kg).

Its dimension is `current · time · mass⁻¹` — **the same** as the [specific charge](../electrical/specificcharge.md)
of a particle. KUnit models one group for both readings; the exposure is one of them. This page documents
that reading.

## Why exposure has no type of its own

KUnit deliberately models the exposure with `KSpecificChargeUnitInstance` rather than a separate
`KExposureUnitInstance`. The reason is the form-recognition contract of this library:

* every standardized group has **one** canonical base-dimension normal form, and
* `toX()` recognises exactly that form.

Exposure and specific charge share the normal form `current¹ · time¹ · mass⁻¹`. Two types over one normal
form would make the native expression ambiguous — `toSpecificCharge()` and a hypothetical `toExposure()`
would both match the same mixed unit, and neither answer would be more correct than the other. A single
type keeps the round-trip deterministic.

The distinction is therefore a matter of *what you name your variable*, not of what type the library hands
you — exactly as in physics, where both are written in C/kg.

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

val exposure = 1 of roentgens                   // read as an ionisation dose
exposure into coulombsPerKilogram                // 2.58e-4

// The charge liberated in 1 kg of air
val q = exposure * (1 of kilo.grams)
q into coulombs                                   // 2.58e-4

// A survey reading in milliroentgen
val small = 20 of milli.roentgens
small into coulombsPerKilogram                    // ≈ 5.16e-6
```

## Real-world example — an old dosimeter reading

A pen dosimeter shows **200 mR** after a shift. Converted to SI and to the charge released in the 1 kg of
air the chamber is calibrated against:

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.electric.specificcharge.*

val shift = 200 of milli.roentgens
shift into coulombsPerKilogram                    // ≈ 5.16e-5
(shift * (1 of kilo.grams)) into micro.coulombs   // ≈ 51.6 µC
```

## See also

* [Specific Charge](../electrical/specificcharge.md) — the same type, read as a particle property.
* [Absorbed Dose](absorbed-dose.md) and [Dose Equivalent](dose-equivalent.md) — the energy-based doses.
* [Dose Rate](dose-rate.md) — dose per time.
