# 电导率

包：`org.pcsoft.framework.kunit.conductivity`
基本单位：**西门子每米**（`KConductivityUnit.BASE == KConductivityUnit.SIEMENS_PER_METER`）

类型：**构造单位**

电导率是一个**构造**单位：其组成为 `质量⁻¹ · 长度⁻³ · 时间³ · 电流²`
（`kg⁻¹·m⁻³·s³·A²`）。`KConductivityUnitInstance` 包装了一个由四项组成的 `KMixedUnitInstance` —— `KMassUnit.BASE`
（克）指数为 `-1`，`KDistanceUnit.BASE`（米）指数为 `-3`，`KTimeUnit.BASE`（秒）指数为 `+3`，以及
`KElectricCurrentUnit.BASE`（安培）指数为 `+2`。由于库中的质量分量以**克**（而非千克）为归一化基准，
且质量指数为*负数*，规范乘积需乘以 1000 才能得到西门子每米；存储值始终以 S/m 归一化。

电导率是电导背后的材料属性，是[电阻率](resistivity.md)的倒数（`σ = 1 / ρ`）。

## 构建电导率

可以用一个命名令牌构建电导率，也可以通过分解构建（见下文）。命名单位以值为 1 的
令牌形式存在（配合 `of`/`into` 使用）：

| 电导率 | 符号 | 令牌 | 1 单位相当于多少 S/m |
|---|---|---:|---:|
| 西门子每米 | `S/m` | `siemensPerMeter` | 1.0 |
| 西门子每厘米 | `S/cm` | `siemensPerCentimeter` | 100.0 |
| 微西门子每厘米 | `µS/cm` | `microsiemensPerCentimeter` | 1.0e-4 |
| 兆西门子每米 | `MS/m` | `megasiemensPerMeter` | 1.0e6 |

命名单位通过 `KPrefixBuilder` 支持 SI 前缀（`mega.siemensPerMeter`、`milli.siemensPerMeter` 等）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.conductivity.*

val sigma = 58 of mega.siemensPerMeter        // 铜
sigma into mega.siemensPerMeter               // 58.0
sigma into siemensPerMeter                    // 5.8e7
(1 of siemensPerCentimeter) into siemensPerMeter // 100.0
```

## 多种分解方式

电导率可以通过多种**等价的分解方式**得到，所有方式产生的结果在值上都相等：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `1 / resistivity` | `KConductivityUnitInstance` | 倒数关系 `σ = 1 / ρ` |
| `conductance / length` | `KConductivityUnitInstance` | `σ = G · l / A`；几何因子 `l / A` 是长度的倒数，因此使用除法 |
| `current²·time³/(mass·length³)` | 通过 `.toConductivity()` | 原生规范形式的 `kg⁻¹·m⁻³·s³·A²` 表达式 |

带类型的操作符形式直接返回电导率。完全原生的表达式仍是一个通用的
`KMixedUnitInstance`，需通过 `toConductivity()` 收窄为具体类型（该方法仅识别规范正规形式，
否则会抛出 `IllegalStateException`）。所有路径在值上都相等。

反向操作符将电导、长度与电导率联系在一起：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `conductivity * length` | `KConductanceUnitInstance` | `G = σ · A / l`（可交换） |
| `conductance / conductivity` | `KLengthUnitInstance` | 几何因子 `A / l = G / σ` |
| `1 / conductivity` | `KResistivityUnitInstance` | 回到电阻率 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.conductance.siemens
import org.pcsoft.framework.kunit.resistivity.ohmMeters
import org.pcsoft.framework.kunit.conductivity.*

// 现实示例 - 铜：电阻率 17 nΩ·m 对应约 58.8 MS/m 的电导率。
val sigma = 1 / (17 of nano.ohmMeters)
sigma into mega.siemensPerMeter               // 58.82352941176471

// 由电导除以导体几何尺寸得到：
val fromConductance = (10 of siemens) / (5 of meters)  // KConductivityUnitInstance，2 S/m

// 以原生的 kg⁻¹·m⁻³·s³·A² 表达式表示的相同电导率：
val raw = 2 of ((amperes pow 2) * (seconds pow 3)) / (kilo.grams * (meters pow 3))
raw.toConductivity() == (2 of siemensPerMeter) // true

// 该倒数对是对称的：
1 / (2 of siemensPerMeter) into ohmMeters      // 0.5
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.conductivity.*

val s = (100 of siemensPerMeter) + (40 of siemensPerMeter)  // 140 S/m
(100 of siemensPerMeter) > (40 of siemensPerMeter)          // true
(100 of siemensPerMeter) * (40 of siemensPerMeter)          // KMixedUnitInstance（脱离该组）
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.conductivity.*

(1 of siemensPerCentimeter).toString()   // "100.0 S/m"（基本单位）
```

## 符号表示

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`³`、`⁻¹`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `S/m` | `siemensPerMeter` | 电导率，基本单位（命名令牌，西门子每米） |
| `1 / ρ` | `1 / (17 of nano.ohmMeters)` | 电导率作为电阻率的倒数 |
| `A²·s³/(kg·m³)` | `((amperes pow 2) * (seconds pow 3)) / (kilo.grams * (meters pow 3))` | 电导率作为电流²·时间³ / (质量·长度³)（分数形式） |
| `kg⁻¹·m⁻³·s³·A²` | `(kilo.grams pow -1) * (meters pow -3) * (seconds pow 3) * (amperes pow 2)` | 相同电导率作为纯乘积形式 |
| `MS/m` | `mega.siemensPerMeter` | 带前缀的电导率（兆西门子每米） |
