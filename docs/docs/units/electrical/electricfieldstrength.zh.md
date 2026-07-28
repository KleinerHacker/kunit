# 电场强度

包：`org.pcsoft.framework.kunit.electric.electricfieldstrength`
基本单位：**伏特每米**（`KElectricFieldStrengthUnit.BASE == KElectricFieldStrengthUnit.VOLT_PER_METER`）

类型：**构造单位**

电场强度是一个**构造**单位：其组成为 `质量 · 长度 · 时间⁻³ · 电流⁻¹`
（`kg·m·s⁻³·A⁻¹`）。`KElectricFieldStrengthUnitInstance` 包装了一个由四项组成的 `KMixedUnitInstance` ——
`KMassUnit.BASE`（克）指数为 `+1`，`KDistanceUnit.BASE`（米）指数为 `+1`，`KTimeUnit.BASE`（秒）指数为 `-3`，以及
`KElectricCurrentUnit.BASE`（安培）指数为 `-1`。由于库中的质量分量以**克**（而非千克）为归一化基准，
规范乘积需除以 1000 才能得到伏特每米；存储值始终以伏特每米归一化。

场强 `E` 是单位长度上的电压降，等价地，也是作用在单位电荷上的力。它通过
[介电常数](permittivity.md)与[电通量密度](electricfluxdensity.md)相关联（`D = ε · E`），并以由
其[电迁移率](electricmobility.md)给出的速度驱动电荷载流子（`v = μ · E`）。

## 构建电场强度

可以用一个命名令牌构建电场强度，也可以通过分解构建（见下文）。命名单位以值为 1 的
令牌形式存在（配合 `of`/`into` 使用）：

| 场强 | 符号 | 令牌 | 1 单位相当于多少 V/m |
|---|---|---:|---:|
| 伏特每米 | `V/m` | `voltsPerMeter` | 1.0 |
| 伏特每厘米 | `V/cm` | `voltsPerCentimeter` | 100.0 |
| 静伏特每厘米（CGS-ESU） | `statV/cm` | `statvoltsPerCentimeter` | 29979.2458 |

命名单位通过 `KPrefixBuilder` 支持 SI 前缀（`kilo.voltsPerMeter`、`mega.voltsPerMeter`、
`kilo.voltsPerCentimeter` 等）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.electric.fieldstrength.*

val e = 3 of mega.voltsPerMeter        // 空气的击穿场强
e into mega.voltsPerMeter              // 3.0
e into voltsPerMeter                   // 3.0e6
(1 of voltsPerCentimeter) into voltsPerMeter // 100.0
```

## 多种分解方式

电场强度可以通过多种**等价的分解方式**得到，所有方式产生的结果在值上都相等：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `voltage / length` | `KElectricFieldStrengthUnitInstance` | `E = U / l`，单位长度上的电压降 |
| `force / charge` | `KElectricFieldStrengthUnitInstance` | `E = F / Q`，作用在单位电荷上的力 |
| `mass·length/(time³·current)` | 通过 `.toElectricFieldStrength()` | 原生规范形式的 `kg·m·s⁻³·A⁻¹` 表达式 |

带类型的操作符形式直接返回场强。完全原生的表达式仍是一个通用的
`KMixedUnitInstance`，需通过 `toElectricFieldStrength()` 收窄为具体类型（该方法仅识别规范正规形式，
否则会抛出 `IllegalStateException`）。所有路径在值上都相等。

反向操作符将电压、长度、力、电荷与场强联系在一起：

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `electricFieldStrength * length` | `KVoltageUnitInstance` | `U = E · l`（可交换） |
| `voltage / electricFieldStrength` | `KLengthUnitInstance` | `l = U / E` |
| `electricFieldStrength * charge` | `KForceUnitInstance` | `F = E · Q`（可交换） |
| `force / electricFieldStrength` | `KChargeUnitInstance` | `Q = F / E` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.electric.fieldstrength.*

// 现实示例 - 市电电压跨越 2 mm 的空气间隙得到 115 kV/m。
val e = (230 of volts) / (2 of milli.meters)   // KElectricFieldStrengthUnitInstance，115000 V/m

// 用力的分解得到的相同场强：
val fromForce = (6 of newtons) / (3 of coulombs)  // 2 V/m

// 以原生的 kg·m·s⁻³·A⁻¹ 表达式表示的相同场强：
val raw = 2 of (kilo.grams * (meters pow 1)) / ((seconds pow 3) * (amperes pow 1))
raw.toElectricFieldStrength() == (2 of voltsPerMeter)  // true
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.fieldstrength.*

val s = (1 of voltsPerMeter) + (1 of voltsPerCentimeter)  // 101 V/m
(1 of voltsPerCentimeter) > (1 of voltsPerMeter)          // true
(2 of voltsPerMeter) * (3 of voltsPerMeter)               // KMixedUnitInstance（脱离该组）
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.fieldstrength.*

(1 of voltsPerCentimeter).toString()   // "100.0 V/m"（基本单位）
```

## 符号表示

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`³`、`⁻¹`），`·` 表示乘法，`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin | 含义 |
|---|---|---|
| `V/m` | `voltsPerMeter` | 电场强度，基本单位（命名令牌，伏特每米） |
| `U / l` | `(230 of volts) / (2 of milli.meters)` | 由电压和距离得出的场强 |
| `F / Q` | `(6 of newtons) / (3 of coulombs)` | 场强作为单位电荷上的力 |
| `kg·m/(s³·A)` | `(kilo.grams * (meters pow 1)) / ((seconds pow 3) * (amperes pow 1))` | 场强作为质量·长度 / (时间³·电流)（分数形式） |
| `kg·m·s⁻³·A⁻¹` | `kilo.grams * (meters pow 1) * (seconds pow -3) * (amperes pow -1)` | 相同场强作为纯乘积形式 |
| `kV/m` | `kilo.voltsPerMeter` | 带前缀的场强（千伏特每米） |
