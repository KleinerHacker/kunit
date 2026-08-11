# Compressibility

Package: `org.pcsoft.framework.kunit.mechanic.compressibility`
Base unit: **reciprocal pascal**
(`KCompressibilityUnit.BASE == KCompressibilityUnit.RECIPROCAL_PASCAL`)

Type: **constructed unit**

The compressibility `κ = −(1/V)·(∂V/∂p)` says how much a material's volume shrinks per unit of pressure.
It is the exact reciprocal of the **bulk modulus** `K`, which is an elastic modulus and therefore a
[pressure](pressure.md). Water is ≈ 4.5 × 10⁻¹⁰ Pa⁻¹ — which is why hydraulics can treat it as
incompressible.

Its canonical base-dimension normal form is `mass⁻¹ · length · time²`.

## Named units

| Unit                            | Symbol  |                   Token | 1 unit in 1/Pa |
|---------------------------------|---------|------------------------:|---------------:|
| Reciprocal pascal               | `1/Pa`  |     `reciprocalPascals` |            1.0 |
| Reciprocal bar                  | `1/bar` |        `reciprocalBars` |           1e-5 |
| Reciprocal standard atmosphere  | `1/atm` | `reciprocalAtmospheres` |      1/101 325 |

All tokens accept every SI prefix (`pico.reciprocalPascals`, …). Like the neighbouring pressure group the
instance stores its **raw gram-based component value**.

## Computing with the group

| Expression                    | Result type                     | Meaning                        |
|-------------------------------|---------------------------------|--------------------------------|
| `1 / pressure`                | `KCompressibilityUnitInstance`  | `κ = 1 / K`                    |
| `1 / compressibility`         | `KPressureUnitInstance`         | `K = 1 / κ`                    |
| `compressibility * pressure`  | `Double`                        | relative volume change `ΔV/V`  |

The two reciprocals are exact: the component bases (`g·m⁻¹·s⁻²` for the pressure, `g⁻¹·m·s²` here) are
reciprocals of one another, so no bridging factor is involved.

## Real-world example — how much does water compress?

Water has a bulk modulus of about **2.2 GPa**. What is its compressibility, and how much does it shrink
under 10 MPa (roughly 1000 m of water depth)?

```kotlin
import org.pcsoft.framework.kunit.*
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.compressibility.*

val kappa = 1 / (2.2 of giga.pascals)          // KCompressibilityUnitInstance
kappa into reciprocalPascals                    // ≈ 4.545e-10

val shrink = kappa * (10 of mega.pascals)       // Double
shrink                                           // ≈ 0.00455 — 0.45 % volume loss

// And back to the bulk modulus
(1 / kappa) into giga.pascals                    // ≈ 2.2
```

## Value semantics

`equals`/`hashCode` compare the **normalized component value**, so
`(1 of reciprocalBars) == (1e-5 of reciprocalPascals)`. `toString()` renders the value in the base unit:
`"1.0 1/Pa"`.

## See also

* [Pressure](pressure.md) — the reciprocal quantity (the bulk modulus).
* [Stress & Elastic Modulus](stress.md) — the same type read as a material property.
* [Mechanics overview](overview.md)
