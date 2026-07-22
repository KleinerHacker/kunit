# 绝对温度

> **温度**主题的一部分 —— 参见[概述](temperature-overview.md)以及线性对应物
> [温度差](temperature-difference.md)。

包: `org.pcsoft.framework.kunit.temperature`
基本单位: **开尔文**(`KTemperatureUnit.BASE == KTemperatureUnit.KELVIN`)

类型：**原生单位**

温度组用于建模热力学温度。它是框架的**第一个(且按设计永久的)仿射例外**:与其他所有组不同,温度单位之间的转换
不是单一的乘法系数,而是**偏移加缩放**(仿射)变换——`25 °C` **并非** `25 × 1 °C`。数值以**绝对开尔文**归一化
存储,因此 `*`/`/`/`pow` 仍照常通过通用引擎运行。

这个组有两点特殊之处:

* **通过钩子而非重载实现仿射转换。** 共享引擎保持纯乘法。仿射变换通过两个 measurable 钩子 `scaledBy`(构造,
  位于 `of` 背后)和 `readBaseValue`(读取,位于 `into` 背后)注入,因此 `25 of celsius` 和 `t into fahrenheit`
  通过普通动词即可工作——无需特定于组的 `of`/`into` 重载(显式导入的通用动词会将其遮蔽)。
* **无前缀。** 温度组有意**不**提供前缀构建器(不建模 `milli.celsius`)。没有 `KTemperatureUnitExtensions.kt`。

## 单位

| 单位 | 枚举值 | 符号 | 令牌 | 与开尔文的换算 |
|---|---|---|---:|---|
| 开尔文 | `KTemperatureUnit.KELVIN` | `K` | `kelvin` | 恒等 |
| 摄氏度 | `KTemperatureUnit.CELSIUS` | `°C` | `celsius` | `K = °C + 273.15` |
| 华氏度 | `KTemperatureUnit.FAHRENHEIT` | `°F` | `fahrenheit` | `K = (°F − 32)·5/9 + 273.15` |
| 兰氏度 | `KTemperatureUnit.RANKINE` | `°R` | `rankine` | `K = °R·5/9` |

每个 `令牌` 都是值为 1 的 `KTemperatureUnitInstance`,与 `of`(构建)和 `into`(读取)配合使用。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.temperature.*

val t = 25 of celsius
t.value             // 298.15(归一化为绝对开尔文)
t into fahrenheit   // 77.0
t into kelvin       // 298.15

(0 of celsius) into kelvin       // 273.15
(100 of celsius) into fahrenheit // 212.0
(32 of fahrenheit) into celsius  // 0.0
(-40 of celsius) into fahrenheit // -40.0(摄氏/华氏的交叉点)
```

## 运算符

绝对温度是仿射**点**,而非向量。因此其运算刻意是非对称的 —— 这是物理上正确的行为(另见
[温度差](temperature-difference.md)):

* `AbsTemp − AbsTemp` → **`KTemperatureDifferenceUnitInstance`**(两者之间的开尔文*区间*,例如
  `30 °C − 10 °C = 20 ΔK`,而**不是** `20 °C`)。
* `AbsTemp ± 差值` → 再次得到绝对温度。
* `AbsTemp + AbsTemp` → **编译错误**(两个绝对温度相加在物理上无意义)。
* `AbsTemp * number` / `AbsTemp / number` → **编译错误**:用纯数字缩放一个仿射点没有意义(其开尔文值带有 −273.15 偏移)。请改为缩放线性的[温差](temperature-difference.md)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*

// 绝对 − 绝对 = 温度差(开尔文)
val d = (30 of celsius) - (10 of celsius)          // KTemperatureDifferenceUnitInstance: 20 ΔK
d.value                                             // 20.0

// 绝对 ± 差值 = 绝对温度
val a = (25 of celsius) + KTemperatureDifference.ofKelvin(5)   // KTemperatureUnitInstance: 303.15 K
val b = (25 of celsius) - KTemperatureDifference.ofKelvin(5)   // KTemperatureUnitInstance: 293.15 K

// (30 of celsius) + (10 of celsius)               // 无法编译

// 比较(按绝对开尔文)
(0 of celsius) == (273.15 of kelvin)      // true(相同的绝对温度)
(100 of celsius) > (100 of fahrenheit)    // true
```

### 比较与相等

`==`、`!=`、`<`、`<=`、`>`、`>=` 比较归一化的绝对开尔文 `value`。`equals` 按绝对温度比较,与构造单位无关,因此
`(0 of celsius) == (273.15 of kelvin)`。

## 使用 `pow` 求幂

使用中缀 `pow` 运算符计算整数次幂。对于温度组,`pow` 返回通用的 `KMixedUnitInstance`(温度没有带量纲的幂类型),
在绝对开尔文项上线性作用:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.temperature.*

val squared = (2 of kelvin) pow 2   // KMixedUnitInstance: 4.0 K²
```

## 与其他单位混合

将温度与另一个组相乘或相除会得到通用的 `KMixedUnitInstance`(不存在标准化的温度组合),计算基于绝对开尔文值:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.temperature.*
import org.pcsoft.framework.kunit.time.seconds

val rate = (2 of kelvin) / (1 of seconds)   // KMixedUnitInstance: 2.0 K·s⁻¹
```

## toString 格式化

只存在基本单位的 `toString()`;通过 `into` 以特定单位格式化:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.temperature.*

(25 of celsius).toString()               // "298.15 K"(基本单位表示)
"${(25 of celsius) into fahrenheit} °F"  // "77.0 °F"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/` 表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。 温度是仿射量，因此没有 `·`/指数的乘积形式，只有命名单位和偏移变换。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `K` | `kelvin` | 绝对温度，基本单位（开尔文） |
| `°C` | `celsius` | 摄氏度（`K = °C + 273.15`） |
| `°F` | `fahrenheit` | 华氏度 |
| `25 °C` | `25 of celsius` | 构造一个绝对温度 |
