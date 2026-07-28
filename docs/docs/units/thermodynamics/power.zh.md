# 功率（热力学）

包：`org.pcsoft.framework.kunit.common.power`
基本单位：**瓦特**（`KPowerUnit.BASE == KPowerUnit.WATT`）

类型：**构造单位**

功率是一个**构造**单位：其组成为 `质量 · 长度² · 时间⁻³`（`kg·m²·s⁻³`）。
`KPowerUnitInstance` 包装了一个由三项组成的 `KMixedUnitInstance` —— `KMassUnit.BASE`（克）指数为 `+1`、
`KDistanceUnit.BASE`（米）指数为 `+2`，以及 `KTimeUnit.BASE`（秒）指数为 `-3`。由于库中的质量分量以
**克**（而非千克）为归一化基准，规范乘积需除以 1000 才能得到瓦特；存储值始终以瓦特归一化。

功率技术上是**一个**量，只是出现在多个学科领域中。本页描述的是它的*热力学*含义 —— **热流率**
`Φ = Q / t`，即单位时间内的热能。同一个 Kotlin 组在其他领域的文档见
[功率（电学）](../electrical/power.md)和[功率（力学）](../mechanics/power.md)。

## 构建功率

可以用一个命名令牌构建功率，也可以通过分解构建（见下文）。命名单位以值为 1 的
令牌形式存在（配合 `of`/`into` 使用）：

| 功率 | 符号 | 令牌 | 1 单位相当于多少 W |
|---|---|---:|---:|
| 瓦特 | `W` | `watts` | 1.0 |
| 公制马力 | `PS` | `metricHorsePowers` | 735.49875 |
| 机械马力 | `hp` | `mechanicalHorsePowers` | 745.6998715822702 |
| 尔格每秒（CGS） | `erg/s` | `ergsPerSecond` | 1.0e-7 |

命名单位通过 `KPrefixBuilder` 支持 SI 前缀（`kilo.watts`、`mega.watts`、`milli.watts` 等）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.*

val heatFlow = 9 of kilo.watts   // 一台房间加热器
heatFlow into kilo.watts         // 9.0
heatFlow into watts              // 9000.0
```

## 多种分解方式

功率可以通过多种**等价的分解方式**得到，所有方式产生的结果在值上都相等：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `energy / time` | `KPowerUnitInstance` | 热流率 `Φ = Q / t`（参见[能量（热力学）](energy.md)） |
| `voltage * current` | `KPowerUnitInstance` | 电功率 `P = U · I`（参见[功率（电学）](../electrical/power.md)） |
| `force * speed` | `KPowerUnitInstance` | 机械功率 `P = F · v`（参见[功率（力学）](../mechanics/power.md)） |
| `mass·length²/time³` | 通过 `.toPower()` | 原生规范形式的 `kg·m²·s⁻³` 表达式 |

带类型的操作符形式直接返回功率。完全原生的表达式仍是一个通用的
`KMixedUnitInstance`，需通过 `toPower()` 收窄为具体类型（该方法仅识别规范正规形式，
否则会抛出 `IllegalStateException`）。所有路径在值上都相等。

热流率形式的反向操作符将能量、时间与功率联系在一起：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `power * time` | `KEnergyUnitInstance` | 释放的热量，`Q = Φ · t`（可交换） |
| `energy / power` | `KTimeUnitInstance` | 所需的时间，`t = Q / Φ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.common.energy.*
import org.pcsoft.framework.kunit.common.power.*

// 现实示例 - 电热水器：在 10 分钟内释放 1200 kJ 热量，即为 2 kW 的热流率。
val heatFlow = (1200 of kilo.joules) / (10 of minutes)   // KPowerUnitInstance
heatFlow into kilo.watts                                 // 2.0

// 用热流率求解一小时内释放的热量：
val heat = (2 of kilo.watts) * (60 of minutes)           // KEnergyUnitInstance，7.2 MJ

// 以原生的 kg·m²·s⁻³ 表达式表示的相同热流率：
val raw = 2000 of (kilo.grams * (meters pow 2)) / (seconds pow 3)
raw.toPower() == (2 of kilo.watts)                       // true
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.power.*

val s = (100 of watts) + (40 of watts)  // 140 W
(100 of watts) > (40 of watts)          // true
(100 of watts) * (40 of watts)          // KMixedUnitInstance（脱离该组）
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.power.*

(9 of kilo.watts).toString()     // "9000.0 W"（基本单位）
```

## 符号表示

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`⁻³`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `W` | `watts` | 功率（热流率），基本单位（命名令牌，瓦特） |
| `Q / t` | `(1200 of kilo.joules) / (10 of minutes)` | 由热量和时间得出的热流率 |
| `kg·m²/s³` | `(kilo.grams * (meters pow 2)) / (seconds pow 3)` | 功率作为质量·长度² / 时间³（分数形式） |
| `kg·m²·s⁻³` | `kilo.grams * (meters pow 2) * (seconds pow -3)` | 相同功率作为纯乘积形式 |
| `kW` | `kilo.watts` | 带前缀的功率（千瓦） |
