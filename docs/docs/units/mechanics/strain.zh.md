# 应变

包: `org.pcsoft.framework.kunit.mechanic.strain`
基本单位: **纯比值**(`KStrainUnit.BASE == KStrainUnit.RATIO`)

类型： **构造单位**

应变 `ε = ΔL / L` 是物体的相对形变。它是 **无量纲的**——一个长度除以另一个长度——但其读法 (百分比、千分比、微应变)
构成了一套真正的单位词汇，因此 KUnit 将其建模为独立的组。

`KStrainUnitInstance` 包装了一个 `KMixedUnitInstance`，其中只有一个指数为 1 的 `KStrainUnit.BASE` 项， 始终归一化为纯比值。

!!! note "为什么是 `toStrain()` 而不是运算符"
通用引擎将 `length / length` 表示为 **不带**任何单位项的混合单位。由于 `KLengthUnitInstance.div`
是成员运算符，无法被重写，因此原生分解是通过形式识别钩子 `toStrain()` 而非类型化运算符实现的。

## 命名单位

| 单位        | 符号 |          令牌 | 1 单位对应的比值 |
|-------------|------|--------------:|-----------------:|
| 纯比值(m/m) | `1`  |       `ratio` |              1.0 |
| 百分比      | `%`  |     `percent` |             0.01 |
| 千分比      | `‰`  |    `perMille` |             1e-3 |
| 微应变      | `µe` | `microstrain` |             1e-6 |

所有单位都支持完整的 SI 前缀范围，因此 `micro.ratio` 是微应变的另一种写法。

## 构建应变

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.strain.*

// 一根 1 m 的杆伸长了 2 mm
val e = ((2 of milli.meters) / (1 of meters)).toStrain()
e into perMille     // 2.0
e into percent      // 0.2
e into microstrain  // 2000.0
e into ratio        // 0.002
```

## 使用应变计算

| 表达式                                   | 结果类型                | 含义                   |
|------------------------------------------|-------------------------|------------------------|
| `(length / length).toStrain()`           | `KStrainUnitInstance`   | `ε = ΔL / L`(原生形式) |
| `stress / strain`                        | `KPressureUnitInstance` | 弹性模量 `E = σ / ε`   |
| `pressure * strain`、`strain * pressure` | `KPressureUnitInstance` | 应力 `σ = E · ε`       |
| `strain + strain`、`strain - strain`     | `KStrainUnitInstance`   | 同类型运算             |

关于胡克定律中弹性模量一侧的内容，请参阅[应力](stress.md)页面。

## 现实示例：钢杆上的应变片

一个钢杆 (E = 210 GPa)上的应变片读数为 950 µe。这对应多大的机械应力？一根 2 m 的杆会伸长多少？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.giga
import org.pcsoft.framework.kunit.mega
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.mechanic.pressure.pascals
import org.pcsoft.framework.kunit.mechanic.strain.*
import org.pcsoft.framework.kunit.times

val e = 950 of microstrain
val stress = (210 of giga.pascals) * e
stress into mega.pascals               // ≈ 199.5

val elongation = (2 of meters) * (e into ratio) // 长度的标量缩放
elongation into milli.meters                    // 1.9
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.strain.*

val sum = (3 of perMille) + (1 of perMille) // 4 ‰
(1 of percent) > (5 of perMille)            // true
(1 of percent) == (10 of perMille)          // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.strain.*

(2 of perMille).toString()                 // "0.002 1"(基本单位：纯比值)
"${(2 of perMille) into percent} %"        // "0.2 %"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/`
表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学         | Kotlin                         | 含义                   |
|--------------|--------------------------------|------------------------|
| `1`(m/m)     | `ratio`                        | 应变，基本单位(无量纲) |
| `%`          | `percent`                      | 百分比读法             |
| `‰`          | `perMille`                     | 千分比读法             |
| `µe`         | `microstrain`                  | 应变片读法(1 µm/m)     |
| `ε = ΔL / L` | `(length / length).toStrain()` | 原生分解               |
| `σ = E · ε`  | `pressure * strain`            | 胡克定律               |
| `E = σ / ε`  | `stress / strain`              | 弹性模量               |
