# 角加速度

包: `org.pcsoft.framework.kunit.mechanic.angularacceleration`
基本单位: **弧度每二次方秒**
(`KAngularAccelerationUnit.BASE == KAngularAccelerationUnit.RADIANS_PER_SECOND_SQUARED`)

类型： **构造单位**

角加速度 `α` 是[加速度](../kinematics/acceleration.md)在旋转运动中的对应量：单位时间内
[角速度](angular-velocity.md)的变化量。它是一个 **构造**单位——组合 `angle · time⁻²`(`rad/s²`)。

`KAngularAccelerationUnitInstance` 包装了一个恰好由两项组成的 `KMixedUnitInstance`，处于规范正规形式： 指数为 `+1` 的
`KAngleUnit.BASE`(弧度)和指数为 `-2` 的 `KTimeUnit.BASE`(秒)。该值始终归一化为 rad/s²。

## 命名单位

| 单位           | 符号      |                            令牌 | 1 单位换算为 rad/s² |
|----------------|-----------|--------------------------------:|--------------------:|
| 弧度每二次方秒 | `rad/s^2` |       `radiansPerSecondSquared` |                 1.0 |
| 度每二次方秒   | `°/s^2`   |       `degreesPerSecondSquared` |               π/180 |
| 转每二次方秒   | `rps^2`   |   `revolutionsPerSecondSquared` |                  2π |
| 转每分钟每秒   | `rpm/s`   | `revolutionsPerMinutePerSecond` |               2π/60 |

前缀应用于各组成部分 (`kilo.radians / (seconds pow 2)`)，因此该组没有自己的前缀构建器。

## 分解方式

角加速度有两种等价的分解方式；两者都归约为同一规范值。

| 形式         | Kotlin                                                             | 结果类型                           |
|--------------|--------------------------------------------------------------------|------------------------------------|
| 类型化运算符 | `angularvelocity / time`                                           | `KAngularAccelerationUnitInstance` |
| 原生表达式   | `(angle.toUnit() / (time.toUnit() pow 2)).toAngularAcceleration()` | `KAngularAccelerationUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val typed = (6 of radians / seconds) / (3 of seconds)
val native = ((2 of radians).toUnit() / ((1 of seconds).toUnit() pow 2)).toAngularAcceleration()

typed == native                        // true —— 两者都是 2 rad/s²
typed into radiansPerSecondSquared     // 2.0
```

## 使用核心单位进行计算

| 表达式                                  | 结果类型                           | 含义                                    |
|-----------------------------------------|------------------------------------|-----------------------------------------|
| `angularvelocity / time`                | `KAngularAccelerationUnitInstance` | `α = ω / t`                             |
| `angularacceleration * time`            | `KAngularVelocityUnitInstance`     | 获得的角速度 `ω = α · t`                |
| `time * angularacceleration`            | `KAngularVelocityUnitInstance`     | 同上，可交换                            |
| `angularvelocity / angularacceleration` | `KTimeUnitInstance`                | 加速时间 `t = ω / α`                    |
| `inertia * angularacceleration`         | `KEnergyUnitInstance`              | 转矩 `M = J · α`，参见[转矩](torque.md) |

## 现实示例:电机加速

一台伺服电机在 0.4 s 内达到 3000 rpm。它的角加速度是多少？从静止开始加速 0.2 s 后转过了多大角度？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val alpha = (3000 of revolutionsPerMinute) / (0.4 of seconds)
alpha into radiansPerSecondSquared      // ≈ 785.4
alpha into revolutionsPerMinutePerSecond // 7500.0

val afterHalf = alpha * (0.2 of seconds) // KAngularVelocityUnitInstance
afterHalf into revolutionsPerMinute      // 1500.0
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*

val sum = (10 of radiansPerSecondSquared) + (4 of radiansPerSecondSquared) // 14 rad/s²
(1 of revolutionsPerSecondSquared) > (300 of degreesPerSecondSquared)      // true
(60 of revolutionsPerMinutePerSecond) == (1 of revolutionsPerSecondSquared) // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularacceleration.*

(2 of radiansPerSecondSquared).toString()                          // "2.0 rad/s^2"
"${(1 of revolutionsPerSecondSquared) into radiansPerSecondSquared} rad/s^2" // "6.283... rad/s^2"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/`
表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学        | Kotlin                                                                  | 含义                         |
|-------------|-------------------------------------------------------------------------|------------------------------|
| `rad/s²`    | `radiansPerSecondSquared`                                               | 角加速度，基本单位(命名令牌) |
| `rad·s⁻²`   | `radians * (seconds pow -2)`                                            | 同一量写成纯乘积             |
| `rad/s²`    | `(radians.toUnit() / (seconds.toUnit() pow 2)).toAngularAcceleration()` | 原生分解方式                 |
| `α = ω / t` | `angularvelocity / time`                                                | 类型化分解方式               |
| `ω = α · t` | `angularacceleration * time`                                            | 求解角速度                   |
| `rpm/s`     | `revolutionsPerMinutePerSecond`                                         | 机床加速速率                 |
