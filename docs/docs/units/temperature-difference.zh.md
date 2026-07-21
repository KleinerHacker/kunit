# 温度差

包: `org.pcsoft.framework.kunit.temperature`
基本单位: **开尔文**(`KTemperatureDifferenceUnit.BASE == KTemperatureDifferenceUnit.KELVIN`)

温度*差*是两个温度之间的区间 —— 一个**线性**量,与仿射的、绝对的[温度](temperature.md)组相反。它**不带偏移**
(仅有开尔文的比例),因此像普通单位组一样运作,并原样通过通用引擎。

从物理上讲,这就是两个绝对温度相减得到开尔文而非温度的原因: `30 °C − 10 °C = 20 ΔK`,而不是 `20 °C`。`20 ΔK`
的差在数值上与 `20 °C` 的差相等(步长相同),因此该组刻意只提供**开尔文**并且**没有前缀**。

## 单位

| 单位 | Enum 值 | 符号 | 开尔文转换 |
|---|---|---|---|
| 开尔文 | `KTemperatureDifferenceUnit.KELVIN` | `ΔK` | 恒等 |

!!! note "符号是 `ΔK`,而非 `K`"
    温度差以符号 **`ΔK`**(例如 `"20.0 ΔK"`)显示,刻意与绝对开尔文(`K`)区分开来。两者是相同的*量纲*
    (开尔文),但却是不同的量 —— 仿射点与线性区间之别。因此在[混合单位](../mixed-units.md)中,`m·K`(绝对)
    与 `m·ΔK`(差)**不是**同一个单位,既不相等也不可相加。区分的符号让这一点一目了然。

## 构建

差值不使用通用 `of` 动词(该动词专用于绝对量)构建。它通过**两个绝对温度相减**,或通过
`KTemperatureDifference.ofKelvin(…)` 工厂**显式**生成 —— 使"这是一个区间"的意图明确:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

val d1 = (30 of celsius) - (10 of celsius)   // KTemperatureDifferenceUnitInstance: 20 ΔK
val d2 = KTemperatureDifference.ofKelvin(20) // 显式,等于 d1
d1.value                                      // 20.0(开尔文)
```

## 运算符

`+`/`-`/比较是普通的线性同类型运算符(差值加差值仍是差值):

```kotlin
import org.pcsoft.framework.kunit.temperature.*

val sum  = KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10) // 30 ΔK
val diff = KTemperatureDifference.ofKelvin(20) - KTemperatureDifference.ofKelvin(10) // 10 ΔK

KTemperatureDifference.ofKelvin(20) > KTemperatureDifference.ofKelvin(10) // true
```

由于温差是线性的,它还可以(不同于绝对温度)用纯数字**缩放**,并保持其类型:

```kotlin
import org.pcsoft.framework.kunit.times

val doubled = KTemperatureDifference.ofKelvin(5) * 2 // KTemperatureDifferenceUnitInstance: 10 ΔK
```

差值可以加到绝对温度上或从中减去,再次得到绝对温度(参见[温度](temperature.md)):

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

(25 of celsius) + KTemperatureDifference.ofKelvin(5) // KTemperatureUnitInstance: 303.15 K
```

## 与其他单位混合

将差值与另一个组相乘或相除会得到通用 `KMixedUnitInstance`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.storage.bytes
import org.pcsoft.framework.kunit.times
import org.pcsoft.framework.kunit.temperature.*

KTemperatureDifference.ofKelvin(2) * (3 of bytes) // KMixedUnitInstance
```

## toString 格式

只有基本单位的 `toString()`(开尔文):

```kotlin
import org.pcsoft.framework.kunit.temperature.*

KTemperatureDifference.ofKelvin(20).toString() // "20.0 ΔK"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/` 表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。 温差只带开尔文标度（无偏移），且需显式构造，而非通用的 `of`。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `ΔK` | `KTemperatureDifference.ofKelvin(20)` | 温度间隔，基本单位（开尔文） |
| `30 °C − 10 °C` | `(30 of celsius) - (10 of celsius)` | 两个绝对温度之差 |
| `20 ΔK + 10 ΔK` | `KTemperatureDifference.ofKelvin(20) + KTemperatureDifference.ofKelvin(10)` | 两个温差之和 |
