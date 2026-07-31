# 动量

包: `org.pcsoft.framework.kunit.mechanic.momentum`
基本单位: **千克米每秒**
(`KMomentumUnit.BASE == KMomentumUnit.KILOGRAM_METERS_PER_SECOND`)

类型： **构造单位**

动量 `p = m · v` 是物体"运动量"的度量。它是一个 **构造**单位——组合
`mass · length · time⁻¹`(`kg·m/s`)。

`KMomentumUnitInstance` 包装了一个恰好由三项组成的 `KMixedUnitInstance`，处于规范正规形式： 指数为 `+1` 的
`KMassUnit.BASE`(克)、指数为 `+1` 的 `KDistanceUnit.BASE`(米)和指数为 `-1` 的
`KTimeUnit.BASE`(秒)。由于本库的质量分量归一化为克，存储值为原始的以克为基准的分量值， 以 kg·m/s 读取时除以固定因子。

!!! note "冲量是同一个量"
**冲量** `F · t` 恰好具有这个维度 (`1 N·s = 1 kg·m/s`)，因此它属于 *这个*组而非独立的组 ——参见[冲量](impulse.md)页面。

## 命名单位

| 单位       | 符号      |                       令牌 | 1 单位换算为 kg·m/s |
|------------|-----------|---------------------------:|--------------------:|
| 千克米每秒 | `kg*m/s`  |  `kilogramMetersPerSecond` |                 1.0 |
| 牛顿秒     | `N*s`     |            `newtonSeconds` |                 1.0 |
| 克厘米每秒 | `g*cm/s`  | `gramCentimetersPerSecond` |                1e-5 |
| 磅英尺每秒 | `lb*ft/s` |       `poundFeetPerSecond` |          ≈ 0.138255 |

所有单位均支持完整的 SI 前缀范围 (`kilo.newtonSeconds`、`milli.kilogramMetersPerSecond`)。

## 分解方式

动量有两种等价的分解方式；所有方式都汇入同一个归一化工厂，因此产生相同的类型化、值相等的结果。

| 形式            | Kotlin                                                           | 结果类型                |
|-----------------|------------------------------------------------------------------|-------------------------|
| 质量 × 速度     | `mass * speed`                                                   | `KMomentumUnitInstance` |
| 力 × 时间(冲量) | `force * time`                                                   | `KMomentumUnitInstance` |
| 原生表达式      | `(mass.toUnit() * length.toUnit() / time.toUnit()).toMomentum()` | `KMomentumUnitInstance` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.mass.grams
import org.pcsoft.framework.kunit.mechanic.momentum.*

val speed = (3 of meters) / (1 of seconds)
val viaMassSpeed = (2 of kilo.grams) * speed
val viaForceTime = (6 of newtons) * (1 of seconds)
val viaNative =
    ((2000 of grams).toUnit() * (3 of meters).toUnit() / (1 of seconds).toUnit()).toMomentum()

viaMassSpeed == viaForceTime            // true
viaMassSpeed == viaNative               // true
viaMassSpeed into kilogramMetersPerSecond // 6.0
```

## 使用核心单位进行计算

| 表达式                         | 结果类型                       | 含义                          |
|--------------------------------|--------------------------------|-------------------------------|
| `mass * speed`、`speed * mass` | `KMomentumUnitInstance`        | `p = m · v`                   |
| `force * time`、`time * force` | `KMomentumUnitInstance`        | 冲量 `p = F · t`              |
| `momentum / mass`              | `KSpeedUnitInstance`           | `v = p / m`                   |
| `momentum / speed`             | `KMassUnitInstance`            | `m = p / v`                   |
| `momentum / time`              | `KForceUnitInstance`           | 平均力 `F = p / t`            |
| `momentum / force`             | `KTimeUnitInstance`            | 作用时间 `t = p / F`          |
| `momentum * length`            | `KAngularMomentumUnitInstance` | [角动量](angular-momentum.md) |

## 现实示例:汽车刹车

一辆 1200 kg 的汽车以 20 m/s 行驶。它的动量是多少？在 5 s 内将其停下需要多大的恒定力？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.speed.div
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.momentum.*

val v = (20 of meters) / (1 of seconds)
val p = (1200 of kilo.grams) * v
p into kilogramMetersPerSecond      // 24000.0

val brakingForce = p / (5 of seconds)
brakingForce into newtons           // 4800.0
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

val sum = (10 of newtonSeconds) + (4 of newtonSeconds) // 14 N·s
(10 of kilogramMetersPerSecond) > (4 of newtonSeconds) // true
(1 of newtonSeconds) == (1 of kilogramMetersPerSecond) // true(同一维度)
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

(6 of kilogramMetersPerSecond).toString()          // "6.0 kg*m/s"(基本单位)
"${(6 of kilogramMetersPerSecond) into newtonSeconds} N*s" // "6.0 N*s"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/`
表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学        | Kotlin                                   | 含义                     |
|-------------|------------------------------------------|--------------------------|
| `kg·m/s`    | `kilogramMetersPerSecond`                | 动量，基本单位(命名令牌) |
| `kg·m·s⁻¹`  | `kilo.grams * meters * (seconds pow -1)` | 同一量写成纯乘积         |
| `N·s`       | `newtonSeconds`                          | 同一维度的冲量写法       |
| `p = m · v` | `mass * speed`                           | 分解方式 A               |
| `p = F · t` | `force * time`                           | 分解方式 B(冲量)         |
| `v = p / m` | `momentum / mass`                        | 求解速度                 |
| `F = p / t` | `momentum / time`                        | 求解平均力               |
