# 能量（热力学）

包：`org.pcsoft.framework.kunit.common.energy`
基本单位： **焦耳**（`KEnergyUnit.BASE == KEnergyUnit.JOULE`）

类型： **构造单位**

能量是一个 **构造**单位：其组成为 `质量 · 长度² · 时间⁻²`（`kg·m²·s⁻²`）。
`KEnergyUnitInstance` 包装了一个由三项组成的 `KMixedUnitInstance` —— `KMassUnit.BASE`（克）指数为 `+1`、
`KDistanceUnit.BASE`（米）指数为 `+2`，以及 `KTimeUnit.BASE`（秒）指数为 `-2`。由于库中的质量分量以 **克**（而非千克）为归一化基准，规范乘积需除以
1000 才能得到焦耳；存储值始终以焦耳归一化。

能量技术上是 **一个**量，只是出现在多个学科领域中。本页描述的是它的 *热力学*含义 —— **热量**，
`Q = Φ · t`。同一个 Kotlin 组在其他领域的文档见
[能量（电学）](../electrical/energy.md)和[能量（力学）](../mechanics/energy.md)。

## 构建能量

可以用一个命名令牌构建能量，也可以通过分解构建（见下文）。命名单位以值为 1 的 令牌形式存在（配合 `of`/`into`
使用）。该组的热学单位是卡路里和英热单位：

| 能量               | 符号  |                  令牌 | 1 单位相当于多少 J |
|--------------------|-------|----------------------:|-------------------:|
| 焦耳               | `J`   |              `joules` |                1.0 |
| 尔格（CGS）        | `erg` |                `ergs` |             1.0e-7 |
| 卡路里（热化学卡） | `cal` |            `calories` |              4.184 |
| 电子伏特           | `eV`  |       `electronVolts` |    1.602176634e-19 |
| 英热单位           | `BTU` | `britishThermalUnits` |      1055.05585262 |

命名单位通过 `KPrefixBuilder` 支持 SI 前缀（`kilo.calories` —— 即"食物卡路里" ——
`kilo.joules`、`mega.joules` 等）。

**千瓦时没有自己的令牌** —— 它并不是一个真正的命名单位，而是乘积
`kilo.watts * hours`，需以这种方式构建。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.common.energy.*

val q = 2000 of kilo.calories   // 每日饮食摄入
q into kilo.joules              // 8368.0
q into britishThermalUnits      // 7931.79...
```

## 多种分解方式

能量可以通过多种 **等价的分解方式**得到，所有方式产生的结果在值上都相等：

| 表达式               | 结果类型              | 含义                                                             |
|----------------------|-----------------------|------------------------------------------------------------------|
| `power * time`       | `KEnergyUnitInstance` | 由热流率随时间得出的热量 `Q = Φ · t`（可交换）                   |
| `power / frequency`  | `KEnergyUnitInstance` | 时间倒数形式（`W/Hz = W·s`）                                     |
| `force * length`     | `KEnergyUnitInstance` | 机械功 `W = F · s`（参见[能量（力学）](../mechanics/energy.md)） |
| `charge * voltage`   | `KEnergyUnitInstance` | 电能 `W = Q · U`（参见[能量（电学）](../electrical/energy.md)）  |
| `mass·length²/time²` | 通过 `.toEnergy()`    | 原生规范形式的 `kg·m²·s⁻²` 表达式                                |

带类型的操作符形式直接返回能量。完全原生的表达式仍是一个通用的
`KMixedUnitInstance`，需通过 `toEnergy()` 收窄为具体类型（该方法仅识别规范正规形式， 否则会抛出 `IllegalStateException`
）。所有路径在值上都相等。

反向操作符将热流率、时间与热量联系在一起：

| 表达式           | 结果类型             | 含义                                                 |
|------------------|----------------------|------------------------------------------------------|
| `energy / time`  | `KPowerUnitInstance` | 热流率 `Φ = Q / t`（参见[功率（热力学）](power.md)） |
| `energy / power` | `KTimeUnitInstance`  | 加热时间 `t = Q / Φ`                                 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.minutes
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.common.power.watts
import org.pcsoft.framework.kunit.common.energy.*

// 现实示例 - 电热水器：2 kW 的热流率在 10 分钟内释放 1200 kJ 的热量。
val q = (2 of kilo.watts) * (10 of minutes)   // KEnergyUnitInstance
q into kilo.joules                            // 1200.0

// 用热量求解一台 2 kW 热水器的加热时间：
val t = (1200 of kilo.joules) / (2 of kilo.watts)  // KTimeUnitInstance，600 s

// 以及求解热流率：
val flow = (1200 of kilo.joules) / (10 of minutes) // KPowerUnitInstance，2 kW

// 以原生的 kg·m²·s⁻² 表达式表示的相同热量：
val raw = 1_200_000 of (kilo.grams * (meters pow 2)) / (seconds pow 2)
raw.toEnergy() == (1200 of kilo.joules)            // true
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.energy.*

val s = (100 of joules) + (40 of joules)  // 140 J
(100 of joules) > (40 of joules)          // true
(100 of joules) * (40 of joules)          // KMixedUnitInstance（脱离该组）
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.common.energy.*

(1 of britishThermalUnits).toString()     // "1055.05585262 J"（基本单位）
```

## 符号表示

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`⁻²`），`·` 表示乘法，
`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示    | Kotlin                                            | 含义                                     |
|-------------|---------------------------------------------------|------------------------------------------|
| `J`         | `joules`                                          | 能量（热量），基本单位（命名令牌，焦耳） |
| `Φ · t`     | `(2 of kilo.watts) * (10 of minutes)`             | 由热流率和时间得出的热量                 |
| `kcal`      | `kilo.calories`                                   | 带前缀的热能（食物卡路里）               |
| `kg·m²/s²`  | `(kilo.grams * (meters pow 2)) / (seconds pow 2)` | 能量作为质量·长度² / 时间²（分数形式）   |
| `kg·m²·s⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2)`  | 相同能量作为纯乘积形式                   |
