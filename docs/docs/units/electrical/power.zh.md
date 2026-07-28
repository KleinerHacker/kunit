# 功率（电学）

包：`org.pcsoft.framework.kunit.power`
基本单位：**瓦特**（`KPowerUnit.BASE == KPowerUnit.WATT`）

类型：**构造单位**

功率是一个**构造**单位：其组成为 `质量 · 长度² · 时间⁻³`（`kg·m²·s⁻³`）。
`KPowerUnitInstance` 包装了一个由三项组成的 `KMixedUnitInstance` —— `KMassUnit.BASE`（克）指数为 `+1`、
`KDistanceUnit.BASE`（米）指数为 `+2`，以及 `KTimeUnit.BASE`（秒）指数为 `-3`。由于库中的质量分量以
**克**（而非千克）为归一化基准，规范乘积需除以 1000 才能得到瓦特；存储值始终以瓦特归一化。

功率技术上是**一个**量，只是出现在多个学科领域中。本页描述的是它的*电学*含义（`P = U · I`）。
同一个 Kotlin 组在其他领域的文档见[功率（力学）](../mechanics/power.md)和
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
| 伏安（视在功率） | `VA` | `voltAmperes` | 1.0 |
| 无功伏安 | `var` | `vars` | 1.0 |

命名单位通过 `KPrefixBuilder` 支持 SI 前缀（`kilo.watts`、`mega.watts`、`milli.watts` 等）。

### 视在功率与无功功率（VA、var）

在交流电系统中，会区分三种功率，它们在量纲上都与瓦特相同：

* **有功功率** `P = U · I · cos φ`，单位为瓦特（`W`）—— 实际做功的部分，
* **视在功率** `S = U · I`，单位为伏安（`VA`）—— 有效电压与有效电流的乘积，
* **无功功率** `Q = U · I · sin φ`，单位为无功伏安（`var`）—— 在电源与负载之间振荡而不做功的部分。

由于这三者只在约定上有所区别，KUnit 将它们保留在同一个组中，仅通过符号加以区分：
`1 VA = 1 var = 1 W`。前缀照常适用，因此 `kilo.voltAmperes` 是 1 kVA，`kilo.vars` 是 1 kvar。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.power.*

// 一台额定 25 kVA 的变压器，为功率因数 cos φ = 0.8 的负载供电：
val s = 25 of kilo.voltAmperes
val p = (25 * 0.8) of kilo.watts     // 20 kW 有功功率
val q = (25 * 0.6) of kilo.vars      // 15 kvar 无功功率
s into kilo.voltAmperes               // 25.0
q into kilo.vars                      // 15.0
```

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.power.*

val p = 2 of kilo.watts
p into kilo.watts               // 2.0
p into watts                    // 2000.0
(100 of metricHorsePowers) into kilo.watts // 73.549875
```

## 多种分解方式

功率可以通过多种**等价的分解方式**得到，所有方式产生的结果在值上都相等：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `voltage * current` | `KPowerUnitInstance` | 电功率 `P = U · I`（可交换） |
| `force * speed` | `KPowerUnitInstance` | 机械功率 `P = F · v`（可交换） |
| `energy / time` | `KPowerUnitInstance` | `P = W / t`（参见[能量](energy.md)） |
| `mass·length²/time³` | 通过 `.toPower()` | 原生规范形式的 `kg·m²·s⁻³` 表达式 |

带类型的操作符形式直接返回功率。完全原生的表达式仍是一个通用的
`KMixedUnitInstance`，需通过 `toPower()` 收窄为具体类型（该方法仅识别规范正规形式，
否则会抛出 `IllegalStateException`）。所有路径在值上都相等。

电学形式的反向操作符将电压、电流与功率联系在一起：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `power / current` | `KVoltageUnitInstance` | `U = P / I` |
| `power / voltage` | `KElectricCurrentUnitInstance` | `I = P / U` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.power.*

// 现实示例 - 家用插座：230 V、10 A 提供 2.3 kW 的功率。
val p = (230 of volts) * (10 of amperes)   // KPowerUnitInstance
p into kilo.watts                          // 2.3

// 用定义式求解 230 V 下 2.3 kW 负载所需的电流：
val i = (2.3 of kilo.watts) / (230 of volts) // KElectricCurrentUnitInstance，10 A

// 以原生的 kg·m²·s⁻³ 表达式表示的相同功率：
val raw = 2300 of (kilo.grams * (meters pow 2)) / (seconds pow 3)
raw.toPower() == (2.3 of kilo.watts)       // true
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.power.*

val s = (100 of watts) + (40 of watts)  // 140 W
(100 of watts) > (40 of watts)          // true
(100 of watts) * (40 of watts)          // KMixedUnitInstance（脱离该组）
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.power.*

(1 of kilo.watts).toString()     // "1000.0 W"（基本单位）
```

## 符号表示

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`⁻³`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `W` | `watts` | 功率，基本单位（命名令牌，瓦特） |
| `U · I` | `(230 of volts) * (10 of amperes)` | 由电压和电流得出的电功率 |
| `kg·m²/s³` | `(kilo.grams * (meters pow 2)) / (seconds pow 3)` | 功率作为质量·长度² / 时间³（分数形式） |
| `kg·m²·s⁻³` | `kilo.grams * (meters pow 2) * (seconds pow -3)` | 相同功率作为纯乘积形式 |
| `kW` | `kilo.watts` | 带前缀的功率（千瓦） |
| `S = U · I`，单位 `VA` | `voltAmperes` | 视在功率（交流电） |
| `Q`，单位 `var` | `vars` | 无功功率（交流电） |
| `kVA` | `kilo.voltAmperes` | 带前缀的视在功率（千伏安） |
| `kvar` | `kilo.vars` | 带前缀的无功功率 |
