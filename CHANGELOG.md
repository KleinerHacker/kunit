# kunit Changelog

## [UNRELEASED]

### Added

- **New `Voltage` unit group** (`org.pcsoft.framework.kunit.voltage`): a *constructed* quantity
  (`mass¹ · distance² · time⁻³ · current⁻¹`, i.e. `kg·m²·s⁻³·A⁻¹`) with base unit **volt**
  (`KVoltageUnit.BASE == KVoltageUnit.VOLT`). Bare tokens `volts`, `statvolts` (CGS-ESU), `abvolts`
  (CGS-EMU), `westonCells` (Weston standard cell) and `daniells` (Daniell cell); full SI prefix support on
  every unit (`milli.volts` = mV, `kilo.volts` = kV). `+`/`-`/comparison and `equals`/`hashCode` operate on
  the normalized volt value (`(1 of kilo.volts) == (1000 of volts)`). It has **multiple equivalent
  decompositions**: the typed Ohm's-law form `resistance * current = voltage` (see Resistance) and the
  native canonical expression `mass·distance²/(time³·current)`, which is narrowed to a typed voltage via
  `KMixedUnitInstance.toVoltage()`. Both yield the same value-equal result. 100 % test coverage included.

- **New `Resistance` unit group** (`org.pcsoft.framework.kunit.resistance`): a *constructed* quantity
  (`mass¹ · distance² · time⁻³ · current⁻²`, i.e. `kg·m²·s⁻³·A⁻²`) with base unit **ohm**
  (`KResistanceUnit.BASE == KResistanceUnit.OHM`). Bare tokens `ohms`, `statohms` (CGS-ESU), `abohms`
  (CGS-EMU), `internationalOhms`, `legalOhms` and `siemensUnits`; full SI prefix support on every unit
  (`milli.ohms` = mΩ, `kilo.ohms` = kΩ). `+`/`-`/comparison and `equals`/`hashCode` operate on the
  normalized ohm value (`(1 of kilo.ohms) == (1000 of ohms)`). Ohm's-law cross operators tie voltage,
  resistance and current together: `voltage / current = resistance` (typed), `resistance * current = voltage`
  and its commutative `current * resistance = voltage`, and `voltage / resistance = current` (typed).
  Like voltage it has **multiple equivalent decompositions**: the typed `voltage / current` and the native
  canonical expression `mass·distance²/(time³·current²)`, narrowed via `KMixedUnitInstance.toResistance()`;
  both yield the same value-equal result. 100 % test coverage included.

- **New `Electric Current` unit group** (`org.pcsoft.framework.kunit.ec`): a plain, one-dimensional native
  group with base unit **ampere** (`KElectricCurrentUnit.BASE == KElectricCurrentUnit.AMPERE`). Besides the
  SI ampere it offers the two classic CGS current units, the **biot / abampere** (EMU, `1 Bi = 10 A`, tokens
  `biot` / `abamperes`) and the **statampere** (ESU, `1 statA ≈ 3.335 641 × 10⁻¹⁰ A`, token `statamperes`).
  Build and read with `of`/`into` through the bare tokens (`amperes`, `biot`, `statamperes`); full SI prefix
  support on every unit (`milli.amperes` = mA, `kilo.amperes` = kA). The group defines **no** cross-unit
  typed results (any `*`/`/` with a foreign group yields a generic mixed unit); `+`/`-`/comparison operate on
  the normalized ampere value, and `equals`/`hashCode` are by current quantity (`(1 of biot) == (10 of amperes)`);
  `KMixedUnitInstance.toElectricCurrent()` converts a single current term back to the pure wrapper. Full docs
  (EN/JA/ZH/KO) and 100 % test coverage included.

- **New `Frequency` unit group** (`org.pcsoft.framework.kunit.frequency`): a native, one-dimensional group
  and the **inverse of time** (`1 Hz = 1/s`) with base unit **hertz** (`KFrequencyUnit.BASE`). Bare tokens
  `hertz`, `rps` (revolutions/s), `fps` (frames/s), `rpm` (revolutions/min, 1/60 Hz) and `bpm`
  (beats/min, 1/60 Hz); full SI prefix support on every unit (`kilo.hertz` = kHz, `mega.hertz` = MHz,
  `giga.hertz` = GHz). `+`/`-`/comparison and `equals`/`hashCode` operate on the normalized hertz value
  (`(1 of kilo.hertz) == (1000 of hertz)`). Its cross-group operators are defined to be **exactly inverse
  to time**: `count / time = frequency` (typed, e.g. `1 / (2 of seconds)` = 0.5 Hz), `count / frequency =
  time`, `frequency * time = count` (dimensionless, commutative), `length * frequency = speed`
  (commutative), and `speed / frequency = distance`. `KMixedUnitInstance.toFrequency()` narrows a matching
  mixed unit. Full docs (EN/JA/ZH/KO) and 100 % test coverage included.

- **Scalar multiplication and division of units with plain numbers**: every unit can now be scaled by a
  `Number` while keeping its type and dimension — `unit * n`, `n * unit` and `unit / n` all return the
  same typed unit (e.g. `(12 of meters) * 3` stays a length, `Math.PI * (r * r)` stays an area, enabling
  circle-area style formulas directly through the unit system). `n / unit` inverts the dimension and
  yields a generic mixed unit (e.g. `1 / (2 of meters)` = m⁻¹, an inverse length). Scalar `+`/`-` remains
  unsupported. The affine **absolute temperature** (`KTemperatureUnitInstance`) deliberately rejects
  scalar `*`/`/` at compile time (scaling an affine point is meaningless); a linear **temperature
  difference** (`KTemperatureDifferenceUnitInstance`) scales normally.

- **New `Acceleration` unit group** (`org.pcsoft.framework.kunit.acceleration`): a *constructed*
  quantity (length · time⁻²) with base unit **m/s²** (`KAccelerationUnit.BASE`). Named tokens `gals`
  (Gal = cm/s²) and `standardGravities` (g₀ = 9.806 65 m/s²), both fully SI-prefixable (e.g. `milli.gals`
  = mGal). Typed operators bridge it to speed and time: `speed / time = acceleration`,
  `acceleration * time = speed` (commutative), `speed / acceleration = time`. `KMixedUnitInstance.toAcceleration()`
  narrows a matching mixed unit. Full docs (EN/JA/ZH/KO) and 100 % test coverage included.

- **New `Force` unit group** (`org.pcsoft.framework.kunit.force`): a *constructed* quantity
  (mass · length · time⁻²) with base unit **newton** (`KForceUnit.BASE`). Named tokens `newtons`,
  `dynes`, `poundsForce` and `ponds` (gram-force); the **kilopond / kilogram-force (kgf) is deliberately
  not a dedicated unit** — it is simply `kilo.ponds`, just as the kilonewton is `kilo.newtons`. Typed
  operator `mass * acceleration = force` (Newton's second law, commutative), plus `force / mass =
  acceleration` and `force / acceleration = mass`. `KMixedUnitInstance.toForce()` narrows a matching
  mixed unit. Full docs (EN/JA/ZH/KO) and 100 % test coverage included.

- **New `Pressure` unit group** (`org.pcsoft.framework.kunit.pressure`): a *constructed* quantity
  (mass · length⁻¹ · time⁻²) with base unit **pascal** (`KPressureUnit.BASE`). Named tokens `pascals`,
  `bars`, `atmospheres`, `psis`, `torrs`; hPa/kPa/**MPa (= N/mm²)** are reached via the prefixes
  (`hecto.pascals`, `kilo.pascals`, `mega.pascals`) rather than as own tokens. Typed operators
  `force / area = pressure`, `pressure * area = force` (commutative) and `force / pressure = area`.
  `KMixedUnitInstance.toPressure()` narrows a matching mixed unit. Full docs (EN/JA/ZH/KO) and 100 %
  test coverage included.

- **New `Density` unit group** (`org.pcsoft.framework.kunit.density`): a *constructed* quantity
  (mass · length⁻³) with base unit **kg/m³** (`KDensityUnit.BASE`). Density has no bare tokens (every
  spelling is a ratio) — it is built as an expression (`kilo.grams / (meters pow 3)`) or produced by the
  typed operator `mass / volume = density`, with the inverses `density * volume = mass` (commutative)
  and `mass / density = volume`. `KMixedUnitInstance.toDensity()` narrows a matching mixed unit. Full
  docs (EN/JA/ZH/KO) and 100 % test coverage included.

- **New `Area Density` unit group** (`org.pcsoft.framework.kunit.areadensity`): a *constructed* quantity
  (mass · length⁻²), the surface mass / areal load used e.g. in construction statics, with base unit
  **kg/m²** (`KAreaDensityUnit.BASE`). Like density it has no bare tokens; built as an expression
  (`kilo.grams / (meters pow 2)`) or produced by `mass / area = area density`, with `area density *
  area = mass` (commutative), `mass / area density = area`, and the density bridge
  `density * length = area density` / `area density / length = density`. `KMixedUnitInstance.toAreaDensity()`
  narrows a matching mixed unit. Full docs (EN/JA/ZH/KO) and 100 % test coverage included.

- **New `Mass` unit group** (`org.pcsoft.framework.kunit.mass`): a plain, one-dimensional native group
  with base unit **gram** (`KMassUnit.BASE == KMassUnit.GRAM`). The **kilogram is deliberately not a
  dedicated unit** — it is simply `kilo.grams` (the SI prefix `kilo` on the gram), like every other
  decimal magnitude. Units span metric (gram, tonne, metric carat), avoirdupois (grain, dram, ounce,
  pound, stone, US/UK hundredweight, short/long ton, slug), troy/apothecary (pennyweight, troy ounce,
  troy pound), historical/regional (German pound, Zentner, Lot, jin/catty, liang/tael, momme, kan) and
  scientific (dalton/u). Build and read with `of`/`into` through the bare tokens (`grams`, `pounds`,
  `troyOunces`, `jin`, `daltons`, …); full SI prefix support on every unit; `+`/`-`/comparison operate
  on the normalized gram value, and `equals`/`hashCode` are by mass quantity
  (`(1 of kilo.grams) == (1000 of grams)`); `KMixedUnitInstance.toMass()` converts a single mass term
  back to the pure wrapper. Full docs (EN/JA/ZH/KO) and 100 % test coverage included.

- **New `Temperature Difference` group** (`org.pcsoft.framework.kunit.temperature`): a **linear**
  (offset-free) counterpart to the affine temperature group, modelling a temperature *interval* rather
  than an absolute point. Kelvin only, no prefixes. Built explicitly via `KTemperatureDifference.ofKelvin(…)`
  or as the result of subtracting two absolute temperatures. Its symbol is rendered as **`ΔK`** (not `K`)
  so a difference is visually distinguishable from an absolute kelvin in mixed units and `toString`.
  Full docs (EN/JA/ZH/KO) and 100 % test coverage included.

- **Rankine (`°R`) added to the `Temperature` group**: absolute scale with Fahrenheit-sized degrees
  (`K = °R·5/9`), built/read via `of`/`into` through the `rankine` token.

### Changed

- **Breaking: absolute-temperature arithmetic corrected to be physically consistent.** Subtracting two
  absolute temperatures now yields a `KTemperatureDifferenceUnitInstance` in **kelvin** (e.g.
  `30 °C − 10 °C = 20 ΔK`), instead of the previous incorrect result that reinterpreted the kelvin
  difference as an absolute temperature (`≈ −253 °C`). An absolute temperature is now modelled as an
  affine point: `AbsTemp ± TemperatureDifference` yields an absolute temperature, while `AbsTemp + AbsTemp`
  is now a **compile error** (adding two absolute temperatures is physically meaningless). `*`/`/` and
  comparison are unchanged.

## [0.5.0]

### Added

- **New prefix-free `light` group for light-travel distances** (`org.pcsoft.framework.kunit.distance`):
  the light-second … light-year units are now grouped behind the `light` builder object and read almost
  like prose — `5 of light.seconds`, `3 of light.years` — via the properties `light.seconds`,
  `light.minutes`, `light.hours`, `light.days`, `light.weeks`, `light.years`. They deliberately accept
  **no** SI prefixes (a `kilo.lightYears` is physically meaningless). Full docs (EN/JA/ZH/KO) and 100 %
  test coverage included. **Breaking:** the previous bare tokens `lightSeconds` … `lightYears` and their
  prefixed forms (`kilo.lightYears`, …) were **removed**; migrate `n of lightYears` → `n of light.years`
  (analogously for second/minute/hour/day/week).

- **Historical volume units added to the `Distance` group** (`org.pcsoft.framework.kunit.distance`):
  **imperial bushel** (`imperialBushels`, 0.03636872 m³), **imperial hogshead** (`hogsheads`,
  0.32731785 m³), **imperial pint** (`imperialPints`, 0.00056826125 m³) and **imperial quart**
  (`imperialQuarts`, 0.0011365225 m³). All build/read via `of`/`into`, come with full SI prefix support
  and are covered by tests; docs (EN/JA/ZH/KO) and READMEs updated. (Imperial gallon, US liquid gallon
  and oil barrel already existed and are unchanged.)

- **Historical area units added to the `Distance` group** (`org.pcsoft.framework.kunit.distance`):
  **rood** (`roods`, 1011.7141056 m²), **square perch / square rod** (`squarePerches`, 25.29285264 m²),
  **Morgen (Prussian)** (`morgens`, 2553.22 m²), **Joch (Austrian)** (`jochs`, 5754.642 m²) and
  **Tagwerk (Bavarian)** (`tagwerks`, 3407.27 m²). All build/read via `of`/`into`, come with full SI
  prefix support and are covered by tests; docs (EN/JA/ZH/KO) and READMEs updated. (The international
  **acre** already existed and is unchanged.)

- **Historical length units added to the `Distance` group** (`org.pcsoft.framework.kunit.distance`):
  **cubit** (`cubits`, 0.4572 m), **Roman foot / pes** (`romanFeet`, 0.2957 m), **Roman pace / passus**
  (`romanPaces`, 1.4787 m), **stadium** (`stadia`, 185.0 m), **Roman mile / mille passus** (`romanMiles`,
  1481.5 m), **rod / perch** (`rods`, 5.0292 m), **league** (`leagues`, 4828.032 m), **cable length**
  (`cableLengths`, 185.2 m), **verst** (`versts`, 1066.8 m) and **Prussian mile** (`prussianMiles`,
  7532.5 m). All build/read via `of`/`into`, come with full SI prefix support and are covered by tests;
  docs (EN/JA/ZH/KO) and READMEs updated. (Nautical mile, statute mile, fathom, furlong and inch already
  existed and are unchanged.)

- **New `Temperature` unit group** (`org.pcsoft.framework.kunit.temperature`) with **Kelvin** (base),
  **Celsius** and **Fahrenheit**. It is the framework's first **affine** group: conversions are
  offset-and-scale (`°C = K − 273.15`, `°F = (K − 273.15)·9/5 + 32`), not a single factor. Build and read
  with the usual verbs — `25 of celsius`, `t into fahrenheit` — through the bare tokens `kelvin`,
  `celsius`, `fahrenheit`. Values are stored as absolute kelvin, so `*`/`/`/`pow` run through the generic
  engine unchanged; the group has **no prefixes**. `+`/`-`/comparison operate on absolute kelvin, and
  `equals`/`hashCode` are by absolute temperature (`(0 of celsius) == (273.15 of kelvin)`). Full docs
  (EN/JA/ZH/KO) and 100 % test coverage included.

### Changed

- **`KUnitMeasurable` gains a `readBaseValue(baseValue)` hook** behind `into` (mirroring `scaledBy`
  behind `of`). The default linear behaviour (`baseValue / value`) is implemented on
  `KMixedUnitInstance` and inherited by every "pure" wrapper via delegation, so `into` is unchanged for
  all existing groups; only non-linear groups (the new affine temperature group) override it. This keeps
  reading correct for every group without shadow-prone `into` overloads. Not a breaking change for
  existing units.

- **README architecture diagram reconciled with the current code.** The class diagram in all four READMEs
  (`README.md` + ko/zh/ja) no longer references the removed `KDerivedUnit`/`KDistanceDerivedUnit` types;
  it now shows the real `KPrefixBuilder` → `KUnitPrefix` relationship instead.
- **Breaking — construction/reading DSL fully replaced by `of` / `into`.** Number and unit are now
  strictly separated: build with `number of <unit-expression>` and read with `value into <unit>`.
  - **Construction:** `10.5 of kilo.meters`, `10.5 of kilo.meters / milli.seconds`, `2 of hectares`,
    `10 of meters * (milli.seconds pow 2)`. `Number.of` is a single group-agnostic infix that scales a
    **value-1 unit template** (backed by a new `KUnitMeasurable.scaledBy`), preserving the strong result
    type (`KLengthUnitInstance`, `KSpeedUnitInstance`, …).
  - **Reading:** `v into kilo.meters`, `area into hectares` (returns `Double`). `into` replaces **all**
    `valueAs(...)` overloads, which were removed everywhere.
  - **Prefix builders.** Prefixes are now builder values (`kilo`, `milli`, …) exposing value-1 unit
    templates via property access (`kilo.meters`, `milli.seconds`). A compile-time hierarchy
    (`KPrefixBuilder` → `KDiminishingPrefixBuilder` / `KAugmentingPrefixBuilder`) enforces which units
    accept which prefixes: `bytes`/`bits` hang only on the augmenting (and binary IEC) builder, so
    `milli.bytes` is a **compile error** while `kilo.bytes`/`kibi.bytes` are valid. Binary IEC prefixes
    are the builder values `kibi`, `mebi`, … (`KStorageBinaryPrefixBuilder`).
  - **Bare tokens are now value-1 instances.** `meters`, `seconds`, `bytes`, `hectares`, `liters`, … are
    `K…UnitInstance` values (1 unit), used as the template for both `of` and `into`. Special/derived units
    (`hectares`, `ares`, `acres`, `liters`, gallons, …) are now named value-1 instances instead of
    `KDerivedUnit` targets. Constructed groups (speed, data rate) drop their spelled-out composite tokens
    entirely - a speed/rate is written as an expression (`meters / seconds`, `bytes / seconds`); only
    genuinely single-named speeds (`knots`, `mach`, `speedOfLight`) remain as tokens.
- **Tests reorganized per aspect.** Each group's single aggregate test class was split into the
  group-named per-aspect classes mandated by the test rules — `K<Group>UnitSystemTest` (the
  `K<Group>UnitInstance` surface), `K<Group>OperatorTest` (all operators), `K<Group>UnitTest` (the
  concrete units / bare values), `K<Group>PrefixTest` (the prefixes) — with a class omitted where the
  group has no such logic (e.g. no `PrefixTest` for the composed speed/data-rate groups, and only
  `System`/`Operator` for the root mixed unit). No behavioural change; coverage is unchanged.
- **Internal simplification (no behavioural change).** Removed unreachable defensive fallbacks so the
  code carries no dead branches: the in-hierarchy `KDistanceUnitInstance.toLength`/`toArea`/`toVolume`
  now use a direct cast (the exponent check already guarantees the concrete leaf type), and
  `combineUnits` inserts the first operand's terms directly (a single mixed unit never repeats a unit).
  Test coverage is now 100 % across line, branch, method and class.

### Removed

- **Breaking.** All previous construction/reading surface: the `Number.xxx` creator properties
  (`5.meters`, `2.hours`, …); every `valueAs(...)` and the custom-unit `toString(target)` overloads (on
  the wrappers **and** `KMixedUnitInstance`); the per-group prefix `infix` functions (`5 kilo meters`,
  `5 milli seconds`, `5 kibi bytes`, …) and their files (`K*UnitPrefix.kt`); the composite bare-value
  tokens `metersPerSecond`/`kilometersPerHour`/`bytesPerSecond`/… — removed entirely (build a speed/rate
  as an expression, `meters / seconds`); the entire `KUnitTarget` system — `KUnitTarget`, `KScaledUnit`,
  `KDerivedUnit`, `KScaledDerivedUnit`, `KBinaryScaledUnit` and every `with` infix — plus
  `KDistanceDerivedUnit`. Reading in a specific unit is now `into` with a value-1 template; there is no
  custom-unit `toString` (format via `"${v into kilo.meters} km"`).

### Added

- `Number.of` / `KUnitMeasurable.into` (root, `KUnitMeasurable.kt`), `KUnitMeasurable.scaledBy`, and the
  `KPrefixBuilder` hierarchy with all 24 SI builder values plus the binary `KStorageBinaryPrefixBuilder`
  values. Per-group value-1 bare tokens and prefix builder properties
  (`val KPrefixBuilder.meters`, `val KAugmentingPrefixBuilder.bytes`, …).

<details><summary>Earlier [UNRELEASED] entries (storage / data-rate groups, pre-DSL-overhaul)</summary>

### Added

- **Data-rate unit group** (`org.pcsoft.framework.kunit.datarate`): a new *constructed* group for data
  transfer rates, `storage · time⁻¹`, base unit **byte per second** (`KDataRateUnit.BYTES_PER_SECOND`),
  with **bit per second** (`KDataRateUnit.BITS_PER_SECOND`, 0.125 B/s). Creator properties
  `Number.bytesPerSecond`/`Number.bitsPerSecond`, bare aliases `bytesPerSecond`/`bitsPerSecond`, and the
  `KMixedUnitInstance.toDataRate()` conversion. `KDataRateUnitInstance` wraps a two-term
  `[KStorageUnit.BASE¹, KTimeUnit.BASE⁻¹]` instance, always normalized to B/s.
  - **Cross-group operators**: `storage / time = data rate`, `data rate * time = storage`,
    `time * data rate = storage`, `storage / data rate = time`, each strongly typed (e.g.
    `100.bytes / 10.seconds` is a `KDataRateUnitInstance`, no `toUnit()` needed).
  - **Prefix policy mirrors storage** (the numerator): only the non-diminishing SI prefixes (`deca`
    upward) are offered - `5 milli bytesPerSecond` is a **compile error** - plus the binary IEC prefixes
    (`kibi`, `mebi`, …, reused from `KStorageBinaryPrefix`), so a rate can distinguish 1000 (`kilo`) from
    1024 (`kibi`). Whole-rate `valueAs`/`toString` targets accept a bare `KDataRateUnit`, an SI-scaled
    (`KUnitPrefix.KILO with bytesPerSecond` → `kB/s`) or a binary-scaled one
    (`KStorageBinaryPrefix.KIBI with bytesPerSecond` → `KiB/s`); no root `resolve()` change was needed
    (the storage `KBinaryScaledUnit` branch already covers it).
  - Full parameterized test suite (conversion, operator and comparison matrices, decimal + binary
    prefix × unit matrices, `toString`, and the bidirectional cross-group storage × time decomposition)
    and a dedicated MkDocs page (`docs/docs/units/datarate.md` + ko/zh/ja).
- **Storage unit group** (`org.pcsoft.framework.kunit.storage`): a new predefined group for digital data
  amounts, base unit **byte** (`KStorageUnit.BYTE`), with **bit** (`KStorageUnit.BIT`, 0.125 B). Creator
  properties `Number.bytes`/`Number.bits`, bare aliases `bytes`/`bits`, and the
  `KMixedUnitInstance.toStorage()` conversion. It is the first *plain, one-dimensional*, `Double`-backed
  wrapper shape (`KStorageUnitInstance`).
  - **No diminishing prefixes.** Only the non-diminishing SI prefixes (`deca` upward) are offered;
    `deci`/`centi`/`milli`/… do not exist for storage, so e.g. `5 milli bytes` is a **compile error**.
  - **Binary (IEC) prefixes.** A second prefix system `KStorageBinaryPrefix` (`kibi`, `mebi`, `gibi`, …,
    powers of 1024) with matching `infix` constructors and a `KBinaryScaledUnit` `valueAs`/`toString`
    target (`KStorageBinaryPrefix.KIBI with bytes`), so a value can distinguish 1000 (`kilo`) from 1024
    (`kibi`).
  - Full parameterized test suite (conversion, operator and comparison matrices, decimal + binary
    prefix × unit matrices, `toString`, and the cross-group storage × time decomposition) and a dedicated
    MkDocs page (`docs/docs/units/storage.md` + ko/zh/ja).
- `KMixedUnitInstance.resolve()` now also resolves the storage group's `KBinaryScaledUnit` target.
- **General cross-group `*`/`/` operators.** Any two "pure" units can now be multiplied/divided directly
  across group boundaries (e.g. `20.bytes / 20.seconds`) without first calling `toUnit()`, yielding a
  `KMixedUnitInstance`. Provided by one group-agnostic pair of extension operators
  `KUnitInstance<*>.times/div(KUnitInstance<*>)` in `KUnitMeasurable.kt`; statically-typed cross-group
  results (e.g. `length / time = speed`, `length * length = area`) are preserved via overload resolution.

### Changed

- **`.claude/CLAUDE.md` reconciled with the actual code**: documented `KUnitTarget`, `KUnitTerm`, `KScaledUnit`/
  `KScaledDerivedUnit` + `with`, the `KDerivedUnit` fields and per-group `object`, the real root-package
  file layout (no `KUnitInstance.kt`/`KDerivedUnit.kt`), the "unit-enum = group name / wrapper = dimension
  name" rule, the three wrapper shapes (dimensioned / `Duration`-backed / plain one-dimensional), and that
  a group may offer a prefix subset and/or an alternative prefix system.

</details>

## [0.3.0]

### Added

- **Dimensioned distance subtypes.** The distance group now models exponents as their own types under an
  open base `KDistanceUnitInstance` (any exponent): `KLengthUnitInstance` (exponent 1),
  `KAreaUnitInstance` (2) and `KVolumeUnitInstance` (3). Results whose exponent leaves `{1,2,3}` fall back
  to `KDistanceUnitInstance`; a dimensionless result (exponent 0) is a `KMixedUnitInstance`.
  - Same-group `*`/`/` are now **strongly typed**: `length * length = area`, `area / length = length`,
    `volume / area = length`, etc., instead of always returning a raw `KMixedUnitInstance`.
  - Cross-dimension `+`/`-`/comparison (`length + area`, `length < volume`, …) are now a **compile
    error** — no such operator exists — rather than a runtime `IllegalStateException`.
- **Exponentiation via `pow`.** A group-agnostic infix power operation raises any unit to an integer
  power: `2.meters pow 2` (= `(2 m)² = 4 m²`, a `KAreaUnitInstance`), `2 kilo meters pow 2`
  (= 4 000 000 m²), `2.meters pow 3` (a volume), `2.hours pow 2` (a generic `KMixedUnitInstance`), and it
  chains (`x pow 2 pow 2`). The value is powered and every term's exponent multiplied by `n`; `pow 0` is
  dimensionless. This is the single power syntax across all groups (Kotlin has no overloadable `^`). For
  distance the result is dimensioned (`KDistanceUnitInstance.pow`); for other groups it is a
  `KMixedUnitInstance`.
- The named derived units (`ares`/`hectares`/`acres`, `liters`/… ) return
  `KAreaUnitInstance`/`KVolumeUnitInstance`.
- New conversions `KMixedUnitInstance.toDistance()`/`toLength()`/`toArea()`/`toVolume()`.
- **In-hierarchy narrowing** `KDistanceUnitInstance.toLength()`/`toArea()`/`toVolume()`: a general distance
  value (or any leaf) can now be narrowed to a specific dimension directly, mirroring the
  `KMixedUnitInstance` extensions (exponent-checked, throws `IllegalStateException` on mismatch). Previously
  only `toDistance()` existed on the wrapper and the narrowing conversions lived solely on
  `KMixedUnitInstance`.

### Changed

- **Tests: prefix × unit matrices now construct through the bare-value DSL.** The prefix cross-matrices
  for every group build their instances via the bare-value aliases (`5 kilo meters`, `5 milli seconds`,
  `5 kilo metersPerHour`, …) instead of the raw enum/wrapper values, so the `K*UnitBareValues.kt` aliases
  are now fully covered and every unit × every prefix runs through the real DSL. The distance area/volume
  matrices now raise a prefixed length via `pow` (`(5 kilo meters) pow 2`). Test-construction policy
  documented in `.claude/CLAUDE.md`.
- **Breaking:** the length group was renamed to **distance** — package
  `org.pcsoft.framework.kunit.length` → `…kunit.distance`, `KLengthUnit` → `KDistanceUnit`,
  `KLengthDerivedUnit` → `KDistanceDerivedUnit`. `KLengthUnitInstance` is retained but now denotes the
  exponent-1 leaf (a length); the general "any exponent" wrapper is the new `KDistanceUnitInstance`.
- **Breaking:** conversion accessors dropped their hard class names for a natural DSL:
  `toKMixedUnitInstance()` → `toUnit()`, `toKTimeUnit()` → `toTime()`, `toKSpeedUnit()` → `toSpeed()`,
  `toKLengthUnit()` → `toDistance()` (plus the new exponent-checked `toLength()`/`toArea()`/`toVolume()`).
- **Breaking:** `KUnitInstance<SELF>` no longer declares `times(SELF)`/`div(SELF)`. Same-group
  multiplication/division comes from `KUnitMeasurable` (against a `KMixedUnitInstance`) plus the
  group-specific typed overloads; this split is what lets the distance leaves narrow their `*`/`/` return
  types without a signature clash.
- The general `KDistanceUnitInstance` is intentionally **not additive** (no `plus`/`minus`/`compareTo`):
  cross-dimension addition lives only on the leaf types, which is what makes `length + area` a compile
  error. Add two `m⁴` values through the mixed engine instead.
- **DSL file reorganization** (internal, no API change): creator/bare-value declarations are now
  co-located per dimension. The catch-all `KDistanceUnitExtensions.kt` was split into per-dimension
  `KLengthUnitExtensions.kt`/`KAreaUnitExtensions.kt`/`KVolumeUnitExtensions.kt` (creators) and
  `KLengthUnitBareValues.kt`/`KAreaUnitBareValues.kt`/`KVolumeUnitBareValues.kt` (bare unit
  references/tokens). Speed and time likewise split their bare references into
  `KSpeedUnitBareValues.kt`/`KTimeUnitBareValues.kt`. The bare area/volume prefix tokens moved out of
  `KDistanceUnitPrefix.kt` into the new `*BareValues.kt` files. The distance tests follow the same
  per-dimension layout: the length instance tests moved into `KLengthUnitInstanceTest` (alongside the
  existing `KAreaUnitInstanceTest`/`KVolumeUnitInstanceTest`), and the shared cross-dimension generator
  matrices/helpers were extracted into `KDistanceTestFixtures.kt`.

### Removed

- **Named `squareXxx`/`cubicXxx` area and volume constructors** and the prefixed area/volume DSL —
  superseded by `pow`. Removed: the `Number.squareMeters`/`squareMiles`/… and
  `Number.cubicMeters`/`cubicMiles`/… creator properties, the `n kilo squareMeters` / `n kilo cubicMeters`
  prefix `infix` overloads, and the supporting token types/aliases (`KDistanceAreaUnit`,
  `KDistanceVolumeUnit`, `KAreaUnitBareValues.kt`, `KVolumeUnitBareValues.kt`). Write `2.meters pow 2`
  instead of `2.squareMeters`, and `(2 kilo meters) pow 2` instead of `2 kilo squareMeters`. The named
  derived special units (`hectares`, `ares`, `acres`, `liters`, `usGallons`, …) are unaffected.

## [0.2.0]

### Added

- New **constructed** unit group **Speed** (`org.pcsoft.framework.kunit.speed`) — the first composed
  quantity (length·time⁻¹):
  - Units: meter per second (base), kilometer per hour, mile per hour, knot, foot per second, Mach
    (ISA sea-level speed of sound, `Ma = 340.29 m/s`) and the speed of light (`c = 299 792 458 m/s`),
    with matching `Number.metersPerSecond` … `Number.speedOfLight` creators, bare `val` aliases and
    the 24 SI-prefix `infix` constructors (`5 kilo metersPerSecond`).
  - `KSpeedUnitInstance` stores a two-term `[m¹, s⁻¹]` mixed instance normalized to m/s and offers the
    standard "pure" unit surface (`value`, `valueAs`, `+`/`-`/`*`/`/`, comparisons, `toString`,
    `toKMixedUnitInstance`), reading back either as a whole speed unit or as a length-per-time pair,
    plus `KMixedUnitInstance.toKSpeedUnit()`.
  - **Direct cross-group operators** so the core units combine straight into a typed speed and back,
    without touching a raw `KMixedUnitInstance`: `length / time → speed`, `speed * time → length`,
    `time * speed → length`, `length / speed → time`. Non-speed shapes (e.g. `area / time`) fail with
    `IllegalStateException` rather than producing a misleading value.
  - Dedicated MkDocs page (`docs/docs/units/speed.md`) with Korean, Chinese and Japanese translations,
    under a new "Constructed Units" navigation section, including a thorough "computing with the core
    units" section.
- Comprehensive **parameterized cross-matrix tests** for every unit group and the mixed unit, built on
  JUnit Jupiter `@ParameterizedTest`/`@MethodSource` (new `junit-jupiter-params` test dependency): a full
  prefix × unit matrix plus one standalone test per SI prefix, a unit → every-other-unit conversion
  matrix, one method per operator (`+`/`-`/`*`/`/`) and per comparison (`==`/`!=`/`<`/`<=`/`>`/`>=`) over
  every unit pair, per-unit `toString`/`toString(target)`, and a length × time cross-group matrix for the
  mixed unit. The procedure is documented in `.claude/CLAUDE.md` as mandatory for future groups.
- Per-group SI-prefix `infix` constructors (`KLengthUnitPrefix.kt`, `KTimeUnitPrefix.kt`): `5 kilo meters`
  now returns a `KLengthUnitInstance` directly (and `5 milli seconds` a `KTimeUnitInstance`), rather than an
  intermediate builder. `5 kilo meters` is exactly equivalent to `5000.meters` and is the preferred
  construction form.
- Test coverage in `KMixedUnitInstanceTest` verifying that `KMixedUnitInstance` term exponents are
  added on `*` and subtracted on `/` — including crossings of the 0-point (negative→positive and
  positive→negative) and the removal of a term whose exponent cancels to exactly `0` — both for pure
  terms and when combining with other mixed units.

- New light-distance units in the **Length** group: light-second (`ls`), light-minute (`lmin`),
  light-hour (`lh`), light-day (`ld`) and light-week (`lw`), complementing the existing light-year.
  Defined via the speed of light (`c = 299 792 458 m/s`) and the Julian time base, with matching
  `Number.lightSeconds` … `Number.lightWeeks` creators and bare `val` aliases.
- New unit group **Time** (`org.pcsoft.framework.kunit.time`):
  - Units: second (base), minute, hour, day. Calendar-based units (week, year) are intentionally
    excluded, since they are defined by calendars rather than by a fixed physical quantity.
  - `KTimeUnitInstance` is a 100 % wrapper around `java.time.Duration` (the `Duration` is the source of
    truth and the full `Duration` API is forwarded), additionally offering the standard "pure" unit surface
    (`value`, `valueAs`, `+`/`-`/`*`/`/`, comparisons, `toString`, `toKMixedUnitInstance`) plus `toDuration()`,
    `Duration.toKTimeUnit()` and `KMixedUnitInstance.toKTimeUnit()`.
  - Sub-second scales are reached via the SI prefixes on `second`; time is always exponent 1 (no derived
    units). Values outside roughly `[1 ns, Long.MAX seconds]` are not representable by the Duration backing.
  - Dedicated MkDocs page (`docs/docs/units/time.md`) with Korean, Chinese and Japanese translations.
- Common interface hierarchy for all unit-value types:
  - `KUnitMeasurable` — the group-agnostic surface shared by every value (`value`,
    `toKMixedUnitInstance()`, `*`/`/` against a `KMixedUnitInstance`). The "pure" wrappers implement it
    via Kotlin `by` delegation to their internal `KMixedUnitInstance`.
  - `KUnitInstance<SELF>` — the self-typed (F-bounded) interface of the "pure" wrappers, adding
    same-type `+`/`-`/comparison, same-group `*`/`/`, and single-target `valueAs`/`toString`.

### Changed

- **Breaking:** the number-extension creators are now extension **properties** instead of functions —
  construct pure units with `5.meters`, `2.hours`, `5.hectares` (previously `5.meters()`, `2.hours()`,
  `5.hectares()`). The `toXxx()` conversions (`toKMixedUnitInstance()`, `toKLengthUnit()`,
  `toKTimeUnit()`, `toDuration()`) remain functions.
- **Breaking:** the prefix `infix` functions are now declared **per group** (over the group's own unit
  type) and return the concrete "pure" unit directly, replacing the single set of generic root-package
  functions that returned a `KPrefixBuilder`. `(5 kilo meters).toKMixedUnitInstance().toKLengthUnit()`
  becomes simply `5 kilo meters`.
- Migrated the throwaway private `TimeUnit` test enums to the official `KTimeUnit`.
- **Breaking:** renamed the mixed-unit class `KUnitInstance` → `KMixedUnitInstance` (the name
  `KUnitInstance` is now the "pure" wrapper interface) and its conversion accessor
  `toKUnitInstance()` → `toKMixedUnitInstance()`.
- **Breaking:** renamed the "pure" wrapper accessor `valueIn(target)` → `valueAs(target)`, matching
  `KMixedUnitInstance.valueAs`.
- **Breaking:** the `KMixedUnitInstance` primary constructor is now `internal`, matching the "pure"
  wrapper classes (`KLengthUnitInstance`, `KTimeUnitInstance`). Externally, mixed units may only be
  obtained via `toKMixedUnitInstance()`, operator results, or the number-extension creators — never by
  constructing `KMixedUnitInstance(...)` directly.

### Removed

- **Breaking:** the `KPrefixBuilder` class and the generic, root-package prefix `infix` functions that
  returned it. Prefix construction now goes through the per-group functions that return the concrete unit
  directly (see _Changed_).

## [0.1.0]

### Added

- Core mixed-unit engine (`KUnitInstance`, `KUnitTerm`) supporting arbitrary combinations of units and
  exponents, with `+`, `-`, `*`, `/` operators and a `hasSameUnits` check for unit/exponent equality.
- Full SI prefix support (`KUnitPrefix`, Quetta/Q to Quecto/q) usable with any unit via generic `infix`
  construction, e.g. `5 kilo meters`.
- Support for special/derived units per unit group and exponent (`KDerivedUnit`, `KScaledDerivedUnit`), e.g.
  hectare for area or liter for volume.
- New unit group **Length** (`org.pcsoft.framework.kunit.length`):
  - Units: meter, mile, nautical mile, yard, foot, inch, fathom, chain, furlong, astronomical unit,
    light-year, parsec.
  - Multi-dimensional support for area (exponent 2) and volume (exponent 3), including the special units
    are, hectare, acre (area) and liter, US gallon, imperial gallon, US fluid ounce, oil barrel (volume).
  - Full operator (`+`, `-`, `*`, `/`) and comparison (`==`, `!=`, `<`, `<=`, `>`, `>=`) support, with
    automatic conversion between length units and `toString`/`valueAs` output in any target unit.
- Creation of units and mixed units from any `Number` type (`Int`, `Long`, `Float`, `Double`, ...), always
  normalized to `Double` internally.
