# 电导

包: `org.pcsoft.framework.kunit.electric.conductance`
基本单位: **西门子**（`KConductanceUnit.BASE == KConductanceUnit.SIEMENS`）

类型: **构成单位**

电导是一个 **构成**单位：其组合为 `mass⁻¹ · length⁻² · time³ · current²`（`kg⁻¹·m⁻²·s³·A²`）。
`KConductanceUnitInstance` 包装了一个由四项组成的 `KMixedUnitInstance` —— 指数 `-1` 的 `KMassUnit.BASE`
（克）、指数 `-2` 的 `KDistanceUnit.BASE`（米）、指数 `+3` 的 `KTimeUnit.BASE`（秒）以及指数 `+2` 的
`KElectricCurrentUnit.BASE`（安培）。由于本库的质量分量归一化到 **克**（而非千克），且质量指数为负， 西门子是原始分量基准的
1/1000 倍；存储的值归一化为西门子。

电导是[电阻](resistance.md)的倒数（`G = 1 / R`），并通过欧姆定律把[电压](voltage.md)与
[电流](ec.md)联系起来。

## 构建电导

可以用命名标记构建电导，或者由分解构建（见下文）。命名单位以值为 1 的标记形式存在（与 `of`/`into` 一起使用）：

| 电导                | 符号    |       标记 |  1 单位（S） |
|---------------------|---------|-----------:|-------------:|
| 西门子              | `S`     |  `siemens` |          1.0 |
| 姆欧（传统名称）    | `℧`     |     `mhos` |          1.0 |
| 绝对姆欧（CGS-EMU） | `ab℧`   |   `abmhos` |        1.0e9 |
| 静电姆欧（CGS-ESU） | `stat℧` | `statmhos` | 1.112650e-12 |

!!! note "`siemens` 与 `siemensUnits`"
`siemens`（本包）是 **电导**的 SI 单位。名称相近的
`org.pcsoft.framework.kunit.electric.resistance` 中的 `siemensUnits` 是历史上的 **西门子水银单位**， 是 0.9534 Ω 的
*电阻*。两者属于不同包中互不相关的量。

命名单位通过 `KPrefixBuilder` 支持 SI 词头（`milli.siemens`、`micro.siemens`、`kilo.siemens` 等）。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.conductance.*

val g = 4 of siemens
g into siemens                    // 4.0
g into milli.siemens              // 4000.0
(1 of milli.siemens) into siemens // 0.001
```

## 多种分解

电导可以通过多种 **等价分解**得到，它们都产生值相等的电导：

| 表达式                          | 结果类型                   | 含义                            |
|---------------------------------|----------------------------|---------------------------------|
| `current / voltage`             | `KConductanceUnitInstance` | 欧姆定律 `G = I / U`            |
| `1 / resistance`                | `KConductanceUnitInstance` | 电阻的倒数 `G = 1 / R`          |
| `time³·current²/(mass·length²)` | 经由 `.toConductance()`    | 原生规范表达式 `kg⁻¹·m⁻²·s³·A²` |

带类型的运算符形式直接返回电导。完全原生的表达式仍是通用的 `KMixedUnitInstance`，需用 `toConductance()`
收窄（它只识别规范形式，否则抛出 `IllegalStateException`）。所有路径的值都相等。

反向运算符把电导、电压和电流联系起来：

| 表达式                  | 结果类型                       | 含义                  |
|-------------------------|--------------------------------|-----------------------|
| `conductance * voltage` | `KElectricCurrentUnitInstance` | `I = G · U`（可交换） |
| `current / conductance` | `KVoltageUnitInstance`         | `U = I / G`           |
| `1 / conductance`       | `KResistanceUnitInstance`      | `R = 1 / G`           |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.electric.resistance.ohms
import org.pcsoft.framework.kunit.electric.conductance.*

// 实际示例 —— 供电电缆的电导：一根通过 2 A 电流、测得压降为 1 V 的电缆，
// 其电导为 2 S（即电阻为 0.5 Ω）。
val g = (2 of amperes) / (1 of volts)    // KConductanceUnitInstance，2 S
val r = 1 / g                            // KResistanceUnitInstance，0.5 Ω

// 与电阻的倒数关系：
1 / (1 of ohms) == (1 of siemens)        // true

// 同一电导用原生 kg⁻¹·m⁻²·s³·A² 表达式表示：
val raw = 2 of ((seconds pow 3) * (amperes pow 2)) / (kilo.grams * (meters pow 2))
raw.toConductance() == (2 of siemens)    // true
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.conductance.*

val s = (100 of siemens) + (40 of siemens)  // 140 S
(100 of siemens) > (40 of siemens)          // true
(100 of siemens) * (40 of siemens)          // KMixedUnitInstance（脱离该组）
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.conductance.*

(4 of siemens).toString()     // "4.0 S"（基本单位）
```

## 记法

下表展示了该单位及其组成部分在数学上的写法与在 Kotlin 中使用 KUnit 的写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`），`·` 表示乘法，
`/` 表示分数。若某个量既可写成分数也可写成带负指数的乘积，则同时列出两种等价的 Kotlin 形式。

| 数学             | Kotlin                                                                      | 含义                                              |
|------------------|-----------------------------------------------------------------------------|---------------------------------------------------|
| `S`              | `siemens`                                                                   | 电导，基本单位（命名标记，西门子）                |
| `s³·A²/(kg·m²)`  | `((seconds pow 3) * (amperes pow 2)) / (kilo.grams * (meters pow 2))`       | 电导表示为 时间³·电流² / (质量·长度²)（分数形式） |
| `kg⁻¹·m⁻²·s³·A²` | `(kilo.grams pow -1) * (meters pow -2) * (seconds pow 3) * (amperes pow 2)` | 同一电导表示为纯乘积                              |
| `mS`             | `milli.siemens`                                                             | 带词头的电导（毫西门子）                          |
