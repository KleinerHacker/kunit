# 电流密度

包：`org.pcsoft.framework.kunit.electric.currentdensity`
基本单位： **安培每平方米**（`KCurrentDensityUnit.BASE == KCurrentDensityUnit.AMPERE_PER_SQUARE_METER`）

类型： **构造单位**

电流密度是一个 **构造**单位：其组成为 `电流 · 长度⁻²`（`A/m²`）—— 即导体横截面上的电流。
`KCurrentDensityUnitInstance` 包装了一个由两项组成的 `KMixedUnitInstance` —— `KElectricCurrentUnit.BASE`
（安培）指数为 `+1`，`KDistanceUnit.BASE`（米）指数为 `-2`。两个分量均以各自组的基本单位存储， 因此该值即为以 A/m² 为单位的读数。

## 构建电流密度

电流密度 **没有命名令牌**，也没有自身的前缀构建器：每一种写法都是一个比值 （`A/m²`、`A/mm²` 等）。可以将其构建为表达式，或使用带类型的
`current / area` 操作符， 并通过 `into` 针对此类表达式读取结果：

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

val crossSection = (2.5 of milli.meters) * (1 of milli.meters)  // 2.5 mm²
val j = (16 of amperes) / crossSection                          // KCurrentDensityUnitInstance

j into (amperes / (meters pow 2))       // 6.4e6
j into (amperes / (milli.meters pow 2)) // 6.4
```

## 多种分解方式

| 表达式            | 结果类型                      | 含义                          |
|-------------------|-------------------------------|-------------------------------|
| `current / area`  | `KCurrentDensityUnitInstance` | 定义式 `J = I / A`            |
| `current/length²` | 通过 `.toCurrentDensity()`    | 原生规范形式的 `A·m⁻²` 表达式 |

带类型的操作符形式直接返回电流密度。完全原生的表达式仍是一个通用的
`KMixedUnitInstance`，需通过 `toCurrentDensity()` 收窄为具体类型（该方法仅识别规范正规 形式，否则会抛出
`IllegalStateException`）。两条路径在值上都相等。

反向操作符将电流、面积与电流密度联系在一起：

| 表达式                     | 结果类型                       | 含义                  |
|----------------------------|--------------------------------|-----------------------|
| `currentDensity * area`    | `KElectricCurrentUnitInstance` | `I = J · A`（可交换） |
| `current / currentDensity` | `KAreaUnitInstance`            | `A = I / J`           |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

// 现实示例 - 导线规格：16 A 的电流流过 2.5 mm² 的铜导线，电流密度为 6.4 A/mm²。
val j = (16 of amperes) / ((2.5 of milli.meters) * (1 of milli.meters))
j into (amperes / (milli.meters pow 2))     // 6.4

// 求解给定密度下某截面积可承载的电流：
val i = j * ((4 of milli.meters) * (1 of milli.meters))  // KElectricCurrentUnitInstance，25.6 A

// 以原生的 A·m⁻² 表达式表示的相同密度：
val raw = (16 of amperes).toUnit() / (2.5e-6 of (meters pow 2))
raw.toCurrentDensity() == j                 // true
```

## 操作符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

val a = (3 of amperes) / ((1 of meters) * (1 of meters))
val b = (1 of amperes) / ((1 of meters) * (1 of meters))
(a + b) into (amperes / (meters pow 2))  // 4.0
a > b                                     // true
a * b                                     // KMixedUnitInstance（脱离该组）
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.electric.current.amperes
import org.pcsoft.framework.kunit.electric.currentdensity.*

((5 of amperes) / ((1 of meters) * (1 of meters))).toString()  // "5.0 A/m²"（基本单位）
```

## 符号表示

下表展示了该单位及其组成部分在数学表示与 Kotlin（配合 KUnit）表示之间的对应关系。指数使用 Unicode 上标（`²`、`⁻²`），`·` 表示乘法，
`/` 表示分数。若某个量既可写成分数形式，也可写成带负指数的乘积形式，则两种等价的 Kotlin 形式都会列出。

| 数学表示 | Kotlin                           | 含义                           |
|----------|----------------------------------|--------------------------------|
| `A/m²`   | `amperes / (meters pow 2)`       | 电流密度，基本单位（分数形式） |
| `A·m⁻²`  | `amperes * (meters pow -2)`      | 相同电流密度作为纯乘积形式     |
| `I / A`  | `(16 of amperes) / crossSection` | 由电流和面积得出的电流密度     |
| `A/mm²`  | `amperes / (milli.meters pow 2)` | 常见布线单位下的电流密度       |
