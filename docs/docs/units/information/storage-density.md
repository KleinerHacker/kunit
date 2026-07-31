# Storage Density

Package: `org.pcsoft.framework.kunit.it.storagedensity`
Base unit: **byte per square meter** (`KStorageDensityUnit.BASE == KStorageDensityUnit.BYTES_PER_SQUARE_METER`)

Type: **constructed unit**

Storage density is a **constructed** unit: it is not a single "real" quantity but a composition,
`storage · distance⁻²` (`B/m²`). `KStorageDensityUnitInstance` therefore wraps a `KMixedUnitInstance` of exactly two
terms - one `KStorageUnit.BASE` (byte) at exponent `+1` and one `KDistanceUnit.BASE` (meter)
at exponent `-2`. The value is always stored normalized to bytes per square meter, regardless of which unit or
storage/area combination it was created from.

## Building a storage density

A storage density is built as a **storage-per-area expression**, e.g. `100 of bytes / area`,
`5 of mega.bytes / area`. The area is any `KAreaUnitInstance` (e.g. `(1 of meters) * (1 of meters)`), so every SI/binary
prefix and length unit combines freely. Read it back in any storage-per-area template (`d into (bits / area)`). There
are deliberately **no** spelled-out composite tokens.

Base unit: a *byte* per square meter, consistent with the storage group. A "bit per square meter" is
`0.125 B/m²`.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)  // 1 m²
val d = 100 of bytes / area
d.value               // 100.0 (normalized to B/m²)
d into (bits / area)  // 800.0 (read back in bit/m²)
```

## Real-world example: areal density of an SSD die

A flash die stores **256 GB** on a surface of **100 mm²**. Its areal storage density is the amount of data divided by
the area:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val data = 256 of giga.bytes                       // 256 GB
val side = 10 of milli.meters                      // a 10 mm × 10 mm die = 100 mm²
val area = side * side
val density = data / area                          // KStorageDensityUnitInstance
density.value                                       // 2.56e15 (B/m²)
density into (giga.bytes / (side * side))           // 256.0 (GB per 100 mm²)
```

## Computing with the core units (storage & area)

A storage density *is* a storage amount divided by an area. Move between the three quantities - storage, area and
storage density - with plain `*` and `/`; each result is **strongly typed**.

| Expression                  | Result type                   | Meaning                 |
|-----------------------------|-------------------------------|-------------------------|
| `storage / area`            | `KStorageDensityUnitInstance` | density = amount / area |
| `storage density * area`    | `KStorageUnitInstance`        | amount = density × area |
| `area * storage density`    | `KStorageUnitInstance`        | amount (commutative)    |
| `storage / storage density` | `KAreaUnitInstance`           | area = amount / density |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)   // 1 m²

// --- core units -> storage density --------------------------------------
val d = (100 of bytes) / area   // KStorageDensityUnitInstance (NO .toStorageDensity() needed!)
d.value               // 100.0 (B/m²)

// --- storage density -> storage (multiply by an area) -------------------
val amount = d * area           // KStorageUnitInstance
amount into bytes     // 100.0
area * d              // same result (commutative)

// --- storage density -> area (divide a storage amount by it) ------------
val a = (600 of bytes) / d      // KAreaUnitInstance (6 m²)
```

!!! warning "Only a *pure* storage / area shape is a storage density"
`KMixedUnitInstance.toStorageDensity()` requires exactly one storage term at exponent `+1` and one distance term at
exponent `-2`. A `B²·m⁻²`, a `B·m⁻¹`, or a `B·m²` shape is not a storage density - the conversion throws
`IllegalStateException`. Likewise, `storage + storage density` (different dimensions) is a compile error.

## Operators

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)

// + / - : same group, automatic conversion between byte- and bit-based densities
val a = (1 of bytes / area) + (8 of bits / area)   // KStorageDensityUnitInstance, 2 B/m²
val b = (2 of bytes / area) - (8 of bits / area)   // 1 B/m²

// comparisons (by normalized B/m² value)
(1 of bytes / area) > (4 of bits / area)           // true
(1 of bytes / area) == (8 of bits / area)          // true

// * / / between two storage densities escape to a KMixedUnitInstance (no longer a pure density)
val squared = (10 of bytes / area) * (2 of bytes / area) // KMixedUnitInstance, [B^2, m^-4]
```

## SI and binary (IEC) prefixes

The storage-density group mirrors the [Storage](storage.md) group's prefix policy (its numerator is a storage amount):
the numerator uses the **augmenting** SI builders (`kilo`, `mega`, …) or the **binary**
builders (`kibi`, `mebi`, …); the denominator (an area) uses any length unit and prefix.

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.it.storage.*
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val mm2 = (1 of milli.meters) * (1 of milli.meters)  // 1 mm²
val d = 1 of kilo.bytes / mm2                         // 1 kB/mm²
d into (kilo.bytes / mm2)  // 1.0
```

## toString formatting

Only the base-unit `toString()` exists; format a specific unit via `into` or `format`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.format
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.it.storage.bytes
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.it.storagedensity.*

val area = (1 of meters) * (1 of meters)
((1000 of bytes) / area).toString()  // "1000.0 B/m²" (base unit)
((1000 of bytes) / area) format (kilo.bytes.toUnit() / area.toUnit()) // "1.0 kB/m^2"
```

## Notation

The table below shows how this unit and its components are written mathematically versus in Kotlin with KUnit. Exponents
use Unicode superscripts (`²`, `³`, `⁻¹`), `·` denotes multiplication and `/` a fraction. Where a quantity can be
written both as a fraction and as a product with negative exponents, both equivalent Kotlin forms are listed.

| Mathematics        | Kotlin                                | Meaning                                                            |
|--------------------|---------------------------------------|--------------------------------------------------------------------|
| `B/m²`             | `bytes / area`                        | storage density, base unit (byte per square meter) — fraction form |
| `B·m⁻²`            | `bytes * (meters pow -2)`             | same density as a product with a negative exponent                 |
| `bit/m²`           | `bits / area`                         | bit per square meter                                               |
| `kB/mm²`           | `kilo.bytes / mm2`                    | kilobyte per square millimeter                                     |
| `256 GB / 100 mm²` | `(256 of giga.bytes) / (side * side)` | build from storage ÷ area                                          |
