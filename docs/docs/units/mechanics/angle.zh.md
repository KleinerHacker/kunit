# 角度

包: `org.pcsoft.framework.kunit.mechanic.angle`
基本单位: **弧度**(`KAngleUnit.BASE == KAngleUnit.RADIAN`)

类型： **原生单位**

平面角是 KUnit 的一个 **原生**单位：一个可直接测量的基本量，拥有自己的单位词汇表，而非组合而成。
`KAngleUnitInstance` 包装了一个只有单一 `KAngleUnit.BASE` 项、指数为 1 的 `KMixedUnitInstance`，始终归一化为弧度。

角度是力学中整个旋转部分的基础：
[角速度](angular-velocity.md)、[角加速度](angular-acceleration.md)、
[角动量](angular-momentum.md)以及[立体角](solid-angle.md)都建立在其之上。

## 命名单位

| 单位        | 符号  |         令牌 |  1 单位换算为 rad |
|-------------|-------|-------------:|------------------:|
| 弧度        | `rad` |    `radians` |               1.0 |
| 度          | `°`   |    `degrees` | π/180 ≈ 0.0174533 |
| 角分        | `'`   | `arcminutes` |           π/10800 |
| 角秒        | `"`   | `arcseconds` |          π/648000 |
| 百分度(gon) | `gon` |   `gradians` |             π/200 |
| 圈(转)      | `tr`  |      `turns` |       2π ≈ 6.2832 |

所有单位均支持完整的 SI 前缀范围 (用于天体测量的 `milli.radians`、`micro.arcseconds` 等)。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.milli
import org.pcsoft.framework.kunit.mechanic.angle.*

val a = 90 of degrees
a into radians      // ≈ 1.5708
a into turns        // 0.25
a into gradians     // 100.0
1 of milli.radians  // 0.001 rad
```

## 使用角度进行计算

| 表达式                           | 结果类型                       | 含义                   |
|----------------------------------|--------------------------------|------------------------|
| `angle + angle`、`angle - angle` | `KAngleUnitInstance`           | 同类型运算             |
| `angle * angle`                  | `KSolidAngleUnitInstance`      | 立体角(`rad² = sr`)    |
| `angle / time`                   | `KAngularVelocityUnitInstance` | 角速度 `ω = φ / t`     |
| `angle / angularvelocity`        | `KTimeUnitInstance`            | 完成一次旋转所需的时间 |
| `angle / angle`                  | `KMixedUnitInstance`           | 无量纲比值             |

三角函数可直接在该值上使用，因为它们读取的是弧度值:
`angle.sin()`、`angle.cos()`、`angle.tan()`。

## 现实示例:齿轮箱输出角度

电机轴转动 3 整圈。传动比为 5:1 的齿轮副对其进行了减速。以度为单位的输出角度是多少， 在 600 rpm 下该运动需要多长时间？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val input = 3 of turns
val output = input / 5                 // KAngleUnitInstance, 0.6 turns
output into degrees                    // 216.0

val t = input / (600 of revolutionsPerMinute) // KTimeUnitInstance
t into seconds                                // 0.3
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.*

val sum = (90 of degrees) + (30 of degrees) // 120°
(1 of turns) > (359 of degrees)             // true
(180 of degrees) == (0.5 of turns)          // true(基于值的相等性)
(90 of degrees).sin()                       // 1.0
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angle.*

(2 of radians).toString()                    // "2.0 rad"(基本单位)
"${(1 of turns) into degrees} °"             // "360.0 °"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/`
表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学            | Kotlin                      | 含义                   |
|-----------------|-----------------------------|------------------------|
| `rad`           | `radians`                   | 平面角，基本单位       |
| `°`             | `degrees`                   | 度                     |
| `mrad`          | `milli.radians`             | 带前缀的角度(毫弧度)   |
| `1 tr = 2π rad` | `(1 of turns) into radians` | 一整圈换算为弧度       |
| `ω = φ / t`     | `angle / time`              | 由角度得到角速度       |
| `Ω = φ²`        | `angle * angle`             | 由两个平面角得到立体角 |
