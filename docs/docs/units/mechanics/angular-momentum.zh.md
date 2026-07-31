# 角动量

包: `org.pcsoft.framework.kunit.mechanic.angularmomentum`
基本单位: **千克平方米每秒**
(`KAngularMomentumUnit.BASE == KAngularMomentumUnit.KILOGRAM_METERS_SQUARED_PER_SECOND`)

类型： **构造单位**

角动量 `L` 是[动量](momentum.md)在旋转运动中的对应量，也是旋转系统的守恒量。它是一个 **构造**单位—— 组合
`mass · length² · time⁻¹`(`kg·m²/s`)。

`KAngularMomentumUnitInstance` 包装了一个恰好由三项组成的 `KMixedUnitInstance`，处于规范正规形式： 指数为 `+1` 的
`KMassUnit.BASE`(克)、指数为 `+2` 的 `KDistanceUnit.BASE`(米)和指数为 `-1` 的
`KTimeUnit.BASE`(秒)。弧度 **不**出现在正规形式中——它是一个无量纲比值。

!!! note "作用量是同一个量"
**作用量**(能量 × 时间)恰好共享这一维度，这也是为什么焦耳秒 (`jouleSeconds`，普朗克常数的单位)
是 *这个*组的一个令牌：`1 J·s = 1 kg·m²/s`。

## 命名单位

| 单位           | 符号       |                              令牌 | 1 单位换算为 kg·m²/s |
|----------------|------------|----------------------------------:|---------------------:|
| 千克平方米每秒 | `kg*m^2/s` |  `kilogramMetersSquaredPerSecond` |                  1.0 |
| 牛顿米秒       | `N*m*s`    |              `newtonMeterSeconds` |                  1.0 |
| 焦耳秒         | `J*s`      |                    `jouleSeconds` |                  1.0 |
| 克平方厘米每秒 | `g*cm^2/s` | `gramCentimetersSquaredPerSecond` |                 1e-7 |

所有单位均支持完整的 SI 前缀范围 (`femto.jouleSeconds`、`milli.jouleSeconds`)。

## 分解方式

角动量有两种等价的分解方式；两者都汇入同一个归一化工厂。

| 形式              | Kotlin                                                                          | 结果类型                       |
|-------------------|---------------------------------------------------------------------------------|--------------------------------|
| 转动惯量 × 角速度 | `inertia * angularvelocity`                                                     | `KAngularMomentumUnitInstance` |
| 动量 × 力臂       | `momentum * length`                                                             | `KAngularMomentumUnitInstance` |
| 原生表达式        | `(mass.toUnit() * (length.toUnit() pow 2) / time.toUnit()).toAngularMomentum()` | `KAngularMomentumUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.pow
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.angle.radians
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.div
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.momentum.kilogramMetersPerSecond

val omega = (3 of radians) / (1 of seconds)
val viaInertia = (2 of kilogramMetersSquared) * omega
val viaMomentum = (3 of kilogramMetersPerSecond) * (2 of meters)
val viaNative =
    ((2000 of grams).toUnit() * ((3 of meters).toUnit() pow 2) / (1 of seconds).toUnit()).toAngularMomentum()

viaInertia == viaMomentum                       // true —— 两者都是 6 kg·m²/s
viaInertia into kilogramMetersSquaredPerSecond  // 6.0
viaNative into kilogramMetersSquaredPerSecond   // 18.0
```

## 使用核心单位进行计算

| 表达式                                   | 结果类型                       | 含义         |
|------------------------------------------|--------------------------------|--------------|
| `inertia * angularvelocity`              | `KAngularMomentumUnitInstance` | `L = J · ω`  |
| `angularvelocity * inertia`              | `KAngularMomentumUnitInstance` | 同上，可交换 |
| `momentum * length`、`length * momentum` | `KAngularMomentumUnitInstance` | `L = p · r`  |
| `angularmomentum / inertia`              | `KAngularVelocityUnitInstance` | `ω = L / J`  |
| `angularmomentum / angularvelocity`      | `KInertiaUnitInstance`         | `J = L / ω`  |
| `angularmomentum / length`               | `KMomentumUnitInstance`        | `p = L / r`  |
| `angularmomentum / momentum`             | `KLengthUnitInstance`          | `r = L / p`  |

## 现实示例:花样滑冰运动员收臂

一名滑冰运动员以 2 rev/s 的速度旋转，转动惯量为 4 kg·m²。收臂将其减小到 1.6 kg·m²。由于角动量守恒， 新的转速可由 `ω = L / J`
求得。

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*
import org.pcsoft.framework.kunit.mechanic.angularvelocity.revolutionsPerSecond
import org.pcsoft.framework.kunit.mechanic.inertia.kilogramMetersSquared

val l = (4 of kilogramMetersSquared) * (2 of revolutionsPerSecond)
l into kilogramMetersSquaredPerSecond // ≈ 50.27

val faster = l / (1.6 of kilogramMetersSquared) // KAngularVelocityUnitInstance
faster into revolutionsPerSecond                 // 5.0
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*

val sum = (10 of jouleSeconds) + (4 of jouleSeconds) // 14 J·s
(10 of jouleSeconds) > (4 of newtonMeterSeconds)     // true
(1 of jouleSeconds) == (1 of newtonMeterSeconds)     // true(同一维度)
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.angularmomentum.*

(6 of kilogramMetersSquaredPerSecond).toString()             // "6.0 kg*m^2/s"(基本单位)
"${(6 of kilogramMetersSquaredPerSecond) into jouleSeconds} J*s" // "6.0 J*s"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/`
表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学        | Kotlin                                           | 含义                       |
|-------------|--------------------------------------------------|----------------------------|
| `kg·m²/s`   | `kilogramMetersSquaredPerSecond`                 | 角动量，基本单位(命名令牌) |
| `kg·m²·s⁻¹` | `kilo.grams * (meters pow 2) * (seconds pow -1)` | 同一量写成纯乘积           |
| `J·s`       | `jouleSeconds`                                   | 同一维度的作用量写法       |
| `L = J · ω` | `inertia * angularvelocity`                      | 分解方式 A                 |
| `L = p · r` | `momentum * length`                              | 分解方式 B                 |
| `ω = L / J` | `angularmomentum / inertia`                      | 求解角速度                 |
| `r = L / p` | `angularmomentum / momentum`                     | 求解力臂                   |
