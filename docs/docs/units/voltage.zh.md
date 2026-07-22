# 电压

包: `org.pcsoft.framework.kunit.voltage`
基本单位:**伏特**(`KVoltageUnit.BASE == KVoltageUnit.VOLT`)

电压(电位差)是一个**构造**单位,即组合 `mass · length² · time⁻³ · current⁻¹`(`kg·m²·s⁻³·A⁻¹`)。
`KVoltageUnitInstance` 包装了一个含四项的 `KMixedUnitInstance` —— 指数 `+1` 的 `KMassUnit.BASE`(克)、
指数 `+2` 的 `KDistanceUnit.BASE`(米)、指数 `-3` 的 `KTimeUnit.BASE`(秒)以及指数 `-1` 的
`KElectricCurrentUnit.BASE`(安培)。由于库的质量分量归一化到**克**(而非千克),伏特是原始分量基准的 1000 倍;
存储的值归一化为伏特。

## 创建电压

用命名令牌创建电压,或通过分解创建(见下文)。命名单位保留为值为 1 的令牌(与 `of`/`into` 一起使用):

| 电压 | 符号 | 令牌 | 1 单位对应伏特 |
|---|---|---:|---:|
| 伏特 | `V` | `volts` | 1.0 |
| 静电伏特(CGS-ESU) | `statV` | `statvolts` | 299.792458 |
| 电磁伏特(CGS-EMU) | `abV` | `abvolts` | 1.0e-8 |
| 韦斯顿电池 | `V_W` | `westonCells` | 1.0183 |
| 丹尼尔电池 | `V_Da` | `daniells` | 1.1 |

命名单位通过 `KPrefixBuilder` 支持 SI 前缀(`kilo.volts`、`mega.volts`、`milli.volts` 等)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.voltage.*

val u = 230 of volts
u into volts                 // 230.0
u into kilo.volts            // 0.23
(1 of kilo.volts) into volts // 1000.0
```

## 多种分解

电压可以通过多种**等价的分解**得到,它们都产生数值相等的电压:

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `resistance * current` | `KVoltageUnitInstance` | 欧姆定律 `U = R · I`(见电阻) |
| `current * resistance` | `KVoltageUnitInstance` | 欧姆定律(可交换) |
| `mass·length²/(time³·current)` | 通过 `.toVoltage()` | 原生规范式 `kg·m²·s⁻³·A⁻¹` 表达式 |

带类型的运算符形式直接返回电压。完全原生的表达式仍是通用的 `KMixedUnitInstance`,通过 `toVoltage()`
(仅识别规范范式,否则抛出 `IllegalStateException`)缩小。两条路径数值相等。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.resistance.ohms
import org.pcsoft.framework.kunit.voltage.*

// 实际示例 —— 欧姆定律:通过 2 A 电流的 115 Ω 电阻产生 230 V 电压降。
val u = (115 of ohms) * (2 of amperes)   // KVoltageUnitInstance,230 V

// 用原生的 kg·m²·s⁻³·A⁻¹ 表达式表示同一电压:
val raw = 230 of (kilo.grams * (meters pow 2)) / (amperes * (seconds pow 3))
raw.toVoltage() == (230 of volts)        // true
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.voltage.*

val s = (100 of volts) + (40 of volts)  // 140 V
(100 of volts) > (40 of volts)          // true
(100 of volts) * (40 of volts)          // KMixedUnitInstance(脱离该组)
```

## toString 格式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.voltage.*

(230 of volts).toString()    // "230.0 V"(基本单位)
```

## 记法

下表展示了该单位及其组成部分在数学上如何书写,以及使用 KUnit 在 Kotlin 中如何书写。指数使用 Unicode 上标(`²`、`³`、`⁻¹`),`·` 表示乘法,`/` 表示分数。对于既可写成分数又可写成含负指数乘积的量,同时列出两种等价的 Kotlin 形式。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `V` | `volts` | 电压,基本单位(命名令牌,伏特) |
| `kg·m²/(s³·A)` | `kilo.grams * (meters pow 2) / (amperes * (seconds pow 3))` | 电压作为 质量·长度² / (时间³·电流)(分数形式) |
| `kg·m²·s⁻³·A⁻¹` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -1)` | 同一电压表示为纯乘积 |
| `kV` | `kilo.volts` | 带前缀的电压(千伏) |
