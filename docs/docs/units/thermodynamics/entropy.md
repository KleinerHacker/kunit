# Entropy

Package: `org.pcsoft.framework.kunit.thermo.heatcapacity`
Base unit: **joule per kelvin** (`KHeatCapacityUnit.BASE == KHeatCapacityUnit.JOULE_PER_KELVIN`)

Type: **constructed unit**

Entropy `S` measures the dispersal of energy in a system. Its unit is `J/K` — **dimensionally identical**
to [heat capacity](heat-capacity.md).

## Why entropy has no type of its own

KUnit deliberately models entropy with `KHeatCapacityUnitInstance` rather than a separate
`KEntropyUnitInstance`. The reason is the form-recognition contract of this library:

* every standardized group has **one** canonical base-dimension normal form, and
* `toX()` recognises exactly that form.

Entropy and heat capacity share the normal form `mass¹ · distance² · time⁻² · temperature⁻¹`. Two types
over one normal form would make the native expression ambiguous — `toHeatCapacity()` and a hypothetical
`toEntropy()` would both match the same mixed unit, and neither answer would be more correct than the
other. A single type keeps the round-trip deterministic.

The distinction between the two quantities is therefore a matter of *what you name your variable*, not of
what type the library hands you — exactly as it is in physics notation, where both are written in J/K.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.heatcapacity.*

val entropyChange = 21.0 of joulesPerKelvin   // ΔS
val heatCapacity = 4184 of joulesPerKelvin    // C
// both are KHeatCapacityUnitInstance
```

## Real-world example: melting ice

Melting 1 kg of ice at 273.15 K absorbs 334 kJ of latent heat. The entropy change is `ΔS = Q / T`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.thermo.heatcapacity.*
import org.pcsoft.framework.kunit.thermo.temperature.KTemperatureDifference

val latentHeat = 334 of kilo.joules
val meltingPoint = KTemperatureDifference.ofKelvin(273.15) // as an interval from absolute zero

val entropyChange = latentHeat / meltingPoint  // KHeatCapacityUnitInstance, in J/K
entropyChange into joulesPerKelvin             // ≈ 1222.8 J/K

// The reverse: how much heat does that entropy change carry at the melting point?
(entropyChange * meltingPoint) into kilo.joules // 334.0 kJ
```

!!! note "Absolute temperature in `ΔS = Q / T`"
    Entropy is divided by an **absolute** temperature, but this library's quotients use the temperature
    *difference* group (`KTemperatureDifferenceUnit`) — an affine scale cannot appear in a denominator.
    Express the absolute kelvin reading as an interval from absolute zero, as above:
    `KTemperatureDifference.ofKelvin(273.15)`. In kelvin the two coincide numerically, which is exactly
    why the kelvin scale is the one thermodynamics uses.

## See also

* [Heat capacity](heat-capacity.md) — the type entropy shares, with the full unit table, all
  decompositions and the complete operator surface
* [Molar heat capacity](molar-heat-capacity.md) — the per-mole form (molar entropy)
* [Specific heat capacity](specific-heat-capacity.md) — the per-kilogram form (specific entropy)
* [Energy](energy.md) — the numerator of `ΔS = Q / T`

## Notation

The table below shows how this quantity is written mathematically versus in Kotlin with KUnit. Exponents use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction.

| Mathematics | Kotlin | Meaning |
|---|---|---|
| `J/K` | `joulesPerKelvin` | entropy, base unit (shared with heat capacity) |
| `kg·m²·s⁻²·K⁻¹` | `grams * (meters pow 2) / (seconds pow 2) / ΔK` | same quantity in base dimensions |
| `ΔS = Q / T` | `latentHeat / meltingPoint` | entropy change from heat ÷ temperature |
| `Q = ΔS · T` | `entropyChange * meltingPoint` | heat from entropy change × temperature |
