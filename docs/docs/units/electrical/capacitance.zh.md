# 电容

包: `org.pcsoft.framework.kunit.electric.capacitance`
基本单位: **法拉**(`KCapacitanceUnit.BASE == KCapacitanceUnit.FARAD`)

类型: **构成单位**

电容是一个**构成**单位: 组合 `mass⁻¹ · length⁻² · time⁴ · current²`(`kg⁻¹·m⁻²·s⁴·A²`)。
`KCapacitanceUnitInstance` 包装一个包含四个项的 `KMixedUnitInstance` — 指数 `-1` 的 `KMassUnit.BASE`(克)、
指数 `-2` 的 `KDistanceUnit.BASE`(米)、指数 `+4` 的 `KTimeUnit.BASE`(秒)以及指数 `+2` 的
`KElectricCurrentUnit.BASE`(安培)。由于本库的质量分量归一化到**克**(而非千克),且质量指数为*负*,
法拉相对于原始分量基准在相反方向上相差 1000 倍;存储的值归一化为法拉。

## 构建电容

可以用具名标记构建电容,或者由分解构建(见下文)。具名单位以值为 1 的标记形式保留(与 `of`/`into` 一起使用):

| 电容 | 符号 | 标记 | 1 单位折合 F |
|---|---|---:|---:|
| 法拉 | `F` | `farads` | 1.0 |
| 绝对法拉(CGS-EMU) | `abF` | `abfarads` | 1.0e9 |
| 静电法拉(CGS-ESU) | `statF` | `statfarads` | 1.112650056e-12 |
| 罐(莱顿瓶) | `jar` | `jars` | 1.11265e-9 |

具名单位通过 `KPrefixBuilder` 支持 SI 词头(`micro.farads`、`nano.farads`、`pico.farads` 等)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.electric.capacitance.*

val c = 470 of micro.farads
c into micro.farads            // 470.0
c into farads                  // 4.7e-4
(1 of milli.farads) into farads // 0.001
```

## 多种分解

电容可以通过若干**等价分解**得到,它们都产生数值相等的电容:

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `charge / voltage` | `KCapacitanceUnitInstance` | 定义 `C = Q / U` |
| `current²·time⁴/(mass·length²)` | 通过 `.toCapacitance()` | 原生规范形式 `kg⁻¹·m⁻²·s⁴·A²` 表达式 |

带类型的运算符形式直接返回电容。完全原生的表达式仍是通用的 `KMixedUnitInstance`,通过 `toCapacitance()`
收窄(它只识别规范形式,否则抛出 `IllegalStateException`)。两条路径数值相等。

反向运算符把电荷、电压和电容联系起来:

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `capacitance * voltage` | `KChargeUnitInstance` | `Q = C · U`(可交换) |
| `charge / capacitance` | `KVoltageUnitInstance` | `U = Q / C` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.voltage.volts
import org.pcsoft.framework.kunit.electric.charge.coulombs
import org.pcsoft.framework.kunit.electric.capacitance.*

// 实际例子 - 已充电的电容器: 470 µF 充到 12 V 储存 5.64 mC。
val q = (470 of micro.farads) * (12 of volts)  // KChargeUnitInstance,0.00564 C

// 定义式对电容求解:
val c = (10 of coulombs) / (5 of volts)        // KCapacitanceUnitInstance,2 F

// 同一电容写成原生的 kg⁻¹·m⁻²·s⁴·A² 表达式:
val raw = 2 of ((amperes pow 2) * (seconds pow 4)) / (kilo.grams * (meters pow 2))
raw.toCapacitance() == (2 of farads)           // true
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.capacitance.*

val s = (100 of farads) + (40 of farads)  // 140 F
(100 of farads) > (40 of farads)          // true
(100 of farads) * (40 of farads)          // KMixedUnitInstance(离开该组)
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.electric.capacitance.*

(470 of farads).toString()     // "470.0 F"(基本单位)
```

## 记法

下表展示该单位及其组成部分在数学上的写法与在 KUnit 的 Kotlin 中的写法。指数使用 Unicode 上标(`²`、`⁴`、`⁻¹`),`·` 表示乘法,`/` 表示分数。当一个量既可写成分数也可写成带负指数的乘积时,两种等价的 Kotlin 形式都会列出。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `F` | `farads` | 电容,基本单位(具名标记,法拉) |
| `A²·s⁴/(kg·m²)` | `(amperes pow 2) * (seconds pow 4) / (kilo.grams * (meters pow 2))` | 电容表示为 电流²·时间⁴ / (质量·长度²)(分数形式) |
| `kg⁻¹·m⁻²·s⁴·A²` | `(kilo.grams pow -1) * (meters pow -2) * (seconds pow 4) * (amperes pow 2)` | 同一电容写成纯乘积 |
| `µF` | `micro.farads` | 带词头的电容(微法) |
