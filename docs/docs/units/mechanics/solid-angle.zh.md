# 立体角

包: `org.pcsoft.framework.kunit.mechanic.solidangle`
基本单位: **球面度**(`KSolidAngleUnit.BASE == KSolidAngleUnit.STERADIAN`)

类型： **构造单位**

立体角是二维角度：圆锥截取球面所占的比例。它是一个 **构造**单位——`1 sr = 1 rad²`—— 但由于球面度是一个拥有自己词汇表
(平方度、球面)的独立命名 SI 单位，因此将其建模为拥有单项包装器的独立组。

`KSolidAngleUnitInstance` 包装了一个只有单一 `KSolidAngleUnit.BASE` 项、指数为 1 的 `KMixedUnitInstance`，
始终归一化为球面度。与[角度](angle.md)组的桥接由类型化运算符 `angle * angle` 与形式识别钩子 `toSolidAngle()`
建立，后者也接受原生的 `rad²` 形式。

## 命名单位

| 单位           | 符号   |            令牌 |       1 单位换算为 sr |
|----------------|--------|----------------:|----------------------:|
| 球面度         | `sr`   |    `steradians` |                   1.0 |
| 平方度         | `deg²` | `squareDegrees` | (π/180)² ≈ 3.04617e-4 |
| 球面(整个球体) | `sp`   |         `spats` |          4π ≈ 12.5664 |

所有单位均支持完整的 SI 前缀范围 (`milli.steradians`、`micro.steradians`)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val full = 1 of spats
full into steradians    // ≈ 12.566
full into squareDegrees // ≈ 41252.96(整个天球)
```

## 分解方式

立体角可以通过两种等价的方式得到，两者都归约为同一规范值。

| 形式         | Kotlin                                  | 结果类型                  |
|--------------|-----------------------------------------|---------------------------|
| 类型化运算符 | `angle * angle`                         | `KSolidAngleUnitInstance` |
| 原生表达式   | `(angle.toUnit() pow 2).toSolidAngle()` | `KSolidAngleUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val typed = (90 of degrees) * (90 of degrees)
val native = ((90 of degrees).toUnit() pow 2).toSolidAngle()

typed == native            // true —— 两者都是 (π/2)² sr ≈ 2.4674 sr
typed into steradians      // ≈ 2.4674
```

## 使用平面角进行计算

| 表达式                    | 结果类型                  | 含义            |
|---------------------------|---------------------------|-----------------|
| `angle * angle`           | `KSolidAngleUnitInstance` | 立体角 `Ω = φ²` |
| `solidangle / angle`      | `KAngleUnitInstance`      | 剩余的平面角    |
| `solidangle + solidangle` | `KSolidAngleUnitInstance` | 同类型运算      |

## 现实示例:LED 光束角

一个 LED 以 30° × 30° 的方形光束发光。它照亮的立体角是多少，占整个球面的比例又是多少？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val beam = (30 of degrees) * (30 of degrees)
beam into steradians    // ≈ 0.2742
beam into squareDegrees // 900.0
beam into spats         // ≈ 0.0218(约占球面的 2.2%)
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.*

val sum = (3 of steradians) + (1 of steradians) // 4 sr
(1 of spats) > (10 of steradians)               // true
(3 of steradians) * (2 of steradians)           // KMixedUnitInstance(逃逸出组)
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.solidangle.*

(2 of steradians).toString()               // "2.0 sr"(基本单位)
"${(1 of spats) into squareDegrees} deg²"  // "41252.96... deg²"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/`
表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学          | Kotlin                                    | 含义                             |
|---------------|-------------------------------------------|----------------------------------|
| `sr`          | `steradians`                              | 立体角，基本单位                 |
| `deg²`        | `squareDegrees`                           | 平方度                           |
| `rad²`        | `(radians.toUnit() pow 2).toSolidAngle()` | 立体角作为平面角的平方(原生形式) |
| `Ω = φ₁ · φ₂` | `angle * angle`                           | 类型化分解方式                   |
| `φ = Ω / φ₁`  | `solidangle / angle`                      | 求解平面角                       |
| `msr`         | `milli.steradians`                        | 带前缀的立体角                   |
