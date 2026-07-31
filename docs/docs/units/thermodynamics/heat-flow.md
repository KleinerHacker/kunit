# Heat Flow

Package: `org.pcsoft.framework.kunit.common.power`
Base unit: **watt** (`KPowerUnit.BASE == KPowerUnit.WATT`)

Type: **constructed unit**

Heat flow `Q̇` (also called thermal power, or heat rate) is the amount of heat transferred per unit of time: `W`. It is
**dimensionally and physically identical to [power](power.md)** — energy per time — and KUnit therefore models it with
`KPowerUnitInstance`.

## Why heat flow has no type of its own

Heat flow is not a separate quantity, it is power that happens to be thermal. There is exactly one canonical normal form
`mass¹ · distance² · time⁻³`, and a second type over it would make `toPower()`
ambiguous without adding any physics. Whether a watt describes an electric motor, a laser or a radiator is context, not
dimension.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.power.*

val motor = 2 of kilo.watts     // mechanical power
val radiator = 1500 of watts    // heat flow
// both are KPowerUnitInstance
```

## Real-world example: a radiator

A radiator rated 1500 W runs for 4 hours. How much energy does it deliver, and what heat flux density does it produce
over its 0.6 m² surface?

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.common.power.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.hours
import org.pcsoft.framework.kunit.thermo.heatfluxdensity.wattsPerSquareMeter

val radiator = 1500 of watts
val runtime = 4 of hours

val energy = radiator * runtime          // KEnergyUnitInstance
energy into kilo.joules                  // 21_600.0 kJ (= 6 kWh)

val surface = (1 of meters) * (0.6 of meters)  // 0.6 m²
val flux = radiator / surface            // KHeatFluxDensityUnitInstance
flux into wattsPerSquareMeter            // 2500.0 W/m²
```

## Where heat flow appears in this field

| Expression               | Result type                    | Meaning                                   |
|--------------------------|--------------------------------|-------------------------------------------|
| `energy / time`          | `KPowerUnitInstance`           | heat flow from heat ÷ duration            |
| `power * time`           | `KEnergyUnitInstance`          | heat delivered over a duration            |
| `power / area`           | `KHeatFluxDensityUnitInstance` | [heat flux density](heat-flux-density.md) |
| `heatFluxDensity * area` | `KPowerUnitInstance`           | total heat flow through a surface         |

A wall's heat loss is the classic chain: a
[heat transfer coefficient](heat-transfer-coefficient.md) times a temperature difference gives the
[heat flux density](heat-flux-density.md), and that times the area gives the heat flow in watts.

## See also

* [Power](power.md) — the type heat flow shares, with the full unit table, all decompositions and the complete operator
  surface
* [Heat flux density](heat-flux-density.md) — heat flow per unit of area
* [Heat transfer coefficient](heat-transfer-coefficient.md) — heat flux density per kelvin
* [Energy](energy.md) — heat flow integrated over time

## Notation

The table below shows how this quantity is written mathematically versus in Kotlin with KUnit. Exponents use Unicode
superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction.

| Mathematics | Kotlin                                     | Meaning                                  |
|-------------|--------------------------------------------|------------------------------------------|
| `W`         | `watts`                                    | heat flow, base unit (shared with power) |
| `kg·m²·s⁻³` | `grams * (meters pow 2) / (seconds pow 3)` | same quantity in base dimensions         |
| `Q̇ = Q / t` | `(21600 of kilo.joules) / runtime`         | heat flow from heat ÷ duration           |
| `Q = Q̇ · t` | `radiator * runtime`                       | heat from heat flow × duration           |
| `q̇ = Q̇ / A` | `radiator / surface`                       | heat flux density from heat flow ÷ area  |
