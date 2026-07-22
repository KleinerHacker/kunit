# 运动学 — 概述

包：`org.pcsoft.framework.kunit.distance`、`…time`、`…speed`、`…acceleration`、`…frequency`

运动学是对**运动**的描述 —— 多远、多久、多快,以及运动的速率本身如何变化 —— 暂不追问背后的力
(那是[力学](../mechanics/overview.md)的主题)。KUnit 用 2 个**原生**基本量和由它们**构造**的
3 个量来建模这一领域,使经典的运动公式成为保持强类型的普通 `*` 与 `/` 表达式。

## 本主题的单位

| 单位 | 类型 | 基准单位 | 页面 |
|---|---|---|---|
| 距离 | 原生 | 米(`m`) | [距离](distance.md) |
| 时间 | 原生 | 秒(`s`) | [时间](time.md) |
| 频率 | 原生 | 赫兹(`Hz`) | [频率](frequency.md) |
| 速度 | 构造 | 米每秒(`m/s`) | [速度](speed.md) |
| 加速度 | 构造 | 米每二次方秒(`m/s²`) | [加速度](acceleration.md) |

## 各量之间的关系

速度是距离÷时间,加速度是速度÷时间,频率是时间的倒数。KUnit 为每种组合返回正确的**类型化**量 ——
你无需手工组装原始混合单位。

| 表达式 | 结果 | 公式 |
|---|---|---|
| `distance / time` | 速度 | `v = s / t` |
| `speed * time` | 距离 | `s = v · t` |
| `speed / time` | 加速度 | `a = Δv / t` |
| `acceleration * time` | 速度 | `v = a · t` |
| `distance * frequency` | 速度 | `v = s · f` |

## 实例 —— 行程的平均速度

一辆汽车在 **1.5 h** 内行驶 **120 km**。其平均速度为 `v = s / t`,再将该速度乘以时长即可重新得到行驶
距离:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.*
import org.pcsoft.framework.kunit.time.*
import org.pcsoft.framework.kunit.speed.*

val v = (120 of kilo.meters) / (1.5 of hours)   // KSpeedUnitInstance
v into (kilo.meters / hours)                     // 80.0(km/h)
v.value                                          // ≈ 22.22(m/s)

val distance = v * (3 of hours)                  // KLengthUnitInstance
distance into kilo.meters                        // 240.0(3 h 内的 km)
```

## 实例 —— 短跑运动员的加速度

短跑运动员从静止在 **2 s** 内达到 **10 m/s**。加速度为 `a = Δv / t`:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.speed.*
import org.pcsoft.framework.kunit.acceleration.*

val a = ((10 of meters) / (1 of seconds)) / (2 of seconds) // KAccelerationUnitInstance,5 m/s²
val reached = a * (2 of seconds)                            // KSpeedUnitInstance,10 m/s
reached.value                                               // 10.0
a into standardGravities                                    // ≈ 0.51(相对 g 的比例)
```

## 输出值(`toString`)

`toString()` 以该组的**基准单位**(值 + 符号)输出值;对于其他单位,在字符串模板中用 `into` 读取并自行
附加符号:

```kotlin
import org.pcsoft.framework.kunit.of
import org.pcsoft.framework.kunit.into
import org.pcsoft.framework.kunit.kilo
import org.pcsoft.framework.kunit.distance.meters
import org.pcsoft.framework.kunit.time.seconds
import org.pcsoft.framework.kunit.time.hours
import org.pcsoft.framework.kunit.speed.*

val v = (10 of meters) / (2 of seconds)   // KSpeedUnitInstance
v.toString()                              // "5.0 m/s"(基准单位)
"${v into (kilo.meters / hours)} km/h"    // "18.0 km/h"
```

## 记法

下表以数学表记与 KUnit 的 Kotlin 表记对照本领域的核心关系。指数使用 Unicode 上标(`²`、`⁻¹`),
`·` 表示乘法,`/` 表示分数。

| 数学 | Kotlin | 含义 |
|---|---|---|
| `v = s / t` | `(120 of kilo.meters) / (1.5 of hours)` | 距离÷时间得速度 |
| `s = v · t` | `v * (3 of hours)` | 速度×时间得距离 |
| `a = Δv / t` | `((10 of meters) / (1 of seconds)) / (2 of seconds)` | 速度÷时间得加速度 |
| `v = a · t` | `a * (2 of seconds)` | 加速度×时间得速度 |
| `f = 1 / T` | `1 / (2 of hertz)` | 周期↔频率(时间的倒数) |

## 后续阅读

* [距离](distance.md) —— 长度、面积、体积归于一组。
* [时间](time.md) —— 以 `Duration` 为基础的时长。
* [速度](speed.md) 与 [加速度](acceleration.md) —— 构造出的运动速率。
* [频率](frequency.md) —— 时间的倒数及其跨单位运算符。
