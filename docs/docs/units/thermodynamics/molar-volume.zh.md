# 摩尔体积

包：`org.pcsoft.framework.kunit.thermo.molarvolume`
基本单位： **立方米每摩尔**（`KMolarVolumeUnit.BASE == KMolarVolumeUnit.CUBIC_METERS_PER_MOLE`）

类型： **构造单位**

摩尔体积是每单位物质的量的体积：`volume / amountOfSubstance`（`m³/mol`）。对于理想气体而言， 在 0 °C 和 100 kPa
下，任何物质的摩尔体积都相同（22.711 l/mol）；对于固体和液体， 它由[摩尔质量](molar-mass.md)和密度推导而来。

`KMolarVolumeUnitInstance` 包装了一个恰好由两项组成的 `KMixedUnitInstance`，处于规范正规形式
`distance³ · substance⁻¹`（`m³·mol⁻¹`），始终以 m³/mol 归一化。两个分量都以其单位组的基本单位存储， 因此原始分量基本单位
*就是*该命名基本单位。

[元素周期表](../../periodic-table.md)中的每个元素都通过下方的第二种分解方式， 从其摩尔质量和密度推导出摩尔体积。

## 命名单位

| 单位           | 符号       |                      令牌 | 1 单位相当于多少 m³/mol |
|----------------|------------|--------------------------:|------------------------:|
| 立方米每摩尔   | `m^3/mol`  |      `cubicMetersPerMole` |                     1.0 |
| 升每摩尔       | `l/mol`    |           `litersPerMole` |                   0.001 |
| 立方厘米每摩尔 | `cm^3/mol` | `cubicCentimetersPerMole` |                  1.0e-6 |

所有单位均支持完整的 SI 前缀范围（`milli.cubicMetersPerMole`、`milli.litersPerMole` 等）。 该包还额外提供了常量
`MOLAR_VOLUME_IDEAL_GAS_STP` = 0.02271095464（m³/mol）， 即标准条件下理想气体的摩尔体积。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val ideal = MOLAR_VOLUME_IDEAL_GAS_STP of cubicMetersPerMole
ideal into litersPerMole          // ≈ 22.711
ideal into cubicCentimetersPerMole // ≈ 22711.0
```

## 现实示例：装满氦气的气球

标准条件下 2 摩尔理想气体占据多大空间——一个 5 升的气球又能装下多少摩尔气体？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val ideal = MOLAR_VOLUME_IDEAL_GAS_STP of cubicMetersPerMole

// 2 摩尔的体积
val volume = ideal * (2 of moles) // KVolumeUnitInstance
volume into liters                // ≈ 45.42 l

// 5 升的气球能装下多少摩尔？
val amount = (5 of liters) / ideal // KAmountOfSubstanceUnitInstance
amount into moles                  // ≈ 0.2202 mol

// 以及从充气气球测得的摩尔体积：
val measured = (45.42 of liters) / (2 of moles)
measured into litersPerMole        // ≈ 22.71
```

## 现实示例：一摩尔水的体积

水的摩尔质量为 18.015 g/mol，密度为 1 kg/l，因此一摩尔水约占 18 cm³ —— 大约一汤匙的量。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val density = (1 of kilo.grams) / (1 of liters)      // KDensityUnitInstance
val molarVolume = (18.015 of gramsPerMole) / density // KMolarVolumeUnitInstance
molarVolume into cubicCentimetersPerMole             // 18.015
```

## 用核心单位计算

| 表达式                            | 结果类型                         | 含义                       |
|-----------------------------------|----------------------------------|----------------------------|
| `volume / amountOfSubstance`      | `KMolarVolumeUnitInstance`       | 摩尔体积                   |
| `molarMass / density`             | `KMolarVolumeUnitInstance`       | 摩尔体积（第二种分解方式） |
| `molarVolume * amountOfSubstance` | `KVolumeUnitInstance`            | 总体积                     |
| `amountOfSubstance * molarVolume` | `KVolumeUnitInstance`            | 总体积（可交换）           |
| `volume / molarVolume`            | `KAmountOfSubstanceUnitInstance` | 涉及的物质的量             |
| `molarVolume * density`           | `KMolarMassUnitInstance`         | [摩尔质量](molar-mass.md)  |
| `density * molarVolume`           | `KMolarMassUnitInstance`         | 摩尔质量（可交换）         |
| `molarMass / molarVolume`         | `KDensityUnitInstance`           | 密度                       |

## 分解方式

所有分解方式都产生相同的类型化、值相等的实例。

| 分解方式                     | 形式                           | 结果                                |
|------------------------------|--------------------------------|-------------------------------------|
| `volume / amountOfSubstance` | 类型化操作符                   | 直接得到 `KMolarVolumeUnitInstance` |
| `molarMass / density`        | 类型化操作符                   | 直接得到 `KMolarVolumeUnitInstance` |
| `distance³ · substance⁻¹`    | 原生表达式 + `toMolarVolume()` | `KMolarVolumeUnitInstance`          |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.liters
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.amountofsubstance.moles
import org.pcsoft.framework.kunit.thermo.molarmass.gramsPerMole
import org.pcsoft.framework.kunit.thermo.molarvolume.*

// 类型化操作符形式：体积 / 物质的量
val typedVolume = (0.018015 of liters) / (1 of moles)

// 类型化操作符形式：摩尔质量 / 密度
val typedMolarMass = (18.015 of gramsPerMole) / ((1 of kilo.grams) / (1 of liters))

// 原生基础维度形式（m³·mol⁻¹），由 toMolarVolume() 识别
val native = (((18.015e-6 of (meters pow 3)).toUnit()) / (1 of moles).toUnit()).toMolarVolume()

typedVolume == typedMolarMass // true
typedVolume == native         // true —— 全部都是 1.8015e-5 m³/mol
```

`toMolarVolume()` 只识别规范正规形式；错误的形状会抛出 `IllegalStateException`。

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarvolume.*

val total = (10 of litersPerMole) + (4 of litersPerMole) // 14 l/mol
val rest  = (10 of litersPerMole) - (4 of litersPerMole) // 6 l/mol

(1 of litersPerMole) > (500 of cubicCentimetersPerMole)   // true
(1 of litersPerMole) == (1000 of cubicCentimetersPerMole) // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.thermo.molarvolume.*

(1 of litersPerMole).toString()    // "0.001 m^3/mol"
(22.4 of litersPerMole).toString() // "0.0224 m^3/mol"
```

## 记法

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·`
表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示      | Kotlin                               | 含义                           |
|---------------|--------------------------------------|--------------------------------|
| `m³/mol`      | `cubicMetersPerMole`                 | 摩尔体积，基本单位 —— 命名令牌 |
| `m³·mol⁻¹`    | `(meters pow 3) / moles`             | 相同的量以基础维度表示         |
| `l/mol`       | `litersPerMole`                      | 升每摩尔                       |
| `cm³/mol`     | `cubicCentimetersPerMole`            | 立方厘米每摩尔                 |
| `V_m = V / n` | `(45.42 of liters) / (2 of moles)`   | 由体积 ÷ 物质的量得到摩尔体积  |
| `V_m = M / ρ` | `(18.015 of gramsPerMole) / density` | 由摩尔质量 ÷ 密度得到摩尔体积  |
| `V = V_m · n` | `ideal * (2 of moles)`               | 由摩尔体积 × 物质的量得到体积  |
| `n = V / V_m` | `(5 of liters) / ideal`              | 由体积 ÷ 摩尔体积得到物质的量  |
| `ρ = M / V_m` | `molarMass / molarVolume`            | 由摩尔质量 ÷ 摩尔体积得到密度  |
