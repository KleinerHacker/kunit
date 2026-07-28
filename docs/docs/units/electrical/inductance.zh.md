# 电感

包: `org.pcsoft.framework.kunit.inductance`
基本单位:**亨利**(`KInductanceUnit.BASE == KInductanceUnit.HENRY`)

类型：**构造单位**

电感是一个**构造**单位,即组合 `mass · length² · time⁻² · current⁻²`(`kg·m²·s⁻²·A⁻²`)。
`KInductanceUnitInstance` 包装了一个含四项的 `KMixedUnitInstance` —— 指数 `+1` 的 `KMassUnit.BASE`(克)、
指数 `+2` 的 `KDistanceUnit.BASE`(米)、指数 `-2` 的 `KTimeUnit.BASE`(秒)以及指数 `-2` 的
`KElectricCurrentUnit.BASE`(安培)。由于库的质量分量归一化到**克**(而非千克),亨利是原始分量基准的 1000 倍;
存储的值归一化为亨利。

## 创建电感

用命名令牌创建电感,或通过分解创建(见下文)。命名单位保留为值为 1 的令牌(与 `of`/`into` 一起使用):

| 电感 | 符号 | 令牌 | 1 单位对应亨利 |
|---|---|---:|---:|
| 亨利 | `H` | `henries` | 1.0 |
| 韦伯每安培 | `Wb/A` | `webersPerAmpere` | 1.0 |
| 电磁亨利(CGS-EMU) | `abH` | `abhenries` | 1.0e-9 |
| 静电亨利(CGS-ESU) | `statH` | `stathenries` | 8.987551787e11 |

命名单位通过 `KPrefixBuilder` 支持 SI 前缀(`milli.henries`、`micro.henries`、`nano.henries` 等)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.inductance.*

val l = 470 of micro.henries
l into henries               // 0.00047
l into milli.henries         // 0.47
(1 of henries) into milli.henries  // 1000.0
```

## 多种分解

电感可以通过多种**等价的分解**得到,它们都产生数值相等的电感:

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `flux / current` | `KInductanceUnitInstance` | 定义 `L = Φ / I` |
| `resistance / frequency` | `KInductanceUnitInstance` | 电抗形式 `L = X / ω`(`Ω/Hz = Ω·s = H`) |
| `mass·length²/(time²·current²)` | 通过 `.toInductance()` | 原生规范式 `kg·m²·s⁻²·A⁻²` 表达式 |

带类型的运算符形式直接返回电感。完全原生的表达式仍是通用的 `KMixedUnitInstance`,通过 `toInductance()`
(仅识别规范范式,否则抛出 `IllegalStateException`)缩小。所有路径数值相等。

逆运算符将磁通量、电流、频率与电阻联系起来:

| 表达式 | 结果类型 | 含义 |
|---|---|---|
| `inductance * current` | `KMagneticFluxUnitInstance` | `Φ = L · I`(可交换) |
| `flux / inductance` | `KElectricCurrentUnitInstance` | `I = Φ / L` |
| `inductance * frequency` | `KResistanceUnitInstance` | `X = ω · L` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.micro
import org.pcsoft.framework.kunit.mass.grams
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.ec.amperes
import org.pcsoft.framework.kunit.frequency.hertz
import org.pcsoft.framework.kunit.magneticflux.webers
import org.pcsoft.framework.kunit.resistance.ohms
import org.pcsoft.framework.kunit.inductance.*

// 实际例子 - 开关电源中的扼流圈:470 µH 的线圈通过 2 A 电流时磁链为 0.00094 Wb,
// 在角频率 100 kHz 下呈现 47 Ω 的电抗。
val l = 470 of micro.henries
val flux = l * (2 of amperes)          // KMagneticFluxUnitInstance,0.00094 Wb
val x = l * (100_000 of hertz)         // KResistanceUnitInstance,47 Ω

// 同一个电感由定义式与电抗形式得到:
(flux / (2 of amperes)) == l           // true
((47 of ohms) / (100_000 of hertz)) == l  // true

// 同一个电感的原生 kg·m²·s⁻²·A⁻² 表达式:
val raw = 2 of (kilo.grams * (meters pow 2)) / ((amperes pow 2) * (seconds pow 2))
raw.toInductance() == (2 of henries)   // true
```

## 磁导

磁路的**磁导** `Λ` 是其[磁阻](reluctance.md)的倒数,`Λ = 1 / Rm`。它与电感在**量纲上相同**,
同样以亨利为单位,因此 KUnit 用该组和符号 `H` 来表示它;没有单独的令牌,也没有单独的类型。
倒数运算符将两个组联系在一起:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.inductance.*
import org.pcsoft.framework.kunit.reluctance.*

// 一个 Rm = 500 A/Wb 的磁路,其磁导为 2 mH。
val permeance = 1 / (500 of amperesPerWeber)   // KInductanceUnitInstance
permeance into milli.henries                    // 2.0

// ……反过来也一样:
1 / (2 of milli.henries) == (500 of amperesPerWeber)  // true
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.inductance.*

val s = (100 of henries) + (40 of henries)  // 140 H
(100 of henries) > (40 of henries)          // true
(100 of henries) * (40 of henries)          // KMixedUnitInstance(脱离该组)
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.inductance.*

(2 of henries).toString()     // "2.0 H"(基本单位)
```

## 记法

下表展示了该单位及其分量在数学中的写法与在 Kotlin(KUnit)中的写法。指数使用 Unicode 上标(`²`、`³`、`⁻¹`),`·` 表示乘法,`/` 表示分数。若某个量既可写成分数也可写成带负指数的乘积,则同时列出两种等价的 Kotlin 形式。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `H` | `henries` | 电感,基本单位(命名令牌,亨利) |
| `Wb/A` | `webersPerAmpere` | 作为韦伯每安培的电感(命名令牌) |
| `kg·m²/(s²·A²)` | `kilo.grams * (meters pow 2) / ((amperes pow 2) * (seconds pow 2))` | 作为质量·长度² / (时间²·电流²) 的电感(分数形式) |
| `kg·m²·s⁻²·A⁻²` | `kilo.grams * (meters pow 2) * (seconds pow -2) * (amperes pow -2)` | 同一电感的纯乘积形式 |
| `mH` | `milli.henries` | 带前缀的电感(毫亨) |
