# 电阻

包: `org.pcsoft.framework.kunit.resistance`
基本单位:**欧姆**(`KResistanceUnit.BASE == KResistanceUnit.OHM`)

类型：**构造单位**

电阻是一个**构造**单位,即组合 `mass · length² · time⁻³ · current⁻²`(`kg·m²·s⁻³·A⁻²`)。
`KResistanceUnitInstance` 包装了一个含四项的 `KMixedUnitInstance` —— 指数 `+1` 的 `KMassUnit.BASE`(克)、
指数 `+2` 的 `KDistanceUnit.BASE`(米)、指数 `-3` 的 `KTimeUnit.BASE`(秒)以及指数 `-2` 的
`KElectricCurrentUnit.BASE`(安培)。由于库的质量分量归一化到**克**(而非千克),欧姆是原始分量基准的 1000 倍;
存储的值归一化为欧姆。

## 创建电阻

用命名令牌创建电阻,或通过分解创建(见下文)。命名单位保留为值为 1 的令牌(与 `of`/`into` 一起使用):

| 电阻 | 符号 | 令牌 | 1 单位对应欧姆 |
|---|---|---:|---:|
| 欧姆 | `Ω` | `ohms` | 1.0 |
| 静电欧姆(CGS-ESU) | `statΩ` | `statohms` | 8.98755179e11 |
| 电磁欧姆(CGS-EMU) | `abΩ` | `abohms` | 1.0e-9 |
| 国际欧姆 | `Ω_int` | `internationalOhms` | 1.000049 |
| 法定欧姆(1884) | `Ω_leg` | `legalOhms` | 0.9972 |
| 西门子汞柱单位 | `Ω_S` | `siemensUnits` | 0.9534 |

命名单位通过 `KPrefixBuilder` 支持 SI 前缀(`kilo.ohms`、`mega.ohms`、`milli.ohms` 等)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.resistance.*

val r = 470 of ohms
r into ohms                  // 470.0
r into kilo.ohms             // 0.47
(1 of kilo.ohms) into ohms   // 1000.0
```

## 多种分解

电阻可以通过多种**等价的分解**得到,它们都产生数值相等的电阻:

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `voltage / current` | `KResistanceUnitInstance` | 欧姆定律 `R = U / I` |
| `mass·length²/(time³·current²)` | 通过 `.toResistance()` | 原生规范式 `kg·m²·s⁻³·A⁻²` 表达式 |

带类型的运算符形式直接返回电阻。完全原生的表达式仍是通用的 `KMixedUnitInstance`,通过 `toResistance()`
(仅识别规范范式,否则抛出 `IllegalStateException`)缩小。两条路径数值相等。

逆欧姆定律运算符将电压、电阻与电流联系起来:

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `resistance * current` | `KVoltageUnitInstance` | `U = R · I`(可交换) |
| `voltage / resistance` | `KElectricCurrentUnitInstance` | `I = U / R` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.voltage.volts
import org.pcsoft.framework.kunit.resistance.*

// 实际示例 —— 欧姆定律:负载两端 230 V、流过 2 A,意味着电阻为 115 Ω。
val r = (230 of volts) / (2 of amperes)  // KResistanceUnitInstance,115 Ω

// 用原生的 kg·m²·s⁻³·A⁻² 表达式表示同一电阻:
val raw = 115 of (kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 3))
raw.toResistance() == (115 of ohms)      // true
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.resistance.*

val s = (100 of ohms) + (40 of ohms)  // 140 Ω
(100 of ohms) > (40 of ohms)          // true
(100 of ohms) * (40 of ohms)          // KMixedUnitInstance(脱离该组)
```

## toString 格式

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.resistance.*

(470 of ohms).toString()     // "470.0 Ω"(基本单位)
```

## 记法

下表展示了该单位及其组成部分在数学上如何书写,以及使用 KUnit 在 Kotlin 中如何书写。指数使用 Unicode 上标(`²`、`³`、`⁻¹`),`·` 表示乘法,`/` 表示分数。对于既可写成分数又可写成含负指数乘积的量,同时列出两种等价的 Kotlin 形式。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `Ω` | `ohms` | 电阻,基本单位(命名令牌,欧姆) |
| `kg·m²/(s³·A²)` | `kilo.grams * (meters pow 2) / ((amperes pow 2) * (seconds pow 3))` | 电阻作为 质量·长度² / (时间³·电流²)(分数形式) |
| `kg·m²·s⁻³·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -3) * (amperes pow -2)` | 同一电阻表示为纯乘积 |
| `kΩ` | `kilo.ohms` | 带前缀的电阻(千欧) |
