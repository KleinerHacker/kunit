# 比能

包：`org.pcsoft.framework.kunit.thermo.specificenergy`
基本单位：**焦耳每千克**（`KSpecificEnergyUnit.BASE == KSpecificEnergyUnit.JOULE_PER_KILOGRAM`）

类型：**构造单位**

比能是每单位质量的能量：`energy / mass`（`J/kg`）。根据语境不同，同一个量也被称为
*比焓*、*比潜热*或*热值* —— 它们都共享这个单位组。

`KSpecificEnergyUnitInstance` 包装了一个恰好由两项组成的 `KMixedUnitInstance`，
处于规范正规形式 `distance² · time⁻²`（`m²·s⁻²`），始终以 J/kg 归一化。

!!! note "质量维度会相互抵消"
    `J/kg = kg·m²·s⁻²/kg = m²·s⁻²`。因此规范正规形式根本**不**携带质量项。
    只有与 `KMassUnitInstance` 之间的操作符会在质量组的克基准与该组的
    每千克定义之间架起桥梁。

每单位温度得到[比热容](specific-heat-capacity.md)；每摩尔而非每千克则得到
[摩尔能量](molar-energy.md)。

## 命名单位

| 单位 | 符号 | 令牌 | 1 单位相当于多少 J/kg |
|---|---|---:|---:|
| 焦耳每千克 | `J/kg` | `joulesPerKilogram` | 1.0 |
| 卡路里每克 | `cal/g` | `caloriesPerGram` | 4184.0 |
| 瓦时每千克 | `Wh/kg` | `wattHoursPerKilogram` | 3600.0 |
| 英热单位每磅 | `Btu/lb` | `btusPerPound` | 2326.0 |

以上单位均支持完整的 SI 前缀范围（`kilo.joulesPerKilogram`、
`mega.joulesPerKilogram`、`kilo.wattHoursPerKilogram` 等）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val h = 334 of kilo.joulesPerKilogram
h into joulesPerKilogram      // 334_000.0
h into caloriesPerGram        // ≈ 79.83
h into wattHoursPerKilogram   // ≈ 92.78
```

## 现实示例：融化冰块

水的融化潜热为 334 kJ/kg。融化一块 2.5 kg 的冰需要多少能量？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.mechanic.mass.*
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val latentHeat = 334 of kilo.joulesPerKilogram
val block = 2.5 of kilo.grams

val energy = latentHeat * block     // KEnergyUnitInstance
energy into kilo.joules             // 835.0 kJ
energy into joules                  // 835_000.0 J

// 反过来：1 MJ 能融化多少冰？
val melted = (1000 of kilo.joules) / latentHeat  // KMassUnitInstance
melted into kilo.grams              // ≈ 2.994 kg
```

## 用核心单位（能量与质量）计算

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `energy / mass` | `KSpecificEnergyUnitInstance` | 比能 |
| `specificEnergy * mass` | `KEnergyUnitInstance` | 总能量 |
| `mass * specificEnergy` | `KEnergyUnitInstance` | 总能量（可交换） |
| `energy / specificEnergy` | `KMassUnitInstance` | 涉及的质量 |

## 分解方式

两种分解方式都产生相同的类型化、值相等的实例。

| 分解方式 | 形式 | 结果 |
|---|---|---|
| `energy / mass` | 类型化操作符 | 直接得到 `KSpecificEnergyUnitInstance` |
| `distance² · time⁻²` | 原生表达式 + `toSpecificEnergy()` | `KSpecificEnergyUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.joules
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.thermo.specificenergy.*

// 类型化操作符形式
val typed = (1 of joules) / (1 of kilo.grams)

// 原生基础维度形式（m²·s⁻²），由 toSpecificEnergy() 识别
val native = (((1 of meters).toUnit() pow 2) / ((1 of seconds).toUnit() pow 2)).toSpecificEnergy()

typed == native // true —— 两者都是 1.0 J/kg
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificenergy.*

val total = (1 of kilo.joulesPerKilogram) + (500 of joulesPerKilogram)  // 1500 J/kg
val rest  = (1 of kilo.joulesPerKilogram) - (250 of joulesPerKilogram)  // 750 J/kg

(1 of kilo.joulesPerKilogram) > (500 of joulesPerKilogram)   // true
(1 of kilo.joulesPerKilogram) == (1000 of joulesPerKilogram) // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.thermo.specificenergy.*

(334 of kilo.joulesPerKilogram).toString()                        // "334000.0 J/kg"
"${(334 of kilo.joulesPerKilogram) into caloriesPerGram} cal/g"   // "79.83..."
```

## 记法

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `J/kg` | `joulesPerKilogram` | 比能，基本单位 —— 命名令牌 |
| `m²·s⁻²` | `(meters pow 2) / (seconds pow 2)` | 相同的量以基础维度表示 |
| `kJ/kg` | `kilo.joulesPerKilogram` | 千焦耳每千克 |
| `Wh/kg` | `wattHoursPerKilogram` | 瓦时每千克（电池能量密度） |
| `q = Q / m` | `(334 of kilo.joules) / (1 of kilo.grams)` | 由能量 ÷ 质量得到比能 |
| `Q = q · m` | `latentHeat * block` | 由比能 × 质量得到能量 |
| `m = Q / q` | `(1000 of kilo.joules) / latentHeat` | 由能量 ÷ 比能得到质量 |
