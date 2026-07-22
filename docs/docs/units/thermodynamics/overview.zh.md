# 热力学 — 概述

包：`org.pcsoft.framework.kunit.temperature`

热力学是**热与温度**的物理学。在 KUnit 中,该领域目前以温度为核心,温度由**两个相关的原生组**建模 ——
因为温度的*读数*与温度的*变化*在物理上是不同类型的量,而将二者区分开正是使运算正确的关键。

## 本主题的单位

| 单位 | 类型 | 性质 | 基准单位 | 页面 |
|---|---|---|---|---|
| 绝对温度 | 原生 | 仿射**点** | 开尔文(`K`) | [绝对温度](temperature.md) |
| 温度差 | 原生 | 线性**区间** | 开尔文(`ΔK`) | [温度差](temperature-difference.md) |

专门的[温度概述](temperature-overview.md)深入解释了点与区间的区别;本页是整个热力学领域的入口。

## 点 vs 区间 —— 运算符规则

| 运算 | 结果 |
|---|---|
| `绝对温度 − 绝对温度` | **温度差** |
| `绝对温度 + 差` | 绝对温度 |
| `绝对温度 − 差` | 绝对温度 |
| `差 ± 差` | 温度差 |
| `绝对温度 + 绝对温度` | **编译错误**(物理上无意义) |

## 实例 —— 一个加热步骤

将水从 **10 °C** 加热到 **30 °C**。其*变化*是温度**差**(`ΔT`),这正是进入诸如 `Q = m · c · ΔT` 这类
热量公式的量;零点相互抵消,因此 `°C` 与 `K` 在步长上一致:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

val start = 10 of celsius
val end   = 30 of celsius

val deltaT = end - start                     // KTemperatureDifferenceUnitInstance: 20 ΔK
deltaT.value                                 // 20.0(开尔文区间)

val back = start + KTemperatureDifference.ofKelvin(20) // KTemperatureUnitInstance: 303.15 K
```

## 输出值(`toString`)

`toString()` 以该组的**基准单位**(开尔文)输出值:绝对温度打印为 `K`,差值打印为独特的 `ΔK` 符号:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

(25 of celsius).toString()                       // "298.15 K"(绝对,基准单位)
KTemperatureDifference.ofKelvin(20).toString()   // "20.0 ΔK"(区间)
```

## 记法

下表以数学表记与 KUnit 的 Kotlin 表记对照温度关系。`Δ` 标记区间量,刻意与绝对点区分开。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `ΔT = T₂ − T₁` | `(30 of celsius) - (10 of celsius)` | 由两个绝对温度得到差 |
| `T + ΔT` | `(10 of celsius) + KTemperatureDifference.ofKelvin(20)` | 以区间平移的绝对温度 |
| `ΔK` | `KTemperatureDifference.ofKelvin(20)` | 显式的温度区间 |
| `20 ΔK + 10 ΔK` | `KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10)` | 两个区间之和 |

## 后续阅读

* [温度概述](temperature-overview.md) —— 点与区间的完整讨论,以及它在物理上为何重要
  (热能、辐射、理想气体定律)。
* [绝对温度](temperature.md) —— 开尔文、摄氏、华氏、兰氏与仿射运算符。
* [温度差](temperature-difference.md) —— 线性开尔文区间组。
