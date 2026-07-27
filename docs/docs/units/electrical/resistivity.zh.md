# 电阻率

包：`org.pcsoft.framework.kunit.resistivity`
基本单位：**欧姆米**（`KResistivityUnit.BASE == KResistivityUnit.OHM_METER`）

类型：**构造单位**

电阻率是一个**构造**单位：其组成为 `质量 · 长度³ · 时间⁻³ · 电流⁻²`
（`kg·m³·s⁻³·A⁻²`）。`KResistivityUnitInstance` 包装了一个由四项组成的 `KMixedUnitInstance` —— `KMassUnit.BASE`
（克）指数为 `+1`，`KDistanceUnit.BASE`（米）指数为 `+3`，`KTimeUnit.BASE`（秒）指数为 `-3`，以及
`KElectricCurrentUnit.BASE`（安培）指数为 `-2`。由于库中的质量分量以**克**（而非千克）为归一化基准，
规范乘积需除以 1000 才能得到欧姆米；存储值始终以欧姆米归一化。

电阻率是电阻背后的材料属性，是[电导率](conductivity.md)的倒数（`ρ = 1 / σ`）。

## 构建电阻率

可以用一个命名令牌构建电阻率，也可以通过分解构建（见下文）。命名单位以值为 1 的
令牌形式存在（配合 `of`/`into` 使用）：

| 电阻率 | 符号 | 令牌 | 1 单位相当于多少 Ω·m |
|---|---|---:|---:|
| 欧姆米 | `Ω·m` | `ohmMeters` | 1.0 |
| 欧姆厘米 | `Ω·cm` | `ohmCentimeters` | 0.01 |
| 静欧姆厘米（CGS-ESU） | `statΩ·cm` | `statohmCentimeters` | 8.98755179e9 |

命名单位通过 `KPrefixBuilder` 支持 SI 前缀（`nano.ohmMeters`、`micro.ohmMeters`、
`milli.ohmCentimeters` 等）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.resistivity.*

val rho = 17 of nano.ohmMeters     // 铜
rho into nano.ohmMeters            // 17.0
rho into ohmMeters                 // 1.7e-8
(1 of ohmMeters) into ohmCentimeters // 100.0
```

## 多种分解方式

电阻率可以通过多种**等价的分解方式**得到，所有方式产生的结果在值上都相等：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `resistance * length` | `KResistivityUnitInstance` | `ρ = R · A / l`，几何因子 `A / l` 是长度（可交换） |
| `1 / conductivity` | `KResistivityUnitInstance` | 倒数关系 `ρ = 1 / σ` |
| `mass·length³/(time³·current²)` | 通过 `.toResistivity()` | 原生规范形式的 `kg·m³·s⁻³·A⁻²` 表达式 |

带类型的操作符形式直接返回电阻率。完全原生的表达式仍是一个通用的
`KMixedUnitInstance`，需通过 `toResistivity()` 收窄为具体类型（该方法仅识别规范正规形式，
否则会抛出 `IllegalStateException`）。所有路径在值上都相等。

反向操作符将电阻、长度与电阻率联系在一起：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `resistivity / length` | `KResistanceUnitInstance` | `R = ρ · l / A` |
| `resistivity / resistance` | `KLengthUnitInstance` | 几何因子 `A / l = ρ / R` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.nano
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.resistance.ohms
import org.pcsoft.framework.kunit.resistivity.*

// 现实示例 - 铜线布线：17 nΩ·m 在 1 mm 的几何因子下得到 17 µΩ。
val r = (17 of nano.ohmMeters) / (1 of milli.meters)  // KResistanceUnitInstance，1.7e-5 Ω

// 用定义式求解电阻率：
val rho = (5 of ohms) * (0.4 of meters)               // KResistivityUnitInstance，2 Ω·m

// 以原生的 kg·m³·s⁻³·A⁻² 表达式表示的相同电阻率：
val raw = 2 of (kilo.grams * (meters pow 3)) / ((seconds pow 3) * (amperes pow 2))
raw.toResistivity() == (2 of ohmMeters)               // true
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.resistivity.*

val s = (100 of ohmMeters) + (40 of ohmMeters)  // 140 Ω·m
(100 of ohmMeters) > (40 of ohmMeters)          // true
(100 of ohmMeters) * (40 of ohmMeters)          // KMixedUnitInstance（脱离该组）
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.resistivity.*

(1 of ohmCentimeters).toString()   // "0.01 Ω·m"（基本单位）
```

## 符号表示

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`³`、`⁻²`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `Ω·m` | `ohmMeters` | 电阻率，基本单位（命名令牌，欧姆米） |
| `R · (A/l)` | `(5 of ohms) * (0.4 of meters)` | 由电阻和几何因子得出的电阻率 |
| `kg·m³/(s³·A²)` | `(kilo.grams * (meters pow 3)) / ((seconds pow 3) * (amperes pow 2))` | 电阻率作为质量·长度³ / (时间³·电流²)（分数形式） |
| `kg·m³·s⁻³·A⁻²` | `kilo.grams * (meters pow 3) * (seconds pow -3) * (amperes pow -2)` | 相同电阻率作为纯乘积形式 |
| `nΩ·m` | `nano.ohmMeters` | 带前缀的电阻率（纳欧姆米） |
