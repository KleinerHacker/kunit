# 角速度

包: `org.pcsoft.framework.kunit.mechanic.angularvelocity`
基本单位: **弧度每秒**(`KAngularVelocityUnit.BASE == KAngularVelocityUnit.RADIANS_PER_SECOND`)

类型： **构造单位**

角速度 `ω` 是[速度](../kinematics/speed.md)在旋转运动中的对应量：单位时间内扫过的角度。它是一个 **构造**
单位——组合 `angle · time⁻¹`(`rad/s`)。

`KAngularVelocityUnitInstance` 包装了一个恰好由两项组成的 `KMixedUnitInstance`，处于规范正规形式： 指数为 `+1` 的
`KAngleUnit.BASE`(弧度)和指数为 `-1` 的 `KTimeUnit.BASE`(秒)。该值始终归一化为 rad/s。

## 构建角速度

由 `angle / time` 构建，或使用常规的转速令牌之一。以直接组合方式书写的写法故意 **没有**自己的令牌：
`rad/s` 就是 `radians / seconds`，`°/s` 就是 `degrees / seconds`。前缀应用于各组成部分 (`kilo.radians / seconds`)
，因此该组没有自己的前缀构建器。

| 单位     | 符号    |                   令牌 | 1 单位换算为 rad/s |
|----------|---------|-----------------------:|-------------------:|
| 弧度每秒 | `rad/s` |    `radians / seconds` |                1.0 |
| 度每秒   | `°/s`   |    `degrees / seconds` |              π/180 |
| 转每分钟 | `rpm`   | `revolutionsPerMinute` |    2π/60 ≈ 0.10472 |
| 转每秒   | `rps`   | `revolutionsPerSecond` |        2π ≈ 6.2832 |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val w = (1 of turns) / (1 of seconds)
w into revolutionsPerMinute  // 60.0
w into (radians / seconds)   // ≈ 6.2832
```

## 使用核心单位 (角度与时间)进行计算

| 表达式                      | 结果类型                           | 含义                                |
|-----------------------------|------------------------------------|-------------------------------------|
| `angle / time`              | `KAngularVelocityUnitInstance`     | `ω = φ / t`                         |
| `angularvelocity * time`    | `KAngleUnitInstance`               | 扫过的角度 `φ = ω · t`              |
| `time * angularvelocity`    | `KAngleUnitInstance`               | 同上，可交换                        |
| `angle / angularvelocity`   | `KTimeUnitInstance`                | 所需时间 `t = φ / ω`                |
| `angularvelocity / time`    | `KAngularAccelerationUnitInstance` | [角加速度](angular-acceleration.md) |
| `inertia * angularvelocity` | `KAngularMomentumUnitInstance`     | [角动量](angular-momentum.md)       |
| `torque * angularvelocity`  | `KPowerUnitInstance`               | 旋转功率，参见[转矩](torque.md)     |

原生形式同样可用：任何通过通用引擎构建的 `angle / time` 表达式都可以通过 `toAngularVelocity()` 进行转换。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val typed = (2 of radians) / (4 of seconds)
val native = ((2 of radians).toUnit() / (4 of seconds).toUnit()).toAngularVelocity()

typed == native // true —— 两者都是 0.5 rad/s
```

## 现实示例:主轴转速

铣床主轴以 12000 rpm 运转。刀具圆周上一点每秒沿角度方向走过多远，转动一圈需要多长时间？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val spindle = 12_000 of revolutionsPerMinute
val perSecond = spindle * (1 of seconds)   // KAngleUnitInstance
perSecond into turns                        // 200.0

val perTurn = (1 of turns) / spindle        // KTimeUnitInstance
perTurn into seconds                        // 0.005
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

val sum = (1000 of revolutionsPerMinute) + (500 of revolutionsPerMinute) // 1500 rpm
(1 of revolutionsPerSecond) > (59 of revolutionsPerMinute)               // true
(60 of revolutionsPerMinute) == (1 of revolutionsPerSecond)              // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularvelocity.*

(1 of revolutionsPerSecond).toString()                        // "6.283185307179586 rad/s"
"${(1 of revolutionsPerSecond) into revolutionsPerMinute} rpm" // "60.0 rpm"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/`
表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学        | Kotlin                       | 含义                       |
|-------------|------------------------------|----------------------------|
| `rad/s`     | `radians / seconds`          | 角速度，基本单位(分数形式) |
| `rad·s⁻¹`   | `radians * (seconds pow -1)` | 同一量写成纯乘积           |
| `rpm`       | `revolutionsPerMinute`       | 转每分钟(命名令牌)         |
| `ω = φ / t` | `angle / time`               | 类型化分解方式             |
| `φ = ω · t` | `angularvelocity * time`     | 求解角度                   |
| `t = φ / ω` | `angle / angularvelocity`    | 求解时间                   |
