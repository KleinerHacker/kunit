# 功率（力学）

包：`org.pcsoft.framework.kunit.common.power`
基本单位：**瓦特**（`KPowerUnit.BASE == KPowerUnit.WATT`）

类型：**构造单位**

功率是一个**构造**单位：其组成为 `质量 · 长度² · 时间⁻³`（`kg·m²·s⁻³`）。
`KPowerUnitInstance` 包装了一个由三项组成的 `KMixedUnitInstance` —— `KMassUnit.BASE`（克）指数为 `+1`、
`KDistanceUnit.BASE`（米）指数为 `+2`，以及 `KTimeUnit.BASE`（秒）指数为 `-3`。由于库中的质量分量以
**克**（而非千克）为归一化基准，规范乘积需除以 1000 才能得到瓦特；存储值始终以瓦特归一化。

功率技术上是**一个**量，只是出现在多个学科领域中。本页描述的是它的*力学*含义（`P = F · v`）。
同一个 Kotlin 组在其他领域的文档见[功率（电学）](../electrical/power.md)和
[功率（热力学）](../thermodynamics/power.md)。

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

val p = 100 of metricHorsePowers
p into kilo.watts               // 73.549875
p into mechanicalHorsePowers    // 98.63200706...
```

## 多种分解方式

功率可以通过多种**等价的分解方式**得到，所有方式产生的结果在值上都相等：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `force * speed` | `KPowerUnitInstance` | 机械功率 `P = F · v`（可交换） |
| `voltage * current` | `KPowerUnitInstance` | 电功率 `P = U · I`（参见[功率（电学）](../electrical/power.md)） |
| `energy / time` | `KPowerUnitInstance` | `P = W / t`（参见[能量（力学）](energy.md)） |
| `mass·length²/time³` | 通过 `.toPower()` | 原生规范形式的 `kg·m²·s⁻³` 表达式 |

带类型的操作符形式直接返回功率。完全原生的表达式仍是一个通用的
`KMixedUnitInstance`，需通过 `toPower()` 收窄为具体类型（该方法仅识别规范正规形式，
否则会抛出 `IllegalStateException`）。所有路径在值上都相等。

力学形式的反向操作符将力、速度与功率联系在一起：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `power / force` | `KSpeedUnitInstance` | `v = P / F` |
| `power / speed` | `KForceUnitInstance` | `F = P / v` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.common.power.*

// 现实示例 - 货运绞盘：100 N 的拉力配合 5 m/s 的速度，需要 500 W 的功率。
val p = (100 of newtons) * ((5 of meters) / (1 of seconds))  // KPowerUnitInstance
p into watts                                                 // 500.0

// 用定义式求解给定速度下所需的拉力：
val f = (500 of watts) / ((5 of meters) / (1 of seconds))     // KForceUnitInstance，100 N

// 以及求解给定拉力下可达到的速度：
val v = (500 of watts) / (100 of newtons)                     // KSpeedUnitInstance，5 m/s

// 以原生的 kg·m²·s⁻³ 表达式表示的相同功率：
val raw = 500 of (kilo.grams * (meters pow 2)) / (seconds pow 3)
raw.toPower() == (500 of watts)                               // true
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
import org.pcsoft.framework.kunit.common.power.*

(1 of metricHorsePowers).toString()     // "735.49875 W"（基本单位）
```

## 符号表示

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`⁻³`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `W` | `watts` | 功率，基本单位（命名令牌，瓦特） |
| `F · v` | `(100 of newtons) * ((5 of meters) / (1 of seconds))` | 由力和速度得出的机械功率 |
| `kg·m²/s³` | `(kilo.grams * (meters pow 2)) / (seconds pow 3)` | 功率作为质量·长度² / 时间³（分数形式） |
| `kg·m²·s⁻³` | `kilo.grams * (meters pow 2) * (seconds pow -3)` | 相同功率作为纯乘积形式 |
| `PS` | `metricHorsePowers` | 公制马力（命名令牌） |
