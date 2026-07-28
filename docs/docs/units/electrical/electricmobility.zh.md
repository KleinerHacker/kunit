# 电迁移率

包：`org.pcsoft.framework.kunit.electricmobility`
基本单位：**平方米每伏特秒**
（`KElectricMobilityUnit.BASE == KElectricMobilityUnit.SQUARE_METER_PER_VOLT_SECOND`）

类型：**构造单位**

电迁移率是一个**构造**单位：其组成为 `质量⁻¹ · 时间² · 电流`
（`kg⁻¹·s²·A` = `m²/(V·s)`）。`KElectricMobilityUnitInstance` 包装了一个由三项组成的 `KMixedUnitInstance` ——
`KMassUnit.BASE`（克）指数为 `-1`，`KTimeUnit.BASE`（秒）指数为 `+2`，以及 `KElectricCurrentUnit.BASE`（安培）
指数为 `+1`。长度维度会相互抵消，因为伏特本身已经包含了 `m²`，因此规范形式只有三项。由于库中的质量分量以
**克**（而非千克）为归一化基准，且质量指数为负，规范乘积需乘以 1000 才能得到基本单位；存储值始终
归一化为平方米每伏特秒。

电迁移率 `μ` 描述了电荷载流子在电场中漂移的速度：`v = μ · E`，其中 `E` 是
[电场强度](electricfieldstrength.md)。

## 构建电迁移率

可以用一个命名令牌构建电迁移率，也可以通过分解构建（见下文）。命名单位以值为 1 的
令牌形式存在（配合 `of`/`into` 使用）：

| 迁移率 | 符号 | 令牌 | 1 单位相当于多少 m²/(V·s) |
|---|---|---:|---:|
| 平方米每伏特秒 | `m²/(V·s)` | `squareMetersPerVoltSecond` | 1.0 |
| 平方厘米每伏特秒 | `cm²/(V·s)` | `squareCentimetersPerVoltSecond` | 1.0e-4 |

厘米的写法是半导体物理学中通用的记法。命名单位通过 `KPrefixBuilder` 支持 SI 前缀
（`milli.squareMetersPerVoltSecond`、`kilo.squareCentimetersPerVoltSecond` 等）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.electricmobility.*

val mu = 1400 of squareCentimetersPerVoltSecond   // 硅中电子的迁移率
mu into squareCentimetersPerVoltSecond            // 1400.0
mu into squareMetersPerVoltSecond                 // 0.14
```

## 多种分解方式

电迁移率可以通过多种**等价的分解方式**得到，所有方式产生的结果在值上都相等：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `speed / electricFieldStrength` | `KElectricMobilityUnitInstance` | `μ = v / E`，单位场强下的漂移速度 |
| `(time²·current)/mass` | 通过 `.toElectricMobility()` | 原生规范形式的 `kg⁻¹·s²·A` 表达式 |

带类型的操作符形式直接返回迁移率。完全原生的表达式仍是一个通用的
`KMixedUnitInstance`，需通过 `toElectricMobility()` 收窄为具体类型（该方法仅识别规范正规形式，
否则会抛出 `IllegalStateException`）。两条路径在值上都相等。

反向操作符将漂移速度、场强与迁移率联系在一起：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `electricMobility * electricFieldStrength` | `KSpeedUnitInstance` | `v = μ · E`（可交换） |
| `speed / electricMobility` | `KElectricFieldStrengthUnitInstance` | `E = v / μ` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.speed.div
import org.pcsoft.framework.kunit.electricfieldstrength.voltsPerMeter
import org.pcsoft.framework.kunit.electricmobility.*

// 现实示例 - 硅中电子以 1400 cm²/(V·s) 的迁移率在 1 kV/m 的场中以 140 m/s 漂移。
val v = (1400 of squareCentimetersPerVoltSecond) * (1000 of voltsPerMeter)  // KSpeedUnitInstance，140 m/s

// 用定义式求解迁移率：
val mu = ((6 of meters) / (1 of seconds)) / (3 of voltsPerMeter)   // 2 m²/(V·s)

// 以原生的 kg⁻¹·s²·A 表达式表示的相同迁移率：
val raw = 2 of ((seconds pow 2) * (amperes pow 1)) / (kilo.grams pow 1)
raw.toElectricMobility() == (2 of squareMetersPerVoltSecond)       // true
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricmobility.*

val s = (1 of squareMetersPerVoltSecond) + (1 of squareCentimetersPerVoltSecond)  // 1.0001 m²/(V·s)
(1 of squareMetersPerVoltSecond) > (1 of squareCentimetersPerVoltSecond)          // true
(2 of squareMetersPerVoltSecond) * (3 of squareMetersPerVoltSecond)               // KMixedUnitInstance
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electricmobility.*

(1400 of squareCentimetersPerVoltSecond).toString()   // "0.14 m²/(V·s)"（基本单位）
```

## 符号表示

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`⁻¹`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `m²/(V·s)` | `squareMetersPerVoltSecond` | 电迁移率，基本单位（命名令牌） |
| `cm²/(V·s)` | `squareCentimetersPerVoltSecond` | 半导体物理学中的记法，1e-4 m²/(V·s) |
| `v / E` | `((6 of meters) / (1 of seconds)) / (3 of voltsPerMeter)` | 由漂移速度和场强得出的迁移率 |
| `μ · E` | `(1400 of squareCentimetersPerVoltSecond) * (1000 of voltsPerMeter)` | 给定场强下的漂移速度 |
| `(s²·A)/kg` | `((seconds pow 2) * (amperes pow 1)) / (kilo.grams pow 1)` | 迁移率作为 (时间²·电流) / 质量（分数形式） |
| `kg⁻¹·s²·A` | `(kilo.grams pow -1) * (seconds pow 2) * (amperes pow 1)` | 相同迁移率作为纯乘积形式 |
