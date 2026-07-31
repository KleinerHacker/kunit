# 冲量

包: `org.pcsoft.framework.kunit.mechanic.momentum`
基本单位: **千克米每秒**(`KMomentumUnit.BASE`)，读作 **牛顿秒**
(`KMomentumUnit.NEWTON_SECOND`)

类型： **构造单位**

冲量 `J = F · t` 是力在其作用时间内传递的动量。从维度上看，它 *就是*
[动量](momentum.md)：`1 N·s = 1 kg·m/s`。因此 KUnit 并 **不**为其引入第二个单位组 ——冲量是动量组的一种 **读法**，用
`newtonSeconds` 令牌表示。本页描述该读法； 该组本身在[动量](momentum.md)页面中描述。

!!! note "同一个组，两种读法"
`(1 of newtonSeconds) == (1 of kilogramMetersPerSecond)` 为 `true`。选择某个令牌只会改变你
读取值的方式，从不改变值本身。当你以"力 × 时间"的方式思考时使用 `newtonSeconds`， 以"质量 × 速度"的方式思考时使用
`kilogramMetersPerSecond`。

## 命名单位

| 单位       | 符号      |                       令牌 | 1 单位换算为 kg·m/s |
|------------|-----------|---------------------------:|--------------------:|
| 牛顿秒     | `N*s`     |            `newtonSeconds` |                 1.0 |
| 千克米每秒 | `kg*m/s`  |  `kilogramMetersPerSecond` |                 1.0 |
| 克厘米每秒 | `g*cm/s`  | `gramCentimetersPerSecond` |                1e-5 |
| 磅英尺每秒 | `lb*ft/s` |       `poundFeetPerSecond` |          ≈ 0.138255 |

每个令牌都存在带前缀的形式 (`kilo.newtonSeconds` = kN·s、`milli.newtonSeconds` = mN·s)。

## 计算冲量

| 表达式            | 结果类型                | 含义                  |
|-------------------|-------------------------|-----------------------|
| `force * time`    | `KMomentumUnitInstance` | `J = F · t`           |
| `time * force`    | `KMomentumUnitInstance` | 同上，可交换          |
| `impulse / time`  | `KForceUnitInstance`    | 平均力 `F = J / t`    |
| `impulse / force` | `KTimeUnitInstance`     | 作用时间 `t = J / F`  |
| `impulse / mass`  | `KSpeedUnitInstance`    | 速度变化 `Δv = J / m` |

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.momentum.*

val j = (10 of newtons) * (3 of seconds)
j into newtonSeconds             // 30.0
j into kilogramMetersPerSecond   // 30.0(相同维度)
```

## 现实示例:火箭级点火

一台模型火箭发动机在 1.6 s 内提供 12 N 的平均推力。它产生的总冲量是多少？ 这对一枚 0.8 kg 的火箭会带来多大的速度变化？

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.kinematic.distance.meters
import org.pcsoft.framework.kunit.kinematic.time.seconds
import org.pcsoft.framework.kunit.mechanic.force.newtons
import org.pcsoft.framework.kunit.mechanic.momentum.*

val impulse = (12 of newtons) * (1.6 of seconds)
impulse into newtonSeconds              // 19.2

val deltaV = impulse / (0.8 of kilo.grams) // KSpeedUnitInstance
deltaV into (meters / seconds)             // 24.0
```

## 运算符

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

val total = (19.2 of newtonSeconds) + (5 of newtonSeconds) // 24.2 N·s
(19.2 of newtonSeconds) > (10 of newtonSeconds)            // true
```

## toString 格式化

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.mechanic.momentum.*

(19.2 of newtonSeconds).toString()                  // "19.2 kg*m/s"(组的基本单位)
"${(19.2 of newtonSeconds) into newtonSeconds} N*s" // "19.2 N*s"
```

## 记法

下表对比该单位及其组成部分的数学写法与使用 KUnit 的 Kotlin 写法。指数使用 Unicode 上标（`²`、`³`、`⁻¹`）表示，`·` 表示乘法，`/`
表示分数。当一个量既可写成分数、也可写成带负指数的乘积时，会同时列出两种等价的 Kotlin 写法。

| 数学         | Kotlin                                   | 含义                   |
|--------------|------------------------------------------|------------------------|
| `N·s`        | `newtonSeconds`                          | 冲量(动量组的命名令牌) |
| `kg·m·s⁻¹`   | `kilo.grams * meters * (seconds pow -1)` | 同一量以基础维度表示   |
| `J = F · t`  | `force * time`                           | 类型化分解方式         |
| `F = J / t`  | `impulse / time`                         | 求解平均力             |
| `Δv = J / m` | `impulse / mass`                         | 质量的速度变化         |
| `kN·s`       | `kilo.newtonSeconds`                     | 带前缀的冲量           |
